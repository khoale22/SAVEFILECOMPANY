/*
 * DiscontinueReason
 *
 * Copyright (c) 2017 HEB
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
 * The discontinue reason. The discontinue reason is the reason why they are discontinuing the item. Seasonal or
 * Non-std-discontinue.
 *
 * @author l730832
 * @since 2.7.0
 */
@Entity
@Table(name = "disco_rsn_cd")
public class DiscontinueReason implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";

	@Id
	@Column(name = "dscon_rsn_cd")
	private String id;

	@Column(name = "dscon_rsn_abb")
	private String abbreviation;

	@Column(name = "dscon_rsn_des")
	private String description;

	/**
	 * Returns the id. The id in the code table.
	 * Code: 0, 1, 2, 3, 4
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
	 * Returns the abbreviation. The abbreviation of the type code description.
	 * Abbreviation: BLANK
	 * SEASNL
	 * NONSTD...
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
	 * Returns the display name. The display name is a nicer way to display the entity. i.e. Test[10]
	 *
	 */
	public String getDisplayName(){
		return String.format(DiscontinueReason.DISPLAY_NAME_FORMAT,
				this.getDescription().trim(),
				this.getId().trim());
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueReason{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
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

		DiscontinueReason that = (DiscontinueReason) o;

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
}
