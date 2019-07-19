/*
 *
 *  * DiscontinueExceptionParameters
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 *
 */

package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a record in the prod_del_cntl_parm table. These are the default product discontinue rules.
 *
 * @author s573181
 * @since 2.0.2
 */
@Entity
@Table(name = "prod_del_cntl_parm")
public class DiscontinueParameters implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DiscontinueParametersKey key;

	@Embedded
	private DiscontinueParameterCommonAttributes attributes = new DiscontinueParameterCommonAttributes();

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public DiscontinueParametersKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key They key for this object.
	 */
	public void setKey(DiscontinueParametersKey key) {
		this.key = key;
	}

	/**
	 * Returns the value for the discontinue job to use for this rule. For example, if this record represents
	 * a last scan date for a vendor, it is the number of days since the last scan for a UPC attached to an item before
	 * the item can be discontinued.
	 *
	 * @return The value for the discontinue job to use for this rule.
	 */
	public String getParameterValue() {
		return this.attributes.parameterValue;
	}

	/**
	 * Sets value for the discontinue job to use for this rule.
	 *
	 * @param parameterValue The value for the discontinue job to use for this rule.
	 */
	public void setParameterValue(String parameterValue) {
		this.attributes.parameterValue = parameterValue;
	}


	/**
	 * Returns whether or not this rule is active.
	 *
	 * @return True if this rule is active and false otherwise.
	 */
	public boolean isActive() {
		return this.attributes.active;
	}

	/**
	 * Sets whether or not this rule is active.
	 *
	 * @param active True if this rule is active and false otherwise.
	 */
	public void setActive(boolean active) {
		this.attributes.active = active;
	}

	/**
	 * Returns the attributes that are common between default and exception parameters as an object. This should
	 * generally not be used and rather the getters and setters of the attributes themselves, but this is available
	 * where needed.
	 *
	 * @return The attributes that are common between default and exception parameters as an object.
	 */
	public DiscontinueParameterCommonAttributes getAttributes() {
		return attributes;
	}

	/**
	 * Compares another object to this one. If that object is an DiscontinueExceptionParameters, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DiscontinueParameters)) return false;

		DiscontinueParameters that = (DiscontinueParameters) o;

		if (this.key != null ? !this.key.equals(that.key) : that.key != null) return false;

		return true;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they (probably) have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.key == null ? 0 : this.key.hashCode();
	}
	/**
	 * Returns whether a DiscontinueParameters has exactly the same elements as the given
	 * DiscontinueParameters.
	 *
	 * @param discontinueParameters DiscontinueParameters to compare to.
	 * @return true if they are equal, false otherwise.
	 */
	public boolean fullItemCompare(DiscontinueParameters discontinueParameters){
		if (this == discontinueParameters) return true;
		if (discontinueParameters == null || getClass() != discontinueParameters.getClass()) return false;

		if (attributes != null ? !attributes.equals(discontinueParameters.attributes) : discontinueParameters.attributes != null) return false;
		if (key != null ? !key.equals(discontinueParameters.key) : discontinueParameters.key != null) return false;

		return true;
	}


	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueParameters{" +
				"key=" + key +
				", parameterValue='" + this.attributes.parameterValue + '\'' +
				", active=" + this.attributes.active +
				'}';
	}
}
