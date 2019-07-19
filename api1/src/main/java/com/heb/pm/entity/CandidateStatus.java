/*
 *  CandidateStatus
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a ps candidate stat.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_candidate_stat")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class CandidateStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * PD_SETUP_STAT_CD_BATCH_UPLOAD.
     */
    public static final String PD_SETUP_STAT_CD_BATCH_UPLOAD = "111";
    /**
     * PD_SETUP_STAT_CD_FAILURE.
     */
    public static final String PD_SETUP_STAT_CD_FAILURE = "109";
    public static final long STAT_CHG_RSN_ID_WRKG = 3;
    public static final long STAT_CNG_RSN_ID_REJECTED = 2;
    public static final long STAT_CNG_RSN_ID_ACTIVATED = 5;
    public static final String REQUEST_STATUS_WORKING ="107";
    public static final String REQUEST_STATUS_REJECTED ="105";
    public static final String REQUEST_STATUS_APPROVED ="108";
    /**
     * The ps candidate stat key.
     */
    @EmbeddedId
    private CandidateStatusKey key;

    /**
     * The ps work rqst.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PS_WORK_ID", nullable = false, insertable = false, updatable = false)
    private CandidateWorkRequest candidateWorkRequest;

    @Column(name = "STAT_CHG_RSN_ID")
    private Long statusChangeReason;

    @Column(name = "UPDT_USR_ID", nullable = false, length = 8)
    @Type(type = "fixedLengthChar")
    private String updateUserId;

    @Column(name = "CMT_TXT", length = 100)
    private String commentText;

    public enum CandidateStatusChangeReason {
        SUBMITTED(1),
        REJECTED(2),
        WORKING(3),
        DELETED(4),
        ACTIVATED(5);
        private long name;
        CandidateStatusChangeReason(long name) {
            this.name = name;
        }
        /**
         * Gets name.
         *
         * @return the name
         */
        public long getName() {
            return this.name;
        }
    }

    /**
     * Returns the key for this object.
     *
     * @return the key for this object.
     */
    public CandidateStatusKey getKey() {
        return this.key;
    }

    /**
     * Sets the key for this object.
     *
     * @param key the key for this object.
     */
    public void setKey(CandidateStatusKey key) {
        this.key = key;
    }

    /**
     * Gets the ps work rqst.
     *
     * @return the ps work rqst.
     */
    public CandidateWorkRequest getPsWorkRqst() {
        return this.candidateWorkRequest;
    }

    /**
     * Sets the work request this record is associated to.
     *
     * @param candidateWorkRequest the work request this record is associated to.
     */
    public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
        this.candidateWorkRequest = candidateWorkRequest;
    }

    /**
     * Returns the reason the status of this record was last changed.
     *
     * @return the reason the status of this record was last changed.
     */
    public Long getStatusChangeReason() {
        return this.statusChangeReason;
    }

    /**
     * Sets the reason the status of this record was last changed.
     *
     * @param statusChangeReason the reason the status of this record was last changed.
     */
    public void setStatusChangeReason(Long statusChangeReason) {
        this.statusChangeReason = statusChangeReason;
    }

    /**
     * Returns the one-pass ID of the last user to update this record.
     *
     * @return the one-pass ID of the last user to update this record.
     */
    public String getUpdateUserId() {
        return this.updateUserId;
    }

    /**
     * Sets the one-pass ID of the last user to update this record.
     *
     * @param updateUserId the one-pass ID of the last user to update this record.
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * Returns the comment text of this record.
     *
     * @return the comment text of this record.
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * Sets the comment text of this record.
     *
     * @param commentText the comment text of this record.
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    /**
     * Compares this object with another for equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateStatus)) return false;

        CandidateStatus that = (CandidateStatus) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    /**
     * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
     * different hash codes.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateStatus{" +
                "key=" + key +
                ", candidateWorkRequest=" + candidateWorkRequest +
                ", statusChangeReason=" + statusChangeReason +
                ", updateUserId='" + updateUserId + '\'' +
                ", commentText='" + commentText + '\'' +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setWorkRequestId() {
        if (this.getKey().getWorkRequestId() == null) {
            this.getKey().setWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
        }
    }
}
