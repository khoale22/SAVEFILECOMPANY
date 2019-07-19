package com.heb.pm.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents a record in the ps_prod_mkt_clm table. It stores potential marketing claims tied to a product.
 *
 * @author d116773
 * @since 2.13.0
 */
@Entity
@Table(name="ps_prod_mkt_clm")
public class CandidateProductMarketingClaim {

	public static final String TURN_CODE_ON = "Y";
	public static final String TURN_CODE_OFF = "D";
	public static final String UPDATE_CODE = "N";
	public static final String MARKETING_CLAIM_STATUS_CODE_SUBMIT = "S";

	/**
	 * Represents the values of the known marketing claims.
	 */
	public enum Codes {

		DISTINCTIVE("00001"),
		PRIMO_PICK("00002"),
		OWN_BRAND("00003"),
		GO_LOCAL("00004"),
		TOTALLY_TEXAS("00005"),
		SELECT_INGREDIENTS("00006");

		String code;

		/**
		 * Called to create a value.
		 * @param code The value of the code in the table.
		 */
		Codes(String code) {
			this.code = code;
		}

		/**
		 * Returns the code that represents each value (the value that will go into the database).
		 *
		 * @return The code that represents each value.
		 */
		public String getCode() {
			return this.code;
		}
	}
	@EmbeddedId
	private CandidateProductMarketingClaimKey key;

	@Column(name="mkt_clm_stat_cd")
	private String status;

	@Column(name="stat_chg_rsn_txt")
	private String changeReason;

	@Column(name="new_dta_sw")
	private String newData;

	@Column(name="eff_dt")
	private LocalDate effectiveDate;

	@Column(name="exprn_dt")
	private LocalDate expirationDate;

	@ManyToOne()
	@JoinColumn(name="ps_prod_id", referencedColumnName = "ps_prod_id", insertable = false, updatable = false)
	private CandidateProductMaster candidateProductMaster;

	/**
	 * Called before this object is saved. It will set the ps_prod_id of the key.
	 */
	@PrePersist
	public void populateKey() {
		if (this.key.getCandidateProductId() == null) {
			this.key.setCandidateProductId(this.candidateProductMaster.getCandidateProductId());
		}
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
		if (!(o instanceof CandidateProductMarketingClaim)) return false;

		CandidateProductMarketingClaim that = (CandidateProductMarketingClaim) o;

		return !(key != null ? !key.equals(that.key) : that.key != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public CandidateProductMarketingClaimKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key The key for this object.
	 */
	public void setKey(CandidateProductMarketingClaimKey key) {
		this.key = key;
	}

	/**
	 * Returns the status of the marketing claim.
	 *
	 * @return The status of the marketing claim.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status of the marketing claim.
	 *
	 * @param status The status of the marketing claim.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns the reason for this change.
	 *
	 * @return The reason for this change.
	 */
	public String getChangeReason() {
		return changeReason;
	}

	/**
	 * Sets the reason for this change.
	 *
	 * @param changeReason The reason for this change.
	 */
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}

	/**
	 * Returns what to do with this record. There are constants defined above for valid values.
	 *
	 * @return What to do with this record.
	 */
	public String getNewData() {
		return newData;
	}

	/**
	 * Sets what to do with this record.
	 *
	 * @param newData What to do with this record.
	 */
	public void setNewData(String newData) {
		this.newData = newData;
	}

	/**
	 * Returns the date this value should go into effect.
	 *
	 * @return The date this value should to into effect.
	 */
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * Sets the date this value should go into effect.
	 *
	 * @param effectiveDate The date this value should go into effect.
	 */
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Returns the date this value expires.
	 *
	 * @return The date this value expires.
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Sets the date this value expires.
	 *
	 * @param expirationDate The date this value expires.
	 */
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Returns the candidate product master this record is tied to.
	 *
	 * @return The candidate product master this record is tied to.
	 */
	public CandidateProductMaster getCandidateProductMaster() {
		return candidateProductMaster;
	}

	/**
	 * Sets the candidate product master this record is tied to.
	 *
	 * @param candidateProductMaster The candidate product master this record is tied to.
	 */
	public void setCandidateProductMaster(CandidateProductMaster candidateProductMaster) {
		this.candidateProductMaster = candidateProductMaster;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "CandidateProductMarketingClaim{" +
				"key=" + key +
				", status='" + status + '\'' +
				", changeReason='" + changeReason + '\'' +
				", newData='" + newData + '\'' +
				", effectiveDate=" + effectiveDate +
				", expirationDate=" + expirationDate +
				'}';
	}
}
