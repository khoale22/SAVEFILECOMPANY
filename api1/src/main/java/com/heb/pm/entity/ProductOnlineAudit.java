/*
 * ProductOnlineAudit
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a product online audit.
 *
 * @author vn86116
 * @since 2.18.4
 */
@Entity
@Table(name = "product_online_aud")
public class ProductOnlineAudit implements Serializable, Audit {
	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "MM-dd-yyyy";

	@EmbeddedId
	private ProductOnlineAuditKey key;

	@AuditableField(displayName = "Show On Site - End Date")
	@Column(name = "exprn_dt")
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	@DateTimeFormat()
	private LocalDate expirationDate;

	@AuditableField(displayName = "Show On Site - Start Date")
	@Column(name = "eff_dt", insertable = false, updatable = false)
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate effectiveDate;

	@Column(name="prod_id_sw")
	private boolean showOnSite;

	@Column(name = "act_cd")
	private String action;

	@Column (name = "lst_updt_uid")
	private String changedBy;

	/**
	 * @return return Product Online Audit Key.
	 */
	public ProductOnlineAuditKey getKey() {
		return key;
	}

	/**
	 * Set the key
	 * @param key the ProductOnlineAuditKey
	 */
	public void setKey(ProductOnlineAuditKey key) {
		this.key = key;
	}

	/**
	 * Get Expiration Date.
	 * @return Expiration Date.
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Set Expiration Date.
	 * @param expirationDate the Expiration Date.
	 */
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Get show on site.
	 * @return show on site.
	 */
	public boolean isShowOnSite() {
		return showOnSite;
	}

	/**
	 * Set Show on site.
	 * @param showOnSite the show on site.
	 */
	public void setShowOnSite(boolean showOnSite) {
		this.showOnSite = showOnSite;
	}

	/**
	 * Get Effective Date.
	 * @return Effective Date.
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Set Effective Date.
	 * @param effectiveDate the Effective Date.
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Get Action.
	 * @return the Action.
	 */
	@Override
	public String getAction() {
		return action;
	}

	/**
	 * Set Action.
	 * @param action the action
	 */
	@Override
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Get user id.
	 * @return user id
	 */
	@Override
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Set user id.
	 * @param changedBy the changed by
	 */
	@Override
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Get time change.
	 * @return time change.
	 */
	@Override
	public LocalDateTime getChangedOn() {
		return key.getChangedOn();
	}

	/**
	 * Set time change.
	 * @param changedOn The time the modification was done.
	 */
	@Override
	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}

	@Override
	public int hashCode() {
		int result = getKey() != null ? getKey().hashCode() : 0;
		result = 31 * result + (getExpirationDate() != null ? getExpirationDate().hashCode() : 0);
		result = 31 * result + (isShowOnSite() ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductOnline)) return false;

		ProductOnline that = (ProductOnline) o;

		if (isShowOnSite() != that.isShowOnSite()) return false;
		if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
		return getExpirationDate() != null ? getExpirationDate().equals(that.getExpirationDate()) : that.getExpirationDate() == null;
	}

	@Override
	public String toString() {
		return "ProductOnline{" +
				"key=" + key +
				", expirationDate=" + expirationDate +
				", showOnSite=" + showOnSite +
				", action=" + action +
				", changeBy=" +changedBy +
				'}';
	}
}
