package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents generic entity relationship audit Data.
 * @author vn70529
 * @since 2.15.0
 */
@Entity
@Table(name = "enty_rlshp_aud")
public class GenericEntityRelationshipAudit implements Serializable,Audit {
    private static final long serialVersionUID = 1L;
    private static final String SORT_DEFAULT_FIELD = "key.changedOn";

    @EmbeddedId
    private GenericEntityRelationshipAuditKey key;

    @Column(name="dflt_parnt_sw")
    private Boolean defaultParent;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false, nullable = false)
    private GenericEntity genericChildEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "parnt_enty_id", referencedColumnName = "enty_id", insertable = false, updatable = false, nullable = false),
            @JoinColumn(name = "hier_cntxt_cd", referencedColumnName = "hier_cntxt_cd", insertable = false, updatable = false, nullable = false)
    })
    @NotFound(action = NotFoundAction.IGNORE)
    private GenericEntityDescription parentDescription;

    /**
     * Returns the Key
     *
     * @return Key
     */
    public GenericEntityRelationshipAuditKey getKey() {
        return key;
    }

    /**
     * Sets the Key
     *
     * @param key The Key
     */
    public void setKey(GenericEntityRelationshipAuditKey key) {
        this.key = key;
    }

    /**
     * Gets default parent. This is whether or not this generic entity relationship is a default parent.
     *
     * @return the default parent
     */
    public Boolean getDefaultParent() {
        return defaultParent;
    }

    /**
     * Sets default parent.
     *
     * @param defaultParent the default parent
     */
    public void setDefaultParent(Boolean defaultParent) {
        this.defaultParent = defaultParent;
    }

    /**
     * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
     * 	@return ActionCode
     */
    @Override
    public String getAction() {
        return action;
    }

    /**
     * Updates the action code
     * @param action the new action
     */
    @Override
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Returns changed by. The changed by shows who was doing the action that is being audited. This is the uid(login) that a
     * user has.
     *
     * @return the changed by
     */
    @Override
    public String getChangedBy() {
        return changedBy;
    }

    /**
     * Sets the changed by uid received from the database.
     *
     * @param changedBy the changed by
     */
    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    /**
     * Gets the changed on time. This is when the modification was done.
     *
     * @return The time the modification was done.
     */
    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    /**
     * Sets the changed on time.
     *
     * @param changedOn The time the modification was done.
     */
    @Override
    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }

    /**
     * Gets generic child entity. This is the generic entity whose id matches this child id.
     *
     * @return the generic child entity
     */
    public GenericEntity getGenericChildEntity() {
        return genericChildEntity;
    }

    /**
     * Sets generic child entity.
     *
     * @param genericChildEntity the generic child entity
     */
    public void setGenericChildEntity(GenericEntity genericChildEntity) {
        this.genericChildEntity = genericChildEntity;
    }

    /**
     * get parent description
     * @return GenericEntityDescription
     */
    public GenericEntityDescription getParentDescription() {
        return parentDescription;
    }
    /**
     * set parent parent Description
     * @param parentDescription the description
     */
    public void setParentDescription(GenericEntityDescription parentDescription) {
        this.parentDescription = parentDescription;
    }

    /**
     * Returns the default sort order for the Vocabulary table.
     *
     * @return The default sort order for the Vocabulary table.
     */
    public static Sort getDefaultSort() {
        return  new Sort(
                new Sort.Order(Sort.Direction.ASC, GenericEntityRelationshipAudit.SORT_DEFAULT_FIELD)
        );
    }
}
