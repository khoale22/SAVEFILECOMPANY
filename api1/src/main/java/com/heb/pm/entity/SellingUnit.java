/*
 * SellingUnit
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Represents a retail selling unit.
 *
 * @author d116773
 * @since 2.0.1
 */
@Entity
@Table(name="prod_scn_codes")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class SellingUnit implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;

	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@Id
	@Column(name="scn_cd_id")
	private long upc;

	@Column(name="prim_scn_cd_sw")
	private boolean primaryUpc;

	@Column(name="tag_sz_des")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")   
	private String tagSize;

	@Column(name="bns_scn_cd_sw")
	private Boolean bonusSwitch;

	@Column(name="PROD_ID")
	private long prodId;

	@Column(name="PROC_SCN_MAINT_SW")
	private boolean processedByScanMaintenance;

	@Column(name = "dscon_dt")
	private LocalDate discontinueDate;

	@Column(name = "lst_scn_dt")
	private LocalDate lastScanDate;

	@Column(name = "frst_scn_dt")
	private LocalDate firstScanDate;

	@Column(name = "retl_unt_sell_sz_1")
	private Double quantity;

	@Column(name = "retl_unt_sell_sz_2")
	private Double quantity2;

	@Column(name = "retl_unt_wt")
	private Double retailWeight;

	@Column(name = "tst_scn_prfmd_sw")
	private boolean testScanned;

	@Column(name = "dsd_deld_sw")
	private Boolean dsdDeleteSwitch;

	@Column(name = "dsd_dept_ovrd_sw")
	private Boolean dsdDeptOverideSwitch;

	@Column(name = "retl_unt_ln")
	private Double retailLength;

	@Column(name = "retl_unt_wd")
	private Double retailWidth;

	@Column(name = "retl_unt_ht")
	private Double retailHeight;

	@Column(name = "lst_updt_ts")
	private Date lastUpdatedOn;

	@Column(name = "lst_updt_uid")
	private String lastUpdatedBy;

	@Column(name = "pse_grams_wt")
	private Double pseGramWeight;

	@Column(name = "TAG_SZ_DES")
	private String tagSizeDescription;

	@Column(name = "wic_apl_id")
	private Long wicApprovedProductListId;

	@Column(name = "retl_sell_sz_cd_1")
	private String retailUnitOfMeasureCode;

	@Column(name="SCN_TYP_CD")
	private String scanTypeCode;

	@Column(name="cre8_uid")
	@Type(type="fixedLengthChar")
	private String createId;

	@Column(name="cre8_ts")
	private LocalDateTime createTime;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prod_sub_brnd_id", referencedColumnName = "prod_sub_brnd_id", insertable = false, updatable = false)
	private ProductSubBrand subBrand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prod_brnd_id", referencedColumnName = "prod_brnd_id", insertable = false, updatable = false)
    private ProductBrand productBrand;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "retl_sell_sz_cd_1", referencedColumnName = "retl_sell_sz_cd", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private RetailUnitOfMeasure retailUnitOfMeasure;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "retl_sell_sz_cd_2", referencedColumnName = "retl_sell_sz_cd", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private RetailUnitOfMeasure retailUnitOfMeasure2;

	// TODO: See if this can be removed
	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private GoodsProduct goodsProduct;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private ProductShippingHandling productShippingHandling;

	@JsonIgnoreProperties({"sellingUnits","prodItems"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", insertable = false, updatable = false)
	private ProductMaster productMaster;

	@OneToMany
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false)
	private List<NutritionalClaims> nutritionalClaims;

	@OneToMany
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false)
	private List<ProductPkVariation> productPkVariations;

	@OneToMany
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false)
	private List<ProductNutrient> productNutrients;

	// This is required to support dynamic search capability and is notused outside that. Therefore, there are no
	// getters or setters on this.
	@OneToMany(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false)
	private List<SearchCriteria> searchCriteria;

	/**
	 * This is required to support search capability and is not used outside that. Therefore, there are no
	 * getters or setters on this.
	 */
	@OneToMany(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false)
	private List<ProductScanImageURIAudit> productScanImageURIAudits;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cst_own_id", referencedColumnName = "cst_own_id", insertable = false, updatable = false)
	private CostOwner costOwner;

	/**
	 * Gets retail unit of measure code.
	 *
	 * @return the retail unit of measure code
	 */
	public String getRetailUnitOfMeasureCode() {
		return retailUnitOfMeasureCode;
	}

	/**
	 * Sets retail unit of measure code.
	 *
	 * @param retailUnitOfMeasureCode the retail unit of measure code
	 */
	public void setRetailUnitOfMeasureCode(String retailUnitOfMeasureCode) {
		this.retailUnitOfMeasureCode = retailUnitOfMeasureCode;
	}

	/**
	 * Gets last updated on.
	 *
	 * @return the last updated on date that the CPS measuring data was modified.
	 */
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * Sets last updated on.
	 *
	 * @param lastUpdatedOn the last updated on date that the CPS measuring data was modified.
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * Gets last updated by user ID of the user that modified the CPS measuring data last.
	 *
	 * @return the last updated by user ID of the user that modified the CPS measuring data last.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * Sets last updated by user ID of the user that modified the CPS measuring data last.
	 *
	 * @param lastUpdatedBy the last updated by user ID of the user that modified the CPS measuring data last.
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}


	/**
	 * Gets retail height for the created product in cps
	 *
	 * @return the retail height for the created product in cps
	 */
	public Double getRetailHeight() {
		return retailHeight;
	}

	/**
	 * Sets retail height for the created product in cps
	 *
	 * @param retailHeight the retail height for the created product in cps
	 */
	public void setRetailHeight(Double retailHeight) {
		this.retailHeight = retailHeight;
	}

	/**
	 * Gets retail width for the created product in cps
	 *
	 * @return the retail width for the created product in cps
	 */
	public Double getRetailWidth() {
		return retailWidth;
	}

	/**
	 * Sets retail width for the created product in cps
	 *
	 * @param retailWidth the retail width for the created product in cps
	 */
	public void setRetailWidth(Double retailWidth) {
		this.retailWidth = retailWidth;
	}

	/**
	 * Gets retail length for the created product in cps
	 *
	 * @return the retail length for the created product in cps.
	 */
	public Double getRetailLength() {
		return retailLength;
	}

	/**
	 * Sets retail length for the created product in cps
	 *
	 * @param retailLength the retail length for the created product in cps
	 */
	public void setRetailLength(Double retailLength) {
		this.retailLength = retailLength;
	}

	/**
	 * Gets product shipping handling, is join between sellingUnit and ProductShippingHandling entity.
	 *
	 * @return the product shipping handling, is join between sellingUnit and ProductShippingHandling entity.
	 */
	public ProductShippingHandling getProductShippingHandling() {
		return productShippingHandling;
	}

	/**
	 * Sets product shipping handling, is join between sellingUnit and ProductShippingHandling entity.
	 *
	 * @param productShippingHandling the product shipping handling, is join between sellingUnit and ProductShippingHandling entity.
	 */
	public void setProductShippingHandling(ProductShippingHandling productShippingHandling) {
		this.productShippingHandling = productShippingHandling;
	}

	/**
	 * Gets goods product, is join between sellingUnit and GoodsProd entity.
	 *
	 * @return the goods product, is join between sellingUnit and GoodsProd entity.
	 */
	public GoodsProduct getGoodsProduct() {
		return goodsProduct;
	}

	/**
	 * Sets goods product, is join between sellingUnit and GoodsProd entity.
	 *
	 * @param goodsProduct the goods product, is join between sellingUnit and GoodsProd entity.
	 */
	public void setGoodsProduct(GoodsProduct goodsProduct) {
		this.goodsProduct = goodsProduct;
	}

	/**
	 * Is test scanned boolean.  Switch that keeps track if upc was scanned at the setup time to ensure the upc being setup correctly.
	 *
	 * @return the boolean switch that keeps track if upc was scanned at the setup time to ensure the upc being setup correctly.
	 */
	public boolean isTestScanned() {
		return testScanned;
	}

	/**
	 * Sets test scanned. Switch that keeps track if upc was scanned at the setup time to ensure the upc being setup correctly.
	 *
	 * @param testScanned the test scanned switch that keeps track if upc was scanned at the setup time to ensure the upc being setup correctly.
	 */
	public void setTestScanned(boolean testScanned) {
		this.testScanned = testScanned;
	}

	/**
	 * Is bonus switch boolean.  Indicates a bonus product.
	 *
	 * @return the boolean that indicates if there is a bonus product.
	 */
	public Boolean isBonusSwitch() {
		return bonusSwitch;
	}

	/**
	 * Gets retail weight of the given upc.
	 *
	 * @return the retail weight  of the upc.
	 */
	public Double getRetailWeight() {
		return retailWeight;
	}

	/**
	 * Sets retail weight of the given upc.
	 *
	 * @param retailWeight the retail weight of the upc.
	 */
	public void setRetailWeight(Double retailWeight) {
		this.retailWeight = retailWeight;
	}

	/**
	 * Is dsd delete switch boolean, that determines if it has been set to delete in dsd.
	 *
	 * @return the boolean, that determines if it has been set to delete in dsd.
	 */
	public Boolean isDsdDeleteSwitch() {
		return dsdDeleteSwitch;
	}

	/**
	 * Sets dsd delete switch.  Which says whether or not this selling unit has been set to be deleted in dsd.
	 *
	 * @param dsdDeleteSwitch the dsd delete switch, that determines if it has been set to delete in dsd.
	 */
	public void setDsdDeleteSwitch(Boolean dsdDeleteSwitch) {
		this.dsdDeleteSwitch = dsdDeleteSwitch;
	}

	/**
	 * Is dsd dept override switch boolean. Which says whether or not this selling unit has been set to be overwritten in dsd.
	 *
	 * @return the boolean that keeps track of the dept override
	 */
	public Boolean isDsdDeptOverideSwitch() {
		return dsdDeptOverideSwitch;
	}

	/**
	 * Sets dsd dept overide switch. Which says whether or not this selling unit has been set to be overwritten in dsd.
	 *
	 * @param dsdDeptOverideSwitch the dsd dept overide switch which says whether or not this selling unit has been set to be overwritten in dsd.
	 */
	public void setDsdDeptOverideSwitch(Boolean dsdDeptOverideSwitch) {
		this.dsdDeptOverideSwitch = dsdDeptOverideSwitch;
	}

	/**
	 * Gets sub brand that is associated with the current upc.
	 *
	 * @return the sub brand of the upc
	 */
	public ProductSubBrand getSubBrand() {
		return subBrand;
	}

	/**
	 * Sets sub brand that is associated with the current upc.
	 *
	 * @param subBrand the sub brand of the upc
	 */
	public void setSubBrand(ProductSubBrand subBrand) {
		this.subBrand = subBrand;
	}

    /**
     * Gets productBrand that is associated with the current upc.
     *
     * @return the brand of the upc
     */
    public ProductBrand getProductBrand() { return productBrand; }

    /**
     * Sets productBrand that is associated with the current upc.
     *
     * @@param productBrand the productBrand of the upc
     */
    public void setProductBrand(ProductBrand productBrand) { this.productBrand = productBrand; }

	/**
	 * Gets retail unit of measure which is used to quantify the inventory items and enables to track them.
	 *
	 * @return the retail unit of measure which is used to quantify the inventory items and enables to track them.
	 */
	public RetailUnitOfMeasure getRetailUnitOfMeasure() {
		return retailUnitOfMeasure;
	}

	/**
	 * Sets retail unit of measure which is used to quantify the inventory items and enables to track them.
	 *
	 * @param retailUnitOfMeasure the retail unit of measure which is used to quantify the inventory items and enables to track them.
	 */
	public void setRetailUnitOfMeasure(RetailUnitOfMeasure retailUnitOfMeasure) {
		this.retailUnitOfMeasure = retailUnitOfMeasure;
	}

	/**
	 * Gets retail unit of measure 2.
	 *
	 * @return the retail unit of measure 2
	 */
	public RetailUnitOfMeasure getRetailUnitOfMeasure2() {
		return retailUnitOfMeasure2;
	}

	/**
	 * Sets retail unit of measure 2.
	 *
	 * @param retailUnitOfMeasure2 the retail unit of measure 2
	 */
	public void setRetailUnitOfMeasure2(RetailUnitOfMeasure retailUnitOfMeasure2) {
		this.retailUnitOfMeasure2 = retailUnitOfMeasure2;
	}

	/**
	 * Gets quantity the selling size of the product
	 *
	 * @return the quantity is the selling size of the product
	 */
	public Double getQuantity() {
		return quantity;
	}

	/**
	 * Gets quantity 2.
	 *
	 * @return the quantity 2
	 */
	public Double getQuantity2() {
		return quantity2;
	}

	/**
	 * Sets quantity 2.
	 *
	 * @param quantity2 the quantity 2
	 */
	public void setQuantity2(Double quantity2) {
		this.quantity2 = quantity2;
	}

	/**
	 * Sets quantity the selling size of the product
	 *
	 * @param quantity the quantity selling size of the product
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets first scan date which the upc was scanned.
	 *
	 * @return the date the upc was first scanned.
	 */
	public LocalDate getFirstScanDate() {
		return firstScanDate;
	}

	/**
	 * Sets first scan date which the upc was scanned.
	 *
	 * @param firstScanDate the date the upc was first scanned.
	 */
	public void setFirstScanDate(LocalDate firstScanDate) {
		this.firstScanDate = firstScanDate;
	}

	/**
	 * Returns the selling unit's UPC. UPC is used generically here. This could be a UPC, PLU, EAN, etc.
	 *
	 * @return The selling unit's UPC. UPC is used generically here. This could be a UPC, PLU, EAN, etc.
	 */
	public long getUpc() {
		return upc;
	}

	/**
	 * Sets the selling unit's UPC.
	 *
	 * @param upc The selling unit's UPC.
	 */
	public void setUpc(long upc) {
		this.upc = upc;
	}

	/**
	 * Returns the size printed on the shelf tags.
	 *
	 * @return The size printed on the shelf tags.
	 */
	public String getTagSize() {
		return tagSize;
	}

	/**
	 * Sets the size printed on the shelf tags.
	 *
	 * @param tagSize The size printed on the shelf tags.
	 */
	public void setTagSize(String tagSize) {
		this.tagSize = tagSize;
	}

	/**
	 * Returns whether or not this UPC is a primary UPC for an item.
	 *
	 * @return True if this UPC is a primary UPC for an item and false otherwise.
	 */
	public boolean isPrimaryUpc() {
		return primaryUpc;
	}

	/**
	 * Sets whether or not this UPC is a primary UPC for an item.
	 *
	 * @param primaryUpc the primary upc
	 * @return True if this UPC is a primary UPC for an item and false otherwise.
	 */
	public void setPrimaryUpc(boolean primaryUpc) {
		this.primaryUpc = primaryUpc;
	}

	/**
	 * Returns the bonusSwitch for an item.
	 *
	 * @return The bonusSwitch for an item.
	 */
	public Boolean getBonusSwitch() {
		return bonusSwitch;
	}

	/**
	 * Sets the bonusSwitch for an item.
	 *
	 * @param bonusSwitch the bonusSwitch for an item.
	 */
	public void setBonusSwitch(Boolean bonusSwitch) {
		this.bonusSwitch = bonusSwitch;
	}

	/**
	 * Returns the selling unit's product id.
	 *
	 * @return The selling unit's product id.
	 */
	public long getProdId() {
		return prodId;
	}

	/**
	 * Sets the selling unit's product id.
	 *
	 * @param prodId The selling unit's product id.
	 */
	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	/**
	 * Returns the selling unit's product master.
	 *
	 * @return The selling unit's product master.
	 */
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * Sets the selling unit's product master.
	 *
	 * @param productMaster The selling unit's product master.
	 */
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	/**
	 * Returns the selling unit as it should be displayed on the GUI.
	 *
	 * @return A String representation of the selling unit as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		if(this.productMaster != null) {
			return String.format(SellingUnit.DISPLAY_NAME_FORMAT, this.productMaster.getDescription().trim(), this.upc);
		} else	{
			return String.valueOf(this.upc);
		}
	}

	/**
	 * Returns the discontinued date.
	 *
	 * @return the discontinued date.
	 */
	public LocalDate getDiscontinueDate() {
		return discontinueDate;
	}

	/**
	 * Sets the discontinued date.
	 *
	 * @param discontinueDate the discontinued date.
	 */
	public void setDiscontinueDate(LocalDate discontinueDate) {
		this.discontinueDate = discontinueDate;
	}

	/**
	 * Returns whether or not this UPC will be processed by scan maintenance.
	 *
	 * @return True if this UPC is processed by scan maintenance and false otherwise.
	 */
	public boolean isProcessedByScanMaintenance() {
		return processedByScanMaintenance;
	}

	/**
	 * Sets whether or not this UPC will be processed by scan maintenance.
	 *
	 * @param processedByScanMaintenance True if this UPC is processed by scan maintenance and false otherwise.
	 */
	public void setProcessedByScanMaintenance(boolean processedByScanMaintenance) {
		this.processedByScanMaintenance = processedByScanMaintenance;
	}

	/**
	 * Returns last scan date.
	 *
	 * @return last scan date.
	 */
	public LocalDate getLastScanDate() {
		return lastScanDate;
	}

	/**
	 * Sets last scan date.
	 *
	 * @param lastScanDate last scan date.
	 */
	public void setLastScanDate(LocalDate lastScanDate) {
		this.lastScanDate = lastScanDate;
	}

	/**
	 * Is product primary boolean.
	 *
	 * @return the boolean
	 */
	public boolean isProductPrimary() {
		return this.productMaster != null && this.upc == this.productMaster.getProductPrimaryScanCodeId();
	}

	/**
	 * Returns the NutritionalClaims. The nutritionalClaims holds all of the nutritionalClaims values for the current selling unit.
	 *
	 * @return NutritionalClaims nutritional claims
	 */
	public List<NutritionalClaims> getNutritionalClaims() {
		return nutritionalClaims;
	}

	/**
	 * Sets the NutritionalClaims
	 *
	 * @param nutritionalClaims The NutritionalClaims
	 */
	public void setNutritionalClaims(List<NutritionalClaims> nutritionalClaims) {
		this.nutritionalClaims = nutritionalClaims;
	}

	/**
	 * Gram weight of PSE in the item if flagged with PSE.
	 *
	 * @return pseGramWeight ;
	 */
	public Double getPseGramWeight() {
		return pseGramWeight;
	}

	/**
	 * Updates pseGramWeight
	 *
	 * @param pseGramWeight the new pseGramWeight
	 */
	public void setPseGramWeight(Double pseGramWeight) {
		this.pseGramWeight = pseGramWeight;
	}

	/**
	 * Size of the product being presented
	 *
	 * @return tagSizeDescription tag size description
	 */
	public String getTagSizeDescription() {
		return tagSizeDescription;
	}

	/**
	 * Updates tagSizeDescription
	 *
	 * @param tagSizeDescription the new tagSizeDescription
	 */
	public void setTagSizeDescription(String tagSizeDescription) {
		this.tagSizeDescription = tagSizeDescription;
	}

	/**
	 * Returns the WicApprovedProductListId. The approved product list id for the current product.
	 *
	 * @return WicApprovedProductListId wic approved product list id
	 */
	public Long getWicApprovedProductListId() {
		return wicApprovedProductListId;
	}

	/**
	 * Sets the WicApprovedProductListId
	 *
	 * @param wicApprovedProductListId The WicApprovedProductListId
	 */
	public void setWicApprovedProductListId(Long wicApprovedProductListId) {
		this.wicApprovedProductListId = wicApprovedProductListId;
	}

	/**
	 * Gets scan type code.
	 *
	 * @return the scan type code
	 */
	public String getScanTypeCode() {
		return scanTypeCode;
	}

	/**
	 * Sets scan type code.
	 *
	 * @param scanTypeCode the scan type code
	 */
	public void setScanTypeCode(String scanTypeCode) {
		this.scanTypeCode = scanTypeCode;
	}

	/**
	 * Get the productPkVariations.
	 *
	 * @return the productPkVariations
	 */
	public List<ProductPkVariation> getProductPkVariations() {
		return productPkVariations;
	}

	/**
	 * Set the productPkVariations.
	 *
	 * @param productPkVariations the productPkVariations to set
	 */
	public void setProductPkVariations(List<ProductPkVariation> productPkVariations) {
		this.productPkVariations = productPkVariations;
	}

	/**
	 * Get the productNutrients.
	 *
	 * @return the productNutrients
	 */
	public List<ProductNutrient> getProductNutrients() {
		return productNutrients;
	}

	/**
	 * Set the productNutrients.
	 *
	 * @param productNutrients the productNutrients to set
	 */
	public void setProductNutrients(List<ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}
	/**
	 * Returns the ID of the person or system who created this record.
	 *
	 * @return The ID of the person or system who created this record.
	 */
	public String getCreateId() {
		return createId;
	}
	/**
	 * Sets the ID of the person or system who created this record.
	 *
	 * @param createId The ID of the person or system who created this record.
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	/**
	 * Returns the time this record was created.
	 *
	 * @return The time this record was created.
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the time this record was created.
	 *
	 * @param createTime The time this record was created.
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * Gets the costOwner of the SellingUnit.
	 *
	 * @return The value of costOwner.
	 */
	public CostOwner getCostOwner() {
		return costOwner;
	}

	/**
	 * Set costOwner of the SellingUnit.
	 *
	 * @param costOwner The cost owner to set.
	 */
	public void setCostOwner(CostOwner costOwner) {
		this.costOwner = costOwner;
	}

	/**
	 * Compares another object to this one. This only compares the keys.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SellingUnit)) return false;

		SellingUnit that = (SellingUnit) o;

		return upc == that.upc;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return (int) (upc ^ (upc >>> SellingUnit.FOUR_BYTES));
	}


	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "SellingUnit{" +
				"upc=" + upc +
				", primaryUpc=" + primaryUpc +
				", tagSize='" + tagSize + '\'' +
				", discontinueDate=" + discontinueDate +
				", processedByScanMaintenance =" + processedByScanMaintenance +
				", prodId=" + prodId +
				", bonusSwitch=" + bonusSwitch +
				", prodId=" + prodId +
				", lastScanDate=" + lastScanDate +
				", pseGramWeight=" + pseGramWeight +
				", tagSizeDescription=" + tagSizeDescription +
				'}';
	}
}
