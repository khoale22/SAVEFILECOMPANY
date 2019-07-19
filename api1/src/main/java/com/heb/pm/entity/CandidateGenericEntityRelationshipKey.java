/*
 *  CandidateGenericEntityRelationshipKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * This is the key for the candidate generic entity relationship.
 *
 * @author l730832
 * @since 2.17.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class CandidateGenericEntityRelationshipKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "ps_work_id")
	private Long workRequestId;

	@Column(name="parnt_enty_id")
	private Long parentEntityId;

	@Column(name="child_enty_id")
	private Long childEntityId;

	@Column(name="hier_cntxt_cd")
	@Type(type="fixedLengthCharPK")
	private String hierarchyContext;

	/**
	 * Returns the WorkRequestId
	 *
	 * @return WorkRequestId
	 */
	public Long getWorkRequestId() {
		return workRequestId;
	}

	/**
	 * Sets the WorkRequestId
	 *
	 * @param workRequestId The WorkRequestId
	 */
	public void setWorkRequestId(Long workRequestId) {
		this.workRequestId = workRequestId;
	}

	/**
	 * Returns the ParentEntityId
	 *
	 * @return ParentEntityId
	 */
	public Long getParentEntityId() {
		return parentEntityId;
	}

	/**
	 * Sets the ParentEntityId
	 *
	 * @param parentEntityId The ParentEntityId
	 */
	public void setParentEntityId(Long parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	/**
	 * Returns the ChildEntityId
	 *
	 * @return ChildEntityId
	 */
	public Long getChildEntityId() {
		return childEntityId;
	}

	/**
	 * Sets the ChildEntityId
	 *
	 * @param childEntityId The ChildEntityId
	 */
	public void setChildEntityId(Long childEntityId) {
		this.childEntityId = childEntityId;
	}

	/**
	 * Returns the HierarchyContext
	 *
	 * @return HierarchyContext
	 */
	public String getHierarchyContext() {
		return hierarchyContext;
	}

	/**
	 * Sets the HierarchyContext
	 *
	 * @param hierarchyContext The HierarchyContext
	 */
	public void setHierarchyContext(String hierarchyContext) {
		this.hierarchyContext = hierarchyContext;
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

		CandidateGenericEntityRelationshipKey that = (CandidateGenericEntityRelationshipKey) o;

		if (workRequestId != null ? !workRequestId.equals(that.workRequestId) : that.workRequestId != null)
			return false;
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
		int result = workRequestId != null ? workRequestId.hashCode() : 0;
		result = PRIME_NUMBER * result + (parentEntityId != null ? parentEntityId.hashCode() : 0);
		result = PRIME_NUMBER * result + (childEntityId != null ? childEntityId.hashCode() : 0);
		result = PRIME_NUMBER * result + (hierarchyContext != null ? hierarchyContext.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "CandidateGenericEntityRelationshipKey{" +
				"workRequestId=" + workRequestId +
				", parentEntityId=" + parentEntityId +
				", childEntityId=" + childEntityId +
				", hierarchyContext='" + hierarchyContext + '\'' +
				'}';
	}
}
