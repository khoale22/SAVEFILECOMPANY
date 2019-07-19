/*
 *  AuditService
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.audit;

import com.heb.pm.entity.*;
import com.heb.pm.productHierarchy.SubCommodityAuditImpl;
import com.heb.pm.repository.*;
import com.heb.pm.taxCategory.TaxCategoryService;
import com.heb.util.audit.*;
import com.heb.util.list.IntegerPopulator;
import com.heb.util.list.LongPopulator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds all business logic related to Audits.
 *
 * @author l730832
 * @since 2.6.0
 */
@Service
public class AuditService {

	public static final String PROPOSED = "Proposed";
	public static final String APPROVED = "Approved";

	private String PRODUCT_TYPE = "PROD";
	private static final String SHIPPING_RESTRICTION_CODE="9";

	public static final String RELATIONSHIP_TYPE = "Relationship Type";
	public static final int SOURCE_SYSTEM_4 = 4;
	public static final int SOURCE_SYSTEM_13 = 13;
	public static final String N_A = "N/A";
	private String PROD_EXT_DATA_CODE_ELONG = "ELONG";
	private String PROD_EXT_DATA_CODE_ESHRT = "ESHRT";
	private long DATA_SOURCE_SYSTEM_4 = 4;
	private long LOG_ATTR_ID_SIZE = 1636;
	private long LOG_ATTR_ID_BRAND = 1642;
	private int MAX_NUMBER_OF_RECORDS_PER_QUERY = 100;
	/** The Constant STRING_Brand. */
	public static final String STRING_BRAND = "Brand";
	/** The Constant STRING_SIZE. */
	/** The Constant SIZE. */
	public static final String SIZE = "Size";

	/** The Constant AUDIT_ATTR_FOR_ECOMMERCE_VIEW_DISP_NAME. */
	public static final String AUDIT_ATTR_FOR_ECOMMERCE_VIEW_DISP_NAME = "Display Name";
	/** The Constant AUDIT_ATTR_FOR_ECOMMERCE_VIEW_ROMANCE_COPY. */
	public static final String AUDIT_ATTR_FOR_ECOMMERCE_VIEW_ROMANCE_COPY = "Romance Copy";

	/** The Constant WARNINGS. */
	public static final String WARNINGS = "Warnings";
	/** The Constant INGREDIENTS. */
	public static final String INGREDIENTS = "Ingredients";
	/** The Constant DIRECTIONS. */
	public static final String DIRECTIONS = "Directions";
	/** The Constant PDP_TEPLATE_ID. */
	public static final String PDP_TEPLATE_ID = "PDP Template Id";
	/** The Constant NUTRITION. */
	public static final String NUTRITION = "Nutrient facts";
	/** The Constant MULTI_DIMENSIONAL. */
	public static final String MULTI_DIMENSIONAL = "Multi-dimensional";
	/** The Constant PURGE. */
	public static final String PURGE = "PURGE";
	// The default number of items to search for. This number
	// and fewer will have the maximum performance.
	private static final int DEFAULT_ITEM_COUNT = 100;
	public static final String PROD_RLSHP_CD = "BPACK";

	/**
	 * RESULT_NA.
	 */
	public static final String RESULT_NA = "NA";
	/** The Constant SELLING_UNITS. */
	public static final String SELLING_UNITS = "Selling Units";
	/** The Constant UPC. */
	public static final String UPC = "UPC";

	/** The Constant for product group membership attribute name. */
	public static final String PRODUCT_GROUP_MEMBERSHIP_ATTRIBUTE_NAME = "Product ID";
	/** The Constant for product group hierarchy attribute name. */
	public static final String PRODUCT_GROUP_HIERARCHY_ATTRIBUTE_NAME = "Customer Hierarchy";
	/** The Constant for Altemate Path*/
	public static final String ALTEMATE_PATH = "Altemate Path";
	/** The Constant for Primary Path*/
	public static final String PRIMARY_PATH = "Primary Path";

	/**
	 * STRING_0.
	 */
	public static final String STRING_0 = "0";
	/**
	 * STRING_SEVEN.
	 */
	public static final String STRING_SEVEN = "7";
	/**
	 * STRING_NINE.
	 */
	public static final String STRING_NINE = "9";
	/**
	 * STRING_EMPTY.
	 */
	public static final String EMPTY = "";
	/**
	 * UNKNOWN
	 */
	public static final String UNKNOWN = "Unknown";
	/**
	 * NULL
	 */
	public static final String NULL = "null";
	/**
	 * DSD
	 */
	public static final String DSD = "DSD";
	/**
	 * DISCONTINUED BY
	 */
	public static final String DISCONTINUED_BY = "Discontinued By";
	/**
	 * DISCONTINUE DATE
	 */
	public static final String DISCONTINUE_DATE = "Discontinue Date";
	/**
	 * DISCONTINUE REASON
	 */
	public static final String DISCONTINUE_REASON = "Discontinue Reason";
	/**
	 * NONE
	 */
	public static final String NONE = "None";
	/**
	 * GUARANTEE IMAGE
	 */
	public static final String GUARANTEE_IMAGE = "Guarantee Image";

	/**
	 * User display name format
	 */
	private static final String USER_DISPLAY_NAME_FORMAT = "%s[%s]";

	/** Logic attribute. */
	public enum LogicAttributeCode {
		BRAND_LOGIC_ATTRIBUTE_ID(1672L),
		WARNING_LOGIC_ATTRIBUTE_ID(1677L),
		ALLERGENS_LOGIC_ATTRIBUTE_ID(1644L),
		WARNINGS_LOGIC_ATTRIBUTE_ID(1682L),
		PDP_TEMPLATE_LOGIC_ATTRIBUTE_ID(515L),
		DIRECTIONS_LOGIC_ATTRIBUTE_ID(1676L),
		NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID(1679L),
		DIMENSIONS_LOGIC_ATTRIBUTE_ID(1784L),
		SPECIFICATION_LOGIC_ATTRIBUTE_ID(1728L),
		ROMANCE_COPY_LOGIC_ATTRIBUTE_ID(1666L),
		TAGS_LOGIC_ATTRIBUTE_ID(1729L),
		INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID(1674L),
		DISPLAY_NAME_LOGIC_ATTRIBUTE_ID(1664L),
		SIZE_LOGIC_ATTRIBUTE_ID(1667L),
		INGREDIENTS_PET_GUARANTEED_LOGIC_ATTRIBUTE_ID(1803L),
		INGREDIENTS_LOGIC_ATTRIBUTE_ID(1643L),
		NULL(0L);

		private Long value;

		LogicAttributeCode(Long value) {
			this.value = value;
		}

		public Long getValue() {
			return this.value;
		}

		public static LogicAttributeCode getAttribute(Long attributeId) {
			for (LogicAttributeCode attribute : LogicAttributeCode.values()) {
				if (attribute.getValue().equals(attributeId)) {
					return attribute;
				}
			}
			return NULL;
		}
	}
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

	@Value("${app.sourceSystemId}")
	private int DefaultSourceSystemCode;

    @Autowired
	private ShipperAuditRepository shipperAuditRepository;

	@Autowired
	private VendorLocationItemAuditRepository vendorLocationItemAuditRepository;

	@Autowired
	private ImportItemAuditRepository importItemAuditRepository;

	@Autowired
	@Qualifier("auditComparisonImpl")
	private AuditComparisonDelegate auditComparisonImplementation;

	@Autowired
	private ProductRelationshipAuditRepository productRelationshipAuditRepository;

	@Autowired
	private GoodsProductAuditRepository goodsProductAuditRepository;

	@Autowired
	private SellingUnitAuditRepository sellingUnitAuditRepository;

    @Autowired
    private ProductMasterAuditRepository productMasterAuditRepository;

	@Autowired
	private DynamicAttributeAuditRepository dynamicAttributeAuditRepository;

    @Autowired
    private RxProductAuditRepository rxProductAuditRepository;

	@Autowired
	private TobaccoProductAuditRepository tobaccoProductAuditRepository;

	@Autowired
	private ProductRestrictionsAuditRepository productRestrictionsAuditRepository;

	@Autowired
	private WarehouseLocationItemAuditRepository warehouseLocationItemAuditRepository;

	@Autowired
	private ProductDescriptionAuditRepository productDescriptionAuditRepository;
	@Autowired
	private  AttributeRepository  attributeRepository;
	@Autowired
	private NutrientAuditRepository nutrientAuditRepository;
	@Autowired
	private ProductPkVarAuditRepository productPkVarAuditRepository;
	@Autowired
	private ProductScanCodeExtentAuditRepositoryWithCount productScanCodeExtentAuditRepositoryWithCount;

	@Autowired
	private MasterDataExtensionAuditRepositoryWithCount masterDataExtensionAuditRepositoryWithCount;
	@Autowired
	private ProductFulfillmentChanelAuditRepository productFulfillmentChanelAuditRepository;

	@Autowired
	private CustomerProductGroupMembershipAuditRepositoryWithCount custProductGroupMembershipAuditRepositoryWithCount;

	@Autowired
	private CustomerProductGroupAuditRepositoryWithCount customerProductGroupAuditRepositoryWithCount;

	@Autowired
	private GenericEntityRelationshipAuditRepositoryWithCount genericEntityRelationshipAuditRepositoryWithCount;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Autowired
	private ProductOnlineAuditRepository productOnlineAuditRepository;

	@Autowired
	private SubCommodityAuditRepository subCommodityAuditRepository;

	@Autowired
	private TaxCategoryService taxCategoryService;

	@Autowired
	private ItemMasterAuditRepository itemMasterAuditRepository;
	@Autowired
	private StoreDepartmentAuditRepository storeDepartmentAuditRepository;

	@Autowired
	private RetailUnitOfMeasureRepository retailUnitOfMeasureRepository;

	@Autowired
	private LdapSearchRepository ldapSearchRepository;

	@Autowired
	private ProductPreferredUnitOfMeasureAuditRepositoryWithCount productPreferredUnitOfMeasureAuditRepositoryWithCount;

	@Autowired
	private SubCommodityStateWarningAuditRepositoryWithCounts subCommodityStateWarningAuditRepositoryWithCounts;

	@Autowired
	private SellingRestrictionHierarchyLevelAuditRepositoryWithCounts sellingRestrictionHierarchyLevelAuditRepositoryWithCounts;

	@Autowired
	private ShippingRestrictionHierarchyLevelAuditRepositoryWithCounts shippingRestrictionHierarchyLevelAuditRepositoryWithCounts;

	// Used to get consistent size lists to query runners.
	private IntegerPopulator integerPopulator = new IntegerPopulator();
	// Used to get consistent size lists to query runners.
	private LongPopulator longPopulator = new LongPopulator();
	/**
	 * Returns a list of mrt audits that also calls the ldap server to retrieve the usernames that correspond to the
	 * uid that performed the actions.
	 *
	 * @param upc The upc to be searched on.
	 * @return List of audits.
	 */
	public List<AuditRecordWithUpc> getMrtAuditInformation(Long upc) {
		List<ShipperAudit> shipperAudits = this.shipperAuditRepository.findByKeyUpcOrderByKeyChangedOn(upc);

		// map of user id -> user display names
		Map<String, String> mapUserNames = this.auditComparisonImplementation.mapUserIdToUserDisplayName(shipperAudits);

		// keeps track of previous audit for a given upc
		Map<Long, ShipperAudit> previousAuditForModifiedUpcMap = new HashMap<>();
		List<AuditRecord> currentAudits;
		List<AuditRecordWithUpc> auditRecordWithUpcs = new ArrayList<>();
		Long currentModifiedUpc;

		for (ShipperAudit currentAudit : shipperAudits) {

			currentModifiedUpc = currentAudit.getKey().getModifiedUpc();
			currentAudits = new ArrayList<>();

			// compare current audit with previous audit mapping same modified upc
			currentAudits.addAll(
					this.auditComparisonImplementation.processClass(
							currentAudit,
							previousAuditForModifiedUpcMap.get(currentModifiedUpc),
							mapUserNames.get(currentAudit.getChangedBy())));

			// convert the audit records to audit records with upc
			auditRecordWithUpcs.addAll(0,
					this.convertAuditRecordsToAuditRecordsWithUpcs(
							currentAudits, currentModifiedUpc));

			// if current audit was a PURGE action, remove current modified upc from the previous audit map
			if(currentAudit.getAction().trim().equals(AuditRecord.ActionCodes.PURGE.name())){
				previousAuditForModifiedUpcMap.remove(currentModifiedUpc);
			}

			// else add current audit as the previous audit for the current modified upc
			else {
				previousAuditForModifiedUpcMap.put(currentModifiedUpc, currentAudit);
			}
		}
		return auditRecordWithUpcs;
	}

	/**
	 * This method converts a list of audit records to a list of audit records with upcs given a list of audit records
	 * and a modified upc. This is done by calling the AuditRecordWithUpc constructor being passed an AuditRecord,
	 * then setting the upc for the AuditRecordWithUpc.
	 *
	 * @param currentAudits List of audit records to convert.
	 * @param modifiedUpc Upc to set as the upc on the list of audit records with upcs.
	 * @return List of audit records with upcs.
	 */
	private List<AuditRecordWithUpc> convertAuditRecordsToAuditRecordsWithUpcs(List<AuditRecord> currentAudits, Long modifiedUpc) {
		List<AuditRecordWithUpc> auditRecordWithUpcs = new ArrayList<>();
		for(AuditRecord currentAudit : currentAudits){

			// call constructor passing an audit record and upc, then add to list
			auditRecordWithUpcs.add(new AuditRecordWithUpc(currentAudit, modifiedUpc));
		}
		return auditRecordWithUpcs;
	}

	/**
	 * Get all vendor location item audits ordered by changed on ascending order(oldest record is first), then send the
	 * the audit comparison implementation the current and previous audit for each of the vendor location item
	 * audits to create audit records. Afterwards, reverse the list so the user sees the list in changed on descending
	 * order (newest record is first).
	 *
	 * @param key The key of the vendor the user has selected to view audits for.
	 * @return List of all audit records, in order of time descending (newest record first).
	 */
	public List<AuditRecord> getVendorAuditInformation(VendorLocationItemKey key) {

		// get all database audit records in ascending date order
		List<VendorLocationItemAudit> tableAudits = this.vendorLocationItemAuditRepository.
				findByKeyItemCodeAndKeyItemTypeAndKeyVendorTypeAndKeyVendorNumberOrderByKeyChangedOn(
						key.getItemCode(), key.getItemType(), key.getVendorType(), key.getVendorNumber()
				);

		return this.auditComparisonImplementation.processClassFromList(tableAudits);
	}

	/**
	 * Get all records of changes to the Case Pack Import
	 * @param key used to uniquely identify the Item
	 * @return List of all audit records, in order of time descending (newest record first).
	 */
	public List<AuditRecord> getImportItemAuditInformation(ImportItemKey key){
		List<ImportItemAudit> tableAudits = this.importItemAuditRepository.
				findByKeyItemCodeAndKeyItemTypeAndKeyVendorTypeAndKeyVendorNumberOrderByKeyChangedOn(
						key.getItemCode(),key.getItemType(), key.getVendorType(), key.getVendorNumber());
		return this.auditComparisonImplementation.processClassFromList(tableAudits);
	}
	/**
	 * Get all records of changes to the Case Pack attributes in an Item Master
	 * @param key used to uniquely identify the Item Master
	 * @param filter used to identify which filter is to be used.
	 * @return
	 */
	public List<AuditRecord> getCasePackAuditInformation(ItemMasterKey key, String filter){
		List<AuditRecord> result = new ArrayList<>();
		List<AuditRecord> returnResult = new ArrayList<>();
		List<ItemMasterAudit> tableAudits =
				this.itemMasterAuditRepository.findByKeyItemCodeAndKeyItemTypeOrderByKeyChangedOn(
						key.getItemCode(), key.getItemType());
		result.addAll(this.auditComparisonImplementation.processClassFromList(tableAudits, filter));
		for (AuditRecord auditRecord : result) {

			if(auditRecord.getChangedTo() == NULL){
				auditRecord.setChangedTo(EMPTY);
			}
			if(key.getItemType().equals(DSD)){
				if(!auditRecord.getChangedFrom().equals(UNKNOWN)){
					returnResult.add(auditRecord);
				}
			}
			else{
				if(!auditRecord.getChangedFrom().equals(UNKNOWN) && !auditRecord.getAttributeName().equals(DISCONTINUED_BY)
						&& !auditRecord.getAttributeName().equals(DISCONTINUE_DATE)
						&& !auditRecord.getAttributeName().equals(DISCONTINUE_REASON)){
					returnResult.add(auditRecord);
				}

			}
		}
		// Sort result list
		returnResult.sort(Comparator.comparing(AuditRecord::getChangedOn));

		return returnResult;
	}

	/**
	 * Get all records of changes to the DRU attributes in an Item Master
	 * @param key used to uniquely identify the Item Master
	 * @param filter used to identify which filter is to be used.
	 * @return
	 */
	public List<AuditRecord> getDruAuditInformation(ItemMasterKey key, String filter){
		List<ItemMasterAudit> tableAudits =
				this.itemMasterAuditRepository.findByKeyItemCodeAndKeyItemTypeOrderByKeyChangedOn(
						key.getItemCode(), key.getItemType());
		return this.auditComparisonImplementation.processClassFromList(tableAudits, filter);
	}

	/**
	 * Get all records of changes to the Selling Unit attributes
	 * @param upc the selling units upc to get changes on.
	 * @param filter used to identify which filter is to be used.
	 * @return
	 */
	public List<AuditRecord> getSellingUnitAuditInformation(Long upc, String filter) {
		List<SellingUnitAudit> tableAudits =
				this.sellingUnitAuditRepository.findByKeyUpcOrderByKeyChangedOn(upc);
		List<RetailUnitOfMeasure> retailUnitOfMeasures =  this.retailUnitOfMeasureRepository.findAll();
		for (SellingUnitAudit rellingUnitAudit: tableAudits) {
			rellingUnitAudit.setRetailSellSizeCode1(this.getNameForRetailUnitOfMeasure(rellingUnitAudit.getRetailSellSizeCode1(),retailUnitOfMeasures));
		}

		return this.auditComparisonImplementation.processClassFromList(tableAudits, filter);
	}

	/**
	 * Get all records of changes to the Related Products attributes
	 * @param parentProdId the product ID to get changes on.
	 * @return
	 */
    public List<AuditRecordWithProdId> getRelatedProductsAuditInformation(Long parentProdId) {
		List<ProductRelationshipAudit> productRelationshipAudits =
				this.productRelationshipAuditRepository.findByKeyProductIdOrderByKeyChangedOn(parentProdId);

		List<AuditRecordWithProdId> result = getAuditRecordsWithProdIdForRelatedProductAudit(productRelationshipAudits);

		// get list of prodIds from productRelationshipAudits to search for goodsProductAudits
        HashSet<Long> relatedProdIds = getRelatedProdIds(productRelationshipAudits);

        // for each prodid get list of audits and calculate changes and add to result list
        // add good prod audit records to result
        for (Long relatedProdId: relatedProdIds) {
            List<GoodsProductAudit> goodsProductAudits =
                   this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(relatedProdId);

            List<AuditRecord> records = this.auditComparisonImplementation.processClassFromList(goodsProductAudits, FilterConstants.RELATED_PRODUCTS_AUDIT);
            for (AuditRecord record: records) {
               AuditRecordWithProdId newRecord= new AuditRecordWithProdId(record, relatedProdId);
               result.add(newRecord);
            }
        }

        // Sort result list
        result.sort(Comparator.comparing(AuditRecordWithProdId::getChangedOn));

        return result;
    }

    /**
     * Get all records of changes to the online attributes attributes
     * @param prodId the product ID to get changes on.
     * @return
     */
    public List<AuditRecord> getOnlineAttributesAuditInformation(Long prodId) {
        String filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT;

        List<AuditRecord> result = new ArrayList<>();

        // serving size information
		List<ProductDescriptionAudit> productDescriptionAudits =
				this.productDescriptionAuditRepository.findByKeyProductIdOrderByKeyChangedOn(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(productDescriptionAudits, filter));

        List<ProductMasterAudit> productMasterAudits =
                this.productMasterAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

        result.addAll(this.auditComparisonImplementation.processClassFromList(productMasterAudits, filter));

        List<GoodsProductAudit> goodsProductAudits =
                this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

        result.addAll(this.auditComparisonImplementation.processClassFromList(goodsProductAudits, filter));

		// Sort result list
		result.sort(Comparator.comparing(AuditRecord::getChangedOn));

        return result;
    }

    /**
     * Get list of unique product IDs extracted from list of ProductRelationshipAudit's
     * @param productRelationshipAudits - list of ProductRelationshipAudit's
     * @return -  list of unique product IDs
     */
    private HashSet<Long> getRelatedProdIds(List<ProductRelationshipAudit> productRelationshipAudits) {
        HashSet<Long> result = new HashSet<>();

        for (ProductRelationshipAudit productRelationshipAudit: productRelationshipAudits) {
            result.add(productRelationshipAudit.getKey().getRelatedProductId());
        }

        return result;
    }

    /**
     * Get all records of changes to the Related Product attributes
     * @param tableAudits - list of product relationship audit's
     * @return - list of audit records that include product IDs
     */
    private List<AuditRecordWithProdId> getAuditRecordsWithProdIdForRelatedProductAudit(List<ProductRelationshipAudit> tableAudits) {
		// map of user id -> user display names
		Map<String, String> mapUserNames = this.auditComparisonImplementation.mapUserIdToUserDisplayName(tableAudits);

		List<AuditRecordWithProdId> result = new ArrayList<>();

		// convert to Audit record with product ID
		for (ProductRelationshipAudit tableAudit : tableAudits) {
			AuditRecordWithProdId auditRecord = new AuditRecordWithProdId();
			auditRecord.setProdId(tableAudit.getKey().getRelatedProductId());
			auditRecord.setChangedFrom(N_A);
			String relationshipDisplayName = ProductRelationship.ProductRelationshipCode.getDescriptionByValue(tableAudit.getKey().getProductRelationshipCode());
			auditRecord.setChangedTo(relationshipDisplayName);
			auditRecord.setAction(tableAudit.getAction());
			auditRecord.setAttributeName(RELATIONSHIP_TYPE);
			auditRecord.setChangedBy(mapUserNames.get(tableAudit.getChangedBy()));
			auditRecord.setChangedOn(tableAudit.getChangedOn());

			result.add(auditRecord);
		}

		return result;
	}

	/**
	 * Get all records of changes to the tags and specs attributes
	 * @param prodId the product ID to get changes on.
	 * @return
	 */
	public List<AuditRecord> getTagsAndSpecsAuditInformation(Long prodId) {
		String filter = FilterConstants.TAGS_AND_SPECS_AUDIT;

		List<AuditRecord> result = new ArrayList<>();

		List<DynamicAttributeAudit> dynamicAttributeAudits =
				this.dynamicAttributeAuditRepository.findByKeyKeyAndKeyKeyTypeAndKeySourceSystemOrderByKeyAttributeId(prodId, PRODUCT_TYPE, DefaultSourceSystemCode);

		result.addAll(this.auditComparisonImplementation.processClassFromList(dynamicAttributeAudits, filter));

		return result;
	}

	/**
	 * Get all records of changes to the pharmacy attributes
	 * @param prodId the product ID to get changes on.
	 * @return
	 */
	public List<AuditRecord> getPharmacyAuditInformation(Long prodId) {
		String filter = FilterConstants.PHARMACY_AUDIT;

		List<AuditRecord> result = new ArrayList<>();

		// Grab all the changes from goods_prod.
        List<GoodsProductAudit> goodsProductAudits =
                this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);
        result.addAll(this.auditComparisonImplementation.processClassFromList(goodsProductAudits, filter));

        // Grab all the changes from rx_prod.
        List<RxProductAudit> rxProductAudits =
                this.rxProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);
        result.addAll(this.auditComparisonImplementation.processClassFromList(rxProductAudits, filter));

        // Grab all the changes from prod_scn_codes.
		List<SellingUnit> sellingUnits = this.sellingUnitRepository.findByProdId(prodId);
		for (SellingUnit s : sellingUnits) {
			List<SellingUnitAudit> sellingUnitAudits =
					this.sellingUnitAuditRepository.findByKeyUpcOrderByKeyChangedOn(s.getUpc());
			result.addAll(this.auditComparisonImplementation.processClassFromList(sellingUnitAudits, filter));
		}

		// Sort result list
		result.sort(Comparator.comparing(AuditRecord::getChangedOn));

		return result;
	}

    /**
     * Get all records of changes to the UPC Info Dimensions attributes
     * @param prodId the product ID to get changes on.
     * @return
     */
    public List<AuditRecord> getDimensionsAuditInformation(Long prodId) {
        String filter = FilterConstants.UPC_INFO_DIMENSIONS_AUDIT;

        List<AuditRecord> result = new ArrayList<>();

        List<GoodsProductAudit> goodsProductAudits =
                this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

        result.addAll(this.auditComparisonImplementation.processClassFromList(goodsProductAudits, filter));

        return result;
    }

	/**
	 * Get all records of changes to the shelf attributes
	 * @param prodId the product ID to get changes on.
	 * @return records of changes to the shelf attributes
	 */
    public List<AuditRecord> getShelfAttributesAuditInformation(Long prodId) {
		String filter = FilterConstants.SHELF_ATTRIBUTES_AUDIT;

		List<AuditRecord> result = new ArrayList<>();

		List<ProductMasterAudit> productMasterAudits =
				this.productMasterAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(productMasterAudits, filter));

		List<GoodsProductAudit> goodsProductAudits =
				this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(goodsProductAudits, filter));

		List<ProductDescriptionAudit> listToAdd = null;
		// Go through each type of description and pull out the changes
		// English Description
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.STANDARD.getId(), ProductDescription.ENGLISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// Spanish Description
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.STANDARD.getId(), ProductDescription.SPANISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// English CFD - 1
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_ONE.getId(), ProductDescription.ENGLISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// English CFD - 2
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_TWO.getId(), ProductDescription.ENGLISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// Spanish CFD - 1
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_ONE.getId(), ProductDescription.SPANISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// Spanish CFD - 2
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_TWO.getId(), ProductDescription.SPANISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// Get proposed service case sign
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.PROPOSED_SIGN_ROMANCE_COPY.getId(), ProductDescription.ENGLISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// Get approved service case sign
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.SIGN_ROMANCE_COPY.getId(), ProductDescription.ENGLISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// Proposed primo pick
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.PRIMO_PICK_LONG.getId(), ProductDescription.ENGLISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// approved primo pick
		listToAdd =	this.productDescriptionAuditRepository.getProductDescriptionAudits(
				DescriptionType.Codes.PRIMO_PICK_SHORT.getId(), ProductDescription.ENGLISH, prodId);
		result.addAll(this.auditComparisonImplementation.processClassFromList(listToAdd, filter));

		// Sort result list
		result.sort(Comparator.comparing(AuditRecord::getChangedOn));

		return result;
    }

	/**
	 * Prepend attribute name in audit record
	 * @param records to be prepended
	 * @param proposed string to prepend to attribute name
	 */
	private void prependAttributeName(List<AuditRecord> records, String proposed) {
		records.stream().forEach((record)-> {
			record.setAttributeName(proposed + " " + record.getAttributeName());
		});
	}

	/**
     * Get all records of changes to the product info attributes
     * @param prodId the product ID to get changes on.
     * @return records of changes to the product info attributes
     */
    public List<AuditRecord> getProductInfoAuditInformation(Long prodId) {
        String filter = FilterConstants.PRODUCT_INFO_AUDIT;

        List<AuditRecord> result = new ArrayList<>();

        List<ProductMasterAudit> productMasterAudits =
                this.productMasterAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

        result.addAll(this.auditComparisonImplementation.processClassFromList(productMasterAudits, filter));

        List<GoodsProductAudit> goodsProductAudits =
                this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

        result.addAll(this.auditComparisonImplementation.processClassFromList(goodsProductAudits, filter));

		// Sort result list
		result.sort(Comparator.comparing(AuditRecord::getChangedOn));

        return result;
    }

	/**
	 * Get all records of changes to the special attributes tobacco
	 * @param prodId the product ID to get changes on.
	 * @return records of changes to the special attributes tobacco
	 */
	public List<AuditRecord> getTobaccoAuditInformation(Long prodId) {
		String filter = FilterConstants.TOBACCO_AUDIT;

		List<AuditRecord> result = new ArrayList<>();

		List<GoodsProductAudit> goodsProductAudits =
				this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(goodsProductAudits, filter));

		List<TobaccoProductAudit> tobaccoProductAudits =
				this.tobaccoProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(tobaccoProductAudits, filter));

		// Sort result list
		result.sort(Comparator.comparing(AuditRecord::getChangedOn));

		return result;
	}

	/**
	 * Get all records of changes to the special attributes code date
	 * @param prodId the product ID to get changes on.
	 * @return records of changes to the special attributes code date
	 */
	public List<AuditRecord> getCodeDateAuditInformation(Long prodId) {
		String filter = FilterConstants.CODE_DATE_AUDIT;

		List<AuditRecord> result = new ArrayList<>();

		List<GoodsProductAudit> goodsProductAudits =
				this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(goodsProductAudits, filter));

		return result;
	}

	/**
	 * Get all records of changes to the special attributes shipping and handling
	 * @param prodId the product ID to get changes on.
	 * @return records of changes to the special attributes shipping and handling
	 */
	public List<AuditRecord> getShippingHandlingAuditInformation(Long prodId) {
		String filter = FilterConstants.SHIPPING_HANDLING_AUDIT;

		List<AuditRecord> result = new ArrayList<>();

		List<GoodsProductAudit> goodsProductAudits =
				this.goodsProductAuditRepository.findByKeyProdIdOrderByKeyChangedOn(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(goodsProductAudits, filter));

		List<ProductRestrictionsAudit> productRestrictionsAudits = getProductShippingRestrictionsAuditByProductId(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(productRestrictionsAudits, filter));

		// Sort result list
		result.sort(Comparator.comparing(AuditRecord::getChangedOn));

		return result;
	}

	/**
	 * Get all records of changes to the special attributes rating and restrictions
	 * @param prodId the product ID to get changes on.
	 * @return records of changes to the special attributes rating and restrictions
	 */
	public List<AuditRecord> getRatingRestrictionsAuditInformation(Long prodId) {
		String filter = FilterConstants.RATING_RESTRICTIONS_AUDIT;

		List<AuditRecord> result = new ArrayList<>();

		List<ProductRestrictionsAudit> productRestrictionsAudits = getProductNotShippingRestrictionsAuditByProductId(prodId);

		result.addAll(this.auditComparisonImplementation.processClassFromList(productRestrictionsAudits, filter));

		return result;
	}

	/**
	 * Gets product shipping audit restrictions by product id.
	 *
	 * @param prodId the prod id
	 * @return the product shipping audit restrictions by product id
	 */
	public List<ProductRestrictionsAudit> getProductShippingRestrictionsAuditByProductId(Long prodId) {
		return this.productRestrictionsAuditRepository.findByKeyProdIdAndRestrictionRestrictionGroupCode(prodId, SHIPPING_RESTRICTION_CODE);
	}

    /**
     * Gets product shipping audit restrictions by product id (non shipping).
     *
     * @param prodId the prod id
     * @return the product shipping audit restrictions by product id
     */
    public List<ProductRestrictionsAudit> getProductNotShippingRestrictionsAuditByProductId(Long prodId) {
        return this.productRestrictionsAuditRepository.findByKeyProdIdAndRestrictionRestrictionGroupCodeNot(prodId, SHIPPING_RESTRICTION_CODE);
    }

	/**
	 * Gets product case pack warehouse audit entries by product id.
	 *
	 * @param itemCode = item code to search by
	 * @param itemType - itemType to search by
	 * @return the audit records for case pack warehouse information
	 */

	public List<AuditRecord> getWarehouseAuditInformation(long itemCode, String itemType) {
		String filter = FilterConstants.WAREHOUSE_AUDIT;

		List<AuditRecord> result = new ArrayList<>();

		List<WarehouseLocationItemAudit> warehouseAudits =
				this.warehouseLocationItemAuditRepository.findByKeyItemCodeAndKeyItemType(itemCode, itemType);

		HashSet<Integer> warehouseIds = getWarehouseIds(warehouseAudits);

		// for each warehouse get the audits for it and process that batch together to get the changes over time
		warehouseIds.stream().forEach((warehouseId)-> {
			List<WarehouseLocationItemAudit> warehouseAuditsForOneWarehouse = getWarehouseAuditsForWarehouse(warehouseAudits, warehouseId);

			List<AuditRecord> records = this.auditComparisonImplementation.processClassFromList(warehouseAuditsForOneWarehouse, filter);
			records.stream().forEach((record)-> {
				result.add(new AuditRecordWithWarehouseId(record, warehouseId));
			});
		});

		result.sort(Comparator.comparing(AuditRecord::getChangedOn));

		return result;
	}

	/**
	 * Get the subset of WarehouseLocationItemAudit's for a particular warehouse
	 * @param warehouseId the warehouse to get subset of
	 * @return subset of WarehouseLocationItemAudit's
	 */
	private List<WarehouseLocationItemAudit> getWarehouseAuditsForWarehouse(List<WarehouseLocationItemAudit> audits, Integer warehouseId) {
			return audits.stream()
					.filter(audit -> audit.getKey().getWarehouseNumber() == warehouseId)
					.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Get list of unique Warehouse IDs extracted from list of WarehouseLocationItemAudit's
	 * @param warehouseLocationItemAudits - list of WarehouseLocationItemAudit's
	 * @return -  list of unique Warehouse IDs
	 */
	private HashSet<Integer> getWarehouseIds(List<WarehouseLocationItemAudit> warehouseLocationItemAudits) {
		HashSet<Integer> result = new HashSet<>();

		warehouseLocationItemAudits.stream().forEach((item)->result.add(item.getKey().getWarehouseNumber()));

		return result;
	}

	/**
	 * Get the list of eCommerce view audit by primaryUPC.
	 * @param primaryUPC - The primaryUPC
	 * @return List<AuditRecord> the list of eCommerce view audit.
	 */
	public List<AuditRecord> getECommerceViewAuditsInformation(Long primaryUPC) {
		List<AuditRecord> result = new ArrayList<>();
		result.addAll(this.getProductScanCodeExtentAudits(primaryUPC, PROD_EXT_DATA_CODE_ELONG));
		result.addAll(this.getProductScanCodeExtentAudits(primaryUPC, PROD_EXT_DATA_CODE_ESHRT));
		result.addAll(this.getMasterDataExtensionAudits(primaryUPC, LOG_ATTR_ID_SIZE));
		result.addAll(this.getMasterDataExtensionAudits(primaryUPC, LOG_ATTR_ID_BRAND));
		return result;
	}

	/**
	 * Get the list of eCommerce view audit in ProductScanCodeExtentAudit.
	 * @param primaryUPC      the primaryUPC.
	 * @param prodExtDataCode the product extent data code to find.
	 * @return List<AuditRecord> the list of eCommerce view audit.
	 */
	private List<AuditRecord>  getProductScanCodeExtentAudits (Long primaryUPC, String prodExtDataCode) {
		List<AuditRecord> auditRecords = new ArrayList<>();
		List<ProductScanCodeExtentAudit> lstAudits = new ArrayList<>();
		int totalRecord = this.productScanCodeExtentAuditRepositoryWithCount.countAllByKey_ScanCodeIdAndProdExtDataCode(primaryUPC, prodExtDataCode);
		if(totalRecord > 0){
			int mod = totalRecord%MAX_NUMBER_OF_RECORDS_PER_QUERY;
			int numberOfPages = totalRecord/MAX_NUMBER_OF_RECORDS_PER_QUERY + (mod > 0 ? 1 : 0);
			for (int i = 0; i < numberOfPages; i++){
				Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY);
				List<ProductScanCodeExtentAudit> productScanCodeExtentAudits =
						this.productScanCodeExtentAuditRepositoryWithCount.findByKey_ScanCodeIdAndProdExtDataCodeOrderByProdExtDataCodeDescKey_ChangedOnAsc(primaryUPC, prodExtDataCode, pageRequest);
				if(productScanCodeExtentAudits != null && !productScanCodeExtentAudits.isEmpty()){
					lstAudits.addAll(productScanCodeExtentAudits);
				}
			}
		}
		if(!lstAudits.isEmpty()){
			auditRecords.addAll(this.auditComparisonImplementation.processClassFromList(lstAudits, FilterConstants.PRODUCT_SCAN_CODE_EXTENT_AUDIT));
		}
		return auditRecords;
	}

	/**
	 * Get the list of eCommerce view audit in MasterDataExtensionAudit.
	 * @param primaryUPC      the primaryUPC.
	 * @param attributeId      the attributeId to find.
	 * @return List<AuditRecord> the list of eCommerce view audit.
	 */
	private List<AuditRecord>  getMasterDataExtensionAudits (Long primaryUPC, Long attributeId) {
		List<AuditRecord> auditRecords = new ArrayList<>();
		List<MasterDataExtensionAudit> lstAudits = new ArrayList<>();
		int totalRecord = this.masterDataExtensionAuditRepositoryWithCount.countAllByKey_KeyIdAndKey_DataSourceSystemAndKey_AttributeId(primaryUPC, DATA_SOURCE_SYSTEM_4,attributeId);
		if(totalRecord > 0){
			int mod = totalRecord%MAX_NUMBER_OF_RECORDS_PER_QUERY;
			int numberOfPages = totalRecord/MAX_NUMBER_OF_RECORDS_PER_QUERY + (mod > 0 ? 1 : 0);
			for (int i = 0; i < numberOfPages; i++){
				Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY);
				List<MasterDataExtensionAudit> masterDataExtensionAudits =
						this.masterDataExtensionAuditRepositoryWithCount.findAllByKey_KeyIdAndKey_DataSourceSystemAndKey_AttributeIdOrderByKey_AttributeIdDescKey_ChangedOnAsc(primaryUPC, DATA_SOURCE_SYSTEM_4,attributeId, pageRequest);
				if(masterDataExtensionAudits != null && !masterDataExtensionAudits.isEmpty()){
					lstAudits.addAll(masterDataExtensionAudits);
				}
			}
		}
		if(!lstAudits.isEmpty()){
			auditRecords.addAll(this.auditComparisonImplementation.processClassFromList(lstAudits, FilterConstants.MASTER_DATA_EXTENSION_EXTENDED_AUDIT));
		}
		return auditRecords;
	}
	/**
	 * Get published audit attributes
	 * @param prodId the product ID to get.
	 * @param upc the scan code to get.
	 * @return
	 * @author vn70633
	 */
	public List<AuditRecord> getPublishedAttributesAuditInformation(Long prodId, Long upc) {
		List<Long> attributeIds = null;
		List<AuditRecord> auditRecordTempOrgs = new ArrayList<>();
		List<AuditRecord> results = new ArrayList<>();
		List<Integer> dataSources = new ArrayList<>();
		dataSources.add(SOURCE_SYSTEM_13);
		dataSources.add(SOURCE_SYSTEM_4);
		this.integerPopulator.populate(dataSources, DEFAULT_ITEM_COUNT);
		List<DynamicAttributeAudit> productDynamicAttributeAudits =
				this.dynamicAttributeAuditRepository.findByKeyKeyAndKeyKeyTypeAndKeyAttributeIdNotAndKeySourceSystemInOrderByKeyChangedOn(prodId, PRODUCT_TYPE, LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.getValue().intValue(), dataSources);
		List<DynamicAttributeAudit> upcDynamicAttributeAudits =
				this.dynamicAttributeAuditRepository.findByKeyKeyAndKeyKeyTypeAndKeyAttributeIdNotAndKeySourceSystemInOrderByKeyChangedOn(upc, UPC,  LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.getValue().intValue(), dataSources);
		List<DynamicAttributeAudit> dynamicAttributeAudits = new ArrayList<>();
		if(!productDynamicAttributeAudits.isEmpty() && !upcDynamicAttributeAudits.isEmpty()){
			dynamicAttributeAudits.addAll(upcDynamicAttributeAudits);
			dynamicAttributeAudits.addAll(productDynamicAttributeAudits);
			dynamicAttributeAudits.sort((prodList, upcList) -> {
				if (prodList.getKey().getChangedOn().isBefore(upcList.getKey().getChangedOn())) {
					return 1;
				} else if (prodList.getKey().getChangedOn().isAfter(upcList.getKey().getChangedOn())) {
					return -1;
				} else {
					return 0;
				}
			});
		} else if(!productDynamicAttributeAudits.isEmpty()) {
			dynamicAttributeAudits.addAll(productDynamicAttributeAudits);
		} else if(!upcDynamicAttributeAudits.isEmpty()){
			dynamicAttributeAudits.addAll(upcDynamicAttributeAudits);
		}
		// map of user id -> user display names
		Map<String, String> mapUserNames = this.auditComparisonImplementation.mapUserIdToUserName(dynamicAttributeAudits);
        if(!dynamicAttributeAudits.isEmpty()){
			List<AuditRecord> auditRecordTemps = new ArrayList<AuditRecord>();
			this.convertDynamicAttrAudits2AuditRecords(dynamicAttributeAudits, auditRecordTempOrgs, mapUserNames);
			//Get tag and spec attribute
			attributeIds = getTagAndSpectAttrs(auditRecordTempOrgs, auditRecordTemps);
			//add attributes which not nutrient, spec, tag
			results.addAll(auditRecordTempOrgs);
			if (!attributeIds.isEmpty()) {
				List<AuditRecord> temps= setDataForTagAndSpecAttr(attributeIds, auditRecordTemps);
				results.addAll(temps);
			}
		}
		//get nutrient audit attribute
		List<Object[]> nutrientAudits = this.nutrientAuditRepository.getPublishedAttributesNutrientAudit(upc);
		if(!nutrientAudits.isEmpty()){
			convertObjectsToAuditRecords(nutrientAudits, results);
			}
		//get productPkVar audit attribute
		List<Object[]>  productPkVarAudits = this.productPkVarAuditRepository.getPublishedAttributesProductPkVarAudit( upc);
		if(!productPkVarAudits.isEmpty()){
			convertObjectsToAuditRecords(productPkVarAudits, results);
		}
		return results;
	}

	/**
	 * Get published audit attributes
	 * @param dynamicAttributeAudits list dynamicAttributeAudits to convert.
	 * @param results list audit records.
	 * @return
	 * @author vn70633
	 */
	private void convertDynamicAttrAudits2AuditRecords(List<DynamicAttributeAudit> dynamicAttributeAudits, List<AuditRecord> results, Map<String, String> mapUserNames){
		for(DynamicAttributeAudit dynamicAttributeAudit : dynamicAttributeAudits){
			AuditRecord auditRecord = new AuditRecord();
			auditRecord.setAttributeId(Long.valueOf(dynamicAttributeAudit.getKey().getAttributeId()));
			auditRecord.setAttributeName(attriNameValue(Long.valueOf(dynamicAttributeAudit.getKey().getAttributeId())));
			auditRecord.setAttributeValue(dynamicAttributeAudit.getTextValue());
			auditRecord.setAction(dynamicAttributeAudit.getAction());
			auditRecord.setChangedBy(mapUserNames.get(dynamicAttributeAudit.getChangedBy()));
			auditRecord.setChangedOn(dynamicAttributeAudit.getChangedOn());
			results.add(auditRecord);
		}
	}
	/**
	 * Get published audit attributes
	 * @param objects list objects to convert.
	 * @param results list audit records.
	 * @return
	 * @author vn70633
	 */
	private void convertObjectsToAuditRecords(List<Object[]> objects, List<AuditRecord> results){
		if(objects!=null){
			for(Object [] object : objects){
				AuditRecord auditRecord = new AuditRecord();
				auditRecord.setAttributeName(NUTRITION);
				auditRecord.setAttributeValue(MULTI_DIMENSIONAL);
				auditRecord.setAttributeId(LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.getValue());
				auditRecord.setAction(String.valueOf(object[1]));
				auditRecord.setChangedBy(String.valueOf(object[2]));
				auditRecord.setChangedOn((LocalDateTime)object[4]);
				results.add(auditRecord);
			}
		}
	}
	/**
	 * Gets the lst tag and spect attr.
	 * @param auditEcommerPublishedData
	 *            the lst audit ecommer published data
	 * @param specTagAttrs
	 *            the lst spec tag
	 * @return the lst tag and spect attr
	 * @author vn70633
	 */
	private List<Long> getTagAndSpectAttrs(List<AuditRecord> auditEcommerPublishedData, List<AuditRecord> specTagAttrs) {
			Set<Long> lstTmp = new HashSet<Long>();
			List<Long> publishAttr = createLstAuditAttribute();
			AuditRecord auditPublishedEcommer = null;
			for (int i = 0; i < auditEcommerPublishedData.size(); i++) {
				auditPublishedEcommer = auditEcommerPublishedData.get(i);
				if (!publishAttr.contains(Long.valueOf(auditPublishedEcommer.getAttributeId()))) {
					lstTmp.add(Long.valueOf(auditPublishedEcommer.getAttributeId()));
					specTagAttrs.add(auditPublishedEcommer);
					auditEcommerPublishedData.remove(i);
					i--;
				}
			}
			return new ArrayList<Long>(lstTmp);
		}
	/**
	 * Attri name value.
	 * @param attributeId
	 *            the attribute id
	 * @return the string
	 * @author vn70633
	 */
	private static String attriNameValue(Long attributeId) {
		String attriName = EMPTY;
		switch (LogicAttributeCode.getAttribute(attributeId)) {
			case BRAND_LOGIC_ATTRIBUTE_ID:
				attriName = STRING_BRAND;
				break;
			case DISPLAY_NAME_LOGIC_ATTRIBUTE_ID:
				attriName = AUDIT_ATTR_FOR_ECOMMERCE_VIEW_DISP_NAME;
				break;
			case SIZE_LOGIC_ATTRIBUTE_ID:
				attriName = SIZE;
				break;
			case ROMANCE_COPY_LOGIC_ATTRIBUTE_ID:
				attriName = AUDIT_ATTR_FOR_ECOMMERCE_VIEW_ROMANCE_COPY;
				break;
			case WARNING_LOGIC_ATTRIBUTE_ID:
				attriName = WARNINGS;
				break;
			case INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID:
				attriName = INGREDIENTS;
				break;
			case DIRECTIONS_LOGIC_ATTRIBUTE_ID:
				attriName = DIRECTIONS;
				break;
			case PDP_TEMPLATE_LOGIC_ATTRIBUTE_ID:
				attriName = PDP_TEPLATE_ID;
				break;
			case NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID:
				attriName = NUTRITION;
				break;
			default:
				attriName = EMPTY;
				break;
		}
		return attriName;
	}
	/**
	 * Creates the lst audit attribute.
	 * @return the list
	 * @author vn70633
	 */
	private List<Long> createLstAuditAttribute() {
		List<Long> lstPublishAtt = new ArrayList<Long>();
		lstPublishAtt.add(LogicAttributeCode.BRAND_LOGIC_ATTRIBUTE_ID.getValue());
		lstPublishAtt.add(LogicAttributeCode.DISPLAY_NAME_LOGIC_ATTRIBUTE_ID.getValue());
		lstPublishAtt.add(LogicAttributeCode.SIZE_LOGIC_ATTRIBUTE_ID.getValue());
		lstPublishAtt.add(LogicAttributeCode.ROMANCE_COPY_LOGIC_ATTRIBUTE_ID.getValue());
		lstPublishAtt.add(LogicAttributeCode.WARNING_LOGIC_ATTRIBUTE_ID.getValue());
		lstPublishAtt.add(LogicAttributeCode.INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID.getValue());
		lstPublishAtt.add(LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.getValue());
		lstPublishAtt.add(LogicAttributeCode.DIRECTIONS_LOGIC_ATTRIBUTE_ID.getValue());
		lstPublishAtt.add(LogicAttributeCode.PDP_TEMPLATE_LOGIC_ATTRIBUTE_ID.getValue());
		return lstPublishAtt;
	}
	/**
	 * setDataForTagAndSpecAttr.
	 * @author vn70633
	 * @param attributeIds
	 *            the lst attr ids
	 * @param audits
	 *            the lst audit
	 * @return data for tag and spec attrs
	 */
	public List<AuditRecord> setDataForTagAndSpecAttr(List<Long> attributeIds, List<AuditRecord> audits) {
		Set<AuditRecord> lstTmp = new HashSet<AuditRecord>();
		this.longPopulator.populate(attributeIds, DEFAULT_ITEM_COUNT);
            List<Attribute> lstGrpAudit = this.attributeRepository.findByAttributeIdIn(attributeIds);
			if (!lstGrpAudit.isEmpty()) {
				for (Attribute publishAttributeGroupVO : lstGrpAudit) {
					boolean change = false;
					if(publishAttributeGroupVO.getLogicalPhysicalRelationship() != null ){
						for (LogicalPhysicalRelationship logicalPhysicalRelationship : publishAttributeGroupVO.getLogicalPhysicalRelationship()) {
							if(logicalPhysicalRelationship.getRelationshipGroup() != null && logicalPhysicalRelationship.getRelationshipGroup().getRelationshipGroupId() > 0){
								changeDataForGroupAttr(publishAttributeGroupVO, logicalPhysicalRelationship, audits);
								change = true;
								break;
							}

						}
					}
					if(!change){
						changeDataForNormalAttr(publishAttributeGroupVO, audits);
					}
				}
				lstTmp = new HashSet<AuditRecord>(audits);
			}
		return new ArrayList<AuditRecord>(lstTmp);

	}

	/**
	 * changeDataForGroupAttr.
	 * @param atribute
	 *            the atribute
	 * @param logicalPhysicalRelationship
	 *            the logicalPhysicalRelationship
	 * @param audits
	 *            the audit list
	 * @author vn70633
	 */
	private void changeDataForGroupAttr(Attribute atribute, LogicalPhysicalRelationship logicalPhysicalRelationship, List<AuditRecord> audits) {
		for (AuditRecord auditRecord : audits) {
			if (auditRecord.getAttributeId() == atribute.getAttributeId()) {
				auditRecord.setAttributeId(Long.valueOf(logicalPhysicalRelationship.getRelationshipGroup().getRelationshipGroupId()));
				auditRecord.setAttributeName(logicalPhysicalRelationship.getRelationshipGroup().getRelationshipGroupDescription());
				auditRecord.setAttributeValue(MULTI_DIMENSIONAL);
			}
		}
	}
	/**
	 * changeDataForNormalAttr.
	 * @param attribute
	 *            the attribute
	 * @param audits
	 *            the audit list
	 * @author 70633
	 */
	private void changeDataForNormalAttr(Attribute attribute, List<AuditRecord> audits) {
		for (AuditRecord auditEcommerPublishedDataVO : audits) {
			if (auditEcommerPublishedDataVO.getAttributeId() == attribute.getAttributeId()) {
				auditEcommerPublishedDataVO.setAttributeName(attribute.getAttributeName());
				auditEcommerPublishedDataVO.setAttributeValue(MULTI_DIMENSIONAL);
			}
		}
	}
	/**
	 * Get published audit attributes
	 * @param prodId the product ID to get.
	 * @return
	 * @author vn70633
	 */
	public List<AuditRecord> getFulfillmentAttributesAuditInformation(Long prodId) {
		List<AuditRecord> results = new ArrayList<>();
		List<ProductFulfillmentChanelAudit> productFulfillmentChanelAudits =
						this.productFulfillmentChanelAuditRepository.findByKeyProductIdOrderByKeyChangedOn(prodId);
		// map of user id -> user display names
		Map<String, String> mapUserNames = this.auditComparisonImplementation.mapUserIdToUserName(productFulfillmentChanelAudits);
		if(!productFulfillmentChanelAudits.isEmpty()) {
			for (ProductFulfillmentChanelAudit productFulfillmentChanelAudit : productFulfillmentChanelAudits) {
				AuditRecord auditRecord = new AuditRecord();
				auditRecord.setFulfillmentProgram(productFulfillmentChanelAudit.getFulfillmentChannel().getDescription());

				auditRecord.setChangedBy(mapUserNames.get(productFulfillmentChanelAudit.getChangedBy()));
				auditRecord.setChangedOn(productFulfillmentChanelAudit.getChangedOn());
				if (auditRecord.getAction() != null && PURGE.equals(productFulfillmentChanelAudit.getAction())) {
					auditRecord.setStartDate(null);
					auditRecord.setEndDate(null);
				} else {
					auditRecord.setStartDate(productFulfillmentChanelAudit.getEffectDate());
					auditRecord.setEndDate(productFulfillmentChanelAudit.getExpirationDate());
				}
				auditRecord.setAction(productFulfillmentChanelAudit.getAction());
				results.add(auditRecord);
			}
		}
		return results;
	}

	/**
	 * Get the list of product group info audit by custProductGroupId.
	 * @param custProductGroupId - The custProductGroupId of Product Group.
	 * @return List<AuditRecord> the list of product group info audit.
	 */
	public List<AuditRecord> getProductGroupInfoAuditsInformation(Long custProductGroupId, String salesChannel, String hierCntxtCd) {
		List<AuditRecord> results = new ArrayList<>();
		results.addAll(this.getCustProductGroupAudits(custProductGroupId));
		results.addAll(this.getCustProductGroupMembershipAudits(custProductGroupId));
		results.addAll(this.getCustProductGroupHierarchyAudits(custProductGroupId,hierCntxtCd));
		results.addAll(this.getCustProductOnlineAudits(custProductGroupId, salesChannel));
		return results;
	}

	/**
	 * Get the list of product group info audit by custProductGroupId.
	 * @param custProductGroupId - The custProductGroupId of Product Group.
	 * @return List<AuditRecord> the list of product group info audit.
	 */
	private List<AuditRecord>  getCustProductGroupAudits (Long custProductGroupId) {
		List<AuditRecord> auditRecords = new ArrayList<>();
		List<CustomerProductGroupAudit> lstAudits = new ArrayList<>();
		for (int i = 0; ; i++){
			Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY, CustomerProductGroupAudit.getDefaultSort());
			Page<CustomerProductGroupAudit> data =
					this.customerProductGroupAuditRepositoryWithCount.findByKeyCustProductGroupId(custProductGroupId, pageRequest);
			if(data != null){
				lstAudits.addAll(data.getContent());
			}
			if(i>=data.getTotalPages()-1){
				break;
			}
		}
		if(!lstAudits.isEmpty()){
			auditRecords.addAll(this.auditComparisonImplementation.processClassFromList(lstAudits, AuditableField.NOT_APPLICABLE));
		}
		return auditRecords;
	}

	/**
	 * Get the list of product group membership audit by custProductGroupId.
	 * @param custProductGroupId - The custProductGroupId of Product Group.
	 * @return List<AuditRecord> the list of product group membership audit.
	 */
	private List<AuditRecord>  getCustProductGroupMembershipAudits (Long custProductGroupId) {
		List<AuditRecord> auditRecords = new ArrayList<>();
		List<CustomerProductGroupMembershipAudit> lstAudits = new ArrayList<>();
		for (int i = 0; ; i++){
			Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY, CustomerProductGroupMembershipAudit.getDefaultSort());
			Page<CustomerProductGroupMembershipAudit> data =
					this.custProductGroupMembershipAuditRepositoryWithCount.findByKeyCustProductGroupId(custProductGroupId, pageRequest);
			if(data != null){
				lstAudits.addAll(data.getContent());
			}
			if(i>=data.getTotalPages()-1){
				break;
			}
		}
		if(!lstAudits.isEmpty()){
			this.convertProductGroupMembershipAudits2AuditRecords(lstAudits,auditRecords);
		}
		return auditRecords;
	}

	/**
	 * Get the list of customer product group hierarchy audit by custProductGroupId.
	 * @param custProductGroupId - The custProductGroupId of Product Group.
	 * @return List<AuditRecord> the list of customer product group hierarchy audit.
	 */
	private List<AuditRecord>  getCustProductGroupHierarchyAudits (Long custProductGroupId, String hierCntxtCd) {
		List<AuditRecord> auditRecords = new ArrayList<>();
		List<GenericEntityRelationshipAudit> lstAudits = new ArrayList<>();
		for (int i = 0; ; i++){
			Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY, GenericEntityRelationshipAudit.getDefaultSort());
			Page<GenericEntityRelationshipAudit> data =
					this.genericEntityRelationshipAuditRepositoryWithCount.findAllByGenericChildEntityDisplayNumberAndKeyHierarchyContext(custProductGroupId, hierCntxtCd, pageRequest);
			if(data != null){
				lstAudits.addAll(data.getContent());
			}
			if(i>=data.getTotalPages()-1){
				break;
			}
		}
		if(!lstAudits.isEmpty()){
			this.convertProductGroupHierarchyAudits2AuditRecords(lstAudits, auditRecords);
		}
		return auditRecords;
	}

	/**
	 * Get the list of product online audit by custProductGroupId and saleChannel.
	 * @param custProductGroupId - The custProductGroupId of Product Group.
	 * @param salesChannel - The Sale chanel code.
	 * @return List<AuditRecord> the list of product online audit.
	 */
	private List<AuditRecord>  getCustProductOnlineAudits (Long custProductGroupId, String salesChannel) {
		List<AuditRecord> auditRecords = new ArrayList<>();
		List<ProductOnlineAudit> lstAudits = new ArrayList<>();
		for (int i = 0; ; i++){
			Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY, CustomerProductGroupAudit.getDefaultSort());
			Page<ProductOnlineAudit> data =
					this.productOnlineAuditRepository.findByKeyProductIdAndKeySalesChannelCodeOrKeyProductIdAndKeySalesChannelCode(custProductGroupId, salesChannel.trim(), custProductGroupId, salesChannel, pageRequest);
			if(data != null){
				lstAudits.addAll(data.getContent());
			}
			if(i>=data.getTotalPages()-1){
				break;
			}
		}
		if(!lstAudits.isEmpty()){
			for (ProductOnlineAudit productOnlineAudit:lstAudits) {
				if (productOnlineAudit.isShowOnSite()) {
					lstAudits.remove(productOnlineAudit);
				}
			}
			auditRecords.addAll(this.auditComparisonImplementation.processClassFromList(lstAudits, AuditableField.NOT_APPLICABLE));
		}
		return auditRecords;
	}
	/**
	 * Convert list of ProductGroupMembershipAudit to list of AuditRecord
	 * @param customerProductGroupMembershipAudits the list of ProductGroupMembershipAudit
	 * @param results the list of AuditRecord
	 */
	private void convertProductGroupMembershipAudits2AuditRecords(List<CustomerProductGroupMembershipAudit> customerProductGroupMembershipAudits, List<AuditRecord> results){
		for(CustomerProductGroupMembershipAudit customerProductGroupMembershipAudit : customerProductGroupMembershipAudits){
			AuditRecord auditRecord = new AuditRecord();
			auditRecord.setAttributeName(PRODUCT_GROUP_MEMBERSHIP_ATTRIBUTE_NAME);
			auditRecord.setAttributeValue(customerProductGroupMembershipAudit.getKey().getProdId().toString());
			auditRecord.setAction(customerProductGroupMembershipAudit.getAction());
			auditRecord.setChangedBy(customerProductGroupMembershipAudit.getChangedBy());
			auditRecord.setChangedOn(customerProductGroupMembershipAudit.getChangedOn());
			results.add(auditRecord);
		}
	}

	/**
	 * Convert list of ProductGroupHierarchyAudit to list of AuditRecord
	 * @param genericEntityRelationshipAudits the list of ProductGroupHierarchyAudit
	 * @param results the list of AuditRecord
	 */
	private void convertProductGroupHierarchyAudits2AuditRecords(List<GenericEntityRelationshipAudit> genericEntityRelationshipAudits, List<AuditRecord> results){
		for(GenericEntityRelationshipAudit genericEntityRelationshipAudit : genericEntityRelationshipAudits){
			Set<String> userIds = new HashSet<>();
			userIds.add(genericEntityRelationshipAudit.getChangedBy());
			List<User> userList = getUserInformation(userIds);
			AuditRecord auditRecord = new AuditRecord();
			auditRecord.setAttributeName(PRODUCT_GROUP_HIERARCHY_ATTRIBUTE_NAME);
			auditRecord.setAttributeValue(genericEntityRelationshipAudit.getParentDescription().getLongDescription());
			auditRecord.setAction(genericEntityRelationshipAudit.getAction());
			if(genericEntityRelationshipAudit.getAction().trim().equals(AuditRecord.ActionCodes.UPDT.toString())){
				if(genericEntityRelationshipAudit.getDefaultParent()){
					auditRecord.setChangedFrom(ALTEMATE_PATH);
				}else {
					auditRecord.setChangedFrom(PRIMARY_PATH);
				}
			}
			if(genericEntityRelationshipAudit.getDefaultParent()){
				auditRecord.setChangedTo(PRIMARY_PATH);
			}else {
				auditRecord.setChangedTo(ALTEMATE_PATH);
			}
			if (userList != null){
				auditRecord.setChangedBy(this.getUserDisplayName(userList.get(0),genericEntityRelationshipAudit.getChangedBy()));
			}
			else {
				auditRecord.setChangedBy(this.getUserDisplayName(null,genericEntityRelationshipAudit.getChangedBy()));
			}
			auditRecord.setChangedOn(genericEntityRelationshipAudit.getChangedOn());
			results.add(auditRecord);
		}
	}

	/**
	 * Get break pack audit attributes
	 * @param prodId the product ID to get.
	 * @return
	 * @author vn70633
	 */
	public List<AuditRecord> getBreakPackAttributesAuditInformation(Long prodId) {
		Map<String, Map<String, ProductRelationshipAudit>> auditAttributesByKey = new HashMap<String, Map<String, ProductRelationshipAudit>>();
		List<ProductRelationshipAudit> productRelationshipAudits = productRelationshipAuditRepository.findByKeyProductRelationshipCodeAndKeyRelatedProductIdOrderByKeyChangedOnDesc(PROD_RLSHP_CD, prodId);
		List<AuditRecord> results = new ArrayList<>();
		Map<String, String> mapUserNames = this.auditComparisonImplementation.mapUserIdToUserName(productRelationshipAudits);
		if(productRelationshipAudits != null && productRelationshipAudits.size() > 0){
			this.generalAuditAttributesNoUpcKey(productRelationshipAudits, results, mapUserNames, auditAttributesByKey);
			this.generalAuditAttributesWithUpcKey(productRelationshipAudits, results, mapUserNames);
		}
		if(results != null && results.size() > 0){
			this.setNAForAuditChangeFromAndUPCBlank(results);
		}
		return results;
	}

	/**
	 * General audit attributes no upc key
	 * @param productRelationshipAudits list dynamicAttributeAudits to convert.
	 * @param results list audit attribute
	 * @param mapUserNames the user name map.
	 * @param auditAttributesByKey
	 * @return
	 * @author vn70633
	 */
	private void generalAuditAttributesNoUpcKey(List<ProductRelationshipAudit> productRelationshipAudits, List<AuditRecord> results, Map<String, String> mapUserNames, Map<String, Map<String, ProductRelationshipAudit>> auditAttributesByKey){
		for(ProductRelationshipAudit productRelationshipAudit : productRelationshipAudits){
			String upc = String.valueOf(productRelationshipAudit.getParentProduct().getProductPrimaryScanCodeId());
			if(auditAttributesByKey.get(upc) != null
					&& auditAttributesByKey.get(upc).get(SELLING_UNITS) != null) {
				ProductRelationshipAudit auditAttributePr = auditAttributesByKey.get(upc).get(SELLING_UNITS);

				if (auditAttributePr != null) {
					AuditRecord auditRecord = new AuditRecord();
					auditRecord.setAttributeName(SELLING_UNITS);
					auditRecord.setChangedTo(String.valueOf(auditAttributePr.getProductQuantity()));
					auditRecord.setChangedOn(auditAttributePr.getChangedOn());
					auditRecord.setChangedBy(mapUserNames.get(auditAttributePr.getChangedBy()));
					auditRecord.setAction(auditAttributePr.getAction());
					auditRecord.setUpcText(String.valueOf(auditAttributePr.getParentProduct().getProductPrimaryScanCodeId()));
					auditRecord.setProductIdText(String.valueOf(auditAttributePr.getKey().getRelatedProductId()));
					String changeFromValue = String.valueOf(productRelationshipAudit.getProductQuantity());
					if (!changeFromValue.equals(auditRecord.getChangedTo())) {
						auditRecord.setChangedFrom(changeFromValue);
						if (auditAttributePr.getAction().trim().equals(AuditRecord.ActionCodes.PURGE.toString())) {
							auditRecord.setChangedFrom(auditRecord.getChangedTo());
							auditRecord.setChangedTo(EMPTY);
						}
						if (!auditAttributePr.getAction().trim().equals(AuditRecord.ActionCodes.ADD.toString())) {
							results.add(auditRecord);
						}
					}
				}

			}
			//save pre record
			if (auditAttributesByKey.get(upc) != null) {
				auditAttributesByKey.get(upc).put(SELLING_UNITS, productRelationshipAudit);
			} else {
				Map<String, ProductRelationshipAudit> auditAttributesVOsBk = new HashMap<String, ProductRelationshipAudit>();
				auditAttributesVOsBk.put(SELLING_UNITS, productRelationshipAudit);
				auditAttributesByKey.put(upc, auditAttributesVOsBk);
			}
		}

	}

	/**
	 * Get published audit attributes
	 * @param productRelationshipAudits list dynamicAttributeAudits to convert.
	 * @param results list audit records.
	 * @param mapUserNames the user name map.
	 * @return
	 * @author vn70633
	 */
	private void generalAuditAttributesWithUpcKey(List<ProductRelationshipAudit> productRelationshipAudits, List<AuditRecord> results, Map<String, String> mapUserNames){
		for(ProductRelationshipAudit productRelationshipAudit : productRelationshipAudits){
			AuditRecord auditRecord = new AuditRecord();
			auditRecord.setAttributeName(UPC);
			auditRecord.setChangedOn(productRelationshipAudit.getChangedOn());
			auditRecord.setChangedBy(mapUserNames.get(productRelationshipAudit.getChangedBy()));
			auditRecord.setAction(productRelationshipAudit.getAction());
			auditRecord.setProductIdText(String.valueOf(productRelationshipAudit.getKey().getRelatedProductId()));
			if(productRelationshipAudit.getAction().trim().equals(AuditRecord.ActionCodes.ADD.toString())){
				auditRecord.setChangedFrom(EMPTY);
				auditRecord.setChangedTo(String.valueOf(productRelationshipAudit.getParentProduct().getProductPrimaryScanCodeId()));
				results.add(auditRecord);
			}
			else if(productRelationshipAudit.getActionCode().trim().equals(AuditRecord.ActionCodes.PURGE.toString())){
				auditRecord.setChangedFrom(String.valueOf(productRelationshipAudit.getParentProduct().getProductPrimaryScanCodeId()));
				auditRecord.setChangedTo(EMPTY);
				results.add(auditRecord);
			}
		}

	}

	/**
	 * Set NA for audit change from and UPC blank
	 * @param auditAttributes list audit records.
	 * @return
	 * @author vn70633
	 */
	private void setNAForAuditChangeFromAndUPCBlank(List<AuditRecord> auditAttributes){
		for(AuditRecord auditAttribute : auditAttributes){
			if(auditAttribute.getUpcText() == null || auditAttribute.getUpcText().trim().equals(EMPTY)){
				auditAttribute.setUpcText(RESULT_NA);
				auditAttribute.setProductIdText(EMPTY);
			}
			if(auditAttribute.getChangedFrom() == null || auditAttribute.getChangedFrom().trim().equals(EMPTY)){
				auditAttribute.setChangedFrom(RESULT_NA);
			}
		}
	}

	/**
	 * Get sub-commodity audit.
	 * @param subCommodityCode the sub-commodity code.
	 * @return List<AuditRecord>
	 * @author vn70529
	 */
	public List<AuditRecord> getSubCommodityDefaultsAudits (Integer subCommodityCode) {
		SubCommodityAuditImpl subCommodityAuditImpl = new SubCommodityAuditImpl();
		List<AuditRecord> auditRecords = new ArrayList<>();
		List<SubCommodityAudit> subCommodityAudits =
				this.subCommodityAuditRepository.findByKey_SubCommodityCodeOrderByLstUpdtTsDesc(subCommodityCode);
		if(!subCommodityAudits.isEmpty()){
			Collections.reverse(subCommodityAudits);
			List<VertexTaxCategory> vertexTaxCategories = new ArrayList<VertexTaxCategory>();
			vertexTaxCategories.addAll(this.taxCategoryService.fetchAllTaxCategories());
			if(vertexTaxCategories != null && vertexTaxCategories.size() > 0){
				this.setNameForListTax_NonTaxCategory(subCommodityAudits, vertexTaxCategories);
			}
			auditRecords.addAll(subCommodityAuditImpl.processClassFromList(subCommodityAudits, FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT));
		}

		List<ProductPreferredUnitOfMeasureAudit>  productPreferredUnitOfMeasureAudits = this.getProductPreferredUnitOfMeasureAudits(subCommodityCode);
		if(productPreferredUnitOfMeasureAudits != null){
			auditRecords.addAll(subCommodityAuditImpl.processClassFromList(productPreferredUnitOfMeasureAudits, FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT));
		}

		List<SubCommodityStateWarningAudit>  subCommodityStateWarningAudits = this.getSubCommodityStateWarningAudits(subCommodityCode);
		if(subCommodityStateWarningAudits != null) {
			auditRecords.addAll(subCommodityAuditImpl.processClassFromList(subCommodityStateWarningAudits, FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT));
		}

		List<SellingRestrictionHierarchyLevelAudit>  sellingRestrictionHierarchyLevelAudits = this.getSellingRestrictionHierarchyLevelAudits(subCommodityCode);
		if(sellingRestrictionHierarchyLevelAudits != null) {
			auditRecords.addAll(subCommodityAuditImpl.processClassFromList(sellingRestrictionHierarchyLevelAudits, FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT));
		}

		List<ShippingRestrictionHierarchyLevelAudit>  shippingRestrictionHierarchyLevelAudits = this.getShippingRestrictionHierarchyLevelAudits(subCommodityCode);
		if(shippingRestrictionHierarchyLevelAudits != null) {
			auditRecords.addAll(subCommodityAuditImpl.processClassFromList(shippingRestrictionHierarchyLevelAudits, FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT));
		}

		return auditRecords;
	}

	/**
	 * Gets product preferred unit of measure audit by sub commodity code.
	 * @param subCommodityCode the sub commodity code.
	 * @return List<ProductPreferredUnitOfMeasureAudit>
	 */
	public List<ProductPreferredUnitOfMeasureAudit> getProductPreferredUnitOfMeasureAudits(Integer subCommodityCode) {
		List<ProductPreferredUnitOfMeasureAudit>  productPreferredUnitOfMeasureAuditListAll = new ArrayList<>();
		int totalRecord = this.productPreferredUnitOfMeasureAuditRepositoryWithCount.countAllByKeySubCommodityCode(subCommodityCode);
		if(totalRecord > 0){
			int mod = totalRecord%MAX_NUMBER_OF_RECORDS_PER_QUERY;
			int numberOfPages = totalRecord/MAX_NUMBER_OF_RECORDS_PER_QUERY + (mod > 0 ? 1 : 0);
			for (int i = 0; i < numberOfPages; i++){
				Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY);
				Page<ProductPreferredUnitOfMeasureAudit>  productPreferredUnitOfMeasureAudits =
						this.productPreferredUnitOfMeasureAuditRepositoryWithCount.findByKeySubCommodityCodeOrderByRetailUnitOfMeasureCodeAscKeyChangedOnAsc(subCommodityCode, pageRequest);
				if(productPreferredUnitOfMeasureAudits != null){
					productPreferredUnitOfMeasureAuditListAll.addAll(productPreferredUnitOfMeasureAudits.getContent());
				}
			}
		}
		return productPreferredUnitOfMeasureAuditListAll;
	}

	/**
	 * Gets sub commodity state warning audit by sub commodity code.
	 * @param subCommodityCode the sub commodity code.
	 * @return List<SubCommodityStateWarningAudit>
	 */
	public List<SubCommodityStateWarningAudit> getSubCommodityStateWarningAudits(Integer subCommodityCode) {
		List<SubCommodityStateWarningAudit>  subCommodityStateWarningAuditListAll = new ArrayList<>();
		int totalRecord = this.subCommodityStateWarningAuditRepositoryWithCounts.countAllByKeySubCommodityCode(subCommodityCode);
		if(totalRecord > 0){
			int mod = totalRecord%MAX_NUMBER_OF_RECORDS_PER_QUERY;
			int numberOfPages = totalRecord/MAX_NUMBER_OF_RECORDS_PER_QUERY + (mod > 0 ? 1 : 0);
			for (int i = 0; i < numberOfPages; i++){
				Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY);
				Page<SubCommodityStateWarningAudit>  subCommodityStateWarningAudits =
						this.subCommodityStateWarningAuditRepositoryWithCounts.findByKeySubCommodityCodeOrderByKeyChangedOnAsc(subCommodityCode, pageRequest);
				if(subCommodityStateWarningAudits != null){
					subCommodityStateWarningAuditListAll.addAll(subCommodityStateWarningAudits.getContent());
				}
			}
		}
		return subCommodityStateWarningAuditListAll;
	}

	/**
	 * Gets selling restriction hierarchy level audit by sub commodity code.
	 * @param subCommodityCode the sub commodity code.
	 * @return List<SellingRestrictionHierarchyLevelAudit>
	 */
	public List<SellingRestrictionHierarchyLevelAudit> getSellingRestrictionHierarchyLevelAudits(Integer subCommodityCode) {
		List<SellingRestrictionHierarchyLevelAudit>  sellingRestrictionHierarchyLevelAuditListAll = new ArrayList<>();
		int totalRecord = this.sellingRestrictionHierarchyLevelAuditRepositoryWithCounts.countAllByKeySubCommodity(subCommodityCode);
		if(totalRecord > 0){
			int mod = totalRecord%MAX_NUMBER_OF_RECORDS_PER_QUERY;
			int numberOfPages = totalRecord/MAX_NUMBER_OF_RECORDS_PER_QUERY + (mod > 0 ? 1 : 0);
			for (int i = 0; i < numberOfPages; i++){
				Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY);
				Page<SellingRestrictionHierarchyLevelAudit>  sellingRestrictionHierarchyLevelAudits =
						this.sellingRestrictionHierarchyLevelAuditRepositoryWithCounts.findByKeySubCommodityOrderByKeyChangedOn(subCommodityCode, pageRequest);
				if(sellingRestrictionHierarchyLevelAudits != null){
					sellingRestrictionHierarchyLevelAuditListAll.addAll(sellingRestrictionHierarchyLevelAudits.getContent());
				}
			}
		}
		return sellingRestrictionHierarchyLevelAuditListAll;
	}

	/**
	 * Gets shipping restriction hierarchy level audit by sub commodity code.
	 * @param subCommodityCode the sub commodity code.
	 * @return List<ShippingRestrictionHierarchyLevelAudit>
	 */
	public List<ShippingRestrictionHierarchyLevelAudit> getShippingRestrictionHierarchyLevelAudits(Integer subCommodityCode) {
		List<ShippingRestrictionHierarchyLevelAudit>  shippingRestrictionHierarchyLevelAuditListAll = new ArrayList<>();
		int totalRecord = this.shippingRestrictionHierarchyLevelAuditRepositoryWithCounts.countAllByKeySubCommodity(subCommodityCode);
		if(totalRecord > 0){
			int mod = totalRecord%MAX_NUMBER_OF_RECORDS_PER_QUERY;
			int numberOfPages = totalRecord/MAX_NUMBER_OF_RECORDS_PER_QUERY + (mod > 0 ? 1 : 0);
			for (int i = 0; i < numberOfPages; i++){
				Pageable pageRequest = new PageRequest(i, MAX_NUMBER_OF_RECORDS_PER_QUERY);
				Page<ShippingRestrictionHierarchyLevelAudit>  shippingRestrictionHierarchyLevelAudits =
						this.shippingRestrictionHierarchyLevelAuditRepositoryWithCounts.findByKeySubCommodityOrderByKeyRestrictionCodeAscKeyChangedOnAsc(subCommodityCode, pageRequest);
				if(shippingRestrictionHierarchyLevelAudits != null){
					shippingRestrictionHierarchyLevelAuditListAll.addAll(shippingRestrictionHierarchyLevelAudits.getContent());
				}
			}
		}
		return shippingRestrictionHierarchyLevelAuditListAll;
	}

	/**
	 * Set name display for tax and non tax category in list sub-commodity audit.
	 * @param subCommodityAudits the list of sub-commodity audit.
	 * @param vertexTaxCategories the list of vertex tax category.
	 * @author vn70529
	 */
	private static void setNameForListTax_NonTaxCategory(List<SubCommodityAudit> subCommodityAudits, List<VertexTaxCategory> vertexTaxCategories) {
		for (SubCommodityAudit subCommodityAudit : subCommodityAudits) {
			if(StringUtils.isNotEmpty(subCommodityAudit.getTaxCategoryCode())){
				subCommodityAudit.setTaxCategoryCode(setNameForCodeTableSubCommodityAud(subCommodityAudit.getTaxCategoryCode(), vertexTaxCategories));
			}
			if(StringUtils.isNotEmpty(subCommodityAudit.getNonTaxCategoryCode())){
				subCommodityAudit.setNonTaxCategoryCode(setNameForCodeTableSubCommodityAud(subCommodityAudit.getNonTaxCategoryCode(), vertexTaxCategories));
			}
		}
	}

	/**
	 * Set name for code table sub-commodity audit.
	 * @param key the key of tax or non tax category.
	 * @param vertexTaxCategories the list of vertex tax category.
	 * @return String the name vertex tax category.
	 * @author vn70529
	 */
	private static String setNameForCodeTableSubCommodityAud(String key, List<VertexTaxCategory> vertexTaxCategories){
		String name = key;
		for (VertexTaxCategory vertexTaxCategory : vertexTaxCategories) {
			if(vertexTaxCategory.getDvrCode().equals(key.trim())){
				name = vertexTaxCategory.getDisplayName();
				break;
			}
		}
		return name;
	}

	/**
	 * Get name display for retail unit of measure code.
	 * @param retailSellSizeCode the retailSellSizeCode.
	 * @param retailUnitOfMeasures the list of RetailUnitOfMeasure.
	 * @return String
	 */
	public static String getNameForRetailUnitOfMeasure(String retailSellSizeCode, List<RetailUnitOfMeasure> retailUnitOfMeasures) {
		if (StringUtils.isNotEmpty(retailSellSizeCode)) {
			for (RetailUnitOfMeasure retailUnitOfMeasure : retailUnitOfMeasures) {
				if (retailSellSizeCode.trim().equals(retailUnitOfMeasure.getId())) {
					return retailUnitOfMeasure.getDisplayName();
				}
			}
		}
		return retailSellSizeCode;
	}

	/**
	 * Get all records of changes to the case pack department attributes
	 * @param  itemCode, itemType
	 * @return List of AuditRecord
	 */
	public List<AuditRecord> getCasePackDepartmentAudit(Long itemCode, String itemType) {
		String filter = FilterConstants.CASE_PACK_DEPARTMENT_AUDIT;
		List<AuditRecord> result = new ArrayList<>();
		List<AuditRecord> returnResult = new ArrayList<>();

		List<ItemMasterAudit> itemMasterAudits =
				this.itemMasterAuditRepository.findByKeyItemCodeAndKeyItemTypeOrderByKeyChangedOn(itemCode, itemType);
		this.setChangedValueForDepartmentAudit(itemMasterAudits);
		List<StoreDepartmentAudit> subDepartments =
				this.storeDepartmentAuditRepository.findAll();
		this.setNameForListDepartment(itemMasterAudits, subDepartments);
		if(itemMasterAudits != null && itemMasterAudits.size() > 0){
			result.addAll(this.auditComparisonImplementation.processClassFromList(itemMasterAudits, filter));
		}
		for (AuditRecord auditRecord : result) {
			if(auditRecord.getChangedFrom() == NULL){
				auditRecord.setChangedFrom(EMPTY);
			}
			if(auditRecord.getChangedTo() == NULL){
				auditRecord.setChangedTo(EMPTY);
			}
			if(!auditRecord.getChangedFrom().equals(UNKNOWN)){
				returnResult.add(auditRecord);
			}
		}
		// Sort result list
		returnResult.sort(Comparator.comparing(AuditRecord::getChangedOn));
		return returnResult;
	}

	/**
	 * setChangedValueForDepartmentAudit.
	 * @param itemMasterAudits
	 *
	 * @author vn70633
	 */
	public static void setChangedValueForDepartmentAudit(List<ItemMasterAudit> itemMasterAudits) {
		for (ItemMasterAudit itemMasterAudit : itemMasterAudits) {
			if (itemMasterAudit.getDepartmentId1() != null && !STRING_0.equals(itemMasterAudit.getDepartmentId1())) {
				itemMasterAudit.setDepartmentId1(getDeptIdAsString(itemMasterAudit.getDepartmentId1()) + itemMasterAudit.getSubDepartmentId1().trim());
				itemMasterAudit.setDepartmentMerchandise1(getNameForDeptMdseTyp(itemMasterAudit.getDepartmentMerchandise1()));
			} else {
				itemMasterAudit.setDepartmentId1(itemMasterAudit.getDepartmentId1());
				itemMasterAudit.setDepartmentMerchandise1(getNameForDeptMdseTyp(itemMasterAudit.getDepartmentMerchandise1()));
			}
			if (itemMasterAudit.getDepartmentId2() != null && !STRING_0.equals(itemMasterAudit.getDepartmentId2())) {
				itemMasterAudit.setDepartmentId2(getDeptIdAsString(itemMasterAudit.getDepartmentId2()) + itemMasterAudit.getSubDepartmentId2().trim());
				itemMasterAudit.setDepartmentMerchandise2(getNameForDeptMdseTyp(itemMasterAudit.getDepartmentMerchandise2()));
			} else {
				itemMasterAudit.setDepartmentId2(itemMasterAudit.getDepartmentId2());
				itemMasterAudit.setDepartmentMerchandise2(getNameForDeptMdseTyp(itemMasterAudit.getDepartmentMerchandise2()));
			}
			if (itemMasterAudit.getDepartmentId3() != null && !STRING_0.equals(itemMasterAudit.getDepartmentId3())) {
				itemMasterAudit.setDepartmentId3(getDeptIdAsString(itemMasterAudit.getDepartmentId3()) + itemMasterAudit.getSubDepartmentId3().trim());
				itemMasterAudit.setDepartmentMerchandise3(getNameForDeptMdseTyp(itemMasterAudit.getDepartmentMerchandise3()));
			} else {
				itemMasterAudit.setDepartmentId3(itemMasterAudit.getDepartmentId3());
				itemMasterAudit.setDepartmentMerchandise3(getNameForDeptMdseTyp(itemMasterAudit.getDepartmentMerchandise3()));
			}
			if (itemMasterAudit.getDepartmentId4() != null && !STRING_0.equals(itemMasterAudit.getDepartmentId4())) {
				itemMasterAudit.setDepartmentId4(getDeptIdAsString(itemMasterAudit.getDepartmentId4()) + itemMasterAudit.getSubDepartmentId4().trim());
				itemMasterAudit.setDepartmentMerchandise4(getNameForDeptMdseTyp(itemMasterAudit.getDepartmentMerchandise4()));
			} else {
				itemMasterAudit.setDepartmentId4(itemMasterAudit.getDepartmentId4());
				itemMasterAudit.setDepartmentMerchandise4(getNameForDeptMdseTyp(itemMasterAudit.getDepartmentMerchandise4()));
			}
		}
	}

	/**
	 * getNameForDeptMdseTyp.
	 * @param deptMdseTypVal
	 *            String
	 * @return String
	 * @author vn70633
	 */
	private static String getNameForDeptMdseTyp(String deptMdseTypVal) {
		String name = EMPTY;
		String deptMdseTyp = deptMdseTypVal;
		if (!deptMdseTyp.isEmpty()) {
			deptMdseTyp = deptMdseTyp.trim();
			if ("0".equals(deptMdseTyp)) {
				name = "Sellable [0]";
			} else if ("1".equals(deptMdseTyp)) {
				name = "Supply [1]";
			} else if ("6".equals(deptMdseTyp)) {
				name = "Wrap [6]";
			} else if (STRING_SEVEN.equals(deptMdseTyp)) {
				name = "Ingredient [7]";
			} else if (STRING_NINE.equals(deptMdseTyp)) {
				name = "Store Coupon [9]";
			} else if ("F".equals(deptMdseTyp)) {
				name = "Freight [F]";
			} else if ("T".equals(deptMdseTyp)) {
				name = "Tax [T]";
			}
		}
		return name;
	}
	/**
	 * getDeptIdAsString.
	 * @param dept
	 *            String
	 * @return String
	 * @author vn70633
	 */
	private static String getDeptIdAsString(String dept) {
		if (null != dept && dept.trim().length() == 1) {
			return STRING_0 + dept.trim();
		}
		return dept;
	}
	/**
	 * setNameForListDepartment.
	 * @param itemMasterAudits
	 *            List<ItemMasterAudit>
	 * @param subDepartments
	 *            List<StrDepartment>
	 * @author vn70633
	 */
	public static void setNameForListDepartment(List<ItemMasterAudit> itemMasterAudits, List<StoreDepartmentAudit> subDepartments) {

		for (ItemMasterAudit itemMasterAudit : itemMasterAudits) {
			// department 1
			itemMasterAudit.setDepartmentId1(getNameForDepartment(itemMasterAudit.getDepartmentId1(), subDepartments));
			// department 2
			itemMasterAudit.setDepartmentId2(getNameForDepartment(itemMasterAudit.getDepartmentId2(), subDepartments));
			// department 3
			itemMasterAudit.setDepartmentId3(getNameForDepartment(itemMasterAudit.getDepartmentId3(), subDepartments));
			// department 4
			itemMasterAudit.setDepartmentId4(getNameForDepartment(itemMasterAudit.getDepartmentId4(), subDepartments));
		}
	}

	/**
	 * getNameForDepartment.
	 * @param dept
	 *            String
	 * @param subDepartments
	 *            List<DepartmentVO>
	 * @return String
	 * @author vn70633
	 */
	public static String getNameForDepartment(String dept, List<StoreDepartmentAudit> subDepartments) {
		String composite;
		if (!dept.isEmpty()) {
			for (StoreDepartmentAudit subDepartment : subDepartments) {
				composite = subDepartment.getKey().getDepartment().trim() + subDepartment.getKey().getSubDepartment().trim();
				if (dept.trim().equals(composite)) {
					return subDepartment.getName() + " " + "[" + dept + "]";
				}
			}
		}
		return EMPTY;
	}

	/**
	 *
	 * Gets a list of user Information from Ldap using the userId. If there is an error, it logs the error.
	 *
	 * @param userIds A list of user ids that get sent to be searched on LDAP.
	 * @return the ldap searched list of userIds or null if nothing came back.
	 */
	private List<User> getUserInformation(Set<String> userIds) {
		try {
			return ldapSearchRepository.getUserList(new ArrayList<>(userIds));
		}  catch (Exception e) {
			AuditService.logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Returns the username as it should be displayed on the GUI (i.e. 'fullName[user id]').
	 *
	 * @param user The user found using ldap.
	 * @param userId The user id of the user.
	 * @return A String representation of the modifying user as it is meant to be displayed on the GUI.
	 */
	private String getUserDisplayName(User user, String userId) {

		// if user is found, and full name is not blank, return formatted display name: fullName[userId]
		if(user != null && !StringUtils.isBlank(user.getFullName())){
			return String.format(USER_DISPLAY_NAME_FORMAT, user.getFullName().trim(),
					userId.trim());
		}

		// else return user id
		else {
			return userId.trim();
		}
	}

}