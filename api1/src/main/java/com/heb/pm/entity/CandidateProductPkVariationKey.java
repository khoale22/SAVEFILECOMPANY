/*
 *  CandidateProductPkVariationKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the PS_PROD_PK_VAR database table.
 * @author vn87351
 * @since 2.12.0
 */
@Embeddable
public class CandidateProductPkVariationKey implements Serializable {
	public static long SEQUENCE_ZERO=0L;
	private static final long serialVersionUID = 1L;
	@Column(name="PS_WORK_ID", insertable=false, updatable=false)
	private Long candidateWorkRequestId;

	@Column(name="SCN_CD_ID")
	private Long upc;

	@Column(name="SEQ_NBR")
	private Long sequenceNumber;

	public Long getCandidateWorkRequestId() {
		return candidateWorkRequestId;
	}

	public CandidateProductPkVariationKey setCandidateWorkRequestId(Long candidateWorkRequestId) {
		this.candidateWorkRequestId = candidateWorkRequestId;
		return this;
	}

	public Long getUpc() {
		return upc;
	}

	public CandidateProductPkVariationKey setUpc(Long upc) {
		this.upc = upc;
		return this;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public CandidateProductPkVariationKey setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
		return this;
	}
	/**
	 * Compares another object to this one. If that object is a entity, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateProductPkVariationKey)) return false;

		CandidateProductPkVariationKey that = (CandidateProductPkVariationKey) o;
		if (!candidateWorkRequestId.equals(that.candidateWorkRequestId)) return false;
		if (!upc.equals(that.upc)) return false;
		return sequenceNumber.equals(that.sequenceNumber);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = candidateWorkRequestId.hashCode();
		result = 31 * result + upc.hashCode();
		result = 31 * result + sequenceNumber.hashCode();
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "CandidateProductPkVariationKey{" +
				"candidateWorkRequestId=" + candidateWorkRequestId +
				", upc='" + upc + '\'' +
				", sequenceNumber='" + sequenceNumber + '\'' +
				'}';
	}
}
