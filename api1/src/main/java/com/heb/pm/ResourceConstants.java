/*
 * ResourceConstants
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm;

/**
 * Holds constants that reflect ARBAF resource names defined for the application.
 *
 * @author d116773
 * @upadated by s753601
 * @since 2.0.1
 */
public final class ResourceConstants {

	private ResourceConstants() {}

	public static final String PRODUCT_DISCONTINUE_REPORT = "PD_DSCO_01";
	public static final String PRODUCT_DISCONTINUE_DEFAULT_PARAMETERS = "PD_DSCO_02";
	public static final String PRODUCT_DISCONTINUE_EXCEPTION_PARAMETERS = "PD_DSCO_03";
	public static final String PRODUCT_DISCONTINUE_PARAMETERS_AUDIT = "PD_DSCO_04";

	public static final String REMOVE_FROM_STORES_SUPPORT = "PD_UPCM_01";

	public static final String PRODUCT_BASIC_INFORMATION = "PD_INFO_01";
	public static final String PRODUCT_DESCRIPTION = "PD_INFO_02";
	public static final String PRODUCT_GENERIC_PRODUCT = "PD_INFO_05";
	public static final String PRODUCT_SELF_MANUFACTURED = "PD_INFO_06";
	public static final String PRODUCT_DEPOSIT_UPC = "PD_INFO_07";
	public static final String PRODUCT_QUANTITY_REQUIRED = "PD_INFO_08";
	public static final String PRODUCT_COLOR = "PD_INFO_09";
	public static final String PRODUCT_MODEL = "PD_INFO_10";
	public static final String PRODUCT_STYLE_DESCRIPTION = "PD_INFO_11";
	public static final String PRODUCT_CRITICAL_PRODUCT = "PD_INFO_12";
	public static final String PRODUCT_PRE_PRICE = "PD_INFO_13";
	public static final String PRODUCT_MAP = "PD_INFO_14";
	public static final String PRODUCT_DRUG_FACT_PANEL = "PD_INFO_15";
	public static final String PRODUCT_FSA = "PD_INFO_16";
	public static final String PRODUCT_FAMILY_CODE_1 = "PD_INFO_17";
	public static final String PRODUCT_FAMILY_CODE_2 = "PD_INFO_18";
	public static final String PRODUCT_PACKAGING = "PD_INFO_19";
	public static final String PRODUCT_SHOW_CALORIES = "PD_INFO_20";
	public static final String PRODUCT_SUB_COMMODITY = "PD_INFO_21";
	public static final String PRODUCT_SCALE_LABEL = "PD_INFO_22";
	public static final String PRODUCT_PRICE_REQUIRED = "PD_INFO_23";
	public static final String PRODUCT_QUALIFYING_CONDITION = "PD_INFO_24";
	public static final String PRODUCT_RETAIL_TAX = "PD_INFO_25";
	public static final String PRODUCT_INFO_SELECT_INGREDIENTS = "PD_INFO_26";
	public static final String PRODUCT_FOOD_STAMP = "PD_INFO_27";
	public static final String BATCH_UPLOAD_BY_eCOMMERCE_ATTRIBUTE = "BU_ECOM_01";
	public static final String ECOMMERCE_ATTRIBUTE_MAINTENANCE = "EC_AMAN_01";
	public static final String HIERARCHY_ATTRIBUTE_MAINTENANCE = "EC_AMAN_02";
	public static final String INVENTORY_LOOKUP_STORE = "IN_STOR_01";

	public static final String UPC_SWAP_COMMON = "PD_MENU_02";
	public static final String UPC_SWAP_WAREHOUSE_TO_WAREHOUSE = "PD_SWAP_04";
	public static final String UPC_SWAP_DSD_TO_BOTH = "PD_SWAP_02";
	public static final String UPC_SWAP_BOTH_TO_DSD = "PD_SWAP_03";
	public static final String ADD_ASSOCIATE = "PD_SWAP_05";

	public static final String VENDOR_LIST = "VN_LIST_01";

	public static final String PRODUCT_HIERARCHY_BDM_LIST = "PH_BDML_01";
	public static final String PRODUCT_HIERARCHY_CLASS_COMMODITYLIST = "PH_CLSC_01";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_LIST = "PH_SUBC_01";
	public static final String PRODUCT_HIERARCHY_SUB_DEPARTMENT_LIST = "PH_SUBD_01";
	public static final String PRODUCT_HIERARCHY_VIEW = "PH_VIEW_00";
	public static final String PRODUCT_HIERARCHY_SHIPPING_RESTRICTIONS = "PH_DFLT_01";
	public static final String PRODUCT_HIERARCHY_SELLING_RESTRICTIONS = "PH_DFLT_02";
	public static final String PRODUCT_HIERARCHY_ITEM_CLASS_DESCRIPTION = "PH_CLSS_01";
	public static final String PRODUCT_HIERARCHY_ITEM_CLASS_BILL_COST = "PH_CLSS_02";
	public static final String PRODUCT_HIERARCHY_ITEM_CLASS_MERCHANT_TYPE = "PH_CLSS_03";
	public static final String PRODUCT_HIERARCHY_ITEM_CLASS_PSS_DEPARTMENT = "PH_CLSS_04";
	public static final String PRODUCT_HIERARCHY_ITEM_CLASS_GENERIC_CLASS = "PH_CLSS_05";
	public static final String PRODUCT_HIERARCHY_COMMODITY_DESCRIPTION = "PH_COMM_01";
	public static final String PRODUCT_HIERARCHY_COMMODITY_PSS_DEPARTMENT = "PH_COMM_02";
	public static final String PRODUCT_HIERARCHY_COMMODITY_BDM_ID = "PH_COMM_03";
	public static final String PRODUCT_HIERARCHY_COMMODITY_BDA = "PH_COMM_04";
	public static final String PRODUCT_HIERARCHY_COMMODITY_EBM = "PH_COMM_05";
	public static final String PRODUCT_HIERARCHY_COMMODITY_ACTIVE = "PH_COMM_06";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_DESCRIPTION = "PH_SUBC_02";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_PRODUCT_CATEGORY_ID = "PH_SUBC_03";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_ACTIVE = "PH_SUBC_04";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_IMS_CODE_COMMODITY = "PH_SUBC_05";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_FOOD_STAMP_ELIGIBLE = "PH_SUBC_06";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_TAXABLE = "PH_SUBC_07";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_TAXABLE_TAX_CATEGORY = "PH_SUBC_08";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_NON_TAXABLE_TAX_CATEGORY = "PH_SUBC_09";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_UNIT_OF_MEASURE = "PH_SUBC_10";
	public static final String PRODUCT_HIERARCHY_SUB_COMMODITY_STATE_WARNINGS = "PH_SUBC_11";

	public static final String INDEX_REFRESH_JOBS = "IS_JOBS_01";

	public static final String INGREDIENTS_REPORT = "RP_INGR_01";

	public static final String SCALE_MANAGEMENT_UPC_MAINTENANCE = "SM_UPCM_01";
	public static final String SCALE_MANAGEMENT_NUTRIENTS = "SM_NTRN_01";
	public static final String SCALE_MANAGEMENT_NUTRIENT_STATEMENT = "SM_NTRN_02";
	public static final String SCALE_MANAGEMENT_INGREDIENTS = "SM_INGR_01";
	public static final String SCALE_MANAGEMENT_INGREDIENT_STATEMENTS = "SM_INGR_02";
	public static final String SCALE_MANAGEMENT_INGREDIENT_CATEGORIES = "SM_CODE_04";
	public static final String SCALE_MAINTENANCE_LABEL_FORMATS = "SM_CODE_02";
	public static final String SCALE_MAINTENANCE_ACTION_CODES = "SM_CODE_01";
	public static final String SCALE_MANAGEMENT_GRAPHICS_CODE = "SM_CODE_03";
	public static final String SCALE_MANAGEMENT_REPORTS = "SM_MENU_01";
	public static final String SCALE_MANAGEMENT_INGREDIENT_REPORT = "SM_INGR_03";
	public static final String SCALE_MANAGEMENT_NUTRIENT_UOM_CODE = "SM_CODE_04";
	public static final String SCALE_MANAGEMENT_NLEA16_NUTRIENT_STATEMENT = "SM_NLEA_01";
	public static final String SCALE_MAINTENANCE_MENU = "SM_MAINT_00";
	public static final String SCALE_MAINTENANCE_SEND_LOAD = "SM_MAINT_01";
	public static final String SCALE_MAINTENANCE_CHECK_STATUS = "SM_MAINT_02";

	public static final String GDSN_VENDOR_SUBSCRIPTION = "GD_VEND_01";

	public static final String ECOMMERCE_SOURCE_PRIORITY_CODE = "EC_SRCP_01";
	public static final String CASE_UPC_GENERATOR = "UT_CUPC_01";
	public static final String CHECK_DIGIT_CALCULATOR ="UT_CKDG_01";
	public static final String UTILITIES_DICTIONARY = "UT_DICT_01";
	public static final String CHECK_STATUS = "BU_STAT_01";

	public static final String SHELF_ATTRIBUTES = "PD_SHLF_00";
	public static final String SHELF_ATTRIBUTES_CUSTOMER_FRIENDLY_DESCRIPTION = "PD_SHLF_01";
	public static final String SHELF_ATTRIBUTES_SUBMIT_PRIMO_PICK = "PD_SHLF_02";
	public static final String SHELF_ATTRIBUTES_APPROVE_PRIMO_PICK = "PD_SHLF_03";
	public static final String SHELF_ATTRIBUTES_DISTINCTIVE = "PD_SHLF_04";
	public static final String SHELF_ATTRIBUTES_TAG_TYPE = "PD_SHLF_05";
	public static final String SHELF_ATTRIBUTES_SUBMIT_SERVICE_CASE_SIGN = "PD_SHLF_06";
	public static final String SHELF_ATTRIBUTES_APPROVE_CASE_SIGN = "PD_SHLF_07";
	public static final String SHELF_ATTRIBUTES_GO_LOCAL = "PD_SHLF_08";
	public static final String SHELF_ATTRIBUTES_SERVICE_CASE_CALLOUT = "PD_SHLF_10";

	public static final String SPECIAL_ATTRIBUTES ="PD_SPCL_01";
	public static final String SPECIAL_ATTRIBUTES_PHARMACY ="PD_SPCL_03";
	public static final String SPECIAL_ATTRIBUTES_CODE_DATE ="PD_SPCL_02";
	public static final String SPECIAL_ATTRIBUTES_RESTRICTIONS="PD_SPCL_04";
	public static final String SPECIAL_ATTRIBUTES_TOBACCO="PD_SPCL_05";
	public static final String ONLINE_ATTRIBUTES = "PD_OLAT_01";
	public static final String ONLINE_ATTRIBUTES_RELATED_PRODUCTS = "PD_OLAT_02";
	public static final String ONLINE_ATTRIBUTES_TIER_PRICING = "PD_OLAT_03";
	public static final String ONLINE_ATTRIBUTES_TAGS_AND_SPECS = "PD_OLAT_04";
	public static final String PRODUCT_ECOMMERCE_VIEW = "PD_ECOM_01";
	public static final String PRODUCT_ECOMMERCE_VIEW_PUBLISH = "PD_ECOM_02";
	public static final String PRODUCT_NUTRITION_FACTS = "PD_NUTR_00";
	public static final String PRODUCT_VARIANTS ="PD_VRNT_01";
	public static final String PRODUCT_BREAK_PACK="PD_BKPK_01";

	public static final String CASE_PACK_INFO = "CP_INFO_01";
	public static final String WAREHOUSE_INFO="CP_WHSE_01";
	public static final String CASE_PACK_VENDOR = "CP_VEND_01";
	public static final String CASE_PACK_ITEM_SUB = "CP_SUBS_01";
	public static final String CASE_PACK_VENDOR_ITEM_FACTORY_INFO = "CP_IMPRT_02";
	public static final String CASE_PACK_PSS_DEPARTMENT = "CP_PSSD_01";
	public static final String DRU_INFO="CP_DRU_01";
	public static final String CASE_PACK_IMPORT = "CP_IMPRT_01";
	public static final String MRT_INFO = "CP_MRT_01";

	public static final String UPC_INFO = "UP_INFO_01";
	public static final String UPC_INFO_PRODUCT_SIZE = "UP_INFO_02";
	public static final String UPC_INFO_QUANTITY = "UP_INFO_03";
	public static final String UPC_INFO_UOM = "UP_INFO_04";
	public static final String UPC_INFO_DSD_DELETE = "UP_INFO_05";
	public static final String UPC_INFO_DSD_OVERRIDE = "UP_INFO_06";
	public static final String UPC_INFO_BONUS = "UP_INFO_07";
	public static final String UPC_INFO_RETAIL_WEIGHT = "UP_INFO_08";
	public static final String IMAGE_INFO="CP_IMG_01";

	public static final String CUSTOM_HIERARCHY_VIEW = "CH_HIER_01";
	public static final String CUSTOM_HIERARCHY_EDIT = "CH_HIER_02";
	public static final String BRAND_COST_OWNER_TOP_2_TOP = "CT_BRND_01";
	public static final String COUNTRY_CODE = "CT_CNTR_01";

	public static final String MASS_UPDATE = "MU_MENU_00";
	public static final String MASS_UPDATE_THIRD_PARTY_SELLABLE = "MU_TRDP_01";
	public static final String MASS_UPDATE_SELECT_INGREDIENTS = "MU_SING_01";
	public static final String MASS_UPDATE_TOTALLY_TEXAS = "MU_TOTX_01";
	public static final String MASS_UPDATE_GO_LOCAL = "MU_GOLC_01";
	public static final String MASS_UPDATE_FOOD_STAMP = "MU_FDST_01";
	public static final String MASS_UPDATE_FSA = "MU_FSA_01";
	public static final String MASS_UPDATE_TAX_FLAG = "MU_TXFL_01";
	public static final String MASS_UPDATE_SELF_MANUFACTURED = "MU_SFMF_01";
	public static final String MASS_UPDATE_TAX_CATEOGRY = "MU_TXCT_01";
	public static final String MASS_UPDATE_UNASSIGN_PRODUCTS = "MU_UNASN_01";
	public static final String MASS_UPDATE_PRIMO_PICK_APPROVER = "MU_PRPK_02";
	public static final String MASS_UPDATE_PRIMO_PICK_SUBMITTER = "MU_PRPK_01";
	public static final String MASS_UPDATE_TAG_TYPE = "MU_TAGT_01";
	public static final String MASS_UPDATE_PRODUCE_ORGANIC = "MU_PRORG_01";

	public static final String BATCH_UPLOAD_BY_NUTRITION_AND_INGREDIENTS = "BU_NUDT_01";
	public static final String BATCH_UPLOAD_BY_SERVICE_CASE_SIGN = "BU_SCST_01";
	public static final String BATCH_UPLOAD_BY_PRIMO_PICK = "BU_PRPK_01";
	public static final String BATCH_UPLOAD_BY_CATEGORY_SPECIFIC_ATTRIBUTES = "BU_CSAT_01";
	public static final String BATCH_UPLOAD_BY_ASSORTMENT = "BU_ASRT_01";
	public static final String BATCH_UPLOAD_EARLEY_HIERARCHY = "BU_EARL_05";
    public static final String BATCH_UPLOAD_EARLEY_PRODUCTS = "BU_EARL_01";
	public static final String BATCH_UPLOAD_EARLEY_ATTRIBUTES = "BU_EARL_02";
	public static final String BATCH_UPLOAD_EARLEY_ATTRIBUTE_VALUES = "BU_EARL_03";
	public static final String BATCH_UPLOAD_EARLEY_PRODUCT_ATTRIBUTE_VALUES = "BU_EARL_04";
	public static final String BATCH_UPLOAD_BY_RELATED_PRODUCTS = "BU_RELP_01";
	public static final String BATCH_UPLOAD_BY_MAT_ATTRIBUTE_VALUES = "BU_MAT_01";
	public static final String BATCH_UPLOAD_BY_EBM_BDA = "BU_EBM_BDA";

	public static final String CODE_TABLE_WINE_AREA = "CT_WINE_03";
	public static final String CODE_TABLE_WINE_MAKER = "CT_WINE_04";
	public static final String CODE_TABLE_WINE_REGION = "CT_WINE_05";
	public static final String CODE_TABLE_WINE_VARIETAL_TYPE = "CT_WINE_06";
	public static final String CODE_TABLE_WINE_VARIETAL = "CT_WINE_02";
	public static final String CODE_TABLE_WINE_SCORING_ORGANIZATION = "CT_WINE_01";
	public static final String CODE_TABLE_FACTORY ="CT_FCTR_01";
    public static final String CODE_TABLE_CHOICE_TYPE = "CT_CHOI_02";

	public static final String CODE_TABLE_CHOICE_OPTION = "CT_CHOI_01";

	public static final String CODE_TABLE_PRODUCT_BRAND = "CT_BRND_02";

	public static final String CODE_TABLE_PRODUCT_BRAND_5 = "CT_BRND_05";

    public static final String CODE_TABLE_STATE_WARNING = "CT_SWRN_01";
	public static final String CODE_TABLE_WIC_LEB = "CT_WICL_01";
	public static final String CODE_TABLE_COUNTRY_CODE = "CT_CNTR_01";
    public static final String CODE_TABLE_PRODUCT_SUB_BRAND = "CT_BRND_03";
    public static final String CODE_TABLE_PRODUCT_LINE_BRAND = "CT_BRND_04";
	public static final String BRND_CST_OWNR_T2T = "CT_BRND_01";
	public static final String CODE_TABLE_PRODUCT_CATEGORY = "CT_PRDC_01";
	public static final String CODE_TABLE_PRODUCT_GROUP_TYPE = "CT_PDGT_01";
	public static final String TOBACCO_PRODUCT_TYPE_CODE = "CT_TPTC_01";
	public static final String CODE_TABLE_SOURCE_SYSTEM = "CT_SRCSYS_01";
	public static final String CODE_TABLE_METADATA = "CT_MTDA_01";
	public static final String CODE_TABLE_PRODUCT_LINE = "CT_PRLN_01";
	public static final String CODE_TABLE_COST_OWNER = "CT_COWN_02";
	public static final String CODE_TABLE_TOP_2_TOP = "CT_TTOT_02";
        public static final String CODE_TABLE_TIER_CODE = "CT_TIERCODE_01";

	public static final String TASK_PRODUCT_UPDATES = "TK_PUDT_01";
	public static final String TASK_NUTRITION_UPDATES = "TK_NUDT_01";
	public static final String TASK_ECOMMERCE = "TK_ECOM_01";

	public static final String PRODUCT_GROUP_SEARCH = "PG_MENU_00";

	public static final String PRODUCT_TAXONOMY_LEVEL = "PD_TXNMY_01";
	public static final String PRODUCT_TAXONOMY_ATTRIBUTES = "PD_TXNMY_02";
	public static final String REACTIVE_ITEM = "CP_INFO_04";
	public static final String AUTHORIZATION = "AS_JOEV_00";
	public static final String AUTHORIZE_ITEM = "AT_JVSS_01";
	public static final String SHOW_ON_SITE = "MU_SOS_01";
}
