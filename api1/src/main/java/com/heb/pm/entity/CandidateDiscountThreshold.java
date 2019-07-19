/*
 *  CandidateDiscountThreshold
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the PS_PROD_DISC_THRH database table.
 *
 * @author
 * @since 2.12.0
 */
@Entity
@Table(name = "PS_PROD_DISC_THRH")
public class CandidateDiscountThreshold implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CandidateDiscountThresholdKey key;

    @Column(name = "THRH_DISC_AMT")
    private Double thresholdDiscountAmount;

    @Column(name = "THRH_DISC_TYP_CD")
    private String thresholdDiscountType;

    /**
     * bi-directional many-to-one association to PsProductMaster
     */
    @ManyToOne
    @JoinColumn(name = "PS_PROD_ID", insertable = false, updatable = false)
    private CandidateProductMaster candidateProductMaster;

    /**
     * Returns the key of this record.
     *
     * @return the key of this record.
     */
    public CandidateDiscountThresholdKey getKey() {
        return key;
    }

    /**
     * Sets the key of this record.
     *
     * @param key the key of this record.
     */
    public void setKey(CandidateDiscountThresholdKey key) {
        this.key = key;
    }

    /**
     * Returns the threshold discount amount.
     *
     * @return the threshold discount amount.
     */
    public Double getThresholdDiscountAmount() {
        return this.thresholdDiscountAmount;
    }

    /**
     * Sets the threshold discount amount.
     *
     * @param thresholdDiscountAmount the threshold discount amount.
     */
    public void setThresholdDiscountAmount(Double thresholdDiscountAmount) {
        this.thresholdDiscountAmount = thresholdDiscountAmount;
    }

    /**
     * Returns the threshold discount type code.
     *
     * @return the threshold discount type code.
     */
    public String getThresholdDiscountType() {
        return this.thresholdDiscountType;
    }

    /**
     * Sets the threshold discount type code.
     *
     * @param thresholdDiscountType the threshold discount type code.
     */
    public void setThresholdDiscountType(String thresholdDiscountType) {
        this.thresholdDiscountType = thresholdDiscountType;
    }

    /**
     * Returns the candidate master this record is related to.
     *
     * @return the candidate master this record is related to.
     */
    public CandidateProductMaster getCandidateProductMaster() {
        return this.candidateProductMaster;
    }

    /**
     * Sets the candidate master this record is related to.
     *
     * @param candidateProductMaster the candidate master this record is related to.
     */
    public void setCandidateProductMaster(CandidateProductMaster candidateProductMaster) {
        this.candidateProductMaster = candidateProductMaster;
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
        if (!(o instanceof CandidateDiscountThreshold)) return false;

        CandidateDiscountThreshold that = (CandidateDiscountThreshold) o;

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
        return "CandidateDiscountThreshold{" +
                "key=" + key +
                ", thresholdDiscountAmount=" + thresholdDiscountAmount +
                ", thresholdDiscountType='" + thresholdDiscountType + '\'' +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateProductId() {
        if (this.getKey().getCandidateProductId() == null) {
            this.getKey().setCandidateProductId(this.candidateProductMaster.getCandidateProductId());
        }
    }
}