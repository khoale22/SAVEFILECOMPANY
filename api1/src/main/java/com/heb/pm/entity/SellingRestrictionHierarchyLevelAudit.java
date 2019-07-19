/*
 * SellingRestrictionHierarchyLevelAudit
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a record in the sals_rstr_grp_dflt_aud table.
 *
 * @author vn70529
 * @since 2.18.4
 */
@Entity
@Table(name="sals_rstr_grp_dflt_aud")
public class SellingRestrictionHierarchyLevelAudit implements Serializable, Audit {

    private static final long serialVersionUID = 1L;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @EmbeddedId
    private SellingRestrictionHierarchyLevelAuditKey key;

    @AuditableField(displayName = "Selling Restrictions", codeTableDisplayNameMethod = "getSellingRestrictionDisplayName", filter = FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT)
    @ManyToOne
    @JoinColumn(name = "sals_rstr_grp_cd", referencedColumnName = "sals_rstr_grp_cd", insertable = false, updatable = false, nullable = false)
    private SellingRestriction sellingRestriction;

    /**
     * Returns Key.
     *
     * @return The Key.
     **/
    public SellingRestrictionHierarchyLevelAuditKey getKey() {
        return key;
    }

    /**
     * Sets the Key.
     *
     * @param key The Key.
     **/
    public void setKey(SellingRestrictionHierarchyLevelAuditKey key) {
        this.key = key;
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
     * Returns SellingRestriction.
     *
     * @return The SellingRestriction.
     **/
    public SellingRestriction getSellingRestriction() {
        return sellingRestriction;
    }

    /**
     * Sets the SellingRestriction.
     *
     * @param sellingRestriction The SellingRestriction.
     **/
    public void setSellingRestriction(SellingRestriction sellingRestriction) {
        this.sellingRestriction = sellingRestriction;
    }

    /**
     * Get Selling Restriction display name in audit table on the GUI.
     *
     * @return A string is the description of Selling Restriction to display on the GUI.
     */
    public String getSellingRestrictionDisplayName(){
        SellingRestriction sellingRestriction = this.getSellingRestriction();
        if(sellingRestriction != null){
            return sellingRestriction.getRestrictionDescription();
        } else {
            return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getKey().getRestrictionGroupCode().trim());
        }
    }
}
