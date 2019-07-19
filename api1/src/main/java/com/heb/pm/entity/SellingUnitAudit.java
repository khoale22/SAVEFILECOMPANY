/*
 *  SellingUnitAudit
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * This is the audit table for a selling unit.
 *
 * @author l730832
 * @since 2.12.0
 */
@Entity
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
})
@Table(name = "prod_scn_codes_aud")
public class SellingUnitAudit implements Serializable, Audit{

	@EmbeddedId
	private SellingUnitAuditKey key;

	@Column(name = "act_cd")
	private String action;

	@Column(name = "lst_updt_uid")
	private String changedBy;

	@AuditableField(displayName = "Product Primary UPC", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "prim_scn_cd_sw")
	private Boolean primaryScanCodeSwitch;

	@AuditableField(displayName = "BNS UPC", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "bns_scn_cd_sw")
	private Boolean bnsScanCodeSwitch;

	@AuditableField(displayName = "Retail Unit Length", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "retl_unt_ln")
	private Double retailUnitLength;

	@AuditableField(displayName = "Retail Unit Width", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "retl_unt_wd")
	private Double retailUnitWidth;

	@AuditableField(displayName = "Retail Unit Weight", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "retl_unt_wt")
	private Double retailUnitWeight;

	@AuditableField(displayName = "Retail Unit Height", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "retl_unt_ht")
	private Double retailUnitHeight;

	@Type(type="fixedLengthChar")
	@AuditableField(displayName = "Retail Sell Size Code 1", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "retl_sell_sz_cd_1")
	private String retailSellSizeCode1;

	@AuditableField(displayName = "Retail Unit Sell Size 1", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "retl_unt_sell_sz_1")
	private Double retailUnitSellSize1;

	@Type(type="fixedLengthChar")
	@AuditableField(displayName = "Retail Sell Size Code 2", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "retl_sell_sz_cd_2")
	private String retailSellSizeCode2;

	@AuditableField(displayName = "Retail Unit Sell Size 1", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "retl_unt_sell_sz_2")
	private Double retailUnitSellSize2;

	@AuditableField(displayName = "Sample Provided Switch", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "samp_provd_sw")
	private Boolean sampleProvidedSwitch;

	@AuditableField(displayName = "First Scan Date", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "frst_scn_dt")
	private Date firstScanDate;

	@AuditableField(displayName = "WIC", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "wic_sw")
	private Boolean wicSwitch;

	@AuditableField(displayName = "LEB", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "leb_sw")
	private Boolean lebSwitch;

	@AuditableField(displayName = "WIC APL ID", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "wic_apl_id")
	private Long wicAplId;

	@AuditableField(displayName = "DSD Department Override", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "dsd_dept_ovrd_sw")
	private Boolean dsdDepartmentOverrideSwitch;

	@AuditableField(displayName = "UPC Active", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "upc_actv_sw")
	private Character upcActiveSwitch;

	@AuditableField(displayName = "DSD Deleted", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "dsd_deld_sw")
	private Boolean dsdDeletedSwitch;

	@AuditableField(displayName = "Scale", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "scale_sw")
	private Boolean scaleSwitch;

	@AuditableField(displayNameMethod = "getPseDescritption", filter = {FilterConstants.PHARMACY_AUDIT})
	@Column(name = "pse_grams_wt")
	private Double pseGramsWeight;

	@AuditableField(displayName = "Tag Size Description", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "tag_sz_des")
	private String tagSizeDescription;

	@AuditableField(displayName = "Discontinue Date", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "dscon_dt")
	private Date discontinueDate;

	@AuditableField(displayName = "Proc Scan Maintenance", filter = {FilterConstants.UPC_INFO_AUDIT})
	@Column(name = "proc_scn_maint_sw")
	private Boolean procScanMaintenanceSwitch;

	public String getPseDescritption() {
		return String.format("PSE Grams Weight [%d]", this.key.getUpc());
	}

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public SellingUnitAuditKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(SellingUnitAuditKey key) {
		this.key = key;
	}

	/**
	 * Returns the PrimaryScanCodeSwitch
	 *
	 * @return PrimaryScanCodeSwitch
	 */
	public Boolean getPrimaryScanCodeSwitch() {
		return primaryScanCodeSwitch;
	}

	/**
	 * Sets the PrimaryScanCodeSwitch
	 *
	 * @param primaryScanCodeSwitch The PrimaryScanCodeSwitch
	 */
	public void setPrimaryScanCodeSwitch(Boolean primaryScanCodeSwitch) {
		this.primaryScanCodeSwitch = primaryScanCodeSwitch;
	}

	/**
	 * Returns the BnsScanCodeSwitch
	 *
	 * @return BnsScanCodeSwitch
	 */
	public Boolean getBnsScanCodeSwitch() {
		return bnsScanCodeSwitch;
	}

	/**
	 * Sets the BnsScanCodeSwitch
	 *
	 * @param bnsScanCodeSwitch The BnsScanCodeSwitch
	 */
	public void setBnsScanCodeSwitch(Boolean bnsScanCodeSwitch) {
		this.bnsScanCodeSwitch = bnsScanCodeSwitch;
	}

	/**
	 * Returns the RetailUnitLength
	 *
	 * @return RetailUnitLength
	 */
	public Double getRetailUnitLength() {
		return retailUnitLength;
	}

	/**
	 * Sets the RetailUnitLength
	 *
	 * @param retailUnitLength The RetailUnitLength
	 */
	public void setRetailUnitLength(Double retailUnitLength) {
		this.retailUnitLength = retailUnitLength;
	}

	/**
	 * Returns the RetailUnitWidth
	 *
	 * @return RetailUnitWidth
	 */
	public Double getRetailUnitWidth() {
		return retailUnitWidth;
	}

	/**
	 * Sets the RetailUnitWidth
	 *
	 * @param retailUnitWidth The RetailUnitWidth
	 */
	public void setRetailUnitWidth(Double retailUnitWidth) {
		this.retailUnitWidth = retailUnitWidth;
	}

	/**
	 * Returns the RetailUnitWeight
	 *
	 * @return RetailUnitWeight
	 */
	public Double getRetailUnitWeight() {
		return retailUnitWeight;
	}

	/**
	 * Sets the RetailUnitWeight
	 *
	 * @param retailUnitWeight The RetailUnitWeight
	 */
	public void setRetailUnitWeight(Double retailUnitWeight) {
		this.retailUnitWeight = retailUnitWeight;
	}

	/**
	 * Returns the RetailUnitHeight
	 *
	 * @return RetailUnitHeight
	 */
	public Double getRetailUnitHeight() {
		return retailUnitHeight;
	}

	/**
	 * Sets the RetailUnitHeight
	 *
	 * @param retailUnitHeight The RetailUnitHeight
	 */
	public void setRetailUnitHeight(Double retailUnitHeight) {
		this.retailUnitHeight = retailUnitHeight;
	}

	/**
	 * Returns the RetailSellSizeCode1
	 *
	 * @return RetailSellSizeCode1
	 */
	public String getRetailSellSizeCode1() {
		return retailSellSizeCode1;
	}

	/**
	 * Sets the RetailSellSizeCode1
	 *
	 * @param retailSellSizeCode1 The RetailSellSizeCode1
	 */
	public void setRetailSellSizeCode1(String retailSellSizeCode1) {
		this.retailSellSizeCode1 = retailSellSizeCode1;
	}

	/**
	 * Returns the RetailUnitSellSize1
	 *
	 * @return RetailUnitSellSize1
	 */
	public Double getRetailUnitSellSize1() {
		return retailUnitSellSize1;
	}

	/**
	 * Sets the RetailUnitSellSize1
	 *
	 * @param retailUnitSellSize1 The RetailUnitSellSize1
	 */
	public void setRetailUnitSellSize1(Double retailUnitSellSize1) {
		this.retailUnitSellSize1 = retailUnitSellSize1;
	}

	/**
	 * Returns the RetailSellSizeCode2
	 *
	 * @return RetailSellSizeCode2
	 */
	public String getRetailSellSizeCode2() {
		return retailSellSizeCode2;
	}

	/**
	 * Sets the RetailSellSizeCode2
	 *
	 * @param retailSellSizeCode2 The RetailSellSizeCode2
	 */
	public void setRetailSellSizeCode2(String retailSellSizeCode2) {
		this.retailSellSizeCode2 = retailSellSizeCode2;
	}

	/**
	 * Returns the RetailUnitSellSize2
	 *
	 * @return RetailUnitSellSize2
	 */
	public Double getRetailUnitSellSize2() {
		return retailUnitSellSize2;
	}

	/**
	 * Sets the RetailUnitSellSize2
	 *
	 * @param retailUnitSellSize2 The RetailUnitSellSize2
	 */
	public void setRetailUnitSellSize2(Double retailUnitSellSize2) {
		this.retailUnitSellSize2 = retailUnitSellSize2;
	}

	/**
	 * Returns the SampleProvidedSwitch
	 *
	 * @return SampleProvidedSwitch
	 */
	public Boolean getSampleProvidedSwitch() {
		return sampleProvidedSwitch;
	}

	/**
	 * Sets the SampleProvidedSwitch
	 *
	 * @param sampleProvidedSwitch The SampleProvidedSwitch
	 */
	public void setSampleProvidedSwitch(Boolean sampleProvidedSwitch) {
		this.sampleProvidedSwitch = sampleProvidedSwitch;
	}

	/**
	 * Returns the FirstScanDate
	 *
	 * @return FirstScanDate
	 */
	public Date getFirstScanDate() {
		return firstScanDate;
	}

	/**
	 * Sets the FirstScanDate
	 *
	 * @param firstScanDate The FirstScanDate
	 */
	public void setFirstScanDate(Date firstScanDate) {
		this.firstScanDate = firstScanDate;
	}

	/**
	 * Returns the WicSwitch
	 *
	 * @return WicSwitch
	 */
	public Boolean getWicSwitch() {
		return wicSwitch;
	}

	/**
	 * Sets the WicSwitch
	 *
	 * @param wicSwitch The WicSwitch
	 */
	public void setWicSwitch(Boolean wicSwitch) {
		this.wicSwitch = wicSwitch;
	}

	/**
	 * Returns the LebSwitch
	 *
	 * @return LebSwitch
	 */
	public Boolean getLebSwitch() {
		return lebSwitch;
	}

	/**
	 * Sets the LebSwitch
	 *
	 * @param lebSwitch The LebSwitch
	 */
	public void setLebSwitch(Boolean lebSwitch) {
		this.lebSwitch = lebSwitch;
	}

	/**
	 * Returns the WicAplId
	 *
	 * @return WicAplId
	 */
	public Long getWicAplId() {
		return wicAplId;
	}

	/**
	 * Sets the WicAplId
	 *
	 * @param wicAplId The WicAplId
	 */
	public void setWicAplId(Long wicAplId) {
		this.wicAplId = wicAplId;
	}

	/**
	 * Returns the DsdDepartmentOverrideSwitch
	 *
	 * @return DsdDepartmentOverrideSwitch
	 */
	public Boolean getDsdDepartmentOverrideSwitch() {
		return dsdDepartmentOverrideSwitch;
	}

	/**
	 * Sets the DsdDepartmentOverrideSwitch
	 *
	 * @param dsdDepartmentOverrideSwitch The DsdDepartmentOverrideSwitch
	 */
	public void setDsdDepartmentOverrideSwitch(Boolean dsdDepartmentOverrideSwitch) {
		this.dsdDepartmentOverrideSwitch = dsdDepartmentOverrideSwitch;
	}

	/**
	 * Returns the UpcActiveSwitch
	 *
	 * @return UpcActiveSwitch
	 */
	public Character getUpcActiveSwitch() {
		return upcActiveSwitch;
	}

	/**
	 * Sets the UpcActiveSwitch
	 *
	 * @param upcActiveSwitch The UpcActiveSwitch
	 */
	public void setUpcActiveSwitch(Character upcActiveSwitch) {
		this.upcActiveSwitch = upcActiveSwitch;
	}

	/**
	 * Returns the DsdDeletedSwitch
	 *
	 * @return DsdDeletedSwitch
	 */
	public Boolean getDsdDeletedSwitch() {
		return dsdDeletedSwitch;
	}

	/**
	 * Sets the DsdDeletedSwitch
	 *
	 * @param dsdDeletedSwitch The DsdDeletedSwitch
	 */
	public void setDsdDeletedSwitch(Boolean dsdDeletedSwitch) {
		this.dsdDeletedSwitch = dsdDeletedSwitch;
	}

	/**
	 * Returns the ScaleSwitch
	 *
	 * @return ScaleSwitch
	 */
	public Boolean getScaleSwitch() {
		return scaleSwitch;
	}

	/**
	 * Sets the ScaleSwitch
	 *
	 * @param scaleSwitch The ScaleSwitch
	 */
	public void setScaleSwitch(Boolean scaleSwitch) {
		this.scaleSwitch = scaleSwitch;
	}

	/**
	 * Returns the PseGramsWeight
	 *
	 * @return PseGramsWeight
	 */
	public Double getPseGramsWeight() {
		return pseGramsWeight;
	}

	/**
	 * Sets the PseGramsWeight
	 *
	 * @param pseGramsWeight The PseGramsWeight
	 */
	public void setPseGramsWeight(Double pseGramsWeight) {
		this.pseGramsWeight = pseGramsWeight;
	}

	/**
	 * Returns the TagSizeDescription
	 *
	 * @return TagSizeDescription
	 */
	public String getTagSizeDescription() {
		return tagSizeDescription;
	}

	/**
	 * Sets the TagSizeDescription
	 *
	 * @param tagSizeDescription The TagSizeDescription
	 */
	public void setTagSizeDescription(String tagSizeDescription) {
		this.tagSizeDescription = tagSizeDescription;
	}

	/**
	 * Returns the DiscontinueDate
	 *
	 * @return DiscontinueDate
	 */
	public Date getDiscontinueDate() {
		return discontinueDate;
	}

	/**
	 * Sets the DiscontinueDate
	 *
	 * @param discontinueDate The DiscontinueDate
	 */
	public void setDiscontinueDate(Date discontinueDate) {
		this.discontinueDate = discontinueDate;
	}

	/**
	 * Returns the ProcScanMaintenanceSwitch
	 *
	 * @return ProcScanMaintenanceSwitch
	 */
	public Boolean getProcScanMaintenanceSwitch() {
		return procScanMaintenanceSwitch;
	}

	/**
	 * Sets the ProcScanMaintenanceSwitch
	 *
	 * @param procScanMaintenanceSwitch The ProcScanMaintenanceSwitch
	 */
	public void setProcScanMaintenanceSwitch(Boolean procScanMaintenanceSwitch) {
		this.procScanMaintenanceSwitch = procScanMaintenanceSwitch;
	}

	@Override
	public String getAction() {
		return action;
	}

	@Override
	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String getChangedBy() {
		return changedBy;
	}

	@Override
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	@Override
	public LocalDateTime getChangedOn() {
		return this.key.getChangedOn();
	}

	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SellingUnitAudit that = (SellingUnitAudit) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "SellingUnitAudit{" +
				"key=" + key +
				", action='" + action + '\'' +
				", changedBy='" + changedBy + '\'' +
				", primaryScanCodeSwitch=" + primaryScanCodeSwitch +
				", bnsScanCodeSwitch=" + bnsScanCodeSwitch +
				", retailUnitLength=" + retailUnitLength +
				", retailUnitWidth=" + retailUnitWidth +
				", retailUnitWeight=" + retailUnitWeight +
				", retailUnitHeight=" + retailUnitHeight +
				", retailSellSizeCode1='" + retailSellSizeCode1 + '\'' +
				", retailUnitSellSize1=" + retailUnitSellSize1 +
				", retailSellSizeCode2='" + retailSellSizeCode2 + '\'' +
				", retailUnitSellSize2=" + retailUnitSellSize2 +
				", sampleProvidedSwitch=" + sampleProvidedSwitch +
				", firstScanDate=" + firstScanDate +
				", wicSwitch=" + wicSwitch +
				", lebSwitch=" + lebSwitch +
				", wicAplId=" + wicAplId +
				", dsdDepartmentOverrideSwitch=" + dsdDepartmentOverrideSwitch +
				", upcActiveSwitch=" + upcActiveSwitch +
				", dsdDeletedSwitch=" + dsdDeletedSwitch +
				", scaleSwitch=" + scaleSwitch +
				", pseGramsWeight=" + pseGramsWeight +
				", tagSizeDescription='" + tagSizeDescription + '\'' +
				", discontinueDate=" + discontinueDate +
				", procScanMaintenanceSwitch=" + procScanMaintenanceSwitch +
				'}';
	}
}
