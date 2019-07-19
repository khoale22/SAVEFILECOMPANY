/*
 * ItemWarehouseVendor
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.Where;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents an Item Warehouse Vendor.
 *
 * @author l730832
 * @since 2.5.0
 */
@Entity
@Where(clause = "vend_loc_typ_cd = 'V' and whse_loc_typ_cd = 'W'")
@Table(name = "itm_whse_vend")
public class ItemWarehouseVendor implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ItemWarehouseVendorKey key;

	@JsonIgnoreProperties("itemWarehouseVendorList")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "vend_loc_nbr", referencedColumnName = "vend_loc_nbr", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "vend_loc_typ_cd", referencedColumnName = "vend_loc_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private VendorLocationItem vendorLocationItem;

	@JsonIgnoreProperties("itemWarehouseVendorList")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "whse_loc_nbr", referencedColumnName = "whse_loc_nbr", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "whse_loc_typ_cd", referencedColumnName = "whse_loc_typ_cd", insertable = false, updatable = false, nullable = false),
	})
	private WarehouseLocationItem warehouseLocationItem;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 **/
	public ItemWarehouseVendorKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 **/

	public void setKey(ItemWarehouseVendorKey key) {
		this.key = key;
	}

	/**
	 * Returns the WarehouseLocationItem
	 *
	 * @return WarehouseLocationItem
	 **/
	public WarehouseLocationItem getWarehouseLocationItem() {
		return warehouseLocationItem;
	}

	/**
	 * Sets the WarehouseLocationItem
	 *
	 * @param warehouseLocationItem The WarehouseLocationItem
	 **/

	public void setWarehouseLocationItem(WarehouseLocationItem warehouseLocationItem) {
		this.warehouseLocationItem = warehouseLocationItem;
	}

	/**
	 * Returns the VendorLocationItem
	 *
	 * @return VendorLocationItem
	 **/
	public VendorLocationItem getVendorLocationItem() {
		return vendorLocationItem;
	}

	/**
	 * Sets the VendorLocationItem
	 *
	 * @param vendorLocationItem The VendorLocationItem
	 **/

	public void setVendorLocationItem(VendorLocationItem vendorLocationItem) {
		this.vendorLocationItem = vendorLocationItem;
	}

	/**
	 * Compares another object to this one. If that object is an ItemNotDeletedReason, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemWarehouseVendor that = (ItemWarehouseVendor) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}
}
