/*
 *  WicCategory
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
 * This is the code table for the wic category.
 *
 * @author l730832
 * @since 2.12.0
 */
@Entity
@Table(name = "wic_cat")
public class WicCategory implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s [%d]";

	@Id
	@Column(name = "wic_cat_id")
	private Long id;

	@Column(name = "wic_cat_des")
	private String description;

	/**
	 * Returns the Id
	 *
	 * @return Id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the Id
	 *
	 * @param id The Id
	 */
	public void setId(Long id) {
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
	 * Gets display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return String.format(WicCategory.DISPLAY_NAME_FORMAT, this.description.trim(), this.id);
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

		WicCategory that = (WicCategory) o;

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
