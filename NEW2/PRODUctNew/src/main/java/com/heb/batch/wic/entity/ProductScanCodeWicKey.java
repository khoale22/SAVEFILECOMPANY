/*
 *  WicKey
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This is the embedded key for ProductScanCodeWic.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Embeddable
public class ProductScanCodeWicKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "scn_cd_id")
	private Long upc;

	@Column(name = "wic_apl_id")
	private Long wicApprovedProductListId;

	@Column(name = "wic_cat_id")
	private Long wicCategoryId;

	@Column(name = "wic_sub_cat_id")
	private Long wicSubCatId;

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
	public Long getWicSubCatId() {
		return wicSubCatId;
	}

	/**
	 * Sets the WicSubCategoryId
	 *
	 * @param wicSubCatId The WicSubCategoryId
	 */
	public void setWicSubCatId(Long wicSubCatId) {
		this.wicSubCatId = wicSubCatId;
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

		ProductScanCodeWicKey sellingUnitWicKey = (ProductScanCodeWicKey) o;

		if (upc != null ? !upc.equals(sellingUnitWicKey.upc) : sellingUnitWicKey.upc != null) return false;
		if (wicApprovedProductListId != null ? !wicApprovedProductListId.equals(sellingUnitWicKey.wicApprovedProductListId) : sellingUnitWicKey.wicApprovedProductListId != null)
			return false;
		if (wicCategoryId != null ? !wicCategoryId.equals(sellingUnitWicKey.wicCategoryId) : sellingUnitWicKey.wicCategoryId != null)
			return false;
		return wicSubCatId != null ? wicSubCatId.equals(sellingUnitWicKey.wicSubCatId) : sellingUnitWicKey.wicSubCatId == null;
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
		result = PRIME_NUMBER * result + (wicSubCatId != null ? wicSubCatId.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ProductScanCodeWicKey{" +
				"upc=" + upc +
				", wicApprovedProductListId=" + wicApprovedProductListId +
				", wicCategoryId=" + wicCategoryId +
				", wicSubCatId=" + wicSubCatId +
				'}';
	}
}
