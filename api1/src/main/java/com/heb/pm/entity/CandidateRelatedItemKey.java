/*
 * CandidateRelatedItemKey
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
 * Represents a ps_related_items data key
 *
 * @author vn70516
 * @since 2.12.0
 */
@Embeddable
public class CandidateRelatedItemKey implements Serializable {

	@Column(name="ps_itm_id")
	private Integer candidateItemId;

	@Column(name = "ps_related_itm_id")
	private Integer candidateRelatedItemCode;

	/**
	 * @return Gets the value of candidateItemId and returns candidateItemId
	 */
	public void setCandidateItemId(Integer candidateItemId) {
		this.candidateItemId = candidateItemId;
	}

	/**
	 * Sets the candidateItemId
	 */
	public Integer getCandidateItemId() {
		return candidateItemId;
	}

	/**
	 * @return Gets the value of candidateRelatedItemCode and returns candidateRelatedItemCode
	 */
	public void setCandidateRelatedItemCode(Integer candidateRelatedItemCode) {
		this.candidateRelatedItemCode = candidateRelatedItemCode;
	}

	/**
	 * Sets the candidateRelatedItemCode
	 */
	public Integer getCandidateRelatedItemCode() {
		return candidateRelatedItemCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateRelatedItemKey)) return false;

		CandidateRelatedItemKey that = (CandidateRelatedItemKey) o;

		if (getCandidateItemId() != null ? !getCandidateItemId().equals(that.getCandidateItemId()) : that.getCandidateItemId() != null)
			return false;
		return getCandidateRelatedItemCode() != null ? getCandidateRelatedItemCode().equals(that.getCandidateRelatedItemCode()) : that.getCandidateRelatedItemCode() == null;
	}

	@Override
	public int hashCode() {
		int result = getCandidateItemId() != null ? getCandidateItemId().hashCode() : 0;
		result = 31 * result + (getCandidateRelatedItemCode() != null ? getCandidateRelatedItemCode().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CandidateRelatedItemKey{" +
				"candidateItemId=" + candidateItemId +
				", candidateRelatedItemCode=" + candidateRelatedItemCode +
				'}';
	}
}
