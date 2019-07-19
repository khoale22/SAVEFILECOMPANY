package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a hierarchy context.
 *
 * @author m314029
 * @since 2.12.0
 */
@Entity
@Table(name = "hier_cntxt")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class HierarchyContext implements Serializable {
	private static final long serialVersionUID = 1L;
	public enum HierarchyContextCode {
		GS1("GS1"),
		CUST("CUST"),
		HEBTO("HEBTO"),
		BLOOM("BLOOM");
		private String name;
		HierarchyContextCode(String name) {
			this.name = name;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
	}
	// default constructor
	public HierarchyContext(){super();}

	// copy constructor
	public HierarchyContext(HierarchyContext hierarchyContext){
		super();
		this.setId(hierarchyContext.getId());
		this.setParentEntityId(hierarchyContext.getParentEntityId());
		this.setDescription(hierarchyContext.getDescription());
	}

	@Id
	@Column(name="hier_cntxt_cd")
	@Type(type="fixedLengthCharPK")
	private String id;

	@Column(name="hier_cntxt_des")
	@Type(type="fixedLengthChar")
	private String description;

	@Column(name="parnt_enty_id")
	private Long parentEntityId;

	@Transient
	private List<GenericEntityRelationship> childRelationships;

	@Transient
	private Boolean isCollapsed = true;

	/**
	 * Gets id. This is the id for this hierarchy context.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets description. This is the description for this hierarchy context.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 *
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets parent entity id. This is the parent id of the generic entity for this hierarchy context.
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
	 * Gets child relationships. These are the first child relationships for this hierarchy context.
	 *
	 * @return the child relationships
	 */
	public List<GenericEntityRelationship> getChildRelationships() {
		return childRelationships;
	}

	/**
	 * Sets child relationships.
	 *
	 * @param childRelationships the child relationships
	 */
	public void setChildRelationships(List<GenericEntityRelationship> childRelationships) {
		this.childRelationships = childRelationships;
	}

	/**
	 * Flag to state if the hierarchy is open or closed
	 * @return isCollapsed
	 */
	public Boolean getIsCollapsed() {
		return isCollapsed;
	}

	/**
	 * Updates isCollapsed
	 * @param collapsed the new isCollapsed
	 */
	public void setIsCollapsed(Boolean collapsed) {
		isCollapsed = collapsed;
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

		HierarchyContext that = (HierarchyContext) o;

		return id != null ? id.equals(that.id) : that.id == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "HierarchyContext{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				", parentEntityId=" + parentEntityId +
				'}';
	}
}
