/*
 * ApLocationKey
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Represents the key of the ap_location table.
 *
 * @author l730832
 * @since 2.5.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ApLocationKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ap_nbr")
	private Integer apVendorNumber;

	@Column(name = "ap_typ_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")  
	private String apTypeCode;

	/**
	 * Returns the ApVendorNumber
	 *
	 * @return ApVendorNumber
	 **/
	public Integer getApVendorNumber() {
		return apVendorNumber;
	}

	/**
	 * Sets the ApVendorNumber
	 *
	 * @param apVendorNumber The ApVendorNumber
	 **/

	public void setApVendorNumber(Integer apVendorNumber) {
		this.apVendorNumber = apVendorNumber;
	}

	/**
	 * Returns the ApTypeCode
	 *
	 * @return ApTypeCode
	 **/
	public String getApTypeCode() {
		return apTypeCode;
	}

	/**
	 * Sets the ApTypeCode
	 *
	 * @param apTypeCode The ApTypeCode
	 **/

	public void setApTypeCode(String apTypeCode) {
		this.apTypeCode = apTypeCode;
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

		ApLocationKey that = (ApLocationKey) o;

		if (apVendorNumber != null ? !apVendorNumber.equals(that.apVendorNumber) : that.apVendorNumber != null) return false;
		return apTypeCode != null ? apTypeCode.equals(that.apTypeCode) : that.apTypeCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = apVendorNumber != null ? apVendorNumber.hashCode() : 0;
		result = 31 * result + (apTypeCode != null ? apTypeCode.hashCode() : 0);
		return result;
	}
}
