/*
 *  ProductECommerceViewService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.eCommerceView;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.repository.AlertStagingRepository;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.*;
import com.heb.util.audit.AuditRecord;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.list.LongPopulator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds all business logic related to product eCommerce View.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Service
public class ProductECommerceViewService {
    @Autowired
    private AuditService auditService;
    private CustomerHierarchyResolver objectResolver = new CustomerHierarchyResolver();
    private static final Logger logger = LoggerFactory.getLogger(ProductECommerceViewService.class);
    /**
     * The const PRODUCT_SHOW_ON_SITE. Product turn on show on site switch.
     */
    public static final boolean PRODUCT_SHOW_ON_SITE = true;
    private static final String SUB_COMMODITY = "Sub Commodity ";
    private static final String BRICK = "Brick ";
    public static final String PRODUCT_ITEM_KEY_CODE = "PROD ";
    private static final String UPC_ITEM_KEY_CODE = "UPC  ";
    private static final String WARNING_PRODUCT_EXTENT_DATA_CODE = "WARN ";
    private static final String DIRECTION_PRODUCT_EXTENT_DATA_CODE = "DIRCT";
    private static final String TAG_ITEM_KEY_CODE = "TAG  ";
    private static final String SPEC_ITEM_KEY_CODE = "SPEC ";
    private static final String INGREDIENTS_PRODUCT_EXTENT_DATA_CODE = "INGRE";
    private static final String GUARANTEED_PRODUCT_EXTENT_DATA_CODE = "GRNTD";
    public static final String ATTRIBUTE_RELATIONSHIP_GROUP_TYPE_CODE = "ATTR";
    private static final String SCALE_MANAGEMENT_SOURCE_SYSTEM = "Scale Management";
    private static final long DIMENSION_HEIGHT_ATTRIBUTE = 1668;
    private static final long DIMENSION_WIDTH_ATTRIBUTE = 1669;
    private static final long DIMENSION_DEPTH_ATTRIBUTE = 1670;
    private static final long DIMENSION_WEIGHT_ATTRIBUTE = 1671;
    private static final String DIMENSION_HEIGHT_ATTRIBUTE_ID = "1668";
    private static final String DIMENSION_WIDTH_ATTRIBUTE_ID = "1669";
    private static final String DIMENSION_DEPTH_ATTRIBUTE_ID = "1670";
    private static final String DIMENSION_WEIGHT_ATTRIBUTE_ID = "1671";
    private static final String DIMENSION_HEIGHT_ATTRIBUTE_NAME = "Height";
    private static final String DIMENSION_WIDTH_ATTRIBUTE_NAME = "Width";
    private static final String DIMENSION_DEPTH_ATTRIBUTE_NAME = "Depth";
    private static final String DIMENSION_WEIGHT_ATTRIBUTE_NAME = "Weight";
    private static final String CPS_DIMENSIONS = "CPS Dimensions";
    public static final String BR_TAG="<br>";
    /**
     * PROD_EXT_DTA_CD_FOR_ECOM_DES1.
     */
    public static final String PROD_EXT_DTA_CD_FOR_ECOM_DES1 = "ESHRT";
    /**
     * PROD_EXT_DTA_CD_FOR_ECOM_DES2.
     */
    public static final String PROD_EXT_DTA_CD_FOR_ECOM_DES2 = "ELONG";
    /**
     * PRODUCT_PRIMARY_PATH.
     */
    public static final boolean PRODUCT_PRIMARY_PATH = true;
    /**
     * ACTIVE_ONLINE.
     */
    public static final boolean ACTIVE_ONLINE = true;
    /**
     * PRIMARY_IMAGE.
     */
    public static final String PRIMARY_IMAGE = "P";
    public static final String AlTERNATES_IMAGE = "A";
    public static final String IMAGE_APPROVE_CODE = "A";

    public static final String SPACE_SEPARATOR = " ";

    //Product description constants
    private static final String PRODUCT_DESCRIPTION_TEXT_TAG1 = "TAG1";
    private static final String PRODUCT_DESCRIPTION_TEXT_TAG2 = "TAG2";

    private static final String PRODUCT_DESCRIPTION_TEXT_LANGUAGE = "ENG";

    private static final int MAX_LENGTH = 10000;

    private static final String GOOGLE_SHOPPING_SALE_CHANEL = "05   ";
    private static final String HEB_COM_SALE_CHANEL = "01   ";
    private static final int LIMIT_QUERY_IN = 999;

    private static final String ALERT_STAGING_PRUPD = "PRUPD";
    private static final String ALERT_STAGING_STATUS_ACTIVE = "ACTIV";
    private static final String COMMA_CHARACTER = ",";

    public enum SourceSystemNumber {
        ECOMMERCE_SOURCE_SYSTEM_NUMBER(13L),
        GLADSON_SOURCE_SYSTEM_NUMBER(7L),
        CORE_PRODUCT_SETUP_SOURCE_SYSTEM_NUMBER(1L),
        PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER(4L),
        OBPS_SOURCE_SYSTEM_NUMBER(20L),
        ESHA_GENESIS_SOURCE_SYSTEM_NUMBER(26L),
        GS1_SOURCE_SYSTEM_NUMBER(9L),
        KWIKEE_SOURCE_SYSTEM_NUMBER(12L),
        ITEMMASTER_COM_SOURCE_SYSTEM_NUMBER(11L),
        MANUFACTURER_SOURCE_SYSTEM_NUMBER(15L),
        OTHER_RETAILERS_SOURCE_SYSTEM_NUMBER(16L),
        SCALE_MANAGEMENT_SOURCE_SYSTEM_NUMBER(17L),
        DEFAULT_SOURCE_SYSTEM_NUMBER(0L),
        EARLEY_SOURCE_SYSTEM_NUMBER(29L),
        MAT_SOURCE_SYSTEM_NUMBER(31L);

        private Long value;

        SourceSystemNumber(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return this.value;
        }

        public static SourceSystemNumber getNumber(Long sourceSystemId) {
            for (SourceSystemNumber number : SourceSystemNumber.values()) {
                if (number.getValue().equals(sourceSystemId)) {
                    return number;
                }
            }
            return SourceSystemNumber.DEFAULT_SOURCE_SYSTEM_NUMBER;
        }
    }

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
        FAVOR_ITEM_DESCRIPTION_LOGIC_ATTRIBUTE_ID(3989L),
        TAGS_LOGIC_ATTRIBUTE_ID(1729L),
        INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID(1674L),
        DISPLAY_NAME_LOGIC_ATTRIBUTE_ID(1664L),
        SIZE_LOGIC_ATTRIBUTE_ID(1667L),
        INGREDIENTS_PET_GUARANTEED_LOGIC_ATTRIBUTE_ID(1803L),
        INGREDIENTS_LOGIC_ATTRIBUTE_ID(1643L);
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
            return null;
        }
    }

    public enum PhysicalAttributeCodeEditable {
        BRAND_PHYSICAL_ATTRIBUTE_EDITABLE(1642L),
        SIZE_PHYSICAL_ATTRIBUTE_EDITABLE(1636L),
        INGREDIENT_STATEMENT_PHYSICAL_ATTRIBUTE_EDITABLE(1643L),
        WARNING_PHYSICAL_ATTRIBUTE_EDITABLE(1682L),
        DIRECTIONS_PHYSICAL_ATTRIBUTE_EDITABLE(1654L),
        DISPLAY_NAME_ATTRIBUTE_EDITABLE(1601L),
        ROMANCE_COPY_ATTRIBUTE_EDITABLE(1600L),
        FAVOR_ITEM_DESCRIPTION_ATTRIBUTE_EDITABLE(3989L);

        private Long value;

        PhysicalAttributeCodeEditable(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return this.value;
        }

        public static PhysicalAttributeCodeEditable getAttribute(Long attributeId) {
            for (PhysicalAttributeCodeEditable attribute : PhysicalAttributeCodeEditable.values()) {
                if (attribute.getValue().equals(attributeId)) {
                    return attribute;
                }
            }
            return null;
        }
    }
    /**
     * PHY_ATT_ID_BRAND.
     */
    public static final int PHY_ATT_ID_BRAND_EDIT = 1642;

    // The default number of items to search for. This number
 	// and fewer will have the maximum performance.
 	private static final int DEFAULT_ITEM_COUNT = 100;

 	// Used to get consistent size lists to query runners.
 	private LongPopulator longPopulator = new LongPopulator();

    @Autowired
    private PDPTemplateRepository pdpTemplateRepository;

    @Autowired
    private ProductPanelTypeRepository productPanelTypeRepository;

    @Autowired
    private SalesChannelRepository salesChannelRepository;

    @Autowired
    private MasterDataExtensionAttributeRepository masterDataExtensionAttributeRepository;

    @Autowired
    private ProductAttributeOverwriteRepository productAttributeOverwriteRepository;

    @Autowired
    private TargetSystemAttributePriorityRepository targetSystemAttributePriorityRepository;

    @Autowired
    private ProductScanCodeExtentRepository productScanCodeExtentRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private PublicationAuditRepository publicationAuditRepository;

    @Autowired
    private LdapSearchRepository ldapSearchRepository;

    @Autowired
    private ProductAssortmentServiceClient productAssortmentServiceClient;

    @Autowired
    private ProductOnlineRepository productOnlineRepository;

    @Autowired
    private FulfillmentChannelRepository fulfillmentChannelRepository;

    @Autowired
    private ProductScanCodeNutrientRepository productScanCodeNutrientRepository;

    @Autowired
    private ProductRelationshipRepository productRelationshipRepository;

    @Autowired
    private CustomerHierarchyAssigmentService customerHierarchyAssigmentService;

    @Autowired
    private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

    @Autowired
    private GenericEntityRepository genericEntityRepository;

    @Autowired
    private HierarchyContextRepository hierarchyContextRepository;

    @Autowired
    private ProductNutrientRepository productNutrientRepository;

    @Autowired
    private ProductPkVariationRepository productPkVariationRepository;

    @Autowired
    private ProductFullfilmentChanelRepository productFullfilmentChanelRepository;

    @Autowired
    private SellingUnitRepository sellingUnitRepository;

    @Autowired
    private GoodsProductRepository goodsProductRepository;

    @Autowired
    private ProductDescriptionRepository productDescriptionRepository;

    @Autowired
    private ScaleUpcRepository scaleUpcRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

    @Autowired
    private ProductManagementServiceClient productManagementServiceClient;

    @Autowired
    private ProductScanImageBannerRepository productScanImageBannerRepository;

    @Autowired
    private ContentManagementServiceClient contentManagementServiceClient;

    @Autowired
    private GenericEntityDescriptionRespository genericEntityDescriptionRespository;

    @Autowired
    private SubCommodityRepository subCommodityRepository;

    @Autowired
    private AlertStagingRepository alertStagingRepository;

    private LazyObjectResolver<TargetSystemAttributePriority> attributePriorityResolver = new TargetSystemAttributePriorityResolver();

    /**
     * Resolver fetch source system data for target system attribute priority object.
     */
    private class TargetSystemAttributePriorityResolver implements LazyObjectResolver<TargetSystemAttributePriority> {

        /**
         * Resolves source system description for target system attribute priority object.
         *
         * @param attributePriority The object to resolve.
         */
        @Override
        public void fetch(TargetSystemAttributePriority attributePriority) {
            if (attributePriority.getSourceSystem() != null) {
                attributePriority.getSourceSystem().getDescription();
            }
        }
    }

    private LazyObjectResolver<List<ProductScanImageBanner>> productScanImageBannerResolver = new ProductScanImageBannerResolver();

    /**
     * Resolves a ProductNutrient object. It will load the following properties:
     */
    private class ProductScanImageBannerResolver implements LazyObjectResolver<List<ProductScanImageBanner>> {//not use
        @Override
        public void fetch(List<ProductScanImageBanner> productScanImageBanners) {
            if(productScanImageBanners != null && productScanImageBanners.size() > 0) {
                productScanImageBanners.forEach((p) -> {
                    if (p.getProductScanImageURI() != null) {
                        p.getProductScanImageURI().getKey();
                        if(p.getProductScanImageURI().getImageCategory() != null){
                            p.getProductScanImageURI().getImageCategory().getId();
                        }
                    }
                });
            }
        }
    }

    /**
     * Check the data of current source is different with the publish source.
     *
     * @param productId - The product id
     * @param logicAttributeCode - The logic attribute code
     * @return The Boolean.
     */
    public boolean validateChangedPublishedSource(Long productId, Long logicAttributeCode) {
        boolean check = false;
        //find ALERT_STAGING_TBL
        List<AlertStaging> alertStagings = this.alertStagingRepository
                .findByAlertTypeCDAndAlertStatusCDAndAlertKeyOrderByAlertKey
                        (ALERT_STAGING_PRUPD, ALERT_STAGING_STATUS_ACTIVE, String.format("%09d", productId));
        if(alertStagings != null && !alertStagings.isEmpty()){
            for (AlertStaging alertStaging : alertStagings) {
                if(StringUtils.isNotBlank(alertStaging.getAlertDataTxt())){
                    String[] alertDataTxtElms = alertStaging.getAlertDataTxt().split(COMMA_CHARACTER);
                    for(String alertDataTxt : alertDataTxtElms){
                        if(logicAttributeCode.equals(Long.valueOf(StringUtils.trim(alertDataTxt)))){
                            check = true;
                            break;
                        }
                    }
                }
                if(check){
                    break;
                }
            }
        }
        return check;
    }

    /**
     * Update nutrition fact information.
     *
     * @param eCommerceViewAttributePriorities - The list of ECommerceViewAttributePriorities
     * @param productId - The product id
     * @param userId    - The user id
     */
    public void updateNutritionFactInformation(List<ECommerceViewAttributePriorities> eCommerceViewAttributePriorities, Long productId, String userId) {
        this.productAttributeManagementServiceClient.updateNutritionFactInformation(eCommerceViewAttributePriorities, productId, userId);
    }

    /**
     * Get nutrition fact information.
     *
     * @param productId - The product id
     * @param upc          - The primary scan code
     * @param sourceSystem - The source system
     * @return The list of ECommerceViewAttributePriorities.
     */
    public List<ECommerceViewAttributePriorities> getNutritionFactInformation(Long productId, Long upc, Long sourceSystem) {
        List<ECommerceViewAttributePriorities> returnList = new ArrayList<ECommerceViewAttributePriorities>();
        ECommerceViewAttributePriorities nutritionFactSourceEcommerce =
                this.getNutritionFactBySourceSystem(productId, upc, sourceSystem);
        if(nutritionFactSourceEcommerce != null){
            //find ALERT_STAGING_TBL
            nutritionFactSourceEcommerce.setDifferentWithDefaultValue(this.validateChangedPublishedSource(productId, LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.getValue()));

            returnList.add(nutritionFactSourceEcommerce);
        }else{
            ECommerceViewAttributePriorities eCommerceViewAttributePriorities = this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.value, productId, upc);
            if (eCommerceViewAttributePriorities != null) {
                returnList.add(eCommerceViewAttributePriorities);
            }
        }
        return returnList;
    }

    /**
     * Get all nutrition facts information by attribute priorities.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @return The list of ECommerceViewAttributePriorities.
     */
    public List<ECommerceViewAttributePriorities> getAllNutritionFactsByAttributePriorities(Long productId, Long upc, Long sourceSystem) {
        List<ECommerceViewAttributePriorities> returnList = new ArrayList<ECommerceViewAttributePriorities>();
        List<ECommerceViewAttributePriorities> sourceList = new ArrayList<ECommerceViewAttributePriorities>();
        ECommerceViewAttributePriorities nutritionFactSourceEcommerce =
                this.getNutritionFactBySourceSystem(productId, upc, sourceSystem);
        if(nutritionFactSourceEcommerce != null){
            returnList.add(nutritionFactSourceEcommerce);
        }
        ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository.findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, productId, LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.value);

        boolean checkSourceSystemDefault = false;
        boolean checkSourceSystemSelected = false;
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this
                .targetSystemAttributePriorityRepository.findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(LogicAttributeCode.NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID.value.intValue());
        if(targetSystemAttributePriorities != null){
            for (TargetSystemAttributePriority targetSystemAttributePriority : targetSystemAttributePriorities) {
                if(targetSystemAttributePriority.getKey().getDataSourceSystemId() == 0){
                    ECommerceViewAttributePriorities item = new ECommerceViewAttributePriorities();
                    item.setSourceSystemId(targetSystemAttributePriority.getKey().getDataSourceSystemId());
                    if(targetSystemAttributePriority.getSourceSystem() != null && targetSystemAttributePriority.getSourceSystem().getDescription() != null){
                        item.setSourceSystemDescription(targetSystemAttributePriority.getSourceSystem().getDescription().trim());
                    }
                    item.setPhysicalAttributeId(targetSystemAttributePriority.getKey().getPhysicalAttributeId());
                    item.setRelationshipGroupTypeCode(targetSystemAttributePriority.getKey().getRelationshipGroupTypeCode());
                    if(productAttributeOverwrite != null && (productAttributeOverwrite.getKey().getSourceSystemId() == targetSystemAttributePriority.getKey().getDataSourceSystemId())){
                        item.setSelectedSourceSystem(true);
                        checkSourceSystemSelected = true;
                    }
                    sourceList.add(item);
                }else{
                    ECommerceViewAttributePriorities eCommerceViewAttributePriorities =
                            this.getNutritionFactBySourceSystem(productId, upc, targetSystemAttributePriority.getKey().getDataSourceSystemId());
                    if(eCommerceViewAttributePriorities != null){
                        if(!checkSourceSystemDefault){
                            checkSourceSystemDefault = true;
                            eCommerceViewAttributePriorities.setSourceSystemDefault(true);
                        }
                        eCommerceViewAttributePriorities.setPhysicalAttributeId(targetSystemAttributePriority.getKey().getPhysicalAttributeId());
                        eCommerceViewAttributePriorities.setRelationshipGroupTypeCode(targetSystemAttributePriority.getKey().getRelationshipGroupTypeCode());
                        if(productAttributeOverwrite != null && (productAttributeOverwrite.getKey().getSourceSystemId() == targetSystemAttributePriority.getKey().getDataSourceSystemId())){
                            eCommerceViewAttributePriorities.setSelectedSourceSystem(true);
                            checkSourceSystemSelected = true;
                        }
                        sourceList.add(eCommerceViewAttributePriorities);
                    }
                }
            }
        }
        if(sourceList != null && sourceList.size() > 0) {
            if(!checkSourceSystemSelected && sourceList.get(0).getSourceSystemId() != 0){
                sourceList.get(0).setSelectedSourceSystem(true);
            }
            returnList.addAll(sourceList);
        }
        return returnList;
    }

    /**
     * Get all nutrition facts information by source system.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @return ECommerceViewAttributePriorities.
     */
    public ECommerceViewAttributePriorities getNutritionFactBySourceSystem(Long productId, Long upc, Long sourceSystem) {
        ECommerceViewAttributePriorities eCommerceViewAttributePriorities = new ECommerceViewAttributePriorities();
        eCommerceViewAttributePriorities.setSourceSystemId(sourceSystem);
        boolean check = false;
        if(sourceSystem == SourceSystemNumber.SCALE_MANAGEMENT_SOURCE_SYSTEM_NUMBER.value){
            ScaleUpc scaleUpc = this.getNutritionFactByScaleSource(upc);
            if(scaleUpc != null){
                eCommerceViewAttributePriorities.setScaleUpc(scaleUpc);
                eCommerceViewAttributePriorities.setSourceSystemDescription(ProductECommerceViewService.SCALE_MANAGEMENT_SOURCE_SYSTEM);
                check = true;
            }
        }else{
            List<ProductNutrient> productNutrientList = this.getNutritionFactDataBySourceSystem(productId, upc, sourceSystem);
            List<ProductPkVariation> productPkVariationList = this.getNutritionFactHeaderBySourceSystem(productId, upc, sourceSystem);
            if (productNutrientList != null && productNutrientList.size() > 0) {
                eCommerceViewAttributePriorities.setProductNutrients(productNutrientList);
                eCommerceViewAttributePriorities.setSourceSystemDescription(productNutrientList.get(0).getSourceSystem().getDescription());
                check = true;
            }
            if (productPkVariationList != null && productPkVariationList.size() > 0) {
                eCommerceViewAttributePriorities.setProductPkVariations(productPkVariationList);
                eCommerceViewAttributePriorities.setSourceSystemDescription(productPkVariationList.get(0).getSourceSystem().getDescription());
                check = true;
            }
            if(sourceSystem == SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.value){
                eCommerceViewAttributePriorities.setPostedDate(this.getNutritionFactPostedDate(productPkVariationList, productNutrientList));
            }else{
                eCommerceViewAttributePriorities.setCreatedDate(this.getNutritionFactCreatedDate(productPkVariationList, productNutrientList));
            }
        }
        return check ? eCommerceViewAttributePriorities : null;
    }

    /**
     * Get created date of nutrition fact.
     *
     * @param lstProPKVarForSpec - The list of ProductPkVariation
     * @param lstNutrientForSpec - The list of ProductNutrient
     * @return The local date.
     */
    private LocalDate getNutritionFactCreatedDate(List<ProductPkVariation> lstProPKVarForSpec, List<ProductNutrient> lstNutrientForSpec) {
        LocalDate returnDate = null;
        if ((lstNutrientForSpec != null && lstNutrientForSpec.size() > 0)
                && (lstProPKVarForSpec != null && lstProPKVarForSpec.size() > 0)) {
            if (lstProPKVarForSpec.get(0).getLastUpdateDate().compareTo(lstNutrientForSpec.get(0).getLastUpdateDate()) >= 0) {
                returnDate = lstProPKVarForSpec.get(0).getLastUpdateDate().toLocalDate();
            }else{
                returnDate = lstNutrientForSpec.get(0).getLastUpdateDate().toLocalDate();
            }
        }else if (lstProPKVarForSpec != null && lstProPKVarForSpec.size() > 0) {
            returnDate = lstProPKVarForSpec.get(0).getLastUpdateDate().toLocalDate();
        }else if (lstNutrientForSpec != null && lstNutrientForSpec.size() > 0) {
            returnDate = lstNutrientForSpec.get(0).getLastUpdateDate().toLocalDate();
        }
        return returnDate;
    }

    /**
     * Get posted date of nutrition fact.
     *
     * @param lstProPKVarForSpec - The list of ProductPkVariation
     * @param lstNutrientForSpec - The list of ProductNutrient
     * @return The local date.
     */
    private LocalDate getNutritionFactPostedDate(List<ProductPkVariation> lstProPKVarForSpec, List<ProductNutrient> lstNutrientForSpec) {
        LocalDate returnDate = null;
        if ((lstNutrientForSpec != null && lstNutrientForSpec.size() > 0)
                && (lstProPKVarForSpec != null && lstProPKVarForSpec.size() > 0)) {
            if (lstProPKVarForSpec.get(0).getCreateDate().compareTo(lstNutrientForSpec.get(0).getCreateDate()) >= 0) {
                returnDate = lstProPKVarForSpec.get(0).getCreateDate().toLocalDate();
            }else{
                returnDate = lstNutrientForSpec.get(0).getCreateDate().toLocalDate();
            }
        }else if (lstProPKVarForSpec != null && lstProPKVarForSpec.size() > 0) {
            returnDate = lstProPKVarForSpec.get(0).getCreateDate().toLocalDate();
        }else if (lstNutrientForSpec != null && lstNutrientForSpec.size() > 0) {
            returnDate = lstNutrientForSpec.get(0).getCreateDate().toLocalDate();
        }
        return returnDate;
    }

    /**
     * Get data list of nutrition fact by source system.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @return The list of ProductNutrient.
     */
    public List<ProductNutrient> getNutritionFactDataBySourceSystem(Long productId, Long upc, Long sourceSystem) {
        List<ProductNutrient> returnList = null;
        if(sourceSystem != SourceSystemNumber.SCALE_MANAGEMENT_SOURCE_SYSTEM_NUMBER.value){
            returnList = this.productNutrientRepository.findByKeyUpcAndKeySourceSystemOrderByKeyValPreprdTypCdDescKeyMasterIdAsc(upc, sourceSystem.intValue());
        }
        return returnList;
    }

    /**
     * Get nutrition fact information of scale source.
     *
     * @param upc - The primary scan code
     * @return The ScaleUpc.
     */
    public ScaleUpc getNutritionFactByScaleSource(Long upc) {
        ScaleUpc returnScaleUpc = null;
        ScaleUpc scaleUpc = this.scaleUpcRepository.findOne(upc);
        if(scaleUpc != null){
            if(scaleUpc.getNutrientStatementDetails() != null && scaleUpc.getNutrientStatementDetails().size() > 0) {
                returnScaleUpc = scaleUpc;
            }
            if(scaleUpc.getNutrientStatementHeader() != null) {
                returnScaleUpc = scaleUpc;
            }
        }
        return returnScaleUpc;
    }

    /**
     * Get header information of nutrition fact by source system.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @return The list of ProductPkVariation.
     */
    public List<ProductPkVariation> getNutritionFactHeaderBySourceSystem(Long productId, Long upc, Long sourceSystem) {
        List<ProductPkVariation> returnList = null;
        if(sourceSystem != SourceSystemNumber.SCALE_MANAGEMENT_SOURCE_SYSTEM_NUMBER.value){
            returnList = this.productPkVariationRepository.findByKeyUpcAndKeySourceSystem(upc, sourceSystem.intValue());
        }
        return returnList;
    }

    /**
     * Get dimension information.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @return The list of dimension.
     */
    public List<AttributeValue> getDimensionInformation(Long productId, Long upc) {
        List<AttributeValue> returnList = null;
        List<AttributeValue> dimensionList = this.getDimensionBySourceSystem(productId, upc, SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.value);
        if(dimensionList != null && dimensionList.size() > 0) {
            returnList = dimensionList;
        }else{
            ECommerceViewAttributePriorities eCommerceViewAttributePriorities = this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode.DIMENSIONS_LOGIC_ATTRIBUTE_ID.value, productId, upc);
            if (eCommerceViewAttributePriorities != null) {
                returnList = eCommerceViewAttributePriorities.getDimensions();
            }
        }
        return returnList;
    }

    /**
     * Get all dimensions information by attribute priorities.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @return The list of ECommerceViewAttributePriorities.
     */
    public ECommerceViewAttributePriority getAllDimensionsByAttributePriorities(Long productId, Long upc, Long sourceSystem) {
        ECommerceViewAttributePriority eCommerceViewAttributePriority = new ECommerceViewAttributePriority();
        eCommerceViewAttributePriority.seteCommerceViewAttributePriorityDetails(new ArrayList<>());
        boolean checkSourceSystemDefault = false;
        boolean checkSourceSystemSelected = false;

        MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(LogicAttributeCode.DIMENSIONS_LOGIC_ATTRIBUTE_ID.value.longValue(), productId,
                        ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, SourceSystemNumber
                                .ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue());
        if (masterDataExtensionAttribute != null) {
            eCommerceViewAttributePriority.setMainContent(
                    new ECommerceViewAttributePriority.ContentString(StringUtils.trim(masterDataExtensionAttribute.getAttributeValueText())));
            eCommerceViewAttributePriority.setMasterDataExtensionAttribute(masterDataExtensionAttribute);
        }

        //Get Product Attribute Overwrite Information
        ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository.findFirstByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, productId, LogicAttributeCode.DIMENSIONS_LOGIC_ATTRIBUTE_ID.value);
        if (productAttributeOverwrite != null) {
            eCommerceViewAttributePriority.setProductAttributeOverwrite(productAttributeOverwrite);
        }
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this
                .targetSystemAttributePriorityRepository.findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(LogicAttributeCode.DIMENSIONS_LOGIC_ATTRIBUTE_ID.value.intValue());
        if(targetSystemAttributePriorities != null){
            for (TargetSystemAttributePriority targetSystemAttributePriority : targetSystemAttributePriorities) {
                if(targetSystemAttributePriority.getKey().getDataSourceSystemId() == 0){
                    ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails = new ECommerceViewAttributePriorityDetails();
                    eCommerceViewAttributePriorityDetails.setTargetSystemAttributePriority(targetSystemAttributePriority);
                    if(targetSystemAttributePriority.getSourceSystem() != null
                            && targetSystemAttributePriority.getSourceSystem().getDescription() != null){
                        eCommerceViewAttributePriorityDetails.setSourceDescription(targetSystemAttributePriority.getSourceSystem().getDescription().trim());
                    }
                    if(productAttributeOverwrite != null
                            && (productAttributeOverwrite.getKey().getSourceSystemId() == targetSystemAttributePriority.getKey().getDataSourceSystemId())){
                        eCommerceViewAttributePriorityDetails.setSelected(true);
                        checkSourceSystemSelected = true;
                    }
                    eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().add(eCommerceViewAttributePriorityDetails);
                }else{
                    List<AttributeValue> dimensions = this.getDimensionBySourceSystem(productId, upc, targetSystemAttributePriority.getKey().getDataSourceSystemId());
                    ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails = new ECommerceViewAttributePriorityDetails();
                    eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentDimension(dimensions));
                    if(dimensions != null && dimensions.size() > 0){
                        if(!checkSourceSystemDefault){
                            checkSourceSystemDefault = true;
                            eCommerceViewAttributePriorityDetails.setSourceDefault(true);
                        }
                        eCommerceViewAttributePriorityDetails.setTargetSystemAttributePriority(targetSystemAttributePriority);
                        if(targetSystemAttributePriority.getSourceSystem() != null
                                && targetSystemAttributePriority.getSourceSystem().getDescription() != null){
                            if(targetSystemAttributePriority.getKey().getDataSourceSystemId() == 1) {
                                eCommerceViewAttributePriorityDetails.setSourceDescription(ProductECommerceViewService.CPS_DIMENSIONS);
                            }
                            else{
                                eCommerceViewAttributePriorityDetails.setSourceDescription(targetSystemAttributePriority.getSourceSystem().getDescription().trim());
                            }
                        }
                        if(productAttributeOverwrite != null
                                && (productAttributeOverwrite.getKey().getSourceSystemId() == targetSystemAttributePriority.getKey().getDataSourceSystemId())){
                            eCommerceViewAttributePriorityDetails.setSelected(true);
                            checkSourceSystemSelected = true;
                        }
                        eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().add(eCommerceViewAttributePriorityDetails);
                    }
                }
            }
        }
        if(eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails() != null
                && eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().size() > 0) {
            if(!checkSourceSystemSelected
                    && eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().get(0).getTargetSystemAttributePriority().getKey().getDataSourceSystemId() != 0){
                eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().get(0).setSelected(true);
            }
        }
        //Get source 13 Information
        List<AttributeValue> dimensionList = this.getDimensionBySourceSystem(productId, upc, SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.value);
        if(dimensionList != null && dimensionList.size() > 0) {
            ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails = new ECommerceViewAttributePriorityDetails();
            eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentDimension(dimensionList));
            eCommerceViewAttributePriorityDetails.setSourceDescription(SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.value.toString());
            eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().add(eCommerceViewAttributePriorityDetails);
        }

        return eCommerceViewAttributePriority;
    }

    /**
     * Get dimension information by source system.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @return The list of dimension.
     */
    public List<AttributeValue> getDimensionBySourceSystem(Long productId, Long upc, Long sourceSystem) {
        List<AttributeValue> returnList =  new ArrayList<AttributeValue>();
        switch (SourceSystemNumber.getNumber(sourceSystem)) {
            case CORE_PRODUCT_SETUP_SOURCE_SYSTEM_NUMBER:
                this.getDimensionDataByCpsSource(upc, returnList);
                break;
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
                this.getDimensionDataByProductMaintenanceSource(productId, returnList);
                break;
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                this.getDimensionDataByGladsonSource(upc, sourceSystem, returnList);
                break;
            case GS1_SOURCE_SYSTEM_NUMBER:
                this.getDimensionDataByGs1Source(upc, sourceSystem, returnList);
                break;
            case ECOMMERCE_SOURCE_SYSTEM_NUMBER:
                this.getDimensionDataByEcommerceSource(productId, sourceSystem, returnList);
                break;
            default:
                break;
        }
        return returnList;
    }

    /**
     * Get specification information.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @return The list of specification.
     */
    public List<AttributeValue> getSpecificationInformation(Long productId, Long upc) {
        List<AttributeValue> returnList =  null;
        List<AttributeValue> specificationList = this.getSpecificationBySourceSystem(productId, upc, SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.value);
        if(specificationList != null && specificationList.size() > 0) {
            returnList = specificationList;
        }else{
            ECommerceViewAttributePriorities eCommerceViewAttributePriorities = this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode.SPECIFICATION_LOGIC_ATTRIBUTE_ID.value, productId, upc);
            if (eCommerceViewAttributePriorities != null) {
                returnList = eCommerceViewAttributePriorities.getSpecifications();
            }
        }
        return returnList;
    }

    /**
     * Get specification information by source system.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @return The list of specification.
     */
    public List<AttributeValue> getSpecificationBySourceSystem(Long productId, Long upc, Long sourceSystem) {
        List<AttributeValue> returnList = null;
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = null;
        switch (SourceSystemNumber.getNumber(sourceSystem)) {
            case EARLEY_SOURCE_SYSTEM_NUMBER:
            case ECOMMERCE_SOURCE_SYSTEM_NUMBER:
            case MAT_SOURCE_SYSTEM_NUMBER:
                masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository
                        .findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeIdAscKeySequenceNumberAsc(productId, ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, sourceSystem);
                returnList = this.getListSpecification(masterDataExtensionAttributes);
                break;
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
                returnList = new ArrayList<AttributeValue>();
                masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository
                        .findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeIdAscKeySequenceNumberAsc(productId, ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, sourceSystem);
                returnList.addAll(this.getListSpecification(masterDataExtensionAttributes));
                masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository
                        .findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeIdAscKeySequenceNumberAsc(upc, ProductECommerceViewService.UPC_ITEM_KEY_CODE, sourceSystem);
                returnList.addAll(this.getListSpecification(masterDataExtensionAttributes));
                break;
            default:
                masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository
                        .findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeIdAscKeySequenceNumberAsc(upc, ProductECommerceViewService.UPC_ITEM_KEY_CODE, sourceSystem);
                returnList = this.getListSpecification(masterDataExtensionAttributes);
                break;
        }
        return returnList;
    }

    /**
     * Get list of specification.
     *
     * @param masterDataExtensionAttributes - The list of masterDataExtensionAttributes
     * @return The list of specification.
     */
    private List<AttributeValue> getListSpecification(List<MasterDataExtensionAttribute> masterDataExtensionAttributes){
        List<Long> attrIdList = new ArrayList<Long>();
        List<AttributeValue> returnList = new ArrayList<>();
        List<AttributeValue> tempList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(masterDataExtensionAttributes)) {
            for (MasterDataExtensionAttribute masterDataExtensionAttribute : masterDataExtensionAttributes) {
                if(masterDataExtensionAttribute.getEcommerUserGroupAttributes() != null
                        && masterDataExtensionAttribute.getEcommerUserGroupAttributes().size() > 0
                        && SPEC_ITEM_KEY_CODE.equalsIgnoreCase(masterDataExtensionAttribute.getEcommerUserGroupAttributes().get(0).getKey().getUsrInrfcGrpCd())){
                    if(masterDataExtensionAttribute.getAttribute() != null){
                        if(masterDataExtensionAttribute.getGroupAttributes() != null
                                && masterDataExtensionAttribute.getGroupAttributes().size() > 0){
                            if(masterDataExtensionAttribute.getGroupAttributes().get(0).getRelationshipGroup() != null){
                                if (!attrIdList.contains(masterDataExtensionAttribute.getKey().getAttributeId())) {
                                    AttributeValue item = new AttributeValue();
                                    item.setAttrId(masterDataExtensionAttribute.getKey().getAttributeId().toString());
                                    item.setName(masterDataExtensionAttribute.getGroupAttributes().get(0).getRelationshipGroup().getRelationshipGroupDescription());
                                    item.setDescription(masterDataExtensionAttribute.getAttribute().getAttributeInstantDescription());
                                    item.setDetails(this.getSpecificationDetailsData(masterDataExtensionAttribute));
                                    tempList.add(item);
                                    attrIdList.add(masterDataExtensionAttribute.getKey().getAttributeId());
                                }else{
                                    String tempDescription = tempList.get(tempList.size() - 1).getDescription();
                                    if(tempDescription != null){
                                        tempDescription = tempDescription.concat(ProductECommerceViewService.SPACE_SEPARATOR).concat(masterDataExtensionAttribute.getAttribute().getAttributeInstantDescription());
                                        tempList.get(tempList.size() - 1).setDescription(tempDescription.trim());
                                    }
                                    String tempDetails = tempList.get(tempList.size() - 1).getDetails();
                                    if(tempDetails != null){
                                        tempDetails = tempDetails.concat(ProductECommerceViewService.SPACE_SEPARATOR).concat(this.getSpecificationDetailsData(masterDataExtensionAttribute));
                                        tempList.get(tempList.size() - 1).setDetails(tempDetails);
                                    }
                                }
                            }
                        }else{
                            if(masterDataExtensionAttribute.getKey().getAttributeId() != ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE
                                    && masterDataExtensionAttribute.getKey().getAttributeId() != ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE
                                    && masterDataExtensionAttribute.getKey().getAttributeId() != ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE
                                    && masterDataExtensionAttribute.getKey().getAttributeId() != ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE){
                                AttributeValue item = new AttributeValue();
                                item.setAttrId(masterDataExtensionAttribute.getKey().getAttributeId().toString());
                                item.setName(masterDataExtensionAttribute.getAttribute().getAttributeName());
                                item.setDescription(masterDataExtensionAttribute.getAttribute().getAttributeInstantDescription());
                                item.setDetails(this.getSpecificationDetailsData(masterDataExtensionAttribute));
                                returnList.add(item);
                            }
                        }
                    }
                }
            }
            returnList.addAll(tempList);
        }
        return returnList;
    }

    /**
     * Get specification detail information.
     *
     * @param masterDataExtensionAttribute - The MasterDataExtensionAttribute entity
     * @return The String object.
     */
    private String getSpecificationDetailsData(MasterDataExtensionAttribute masterDataExtensionAttribute) {
        String returnString = StringUtils.EMPTY;
        if(masterDataExtensionAttribute.getAttribute().getAttributeValueList()){
            if(null != masterDataExtensionAttribute.getAttributeCode()) {
                returnString = masterDataExtensionAttribute.getAttributeCode().getAttributeValueCode();
            }
        }else{
            switch (masterDataExtensionAttribute.getAttribute().getAttributeDomainCode()) {
                case MasterDataExtensionAttribute.STRING_ATTRIBUTE_VALUE:
                    returnString = masterDataExtensionAttribute.getAttributeValueText();
                    break;
                case MasterDataExtensionAttribute.TIME_ATTRIBUTE_VALUE:
                    returnString = masterDataExtensionAttribute.getAttributeValueTime().toString();
                    break;
                case MasterDataExtensionAttribute.DATE_ATTRIBUTE_VALUE:
                    returnString = masterDataExtensionAttribute.getAttributeValueDate().toString();
                    break;
                case MasterDataExtensionAttribute.DECIMAL_ATTRIBUTE_VALUE:
                    returnString = masterDataExtensionAttribute.getAttributeValueNumber().toString();
                    break;
                case MasterDataExtensionAttribute.INTEGER_ATTRIBUTE_VALUE:
                    returnString = String.valueOf(masterDataExtensionAttribute.getAttributeValueNumber().intValue());
                    break;
                default:
                    break;
            }
        }
        return returnString;
    }

    /**
     * Get dimension information of ecommerce source.
     *
     * @param productId - The product id
     * @param sourceSystem - The source system
     * @param returnList - The list of dimension
     */
    private void getDimensionDataByEcommerceSource(Long productId, Long sourceSystem, List<AttributeValue> returnList) {
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository
                .findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeId(productId, ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, sourceSystem.intValue());
        AttributeValue height = new AttributeValue();
        height.setAttrId(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_ID);
        height.setName(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_NAME);
        AttributeValue width = new AttributeValue();
        width.setAttrId(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_ID);
        width.setName(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_NAME);
        AttributeValue depth = new AttributeValue();
        depth.setAttrId(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_ID);
        depth.setName(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_NAME);
        AttributeValue weight = new AttributeValue();
        weight.setAttrId(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_ID);
        weight.setName(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_NAME);
        if (masterDataExtensionAttributes != null && masterDataExtensionAttributes.size() > 0) {
            for (MasterDataExtensionAttribute masterDataExtensionAttribute : masterDataExtensionAttributes) {
                if(masterDataExtensionAttribute.getKey().getAttributeId() == ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE){
                    height.setDetails(masterDataExtensionAttribute.getAttributeValueText());
                    //height.setDescription(item.getRetailHeightDescription());
                }else if(masterDataExtensionAttribute.getKey().getAttributeId() == ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE){
                    width.setDetails(masterDataExtensionAttribute.getAttributeValueText());
                    //width.setDescription(item.getRetailWidthDescription());
                }else if(masterDataExtensionAttribute.getKey().getAttributeId() == ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE){
                    depth.setDetails(masterDataExtensionAttribute.getAttributeValueText());
                    //depth.setDescription(item.getRetailLengthDescription());
                }else if(masterDataExtensionAttribute.getKey().getAttributeId() == ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE){
                    weight.setDetails(masterDataExtensionAttribute.getAttributeValueText());
                    //weight.setDescription(item.getRetailWeightDescription());
                }
            }
            if (StringUtils.isNotEmpty(height.getDetails()) || StringUtils.isNotEmpty(width.getDetails())
                    || StringUtils.isNotEmpty(depth.getDetails()) || StringUtils.isNotEmpty(weight.getDetails())) {
                returnList.add(height);
                returnList.add(width);
                returnList.add(depth);
                returnList.add(weight);
            }
        }
    }

    /**
     * Get dimension information of gs1 source.
     *
     * @param upc - The upc
     * @param sourceSystem - The source system
     * @param returnList - The list of dimension
     */
    private void getDimensionDataByGs1Source(Long upc, Long sourceSystem, List<AttributeValue> returnList) {
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository
                .findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeId(upc, ProductECommerceViewService.UPC_ITEM_KEY_CODE, sourceSystem.intValue());
        AttributeValue height = new AttributeValue();
        height.setAttrId(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_ID);
        height.setName(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_NAME);
        AttributeValue width = new AttributeValue();
        width.setAttrId(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_ID);
        width.setName(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_NAME);
        AttributeValue depth = new AttributeValue();
        depth.setAttrId(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_ID);
        depth.setName(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_NAME);
        AttributeValue weight = new AttributeValue();
        weight.setAttrId(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_ID);
        weight.setName(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_NAME);
        if (masterDataExtensionAttributes != null && masterDataExtensionAttributes.size() > 0) {
            for (MasterDataExtensionAttribute masterDataExtensionAttribute : masterDataExtensionAttributes) {
                if (masterDataExtensionAttribute.getKey().getAttributeId() == 1620) {
                    if (masterDataExtensionAttribute.getAttributeValueNumber() > 0) {
                        height.setDetails(String.valueOf(masterDataExtensionAttribute.getAttributeValueNumber()));
                    }
                    //height.setDescription(item.getRetailHeightDescription());
                } else if (masterDataExtensionAttribute.getKey().getAttributeId() == 1622) {
                    if (masterDataExtensionAttribute.getAttributeValueNumber() > 0) {
                        width.setDetails(String.valueOf(masterDataExtensionAttribute.getAttributeValueNumber()));
                    }
                    //width.setDescription(item.getRetailWidthDescription());
                } else if (masterDataExtensionAttribute.getKey().getAttributeId() == 1624) {
                    if (masterDataExtensionAttribute.getAttributeValueNumber() > 0) {
                        depth.setDetails(String.valueOf(masterDataExtensionAttribute.getAttributeValueNumber()));
                    }
                    //depth.setDescription(item.getRetailLengthDescription());
                } else if (masterDataExtensionAttribute.getKey().getAttributeId() == 1626) {
                    if (masterDataExtensionAttribute.getAttributeValueNumber() > 0) {
                        weight.setDetails(String.valueOf(masterDataExtensionAttribute.getAttributeValueNumber()));
                    }
                    //weight.setDescription(item.getRetailWeightDescription());
                }
            }
            if ((StringUtils.isNotEmpty(height.getDetails()) && Double.valueOf(height.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(width.getDetails()) && Double.valueOf(width.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(depth.getDetails()) && Double.valueOf(depth.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(weight.getDetails()) && Double.valueOf(weight.getDetails()) > 0)) {
                returnList.add(height);
                returnList.add(width);
                returnList.add(depth);
                returnList.add(weight);
            }
        }
    }

    /**
     * Get dimension information of gladson source.
     *
     * @param upc - The upc
     * @param sourceSystem - The source system
     * @param returnList - The list of dimension
     */
    private void getDimensionDataByGladsonSource(Long upc, Long sourceSystem, List<AttributeValue> returnList) {
        List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository.findByKeyScanCodeIdAndSourceSystem(upc, sourceSystem.intValue());
        if (productScanCodeExtents != null && productScanCodeExtents.size() > 0) {
            AttributeValue height = new AttributeValue();
            height.setAttrId(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_ID);
            height.setName(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_NAME);
            AttributeValue width = new AttributeValue();
            width.setAttrId(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_ID);
            width.setName(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_NAME);
            AttributeValue depth = new AttributeValue();
            depth.setAttrId(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_ID);
            depth.setName(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_NAME);
            AttributeValue weight = new AttributeValue();
            weight.setAttrId(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_ID);
            weight.setName(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_NAME);
            for (ProductScanCodeExtent productScanCodeExtent : productScanCodeExtents) {
                if ("HEIGH".equalsIgnoreCase(productScanCodeExtent.getKey().getProdExtDataCode().trim())) {
                    height.setDetails(productScanCodeExtent.getProdDescriptionText());
                    //height.setDescription(item.getRetailHeightDescription());
                } else if ("WIDTH".equalsIgnoreCase(productScanCodeExtent.getKey().getProdExtDataCode().trim())) {
                    width.setDetails(productScanCodeExtent.getProdDescriptionText());
                    //width.setDescription(item.getRetailWidthDescription());
                } else if ("DEPTH".equalsIgnoreCase(productScanCodeExtent.getKey().getProdExtDataCode().trim())) {
                    depth.setDetails(productScanCodeExtent.getProdDescriptionText());
                    //depth.setDescription(item.getRetailLengthDescription());
                } else if ("WEIGH".equalsIgnoreCase(productScanCodeExtent.getKey().getProdExtDataCode().trim())) {
                    weight.setDetails(productScanCodeExtent.getProdDescriptionText());
                    //weight.setDescription(item.getRetailWeightDescription());
                }
            }
            if (StringUtils.isNotEmpty(height.getDetails()) || StringUtils.isNotEmpty(width.getDetails())
                    || StringUtils.isNotEmpty(depth.getDetails()) || StringUtils.isNotEmpty(weight.getDetails())) {
                returnList.add(height);
                returnList.add(width);
                returnList.add(depth);
                returnList.add(weight);
            }
        }
    }

    /**
     * Get dimension information of product maintenance source.
     *
     * @param productId - The product id
     * @param returnList - The list of dimension
     */
    private void getDimensionDataByProductMaintenanceSource(Long productId, List<AttributeValue> returnList) {
        GoodsProduct goodsProduct = this.goodsProductRepository.getOne(productId);
        if (goodsProduct != null) {
            AttributeValue height = new AttributeValue();
            height.setAttrId(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_ID);
            height.setName(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_NAME);
            if (goodsProduct.getRetailHeight() > 0) {
                height.setDetails(String.valueOf(goodsProduct.getRetailHeight()));
            }
            //height.setDescription(item.getRetailHeightDescription());
            AttributeValue width = new AttributeValue();
            width.setAttrId(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_ID);
            width.setName(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_NAME);
            if (goodsProduct.getRetailWidth() > 0) {
                width.setDetails(String.valueOf(goodsProduct.getRetailWidth()));
            }
            //width.setDescription(item.getRetailWidthDescription());
            AttributeValue depth = new AttributeValue();
            depth.setAttrId(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_ID);
            depth.setName(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_NAME);
            if (goodsProduct.getRetailLength() > 0) {
                depth.setDetails(String.valueOf(goodsProduct.getRetailLength()));
            }
            //depth.setDescription(item.getRetailLengthDescription());
            AttributeValue weight = new AttributeValue();
            weight.setAttrId(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_ID);
            weight.setName(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_NAME);
            if (goodsProduct.getRetailWeight() > 0) {
                weight.setDetails(String.valueOf(goodsProduct.getRetailWeight()));
            }
            //weight.setDescription(item.getRetailWeightDescription());
            if ((StringUtils.isNotEmpty(height.getDetails()) && Double.valueOf(height.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(width.getDetails()) && Double.valueOf(width.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(depth.getDetails()) && Double.valueOf(depth.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(weight.getDetails()) && Double.valueOf(weight.getDetails()) > 0)) {
                returnList.add(height);
                returnList.add(width);
                returnList.add(depth);
                returnList.add(weight);
            }
        }
    }

    /**
     * Get dimension information of cps source.
     *
     * @param upc - The upc
     * @param returnList - The list of dimension
     */
    private void getDimensionDataByCpsSource(Long upc, List<AttributeValue> returnList) {
        SellingUnit sellingUnit = this.sellingUnitRepository.getOne(upc);
        if (sellingUnit != null) {
            AttributeValue height = new AttributeValue();
            height.setAttrId(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_ID);
            height.setName(ProductECommerceViewService.DIMENSION_HEIGHT_ATTRIBUTE_NAME);
            if (sellingUnit.getRetailHeight() > 0) {
                height.setDetails(String.valueOf(sellingUnit.getRetailHeight()));
            }
            //height.setDescription(item.getRetailHeightDescription());
            AttributeValue width = new AttributeValue();
            width.setAttrId(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_ID);
            width.setName(ProductECommerceViewService.DIMENSION_WIDTH_ATTRIBUTE_NAME);
            if (sellingUnit.getRetailWidth() > 0) {
                width.setDetails(String.valueOf(sellingUnit.getRetailWidth()));
            }
            //width.setDescription(item.getRetailWidthDescription());
            AttributeValue depth = new AttributeValue();
            depth.setAttrId(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_ID);
            depth.setName(ProductECommerceViewService.DIMENSION_DEPTH_ATTRIBUTE_NAME);
            if (sellingUnit.getRetailLength() > 0) {
                depth.setDetails(String.valueOf(sellingUnit.getRetailLength()));
            }
            //depth.setDescription(item.getRetailLengthDescription());
            AttributeValue weight = new AttributeValue();
            weight.setAttrId(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_ID);
            weight.setName(ProductECommerceViewService.DIMENSION_WEIGHT_ATTRIBUTE_NAME);
            if (sellingUnit.getRetailWeight() > 0) {
                weight.setDetails(String.valueOf(sellingUnit.getRetailWeight()));
            }
            //weight.setDescription(item.getRetailWeightDescription());
            if ((StringUtils.isNotEmpty(height.getDetails()) && Double.valueOf(height.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(width.getDetails()) && Double.valueOf(width.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(depth.getDetails()) && Double.valueOf(depth.getDetails()) > 0)
                    || (StringUtils.isNotEmpty(weight.getDetails()) && Double.valueOf(weight.getDetails()) > 0)) {
                returnList.add(height);
                returnList.add(width);
                returnList.add(depth);
                returnList.add(weight);
            }
        }
    }

    /**
     * Find all the list of pdp template for eCommerce View screen.
     *
     * @return List<PDPTemplate> - The list of pdp template
     */
    public List<PDPTemplate> findAllPDPTemplate() {
        return this.pdpTemplateRepository.findAll(PDPTemplate.getDefaultSort());
    }

    /**
     * Find all the list of product panel type for eCommerce View screen.
     *
     * @return List<ProductPanelType> - The list of product panel type
     */
    public List<ProductPanelType> findAllProductPanelTypes() {
        return this.productPanelTypeRepository.findAll(ProductPanelType.getDefaultSort());
    }

    /**
     * Find all the list of sale channel for eCommerce View screen.
     *
     * @return List<SalesChannel> - The list of sale channel
     */
    public List<SalesChannel> findAllSaleChannels() {
        return this.salesChannelRepository.findAll(SalesChannel.getDefaultSort());
    }

    /**
     * Get information for eCommerce View screen. Load all basic information.
     *
     * @param productId - The product id
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    public ECommerceViewDetails getECommerceViewInformation(long productId, long upc) throws Exception {
        ECommerceViewDetails eCommerceViewDetails = new ECommerceViewDetails();
        //get published information(published date, published by)
        PublicationAudit publicationAudit = this.publicationAuditRepository.findTop1ByKeyItemProductIdOrderByKeyPublishDateDesc(productId);
        if (publicationAudit != null) {
            eCommerceViewDetails.setPublishedDate(publicationAudit.getKey().getPublishDate());
            List<User> users = ldapSearchRepository.getUserList(new ArrayList<>(Arrays.asList(publicationAudit.getPublishBy())));
            if (users != null && !users.isEmpty()) {
                eCommerceViewDetails.setPublishedBy(users.get(0).getFullName().concat(" [").concat(publicationAudit.getPublishBy()).concat("]"));
            } else {
                eCommerceViewDetails.setPublishedBy(publicationAudit.getPublishBy());
            }
        }
        //get SSA Count
        ProductAssortment productAssortment = null;
        try {
            productAssortment = this.productAssortmentServiceClient.getAssortmentByProductId(String
                    .valueOf(productId));
        } catch (CheckedSoapException e) {
            //log the error but not stop getting other information
            ProductECommerceViewService.logger.error(e.getMessage());
        }
        if (productAssortment != null) {
            eCommerceViewDetails.setStoreCount(productAssortment.getStoreCount());
        }
        //get pdp template
        MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(LogicAttributeCode
                        .PDP_TEMPLATE_LOGIC_ATTRIBUTE_ID.value, productId, ProductECommerceViewService
                        .PRODUCT_ITEM_KEY_CODE, SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.value);
        if (masterDataExtensionAttribute != null) {
            eCommerceViewDetails.setPdpTemplateId(masterDataExtensionAttribute.getAttributeValueText());
        }
        //get snipes
        List<ProductScanCodeNutrient> productScanCodeNutrients = productScanCodeNutrientRepository.findByKeyScanCodeId(upc);
        if (productScanCodeNutrients != null && !productScanCodeNutrients.isEmpty()) {
            StringBuilder snipesBuilder = new StringBuilder();
            productScanCodeNutrients.forEach(p -> {
                if (p.getProductNutrientAttribute() != null) {
                    if (StringUtils.isNotBlank(snipesBuilder.toString())) {
                        snipesBuilder.append(", ");
                    }
                    snipesBuilder.append(p.getProductNutrientAttribute().getDescription());
                }
            });
            eCommerceViewDetails.setSnipes(snipesBuilder.toString());
        }
        return eCommerceViewDetails;
    }

    /**
     * Get more information for eCommerce View screen. Load all information by tab(sale channel).
     *
     * @param productId - The product id
     * @param upc       - The primary scan code
     * @param commodity - The commodity code.
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    public ECommerceViewDetails getMoreECommerceViewInformationByTab(long productId, long upc, String commodity, String
            saleChannel, String hierCntxtCd) throws Exception {
        ECommerceViewDetails eCommerceViewDetails = new ECommerceViewDetails();
        //get brand
        eCommerceViewDetails.setBrand(this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                    .BRAND_LOGIC_ATTRIBUTE_ID.value, productId, upc));
        //get display name
        eCommerceViewDetails.setDisplayName(this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                .DISPLAY_NAME_LOGIC_ATTRIBUTE_ID.value, productId, upc));
        //get size
        eCommerceViewDetails.setSize(this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                .SIZE_LOGIC_ATTRIBUTE_ID.value, productId, upc));

        //get the list of fulfillment chanel by sale channel
        eCommerceViewDetails.setFulfillmentChannels(this.fulfillmentChannelRepository.findByKeySalesChannelCodeOrderByDescription(saleChannel));
        //get eCommerce View Fulfillment channel
        eCommerceViewDetails.setProductFullfilmentChanels(this.productFullfilmentChanelRepository
                .findByKeyProductIdAndKeySalesChanelCodeAndExpirationDateGreaterThanEqual(productId, saleChannel,
                        LocalDate.now()));
        //get Show On Site
        ProductOnline productOnline = this.productOnlineRepository
                .findTop1ByKeyProductIdAndKeySaleChannelCodeAndShowOnSiteOrderByKeyEffectiveDateDesc(productId,
                        saleChannel, PRODUCT_SHOW_ON_SITE);
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository
                .findByKeyIdAndKeyDataSourceSystem(productId, SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue());
        if(masterDataExtensionAttributes != null && masterDataExtensionAttributes.size()>0){
            eCommerceViewDetails.setProductPublished(true);
        if (productOnline != null) {
                eCommerceViewDetails.setProductOnline(true);
            eCommerceViewDetails.setShowOnSiteStartDate(productOnline.getKey().getEffectiveDate());
            eCommerceViewDetails.setShowOnSiteEndDate(productOnline.getExpirationDate());
        }
        }

        //get customer hierarchy
        String primaryPath = StringUtils.EMPTY;
        GenericEntity genericEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(productId, PRODUCT_ITEM_KEY_CODE);
        if (genericEntity != null) {
            GenericEntityRelationship genericEntityRelationship = this.genericEntityRelationshipRepository
                    .findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(genericEntity.getId(), hierCntxtCd,
                            PRODUCT_PRIMARY_PATH);
            while (genericEntityRelationship != null && genericEntityRelationship.getParentRelationships() != null &&
                    !genericEntityRelationship.getParentRelationships().isEmpty()) {
				String description = GenericEntityDescription.EMPTY_DESCRIPTION;
				if(genericEntityRelationship.getGenericParentEntity().getDisplayNumber()>0){
					description=genericEntityRelationship.getParentDescription().getLongDescription();
				}else{
					description=genericEntityRelationship.getGenericParentEntity().getDisplayText();
				}
                if (StringUtils.isNotBlank(primaryPath)) {
					primaryPath = description.concat("&#8594").concat(primaryPath);
                } else {
					primaryPath = description;
                }
                genericEntityRelationship = this.genericEntityRelationshipRepository
                        .findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(genericEntityRelationship
                                .getKey().getParentEntityId(), hierCntxtCd, PRODUCT_PRIMARY_PATH);
            }
            eCommerceViewDetails.setCustomerHierarchyPrimaryPath(primaryPath);
        }
        //get image
        List<SellingUnit> sellingUnits = this.sellingUnitRepository.findByProdId(productId);
        if(sellingUnits != null){
            List<Long> upcs = sellingUnits.stream().map(sellingUnit -> sellingUnit.getUpc()).collect(Collectors.toList());
            saleChannel = saleChannel.equals(GOOGLE_SHOPPING_SALE_CHANEL) ? HEB_COM_SALE_CHANEL:saleChannel;
            this.longPopulator.populate(upcs, DEFAULT_ITEM_COUNT);
            List<ProductScanImageBanner> productScanImageBanners = this.productScanImageBannerRepository
                    .findByKeyIdInAndKeySalesChannelCode(upcs, StringUtils.trim(saleChannel));
            //set primary image
            if (productScanImageBanners != null) {
                eCommerceViewDetails.setImageAlternates(new ArrayList<>());
                productScanImageBanners.forEach(p -> {
                    if (p.getProductScanImageURI() != null && IMAGE_APPROVE_CODE.equalsIgnoreCase(StringUtils.trim(p
                            .getProductScanImageURI().getImageStatusCode())) && p.getProductScanImageURI().isActiveOnline()) {
                        if (PRIMARY_IMAGE.equalsIgnoreCase(StringUtils.trim(p.getProductScanImageURI().getImagePriorityCode()))) {
                            eCommerceViewDetails.setImagePrimary(this.contentManagementServiceClient.getImage(p.getProductScanImageURI()
                                    .getImageURI()));
                            eCommerceViewDetails.setImagePrimaryFormat(p.getProductScanImageURI().getImageType().getImageFormat());
                        }
                        else if(AlTERNATES_IMAGE.equalsIgnoreCase(StringUtils.trim(p.getProductScanImageURI().getImagePriorityCode()))){
                            eCommerceViewDetails.getImageAlternates().add(p);
                        }
                    }
                });
            }
        }
        return eCommerceViewDetails;
    }

    /**
     * Get brand information.
     *
     * @param productId - The product id
     * @param upc       - The primary scan code
     * @param saleChannel - The sale channel.
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    public ECommerceViewDetails getBrandInformation(long productId, long upc, String saleChannel) throws Exception {
        ECommerceViewDetails eCommerceViewDetails = new ECommerceViewDetails();
        eCommerceViewDetails.setBrand(this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                    .BRAND_LOGIC_ATTRIBUTE_ID.value, productId, upc));
        return eCommerceViewDetails;
    }

    /**
     * Get display name information.
     *
     * @param productId - The product id
     * @param upc       - The primary scan code
     * @param saleChannel - The sale channel.
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    public ECommerceViewDetails getDisplayNameInformation(long productId, long upc, String saleChannel) throws Exception {
        ECommerceViewDetails eCommerceViewDetails = new ECommerceViewDetails();
        eCommerceViewDetails.setDisplayName(this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                    .DISPLAY_NAME_LOGIC_ATTRIBUTE_ID.value, productId, upc));
        return eCommerceViewDetails;
    }

    /**
     * Get size information.
     *
     * @param productId - The product id
     * @param upc       - The primary scan code
     * @param saleChannel - The sale channel.
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    public ECommerceViewDetails getSizeInformation(long productId, long upc, String saleChannel) throws Exception {
        ECommerceViewDetails eCommerceViewDetails = new ECommerceViewDetails();
        eCommerceViewDetails.setSize(this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                    .SIZE_LOGIC_ATTRIBUTE_ID.value, productId, upc));
        return eCommerceViewDetails;
    }

    /**
     * Find the eCommerce image by URI
     * @param imageURI - The image URI
     * @return ECommerceViewDetails
     */
    public byte[] findECommerceViewImageByUri(String imageURI){
        return this.contentManagementServiceClient.getImage(imageURI);
    }

    /**
     * Get the warnings by product id or upc with attribute id 1677.
     *
     * @param productId  the product id to find.
     * @param scanCodeId the scan code id to find.
     * @return the warnings.
     */
    public ECommerceViewAttributePriorities getWarningsByProductIdOrUpc(long productId, long scanCodeId) {
        return this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode.WARNING_LOGIC_ATTRIBUTE_ID.getValue(),
                productId, scanCodeId);
    }

    /**
     * Get the directions by product id or upc with attribute id 1676.
     *
     * @param productId  the product id to find.
     * @param scanCodeId the scan code id to find.
     * @return the directions.
     */
    public ECommerceViewAttributePriorities getDirectionsByProductIdOrUpc(long productId, long scanCodeId) {
        return this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode.DIRECTIONS_LOGIC_ATTRIBUTE_ID.getValue(),
                productId, scanCodeId);
    }

    /**
     * Get the ingredients by product id or upc with attribute id 1674.
     *
     * @param productId  the product id to find.
     * @param scanCodeId the scan code id to find.
     * @return the ingredients.
     */
    public ECommerceViewAttributePriorities getIngredientsByProductIdOrUpc(long productId, long scanCodeId) {
        return this.getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode.INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID.getValue(),
                productId, scanCodeId);
    }

    /**
     * Get eCommerce data source system by attribute id, upc/product id.
     *
     * @param attributeId - The attribute id,
     * @param productId   - The product id
     * @param upc         - The scan code number
     * @return ECommerceViewAttributePriorities
     */
    public ECommerceViewAttributePriorities getECommerceDataSystemByLogicAttributeAndUpc(Long attributeId, long
            productId, long upc) {

        return getEcommerceDataBasedOnPriority(attributeId, productId, upc, SourceSystemNumber
                .ECOMMERCE_SOURCE_SYSTEM_NUMBER.value);
    }

    /**
     * Check content source system login attributes priority.
     *
     * @param attributeId         - The attribute id
     * @param sourceSystemId      - The sourcce system id
     * @param physicalAttributeId - The physical attribute id
     * @param upc                 - The scan code id
     * @param productId           - The product id
     * @return ECommerceViewAttributePriorities
     */
    private ECommerceViewAttributePriorities checkLogicDataSourceByByLogicAttributePriorities(Long attributeId, Long
            sourceSystemId, Long physicalAttributeId, Long upc, Long productId) {
        ECommerceViewAttributePriorities eCommerceViewAttributePriorities = new ECommerceViewAttributePriorities();
        switch (LogicAttributeCode.getAttribute(attributeId)) {
            case BRAND_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities.setContent(this.getDataSourceSystemForBrand(sourceSystemId, physicalAttributeId, upc, productId));
                eCommerceViewAttributePriorities.setSourceSystemId(sourceSystemId);
                break;
            case NUTRITION_FACTS_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities.setSourceSystemId(sourceSystemId);
                if (sourceSystemId == SourceSystemNumber.SCALE_MANAGEMENT_SOURCE_SYSTEM_NUMBER.value) {
                    ScaleUpc scaleUpc = this.getNutritionFactByScaleSource(upc);
                    if (scaleUpc != null) {
                        eCommerceViewAttributePriorities.setScaleUpc(scaleUpc);
                        eCommerceViewAttributePriorities.setContent(sourceSystemId.toString());
                    }
                }else{
                    List<ProductNutrient> productNutrientList = this.getNutritionFactDataBySourceSystem(productId, upc, sourceSystemId);
                    if(productNutrientList != null && productNutrientList.size() > 0) {
                        eCommerceViewAttributePriorities.setProductNutrients(productNutrientList);
                        eCommerceViewAttributePriorities.setContent(sourceSystemId.toString());
                    }
                    List<ProductPkVariation> productPkVariationList = this.getNutritionFactHeaderBySourceSystem(productId, upc, sourceSystemId);
                    if(productPkVariationList != null && productPkVariationList.size() > 0) {
                        eCommerceViewAttributePriorities.setProductPkVariations(productPkVariationList);
                        eCommerceViewAttributePriorities.setContent(sourceSystemId.toString());
                    }
                }
                break;
            case DIMENSIONS_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities.setSourceSystemId(sourceSystemId);
                List<AttributeValue> dimensionList = this.getDimensionBySourceSystem(productId, upc, sourceSystemId);
                if (dimensionList != null && dimensionList.size() > 0) {
                    eCommerceViewAttributePriorities.setDimensions(dimensionList);
                    eCommerceViewAttributePriorities.setContent(sourceSystemId.toString());
                }
                break;
            case SPECIFICATION_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities.setSourceSystemId(sourceSystemId);
                List<AttributeValue> specificationList = this.getSpecificationBySourceSystem(productId, upc, sourceSystemId);
                if (specificationList != null && specificationList.size() > 0) {
                    eCommerceViewAttributePriorities.setSpecifications(specificationList);
                    eCommerceViewAttributePriorities.setContent(sourceSystemId.toString());
                }
                break;
            case WARNING_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities = this.getWarningsByAttributePriorities(physicalAttributeId, sourceSystemId, upc);
                break;
            case DIRECTIONS_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities = this.getDirectionsByAttributePriorities(physicalAttributeId, sourceSystemId, upc);
                break;
            case INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities = this.getIngredientsByAttributePriorities(physicalAttributeId, sourceSystemId, upc);
                break;
            case DISPLAY_NAME_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities.setContent(this.getDataSourceSystemForDisplayName(sourceSystemId,
                        physicalAttributeId, upc, productId));
                eCommerceViewAttributePriorities.setSourceSystemId(sourceSystemId);
                break;
            case SIZE_LOGIC_ATTRIBUTE_ID:
                eCommerceViewAttributePriorities.setContent(this.getDataSourceSystemForSize(sourceSystemId,
                        physicalAttributeId, upc, productId));
                eCommerceViewAttributePriorities.setSourceSystemId(sourceSystemId);
                break;
            default:
                break;
        }
        eCommerceViewAttributePriorities.setPhysicalAttributeId(physicalAttributeId);
        return eCommerceViewAttributePriorities;
    }

    /**
     * Get content source for brand -> for case data not exist in source 13, but exist in product override.
     *
     * @param sourceSystemId      - The source system id
     * @param physicalAttributeId - The physical attribute id
     * @param upc                 - The scan code number
     * @param productId           - The product id
     * @return The content {String}
     */
    private String getDataSourceSystemForBrand(Long sourceSystemId, Long physicalAttributeId, Long upc, Long productId) {
        StringBuilder contentBuilder = new StringBuilder();
        if (sourceSystemId.equals(SourceSystemNumber.GLADSON_SOURCE_SYSTEM_NUMBER.value)) {
            List<ProductScanCodeExtent> productScanCodeExtents = null;
            if (physicalAttributeId.equals(Long.valueOf(312))) {
                productScanCodeExtents = this.productScanCodeExtentRepository
                        .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                (ProductScanCodeExtent.BRAND_DIMENSION_CODE, ProductScanCodeExtent
                                        .PLINE_DIMENSION_CODE)));
            } else if (physicalAttributeId.equals(Long.valueOf(1602))) {
                productScanCodeExtents = this.productScanCodeExtentRepository
                        .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                (ProductScanCodeExtent.BRAND_DIMENSION_CODE)));
            } else if (physicalAttributeId.equals(Long.valueOf(1612))) {
                productScanCodeExtents = this.productScanCodeExtentRepository
                        .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                (ProductScanCodeExtent.PLINE_DIMENSION_CODE)));
            }
            //Check data exist, and return to content text
            if (productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                productScanCodeExtents.forEach(p -> {
                    if (StringUtils.isNotBlank(contentBuilder.toString())) {
                        contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                    }
                    if (p.getProdDescriptionText() != null) {
                        contentBuilder.append(p.getProdDescriptionText());
                    }
                });
            }
        } else if (sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.value)) {
            if (physicalAttributeId.equals(Long.valueOf(523))) {
                ProductMaster productMaster = productInfoRepository.findBySellingUnitsUpc(upc);
                if (productMaster != null && productMaster.getProductBrand() != null && productMaster.getProductBrand().getDisplayName() != null) {
                    contentBuilder.append(productMaster.getProductBrand().getDisplayName());
                }
            } else if (physicalAttributeId.equals(Long.valueOf(1642))) {
                MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                        .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, SourceSystemNumber
                                .PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.value);
                if (masterDataExtensionAttribute != null && masterDataExtensionAttribute.getAttributeValueText() != null) {
                    contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
                }
            }
        } else {
            MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                    .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, sourceSystemId);
            if (masterDataExtensionAttribute != null && masterDataExtensionAttribute.getAttributeValueText() != null) {
                contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
            }
        }
        return contentBuilder.toString();
    }

    /**
     * Get content source for display name -> for case data not exist in source 13, but exist in product override.
     *
     * @param sourceSystemId      - The source system id
     * @param physicalAttributeId - The physical attribute id
     * @param upc                 - The scan code number
     * @param productId           - The product id
     * @return The content {String}
     */
    private String getDataSourceSystemForDisplayName(Long sourceSystemId, Long physicalAttributeId, Long upc, Long
            productId) {
        StringBuilder contentBuilder = new StringBuilder();
        if (sourceSystemId.equals(SourceSystemNumber.GLADSON_SOURCE_SYSTEM_NUMBER.value)) {
            List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository
                    .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                            (ProductScanCodeExtent.IDESC_DIMENSION_CODE)));
            //Check data exist, and return to content text
            if (productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                productScanCodeExtents.forEach(p -> {
                    if (StringUtils.isNotBlank(contentBuilder.toString())) {
                        contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                    }
                    if (p.getProdDescriptionText() != null) {
                        contentBuilder.append(p.getProdDescriptionText());
                    }
                });
            }
        } else if (sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.value)) {
            if (physicalAttributeId.equals(Long.valueOf(310))) {
                List<ProductDescription> productDescriptions = this.productDescriptionRepository
                        .findByKeyProductIdAndAndKeyDescriptionTypeInAndKeyLanguageType(productId, new ArrayList<>
                                        (Arrays.asList(PRODUCT_DESCRIPTION_TEXT_TAG1, PRODUCT_DESCRIPTION_TEXT_TAG2))
                                , PRODUCT_DESCRIPTION_TEXT_LANGUAGE);
                productDescriptions.forEach(p -> {
                    if (StringUtils.isNotBlank(contentBuilder.toString())) {
                        contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                    }
                    if (p.getDescription() != null) {
                        contentBuilder.append(p.getDescription());
                    }
                });
            } else if (physicalAttributeId.equals(Long.valueOf(1601))) {
                List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository
                        .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                (ProductScanCodeExtent.ESHRT_DIMENSION_CODE)));
                //Check data exist, and return to content text
                if (productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                    productScanCodeExtents.forEach(p -> {
                        if (StringUtils.isNotBlank(contentBuilder.toString())) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                        }
                        if (p.getProdDescriptionText() != null) {
                            contentBuilder.append(p.getProdDescriptionText());
                        }
                    });
                }
            }
        } else {
            MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                    .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, sourceSystemId);
            if (masterDataExtensionAttribute != null && masterDataExtensionAttribute.getAttributeValueText() != null) {
                contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
            }
        }
        return contentBuilder.toString();
    }

    /**
     * Get content source for size -> for case data not exist in source 13, but exist in product override.
     *
     * @param sourceSystemId      - The source system id
     * @param physicalAttributeId - The physical attribute id
     * @param upc                 - The scan code number
     * @param productId           - The product id
     * @return The content {String}
     */
    private String getDataSourceSystemForSize(Long sourceSystemId, Long physicalAttributeId, Long upc, Long productId) {
        StringBuilder contentBuilder = new StringBuilder();
        switch (SourceSystemNumber.getNumber(sourceSystemId)) {
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                List<ProductScanCodeExtent> productScanCodeExtents = null;
                if (physicalAttributeId.equals(Long.valueOf(1606))) {
                    productScanCodeExtents = this.productScanCodeExtentRepository
                            .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                    (ProductScanCodeExtent.ESIZE_DIMENSION_CODE)));
                } else {
                    productScanCodeExtents = this.productScanCodeExtentRepository
                            .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                    (ProductScanCodeExtent.ISIZE_DIMENSION_CODE, ProductScanCodeExtent.IUOM_DIMENSION_CODE)));
                }
                //Check data exist, and return to content text
                if (productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                    productScanCodeExtents.forEach(p -> {
                        if (StringUtils.isNotBlank(contentBuilder.toString())) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                        }
                        if (p.getProdDescriptionText() != null) {
                            contentBuilder.append(p.getProdDescriptionText());
                        }
                    });
                }
                break;
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
                if (physicalAttributeId.equals(Long.valueOf(313))) {
                    SellingUnit sellingUnit = sellingUnitRepository.findOne(upc);
                    if (sellingUnit != null) {
                        if (sellingUnit.getQuantity() != null) {
                            contentBuilder.append(sellingUnit.getQuantity());
                        }
                        if (sellingUnit.getRetailUnitOfMeasure() != null && sellingUnit.getRetailUnitOfMeasure().getDescription() != null) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR).append(sellingUnit.getRetailUnitOfMeasure().getDescription());
                        }
                    }
                } else if (physicalAttributeId.equals(Attribute.SIZE)) {
                    MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                            .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, sourceSystemId);
                    if (masterDataExtensionAttribute != null && masterDataExtensionAttribute.getAttributeValueText() != null) {
                        contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
                    }
                } else if (physicalAttributeId.equals(Long.valueOf(591))) {
                    SellingUnit sellingUnit = sellingUnitRepository.findOne(upc);
                    if (sellingUnit != null && sellingUnit.getTagSize() != null) {
                        contentBuilder.append(sellingUnit.getTagSize());
                    }
                }
                break;
            case GS1_SOURCE_SYSTEM_NUMBER:
			List<Long> attributesIds = new ArrayList<>(Arrays.asList(Attribute.NET_CONTENT, Attribute.NET_CONTENT_UNIT_OF_MEASURE));
			this.longPopulator.populate(attributesIds, DEFAULT_ITEM_COUNT);
			List<MasterDataExtensionAttribute> masterDataExtensionAttributes1 = this
                        .masterDataExtensionAttributeRepository.findByKeyAttributeIdInAndKeyIdAndKeyDataSourceSystemOrderByKeyAttributeId(attributesIds, upc, sourceSystemId);
                //Check data exist, and return to content text
                if (masterDataExtensionAttributes1 != null && !masterDataExtensionAttributes1.isEmpty()) {
                    masterDataExtensionAttributes1.forEach(m -> {
                        if (StringUtils.isNotBlank(contentBuilder.toString())) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                        }
                        if(m.getKey().getAttributeId().equals(Attribute.NET_CONTENT)) {
                            if (m.getAttributeValueNumber() != null) {
                                contentBuilder.append(m.getAttributeValueNumber());
                            }
                        }else{
                            if (m.getAttributeValueText() != null) {
                                contentBuilder.append(m.getAttributeValueText());
                            }
                        }
                    });
                }
                break;
            case KWIKEE_SOURCE_SYSTEM_NUMBER:
            List<Long> listAttributesId = new ArrayList<>(Arrays.asList(Attribute.SIZE, Attribute.PRODUCT_UOM));
            this.longPopulator.populate(listAttributesId, DEFAULT_ITEM_COUNT);
			List<MasterDataExtensionAttribute> masterDataExtensionAttributes2 = this
                        .masterDataExtensionAttributeRepository.findByKeyAttributeIdInAndKeyIdAndKeyDataSourceSystemOrderByKeyAttributeId(listAttributesId, upc, sourceSystemId);
                //Check data exist, and return to content text
                if (masterDataExtensionAttributes2 != null && !masterDataExtensionAttributes2.isEmpty()) {
                    masterDataExtensionAttributes2.forEach(m -> {
                        if (StringUtils.isNotBlank(contentBuilder.toString())) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                        }
                        if (m.getAttributeValueText() != null) {
                            contentBuilder.append(m.getAttributeValueText());
                        }
                    });
                }
                break;
            default:
                MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                        .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, sourceSystemId);
                if (masterDataExtensionAttribute != null && masterDataExtensionAttribute.getAttributeValueText() != null) {
                    contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
                }
                break;
        }
        return contentBuilder.toString();
    }

    /**
     * Gets the cust hier assign.
     *
     * @param customerHierarchyAssigment CustomerHierarchyAssigment
     * @return vn55306
     */
    public CustomerHierarchyAssigment getCustomHierarchyAssigment(CustomerHierarchyAssigment customerHierarchyAssigment) {
        logger.info("getCustomHierarchyAssigment");
        this.customerHierarchyAssigmentService.setPathTree(customerHierarchyAssigment.getCustomerHierarchyContext().getChildRelationships(),null,null,null);
        CustomerHierarchyAssigment customerHierarchyAssigmentPath = new CustomerHierarchyAssigment();
        List<GenericEntityRelationship> lowestLevelBySubComm = null;
        List<GenericEntityRelationship> lowestLevelByByCurrentPath = null;
        List<GenericEntityRelationship> lowestLevelByBrick = null;
        lowestLevelBySubComm = this.findEntityBySubCommodity(customerHierarchyAssigment.getSubCommodity(),customerHierarchyAssigment.getHierachyContextCode());
        List<Long> productCurrentPath = new ArrayList<Long>();
        productCurrentPath.add(customerHierarchyAssigment.getProductId());
//		SubCommodityKey key = new SubCommodityKey();
//		key.setSubCommodityCode(customerHierarchyAssigment.getSubCommodity());
//		key.setClassCode(customerHierarchyAssigment.getClassCode());
//		key.setCommodityCode(customerHierarchyAssigment.getCommodityCode());
        List<SubCommodity> subcommodities = subCommodityRepository.findByKeySubCommodityCodeAndSubCommodityActive(customerHierarchyAssigment.getSubCommodity(), 'A');
        customerHierarchyAssigmentPath.setCustomerHierarchyContext(customerHierarchyAssigment.getCustomerHierarchyContext());
        customerHierarchyAssigmentPath.setProductId(customerHierarchyAssigment.getProductId());
        customerHierarchyAssigmentPath.setHierachyContextCode(customerHierarchyAssigment.getHierachyContextCode());
        if (subcommodities != null) {
            customerHierarchyAssigmentPath.setSubCommodityName(SUB_COMMODITY + subcommodities.get(0).getName());
        }
        this.longPopulator.populate(productCurrentPath, DEFAULT_ITEM_COUNT);
        lowestLevelByByCurrentPath = this.genericEntityRelationshipRepository.findByCurrentHierarchy(productCurrentPath, customerHierarchyAssigment.getHierachyContextCode(), GenericEntity.EntyType.PROD.getName());
        if (customerHierarchyAssigment.getUpc() != null) {
            Long brick = this.genericEntityRelationshipRepository.findByGenericChildEntityDisplayNumberAndHierarchyContextAndGenericChildEntityType(customerHierarchyAssigment.getUpc(), HierarchyContext.HierarchyContextCode.GS1.getName(), GenericEntity.EntyType.UPC.getName());
            if (brick != null) {
                List<Long> upcs = null;
                List<Long> productIdBrick = null;
                GenericEntityDescriptionKey genericEntityDescriptionKey = new GenericEntityDescriptionKey();
                genericEntityDescriptionKey.setEntityId(brick);
                genericEntityDescriptionKey.setHierarchyContext(HierarchyContext.HierarchyContextCode.GS1.getName());
                GenericEntityDescription genericEntityDescription = this.genericEntityDescriptionRespository.findOne(genericEntityDescriptionKey);
                if (genericEntityDescription != null) {
                    customerHierarchyAssigmentPath.setBrick(BRICK + genericEntityDescription.getShortDescription());
                }
//                // FIND ALL CHILD BY PARENT ID
                upcs = this.findUpcByBrick(brick);
                if(upcs!=null && upcs.size() > 0) {
                    productIdBrick = this.findProductIdByUpc(upcs);
                    if (productIdBrick != null && productIdBrick.size() > 0) {
                        lowestLevelByBrick = this.findGenericEntityRelationshipByBrick(productIdBrick,customerHierarchyAssigment.getHierachyContextCode());
                    }
                }
            }
        }
        this.customerHierarchyAssigmentService.findCustomerHierarchyAssignmentPath(customerHierarchyAssigmentPath, customerHierarchyAssigment.getCustomerHierarchyContext().getChildRelationships(), lowestLevelByByCurrentPath, lowestLevelBySubComm, lowestLevelByBrick);
        return customerHierarchyAssigmentPath;
    }

    /**
     * find Entity By Brick.
     * @param brick the sub Commodity code
     * @return vn55306
     */
    public List<Long> findUpcByBrick(Long brick){
        List<Long> displayNumbers = new ArrayList<Long>();
        int start = 0;
        int end = 0;
        int totalPage=0;
        List<Long> childEntys = this.genericEntityRelationshipRepository.findBrickChildEntityIdByKeyParentEntityIdAndHierarchyContext(brick, HierarchyContext.HierarchyContextCode.GS1.getName());
        if (childEntys != null && childEntys.size() > 0) {
            totalPage =  childEntys.size() / LIMIT_QUERY_IN +1;
            for (int page = 0; page < totalPage; page++) {
                start = LIMIT_QUERY_IN * page;
                end = LIMIT_QUERY_IN * page + LIMIT_QUERY_IN;
                if (end > childEntys.size()) {
                    end = childEntys.size();
                }
                displayNumbers.addAll(this.genericEntityRepository.findDisplayNumberByIdyAndType(childEntys.subList(start, end), GenericEntity.EntyType.UPC.getName()));
            }
        }
        return displayNumbers;
    }
    /**
     * Find HierarchyContext by Sub Commodity.
     * @param subCommodity the sub Commodity code
     * @param hierarchyContextId the hierarchyContextId
     * @return vn55306
     */
    public List<GenericEntityRelationship> findEntityBySubCommodity(Integer subCommodity,String hierarchyContextId){
        logger.info("findEntityBySubCommodity "+subCommodity+" -- "+hierarchyContextId);
        List<GenericEntityRelationship> lowestLevelBySubComm = new ArrayList<GenericEntityRelationship>();
        int start = 0;
        int end = 0;
        int totalPage=0;
        // Find Product ID by Sub Commodity
        List<Long> prodIds = this.productInfoRepository.findProductIdBySubCommodityCode(subCommodity);
        if(prodIds!=null && !prodIds.isEmpty()) {
            logger.info("prodIds "+prodIds.size());
            totalPage =  prodIds.size() / LIMIT_QUERY_IN +1;
            for (int page = 0; page <totalPage; page++) {
                start = LIMIT_QUERY_IN*page;
                end = LIMIT_QUERY_IN * page + LIMIT_QUERY_IN;
                if(end> prodIds.size()){
                    end=prodIds.size();
                }
                logger.info("start "+start+" end "+end);
                // NODE LOWEST LEVEL
                lowestLevelBySubComm.addAll(this.genericEntityRelationshipRepository.findByListDisplayNumberAndHierarchyContextAndGenericChildEntityType(prodIds.subList(start,end), hierarchyContextId, GenericEntity.EntyType.PROD.getName()));
            }
        }
        return lowestLevelBySubComm;
    }
    /**
     * find Product ID By UPC.
     * @param upcs the list UPC
     * @return vn55306
     */
    public List<Long> findProductIdByUpc(List<Long> upcs){
        int start = 0;
        int end = 0;
        int totalPage=0;
        List<Long> productIdBrick = new ArrayList<Long>();
        totalPage =  upcs.size()/LIMIT_QUERY_IN +1;
        for (int page = 0; page < totalPage; page++) {
            start = LIMIT_QUERY_IN * page;
            end = LIMIT_QUERY_IN * page + LIMIT_QUERY_IN;
            if (end > upcs.size()) {
                end = upcs.size();
            }
            productIdBrick.addAll(this.productInfoRepository.findProductIdByProductPrimaryScanCodeId(upcs.subList(start,end)));
        }
        return productIdBrick;
    }
    /**
     * find GenericEntityRelationship By Brick.
     * @param hierachyContextCode the hierarchy Context Code
     * @param productIdBrick the productIdBrick
     * @return vn55306
     */
    public List<GenericEntityRelationship> findGenericEntityRelationshipByBrick(List<Long> productIdBrick,String hierachyContextCode){
        int start = 0;
        int end = 0;
        int totalPage =  productIdBrick.size() / LIMIT_QUERY_IN +1;
        List<GenericEntityRelationship> lowestLevelByBrick = new ArrayList<GenericEntityRelationship>();
        for (int page = 0; page < totalPage; page++) {
            start = LIMIT_QUERY_IN * page;
            end = LIMIT_QUERY_IN * page + LIMIT_QUERY_IN;
            if (end > productIdBrick.size()) {
                end = productIdBrick.size();
            }
            lowestLevelByBrick.addAll(genericEntityRelationshipRepository.findByListDisplayNumberAndHierarchyContextAndGenericChildEntityType(productIdBrick.subList(start, end), hierachyContextCode, GenericEntity.EntyType.PROD.getName()));
        }
        return lowestLevelByBrick;
    }
    /**
     * Find HierarchyContext by hierarchyContext Id.
     * @param hierarchyContextId the hierarchyContextId
     * @return vn55306
     */
    public HierarchyContext findById(String hierarchyContextId) {
        HierarchyContext hierarchyContext = this.hierarchyContextRepository.findOne(hierarchyContextId);
        hierarchyContext.setChildRelationships(this.objectResolver.fetch(this.findByHierarchyContext(hierarchyContext)));
        return hierarchyContext;
    }

    /**
     * This method returns all generic entity relationships matching a given hierarchy context's context and parent id.
     *
     * @param hierarchyContext
     * @return vn55306
     */
    public List<GenericEntityRelationship> findByHierarchyContext(HierarchyContext hierarchyContext) {
        return this.genericEntityRelationshipRepository.
                findByKeyParentEntityIdAndHierarchyContext(hierarchyContext.getParentEntityId(), hierarchyContext.getId());
    }

    /**
     * Get the product description by product id, upc and logicAttributeId.
     *
     * @param productId the id of product.
     * @param upc       the upc.
     * @return the ECommerceViewAttributePriorities object.
     */
    public ECommerceViewAttributePriorities findProductDescriptionByProductIdAndUpcAndLogAttrId(Long productId, Long upc) {
        return findDescriptionByProductIdAndUpcAndLogAttrId(productId, upc, LogicAttributeCode.ROMANCE_COPY_LOGIC_ATTRIBUTE_ID);
    }
    /**
     * Get the favor item description by product id, upc and logic Attribute Id.
     *
     * @param productId the id of product.
     * @param upc       the upc.
     * @return the ECommerceViewAttributePriorities object.
     */
    public ECommerceViewAttributePriorities findFavorItemDescriptionByProductId(Long productId, Long upc) {
        return findDescriptionByProductIdAndUpcAndLogAttrId(productId, upc, LogicAttributeCode.FAVOR_ITEM_DESCRIPTION_LOGIC_ATTRIBUTE_ID);
    }
    /**
     * Get the description by product id, upc and logicAttributeId.
     *
     * @param productId the id of product.
     * @param upc       the upc.
     * @param attributeCode       the LogicAttributeCode.
     * @return the ECommerceViewAttributePriorities object.
     */
    private ECommerceViewAttributePriorities findDescriptionByProductIdAndUpcAndLogAttrId(Long productId, Long upc, LogicAttributeCode attributeCode) {
        ECommerceViewAttributePriorities eCommerceViewAttributePriorities;
        MasterDataExtensionAttribute masterDataExtensionAttributeECommerce = null;
        // Case 1: Gets the data from eCommerce (13).
        if (productFullfilmentChanelRepository.countByKeyProductId(productId) > 0) {
            masterDataExtensionAttributeECommerce = this.masterDataExtensionAttributeRepository.findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(
                    attributeCode.getValue(), productId, PRODUCT_ITEM_KEY_CODE, SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue());
        }
        if (masterDataExtensionAttributeECommerce != null) {
            // Gets description from eCommerce (13).
            eCommerceViewAttributePriorities = ProductECommerceViewConverter.convertToECommerceViewAttributePrioritiesFromMasterDataExtensionAttribute(masterDataExtensionAttributeECommerce);
            eCommerceViewAttributePriorities.setMandatory(checkSourceSystemIsMandatory(productId, attributeCode.getValue(), upc));
        } else {
            // Case 2: Gets the data by Overwrite or Priority.
            // Gets the ProductAttributeOverwrite by product id for LOGIC attribute 1666.
            ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository.
                    findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(PRODUCT_ITEM_KEY_CODE,
                            productId, attributeCode.getValue());
            if (productAttributeOverwrite != null) {
                // Case Overwrite.
                eCommerceViewAttributePriorities = getDataByUpcAndAttrIdAndSourceSystemId(upc, attributeCode.getValue(), productAttributeOverwrite.getKey().getPhysicalAttributeId(), productAttributeOverwrite.getKey().getSourceSystemId(), true);
            } else {
                // Case Not overwrite. Gets the data by priority.
                eCommerceViewAttributePriorities = getProductDescriptionByPriority(upc, attributeCode.getValue().intValue());
            }
        }
        //find ALERT_STAGING_TBL
        eCommerceViewAttributePriorities.setDifferentWithDefaultValue(this.validateChangedPublishedSource(productId, attributeCode.getValue()));
        String romanceCopyContent = eCommerceViewAttributePriorities.getContent();
        eCommerceViewAttributePriorities.setHtmlContent(romanceCopyContent);
        if(StringUtils.isNotEmpty(romanceCopyContent)){
        	romanceCopyContent = romanceCopyContent.replaceAll(String.valueOf((char) 10), ProductECommerceViewService.BR_TAG);
            // erases all the ASCII control characters
            romanceCopyContent = romanceCopyContent.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", StringUtils.EMPTY);
        	eCommerceViewAttributePriorities.setContent(romanceCopyContent);
        }
        return eCommerceViewAttributePriorities;
    }

    /**
     * Get product Description by upc and order by AttributePriorityNumber asc.
     *
     * @param upc              the upc.
     * @param logicAttributeId the logicAttributeId.
     * @return the ECommerceViewAttributePriorities object.
     */
    private ECommerceViewAttributePriorities getProductDescriptionByPriority(long upc, int logicAttributeId) {
        boolean isFoundContent = false;
        ECommerceViewAttributePriorities eCommerceViewAttributePriorities = new ECommerceViewAttributePriorities();
        // Gets the list of targetSystemAttributePriorities by 1666 and order by AttributePriorityNumber asc.
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this.targetSystemAttributePriorityRepository.findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(logicAttributeId);
        for (TargetSystemAttributePriority item : targetSystemAttributePriorities) {
            eCommerceViewAttributePriorities = getDataByUpcAndAttrIdAndSourceSystemId(upc, logicAttributeId, item.getKey().getPhysicalAttributeId(), item.getKey().getDataSourceSystemId(), false);
            if (StringUtils.isNotBlank(eCommerceViewAttributePriorities.getContent())) {
                //Found a product description is not null.
                isFoundContent = true;
                break;
            }
        }
        // If it is not found data by priority source, then doesn't return any source.
        if(!isFoundContent) {
            eCommerceViewAttributePriorities.setSourceSystemId(null);
            eCommerceViewAttributePriorities.setPhysicalAttributeId(null);
        }
        return eCommerceViewAttributePriorities;
    }

    /**
     * Get the data by upc, physicalAttributeId, sourceSystemId and isOverwrite.
     *
     * @param upc                 the scan code id.
     * @param logicAttributeId the logicAttributeId.
     * @param physicalAttributeId the physicalAttributeId.
     * @param sourceSystemId      the sourceSystemId.
     * @param isOverwrite         if set true then get overwrite data or false then get data by priority.
     * @return the ECommerceViewAttributePriorities object.
     */
    private ECommerceViewAttributePriorities getDataByUpcAndAttrIdAndSourceSystemId(long upc, long logicAttributeId, long physicalAttributeId, long sourceSystemId, boolean isOverwrite) {
        ECommerceViewAttributePriorities eCommerceViewAttributePriorities = new ECommerceViewAttributePriorities();
        List<ProductScanCodeExtent> productScanCodeExtents;
        switch (SourceSystemNumber.getNumber(sourceSystemId)) {
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
                // Case 1: Get the product description from ProductManagement(4)
                if(logicAttributeId == LogicAttributeCode.ROMANCE_COPY_LOGIC_ATTRIBUTE_ID.getValue()) {
                    productScanCodeExtents = this.productScanCodeExtentRepository.findByKeyScanCodeIdAndKeyProdExtDataCodeIn(
                            upc, Arrays.asList(ProductScanCodeExtent.ELONG_PRODUCT_DESCRIPTION_CODE));
                    eCommerceViewAttributePriorities = ProductECommerceViewConverter.convertToECommerceViewAttributePrioritiesFromProductManagement(productScanCodeExtents);
                }else {
                    // Fetch Favor item description from product scan code.
                    productScanCodeExtents = this.productScanCodeExtentRepository.findByKeyScanCodeIdAndKeyProdExtDataCodeIn(
                            upc, Arrays.asList(ProductScanCodeExtent.FAVOR_ITEM_DESCRIPTION_CODE));
                    eCommerceViewAttributePriorities = ProductECommerceViewConverter.convertToECommerceViewAttributePrioritiesFromProductManagement(productScanCodeExtents);
                }
                break;
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                // Case 2: Get the product description from Gladson(7)
                productScanCodeExtents = this.productScanCodeExtentRepository.findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, Arrays.asList(
                        ProductScanCodeExtent.PDETL_PRODUCT_DESCRIPTION_CODE,
                        ProductScanCodeExtent.INDCN_PRODUCT_DESCRIPTION_CODE));
                eCommerceViewAttributePriorities = ProductECommerceViewConverter.convertToECommerceViewAttributePrioritiesFromGladson(productScanCodeExtents);
                break;
            default:
                // Case 3: Get the product description from MasterDataExtensionAttribute for left sources .
                MasterDataExtensionAttribute masterDataExtensionAttribute;
                if (isOverwrite) {
                    // Gets the data by overwrite.
                    masterDataExtensionAttribute = masterDataExtensionAttributeRepository.findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(
                            physicalAttributeId, upc, UPC_ITEM_KEY_CODE, sourceSystemId);
                } else {
                    // Get the data by priority.
                    masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository.findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(
                            physicalAttributeId, upc, UPC_ITEM_KEY_CODE, sourceSystemId);
                }
                if (masterDataExtensionAttribute != null) {
                    eCommerceViewAttributePriorities = ProductECommerceViewConverter.convertToECommerceViewAttributePrioritiesFromMasterDataExtensionAttribute(masterDataExtensionAttribute);
                }
                break;
        }
        eCommerceViewAttributePriorities.setPhysicalAttributeId(physicalAttributeId);
        eCommerceViewAttributePriorities.setSourceSystemId(sourceSystemId);
        return eCommerceViewAttributePriorities;
    }

    /**
     * Gets the tags by product id and upc.
     *
     * @param productId the id of product.
     * @param upc       the upc.
     * @return the string of tags.
     */
    public ECommerceViewAttributePriorities findTagsByProductIdAndUpc(long productId, long upc) {
        ECommerceViewAttributePriorities eCommerceViewAttributes = null;
        if (productFullfilmentChanelRepository.countByKeyProductId(productId) > 0 && upc > 0) {
            // Get tags from eCommerce (13).
            eCommerceViewAttributes = this.getTagsByProductIdOrUpcAndItemCodeAndDataSource(productId, PRODUCT_ITEM_KEY_CODE, SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue());
        }
        if (eCommerceViewAttributes == null) {
            // Get tags from overwrite tag.
            ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository.
                    findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(PRODUCT_ITEM_KEY_CODE,
                            productId, LogicAttributeCode.TAGS_LOGIC_ATTRIBUTE_ID.getValue());
            if (productAttributeOverwrite != null) {
                // Case 1: Overwrite.
                eCommerceViewAttributes = this.getTagsProductIdAndUpcAndDataSource(productId, upc, productAttributeOverwrite.getKey().getSourceSystemId());
                if(eCommerceViewAttributes!= null){
                    eCommerceViewAttributes.setSourceSystemId(productAttributeOverwrite.getKey().getSourceSystemId());
                    eCommerceViewAttributes.setPhysicalAttributeId(productAttributeOverwrite.getKey().getPhysicalAttributeId());
                }
            } else {
                // Case 2: Not overwrite. Gets the data by priority.
                // If There is no the tag from overwrite or eCommerce then we get the tag from systems.
                List<TargetSystemAttributePriority> targetSystemAttributePriorities = this.targetSystemAttributePriorityRepository.findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(LogicAttributeCode.TAGS_LOGIC_ATTRIBUTE_ID.getValue().intValue());
                if (targetSystemAttributePriorities != null) {
                    ECommerceViewAttributePriorities eCommerceViewAttributePriorities = null;
                    for (TargetSystemAttributePriority item : targetSystemAttributePriorities) {
                        eCommerceViewAttributePriorities = this.getTagsProductIdAndUpcAndDataSource(productId, upc, item.getKey().getDataSourceSystemId());
                        if (eCommerceViewAttributePriorities != null && StringUtils.isNotBlank(eCommerceViewAttributePriorities.getContent())) {
                            eCommerceViewAttributes = eCommerceViewAttributePriorities;
                            eCommerceViewAttributePriorities.setActionCode(ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
                            break;
                        }
                    }
                }
            }
        }
        if(eCommerceViewAttributes!= null) {
            //find ALERT_STAGING_TBL
            eCommerceViewAttributes.setDifferentWithDefaultValue(this.validateChangedPublishedSource(productId, LogicAttributeCode.TAGS_LOGIC_ATTRIBUTE_ID.getValue()));
        }
        return eCommerceViewAttributes;
    }
    /**
     * Gets the tags from source 4 or other source by product id and upc and data source.
     *
     * @param productId    the id of product.
     * @param upc          the scan code.
     * @param dataSourceId the id of data source.
     * @return the ECommerceViewAttributePriorities object.
     */
    private ECommerceViewAttributePriorities getTagsProductIdAndUpcAndDataSource(long productId, long upc, long dataSourceId) {
        ECommerceViewAttributePriorities eCommerceViewAttributePriorities = null;
        if (dataSourceId == SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()) {
            // Get from source 4.
            // Get masterDataExtensionAttributes by product id.
            eCommerceViewAttributePriorities = this.getTagsByProductIdOrUpcAndItemCodeForMaintenanceSource(productId, upc);
        }else {
            if (eCommerceViewAttributePriorities == null) {
                // Get masterDataExtensionAttributes by upc.
                eCommerceViewAttributePriorities = this.getTagsByProductIdOrUpcAndItemCodeAndDataSource(upc, UPC_ITEM_KEY_CODE, dataSourceId);
            }
        }
        return eCommerceViewAttributePriorities;
    }
    /**
     * Get the tags from special data source by productIdOrUpc and itemKeyCode.
     *
     * @param productIdOrUpc the id of product or upc.
     * @return the ECommerceViewAttributePriorities object.
     */
    private ECommerceViewAttributePriorities getTagsByProductIdOrUpcAndItemCodeAndDataSource(long productIdOrUpc, String itemKeyCode, long dataSourceId) {
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository.findByKeyIdAndKeyDataSourceSystemAndKeyItemProdIdCodeAndEcommerUserGroupAttributesKeyUsrInrfcGrpCdOrderByKeyAttributeIdAscKeySequenceNumber(productIdOrUpc, dataSourceId, itemKeyCode, TAG_ITEM_KEY_CODE);
        return this.getTagsInformation(masterDataExtensionAttributes, dataSourceId);
    }
    /**
     * Get tags from maintenance source.
     *
     * @param productId the product id.
     * @param upc the scan code.
     * @return
     */
    private ECommerceViewAttributePriorities getTagsByProductIdOrUpcAndItemCodeForMaintenanceSource(long productId, long upc) {
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = new ArrayList<>();
        // Get attribute by product.
        List<MasterDataExtensionAttribute> masterDataExtensionAttributesByProductId = this.masterDataExtensionAttributeRepository.findByKeyIdAndKeyDataSourceSystemAndKeyItemProdIdCodeAndEcommerUserGroupAttributesKeyUsrInrfcGrpCdOrderByKeyAttributeIdAscKeySequenceNumber(productId,
                SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue(), PRODUCT_ITEM_KEY_CODE, TAG_ITEM_KEY_CODE);
        // Get attribute by upc.
        List<MasterDataExtensionAttribute> masterDataExtensionAttributesByUpc = this.masterDataExtensionAttributeRepository.findByKeyIdAndKeyDataSourceSystemAndKeyItemProdIdCodeAndEcommerUserGroupAttributesKeyUsrInrfcGrpCdOrderByKeyAttributeIdAscKeySequenceNumber(upc,
                SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue(), UPC_ITEM_KEY_CODE, TAG_ITEM_KEY_CODE);
        if(masterDataExtensionAttributesByProductId != null && !masterDataExtensionAttributesByProductId.isEmpty()){
            masterDataExtensionAttributes.addAll(masterDataExtensionAttributesByProductId);
        }
        if(masterDataExtensionAttributesByUpc != null && !masterDataExtensionAttributesByUpc.isEmpty()){
            masterDataExtensionAttributes.addAll(masterDataExtensionAttributesByUpc);
        }
        // Sort by getAttributeId, SequenceNumber.
        masterDataExtensionAttributes.sort((MasterDataExtensionAttribute o1, MasterDataExtensionAttribute o2) -> {
            if (o1.getKey().getAttributeId().compareTo(o2.getKey().getAttributeId()) == 0) {
                return (int)(o1.getKey().getSequenceNumber() - o2.getKey().getSequenceNumber());
            } else {
                return o1.getKey().getAttributeId().compareTo(o2.getKey().getAttributeId());
            }});
        return this.getTagsInformation(masterDataExtensionAttributes, SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue());
    }
    /**
     * Get tags information from masterDataExtensionAttributes.
     *
     * @param masterDataExtensionAttributes the list of masterDataExtensionAttributes.
     * @param dataSourceId the data source id.
     * @return the ECommerceViewAttributePriorities object.
     */
    private  ECommerceViewAttributePriorities getTagsInformation(List<MasterDataExtensionAttribute> masterDataExtensionAttributes, long dataSourceId){
        ECommerceViewAttributePriorities eCommerceViewAttributePriorities = null;
        if (masterDataExtensionAttributes != null && !masterDataExtensionAttributes.isEmpty()) {
            StringBuilder tagsSummaryStringBuilder = new StringBuilder();
            String tagSummary;
            boolean isNeedsAddColonsChar = false;
            for (MasterDataExtensionAttribute item : masterDataExtensionAttributes) {
                tagSummary = ProductECommerceViewUtil.getTagSummary(item);
                if (!StringUtils.isEmpty(tagSummary)) {
                    if (isNeedsAddColonsChar) {
                        tagsSummaryStringBuilder.append(ProductECommerceViewUtil.STRING_COLONS);
                        tagsSummaryStringBuilder.append(ProductECommerceViewUtil.SPACE);
                    }
                    tagsSummaryStringBuilder.append(tagSummary);
                    isNeedsAddColonsChar = true;
                }
            }
            eCommerceViewAttributePriorities = new ECommerceViewAttributePriorities();
            eCommerceViewAttributePriorities.setActionCode(ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
            eCommerceViewAttributePriorities.setSourceSystemId(dataSourceId);
            eCommerceViewAttributePriorities.setLogicAttributeId(LogicAttributeCode.TAGS_LOGIC_ATTRIBUTE_ID.getValue());
            eCommerceViewAttributePriorities.setDifferentWithDefaultValue(true);
            eCommerceViewAttributePriorities.setContent(tagsSummaryStringBuilder.toString());
        }
        return eCommerceViewAttributePriorities;
    }
    /**
     * Find all tags attributes.
     *
     * @param productId  the product id.
     * @param scanCodeId the scan code id.
     * @return the ECommerceViewAttributePriority.
     */
    public ECommerceViewAttributePriority findAllTagsAttributePriorities(long productId, long scanCodeId) {
        ECommerceViewAttributePriority eCommerceViewAttributePriority = new ECommerceViewAttributePriority();
        eCommerceViewAttributePriority.seteCommerceViewAttributePriorityDetails(new ArrayList<>());
        // Get eCommerce attribute information.
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository.
                findByKeyIdAndKeyDataSourceSystemAndKeyItemProdIdCodeAndEcommerUserGroupAttributesKeyUsrInrfcGrpCdOrderByKeyAttributeIdAscKeySequenceNumber(productId, SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue(), PRODUCT_ITEM_KEY_CODE, TAG_ITEM_KEY_CODE);
        StringBuilder tagsSummaryStringBuilder = new StringBuilder();
        if (masterDataExtensionAttributes != null && !masterDataExtensionAttributes.isEmpty()) {
            String tagSummary;
            boolean isNeedsAddColonsChar = false;
            for (MasterDataExtensionAttribute item : masterDataExtensionAttributes) {
                tagSummary = ProductECommerceViewUtil.getTagSummary(item);
                if (!StringUtils.isEmpty(tagSummary)) {
                    if (isNeedsAddColonsChar) {
                        tagsSummaryStringBuilder.append(ProductECommerceViewUtil.STRING_COLONS);
                        tagsSummaryStringBuilder.append(ProductECommerceViewUtil.SPACE);
                    }
                    tagsSummaryStringBuilder.append(tagSummary);
                    isNeedsAddColonsChar = true;
                    eCommerceViewAttributePriority.setMasterDataExtensionAttribute(item);
                }
            }
            eCommerceViewAttributePriority.setMainContent(new ECommerceViewAttributePriority.ContentString(tagsSummaryStringBuilder.toString()));
            eCommerceViewAttributePriority.setAttributeId(LogicAttributeCode.TAGS_LOGIC_ATTRIBUTE_ID.getValue());
        }
        // Get overwrite attribute information.
        ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository.
                findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(PRODUCT_ITEM_KEY_CODE,
                        productId, LogicAttributeCode.TAGS_LOGIC_ATTRIBUTE_ID.getValue());
        if (productAttributeOverwrite != null) {
            eCommerceViewAttributePriority.setProductAttributeOverwrite(productAttributeOverwrite);
        }
        // Get priority attribute.
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this.targetSystemAttributePriorityRepository
                .findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(
                        LogicAttributeCode.TAGS_LOGIC_ATTRIBUTE_ID.getValue().intValue());
        boolean isSourceSystemDefault = false;
        if (targetSystemAttributePriorities != null && !targetSystemAttributePriorities.isEmpty()) {
            Map<String, Boolean> addedAttribute = new HashMap<>();
            String key;
            for (TargetSystemAttributePriority attributePriority : targetSystemAttributePriorities) {
                key = String.format("%s_%s", attributePriority.getKey().getDataSourceSystemId(), attributePriority.getKey().getPhysicalAttributeId());
                // Check to avoid duplicate attribute.
                if(!addedAttribute.containsKey(key)) {
                    ECommerceViewAttributePriorities attribute = this.getTagsProductIdAndUpcAndDataSource(productId, scanCodeId, attributePriority.getKey().getDataSourceSystemId());
                    if ((attribute != null && StringUtils.isNotBlank(attribute.getContent())) || attributePriority.getKey().getDataSourceSystemId() == 0) {
                        ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails = new ECommerceViewAttributePriorityDetails();
                        if (attribute != null) {
							eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(attribute.getContent()));
						}else{
							eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(StringUtils.EMPTY));
						}
                        if (SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue().equals(
                                attributePriority.getKey().getDataSourceSystemId())) {
                            eCommerceViewAttributePriorityDetails.setSourceEditable(true);
                        }
                        // get the first source after information display at HEB.com area.
                        if (!isSourceSystemDefault && attributePriority.getKey().getDataSourceSystemId() != 0) {
                            isSourceSystemDefault = true;
                            eCommerceViewAttributePriorityDetails.setSourceDefault(true);
                            // If no overwrite then set selected first source.
                            if(productAttributeOverwrite == null){
                                eCommerceViewAttributePriorityDetails.setSelected(true);
                            }
                        }
                        if (productAttributeOverwrite != null && (productAttributeOverwrite.getKey().getLogicAttributeId()
                                .intValue() == attributePriority.getKey().getLogicalAttributeId()) &&
                                productAttributeOverwrite.getKey().getPhysicalAttributeId().equals
                                        (attributePriority.getKey().getPhysicalAttributeId()) &&
                                productAttributeOverwrite.getKey().getSourceSystemId().equals
                                        (attributePriority.getKey().getDataSourceSystemId())) {
                            eCommerceViewAttributePriorityDetails.setSelected(true);
                        }
                        if (attributePriority.getSourceSystem() != null) {
                            attributePriority.getSourceSystem().getId();
                            eCommerceViewAttributePriorityDetails.setSourceDescription
                                    (StringUtils.trim(attributePriority.getSourceSystem().getDescription()));
                        }
                        eCommerceViewAttributePriorityDetails.setTargetSystemAttributePriority(attributePriority);
                        eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().add(eCommerceViewAttributePriorityDetails);
                        addedAttribute.put(key, true);
                    }
                }
            }
        }
        eCommerceViewAttributePriority.setAttributeId(LogicAttributeCode.TAGS_LOGIC_ATTRIBUTE_ID.getValue());
        return eCommerceViewAttributePriority;
    }
    /**
     * Returns the warnings of the item find by attribute priorities.
     *
     * @param physicalAttributeId the physical attribute id to find.
     * @param dataSourceSystem    the data source system id to find.
     * @param scanCodeId          the scan code id to find.
     * @return the entity that contains warnings text.
     */
    private ECommerceViewAttributePriorities getWarningsByAttributePriorities(Long physicalAttributeId, Long dataSourceSystem, long scanCodeId) {
        ECommerceViewAttributePriorities warning = null;
        switch (SourceSystemNumber.getNumber(dataSourceSystem)) {
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                warning = this.getDescriptionFromProductScanCodeExtents(scanCodeId,
                        Arrays.asList(ProductECommerceViewService.WARNING_PRODUCT_EXTENT_DATA_CODE));
                break;
            case OBPS_SOURCE_SYSTEM_NUMBER: {
                ECommerceViewAttributePriorities warningsAttribute = this.getAttributeFromMasterDataExtension(
                        LogicAttributeCode.WARNINGS_LOGIC_ATTRIBUTE_ID.getValue(), scanCodeId,
                        ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                ECommerceViewAttributePriorities allergensAttribute = this.getAttributeFromMasterDataExtension(
                        LogicAttributeCode.ALLERGENS_LOGIC_ATTRIBUTE_ID.getValue(), scanCodeId,
                        ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                warning = this.getMergedAttributePriorities(Arrays.asList(warningsAttribute, allergensAttribute));
                break;
            }
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
            case ESHA_GENESIS_SOURCE_SYSTEM_NUMBER:
            case GS1_SOURCE_SYSTEM_NUMBER:
            case KWIKEE_SOURCE_SYSTEM_NUMBER:
            case ITEMMASTER_COM_SOURCE_SYSTEM_NUMBER:
            case MANUFACTURER_SOURCE_SYSTEM_NUMBER:
            case OTHER_RETAILERS_SOURCE_SYSTEM_NUMBER:
                warning = this.getAttributeFromMasterDataExtension(physicalAttributeId, scanCodeId,
                        ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                break;
            default:
                break;
        }
        return warning;
    }

    /**
     * Returns the directions of the item find by attribute priorities.
     *
     * @param physicalAttributeId the physical attribute id to find.
     * @param dataSourceSystem    the data source system id to find.
     * @param scanCodeId          the scan code id to find.
     * @return the entity that contains directions text.
     */
    private ECommerceViewAttributePriorities getDirectionsByAttributePriorities(Long physicalAttributeId, Long dataSourceSystem, long scanCodeId) {
        ECommerceViewAttributePriorities direction = null;
        switch (SourceSystemNumber.getNumber(dataSourceSystem)) {
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                direction = this.getDescriptionFromProductScanCodeExtents(scanCodeId,
                        Arrays.asList(ProductECommerceViewService.DIRECTION_PRODUCT_EXTENT_DATA_CODE));
                break;
            case OBPS_SOURCE_SYSTEM_NUMBER:
            case KWIKEE_SOURCE_SYSTEM_NUMBER:
            case ITEMMASTER_COM_SOURCE_SYSTEM_NUMBER:
            case MANUFACTURER_SOURCE_SYSTEM_NUMBER:
            case OTHER_RETAILERS_SOURCE_SYSTEM_NUMBER:
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
            case GS1_SOURCE_SYSTEM_NUMBER:
                direction = this.getAttributeFromMasterDataExtension(physicalAttributeId, scanCodeId,
                        ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                break;
            default:
                break;
        }
        return direction;
    }

    /**
     * Returns the ingredients of the item find by attribute priorities.
     *
     * @param physicalAttributeId the physical attribute id to find.
     * @param dataSourceSystem    the data source system id to find.
     * @param scanCodeId          the scan code id to find.
     * @return the entity that contains ingredients text.
     */
    private ECommerceViewAttributePriorities getIngredientsByAttributePriorities(Long physicalAttributeId, Long dataSourceSystem, long scanCodeId) {
        ECommerceViewAttributePriorities ingredient = null;
        switch (SourceSystemNumber.getNumber(dataSourceSystem)) {
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                ingredient = this.getDescriptionFromProductScanCodeExtents(scanCodeId, Arrays.asList(
                        ProductECommerceViewService.INGREDIENTS_PRODUCT_EXTENT_DATA_CODE,
                        ProductECommerceViewService.GUARANTEED_PRODUCT_EXTENT_DATA_CODE));
                break;
            case SCALE_MANAGEMENT_SOURCE_SYSTEM_NUMBER:
                ingredient = this.getIngredientsForScaleManagement(scanCodeId);
                break;
            case OBPS_SOURCE_SYSTEM_NUMBER: {
                if (LogicAttributeCode.INGREDIENTS_LOGIC_ATTRIBUTE_ID.getValue().equals(physicalAttributeId)) {
                    ingredient = this.getAttributeFromMasterDataExtension(physicalAttributeId, scanCodeId,
                            ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                } else if (LogicAttributeCode.INGREDIENTS_PET_GUARANTEED_LOGIC_ATTRIBUTE_ID.getValue().equals(physicalAttributeId)) {
                    ECommerceViewAttributePriorities ingredientAttribute = this.getAttributeFromMasterDataExtension(
                            LogicAttributeCode.INGREDIENTS_LOGIC_ATTRIBUTE_ID.getValue(), scanCodeId,
                            ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                    ECommerceViewAttributePriorities guaranteedDescription = this.getDescriptionFromProductScanCodeExtent(
                            scanCodeId, ProductECommerceViewService.GUARANTEED_PRODUCT_EXTENT_DATA_CODE,
                            dataSourceSystem.intValue());
                    ingredient = this.getMergedAttributePriorities(Arrays.asList(ingredientAttribute, guaranteedDescription));
                }
                break;
            }
            case ESHA_GENESIS_SOURCE_SYSTEM_NUMBER:
            case GS1_SOURCE_SYSTEM_NUMBER:
            case ITEMMASTER_COM_SOURCE_SYSTEM_NUMBER:
            case MANUFACTURER_SOURCE_SYSTEM_NUMBER:
            case OTHER_RETAILERS_SOURCE_SYSTEM_NUMBER:
            case KWIKEE_SOURCE_SYSTEM_NUMBER:
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
                ingredient = this.getAttributeFromMasterDataExtension(physicalAttributeId, scanCodeId,
                        ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                break;
            default:
                break;
        }
        return ingredient;
    }

    /**
     * Get product relationship data by product id.
     *
     * @param productId the product id to search.
     * @return the list of product relationship data.
     */
    public List<ProductRelationship> getProductRelationshipByProductId(Long productId) {
        List<String> productRelationshipCodes = Arrays.asList(
                ProductRelationship.ProductRelationshipCode.ADD_ON_PRODUCT.getValue(),
                ProductRelationship.ProductRelationshipCode.FIXED_RELATED_PRODUCT.getValue(),
                ProductRelationship.ProductRelationshipCode.UPSELL.getValue());
        return this.productRelationshipRepository.findByKeyProductIdAndKeyProductRelationshipCodeIn(productId, productRelationshipCodes);
    }

    /**
     * Returns the attribute entity of the master data extension attribute.
     *
     * @param attributeId      the attribute id to find.
     * @param id               the key id to find, product id or upc base on value of itemProdKeyCode.
     * @param itemProdKeyCode  the item prod key code, type of key id is product id or upc.
     * @param dataSourceSystem the data source system to find.
     * @return the attribute entity.
     */
    private ECommerceViewAttributePriorities getAttributeFromMasterDataExtension(Long attributeId, long id,
                                                                                 String itemProdKeyCode, Long dataSourceSystem) {
        ECommerceViewAttributePriorities attributePriority = new ECommerceViewAttributePriorities();
        MasterDataExtensionAttribute attribute = this.masterDataExtensionAttributeRepository
                .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(attributeId, id,
                        itemProdKeyCode, dataSourceSystem);
        if (attribute != null) {
            attributePriority.setContent(attribute.getAttributeValueText());
            attributePriority.setCreatedDate(attribute.getLstUpdtTs());
            attributePriority.setPostedDate(
                    attribute.getAttributeValueTime() != null ? attribute.getAttributeValueTime().toLocalDate() : null);
            attributePriority.setActionCode(ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
        } else {
            attributePriority.setActionCode(ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
        }
        attributePriority.setSourceSystemId(dataSourceSystem);
        attributePriority.setLogicAttributeId(attributeId);
        return attributePriority;
    }

    /**
     * Returns the product description text of the product scan code extent.
     *
     * @param scanCodeId      the upc to find.
     * @param prodExtDataCode the prod extent data codes to find.
     * @param sourceSystem    the source system to find.
     * @return the entity that contains product description text.
     */
    private ECommerceViewAttributePriorities getDescriptionFromProductScanCodeExtent(long scanCodeId, String prodExtDataCode,
                                                                                     int sourceSystem) {
        ECommerceViewAttributePriorities attributePriority = new ECommerceViewAttributePriorities();
        ProductScanCodeExtent productScanCodeExtent = this.productScanCodeExtentRepository.
                findByKeyScanCodeIdAndKeyProdExtDataCodeAndSourceSystem(scanCodeId, prodExtDataCode, sourceSystem);
        if (productScanCodeExtent != null) {
            attributePriority.setContent(productScanCodeExtent.getProdDescriptionText());
            attributePriority.setCreatedDate(productScanCodeExtent.getLstUpdtTs());
        }
        return attributePriority;
    }

    /**
     * Returns the product description text of the product scan code extent.
     *
     * @param scanCodeId       the upc to find.
     * @param prodExtDataCodes the list of prod extent data codes to find.
     * @return the entity that contains product description text.
     */
    private ECommerceViewAttributePriorities getDescriptionFromProductScanCodeExtents(long scanCodeId, List<String> prodExtDataCodes) {
        ECommerceViewAttributePriorities attributePriority = new ECommerceViewAttributePriorities();
        List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository.
                findByKeyScanCodeIdAndKeyProdExtDataCodeInOrderByKeyProdExtDataCodeDesc(scanCodeId, prodExtDataCodes);
        if (!productScanCodeExtents.isEmpty()) {
            attributePriority.setContent(productScanCodeExtents.stream()
                    .filter(productScanCodeExtent -> StringUtils.isNotBlank(productScanCodeExtent.getProdDescriptionText()))
                    .map(ProductScanCodeExtent::getProdDescriptionText)
                    .collect(Collectors.joining(ProductECommerceViewService.SPACE_SEPARATOR)));
            attributePriority.setCreatedDate(productScanCodeExtents.stream()
                    .filter(productScanCodeExtent -> productScanCodeExtent.getLstUpdtTs() != null)
                    .map(ProductScanCodeExtent::getLstUpdtTs)
                    .max(Comparator.naturalOrder()).orElse(null));
        }
        return attributePriority;
    }

    /**
     * Returns the ingredient description for scale management by the upc.
     *
     * @param scanCodeId the upc to find.
     * @return the entity that contains ingredient description.
     */
    private ECommerceViewAttributePriorities getIngredientsForScaleManagement(long scanCodeId) {
        ECommerceViewAttributePriorities attributePriority = new ECommerceViewAttributePriorities();
        ScaleUpc scaleUpc = this.scaleUpcRepository.findOne(scanCodeId);
        if (scaleUpc != null) {
            long statementNumber = scaleUpc.getIngredientStatement();
            List<Ingredient> ingredients = this.ingredientRepository
                    .findByIngredientCodeOrderByStatementDetails(statementNumber);
            String content = ingredients.stream()
                    .filter(ingredient -> StringUtils.isNotBlank(ingredient.getDisplayText()))
                    .map(Ingredient::getDisplayText)
                    .collect(Collectors.joining(ProductECommerceViewService.SPACE_SEPARATOR));
            attributePriority.setContent(content);
            // not set last update time for scale management.
        }
        return attributePriority;
    }

    /**
     * Call the service to public eCommerce data to HEB Com.
     *
     * @param product         the ECommerceViewDetails object.
     * @param userId     the user id.
     */
    public void publishECommerceViewDataToHebCom(ECommerceViewDetails product, String userId) {
        this.productAttributeManagementServiceClient.publishECommerceViewDataToHebCom(product, userId);
    }

    /**
     * Find all ingredient attribute priorities by the upc to show.
     *
     * @param productId	 the product id to find.
     * @param scanCodeId the upc to find.
     * @return all ingredient attribute priorities.
     */
    public List<ECommerceViewAttributePriorities> findAllIngredientAttributePriorities(long productId, long scanCodeId) {
        return findAllIngredientAttributePriorities(productId, scanCodeId, SourceSystemNumber
                .ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue());
    }

    /**
     * Returns the entity that contains attribute value text merge from many sources, and the max of last update time.
     *
     * @param attributePriorities the list of attribute to merge.
     * @return the entity that contains attribute value text and last update time.
     */
    private ECommerceViewAttributePriorities getMergedAttributePriorities(List<ECommerceViewAttributePriorities> attributePriorities) {
        ECommerceViewAttributePriorities results = new ECommerceViewAttributePriorities();
        results.setContent(attributePriorities.stream()
                .filter(attributePriority -> StringUtils.isNotBlank(attributePriority.getContent()))
                .map(ECommerceViewAttributePriorities::getContent)
                .collect(Collectors.joining(ProductECommerceViewService.SPACE_SEPARATOR)));
        results.setCreatedDate(attributePriorities.stream()
                .filter(attributePriority -> attributePriority.getCreatedDate() != null)
                .map(ECommerceViewAttributePriorities::getCreatedDate)
                .max(Comparator.naturalOrder()).orElse(null));
        return results;
    }

    /**
     * Get eCommerce data source system by attribute id, upc/product id, publish source
     *
     * @param attributeId - The attribute id,
     * @param productId   - The product id
     * @param upc         - The scan code number
     * @return ECommerceViewAttributePriorities
     */
    public ECommerceViewAttributePriorities getEcommerceDataBasedOnPriority(Long  attributeId, long productId, long
            upc, long publishedSource) {
        ECommerceViewAttributePriorities eCommerceViewAttributePriorities = null;
        MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(attributeId, productId,
                        ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, publishedSource);
        if (masterDataExtensionAttribute != null) {
            eCommerceViewAttributePriorities = ProductECommerceViewConverter
                    .convertToECommerceViewAttributePrioritiesFromMasterDataExtensionAttribute(masterDataExtensionAttribute);
            if(attributeId  == LogicAttributeCode.BRAND_LOGIC_ATTRIBUTE_ID.getValue() || attributeId  == LogicAttributeCode.SIZE_LOGIC_ATTRIBUTE_ID.getValue()
                    || attributeId  == LogicAttributeCode.DISPLAY_NAME_LOGIC_ATTRIBUTE_ID.getValue() ||
                    attributeId  == LogicAttributeCode.ROMANCE_COPY_LOGIC_ATTRIBUTE_ID.getValue() ||
                    attributeId  == LogicAttributeCode.FAVOR_ITEM_DESCRIPTION_LOGIC_ATTRIBUTE_ID.getValue().longValue()){
                //  eCommerceViewAttributePriorities.setMandatory(true);
                eCommerceViewAttributePriorities.setMandatory(checkSourceSystemIsMandatory(productId, attributeId, upc));
            }
        } else {
            ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository.findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, productId, attributeId);
            if (productAttributeOverwrite != null) {
                eCommerceViewAttributePriorities = this.checkLogicDataSourceByByLogicAttributePriorities(attributeId,
                        productAttributeOverwrite.getKey().getSourceSystemId(), productAttributeOverwrite.getKey()
                                .getPhysicalAttributeId(), upc, productId);
                eCommerceViewAttributePriorities.setRelationshipGroupTypeCode(productAttributeOverwrite.getKey().getRelationshipGroupTypeCode());
            } else {
                List<TargetSystemAttributePriority> targetSystemAttributePriorities = this
                        .targetSystemAttributePriorityRepository.findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(attributeId.intValue());
                if (targetSystemAttributePriorities != null) {
                    for (TargetSystemAttributePriority targetSystemAttributePriority : targetSystemAttributePriorities) {
                        eCommerceViewAttributePriorities = this.checkLogicDataSourceByByLogicAttributePriorities(attributeId,
                                targetSystemAttributePriority.getKey().getDataSourceSystemId(),
                                targetSystemAttributePriority.getKey().getPhysicalAttributeId(), upc, productId);
                        if (StringUtils.isNotBlank(eCommerceViewAttributePriorities.getContent())) {
                            eCommerceViewAttributePriorities.setRelationshipGroupTypeCode
                                    (targetSystemAttributePriority.getKey().getRelationshipGroupTypeCode());
                            break;
                        }
                    }
                }
            }
        }
        //find ALERT_STAGING_TBL
        eCommerceViewAttributePriorities.setDifferentWithDefaultValue(this.validateChangedPublishedSource(productId, attributeId));
        eCommerceViewAttributePriorities.setHtmlContent(eCommerceViewAttributePriorities.getContent());

        return eCommerceViewAttributePriorities;
    }

    /**
     * Check source system is mandatory for brand, display name, romance, size
     *
     * @param attributeId - The attribute id,
     * @param productId   - The product id
     * @param scanCodeId  - The scan code number
     * @return boolean(true or false)
     */
    private boolean checkSourceSystemIsMandatory(long productId, Long attributeId, long scanCodeId) {
        boolean isEmpty = true;
        boolean isMandatory = false;
        ECommerceViewAttributePriority eCommerceViewAttributePriority = new ECommerceViewAttributePriority();
        eCommerceViewAttributePriority.seteCommerceViewAttributePriorityDetails(new ArrayList<>());
        ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository.findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, productId, attributeId);
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this
                .targetSystemAttributePriorityRepository.findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(attributeId.intValue());
        if (targetSystemAttributePriorities != null) {
            boolean fistPriority = false;
            boolean hasSelected = false;
            ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails = null;
            int indexDefaultSource = 0;
            for (TargetSystemAttributePriority targetSystemAttributePriority : targetSystemAttributePriorities) {
                eCommerceViewAttributePriorityDetails = this.checkLogicDataSourceByByLogicAttributePriority(attributeId,
                        targetSystemAttributePriority.getKey().getDataSourceSystemId(),
                        targetSystemAttributePriority.getKey().getPhysicalAttributeId(), scanCodeId, productId);
                if (eCommerceViewAttributePriorityDetails.getContent() != null && StringUtils.isNotBlank(eCommerceViewAttributePriorityDetails.<String>getContent().getContent())
                        || (SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue().equals(
                        targetSystemAttributePriority.getKey().getDataSourceSystemId()) && eCommerceViewAttributePriorityDetails.isSourceEditable())) {
                    if(!fistPriority && eCommerceViewAttributePriorityDetails.<String>getContent() != null) {
                        if(StringUtils.isNotBlank(eCommerceViewAttributePriorityDetails.<String>getContent().getContent())){
                            eCommerceViewAttributePriorityDetails.setSourceDefault(true);
                            fistPriority = true;
                        }
                    }
                    if(!fistPriority) {
                        indexDefaultSource++;
                    }
                    if(productAttributeOverwrite != null && (productAttributeOverwrite.getKey().getLogicAttributeId()
                            .intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()) &&
                            productAttributeOverwrite.getKey().getPhysicalAttributeId().equals
                                    (targetSystemAttributePriority.getKey().getPhysicalAttributeId()) &&
                            productAttributeOverwrite.getKey().getSourceSystemId().equals
                                    (targetSystemAttributePriority.getKey().getDataSourceSystemId())){
                        eCommerceViewAttributePriorityDetails.setSelected(true);
                        hasSelected = true;
                    }
                    eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().add(eCommerceViewAttributePriorityDetails);
                }
            }
            if(!hasSelected && !eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().isEmpty()
                    && fistPriority) {
                eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().get(indexDefaultSource).setSelected(true);
            }

            for(ECommerceViewAttributePriorityDetails eCommerceDetails : eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails()){
                if (eCommerceDetails.getContent() != null && StringUtils.isNotBlank(eCommerceDetails.<String>getContent().getContent())) {
                    isEmpty = false;
                }
                else if(eCommerceDetails.isSourceEditable() && eCommerceDetails.isSelected()){
                    isMandatory = true;
                }
            }
        }
        return isMandatory? isMandatory : isEmpty;
    }

    /**
     * find all ingredient Attribute base on priority.
     *
     * @param publishedSource - The attribute id,
     * @param productId       - The product id
     * @param scanCodeId      - The scan code number
     * @return ECommerceViewAttributePriorities
     */
    private List<ECommerceViewAttributePriorities> findAllIngredientAttributePriorities(long productId, long scanCodeId,
                                                                                        long publishedSource) {
        List<ECommerceViewAttributePriorities> ingredientAttributes = new ArrayList<>();
        ECommerceViewAttributePriorities eCommerceViewSource = this.getAttributeFromMasterDataExtension(
                LogicAttributeCode.INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID.getValue(),
                productId, ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE,
                publishedSource);
        ProductAttributeOverwrite attributeOverwrite = this.productAttributeOverwriteRepository
                .findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(
                        ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, productId,
                        LogicAttributeCode.INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID.getValue());
        if (attributeOverwrite != null) {	// set selected source by sourceSystemId and physicalAttributeId.
            String sourceSystemSelected = attributeOverwrite.getKey().getSourceSystemId() + "_"
                    + attributeOverwrite.getKey().getPhysicalAttributeId();
            eCommerceViewSource.setSourceSystemSelected(sourceSystemSelected);
        }
        ingredientAttributes.add(eCommerceViewSource);  // first element is from eCommverceView source (13) to display in HEB.com area.
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this.targetSystemAttributePriorityRepository
                .findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(
                        LogicAttributeCode.INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID.getValue().intValue());
        boolean isSourceSystemDefault = false;
        if (targetSystemAttributePriorities != null && !targetSystemAttributePriorities.isEmpty()) {
            for (TargetSystemAttributePriority attributePriority : targetSystemAttributePriorities) {
                ECommerceViewAttributePriorities ingredient = this.getIngredientsByAttributePriorities(
                        attributePriority.getKey().getPhysicalAttributeId(),
                        attributePriority.getKey().getDataSourceSystemId(), scanCodeId);
                if (StringUtils.isNotBlank(ingredient.getContent())
                        || SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue().equals(
                        attributePriority.getKey().getDataSourceSystemId())) {
                    // get the first source after information display at HEB.com area.
                    if (StringUtils.isNotBlank(ingredient.getContent()) && !isSourceSystemDefault) {
                        isSourceSystemDefault = true;
                        ingredient.setSourceSystemDefault(Boolean.TRUE);
                        if (ingredientAttributes.get(0).getSourceSystemSelected() == null) {
                            String sourceSystemSelected = attributePriority.getKey().getDataSourceSystemId() + "_"
                                    + attributePriority.getKey().getPhysicalAttributeId();
                            ingredientAttributes.get(0).setSourceSystemSelected(sourceSystemSelected);
                        }
                    }
                    this.attributePriorityResolver.fetch(attributePriority);
                    ingredient.setSourceSystemDescription(attributePriority.getSourceSystem().getDescription());
                    ingredient.setSourceSystemId(attributePriority.getKey().getDataSourceSystemId());
                    ingredient.setPhysicalAttributeId(attributePriority.getKey().getPhysicalAttributeId());
                    ingredientAttributes.add(ingredient);
                }
            }
        }
        return ingredientAttributes;
    }

    /**
     * Check the Fulfillment is valid or invalid by productId.
     *
     * @param productId the productId.
     * @return true if Fulfillment by product id is valid or not.
     */
    public boolean isValidFulfillmentByProductId(long productId){
        boolean isValid = false;
        List<ProductFullfilmentChanel>  productFullfilmentChanels = this.productFullfilmentChanelRepository.findByKeyProductId(productId);
        if(productFullfilmentChanels != null){
            for (ProductFullfilmentChanel item: productFullfilmentChanels) {
                if(item.getFulfillmentChannel()!= null && item.getExpirationDate() != null &&
                        ProductECommerceViewUtil.compareGreaterThanOrEqualToCurrentDate(item.getExpirationDate())){
                    isValid = true;
                    break;
                }
            }
        }
        return isValid;
    }

    /**
     * Call webservice client to update ingredient product attribute override.
     *
     * @param eCommerceViewDetails the ECommerceViewAttributePriorities entity to be updated.
     * @param userId            the user id that request updating.
     */
    public void updateIngredientAttributePriorities(ECommerceViewDetails eCommerceViewDetails, String userId) {
        if (eCommerceViewDetails != null) {
            if (eCommerceViewDetails.getIngredients() != null) {
                ECommerceViewAttributePriorities ingredient = eCommerceViewDetails.getIngredients();
                ingredient.setLogicAttributeId(LogicAttributeCode.INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID.getValue());
            }
            if (eCommerceViewDetails.getIngredientsToDelete() != null) {
                ECommerceViewAttributePriorities ingredientToDelete = eCommerceViewDetails.getIngredientsToDelete();
                ingredientToDelete.setLogicAttributeId(LogicAttributeCode.INGREDIENT_STATEMENT_LOGIC_ATTRIBUTE_ID.getValue());
            }
            if (eCommerceViewDetails.getIngredientsAttribute() != null) {
                ECommerceViewAttributePriorities ingredientAttribute = eCommerceViewDetails.getIngredientsAttribute();
                if (ingredientAttribute.getCreatedDate() == null) {
                    ingredientAttribute.setCreatedDate(LocalDate.now());
                }
                if (StringUtils.isBlank(ingredientAttribute.getContent())) {
                    ingredientAttribute.setContent(StringUtils.EMPTY);
                }
            }
            this.productAttributeManagementServiceClient.updateMasterDataExtensionAttributeAndProductAttributeOverride(eCommerceViewDetails, userId);
        }
    }

    /**
     * Returns the HebGuaranteeTypeCode by product id.
     *
     * @param productId the product id.
     * @return the HebGuaranteeTypeCode object or null.
     */
    public HebGuaranteeTypeCode findHebGuaranteeTypeCodeByProductId(long productId){
        GoodsProduct  goodsProduct = this.goodsProductRepository.findOne(productId);
        if(goodsProduct != null){
            return goodsProduct.getHebGuaranteeTypeCode();
        }
        return null;
    }


    /**
     * Find information eCommerce View data source. Contain product attribute overwrite, attribute priority, content
     * text. Handle classify source(source default, source editable...ect).
     * @param productId - The product id
     * @param attributeId - The attribute id
     * @param scanCodeId - The scan code id
     * @param salesChannel - The sales channel code
     * @return ECommerceViewAttributePriority object
     */
    public ECommerceViewAttributePriority findECommerceViewDataSourceByAttributeId(long productId, long attributeId,
                                                                                   long scanCodeId, String salesChannel)
            throws Exception{
        ECommerceViewAttributePriority eCommerceViewAttributePriority = this.getECommerceViewDataBasedOnPriority(productId, attributeId, scanCodeId, salesChannel);
        //set base information
        eCommerceViewAttributePriority.setAttributeId(attributeId);
        eCommerceViewAttributePriority.setPrimaryScanCode(scanCodeId);
        eCommerceViewAttributePriority.setProductId(productId);
        eCommerceViewAttributePriority.setSalesChannel(salesChannel);
        if(CollectionUtils.isNotEmpty(eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails())){
        	String romanceCopyContent = eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().get(0).<String>getContent().getContent();
        	eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().get(0).setHtmlContent(
        			new ECommerceViewAttributePriority.ContentString(romanceCopyContent));
        	if(attributeId == Attribute.LogicalAttribute.LOGICAL_ATTRIBUTE_ROMANCE_COPY.getValue() ||
                    attributeId == LogicAttributeCode.FAVOR_ITEM_DESCRIPTION_LOGIC_ATTRIBUTE_ID.getValue()){
        		if(StringUtils.isNotEmpty(romanceCopyContent)){
        			romanceCopyContent = romanceCopyContent.replaceAll(String.valueOf((char) 10), ProductECommerceViewService.BR_TAG);
        			// erases all the ASCII control characters
        			romanceCopyContent = romanceCopyContent.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", StringUtils.EMPTY);
        			eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().get(0).setContent(
        					new ECommerceViewAttributePriority.ContentString(romanceCopyContent));
        		}
        	}
        }
        return eCommerceViewAttributePriority;
    }

    /**
     * Get eCommerce data source system by attribute id, upc/product id, publish source
     *
     * @param attributeId - The attribute id,
     * @param productId   - The product id
     * @param scanCodeId  - The scan code number
     * @param salesChannel  - The sales channel code
     * @return ECommerceViewAttributePriorities
     */
    private ECommerceViewAttributePriority getECommerceViewDataBasedOnPriority(long productId, Long attributeId, long
            scanCodeId, String salesChannel) {
        ECommerceViewAttributePriority eCommerceViewAttributePriority = new ECommerceViewAttributePriority();
        eCommerceViewAttributePriority.seteCommerceViewAttributePriorityDetails(new ArrayList<>());
        //Get master data extension attribute.
        MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(attributeId, productId,
                        ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, SourceSystemNumber
                                .ECOMMERCE_SOURCE_SYSTEM_NUMBER.getValue());
        if (masterDataExtensionAttribute != null) {
            eCommerceViewAttributePriority.setMainContent(
                    new ECommerceViewAttributePriority.ContentString(StringUtils.trim(masterDataExtensionAttribute.getAttributeValueText())));
            eCommerceViewAttributePriority.setMasterDataExtensionAttribute(masterDataExtensionAttribute);
        }
        //Get Product Attribute Overwrite Information
        ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository.findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, productId, attributeId);
        if (productAttributeOverwrite != null) {
            eCommerceViewAttributePriority.setProductAttributeOverwrite(productAttributeOverwrite);
        }
        //Get the list of target system attribute priority
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this
                .targetSystemAttributePriorityRepository.findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(attributeId.intValue());
        if (targetSystemAttributePriorities != null) {
            boolean fistPriority = false;
            boolean hasSelected = false;
            ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails = null;
            Map<Long, List<TargetSystemAttributePriority>> dataSourceCounters = targetSystemAttributePriorities.stream()
                    .collect(Collectors.groupingBy(s->s
                            .getKey()
                            .getDataSourceSystemId()));
            Map<Long, Integer> dataSourceIdCounters = new HashMap<>();
            int indexDefaultSource = 0;
            for (TargetSystemAttributePriority targetSystemAttributePriority : targetSystemAttributePriorities) {
                eCommerceViewAttributePriorityDetails = this.checkLogicDataSourceByByLogicAttributePriority(attributeId,
                        targetSystemAttributePriority.getKey().getDataSourceSystemId(),
                        targetSystemAttributePriority.getKey().getPhysicalAttributeId(), scanCodeId, productId);
                if (eCommerceViewAttributePriorityDetails.getContent() != null && StringUtils.isNotBlank(eCommerceViewAttributePriorityDetails.<String>getContent().getContent())
                        || (SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue().equals(
                        targetSystemAttributePriority.getKey().getDataSourceSystemId()) && eCommerceViewAttributePriorityDetails.isSourceEditable())) {
                    if(!fistPriority && eCommerceViewAttributePriorityDetails.<String>getContent() != null) {
                        if(StringUtils.isNotBlank(eCommerceViewAttributePriorityDetails.<String>getContent().getContent())){
                            eCommerceViewAttributePriorityDetails.setSourceDefault(true);
                            fistPriority = true;
                        }
                    }
                    if(!fistPriority) {
                        indexDefaultSource++;
                    }
                    if(productAttributeOverwrite != null && (productAttributeOverwrite.getKey().getLogicAttributeId()
                            .intValue() == targetSystemAttributePriority.getKey().getLogicalAttributeId()) &&
                            productAttributeOverwrite.getKey().getPhysicalAttributeId().equals
                                    (targetSystemAttributePriority.getKey().getPhysicalAttributeId()) &&
                            productAttributeOverwrite.getKey().getSourceSystemId().equals
                                    (targetSystemAttributePriority.getKey().getDataSourceSystemId())){
                        eCommerceViewAttributePriorityDetails.setSelected(true);
                        hasSelected = true;
                    }
                    if(targetSystemAttributePriority.getSourceSystem() != null){
                        targetSystemAttributePriority.getSourceSystem().getId();
                        if(dataSourceCounters.get(targetSystemAttributePriority.getSourceSystem().getId()) != null &&
                                dataSourceCounters.get(targetSystemAttributePriority.getSourceSystem().getId()).size
                                        () > 1){
                            List<TargetSystemAttributePriority> targetSystemAttributePriories = dataSourceCounters.get(targetSystemAttributePriority.getSourceSystem().getId());
                            Integer count = 1;
                            for(TargetSystemAttributePriority targetSystemAttributePriorityInMap : targetSystemAttributePriories){
                                if(targetSystemAttributePriorityInMap.getKey().equals(targetSystemAttributePriority.getKey())){
                                    eCommerceViewAttributePriorityDetails.setSourceDescription(StringUtils.trim(targetSystemAttributePriorityInMap.getSourceSystem().getDescription()).concat(" - ").concat(count.toString()));
                                }
                                count++;
                            }
                        }else{
                            eCommerceViewAttributePriorityDetails.setSourceDescription
                                    (StringUtils.trim(targetSystemAttributePriority
                                            .getSourceSystem().getDescription()));
                        }
                    }
                    eCommerceViewAttributePriorityDetails.setTargetSystemAttributePriority(targetSystemAttributePriority);
                    eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().add(eCommerceViewAttributePriorityDetails);
                }
            }
            if(!hasSelected && !eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().isEmpty()
                    && fistPriority) {
                eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().get(indexDefaultSource).setSelected(true);
            }
        }
        return eCommerceViewAttributePriority;
    }

    /**
     * Check logic data source by logic attribute priority.
     * @param attributeId -  The attribute id
     * @param sourceSystemId - The source system id
     * @param physicalAttributeId - The physical attribute id
     * @param upc - The upc
     * @param productId - The product id
     * @return ECommerceViewAttributePriorityDetails
     */
    private ECommerceViewAttributePriorityDetails checkLogicDataSourceByByLogicAttributePriority(Long attributeId, Long
            sourceSystemId, Long physicalAttributeId, Long upc, Long productId) {
        ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails = new ECommerceViewAttributePriorityDetails();
        switch (LogicAttributeCode.getAttribute(attributeId)) {
            case BRAND_LOGIC_ATTRIBUTE_ID:
                this.getDataSourceSystemForBrandAttribute(sourceSystemId, physicalAttributeId, upc, eCommerceViewAttributePriorityDetails);
                if(sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()) &&
                        physicalAttributeId.equals(PhysicalAttributeCodeEditable.BRAND_PHYSICAL_ATTRIBUTE_EDITABLE.getValue())){
                    eCommerceViewAttributePriorityDetails.setSourceEditable(true);
                }
                break;
            case WARNING_LOGIC_ATTRIBUTE_ID:
                this.getDataSourceSystemForWarningsAttribute(sourceSystemId, physicalAttributeId, upc, eCommerceViewAttributePriorityDetails);
                if(sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()) &&
                        physicalAttributeId.equals(PhysicalAttributeCodeEditable.WARNING_PHYSICAL_ATTRIBUTE_EDITABLE.getValue())){
                    eCommerceViewAttributePriorityDetails.setSourceEditable(true);
                }
                break;
            case DIRECTIONS_LOGIC_ATTRIBUTE_ID:
                this.getDataSourceSystemForDirectionsAttribute(sourceSystemId, physicalAttributeId, upc, eCommerceViewAttributePriorityDetails);
                if(sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()) &&
                        physicalAttributeId.equals(PhysicalAttributeCodeEditable.DIRECTIONS_PHYSICAL_ATTRIBUTE_EDITABLE.getValue())){
                    eCommerceViewAttributePriorityDetails.setSourceEditable(true);
                }
                break;
            case DISPLAY_NAME_LOGIC_ATTRIBUTE_ID:
                this.getDataSourceSystemForDisplayNameAttribute(sourceSystemId, physicalAttributeId, upc, productId, eCommerceViewAttributePriorityDetails);
                if(sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()) &&
                        physicalAttributeId.equals(PhysicalAttributeCodeEditable.DISPLAY_NAME_ATTRIBUTE_EDITABLE.getValue())){
                    eCommerceViewAttributePriorityDetails.setSourceEditable(true);
                }
                break;
            case SIZE_LOGIC_ATTRIBUTE_ID:
                this.getDataSourceSystemForSizeAttribute(sourceSystemId, physicalAttributeId, upc, eCommerceViewAttributePriorityDetails);
                if(sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue()) &&
                        physicalAttributeId.equals(PhysicalAttributeCodeEditable.SIZE_PHYSICAL_ATTRIBUTE_EDITABLE.getValue())){
                    eCommerceViewAttributePriorityDetails.setSourceEditable(true);
                }
                break;
            case ROMANCE_COPY_LOGIC_ATTRIBUTE_ID:
                ECommerceViewAttributePriorities attribute = this.getDataByUpcAndAttrIdAndSourceSystemId(upc, attributeId, physicalAttributeId, sourceSystemId, true);
                eCommerceViewAttributePriorityDetails.setCreatedDate(attribute.getCreatedDate());
                if (SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue().equals(
                        sourceSystemId) &&
                        physicalAttributeId.equals(PhysicalAttributeCodeEditable.ROMANCE_COPY_ATTRIBUTE_EDITABLE.getValue())) {
                    eCommerceViewAttributePriorityDetails.setSourceEditable(true);
                }
                eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(attribute.getContent()));
                break;
            case FAVOR_ITEM_DESCRIPTION_LOGIC_ATTRIBUTE_ID:
                // Get favor item description
                ECommerceViewAttributePriorities eCommerceViewAttributePriorities = this.getDataByUpcAndAttrIdAndSourceSystemId(upc, attributeId, physicalAttributeId, sourceSystemId, true);
                eCommerceViewAttributePriorityDetails.setCreatedDate(eCommerceViewAttributePriorities.getCreatedDate());
                if (SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue().equals(
                        sourceSystemId) &&
                        physicalAttributeId.equals(PhysicalAttributeCodeEditable.FAVOR_ITEM_DESCRIPTION_ATTRIBUTE_EDITABLE.getValue())) {
                    eCommerceViewAttributePriorityDetails.setSourceEditable(true);
                }
                eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(eCommerceViewAttributePriorities.getContent()));
                break;
            default:
                break;
        }
        return eCommerceViewAttributePriorityDetails;
    }

    /**
     * Get content source for brand -> for case data not exist in source 13, but exist in product override.
     *
     * @param sourceSystemId      - The source system id
     * @param physicalAttributeId - The physical attribute id
     * @param upc                 - The scan code number
     */
    private void getDataSourceSystemForBrandAttribute(Long sourceSystemId, Long physicalAttributeId, Long upc,
                                                      ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails) {
        StringBuilder contentBuilder = new StringBuilder();
        if (sourceSystemId.equals(SourceSystemNumber.GLADSON_SOURCE_SYSTEM_NUMBER.value)) {
            List<ProductScanCodeExtent> productScanCodeExtents = null;
            if (physicalAttributeId.equals(Long.valueOf(312))) {
                productScanCodeExtents = this.productScanCodeExtentRepository
                        .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                (ProductScanCodeExtent.BRAND_DIMENSION_CODE, ProductScanCodeExtent
                                        .PLINE_DIMENSION_CODE)));
            } else if (physicalAttributeId.equals(Long.valueOf(1602))) {
                productScanCodeExtents = this.productScanCodeExtentRepository
                        .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                (ProductScanCodeExtent.BRAND_DIMENSION_CODE)));
            } else if (physicalAttributeId.equals(Long.valueOf(1612))) {
                productScanCodeExtents = this.productScanCodeExtentRepository
                        .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                (ProductScanCodeExtent.PLINE_DIMENSION_CODE)));
            }
            //Check data exist, and return to content text
            if (productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                productScanCodeExtents.forEach(p -> {
                    if (StringUtils.isNotBlank(contentBuilder.toString())) {
                        contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                    }
                    if(p.getProdDescriptionText() != null) {
                        contentBuilder.append(p.getProdDescriptionText());
                    }
                    if(p.getLstUpdtTs() != null) {
                        eCommerceViewAttributePriorityDetails.setCreatedDate(p.getLstUpdtTs());
                    }
                });
            }
        } else if (sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.value)) {
            if (physicalAttributeId.equals(Long.valueOf(523))) {
                ProductMaster productMaster = productInfoRepository.findBySellingUnitsUpc(upc);
                if (productMaster != null && productMaster.getProductBrand() != null && productMaster.getProductBrand().getDisplayName() != null) {
                    contentBuilder.append(productMaster.getProductBrand().getDisplayName());
                }
            } else if (physicalAttributeId.equals(Long.valueOf(1642))) {
                MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                        .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, SourceSystemNumber
                                .PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.value);
                if (masterDataExtensionAttribute != null) {
                    if (masterDataExtensionAttribute.getAttributeValueText() != null) {
                        contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
                    }
                    if(masterDataExtensionAttribute.getLstUpdtTs() != null) {
                        eCommerceViewAttributePriorityDetails.setCreatedDate(masterDataExtensionAttribute.getLstUpdtTs());
                    }
                }
            }
        } else {
            MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                    .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, sourceSystemId);
            if (masterDataExtensionAttribute != null) {
                if (masterDataExtensionAttribute.getAttributeValueText() != null) {
                    contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
                }
                if(masterDataExtensionAttribute.getLstUpdtTs() != null) {
                    eCommerceViewAttributePriorityDetails.setCreatedDate(masterDataExtensionAttribute.getLstUpdtTs());
                }
            }
        }
        eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(contentBuilder.toString()));
    }

    /**
     * Get content source for size -> for case data not exist in source 13, but exist in product override.
     *
     * @param sourceSystemId      - The source system id
     * @param physicalAttributeId - The physical attribute id
     * @param upc                 - The scan code number
     */
    private void getDataSourceSystemForSizeAttribute(Long sourceSystemId, Long physicalAttributeId, Long upc,
                                                     ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails) {
        StringBuilder contentBuilder = new StringBuilder();
        switch (SourceSystemNumber.getNumber(sourceSystemId)) {
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                List<ProductScanCodeExtent> productScanCodeExtents = null;
                if (physicalAttributeId.equals(Long.valueOf(1606))) {
                    productScanCodeExtents = this.productScanCodeExtentRepository
                            .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                    (ProductScanCodeExtent.ESIZE_DIMENSION_CODE)));
                } else {
                    productScanCodeExtents = this.productScanCodeExtentRepository
                            .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                    (ProductScanCodeExtent.ISIZE_DIMENSION_CODE, ProductScanCodeExtent.IUOM_DIMENSION_CODE)));
                }
                //Check data exist, and return to content text
                if (productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                    productScanCodeExtents.forEach(p -> {
                        if (StringUtils.isNotBlank(contentBuilder.toString())) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                        }
                        if(p.getProdDescriptionText() != null) {
                            contentBuilder.append(p.getProdDescriptionText());
                        }
                        if(p.getLstUpdtTs() != null) {
                            eCommerceViewAttributePriorityDetails.setCreatedDate(p.getLstUpdtTs());
                        }
                    });
                }
                break;
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
                if (physicalAttributeId.equals(Long.valueOf(313))) {
                    SellingUnit sellingUnit = sellingUnitRepository.findOne(upc);
                    if (sellingUnit != null) {
                        if (sellingUnit.getQuantity() != null) {
                            contentBuilder.append(sellingUnit.getQuantity());
                        }
                        if (sellingUnit.getRetailUnitOfMeasure() != null && sellingUnit.getRetailUnitOfMeasure().getDescription() != null) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR).append(sellingUnit.getRetailUnitOfMeasure().getDescription());
                        }
                    }
                } else if (physicalAttributeId.equals(Attribute.SIZE)) {
                    MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                            .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, sourceSystemId);
                    if (masterDataExtensionAttribute != null) {
                        if (masterDataExtensionAttribute.getAttributeValueText() != null) {
                            contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
                        }
                        if(masterDataExtensionAttribute.getLstUpdtTs() != null) {
                            eCommerceViewAttributePriorityDetails.setCreatedDate(masterDataExtensionAttribute.getLstUpdtTs());
                        }
                    }
                } else if (physicalAttributeId.equals(Long.valueOf(591))) {
                    SellingUnit sellingUnit = sellingUnitRepository.findOne(upc);
                    if (sellingUnit != null && sellingUnit.getTagSize() != null) {
                        contentBuilder.append(sellingUnit.getTagSize());
                    }
                }
                break;
            case GS1_SOURCE_SYSTEM_NUMBER:
            	List<Long> attributesIds = new ArrayList<>(Arrays.asList(Attribute.NET_CONTENT,
    					Attribute.NET_CONTENT_UNIT_OF_MEASURE));
    			this.longPopulator.populate(attributesIds, DEFAULT_ITEM_COUNT);
                List<MasterDataExtensionAttribute> masterDataExtensionAttributes1 = this
                        .masterDataExtensionAttributeRepository.findByKeyAttributeIdInAndKeyIdAndKeyDataSourceSystemOrderByKeyAttributeId(attributesIds, upc, sourceSystemId);
                //Check data exist, and return to content text
                if (masterDataExtensionAttributes1 != null && !masterDataExtensionAttributes1.isEmpty()) {
                    masterDataExtensionAttributes1.forEach(m -> {
                        if (StringUtils.isNotBlank(contentBuilder.toString())) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                        }
                        if(m.getKey().getAttributeId().equals(Attribute.NET_CONTENT)) {
                            if(m.getAttributeValueNumber() != null) {
                                contentBuilder.append(m.getAttributeValueNumber());
                            }
                        }else{
                            if(m.getAttributeValueText() != null) {
                                contentBuilder.append(m.getAttributeValueText());
                            }
                        }
                        if(m.getLstUpdtTs() != null) {
                            eCommerceViewAttributePriorityDetails.setCreatedDate(m.getLstUpdtTs());
                        }
                    });
                }
                break;
            case KWIKEE_SOURCE_SYSTEM_NUMBER:
            	List<Long> listAttributesId = new ArrayList<>(Arrays.asList(Attribute.SIZE, Attribute.PRODUCT_UOM));
                this.longPopulator.populate(listAttributesId, DEFAULT_ITEM_COUNT);
                List<MasterDataExtensionAttribute> masterDataExtensionAttributes2 = this
                        .masterDataExtensionAttributeRepository.findByKeyAttributeIdInAndKeyIdAndKeyDataSourceSystemOrderByKeyAttributeId(listAttributesId, upc, sourceSystemId);
                //Check data exist, and return to content text
                if (masterDataExtensionAttributes2 != null && !masterDataExtensionAttributes2.isEmpty()) {
                    masterDataExtensionAttributes2.forEach(m -> {
                        if (StringUtils.isNotBlank(contentBuilder.toString())) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                        }
                        if(m.getAttributeValueText() != null) {
                            contentBuilder.append(m.getAttributeValueText());
                        }
                        if(m.getLstUpdtTs() != null) {
                            eCommerceViewAttributePriorityDetails.setCreatedDate(m.getLstUpdtTs());
                        }
                    });
                }
                break;
            default:
                MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                        .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, sourceSystemId);
                if (masterDataExtensionAttribute != null) {
                    if (masterDataExtensionAttribute.getAttributeValueText() != null) {
                        contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
                    }
                    if(masterDataExtensionAttribute.getLstUpdtTs() != null) {
                        eCommerceViewAttributePriorityDetails.setCreatedDate(masterDataExtensionAttribute.getLstUpdtTs());
                    }
                }
                break;
        }
        eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(contentBuilder.toString()));
    }

    /**
     * Get content source for display name -> for case data not exist in source 13, but exist in product override.
     *
     * @param sourceSystemId      - The source system id
     * @param physicalAttributeId - The physical attribute id
     * @param upc                 - The scan code number
     * @param productId           - The product id
     */
    private void getDataSourceSystemForDisplayNameAttribute(Long sourceSystemId, Long physicalAttributeId,
                                                            Long upc, Long productId,
                                                            ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails) {
        StringBuilder contentBuilder = new StringBuilder();
        if (sourceSystemId.equals(SourceSystemNumber.GLADSON_SOURCE_SYSTEM_NUMBER.value)) {
            List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository
                    .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                            (ProductScanCodeExtent.IDESC_DIMENSION_CODE)));
            //Check data exist, and return to content text
            if (productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                productScanCodeExtents.forEach(p -> {
                    if (StringUtils.isNotBlank(contentBuilder.toString())) {
                        contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                    }
                    if(p.getProdDescriptionText() != null){
                        contentBuilder.append(p.getProdDescriptionText());
                    }
                    if(p.getLstUpdtTs() != null){
                        eCommerceViewAttributePriorityDetails.setCreatedDate(p.getLstUpdtTs());
                    }
                });
            }
        } else if (sourceSystemId.equals(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.value)) {
            if (physicalAttributeId.equals(Long.valueOf(310))) {
                List<ProductDescription> productDescriptions = this.productDescriptionRepository
                        .findByKeyProductIdAndAndKeyDescriptionTypeInAndKeyLanguageType(productId, new ArrayList<>
                                        (Arrays.asList(PRODUCT_DESCRIPTION_TEXT_TAG1, PRODUCT_DESCRIPTION_TEXT_TAG2))
                                , PRODUCT_DESCRIPTION_TEXT_LANGUAGE);
                productDescriptions.forEach(p -> {
                    if (StringUtils.isNotBlank(contentBuilder.toString())) {
                        contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                    }
                    if(p.getDescription() != null){
                        contentBuilder.append(p.getDescription());
                    }
                });
            } else if (physicalAttributeId.equals(Long.valueOf(1601))) {
                List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository
                        .findByKeyScanCodeIdAndKeyProdExtDataCodeIn(upc, new ArrayList<>(Arrays.asList
                                (ProductScanCodeExtent.ESHRT_DIMENSION_CODE)));
                //Check data exist, and return to content text
                if (productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                    productScanCodeExtents.forEach(p -> {
                        if (StringUtils.isNotBlank(contentBuilder.toString())) {
                            contentBuilder.append(ProductECommerceViewService.SPACE_SEPARATOR);
                        }
                        if(p.getProdDescriptionText() != null){
                            contentBuilder.append(p.getProdDescriptionText());
                        }
                        if(p.getLstUpdtTs() != null){
                            eCommerceViewAttributePriorityDetails.setCreatedDate(p.getLstUpdtTs());
                        }
                    });
                }
            }
        } else {
            MasterDataExtensionAttribute masterDataExtensionAttribute = this.masterDataExtensionAttributeRepository
                    .findByKeyAttributeIdAndKeyIdAndKeyDataSourceSystem(physicalAttributeId, upc, sourceSystemId);
            if (masterDataExtensionAttribute != null) {
                if (masterDataExtensionAttribute.getAttributeValueText() != null) {
                    contentBuilder.append(masterDataExtensionAttribute.getAttributeValueText());
                }
                if(masterDataExtensionAttribute.getLstUpdtTs() != null){
                    eCommerceViewAttributePriorityDetails.setCreatedDate(masterDataExtensionAttribute.getLstUpdtTs());
                }
            }
        }
        eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(contentBuilder.toString()));
    }

    /**
     * Returns the directions of the item find by attribute priorities.
     *
     * @param physicalAttributeId the physical attribute id to find.
     * @param dataSourceSystem    the data source system id to find.
     * @param scanCodeId          the scan code id to find.
     * @return the entity that contains directions text.
     */
    private void getDataSourceSystemForDirectionsAttribute(Long dataSourceSystem, Long physicalAttributeId, long scanCodeId,
                                                           ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails) {
        switch (SourceSystemNumber.getNumber(dataSourceSystem)) {
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository.
                        findByKeyScanCodeIdAndKeyProdExtDataCodeInOrderByKeyProdExtDataCodeDesc(scanCodeId, Arrays.asList(ProductECommerceViewService.DIRECTION_PRODUCT_EXTENT_DATA_CODE));
                if (!productScanCodeExtents.isEmpty()) {
                    eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(productScanCodeExtents.stream()
                            .filter(productScanCodeExtent -> StringUtils.isNotBlank(productScanCodeExtent.getProdDescriptionText()))
                            .map(ProductScanCodeExtent::getProdDescriptionText)
                            .collect(Collectors.joining(ProductECommerceViewService.SPACE_SEPARATOR))));
                    eCommerceViewAttributePriorityDetails.setCreatedDate(productScanCodeExtents.stream()
                            .filter(productScanCodeExtent -> productScanCodeExtent.getLstUpdtTs() != null)
                            .map(ProductScanCodeExtent::getLstUpdtTs)
                            .max(Comparator.naturalOrder()).orElse(null));
                }else{
					eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(StringUtils.EMPTY));
				}
                break;
            case OBPS_SOURCE_SYSTEM_NUMBER:
            case KWIKEE_SOURCE_SYSTEM_NUMBER:
            case ITEMMASTER_COM_SOURCE_SYSTEM_NUMBER:
            case MANUFACTURER_SOURCE_SYSTEM_NUMBER:
            case OTHER_RETAILERS_SOURCE_SYSTEM_NUMBER:
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
            case GS1_SOURCE_SYSTEM_NUMBER:
                MasterDataExtensionAttribute attribute = this.masterDataExtensionAttributeRepository
                        .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(physicalAttributeId, scanCodeId,
                                ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                if (attribute != null) {
                    eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(attribute.getAttributeValueText()));
                    eCommerceViewAttributePriorityDetails.setCreatedDate(attribute.getLstUpdtTs());
                    eCommerceViewAttributePriorityDetails.setPostedDate(
                            attribute.getAttributeValueTime() != null ? attribute.getAttributeValueTime().toLocalDate() : null);
                }else{
					eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(StringUtils.EMPTY));
				}
                break;
            default:
				eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(StringUtils.EMPTY));
                break;
        }
    }

    /**
     * Returns the directions of the item find by attribute priorities.
     *
     * @param physicalAttributeId the physical attribute id to find.
     * @param dataSourceSystem    the data source system id to find.
     * @param scanCodeId          the scan code id to find.
     * @return the entity that contains directions text.
     */
    private void getDataSourceSystemForWarningsAttribute(Long dataSourceSystem, Long physicalAttributeId, long scanCodeId,
                                                         ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails) {
        switch (SourceSystemNumber.getNumber(dataSourceSystem)) {
            case GLADSON_SOURCE_SYSTEM_NUMBER:
                List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository.
                        findByKeyScanCodeIdAndKeyProdExtDataCodeInOrderByKeyProdExtDataCodeDesc(scanCodeId, Arrays.asList(ProductECommerceViewService.WARNING_PRODUCT_EXTENT_DATA_CODE));
                if (!productScanCodeExtents.isEmpty()) {
                    eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(productScanCodeExtents.stream()
                            .filter(productScanCodeExtent -> StringUtils.isNotBlank(productScanCodeExtent.getProdDescriptionText()))
                            .map(ProductScanCodeExtent::getProdDescriptionText)
                            .collect(Collectors.joining(ProductECommerceViewService.SPACE_SEPARATOR))));
                    eCommerceViewAttributePriorityDetails.setCreatedDate(productScanCodeExtents.stream()
                            .filter(productScanCodeExtent -> productScanCodeExtent.getLstUpdtTs() != null)
                            .map(ProductScanCodeExtent::getLstUpdtTs)
                            .max(Comparator.naturalOrder()).orElse(null));
                }else{
					eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(StringUtils.EMPTY));
				}
                break;
            case OBPS_SOURCE_SYSTEM_NUMBER:
                MasterDataExtensionAttribute warningsAttribute = this.masterDataExtensionAttributeRepository
                        .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(LogicAttributeCode.WARNINGS_LOGIC_ATTRIBUTE_ID.getValue(), scanCodeId,
                                ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                if (warningsAttribute != null) {
                    eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(warningsAttribute.getAttributeValueText()));
                    eCommerceViewAttributePriorityDetails.setCreatedDate(warningsAttribute.getLstUpdtTs());
                    eCommerceViewAttributePriorityDetails.setPostedDate(
                            warningsAttribute.getAttributeValueTime() != null ? warningsAttribute.getAttributeValueTime().toLocalDate() : null);
                }

                MasterDataExtensionAttribute allergensAttribute = this.masterDataExtensionAttributeRepository
                        .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(LogicAttributeCode.ALLERGENS_LOGIC_ATTRIBUTE_ID.getValue(), scanCodeId,
                                ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                if (allergensAttribute != null) {
                    eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(allergensAttribute.getAttributeValueText()));
                    eCommerceViewAttributePriorityDetails.setCreatedDate(allergensAttribute.getLstUpdtTs());
                    eCommerceViewAttributePriorityDetails.setPostedDate(
                            allergensAttribute.getAttributeValueTime() != null ? allergensAttribute.getAttributeValueTime().toLocalDate() : null);
                }else{
					eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(StringUtils.EMPTY));
				}
                break;
            case PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER:
            case ESHA_GENESIS_SOURCE_SYSTEM_NUMBER:
            case GS1_SOURCE_SYSTEM_NUMBER:
            case KWIKEE_SOURCE_SYSTEM_NUMBER:
            case ITEMMASTER_COM_SOURCE_SYSTEM_NUMBER:
            case MANUFACTURER_SOURCE_SYSTEM_NUMBER:
            case OTHER_RETAILERS_SOURCE_SYSTEM_NUMBER:
                MasterDataExtensionAttribute attribute = this.masterDataExtensionAttributeRepository
                        .findByKeyAttributeIdAndKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystem(physicalAttributeId, scanCodeId,
                                ProductECommerceViewService.UPC_ITEM_KEY_CODE, dataSourceSystem);
                if (attribute != null) {
                    eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(attribute.getAttributeValueText()));
                    eCommerceViewAttributePriorityDetails.setCreatedDate(attribute.getLstUpdtTs());
                    eCommerceViewAttributePriorityDetails.setPostedDate(
                            attribute.getAttributeValueTime() != null ? attribute.getAttributeValueTime().toLocalDate() : null);
                }else{
					eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(StringUtils.EMPTY));
				}
                break;
            default:
				eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentString(StringUtils.EMPTY));
                break;
        }
    }

    /**
     * Update eCommerce View data source. Will by update content and set product attribute overwrite.
     *
     * @param eCommerceViewAttributePriority the eCommerceViewAttributePriorities object. Contain information change
     * @param userId the user id handle updated
     * @throws Exception
     */
    public void updateECommerceViewDataSource(ECommerceViewAttributePriority eCommerceViewAttributePriority, String userId) throws Exception {
    	//Use web service to save all eCommerce View related changes except for Romance Copy and Display name.
        this.productAttributeManagementServiceClient.updateECommerceViewDataSource(eCommerceViewAttributePriority, userId);
        //Use JDBC to save data of Romance Copy and Display name.
        this.updateRomanceCopyAndDisplayName(eCommerceViewAttributePriority, userId);
    }

    /**
     * Update data of Romance Copy and Display name by JDBC.
     *
     * @param eCommerceViewAttributePriority the eCommerceViewAttributePriorities object. Contain information change
     * @param userId the user id handle updated
     */
	private void updateRomanceCopyAndDisplayName(ECommerceViewAttributePriority eCommerceViewAttributePriority, String userId) {
		if(CollectionUtils.isNotEmpty(eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails())) {
        	for(ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails : eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails()){
        		if (eCommerceViewAttributePriorityDetails.isSelected() && eCommerceViewAttributePriorityDetails.isSourceEditable()) {
        			if(eCommerceViewAttributePriority.getAttributeId().equals(ProductECommerceViewService
        					.LogicAttributeCode.DISPLAY_NAME_LOGIC_ATTRIBUTE_ID.getValue())){
        				this.updateProductScanCodeExtent(eCommerceViewAttributePriority.getPrimaryScanCode(), 
        						eCommerceViewAttributePriorityDetails.<String>getContent().getContent(),
        						PROD_EXT_DTA_CD_FOR_ECOM_DES1, userId);
        			}else if(eCommerceViewAttributePriority.getAttributeId().equals(ProductECommerceViewService
        					.LogicAttributeCode.ROMANCE_COPY_LOGIC_ATTRIBUTE_ID.getValue())){
        				this.updateProductScanCodeExtent(eCommerceViewAttributePriority.getPrimaryScanCode(), 
        						eCommerceViewAttributePriorityDetails.<String>getContent().getContent(),
        						PROD_EXT_DTA_CD_FOR_ECOM_DES2, userId);
        			}else if(eCommerceViewAttributePriority.getAttributeId().equals(LogicAttributeCode.FAVOR_ITEM_DESCRIPTION_LOGIC_ATTRIBUTE_ID.getValue())){
        			    // Update Favor item description into product scan code extent
        				this.updateProductScanCodeExtent(eCommerceViewAttributePriority.getPrimaryScanCode(),
        						eCommerceViewAttributePriorityDetails.<String>getContent().getContent(),
                                ProductScanCodeExtent.FAVOR_ITEM_DESCRIPTION_CODE, userId);
        			}
        		}
        	}
        }
	}
    /**
     * Find all specification attribute priorities to edit source.
     *
     * @param productId  the product id.
     * @param scanCodeId the scan code id.
     * @return the  ECommerceViewAttributePriority object.
     */
    public ECommerceViewAttributePriority findAllSpecificationAttributePriorities(long productId, long scanCodeId) {
        ECommerceViewAttributePriority eCommerceViewAttributePriority = new ECommerceViewAttributePriority();
        eCommerceViewAttributePriority.seteCommerceViewAttributePriorityDetails(new ArrayList<>());
        // Get eCommerce attribute information.
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = this.masterDataExtensionAttributeRepository
                .findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeIdAscKeySequenceNumberAsc(productId, ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, SourceSystemNumber.ECOMMERCE_SOURCE_SYSTEM_NUMBER.value);
        if (masterDataExtensionAttributes != null && !masterDataExtensionAttributes.isEmpty()) {
            List<AttributeValue> specifications = this.getListSpecification(masterDataExtensionAttributes);
            eCommerceViewAttributePriority.setMainContent(new ECommerceViewAttributePriority.ContentSpecification(specifications));
            eCommerceViewAttributePriority.setMasterDataExtensionAttribute(masterDataExtensionAttributes.get(0));
        }
        //Get Overwrite Attribute Information
        ProductAttributeOverwrite productAttributeOverwrite = this.productAttributeOverwriteRepository
                .findByKeyItemProductKeyCodeAndKeyKeyIdAndKeyLogicAttributeId(
                        ProductECommerceViewService.PRODUCT_ITEM_KEY_CODE, productId,
                        LogicAttributeCode.SPECIFICATION_LOGIC_ATTRIBUTE_ID.value);
        if (productAttributeOverwrite != null) {
            eCommerceViewAttributePriority.setProductAttributeOverwrite(productAttributeOverwrite);
        }
        // Get priority attribute Information.
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this.targetSystemAttributePriorityRepository
                .findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(
                        LogicAttributeCode.SPECIFICATION_LOGIC_ATTRIBUTE_ID.getValue().intValue());
        boolean isSourceSystemDefault = false;
        if (targetSystemAttributePriorities != null && !targetSystemAttributePriorities.isEmpty()) {
            Map<String, Boolean> addedAttribute = new HashMap<>();
            String key;
            for (TargetSystemAttributePriority attributePriority : targetSystemAttributePriorities) {
                key = String.format("%s_%s", attributePriority.getKey().getDataSourceSystemId(), attributePriority.getKey().getPhysicalAttributeId());
                // Check to avoid duplicate attribute.
                if(!addedAttribute.containsKey(key)) {
                    List<AttributeValue> specificationList = this.getSpecificationBySourceSystem(productId, scanCodeId, attributePriority.getKey().getDataSourceSystemId());
                    if ((specificationList != null &&
                            !specificationList.isEmpty()) ||
                            attributePriority.getKey().getDataSourceSystemId() == 0) {
                        ECommerceViewAttributePriorityDetails eCommerceViewAttributePriorityDetails = new ECommerceViewAttributePriorityDetails();
                        if(specificationList!= null) {
                            eCommerceViewAttributePriorityDetails.setContent(new ECommerceViewAttributePriority.ContentSpecification(specificationList));
                        }
                        // get the first source after information display at HEB.com area.
                        if (!isSourceSystemDefault && attributePriority.getKey().getDataSourceSystemId()!=0) {
                            isSourceSystemDefault = true;
                            eCommerceViewAttributePriorityDetails.setSourceDefault(true);
                            if(productAttributeOverwrite == null){
                                // If no overwrite then set selected first source.
                                eCommerceViewAttributePriorityDetails.setSelected(true);
                            }
                        }
                        if (productAttributeOverwrite != null && (productAttributeOverwrite.getKey().getLogicAttributeId()
                                .intValue() == attributePriority.getKey().getLogicalAttributeId()) &&
                                productAttributeOverwrite.getKey().getPhysicalAttributeId().equals
                                        (attributePriority.getKey().getPhysicalAttributeId()) &&
                                productAttributeOverwrite.getKey().getSourceSystemId().equals
                                        (attributePriority.getKey().getDataSourceSystemId())) {
                            eCommerceViewAttributePriorityDetails.setSelected(true);
                        }
                        if (attributePriority.getSourceSystem() != null) {
                            attributePriority.getSourceSystem().getId();
                            eCommerceViewAttributePriorityDetails.setSourceDescription
                                    (StringUtils.trim(attributePriority.getSourceSystem().getDescription()));
                        }
                        eCommerceViewAttributePriorityDetails.setTargetSystemAttributePriority(attributePriority);
                        eCommerceViewAttributePriority.geteCommerceViewAttributePriorityDetails().add(eCommerceViewAttributePriorityDetails);
                        addedAttribute.put(key, true);
                    }
                }
            }
        }
        return eCommerceViewAttributePriority;
    }

    /**
     * Update ECommerce View information.
     *
     * @param eCommerceViewDetails the new ECommerceViewDetails.
     * @param userId the user id.
     */
    public void updateECommerceViewInformation(ECommerceViewDetails eCommerceViewDetails, String userId) {
        //Update eCommerce View information with product attributes management web service. The list of attribute
        //of eCommerce View information has been updated with product eCommerce View information with product
        //attribute management web service: all resource, show on site
        this.productAttributeManagementServiceClient.updateECommerceViewInformation(eCommerceViewDetails, userId);
        //Update eCommerce View information with product management web service.
        //The list of attribute of eCommerce View information has been updated with product eCommerce View
        //information web service : FulfillmentProgram, SubscriptionDate, Show on site, short description
        this.productManagementServiceClient.updateECommerceViewInformation(eCommerceViewDetails, userId);
        //Update data of Display name by JDBC.
        if(eCommerceViewDetails.getDisplayName() != null){
        	this.updateProductScanCodeExtent(eCommerceViewDetails.getScanCodeId(), eCommerceViewDetails.getDisplayName().getContent(),
        			PROD_EXT_DTA_CD_FOR_ECOM_DES1, userId);
        }
        //Update data of Romance Copy by JDBC.
        if(eCommerceViewDetails.getRomanceCopy() != null){
        	this.updateProductScanCodeExtent(eCommerceViewDetails.getScanCodeId(), eCommerceViewDetails.getRomanceCopy().getContent(),
					PROD_EXT_DTA_CD_FOR_ECOM_DES2, userId);
		}
        //Update data of Favor item description by JDBC.
        if(eCommerceViewDetails.getFavorItemDescription() != null){
            this.updateProductScanCodeExtent(eCommerceViewDetails.getScanCodeId(), eCommerceViewDetails.getFavorItemDescription().getContent(),
                    ProductScanCodeExtent.FAVOR_ITEM_DESCRIPTION_CODE, userId);
        }
    }

    /**
     * Update data of display name or romance copy into ProductScanCodeExtent table.
     *
     * @param scanCodeId - The scan code id.
     * @param content - The data of display name or romance copy.
     * @param prodExtDtaCd - The product extend data code.
     * @param userId - The user id.
     */
	private void updateProductScanCodeExtent(long scanCodeId, String content, String prodExtDtaCd, String userId) {
		ProductScanCodeExtent productScanCodeExtent = this.productScanCodeExtentRepository.
		        findByKeyScanCodeIdAndKeyProdExtDataCodeAndSourceSystem(scanCodeId, prodExtDtaCd, SourceSystemNumber
						.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue().intValue());
		if(productScanCodeExtent == null){
			ProductScanCodeExtentKey productScanCodeExtentKey = new ProductScanCodeExtentKey();
			productScanCodeExtentKey.setScanCodeId(scanCodeId);
			productScanCodeExtentKey.setProdExtDataCode(prodExtDtaCd);
			productScanCodeExtent = new ProductScanCodeExtent();
			productScanCodeExtent.setKey(productScanCodeExtentKey);
			productScanCodeExtent.setSourceSystem(SourceSystemNumber.PRODUCT_MAINTENANCE_SOURCE_SYSTEM_NUMBER.getValue().intValue());
		}
		productScanCodeExtent.setLstUpdtTs(LocalDate.now());
		productScanCodeExtent.setCreateUserId(userId);
		productScanCodeExtent.setLastUpdateUserId(userId);
		if(StringUtils.isNotBlank(content)) {
			productScanCodeExtent.setProdDescriptionText(content);
			this.productScanCodeExtentRepository.save(productScanCodeExtent);
		}else{
		    // Check product is existing or not before delete to avoid error when delete a object that not existed in data.
            List<ProductScanCodeExtent> productScanCodeExtents = this.productScanCodeExtentRepository.findByKeyScanCodeIdAndKeyProdExtDataCodeIn(
                    scanCodeId, Arrays.asList(prodExtDtaCd));
            if(productScanCodeExtents != null && !productScanCodeExtents.isEmpty()) {
                this.productScanCodeExtentRepository.delete(productScanCodeExtent);
            }
		}
	}

    /**
     * Find the list of Target System Attribute Priority for show name attribute.
     * @param attributeId - The attribute id
     * @return ECommerceViewAttributePriority object
     * @throws Exception
     */
    public List<TargetSystemAttributePriority> findAttributeMappingByLogicalAttribute(int attributeId) throws Exception{
        List<TargetSystemAttributePriority> targetSystemAttributePriorities = this.targetSystemAttributePriorityRepository
                .findByKeyLogicalAttributeIdOrderByAttributePriorityNumber(attributeId);
        if(targetSystemAttributePriorities != null && !targetSystemAttributePriorities.isEmpty()){
            Map<Long, List<TargetSystemAttributePriority>> dataSourceCounters = targetSystemAttributePriorities.stream()
                    .collect(Collectors.groupingBy(s->s.getKey().getDataSourceSystemId()));
            Map<Long, Integer> dataSourceIdCounters = new HashMap<>();
            for(TargetSystemAttributePriority targetSystemAttributePriority : targetSystemAttributePriorities){
                if(targetSystemAttributePriority.getSourceSystem() != null){
                    targetSystemAttributePriority.getSourceSystem().getId();
                    if(dataSourceCounters.get(targetSystemAttributePriority.getSourceSystem().getId()) != null &&
                            dataSourceCounters.get(targetSystemAttributePriority.getSourceSystem().getId()).size
                                    () > 1){
                        Integer idDatasource = 0;
                        if(dataSourceIdCounters.get(targetSystemAttributePriority.getSourceSystem().getId()) != null){
                            idDatasource = dataSourceIdCounters.get(targetSystemAttributePriority.getSourceSystem().getId());
                        }
                        idDatasource++;
                        dataSourceIdCounters.put(targetSystemAttributePriority.getSourceSystem().getId(),
                                idDatasource);
                        targetSystemAttributePriority.setSourceSystemName(
                                (StringUtils.trim(targetSystemAttributePriority
                                        .getSourceSystem().getDescription()).concat(" - ").concat(idDatasource
                                        .toString())));
                    }else{
                        targetSystemAttributePriority.setSourceSystemName(StringUtils.trim(targetSystemAttributePriority
                                .getSourceSystem().getDescription()));
                    }
                }
            }
        }
        return targetSystemAttributePriorities;
    }
    /**
     * This returns a list of online attributes audits based on the prodId.
     * @param prodId way to uniquely ID the set of online attributes audits requested
     * @return a list of all the changes made to an product's online attributes audits.
     */
    List<AuditRecord> getPublishedAttributesAuditInformation(Long prodId, Long upc) {
        return this.auditService.getPublishedAttributesAuditInformation(prodId, upc);
    }
    /**
     * This returns a list of online attributes audits based on the prodId.
     * @param prodId way to uniquely ID the set of online attributes audits requested
     * @return a list of all the changes made to an product's online attributes audits.
     */
    List<AuditRecord> getFulfillmentAttributesAuditInformation(Long prodId) {
        return this.auditService.getFulfillmentAttributesAuditInformation(prodId);
    }

    /**
     * Find alert staging by product id.
     *
     * @param productId the product id.
     * @return list of alert staging.
     */
    public List<AlertStaging> findAlertStagingByProductId(long productId) {
        return this.alertStagingRepository
                .findAlertStagingByAlertTypeAndAlertStatusAndAlertKey
                        (ALERT_STAGING_PRUPD, ALERT_STAGING_STATUS_ACTIVE, String.format("%09d", productId));
    }
}