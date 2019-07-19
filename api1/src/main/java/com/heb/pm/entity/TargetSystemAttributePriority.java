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
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Holds the target system attribute priority relationship.
 *
 * @author s753601
 * @since 2.5.0
 */
@Entity
@Table(name="TRGT_SYS_ATTR_PRTY")
public class TargetSystemAttributePriority implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TargetSystemAttributePriorityKey key;

    @Column(name = "ATTR_PRTY_NBR")
    private int attributePriorityNumber;


    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumns({
            @JoinColumn(name = "LOGIC_ATTR_ID", referencedColumnName = "LOGIC_ATTR_ID", insertable = false, updatable = false),
            @JoinColumn(name = "PHY_ATTR_ID", referencedColumnName = "PHY_ATTR_ID", insertable = false, updatable = false),
            @JoinColumn(name = "RLSHP_GRP_TYP_CD", referencedColumnName = "RLSHP_GRP_TYP_CD", insertable = false, updatable = false)
    })
    private LogicalPhysicalRelationship logicalPhysicalRelationship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="DTA_SRC_SYS_ID", referencedColumnName = "src_system_id", insertable = false, updatable = false)
    })
    private SourceSystem sourceSystem;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(targetEntity = Attribute.class, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "PHY_ATTR_ID", referencedColumnName = "ATTR_ID", insertable = false, updatable = false),
            @JoinColumn(name = "DTA_SRC_SYS_ID", referencedColumnName = "SRC_SYSTEM_ID", insertable = false, updatable = false)
    })
    private Attribute attribute;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(targetEntity = Attribute.class, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "PHY_ATTR_ID", referencedColumnName = "ATTR_ID", insertable = false, updatable = false)
    })
    private Attribute attributeMapping;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(targetEntity = RelationshipGroup.class, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "PHY_ATTR_ID", referencedColumnName = "RLSHP_GRP_ID", insertable = false, updatable = false)
    })
    private RelationshipGroup relationshipGroup;

    @Transient
    private String sourceSystemName;

    /**
     * Returns the target system attribute priority key
     * @return the target system attribute priority key
     */
    public TargetSystemAttributePriorityKey getKey() {
        return key;
    }

    /**
     * Updates the target system attribute priority key
     * @param key the new target system attribute priority key
     */
    public void setKey(TargetSystemAttributePriorityKey key) {
        this.key = key;
    }

    /**
     * Gets the attribute priority number
     * @return the attribute priority number
     */
    public int getAttributePriorityNumber() {
        return attributePriorityNumber;
    }

    /**
     * Updates the attribute priority number
     * @param attributePriorityNumber attribute priority number
     */
    public void setAttributePriorityNumber(int attributePriorityNumber) {
        this.attributePriorityNumber = attributePriorityNumber;
    }

    /**
     * Returns the logical physical relationship
     * @return the logical physical relationship
     */
    public LogicalPhysicalRelationship getLogicalPhysicalRelationship() {
        return logicalPhysicalRelationship;
    }

    /**
     * Sets a new logical physical relationship
     * @param logicalPhysicalRelationship the new logical physical relationship
     */
    public void setLogicalPhysicalRelationship(LogicalPhysicalRelationship logicalPhysicalRelationship) {
        this.logicalPhysicalRelationship = logicalPhysicalRelationship;
    }

    /**
     * Returns the source system
     * @return the source system
     */
    public SourceSystem getSourceSystem() {
        return sourceSystem;
    }

    /**
     * Replaces the source system
     * @param sourceSystem the new source system
     */
    public void setSourceSystem(SourceSystem sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    /**
     * Returns the Source Attribute associated with the TargetSystemAttributePriority
     * @return he Source Attribute associated with the TargetSystemAttributePriority
     */
    public Attribute getAttribute(){
        return attribute;
    }

    /**
     * Replaces the Source Attribute associated with the TargetSystemAttributePriority
     * @param attribute the new Source Attribute associated with the TargetSystemAttributePriority
     */
    public void setAttribute(Attribute attribute){
        this.attribute = attribute;
    }

    /**
     * Return data source system;
     * @return {{Long}}
     */
    public Long getDataSource(){
        return this.key.getDataSourceSystemId();
    }

    /**
     * @return Gets the value of attributeMapping and returns attributeMapping
     */
    public void setAttributeMapping(Attribute attributeMapping) {
        this.attributeMapping = attributeMapping;
    }

    /**
     * Sets the attributeMapping
     */
    public Attribute getAttributeMapping() {
        return attributeMapping;
    }

    /**
     * @return Gets the value of relationshipGroup and returns relationshipGroup
     */
    public void setRelationshipGroup(RelationshipGroup relationshipGroup) {
        this.relationshipGroup = relationshipGroup;
    }

    /**
     * Sets the relationshipGroup
     */
    public RelationshipGroup getRelationshipGroup() {
        return relationshipGroup;
    }

    /**
     * @return Gets the value of sourceSystemName and returns sourceSystemName
     */
    public void setSourceSystemName(String sourceSystemName) {
        this.sourceSystemName = sourceSystemName;
    }

    /**
     * Sets the sourceSystemName
     */
    public String getSourceSystemName() {
        return sourceSystemName;
    }
    
    /**
     * Returns the attribute priority sort order for the TargetSystemAttributePriority table.
     *
     * @return The attribute priority sort order for the TargetSystemAttributePriority table.
     */
    public static Sort getAttributePrioritySort() {
        return new Sort(Sort.Direction.ASC, "attributePriorityNumber");
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

        TargetSystemAttributePriority that = (TargetSystemAttributePriority) o;

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
        return "TargetSystemAttributePriority{" +
                "key=" + key +
                ", attributePriorityNumber=" + attributePriorityNumber +
                '}';
    }
}
