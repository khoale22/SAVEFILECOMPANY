/*
 * AlertRecipientKey
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
/**
 * Represents the composite key of the Alert recipient.
 *
 * @author vn40486
 * @since 2.15.0
 */
@Embeddable
//DB2Oracle changes vn00710 starts
@TypeDefs({
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class AlertRecipientKey implements Serializable{
    private static final long serialVersionUID = 1L;

    @Column(name = "alrt_id")
    private Integer alertID;

  	@Type(type="fixedLengthCharPK")  
    @Column(name = "recipient_id")
    private String recipientId;

  	@Type(type="fixedLengthCharPK")  
    @Column(name = "alrt_recp_typ_cd")
    private String alertRecipientTypeCode;

    public Integer getAlertID() {
        return alertID;
    }

    public void setAlertID(Integer alertID) {
        this.alertID = alertID;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getAlertRecipientTypeCode() {
        return alertRecipientTypeCode;
    }

    public void setAlertRecipientTypeCode(String alertRecipientTypeCode) {
        this.alertRecipientTypeCode = alertRecipientTypeCode;
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

        AlertRecipientKey that = (AlertRecipientKey) o;

        if (!alertID.equals(that.alertID)) return false;
        if (!recipientId.equals(that.recipientId)) return false;
        return alertRecipientTypeCode.equals(that.alertRecipientTypeCode);
    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this obejct.
     */
    @Override
    public int hashCode() {
        int result = alertID.hashCode();
        result = 31 * result + recipientId.hashCode();
        result = 31 * result + alertRecipientTypeCode.hashCode();
        return result;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "AlertRecipientKey{" +
                "alertID=" + alertID +
                ", recipientId='" + recipientId + '\'' +
                ", alertRecipientTypeCode='" + alertRecipientTypeCode + '\'' +
                '}';
    }
}
