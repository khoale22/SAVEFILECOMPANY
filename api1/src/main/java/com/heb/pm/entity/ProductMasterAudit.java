package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.*;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity for the product_master_aud table.
 *
 */
@Entity
@Table(name="PRODUCT_MASTER_AUD")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ProductMasterAudit implements Audit,Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final String PRODUCT_ID_SORT_FIELD = "prodId";

	@EmbeddedId
	private ProductMasterAuditKey key;

    @Column(name = "act_cd")
    private String action;

	@AuditableField(displayName = "Description",filter = {FilterConstants.SHELF_ATTRIBUTES_AUDIT, FilterConstants.PRODUCT_INFO_AUDIT})
	@Column(name="PROD_ENG_DES")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String description;

	@AuditableField(displayName = "BDM", filter = FilterConstants.PRODUCT_INFO_AUDIT, codeTableDisplayNameMethod= "getBdmDisplayName")
	@Column(name = "BDM_CD", nullable = false)
	@Type(type="fixedLengthCharPK")
	private String bdmCode;
 // dB2Oracle changes vn00907 starts
	@Column(name="pd_omi_com_cls_cd")
	private Integer classCode;

	@AuditableField(displayName = "Commodity",filter = FilterConstants.PRODUCT_INFO_AUDIT, codeTableDisplayNameMethod= "getCommodityDisplayName")
	@Column(name="pd_omi_com_cd")
	private Integer commodityCode;

	@AuditableField(displayName = "Sub-Commodity",filter = FilterConstants.PRODUCT_INFO_AUDIT, codeTableDisplayNameMethod= "getSubCommodityDisplayName")
	@Column(name="pd_omi_sub_com_cd")
	private Integer subCommodityCode;
 // dB2Oracle changes vn00907 ends
	@Column(name="STR_DEPT_NBR")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")   
	private String departmentCode;

	@Column(name="STR_SUB_DEPT_ID")
	@Type(type="fixedLengthChar")  
	private String subDepartmentCode;

	@Column(name = "PROD_PRIM_SCN_ID")
	private Long productPrimaryScanCodeId;

	@AuditableField(displayName = "Spanish Description",filter = FilterConstants.SHELF_ATTRIBUTES_AUDIT)
	@Column(name="PROD_SPNSH_DES")
	private String spanishDescription;

	@AuditableField(displayName = "Tag Item Code",filter = FilterConstants.SHELF_ATTRIBUTES_AUDIT)
	@Column(name = "TAG_ITM_ID")
	private Long tagItemCode;

	@AuditableField(displayName = "Tag Item Type",filter = FilterConstants.SHELF_ATTRIBUTES_AUDIT)
	@Column(name="TAG_ITM_KEY_TYP_CD")
	private String tagItemType;

	@Column(name = "PROD_SZ_TXT")
	private String productSizeText;

	@Column(name = "pss_dept_1")
	private Integer pssDepartmentOne;

	@Column(name = "PROD_TYP_CD", nullable = false)
	@Type(type="fixedLengthChar")
	private String prodTypCd;

	@AuditableField(displayName = "Retail Link Code", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name="RETL_LINK_CD")
	private long retailLink;

	@AuditableField(displayName = "Brand", filter = FilterConstants.PRODUCT_INFO_AUDIT, codeTableDisplayNameMethod = "getProductBrandDisplayName")
	@Column(name = "prod_brnd_id")
	private long prodBrandId;

	@Column(name = "rc_im_qty_req_flag")
	private String quantityRequiredFlag;

	@AuditableField(displayName = "Price Required", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name ="rc_im_prc_req_flag")
	private Boolean priceRequiredFlag;

	@AuditableField(displayName = "Qualifying Condition", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name ="tax_qual_cd")
	@Type(type="fixedLengthCharPk")
	private String taxQualifyingCode;

	@Column(name = "SALS_RSTR_CD")
	private String salesRestrictCode;

	@Column(name="lst_updt_ts")
	private LocalDateTime lastUpdateTime;

	@Column(name="lst_updt_uid")
	private String lastUpdateUserId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_brnd_id", insertable = false, updatable = false)

	private ProductBrand productBrand;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="pd_omi_com_cls_cd", referencedColumnName = "pd_omi_com_cls_cd", insertable = false, updatable = false),
			@JoinColumn(name="pd_omi_com_cd", referencedColumnName = "pd_omi_com_cd", insertable = false, updatable = false)
	})
	private ClassCommodity classCommodity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"viewableSellingRestrictions","viewableShippingRestrictions"})
	@JoinColumns({
			@JoinColumn(name="STR_DEPT_NBR", referencedColumnName = "STR_DEPT_NBR", insertable = false, updatable = false),
			@JoinColumn(name="STR_SUB_DEPT_ID", referencedColumnName = "STR_SUB_DEPT_ID", insertable = false, updatable = false)
	})
	private SubDepartment subDepartment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="pd_omi_com_cls_cd", referencedColumnName = "pd_omi_com_cls_cd", insertable = false, updatable = false),
			@JoinColumn(name="pd_omi_com_cd", referencedColumnName = "pd_omi_com_cd", insertable = false, updatable = false),
			@JoinColumn(name="pd_omi_sub_com_cd", referencedColumnName = "pd_omi_sub_com_cd", insertable = false, updatable = false)
	})
	@NotFound(action = NotFoundAction.IGNORE)
	private SubCommodity subCommodity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="BDM_CD", referencedColumnName = "BDM_CD", insertable = false, updatable = false)
	})
	private Bdm bdm;

	@JsonIgnoreProperties({"productMaster","associateUpcs"})
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_PRIM_SCN_ID", referencedColumnName = "scn_cd_id", insertable = false, updatable = false, nullable = false)
	private SellingUnit productPrimarySellingUnit;


//	@OneToMany(fetch = FetchType.LAZY)
//	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
//	private List<ProductDescription> productDescriptions;

	@AuditableField(displayName = "Show Calories", filter = FilterConstants.PRODUCT_INFO_AUDIT)
	@Column(name = "SHOW_CLRS_SW")
	private Boolean showCalories;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
	private GoodsProduct goodsProduct;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_temp_cntl_cd", referencedColumnName = "prod_temp_cntl_cd", insertable = false, updatable = false)
	private ProductTemperatureControl temperatureControl;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false)
	private List<ProductMarketingClaim> productMarketingClaims;

//	@OneToMany(fetch = FetchType.LAZY)
//	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
//	@OrderBy("seq_nbr desc")
//	private List<ProductCubiscan> productCubiscan;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pd_omi_com_cls_cd", referencedColumnName = "item_cls_code", insertable = false, updatable = false)
	private ItemClass itemClass;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false)
	private List<ProductRestrictions> restrictions;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="attr_val_nbr", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private List<DistributionFilter> distributionFilters;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private List<TierPricing> tierPricings;

	@Where(clause = "dta_src_sys = 4 and itm_prod_key_cd = 'PROD' and attr_cd_id is not null")
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private List<MasterDataExtensionAttribute> masterDataExtensionAttributes;

	@Column(name="subscr_prod_sw")
	private boolean subscription;

	@Column(name="subscr_strt_dt")
	private LocalDate subscriptionStartDate;

	@Column(name="subscr_end_dt")
	private LocalDate subscriptionEndDate;

	@Column(name="cre8_ts")
	private LocalDate createDate;

    @Column(name = "CRE8_UID")
    private String createdBy;

    @AuditableField(displayName = "Gift Message Required",filter = FilterConstants.ONLINE_ATTRIBUTES_AUDIT)
	@Column(name="GIFT_MSG_REQ_SW")
	private Boolean giftMessageSwitch;

	@Transient
	private Boolean isDistinctive;

	@Transient
	private Boolean isPrimoPick;

	@Transient
	private Boolean isOwnBrand;

	@Transient
	private Boolean isGoLocal;

	@Transient
	private Boolean isTotallyTexas;

	@Transient
	private Boolean isSelectIngredients;

	@Transient
	private boolean descriptionsSet = false;

	@Transient
	private String primoPickProposedDescription;

	@Transient
	private String primoPickDescription;

	public ProductTemperatureControl getTemperatureControl() {
		return temperatureControl;
	}

	public void setTemperatureControl(ProductTemperatureControl temperatureControl) {
		this.temperatureControl = temperatureControl;
	}

	@Transient
	private String actionCode;

	/**
	 * Getter for key
	 * @return key
	 */
	public ProductMasterAuditKey getKey() {
		return key;
	}

	/**
	 * Setter for key
	 * @param key
	 */
	public void setKey(ProductMasterAuditKey key) {
		this.key = key;
	}

	/**
	 * This is the action code for this product master.
	 *
	 * @return The action code.
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Set the action code for this product master.
	 *
	 * @param actionCode The action code.
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Whether or not this product contains a marketing claim defined as distinctive.
	 *
	 * @return True if this product contains a distinctive marketing claim, false otherwise.
	 */
	public Boolean getDistinctive() {
		return isDistinctive;
	}

	/**
	 * Sets whether this product has a distinctive marketing claim.
	 *
	 * @param distinctive The has distinctive marketing claim.
	 */
	public void setDistinctive(Boolean distinctive) {
		this.isDistinctive = distinctive;
	}

	/**
	 * Whether or not this product contains a marketing claim defined as primo pick.
	 *
	 * @return True if this product contains a primo pick marketing claim, false otherwise.
	 */
	public Boolean getPrimoPick() {
		return isPrimoPick;
	}

	/**
	 * Sets whether this product has a primo pick marketing claim.
	 *
	 * @param primoPick The has primo pick marketing claim.
	 */
	public void setPrimoPick(Boolean primoPick) {
		this.isPrimoPick = primoPick;
	}

	/**
	 * Whether or not this product contains a marketing claim defined as own brand.
	 *
	 * @return True if this product contains an own brand marketing claim, false otherwise.
	 */
	public Boolean getOwnBrand() {
		return isOwnBrand;
	}

	/**
	 * Sets whether this product has an own brand marketing claim.
	 *
	 * @param ownBrand The has own brand marketing claim.
	 */
	public void setOwnBrand(Boolean ownBrand) {
		this.isOwnBrand = ownBrand;
	}

	/**
	 * Whether or not this product contains a marketing claim defined as go local.
	 *
	 * @return True if this product contains a go local marketing claim, false otherwise.
	 */
	public Boolean getGoLocal() {
		return isGoLocal;
	}

	/**
	 * Sets whether this product has a go local marketing claim.
	 *
	 * @param goLocal The has go local marketing claim.
	 */
	public void setGoLocal(Boolean goLocal) {
		this.isGoLocal = goLocal;
	}

	/**
	 * Whether or not this product contains a marketing claim defined as totally texas.
	 *
	 * @return True if this product contains a totally texas marketing claim, false otherwise.
	 */
	public Boolean getTotallyTexas() {
		return isTotallyTexas;
	}

	/**
	 * Sets whether this product has a totally texas marketing claim.
	 *
	 * @param totallyTexas The has totally texas marketing claim.
	 */
	public void setTotallyTexas(Boolean totallyTexas) {
		this.isTotallyTexas = totallyTexas;
	}

	/**
	 * Whether or not this product contains a marketing claim defined as select ingredients.
	 *
	 * @return True if this product contains a select ingredients marketing claim, false otherwise.
	 */
	public Boolean getSelectIngredients() {
		return isSelectIngredients;
	}

	/**
	 * Sets whether this product has a select ingredients marketing claim.
	 *
	 * @param selectIngredients The has select ingredients marketing claim
	 */
	public void setSelectIngredients(Boolean selectIngredients) {
		this.isSelectIngredients = selectIngredients;
	}

	/**
	 * Gets price required flag. Indicates whether the retail price must be hand keyed by the checker.
	 *
	 * @return the price required flag Indicates whether the retail price must be hand keyed by the checker.
	 */
	public Boolean getPriceRequiredFlag() {
		return priceRequiredFlag;
	}

	/**
	 * Sets price required flag.  Indicates whether the retail price must be hand keyed by the checker.
	 *
	 * @param priceRequiredFlag the price required flag Indicates whether the retail price must be hand keyed by the checker.
	 */
	public void setPriceRequiredFlag(Boolean priceRequiredFlag) {
		this.priceRequiredFlag = priceRequiredFlag;
	}

	/**
	 * Gets prod brand id for a category of products that are all made by a particular company and all have a particular name.
	 *
	 * @return the prod brand id for a category of products that are all made by a particular company and all have a particular name.
	 */
	public long getProdBrandId() {
		return prodBrandId;
	}

	/**
	 * Sets prod brand id.  Id set for a category of products that are all made by a particular company and all have a particular name.
	 *
	 * @param prodBrandId the prod brand id for a category of products that are all made by a particular company and all have a particular name.
	 */
	public void setProdBrandId(long prodBrandId) {
		this.prodBrandId = prodBrandId;
	}

	/**
	 * Gets prod brand.  A category of products that are all made by a particular company and all have a particular name.
	 *
	 * @return the prod brand category of products that are all made by a particular company and all have a particular name.
	 */

	public ProductBrand getProductBrand() {
		return productBrand;
	}

	/**
	 * Sets prod brand. A category of products that are all made by a particular company and all have a particular name.
	 *
	 * @param productBrand the prod brand category of products that are all made by a particular company and all have a particular name.
	 */
	public void setProductBrand(ProductBrand productBrand) {
		this.productBrand = productBrand;
	}

	public String getProductBrandDisplayName(){
		if (this.getProductBrand() == null) {
			return "Unknown";
		} else {
			return this.getProductBrand().getDisplayName();
		}
	}

	/**
	 * Get sub commodity display name.
	 * @return String
	 */
	public String getSubCommodityDisplayName() {
		if (this.getSubCommodity() == null) {
			return "Unknown";
		} else {
			return this.getSubCommodity().getDisplayName();
		}
	}

	/**
	 * Get bdm display name.
	 * @return String
	 */
	public String getBdmDisplayName() {
		if (this.getBdm() == null) {
			return "Unknown";
		} else {
			return this.getBdm().getDisplayName();
		}
	}
	/**
	 * Get commodity display name.
	 * @return String
	 */
	public String getCommodityDisplayName() {
		if (this.getClassCommodity() == null) {
			return "Unknown";
		} else {
			return this.getClassCommodity().getDisplayName();
		}
	}
	/**
	 * Gets quantity required flag.  Indicator that will prompt at POS that the qty of items being purchased needs to be registered;
	 * to help with scan thru and loss prevention as well as restrictions if necessary.
	 *
	 * @return the quantity required flag of an Indicator that will prompt at POS that the qty of items being purchased needs to be registered;
	 * to help with scan thru and loss prevention as well as restrictions if necessary.
	 */
	public String getQuantityRequiredFlag() {
		return quantityRequiredFlag;
	}

	/**
	 * Sets quantity required flag.  Indicator that will prompt at POS that the qty of items being purchased needs to be registered;
	 * to help with scan thru and loss prevention as well as restrictions if necessary.
	 *
	 * @param quantityRequiredFlag the quantity required flag of an Indicator that will prompt at POS that the qty of items being purchased needs to be registered;
	 * to help with scan thru and loss prevention as well as restrictions if necessary.
	 */
	public void setQuantityRequiredFlag(String quantityRequiredFlag) {
		this.quantityRequiredFlag = quantityRequiredFlag;
	}
	/**
	 * Returns the bdm.
	 *
	 * @return the bdm.
	 */
	public Bdm getBdm() {
		return bdm;
	}

	/**
	 * Sets the bdm.
	 *
	 * @param bdm The bdm.
	 */
	public void setBdm(Bdm bdm) {
		this.bdm = bdm;
	}
//	/**
//	 * Gets product cubiscan measurements.
//	 *
//	 * @return the product cubiscan measurements from the cubiscan measuring device.
//	 */
//	public List<ProductCubiscan> getProductCubiscan() {
//		return productCubiscan;
//	}
//
//	/**
//	 * Sets product cubiscan measurements.
//	 *
//	 * @param productCubiscan the product cubiscan measurements from the cubiscan measuring device.
//	 */
//	public void setProductCubiscan(List<ProductCubiscan> productCubiscan) {
//		this.productCubiscan = productCubiscan;
//	}

	/**
	 * Is show calories boolean.
	 *
	 * @return the boolean
	 */
	public Boolean getShowCalories() {
		return showCalories;
	}

	/**
	 * Sets show calories.
	 *
	 * @param showCalories the show calories
	 */
	public void setShowCalories(Boolean showCalories) {
		this.showCalories = showCalories;
	}

	/**
	 * Boolean comparison to Y.
	 */
	private static final String YES = "Y";


	/**
	 * Get department string string.
	 *
	 * @return the string
	 */
	public String getDepartmentString(){
		if(this.departmentCode == null || this.subDepartmentCode == null){
			return null;
		}
		return this.departmentCode.trim() + this.subDepartmentCode.trim();
	}

	/**
	 * Gets retail link.
	 *
	 * @return the retail link code
	 */
	public long getRetailLink() {
		return retailLink;
	}

	/**
	 * Sets retail link.
	 *
	 * @param retailLink the retail link code
	 */
	public void setRetailLink(long retailLink) {
		this.retailLink = retailLink;
	}

	/**
	 * Returns the product description.
	 *
	 * @return description the product description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the product description.
	 *
	 * @param description the product description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

//	/**
//	 * Returns the ProdItem records associated with this item.
//	 *
//	 * @return The ProdItem records associated with this item.
//	 */
//	public List<ProdItem> getProdItems() {
//		return prodItems;
//	}
//
//	/**
//	 * Sets the prodItems records associated with this item.
//	 *
//	 * @param prodItems The prodItems records associated with this item.
//	 */
//	public void setProdItems(List<ProdItem> prodItems) {
//		this.prodItems = prodItems;
//	}

	/**
	 * Returns the prodId  associated with this item.
	 *
	 * @return The prodId associated with this item.
	 */
	public Long getProdId() {
		return key.getProdId();
	}

	/**
	 * Sets the prodId associated with this item.
	 *
	 * @param prodId The prodId associated with this item.
	 */
	public void setProdId(Long prodId) {
		this.key.setProdId(prodId);
	}

	/**
	 * Returns the OMI Class for this object.
	 *
	 * @return The OMI Class for this object.
	 */
	public Integer getClassCode() {
		return classCode;
	}

	/**
	 * Sets the OMI Class for this object.
	 *
	 * @param classCode The Class for this object.
	 */
	public void setClassCode(Integer classCode) {
		this.classCode = classCode;
	}

	/**
	 * Sets the Commodity for this object.
	 *
	 * @param commodityCode The Commodity for this object.
	 */
	public void setCommodityCode(Integer commodityCode) {
		this.commodityCode = commodityCode;
	}

	/**
	 * Returns the Commodity for this object.
	 *
	 * @return The Commodity for this object.
	 */
	public Integer getCommodityCode() {
		return commodityCode;
	}

	/**
	 * Returns the productPrimarySellingUnit for the product
	 *
	 * @return the productPrimarySellingUnit
	 */
	public SellingUnit getProductPrimarySellingUnit() {
		return productPrimarySellingUnit;
	}

	/**
	 * Updates the primarySellingUnit for the product
	 *
	 * @param productPrimarySellingUnit the new primary selling unit
	 */
	public void setProductPrimarySellingUnit(SellingUnit productPrimarySellingUnit) {
		this.productPrimarySellingUnit = productPrimarySellingUnit;
	}

	/**
	 * Returns the sub commodity for this object.
	 *
	 * @return The sub commodity for this object.
	 */
	public Integer getSubCommodityCode() {
		return subCommodityCode;
	}

	/**
	 * Sets the sub commodity for this object.
	 *
	 * @param subCommodityCode The sub commodity for this object.
	 */
	public void setSubCommodityCode(Integer subCommodityCode) {
		this.subCommodityCode = subCommodityCode;
	}

	/**
	 * Returns the department code.
	 *
	 * @return The departmentCode for this object.
	 */
	public String getDepartmentCode() {
		return departmentCode;
	}

	/**
	 * Sets the department code.
	 *
	 * @param departmentCode The departmentCode for this object.
	 */
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	/**
	 * Returns the sub department code.
	 *
	 * @return The sub department code for this object.
	 */
	public String getSubDepartmentCode() {
		return subDepartmentCode;
	}

	/**
	 * Sets the sub department code.
	 *
	 * @param subDepartmentCode The sub departmentCode for this object.
	 */
	public void setSubDepartmentCode(String subDepartmentCode) {
		this.subDepartmentCode = subDepartmentCode;
	}

//	/**
//	 * Returns the product master's selling unit.
//	 *
//	 * @return The product master's selling unit.
//	 */
//	public List<SellingUnit> getSellingUnits() {
//		return sellingUnits;
//	}
//
//	/**
//	 * Sets the product master's selling unit.
//	 *
//	 * @param sellingUnits The product master's selling unit.
//	 */
//	public void setSellingUnits(List<SellingUnit> sellingUnits) {
//		this.sellingUnits = sellingUnits;
//	}

	/**
	 * Returns the product master's class commodity.
	 *
	 * @return The product master's class commodity.
	 */
	public ClassCommodity getClassCommodity() {
		return classCommodity;
	}

	/**
	 * Sets the product master's class commodity.
	 *
	 * @param classCommodity The product master's class commodity.
	 */
	public void setClassCommodity(ClassCommodity classCommodity) {
		this.classCommodity = classCommodity;
	}

	/**
	 * Returns the product master's sub department.
	 *
	 * @return The product master's sub department.
	 */
	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	/**
	 * Sets the product master's sub department.
	 *
	 * @param subDepartment The product master's sub department.
	 */
	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * Returns the product master's sub commodity.
	 *
	 * @return The product master's sub commodity.
	 */
	public SubCommodity getSubCommodity() {
		return subCommodity;
	}

	/**
	 * Sets the product master's sub commodity.
	 *
	 * @param subCommodity The product master's sub commodity.
	 */
	public void setSubCommodity(SubCommodity subCommodity) {
		this.subCommodity = subCommodity;
	}

//	/**
//	 * Get the list of customer friendly product descriptions
//	 *
//	 * @return the list of customer friendly product descriptions
//	 */
//	public List<ProductDescription> getProductDescriptions() {
//		return productDescriptions;
//	}
//
//	/**
//	 * Update the list of product descriptions
//	 *
//	 * @param productDescriptions the new list of product descriptions.
//	 */
//	public void setProductDescriptions(List<ProductDescription> productDescriptions) {
//		this.productDescriptions = productDescriptions;
//	}

	/**
	 * Returns the ProductPrimaryScanCodeId
	 *
	 * @return ProductPrimaryScanCodeId
	 */
	public Long getProductPrimaryScanCodeId() {
		return productPrimaryScanCodeId;
	}

	/**
	 * Sets the ProductPrimaryScanCodeId
	 *
	 * @param productPrimaryScanCodeId The ProductPrimaryScanCodeId
	 */
	public void setProductPrimaryScanCodeId(Long productPrimaryScanCodeId) {
		this.productPrimaryScanCodeId = productPrimaryScanCodeId;
	}

	/**
	 * Returns the spanish description.
	 *
	 * @return the spanish description.
	 */
	public String getSpanishDescription() {
		return this.spanishDescription != null ? spanishDescription.trim() : StringUtils.EMPTY;
	}

	/**
	 * Sets the spanish description.
	 * 
	 * @param spanishDescription the spanish description.
	 */
	public void setSpanishDescription(String spanishDescription) {
		this.spanishDescription = spanishDescription;
	}

	/**
	 * Returns the tag item code.
	 *
	 * @return the tag item code.
	 */
	public Long getTagItemCode() {
		return tagItemCode;
	}

	/**
	 * Sets the tag item code.
	 *
	 * @param tagItemCode the tag item code.
	 */
	public void setTagItemCode(Long tagItemCode) {
		this.tagItemCode = tagItemCode;
	}

	/**
	 * Returns the tag item type.
	 *
	 * @return the tag item type.
	 */
	public String getTagItemType() {
		return tagItemType;
	}

	/**
	 * Sets  the tag item type.
	 * 
	 * @param tagItemType the tag item type.
	 */
	public void setTagItemType(String tagItemType) {
		this.tagItemType = tagItemType;
	}

	/**
	 * Returns the Goods Product entity.
	 *
	 * @return the Goods Product entity.
	 */
	public GoodsProduct getGoodsProduct() {
		return goodsProduct;
	}

	/**
	 * Sets the Goods Product entity.
	 * 
	 * @param goodsProduct the Goods Product entity.
	 */
	public void setGoodsProduct(GoodsProduct goodsProduct) {
		this.goodsProduct = goodsProduct;
	}

	/**
	 * Returns a list of the Product Marketing Claims.
	 *
	 * @return a list of the Product Marketing Claims.
	 */
	public List<ProductMarketingClaim> getProductMarketingClaims() {
		return productMarketingClaims;
	}

	/**
	 * Sets the list of the Product Marketing Claims.
	 *
	 * @param productMarketingClaims the list of the Product Marketing Claims.
	 */
	public void setProductMarketingClaims(List<ProductMarketingClaim> productMarketingClaims) {
		this.productMarketingClaims = productMarketingClaims;
	}

	/**
	 * Returns the PssDepartmentOne. This is the default pss department. WHS uses default pss department if needed.
	 *
	 * @return PssDepartmentOne
	 */
	public Integer getPssDepartmentOne() {
		return pssDepartmentOne;
	}

	/**
	 * Sets the PssDepartmentOne
	 *
	 * @param pssDepartmentOne The PssDepartmentOne
	 */
	public void setPssDepartmentOne(Integer pssDepartmentOne) {
		this.pssDepartmentOne = pssDepartmentOne;
	}

	/**
	 * Returns the ItemClass. The current products item class.
	 * Represents item class in the product hierarchy (in between sub-department and class-commodity).
	 *
	 * @return ItemClass
	 */
	public ItemClass getItemClass() {
		return itemClass;
	}
	public String getProductSizeText() {
		return productSizeText;
	}

	/**
	 * Sets the ItemClass
	 *
	 * @param itemClass The ItemClass
	 */
	public void setItemClass(ItemClass itemClass) {
		this.itemClass = itemClass;
	}

	/**
	 * Returns the product's size.
	 *
	 * @param productSizeText The product's size.
	 */
	public void setProductSizeText(String productSizeText) {
		this.productSizeText = productSizeText;
	}

	/**
	 * Returns the distribution filters tied to this product.
	 *
	 * @return The distribution filters tied to this product.
	 */
	public List<DistributionFilter> getDistributionFilters() {
		return distributionFilters;
	}

	/**
	 * Sets the distribution filters tied to this product.
	 *
	 * @param distributionFilters The distribution filters tied to this product.
	 */
	public void setDistributionFilters(List<DistributionFilter> distributionFilters) {
		this.distributionFilters = distributionFilters;
	}

	/**
	 * Returns the last date and time of an update to this record.
	 *
	 * @return The last date and time of an update to this record.
	 */
	public LocalDateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * Sets the last date and time of an update to this record.
	 *
	 * @param lastUpdateTime The last date and time of an update to this record.
	 */
	public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * Returns the one-pass ID of the last user to update this record.
	 *
	 * @return The one-pass ID of the last user to update this record.
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the one-pass ID of the last user to update this record.
	 *
	 * @param lastUpdateUserId The one-pass ID of the last user to update this record.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

//	/**
//	 * Determines whether the product is DSV or not.
//	 * @return whether the product is DSV or not.
//	 */
//	public Boolean isDSV() {
//		if(this.getProdItems() == null) {
//			return null;
//		}
//		for(ProdItem prodItem : this.getProdItems()) {
//			if (prodItem.getItemMaster().isDsv().trim().equals(YES)) {
//				return true;
//			}
//		}
//		return false;
//	}

	/**
	 * Returns a list of selling restrictions attached to a product
	 * @return restrictions
	 */
	public List<ProductRestrictions> getRestrictions() {
		return restrictions;
	}

	/**
	 * Updates restrictions
	 * @param restrictions the new set of restrictions
	 */
	public void setRestrictions(List<ProductRestrictions> restrictions) {
		this.restrictions = restrictions;
	}
	/**
	 * Returns the prodTypCd.
	 *
	 * @return String
	 */
	public String getProdTypCd() {
		return this.prodTypCd;
	}
	/**
	 * Sets the prodTypCd
	 *
	 * @param prodTypCd The prodTypCd
	 */
	public void setProdTypCd(String prodTypCd) {
		this.prodTypCd = prodTypCd;
	}
	/**
	 * Returns the bdmCode.
	 *
	 * @return String
	 */
	public String getBdmCode() {
		return bdmCode;
	}
	/**
	 * Sets the bdmCode
	 *
	 * @param bdmCode The bdmCode
	 */
	public void setBdmCode(String bdmCode) {
		this.bdmCode = bdmCode;
	}

	/**
	 * Returns the TierPricings. The tier pricing is a pricing method where it entices customers to buy larger quantities
	 * at a discount.
	 *
	 * @return TierPricings
	 */
	public List<TierPricing> getTierPricings() {
		return tierPricings;
	}

	/**
	 * Sets the TierPricings
	 *
	 * @param tierPricings The TierPricings
	 */
	public void setTierPricings(List<TierPricing> tierPricings) {
		this.tierPricings = tierPricings;
	}

	/**
	 * Returns the MasterDataExtensionAttributes. These are the attributes that are tied to this product.
	 *
	 * @return MasterDataExtensionAttributes
	 */
	public List<MasterDataExtensionAttribute> getMasterDataExtensionAttributes() {
		return masterDataExtensionAttributes;
	}

	/**
	 * Sets the MasterDataExtensionAttributes
	 *
	 * @param masterDataExtensionAttributes The MasterDataExtensionAttributes
	 */
	public void setMasterDataExtensionAttributes(List<MasterDataExtensionAttribute> masterDataExtensionAttributes) {
		this.masterDataExtensionAttributes = masterDataExtensionAttributes;
	}

	/**
	 * The date this product was created.
	 *
	 * @return The date this product was created.
	 */
	public LocalDate getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the date this product was created.
	 *
	 * @param createDate The date this product was created.
	 */
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	/**
	 * Returns the tax qualifying code. This is a code that can be used to call Vertex service to retrieve tax a
	 * qualifying condition.
	 *
	 * @return Tax qualifying code.
	 */
	public String getTaxQualifyingCode() {
		return taxQualifyingCode;
	}

	/**
	 * Sets the tax qualifying code.
	 *
	 * @param taxQualifyingCode The tax qualifying code.
	 */
	public void setTaxQualifyingCode(String taxQualifyingCode) {
		this.taxQualifyingCode = taxQualifyingCode;
	}

//	/**
//	 * Returns the proposed primo pick description of this product (if set).
//	 *
//	 * @return The proposed primo pick description of this product (Null if it is not set)
//	 */
//	public String getPrimoPickProposedDescription() {
//		this.loadDescriptions();
//		return this.primoPickProposedDescription;
//	}
//
//	/**
//	 * Returns the primo pick description of this product (if set).
//	 *
//	 * @return The primo pick description of this product (Null if not set).
//	 */
//	public String getPrimoPickDescription() {
//		this.loadDescriptions();
//		return this.primoPickDescription;
//	}

//	/**
//	 * Loads all the descriptions that are tied to the prod_des_txt table.
//	 */
//	private void loadDescriptions() {
//		if (this.productDescriptions == null || this.descriptionsSet) {
//			return;
//		}
//
//		for (ProductDescription description : this.productDescriptions) {
//			switch (description.getKey().getDescriptionType()) {
//				case DescriptionType.PRIMO_PICK_LONG_DESCRIPTION:
//					this.primoPickProposedDescription = description.getDescription();
//					break;
//				case DescriptionType.PRIMO_PICK_SHORT_DESCRIPTION:
//					this.primoPickDescription = description.getDescription();
//			}
//		}
//
//		this.descriptionsSet = true;
//	}
       /**
	 * Returns the salesRestrictCode. These are the attributes that are tied to this product.
	 *
	 * @return salesRestrictCode
	 */
	public String getSalesRestrictCode() {
		return salesRestrictCode;
	}
	/**
	 * Sets the taxQualityCode
	 *
	 * @param salesRestrictCode The taxQualityCode
	 */
	public void setSalesRestrictCode(String salesRestrictCode) {
		this.salesRestrictCode = salesRestrictCode;
	}

	/**
	 * @return Gets the value of subscription and returns subscription
	 */
	public void setSubscription(boolean subscription) {
		this.subscription = subscription;
	}

	/**
	 * Sets the subscription
	 */
	public boolean isSubscription() {
		return subscription;
	}

	/**
	 * @return Gets the value of subscriptionStartDate and returns subscriptionStartDate
	 */
	public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	/**
	 * Sets the subscriptionStartDate
	 */
	public LocalDate getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	/**
	 * @return Gets the value of subscriptionEndDate and returns subscriptionEndaDate
	 */
	public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	/**
	 * Sets the subscriptionEndDate
	 */
	public LocalDate getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	/**
	 * Returns whether or not the product requires a gift message
	 * @return
	 */
	public Boolean getGiftMessageSwitch() {
		return giftMessageSwitch;
	}

	/**
	 * Updates whether or not the product requires a gift message
	 * @param giftMessageSwitch
	 */
	public void setGiftMessageSwitch(Boolean giftMessageSwitch) {
		this.giftMessageSwitch = giftMessageSwitch;
	}

	/**
	 * Compares another object to this one. If that object is an ItemMaster, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductMasterAudit)) return false;
		ProductMasterAudit that = (ProductMasterAudit) o;
		Long prodId = this.getProdId();
		return prodId != null ? prodId.equals(that.getProdId()) : that.getProdId()== null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return (int) (this.getProdId() ^ (this.getProdId() >>> FOUR_BYTES));
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductMasterAudit{" +
				"description='" + description + '\'' +
				", prodId=" + this.getProdId() +
				'}';
	}

	/**
	 * Returns the default sort order for the prod_del table.
	 *
	 * @return The default sort order for the prod_del table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, ProductMasterAudit.PRODUCT_ID_SORT_FIELD)
		);
	}

    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }

    @Override
    public String getChangedBy() {
        return lastUpdateUserId;
    }

    @Override
    public void setChangedBy(String changedBy) {
        this.lastUpdateUserId = changedBy;
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
}
