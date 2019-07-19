/*
 *  CandidateItemScanCodeKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key for CandidateItemScanCode.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Embeddable
public class CandidateItemScanCodeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "SCN_CD_ID", nullable = false, precision = 17)
    private Long upc;

    @Column(name = "PS_ITM_ID", nullable = false)
    private Integer candidateItemId;

    /**
     * Returns the UPC this record is for.
     *
     * @return the UPC this record is for.
     */
    public Long getUpc() {
        return this.upc;
    }

    /**
     * Sets the UPC this record is for.
     *
     * @param upc the UPC this record is for.
     */
    public void setUpc(Long upc) {
        this.upc = upc;
    }

    /**
     * Returns the candidate item code.
     *
     * @return the candidate item code.
     */
    public Integer getCandidateItemId() {
        return this.candidateItemId;
    }

    /**
     * Sets the candidate item code.
     *
     * @param candidateItemId the candidate item code.
     */
    public void setCandidateItemId(Integer candidateItemId) {
        this.candidateItemId = candidateItemId;
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
        if (!(o instanceof CandidateItemScanCodeKey)) return false;

        CandidateItemScanCodeKey that = (CandidateItemScanCodeKey) o;

        if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
        return candidateItemId != null ? candidateItemId.equals(that.candidateItemId) : that.candidateItemId == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = upc != null ? upc.hashCode() : 0;
        result = 31 * result + (candidateItemId != null ? candidateItemId.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateItemScanCodeKey{" +
                "upc=" + upc +
                ", candidateItemId=" + candidateItemId +
                '}';
    }
}
