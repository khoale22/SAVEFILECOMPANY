/*
 *  CandidateCommentsKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key for CandidateComments.
 *
 * @author vn40486
 * @since 2.15.0
 */
@Embeddable
public class CandidateCommentsKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "PS_WORK_ID", nullable = false)
    private Long workRequestId;

    @Column(name = "SEQ_NBR", nullable = false)
    private Integer sequenceNumber;

    public Long getWorkRequestId() {
        return workRequestId;
    }

    public void setWorkRequestId(Long workRequestId) {
        this.workRequestId = workRequestId;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Compares another object to this one. This is a deep compare.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CandidateCommentsKey that = (CandidateCommentsKey) o;

        if (!workRequestId.equals(that.workRequestId)) return false;
        return sequenceNumber.equals(that.sequenceNumber);
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = workRequestId.hashCode();
        result = 31 * result + sequenceNumber.hashCode();
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateCommentsKey{" +
                "workRequestId=" + workRequestId +
                ", sequenceNumber=" + sequenceNumber +
                '}';
    }
}
