/*
 * DsdLocation
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
 * Represents a dsd location. This represents the vendor from whom we purchase DSD products.
 *
 * @author l730832
 * @since 2.5.0
 */
@Entity
@Table(name = "dsd_loc")
public class DsdLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DsdLocationKey key;

	@Column(name = "trusted_vend_sw")
	private Boolean trustedVendSwitch;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 **/
	public DsdLocationKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 **/
	public void setKey(DsdLocationKey key) {
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
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "DsdLocation{" +
				"key=" + key +
				", trustedVendSwitch=" + trustedVendSwitch +
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

		DsdLocation that = (DsdLocation) o;

		if (key != null ? !key.equals(that.key) : that.key != null) return false;
		return trustedVendSwitch != null ? trustedVendSwitch.equals(that.trustedVendSwitch) : that.trustedVendSwitch == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (trustedVendSwitch != null ? trustedVendSwitch.hashCode() : 0);
		return result;
	}
}
