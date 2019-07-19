/*
 * OrderQuantityType
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
 * The Order Quantity Type. The way a shipment is flagged to be received as. Default is cases
 *
 * @author s753601
 * @since 2.8.0
 */
@Entity
@Table(name = "ORD_QTY_TYP_CD")
public class OrderQuantityType {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";

	@Id
	@Column(name = "ORD_QTY_TYP_CD")
	private String id;

	@Column(name = "ORD_QTY_TYP_ABB")
	private String abbreviation;

	@Column(name = "ORD_QTY_TYP_DES")
	private String description;

	/**
	 * Returns the code for the order quantity type used in other tables
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
	 * Gets the abbreviation of the order quantity type
	 * @return
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Updates the abbreviation of the order quantity type
	 * @param abbreviation the new abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns the description for the order quantity type
	 * @return the order quantity type description
	 */
	public String getDescription() {
		return description;
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
	public String getOrderQuantityTypeDisplay(){
		return String.format(DISPLAY_NAME_FORMAT, this.description.trim(), this.id.trim());
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
	 * determines if the order quantity are equal
	 * @param o object to be tested
	 * @return boolean if two objects are the same.
	 */
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OrderQuantityType orderQuantityType = (OrderQuantityType) o;

		return id != null ? id.equals(orderQuantityType.id) : orderQuantityType.id == null;
	}

	/**
	 * Hash code generation for flow types
	 * @return hash code
	 */
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
