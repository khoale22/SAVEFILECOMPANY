/*
 * ProductOnlineAuditKey
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heb.util.audit.AuditableField;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a product online data key for table product online audit.
 *
 * @author vn86116
 * @since 2.18.4
 */
@Embeddable
public class ProductOnlineAuditKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	@Column(name = "prod_id")
	private Long productId;

	@Column(name = "sals_chnl_cd")
	@Type(type = "fixedLengthCharPK")
	private String salesChannelCode;

	@AuditableField(displayName = "Effective Date")
	@Column(name = "eff_dt")
	@JsonFormat(
			pattern=DATE_FORMAT,
			shape=JsonFormat.Shape.STRING)
	private LocalDate effectiveDate;

	@Column(name = "aud_rec_cre8_ts", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0, insertable = false, updatable = false)
	private LocalDateTime changedOn;

	/**
	 * Get Product Id
	 * @return productId the Product Id
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Set product id
	 * @param productId the product id
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Get Sale Channel Code
	 * @return saleChannelCode
	 */
	public String getSalesChannelCode() {
		return salesChannelCode;
	}

	/**
	 * Set Sale Channel Code
	 * @param saleChannelCode the Sale Channel Code
	 */
	public void setSalesChannelCode(String saleChannelCode) {
		this.salesChannelCode = salesChannelCode;
	}

	/**
	 * Get Effective Date
	 * @return effectiveDate the Effective Date
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Set Effective Date
	 * @param effectiveDate the Effective Date
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Get Changed On
	 * @return changedOn the Changed On
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Set Changed On
	 * @param changedOn the Changed On
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductOnlineAuditKey that = (ProductOnlineAuditKey) o;
		return Objects.equals(changedOn, that.changedOn) &&
				Objects.equals(productId, that.productId);
	}

	@Override
	public int hashCode() {

		return Objects.hash(changedOn, productId);
	}

	@Override
	public String toString() {
		return "ProductMasterAuditKey{" +
				"changedOn=" + changedOn +
				", prodId=" + productId +
				'}';
	}
}
