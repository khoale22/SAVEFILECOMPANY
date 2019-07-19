/*
 * com.heb.pm.entity.ItemMasterKey
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
 * Composite key for Item_Master.
 *
 * @author d116773
 * @since 2.0.0
 */
@Embeddable
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ItemMasterKey implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String DSD = "DSD";
	public static final String WAREHOUSE = "ITMCD";

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name="itm_key_typ_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String itemType;

	@Column(name="itm_id")
	private Long itemCode;

	/**
	 * Returns the item code for the ProductDiscontinue object.
	 *
	 * @return The item code for the ProductDiscontinue object.
	 */
	public Long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the item code for the ProductDiscontinue object.
	 *
	 * @param itemCode The item code for the ProductDiscontinue object.
	 */
	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the item type for the ProductDiscontinue object.
	 *
	 * @return The item type for the ProductDiscontinue object.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Returns the item type for the ProductDiscontinue object.
	 *
	 * @param itemType The item type for the ProductDiscontinue object.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns whether or not this item is DSD.
	 *
	 * @return True if the item is DSD and false otherwise.
	 */
	public boolean isDsd() {
		return ItemMasterKey.DSD.equals(this.getItemType());
	}

	/**
	 * Returns whether or not this item is warehouse.
	 *
	 * @return True if the item is warehouse and false otherwise.
	 */
	public boolean isWarehouse() {
		return ItemMasterKey.WAREHOUSE.equals(this.getItemType());
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
		if (!(o instanceof ItemMasterKey)) return false;

		ItemMasterKey that = (ItemMasterKey) o;

		if (itemCode != that.itemCode) return false;
		return itemType.equals(that.itemType);

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = itemType.hashCode();
		result = ItemMasterKey.PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> ItemMasterKey.FOUR_BYTES));
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ItemMasterKey{" +
				"itemType='" + itemType + '\'' +
				", itemCode=" + itemCode +
				'}';
	}
}
