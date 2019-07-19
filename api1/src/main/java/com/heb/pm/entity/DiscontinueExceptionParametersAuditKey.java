/*
 *
 *  DiscontinueExceptionParametersAuditKey
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Key for the DiscontinueExceptionParametersAudit.
 *
 * @author s573181
 * @since 2.0.3
 */
@Embeddable
public class DiscontinueExceptionParametersAuditKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	/**
	 * The Discontinue Exception Parameters audit key.
	 */
	@Embedded
	DiscontinueParametersAuditKey parametersAuditKey = new DiscontinueParametersAuditKey();

	@Column(name = "excp_seq_nbr")
	private int exceptionNumber;

	/**
	 * Returns the ID for this object. This combined with the sequence number defines what type of
	 * rule this exception is.
	 *
	 * @return The ID.
	 */
	public int getId() {
		return this.parametersAuditKey.getId();
	}

	/**
	 * Sets the ID for this object.
	 *
	 * @param id The ID for this object.
	 */
	public void setId(int id) {
		this.parametersAuditKey.setId(id);
	}

	/**
	 * Returns the sequence number for this object. This combined with the ID defines what type of
	 * rule this exception is.
	 *
	 * @return The sequence number for this object
	 */
	public int getSequenceNumber() {
		return  this.parametersAuditKey.getSequenceNumber();
	}

	/**
	 * Sets the sequence number for this object
	 *
	 * @param sequenceNumber The sequence number for this object.
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.parametersAuditKey.setSequenceNumber(sequenceNumber);
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
	 * Returns the timestamp of when the audit was created.
	 *
	 * @return The timestamp of when the audit was created.
	 */
	public LocalDateTime getTimestamp() {
		return this.parametersAuditKey.timestamp;
	}

	/**
	 * Sets the timestamp of when the audit was created.
	 * @param timestamp The timestamp of when the audit was created.
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.parametersAuditKey.setTimestamp(timestamp);
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
		if (!(o instanceof DiscontinueExceptionParametersAuditKey)) return false;

		DiscontinueExceptionParametersAuditKey that = (DiscontinueExceptionParametersAuditKey) o;

		if (exceptionNumber != that.exceptionNumber) return false;
		return !(parametersAuditKey != null ? !parametersAuditKey.equals(that.parametersAuditKey) : that.parametersAuditKey != null);

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = parametersAuditKey != null ? parametersAuditKey.hashCode() : 0;
		result = DiscontinueExceptionParametersAuditKey.PRIME_NUMBER * result + exceptionNumber;
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueExceptionParametersAuditKey{" +
				"parametersAuditKey=" + parametersAuditKey +
				", exceptionNumber=" + exceptionNumber +
				'}';
	}
}
