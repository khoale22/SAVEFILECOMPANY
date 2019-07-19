/*
 * CandidateScanCodeExtent
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a ps product scan code extent data that is extended attributes for UPCs.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_prod_scn_cd_ext")
public class CandidateScanCodeExtent implements Serializable {
	public static final String STRING_ESHRT = "ESHRT";
	public static final String STRING_ELONG = "ELONG";
	@EmbeddedId
	private CandidateScanCodeExtentKey key;

	@Column(name="ps_work_id")
	private Long workRequestId;

	@Column(name = "prod_des_txt")
	private String productDescription;



	/**
	 * bi-directional many-to-one association to PsProductMaster
	 */
	@ManyToOne
	@JoinColumn(name = "PS_PROD_ID", insertable = false, updatable = false)
	private CandidateProductMaster candidateProductMaster;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PS_WORK_ID", referencedColumnName = "PS_WORK_ID", insertable = false, updatable = false, nullable = false)
	private CandidateWorkRequest candidateWorkRequest;
	/**
	 * Gets the key composite key to represent the primary keys for the ps_prod_scn_cd_extent table.
	 *
	 * @return the key composite key to represent the primary keys for the ps_prod_scn_cd_extent table.
	 */
	public CandidateScanCodeExtentKey getKey() {
		return key;
	}

	/**
	 * Sets the key composite key to represent the primary keys for the ps_prod_scn_cd_extent table.
	 *
	 * @param key the key composite key to represent the primary keys for the ps_prod_scn_cd_extent table.
	 */
	public void setKey(CandidateScanCodeExtentKey key) {
		this.key = key;
	}

	/**
	 * Sets the word request id for the ps_prod_scn_cd_extent table.
	 */
	public Long getWorkRequestId() {
		return workRequestId;
	}

	/**
	 * @return Gets the value of workRequestId and returns workRequestId
	 */
	public void setWorkRequestId(Long workRequestId) {
		this.workRequestId = workRequestId;
	}

	/**
	 * Gets prod description text. That holds values tied to the prod_ext_dta_cd within the key.
	 *
	 * @return the prod description text that holds values tied to the prod_ext_dta_cd within the key.
	 */
	public String getProductDescription() {
		return productDescription;
	}

	/**
	 * Sets prod description text that holds values tied to the prod_ext_dta_cd within the key.
	 *
	 * @param productDescription the prod description text that holds values tied to the prod_ext_dta_cd within the key.
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public CandidateProductMaster getCandidateProductMaster() {
		return candidateProductMaster;
	}

	public void setCandidateProductMaster(CandidateProductMaster candidateProductMaster) {
		this.candidateProductMaster = candidateProductMaster;
	}
	public CandidateWorkRequest getCandidateWorkRequest() {
		return candidateWorkRequest;
	}

	public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
		this.candidateWorkRequest = candidateWorkRequest;
	}
	/**
	 * Compares another object to this one. If that object is an ProductScanCodeExtent, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateScanCodeExtent)) return false;
		CandidateScanCodeExtent that = (CandidateScanCodeExtent) o;

		return key == that.key;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.key != null ? this.key.hashCode() : 0;
	}
	/**
	 * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
	 * it is inserted into the work request table.
	 */
	@PrePersist
	public void setCandidateKey() {
		if (this.getKey().getCandidateProductId() == null) {
			this.getKey().setCandidateProductId(this.candidateProductMaster.getCandidateProductId());
		}
		if (this.getWorkRequestId() == null) {
			this.setWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
		}
	}
}
