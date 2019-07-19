/*
 *  VendorItemFactory
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
/**
 * Represents a Vendor Import Factory.
 * @author s573181
 * @since 2.6.0
 */
@Entity
@Table(name = "vend_itm_fctry_xrf")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class VendorItemFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VendorItemFactoryKey key;

	@Column(name = "lst_uid")
	private String lastUpdatedUserID;

	@Column(name = "lst_updt_dt")
	private LocalDate lastUpdatedTimeStamp;


	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fctry_id", referencedColumnName = "fctry_id", insertable = false, updatable = false)
	private Factory factory;

	/**
	 * Retunrs the VendorItemFactoryKey.
	 *
	 * @return the VendorItemFactoryKey.
	 */
	public VendorItemFactoryKey getKey() {
		return key;
	}

	/**
	 * Sets  the VendorItemFactoryKey.
	 *
	 * @param key  the VendorItemFactoryKey.
	 */
	public void setKey(VendorItemFactoryKey key) {
		this.key = key;
	}

	/**
	 * Return the last updated user id.
	 *
	 * @return the last updated user id.
	 */
	public String getLastUpdatedUserID() {
		return lastUpdatedUserID;
	}

	/**
	 * Sets the last updated user id.
	 *
	 * @param lastUpdatedUserID the last updated user id.
	 */
	public void setLastUpdatedUserID(String lastUpdatedUserID) {
		this.lastUpdatedUserID = lastUpdatedUserID;
	}

	/**
	 * Returns the last updated time stamp.
	 *
	 * @return the last update time stamp.
	 */
	public LocalDate getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * Sets the last update time stamp.
	 *
	 * @param lastUpdatedTimeStamp the last updated time stamp.
	 */
	public void setLastUpdatedTimeStamp(LocalDate lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * Returns the Factory object for the object.
	 *
	 * @return the Factory object for the object.
	 */
	public Factory getFactory() {
		return factory;
	}

	/**
	 * Sets the Factory object for the object.
	 *
	 * @param factory the Factory object for the object.
	 */
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	/**
	 * Compares another object to this one. If that object is a VendorItemFactory, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VendorItemFactory)) return false;

		VendorItemFactory that = (VendorItemFactory) o;

		return key.equals(that.key);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return key.hashCode();
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VendorItemFactory{" +
				"key=" + key +
				", lastUpdatedUserID='" + lastUpdatedUserID + '\'' +
				", lastUpdatedTimeStamp='" + lastUpdatedTimeStamp + '\'' +
				'}';
	}
}
