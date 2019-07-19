/*
 *  NutritionalKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This is the embedded key for NutritionalClaims.
 *
 * @author l730832
 * @since 2.11.0
 */

@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
})
@Embeddable
public class NutritionalClaimsKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "scn_cd_id")
	private Long upc;

	@Type(type="fixedLengthChar")
	@Column(name = "prod_ntrntl_cd")
	private String nutritionalClaimsCode;

	/**
	 * Returns the Upc.
	 *
	 * @return Upc
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets the Upc
	 *
	 * @param upc The Upc
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Returns the NutritionalClaimsCode. The nutritional code is used to determine what nutritional information the current
	 * upc contains. If it does not contain that nutritional code then it will not be inside of the db. Only the values
	 * that are associated to it will be included in the db.
	 *
	 * @return NutritionalCode
	 */
	public String getNutritionalClaimsCode() {
		return nutritionalClaimsCode;
	}

	/**
	 * Sets the NutritionalClaimsCode
	 *
	 * @param nutritionalClaimsCode The NutritionalClaimsCode
	 */
	public void setNutritionalClaimsCode(String nutritionalClaimsCode) {
		this.nutritionalClaimsCode = nutritionalClaimsCode;
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

		NutritionalClaimsKey that = (NutritionalClaimsKey) o;

		if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
		return nutritionalClaimsCode != null ? nutritionalClaimsCode.equals(that.nutritionalClaimsCode) : that.nutritionalClaimsCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = upc != null ? upc.hashCode() : 0;
		result = 31 * result + (nutritionalClaimsCode != null ? nutritionalClaimsCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "NutritionalClaimsKey{" +
				"upc=" + upc +
				", nutritionalClaimsCode='" + nutritionalClaimsCode + '\'' +
				'}';
	}
}
