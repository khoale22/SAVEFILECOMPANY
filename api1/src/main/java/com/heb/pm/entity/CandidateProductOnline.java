/*
 *  CandidateProductOnline
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Represents a ps product online.
 *
 * @author vn70529
 * @since 2.26.0
 */
@Entity
@Table(name = "ps_product_online")
public class CandidateProductOnline implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String SHOW_ON_SITE_YES = "Y";

	@EmbeddedId
	private CandidateProductOnlineKey key;

	@Column(name = "eff_dt")
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private Date effectiveDate;

	@Column(name = "exprn_dt")
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate expirationDate;

	@Column(name="prod_id_sw")
	@Type(type = "fixedLengthCharPK")
	private String showOnSite;

	@Column(name="cre8_uid")
	@Type(type="fixedLengthChar")
	private String createUserId;

	@Column(name="cre8_ts")
	private LocalDateTime createDate;

	@Column(name = "LST_UPDT_UID")
	private String lastUpdateUserId;

	@Column(name="lst_updt_ts")
	private LocalDateTime lastUpdateDate;

	@JsonIgnoreProperties("candidateProductOnlines")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ps_prod_id", referencedColumnName = "ps_prod_id", insertable = false, updatable = false, nullable = false)
	private CandidateProductMaster candidateProductMaster;

	@JsonIgnoreProperties("candidateProductOnlines")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ps_work_id", referencedColumnName = "ps_work_id", insertable = false, updatable = false, nullable = false)
	private CandidateWorkRequest candidateWorkRequest;

	/**
	 * Sets the Candidate Product Online Key.
	 * @param key the Candidate Product Online Key.
	 */
	public void setKey(CandidateProductOnlineKey key) {
		this.key = key;
	}

	/**
	 * Returns the Candidate Product Online Key.
	 * @return the Candidate Product Online Key.
	 */
	public CandidateProductOnlineKey getKey() {
		return key;
	}

	/**
	 * Called before this object is saved. It will set the ps_prod_id and work request id for the key.
	 */
	@PrePersist
	public void populateKey() {
		if (getKey()!= null) {
			if(getKey().getCandidateProductId() == null) {
				this.getKey().setCandidateProductId(this.candidateProductMaster.getCandidateProductId());
			}
			if(getKey().getWorkRequestId() == null) {
				this.getKey().setWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
			}
		}
	}

	/**
	 * Returns the effective date.
	 *
	 * @return the effective date.
	 */
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	/**
	 * Sets the effective date.
	 *
	 * @param effectiveDate the effective date.
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return Gets the value of expirationDate and returns expirationDate
	 */
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Sets the expirationDate
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @return Gets the value of showOnSite and returns showOnSite
	 */
	public void setShowOnSite(String showOnSite) {
		this.showOnSite = showOnSite;
	}

	/**
	 * Sets the showOnSite
	 */
	public String getShowOnSite() {
		return showOnSite;
	}

	/**
	 * Returns the ID of the user who created this record.
	 *
	 * @return The ID of the user who created this record.
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Sets the ID of the user who created this record.
	 *
	 * @param createUserId The ID of the user who created this record.
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Returns the date and time this record was created.
	 *
	 * @return The date and time this record was created.
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the date and time this record was created.
	 *
	 * @param createDate The date and time this record was created.
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	/**
	 * Returns the one-pass ID of the last user to update this record.
	 *
	 * @return The one-pass ID of the last user to update this record.
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the one-pass ID of the last user to update this record.
	 *
	 * @param lastUpdateUserId The one-pass ID of the last user to update this record.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	/**
	 * Returns the date and time this record was last updated.
	 *
	 * @return The date and time this record was last updated.
	 */
	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * Sets the date and time this record was last updated.
	 *
	 * @param lastUpdateDate The date and time this record was last updated.
	 */
	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * Returns the candidate work request.
	 * @return the candidate work request.
	 */
	public CandidateWorkRequest getCandidateWorkRequest() {
		return candidateWorkRequest;
	}

	/**
	 * Sets the candidate work request.
	 * @param candidateWorkRequest the candidate work request.
	 */
	public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
		this.candidateWorkRequest = candidateWorkRequest;
	}

	/**
	 * Returns the candidate product master this record is tied to.
	 *
	 * @return The candidate product master this record is tied to.
	 */
	public CandidateProductMaster getCandidateProductMaster() {
		return candidateProductMaster;
	}

	/**
	 * Sets the candidate product master this record is tied to.
	 *
	 * @param candidateProductMaster The candidate product master this record is tied to.
	 */
	public void setCandidateProductMaster(CandidateProductMaster candidateProductMaster) {
		this.candidateProductMaster = candidateProductMaster;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateProductOnline)) return false;
		CandidateProductOnline that = (CandidateProductOnline) o;
		if (getKey() != null ? !getKey().equals(that.getCandidateWorkRequest()) : that.getKey() != null) return false;
		if (getShowOnSite() != null ? !getShowOnSite().equals(that.getShowOnSite()) : that.getShowOnSite() != null) return false;
		if (getEffectiveDate() != null ? !getEffectiveDate().equals(that.getEffectiveDate()) : that.getEffectiveDate() != null) return false;
		if (getCreateUserId() != null ? !getCreateUserId().equals(that.getCreateUserId()) : that.getCreateUserId() != null) return false;
		if (getCreateDate() != null ? !getCreateDate().equals(that.getCreateDate()) : that.getCreateDate() != null) return false;
		if (getLastUpdateDate() != null ? !getLastUpdateDate().equals(that.getLastUpdateDate()) : that.getLastUpdateDate() != null) return false;
		if (getLastUpdateUserId() != null ? !getLastUpdateUserId().equals(that.getLastUpdateUserId()) : that.getLastUpdateUserId() != null) return false;
		return getExpirationDate() != null ? getExpirationDate().equals(that.getExpirationDate()) : that.getExpirationDate() == null;
	}

	@Override
	public int hashCode() {
		int result = getKey() != null ? getKey().hashCode() : 0;
		result = 31 * result + (getEffectiveDate() != null ? getEffectiveDate().hashCode() : 0);
		result = 31 * result + (getExpirationDate() != null ? getExpirationDate().hashCode() : 0);
		result = 31 * result + (getCreateUserId() != null ? getCreateUserId().hashCode() : 0);
		result = 31 * result + (getCreateDate() != null ? getCreateDate().hashCode() : 0);
		result = 31 * result + (getLastUpdateDate() != null ? getLastUpdateDate().hashCode() : 0);
		result = 31 * result + (getLastUpdateUserId() != null ? getLastUpdateUserId().hashCode() : 0);
		result = 31 * result + (getShowOnSite() != null ? getShowOnSite().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CandidateProductOnline{" +
				"key=" + key +
				", effectiveDate=" + effectiveDate +
				", expirationDate=" + expirationDate +
				", createDate=" + createDate +
				", createUserId=" + createUserId +
				", lastUpdateUserId=" + lastUpdateUserId +
				", lastUpdateDate=" + lastUpdateDate +
				", showOnSite=" + showOnSite +
				'}';
	}
}