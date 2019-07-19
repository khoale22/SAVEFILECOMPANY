/*
 *  NutritionalAttributes
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
 * This is the code table for nutritional attributes. If the upc is one of these then it will be in the db otherwise it
 * will not hold a value for it in the db.
 *
 * @author l730832
 * @since 2.11.0
 */
@Entity
@Table(name = "prod_ntrntl_attr")
public class NutritionalClaimsAttribute implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "prod_ntrntl_cd")
	private String id;

	@Column(name = "prod_ntrntl_abb")
	private String abbreviation;

	@Column(name = "prod_ntrntl_des")
	private String description;

	/**
	 * Returns the Id
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
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NutritionalClaimsAttribute that = (NutritionalClaimsAttribute) o;

		return id != null ? !id.equals(that.id) : that.id != null;
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

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "NutritionalClaimsAttribute{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
