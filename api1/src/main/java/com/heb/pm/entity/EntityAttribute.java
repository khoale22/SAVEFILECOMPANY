/*
 *  EntityAttribute
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This represents an Entity Attribute.
 *
 * @author l730832
 * @since 2.13.0
 */
@Entity
@Table(name = "enty_attr")
public class EntityAttribute implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int NOOP = 0;
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;

	@EmbeddedId
	private EntityAttributeKey key;

	@Column(name = "enty_attr_fld_nm")
	private String entityAttributeFieldName;

	@Column(name = "ENTY_ATTR_SEQ_NBR")
	private Long sequenceNumber;

	@Column(name = "CRE8_UID")
	private String createUserId;

	@Column(name = "LST_UPDT_UID")
	private String lastUpdateUserId;

	@Column(name = "ATTR_REQ_SW")
	private Boolean required;

	@Column(name = "BAS_ATTR_SW")
	private Boolean baseAttribute;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "attr_id", referencedColumnName = "attr_id", insertable = false, updatable = false)
	Attribute attribute;

	@Transient
	private int action;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public EntityAttributeKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(EntityAttributeKey key) {
		this.key = key;
	}

	/**
	 * Returns the EntityAttributeFieldName. This represents the name of the entity attribute. "COLOR, Number of servings"
	 *
	 * @return EntityAttributeFieldName
	 */
	public String getEntityAttributeFieldName() {
		return entityAttributeFieldName;
	}

	/**
	 * Sets the EntityAttributeFieldName
	 *
	 * @param entityAttributeFieldName The EntityAttributeFieldName
	 */
	public void setEntityAttributeFieldName(String entityAttributeFieldName) {
		this.entityAttributeFieldName = entityAttributeFieldName;
	}

	/**
	 * Get the sequence number for the attribute
	 * @return
	 */
	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Updates the serquenceNumber for the attribute
	 * @param sequenceNumber
	 */
	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Returns the raw attribute tied to the entitiy attribute
	 * @return
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * updates the attribute
	 * @param attribute
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	/** Returns the ID of the user who created this record.
	 *
	 * @return The ID of the user who created this record.
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Sets the ID of the user who created this record.
	 *
	 * @param createUserId The ID of the user who created this record.
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Returns the ID of the user who last updated this record.
	 *
	 * @return The ID of the user who last updated this record.
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the ID of the user who last updated this record.
	 *
	 * @param lastUpdateUserId The ID of the user who last updated this record.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * Returns whether or not this attribute is required for this entity.
	 *
	 * @return Whether or not this attribute is required for this entity.
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * Sets Whether or not this attribute is required for this entity.
	 *
	 * @param required Whether or not this attribute is required for this entity.
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * Unknown. This is always Y.
	 *
	 * @return
	 */
	public Boolean getBaseAttribute() {
		return baseAttribute;
	}

	/**
	 * Unknown
	 * @param baseAttribute
	 */
	public void setBaseAttribute(Boolean baseAttribute) {
		this.baseAttribute = baseAttribute;
	}

	/**
	 * Returns the action to perform with this record.
	 *
	 * @return The action to perform with this record.
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Sets the action to perform with this record.
	 *
	 * @param action The action to perform with this record.
	 */
	public void setAction(int action) {
		this.action = action;
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
		if (o == null || getClass() != o.getClass()) return false;

		EntityAttribute that = (EntityAttribute) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
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
		return "EntityAttribute{" +
				"key=" + key +
				", entityAttributeFieldName='" + entityAttributeFieldName + '\'' +
				", sequenceNumber=" + sequenceNumber +
				", createUserId='" + createUserId + '\'' +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				", required=" + required +
				", baseAttribute=" + baseAttribute +
				'}';
	}
}

