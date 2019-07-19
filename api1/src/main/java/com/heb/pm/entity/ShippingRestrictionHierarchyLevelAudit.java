/*
 * ShippingRestrictionHierarchyLevelAudit
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
 * Represents a record in the sals_rstr_prod_dfl_aud table.
 *
 * @author vn70529
 * @since 2.18.4
 */
@Entity
@Table(name="sals_rstr_prod_dfl_aud")
public class ShippingRestrictionHierarchyLevelAudit implements Serializable, Audit {

    private static final long serialVersionUID = 1L;
    private static final String YES_SW = "Y";
    private static final String NO_SW = "N";

    @EmbeddedId
    private ShippingRestrictionHierarchyLevelAuditKey key;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @AuditableField(displayName = "Shipping Restrictions", codeTableDisplayNameMethod = "getShippingRestrictionSwitchDisplayName", filter = FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT)
    @Column(name = "excsn_sw")
    private Boolean applicableAtThisLevel;

    @ManyToOne
    @JoinColumn(name = "sals_rstr_cd", referencedColumnName = "sals_rstr_cd", insertable = false, updatable = false, nullable = false)
    private SellingRestrictionCode sellingRestrictionCode;

    /**
     * Returns Key.
     *
     * @return The Key.
     **/
    public ShippingRestrictionHierarchyLevelAuditKey getKey() {
        return key;
    }

    /**
     * Sets the Key.
     *
     * @param key The Key.
     **/
    public void setKey(ShippingRestrictionHierarchyLevelAuditKey key) {
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
     * Returns SellingRestrictionCode.
     *
     * @return The SellingRestrictionCode.
     **/
    public SellingRestrictionCode getSellingRestrictionCode() {
        return sellingRestrictionCode;
    }

    /**
     * Sets the SellingRestrictionCode.
     *
     * @param sellingRestrictionCode The SellingRestrictionCode.
     **/
    public void setSellingRestrictionCode(SellingRestrictionCode sellingRestrictionCode) {
        this.sellingRestrictionCode = sellingRestrictionCode;
    }

    /**
     * Returns ApplicableAtThisLevel.
     *
     * @return The ApplicableAtThisLevel.
     **/
    public Boolean getApplicableAtThisLevel() {
        return applicableAtThisLevel;
    }

    /**
     * Sets the ApplicableAtThisLevel.
     *
     * @param applicableAtThisLevel The ApplicableAtThisLevel.
     **/
    public void setApplicableAtThisLevel(Boolean applicableAtThisLevel) {
        this.applicableAtThisLevel = applicableAtThisLevel;
    }

    /**
     * Get Shipping Restriction switch display name in audit table on the GUI.
     *
     * @return A string to display on the GUI.
     */
    public String getShippingRestrictionSwitchDisplayName(){
        SellingRestrictionCode sellingRestrictionCode = this.getSellingRestrictionCode();
        if(sellingRestrictionCode != null){
            return this.getApplicableAtThisLevel()?YES_SW:NO_SW;
        } else {
            return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getKey().getRestrictionCode().trim());
        }
    }
}
