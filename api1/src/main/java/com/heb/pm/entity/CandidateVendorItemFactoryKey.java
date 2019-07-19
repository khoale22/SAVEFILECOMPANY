/*
 *  CandidateVendorItemFactoryKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents a ps_vnd_itm_fct_xrf key. A ps vend loc itm key contains the id of the ps_vnd_itm_fct_xrf.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateVendorItemFactoryKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "vend_loc_typ_cd", nullable = false, length = 2)
    @Type(type = "fixedLengthCharPK")
    private String vendorType;

    @Column(name = "vend_loc_nbr", nullable = false)
    private Integer vendorNumber;

    @Column(name = "ps_itm_id", nullable = false)
    private Integer candidateItemId;

    @Column(name = "fctry_id", nullable = false)
    private Integer factoryId;

    /**
     * Sets the vendorType
     */
    public String getVendorType() {
        return vendorType;
    }

    /**
     * @return Gets the value of vendorType and returns vendorType
     */
    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    /**
     * Sets the vendorNumber
     */
    public Integer getVendorNumber() {
        return vendorNumber;
    }

    /**
     * @return Gets the value of vendorNumber and returns vendorNumber
     */
    public void setVendorNumber(Integer vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    /**
     * @return Gets the value of candidateItemId and returns candidateItemId
     */
    public void setCandidateItemId(Integer candidateItemId) {
        this.candidateItemId = candidateItemId;
    }

    /**
     * Sets the candidateItemId
     */
    public Integer getCandidateItemId() {
        return candidateItemId;
    }

    /**
     * @return Gets the value of factoryId and returns factoryId
     */
    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    /**
     * Sets the factoryId
     */
    public Integer getFactoryId() {
        return factoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateVendorItemFactoryKey)) return false;

        CandidateVendorItemFactoryKey that = (CandidateVendorItemFactoryKey) o;

        if (getVendorType() != null ? !getVendorType().equals(that.getVendorType()) : that.getVendorType() != null)
            return false;
        if (getVendorNumber() != null ? !getVendorNumber().equals(that.getVendorNumber()) : that.getVendorNumber() != null)
            return false;
        return getCandidateItemId() != null ? getCandidateItemId().equals(that.getCandidateItemId()) : that.getCandidateItemId() == null;
    }

    @Override
    public int hashCode() {
        int result = getVendorType() != null ? getVendorType().hashCode() : 0;
        result = 31 * result + (getVendorNumber() != null ? getVendorNumber().hashCode() : 0);
        result = 31 * result + (getCandidateItemId() != null ? getCandidateItemId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CandidateVendorLocationItemKey{" +
                "vendorType='" + vendorType + '\'' +
                ", vendorNumber=" + vendorNumber +
                ", candidateItemId=" + candidateItemId +
                '}';
    }
}