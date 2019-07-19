/*
 *  WicCategoryRetailSizeKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This holds the embedded key for WicCategoryRetailSize.
 *
 * @author l730832
 * @since 2.12.0
 */
@Embeddable
public class WicCategoryRetailSizeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "wic_cat_id")
	private Long wicCategoryId;

	@Column(name = "wic_sub_cat_id")
	private Long wicSubCategoryId;

	@Column(name = "retl_sell_sz_cd")
	private String retailSellSizeCode;

	@Column(name = "retl_unt_sell_sz", precision = 7, scale = 2)
	private Double retailUnitSellSize;

	/**
	 * Returns the WicCategoryId
	 *
	 * @return WicCategoryId
	 */
	public Long getWicCategoryId() {
		return wicCategoryId;
	}

	/**
	 * Sets the WicCategoryId
	 *
	 * @param wicCategoryId The WicCategoryId
	 */
	public void setWicCategoryId(Long wicCategoryId) {
		this.wicCategoryId = wicCategoryId;
	}

	/**
	 * Returns the WicSubCategoryId
	 *
	 * @return WicSubCategoryId
	 */
	public Long getWicSubCategoryId() {
		return wicSubCategoryId;
	}

	/**
	 * Sets the WicSubCategoryId
	 *
	 * @param wicSubCategoryId The WicSubCategoryId
	 */
	public void setWicSubCategoryId(Long wicSubCategoryId) {
		this.wicSubCategoryId = wicSubCategoryId;
	}

	/**
	 * Returns the RetailSellSizeCode
	 *
	 * @return RetailSellSizeCode
	 */
	public String getRetailSellSizeCode() {
		return retailSellSizeCode;
	}

	/**
	 * Sets the RetailSellSizeCode
	 *
	 * @param retailSellSizeCode The RetailSellSizeCode
	 */
	public void setRetailSellSizeCode(String retailSellSizeCode) {
		this.retailSellSizeCode = retailSellSizeCode;
	}

	/**
	 * Returns the RetailUnitSellSize. This is the size of the retail unit. (123.00)
	 *
	 * @return RetailUnitSellSize
	 */
	public Double getRetailUnitSellSize() {
		return retailUnitSellSize;
	}

	/**
	 * Sets the RetailUnitSellSize
	 *
	 * @param retailUnitSellSize The RetailUnitSellSize
	 */
	public void setRetailUnitSellSize(Double retailUnitSellSize) {
		this.retailUnitSellSize = retailUnitSellSize;
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

		WicCategoryRetailSizeKey that = (WicCategoryRetailSizeKey) o;

		if (wicCategoryId != null ? !wicCategoryId.equals(that.wicCategoryId) : that.wicCategoryId != null)
			return false;
		if (wicSubCategoryId != null ? !wicSubCategoryId.equals(that.wicSubCategoryId) : that.wicSubCategoryId != null)
			return false;
		return retailSellSizeCode != null ? retailSellSizeCode.equals(that.retailSellSizeCode) : that.retailSellSizeCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = wicCategoryId != null ? wicCategoryId.hashCode() : 0;
		result = 31 * result + (wicSubCategoryId != null ? wicSubCategoryId.hashCode() : 0);
		result = 31 * result + (retailSellSizeCode != null ? retailSellSizeCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "WicCategoryRetailSizeKey{" +
				"wicCategoryId=" + wicCategoryId +
				", wicSubCategoryId=" + wicSubCategoryId +
				", retailSellSizeCode='" + retailSellSizeCode + '\'' +
				'}';
	}
}
