/*
 *  CandidateGenericEntityRelationship
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This is the candidate generic entity relationship for the mass updates.
 *
 * @author l730832
 * @since 2.17.0
 */
@Entity
@Table(name = "ps_enty_rlshp")
public class CandidateGenericEntityRelationship implements Serializable {

	@EmbeddedId
	private CandidateGenericEntityRelationshipKey key;

	@Column(name="dflt_parnt_sw")
	private Boolean defaultParent;

	@Column(name="dsply_sw")
	private Boolean display;

	@Column(name="seq_nbr")
	private Long sequence;

	@Column(name="CRE8_UID")
	private String createUserId;

	@Column(name="CRE8_TS")
	private LocalDateTime createDate;

	@Column(name="eff_dt")
	private LocalDate effectiveDate;

	@Column(name="exprn_dt")
	private LocalDate expirationDate;

	@Column(name="lst_updt_uid")
	private String lastUpdatedUserId;

	@Column(name="lst_updt_ts")
	private LocalDateTime lastUpdatedTimestamp;

	@Column(name = "new_dta_sw")
	private String newDataSwitch;

	@Column(name = "actv_sw")
	private String activeSwitch;

	@Transient
	private String actionCode;

	//bi-directional many-to-one association to PsWorkRqst
	@JsonIgnoreProperties({"candidateMasterDataExtensionAttributes", "candidateProductPkVariations", "candidateNutrients", "candidateNutrientPanelHeaders", "candidateGenericEntityRelationship"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PS_WORK_ID" , insertable = false, updatable = false, nullable = false)
	private CandidateWorkRequest candidateWorkRequest;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public CandidateGenericEntityRelationshipKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(CandidateGenericEntityRelationshipKey key) {
		this.key = key;
	}

	/**
	 * Returns the DefaultParent
	 *
	 * @return DefaultParent
	 */
	public Boolean getDefaultParent() {
		return defaultParent;
	}

	/**
	 * Sets the DefaultParent
	 *
	 * @param defaultParent The DefaultParent
	 */
	public void setDefaultParent(Boolean defaultParent) {
		this.defaultParent = defaultParent;
	}

	/**
	 * Returns the Display
	 *
	 * @return Display
	 */
	public Boolean getDisplay() {
		return display;
	}

	/**
	 * Sets the Display
	 *
	 * @param display The Display
	 */
	public void setDisplay(Boolean display) {
		this.display = display;
	}

	/**
	 * Returns the Sequence
	 *
	 * @return Sequence
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * Sets the Sequence
	 *
	 * @param sequence The Sequence
	 */
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	/**
	 * Returns the CreateUserId
	 *
	 * @return CreateUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Sets the CreateUserId
	 *
	 * @param createUserId The CreateUserId
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Returns the CreateDate
	 *
	 * @return CreateDate
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the CreateDate
	 *
	 * @param createDate The CreateDate
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * Returns the EffectiveDate
	 *
	 * @return EffectiveDate
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Sets the EffectiveDate
	 *
	 * @param effectiveDate The EffectiveDate
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Returns the ExpirationDate
	 *
	 * @return ExpirationDate
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Sets the ExpirationDate
	 *
	 * @param expirationDate The ExpirationDate
	 */
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Returns the LastUpdatedUserId
	 *
	 * @return LastUpdatedUserId
	 */
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}

	/**
	 * Sets the LastUpdatedUserId
	 *
	 * @param lastUpdatedUserId The LastUpdatedUserId
	 */
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}

	/**
	 * Returns the LastUpdatedTimestamp
	 *
	 * @return LastUpdatedTimestamp
	 */
	public LocalDateTime getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	/**
	 * Sets the LastUpdatedTimestamp
	 *
	 * @param lastUpdatedTimestamp The LastUpdatedTimestamp
	 */
	public void setLastUpdatedTimestamp(LocalDateTime lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	/**
	 * Returns the NewDataSwitch
	 *
	 * @return NewDataSwitch
	 */
	public String getNewDataSwitch() {
		return newDataSwitch;
	}

	/**
	 * Sets the NewDataSwitch. This is whether or not you are removing a product or adding a product to a hierarchy.
	 *
	 * @param newDataSwitch The NewDataSwitch
	 */
	public void setNewDataSwitch(String newDataSwitch) {
		this.newDataSwitch = newDataSwitch;
	}

	/**
	 * Gets the current candidate work request.
	 * @return
	 */
	public CandidateWorkRequest getCandidateWorkRequest() {
		return candidateWorkRequest;
	}

	/**
	 * Returns the ActiveSwitch
	 *
	 * @return ActiveSwitch
	 */
	public String getActiveSwitch() {
		return activeSwitch;
	}

	/**
	 * Sets the ActiveSwitch
	 *
	 * @param activeSwitch The ActiveSwitch
	 */
	public void setActiveSwitch(String activeSwitch) {
		this.activeSwitch = activeSwitch;
	}

	/**
	 * Sets the candidate work request.
	 * @param candidateWorkRequest
	 * @return
	 */
	public CandidateGenericEntityRelationship setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
		this.candidateWorkRequest = candidateWorkRequest;
		return this;
	}

	/**
	 * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
	 * it is inserted into the work request table.
	 */
	@PrePersist
	public void setCandidateWorkRequestId() {
		if (this.getKey().getWorkRequestId()== null) {
			this.getKey().setWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
		}
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

		CandidateGenericEntityRelationship that = (CandidateGenericEntityRelationship) o;

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
}
