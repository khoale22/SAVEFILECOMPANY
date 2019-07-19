/*
 * ClassCommodity
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a record in the pd_class_commodity table.
 *
 * @author d116773
 * @since 2.0.2
 */
@Entity
@Table(name="pd_class_commodity")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ClassCommodity implements Serializable {

	// default constructor
	public ClassCommodity(){super();}

	// copy constructor
	// this does not call 'setItemClassMaster' or 'setSubCommodityList' to prevent infinite loops -- if these methods
	// need to be called, they need to be done after creating all of the objects
	public ClassCommodity(ClassCommodity commodity){
		super();
		this.setKey(new ClassCommodityKey(commodity.getKey()));
		this.setClassCommodityActive(commodity.getClassCommodityActive());
		this.setbDAid(commodity.getbDAid());
		this.setBdmCode(commodity.getBdmCode());
		this.seteBMid(commodity.geteBMid());
		this.setImsCommunicationCode(commodity.getImsCommunicationCode());
		this.setName(commodity.getName());
		this.setMaxCustomerOrderQuantity(commodity.getMaxCustomerOrderQuantity());
		this.setOmiDirector(commodity.getOmiDirector());
		this.setPdpTemplateCode(commodity.getPdpTemplateCode());
		this.setPssDepartment(commodity.getPssDepartment());
		this.setBdm(commodity.getBdm() != null ? new Bdm(commodity.getBdm()) : null);
		this.setPdpTemplate(commodity.getPdpTemplate() != null ? new PDPTemplate(commodity.getPdpTemplate()) : null);
	}

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@EmbeddedId
	private ClassCommodityKey key;

	@Column(name="pd_omi_com_des")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String name;

	@Column(name="bdm_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String bdmCode;

	@Formula("TRIM(bdm_cd)")
	private String trimmedBdmCode;

	@Column(name="ims_com_cd")
	private Integer imsCommunicationCode;

	@Column(name="pd_pss_dept_no")
	private Integer pssDepartment;

	@Column(name="pd_omi_dir_cd")
	private Integer omiDirector;

	@Column(name="pc_cls_com_actv_cd")
	private Character classCommodityActive;

	@Column(name="ecomm_bus_mgr_id")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String eBMid;

	@Column(name="bda_uid")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String bDAid;

	@Column(name="dflt_tmplt_id")
	private String pdpTemplateCode;

	@Column(name="max_cust_ord_qty")
	private Integer maxCustomerOrderQuantity;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="bdm_cd", referencedColumnName = "bdm_cd", insertable = false, updatable = false)
	private Bdm bdm;

	@JsonIgnoreProperties("commodityList")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_omi_com_cls_cd", referencedColumnName = "item_cls_code", insertable = false, updatable = false)
	private ItemClass itemClassMaster;

	@JsonIgnoreProperties("commodityMaster")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "commodityMaster")
	private List<SubCommodity> subCommodityList;

	@OneToOne
	@JoinColumn(name = "dflt_tmplt_id", referencedColumnName = "tmplt_id", insertable = false, updatable = false)
	private PDPTemplate pdpTemplate;

	// These are required to support dynamic search capability and is not used outside that. Therefore, there are no
	// getters or setters on this.

	@Column(name="pd_omi_com_cls_cd", insertable = false, updatable = false)
	private Integer classCode;

	@Column(name="pd_omi_com_cd", insertable = false, updatable = false)
	private Integer commodityCode;

	@OneToMany(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "bdm_cd", referencedColumnName = "bdm_cd", insertable = false, updatable = false)
	private List<SearchCriteria> searchCriteria;


	/**
	 * Returns the BdmCode has been removed spaces.
	 * @return The BdmCode has been removed spaces for this object.
	 */
	public String getTrimmedBdmCode() {
		return trimmedBdmCode;
	}

	/**
	 * Sets the trimmedBdmCode.
	 *
	 * @param trimmedBdmCode The ItemClassMaster.
	 **/
	public void setTrimmedBdmCode(String trimmedBdmCode) {
		this.trimmedBdmCode = trimmedBdmCode;
	}
	/**
	 * Gets sub commodity list.
	 *
	 * @return the sub commodity list
	 */
	public List<SubCommodity> getSubCommodityList() {
		if(subCommodityList == null){
			subCommodityList = new ArrayList<>();
		}
		return subCommodityList;
	}

	/**
	 * Sets sub commodity list.
	 *
	 * @param subCommodityList the sub commodity list
	 */
	public void setSubCommodityList(List<SubCommodity> subCommodityList) {
		this.subCommodityList = subCommodityList;
	}

	/**
	 * Returns ItemClassMaster.
	 *
	 * @return The ItemClassMaster.
	 **/
	public ItemClass getItemClassMaster() {
		return itemClassMaster;
	}

	/**
	 * Sets the ItemClassMaster.
	 *
	 * @param itemClassMaster The ItemClassMaster.
	 **/
	public void setItemClassMaster(ItemClass itemClassMaster) {
		this.itemClassMaster = itemClassMaster;
	}

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public ClassCommodityKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key The key for this object.
	 */
	public void setKey(ClassCommodityKey key) {
		this.key = key;
	}

	/**
	 * Returns the name of this class/commodity.
	 *
	 * @return The name of this class/commodity.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this class/commodity.
	 *
	 * @param name The name of this class/commodity.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the bdm code of this class/commodity.
	 *
	 * @return The bdm code of this class/commodity.
	 */
	public String getBdmCode() {
		return bdmCode;
	}

	/**
	 * Sets the bdm code of this class/commodity.
	 *
	 * @param bdmCode The bdm code of this class/commodity.
	 */
	public void setBdmCode(String bdmCode) {
		this.bdmCode = bdmCode;
	}

	/**
	 * Returns the class commodity's bdm.
	 *
	 * @return The class commodity's bdm.
	 */
	public Bdm getBdm() {
		return bdm;
	}

	/**
	 * Sets the class commodity's bdm.
	 *
	 * @param bdm The class commodity's bdm.
	 */
	public void setBdm(Bdm bdm) {
		this.bdm = bdm;
	}

	/**
	 * Returns whether or not this objects represents a class.
	 *
	 * @return True if this object is a class and false otherwise.
	 */
	public boolean isClass() {
		return !(this.getKey() == null || this.getKey().getCommodityCode() != 0);
	}

	/**
	 * Returns whether or not this object represents a commodity.
	 *
	 * @return True if this object is a commodity and false otherwise.
	 */
	public boolean isCommodity() {
		return !(this.getKey() == null || this.getKey().getCommodityCode() == 0);
	}

	/**
	 * Returns ImsCommunicationCode.
	 *
	 * @return The ImsCommunicationCode.
	 **/
	public Integer getImsCommunicationCode() {
		return imsCommunicationCode;
	}

	/**
	 * Sets the ImsCommunicationCode.
	 *
	 * @param imsCommunicationCode The ImsCommunicationCode.
	 **/
	public void setImsCommunicationCode(Integer imsCommunicationCode) {
		this.imsCommunicationCode = imsCommunicationCode;
	}

	/**
	 * Returns PssDepartment.
	 *
	 * @return The PssDepartment.
	 **/
	public Integer getPssDepartment() {
		return pssDepartment;
	}

	/**
	 * Sets the PssDepartment.
	 *
	 * @param pssDepartment The PssDepartment.
	 **/
	public void setPssDepartment(Integer pssDepartment) {
		this.pssDepartment = pssDepartment;
	}

	/**
	 * Returns OmiDirector.
	 *
	 * @return The OmiDirector.
	 **/
	public Integer getOmiDirector() {
		return omiDirector;
	}

	/**
	 * Sets the OmiDirector.
	 *
	 * @param omiDirector The OmiDirector.
	 **/
	public void setOmiDirector(Integer omiDirector) {
		this.omiDirector = omiDirector;
	}

	/**
	 * Returns ClassCommodityActive.
	 *
	 * @return The ClassCommodityActive.
	 **/
	public Character getClassCommodityActive() {
		return classCommodityActive;
	}

	/**
	 * Sets the ClassCommodityActive.
	 *
	 * @param classCommodityActive The ClassCommodityActive.
	 **/
	public void setClassCommodityActive(Character classCommodityActive) {
		this.classCommodityActive = classCommodityActive;
	}

	/**
	 * Returns eBMid.
	 *
	 * @return The eBMid.
	 **/
	public String geteBMid() {
		return eBMid;
	}

	/**
	 * Sets the eBMid.
	 *
	 * @param eBMid The eBMid.
	 **/
	public void seteBMid(String eBMid) {
		this.eBMid = eBMid;
	}

	/**
	 * Returns bDAid.
	 *
	 * @return The bDAid.
	 **/
	public String getbDAid() {
		return bDAid;
	}

	/**
	 * Sets the bDAid.
	 *
	 * @param bDAid The bDAid.
	 **/
	public void setbDAid(String bDAid) {
		this.bDAid = bDAid;
	}

	/**
	 * Returns a unique ID across classes or commodities. If this object is a class, it returns the class ID. If
	 * this object is a commodity, it returns a commodity ID.
	 *
	 * @return A unique ID across classes or commodities.
	 */
	public Integer getNormalizedId() {
		if(this.getKey() == null){
			return null;
		}
		if (this.isClass()) {
			return this.key.getClassCode();
		}
		return this.key.getCommodityCode();
	}

	/**
	 * Returns the class/commodity as it should be displayed on the GUI.
	 *
	 * @return A String representation of the class/commodity as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		if(this.name == null || this.getKey() == null ||
				this.getKey().getCommodityCode() == null || this.getKey().getClassCode() == null){
			return null;
		}
		if (this.isClass()) {
			return String.format(ClassCommodity.DISPLAY_NAME_FORMAT, this.name.trim(), this.getKey().getClassCode());
		}
		return String.format(ClassCommodity.DISPLAY_NAME_FORMAT, this.name.trim(), this.getKey().getCommodityCode());
	}

	/**
	 * Returns MaxCustomerOrderQuantity.
	 *
	 * @return The MaxCustomerOrderQuantity.
	 **/
	public Integer getMaxCustomerOrderQuantity() {
		return maxCustomerOrderQuantity;
	}

	/**
	 * Sets the MaxCustomerOrderQuantity.
	 *
	 * @param maxCustomerOrderQuantity The MaxCustomerOrderQuantity.
	 **/
	public void setMaxCustomerOrderQuantity(Integer maxCustomerOrderQuantity) {
		this.maxCustomerOrderQuantity = maxCustomerOrderQuantity;
	}

	/**
	 * Returns PdpTemplateCode.
	 *
	 * @return The PdpTemplateCode.
	 **/
	public String getPdpTemplateCode() {
		return pdpTemplateCode;
	}

	/**
	 * Sets the PdpTemplateCode.
	 *
	 * @param pdpTemplateCode The PdpTemplateCode.
	 **/
	public void setPdpTemplateCode(String pdpTemplateCode) {
		this.pdpTemplateCode = pdpTemplateCode;
	}

	/**
	 * Returns PdpTemplate.
	 *
	 * @return The PdpTemplate.
	 **/
	public PDPTemplate getPdpTemplate() {
		return pdpTemplate;
	}

	/**
	 * Sets the PdpTemplate.
	 *
	 * @param pdpTemplate The PdpTemplate.
	 **/
	public void setPdpTemplate(PDPTemplate pdpTemplate) {
		this.pdpTemplate = pdpTemplate;
	}

	/**
	 * Tests for equality with another object. Equality is based on the key.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ClassCommodity)) return false;

		ClassCommodity that = (ClassCommodity) o;

		return !(key != null ? !key.equals(that.key) : that.key != null);
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ClassCommodity{" +
				"key=" + key +
				", name='" + name + '\'' +
				", bdmCode='" + bdmCode + '\'' +
				", imsCommunicationCode=" + imsCommunicationCode +
				", pssDepartment=" + pssDepartment +
				", omiDirector=" + omiDirector +
				", classCommodityActive=" + classCommodityActive +
				", eBMid='" + eBMid + '\'' +
				", bDAid='" + bDAid + '\'' +
				", pdpTemplateCode='" + pdpTemplateCode + '\'' +
				", maxCustomerOrderQuantity=" + maxCustomerOrderQuantity +
				'}';
	}

	/**
	 * Returns a hash code for this object. Equal objects always return the same value. Unequals objects (probably)
	 * return different values.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

}
