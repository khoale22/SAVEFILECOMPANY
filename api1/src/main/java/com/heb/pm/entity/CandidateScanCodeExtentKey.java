/*
 * CandidateScanCodeExtentKey
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a ps product scan code extent data key that is extended attributes for UPCs.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Embeddable
public class CandidateScanCodeExtentKey implements Serializable {

	private static final int FOUR_BYTES = 32;

	@Column(name="ps_prod_id")
	private Long candidateProductId;

	@Column(name = "scn_cd_id")
	private Long scanCodeId;

	@Column(name = "prod_ext_dta_cd")
	private String productExtentDataCode;

	/**
	 * @return Gets the value of candidateProductId and returns candidateProductId
	 */
	public void setCandidateProductId(Long candidateProductId) {
		this.candidateProductId = candidateProductId;
	}

	/**
	 * Sets the candidateProductId
	 */
	public Long getCandidateProductId() {
		return candidateProductId;
	}

	/**
	 * Gets scan code id that is attached to the measurements given by the gladson measuring data.
	 *
	 * @return the scan code id that is attached to the measurements given by the gladson measuring data.
	 */
	public Long getScanCodeId() {
		return scanCodeId;
	}

	/**
	 * Sets scan code id that is attached to the measurements given by the gladson measuring data.
	 *
	 * @param scanCodeId the scan code id that is attached to the measurements given by the gladson measuring data.
	 */
	public void setScanCodeId(Long scanCodeId) {
		this.scanCodeId = scanCodeId;
	}

	/**
	 * Gets prod ext data code.
	 *
	 * @return the prod ext data code that is tied to a description that then provides the measurement for that code.
	 */
	public String getProductExtentDataCode() {
		return productExtentDataCode;
	}

	/**
	 * Sets prod ext data code.
	 *
	 * @param productExtentDataCode the prod ext data code that is tied to a description that then provides the measurement for that code.
	 */
	public void setProductExtentDataCode(String productExtentDataCode) {
		this.productExtentDataCode = productExtentDataCode;
	}

	/**
	 * Compares another object to this one. If that object is an ProductScanCodeExtentKey, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CandidateScanCodeExtentKey)) {
			return false;
		}
		CandidateScanCodeExtentKey castOther = (CandidateScanCodeExtentKey)o;
		return
				(this.candidateProductId == castOther.candidateProductId)
						&& (this.scanCodeId == castOther.scanCodeId)
						&& this.productExtentDataCode.equals(castOther.productExtentDataCode);

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.candidateProductId ^ (this.candidateProductId >>> 32)));
		hash = hash * prime + ((int) (this.scanCodeId ^ (this.scanCodeId >>> 32)));
		hash = hash * prime + this.productExtentDataCode.hashCode();

		return hash;
	}
}
