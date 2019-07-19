/*
 *  RetailLocation
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the retail_loc.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Entity
@Table(name = "retail_loc")
public class RetailLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RetailLocationKey key;

	@Column(name = "LIN_OF_BUS_ID")
	private Integer linOfBusId;

	@Column(name = "RETL_LOC_STAT_CD")
	private String retailLocationStatusCode;

	@Column(name = "FINANCIAL_DIV_ID")
	private Integer financialDivisionId;

	@Column(name = "PD_ZONE_NO")
	private Integer zoneId;

	/**
	 * Returns the RetailLocationKey
	 *
	 * @return key
	 **/
	public RetailLocationKey getKey() {
		return key;
	}

	/**
	 * Sets the RetailLocationKey
	 *
	 * @param key The RetailLocationKey
	 **/
	public void setKey(RetailLocationKey key) {
		this.key = key;
	}

	/**
	 * Returns the lin of bus id
	 *
	 * @return  the lin of bus id
	 **/
	public Integer getLinOfBusId() {
		return linOfBusId;
	}

	/**
	 * Sets the lin of bus id
	 *
	 * @param linOfBusId the lin of bus id
	 **/
	public void setLinOfBusId(Integer linOfBusId) {
		this.linOfBusId = linOfBusId;
	}

	/**
	 * Return retail location status code
	 *
	 * @return retail location status code
	 */
	public String getRetailLocationStatusCode() {
		return retailLocationStatusCode;
	}

	/**
	 * Set the retail location status code
	 *
	 * @param retailLocationStatusCode the retail location status code
	 */
	public void setRetailLocationStatusCode(String retailLocationStatusCode) {
		this.retailLocationStatusCode = retailLocationStatusCode;
	}

	/**
	 * Return financial division id
	 *
	 * @return financial division id
	 */
	public Integer getFinancialDivisionId() {
		return financialDivisionId;
	}

	/**
	 * Set the financial division id
	 *
	 * @param financialDivisionId the financial division id
	 */
	public void setFinancialDivisionId(Integer financialDivisionId) {
		this.financialDivisionId = financialDivisionId;
	}

	/**
	 * Returns ZoneId.
	 *
	 * @return The ZoneId.
	 **/
	public Integer getZoneId() {
		return zoneId;
	}

	/**
	 * Sets the ZoneId.
	 *
	 * @param zoneId The ZoneId.
	 **/
	public RetailLocation setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
		return this;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "RetailLocation{" +
				"RetailLocationKey=" + key +
				", linOfBusId='" + linOfBusId + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a Location, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RetailLocation that = (RetailLocation) o;

		if (key != null ? !key.equals(that.key) : that.key != null)
			return false;
		return linOfBusId != null ? linOfBusId.equals(that.linOfBusId) : that.linOfBusId == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + (linOfBusId != null ? linOfBusId.hashCode() : 0);
		return result;
	}
}
