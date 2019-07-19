package com.heb.pm.batchUpload.matAttributeValues;

import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Processes MatAttributeValueModels into WrappedMatAttributeValueToMasterDataExtendedAttribute.
 *
 * @author s573181
 * @since 2.29.0
 */
public class MatAttributeValuesProcessor implements ItemProcessor<MatAttributeValueModel, WrappedMatAttributeValueToMasterDataExtendedAttribute>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(MatAttributeValuesProcessor.class);

	public static final String PRODUCT_KEY_TYPE = "PRODUCT";
	public static final String ITEM_KEY_TYPE = "ITEM";
	public static final String UPC_KEY_TYPE = "UPC";

	@Value("${app.mat.HierarchyContext}")
	private String matHierarchyContext;

	@Value("${app.mat.sourceSystemId}")
	private int sourceSystemId;

	private Long matHierarchyContextEntityId;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Autowired
	private GenericEntityRepository genericEntityRepository;

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	private EntityAttributeRepository entityAttributeRepository;

	@Autowired
	private AttributeRepository attributeRepository;

	@Autowired
	private HierarchyContextRepository hierarchyContextRepository;

	@Autowired
	private EntityAttributeCodeRepository entityAttributeCodeRepository;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	/**
	 * Sets the mat hierarchy context entity id.
	 *
	 * @param stepExecution the stepExecution.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		matHierarchyContextEntityId = this.hierarchyContextRepository.findOne(this.matHierarchyContext).getParentEntityId();
	}

	/**
	 * Empty override function.
	 * @param stepExecution the stepExecution.
	 * @return null;
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	/**
	 * Processes a matAttributeValueModel into a WrappedMatAttributeValueToMasterDataExtendedAttribute.
	 *
	 * @param item the matAttributeValueModel to process.
	 * @return a WrappedMatAttributeValueToMasterDataExtendedAttribute
	 * @throws FileProcessingException exception.
	 */
	@Override
	public WrappedMatAttributeValueToMasterDataExtendedAttribute process(MatAttributeValueModel item) throws FileProcessingException {

		try {
			if (item == null) {
				return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Invalid data");
			} else if(StringUtils.isNotBlank(item.getErrorMessage())) {
				return new WrappedMatAttributeValueToMasterDataExtendedAttribute(item.getErrorMessage());
			}
			MasterDataExtensionAttribute masterDataExtensionAttribute = new MasterDataExtensionAttribute();
			Attribute attribute = this.attributeRepository.findOne(item.getAttributeId());
			if (attribute == null) {
				return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Attribute id: " + item.getAttributeId() + "not found.");
				// if item key type doesn't match supplied product type (prod, item, upc).
			} else if (attribute.getApplicableTypeCode() == null) {
				return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Attribute type not tied to a product type.");
			} else if ((item.getKeyType().toUpperCase().equalsIgnoreCase(PRODUCT_KEY_TYPE) &&
					!attribute.getApplicableTypeCode().equalsIgnoreCase(ApplicableType.Code.PRODUCT.getId())) ||
					(item.getKeyType().toUpperCase().equalsIgnoreCase(ITEM_KEY_TYPE) &&
							!attribute.getApplicableTypeCode().equalsIgnoreCase(ApplicableType.Code.WHSE_ITEM.getId())) ||
					(item.getKeyType().toUpperCase().equalsIgnoreCase(UPC_KEY_TYPE) &&
							!attribute.getApplicableTypeCode().equalsIgnoreCase(ApplicableType.Code.UPC.getId()))) {
				return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Attribute type " +
						attribute.getApplicableTypeCode() + "doesn't match supplied product type: " + item.getKeyType());
			}

			masterDataExtensionAttribute.setKey(new MasterDataExtensionAttributeKey());
			masterDataExtensionAttribute.getKey().setAttributeId(attribute.getAttributeId());
			masterDataExtensionAttribute.getKey().setId(item.getKeyId());
			masterDataExtensionAttribute.getKey().setDataSourceSystem((long) this.sourceSystemId);
			masterDataExtensionAttribute.getKey().setSequenceNumber(0L);
			masterDataExtensionAttribute.setAction(MasterDataExtensionAttribute.INSERT);

			// If attribute isn't a global attribute, find product id
			if (!item.getKeyType().equalsIgnoreCase(ITEM_KEY_TYPE)) {
				long prodId;
				if (item.getKeyType().equalsIgnoreCase(PRODUCT_KEY_TYPE)) {
					prodId = item.getKeyId();
					if (this.productMasterRepository.findOne(prodId) == null) {
						return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Product ID not found.");
					}
					masterDataExtensionAttribute.getKey().setItemProdIdCode("PROD");
				} else if (item.getKeyType().equalsIgnoreCase(UPC_KEY_TYPE)) {
					masterDataExtensionAttribute.getKey().setItemProdIdCode("UPC");
					SellingUnit sellingUnit = this.sellingUnitRepository.findOne(item.getKeyId());
					if (sellingUnit == null) {
						return new WrappedMatAttributeValueToMasterDataExtendedAttribute("UPC not found.");
					}
					prodId = sellingUnit.getProdId();
				} else {
					return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Invalid product type: '" + item.getKeyType() + "'.");
				}
				Long entityId;

				// find Entity id associated with the product attribute.
				try {
					entityId = this.getProductAttributeEntityId(prodId, item.getAttributeId());
				} catch (Exception e) {
					return new WrappedMatAttributeValueToMasterDataExtendedAttribute(e.getMessage());
				}
				if (entityId == null) {
					return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Attribute not tied to product.");
				}
			} else {
				masterDataExtensionAttribute.getKey().setItemProdIdCode("ITMCD");
				ItemMasterKey itemMasterKey = new ItemMasterKey();
				itemMasterKey.setItemType(ItemMasterKey.WAREHOUSE);
				itemMasterKey.setItemCode(item.getKeyId());
				ItemMaster itemMaster = this.itemMasterRepository.findOne(itemMasterKey);
				if (itemMaster == null) {
					return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Item id: " + item.getKeyId() + " not found.");
				}

			}
			if(attribute.isAttributeValueList()){
				List<EntityAttributeCode> entityAttributeCodes = this.entityAttributeCodeRepository.findByKeyAttributeIdAndKeyAttributeCodeId(
						item.getAttributeId(), (long) Double.parseDouble(item.getAttributeValue()));
				if(CollectionUtils.isEmpty(entityAttributeCodes)) {
					return new WrappedMatAttributeValueToMasterDataExtendedAttribute("Attribute code: " +
							(long) Double.parseDouble(item.getAttributeValue()) + " not found.");
				}
				masterDataExtensionAttribute.setAttributeCodeId((long) Double.parseDouble(item.getAttributeValue()));
			} else {
				try {
					this.setAttributeValueByAttributeDomainCode(attribute.getAttributeDomainCode(), masterDataExtensionAttribute, item);
				} catch (IllegalArgumentException e) {
					return new WrappedMatAttributeValueToMasterDataExtendedAttribute(e.getMessage());
				}
			}
			return new WrappedMatAttributeValueToMasterDataExtendedAttribute(masterDataExtensionAttribute);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new WrappedMatAttributeValueToMasterDataExtendedAttribute(e.getMessage());
		}
	}


	/**
	 * Returns the entity id associated with an attribute id and a product id, or null if it's not found.
	 *
	 * @param productId the product id.
	 * @param attributeId the attribute id.
	 * @return the entity id associated with an attribute id and a product id, or null if it's not found.
	 * @throws Exception exception containing detailed error message.
	 */
	private Long getProductAttributeEntityId(long productId, long attributeId) throws Exception {
		List<GenericEntity> genericEntities =
				this.genericEntityRepository.findByDisplayNumberAndType(productId, GenericEntity.EntyType.PROD.getName());
		if(CollectionUtils.isEmpty(genericEntities)) {
			throw new Exception("Entity not tied to a product.");
		} else if(genericEntities.size() > 1) {
			throw new Exception("More than one entity tied to a product.");
		}
		return this.findEntityIdByChildEntityIdAndAttributeId(genericEntities.get(0).getId(), attributeId);
	}

	/**
	 * Recursive call that returns the entity id associated with an attribute id and a child id,
	 * or null if it's not found.
	 *
	 * @param entityId the entity id of the child.
	 * @param attributeId the attribute id.
	 * @return the entity id associated with an attribute id and a child id, or null if it's not found.
	 * @throws Exception exception containing detailed error message.
	 */
	private Long findEntityIdByChildEntityIdAndAttributeId(long entityId, long attributeId) throws Exception  {

		List<GenericEntityRelationship> genericEntityRelationships = this.genericEntityRelationshipRepository.
				findByKeyChildEntityIdAndHierarchyContext(entityId, this.matHierarchyContext);
		if(CollectionUtils.isEmpty(genericEntityRelationships)) {
			throw new Exception("Product not tied to the MAT hierarchy.");
		} else if (genericEntityRelationships.size() > 1) {
			throw new Exception("Product tied to more than one spot in the MAT hierarchy.");
		}

		EntityAttributeKey key = new EntityAttributeKey();
		key.setAttributeId(attributeId);
		key.setEntityId(genericEntityRelationships.get(0).getKey().getParentEntityId());
		EntityAttribute entityAttribute = this.entityAttributeRepository.findOne(key);
		if(entityAttribute != null) {
			return entityAttribute.getKey().getEntityId();
			// if is root hierarchy and attribute not found, return null;
		} else if (genericEntityRelationships.get(0).getKey().getParentEntityId().equals(this.matHierarchyContextEntityId)) {
			return null;
		} else {
			return this.findEntityIdByChildEntityIdAndAttributeId(
					genericEntityRelationships.get(0).getKey().getParentEntityId(), attributeId);
		}
	}

	/**
	 * Sets the corresponding columns attribute value by domain code.
	 *
	 * @param domainCode the domain code.
	 * @param masterDataExtensionAttribute the masterDataExtensionAttribute.
	 * @param model the model.
	 * @throws IllegalArgumentException throws an exception with message of the error.
	 */
	private void setAttributeValueByAttributeDomainCode(String domainCode, MasterDataExtensionAttribute
			masterDataExtensionAttribute, MatAttributeValueModel model) throws IllegalArgumentException {
		switch (domainCode.toUpperCase()) {
			case MasterDataExtensionAttribute.STRING_ATTRIBUTE_VALUE: {
				if(!model.getAttributeValue().isEmpty()) {
					masterDataExtensionAttribute.setAttributeValueText(model.getAttributeValue());
					break;
				} else {
					throw new IllegalArgumentException("Attribute value isn't a string.");
				}
			}
			case MasterDataExtensionAttribute.TIME_ATTRIBUTE_VALUE: {
				if(model.getAttributeDateValue() != null) {
					masterDataExtensionAttribute.setAttributeValueTime(LocalDateTime.ofInstant(
							model.getAttributeDateValue().toInstant(), ZoneId.systemDefault()));
					break;
				} else {
					throw new IllegalArgumentException("Attribute value isn't a date.");
				}
			}
			case MasterDataExtensionAttribute.DATE_ATTRIBUTE_VALUE: {
				if(model.getAttributeDateValue() != null) {
					masterDataExtensionAttribute.setAttributeValueDate(model.getAttributeDateValue().toInstant().atZone(
							ZoneId.systemDefault()).toLocalDate());
					break;
				} else {
					throw new IllegalArgumentException("Attribute value isn't a date.");
				}
			}
			case MasterDataExtensionAttribute.DECIMAL_ATTRIBUTE_VALUE: {
				try {
					masterDataExtensionAttribute.setAttributeValueText(model.getAttributeValue());
					masterDataExtensionAttribute.setAttributeValueNumber(Double.parseDouble(model.getAttributeValue()));
				} catch (Exception e) {
					throw new IllegalArgumentException("Attribute value isn't a number.");
				}
				break;
			}
			case MasterDataExtensionAttribute.INTEGER_ATTRIBUTE_VALUE: {
				try {
					masterDataExtensionAttribute.setAttributeValueText(model.getAttributeValue());
					masterDataExtensionAttribute.setAttributeValueNumber(Double.parseDouble(model.getAttributeValue()));
				} catch (Exception e) {
					throw new IllegalArgumentException("Attribute value isn't a number.");
				}
				break;
			}
		}
	}
}
