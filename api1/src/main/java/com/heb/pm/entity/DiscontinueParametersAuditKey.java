/*
 *
 *  DiscontinueParametersAuditKey
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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Key for the DiscontinueParametersAudit.
 *
 * @author s573181
 * @since 2.0.3
 */
@Embeddable
public class DiscontinueParametersAuditKey implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "cre8_ts")
	protected LocalDateTime timestamp;

	@Column(name = "sys_gend_id")
	private int id;

	@Column(name = "seq_nbr")
	private int sequenceNumber;

	/**
	 * Returns the timestamp of when the audit was created.
	 *
	 * @return The timestamp of when the audit was created.
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp of when the audit was created.
	 * @param timestamp The timestamp of when the audit was created.
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the ID for this object. This combined with the sequence number not only makes the record unique,
	 * but it also defines what type of rule this exception is.
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
	 * Returns the sequence number for this object. This combined with the ID  not only makes the record unique,
	 * but it also defines what type of rule this exception is.
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
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DiscontinueParametersAuditKey)) return false;

		DiscontinueParametersAuditKey that = (DiscontinueParametersAuditKey) o;

		if (id != that.id) return false;
		if (sequenceNumber != that.sequenceNumber) return false;
		return !(timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null);

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = timestamp != null ? timestamp.hashCode() : 0;
		result = DiscontinueParametersAuditKey.PRIME_NUMBER * result + id;
		result = DiscontinueParametersAuditKey.PRIME_NUMBER * result + sequenceNumber;
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueParametersAuditKey{" +
				"timestamp='" + timestamp + '\'' +
				", id=" + id +
				", sequenceNumber=" + sequenceNumber +
				'}';
	}
}
