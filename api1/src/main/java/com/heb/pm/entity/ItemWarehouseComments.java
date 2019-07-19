package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

/**
 * A text field housing additional comments by submitter or updater tied to this record.
 * @author  s753601
 * @version 2.8.0
 */
@Entity
@Table(name = "ITM_WHSE_COMMENTS")
public class ItemWarehouseComments implements Serializable{

	@EmbeddedId
	private ItemWarehouseCommentsKey key;
	public static final String STRING_REMRK = "REMRK";

	@Column(name = "ITM_WHSE_CMT_TXT")
	private String itemCommentContents;

	@JsonIgnoreProperties("itemWarehouseCommentsList")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="itm_id", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="itm_key_typ_cd", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "whse_loc_nbr", referencedColumnName = "whse_loc_nbr", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "whse_loc_typ_cd", referencedColumnName = "whse_loc_typ_cd", insertable = false, updatable = false, nullable = false),
	})
	private WarehouseLocationItem warehouseLocationItem;

	/**
	 * Gets the key to uniquely identify the item warehouse comment
	 * @return the key
	 */
	public ItemWarehouseCommentsKey getKey() {
		return key;
	}

	/**
	 * Updates the key
	 * @param key the new key
	 */
	public void setKey(ItemWarehouseCommentsKey key) {
		this.key = key;
	}

	/**
	 * Gets the string holding the comments for the item in the warehouse
	 * @return  itemCommentContents
	 */
	public String getItemCommentContents() {
		return itemCommentContents;
	}

	/**
	 * Updates the itemCommentContents
	 * @param itemCommentContents the new itemCommentContents
	 */
	public void setItemCommentContents(String itemCommentContents) {
		this.itemCommentContents = itemCommentContents;
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
	 * Returns a String representation of the object.
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "WarehouseLocationItem{" +
				"key=" + key +
				", itemCommentContents='" + itemCommentContents + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a WarehouseLocationItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ItemWarehouseComments)) {
			return false;
		}

		ItemWarehouseComments that = (ItemWarehouseComments) o;
		if (this.key != null ? !this.key.equals(that.key) : that.key != null) return false;

		return true;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return this.key == null ? 0 : this.key.hashCode();
	}
}
