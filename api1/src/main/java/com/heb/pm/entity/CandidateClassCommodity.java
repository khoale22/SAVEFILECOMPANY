/*
 * CandidateClassCommodity
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a candidate class commodity.
 *
 * @author vn70529
 * @since 2.33.0
 */
@Entity
@Table(name = "PS_PD_CLASS_COMMODITY")
public class CandidateClassCommodity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CandidateClassCommodityKey key;

    @Column(name = "ecomm_bus_mgr_id")
    private String ebmId;

    @Column(name = "bda_uid")
    private String bdaId;

    @Column(name = "CRE8_TS")
    private LocalDateTime createDate;

    @Column(name = "CRE8_ID")
    private String createUserId;

    @Column(name = "LST_UPDT_TS")
    private LocalDateTime lastUpdateDate;

    @Column(name = "LST_UPDT_UID")
    private String lastUpdateUserId;

    @JsonIgnoreProperties("candidateClassCommodities")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ps_work_id", referencedColumnName = "ps_work_id", insertable = false, updatable = false, nullable = false)
    private CandidateWorkRequest candidateWorkRequest;

    /**
     * Returns the key for this object.
     *
     * @return The key for this object.
     */
    public CandidateClassCommodityKey getKey() {
        return key;
    }

    /**
     * Sets the key for this object.
     *
     * @param key The key for this object.
     */
    public void setKey(CandidateClassCommodityKey key) {
        this.key = key;
    }

    /**
     * Returns eBM id.
     *
     * @return The ebmId.
     **/
    public String getEbmId() {
        return ebmId;
    }

    /**
     * Sets the eBM id.
     *
     * @param ebmId The ebmId.
     **/
    public void setEbmId(String ebmId) {
        this.ebmId = ebmId;
    }

    /**
     * Returns BDA id.
     *
     * @return The bdaId.
     **/
    public String getBdaId() {
        return bdaId;
    }

    /**
     * Sets the BDA id.
     *
     * @param bdaId The bdaId.
     **/
    public void setBdaId(String bdaId) {
        this.bdaId = bdaId;
    }

    /**
     * Get the create date.
     *
     * @return create date
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Set the create date.
     *
     * @param createDate the create date.
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Get the user id that created.
     *
     * @return created user id.
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * Set the user id that created.
     *
     * @param createUserId the create user id.
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * Get the last update date.
     *
     * @return the last update date.
     */
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Set the last update date.
     *
     * @param lastUpdateDate the last update date.
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Returns the ID of the user who last updated this record.
     *
     * @return The ID of the user who last updated this record.
     */
    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    /**
     * Sets the one-pass ID of the user who last updated this record.
     *
     * @param lastUpdateUserId The one-pass ID of the user who last updated this record.
     */
    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public CandidateWorkRequest getCandidateWorkRequest() {
        return candidateWorkRequest;
    }

    public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
        this.candidateWorkRequest = candidateWorkRequest;
    }

    /**
     * Called before this object is saved. It will set the ps_prod_id and work request id for the key.
     */
    @PrePersist
    public void populateKey() {
        if (getKey()!= null) {
            if(getKey().getWorkRequestId() == null) {
                this.getKey().setWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
            }
        }
    }

    /**
     * Tests for equality with another object. Equality is based on the key.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateClassCommodity)) return false;
        CandidateClassCommodity that = (CandidateClassCommodity) o;
        return !(key != null ? !key.equals(that.key) : that.key != null);
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "CandidateClassCommodity{" +
                "key=" + key +
                ", eBMid='" + ebmId + '\'' +
                ", bDAid='" + bdaId + '\'' +
                '}';
    }

    /**
     * Returns a hash code for this object. Equal objects always return the same value. Unequals objects (probably)
     * return different values.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
