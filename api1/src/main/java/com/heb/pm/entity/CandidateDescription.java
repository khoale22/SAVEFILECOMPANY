/*
 *  CandidateDescription
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
 * Represents the customer friendly descriptions of candidate.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_prod_desc_txt")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class CandidateDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CandidateDescriptionKey key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PS_PROD_ID", nullable = false, insertable = false, updatable = false)
    private CandidateProductMaster candidateProductMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DES_TYP_CD", nullable = false, insertable = false, updatable = false)
    private DescriptionType descriptionType;

    @Column(name = "PROD_DES", nullable = false)
    private String description;

    @Column(name = "LST_UPDT_USR_ID", nullable = false, length = 8)
    @Type(type = "fixedLengthChar")
    private String lastUpdateUserId;

    @Column(name = "LST_UPDT_TS", nullable = false, length = 0)
    private LocalDateTime lastUpdateDate;

    /**
     * Returns the key for this record.
     *
     * @return the key for this record.
     */
    public CandidateDescriptionKey getKey() {
        return this.key;
    }

    /**
     * Sets the key for this record.
     *
     * @param key the key for this record.
     */
    public void setKey(CandidateDescriptionKey key) {
        this.key = key;
    }

    /**
     * Returns the candidate product master.
     *
     * @return the candidate product master.
     */
    public CandidateProductMaster getCandidateProductMaster() {
        return this.candidateProductMaster;
    }

    /**
     * Sets the candidate product master.
     *
     * @param candidateProductMaster the candidate product master to set.
     */
    public void setCandidateProductMaster(CandidateProductMaster candidateProductMaster) {
        this.candidateProductMaster = candidateProductMaster;
    }

    /**
     * Returns the description type. The description type holds all of the information for the current description type
     * including the length. The code table for description type.
     *
     * @return the description type.
     */
    public DescriptionType getDescriptionType() {
        return this.descriptionType;
    }

    /**
     * Sets the description type.
     *
     * @param descriptionType the description type to set.
     */
    public void setDescriptionType(DescriptionType descriptionType) {
        this.descriptionType = descriptionType;
    }

    /**
     * Returns the description of this candidate.
     *
     * @return the description of this candidate.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of this candidate.
     *
     * @param description the description of this candidate.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the one-pass ID of the last user to update this record.
     *
     * @return the one-pass ID of the last user to update this record.
     */
    public String getLastUpdateUserId() {
        return this.lastUpdateUserId;
    }

    /**
     * Sets the one-pass ID of the last user to update this record.
     *
     * @param lastUpdateUserId the one-pass ID of the last user to update this record.
     */
    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    /**
     * Returns the date and time this record was last updated.
     *
     * @return the date and time this record was last updated.
     */
    public LocalDateTime getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    /**
     * Sets the date and time this record was last updated.
     *
     * @param lastUpdateDate the date and time this record was last updated.
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Compares another object to this one. The key is the only thing used to determine equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateDescription)) return false;

        CandidateDescription that = (CandidateDescription) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateDescription{" +
                "key=" + key +
                ", candidateProductMaster=" + candidateProductMaster +
                ", descriptionType=" + descriptionType +
                ", description='" + description + '\'' +
                ", lastUpdateUserId='" + lastUpdateUserId + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
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
