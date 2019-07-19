/*
 * NutrientAudit
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;
import com.heb.util.audit.Audit;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents Nutrient Audit Data.
 * @author vn70633
 * @since 2.15.0
 */
@Entity
@Table(name = "nutrient_aud")
public class NutrientAudit implements Audit, Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private NutrientAuditKey key;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @Column(name = "LST_UPDT_TS",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
    private LocalDateTime lastUpdateTs;

    /**
     * Getter for lastUpdateTs attribute
     * @return
     */
    public LocalDateTime getLastUpdateTs() {
        return lastUpdateTs;
    }

    /**
     * Setter for lastUpdateTs attribute
     * @param lastUpdateTs - new attribute to set
     */
    public void setLastUpdateTs(LocalDateTime lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    /**
     * Returns the key for the dynamic attribtue.
     *
     * @return The key for the dynamic attribtue.
     */
    public NutrientAuditKey getKey() {
        return key;
    }

    /**
     * Sets the key for the dynamic attribtue.
     *
     * @param key The key for the dynamic attribtue.
     */
    public void setKey(NutrientAuditKey key) {
        this.key = key;
    }

    /**
     * Returns a hash code for the object. Equal objects return the same hash code. Unequal objects (probably) return
     * different hash codes.
     *
     * @return A hash code for the object.
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "NutrientAudit{" +
                "key=" + key +
                ", changed by='" + changedBy + '\'' +
                '}';
    }

    /**
     * 	Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
     * 	@return ActionCode
     */
    public String getAction() {
        return action;
    }

    /**
     * Updates the action code
     * @param action the new action
     */
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String getChangedBy() {
        return changedBy;
    }

    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }
}
