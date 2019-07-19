/*
 *  DescriptionType
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * This represents the description type. This is the code table for all descriptions: PRIMS, SIGN1, SIGN2, TAG1, TAG2
 *
 * @author l730832
 * @since 2.8.0
 */
@Entity
@Table(name = "des_typ")
public class DescriptionType implements Serializable {
	public static final String PRIMO_PICK_SHORT_DESCRIPTION = "PRIMS";
	public static final String PRIMO_PICK_LONG_DESCRIPTION = "PRIML";
	public static final String CUSTOMER_FRIENDLY_DESCRIPTION_LINE_ONE = "TAG1";
	public static final String CUSTOMER_FRIENDLY_DESCRIPTION_LINE_TWO = "TAG2";
	public static final String SERVICE_CASE_DESCRIPTION = "SGNRC";
	public static final String SERVICE_CASE_CALLOUT = "SRVCC";

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "des_typ_cd")
	private String id;

	@Column(name = "des_typ_abb")
	private String abbreviation;

	@Column(name = "des_typ_des")
	private String description;

	@Column(name = "des_ln")
	private Integer descriptionLength;

	/**
	 * Represents the values of the known description types.
	 */
	public enum Codes {
		COUNTRY_OF_ORIGIN("COOL "),
		POS_RECEIPT("POSRD"),
		PRIMO_PICK_LONG("PRIML"),
		PRIMO_PICK_SHORT("PRIMS"),
		SIGN_ROMANCE_COPY("SGNRC"),
		PROPOSED_SIGN_ROMANCE_COPY("SGNRP"),
		SIGN_CUSTOMER_FRIENDLY_LINE_ONE("SIGN1"),
		SIGN_CUSTOMER_FRIENDLY_LINE_TWO("SIGN2"),
		ITEM_SIZE("SIZE "),
		SERVING_SIZE("SSIZE"),
		STANDARD("STND "),
		STYLE("STYLE"),
		TAG_CUSTOMER_FRIENDLY_LINE_ONE("TAG1 "),
		TAG_CUSTOMER_FRIENDLY_LINE_TWO("TAG2 "),
		SERVICE_CASE_CALL_OUT("SRVCC");

		String id;

		/**
		 * Called to create a value.
		 *
		 * @param id The value of the id in the table.
		 */
		Codes(String id) {
			this.id = id;
		}

		/**
		 * Returns the id that represents each description type (the value that will go into the database).
		 *
		 * @return The id that represents each description type.
		 */
		public String getId() {
			return this.id;
		}
	}

	/**
	 * Returns the id.
	 *
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id
	 *
	 * @param id The id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the abbreviation.
	 *
	 * @return abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the abbreviation
	 *
	 * @param abbreviation The abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns the description. The description that is associated with the type code.
	 *
	 * @return description
	 */
	public String getDescription() {
		return description.trim();
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
	 * Returns the DescriptionLength. The description lenght is the maximum number of characters the description can be
	 * minus 1.
	 *
	 * @return DescriptionLength
	 */
	public Integer getDescriptionLength() {
		return descriptionLength;
	}

	/**
	 * Sets the DescriptionLength
	 *
	 * @param descriptionLength The DescriptionLength
	 */
	public void setDescriptionLength(Integer descriptionLength) {
		this.descriptionLength = descriptionLength;
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

		DescriptionType that = (DescriptionType) o;

		return (id != null ? !id.equals(that.id) : that.id != null);
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
