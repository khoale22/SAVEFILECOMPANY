/*
 * Location
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a location.
 *
 * @author m314029
 * @since 2.2.0
 */
@Entity
@Table(name = "location")
//dB2Oracle changes vn00907 starts
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class Location implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final String WAREHOUSE_AP_TYPE_CODE = "AP";
	public static final String DSD_AP_TYPE_CODE = "DS";
	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";


	@EmbeddedId
	private LocationKey key;

	@Column(name = "ap_nbr")
	private Integer apVendorNumber;

	@Column(name = "ap_typ_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String apTypeCode;

	@Column(name = "loc_nm")
	private String locationName;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="ap_nbr", referencedColumnName = "ap_nbr", insertable = false, updatable = false),
			@JoinColumn(name="ap_typ_cd", referencedColumnName = "ap_typ_cd", insertable = false, updatable = false)
	})
	private ApLocation apLocation;

	// These are required to support dynamic search capability and is not used outside that. Therefore, there are no
	// getters or setters on this.

	@OneToMany(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "vend_loc_nbr", referencedColumnName = "ap_nbr", insertable = false, updatable = false)
	private List<SearchCriteria> searchCriteria;

	@Column(name = "INACTIVE_SW", nullable = false)
	private String inactiveSW;
	/**
	 * Gets the location key.
	 *
	 * @return the location key
	 */
	public LocationKey getKey() {
		return key;
	}

	/**
	 * Sets the location key.
	 *
	 * @param key the location key
	 */
	public void setKey(LocationKey key) {
		this.key = key;
	}

	/**
	 * Gets the ap vendor number.
	 *
	 * @return the ap vendor number
	 */
	public Integer getApVendorNumber() {
		return apVendorNumber;
	}

	/**
	 * Sets the ap vendor number.
	 *
	 * @param apVendorNumber the ap vendor number
	 */
	public void setApVendorNumber(Integer apVendorNumber) {
		this.apVendorNumber = apVendorNumber;
	}

	/**
	 * Gets the ap type code.
	 *
	 * @return the ap type code
	 */
	public String getApTypeCode() {
		return apTypeCode;
	}

	/**
	 * Sets the ap type code.
	 *
	 * @param apTypeCode the ap type code
	 */
	public void setApTypeCode(String apTypeCode) {
		this.apTypeCode = apTypeCode;
	}

	/**
	 * Returns the LocationName
	 *
	 * @return LocationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * Sets the LocationName
	 *
	 * @param locationName The LocationName
	 */

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * Returns the ApLocation
	 *
	 * @return ApLocation
	 **/
	public ApLocation getApLocation() {
		return apLocation;
	}

	/**
	 * Sets the ApLocation
	 *
	 * @param apLocation The ApLocation
	 **/

	public void setApLocation(ApLocation apLocation) {
		this.apLocation = apLocation;
	}

	/**
	 * Creates a Display Name string used to show relevant information about an location in the format of Name[Number]
	 * @return Display name of the location.
	 */
	public String getDisplayName(){
		return String.format(DISPLAY_NAME_FORMAT, this.locationName.trim(), this.key.getLocationNumber());
	}

	/**
	 * Returns the inactive switch
	 * @return the inactive switch
	 */
	public String inactiveSW() {
		return inactiveSW;
	}

	/**
	 * Sets the inactive switch
	 * @param inactiveSW the inactive switch
	 */
	public void setInactiveSW(String inactiveSW) {
		this.inactiveSW = inactiveSW;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Location{" +
				"key=" + key +
				"name=" + locationName +
				", apVendorNumber=" + apVendorNumber +
				", inactiveSW=" + inactiveSW +
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

		Location location = (Location) o;

		return key != null ? key.equals(location.key) : location.key == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}
}
