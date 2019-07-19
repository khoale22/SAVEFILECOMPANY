package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This entity uniquely identifies ProductRestrictions
 * @author s753601
 * @version 2.12.0
 */
@Embeddable
public class ProductRestrictionsKey implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "PROD_ID")
	private long prodId;

	@Column(name = "SALS_RSTR_CD")
	private String restrictionCode;

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
		return "ProductRestrictionsKey{" +
				"prodId='" + prodId + '\'' +
				", restrictionCode=" + restrictionCode +
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
		if (!(o instanceof ProductRestrictionsKey)) return false;

		ProductRestrictionsKey that = (ProductRestrictionsKey) o;

		if (prodId != that.prodId) return false;
		if (restrictionCode != null ? !restrictionCode.equals(that.restrictionCode) : that.restrictionCode != null) return false;
		return true;

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result=(int) prodId>>> ProductRestrictionsKey.FOUR_BYTES;
		if(restrictionCode != null){
			result = ProductRestrictionsKey.PRIME_NUMBER * result + restrictionCode.hashCode();
		}
		return result;
	}
}
