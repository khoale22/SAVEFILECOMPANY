/*
 *  CandidateDiscountThresholdKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The primary key class for the PS_PROD_DISC_THRH database table.
 *
 * @author
 * @since 2.12.0
 */
@Embeddable
public class CandidateDiscountThresholdKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "PS_PROD_ID", insertable = false, updatable = false)
    private Long candidateProductId;

    @Column(name = "EFF_TS")
    private LocalDate effectiveDate;

    @Column(name = "MIN_DISC_THRH_QTY")
    private Long minDiscountThresholdQuantity;

    /**
     * Returns the product id of this record.
     *
     * @return the product id of this record.
     */
    public Long getCandidateProductId() {
        return this.candidateProductId;
    }

    /**
     * Sets the product id of this record.
     *
     * @param candidateProductId the product id of this record.
     */
    public void setCandidateProductId(Long candidateProductId) {
        this.candidateProductId = candidateProductId;
    }

    /**
     * Returns the effective date.
     *
     * @return the effective date.
     */
    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    /**
     * Sets the effective date.
     *
     * @param effectiveDate the effective date.
     */
    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * Returns the min discount threshold quantity.
     *
     * @return the min discount threshold quantity.
     */
    public Long getMinDiscountThresholdQuantity() {
        return this.minDiscountThresholdQuantity;
    }

    /**
     * Sets the min discount threshold quantity.
     *
     * @param minDiscountThresholdQuantity the min discount threshold quantity.
     */
    public void setMinDiscountThresholdQuantity(Long minDiscountThresholdQuantity) {
        this.minDiscountThresholdQuantity = minDiscountThresholdQuantity;
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
        if (!(o instanceof CandidateDiscountThresholdKey)) return false;

        CandidateDiscountThresholdKey that = (CandidateDiscountThresholdKey) o;

        if (candidateProductId != null ? !candidateProductId.equals(that.candidateProductId) : that.candidateProductId != null) return false;
        if (effectiveDate != null ? !effectiveDate.equals(that.effectiveDate) : that.effectiveDate != null)
            return false;
        return minDiscountThresholdQuantity != null ? minDiscountThresholdQuantity.equals(that.minDiscountThresholdQuantity) : that.minDiscountThresholdQuantity == null;
    }

    /**
     * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
     * different hash codes.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = candidateProductId != null ? candidateProductId.hashCode() : 0;
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (minDiscountThresholdQuantity != null ? minDiscountThresholdQuantity.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateDiscountThresholdKey{" +
                "candidateProductId=" + candidateProductId +
                ", effectiveDate=" + effectiveDate +
                ", minDiscountThresholdQuantity=" + minDiscountThresholdQuantity +
                '}';
    }
}