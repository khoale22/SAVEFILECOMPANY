/*
 *  CandidateProductScanCodeNutrient.java
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity for the candidate product scan code nutrient table.
 *
 * @author vn73545
 * @since 2.40.0
 */
@Entity
@Table(name = "ps_prod_scn_ntrntl")
public class CandidateProductScanCodeNutrient implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String ACTION_CODE_YES = "Y";
	public static final String ACTION_CODE_NO = "N";
	public static final String ORGANIC_NUTRITION_CODE = "O95";

	@EmbeddedId
	private CandidateProductScanCodeNutrientKey key;

	@Column(name = "cre8_ts")
    private LocalDateTime createDate;

    @Column(name = "cre8_uid")
    private String createUserId;

    @Column(name = "act_cd")
    private String actionCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ps_work_id", referencedColumnName = "ps_work_id", insertable = false, updatable = false, nullable = false)
    private CandidateWorkRequest candidateWorkRequest;

	/**
	 * Get the key.
	 *
	 * @return the key
	 */
	public CandidateProductScanCodeNutrientKey getKey() {
		return key;
	}

	/**
	 * Set the key.
	 *
	 * @param key the key to set
	 */
	public void setKey(CandidateProductScanCodeNutrientKey key) {
		this.key = key;
	}

	/**
	 * Get the createDate.
	 *
	 * @return the createDate
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Set the createDate.
	 *
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * Get the createUserId.
	 *
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Set the createUserId.
	 *
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Get the actionCode.
	 *
	 * @return the actionCode
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Set the actionCode.
	 *
	 * @param actionCode the actionCode to set
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Get the candidateWorkRequest.
	 *
	 * @return the candidateWorkRequest
	 */
	public CandidateWorkRequest getCandidateWorkRequest() {
		return candidateWorkRequest;
	}

	/**
	 * Set the candidateWorkRequest.
	 *
	 * @param candidateWorkRequest the candidateWorkRequest to set
	 */
	public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
		this.candidateWorkRequest = candidateWorkRequest;
	}
	
	/**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setWorkRequestId() {
        if (this.getKey().getWorkRequestId() == null) {
            this.getKey().setWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
        }
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

		CandidateProductScanCodeNutrient that = (CandidateProductScanCodeNutrient) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
	 * different hash codes.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
	@Override
	public String toString() {
		return "CandidateProductScanCodeNutrient{" +
				"key=" + key +
				", createDate=" + createDate +
				", createUserId='" + createUserId + '\'' +
				", actionCode='" + actionCode + '\'' +
				'}';
	}
}