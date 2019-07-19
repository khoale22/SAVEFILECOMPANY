/*
 *  PrimaryUpc
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The type Primary upc.
 *
 * @author s573181
 * @since 2.0.3
 */
@Entity
@Table(name="PD_UPC")
public class PrimaryUpc implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int FOUR_BYTES = 32;

    @Id
	@Column(name="pd_upc_no")
    private long upc;

	@Column(name = "pd_item_no")
	private long itemCode;

    @NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties("primaryUpc")
	@OneToMany(mappedBy = "primaryUpc", fetch = FetchType.LAZY)
	private List<AssociatedUpc> associateUpcs;

	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties("primaryUpc")
	@OneToMany(mappedBy = "primaryUpc", fetch = FetchType.LAZY)
	private List<Shipper> shipper;

	@NotFound(action = NotFoundAction.IGNORE)
	@JsonIgnoreProperties("realUpc")
	@OneToMany(mappedBy = "realUpc", fetch = FetchType.LAZY)
	private List<Shipper> productShipper;

    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties("primaryUpc")
    @OneToMany(mappedBy = "primaryUpc", fetch = FetchType.LAZY)
    private List<ItemMaster> itemMasters;

    /**
     * Returns the upc for this object.
     *
     * @return The upc for this object.
     */
    public long getUpc() {
        return upc;
    }

    /**
     * Sets the  upc for this objec.
     *
     * @param upc The  upc for this object.
     */
    public void setUpc(long upc) {
        this.upc = upc;
    }

	/**
	 * Returns the Item Code for this object.
	 *
	 * @return The Item Code for this object.
	 */
	public long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the Item Code for this objec.
	 *
	 * @param itemCode The Item Code for this object.
	 */
	public void setItemCode(long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the list of UPCs associated to this primary.
	 *
	 * @return The list of UPCs associated to this primary.
	 */
	public List<AssociatedUpc> getAssociateUpcs() {
		return associateUpcs;
	}

	/**
	 * Sets the list of UPCs associated to this primary.
	 *
	 * @param associateUpcs The list of UPCs associated to this primary.
	 */
	public void setAssociateUpcs(List<AssociatedUpc> associateUpcs) {
		this.associateUpcs = associateUpcs;
	}

	/**
	 * Returns the shipper associated with this primary.
	 *
	 * @return The shipper associated with this primary.
	 */
	public List<Shipper> getShipper() {
		return shipper;
	}

	/**
	 * Sets the shipper associated with this primary.
	 *
	 * @param shipper The shipper associated with this primary.
	 */
	public void setShipper(List<Shipper> shipper) {
		this.shipper = shipper;
	}

	public List<Shipper> getProductShipper() {
		return productShipper;
	}

	public void setProductShipper(List<Shipper> productShipper) {
		this.productShipper = productShipper;
	}

	/**
     * Returns the itemMasters associated with this primary.
     *
     * @return The itemMasters associated with this primary.
     */
    public List<ItemMaster> getItemMasters() {
        return itemMasters;
    }

    /**
     * Sets the itemMasters associated with this primary.
     *
     * @param itemMasters The itemMasters associated with this primary.
     */
    public void setItemMasters(List<ItemMaster> itemMasters) {
        this.itemMasters = itemMasters;
    }

    /**
     * The method is used to retrieve a warehouse item master.
     *
     * @return The item master where the type is "ITMCD".
     */
	@JsonIgnore
    public ItemMaster getWarehouseItem(){
        ItemMaster tempItemMaster = null;
        for(ItemMaster im : this.getItemMasters()){
            if(ItemMasterKey.WAREHOUSE.equals(im.getKey().getItemType())) {
                tempItemMaster = im;
                break;
            }
        }

        return tempItemMaster;
    }

    /**
     * The method is used to retrieve a dsd item master.
     *
     * @return The item master where the type is "DSD  ".
     */
	@JsonIgnore
    public ItemMaster getDsdItem(){
        ItemMaster tempItemMaster = null;
        for(ItemMaster im : this.getItemMasters()){
            if(ItemMasterKey.DSD.equals(im.getKey().getItemType())) {
                tempItemMaster = im;
                break;
            }
        }

        return tempItemMaster;
    }

    /**
     * Compares another object to this one. If that object is an PrimaryUpc, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrimaryUpc that = (PrimaryUpc) o;

        return upc == that.upc;

    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this obejct.
     */
    @Override
    public int hashCode() {
        return (int) (upc ^ (upc >>> FOUR_BYTES));
    }

    /**
     * Returns a printable representation of the object.
     *
     * @return A printable representation of the object.
     */
    @Override
    public String toString(){
        return "PrimaryUpc{" +
                "upc='" + upc + '\'' +
                '}';
    }
}
