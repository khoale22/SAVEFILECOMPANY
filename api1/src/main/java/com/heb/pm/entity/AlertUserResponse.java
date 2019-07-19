/*
 * AlertUserResponse
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the ALERT_USER_RESP table.
 *
 * @author vn40486
 * @since 2.16.0
 */
@Entity
@Table(name="alert_user_resp")
public class AlertUserResponse implements Serializable{
    private static final long serialVersionUID = 1L;

    public static final String RESOLUTION_CODE_DEFAULT = "";
    public static final String RESOLUTION_CODE_CLOSE = "CLOSE";
    public static final String ALERT_READ_SW_NO = "N";
    public static final String ALERT_HIDE_SW_NO = "N";
    public static final String ROLE_CD_DEFAULT = "";

    @EmbeddedId
    private AlertUserResponseKey key;

    @Column(name = "alrt_resl_cd")
    private String alertResolutionCode;

    public AlertUserResponseKey getKey() {
        return key;
    }

    public void setKey(AlertUserResponseKey key) {
        this.key = key;
    }

    public String getAlertResolutionCode() {
        return alertResolutionCode;
    }

    public void setAlertResolutionCode(String alertResolutionCode) {
        this.alertResolutionCode = alertResolutionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlertUserResponse that = (AlertUserResponse) o;

        if (!key.equals(that.key)) return false;
        return alertResolutionCode.equals(that.alertResolutionCode);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + alertResolutionCode.hashCode();
        return result;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "AlertUserResponse{" +
                "key=" + key +
                ", alertResolutionCode='" + alertResolutionCode + '\'' +
                '}';
    }
}
