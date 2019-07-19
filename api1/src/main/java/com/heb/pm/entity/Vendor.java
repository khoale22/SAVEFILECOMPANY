/*
 * Vendor
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import com.heb.util.ws.MessageField;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

/**
 * A representation of a company that sells to HEB.
 *
 * @author d116773
 * @since 2.0.2
 */
@Document(indexName="vendors", type="vendor")
public class Vendor implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";
	private static final String VENDOR_NUMBER_SORT_FIELD = "vendorNumber";

	@Id
	@MessageField(sourceField = "APNUMBER")
	private int vendorNumber;

	@MessageField(sourceField = "APNAME")
	private String vendorName;

	// This field is there to support searching by vendor numbers with a regular expression.
	@MessageField(sourceField="APNUMBER")
	private String vendorNumberAsString;

	/**
	 * Returns the vendor's ID number.
	 *
	 * @return The vendor's ID.
	 */
	public int getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * Sets the vendor's ID number.
	 *
	 * @param vendorNumber The vendor's ID.
	 */
	public void setVendorNumber(int vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	/**
	 * Returns the vendor's name.
	 *
	 * @return The vendor's name.
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * Set's the vendor's name.
	 *
	 * @param vendorName The vendor's name.
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * Returns the vendor number as a String. This is used to enable searching for a vendor by a partial vendor number
	 * through the index.
	 *
	 * @return The vendor number as a String.
	 */
	public String getVendorNumberAsString() {
		return vendorNumberAsString;
	}

	/**
	 * Sets  th vendor number as a string. Note this does not actually change the ID vendor number, it is a
	 * separate field.
	 *
	 * @param vendorNumberAsString The vendor number as a String.
	 */
	public void setVendorNumberAsString(String vendorNumberAsString) {
		this.vendorNumberAsString = vendorNumberAsString;
	}

	/**
	 * Returns the vendor as it should be displayed on the GUI.
	 *
	 * @return A String representation of the vendor as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		return String.format(Vendor.DISPLAY_NAME_FORMAT, this.vendorName.trim(), this.vendorNumber);
	}

	/**
	 * Returns a unique ID for this vendor as a String. This can be used in things like lists where
	 * you need a unique key and cannot access the components of the key directly.
	 *
	 * @return A unique ID for this vendor as a String
	 */
	public String getNormalizedId() {
		return this.vendorNumberAsString;
	}

	/**
	 * Returns a String representation of the obejct.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Vendor{" +
				"vendorNumber=" + vendorNumber +
				", vendorName='" + vendorName + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. Equality is based on vendor number.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Vendor)) return false;

		Vendor vendor = (Vendor) o;

		return vendorNumber == vendor.vendorNumber;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return vendorNumber;
	}

	/**
	 * Returns the number vendor sort order for the vendor document.
	 *
	 * @return The number vendor sort order for the vendor document.
	 */
	public static Sort getSortByVendorNumber() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, VENDOR_NUMBER_SORT_FIELD)
		);
	}
}
