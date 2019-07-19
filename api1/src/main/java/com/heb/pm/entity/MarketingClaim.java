/*
 *  MarketingClaim
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * Represents a marketing claim.
 *
 * @author s573181
 * @since 2.6.0
 */
@Entity
@Table(name = "mkt_clm")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class MarketingClaim implements Serializable {

	private static final long serialVersionUID = 1L;

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

	@Id
	@Column(name = "mkt_clm_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String marketingClaimCode;

	@Column(name = "mkt_clm_des")
	private String marketingClaimDescription;

	@Column(name = "mkt_clm_abb")
	private String marketingClaimAbbreviation;

	/**
	 * Returns the marketing claim code.
	 *
	 * @return the marketing claim code.
	 */
	public String getMarketingClaimCode() {
		return marketingClaimCode;
	}

	/**
	 * Sets the marketing claim code.
	 *
	 * @param marketingClaimCode the marketing claim code.
	 */
	public void setMarketingClaimCode(String marketingClaimCode) {
		this.marketingClaimCode = marketingClaimCode;
	}

	/**
	 * Returns the marketing claim description.
	 *
	 * @return the marketing claim description.
	 */
	public String getMarketingClaimDescription() {
		return marketingClaimDescription;
	}

	/**
	 * Sets the marketing claim description.
	 *
	 * @param marketingClaimDescription the marketing claim description.
	 */
	public void setMarketingClaimDescription(String marketingClaimDescription) {
		this.marketingClaimDescription = marketingClaimDescription;
	}

	/**
	 * Returns the marketing claim abbreviation.
	 *
	 * @return the marketing claim abbreviation.
	 */
	public String getMarketingClaimAbbreviation() {
		return marketingClaimAbbreviation;
	}

	/**
	 * Sets the marketing claim abbreviation.
	 *
	 * @param marketingClaimAbbreviation the marketing claim abbreviation.
	 */
	public void setMarketingClaimAbbreviation(String marketingClaimAbbreviation) {
		this.marketingClaimAbbreviation = marketingClaimAbbreviation;
	}

	/**
	 * Compares another object to this one. If that object is a ImportItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MarketingClaim)) return false;

		MarketingClaim that = (MarketingClaim) o;

		return marketingClaimCode.equals(that.marketingClaimCode);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return marketingClaimCode.hashCode();
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "MarketingClaim{" +
				"marketingClaimCode='" + marketingClaimCode + '\'' +
				", marketingClaimDescription='" + marketingClaimDescription + '\'' +
				", marketingClaimAbbreviation='" + marketingClaimAbbreviation + '\'' +
				'}';
	}
}
