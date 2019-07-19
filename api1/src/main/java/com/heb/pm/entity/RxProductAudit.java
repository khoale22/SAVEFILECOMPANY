/*
 *  RxProductAudit
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents RX product audit information.
 *
 */
@Entity
@Table(name = "rx_prod_aud")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPk", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class RxProductAudit implements Audit, Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RxProductAuditKey key;

	@Column(name = "act_cd")
	private String action;

	@AuditableField(displayName = "NDC",filter = FilterConstants.PHARMACY_AUDIT)
	@Column(name = "ndc_id")
	private String ndcId;

	@Column(name = "lst_updt_uid")
	private String changedBy;

	@AuditableField(displayName = "Drug Schedule",filter = FilterConstants.PHARMACY_AUDIT)
	@Column(name = "drug_sch_typ_cd")
	private String drugScheduleTypeCode;

	/**
	 * Getter for prodId
	 * @return prodId
	 */
	public Long getProdId() {
		return key.getProdId();
	}

	/**
	 * Setter for prodId attribute
	 * @param prodId productId to set
	 */
	public void setProdId(Long prodId) {
		this.key.setProdId(prodId);
	}

	/**
	 * Getter for changedOn attribute
	 * @return changedOn attribute
	 */
	@Override
	public LocalDateTime getChangedOn() {
		return key.getChangedOn();
	}

	/**
	 * Setter for changedOn
	 * @param changedOn The time the modification was done.
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}

	/**
	 * Getter for action
	 * @return action
	 */
	@Override
	public String getAction() {
		return action;
	}

	/**
	 * Setter for action
	 * @param action the action to set
	 */
	@Override
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Getter for ndcId
	 * @return ndcId
	 */
	public String getNdcId() {
		return ndcId;
	}

	/**
	 *Setter for ndcId
	 * @param ndcId to set
	 */
	public void setNdcId(String ndcId) {
		this.ndcId = ndcId;
	}

	/**
	 * Getter for changedBy
	 * @return changedBy
	 */
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Setter for changedBy
	 * @param changedBy
	 */
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RxProductAudit that = (RxProductAudit) o;
		return Objects.equals(key, that.key) &&
				Objects.equals(action, that.action) &&
				Objects.equals(ndcId, that.ndcId) &&
				Objects.equals(changedBy, that.changedBy) &&
				Objects.equals(drugScheduleTypeCode, that.drugScheduleTypeCode);
	}

	@Override
	public int hashCode() {

		return Objects.hash(key, action, ndcId, changedBy, drugScheduleTypeCode);
	}

	/**
	 * Getter for key attribute
	 * @return key attribute
	 */
	public RxProductAuditKey getKey() {
		return key;

	}

	/**
	 * Setter for key attribute
	 * @param key to set attribute
	 */
	public void setKey(RxProductAuditKey key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "RxProductAudit{" +
				"key=" + key +
				", action='" + action + '\'' +
				", ndcId='" + ndcId + '\'' +
				", changedBy='" + changedBy + '\'' +
				", drugScheduleTypeCode='" + drugScheduleTypeCode + '\'' +
				'}';
	}

	/**

	 * Getter for drugScheduleTypeCode
	 * @return drugScheduleTypeCode
	 */
	public String getDrugScheduleTypeCode() {
		return drugScheduleTypeCode;
	}

	/**
	 * Setter for drugScheduleTypeCode
	 * @param drugScheduleTypeCode
	 */
	public void setDrugScheduleTypeCode(String drugScheduleTypeCode) {
		this.drugScheduleTypeCode = drugScheduleTypeCode;
	}
}
