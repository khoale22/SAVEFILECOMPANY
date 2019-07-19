/*
 *  PsProdScoreKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import java.io.Serializable;

/**
 *  Represents a ps prod score key. A ps prod score key contains the id of the prod  and scoring org id.
 *
 * @author vn70529
 * @since 2.12
 */
public class CandidateProductScoreKey implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The ps prod id.
	 */
	@Column(name = "PS_PROD_ID", nullable = false)
	private Long candidateProductId;

	/**
	 * The scoring org id.
	 */
	@Column(name = "SCORING_ORG_ID", nullable = false)
	private Integer scoringOrgId;

	/**
	 * Get ps prod id.
	 * @return ps prod id.
	 */
	public Long getCandidateProductId() {
		return candidateProductId;
	}

	/**
	 * Set Ps prod id.
	 *
	 * @param candidateProductId ps prod id.
	 */
	public void setCandidateProductId(Long candidateProductId) {
		this.candidateProductId = candidateProductId;
	}

	/**
	 * Get scoring org id.
	 *
	 * @return scoring org id.
	 */
	public Integer getScoringOrgId() {
		return scoringOrgId;
	}

	/**
	 * Set scoring org id.
	 *
	 * @param scoringOrgId scoring org id
	 */
	public void setScoringOrgId(Integer scoringOrgId) {
		this.scoringOrgId = scoringOrgId;
	}

	/**
	 * Compares another object to this one for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateProductScoreKey)) return false;

		CandidateProductScoreKey that = (CandidateProductScoreKey) o;

		if (candidateProductId != null ? !candidateProductId.equals(that.candidateProductId) : that.candidateProductId != null)
			return false;
		return scoringOrgId != null ? scoringOrgId.equals(that.scoringOrgId) : that.scoringOrgId == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = candidateProductId != null ? candidateProductId.hashCode() : 0;
		result = 31 * result + (scoringOrgId != null ? scoringOrgId.hashCode() : 0);
		return result;
	}
}
