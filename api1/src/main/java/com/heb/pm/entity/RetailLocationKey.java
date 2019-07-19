/*
 * RetailLocationKey
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of the retail_location table.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Embeddable
@TypeDefs({
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class RetailLocationKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "loc_nbr")
	private Integer locationNumber;

	@Column(name = "loc_typ_cd")
	@Type(type="fixedLengthCharPK")  
	private String locationTypeCode;

	/**
	 * Returns the location number.
	 *
	 * @return the location number.
	 **/
	public Integer getLocationNumber() {
		return locationNumber;
	}

	/**
	 * Sets the Location Number
	 *
	 * @param locationNumber The location number
	 **/

	public void setLocationNumber(Integer locationNumber) {
		this.locationNumber = locationNumber;
	}

	/**
	 * Returns the location type code.
	 *
	 * @return the location type code.
	 **/
	public String getLocationTypeCode() {
		return locationTypeCode;
	}

	/**
	 * Sets the location type code.
	 *
	 * @param locationTypeCode the location type code.
	 **/

	public void setLocationTypeCode(String locationTypeCode) {
		this.locationTypeCode = locationTypeCode;
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

		RetailLocationKey that = (RetailLocationKey) o;

		if (locationNumber != null ? !locationNumber.equals(that.locationNumber) : that.locationNumber != null) return false;
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
