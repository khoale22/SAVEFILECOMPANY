/*
 * CandidateVendorItemFactory
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a candidate vendor item factory.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_vnd_itm_fct_xrf")
public class CandidateVendorItemFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CandidateVendorItemFactoryKey key;

    /**
     * The lst updt ts.
     */
    @Column(name="LST_UPDT_DT", nullable=false)
    private LocalDate lastUpdateTs;

    /**
     * The lst updt usr id.
     */
    @Column(name="LST_UID", nullable=false)
    @Type(type="fixedLengthCharPK")
    private String lastUpdateUserId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "fctry_id", referencedColumnName = "fctry_id", insertable = false, updatable = false, nullable = false)
    })
    private Factory factory;

    /**
     * The ps vend loc itm.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "VEND_LOC_TYP_CD", referencedColumnName = "VEND_LOC_TYP_CD", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "VEND_LOC_NBR", referencedColumnName = "VEND_LOC_NBR", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "PS_ITM_ID", referencedColumnName = "PS_ITM_ID", nullable = false, insertable = false, updatable = false)
    })
    private CandidateVendorLocationItem candidateVendorLocationItem;
    /**
     * @return Gets the value of key and returns key
     */
    public void setKey(CandidateVendorItemFactoryKey key) {
        this.key = key;
    }

    /**
     * Sets the key
     */
    public CandidateVendorItemFactoryKey getKey() {
        return key;
    }

    /**
     * @return Gets the value of factory and returns factory
     */
    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    /**
     * Sets the factory
     */
    public Factory getFactory() {
        return factory;
    }
    public LocalDate getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(LocalDate lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }
    public CandidateVendorLocationItem getCandidateVendorLocationItem() {
        return candidateVendorLocationItem;
    }

    public void setCandidateVendorLocationItem(CandidateVendorLocationItem candidateVendorLocationItem) {
        this.candidateVendorLocationItem = candidateVendorLocationItem;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateVendorItemFactory)) return false;

        CandidateVendorItemFactory that = (CandidateVendorItemFactory) o;

        if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
        return getFactory() != null ? getFactory().equals(that.getFactory()) : that.getFactory() == null;
    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getFactory() != null ? getFactory().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CandidateVendorItemFactory{" +
                "key=" + key +
                ", factory=" + factory +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateItemId() {
        if (this.getKey().getCandidateItemId()== null) {
            this.getKey().setCandidateItemId(this.candidateVendorLocationItem.getKey().getCandidateItemId());
        }
    }
}
