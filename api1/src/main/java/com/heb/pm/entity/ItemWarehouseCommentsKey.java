package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Key to uniquely identify the ItemWarehouseComment
 * @author  s753601
 * @version 2.8.0
 */
@Embeddable
public class ItemWarehouseCommentsKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "ITM_ID")
	private long itemId;

	@Column(name = "ITM_KEY_TYP_CD")
	private String itemType;

	@Column(name = "WHSE_LOC_NBR")
	private int warehouseNumber;

	@Column(name = "WHSE_LOC_TYP_CD")
	private String warehouseType;

	@Column(name = "ITM_CMT_TYP_CD")
	private String itemCommentType;

	@Column(name = "ITM_WHSE_CMT_NBR")
	private int itemCommentNumber;

	/**
	 * Gets the ID for the item in the warehouse
	 * @return itemId
	 */
	public long getItemId() {
		return itemId;
	}

	/**
	 * Updates the itemId
	 * @param itemId the new itemId
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	/**
	 * Gets the string code to state what kind of item the item in question is.
	 * @return itemKeyTypeCode
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Updates the itemKeyTypeCode
	 * @param itemType the new itemKeyTypeCode
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Gets the warehouse identification number for the item comment
	 * @return warehouseNumber
	 */
	public int getWarehouseNumber() {
		return warehouseNumber;
	}

	/**
	 * Updates the warehouseNumber
	 * @param warehouseNumber the new warehouseNumber
	 */
	public void setWarehouseNumber(int warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	/**
	 * Gets the type of warehouse the item is stored in
	 * @return warehouseType
	 */
	public String getWarehouseType() {
		return warehouseType;
	}

	/**
	 * Updates the warehouseType
	 * @param warehouseType the new warehouseType
	 */
	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}

	/**
	 * Gets the type of comment that the item has
	 * @return getItemCommentType
	 */
	public String getItemCommentType() {
		return itemCommentType;
	}

	/**
	 * Updates the getItemCommentType
	 * @param itemCommentType the new getItemCommentType
	 */
	public void setItemCommentType(String itemCommentType) {
		this.itemCommentType = itemCommentType;
	}

	/**
	 * Get the line number for the item comment
	 * @return itemCommentNumber
	 */
	public int getItemCommentNumber() {
		return itemCommentNumber;
	}

	/**
	 * Updates the itemCommentNumber
	 * @param itemCommentNumber the new itemCommentNumber
	 */
	public void setItemCommentNumber(int itemCommentNumber) {
		this.itemCommentNumber = itemCommentNumber;
	}

	/**
	 * Returns a String representation of this object.
	 * @return A String representation of this object.
	 */
	public String toString() {
		return "WarehouseLocationItemKey{" +
				"itemType='" + itemType + '\'' +
				", itemCode='" + itemId +'\'' +
				", warehouseType='" + warehouseType +'\'' +
				", warehouseNumber='" + warehouseNumber +'\'' +
				", itemCommentType='" + itemCommentType +'\'' +
				", itemCommentNumber='" + itemCommentNumber +'\'' +
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
		if (!(o instanceof ItemWarehouseCommentsKey)) return false;

		ItemWarehouseCommentsKey that = (ItemWarehouseCommentsKey) o;

		if (itemId != that.itemId) return false;
		if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) return false;
		if (warehouseNumber != that.warehouseNumber) return false;
		if (warehouseType != null ? !warehouseType.equals(that.warehouseType) : that.warehouseType != null) return false;
		if (itemCommentNumber != that.itemCommentNumber) return false;
		if (itemCommentType != null ? !itemCommentType.equals(that.itemCommentType) : that.itemCommentType != null) return false;
		return true;

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = itemType != null ? itemType.hashCode() : 0;
		result = ItemWarehouseCommentsKey.PRIME_NUMBER * result + (int) (itemId ^ (itemId >>> ItemWarehouseCommentsKey.FOUR_BYTES));
		result = ItemWarehouseCommentsKey.PRIME_NUMBER * result + (warehouseType != null ? warehouseType.hashCode() : 0);
		result = ItemWarehouseCommentsKey.PRIME_NUMBER * result + warehouseNumber;
		result = ItemWarehouseCommentsKey.PRIME_NUMBER * result + (itemCommentType != null ? itemCommentType.hashCode() : 0);
		result = ItemWarehouseCommentsKey.PRIME_NUMBER * result + itemCommentNumber;
		return result;
	}
}
