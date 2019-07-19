/*
 * com.heb.pm.entity.ProductDiscontinue
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Holds the parameters that indicate information relating to the logical physical relationships.
 *
 * @author s753601
 * @since 2.5.0
 */
@Entity
@Table(name="LOGIC_PHY_RLSHP")
public class LogicalPhysicalRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private LogicalPhysicalRelationshipKey key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "LOGIC_ATTR_ID", referencedColumnName = "ATTR_ID", insertable = false, updatable = false)
    })
    private Attribute attribute;

    @OneToMany(mappedBy = "logicalPhysicalRelationship", fetch = FetchType.LAZY)
    @OrderBy(value = "ATTR_PRTY_NBR")
    private List<TargetSystemAttributePriority> targetSystemAttributePriorities;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHY_ATTR_ID", referencedColumnName = "RLSHP_GRP_ID", insertable = false, updatable = false)
    private RelationshipGroup relationshipGroup;

    /**
     * Returns the key
     * @return the key
     */
    public LogicalPhysicalRelationshipKey getKey() {
        return key;
    }

    /**
     * Updates the key
     * @param key the new key
     */
    public void setKey(LogicalPhysicalRelationshipKey key) {
        this.key = key;
    }

    /**
     * Gets the current attribute
     * @return the current attribute
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Updates the attribute
     * @param attribute the new attribute
     */
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Returns the list of target system attribute priority
     * @return the list of target system attribute priority
     */
    public List<TargetSystemAttributePriority> getTargetSystemAttributePriorities() {
        return targetSystemAttributePriorities;
    }

    /**
     * Updates the list of target system attribute priority
     * @param targetSystemAttributePriorities the new list of target system attribute priority
     */
    public void setTargetSystemAttributePriorities(List<TargetSystemAttributePriority> targetSystemAttributePriorities) {
        this.targetSystemAttributePriorities = targetSystemAttributePriorities;
    }

    /**
     * Returns the Optional Relationship group
     * @return the relationship group
     */
    public RelationshipGroup getRelationshipGroup() {
        return relationshipGroup;
    }

    /**
     * Updates the relationship group
     * @param relationshipGroup the new relationship group
     */
    public void setRelationshipGroup(RelationshipGroup relationshipGroup) {
        this.relationshipGroup = relationshipGroup;
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

		LogicalPhysicalRelationship that = (LogicalPhysicalRelationship) o;

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
		return "LogicalPhysicalRelationship{" +
				"key=" + key +
				'}';
	}
}
