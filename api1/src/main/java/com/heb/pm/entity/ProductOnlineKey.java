/*
 *  ProductOnlineKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a product online data key for table product online.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Embeddable
@TypeDefs({
	@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
	@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductOnlineKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "prod_id")
	private Long productId;

	@Column(name = "sals_chnl_cd")
	@Type(type = "fixedLengthCharPK")
	private String saleChannelCode;

	@Column(name = "eff_dt")
	private LocalDate effectiveDate;

	/**
	 * @return Gets the value of productId and returns productId
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Sets the productId
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * @return Gets the value of saleChannelCode and returns saleChannelCode
	 */
	public void setSaleChannelCode(String saleChannelCode) {
		this.saleChannelCode = saleChannelCode;
	}

	/**
	 * Sets the saleChannelCode
	 */
	public String getSaleChannelCode() {
		return saleChannelCode;
	}

	/**
	 * @return Gets the value of effectiveDate and returns effectiveDate
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Sets the effectiveDate
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductOnlineKey)) return false;

		ProductOnlineKey that = (ProductOnlineKey) o;

		if (getProductId() != null ? !getProductId().equals(that.getProductId()) : that.getProductId() != null)
			return false;
		if (getSaleChannelCode() != null ? !getSaleChannelCode().equals(that.getSaleChannelCode()) : that.getSaleChannelCode() != null)
			return false;
		return getEffectiveDate() != null ? getEffectiveDate().equals(that.getEffectiveDate()) : that.getEffectiveDate() == null;
	}

	@Override
	public int hashCode() {
		int result = getProductId() != null ? getProductId().hashCode() : 0;
		result = 31 * result + (getSaleChannelCode() != null ? getSaleChannelCode().hashCode() : 0);
		result = 31 * result + (getEffectiveDate() != null ? getEffectiveDate().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProductOnlineKey{" +
				"productId=" + productId +
				", saleChannelCode='" + saleChannelCode + '\'' +
				", effectiveDate='" + effectiveDate + '\'' +
				'}';
	}
}