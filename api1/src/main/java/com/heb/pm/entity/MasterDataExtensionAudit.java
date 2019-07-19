/*
 * MasterDataExtensionAudit
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
import java.util.Objects;

/**
 * Represents master data extension audit data.
 * @author vn70529
 * @since 2.15.0
 */
@Entity
@Table(name = "mst_dta_extn_aud")
public class MasterDataExtensionAudit implements Serializable, Audit {

    @EmbeddedId
    private MasterDataExtensionAuditKey key;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @AuditableField(displayName = "MASTER DATA EXTENSION EXTENDED", filter = FilterConstants.MASTER_DATA_EXTENSION_EXTENDED_AUDIT)
    @Column(name = "attr_val_txt")
    private String attributeValueText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attr_id", referencedColumnName = "attr_id", insertable = false, updatable = false)
    private Attribute attribute;

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
     * Gets key.
     *
     * @return the key
     */
    public MasterDataExtensionAuditKey getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(MasterDataExtensionAuditKey key) {
        this.key = key;
    }

    /**
     * Returns the AttributeValueText
     *
     * @return AttributeValueText
     */
    public String getAttributeValueText() {
        return attributeValueText;
    }

    /**
     * Sets the AttributeValueText. The attribute value text is what the attribute is called.
     *
     * @param attributeValueText The AttributeValueText
     */
    public void setAttributeValueText(String attributeValueText) {
        this.attributeValueText = attributeValueText;
    }

    /**
     * Returns the attribute.
     * @return the attribute.
     */
    public Attribute getAttribute() {
        return this.attribute;
    }

    /**
     * Sets the attribute.
     *
     * @param attribute the attribute.
     */
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Method to call to retrieve the display name for Master Data Extension Audit.
     *
     * @return  MasterDataExtensionAudit display name.
     */
    public String getMasterDataExtensionAuditDisplayName() {
        return this.attribute.getAttributeName();
    }

    /**
     * Compares another object to this one. If that object is an MasterDataExtensionAudit, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterDataExtensionAudit that = (MasterDataExtensionAudit) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(action, that.action) &&
                Objects.equals(changedBy, that.changedBy) &&
                Objects.equals(attributeValueText, that.attributeValueText) &&
                Objects.equals(attribute, that.attribute) ;
    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(key, action, changedBy, attributeValueText, attribute);
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "MasterDataExtensionAudit{" +
                "key=" + key +
                ", action='" + action +
                ", changedBy='" + changedBy +
                ", attributeValueText='" + attributeValueText +
                ", attribute='" + attribute +
                '}';
    }
}
