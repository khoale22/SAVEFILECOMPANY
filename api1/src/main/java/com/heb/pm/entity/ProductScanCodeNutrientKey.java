/*
 *  ProductScanCodeNutrientKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents a product scan code nutrient data key for table product scan code nutrient.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Embeddable
public class ProductScanCodeNutrientKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "scn_cd_id")
	private Long scanCodeId;

	@Column(name = "prod_ntrntl_cd")
	@Type(type = "fixedLengthCharPK")
	private String productNutrientCode;

	/**
	 * @return Gets the value of scanCodeId and returns scanCodeId
	 */
	public void setScanCodeId(Long scanCodeId) {
		this.scanCodeId = scanCodeId;
	}

	/**
	 * Sets the scanCodeId
	 */
	public Long getScanCodeId() {
		return scanCodeId;
	}

	/**
	 * @return Gets the value of productNutrientCode and returns productNutrientCode
	 */
	public void setProductNutrientCode(String productNutrientCode) {
		this.productNutrientCode = productNutrientCode;
	}

	/**
	 * Sets the productNutrientCode
	 */
	public String getProductNutrientCode() {
		return productNutrientCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductScanCodeNutrientKey)) return false;

		ProductScanCodeNutrientKey that = (ProductScanCodeNutrientKey) o;

		if (getScanCodeId() != null ? !getScanCodeId().equals(that.getScanCodeId()) : that.getScanCodeId() != null)
			return false;
		return getProductNutrientCode() != null ? getProductNutrientCode().equals(that.getProductNutrientCode()) : that.getProductNutrientCode() == null;
	}

	@Override
	public int hashCode() {
		int result = getScanCodeId() != null ? getScanCodeId().hashCode() : 0;
		result = 31 * result + (getProductNutrientCode() != null ? getProductNutrientCode().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProductScanCodeNutrientKey{" +
				"scanCodeId=" + scanCodeId +
				", productNutrientCode='" + productNutrientCode + '\'' +
				'}';
	}
}