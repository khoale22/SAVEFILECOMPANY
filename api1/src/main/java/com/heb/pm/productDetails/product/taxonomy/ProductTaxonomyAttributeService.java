package com.heb.pm.productDetails.product.taxonomy;

import com.heb.pm.customHierarchy.CustomerHierarchyService;
import com.heb.pm.customHierarchy.GenericEntityRelationshipService;
import com.heb.pm.ecommerce.AttributeMaintenanceService;
import com.heb.pm.entity.*;
import com.heb.pm.product.DynamicAttributeService;
import com.heb.pm.repository.ProductMasterRepository;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds all business logic related to product taxonomy attributes.
 *
 * @author m314029
 * @since 2.18.4
 */
@Service
public class ProductTaxonomyAttributeService {

	private static final String DESCRIPTION_NOT_FOUND_ERROR = "Description not found for entity id: %d in product" +
			" taxonomy.";
	private static final String PRODUCT_NOT_FOUND_ERROR = "Product not found for product id: %d.";
	private static final String PRODUCT_ATTRIBUTE_CHANGES = "Found changes for Product Attribute with attributeId: %d " +
			"and value(s): %s.";

	@Value("${app.mat.HierarchyContext}")
	private String matHierarchyContext;

	@Autowired
	private DynamicAttributeService dynamicAttributeService;

	@Autowired
	private CustomerHierarchyService customerHierarchyService;

	@Autowired
	private GenericEntityRelationshipService genericEntityRelationshipService;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	@Autowired
	private AttributeMaintenanceService attributeMaintenanceService;

	@Autowired
	private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	private static final String GLOBAL_ATTRIBUTE_DESCRIPTION = "Global";

	private static final Logger logger = LoggerFactory.getLogger(ProductTaxonomyAttributeService.class);

	/**
	 * Gets product attributes by product id and product type.
	 *
	 * @param productId Product id to search for.
	 * @param productType Product type to search for.
	 * @return List of product attributes.
	 */
	public List<ProductAttribute> getByProductIdAndProductType(Long productId, String productType) {
		ProductMaster productMaster = this.getProductMasterByProdId(productId);

		// if product not found, throw exception
		if(productMaster == null){
			throw new IllegalArgumentException(
					String.format(PRODUCT_NOT_FOUND_ERROR, productId));
		}

		// get relationships for a product (these relationships will be the same regardless of whether you want
		// product, upc, or case pack attributes
		List<GenericEntityRelationship> parentRelationships =
				this.genericEntityRelationshipService
						.findParentsByChildDisplayNumberAndHierarchyContextAndEntityType(
								productId, matHierarchyContext, GenericEntity.EntyType.PROD);

		// get all attributes tied to the product via the hierarchy
		Set<Attribute> attributes = this.getAllAttributesForProduct(parentRelationships);

		// filter attributes to only contain attributes by product type
		attributes = this.filterAttributesByProductTypes(
				attributes,
				productType);
		List<ProductAttribute> toReturn = this.getProductAttributesByProductIdAndProductType(
				productMaster,
				productType,
				parentRelationships,
				attributes);
		toReturn.sort(ProductAttribute::compareTo);
		return toReturn;
	}

	/**
	 * Gets all attributes tied to a product by looping through it's parent relationships, and getting all attributes
	 * tied to those relationships.
	 *
	 * @param parentRelationships Relationships tied to a product.
	 * @return Attributes tied to the parent relationships.
	 */
	private Set<Attribute> getAllAttributesForProduct(List<GenericEntityRelationship> parentRelationships) {
		Set<Attribute> toReturn = new HashSet<>();
		int firstLevel = 0;
		this.addAllAttributesFromParents(parentRelationships, toReturn, firstLevel);
		return toReturn;
	}

	/**
	 * Filters a set of attributes by product types.
	 *
	 * @param attributes Set of un-filtered attributes.
	 * @param productType Product types to filter the full list by.
	 * @return Attributes that are connected to the product type given.
	 */
	private Set<Attribute> filterAttributesByProductTypes(Set<Attribute> attributes, String productType) {
		Set<Attribute> filteredAttributes = new HashSet<>();
		for(Attribute attribute : attributes){
			if(attribute.getApplicableTypeCode() == null){
				continue;
			}
			if(productType.equalsIgnoreCase(attribute.getApplicableTypeCode().trim())){
				filteredAttributes.add(attribute);
			}
		}
		return filteredAttributes;
	}

	/**
	 * Gets the product attributes for a product by product level (product, upc, case pack).
	 *
	 * @param productMaster Product master to get attributes for.
	 * @param productType Type of product (product, upc, case pack).
	 * @param parentRelationships Parent relationships of the product master.
	 * @return List of product attributes for the given product and product level.
	 */
	private List<ProductAttribute> getProductAttributesByProductIdAndProductType(
			ProductMaster productMaster, String productType, List<GenericEntityRelationship> parentRelationships,
			Set<Attribute> attributes) {
		List<DynamicAttribute> dynamicAttributes = this.getDynamicAttributesByProductType(productMaster, productType);
		return this.convertDynamicAttributesToProductAttributes(productMaster, productType, dynamicAttributes, parentRelationships, attributes);
	}

	/**
	 * Creates a product attribute record for each key (Product -> prodID, Selling Unit -> UPC, Case Pack -> itemId & itemType)
	 *
	 * @param productMaster The product master.
	 * @param productType The productType.
	 * @param parentRelationships The parentRelationships.
	 * @return The Product Attributes.
	 */
	private List<ProductAttribute> createProductAttributes(ProductMaster productMaster, String productType, List<GenericEntityRelationship> parentRelationships, Set<Attribute> attributes) {
		List<ProductAttribute> toReturn = new ArrayList<>();
		productType = productType.trim();
		if(ApplicableType.Code.PRODUCT.getId().equalsIgnoreCase(productType)){
			toReturn.addAll(
					createProductAttributesForKey(
							productType, parentRelationships, attributes, productMaster.getProdId()));
		}
		else if(ApplicableType.Code.UPC.getId().equalsIgnoreCase(productType)){
			// get selling units for a product
			List<SellingUnit> sellingUnits = productMaster.getSellingUnits();
			Long currentUpc;
			for (SellingUnit sellingUnit : sellingUnits){
				currentUpc = sellingUnit.getUpc();
				toReturn.addAll(createProductAttributesForKey(productType, parentRelationships,attributes, currentUpc));
			}
		}
		else if(ApplicableType.Code.DSD_ITEM.getId().equalsIgnoreCase(productType) ||
				ApplicableType.Code.WHSE_ITEM.getId().equalsIgnoreCase(productType)) {
			List<ProdItem> prodItems = productMaster.getProdItems();
			ProdItemKey key;
			for(ProdItem prodItem: prodItems){
				key = prodItem.getKey();
				if(productType.equalsIgnoreCase(key.getItemType().trim())) {
					toReturn.addAll(
							createProductAttributesForKey(
									key.getItemType(), parentRelationships, attributes, key.getItemCode()));
				}
			}
		}
		return toReturn;
	}

	/**
	 * Creates product attribute from key (Product -> prodID, Selling Unit -> UPC, Case Pack -> itemId & itemType)
	 *
	 * @param keyType The keyType.
	 * @param parentRelationships The parentRelationships.
	 * @param attributes The attributes.
	 * @param keyId The keyId.
	 * @return The Product Attributes.
	 */
	private List<ProductAttribute> createProductAttributesForKey(String keyType, List<GenericEntityRelationship> parentRelationships, Set<Attribute> attributes, Long keyId) {
		List<ProductAttribute> toReturn = new ArrayList<>();
		Set<GenericEntityRelationship> relationshipsTiedToAttribute;
		List<ProductAttributeHierarchyLevel> currentHierarchyLevels;
		ProductAttribute currentProductAttribute;
		for(Attribute attribute : attributes) {
			if(keyType.equalsIgnoreCase(attribute.getApplicableTypeCode())) {
				relationshipsTiedToAttribute = new HashSet<>();
				this.findParentEntityTiedToAttribute(
						relationshipsTiedToAttribute,
						parentRelationships, attribute.getAttributeId());
				currentHierarchyLevels = this.convertParentRelationshipsToProductAttributeHierarchyLevels(
						new ArrayList<>(relationshipsTiedToAttribute));
				currentProductAttribute = new ProductAttribute(
						attribute.getAttributeId(),
						keyId,
						keyType,
						attribute.getAttributeName(),
						new ArrayList<>(),
						attribute.getMultipleValueSwitch(),
						attribute.getAttributeValueList(),
						currentHierarchyLevels,
						attribute.getPrecision(),
						attribute.getMaximumLength(),
						attribute.getAttributeDomainCode())
						.setHierarchyLevelPosition(attribute.getLevelOfHierarchy());
				toReturn.add(currentProductAttribute);
			}
		}
		return toReturn;
	}

	/**
	 * Ties entity relationship -> entity -> entityAttribute -> attribute to get all attribute related to a relationship
	 * or any of its parents' relationships
	 *
	 * @param parentRelationships The parentRelationships.
	 * @param attributes The attributes.
	 */
	private void addAllAttributesFromParents(List<GenericEntityRelationship> parentRelationships, Set<Attribute> attributes, int levelOfHierarchy) {
		if(CollectionUtils.isEmpty(parentRelationships)){
			return;
		}
		int nextLevel = levelOfHierarchy + 1;
		final Attribute[] toAdd = new Attribute[1];
		parentRelationships.forEach(relationship -> {
			relationship.getGenericParentEntity().getEntityAttributes().forEach(
					entityAttribute -> {
						toAdd[0] = new Attribute(entityAttribute.getAttribute());
						toAdd[0].setLevelOfHierarchy(levelOfHierarchy);
						attributes.add(toAdd[0]);
					});
			this.addAllAttributesFromParents(relationship.getParentRelationships(), attributes, nextLevel);
		});
	}

	/**
	 * Gets the dynamic attributes based on the product type:
	 * 1. For 'product' type, return the dynamic attributes linked to the given product id.
	 * 2. For 'upc' type, return the dynamic attributes linked to the UPCs attached to the given product id.
	 * 3. For 'warehouse item' type, return the dynamic attributes linked to the warehouse items attached to the
	 * given product id.
	 * 4. For 'dsd item' type, return the dynamic attributes linked to the dsd items attached to the given product id.
	 *
	 * @param productMaster Product Master to retrieve attributes for.
	 * @param productType Type of product to retrieve attributes for.
	 * @return List of dynamic attributes for the given product id and product type.
	 */
	private List<DynamicAttribute> getDynamicAttributesByProductType(ProductMaster productMaster, String productType) {
		productType = productType.trim();
		if(ApplicableType.Code.PRODUCT.getId().equalsIgnoreCase(productType)){
			return this.dynamicAttributeService.getByKeyIdAndKeyType(productMaster.getProdId(), productType);
		}
		else if(ApplicableType.Code.UPC.getId().equalsIgnoreCase(productType)){
			// get selling units for a product
			List<SellingUnit> sellingUnits = productMaster.getSellingUnits();
			// get all upcs from the selling units
			List<Long> upcs = new ArrayList<>();
			for (SellingUnit sellingUnit : sellingUnits){
				upcs.add(sellingUnit.getUpc());
			}
			// get all dynamic attributes for a list of upcs
			return this.dynamicAttributeService.getByKeyIdsAndKeyType(upcs, productType);
		}
		else if(ApplicableType.Code.DSD_ITEM.getId().equalsIgnoreCase(productType) ||
				ApplicableType.Code.WHSE_ITEM.getId().equalsIgnoreCase(productType)) {
			return this.dynamicAttributeService.
					findCasePackDynamicAttributesByProductIdAndItemType(productMaster, productType);
		}
		return new ArrayList<>();
	}

	/**
	 * Converts a list of dynamic attributes to a list of product attributes. Each dynamic attribute contains a value
	 * for a given attribute, but the user needs to see records grouped by attribute. Each Product attribute is about
	 * one attribute, and may have more than one value.
	 *
	 *
	 * @param productMaster Product linking the dynamic attributes.
	 * @param productType Type of product.
	 * @param dynamicAttributes Dynamic attributes to convert.
	 * @param parentRelationships parent relationships tied to the given product.
	 * @return List of product attributes.
	 */
	private List<ProductAttribute> convertDynamicAttributesToProductAttributes(
			ProductMaster productMaster, String productType, List<DynamicAttribute> dynamicAttributes,
			List<GenericEntityRelationship> parentRelationships, Set<Attribute> attributes) {
		ProductAttribute currentProductAttribute;

		List<ProductAttribute> toReturn = this.createProductAttributes(productMaster, productType, parentRelationships, attributes);
		Set<GenericEntityRelationship> relationshipsTiedToAttribute;
		for(DynamicAttribute dynamicAttribute : dynamicAttributes){
			relationshipsTiedToAttribute = new HashSet<>();
			this.findParentEntityTiedToAttribute(
					relationshipsTiedToAttribute,
					parentRelationships, (long) dynamicAttribute.getKey().getAttributeId());
			if(relationshipsTiedToAttribute.isEmpty()){
				continue;
			}
			currentProductAttribute =
					this.findProductAttributeInList(toReturn, (long)dynamicAttribute.getKey().getAttributeId(), dynamicAttribute.getKey().getKey(), dynamicAttribute.getKey().getKeyType());
			// if the product attribute is found, add to the values
			if(currentProductAttribute != null){
				currentProductAttribute.getValues().add(this.getAttributeValue(dynamicAttribute));
			}
		}
		return toReturn;
	}

	/**
	 * Finds bottom most parent relationship the attribute it is tied to. If a relationship is found, return it. Else
	 * return null.
	 *
	 * @param parentRelationships List of parent relationships to look through.
	 * @param attributeId Attribute id to look for.
	 */
	private void findParentEntityTiedToAttribute(
			Set<GenericEntityRelationship> toReturn,
			List<GenericEntityRelationship> parentRelationships, long attributeId) {
		if(CollectionUtils.isEmpty(parentRelationships)){
			return;
		}
		for(GenericEntityRelationship parentRelationship: parentRelationships){
			if(this.isAttributeInEntityAttributes(
					parentRelationship.getGenericParentEntity().getEntityAttributes(),
					attributeId)){
				toReturn.add(parentRelationship);
			} else {
				findParentEntityTiedToAttribute(toReturn, parentRelationship.getParentRelationships(), attributeId);
			}
		}
	}

	/**
	 * This helper method finds out if a list of entity attributes contains the given attribute id.
	 *
	 * @param entityAttributes List of entity attributes to look through.
	 * @param attributeId Attribute id to look for.
	 * @return True if attribute id is found, else false.
	 */
	private boolean isAttributeInEntityAttributes(List<EntityAttribute> entityAttributes, Long attributeId) {
		for(EntityAttribute entityAttribute : entityAttributes){
			if(attributeId.equals(entityAttribute.getKey().getAttributeId())){
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the attribute value for a given attribute value. What this method returns depends on the following:
	 * 1. if the dynamic attribute's attribute is declared as having a value list, use the attribute code/value
	 * 2. else depending on the attribute domain type (number, date, string, etc), set the value from the respective
	 * data.
	 *
	 * @param dynamicAttribute Dynamic attribute related to the value for an attribute.
	 * @return The value for this dynamic attribute.
	 */
	private ProductAttributeValue getAttributeValue(DynamicAttribute dynamicAttribute) {
		if(dynamicAttribute.getAttribute().isAttributeValueList()){
			return new ProductAttributeValue(
					dynamicAttribute.getAttributeCode().getAttributeCodeId(),
					dynamicAttribute.getAttributeCode().getAttributeValueText(),
					dynamicAttribute.getKey().getSequenceNumber(),
					dynamicAttribute.getKey().getSourceSystem());
		}
		AttributeDomain.Code domainCode = AttributeDomain.Code
				.getDomainById(dynamicAttribute.getAttribute().getAttributeDomainCode());
		if(domainCode == null){
			return null;
		}
		switch (domainCode){
			case DECIMAL:{
				return new ProductAttributeValue(dynamicAttribute.getAttributeValueNumber(),
						dynamicAttribute.getKey().getSequenceNumber(),
						dynamicAttribute.getKey().getSourceSystem() );
			}
			case DATE:{
				return new ProductAttributeValue(dynamicAttribute.getAttributeValueDate(),
						dynamicAttribute.getKey().getSequenceNumber(),
						dynamicAttribute.getKey().getSourceSystem());
			}
			case INTEGER:{
				return new ProductAttributeValue(dynamicAttribute.getAttributeValueNumber(),
						dynamicAttribute.getKey().getSequenceNumber(),
						dynamicAttribute.getKey().getSourceSystem());
			}
			case STRING: {
				return new ProductAttributeValue(dynamicAttribute.getTextValue(),
						dynamicAttribute.getKey().getSequenceNumber(),
						dynamicAttribute.getKey().getSourceSystem());
			}
			case TIMESTAMP:{
				return new ProductAttributeValue(dynamicAttribute.getAttributeValueTime(),
						dynamicAttribute.getKey().getSequenceNumber(),
						dynamicAttribute.getKey().getSourceSystem());
			}
		}
		return null;
	}

	/**
	 * Converts a list of relationships tied to an attribute to a list of product attribute hierarchy levels.
	 *
	 * @param relationshipsTiedToAttribute Relationships tied to an attribute.
	 * @return List of product attribute hierarchy levels.
	 */
	private List<ProductAttributeHierarchyLevel> convertParentRelationshipsToProductAttributeHierarchyLevels(List<GenericEntityRelationship> relationshipsTiedToAttribute) {

		if(CollectionUtils.isEmpty(relationshipsTiedToAttribute)){
			return new ArrayList<>();
		}

		ProductAttributeHierarchyLevel currentAttributeLevel;
		GenericEntityDescription currentEntityDescription;
		List<GenericEntityRelationship> currentAllPaths;
		List<ProductAttributeHierarchyLevel> toReturn = new ArrayList<>();
		List<Long> entityIdsAdded = new ArrayList<>();

		for(GenericEntityRelationship relationship: relationshipsTiedToAttribute) {
			if(entityIdsAdded.contains(relationship.getKey().getParentEntityId())){
				continue;
			}
			currentAllPaths = this.getAllPathsToParent(relationship);
			// if you are at the root node, the paths will be empty; set "Global" as description
			if(CollectionUtils.isEmpty(currentAllPaths)){
				GenericEntityRelationship rootNode = new GenericEntityRelationship();
				GenericEntityDescription contextDescription = new GenericEntityDescription();
				contextDescription.setShortDescription(ProductTaxonomyAttributeService.GLOBAL_ATTRIBUTE_DESCRIPTION);
				rootNode.setChildDescription(contextDescription);
				currentAllPaths = new ArrayList<>();
				currentAllPaths.add(rootNode);
				currentEntityDescription = contextDescription;
			} else {
				// else you are at a child node
				currentEntityDescription = relationship.getParentDescription();
				if (currentEntityDescription == null) {
					currentEntityDescription = new GenericEntityDescription();
					currentEntityDescription.setKey(new GenericEntityDescriptionKey());
					currentEntityDescription.getKey().setEntityId(relationship.getKey().getParentEntityId());
					currentEntityDescription.getKey().setHierarchyContext(matHierarchyContext);
					currentEntityDescription.setShortDescription(
							String.format(DESCRIPTION_NOT_FOUND_ERROR, relationship.getKey().getParentEntityId()));
				}
			}
			currentAttributeLevel = new ProductAttributeHierarchyLevel()
					.setEntityDescription(currentEntityDescription)
					.setPaths(currentAllPaths);
			toReturn.add(currentAttributeLevel);
			entityIdsAdded.add(relationship.getKey().getParentEntityId());
		}
		return toReturn;
	}

	/**
	 * Returns all paths to a given relationship within the MAT.
	 *
	 * @param relationship Relationship to find all paths to.
	 * @return List of relationships that represent the paths to the given relationship's parent entity id.
	 */
	private List<GenericEntityRelationship> getAllPathsToParent(GenericEntityRelationship relationship) {
		return this.customerHierarchyService.getAllParentsOfChild(
				relationship.getKey().getParentEntityId(), matHierarchyContext);
	}

	/**
	 * Searches for a product attribute with the given attribute id within a given list of product attributes. If the
	 * product attribute is found, return that product attribute. Else return null.
	 *
	 * @param productAttributes List of product attributes to search through.
	 * @param attributeId Attribute id to search for.
	 * @param key The key id (i.e. the prod id, upc, itmcd, etc).
	 * @param keyType The key type (PROD, ITMCD, DSD, UPC, etc).
	 * @return Product attribute with the given attribute id, or null if not found.
	 */
	private ProductAttribute findProductAttributeInList(List<ProductAttribute> productAttributes, long attributeId, long key, String keyType) {
		for(ProductAttribute productAttribute : productAttributes){
			if(attributeId == productAttribute.getAttributeId() && key == productAttribute.getKeyId() && keyType.equalsIgnoreCase(productAttribute.getKeyType())){
				return productAttribute;
			}
		}
		return null;
	}

	/**
	 * Returns a product master based on a product id.
	 *
	 * @param prodId The product id to search for.
	 * @return The product master
	 */
	private ProductMaster getProductMasterByProdId(Long prodId){
		return this.productMasterRepository.findOne(prodId);
	}

	/**
	 * Returns a list of product attribute values created with a list of entity attribute codes.
	 *
	 * @param attributeId The attributeId used to search for list of entity attribute codes.
	 * @return The list of ProductAttributeValues.
	 */
	public List<ProductAttributeValue> getProductAttributeValues(Long attributeId) {
		List<ProductAttributeValue> toReturn = new ArrayList<>();
		this.attributeMaintenanceService.getAttributeValues(attributeId).forEach(entityAttributeCode -> {
			toReturn.add(
					new ProductAttributeValue(entityAttributeCode.getKey().getAttributeCodeId(), entityAttributeCode.getAttributeCode().getAttributeValueText())
			);
		});
		return toReturn;
	}

	/**
	 * Sends all added or removed attribute values to the web service.
	 *
	 * @param productAttributeHeader The productAttributeHeader.
	 */
	public void update(ProductAttributeHeader productAttributeHeader) {
		List<ProductAttribute> currentProductAttributes  = productAttributeHeader.getProductAttributes();
		List<ProductAttribute> originalProductAttributes =
				this.getByProductIdAndProductType(productAttributeHeader.getProductId(), productAttributeHeader.getProductType());
		List<ProductAttribute> toReturn = new ArrayList<>();
		String addAction = "A";
		String deleteAction = "D";

		this.getUpdates(originalProductAttributes, currentProductAttributes, deleteAction, toReturn);
		this.getUpdates(currentProductAttributes, originalProductAttributes, addAction, toReturn);

		toReturn.forEach( productAttribute -> ProductTaxonomyAttributeService.logger.info(String.format(
				PRODUCT_ATTRIBUTE_CHANGES, productAttribute.getAttributeId(), productAttribute.getValues().toString())));
		productAttributeManagementServiceClient.updateDynamicAttributes(toReturn);
	}

	/**
	 * Finds all the attribute values that were added or removed.
	 *
	 * @param productAttributes List of productAttributes.
	 * @param productAttributesToCompare List of productAttributes being compared.
	 * @param action The action of adding or removing attribute value.
	 * @param toReturn The added or removed attribute values.
	 */
	private void getUpdates(List<ProductAttribute> productAttributes, List<ProductAttribute> productAttributesToCompare, String action, List<ProductAttribute> toReturn) {
		ProductAttribute productAttribute;
		ProductAttribute compareProductAttribute;
		ProductAttribute existingProductAttribute;
		List<ProductAttributeValue> values;
		Integer nextSequence;

		for(ProductAttribute attribute : productAttributes){
			productAttribute = attribute;
			nextSequence = null;
			values = new ArrayList<>();
			compareProductAttribute = this.findProductAttributeInList(productAttributesToCompare, attribute.getAttributeId(), attribute.getKeyId(), attribute.getKeyType());

			if(compareProductAttribute == null){
				throw new IllegalArgumentException("error");
			}

			for (ProductAttributeValue value : productAttribute.getValues()) {
				if (value == null) {
					continue;
				}

				if (!this.findMatchingProductAttributeValue(value, compareProductAttribute)) {
					if("D".equals(action)) {
						values.add(
								new ProductAttributeValue(value.getType(), value.getText(), value.getNumber(), value.getTimestamp(),
										value.getDate(), value.getCode(), value.getSequenceNumber(), (long)value.getSourceSystem(), action)
						);
					}
					else{
						if(nextSequence == null){
							nextSequence = this.dynamicAttributeService.
									findMaxSequenceNumberForByAttributeIdAndKeyIdAndKeyType(
											attribute.getAttributeId(), attribute.getKeyId(),
											attribute.getKeyType(),
											SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_MAT.getValue());
							nextSequence = nextSequence == null ? 0 : nextSequence;
						}
						values.add(
								new ProductAttributeValue(value.getType(), value.getText(), value.getNumber(), value.getTimestamp(),
										value.getDate(), value.getCode(), ++nextSequence, SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_MAT.getValue(), action)
						);
					}
				}
			}

			if(!CollectionUtils.isEmpty(values)){
				existingProductAttribute = this.findProductAttributeInList(toReturn, attribute.getAttributeId(), attribute.getKeyId(), attribute.getKeyType());
				if(existingProductAttribute != null){
					existingProductAttribute.getValues().addAll(values);
				}
				else{
					toReturn.add(
							new ProductAttribute(attribute.getAttributeId(), attribute.getKeyId(), attribute.getKeyType(),
									attribute.getAttributeName(), values, attribute.isMultipleAllowed(), attribute.isListOfValues(),
									attribute.getHierarchyLevels(), attribute.getPrecision(), attribute.getMaximumLength(),
									attribute.getDomain())
					);
				}
			}
		}
	}

	/**
	 * Tells whether a attribute value is in a list of productAttribute values.
	 *
	 * @param value The attribute value.
	 * @param productAttribute Contains the list of attribute values.
	 * @return A boolean.
	 */
	private boolean findMatchingProductAttributeValue(ProductAttributeValue value, ProductAttribute productAttribute) {
		if(CollectionUtils.isEmpty(productAttribute.getValues())){
			return false;
		}
		return productAttribute.getValues().stream().anyMatch(value::equals);
	}
}
