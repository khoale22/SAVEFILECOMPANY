package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the key for a generic entity relationship audit.
 *
 * @author vn70529
 * @since 2.15.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class GenericEntityRelationshipAuditKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="parnt_enty_id")
    private Long parentEntityId;

    @Column(name="child_enty_id")
    private Long childEntityId;

    @Column(name="hier_cntxt_cd")
    @Type(type="fixedLengthCharPK")
    private String hierarchyContext;

    @Column(name = "AUD_REC_CRE8_TS")
    private LocalDateTime changedOn;

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
     * Returns the changed on. The changed on is the timestamp in which the transaction occurred.
     *
     * @return Timestamp
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Sets the Timestamp
     *
     * @param timestamp The Timestamp
     */
    public void setChangedOn(LocalDateTime timestamp) {
        this.changedOn = changedOn;
    }

}
