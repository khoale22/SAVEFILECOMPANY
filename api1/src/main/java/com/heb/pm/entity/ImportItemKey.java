/*
 *  ImportItem
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Represents item/vendor information key for import.
 *
 * @author s573181
 * @since 2.5.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ImportItemKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name="itm_key_typ_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")  
	private String itemType;

	@Column(name="itm_id")
	private Long itemCode;

	@Column(name = "vend_loc_typ_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")  
	private String vendorType;

	@Column(name = "vend_loc_nbr")
	private Integer vendorNumber;

	/**
	 * Returns the item code for the ImportItem object.
	 * 
	 * @return the item code for the ImportItem object.
	 */
	public Long getItemCode() {
		return itemCode;
	}
	
	/**
	 * Sets the item code for the ImportItem object.
	 *
	 * @param itemCode The item code for the ImportItem object.
	 */
	public void setItemCode(long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the item type for the ImportItem object.
	 *
	 * @return The item type for the ImportItem object.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Returns the item type for the ImportItem object.
	 *
	 * @param itemType The item type for the ImportItem object.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Gets the vendor type for the ImportItem.
	 *
	 * @return The vendor type.
	 */
	public String getVendorType() {
		return vendorType;
	}

	/**
	 * Sets the vendor type for the ImportItem.
	 *
	 * @param vendorType The vendor type.
	 */
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	/**
	 * Gets the vendor number for the ImportItem.
	 *
	 * @return The vendor number.
	 */
	public Integer getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * Sets the vendor number for the ImportItem.
	 *
	 * @param vendorNumber The vendor number.
	 */
	public void setVendorNumber(int vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	
	/**
	 * Tests for equality with another object. Equality is based on class and commodity.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ImportItemKey that = (ImportItemKey) o;

		return itemCode == that.itemCode && vendorNumber == that.vendorNumber && (itemType != null ?
				itemType.equals(that.itemType) : that.itemType == null) && (vendorType != null ?
				vendorType.equals(that.vendorType) : that.vendorType == null);
	}

	/**
	 * Returns a hash code for this object. Equal objects always return the same value. Unequals objects (probably)
	 * return different values.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = itemType != null ? itemType.hashCode() : 0;
		result = PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> FOUR_BYTES));
		result = PRIME_NUMBER * result + (vendorType != null ? vendorType.hashCode() : 0);
		result = PRIME_NUMBER * result + vendorNumber;
		return result;
	}
}
