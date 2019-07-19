package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This object acts as a mapping between attributes and attributes codes for dynamic attributes.  Entity Attribute Codes
 * will return rows that for each attribute what possible attribute codes are valid.
 *
 * @author s753601
 * @version 2.14.0
 */
@Entity
@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
@Table(name = "ENTY_ATTR_CD")
public class EntityAttributeCode  implements Serializable{

	public static final int NOOP = 0;
	public static final int INSERT= 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;

	@EmbeddedId
	private EntityAttributeCodeKey key;

	@Column(name="LST_UPDT_UID")
	private String lastUpdateUserId;

	@Column(name="CRE8_UID")
	private String createUserId;

	@Column(name="LST_UPDT_TS")
	private LocalDateTime lastUpdateTime;

	@Column(name="CRE8_TS")
	private LocalDateTime createTime;

	@OneToOne
	@JoinColumn(name = "ATTR_CD_ID", referencedColumnName = "ATTR_CD_ID", insertable = false, updatable = false)
	private AttributeCode attributeCode;

	@Transient
	private Integer action;

	@Transient
	private String operation;

	@Transient
	private String entityDescription;

	/**
	 * Getter for entity description
	 * @return
	 */
	public String getEntityDescription() {
		return entityDescription;
	}

	/**
	 * Setter for entity description
	 * @param entityDescription
	 */
	public void setEntityDescription(String entityDescription) {
		this.entityDescription = entityDescription;
	}

	/**
	 * Get operaton attribute
	 * @return
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Set operaton attribute
	 * @param operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * returns the identification key object for an entity attribute code
	 * @return
	 */
	public EntityAttributeCodeKey getKey() {
		return key;
	}

	/**
	 * Updates the key
	 * @param key
	 */
	public void setKey(EntityAttributeCodeKey key) {
		this.key = key;
	}

	/**
	 * This returns the attribute code tied to the entity attribute code
	 * @return
	 */
	public AttributeCode getAttributeCode() {
		return attributeCode;
	}

	/**
	 * updates the attributeCode
	 * @param attributeCode
	 */
	public void setAttributeCode(AttributeCode attributeCode) {
		this.attributeCode = attributeCode;
	}

	/**
	 * Return the one-pass ID of the user who last updated this record.
	 *
	 * @return The one-pass ID of the user who last updated this record.
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the one-pass ID of the user who last updated this record.
	 *
	 * @param lastUpdateUserId The one-pass ID of the user who last updated this record.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * Returns the one-pass ID of the user who created this record.
	 *
	 * @return The one-pass ID of the user who created this record.
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * Sets the one-pass ID of the user who created this record.
	 *
	 * @param createUserId The one-pass ID of the user who created this record.
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Returns the time of the last update to this record.
	 *
	 * @return The time of the last update to this record.
	 */
	public LocalDateTime getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * Sets the time of the last update to this record.
	 *
	 * @param lastUpdateTime The time of the last update to this record.
	 */
	public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * Returns the time this record was created.
	 *
	 * @return The time this record was created.
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the time this record was created.
	 *
	 * @param createTime The time this record was created.
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * Returns the action to perform with this entity.
	 *
	 * @return The action to perform with this entity.
	 */
	public Integer getAction() {
		return action;
	}

	/**
	 * Sets the action to perform with this entity.
	 *
	 * @param action The action to perform with this entity.
	 */
	public void setAction(Integer action) {
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

		EntityAttributeCode that = (EntityAttributeCode) o;

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
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "EntityAttributeCode{" +
				"key=" + key +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				", createUserId='" + createUserId + '\'' +
				", lastUpdateTime=" + lastUpdateTime +
				", createTime=" + createTime +
				", attributeCode=" + attributeCode +
				", action=" + action +
				'}';
	}
}

