/*
 * LocationKey
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
 * Composite key for a Location.
 *
 * @author m314029
 * @since 2.2.0
 */
@Embeddable
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class LocationKey  implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "loc_typ_cd")
	@Type(type="fixedLengthCharPK")  
	private String locationType;

	@Column(name = "loc_nbr")
	private int locationNumber;

	/**
	 * Gets the location type.
	 *
	 * @return the location type
	 */
	public String getLocationType() {
		return locationType;
	}

	/**
	 * Sets the location type.
	 *
	 * @param locationType the location type
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * Gets the location number.
	 *
	 * @return the location number
	 */
	public int getLocationNumber() {
		return locationNumber;
	}

	/**
	 * Sets the location number.
	 *
	 * @param locationNumber the location number
	 */
	public void setLocationNumber(int locationNumber) {
		this.locationNumber = locationNumber;
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

		LocationKey that = (LocationKey) o;

		if (locationNumber != that.locationNumber) return false;
		return locationType != null ? locationType.equals(that.locationType) : that.locationType == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = locationType != null ? locationType.hashCode() : 0;
		result = LocationKey.PRIME_NUMBER * result + locationNumber;
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "LocationKey{" +
				"locationType='" + locationType + '\'' +
				", locationNumber=" + locationNumber +
				'}';
	}
}
