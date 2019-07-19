/*
 * VendorLocation
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a vendor location. A vendor location represents a location from which a vendor ships products.
 *
 * @author l730832
 * @since 2.5.0
 */
@Entity
@Table(name = "vendor_location")
public class VendorLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private VendorLocationKey key;

	@Column(name = "trusted_vend_sw")
	private Boolean trustedVendSwitch;

	@Column(name = "mix_itm_cls_sw")
	private Boolean mixedClassVendorSwitch;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 **/
	public VendorLocationKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 **/
	public void setKey(VendorLocationKey key) {
		this.key = key;
	}

	/**
	 * Returns the TrustedVendSwitch
	 *
	 * @return TrustedVendSwitch
	 **/
	public Boolean getTrustedVendSwitch() {
		return trustedVendSwitch;
	}

	/**
	 * Sets the TrustedVendSwitch
	 *
	 * @param trustedVendSwitch The TrustedVendSwitch
	 **/
	public void setTrustedVendSwitch(Boolean trustedVendSwitch) {
		this.trustedVendSwitch = trustedVendSwitch;
	}

	/**
	 * Returns the MixedVendorClassSwitch. The mixed vendor class switch means whether that bicep can support multiple
	 * item classes.
	 *
	 * @return MixedVendorClassSwitch
	 */
	public Boolean getMixedClassVendorSwitch() {
		return mixedClassVendorSwitch;
	}

	/**
	 * Sets the mixedClassVendorSwitch
	 *
	 * @param mixedClassVendorSwitch The mixedClassVendorSwitch
	 */
	public void setMixedClassVendorSwitch(Boolean mixedClassVendorSwitch) {
		this.mixedClassVendorSwitch = mixedClassVendorSwitch;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VendorLocation{" +
				"key=" + key +
				", trustedVendSwitch=" + trustedVendSwitch +
				", mixedClassVendorSwitch=" + mixedClassVendorSwitch +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a VendorLocationItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		VendorLocation that = (VendorLocation) o;

		return key != null ? !key.equals(that.key) : that.key != null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}
}