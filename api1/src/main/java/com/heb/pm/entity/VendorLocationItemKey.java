/*
 * VendorLocationItemKey
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
 * Composite key for VendorLocationItem.
 *
 * @author m314029
 * @since 2.2.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class VendorLocationItemKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "itm_key_typ_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK")  
	private String itemType;

	@Column(name = "itm_id")
	private Long itemCode;

	@Column(name = "vend_loc_typ_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK")  
	private String vendorType;

	@Column(name = "vend_loc_nbr")
	private Integer vendorNumber;

	/**
	 * Gets the item type for the VendorLocationItem.
	 *
	 * @return The item type.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Sets the item type for the VendorLocationItem.
	 *
	 * @param itemType The item type.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Gets the item code for the VendorLocationItem.
	 *
	 * @return The item code.
	 */
	public Long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the item code for the VendorLocationItem.
	 *
	 * @param itemCode The item code.
	 */
	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Gets the vendor type for the VendorLocationItem.
	 *
	 * @return The vendor type.
	 */
	public String getVendorType() {
		return vendorType;
	}

	/**
	 * Sets the vendor type for the VendorLocationItem.
	 *
	 * @param vendorType The vendor type.
	 */
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	/**
	 * Gets the vendor number for the VendorLocationItem.
	 *
	 * @return The vendor number.
	 */
	public Integer getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * Sets the vendor number for the VendorLocationItem.
	 *
	 * @param vendorNumber The vendor number.
	 */
	public void setVendorNumber(Integer vendorNumber) {
		this.vendorNumber = vendorNumber;
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
		if (!(o instanceof VendorLocationItemKey)) return false;

		VendorLocationItemKey that = (VendorLocationItemKey) o;

		if (itemCode != that.itemCode) return false;
		if (vendorNumber != that.vendorNumber) return false;
		if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) return false;
		return !(vendorType != null ? !vendorType.equals(that.vendorType) : that.vendorType != null);

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
		result = VendorLocationItemKey.PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> VendorLocationItemKey.FOUR_BYTES));
		result = VendorLocationItemKey.PRIME_NUMBER * result + (vendorType != null ? vendorType.hashCode() : 0);
		result = VendorLocationItemKey.PRIME_NUMBER * result + vendorNumber;
		return result;
	}
}
