package com.heb.pm.productDetails.product.taxonomy;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;

/**
 * Model representing a product attribute.
 *
 * @author m314029
 * @since 2.18.4
 */
public class ProductAttribute implements Serializable, Comparable<ProductAttribute> {
	private static final long serialVersionUID = 4583004192844663791L;

	private Long attributeId;
	private Long keyId;
	private String keyType;
	private String attributeName;
	private List<ProductAttributeValue> values;
	private boolean multipleAllowed;
	private boolean listOfValues;
	private List<ProductAttributeHierarchyLevel> hierarchyLevels;
	private Long precision;
	private Long maximumLength;
	private String domain;
	private int hierarchyLevelPosition;

	// newly requested values
	private String newValues;

	// note for newly requested values
	private String note;

	public ProductAttribute() {
	}

	public ProductAttribute(
			Long attributeId, Long keyId, String keyType, String attributeName, List<ProductAttributeValue> values,
			boolean multipleAllowed, boolean listOfValues, List<ProductAttributeHierarchyLevel> hierarchyLevels,
			Long precision, Long maximumLength, String domain) {
		this.attributeId = attributeId;
		this.keyId = keyId;
		this.keyType = keyType;
		this.attributeName = attributeName;
		this.values = values;
		this.multipleAllowed = multipleAllowed;
		this.listOfValues = listOfValues;
		this.hierarchyLevels = hierarchyLevels;
		this.precision = precision;
		this.maximumLength = maximumLength;
		this.domain = domain;
	}

	/**
	 * Returns AttributeId.
	 *
	 * @return The AttributeId.
	 **/
	public Long getAttributeId() {
		return attributeId;
	}

	/**
	 * Returns AttributeName.
	 *
	 * @return The AttributeName.
	 **/
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Returns Values.
	 *
	 * @return The Values.
	 **/
	public List<ProductAttributeValue> getValues() {
		return values;
	}

	/**
	 * Returns HierarchyLevels.
	 *
	 * @return The HierarchyLevels.
	 **/
	public List<ProductAttributeHierarchyLevel> getHierarchyLevels() {
		return hierarchyLevels;
	}

	/**
	 * Returns MultipleAllowed.
	 *
	 * @return The MultipleAllowed.
	 **/
	public boolean isMultipleAllowed() {
		return multipleAllowed;
	}

	/**
	 * Returns listOfValues.
	 * @return The ListOfValues.
	 */
	public boolean isListOfValues() {
		return listOfValues;
	}

	/**
	 * Returns Precision.
	 *
	 * @return The Precision.
	 **/
	public Long getPrecision() {
		return precision;
	}

	/**
	 * Returns MaximumLength.
	 *
	 * @return The MaximumLength.
	 **/
	public Long getMaximumLength() {
		return maximumLength;
	}

	/**
	 * Returns keyId.
	 *
	 * @return The keyId.
	 */
	public Long getKeyId() {
		return keyId;
	}

	/**
	 * Returns keyType.
	 *
	 * @return the keyType.
	 */
	public String getKeyType() {
		return keyType;
	}

	/**
	 * Returns Domain.
	 *
	 * @return the Domain.
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the AttributeId.
	 *
	 * @param attributeId The AttributeId.
	 **/
	public ProductAttribute setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
		return this;
	}

	/**
	 * Sets the KeyId.
	 *
	 * @param keyId The KeyId.
	 **/
	public ProductAttribute setKeyId(Long keyId) {
		this.keyId = keyId;
		return this;
	}

	/**
	 * Sets the KeyType.
	 *
	 * @param keyType The KeyType.
	 **/
	public ProductAttribute setKeyType(String keyType) {
		this.keyType = keyType;
		return this;
	}

	/**
	 * Sets the AttributeName.
	 *
	 * @param attributeName The AttributeName.
	 **/
	public ProductAttribute setAttributeName(String attributeName) {
		this.attributeName = attributeName;
		return this;
	}

	/**
	 * Sets the Values.
	 *
	 * @param values The Values.
	 **/
	public ProductAttribute setValues(List<ProductAttributeValue> values) {
		this.values = values;
		return this;
	}

	/**
	 * Sets the MultipleAllowed.
	 *
	 * @param multipleAllowed The MultipleAllowed.
	 **/
	public ProductAttribute setMultipleAllowed(boolean multipleAllowed) {
		this.multipleAllowed = multipleAllowed;
		return this;
	}

	/**
	 * Sets the ListOfValues.
	 *
	 * @param listOfValues The ListOfValues.
	 **/
	public ProductAttribute setListOfValues(boolean listOfValues) {
		this.listOfValues = listOfValues;
		return this;
	}

	/**
	 * Sets the HierarchyLevels.
	 *
	 * @param hierarchyLevels The HierarchyLevels.
	 **/
	public ProductAttribute setHierarchyLevels(List<ProductAttributeHierarchyLevel> hierarchyLevels) {
		this.hierarchyLevels = hierarchyLevels;
		return this;
	}

	/**
	 * Sets the Precision.
	 *
	 * @param precision The Precision.
	 **/
	public ProductAttribute setPrecision(Long precision) {
		this.precision = precision;
		return this;
	}

	/**
	 * Sets the MaximumLength.
	 *
	 * @param maximumLength The MaximumLength.
	 **/
	public ProductAttribute setMaximumLength(Long maximumLength) {
		this.maximumLength = maximumLength;
		return this;
	}

	/**
	 * Sets the Domain.
	 *
	 * @param domain The Domain.
	 **/
	public ProductAttribute setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	/**
	 * Gets the new values
	 * @return newValues
	 */
	public String getNewValues() {
		return newValues;
	}

	/**
	 * Sets the newValues
	 * @param newValues
	 */
	public void setNewValues(String newValues) {
		this.newValues = newValues;
	}

	/**
	 * Get the note
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Set the note
	 * @param note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Returns HierarchyLevelPosition.
	 *
	 * @return The HierarchyLevelPosition.
	 **/
	public int getHierarchyLevelPosition() {
		return hierarchyLevelPosition;
	}

	/**
	 * Sets the HierarchyLevelPosition.
	 *
	 * @param hierarchyLevelPosition The HierarchyLevelPosition.
	 **/
	public ProductAttribute setHierarchyLevelPosition(int hierarchyLevelPosition) {
		this.hierarchyLevelPosition = hierarchyLevelPosition;
		return this;
	}

	/**
	 * This method implements comparable so lists of ProductAttribute can be sorted as currently described by the
	 * business:
	 * 1. Hierarchy level position (distance from Product up to Hierarchy Level in the hierarchy) with immediate
	 * parent(s) being first, and the root nodes being last.
	 * 2. Attribute name (ascending A-Z)
	 * 3. Id of the {Product, UPC, Case Pack} ascending.
	 *
	 * @param toCompare The ProductAttribute to compare against this ProductAttribute.
	 * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
	 * the passed in ProductAttribute.
	 */
	@Override
	public int compareTo(@Nonnull ProductAttribute toCompare) {

		// sort by hierarchy level position
		if(this.getHierarchyLevelPosition() != toCompare.getHierarchyLevelPosition()){
			return this.getHierarchyLevelPosition() - toCompare.getHierarchyLevelPosition();
		}
		int compareTo;

		// sort by attribute name
		compareTo = this.getAttributeName().compareTo(toCompare.getAttributeName());
		if(compareTo != 0) {
			return compareTo;
		}

		// sort by id
		return this.getKeyId().compareTo(toCompare.getKeyId());
	}
}
