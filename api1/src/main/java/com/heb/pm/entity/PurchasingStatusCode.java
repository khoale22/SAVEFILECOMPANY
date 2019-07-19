/*
 *  PurchasingStatus
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
 * Represents a Purchasing Status Code from the PURCHASE_STAT_CD table.
 *
 * @author l730832
 * @since 2.7.0
 */
@Entity
@Table(name = "purchase_stat_cd")
public class PurchasingStatusCode implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";
	public static final String PURCHASING_STATUS_CODE_SUSPENDED = "S";
	@Id
	@Column(name = "prch_stat_cd")
	private String id;

	@Column(name = "prch_stat_abb")
	private String abbreviation;

	@Column(name = "prch_stat_des")
	private String description;

	/**
	 * Returns the Id.
	 * Code: A, D
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
	 * Returns the Abbreviation.
	 * DISCON
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
	 * Returns the Description.
	 * ACTIVE, DESCRIPTION
	 *
	 * @return Description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the display name. The display name is a nicer way to display the entity. i.e. Test[10]
	 *
	 */
	public String getDisplayName(){
		return String.format(PurchasingStatusCode.DISPLAY_NAME_FORMAT,
				this.getDescription().trim(),
				this.getId().trim());
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
	 * Represents known purchasing statuses for an item at a warehouse.
	 */
	public enum CodeValues {

		ACTIVE("A    "),
		DISCONTINUED("D    "),
		NEW("N    "),
		SUSPENDED("S    "),
		TEMPORARY_OUT_OF_STOCK("T    "),
		NO_ORDER("X    ");


		private String purchasingStatus;

		CodeValues(String purchasingStatus) {
			this.purchasingStatus = purchasingStatus;
		}

		public String getPurchasingStatus() {
			return this.purchasingStatus;
		}
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

		PurchasingStatusCode that = (PurchasingStatusCode) o;

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
		return "PurchasingStatusCode{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
