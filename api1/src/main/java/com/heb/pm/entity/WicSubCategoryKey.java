/*
 *  WicSubCategoryKey
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
 * This is the key for the wic sub category.
 *
 * @author l730832
 * @since 2.12.0
 */
@Embeddable
public class WicSubCategoryKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "wic_cat_id")
	private Long wicCategoryid;

	@Column(name = "wic_sub_cat_id")
	private Long wicSubCategoryId;

	/**
	 * Returns the WicCategoryid
	 *
	 * @return WicCategoryid
	 */
	public Long getWicCategoryid() {
		return wicCategoryid;
	}

	/**
	 * Sets the WicCategoryid
	 *
	 * @param wicCategoryid The WicCategoryid
	 */
	public void setWicCategoryid(Long wicCategoryid) {
		this.wicCategoryid = wicCategoryid;
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

		WicSubCategoryKey that = (WicSubCategoryKey) o;

		if (wicCategoryid != null ? !wicCategoryid.equals(that.wicCategoryid) : that.wicCategoryid != null)
			return false;
		return wicSubCategoryId != null ? wicSubCategoryId.equals(that.wicSubCategoryId) : that.wicSubCategoryId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = wicCategoryid != null ? wicCategoryid.hashCode() : 0;
		result = 31 * result + (wicSubCategoryId != null ? wicSubCategoryId.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "WicSubCategoryKey{" +
				"wicCategoryid=" + wicCategoryid +
				", wicSubCategoryId=" + wicSubCategoryId +
				'}';
	}
}
