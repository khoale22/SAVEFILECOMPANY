package com.heb.pm.entity;

import java.util.List;

/**
 * This Object takes all of the current changes to a products Tags and Specs Attributes and feeds it to the webservice
 * @author s753601
 * @version 2.14.0
 */
public class TagsAndSpecsAttributeUpdate {

	private static final int FOUR_BYTES = 32;
	private Long prodId;
	private List<TagsAndSpecsAttribute> updates;
	private List<TagsAndSpecsAttribute> deletes;

	/**
	 * Gets the product id for the tags and specs being updated
	 * @return
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * Updates the prodId
	 * @param prodId
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	/**
	 * Gets all of the attributes that are either being edited or added
	 * @return
	 */
	public List<TagsAndSpecsAttribute> getUpdates() {
		return updates;
	}

	/**
	 * updates the updates
	 * @param updates
	 */
	public void setUpdates(List<TagsAndSpecsAttribute> updates) {
		this.updates = updates;
	}

	/**
	 * Gets all of the attributes the product is deleting
	 * @return
	 */
	public List<TagsAndSpecsAttribute> getDeletes() {
		return deletes;
	}

	/**
	 * updates the deletes
	 * @param deletes
	 */
	public void setDeletes(List<TagsAndSpecsAttribute> deletes) {
		this.deletes = deletes;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "TagsAndSpecsAttributeUpdate{" +
				"prodId=" + prodId +
				", updates=" + updates +
				", deletes=" + deletes +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is an ItemMaster, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TagsAndSpecsAttributeUpdate)) return false;

		TagsAndSpecsAttributeUpdate that = (TagsAndSpecsAttributeUpdate) o;

		return prodId == that.prodId;

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return (int) (prodId ^ (prodId >>> FOUR_BYTES));
	}
}
