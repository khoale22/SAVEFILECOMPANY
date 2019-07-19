package com.heb.pm.entity;




import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This Entity is used to uniquely Identify EntityAttributeCodes which bridge attributes and their values
 * @author s753601
 * @version 2.14.0
 */
@Embeddable
public class EntityAttributeCodeKey implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "ENTY_ID")
	private Long entityId;

	@Column(name = "ATTR_ID")
	private Long attributeId;

	@Column(name = "ATTR_CD_ID")
	private Long attributeCodeId;

	/**
	 * Returns the entity code for the object
	 * @return
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * Updates the entity code for the object
	 * @param entityId
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * Returns the attribute id for the object
	 * @return
	 */
	public Long getAttributeId() {
		return attributeId;
	}

	/**
	 * Updates the attribute id for the object
	 * @param attributeId
	 */
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * Returns the attribute code or value for the object
	 * @return
	 */
	public Long getAttributeCodeId() {
		return attributeCodeId;
	}

	/**
	 * Updates the attribute code for the object
	 * @param attributeCodeId
	 */
	public void setAttributeCodeId(Long attributeCodeId) {
		this.attributeCodeId = attributeCodeId;
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

		EntityAttributeCodeKey that = (EntityAttributeCodeKey) o;

		if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
		if (attributeId != that.attributeId) return false;
		if (attributeCodeId != null ? !attributeCodeId.equals(that.attributeCodeId) : that.attributeCodeId != null)
			return false;
		return true;
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
		result = PRIME_NUMBER * result + (int) (attributeId ^ (attributeId >>> 32));
		result = PRIME_NUMBER * result + (int) (attributeCodeId ^ (attributeCodeId >>> 32));
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "EntityAttributeCodeKey{" +
				"entityId=" + entityId +
				", attributeId=" + attributeId +
				", attributeCodeId=" + attributeCodeId +
				'}';
	}
}
