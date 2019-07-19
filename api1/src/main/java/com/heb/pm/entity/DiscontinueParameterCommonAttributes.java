/*
 * DiscontinueCommonAttributes
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Holds attributes that are common between product discontinue default rules and product discontinue exception
 * rules.
 *
 * @author d116773
 * @since 2.0.2
 */
@Embeddable
public class DiscontinueParameterCommonAttributes {

	private static final int PRIME_NUMBER = 31;

	@Column(name = "parm_val_txt")
	protected String parameterValue;

	@Column(name = "prty_nbr")
	protected int priority;

	@Column(name = "actv_sw")
	protected boolean active;

	/**
	 * Returns the value for the discontinue job to use for this rule. For example, if this record represents
	 * a last scan date for a vendor, it is the number of days since the last scan for a UPC attached to an item before
	 * the item can be discontinued.
	 *
	 * @return The value for the discontinue job to use for this rule.
	 */
	public String getParameterValue() {
		return parameterValue;
	}

	/**
	 * Sets value for the discontinue job to use for this rule.
	 *
	 * @param parameterValue The value for the discontinue job to use for this rule.
	 */
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	/**
	 * Returns the priority of this rule.
	 *
	 * @return The priority of this rule.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Sets the priority of this rule.
	 *
	 * @param priority The priority of this rule.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Returns whether or not this rule is active.
	 *
	 * @return True if this rule is active and false otherwise.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets whether or not this rule is active.
	 *
	 * @param active True if this rule is active and false otherwise.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Tests for equality against an object. Equality is based on all fields.
	 *
	 * @param o The object to test against.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DiscontinueParameterCommonAttributes that = (DiscontinueParameterCommonAttributes) o;

		if (active != that.active) return false;
		if (priority != that.priority) return false;
		if (parameterValue != null ? !parameterValue.trim().equals(that.parameterValue.trim()) : that.parameterValue != null)
			return false;

		return true;
	}

	/**
	 * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects (probably) have
	 * different hash codes.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = parameterValue != null ? parameterValue.hashCode() : 0;
		result = PRIME_NUMBER * result + priority;
		result = PRIME_NUMBER * result + (active ? 1 : 0);
		return result;
	}
}
