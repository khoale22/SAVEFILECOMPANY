/*
 *  Sca
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Represents the SCA.
 *
 * @author l730832
 * @since 2.5.0
 */
@Entity
@Table(name = "sca")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class Sca implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT_STRING = "%s[%s]";

	@Id
	@Column(name = "sca_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK")  
	private String scaCode;

	@Column(name = "sca_full_nm")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")  
	private String scaFullName;

	/**
	 * Returns the ScaCode
	 *
	 * @return ScaCode
	 **/
	public String getScaCode() {
		return scaCode;
	}

	/**
	 * Sets the ScaCode
	 *
	 * @param scaCode The ScaCode
	 **/

	public void setScaCode(String scaCode) {
		this.scaCode = scaCode;
	}

	/**
	 * Returns the ScaFullName
	 *
	 * @return ScaFullName
	 **/
	public String getScaFullName() {
		return scaFullName;
	}

	/**
	 * Sets the ScaFullName
	 *
	 * @param scaFullName The ScaFullName
	 **/
	public void setScaFullName(String scaFullName) {
		this.scaFullName = scaFullName;
	}

	/**
	 * The display name for the SCA
	 * @return the display name in the format "JIM ECK[97]"
	 */
	public String getDisplayName() {
		return String.format(Sca.DISPLAY_NAME_FORMAT_STRING, this.getScaFullName().trim(),
				this.getScaCode().trim());
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Sca{" +
				"scaCode='" + scaCode + '\'' +
				", scaFullName='" + scaFullName + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a Location, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Sca sca = (Sca) o;

		if (scaCode != null ? !scaCode.equals(sca.scaCode) : sca.scaCode != null) return false;
		return scaFullName != null ? scaFullName.equals(sca.scaFullName) : sca.scaFullName == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = scaCode != null ? scaCode.hashCode() : 0;
		result = 31 * result + (scaFullName != null ? scaFullName.hashCode() : 0);
		return result;
	}
}
