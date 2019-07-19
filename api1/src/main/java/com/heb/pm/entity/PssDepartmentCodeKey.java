/*
 *  PssDepartmentCodeKey
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
 * Represents a pss department code key. A pss department code key contains the id of the pss department.
 *
 * @author l730832
 * @since 2.8.0
 */

@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
@Embeddable
public class PssDepartmentCodeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "pd_cntl_seq_no")
	private Integer id;

	@Type(type="fixedLengthCharPK")
	@Column(name = "pd_cntl_fld_cd")
	private String pssDepartmentIdentifierString;

	@Type(type="fixedLengthCharPK")
	@Column(name = "pd_scrn_cntl_no")
	private String pssMainframeScreen;

	/**
	 * Default construction method.
	 */
	public PssDepartmentCodeKey() {
	}

	/**
	 * Construction method.
	 * @param id
	 * @param pssDepartmentIdentifierString
	 * @param pssMainframeScreen
	 */
	public PssDepartmentCodeKey(Integer id, String pssDepartmentIdentifierString, String pssMainframeScreen) {
		this.id = id;
		this.pssDepartmentIdentifierString = pssDepartmentIdentifierString;
		this.pssMainframeScreen = pssMainframeScreen;
	}

	/**
	 * Returns the id.
	 *
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id
	 *
	 * @param id The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Returns the PssDepartmentIdentifierString. This is how you find a pss department in the current table. For a pss
	 * department this string will always be "PD_PSS_DEPT_NO". It is also what jpa searches on to find pss departments.
	 *
	 * @return PssDepartmentIdentifierString
	 */
	public String getPssDepartmentIdentifierString() {
		return pssDepartmentIdentifierString;
	}

	/**
	 * Sets the PssDepartmentIdentifierString
	 *
	 * @param pssDepartmentIdentifierString The PssDepartmentIdentifierString
	 */
	public void setPssDepartmentIdentifierString(String pssDepartmentIdentifierString) {
		this.pssDepartmentIdentifierString = pssDepartmentIdentifierString;
	}

	/**
	 * Returns the PssMainframeScreen. This is the old mainframe screen name where pss departments were created.
	 * This is also needed to find the pss department numbers. For pss departments it will always be, "I18MP462".
	 *
	 * @return PssMainframeScreen
	 */
	public String getPssMainframeScreen() {
		return pssMainframeScreen;
	}

	/**
	 * Sets the PssMainframeScreen
	 *
	 * @param pssMainframeScreen The PssMainframeScreen
	 */
	public void setPssMainframeScreen(String pssMainframeScreen) {
		this.pssMainframeScreen = pssMainframeScreen;
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

		PssDepartmentCodeKey that = (PssDepartmentCodeKey) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (pssDepartmentIdentifierString != null ? !pssDepartmentIdentifierString.equals(that.pssDepartmentIdentifierString) : that.pssDepartmentIdentifierString != null)
			return false;
		return pssMainframeScreen != null ? pssMainframeScreen.equals(that.pssMainframeScreen) : that.pssMainframeScreen == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (pssDepartmentIdentifierString != null ? pssDepartmentIdentifierString.hashCode() : 0);
		result = 31 * result + (pssMainframeScreen != null ? pssMainframeScreen.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "PssDepartmentCodeKey{" +
				"id=" + id +
				", pssDepartmentIdentifierString='" + pssDepartmentIdentifierString + '\'' +
				", pssMainframeScreen='" + pssMainframeScreen + '\'' +
				'}';
	}
}
