/*
 * FlowTypeCode
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

/**
 * Flow thru items that do not get stocked into the shelves until the warehouse flushes. More to do with supply chain
 * vs. the product itself (Supply Chain Attribute)
 *
 * @author s753601
 * @since 2.8.0
 */
@Entity
@Table(name = "FLW_TYP")
public class FlowType {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";

	@Id
	@Column(name = "FLW_TYP_CD")
	private String id;

	@Column(name = "FLW_TYP_ABB")
	private String abbreviation;

	@Column(name = "FLW_TYP_DES")
	private String description;

	/**
	 * Returns the code for the flow type used in other tables
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Updates the id
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the abbreviation of the flow type
	 * @return
	 */
	public String getAbbreviation() {
		return abbreviation.trim();
	}

	/**
	 * Updates the abbreviation of the flow type
	 * @param abbreviation the new abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation.trim();
	}

	/**
	 * Returns the description for the flow type
	 * @return the flow type description
	 */
	public String getDescription() {
		return description.trim();
	}

	/**
	 * Update the description
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns a string used for displaying the order description type in the format Description[Code]
	 * @return the display string
	 */
	public String getDisplayName(){
		return String.format(DISPLAY_NAME_FORMAT, this.description.trim(), this.id);
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "FlowType{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	/**
	 * determines if the flow types are equal
	 * @param o object to be tested
	 * @return boolean if two objects are the same.
	 */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FlowType flowType = (FlowType) o;

		return id != null ? id.equals(flowType.id) : flowType.id == null;
	}

	/**
	 * Hash code generation for flow types
	 * @return hash code
	 */
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
