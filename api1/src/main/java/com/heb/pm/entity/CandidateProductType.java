/*
 * CandidateProductType
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the item type code table. This tells whether it is Sellable or an ingredient or display or freights.
 *
 * @author vn73545
 * @since 2.7.0
 */
@Entity
@Table(name = "ps_product_typ_cd")
public class CandidateProductType implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;
	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";

	@Id
	@Column(name="PROD_TYP_CD")
	private String id;

	@Column(name="PROD_TYP_DES")
	private String description;

	@Column(name="PROD_TYP_ABB")
	private String abbreviation;

	/**
	 * Returns the Id in the code table.
	 *
	 * @return Id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the Id
	 *
	 * @param id The Id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the Description
	 *
	 * @return Description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description
	 *
	 * @param description The Description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the Abbreviation
	 *
	 * @return Abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the Abbreviation
	 *
	 * @param abbreviation The Abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns the display name. The display name is a nicer way to display the entity. i.e. Test[10]
	 *
	 */
	public String getDisplayName(){
		return String.format(CandidateProductType.DISPLAY_NAME_FORMAT, this.getDescription().trim(), this.getId().trim());
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

		CandidateProductType itemType = (CandidateProductType) o;

		if (id != null ? !id.equals(itemType.id) : itemType.id != null) return false;
		if (description != null ? !description.equals(itemType.description) : itemType.description != null)
			return false;
		return abbreviation != null ? abbreviation.equals(itemType.abbreviation) : itemType.abbreviation == null;
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
		result = PRIME_NUMBER * result + (description != null ? description.hashCode() : 0);
		result = PRIME_NUMBER * result + (abbreviation != null ? abbreviation.hashCode() : 0);
		return result;
	}
}
