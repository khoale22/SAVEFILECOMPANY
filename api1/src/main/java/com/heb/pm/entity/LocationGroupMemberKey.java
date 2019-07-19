/*
 *  LocationGroupMemberKey.java
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents a LocationGroupMember data key for table LocationGroupMember.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Embeddable
public class LocationGroupMemberKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "SPLR_LOC_NBR")
	private Integer splrLocationNumber;

	@Column(name = "SPLR_LOC_TYP_CD")
	private String splrLocationTypeCode;

	@Column(name = "DEPT_ID")
	private Integer departmentId;

	@Column(name = "SUB_DEPT_ID")
	private String subDepartmentId;

	@Column(name = "SPLR_LOC_GRP_ID")
	private Integer splrLocationGroupId;

	@Column(name = "CUST_LOC_NBR")
	private Integer custLocationNumber;

	@Column(name = "CUST_LOC_TYP_CD")
	private String custLocationTypeCode;

	/**
	 * Returns the splr Location Number.
	 * @return the splr Location Number.
	 */
	public Integer getSplrLocationNumber() {
		return splrLocationNumber;
	}

	/**
	 * Sets the splr Location Number.
	 * @param splrLocationNumber the splr Location Number.
	 */
	public void setSplrLocationNumber(Integer splrLocationNumber) {
		this.splrLocationNumber = splrLocationNumber;
	}

	/**
	 * Returns the splr Location Type Code.
	 * @return the splr Location Type Code.
	 */
	public String getSplrLocationTypeCode() {
		return splrLocationTypeCode;
	}

	/**
	 * Sets the splr Location Type Code.
	 * @param splrLocationTypeCode the splr Location Type Code.
	 */
	public void setSplrLocationTypeCode(String splrLocationTypeCode) {
		this.splrLocationTypeCode = splrLocationTypeCode;
	}

	/**
	 * Returns the department Id.
	 * @return the department Id.
	 */
	public Integer getDepartmentId() {
		return departmentId;
	}

	/**
	 * Sets the department Id.
	 * @param departmentId the department Id.
	 */
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * Returns the sub department Id.
	 * @return the sub department Id.
	 */
	public String getSubDepartmentId() {
		return subDepartmentId;
	}

	/**
	 * Sets the sub department Id.
	 * @param subDepartmentId the sub department Id.
	 */
	public void setSubDepartmentId(String subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	/**
	 * Returns the splr Location Group Id.
	 * @returnthe splr Location Group Id.
	 */
	public Integer getSplrLocationGroupId() {
		return splrLocationGroupId;
	}

	/**
	 * Sets the splr Location Group Id
	 * @param splrLocationGroupId the splr Location Group Id
	 */
	public void setSplrLocationGroupId(Integer splrLocationGroupId) {
		this.splrLocationGroupId = splrLocationGroupId;
	}

	/**
	 * Returns the cust Location Number.
	 * @return the cust Location Number.
	 */
	public Integer getCustLocationNumber() {
		return custLocationNumber;
	}

	/**
	 * Sets the cust Location Number.
	 * @param custLocationNumber the cust Location Number.
	 */
	public void setCustLocationNumber(Integer custLocationNumber) {
		this.custLocationNumber = custLocationNumber;
	}

	/**
	 * Returns the cust Location Type Code.
	 * @return the cust Location Type Code.
	 */
	public String getCustLocationTypeCode() {
		return custLocationTypeCode;
	}

	/**
	 * Sets the cust Location Type Code.
	 * @param custLocationTypeCode the cust Location Type Code.
	 */
	public void setCustLocationTypeCode(String custLocationTypeCode) {
		this.custLocationTypeCode = custLocationTypeCode;
	}

	@Override
	public int hashCode() {
		int result = getCustLocationNumber() != null ? getCustLocationNumber().hashCode() : 0;
		result = 31 * result + (splrLocationTypeCode != null ? splrLocationTypeCode.hashCode() : 0);
		result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
		result = 31 * result + (subDepartmentId != null ? subDepartmentId.hashCode() : 0);
		result = 31 * result + (splrLocationGroupId != null ? splrLocationGroupId.hashCode() : 0);
		result = 31 * result + (custLocationNumber != null ? custLocationNumber.hashCode() : 0);
		result = 31 * result + (custLocationTypeCode != null ? custLocationTypeCode.hashCode() : 0);
		return result;
	}
	@Override
	public String toString() {
		return "LocationGroupMemberKey{" +
				"splrLocationNumber=" + splrLocationNumber +
				", splrLocationTypeCode='" + splrLocationTypeCode + '\'' +
				", departmentId='" + departmentId + '\'' +
				", subDepartmentId='" + subDepartmentId + '\'' +
				", splrLocationGroupId='" + splrLocationGroupId + '\'' +
				", custLocationNumber='" + custLocationNumber + '\'' +
				", custLocationTypeCode='" + custLocationTypeCode + '\'' +
				'}';
	}
}