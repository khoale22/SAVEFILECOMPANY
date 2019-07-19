package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key for the generic entity description entity.
 *
 * @author m314029
 * @since 2.12.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class GenericEntityDescriptionKey implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	// default constructor
	public GenericEntityDescriptionKey(){super();}

	// copy constructor
	public GenericEntityDescriptionKey(GenericEntityDescriptionKey key){
		super();
		this.setEntityId(key.getEntityId());
		this.setHierarchyContext(key.getHierarchyContext());
	}

	@Column(name="enty_id")
	private Long entityId;

	@Column(name="hier_cntxt_cd")
	@Type(type="fixedLengthCharPK")
	private String hierarchyContext;

	/**
	 * Gets entity id. This is the id of the generic entity this generic entity description is for.
	 *
	 * @return the entity id
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * Sets entity id.
	 *
	 * @param entityId the entity id
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * Gets hierarchy context. This is the hierarchy context this generic entity description is for.
	 *
	 * @return the hierarchy context
	 */
	public String getHierarchyContext() {
		return hierarchyContext;
	}

	/**
	 * Sets hierarchy context.
	 *
	 * @param hierarchyContext the hierarchy context
	 */
	public void setHierarchyContext(String hierarchyContext) {
		this.hierarchyContext = hierarchyContext;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GenericEntityDescriptionKey that = (GenericEntityDescriptionKey) o;

		if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
		return hierarchyContext != null ? hierarchyContext.equals(that.hierarchyContext) : that.hierarchyContext == null;
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
		result = PRIME_NUMBER * result + (hierarchyContext != null ? hierarchyContext.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "GenericEntityDescriptionKey{" +
				"entityId=" + entityId +
				", hierarchyContext='" + hierarchyContext + '\'' +
				'}';
	}
}
