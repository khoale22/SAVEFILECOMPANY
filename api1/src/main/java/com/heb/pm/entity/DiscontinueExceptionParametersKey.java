/*
 *
 * DiscontinueExceptionRulesKey
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 *
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Key for DiscontinueExceptionParameters.
 *
 * @author s573181
 * @since 2.0.2
 */
@Embeddable
public class DiscontinueExceptionParametersKey implements DiscontinueParameterCommonKey, Serializable{

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "sys_gend_id")
	private int id;

	@Column(name = "seq_nbr")
	private int sequenceNumber;

	@Column(name = "excp_seq_nbr")
	private int exceptionNumber;

	/**
	 * Returns the ID for this object. This combined with the sequence number defines what type of
	 * rule this exception is.
	 *
	 * @return The ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the ID for this object.
	 *
	 * @param id The ID for this object.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the sequence number for this object. This combined with the ID defines what type of
	 * rule this exception is.
	 *
	 * @return The sequence number for this object
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Sets the sequence number for this object
	 *
	 * @param sequenceNumber The sequence number for this object.
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Returns the exception number for this object. An exception rule is grouped together by these numbers. For
	 * example, if there is a rule set up for a particular vendor, all the components of the rule share this ID.
	 * The ID and sequence number make the components of the rule unique.
	 *
	 * @return The exception number for this object.
	 */
	public int getExceptionNumber() {
		return exceptionNumber;
	}

	/**
	 * Sets the exception number for this object.
	 *
	 * @param exceptionNumber The exception number for this object.
	 */
	public void setExceptionNumber(int exceptionNumber) {
		this.exceptionNumber = exceptionNumber;
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
		if (!(o instanceof DiscontinueExceptionParametersKey)) return false;

		DiscontinueExceptionParametersKey that = (DiscontinueExceptionParametersKey) o;

		if (this.id != that.id) return false;
		if (this.sequenceNumber != that.sequenceNumber) return false;
		return exceptionNumber == that.exceptionNumber;

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = this.id;
		result = DiscontinueExceptionParametersKey.PRIME_NUMBER * result + this.sequenceNumber;
		result = DiscontinueExceptionParametersKey.PRIME_NUMBER * result + exceptionNumber;
		return result;
	}


	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueExceptionRulesKey{" +
				"id=" + this.id +
				", sequenceNumber=" + this.sequenceNumber +
				", exceptionNumber=" + exceptionNumber +
				'}';
	}
}
