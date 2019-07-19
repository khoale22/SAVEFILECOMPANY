/*
 * DynamicAttributeKey
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of a dynamic attribute.
 *
 * @author d116773
 * @since 2.0.7
 */
@Embeddable
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class DynamicAttributeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	private static final String KEY_SORT_FIELD = "key.key";

	@Column(name = "attr_id")
	private int attributeId;

	@Column(name = "key_id")
	private long key;

	@Column(name = "itm_prod_key_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK") 
	private String keyType;

	@Column(name = "seq_nbr")
	private int sequenceNumber;

	@Column(name="dta_src_sys")
	private int sourceSystem;

	/**
	 * Returns the ID of the attribute this key is for.
	 *
	 * @return The ID of the attribute this key is for.
	 */
	public int getAttributeId() {
		return attributeId;
	}

	/**
	 * Sets the ID of the attribute this key is for.
	 *
	 * @param attributeId The ID of the attribute this key is for.
	 */
	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * Returns the ID of the object this is for. Will either be a UPC, item code, or product ID.
	 *
	 * @return The ID of the object this is for.
	 */
	public long getKey() {
		return key;
	}

	/**
	 * Sets the ID of the object this is for.
	 *
	 * @param key The ID of the object this is for.
	 */
	public void setKey(long key) {
		this.key = key;
	}

	/**
	 * Returns the type of object this is for. It can be either a UPC, item, or product.
	 *
	 * @return The type of object this is for.
	 */
	public String getKeyType() {
		return keyType;
	}

	/**
	 * Sets the type of object this is for.
	 *
	 * @param keyType The type of object this is for.
	 */
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	/**
	 * An attribute can have multiple values. This sequence number provides uniqueness.
	 *
	 * @return The sequence number of the attribute value.
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Sets the sequence number of the attribute value.
	 *
	 * @param sequenceNumber The sequence number of the attribute value.
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Returns the system this attribute value came from.
	 *
	 * @return The system this attribute value came from.
	 */
	public int getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * Sets the system this attribute value came from.
	 *
	 * @param sourceSystem the system this attribute value came from.
	 */
	public void setSourceSystem(int sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "DynamicAttributeKey{" +
				"attributeId=" + attributeId +
				", key=" + key +
				", keyType='" + keyType + '\'' +
				", sequenceNumber=" + sequenceNumber +
				", sourceSystem=" + sourceSystem +
				'}';
	}

	/**
	 * Tests this object to another for equality.
	 *
	 * @param o The object to test against.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DynamicAttributeKey)) return false;

		DynamicAttributeKey that = (DynamicAttributeKey) o;

		if (attributeId != that.attributeId) return false;
		if (key != that.key) return false;
		if (sequenceNumber != that.sequenceNumber) return false;
		if (sourceSystem != that.sourceSystem) return false;
		return keyType.equals(that.keyType);

	}

	/**
	 * Returns a hash code for this object. Equal objects return the same value. Unequal objects (probably)
	 * return different values.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = (attributeId ^ (attributeId >>> DynamicAttributeKey.FOUR_BYTES));
		result = DynamicAttributeKey.PRIME_NUMBER * result + (int) (key ^ (key >>> DynamicAttributeKey.FOUR_BYTES));
		result = DynamicAttributeKey.PRIME_NUMBER * result + keyType.hashCode();
		result = DynamicAttributeKey.PRIME_NUMBER * result + sequenceNumber;
		result = DynamicAttributeKey.PRIME_NUMBER * result + sourceSystem;
		return result;
	}

	/**
	 * Returns the default sort for searches using this table.
	 *
	 * @return The default sort for searches using this table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(new Sort.Order(Sort.Direction.ASC, DynamicAttributeKey.KEY_SORT_FIELD));
	}
}
