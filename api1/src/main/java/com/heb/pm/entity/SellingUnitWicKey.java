/*
 *  WicKey
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
 * This is the embedded key for SellingUnitWic.
 *
 * @author l730832
 * @since 2.12.0
 */
@Embeddable
public class SellingUnitWicKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "scn_cd_id")
	private Long upc;

	@Column(name = "wic_apl_id")
	private Long wicApprovedProductListId;

	@Column(name = "wic_cat_id")
	private Long wicCategoryId;

	@Column(name = "wic_sub_cat_id")
	private Long wicSubCategoryId;

	/**
	 * Returns the Upc
	 *
	 * @return Upc
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets the Upc
	 *
	 * @param upc The Upc
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Returns the WicApprovedProductListId
	 *
	 * @return WicApprovedProductListId
	 */
	public Long getWicApprovedProductListId() {
		return wicApprovedProductListId;
	}

	/**
	 * Sets the WicApprovedProductListId
	 *
	 * @param wicApprovedProductListId The WicApprovedProductListId
	 */
	public void setWicApprovedProductListId(Long wicApprovedProductListId) {
		this.wicApprovedProductListId = wicApprovedProductListId;
	}

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
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SellingUnitWicKey sellingUnitWicKey = (SellingUnitWicKey) o;

		if (upc != null ? !upc.equals(sellingUnitWicKey.upc) : sellingUnitWicKey.upc != null) return false;
		if (wicApprovedProductListId != null ? !wicApprovedProductListId.equals(sellingUnitWicKey.wicApprovedProductListId) : sellingUnitWicKey.wicApprovedProductListId != null)
			return false;
		if (wicCategoryId != null ? !wicCategoryId.equals(sellingUnitWicKey.wicCategoryId) : sellingUnitWicKey.wicCategoryId != null)
			return false;
		return wicSubCategoryId != null ? wicSubCategoryId.equals(sellingUnitWicKey.wicSubCategoryId) : sellingUnitWicKey.wicSubCategoryId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = upc != null ? upc.hashCode() : 0;
		result = PRIME_NUMBER * result + (wicApprovedProductListId != null ? wicApprovedProductListId.hashCode() : 0);
		result = PRIME_NUMBER * result + (wicCategoryId != null ? wicCategoryId.hashCode() : 0);
		result = PRIME_NUMBER * result + (wicSubCategoryId != null ? wicSubCategoryId.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "SellingUnitWicKey{" +
				"upc=" + upc +
				", wicApprovedProductListId=" + wicApprovedProductListId +
				", wicCategoryId=" + wicCategoryId +
				", wicSubCategoryId=" + wicSubCategoryId +
				'}';
	}
}
