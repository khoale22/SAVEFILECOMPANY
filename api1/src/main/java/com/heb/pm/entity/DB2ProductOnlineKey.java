/*
 *  DB2ProductOnlineKey.java
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef; 
import org.hibernate.annotations.TypeDefs;

/**
 * Represents a product online data key for table product online.
 *
 * @author vn40486
 * @since 2.17.0
 */
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
//dB2Oracle changes vn00907
})
@Embeddable
public class DB2ProductOnlineKey implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SALES_CHANNEL_HEB_COM = "01";

	@Column(name = "prod_id")
	private Long productId;

	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	@Column(name = "sals_chnl_cd")    
	private String saleChannelCode;

	@Column(name = "eff_dt")
	private Date effectiveDate;

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
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Sets the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DB2ProductOnlineKey)) return false;

		DB2ProductOnlineKey that = (DB2ProductOnlineKey) o;

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
		return "DB2ProductOnlineKey{" +
				"productId=" + productId +
				", saleChannelCode='" + saleChannelCode + '\'' +
				", effectiveDate='" + effectiveDate + '\'' +
				'}';
	}
}