/*
 * ItemClass
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents item class in the product hierarchy (in between sub-department and class-commodity).
 *
 * @author m314029
 * @since 2.4.0
 */
@Entity
@Table(name = "itm_cls")
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ItemClass implements Serializable {

	// default constructor
	public ItemClass(){super();}

	// copy constructor
	// this does not call 'setSubDepartmentMaster' or 'setCommodityList' to prevent infinite loops -- if these methods
	// need to be called, they need to be done after creating all of the objects
	public ItemClass(ItemClass itemClass){
		super();
		this.setItemClassCode(itemClass.getItemClassCode());
		this.setDepartmentId(itemClass.getDepartmentId());
		this.setSubDepartmentId(itemClass.getSubDepartmentId());
		this.setItemClassDescription(itemClass.getItemClassDescription());
		this.setBeginAdLeadTime(itemClass.getBeginAdLeadTime());
		this.setEndAdLeadTime(itemClass.getEndAdLeadTime());
		this.setScanDepartment(itemClass.getScanDepartment());
		this.setPriceBulletin(itemClass.getPriceBulletin());
		this.setBillCostEligible(itemClass.getBillCostEligible());
		this.setLowGrossMargin(itemClass.getLowGrossMargin());
		this.setHiGrossMargin(itemClass.getHiGrossMargin());
		this.setMerchantTypeCode(itemClass.getMerchantTypeCode());
		this.setDataCheckerDepartment(itemClass.getDataCheckerDepartment());
		this.setMixMatchGroup(itemClass.getMixMatchGroup());
		this.setPriceChangePercent(itemClass.getPriceChangePercent());
		this.setClassVariance(itemClass.getClassVariance());
		this.setBillAtAd(itemClass.getBillAtAd());
		this.setScanMaintenanceGroup(itemClass.getScanMaintenanceGroup());
		this.setAdPriceChangePercent(itemClass.getAdPriceChangePercent());
		this.setClassDirectorId(itemClass.getClassDirectorId());
		this.setGenericClass(itemClass.getGenericClass());
		this.setTemperatureControlCode(itemClass.getTemperatureControlCode());
		this.setTemperatureControl(itemClass.getTemperatureControl() != null ?
				new ProductTemperatureControl(itemClass.getTemperatureControl()) : null);
		this.setMerchantType(itemClass.getMerchantType() != null ?
				new MerchantType(itemClass.getMerchantType()) : null);
	}

	private static final long serialVersionUID = 1L;
	/**
	 * The constant DISPLAY_NAME_FORMAT.
	 */
	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@Id
	@Column(name="item_cls_code")
	private Integer itemClassCode;

	@Column(name="dept_id")
	private Integer departmentId;

	@Column(name="sub_dept_id")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String subDepartmentId;

	@Column(name="itm_cls_des")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String itemClassDescription;

	@Column(name="beg_ad_lead_tm_dd")
	private Integer beginAdLeadTime;

	@Column(name="end_ad_lead_tm_dd")
	private Integer endAdLeadTime;

	@Column(name="scn_dept")
	private Integer scanDepartment;

	@Column(name="prc_bul_cd")
	private String priceBulletin;

	@Column(name="bil_cst_elig_sw")
	private Boolean billCostEligible;

	@Column(name="cls_lo_grmgn")
	private Double lowGrossMargin;

	@Column(name="cls_hi_grmgn")
	private Double hiGrossMargin;

	@Column(name="merch_typ_cd")
	private String merchantTypeCode;

	@Column(name="dta_chkr_dept")
	private Integer dataCheckerDepartment;

	@Column(name="mix_mat_grp")
	private Integer mixMatchGroup;

	@Column(name="prc_chg_pct")
	private Integer priceChangePercent;

	@Column(name="cls_var_amt")
	private Double classVariance;

	@Column(name="bil_at_ad_sw")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String billAtAd;

	@Column(name="scn_maint_grp")
	private Integer scanMaintenanceGroup;

	@Column(name="ad_prc_chg_pct")
	private Double adPriceChangePercent;

	@Column(name="cls_director_id")
	private Integer classDirectorId;

	@Column(name="gnrc_cls")
	private Integer genericClass;

	@Column(name="prod_temp_cntl_cd")
	private String temperatureControlCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_temp_cntl_cd", referencedColumnName = "prod_temp_cntl_cd", insertable = false, updatable = false)
	private ProductTemperatureControl temperatureControl;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "merch_typ_cd", referencedColumnName = "merch_typ_cd", insertable = false, updatable = false)
	private MerchantType merchantType;

	@JsonIgnoreProperties("itemClassMaster")
	@Where(clause = "pd_omi_com_cd != 0")
	@OneToMany(mappedBy = "itemClassMaster", fetch = FetchType.LAZY)
	private List<ClassCommodity> commodityList;

	@Transient
	@JsonIgnoreProperties("itemClasses")
	private SubDepartment subDepartmentMaster;

	/**
	 * Returns the MerchantType
	 *
	 * @return MerchantType
	 */
	public MerchantType getMerchantType() {
		return merchantType;
	}

	/**
	 * Sets the MerchantType
	 *
	 * @param merchantType The MerchantType
	 */
	public void setMerchantType(MerchantType merchantType) {
		this.merchantType = merchantType;
	}

	/**
	 * Returns SubDepartment.
	 *
	 * @return The SubDepartment.
	 **/
	public SubDepartment getSubDepartmentMaster() {
		return subDepartmentMaster;
	}

	/**
	 * Sets the SubDepartment.
	 *
	 * @param subDepartmentMaster The SubDepartment.
	 **/
	public void setSubDepartmentMaster(SubDepartment subDepartmentMaster) {
		this.subDepartmentMaster = subDepartmentMaster;
	}

	/**
	 * Gets item class code.
	 *
	 * @return the item class code
	 */
	public Integer getItemClassCode() {
		return itemClassCode;
	}

	/**
	 * Sets item class code.
	 *
	 * @param itemClassCode the item class code
	 */
	public void setItemClassCode(Integer itemClassCode) {
		this.itemClassCode = itemClassCode;
	}

	/**
	 * Gets department id.
	 *
	 * @return the department id
	 */
	public Integer getDepartmentId() {
		return departmentId;
	}

	/**
	 * Sets department id.
	 *
	 * @param departmentId the department id
	 */
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * Gets sub department id.
	 *
	 * @return the sub department id
	 */
	public String getSubDepartmentId() {
		return subDepartmentId;
	}

	/**
	 * Sets sub department id.
	 *
	 * @param subDepartmentId the sub department id
	 */
	public void setSubDepartmentId(String subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	/**
	 * Gets item class description.
	 *
	 * @return the item class description
	 */
	public String getItemClassDescription() {
		return itemClassDescription;
	}

	/**
	 * Sets item class description.
	 *
	 * @param itemClassDescription the item class description
	 */
	public void setItemClassDescription(String itemClassDescription) {
		this.itemClassDescription = itemClassDescription;
	}

	/**
	 * Returns BeginAdLeadTime.
	 *
	 * @return The BeginAdLeadTime.
	 **/
	public Integer getBeginAdLeadTime() {
		return beginAdLeadTime;
	}

	/**
	 * Sets the BeginAdLeadTime.
	 *
	 * @param beginAdLeadTime The BeginAdLeadTime.
	 **/
	public void setBeginAdLeadTime(Integer beginAdLeadTime) {
		this.beginAdLeadTime = beginAdLeadTime;
	}

	/**
	 * Returns EndAdLeadTime.
	 *
	 * @return The EndAdLeadTime.
	 **/
	public Integer getEndAdLeadTime() {
		return endAdLeadTime;
	}

	/**
	 * Sets the EndAdLeadTime.
	 *
	 * @param endAdLeadTime The EndAdLeadTime.
	 **/
	public void setEndAdLeadTime(Integer endAdLeadTime) {
		this.endAdLeadTime = endAdLeadTime;
	}

	/**
	 * Returns ScanDepartment.
	 *
	 * @return The ScanDepartment.
	 **/
	public Integer getScanDepartment() {
		return scanDepartment;
	}

	/**
	 * Sets the ScanDepartment.
	 *
	 * @param scanDepartment The ScanDepartment.
	 **/
	public void setScanDepartment(Integer scanDepartment) {
		this.scanDepartment = scanDepartment;
	}

	/**
	 * Returns PriceBulletin.
	 *
	 * @return The PriceBulletin.
	 **/
	public String getPriceBulletin() {
		return priceBulletin;
	}

	/**
	 * Sets the PriceBulletin.
	 *
	 * @param priceBulletin The PriceBulletin.
	 **/
	public void setPriceBulletin(String priceBulletin) {
		this.priceBulletin = priceBulletin;
	}

	/**
	 * Returns BillCostEligible.
	 *
	 * @return The BillCostEligible.
	 **/
	public Boolean getBillCostEligible() {
		return billCostEligible;
	}

	/**
	 * Sets the BillCostEligible.
	 *
	 * @param billCostEligible The BillCostEligible.
	 **/
	public void setBillCostEligible(Boolean billCostEligible) {
		this.billCostEligible = billCostEligible;
	}

	/**
	 * Returns LowGrossMargin.
	 *
	 * @return The LowGrossMargin.
	 **/
	public Double getLowGrossMargin() {
		return lowGrossMargin;
	}

	/**
	 * Sets the LowGrossMargin.
	 *
	 * @param lowGrossMargin The LowGrossMargin.
	 **/
	public void setLowGrossMargin(Double lowGrossMargin) {
		this.lowGrossMargin = lowGrossMargin;
	}

	/**
	 * Returns HiGrossMargin.
	 *
	 * @return The HiGrossMargin.
	 **/
	public Double getHiGrossMargin() {
		return hiGrossMargin;
	}

	/**
	 * Sets the HiGrossMargin.
	 *
	 * @param hiGrossMargin The HiGrossMargin.
	 **/
	public void setHiGrossMargin(Double hiGrossMargin) {
		this.hiGrossMargin = hiGrossMargin;
	}

	/**
	 * Returns MerchandiseTypeCode.
	 *
	 * @return The MerchandiseTypeCode.
	 **/
	public String getMerchantTypeCode() {
		return merchantTypeCode;
	}

	/**
	 * Sets the MerchandiseTypeCode.
	 *
	 * @param merchantTypeCode The MerchandiseTypeCode.
	 **/
	public void setMerchantTypeCode(String merchantTypeCode) {
		this.merchantTypeCode = merchantTypeCode;
	}

	/**
	 * Returns DataCheckerDepartment.
	 *
	 * @return The DataCheckerDepartment.
	 **/
	public Integer getDataCheckerDepartment() {
		return dataCheckerDepartment;
	}

	/**
	 * Sets the DataCheckerDepartment.
	 *
	 * @param dataCheckerDepartment The DataCheckerDepartment.
	 **/
	public void setDataCheckerDepartment(Integer dataCheckerDepartment) {
		this.dataCheckerDepartment = dataCheckerDepartment;
	}

	/**
	 * Returns MixMatchGroup.
	 *
	 * @return The MixMatchGroup.
	 **/
	public Integer getMixMatchGroup() {
		return mixMatchGroup;
	}

	/**
	 * Sets the MixMatchGroup.
	 *
	 * @param mixMatchGroup The MixMatchGroup.
	 **/
	public void setMixMatchGroup(Integer mixMatchGroup) {
		this.mixMatchGroup = mixMatchGroup;
	}

	/**
	 * Returns PriceChangePercent.
	 *
	 * @return The PriceChangePercent.
	 **/
	public Integer getPriceChangePercent() {
		return priceChangePercent;
	}

	/**
	 * Sets the PriceChangePercent.
	 *
	 * @param priceChangePercent The PriceChangePercent.
	 **/
	public void setPriceChangePercent(Integer priceChangePercent) {
		this.priceChangePercent = priceChangePercent;
	}

	/**
	 * Returns ClassVariance.
	 *
	 * @return The ClassVariance.
	 **/
	public Double getClassVariance() {
		return classVariance;
	}

	/**
	 * Sets the ClassVariance.
	 *
	 * @param classVariance The ClassVariance.
	 **/
	public void setClassVariance(Double classVariance) {
		this.classVariance = classVariance;
	}

	/**
	 * Returns BillAtAd.
	 *
	 * @return The BillAtAd.
	 **/
	public String getBillAtAd() {
		return billAtAd;
	}

	/**
	 * Sets the BillAtAd.
	 *
	 * @param billAtAd The BillAtAd.
	 **/
	public void setBillAtAd(String billAtAd) {
		this.billAtAd = billAtAd;
	}

	/**
	 * Returns ScanMaintenanceGroup.
	 *
	 * @return The ScanMaintenanceGroup.
	 **/
	public Integer getScanMaintenanceGroup() {
		return scanMaintenanceGroup;
	}

	/**
	 * Sets the ScanMaintenanceGroup.
	 *
	 * @param scanMaintenanceGroup The ScanMaintenanceGroup.
	 **/
	public void setScanMaintenanceGroup(Integer scanMaintenanceGroup) {
		this.scanMaintenanceGroup = scanMaintenanceGroup;
	}

	/**
	 * Returns AdPriceChangePercent.
	 *
	 * @return The AdPriceChangePercent.
	 **/
	public Double getAdPriceChangePercent() {
		return adPriceChangePercent;
	}

	/**
	 * Sets the AdPriceChangePercent.
	 *
	 * @param adPriceChangePercent The AdPriceChangePercent.
	 **/
	public void setAdPriceChangePercent(Double adPriceChangePercent) {
		this.adPriceChangePercent = adPriceChangePercent;
	}

	/**
	 * Returns ClassDirectorId.
	 *
	 * @return The ClassDirectorId.
	 **/
	public Integer getClassDirectorId() {
		return classDirectorId;
	}

	/**
	 * Sets the ClassDirectorId.
	 *
	 * @param classDirectorId The ClassDirectorId.
	 **/
	public void setClassDirectorId(Integer classDirectorId) {
		this.classDirectorId = classDirectorId;
	}

	/**
	 * Returns GenericClass.
	 *
	 * @return The GenericClass.
	 **/
	public Integer getGenericClass() {
		return genericClass;
	}

	/**
	 * Sets the GenericClass.
	 *
	 * @param genericClass The GenericClass.
	 **/
	public void setGenericClass(Integer genericClass) {
		this.genericClass = genericClass;
	}

	/**
	 * Returns TemperatureControlCode.
	 *
	 * @return The TemperatureControlCode.
	 **/
	public String getTemperatureControlCode() {
		return temperatureControlCode;
	}

	/**
	 * Sets the TemperatureControlCode.
	 *
	 * @param temperatureControlCode The TemperatureControlCode.
	 **/
	public void setTemperatureControlCode(String temperatureControlCode) {
		this.temperatureControlCode = temperatureControlCode;
	}

	/**
	 * Returns TemperatureControl.
	 *
	 * @return The TemperatureControl.
	 **/
	public ProductTemperatureControl getTemperatureControl() {
		return temperatureControl;
	}

	/**
	 * Sets the TemperatureControl.
	 *
	 * @param temperatureControl The TemperatureControl.
	 **/
	public void setTemperatureControl(ProductTemperatureControl temperatureControl) {
		this.temperatureControl = temperatureControl;
	}

	/**
	 * Returns the sub-department as it should be displayed on the GUI.
	 *
	 * @return A String representation of the sub-department as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {

		return String.format(ItemClass.DISPLAY_NAME_FORMAT,
				this.itemClassDescription.trim(), this.getItemClassCode());
	}

	/**
	 * Returns a unique ID for this sub-department as a string. This can be used in things like lists where
	 * you need a unique key and cannot access the components of the key directly.
	 *
	 * @return A unique ID for this sub-department as a string
	 */
	public Integer getNormalizedId() {
		return this.getItemClassCode();
	}

	/**
	 * Returns CommodityList.
	 *
	 * @return The CommodityList.
	 **/
	public List<ClassCommodity> getCommodityList() {
		if(commodityList == null){
			commodityList = new ArrayList<>();
		}
		return commodityList;
	}

	/**
	 * Sets the CommodityList.
	 *
	 * @param commodityList The CommodityList.
	 **/
	public void setCommodityList(List<ClassCommodity> commodityList) {
		this.commodityList = commodityList;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemClass itemClass = (ItemClass) o;

		return this.getItemClassCode() != null ? this.getItemClassCode().equals(itemClass.getItemClassCode()) : itemClass.getItemClassCode() == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.getItemClassCode() != null ? this.getItemClassCode().hashCode() : 0;
	}


	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ItemClass{" +
				"itemClassCode=" + itemClassCode +
				", departmentId=" + departmentId +
				", subDepartmentId='" + subDepartmentId + '\'' +
				", itemClassDescription='" + itemClassDescription + '\'' +
				", beginAdLeadTime=" + beginAdLeadTime +
				", endAdLeadTime=" + endAdLeadTime +
				", scanDepartment=" + scanDepartment +
				", priceBulletin=" + priceBulletin +
				", billCostEligible=" + billCostEligible +
				", lowGrossMargin=" + lowGrossMargin +
				", hiGrossMargin=" + hiGrossMargin +
				", merchantTypeCode=" + merchantTypeCode +
				", dataCheckerDepartment=" + dataCheckerDepartment +
				", mixMatchGroup=" + mixMatchGroup +
				", priceChangePercent=" + priceChangePercent +
				", classVariance=" + classVariance +
				", billAtAd=" + billAtAd +
				", scanMaintenanceGroup=" + scanMaintenanceGroup +
				", adPriceChangePercent=" + adPriceChangePercent +
				", classDirectorId=" + classDirectorId +
				", genericClass=" + genericClass +
				", temperatureControlCode='" + temperatureControlCode + '\'' +
				", temperatureControl=" + temperatureControl +
				'}';
	}
}
