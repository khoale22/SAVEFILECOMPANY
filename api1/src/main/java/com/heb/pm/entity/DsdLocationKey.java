/*
 * DsdLocationKey
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
 * Composite key for VendorLocation.
 *
 * @author l730832
 * @since 2.5.0
 */
@Embeddable
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class DsdLocationKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "loc_nbr")
	private Integer locationNumber;

	@Column(name = "loc_typ_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String locationTypeCode;

	/**
	 * Returns the LocationNumber
	 *
	 * @return LocationNumber
	 **/
	public Integer getLocationNumber() {
		return locationNumber;
	}

	/**
	 * Sets the LocationNumber
	 *
	 * @param locationNumber The LocationNumber
	 **/
	public void setLocationNumber(Integer locationNumber) {
		this.locationNumber = locationNumber;
	}

	/**
	 * Returns the LocationTypeCode
	 *
	 * @return LocationTypeCode
	 **/
	public String getLocationTypeCode() {
		return locationTypeCode;
	}

	/**
	 * Sets the LocationTypeCode
	 *
	 * @param locationTypeCode The LocationTypeCode
	 **/
	public void setLocationTypeCode(String locationTypeCode) {
		this.locationTypeCode = locationTypeCode;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "DsdLocationKey{" +
				"locationNumber=" + locationNumber +
				", locationTypeCode='" + locationTypeCode + '\'' +
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

		DsdLocationKey that = (DsdLocationKey) o;

		if (locationNumber != null ? !locationNumber.equals(that.locationNumber) : that.locationNumber != null)
			return false;
		return locationTypeCode != null ? locationTypeCode.equals(that.locationTypeCode) : that.locationTypeCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = locationNumber != null ? locationNumber.hashCode() : 0;
		result = 31 * result + (locationTypeCode != null ? locationTypeCode.hashCode() : 0);
		return result;
	}
}
