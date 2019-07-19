/*
 * WarehouseLocationItemKey
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Composite key for WarehouseLocationItem.
 *
 * @author vn40486
 * @since 2.0.1
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class WarehouseLocationItemKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "itm_key_typ_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String itemType;

	@Column(name = "itm_id")
	private long itemCode;

	@Column(name = "whse_loc_typ_cd")
	private String warehouseType;

	@Column(name = "whse_loc_nbr")
	private int warehouseNumber;

	/**
	 * Returns the item code for the WarehouseLocationItem object.
	 *
	 * @return The item code for the WarehouseLocationItem object.
	 */
	public long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the item code for the WarehouseLocationItem object.
	 *
	 * @param itemCode The item code for the WarehouseLocationItem object.
	 */
	public void setItemCode(long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the item type for the WarehouseLocationItem object.
	 *
	 * @return The item type for the WarehouseLocationItem object.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Returns the item type for the WarehouseLocationItem object.
	 *
	 * @param itemType The item type for the WarehouseLocationItem object.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns the warehouse type for the WarehouseLocationItem object.
	 *
	 * @return The warehouse type for the WarehouseLocationItem object.
	 */
	public String getWarehouseType() {
		return warehouseType;
	}

	/**
	 * Sets the warehouse type for the WarehouseLocationItem object.
	 *
	 * @param warehouseType The warehouse type for the WarehouseLocationItem object.
	 */
	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}

	/**
	 * Returns the warehouse number for the WarehouseLocationItem object.
	 *
	 * @return The warehouse number for the WarehouseLocationItem object.
	 */
	public int getWarehouseNumber() {
		return warehouseNumber;
	}

	/**
	 * Sets the warehouse number for the WarehouseLocationItem object.
	 *
	 * @param warehouseNumber The warehouse number for the WarehouseLocationItem object.
	 */
	public void setWarehouseNumber(int warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}


	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "WarehouseLocationItemKey{" +
				"itemType='" + itemType + '\'' +
				", itemCode=" + itemCode +
				", warehouseType=" + warehouseType +
				", warehouseNumber=" + warehouseNumber +
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
		if (!(o instanceof WarehouseLocationItemKey)) return false;

		WarehouseLocationItemKey that = (WarehouseLocationItemKey) o;

		if (itemCode != that.itemCode) return false;
		if (warehouseNumber != that.warehouseNumber) return false;
		if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) return false;
		return !(warehouseType != null ? !warehouseType.equals(that.warehouseType) : that.warehouseType != null);

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
		result = WarehouseLocationItemKey.PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> WarehouseLocationItemKey.FOUR_BYTES));
		result = WarehouseLocationItemKey.PRIME_NUMBER * result + (warehouseType != null ? warehouseType.hashCode() : 0);
		result = WarehouseLocationItemKey.PRIME_NUMBER * result + warehouseNumber;
		return result;
	}
}
