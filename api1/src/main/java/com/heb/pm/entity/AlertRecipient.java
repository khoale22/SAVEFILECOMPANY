/*
 * AlertRecipient
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.ProductMaster;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Represents the ALERT_RECIPIENT.
 *
 * @author vn40486
 * @since 2.15.0
 */
@Entity
@Table(name="alert_recipient")
public class AlertRecipient implements Serializable{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AlertRecipientKey key;

    @Column(name = "info_alrt_sw")
    private String infoAlert;

    @Column(name = "info_only_sw")
    private String infoOnly;

    public AlertRecipientKey getKey() {
        return key;
    }

    public void setKey(AlertRecipientKey key) {
        this.key = key;
    }

    public String getInfoAlert() {
        return infoAlert;
    }

    public void setInfoAlert(String infoAlert) {
        this.infoAlert = infoAlert;
    }

    public String getInfoOnly() {
        return infoOnly;
    }

    public void setInfoOnly(String infoOnly) {
        this.infoOnly = infoOnly;
    }

    /**
     * Compares another object to this one. If that object is a Location, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlertRecipient that = (AlertRecipient) o;

        return key.equals(that.key);
    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this obejct.
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "AlertRecipient{" +
                "key=" + key +
                ", infoAlert='" + infoAlert + '\'' +
                ", infoOnly='" + infoOnly + '\'' +
                '}';
    }
}
