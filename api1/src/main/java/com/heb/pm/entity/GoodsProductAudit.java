/*
 *  GoodsProductAudit
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Represents goods product audit information.
 *
 */
@Entity
@Table(name = "goods_prod_aud")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPk", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class GoodsProductAudit implements Audit, Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * Enum Strings for PSEType
	 */
	private static final String NO_PSE_CODE="N";
	private static final String NO_PSE_DISPLAY="No PSE";
	private static final String DEFAULT_PSE_CODE="";
	private static final String DEFAULT_PSE_DISPLAY="Default";
	private static final String PSE_SOLID_CODE="S";
	private static final String PSE_SOLID_DISPLAY="PSE Solid";
	private static final String PSE_NON_SOLID_CODE="L";
	private static final String PSE_NON_SOLID_DISPLAY="PSE Non-Solid";
	/**
	 * Enum Strings for MedicaidType
	 */
	private static final String ACQUISITION_CODE="D";
	private static final String ACQUISITION_STRING="Acquisition Cost";
	private static final String NOT_MEDICAID_CODE="N";
	private static final String NOT_MEDICAID_STRING="Not Medicaid Eligible";
	private static final String AVERAGE_WHOLESALE_CODE="W";
	private static final String AVERAGE_WHOLESALE_STRING="Average Wholesale Price";

	@EmbeddedId
	private GoodsProductAuditKey key;

	@Column(name = "act_cd")
	private String action;

	@AuditableField(displayName = "Tag Effective Date",filter = FilterConstants.SHELF_ATTRIBUTES_AUDIT)
	@Column(name = "tag_eff_dt")
	private LocalDate tagEffectiveDate;

	@AuditableField(displayName = "Tag Size",filter = FilterConstants.SHELF_ATTRIBUTES_AUDIT)
	@Column(name = "shlf_tag_sz_cd")
	private String shelfTagSizeCode;

	@AuditableField(displayName = "Number of Tags",filter = FilterConstants.SHELF_ATTRIBUTES_AUDIT)
	@Column(name = "nbr_of_tags")
	private Integer numberOfTags;

	@Column(name = "max_nest_cnt")
	private Integer maxNestCount;

	@Column(name = "stk_sw")
	private Boolean stackableSwitch;

	@Column(name = "nest_ln")
	private Double retailIncrLength;

	@Column(name = "nest_ht")
	private Double retailIncrHeight;

	@Column(name = "nest_wd")
	private Double retailIncrWidth;

    @AuditableField(displayName = "Retail Length", filter = FilterConstants.UPC_INFO_DIMENSIONS_AUDIT)
	@Column(name = "retl_unt_ln")
	private Double retailLength;

    @AuditableField(displayName = "Retail Width", filter = FilterConstants.UPC_INFO_DIMENSIONS_AUDIT)
	@Column(name = "retl_unt_wd")
	private Double retailWidth;

    @AuditableField(displayName = "Retail Height", filter = FilterConstants.UPC_INFO_DIMENSIONS_AUDIT)
	@Column(name = "retl_unt_ht")
	private Double retailHeight;

    @AuditableField(displayName = "Retail Weight", filter = FilterConstants.UPC_INFO_DIMENSIONS_AUDIT)
	@Column(name = "retl_unt_wt")
	private Double retailWeight;

	@Column(name = "lst_updt_ts")
	private Date lastUpdatedOn;

	@Column(name = "lst_updt_uid")
	private String lastUpdateBy;

	@Column(name = "vertex_tax_cat_cd")
	@Type(type="fixedLengthCharPk")
	private String vertexTaxCategoryCode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "retl_sell_sz_cd_1", referencedColumnName = "retl_sell_sz_cd", insertable = false, updatable = false)
	private RetailUnitOfMeasure retailUnitOfMeasure;

	@AuditableField(displayName = "Color", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "color_cd")
	@Type(type="fixedLengthChar")
	private String colorCode;

	@Column(name = "priv_lbl_sw")
	private String privateLabelSwitch;

	@AuditableField(displayName = "Model", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "prod_modl_txt")
	private String productModelText;

	@Column(name = "retl_sell_sz_cd_1")
	private String retailSellSizeCode;

	@AuditableField(displayName = "Packaging", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "pkg_txt")
	private String packagingText;

	@AuditableField(displayName = "Family Code 1", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "fam_1_cd")
	private Long familyCode1;

	@AuditableField(displayName = "Family Code 2", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "fam_2_cd")
	private Long familyCode2;

	@AuditableField(displayName = "Seasonality Year", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "sesnly_yy")
	private Long seasonalityYear;

	@AuditableField(displayName = "Pre Price", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "premrk_prc_amt")
	private Double prePrice;

	@AuditableField(displayName = "Wine", filter = {FilterConstants.ONLINE_ATTRIBUTES_AUDIT, FilterConstants.PRODUCT_INFO_AUDIT})
	@Column(name = "wine_prod_sw")
	private Boolean wineProductSwitch;

	@AuditableField(displayName = "Food Stamp", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "fd_stmp_sw")
	private Boolean foodStampSwitch;

	@AuditableField(displayName = "Retail Tax", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "sals_tax_sw")
	private Boolean retailTaxSwitch;

	@AuditableField(displayName = "Generic Product", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "gnrc_prod_sw")
	private Boolean genericProductSwitch;

	@AuditableField(displayName = "In-Store Production", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name = "in_str_prodn_sw")
	private Boolean inStoreProductionSwitch;

	@AuditableField(displayName = "Drug Fact Panel", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "drg_fact_pan_sw")
	private Boolean drugFactPanelSwitch;

	@AuditableField(displayName = "Self Manufactured", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "self_mfg_sw")
	private Boolean selfManufactureSwitch;

	@Column(name = "crit_prod_sw")
	private String criticalProductSwitch;

	@AuditableField(displayName = "Scale Label", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "scale_sw")
	private Boolean scaleLabelSwitch;

	@AuditableField(displayName = "FSA", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "fsa_cd")
	private String fsaCode;

	@OneToOne
	@JoinColumn(name = "sesnly_id", referencedColumnName = "sesnly_id", insertable = false, updatable = false)
	private Seasonality seasonality;

	@Column(name = "cre8_ts")
	private Date createdOn;

	@Column(name = "cre8_uid")
	private String createdBy;

	@AuditableField(displayName = "PSE",filter = FilterConstants.PHARMACY_AUDIT)
	@Column(name = "pse_type_cd")
	private String pseTypeCode;

	@AuditableField(displayName = "Medicaid",filter = FilterConstants.PHARMACY_AUDIT)
	@Column(name = "MEDICAID_CD")
	private String medicaidCode;

	@AuditableField(displayName = "Pharmacy", filter = {FilterConstants.PRODUCT_INFO_AUDIT, FilterConstants.PHARMACY_AUDIT})
	@Column(name = "RX_PROD_SW")
	private Boolean rxProductFlag;

//	@Column(name = "GRAMS_OF_PSE")
//	private Long gramsOfPSE;

	@AuditableField(displayName = "Code Dated", filter = {FilterConstants.PRODUCT_INFO_AUDIT, FilterConstants.CODE_DATE_AUDIT})
	@Column(name = "CD_DATED_ITM_CD")
	private Boolean codeDate;

	@AuditableField(displayName = "Max Shelf Days", filter = FilterConstants.CODE_DATE_AUDIT)
	@Column(name = "MAX_SHLF_LIFE_DD")
	private Long maxShelfLifeDays;

	@AuditableField(displayName = "Inbound Specification Days", filter = FilterConstants.CODE_DATE_AUDIT)
	@Column(name = "INBND_SPCFN_DD")
	private Long inboundSpecificationDays;

	@AuditableField(displayName = "Warehouse Reaction Days", filter = FilterConstants.CODE_DATE_AUDIT)
	@Column(name = "REACT_DD")
	private Long warehouseReactionDays;

	@AuditableField(displayName = "Guarantee To Store Days", filter = FilterConstants.CODE_DATE_AUDIT)
	@Column(name = "GUARN_TO_STR_DD")
	private Long guaranteeToStoreDays;

	@AuditableField(displayName = "Send Code Date Data to EXE", filter = FilterConstants.CODE_DATE_AUDIT)
	@Column(name = "SND_DD_to_wms_sw")
	private Boolean sendCodeDate;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false)
	private RxProduct rxProduct;

	@Transient
	private VertexTaxCategory vertexTaxCategory;

	@AuditableField(displayName = "Max. Customer Order Quantity", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name = "max_cust_ord_qty")
	private Long maxCustomerOrderQuantity;

	@AuditableField(displayName = "Min. Customer Order Quantity", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name = "min_cust_ord_qty")
	private Long minCustomerOrderQuantity;

	@AuditableField(displayName = "Order Increments", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name = "cust_ord_incrm_qty")
	private Long customerOrderIncrementQuantity;

	@AuditableField(displayName = "Flex Weight", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name = "wt_sw")
	private Boolean flexWeightSwitch;

	@AuditableField(displayName = "Min. Weight", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name = "min_unt_sz_wt")
	private Double minWeight;

	@AuditableField(displayName = "Max. Weight", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name = "max_unt_sz_wt")
	private Double maxWeight;

	@AuditableField(displayName = "Tobacco", filter = {FilterConstants.PRODUCT_INFO_AUDIT, FilterConstants.TOBACCO_AUDIT})
	@Column(name = "TBCO_PROD_SW")
	private Boolean tobaccoProductSwitch;

	@Column(name = "MRK_PRC_IN_STR_SW")
	private Boolean mrkPrcInStrSwitch;

//	@Column(name = "WIC_SW")
//	private Boolean wicSwitch;

//	@Column(name="T2T_ID")
//	private Long t2tId;

	@AuditableField(displayName = "Sold By", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name = "consm_prch_chc_cd")
	private String soldBy;

	@AuditableField(displayName= "Add on Sellable", filter = {FilterConstants.RELATED_PRODUCTS_AUDIT})
	@Column(name = "SOLD_SEPLY_SW")
	private Boolean sellableProduct;

	@AuditableField(displayName= "Fragile", filter = {FilterConstants.SHIPPING_HANDLING_AUDIT})
	@Column(name = "fragile_sw")
	private Boolean fragile;

//	@AuditableField(displayName= "Hazardous Type", filter = {FilterConstants.SHIPPING_HANDLING_AUDIT})
	@Column(name = "hazard_cd")
	private String hazardType;

	@AuditableField(displayName= "ORMD", filter = {FilterConstants.SHIPPING_HANDLING_AUDIT})
	@Column(name = "ormd_sw")
	private Boolean ormd;

	@AuditableField(displayName= "Ship By Itself", filter = {FilterConstants.SHIPPING_HANDLING_AUDIT})
	@Column(name = "shp_alone_sw")
	private Boolean shipByItself;

	@AuditableField(displayName = "Guarantee Image", codeTableDisplayNameMethod = "getHebGuaranteeTypeDisplayName", filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HEB_GUARN_TYP_CD", referencedColumnName = "HEB_GUARN_TYP_CD", insertable = false, updatable = false, nullable = false)
	private HebGuaranteeTypeCode hebGuaranteeTypeCode;

        //bi-directional one-to-one association to TibcoProduct
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false)
	private TobaccoProduct tobaccoProduct;

	/**
	 * Enum for the PSEType
	 */
	public enum PSEType{

		NoPSE (NO_PSE_CODE, NO_PSE_DISPLAY),
		SolidPSE (PSE_SOLID_CODE, PSE_SOLID_DISPLAY),
		NonSolidPSE (PSE_NON_SOLID_CODE, PSE_NON_SOLID_DISPLAY),
		DefaultPSE (DEFAULT_PSE_CODE, DEFAULT_PSE_DISPLAY);

		private String code;
		private String displayName;


		PSEType(String code, String displayName){
			this.code = code;
			this.displayName=displayName;
		}

		public String getCode(){
			return this.code;
		}
		public String getDisplayName(){
			return this.displayName;
		}
	}

	/**
	 * Enum for the PSEType
	 */
	public enum MedicaidType{

		Acquisition (ACQUISITION_CODE, ACQUISITION_STRING),
		NotMedicaid (NOT_MEDICAID_CODE, NOT_MEDICAID_STRING),
		Average (AVERAGE_WHOLESALE_CODE, AVERAGE_WHOLESALE_STRING);

		private String code;
		private String displayName;


		MedicaidType(String code, String displayName){
			this.code = code;
			this.displayName=displayName;
		}

		public String getCode(){
			return this.code;
		}
		public String getDisplayName(){
			return this.displayName;
		}
	}

	/**
	 * Gets hazard type.
	 *
	 * @return the hazard type
	 */
	public String getHazardType() {
		return hazardType;
	}

	/**
	 * Sets hazard type.
	 *
	 * @param hazardType the hazard type
	 */
	public void setHazardType(String hazardType) {
		this.hazardType = hazardType;
	}

	/**
	 * Gets ormd.
	 *
	 * @return the ormd
	 */
	public Boolean getOrmd() {
		return ormd;
	}

	/**
	 * Sets ormd.
	 *
	 * @param ormd the ormd
	 */
	public void setOrmd(Boolean ormd) {
		this.ormd = ormd;
	}

	/**
	 * Gets ship by itself.
	 *
	 * @return the ship by itself
	 */
	public Boolean getShipByItself() {
		return shipByItself;
	}

	/**
	 * Sets ship by itself.
	 *
	 * @param shipByItself the ship by itself
	 */
	public void setShipByItself(Boolean shipByItself) {
		this.shipByItself = shipByItself;
	}

	/**
	 * Gets fragile.
	 *
	 * @return the fragile
	 */
	public Boolean getFragile() {
		return fragile;
	}

	/**
	 * Sets fragile.
	 *
	 * @param fragile the fragile
	 */
	public void setFragile(Boolean fragile) {
		this.fragile = fragile;
	}

	/**
	 * Gets sold by.
	 *
	 * @return the sold by
	 */
	public String getSoldBy() {
		return soldBy;
	}

	/**
	 * Sets sold by.
	 *
	 * @param soldBy the sold by
	 */
	public void setSoldBy(String soldBy) {
		this.soldBy = soldBy;
	}

	/**
	 * Gets flex weight switch.  Variable amount of product can be received.
	 *
	 * @return the flex weight switch
	 */
	public Boolean getFlexWeightSwitch() {
		return flexWeightSwitch;
	}

	/**
	 * Sets flex weight switch. Variable amount of product can be received.
	 *
	 * @param flexWeightSwitch the flex weight switch
	 */
	public void setFlexWeightSwitch(Boolean flexWeightSwitch) {
		this.flexWeightSwitch = flexWeightSwitch;
	}

	/**
	 * Gets min weight.  Minimum weight the product can be.
	 *
	 * @return the min weight
	 */
	public Double getMinWeight() {
		return minWeight;
	}

	/**
	 * Sets min weight. Minimum weight the product can be.
	 *
	 * @param minWeight the min weight
	 */
	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	/**
	 * Gets max weight.  The Maximum weight that a product can be.
	 *
	 * @return the max weight
	 */
	public Double getMaxWeight() {
		return maxWeight;
	}

	/**
	 * Sets max weight.  The Maximum weight that a product can be.
	 *
	 * @param maxWeight the max weight
	 */
	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	/**
	 * Gets max customer order quantity.  The maximum customer order quantity that can be ordered.
	 *
	 * @return the max customer order quantity
	 */
	public Long getMaxCustomerOrderQuantity() {
		return maxCustomerOrderQuantity;
	}

	/**
	 * Sets max customer order quantity.  The maximum customer order quantity that can be ordered.
	 *
	 * @param maxCustomerOrderQuantity the max customer order quantity
	 */
	public void setMaxCustomerOrderQuantity(Long maxCustomerOrderQuantity) {
		this.maxCustomerOrderQuantity = maxCustomerOrderQuantity;
	}

	/**
	 * Gets min customer order quantity.  The minimum customer order quantity that can be ordered.
	 *
	 * @return the min customer order quantity
	 */
	public Long getMinCustomerOrderQuantity() {
		return minCustomerOrderQuantity;
	}

	/**
	 * Sets min customer order quantity.  The minimum customer order quantity that can be ordered.
	 *
	 * @param minCustomerOrderQuantity the min customer order quantity
	 */
	public void setMinCustomerOrderQuantity(Long minCustomerOrderQuantity) {
		this.minCustomerOrderQuantity = minCustomerOrderQuantity;
	}

	/**
	 * Gets customer order increment quantity.
	 *
	 * @return the customer order increment quantity
	 */
	public Long getCustomerOrderIncrementQuantity() {
		return customerOrderIncrementQuantity;
	}

	/**
	 * Sets customer order increment quantity.
	 *
	 * @param customerOrderIncrementQuantity the customer order increment quantity
	 */
	public void setCustomerOrderIncrementQuantity(Long customerOrderIncrementQuantity) {
		this.customerOrderIncrementQuantity = customerOrderIncrementQuantity;
	}

	/**
	 * Is scale label switch boolean.
	 *
	 * @return the boolean
	 */
	public Boolean getScaleLabelSwitch() {
		return scaleLabelSwitch;
	}

	/**
	 * Sets scale label switch.
	 *
	 * @param scaleLabelSwitch the scale label switch
	 */
	public void setScaleLabelSwitch(Boolean scaleLabelSwitch) {
		this.scaleLabelSwitch = scaleLabelSwitch;
	}

	/**
	 * Gets critical product switch value of which specifies the critical level of product(i.e. Normal, Sensitive, Profit, and business).
	 *
	 * @return the critical product switch which specifies the critical level of product(i.e. Normal, Sensitive, Profit, and business).
	 */
	public String getCriticalProductSwitch() {
		return criticalProductSwitch;
	}

	/**
	 * Sets critical product switch which specifies the critical level of product(i.e. Normal, Sensitive, Profit, and business)
	 *
	 * @param criticalProductSwitch the critical product switch which specifies the critical level of product(i.e. Normal, Sensitive, Profit, and business)
	 */
	public void setCriticalProductSwitch(String criticalProductSwitch) {
		this.criticalProductSwitch = criticalProductSwitch;
	}

	/**
	 * Is self manufacture switch that dictates weather the product is manufactured by HEB.
	 *
	 * @return self manufacture switch that dictates weather the product is manufactured by HEB.
	 */
	public Boolean getSelfManufactureSwitch() {
		return selfManufactureSwitch;
	}

	/**
	 * Sets self manufacture switch that dictates weather the product is manufactured by HEB.
	 *
	 * @param selfManufactureSwitch the self manufacture switch dictates weather the product is manufactured by HEB.
	 */
	public void setSelfManufactureSwitch(Boolean selfManufactureSwitch) {
		this.selfManufactureSwitch = selfManufactureSwitch;
	}

	/**
	 * Gets seasonality. Product assigned as a seasonal product, not sold during other times of the year except during the season it is flagged for.
	 *
	 * @return Product assigned as a seasonal product, not sold during other times of the year except during the season it is flagged for.
	 */
	public Seasonality getSeasonality() {
		return seasonality;
	}

	/**
	 * Sets seasonality.  Product assigned as a seasonal product, not sold during other times of the year except during the season it is flagged for.
	 *
	 * @param seasonality Product assigned as a seasonal product, not sold during other times of the year except during the season it is flagged for.
	 */
	public void setSeasonality(Seasonality seasonality) {
		this.seasonality = seasonality;
	}

	/**
	 * Gets fsa code. Flag to indicate the product is FSA Eligible.
	 *
	 * @return the Flag to indicate the product is FSA Eligible.
	 */
	public String getFsaCode() {
		return fsaCode;
	}

	/**
	 * Sets fsa code. Flag to indicate the product is FSA Eligible.
	 *
	 * @param fsaCode the Flag to indicate the product is FSA Eligible.
	 */
	public void setFsaCode(String fsaCode) {
		this.fsaCode = fsaCode;
	}

	/**
	 * Is drug fact panel switch boolean. Flag to indicate that there is a Drug Fact Panel available.
	 *
	 * @return the Flag to indicate that there is a Drug Fact Panel available.
	 */
	public Boolean getDrugFactPanelSwitch() {
		return drugFactPanelSwitch;
	}

	/**
	 * Sets drug fact panel switch. Flag to indicate that there is a Drug Fact Panel available.
	 *
	 * @param drugFactPanelSwitch the drug fact panel Flag to indicate that there is a Drug Fact Panel available.
	 */
	public void setDrugFactPanelSwitch(Boolean drugFactPanelSwitch) {
		this.drugFactPanelSwitch = drugFactPanelSwitch;
	}

	/**
	 * Is in store production switch boolean. A flag to indicate that this product is produced inside the stores; examples are Rotissere Chickens, Tortillas, etc..
	 *
	 * @return the flag to indicate that this product is produced inside the stores; examples are Rotissere Chickens, Tortillas, etc.
	 */
	public Boolean getInStoreProductionSwitch() {
		return inStoreProductionSwitch;
	}

	/**
	 * Sets in store production switch. A flag to indicate that this product is produced inside the stores; examples are Rotissere Chickens, Tortillas, etc.
	 *
	 * @param inStoreProductionSwitch the in store production flag to indicate that this product is produced inside the stores; examples are Rotissere Chickens, Tortillas, etc.
	 */
	public void setInStoreProductionSwitch(Boolean inStoreProductionSwitch) {
		this.inStoreProductionSwitch = inStoreProductionSwitch;
	}

	/**
	 * Is generic product switch boolean. Indicates whether the product is a generic item or not.
	 *
	 * @return the boolean that Indicates whether the product is a generic item or not.
	 */
	public Boolean getGenericProductSwitch() {
		return genericProductSwitch;
	}

	/**
	 * Sets generic product switch. Indicates whether the product is a generic item or not.
	 *
	 * @param genericProductSwitch the generic product switch that Indicates whether the product is a generic item or not.
	 */
	public void setGenericProductSwitch(Boolean genericProductSwitch) {
		this.genericProductSwitch = genericProductSwitch;
	}

	/**
	 * Is retail tax switch boolean. Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package.
	 *
	 * @return the boolean flag that a  Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package.
	 */
	public Boolean getRetailTaxSwitch() {
		return retailTaxSwitch;
	}

	/**
	 * Sets retail tax switch. Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package.
	 *
	 * @param retailTaxSwitch the retail tax switch Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package.
	 */
	public void setRetailTaxSwitch(Boolean retailTaxSwitch) {
		this.retailTaxSwitch = retailTaxSwitch;
	}

	/**
	 * Is food stamp switch boolean. Indicator that the product is eligible to be purchased using food stamps.
	 *
	 * @return the boolean Indicator that the product is eligible to be purchased using food stamps.
	 */
	public Boolean getFoodStampSwitch() {
		return foodStampSwitch;
	}

	/**
	 * Sets food stamp switch. Indicator that the product is eligible to be purchased using food stamps
	 *
	 * @param foodStampSwitch the food stamp switch Indicator that the product is eligible to be purchased using food stamps.
	 */
	public void setFoodStampSwitch(Boolean foodStampSwitch) {
		this.foodStampSwitch = foodStampSwitch;
	}

	/**
	 * Is wine product switch boolean. Flag that indicates product is considered to be under the Wine based products.
	 *
	 * @return the boolean Flag that indicates product is considered to be under the Wine based products.
	 */
	public Boolean getWineProductSwitch() {
		return wineProductSwitch;
	}

	/**
	 * Sets wine product switch. Flag that indicates product is considered to be under the Wine based products.
	 *
	 * @param wineProductSwitch the wine product Flag that indicates product is considered to be under the Wine based products.
	 */
	public void setWineProductSwitch(Boolean wineProductSwitch) {
		this.wineProductSwitch = wineProductSwitch;
	}

	/**
	 * Gets pre price. Indicator that the product comes to our warehouse/stores with a price already printed on the packaging.
	 *
	 * @return the pre price Indicator that the product comes to our warehouse/stores with a price already printed on the packaging.
	 */
	public Double getPrePrice() {
		return prePrice;
	}

	/**
	 * Sets pre price. Indicator that the product comes to our warehouse/stores with a price already printed on the packaging.
	 *
	 * @param prePrice the pre price Indicator that the product comes to our warehouse/stores with a price already printed on the packaging.
	 */
	public void setPrePrice(Double prePrice) {
		this.prePrice = prePrice;
	}

	/**
	 * Gets packaging text. Text field that provides description related to the products packaging.
	 *
	 * @return the packaging Text field that provides description related to the products packaging.
	 */
	public String getPackagingText() {
		return packagingText;
	}

	/**
	 * Sets packaging text.  Text field that provides description related to the products packaging.
	 *
	 * @param packagingText the packaging Text field that provides description related to the products packaging.
	 */
	public void setPackagingText(String packagingText) {
		this.packagingText = packagingText;
	}

	/**
	 * Gets family code 1. # embedded into coupon bar code so when we set up items vendors are asked for this and if they promote it using coupons - we need it tied.
	 *
	 * @return the family code 1 # embedded into coupon bar code so when we set up items vendors are asked for this and if they promote it using coupons - we need it tied.
	 */
	public Long getFamilyCode1() {
		return familyCode1;
	}

	/**
	 * Sets family code 1. # embedded into coupon bar code so when we set up items vendors are asked for this and if they promote it using coupons - we need it tied.
	 *
	 * @param familyCode1 the family code 1 # embedded into coupon bar code so when we set up items vendors are asked for this and if they promote it using coupons - we need it tied.
	 */
	public void setFamilyCode1(Long familyCode1) {
		this.familyCode1 = familyCode1;
	}

	/**
	 * Gets family code 2. # embedded into coupon bar code so when we set up items vendors are asked for this and if they promote it using coupons - we need it tied.
	 *
	 * @return the family code 2 # embedded into coupon bar code so when we set up items vendors are asked for this and if they promote it using coupons - we need it tied.
	 */
	public Long getFamilyCode2() {
		return familyCode2;
	}

	/**
	 * Sets family code 2. # embedded into coupon bar code so when we set up items vendors are asked for this and if they promote it using coupons - we need it tied.
	 *
	 * @param familyCode2 the family code 2 # embedded into coupon bar code so when we set up items vendors are asked for this and if they promote it using coupons - we need it tied.
	 */
	public void setFamilyCode2(Long familyCode2) {
		this.familyCode2 = familyCode2;
	}

	/**
	 * Gets seasonality year. Season a product would be flagged as being available to sell during.
	 *
	 * @return Season a product would be flagged as being available to sell during.
	 */
	public Long getSeasonalityYear() {
		return seasonalityYear;
	}

	/**
	 * Sets seasonality year. Season a product would be flagged as being available to sell during.
	 *
	 * @param seasonalityYear Season a product would be flagged as being available to sell during.
	 */
	public void setSeasonalityYear(Long seasonalityYear) {
		this.seasonalityYear = seasonalityYear;
	}

	/**
	 * Gets retail sell size code. The size of the high level product and description
	 *
	 * @return the retail sell size of the high level product and description
	 */
	public String getRetailSellSizeCode() {
		return retailSellSizeCode;
	}

	/**
	 * Sets retail sell size code. The size of the high level product and description.
	 *
	 * @param retailSellSizeCode the retail sell size of the high level product and description.
	 */
	public void setRetailSellSizeCode(String retailSellSizeCode) {
		this.retailSellSizeCode = retailSellSizeCode;
	}

	/**
	 * Gets product model text. Model number to the item if assigned.
	 *
	 * @return the product Model number to the item if assigned.
	 */
	public String getProductModelText() {
		return productModelText;
	}

	/**
	 * Sets product model text. Model number to the item if assigned.
	 *
	 * @param productModelText the product Model number to the item if assigned.
	 */
	public void setProductModelText(String productModelText) {
		this.productModelText = productModelText;
	}

	/**
	 * Gets color code. Color of the item
	 *
	 * @return the color code of the Color of the item.
	 */
	public String getColorCode() {
		return colorCode;
	}

	/**
	 * Sets color code. Color of the item.
	 *
	 * @param colorCode the color code of the Color of the item.
	 */
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	/**
	 * Gets private label switch. Indicator that the product is a part of HEB's private label program.
	 *
	 * @return the private label Indicator that the product is a part of HEB's private label program.
	 */
	public String getPrivateLabelSwitch() {
		return privateLabelSwitch;
	}

	/**
	 * Sets private label switch. Indicator that the product is a part of HEB's private label program.
	 *
	 * @param privateLabelSwitch the private label Indicator that the product is a part of HEB's private label program.
	 */
	public void setPrivateLabelSwitch(String privateLabelSwitch) {
		this.privateLabelSwitch = privateLabelSwitch;
	}

	/**
	 * Gets vertex tax category code. Assigned by Vertex (for global sales tax data) based on the buyers location.
	 *
	 * @return the vertex tax category code Assigned by Vertex (for global sales tax data) based on the buyers location.
	 */
	public String getVertexTaxCategoryCode() {
		return vertexTaxCategoryCode;
	}

	/**
	 * Sets vertex tax category code. Assigned by Vertex (for global sales tax data) based on the buyers location.
	 *
	 * @param vertexTaxCategoryCode the vertex tax category code Assigned by Vertex (for global sales tax data) based on the buyers location.
	 */
	public void setVertexTaxCategoryCode(String vertexTaxCategoryCode) {
		this.vertexTaxCategoryCode = vertexTaxCategoryCode;
	}

	/**
	 * Gets retail unit of measure. Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package X FOR (retails for).
	 *
	 * @return the retail unit available for retail, but not considered inventory.  1 Bottle, 1 Package X FOR (retails for)
	 */
	public RetailUnitOfMeasure getRetailUnitOfMeasure() {
		return retailUnitOfMeasure;
	}

	/**
	 * Sets retail unit of measure. Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package X FOR (retails for)
	 *
	 * @param retailUnitOfMeasure the retail Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package X FOR (retails for)
	 */
	public void setRetailUnitOfMeasure(RetailUnitOfMeasure retailUnitOfMeasure) {
		this.retailUnitOfMeasure = retailUnitOfMeasure;
	}

	/**
	 * Gets the Last updated on date for retail dimensions
	 *
	 * @return  lastUpdatedOn date for retail dimensions
	 */
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * Sets the Last updated on date for retail dimensions
	 *
	 * @param lastUpdatedOn date for retail dimensions
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * Gets the Last updated user ID for retail dimensions
	 *
	 * @return last updated by id
	 */
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	/**
	 * Sets the Last updated user ID for retail dimensions
	 *
	 * @param lastUpdateBy last updated by id
	 */
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	/**
	 * Gets the retail length of upc info dimensions
	 *
	 * @return retail length for dimensions
	 */
	public Double getRetailLength() {
		return retailLength;
	}

	/**
	 * Sets the retail length of upc info dimensions
	 *
	 * @param retailLength retail length for dimensions
	 */
	public void setRetailLength(Double retailLength) {
		this.retailLength = retailLength;
	}

	/**
	 * Gets the retail width of upc info dimensions
	 *
	 * @return retail width for dimensions
	 */
	public Double getRetailWidth() {
		return retailWidth;
	}

	/**
	 * Sets the retail width of upc info dimensions
	 *
	 * @param retailWidth width of dimensions
	 */
	public void setRetailWidth(Double retailWidth) {
		this.retailWidth = retailWidth;
	}

	/**
	 * Gets the retail height of upc info dimensions
	 *
	 * @return height of dimensions
	 */
	public Double getRetailHeight() {
		return retailHeight;
	}

	/**
	 * Sets the retail height of upc info dimensions
	 *
	 * @param retailHeight height of dimensions
	 */
	public void setRetailHeight(Double retailHeight) {
		this.retailHeight = retailHeight;
	}

	/**
	 * Gets the retail weight of upc info dimensions
	 *
	 * @return retail weight of dimensions
	 */
	public Double getRetailWeight() {
		return retailWeight;
	}

	/**
	 * Sets the retail weight of upc info dimensions
	 *
	 * @param retailWeight weight of dimensions
	 */
	public void setRetailWeight(Double retailWeight) {
		this.retailWeight = retailWeight;
	}

	/**
	 * Gets the maximum number of bins you can nest inside this product.
	 *
	 * @return the maximum number of bins you can nest inside this product.
	 */
	public Integer getMaxNestCount() {
		return maxNestCount;
	}

	/**
	 * Sets the maximum number of bins you can nest inside this product.
	 *
	 * @param maxNestCount the maximum number of bins you can nest inside this product.
	 */
	public void setMaxNestCount(Integer maxNestCount) {
		this.maxNestCount = maxNestCount;
	}

	/**
	 * Whether or not this product can be stacked on top of another of the same product.
	 *
	 * @return True if the product is stackable and false otherwise.
	 */
	public Boolean getStackableSwitch() {
		return stackableSwitch;
	}

	/**
	 * Whether or not this product can be stacked on top of another of the same product.
	 *
	 * @param stackableSwitch True if the product is stackable and false otherwise.
	 */
	public void setStackableSwitch(Boolean stackableSwitch) {
		this.stackableSwitch = stackableSwitch;
	}

	/**
	 * Gets retail incremental length that is based on the nesting and stacking.
	 *
	 * @return retail incremental length that is based on the nesting and stacking.
	 */
	public Double getRetailIncrLength() {
		return retailIncrLength;
	}

	/**
	 * Sets retail incremental length that is based on the nesting and stacking.
	 *
	 * @param retailIncrLength the retail incremental length that is based on the nesting and stacking.
	 */
	public void setRetailIncrLength(Double retailIncrLength) {
		this.retailIncrLength = retailIncrLength;
	}

	/**
	 * Gets retail incremental height that is based on the nesting and stacking.
	 *
	 * @return the retail incremental height that is based on the nesting and stacking.
	 */
	public Double getRetailIncrHeight() {
		return retailIncrHeight;
	}

	/**
	 * Sets retail incremental height that is based on the nesting and stacking.
	 *
	 * @param retailIncrHeight the retail incremental height that is based on the nesting and stacking.
	 */
	public void setRetailIncrHeight(Double retailIncrHeight) {
		this.retailIncrHeight = retailIncrHeight;
	}

	/**
	 * Gets retail incremental width that is based on the nesting and stacking.
	 *
	 * @return the retail incr width based on stack/nesting
	 */
	public Double getRetailIncrWidth() {
		return retailIncrWidth;
	}

	/**
	 * Sets retail incr width.
	 *
	 * @param retailIncrWidth the retail incr width based on stack/nesting
	 */
	public void setRetailIncrWidth(Double retailIncrWidth) {
		this.retailIncrWidth = retailIncrWidth;
	}

	/**
	 * Returns tag date that is to be used for changes to take affect.
	 *
	 * @return tag date that is to be used for changes to take affect.
	 */
	public LocalDate getTagEffectiveDate() {
		return tagEffectiveDate;
	}

	/**
	 * Set the tag date that is to be used for changes to take affect.
	 *
	 * @param tagEffectiveDate the tag date that is to be used for changes to take affect.
	 */
	public void setTagEffectiveDate(LocalDate tagEffectiveDate) {
		this.tagEffectiveDate = tagEffectiveDate;
	}

	/**
	 * Returns the shelf tag size code assigned to the product.
	 *
	 * @return the shelf tag size code assigned to the product.
	 */
	public String getShelfTagSizeCode() {
		return shelfTagSizeCode;
	}

	/**
	 * Sets the code that ties it to a tag size.
	 *
	 * @param shelfTagSizeCode the code that ties it to a tag size.
	 */
	public void setShelfTagSizeCode(String shelfTagSizeCode) {
		this.shelfTagSizeCode = shelfTagSizeCode;
	}

	/**
	 * Returns the number of tags that are associated wit the product.
	 *
	 * @return the number of tags that are associated wit the product.
	 */
	public Integer getNumberOfTags() {
		return numberOfTags;
	}

	/**
	 * Sets the number of tags that are associated wit the product.
	 *
	 * @param numberOfTags the number of tags that are associated wit the product.
	 */
	public void setNumberOfTags(Integer numberOfTags) {
		this.numberOfTags = numberOfTags;
	}

	/**
	 * Flag that this is product with Pseudofedrine (will have controls in place by restrictions if flagged)
	 * @return pseTypeCode
	 */
	public String getPseTypeCode() {
		return pseTypeCode;
	}

	/**
	 * Updates pseTypeCode
	 * @param pseTypeCode the new pseTypeCode
	 */
	public void setPseTypeCode(String pseTypeCode) {
		this.pseTypeCode = pseTypeCode;
	}

	/**
	 * Switch that indicates the item is eligible by Medicaid, values can be D or N.
	 * D = Acquisiiton Cost
	 * N = Not Medicaid Eligible
	 * W = Average Wholesale Price
	 * @return medicateCode
	 */
	public String getMedicaidCode() {
		return medicaidCode;
	}

	/**
	 * Updates the medicaidCode
	 * @param medicaidCode the new medicaidCode
	 */
	public void setMedicaidCode(String medicaidCode) {
		this.medicaidCode = medicaidCode;
	}

	/**
	 * Flag that item is a Pharmacy based item.
	 * @return rxProduct
	 */
	public Boolean getRxProductFlag() {
		return rxProductFlag;
	}

	/**
	 * Updates the rxProduct
	 * @param rxProductFlag the new rxProduct
	 */
	public void setRxProductFlag(Boolean rxProductFlag) {
		this.rxProductFlag = rxProductFlag;
	}

//	/**
//	 * Gram weight of PSE in the item if flagged with PSE.
//	 * @return gramsOfPSE
//	 */
//	public Long getGramsOfPSE() {
//		return gramsOfPSE;
//	}

//	/**
//	 * Updates gramsOfPSE
//	 * @param gramsOfPSE the new gramsOfPSE
//	 */
//	public void setGramsOfPSE(Long gramsOfPSE) {
//		this.gramsOfPSE = gramsOfPSE;
//	}

	/**
	 * Returns the date the record was created
	 * @return createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * Updates the createdOn
	 * @param createdOn the new createdOn
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Returns who created the record
	 * @return createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Updates createdBy
	 * @param createdBy the new createdBy
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Returns Rx information for the product
	 * @return the rxProduct
	 */
	public RxProduct getRxProduct() {
		return rxProduct;
	}

	/**
	 * Updates the rxProduct
	 * @param rxProduct the new rxProduct
	 */
	public void setRxProduct(RxProduct rxProduct) {
		this.rxProduct = rxProduct;
	}

	/**
	 * Gets the String representation of the PSE Type.
	 * @return
	 */
	public String getPSEDisplay(){
		if(this.pseTypeCode == null) {
			return null;
		}
		switch (this.pseTypeCode.trim()){
			case(NO_PSE_CODE):
				return NO_PSE_DISPLAY;
			case(PSE_NON_SOLID_CODE):
				return PSE_NON_SOLID_DISPLAY;
			case(PSE_SOLID_CODE):
				return PSE_SOLID_DISPLAY;
			default:
				return DEFAULT_PSE_DISPLAY;
		}
	}

	/**
	 * Gets the String representation of the Medicaid Type.
	 * @return
	 */
	public String getMedicaidDisplay(){
		if(this.medicaidCode == null) {
			return null;
		}
		switch (this.medicaidCode.trim()){
			case(ACQUISITION_CODE):
				return ACQUISITION_STRING;
			case(NOT_MEDICAID_CODE):
				return NOT_MEDICAID_STRING;
			default:
				return AVERAGE_WHOLESALE_STRING;
		}
	}

	/**
	 *Flag indicates that the rest of the days need to be completed or not, ie. Oat meal is code dated where batteries
	 * are not (ediable or not)
	 * @return codeDate
	 */
	public Boolean getCodeDate() {
		return codeDate;
	}

	/**
	 * Update codeDate
	 * @param codeDate new codeDate
	 */
	public void setCodeDate(Boolean codeDate) {
		this.codeDate = codeDate;
	}

	/**
	 * Maximum number of days a product can stay on the shelf
	 * @return maxShelfLifeDays
	 */
	public Long getMaxShelfLifeDays() {
		return maxShelfLifeDays;
	}

	/**
	 * Updates maxShelfLifeDays
	 * @param maxShelfLifeDays the new maxShelfLifeDays
	 */
	public void setMaxShelfLifeDays(Long maxShelfLifeDays) {
		this.maxShelfLifeDays = maxShelfLifeDays;
	}

	/**
	 * # of days it will take for a product to get to store.
	 * @return inboundSpecificationDays
	 */
	public Long getInboundSpecificationDays() {
		return inboundSpecificationDays;
	}

	/**
	 * Update inboundSpecificationDays
	 * @param inboundSpecificationDays the new inboundSpecificationDays
	 */
	public void setInboundSpecificationDays(Long inboundSpecificationDays) {
		this.inboundSpecificationDays = inboundSpecificationDays;
	}

	/**
	 * when we become short - the warehouse has xx days to bill it out to store
	 * @return warehouseReactionDays
	 */
	public Long getWarehouseReactionDays() {
		return warehouseReactionDays;
	}

	/**
	 * Updates warehouseReactionDays
	 * @param warehouseReactionDays the new warehouseReactionDays
	 */
	public void setWarehouseReactionDays(Long warehouseReactionDays) {
		this.warehouseReactionDays = warehouseReactionDays;
	}

	/**
	 * days store is gauranteed to have this item
	 * @return guaranteeToStoreDays
	 */
	public Long getGuaranteeToStoreDays() {
		return guaranteeToStoreDays;
	}

	/**
	 * Update guaranteeToStoreDays
	 * @param guaranteeToStoreDays the new guaranteeToStoreDays
	 */
	public void setGuaranteeToStoreDays(Long guaranteeToStoreDays) {
		this.guaranteeToStoreDays = guaranteeToStoreDays;
	}

	/**
	 * Flag to alert that this data needs to go to wrehouse data when timing hits.
	 * @return sendCodeDate
	 */
	public Boolean getSendCodeDate() {
		return sendCodeDate;
	}

	/**
	 * Updates sendCodeDate
	 * @param sendCodeDate the new sendCodeDate
	 */
	public void setSendCodeDate(Boolean sendCodeDate) {
		this.sendCodeDate = sendCodeDate;
	}

	/**
	 * Returns the Vertex tax category for this product.
	 *
	 * @return The Vertex tax category for this product.
	 */
	public VertexTaxCategory getVertexTaxCategory() {
		return vertexTaxCategory;
	}

	/**
	 * Sets the Vertex tax category for this product.
	 *
	 * @param vertexTaxCategory The Vertex tax category for this product.
	 */
	public void setVertexTaxCategory(VertexTaxCategory vertexTaxCategory) {
		this.vertexTaxCategory = vertexTaxCategory;
	}

	/**
	 * This flag states if the goods product is a tobacco product
	 * @return
	 */
	public Boolean getTobaccoProductSwitch() {
		return tobaccoProductSwitch;
	}

	/**
	 * Updates if this product is a tobacco product
	 * @param tobaccoProductSwitch
	 */
	public void setTobaccoProductSwitch(Boolean tobaccoProductSwitch) {
		this.tobaccoProductSwitch = tobaccoProductSwitch;
	}

	public Boolean getMrkPrcInStrSwitch() {
		return mrkPrcInStrSwitch;
	}

	public void setMrkPrcInStrSwitch(Boolean mrkPrcInStrSwitch) {
		this.mrkPrcInStrSwitch = mrkPrcInStrSwitch;
	}

//	public Boolean getWicSwitch() {
//		return wicSwitch;
//	}
//
//	public void setWicSwitch(Boolean wicSwitch) {
//		this.wicSwitch = wicSwitch;
//	}

	/**
	 * Returns the sellableProduct.
	 *
	 * @return the sellableProduct.
	 */
	public Boolean isSellableProduct() {
		return sellableProduct;
	}

	/**
	 * Sets the sellableProduct.
	 *
	 * @param sellableProduct the sellableProduct to set.
	 */
	public void setSellableProduct(Boolean sellableProduct) {
		this.sellableProduct = sellableProduct;
	}

//	public Long getT2tId() {
//		return t2tId;
//	}
//
//	public void setT2tId(Long t2tId) {
//		this.t2tId = t2tId;
//	}

	public TobaccoProduct getTobaccoProduct() {
		return tobaccoProduct;
	}

	public void setTobaccoProduct(TobaccoProduct tobaccoProduct) {
		this.tobaccoProduct = tobaccoProduct;
	}

	/**
	 * Returns the hebGuaranteeTypeCode.
	 * @return the hebGuaranteeTypeCode.
	 */
	public HebGuaranteeTypeCode getHebGuaranteeTypeCode() {
		return hebGuaranteeTypeCode;
	}

	/**
	 * Sets the hebGuaranteeTypeCode.
	 *
	 * @param hebGuaranteeTypeCode the hebGuaranteeTypeCode.
	 */
	public void setHebGuaranteeTypeCode(HebGuaranteeTypeCode hebGuaranteeTypeCode) {
		this.hebGuaranteeTypeCode = hebGuaranteeTypeCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GoodsProductAudit that = (GoodsProductAudit) o;
		return Objects.equals(key, that.key) &&
				Objects.equals(action, that.action) &&
				Objects.equals(tagEffectiveDate, that.tagEffectiveDate) &&
				Objects.equals(shelfTagSizeCode, that.shelfTagSizeCode) &&
				Objects.equals(numberOfTags, that.numberOfTags) &&
				Objects.equals(maxNestCount, that.maxNestCount) &&
				Objects.equals(stackableSwitch, that.stackableSwitch) &&
				Objects.equals(retailIncrLength, that.retailIncrLength) &&
				Objects.equals(retailIncrHeight, that.retailIncrHeight) &&
				Objects.equals(retailIncrWidth, that.retailIncrWidth) &&
				Objects.equals(retailLength, that.retailLength) &&
				Objects.equals(retailWidth, that.retailWidth) &&
				Objects.equals(retailHeight, that.retailHeight) &&
				Objects.equals(retailWeight, that.retailWeight) &&
				Objects.equals(lastUpdatedOn, that.lastUpdatedOn) &&
				Objects.equals(lastUpdateBy, that.lastUpdateBy) &&
				Objects.equals(vertexTaxCategoryCode, that.vertexTaxCategoryCode) &&
				Objects.equals(retailUnitOfMeasure, that.retailUnitOfMeasure) &&
				Objects.equals(colorCode, that.colorCode) &&
				Objects.equals(privateLabelSwitch, that.privateLabelSwitch) &&
				Objects.equals(productModelText, that.productModelText) &&
				Objects.equals(retailSellSizeCode, that.retailSellSizeCode) &&
				Objects.equals(packagingText, that.packagingText) &&
				Objects.equals(familyCode1, that.familyCode1) &&
				Objects.equals(familyCode2, that.familyCode2) &&
				Objects.equals(seasonalityYear, that.seasonalityYear) &&
				Objects.equals(prePrice, that.prePrice) &&
				Objects.equals(wineProductSwitch, that.wineProductSwitch) &&
				Objects.equals(foodStampSwitch, that.foodStampSwitch) &&
				Objects.equals(retailTaxSwitch, that.retailTaxSwitch) &&
				Objects.equals(genericProductSwitch, that.genericProductSwitch) &&
				Objects.equals(inStoreProductionSwitch, that.inStoreProductionSwitch) &&
				Objects.equals(drugFactPanelSwitch, that.drugFactPanelSwitch) &&
				Objects.equals(selfManufactureSwitch, that.selfManufactureSwitch) &&
				Objects.equals(criticalProductSwitch, that.criticalProductSwitch) &&
				Objects.equals(scaleLabelSwitch, that.scaleLabelSwitch) &&
				Objects.equals(fsaCode, that.fsaCode) &&
				Objects.equals(seasonality, that.seasonality) &&
				Objects.equals(createdOn, that.createdOn) &&
				Objects.equals(createdBy, that.createdBy) &&
				Objects.equals(pseTypeCode, that.pseTypeCode) &&
				Objects.equals(medicaidCode, that.medicaidCode) &&
				Objects.equals(rxProductFlag, that.rxProductFlag) &&
				Objects.equals(codeDate, that.codeDate) &&
				Objects.equals(maxShelfLifeDays, that.maxShelfLifeDays) &&
				Objects.equals(inboundSpecificationDays, that.inboundSpecificationDays) &&
				Objects.equals(warehouseReactionDays, that.warehouseReactionDays) &&
				Objects.equals(guaranteeToStoreDays, that.guaranteeToStoreDays) &&
				Objects.equals(sendCodeDate, that.sendCodeDate) &&
				Objects.equals(rxProduct, that.rxProduct) &&
				Objects.equals(vertexTaxCategory, that.vertexTaxCategory) &&
				Objects.equals(maxCustomerOrderQuantity, that.maxCustomerOrderQuantity) &&
				Objects.equals(minCustomerOrderQuantity, that.minCustomerOrderQuantity) &&
				Objects.equals(customerOrderIncrementQuantity, that.customerOrderIncrementQuantity) &&
				Objects.equals(flexWeightSwitch, that.flexWeightSwitch) &&
				Objects.equals(minWeight, that.minWeight) &&
				Objects.equals(maxWeight, that.maxWeight) &&
				Objects.equals(tobaccoProductSwitch, that.tobaccoProductSwitch) &&
				Objects.equals(mrkPrcInStrSwitch, that.mrkPrcInStrSwitch) &&
				Objects.equals(soldBy, that.soldBy) &&
				Objects.equals(sellableProduct, that.sellableProduct) &&
				Objects.equals(fragile, that.fragile) &&
				Objects.equals(hazardType, that.hazardType) &&
				Objects.equals(ormd, that.ormd) &&
				Objects.equals(shipByItself, that.shipByItself) &&
				Objects.equals(hebGuaranteeTypeCode, that.hebGuaranteeTypeCode) &&
				Objects.equals(tobaccoProduct, that.tobaccoProduct);
	}

	@Override
	public int hashCode() {

		return Objects.hash(key, action, tagEffectiveDate, shelfTagSizeCode, numberOfTags, maxNestCount, stackableSwitch, retailIncrLength, retailIncrHeight, retailIncrWidth, retailLength, retailWidth, retailHeight, retailWeight, lastUpdatedOn, lastUpdateBy, vertexTaxCategoryCode, retailUnitOfMeasure, colorCode, privateLabelSwitch, productModelText, retailSellSizeCode, packagingText, familyCode1, familyCode2, seasonalityYear, prePrice, wineProductSwitch, foodStampSwitch, retailTaxSwitch, genericProductSwitch, inStoreProductionSwitch, drugFactPanelSwitch, selfManufactureSwitch, criticalProductSwitch, scaleLabelSwitch, fsaCode, seasonality, createdOn, createdBy, pseTypeCode, medicaidCode, rxProductFlag, codeDate, maxShelfLifeDays, inboundSpecificationDays, warehouseReactionDays, guaranteeToStoreDays, sendCodeDate, rxProduct, vertexTaxCategory, maxCustomerOrderQuantity, minCustomerOrderQuantity, customerOrderIncrementQuantity, flexWeightSwitch, minWeight, maxWeight, tobaccoProductSwitch, mrkPrcInStrSwitch, soldBy, sellableProduct, fragile, hazardType, ormd, shipByItself, hebGuaranteeTypeCode, tobaccoProduct);
	}

	/**
	 * Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
	 *
	 * @return ActionCode
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the ActionCode
	 *
	 * @param action The ActionCode
	 */
	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String getChangedBy() {
		return lastUpdateBy;
	}

	@Override
	public void setChangedBy(String changedBy) {
		this.lastUpdateBy = changedBy;
	}

	@Override
	public LocalDateTime getChangedOn() {
		return this.key.getChangedOn();
	}

	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}

	public GoodsProductAuditKey getKey() {
		return key;
	}

	public void setKey(GoodsProductAuditKey key) {
		this.key = key;
	}

	public Boolean getSellableProduct() {
		return sellableProduct;
	}

	/**
	 * Get heb guarantee type display name.
	 *
	 * @return A string is the description of heb guarantee type display name.
	 */
	public String getHebGuaranteeTypeDisplayName(){
		HebGuaranteeTypeCode hebGuaranteeTypeCode = this.getHebGuaranteeTypeCode();
		if(hebGuaranteeTypeCode != null){
			return hebGuaranteeTypeCode.getHebGuaranteeTypeDescription();
		}
		return null;
	}
	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "GoodsProduct{" +
				", tagEffectiveDate=" + tagEffectiveDate +
				", shelfTagSizeCode='" + shelfTagSizeCode + '\'' +
				", numberOfTags=" + numberOfTags +
				", maxNestCount=" + maxNestCount +
				", stackableSwitch=" + stackableSwitch +
				", retailIncrLength=" + retailIncrLength +
				", retailIncrHeight=" + retailIncrHeight +
				", retailIncrWidth=" + retailIncrWidth +
				", retailLength=" + retailLength +
				", retailWidth=" + retailWidth +
				", retailHeight=" + retailHeight +
				", retailWeight=" + retailWeight +
				", lastUpdatedOn=" + lastUpdatedOn +
				", lastUpdateBy='" + lastUpdateBy + '\'' +
				", vertexTaxCategoryCode='" + vertexTaxCategoryCode + '\'' +
				", colorCode='" + colorCode + '\'' +
				", privateLabelSwitch='" + privateLabelSwitch + '\'' +
				", productModelText='" + productModelText + '\'' +
				", retailSellSizeCode='" + retailSellSizeCode + '\'' +
				", packagingText='" + packagingText + '\'' +
				", familyCode1=" + familyCode1 +
				", familyCode2=" + familyCode2 +
				", seasonalityYear=" + seasonalityYear +
				", prePrice=" + prePrice +
				", wineProductSwitch=" + wineProductSwitch +
				", foodStampSwitch=" + foodStampSwitch +
				", retailTaxSwitch=" + retailTaxSwitch +
				", genericProductSwitch=" + genericProductSwitch +
				", inStoreProductionSwitch=" + inStoreProductionSwitch +
				", drugFactPanelSwitch=" + drugFactPanelSwitch +
				", selfManufactureSwitch=" + selfManufactureSwitch +
				", criticalProductSwitch='" + criticalProductSwitch + '\'' +
				", scaleLabelSwitch=" + scaleLabelSwitch +
				", fsaCode='" + fsaCode + '\'' +
				", createdOn=" + createdOn +
				", createdBy='" + createdBy + '\'' +
				", pseTypeCode='" + pseTypeCode + '\'' +
				", medicaidCode='" + medicaidCode + '\'' +
				", rxProductFlag=" + rxProductFlag +
//				", gramsOfPSE=" + gramsOfPSE +
				", codeDate=" + codeDate +
				", maxShelfLifeDays=" + maxShelfLifeDays +
				", inboundSpecificationDays=" + inboundSpecificationDays +
				", warehouseReactionDays=" + warehouseReactionDays +
				", guaranteeToStoreDays=" + guaranteeToStoreDays +
				", sendCodeDate=" + sendCodeDate +
				", maxCustomerOrderQuantity=" + maxCustomerOrderQuantity +
				", minCustomerOrderQuantity=" + minCustomerOrderQuantity +
				", customerOrderIncrementQuantity=" + customerOrderIncrementQuantity +
				", flexWeightSwitch=" + flexWeightSwitch +
				", minWeight=" + minWeight +
				", maxWeight=" + maxWeight +
				", tobaccoProductSwitch=" + tobaccoProductSwitch +
				", mrkPrcInStrSwitch=" + mrkPrcInStrSwitch +
//				", wicSwitch=" + wicSwitch +
//				", t2tId=" + t2tId +
				", tobaccoProduct=" + tobaccoProduct +
				'}';
	}
}
