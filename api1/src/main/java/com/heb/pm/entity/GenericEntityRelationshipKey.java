package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key for a generic entity relationship.
 *
 * @author m314029
 * @since 2.12.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class GenericEntityRelationshipKey implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	// default constructor
	public GenericEntityRelationshipKey(){super();}

	// copy constructor
	public GenericEntityRelationshipKey(GenericEntityRelationshipKey key){
		super();
		this.setParentEntityId(key.getParentEntityId());
		this.setChildEntityId(key.getChildEntityId());
		this.setHierarchyContext(key.getHierarchyContext());
	}

	@Column(name="parnt_enty_id")
	private Long parentEntityId;

	@Column(name="child_enty_id")
	private Long childEntityId;

	@Column(name="hier_cntxt_cd")
	@Type(type="fixedLengthCharPK")
	private String hierarchyContext;

	/**
	 * Gets parent entity id. This is the id of the parent generic entity in this relationship.
	 *
	 * @return the parent entity id
	 */
	public Long getParentEntityId() {
		return parentEntityId;
	}

	/**
	 * Sets parent entity id.
	 *
	 * @param parentEntityId the parent entity id
	 */
	public void setParentEntityId(Long parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	/**
	 * Gets child entity id. This is the id of the child generic entity in this relationship.
	 *
	 * @return the child entity id
	 */
	public Long getChildEntityId() {
		return childEntityId;
	}

	/**
	 * Sets child entity id.
	 *
	 * @param childEntityId the child entity id
	 */
	public void setChildEntityId(Long childEntityId) {
		this.childEntityId = childEntityId;
	}

	/**
	 * Gets hierarchy context. This is the hierarchy context using this relationship.
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

		GenericEntityRelationshipKey that = (GenericEntityRelationshipKey) o;

		if (parentEntityId != null ? !parentEntityId.equals(that.parentEntityId) : that.parentEntityId != null)
			return false;
		if (childEntityId != null ? !childEntityId.equals(that.childEntityId) : that.childEntityId != null)
			return false;
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
		int result = parentEntityId != null ? parentEntityId.hashCode() : 0;
		result = PRIME_NUMBER * result + (childEntityId != null ? childEntityId.hashCode() : 0);
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
		return "GenericEntityRelationshipKey{" +
				"parentEntityId=" + parentEntityId +
				", childEntityId=" + childEntityId +
				", hierarchyContext='" + hierarchyContext + '\'' +
				'}';
	}
}
