package com.heb.pm.entity;

/**
 * This object holds all the information needed for attribute codes that are assoicated with a tags and specs attribute
 * @author s753601
 * @version 2.14.0
 */
public class TagsAndSpecsValue {
	private long attributeCodeId;
	private String name;
	private Boolean selected;
	private Integer sequenceNumber;
	private static final int FOUR_BYTES = 32;

	/**
	 * The attribute code that is tied to the attribute
	 * @return
	 */
	public long getAttributeCodeId() {
		return attributeCodeId;
	}

	/**
	 * updates the attribute code id
	 * @param attributeCodeId
	 */
	public void setAttributeCodeId(long attributeCodeId) {
		this.attributeCodeId = attributeCodeId;
	}

	/**
	 * The human readable name of the attriubte code
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Updates the name of the attribute code
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns if the current value is active on the attribute
	 */
	public Boolean getSelected() {
		return selected;
	}

	/**
	 * Updates the selected
	 * @param selected
	 */
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	/**
	 * Returns the sequence number used for attributes with multiple values
	 * @return
	 */
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Updates the sequenceNumber
	 * @param sequenceNumber
	 */
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "TagsAndSpecsValue{" +
				"attributeCodeId=" + attributeCodeId +
				", name='" + name + '\'' +
				", selected=" + selected +
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
		if (!(o instanceof TagsAndSpecsValue)) return false;

		TagsAndSpecsValue that = (TagsAndSpecsValue) o;

		return attributeCodeId == that.attributeCodeId;

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return (int) (attributeCodeId ^ (attributeCodeId >>> FOUR_BYTES));
	}
}
