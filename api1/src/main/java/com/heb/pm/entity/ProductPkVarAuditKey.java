/*
 * ProductPkVarAuditKey
 *
 *  Copyright (c) 2018 HEB
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
 * The primary key class for the prod_pk_variation database table.
 * @author vn70633
 * @since 2.15.0
 */
@Embeddable
public class ProductPkVarAuditKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="scn_cd_id")
    private Long upc;

    @Column(name="seq_nbr")
    private Long sequence;

    @Column(name="src_system_id")
    private Integer sourceSystem;

    @Column(name = "AUD_REC_CRE8_TS", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, length = 0)
    private LocalDateTime changedOn;

    /**
     * Getter for changedOn attribute
     * @return
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Setter for changedOn attribute
     * @param changedOn - new attribute to set
     */
    public void setChangedOn(LocalDateTime changedOn) {
        this.changedOn = changedOn;
    }

    /**
     * @return the upc
     */
    public Long getUpc() {
        return upc;
    }

    /**
     * @param upc the upc to set
     */
    public void setUpc(Long upc) {
        this.upc = upc;
    }

    /**
     * @return the sequence
     */
    public Long getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the sourceSystem
     */
    public Integer getSourceSystem() {
        return sourceSystem;
    }

    /**
     * @param sourceSystem the sourceSystem to set
     */
    public void setSourceSystem(Integer sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    /**
     * Compares this object to another for equality.
     *
     * @param o The object to compare to.
     * @return True if the objects are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductPkVarAuditKey)) return false;

        ProductPkVarAuditKey that = (ProductPkVarAuditKey) o;

        if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
        if (sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) return false;
        if (sourceSystem != null ? !sourceSystem.equals(that.sourceSystem) : that.sourceSystem != null) return false;
        return changedOn != null ? changedOn.equals(that.changedOn) : that.changedOn == null;
    }

    /**
     * Returns a hash code for this object.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = upc != null ? upc.hashCode() : 0;
        result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
        result = 31 * result + (sourceSystem != null ? sourceSystem.hashCode() : 0);
        result = 31 * result + (changedOn != null ? changedOn.hashCode() : 0);
        return result;
    }
}
