/*
 *  CandidateProductScanCodeNutrientKey.java
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Entity key for the candidate product scan code nutrient table.
 *
 * @author vn73545
 * @since 2.40.0
 */
@Embeddable
public class CandidateProductScanCodeNutrientKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ps_work_id ")
    private Long workRequestId;
	
	@Column(name = "scn_cd_id")
	private Long scanCodeId;

	@Column(name = "prod_ntrntl_cd")
	@Type(type = "fixedLengthCharPK")
	private String productNutrientCode;

	/**
	 * Get the workRequestId.
	 *
	 * @return the workRequestId
	 */
	public Long getWorkRequestId() {
		return workRequestId;
	}

	/**
	 * Set the workRequestId.
	 *
	 * @param workRequestId the workRequestId to set
	 */
	public void setWorkRequestId(Long workRequestId) {
		this.workRequestId = workRequestId;
	}

	/**
	 * Get the scanCodeId.
	 *
	 * @return the scanCodeId
	 */
	public Long getScanCodeId() {
		return scanCodeId;
	}

	/**
	 * Set the scanCodeId.
	 *
	 * @param scanCodeId the scanCodeId to set
	 */
	public void setScanCodeId(Long scanCodeId) {
		this.scanCodeId = scanCodeId;
	}

	/**
	 * Get the productNutrientCode.
	 *
	 * @return the productNutrientCode
	 */
	public String getProductNutrientCode() {
		return productNutrientCode;
	}

	/**
	 * Set the productNutrientCode.
	 *
	 * @param productNutrientCode the productNutrientCode to set
	 */
	public void setProductNutrientCode(String productNutrientCode) {
		this.productNutrientCode = productNutrientCode;
	}

	/**
	 * Compares this object with another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CandidateProductScanCodeNutrientKey that = (CandidateProductScanCodeNutrientKey) o;

		if (workRequestId != null ? !workRequestId.equals(that.workRequestId) : that.workRequestId != null)
			return false;
		if (scanCodeId != null ? !scanCodeId.equals(that.scanCodeId) : that.scanCodeId != null) return false;
		return productNutrientCode != null ? productNutrientCode.equals(that.productNutrientCode) : that.productNutrientCode == null;
	}

	/**
	 * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
	 * different hash codes.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = workRequestId != null ? workRequestId.hashCode() : 0;
		result = 31 * result + (scanCodeId != null ? scanCodeId.hashCode() : 0);
		result = 31 * result + (productNutrientCode != null ? productNutrientCode.hashCode() : 0);
		return result;
	}

	/**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
	@Override
	public String toString() {
		return "CandidateProductScanCodeNutrientKey{" +
				"workRequestId=" + workRequestId +
				", scanCodeId=" + scanCodeId +
				", productNutrientCode='" + productNutrientCode + '\'' +
				'}';
	}
}