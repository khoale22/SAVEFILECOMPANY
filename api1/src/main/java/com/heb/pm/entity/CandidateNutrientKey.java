/*
 *  CandidateNutrient
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * The primary key class for the PS_NUTRIENT database table.
 *
 * @author vn87351
 * @since 2.12.0
 */
public class CandidateNutrientKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	@Column(name="PS_WORK_ID", insertable=false, updatable=false)
	private Long candidateWorkId;

	@Column(name="SCN_CD_ID")
	private Long upc;

	@Column(name="SEQ_NBR")
	private Long sequence;

	public Long getCandidateWorkId() {
		return candidateWorkId;
	}

	public CandidateNutrientKey setCandidateWorkId(Long candidateWorkId) {
		this.candidateWorkId = candidateWorkId;
		return this;
	}

	public Long getUpc() {
		return upc;
	}

	public CandidateNutrientKey setUpc(Long upc) {
		this.upc = upc;
		return this;
	}

	public Long getSequence() {
		return sequence;
	}

	public CandidateNutrientKey setSequence(Long sequence) {
		this.sequence = sequence;
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
		if (!(o instanceof CandidateNutrientKey)) return false;

		CandidateNutrientKey that = (CandidateNutrientKey) o;

		if (!candidateWorkId.equals(that.candidateWorkId)) return false;
		if (!upc.equals(that.upc)) return false;
		return sequence.equals(that.sequence);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = candidateWorkId.hashCode();
		result = 31 * result + upc.hashCode();
		result = 31 * result + sequence.hashCode();
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "CandidateNutrientKey{" +
				"candidateWorkId=" + candidateWorkId +
				", upc='" + upc + '\'' +
				", sequence='" + sequence + '\'' +
				'}';
	}
}
