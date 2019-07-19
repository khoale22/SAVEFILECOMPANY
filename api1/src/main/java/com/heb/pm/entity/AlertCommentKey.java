/*
 * AlertCommentKey
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package  com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the composite key of the Alert Comment.
 *
 * @author vn40486
 * @since 2.16.0
 */
@Embeddable
public class AlertCommentKey implements Serializable{
    private static final long serialVersionUID = 1L;

    @Column(name = "alrt_id")
    private Integer alertID;

    @Column(name = "seq_nbr")
    private Integer sequenceNumber;

    public AlertCommentKey() {}

    public AlertCommentKey(Integer alertID, Integer sequenceNumber) {
        this.alertID = alertID;
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getAlertID() {
        return alertID;
    }

    public void setAlertID(Integer alertID) {
        this.alertID = alertID;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlertCommentKey that = (AlertCommentKey) o;

        if (!alertID.equals(that.alertID)) return false;
        return sequenceNumber.equals(that.sequenceNumber);
    }

    @Override
    public int hashCode() {
        int result = alertID.hashCode();
        result = 31 * result + sequenceNumber.hashCode();
        return result;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "AlertCommentKey{" +
                "alertID=" + alertID +
                ", sequenceNumber=" + sequenceNumber +
                '}';
    }
}
