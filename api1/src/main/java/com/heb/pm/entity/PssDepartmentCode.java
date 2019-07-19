/*
 * PssDepartmentCode
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a pss department code table. There are two different things inside of this table: pre-digit 4's and pss
 * departments. For this specific entity we are just going to be using the pss department codes.
 * The Id is located in the Key and the description is located here. The @Where clause will make sure that we only get
 * back pss departments from the NIS_CNTL_TAB.
 *
 * @author l730832
 * @since 2.8.0
 */
@Entity
@Table(name = "PD_NIS_CNTL_TAB")
@Where(clause = "PD_SCRN_CNTL_NO = 'I18MP462' AND PD_CNTL_FLD_CD='PD_PSS_DEPT_NO'")
public class PssDepartmentCode implements Serializable {

	// Constants to identify a pss department from the PD_NIS_CNTL_TAB
	public static final String PSS_MAINFRAME_SCREEN = "I18MP462";
	public static final String PSS_IDENTIFIER_STRING = "PD_PSS_DEPT_NO";

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s - %s";

	@EmbeddedId
	private PssDepartmentCodeKey key;

	@Column(name = "pd_cntl_val_des")
	private String description;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public PssDepartmentCodeKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(PssDepartmentCodeKey key) {
		this.key = key;
	}

	/**
	 * Returns the description.
	 *
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 *
	 * @param description The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 * Returns the display name. The display name is a nicer way to display the entity. i.e. 10 - Test
	 *
	 * @return display name
	 */
	public String getDisplayName(){
		return String.format(PssDepartmentCode.DISPLAY_NAME_FORMAT, this.getKey().getId().toString(),
				this.description.trim());
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

		PssDepartmentCode that = (PssDepartmentCode) o;

		return key != null ? !key.equals(that.key) : that.key != null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "PssDepartmentCode{" +
				"key=" + key +
				", description='" + description + '\'' +
				'}';
	}
}
