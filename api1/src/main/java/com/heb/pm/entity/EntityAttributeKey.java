/*
 *  EntityAttributeKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This is the Entity Attribute Key.
 *
 * @author l730832
 * @since 2.13.0
 */
@Embeddable
public class EntityAttributeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "enty_id")
	private Long entityId;

	@Column(name = "attr_id")
	private Long attributeId;

	/**
	 * Returns the EntityId. This is the corresponding entity id.
	 *
	 * @return EntityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * Sets the EntityId
	 *
	 * @param entityId The EntityId
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * Returns the AttributeId. This is the corresponding attribute Id.
	 *
	 * @return AttributeId
	 */
	public Long getAttributeId() {
		return attributeId;
	}

	/**
	 * Sets the AttributeId
	 *
	 * @param attributeId The AttributeId
	 */
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
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

		EntityAttributeKey that = (EntityAttributeKey) o;

		if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
		return attributeId != null ? attributeId.equals(that.attributeId) : that.attributeId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = entityId != null ? entityId.hashCode() : 0;
		result = PRIME_NUMBER * result + (attributeId != null ? attributeId.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "EntityAttributeKey{" +
				"entityId=" + entityId +
				", attributeId=" + attributeId +
				'}';
	}
}
