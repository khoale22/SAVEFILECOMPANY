/*
 *
 * DiscontinueExceptionRules
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 *
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Represents the exception rules for product discontinue.
 *
 * @author s573181
 * @since 2.0.2
 */
@Entity
@Table(name = "prod_del_excp_rule")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
@TypeDef(name = "fixedChar", typeClass = com.heb.pm.util.oracle.OracleCharType.class)
})
// dB2Oracle changes vn00907
public class DiscontinueExceptionParameters implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final String ID_SORT_FIELD = "key.id";
	private static final String SEQUENCE_NUMBER_FIELD = "key.sequenceNumber";
	private static final String EXCEPTION_NUMBER_FIELD = "key.exceptionNumber";

	/**
	 * Returns the default sort for the class. It is by exception sequence number, ID, sequence number. That way
	 * all of the ones that are about the same thing (a given vendor, a given UPC, etc), come out together.
	 *
	 * @return The default sort for this class.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, DiscontinueExceptionParameters.EXCEPTION_NUMBER_FIELD),
				new Sort.Order(Sort.Direction.ASC, DiscontinueExceptionParameters.ID_SORT_FIELD),
				new Sort.Order(Sort.Direction.ASC, DiscontinueExceptionParameters.SEQUENCE_NUMBER_FIELD)

		);
	}

	@EmbeddedId
	private DiscontinueExceptionParametersKey key;

	@Embedded
	private DiscontinueParameterCommonAttributes attributes = new DiscontinueParameterCommonAttributes();

	@Column(name = "excp_attr_val_txt")
	//db2o changes  vn00907
	@Type(type="fixedChar") 
	private String exceptionTypeId;

	@Column(name = "excp_attr_nm")
	//db2o changes  vn00907
	@Type(type="fixedChar") 
	private String exceptionType;

	@Column(name = "nev_del_sw")
	private boolean neverDelete;


	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public DiscontinueExceptionParametersKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key The key for this object.
	 */
	public void setKey(DiscontinueExceptionParametersKey key) {
		this.key = key;
	}

	/**
	 * Returns the value for the discontinue job to use for this rule. For example, if this record represents
	 * a last scan date for a vendor, it is the number of days since the last scan for a UPC attached to an item before
	 * the item can be discontinued.
	 *
	 * @return The value for the discontinue job to use for this rule.
	 */
	public String getParameterValue() {
		return this.attributes.parameterValue;
	}

	/**
	 * Sets value for the discontinue job to use for this rule.
	 *
	 * @param parameterValue The value for the discontinue job to use for this rule.
	 */
	public void setParameterValue(String parameterValue) {
		this.attributes.parameterValue = parameterValue;
	}

	/**
	 * Returns the priority of this rule.
	 *
	 * @return The priority of this rule.
	 */
	public int getPriority() {
		return this.attributes.priority;
	}

	/**
	 * Sets the priority of this rule.
	 *
	 * @param priority The priority of this rule.
	 */
	public void setPriority(int priority) {
		this.attributes.priority = priority;
	}

	/**
	 * Returns the identifier for who this rule applies to. For example, if this is a vendor rule, it is the vendor
	 * number.
	 *
	 * @return The identifier for who this rule applies to.
	 */
	public String getExceptionTypeId() {
		return exceptionTypeId;
	}

	/**
	 * Sets the identifier for who this rule applies to. For example, if this is a vendor rule, it is the vendor
	 * number.
	 *
	 * @param exceptionTypeId The identifier for who this rule applies to.
	 */
	public void setExceptionTypeId(String exceptionTypeId) {
		this.exceptionTypeId = exceptionTypeId;
	}

	/**
	 * Returns the type of exception this is. For example, if this rule is about a vendor, it returns 'Vendor'.
	 * These are defined in the ProductDiscontinueExceptionType class.
	 *
	 * @return The exception type.
	 */
	public String getExceptionType() {
		return exceptionType;
	}

	/**
	 * Sets the exception type.
	 *
	 * @param exceptionType The exception type.
	 */
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	/**
	 * Returns whether or not this rule is active.
	 *
	 * @return True if this rule is active and false otherwise.
	 */
	public boolean isActive() {
		return this.attributes.active;
	}

	/**
	 * Sets whether or not this rule is active.
	 *
	 * @param active True if this rule is active and false otherwise.
	 */
	public void setActive(boolean active) {
		this.attributes.active = active;
	}

	/**
	 * Returns whether or not items matching this type and value should ever be deleted.
	 *
	 * @return True if items matching this type and value should never be deleted and false otherwise.
	 */
	public boolean isNeverDelete() {
		return neverDelete;
	}

	/**
	 * Sets whether or not items matching this type and value should ever be deleted.
	 *
	 * @param neverDelete True if items matching this type and value should never be deleted and false otherwise.
	 */
	public void setNeverDelete(boolean neverDelete) {
		this.neverDelete = neverDelete;
	}

	/**
	 * Returns the attributes that are common between default and exception parameters as an object. This should
	 * generally not be used and rather the getters and setters of the attributes themselves, but this is available
	 * where needed.
	 *
	 * @return The attributes that are common between default and exception parameters as an object.
	 */
	public DiscontinueParameterCommonAttributes getAttributes() {
		return attributes;
	}

	/**
	 * Returns whether a DiscontinueExceptionParameters has exactly the same elements as the given
	 * DiscontinueExceptionParameters.
	 *
	 * @param exceptionParameters DiscontinueExceptionParameters to compare to.
	 * @return true if they are equal, false otherwise.
	 */
	public boolean fullItemCompare(DiscontinueExceptionParameters exceptionParameters){
		if (this == exceptionParameters) return true;
		if (exceptionParameters == null || getClass() != exceptionParameters.getClass()) return false;

		if (neverDelete != exceptionParameters.neverDelete) return false;
		if (attributes != null ? !attributes.equals(exceptionParameters.attributes) : exceptionParameters.attributes != null) return false;
		if (exceptionType != null ? !exceptionType.equals(exceptionParameters.exceptionType) : exceptionParameters.exceptionType != null)
			return false;
		if (exceptionTypeId != null ? !exceptionTypeId.equals(exceptionParameters.exceptionTypeId) : exceptionParameters.exceptionTypeId != null)
			return false;
		if (key != null ? !key.equals(exceptionParameters.key) : exceptionParameters.key != null) return false;

		return true;
	}

	/**
	 * Tests equality against this object. Equality is based on the key.
	 *
	 * @param o The object to test against.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DiscontinueExceptionParameters)) return false;

		DiscontinueExceptionParameters that = (DiscontinueExceptionParameters) o;

		return !(key != null ? !key.equals(that.key) : that.key != null);

	}

	/**
	 * Returns a hash code for this object. Equal objects return the same hash code. Unequal objects (probably)
	 * return different values.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}


	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueExceptionParameters{" +
				"key=" + key +
				", parameterValue='" + attributes.parameterValue + '\'' +
				", priority=" + attributes.priority +
				", exceptionTypeId='" + exceptionTypeId + '\'' +
				", exceptionType='" + exceptionType + '\'' +
				", active=" + this.attributes.active +
				", neverDelete=" + neverDelete +
				'}';
	}
}
