/*
 *  CandidateVendorLocationItemKey
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
 * Represents a ps vend loc itm key. A ps vend loc itm key contains the id of the ps vend loc itm.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateVendorLocationItemKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The vend loc typ cd.
     */
    @Column(name = "vend_loc_typ_cd", nullable = false, length = 2)
    @Type(type = "fixedLengthCharPK")
    private String vendorType;

    /**
     * The vend loc nbr.
     */
    @Column(name = "vend_loc_nbr", nullable = false)
    private Integer vendorNumber;

    /**
     * The ps itm id.
     */
    @Column(name = "ps_itm_id", nullable = false)
    private Integer candidateItemId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateVendorLocationItemKey)) return false;

        CandidateVendorLocationItemKey that = (CandidateVendorLocationItemKey) o;

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