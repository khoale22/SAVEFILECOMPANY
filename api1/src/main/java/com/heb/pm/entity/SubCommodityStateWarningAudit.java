/*
 * SubCommodityStateWarningAudit
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
 * Represents a warning audit for a particular state attached to any product under a product hierarchy -> sub commodity.
 *
 * @author vn70529
 * @since 2.18.4
 */
@Entity
@Table(name = "st_warn_dflt_scom_aud")
public class SubCommodityStateWarningAudit implements Serializable, Audit {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private SubCommodityStateWarningAuditKey key;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @AuditableField(displayName = "State Warnings", codeTableDisplayNameMethod = "getProductStateWarningDisplayName", filter = FilterConstants.SUB_COMMODITY_DEFAULTS_AUDIT)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "st_cd", referencedColumnName = "st_cd", insertable = false, updatable = false),
            @JoinColumn(name = "st_prod_warn_cd", referencedColumnName = "st_prod_warn_cd", insertable = false, updatable = false)
    })
    private ProductStateWarning productStateWarning;

    /**
     * Returns the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
     *
     * @return the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
     **/
    public SubCommodityStateWarningAuditKey getKey() {
        return key;
    }

    /**
     * Sets the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
     *
     * @param key the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
     **/
    public void setKey(SubCommodityStateWarningAuditKey key) {
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
     * Returns the product state warning matching this.key.stateCode, and this.key.stateProductWarningCode.
     *
     * @return The ProductStateWarning that is required by law to inform of issues the ingredients in the product may cause.
     **/
    public ProductStateWarning getProductStateWarning() {
        return productStateWarning;
    }

    /**
     * Sets the product state warning for this sub-commodity state warning.
     *
     * @param productStateWarning The ProductStateWarning that is required by law to inform of issues the ingredients in the product may cause.
     **/
    public void setProductStateWarning(ProductStateWarning productStateWarning) {
        this.productStateWarning = productStateWarning;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "SubCommodityStateWarningAudit{" +
                "key=" + key +
                ", action='" + action +
                ", changedBy='" + changedBy +
                '}';
    }

    /**
     * Get Product State Warning display name in audit table on the GUI.
     *
     * @return A string is the abbreviation of product state warning to display on the GUI.
     */
    public String getProductStateWarningDisplayName(){
        ProductStateWarning productStateWarning = this.getProductStateWarning();
        if(productStateWarning != null){
            return productStateWarning.getAbbreviation();
        } else {
            return String.format(Audit.DISPLAY_NAME_FORMAT_FOR_CODE_TABLE_NOT_FOUND, this.getKey().getStateProductWarningCode().trim());
        }
    }
}
