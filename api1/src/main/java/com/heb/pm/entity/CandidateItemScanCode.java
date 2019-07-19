/*
 *  CandidateItemScanCode
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the item scan code of candidate.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_itm_scn_cd")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class CandidateItemScanCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CandidateItemScanCodeKey key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PS_ITM_ID", nullable = false, insertable = false, updatable = false)
    private CandidateItemMaster candidateItemMaster;

    @Column(name = "SCN_TYP_CD", nullable = false, length = 5)
    @Type(type = "fixedLengthChar")
    private String scanType;

    @Column(name = "RETL_PACK_QTY")
    private Integer retailPackQuantity;

    @Column(name = "NEW_DATA_SW", nullable = false, length = 1)
    private Boolean newData;

    @Column(name = "PRIM_SW", nullable = false)
    private Boolean primarySwitch;

    @Transient
    @JsonIgnoreProperties
    private boolean linked;
    /**
     * Returns the key for this record.
     *
     * @return the key for this record.
     */
    public CandidateItemScanCodeKey getKey() {
        return this.key;
    }

    /**
     * Sets the key for this record.
     *
     * @param key the key for this record to set.
     */
    public void setKey(CandidateItemScanCodeKey key) {
        this.key = key;
    }

    /**
     * Returns the candidate item master.
     *
     * @return the candidate item master.
     */
    public CandidateItemMaster getCandidateItemMaster() {
        return this.candidateItemMaster;
    }

    /**
     * Sets the candidate item master.
     *
     * @param candidateItemMaster the candidate item master to set.
     */
    public void setCandidateItemMaster(CandidateItemMaster candidateItemMaster) {
        this.candidateItemMaster = candidateItemMaster;
    }

    /**
     * Returns the scan type code.
     *
     * @return the scan type code.
     */
    public String getScanType() {
        return this.scanType;
    }

    /**
     * Sets the scan type code.
     *
     * @param scanType the scan type code to set.
     */
    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    /**
     * Returns the retail pack quantity.
     *
     * @return the retail pack quantity.
     */
    public Integer getRetailPackQuantity() {
        return this.retailPackQuantity;
    }

    /**
     * Sets the retail pack quantity.
     *
     * @param retailPackQuantity the retail pack quantity to set.
     */
    public void setRetailPackQuantity(Integer retailPackQuantity) {
        this.retailPackQuantity = retailPackQuantity;
    }

    /**
     * Returns the newData to take on this record.
     *
     * @return the newData to take on this record.
     */
    public Boolean isNewData() {
        return this.newData;
    }

    /**
     * Sets the newData to take on this record.
     *
     * @param newData the newData to take on this record to set.
     */
    public void setNewData(Boolean newData) {
        this.newData = newData;
    }

    /**
     * Returns the primary switch.
     *
     * @return the primary switch.
     */
    public Boolean getPrimarySwitch() {
        return this.primarySwitch;
    }

    /**
     * Sets the primary switch.
     *
     * @param primarySwitch the primary switch to set.
     */
    public void setPrimarySwitch(Boolean primarySwitch) {
        this.primarySwitch = primarySwitch;
    }
    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
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
        if (!(o instanceof CandidateItemScanCode)) return false;

        CandidateItemScanCode that = (CandidateItemScanCode) o;

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
        return "CandidateItemScanCode{" +
                "key=" + key +
                ", candidateItemMaster=" + candidateItemMaster +
                ", scanType='" + scanType + '\'' +
                ", retailPackQuantity=" + retailPackQuantity +
                ", newData='" + newData + '\'' +
                ", primarySwitch='" + primarySwitch + '\'' +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateItemId() {
        if (this.getKey().getCandidateItemId() == null) {
            this.getKey().setCandidateItemId(this.candidateItemMaster.getCandidateItemId());
        }
    }
}
