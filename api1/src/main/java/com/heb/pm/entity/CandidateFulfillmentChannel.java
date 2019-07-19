/*
 *  CandidateFulfillmentChannel
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * The persistent class for the PS_PROD_FLFL_CHNL database table.
 *
 * @author
 * @since 2.12.0
 */
@Entity
@Table(name = "PS_PROD_FLFL_CHNL")
public class CandidateFulfillmentChannel implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String YES_STATUS = "Y";
	public static final String NO_STATUS = "N";
    public static final String DELETED_STATUS = "D";

    @EmbeddedId
    private CandidateFulfillmentChannelKey key;

    @Column(name = "EFF_DT")
    private LocalDate effectiveDate;

    @Column(name = "EXPRN_DT")
    private LocalDate expirationDate;

    @Column(name = "NEW_DATA_SW")
    private String newData;
    /**
     * bi-directional many-to-one association to PsProductMaster
     */
    @ManyToOne
    @JoinColumn(name = "PS_PROD_ID", insertable = false, updatable = false)
    private CandidateProductMaster candidateProductMaster;
    /**
     * The ps work rqst.
     */
    @JsonIgnoreProperties({"candidateMasterDataExtensionAttributes", "candidateProductPkVariations", "candidateNutrients", "candidateNutrientPanelHeaders", "candidateGenericEntityRelationship", "candidateFulfillmentChannels"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PS_WORK_ID", referencedColumnName = "PS_WORK_ID", insertable = false, updatable = false, nullable = false)
    private CandidateWorkRequest candidateWorkRequest;
    /**
     * Returns the key for this object.
     *
     * @return the key for this object.
     */
    public CandidateFulfillmentChannelKey getKey() {
        return key;
    }

    /**
     * Sets the key for this object.
     *
     * @param key the key for this object.
     */
    public void setKey(CandidateFulfillmentChannelKey key) {
        this.key = key;
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
     * Returns the expiration date.
     *
     * @return the expiration date.
     */
    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    /**
     * Sets the expiration date.
     *
     * @param expirationDate the expiration date.
     */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Returns the NewData
     *
     * @return NewData
     */
    public String getNewData() {
        return newData;
    }

    /**
     * Sets the NewData
     *
     * @param newData The NewData
     */
    public void setNewData(String newData) {
        this.newData = newData;
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
     * Returns the candidateWorkRequest this record is related to.
     *
     * @return the candidateWorkRequest this record is related to.
     */

    public CandidateWorkRequest getCandidateWorkRequest() {
        return candidateWorkRequest;
    }
    /**
     * Sets the candidateWorkRequest this record is related to.
     *
     * @param candidateWorkRequest the candidateWorkRequest this record is related to.
     */
    public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
        this.candidateWorkRequest = candidateWorkRequest;
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
        if (!(o instanceof CandidateFulfillmentChannel)) return false;

        CandidateFulfillmentChannel that = (CandidateFulfillmentChannel) o;

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
        return "CandidateFulfillmentChannel{" +
                "key=" + key +
                ", effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", newData='" + newData + '\'' +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidatId() {
        if (this.getKey().getCandidateProductId() == null && this.candidateProductMaster!=null) {
            this.getKey().setCandidateProductId(this.candidateProductMaster.getCandidateProductId());
        }
        if (this.getKey().getWorkRequestId() == null && candidateWorkRequest!=null) {
            this.getKey().setWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
        }
    }
}