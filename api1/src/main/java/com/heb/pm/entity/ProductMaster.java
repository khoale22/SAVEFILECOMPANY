package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Entity for the product_master table.
 * Created by s573181 on 6/3/2016.
 * Updated by s753601 on 3/24/2017
 *
 * @since 2.4.0
 */
@Entity
@Table(name="PRODUCT_MASTER")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ProductMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;
	private static final long PDP_TEMPLATE_CODE = 515L;
	private static final String PRODUCT_ID_SORT_FIELD = "prodId";

	@Column(name="PROD_ENG_DES")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String description;
	@Column(name = "BDM_CD", nullable = false)
	@Type(type="fixedLengthCharPK")
	private String bdmCode;
 // dB2Oracle changes vn00907 starts
	@Column(name="pd_omi_com_cls_cd")
	private Integer classCode;

	@Column(name="pd_omi_com_cd")
	private Integer commodityCode;

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

	@Column(name="PROD_SPNSH_DES")
	@Type(type="fixedLengthChar")
	private String spanishDescription;

	@Column(name = "TAG_ITM_ID")
	private Long tagItemCode;

	@Column(name="TAG_ITM_KEY_TYP_CD")
	private String tagItemType;

	@Column(name = "PROD_SZ_TXT")
	private String productSizeText;

	@Column(name = "pss_dept_1")
	private Integer pssDepartmentOne;

	@Id
	@Column(name="PROD_ID")
	private Long prodId;

	@Column(name = "PROD_TYP_CD", nullable = false)
	@Type(type="fixedLengthChar")
	private String prodTypCd;

	@JsonIgnoreProperties("productMaster")
	@OneToMany(mappedBy = "productMaster", fetch = FetchType.LAZY)
	private List<ProdItem> prodItems;

	@JsonIgnoreProperties({"productMaster","associateUpcs"})
	@OneToMany(mappedBy = "productMaster", fetch = FetchType.LAZY)
	private List<SellingUnit> sellingUnits;

	@Column(name="RETL_LINK_CD")
	private long retailLink;

	@Column(name = "prod_brnd_id")
	private long prodBrandId;

	@Column(name = "rc_im_qty_req_flag")
	private String quantityRequiredFlag;

	@Column(name ="rc_im_prc_req_flag")
	private Boolean priceRequiredFlag;

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

	@JsonIgnoreProperties({"productMaster","associateUpcs"})
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_PRIM_SCN_ID", referencedColumnName = "scn_cd_id", insertable = false, updatable = false, nullable = false)
	private SellingUnit productPrimarySellingUnit;


	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
	private List<ProductDescription> productDescriptions;

	@Column(name = "SHOW_CLRS_SW")
	private Boolean showCalories;

	@OneToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
	private GoodsProduct goodsProduct;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_temp_cntl_cd", referencedColumnName = "prod_temp_cntl_cd", insertable = false, updatable = false)
	private ProductTemperatureControl temperatureControl;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false)
	private List<ProductMarketingClaim> productMarketingClaims;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false, nullable = false)
	@OrderBy("seq_nbr desc")
	private List<ProductCubiscan> productCubiscan;

	@ManyToOne(fetch = FetchType.LAZY)
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

	@Where(clause = "dta_src_sys = 4 and itm_prod_key_cd = 'PROD'")
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private List<MasterDataExtensionAttribute> masterDataExtensionAttributes;

	@Where(clause = "itm_prod_key_cd = 'PROD'")
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private List<MasterDataExtensionAttribute> exportMasterDataExtensionAttributes;

	@Where(clause = "itm_prod_key_cd = 'UPC'")
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_id", referencedColumnName = "PROD_PRIM_SCN_ID", insertable = false, updatable = false)
	private List<MasterDataExtensionAttribute> exportUpcMasterDataExtensionAttributes;
	
	@Where(clause = "enty_typ_cd = 'PROD'")
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "enty_dsply_nbr", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private List<GenericEntity> genericEntities;

	@Where(clause = "itm_prod_key_cd = 'PROD'")
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private List<ProductAttributeOverwrite> productAttributeOverwrites;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "prod_prim_scn_id", insertable = false, updatable = false)
	private List<ProductScanCodeExtent> productScanCodeExtents;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_PRIM_SCN_ID", referencedColumnName = "upc_key", insertable = false, updatable = false)
	private ExportScaleUpc exportScaleUpc;

	@Column(name="subscr_prod_sw")
	private boolean subscription;

	@Column(name="subscr_strt_dt")
	private LocalDate subscriptionStartDate;

	@Column(name="subscr_end_dt")
	private LocalDate subscriptionEndDate;

	@Column(name="cre8_ts")
	private LocalDateTime createDate;

	@Column(name="cre8_ts", insertable = false, updatable = false)
	private LocalDateTime createdDateTime;

	@Column(name = "cre8_uid")
	private String createUid;

	public String getCreateUid() {
		return createUid;
	}

	public void setCreateUid(String createUid) {
		this.createUid=createUid;
	}

	@Column(name = "cre8_uid", insertable = false, updatable = false)
	public String displayCreatedName;

	@Column(name="GIFT_MSG_REQ_SW")
	private Boolean giftMessageSwitch;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false)
	private List<ProductFullfilmentChanel> productFullfilmentChanels;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROD_ID", referencedColumnName = "PROD_ID", insertable = false, updatable = false)
	private List<ProductOnline> productOnlines;
	
	@JsonIgnoreProperties("productMaster")
	@OneToMany(mappedBy = "productMaster", fetch = FetchType.LAZY)
	private List<CandidateWorkRequest> candidateWorkRequests;

	@Transient
	private GenericEntityRelationship primaryCustomerHierarchy;

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
	private ServiceCaseSign serviceCaseSign;

	@Transient
	private PrimoPickProperties primoPickProperties;

	@Transient
	private String primoPickStatus;

	@Transient
	private String primoPickProposedDescription;

	@Transient
	private String primoPickDescription;

	@Transient
	private LocalDate primoPickEffectiveDate;

	@Transient
	private LocalDate primoPickExpirationDate;

	@Transient
	private MasterDataExtensionAttribute pdpTemplate;

	@Transient
	private String  ebmName;

	/**
	 * Get the exportScaleUpc.
	 *
	 * @return the exportScaleUpc
	 */
	public ExportScaleUpc getExportScaleUpc() {
		return exportScaleUpc;
	}

	/**
	 * Set the exportScaleUpc.
	 *
	 * @param exportScaleUpc the exportScaleUpc to set
	 */
	public void setExportScaleUpc(ExportScaleUpc exportScaleUpc) {
		this.exportScaleUpc = exportScaleUpc;
	}
	
	/**
	 * Get the productScanCodeExtents.
	 *
	 * @return the productScanCodeExtents
	 */
	public List<ProductScanCodeExtent> getProductScanCodeExtents() {
		return productScanCodeExtents;
	}

	/**
	 * Set the productScanCodeExtents.
	 *
	 * @param productScanCodeExtents the productScanCodeExtents to set
	 */
	public void setProductScanCodeExtents(List<ProductScanCodeExtent> productScanCodeExtents) {
		this.productScanCodeExtents = productScanCodeExtents;
	}

	/**
	 * Get the productAttributeOverwrites.
	 *
	 * @return the productAttributeOverwrites
	 */
	public List<ProductAttributeOverwrite> getProductAttributeOverwrites() {
		return productAttributeOverwrites;
	}

	/**
	 * Set the productAttributeOverwrites.
	 *
	 * @param productAttributeOverwrites the productAttributeOverwrites to set
	 */
	public void setProductAttributeOverwrites(List<ProductAttributeOverwrite> productAttributeOverwrites) {
		this.productAttributeOverwrites = productAttributeOverwrites;
	}

	/**
	 * Get the genericEntities.
	 *
	 * @return the genericEntities
	 */
	public List<GenericEntity> getGenericEntities() {
		return genericEntities;
	}

	/**
	 * Set the genericEntities.
	 *
	 * @param genericEntities the genericEntities to set
	 */
	public void setGenericEntities(List<GenericEntity> genericEntities) {
		this.genericEntities = genericEntities;
	}

	/**
	 * Get the exportUpcMasterDataExtensionAttributes.
	 *
	 * @return the exportUpcMasterDataExtensionAttributes
	 */
	public List<MasterDataExtensionAttribute> getExportUpcMasterDataExtensionAttributes() {
		return exportUpcMasterDataExtensionAttributes;
	}

	/**
	 * Set the exportUpcMasterDataExtensionAttributes.
	 *
	 * @param exportUpcMasterDataExtensionAttributes the exportUpcMasterDataExtensionAttributes to set
	 */
	public void setExportUpcMasterDataExtensionAttributes(
			List<MasterDataExtensionAttribute> exportUpcMasterDataExtensionAttributes) {
		this.exportUpcMasterDataExtensionAttributes = exportUpcMasterDataExtensionAttributes;
	}

	/**
	 * Get the exportMasterDataExtensionAttributes.
	 *
	 * @return the exportMasterDataExtensionAttributes
	 */
	public List<MasterDataExtensionAttribute> getExportMasterDataExtensionAttributes() {
		return exportMasterDataExtensionAttributes;
	}

	/**
	 * Set the exportMasterDataExtensionAttributes.
	 *
	 * @param exportMasterDataExtensionAttributes the exportMasterDataExtensionAttributes to set
	 */
	public void setExportMasterDataExtensionAttributes(
			List<MasterDataExtensionAttribute> exportMasterDataExtensionAttributes) {
		this.exportMasterDataExtensionAttributes = exportMasterDataExtensionAttributes;
	}

	/**
	 * Get the candidateWorkRequests.
	 *
	 * @return the candidateWorkRequests
	 */
	public List<CandidateWorkRequest> getCandidateWorkRequests() {
		return candidateWorkRequests;
	}

	/**
	 * Set the candidateWorkRequests.
	 *
	 * @param candidateWorkRequests the candidateWorkRequests to set
	 */
	public void setCandidateWorkRequests(List<CandidateWorkRequest> candidateWorkRequests) {
		this.candidateWorkRequests = candidateWorkRequests;
	}

	/**
	 * This method returns the PDP template value
	 * @return
	 */
	public MasterDataExtensionAttribute getPdpTemplate() {
		return pdpTemplate;
	}

	/**
	 * This method sets the PDP template value
	 * @param pdpTemplate
	 */
	public void setPdpTemplate(MasterDataExtensionAttribute pdpTemplate) {
		this.pdpTemplate = pdpTemplate;
	}

	public void setPrimoPickProposedDescription(String primoPickProposedDescription) {
		this.primoPickProposedDescription = primoPickProposedDescription;
	}

	public void setPrimoPickDescription(String primoPickDescription) {
		this.primoPickDescription = primoPickDescription;
	}

	public LocalDate getPrimoPickEffectiveDate() {
		return primoPickEffectiveDate;
	}

	public void setPrimoPickEffectiveDate(LocalDate primoPickEffectiveDate) {
		this.primoPickEffectiveDate = primoPickEffectiveDate;
	}

	public LocalDate getPrimoPickExpirationDate() {
		return primoPickExpirationDate;
	}

	public void setPrimoPickExpirationDate(LocalDate primoPickExpirationDate) {
		this.primoPickExpirationDate = primoPickExpirationDate;
	}

	@JsonIgnoreProperties("productMaster")
	@OneToMany(mappedBy = "productMaster", fetch = FetchType.LAZY)
	private List<ProductItemVariant> lstProductItemVariant;

	/**
	 * get list product item variant
	 * @return
	 */
	public List<ProductItemVariant> getLstProductItemVariant() {
		return lstProductItemVariant;
	}

	/**
	 * set list product item variant
	 * @return
	 */
	public void setLstProductItemVariant(List<ProductItemVariant> lstProductItemVariant) {
		this.lstProductItemVariant = lstProductItemVariant;
	}

	public ProductTemperatureControl getTemperatureControl() {
		return temperatureControl;
	}

	public void setTemperatureControl(ProductTemperatureControl temperatureControl) {
		this.temperatureControl = temperatureControl;
	}

	@Transient
	private String actionCode;

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
	 * Gets product cubiscan measurements.
	 *
	 * @return the product cubiscan measurements from the cubiscan measuring device.
	 */
	public List<ProductCubiscan> getProductCubiscan() {
		return productCubiscan;
	}

	/**
	 * Sets product cubiscan measurements.
	 *
	 * @param productCubiscan the product cubiscan measurements from the cubiscan measuring device.
	 */
	public void setProductCubiscan(List<ProductCubiscan> productCubiscan) {
		this.productCubiscan = productCubiscan;
	}

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

	/**
	 * Returns the ProdItem records associated with this item.
	 *
	 * @return The ProdItem records associated with this item.
	 */
	public List<ProdItem> getProdItems() {
		if (prodItems == null) {
			return new ArrayList<>();
		} else {
			return prodItems;
		}
	}

	/**
	 * Sets the prodItems records associated with this item.
	 *
	 * @param prodItems The prodItems records associated with this item.
	 */
	public void setProdItems(List<ProdItem> prodItems) {
		this.prodItems = prodItems;
	}

	/**
	 * Returns the prodId  associated with this item.
	 *
	 * @return The prodId associated with this item.
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * Sets the prodId associated with this item.
	 *
	 * @param prodId The prodId associated with this item.
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
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

	/**
	 * Returns the product master's selling unit.
	 *
	 * @return The product master's selling unit.
	 */
	public List<SellingUnit> getSellingUnits() {
		return sellingUnits;
	}

	/**
	 * Sets the product master's selling unit.
	 *
	 * @param sellingUnits The product master's selling unit.
	 */
	public void setSellingUnits(List<SellingUnit> sellingUnits) {
		this.sellingUnits = sellingUnits;
	}

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

	/**
	 * Get the list of customer friendly product descriptions
	 *
	 * @return the list of customer friendly product descriptions
	 */
	public List<ProductDescription> getProductDescriptions() {
		if (this.productDescriptions == null) {
			this.productDescriptions = new LinkedList<>();
		}
		return productDescriptions;
	}

	/**
	 * Update the list of product descriptions
	 *
	 * @param productDescriptions the new list of product descriptions.
	 */
	public void setProductDescriptions(List<ProductDescription> productDescriptions) {
		this.productDescriptions = productDescriptions;
	}

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
		return this.spanishDescription;
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

	/**
	 * Determines whether the product is DSV or not.
	 * @return whether the product is DSV or not.
	 */
	public Boolean isDSV() {
		if(this.getProdItems() == null) {
			return null;
		}
		for(ProdItem prodItem : this.getProdItems()) {
			if (prodItem.getItemMaster().isDsv().trim().equals(YES)) {
				return true;
			}
		}
		return false;
	}

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
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the date this product was created.
	 *
	 * @param createDate The date this product was created.
	 */
	public void setCreateDate(LocalDateTime createDate) {
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

	/**
	 * Returns the proposed primo pick description of this product (if set).
	 *
	 * @return The proposed primo pick description of this product (Null if it is not set)
	 */
	public String getPrimoPickProposedDescription() {
		this.loadDescriptions();
		return this.primoPickProposedDescription;
	}

	/**
	 * Returns the primo pick description of this product (if set).
	 *
	 * @return The primo pick description of this product (Null if not set).
	 */
	public String getPrimoPickDescription() {
		this.loadDescriptions();
		return this.primoPickDescription;
	}

	/**
	 * Loads all the descriptions that are tied to the prod_des_txt table.
	 */
	private void loadDescriptions() {
		if (this.productDescriptions == null || this.descriptionsSet) {
			return;
		}

		for (ProductDescription description : this.productDescriptions) {
			switch (description.getKey().getDescriptionType()) {
				case DescriptionType.PRIMO_PICK_LONG_DESCRIPTION:
					this.primoPickProposedDescription = description.getDescription();
					break;
				case DescriptionType.PRIMO_PICK_SHORT_DESCRIPTION:
					this.primoPickDescription = description.getDescription();
			}
		}

		this.descriptionsSet = true;
	}
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
	 * Returns list of product fulfillment channel of the product.
	 * @return list of fulfillment channels.
	 */
	public List<ProductFullfilmentChanel> getProductFullfilmentChanels() {
		return productFullfilmentChanels;
	}

	/**
	 * Sets fulfillment channel of the product.
	 * @param productFullfilmentChanels
	 */
	public void setProductFullfilmentChanels(List<ProductFullfilmentChanel> productFullfilmentChanels) {
		this.productFullfilmentChanels = productFullfilmentChanels;
	}

	/**
	 * Returns list of product online records (show on site) with sales channel info.
	 * @return list of product online.
	 */
	public List<ProductOnline> getProductOnlines() {
		return productOnlines;
	}

	/**
	 * Sets list of product online of the product.
	 * @param productOnlines
	 */
	public void setProductOnlines(List<ProductOnline> productOnlines) {
		this.productOnlines = productOnlines;
	}

	/**
	 * Returns primary customer hierarchy info.
	 * @return customer hierarchy (product to hierarchy relationship)
	 */
	public GenericEntityRelationship getPrimaryCustomerHierarchy() {
		return primaryCustomerHierarchy;
	}

	/**
	 * Sets customer hierarhcy of a product as generic entity relationship.
	 * @param primaryCustomerHierarchy
	 */
	public void setPrimaryCustomerHierarchy(GenericEntityRelationship primaryCustomerHierarchy) {
		this.primaryCustomerHierarchy = primaryCustomerHierarchy;
	}

	public String getPrimoPickStatus() {
		return primoPickStatus;
	}

	public void setPrimoPickStatus(String primoPickStatus) {
		this.primoPickStatus = primoPickStatus;
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
		if (!(o instanceof ProductMaster)) return false;
		ProductMaster that = (ProductMaster) o;
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
		return (int) (prodId ^ (prodId >>> FOUR_BYTES));
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductMaster{" +
				"description='" + description + '\'' +
				", prodId=" + prodId +
				'}';
	}

	/**
	 * Returns the default sort order for the prod_del table.
	 *
	 * @return The default sort order for the prod_del table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, ProductMaster.PRODUCT_ID_SORT_FIELD)
		);
	}

	/**
	 * Returns data related to the service case sign description for this product. It is used as a way
	 * to transfer data from the front-end to the back-end for processing.
	 *
	 * @return Data related to the service case sign description for this product.
	 */
	public ServiceCaseSign getServiceCaseSign() {
		return serviceCaseSign;
	}

	/**
	 * Sets the data related to the service case sign description for this product.
	 * @param serviceCaseSign Data related to the service case sign description for this product.
	 */
	public void setServiceCaseSign(ServiceCaseSign serviceCaseSign) {
		this.serviceCaseSign = serviceCaseSign;
	}

	/**
	 * Inner class used to pass primo-pick data related to this product back and forth between the front
	 * and back-end.
	 */
	public class PrimoPickProperties {
		private Boolean distinctive;
		private String primoPickStatus;
		private String primoPickProposedDescription;
		private String primoPickDescription;
		private LocalDate primoPickEffectiveDate;
		private LocalDate primoPickExpirationDate;
		private String statusChangeReason;

		/**
		 * Returns whether or not this product is distinctive.
		 *
		 * @return Whether or not this product is distinctive.
		 */
		public Boolean getDistinctive() {
			return distinctive;
		}

		/**
		 * Sets whether or not this product is distinctive.
		 *
		 * @param distinctive Whether or not this product is distinctive.
		 */
		public void setDistinctive(Boolean distinctive) {
			this.distinctive = distinctive;
		}

		/**
		 * Returns the primo-pick status of this product.
		 *
		 * @return The primo-pick status of this product.
		 */
		public String getPrimoPickStatus() {
			return primoPickStatus;
		}

		/**
		 * Sets the primo-pick status of this product.
		 *
		 * @param primoPickStatus The primo-pick status of this product.
		 */
		public void setPrimoPickStatus(String primoPickStatus) {
			this.primoPickStatus = primoPickStatus;
		}

		/**
		 * Returns the proposed primo-pick description of this product.
		 *
		 * @return The proposed primo-pick description of this product.
		 */
		public String getPrimoPickProposedDescription() {
			return primoPickProposedDescription;
		}

		/**
		 * Sets the proposed primo-pick description of this product.
		 *
		 * @param primoPickProposedDescription The proposed primo-pick description of this product.
		 */
		public void setPrimoPickProposedDescription(String primoPickProposedDescription) {
			this.primoPickProposedDescription = primoPickProposedDescription;
		}

		/**
		 * Returns the status change reason.
		 *
		 * @return the status change reason.
		 */
		public String getStatusChangeReason() {
			return statusChangeReason;
		}

		/**
		 * Sets the status change reason.
		 *
		 * @param statusChangeReason the status change reason.
		 */
		public void setStatusChangeReason(String statusChangeReason) {
			this.statusChangeReason = statusChangeReason;
		}
		/**
		 * Returns the approved primo-pick description of this product.
		 *
		 * @return The approved primo-pick description of this product.
		 */
		public String getPrimoPickDescription() {
			return primoPickDescription;
		}

		/**
		 * Sets the approved primo-pick description of this product.
		 *
		 * @param primoPickDescription The approved primo-pick description of this product.
		 */
		public void setPrimoPickDescription(String primoPickDescription) {
			this.primoPickDescription = primoPickDescription;
		}

		/**
		 * Returns the effective date of the primo-pick.
		 *
		 * @return The effective date of the primo-pick.
		 */
		public LocalDate getPrimoPickEffectiveDate() {
			return primoPickEffectiveDate;
		}

		/**
		 * Sets the effective date of the primo-pick.
		 *
		 * @param primoPickEffectiveDate The effective date of the primo-pick.
		 */
		public void setPrimoPickEffectiveDate(LocalDate primoPickEffectiveDate) {
			this.primoPickEffectiveDate = primoPickEffectiveDate;
		}

		/**
		 * Returns the end date of the primo-pick.
		 *
		 * @return The end date of the primo-pick.
		 */
		public LocalDate getPrimoPickExpirationDate() {
			return primoPickExpirationDate;
		}

		/**
		 * Sets the end date of the primo-pick.
		 *
		 * @param primoPickExpirationDate The end date of the primo-pick.
		 */
		public void setPrimoPickExpirationDate(LocalDate primoPickExpirationDate) {
			this.primoPickExpirationDate = primoPickExpirationDate;
		}
	}

	/**
	 * Returns the primo-pick properties for this product. This is used to pass data to update from
	 * the front-end to the back end.
	 *
	 * @return The primo-pick properties for this product.
	 */
	public PrimoPickProperties getPrimoPickProperties() {
		return primoPickProperties;
	}

	/**
	 * Sets the primo-pick properties for this product.
	 *
	 * @param primoPickProperties The primo-pick properties for this product.
	 */
	public void setPrimoPickProperties(PrimoPickProperties primoPickProperties) {
		this.primoPickProperties = primoPickProperties;
	}

	public String getFulfillmentProgramString(){
		StringBuilder fulfillmentProgramList = new StringBuilder("");
		if (this.productFullfilmentChanels == null || this.productFullfilmentChanels.isEmpty()) {
			return fulfillmentProgramList.toString();
		}

		fulfillmentProgramList.append(this.productFullfilmentChanels.get(0).getFulfillmentChannel().getDescription().trim());
		for(int index = 1; index>this.productFullfilmentChanels.size(); index++){
			fulfillmentProgramList.append(", ").append(this.productFullfilmentChanels.get(index).getFulfillmentChannel().getDescription().trim());
		}
		return fulfillmentProgramList.toString();
	}

	public String getPdpTemplateString(){

		if (this.masterDataExtensionAttributes == null) {
			return "";
		}
		for (MasterDataExtensionAttribute masterDataExtensionAttribute: this.masterDataExtensionAttributes){
			if(masterDataExtensionAttribute.getKey().getAttributeId() == PDP_TEMPLATE_CODE){
				return masterDataExtensionAttribute.getAttributeValueText();
			}
		}
		return "";
	}

	/**
	 * Returns the name of the eBM.
	 *
	 * @return The name of the eBM.
	 */
	public String getEbmName() {
		return ebmName;
	}

	/**
	 * Sets the name of the eBM.
	 *
	 * @param ebmName The name of the eBM.
	 */
	public void setEbmName(String ebmName) {
		this.ebmName = ebmName;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime=createdDateTime;
	}

	public String getDisplayCreatedName() {
		return displayCreatedName;
	}

	public void setDisplayCreatedName(String displayCreatedName) {
		this.displayCreatedName=displayCreatedName;
	}
}
