package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This entity uniquely identifies ProductRestrictions audits
 */
@Embeddable
public class ProductRestrictionsAuditKey implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "PROD_ID")
	private long prodId;

	@Column(name = "SALS_RSTR_CD")
	private String restrictionCode;

	@Column(name = "AUD_REC_CRE8_TS")
	private LocalDateTime changedOn;

	@Column(name = "ACT_CD")
	private String action;

	@Column(name = "LST_UPDT_UID")
	private String changedBy;

	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Returns the id of the product that has the restriction
	 * @return
	 */
	public long getProdId() {
		return prodId;
	}

	/**
	 * Updates the prodId
	 * @param prodId the new prodId
	 */
	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	/**
	 * Returns the code for the specific restriction
	 * @return restriction code
	 */
	public String getRestrictionCode() {
		return restrictionCode;
	}

	/**
	 * Updates the resttriction code
	 * @param restrictionCode the new restriction code.
	 */
	public void setRestrictionCode(String restrictionCode) {
		this.restrictionCode = restrictionCode;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ProductRestrictionsAuditKey{" +
				"prodId=" + prodId +
				", restrictionCode='" + restrictionCode + '\'' +
				", changedOn=" + changedOn +
				", action='" + action + '\'' +
				", changedBy='" + changedBy + '\'' +
				'}';
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
		if (o == null || getClass() != o.getClass()) return false;
		ProductRestrictionsAuditKey that = (ProductRestrictionsAuditKey) o;
		return prodId == that.prodId &&
				Objects.equals(restrictionCode, that.restrictionCode) &&
				Objects.equals(changedOn, that.changedOn) &&
				Objects.equals(action, that.action) &&
				Objects.equals(changedBy, that.changedBy);
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {

		return Objects.hash(prodId, restrictionCode, changedOn, action, changedBy);
	}
}
