/*
 *  VendorItemFactoryKey
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

/**
 * Represents a Vendor Import Factory key.
 * @author s573181
 * @since 2.6.0
 */
@Embeddable
public class VendorItemFactoryKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;
	
	@Column(name = "fctry_id")
	private Integer factoryId;

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
	 * Returns the factory id.
	 *
	 * @return the factory id.
	 */
	public Integer getFactoryId() {
		return factoryId;
	}

	/**
	 * Sets the factory id.
	 *
	 * @param factoryId the factory id.
	 */
	public void setFactoryId(Integer factoryId) {
		this.factoryId = factoryId;
	}

	/**
	 * Returns the item type.
	 * 
	 * @return the item type.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Sets the item type.
	 * 
	 * @param itemType the item type.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns the item code.
	 * 
	 * @return the item code.
	 */
	public Long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the item code.
	 * 
	 * @param itemCode the item code.
	 */
	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the vendor type.
	 *
	 * @return the vendor type.
	 */
	public String getVendorType() {
		return vendorType;
	}

	/**
	 * Sets the vendor type.
	 *
	 * @param vendorType the vendor type.
	 */
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	/**
	 * Returns the vendor number.
	 * 
	 * @return the vendor number.
	 */
	public Integer getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * Sets the vendor number.
	 *
	 * @param vendorNumber the vendor number.
	 */
	public void setVendorNumber(Integer vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	/**
	 * Compares another object to this one. If that object is a VendorItemFactoryKey, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VendorItemFactoryKey)) return false;

		VendorItemFactoryKey that = (VendorItemFactoryKey) o;

		if (!factoryId.equals(that.factoryId)) return false;
		if (!itemType.equals(that.itemType)) return false;
		if (!itemCode.equals(that.itemCode)) return false;
		if (!vendorType.equals(that.vendorType)) return false;
		return vendorNumber.equals(that.vendorNumber);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = factoryId.hashCode();
		result = PRIME_NUMBER * result + itemType.hashCode();
		result = PRIME_NUMBER * result + itemCode.hashCode();
		result = PRIME_NUMBER * result + vendorType.hashCode();
		result = PRIME_NUMBER * result + vendorNumber.hashCode();
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VendorItemFactoryKey{" +
				"factoryId=" + factoryId +
				", itemType='" + itemType + '\'' +
				", itemCode=" + itemCode +
				", vendorType='" + vendorType + '\'' +
				", vendorNumber=" + vendorNumber +
				'}';
	}
}
