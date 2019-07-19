package com.heb.pm.entity;

import java.util.List;

/**
 * This object is designed to take all of the data associated with tags and specs from the various
 * entities into one central location
 * @author s753601
 * @version 2.14.0
 */
public class TagsAndSpecsAttribute {

	private static final int FOUR_BYTES = 32;
	private Long prodId;
	private int attributeId;
	private String attributeName;
	private String alternativeAttributeCodeText;
	private Boolean multiValueFlag;

	private List<TagsAndSpecsValue> values;

	/**
	 * This value is the product identification code for which this attribute belongs to
	 * @return
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * updates the prodId
	 * @param prodId
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	/**
	 * This is the identification code for the attribute the product currently has
	 * @return
	 */
	public int getAttributeId() {
		return attributeId;
	}

	/**
	 * This updates the attribute ID
	 * @param attributeId
	 */
	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * This is the human readable name of the attribute
	 * @return
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * This updates the human readable name for the attribute
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * This flag is set for if the attribute can hold more than one value
	 * @return
	 */
	public Boolean getMultiValueFlag() {
		return multiValueFlag;
	}

	/**
	 * This updates the multiValueFalg
	 * @param multiValueFlag
	 */
	public void setMultiValueFlag(Boolean multiValueFlag) {
		this.multiValueFlag = multiValueFlag;
	}

	/**
	 * This is a list of all possible values for an attribute
	 * @return
	 */
	public List<TagsAndSpecsValue> getValues() {
		return values;
	}

	/**
	 * Updates the list of all possible values
	 * @param values
	 */
	public void setValues(List<TagsAndSpecsValue> values) {
		this.values = values;
	}

	/**
	 * Gets the alternative attribute text value for attributes that don't have any attribute codes
	 * @return
	 */
	public String getAlternativeAttributeCodeText() {
		return alternativeAttributeCodeText;
	}

	/**
	 * Updates the alternativeAttributeCodeText
	 * @param alternativeAttributeCodeText
	 */
	public void setAlternativeAttributeCodeText(String alternativeAttributeCodeText) {
		this.alternativeAttributeCodeText = alternativeAttributeCodeText;
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
		if (!(o instanceof TagsAndSpecsAttribute)) return false;

		TagsAndSpecsAttribute that = (TagsAndSpecsAttribute) o;

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
