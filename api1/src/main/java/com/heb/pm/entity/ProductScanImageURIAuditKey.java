/*
 * ProductScanImageURIAuditKey
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Holds the unique identifier for the ProductScanImageUIRAudit object
 *
 * @author vn70529
 * @version 2.39.0
 */
@Embeddable
public class ProductScanImageURIAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int FOUR_BYTES = 32;


    @Column(name = "scn_cd_id")
    private long id;

    @Column(name = "seq_nbr")
    private long sequenceNumber;

    @Column(name = "aud_rec_cre8_ts")
    private LocalDateTime changedOn;

    /**
     * Returns the id component to uniquely identify a product upc scan code object
     *
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id component to uniquely identify a product upc scan code object
     *
     * @param id the new id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the sequenceNumber of ProductScanImageURIAudit object
     *
     * @return sequenceNumber the sequence number.
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Set the sequenceNumber of ProductScanImageURIAudit object.
     *
     * @param sequenceNumber the sequenceNumber
     */
    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Returns the timestamp for when the record was made
     *
     * @return changedOn the change on date time
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Sets the changedOn data time
     *
     * @param changedOn the changed on data time.
     */
    public void setChangedOn(LocalDateTime changedOn) {
        this.changedOn = changedOn;
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

        ProductScanImageURIAuditKey that = (ProductScanImageURIAuditKey) o;

        if (id != that.id) return false;
        if (sequenceNumber != that.sequenceNumber) return false;
        return changedOn != null ? changedOn.equals(that.getChangedOn()) : that.getChangedOn() == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> FOUR_BYTES));
        result = FOUR_BYTES * result + (int) (sequenceNumber ^ (sequenceNumber >>> FOUR_BYTES));
        result = FOUR_BYTES * result + (changedOn != null ? changedOn.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "ProductScanImageURIAuditKey{" +
                "id=" + id +
                ", sequenceNumber=" + sequenceNumber +
                ", changedOn=" + changedOn +
                '}';
    }
}
