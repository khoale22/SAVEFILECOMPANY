package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Composite key for ItemWarehouseVendor.
 *
 * @author l730832
 * @since 2.5.0
 */
@Embeddable
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ItemWarehouseVendorKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "itm_key_typ_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK") 
	private String itemType;

	@Column(name = "itm_id") 
	private long itemCode;

	@Column(name = "vend_loc_typ_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK") 
	private String vendorType;

	@Column(name = "vend_loc_nbr")
	private int vendorNumber;

	@Column(name = "whse_loc_typ_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK") 
	private String warehouseType;

	@Column(name = "whse_loc_nbr") 
	private int warehouseNumber;

	/**
	 * Returns the ItemType
	 *
	 * @return ItemType
	 **/
	public String getItemType() {
		return itemType;
	}

	/**
	 * Sets the ItemType
	 *
	 * @param itemType The ItemType
	 **/

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns the ItemCode
	 *
	 * @return ItemCode
	 **/
	public long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the ItemCode
	 *
	 * @param itemCode The ItemCode
	 **/

	public void setItemCode(long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the VendorType
	 *
	 * @return VendorType
	 **/
	public String getVendorType() {
		return vendorType;
	}

	/**
	 * Sets the VendorType
	 *
	 * @param vendorType The VendorType
	 **/

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	/**
	 * Returns the VendorNumber
	 *
	 * @return VendorNumber
	 **/
	public int getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * Sets the VendorNumber
	 *
	 * @param vendorNumber The VendorNumber
	 **/

	public void setVendorNumber(int vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	/**
	 * Returns the WarehouseType
	 *
	 * @return WarehouseType
	 **/
	public String getWarehouseType() {
		return warehouseType;
	}

	/**
	 * Sets the WarehouseType
	 *
	 * @param warehouseType The WarehouseType
	 **/

	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}

	/**
	 * Returns the WarehouseNumber
	 *
	 * @return WarehouseNumber
	 **/
	public int getWarehouseNumber() {
		return warehouseNumber;
	}

	/**
	 * Sets the WarehouseNumber
	 *
	 * @param warehouseNumber The WarehouseNumber
	 **/

	public void setWarehouseNumber(int warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
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

		ItemWarehouseVendorKey that = (ItemWarehouseVendorKey) o;

		if (itemCode != that.itemCode) return false;
		if (vendorNumber != that.vendorNumber) return false;
		if (warehouseNumber != that.warehouseNumber) return false;
		if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) return false;
		return vendorType != null ? vendorType.equals(that.vendorType) : that.vendorType == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = itemType != null ? itemType.hashCode() : 0;
		result = 31 * result + (int) (itemCode ^ (itemCode >>> 32));
		result = 31 * result + (vendorType != null ? vendorType.hashCode() : 0);
		result = 31 * result + vendorNumber;
		result = 31 * result + warehouseNumber;
		return result;
	}
}
