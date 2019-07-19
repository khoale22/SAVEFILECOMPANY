package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The key for the ps_prod_mkt_clm table.
 *
 * @author d116773
 * @since 2.13.0
 */
@Embeddable
public class CandidateProductMarketingClaimKey implements Serializable {


	private static final long serialVersionUID = -6691537079176906967L;

	@Column(name = "ps_prod_id")
	private Long CandidateProductId;

	@Column(name="mkt_clm_cd")
	private String marketingClaimCode;

	/**
	 * Returns the candidate product ID.
	 *
	 * @return The candidate product ID.
	 */
	public Long getCandidateProductId() {
		return CandidateProductId;
	}

	/**
	 * Sets the candidate product ID.
	 *
	 * @param candidateProductId The candidate product ID.
	 */
	public void setCandidateProductId(Long candidateProductId) {
		CandidateProductId = candidateProductId;
	}

	/**
	 * Returns the marketing claim code.
	 *
	 * @return The marketing claim code.
	 */
	public String getMarketingClaimCode() {
		return marketingClaimCode;
	}

	/**
	 * Sets the marketing claim code.
	 *
	 * @param marketingClaimCode The marketing claim code.
	 */
	public void setMarketingClaimCode(String marketingClaimCode) {
		this.marketingClaimCode = marketingClaimCode;
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateProductMarketingClaimKey)) return false;

		CandidateProductMarketingClaimKey that = (CandidateProductMarketingClaimKey) o;

		if (CandidateProductId != null ? !CandidateProductId.equals(that.CandidateProductId) : that.CandidateProductId != null)
			return false;
		return !(marketingClaimCode != null ? !marketingClaimCode.equals(that.marketingClaimCode) : that.marketingClaimCode != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = CandidateProductId != null ? CandidateProductId.hashCode() : 0;
		result = 31 * result + (marketingClaimCode != null ? marketingClaimCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "CandidateProductMarketingClaimKey{" +
				"CandidateProductId=" + CandidateProductId +
				", marketingClaimCode='" + marketingClaimCode + '\'' +
				'}';
	}
}
