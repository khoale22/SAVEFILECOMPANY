/*
 *  ApLocation
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the Ap_Location.
 *
 * @author l730832
 * @since 2.5.0
 */
@Entity
@Table(name = "ap_location")
public class ApLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@EmbeddedId
	private ApLocationKey key;

	@Column(name = "rmit_co_nm")
	private String remitCoName;
	@Column(name = "DSD_SBB_SW")
	private Boolean dsdSbbSw;
	@Column(name = "DSD_DBV_NBR")
	private Integer dsdDbvNumber;
	@Column(name = "RMIT_ADR_1")
	private String remitAdr1;
	@Column(name = "RMIT_ADR_2")
	private String remitAdr2;
	@Column(name = "RMIT_ST")
	private String remitState;
	@Column(name = "CORR_CONTC_NM")
	private String corrContcName;
	@Column(name = "RMIT_ZIP5_CD")
	private Integer remitZip5Cd;
	@Column(name = "RMIT_ZIP4_CD")
	private Integer remitZip4Cd;
	@Column(name = "RMIT_AREA_CD")
	private Integer remitAreaCd;
	@Column(name = "RMIT_PHN_CMT")
	private String remitPhnCmt;
	@Column(name = "RMIT_PHN_NBR")
	private Integer remitPhnNumber;

	@Column(name = "DSD_PROC_MODE")
	private String dsdProcMode;
	@Column(name = "DSD_BNK_CD")
	private String dsdBnkCode;
	@Column(name = "DSD_SBB_EFF_DT")
	private LocalDateTime dsdSbbEffDt;
	@Column(name = "DSD_DBV_LOC_CD")
	private Integer dsdDbvLocationCode;

	/**
	 * Returns the ApLocationKey
	 *
	 * @return key
	 **/
	public ApLocationKey getKey() {
		return key;
	}

	/**
	 * Sets the ApLocationKey
	 *
	 * @param key The ApLocationKey
	 **/
	public void setKey(ApLocationKey key) {
		this.key = key;
	}

	/**
	 * Returns the RemitCoName
	 *
	 * @return RemitCoName
	 **/
	public String getRemitCoName() {
		return remitCoName;
	}

	/**
	 * Sets the RemitCoName
	 *
	 * @param remitCoName The RemitCoName
	 **/
	public void setRemitCoName(String remitCoName) {
		this.remitCoName = remitCoName;
	}

	/**
	 * Returns the dsdDbvNumber.
	 * @return the dsdDbvNumber.
	 */
	public Integer getDsdDbvNumber() {
		return dsdDbvNumber;
	}

	/**
	 * Sets the dsdDbvNumber.
	 * @param dsdDbvNumber the dsdDbvNumber.
	 */
	public void setDsdDbvNumber(Integer dsdDbvNumber) {
		this.dsdDbvNumber = dsdDbvNumber;
	}

	/**
	 * Returns the remitAdr1.
	 * @return the remitAdr1.
	 */
	public String getRemitAdr1() {
		return remitAdr1;
	}

	/**
	 * Sets the remitAdr1.
	 * @param remitAdr1 the remitAdr1.
	 */
	public void setRemitAdr1(String remitAdr1) {
		this.remitAdr1 = remitAdr1;
	}

	/**
	 * Returns the remitAdr2.
	 * @return the remitAdr2.
	 */
	public String getRemitAdr2() {
		return remitAdr2;
	}

	/**
	 * Sets the remitAdr2.
	 * @param remitAdr2 the remitAdr2.
	 */
	public void setRemitAdr2(String remitAdr2) {
		this.remitAdr2 = remitAdr2;
	}

	/**
	 * Returns the remitState.
	 * @return the remitState.
	 */
	public String getRemitState() {
		return remitState;
	}

	/**
	 * Sets the remitState.
	 * @param remitState the remitState.
	 */
	public void setRemitState(String remitState) {
		this.remitState = remitState;
	}

	/**
	 * Returns the corrContcName.
	 * @returnthe corrContcName.
	 */
	public String getCorrContcName() {
		return corrContcName;
	}

	/**
	 * Sets the corrContcName.
	 * @param corrContcName the corrContcName.
	 */
	public void setCorrContcName(String corrContcName) {
		this.corrContcName = corrContcName;
	}

	/**
	 * Returns the remitZip5Cd.
	 * @return the remitZip5Cd.
	 */
	public Integer getRemitZip5Cd() {
		return remitZip5Cd;
	}

	/**
	 * Sets the remitZip5Cd.
	 * @param remitZip5Cd the remitZip5Cd.
	 */
	public void setRemitZip5Cd(Integer remitZip5Cd) {
		this.remitZip5Cd = remitZip5Cd;
	}

	/**
	 * Returns the remitZip4Cd.
	 * @return the remitZip4Cd.
	 */
	public Integer getRemitZip4Cd() {
		return remitZip4Cd;
	}

	/**
	 * Sets the remitZip4Cd.
	 * @param remitZip4Cd the remitZip4Cd.
	 */
	public void setRemitZip4Cd(Integer remitZip4Cd) {
		this.remitZip4Cd = remitZip4Cd;
	}

	/**
	 * Returns the remitAreaCd.
	 * @return the remitAreaCd.
	 */
	public Integer getRemitAreaCd() {
		return remitAreaCd;
	}

	/**
	 * Sets the remitAreaCd.
	 * @param remitAreaCd the remitAreaCd.
	 */
	public void setRemitAreaCd(Integer remitAreaCd) {
		this.remitAreaCd = remitAreaCd;
	}

	/**
	 * Returns the remitPhnCmt.
	 * @return the remitPhnCmt.
	 */
	public String getRemitPhnCmt() {
		return remitPhnCmt;
	}

	/**
	 * Sets the remitPhnCmt.
	 * @param remitPhnCmt the remitPhnCmt.
	 */
	public void setRemitPhnCmt(String remitPhnCmt) {
		this.remitPhnCmt = remitPhnCmt;
	}

	/**
	 * Returns the remitPhnNumber.
	 * @return the remitPhnNumber.
	 */
	public Integer getRemitPhnNumber() {
		return remitPhnNumber;
	}

	/**
	 * Sets the remitPhnNumber.
	 * @param remitPhnNumber the remitPhnNumber.
	 */
	public void setRemitPhnNumber(Integer remitPhnNumber) {
		this.remitPhnNumber = remitPhnNumber;
	}

	/**
	 * Returns the dsdProcMode.
	 * @return the dsdProcMode.
	 */
	public String getDsdProcMode() {
		return dsdProcMode;
	}

	/**
	 * Sets the dsdProcMode.
	 * @param dsdProcMode the dsdProcMode.
	 */
	public void setDsdProcMode(String dsdProcMode) {
		this.dsdProcMode = dsdProcMode;
	}

	/**
	 * Returns the dsdBnkCode.
	 * @return the dsdBnkCode.
	 */
	public String getDsdBnkCode() {
		return dsdBnkCode;
	}

	/**
	 * Sets the dsdBnkCode.
	 * @param dsdBnkCode the dsdBnkCode.
	 */
	public void setDsdBnkCode(String dsdBnkCode) {
		this.dsdBnkCode = dsdBnkCode;
	}

	/**
	 * Returns the dsdSbbEffDt.
	 * @return the dsdSbbEffDt.
	 */
	public LocalDateTime getDsdSbbEffDt() {
		return dsdSbbEffDt;
	}

	/**
	 * Sets the dsdSbbEffDt.
	 * @param dsdSbbEffDt the dsdSbbEffDt.
	 */
	public void setDsdSbbEffDt(LocalDateTime dsdSbbEffDt) {
		this.dsdSbbEffDt = dsdSbbEffDt;
	}

	/**
	 * Returns the dsdDbvLocationCode.
	 * @return the dsdDbvLocationCode.
	 */
	public Integer getDsdDbvLocationCode() {
		return dsdDbvLocationCode;
	}

	/**
	 * Sets the dsdDbvLocationCode.
	 * @param dsdDbvLocationCode the dsdDbvLocationCode.
	 */
	public void setDsdDbvLocationCode(Integer dsdDbvLocationCode) {
		this.dsdDbvLocationCode = dsdDbvLocationCode;
	}

	/**
	 * Gets display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return String.format(ApLocation.DISPLAY_NAME_FORMAT, this.getRemitCoName().trim(),
				this.getKey().getApVendorNumber());
	}

	/**
	 * Returns the dsdSbbSw.
	 * @return the dsdSbbSw.
	 */
	public Boolean getDsdSbbSw() {
		return dsdSbbSw;
	}

	/**
	 * Sets the dsdSbbSw.
	 * @param dsdSbbSw the dsdSbbSw.
	 */
	public void setDsdSbbSw(Boolean dsdSbbSw) {
		this.dsdSbbSw = dsdSbbSw;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ApLocation{" +
				"apLocationKey=" + key +
				", remitCoName='" + remitCoName + '\'' +
				", dsdSbbSw='" + dsdSbbSw + '\'' +
				", dsdDbvNumber='" + dsdDbvNumber + '\'' +
				", remitAdr1='" + remitAdr1 + '\'' +
				", remitAdr2='" + remitAdr2 + '\'' +
				", remitState='" + remitState + '\'' +
				", corrContcName='" + corrContcName + '\'' +
				", remitZip5Cd='" + remitZip5Cd + '\'' +
				", remitZip4Cd='" + remitZip4Cd + '\'' +
				", remitAreaCd='" + remitAreaCd + '\'' +
				", remitPhnCmt='" + remitPhnCmt + '\'' +
				", remitPhnNumber='" + remitPhnNumber + '\'' +
				", dsdProcMode='" + dsdProcMode + '\'' +
				", dsdBnkCode='" + dsdBnkCode + '\'' +
				", dsdSbbEffDt='" + dsdSbbEffDt + '\'' +
				", dsdDbvLocationCode='" + dsdDbvLocationCode + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a Location, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ApLocation that = (ApLocation) o;

		if (key != null ? !key.equals(that.key) : that.key != null)
			return false;
		return remitCoName != null ? remitCoName.equals(that.remitCoName) : that.remitCoName == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (remitCoName != null ? remitCoName.hashCode() : 0);
		return result;
	}
}
