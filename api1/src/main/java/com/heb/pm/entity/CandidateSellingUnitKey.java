/*
 * CandidateSellingUnitKey
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
import java.io.Serializable;

/**
 * Represents a candidate retail selling unit data key that is extended attributes for UPCs.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Embeddable
public class CandidateSellingUnitKey implements Serializable {

	@Column(name="ps_prod_id")
	private Long candidateProductId;

	@Column(name = "scn_cd_seq_nbr")
	private Long sequenceNumber;

	/**
	 * @return Gets the value of candidateProductId and returns candidateProductId
	 */
	public void setCandidateProductId(Long candidateProductId) {
		this.candidateProductId = candidateProductId;
	}

	/**
	 * Sets the candidateProductId
	 */
	public Long getCandidateProductId() {
		return candidateProductId;
	}

	/**
	 * Sets the sequenceNumber
	 */
	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @return Gets the value of sequenceNumber and returns sequenceNumber
	 */
	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Compares another object to this one. If that object is an CandidateSellingUnitKey, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateSellingUnitKey)) return false;

		CandidateSellingUnitKey that = (CandidateSellingUnitKey) o;

		if (getCandidateProductId() != null ? !getCandidateProductId().equals(that.getCandidateProductId()) : that.getCandidateProductId() != null)
			return false;
		return getSequenceNumber() != null ? getSequenceNumber().equals(that.getSequenceNumber()) : that.getSequenceNumber() == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = getCandidateProductId() != null ? getCandidateProductId().hashCode() : 0;
		result = 31 * result + (getSequenceNumber() != null ? getSequenceNumber().hashCode() : 0);
		return result;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "CandidateSellingUnitKey{" +
				"candidateProductId=" + candidateProductId +
				", sequenceNumber=" + sequenceNumber +
				'}';
	}
}
