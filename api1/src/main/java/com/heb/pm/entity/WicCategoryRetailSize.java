/*
 *  WicCategoryRetailSize
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * This is the code table for
 *
 * @author l730832
 * @since 2.12.0
 */
@Entity
@Table(name = "wic_cat_retl_sz")
public class WicCategoryRetailSize implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private WicCategoryRetailSizeKey key;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "retl_sell_sz_cd", referencedColumnName = "retl_sell_sz_cd", insertable = false, updatable = false)
	private RetailSellingUnit retailSellingUnit;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public WicCategoryRetailSizeKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(WicCategoryRetailSizeKey key) {
		this.key = key;
	}

	/**
	 * Returns the RetailSellingUnit
	 *
	 * @return RetailSellingUnit
	 */
	public RetailSellingUnit getRetailSellingUnit() {
		return retailSellingUnit;
	}

	/**
	 * Sets the RetailSellingUnit
	 *
	 * @param retailSellingUnit The RetailSellingUnit
	 */
	public void setRetailSellingUnit(RetailSellingUnit retailSellingUnit) {
		this.retailSellingUnit = retailSellingUnit;
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

		WicCategoryRetailSize that = (WicCategoryRetailSize) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "WicCategoryRetailSize{" +
				"key=" + key +
				'}';
	}
}
