/*
 * VendorLocationItemMasterKey
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Composite key for VendorLocationItemMaster.
 *
 * @author l730832
 * @since 2.5.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class VendorLocationItemMasterKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;
	private static final int FOUR_BYTES = 32;

	@Column(name = "itm_key_typ_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK")  
	private String itemType;

	@Column(name = "itm_id")
	private long itemCode;

	@Column(name = "loc_typ_cd")
	@Type(type="fixedLengthCharPK")  
	private String locationType;

	@Column(name = "loc_nbr")
	private Integer locationNumber;

	/**
	 * Returns the ItemCode
	 *
	 * @return ItemCode
	 **/
	public long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the ItemCode
	 *
	 * @param itemCode The ItemCode
	 **/

	public void setItemCode(long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the ItemType
	 *
	 * @return ItemType
	 **/
	public String getItemType() {
		return itemType;
	}

	/**
	 * Sets the ItemType
	 *
	 * @param itemType The ItemType
	 **/

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Get the locationType.
	 *
	 * @return the locationType
	 */
	public String getLocationType() {
		return locationType;
	}

	/**
	 * Set the locationType.
	 *
	 * @param locationType the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * Get the locationNumber.
	 *
	 * @return the locationNumber
	 */
	public Integer getLocationNumber() {
		return locationNumber;
	}

	/**
	 * Set the locationNumber.
	 *
	 * @param locationNumber the locationNumber to set
	 */
	public void setLocationNumber(Integer locationNumber) {
		this.locationNumber = locationNumber;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VendorLocationItemMasterKey{" +
				"itemCode=" + itemCode +
				", itemType='" + itemType + '\'' +
				'}';
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

		VendorLocationItemMasterKey that = (VendorLocationItemMasterKey) o;

		if (itemCode != that.itemCode) return false;
		return itemType != null ? itemType.equals(that.itemType) : that.itemType == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = itemType != null ? itemType.hashCode() : 0;
		result = PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> FOUR_BYTES));
		return result;
	}
}
