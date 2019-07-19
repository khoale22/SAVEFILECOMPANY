/*
 * VendorLocation
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a vendor location.
 *
 * @author l730832
 * @since 2.5.0
 */
@Entity
@Table(name = "vend_loc_itm_mst")
public class VendorLocationItemMaster implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@EmbeddedId
	private VendorLocationItemMasterKey key;

	@Column(name = "trusted_itm_sw")
	private Boolean trustedItemSwitch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="loc_typ_cd", referencedColumnName = "loc_typ_cd", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="loc_nbr", referencedColumnName = "loc_nbr", insertable = false, updatable = false, nullable = false)
	})
	private Location location;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
		@JoinColumn(name = "itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false),
		@JoinColumn(name = "loc_nbr", referencedColumnName = "vend_loc_nbr", insertable = false, updatable = false, nullable = false),
		@JoinColumn(name = "loc_typ_cd", referencedColumnName = "vend_loc_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private ImportItem importItem;

	/**
	 * Returns the class as it should be displayed on the GUI.
	 *
	 * @return A String representation of the class as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		return String.format(VendorLocationItemMaster.DISPLAY_NAME_FORMAT, this.getLocation().getLocationName().trim(),
				this.getKey().getLocationNumber());
	}

	/**
	 * Get the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Set the location.
	 *
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Get the importItem.
	 *
	 * @return the importItem
	 */
	public ImportItem getImportItem() {
		return importItem;
	}

	/**
	 * Set the importItem.
	 *
	 * @param importItem the importItem to set
	 */
	public void setImportItem(ImportItem importItem) {
		this.importItem = importItem;
	}

	/**
	 * Returns the Key
	 *
	 * @return Key
	 **/
	public VendorLocationItemMasterKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 **/
	public void setKey(VendorLocationItemMasterKey key) {
		this.key = key;
	}

	/**
	 * Returns the TrustedItemSwitch
	 *
	 * @return TrustedItemSwitch
	 **/
	public Boolean getTrustedItemSwitch() {
		return trustedItemSwitch;
	}

	/**
	 * Sets the TrustedItemSwitch
	 *
	 * @param trustedItemSwitch The TrustedItemSwitch
	 **/
	public void setTrustedItemSwitch(Boolean trustedItemSwitch) {
		this.trustedItemSwitch = trustedItemSwitch;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VendorLocationItemMaster{" +
				"key=" + key +
				", trustedItemSwitch=" + trustedItemSwitch +
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

		VendorLocationItemMaster that = (VendorLocationItemMaster) o;

		return (key != null ? !key.equals(that.key) : that.key != null);
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}
}
