/*
 *  CandidateItemWarehouseVendorKey
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
 * Represents the key of CandidateItemWarehouseVendor.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateItemWarehouseVendorKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "VEND_LOC_TYP_CD", nullable = false, length = 2)
    @Type(type = "fixedLengthCharPK")
    private String vendorType;

    @Column(name = "VEND_LOC_NBR", nullable = false)
    private Integer vendorNumber;

    @Column(name = "WHSE_LOC_TYP_CD", nullable = false, length = 2)
    @Type(type = "fixedLengthCharPK")
    private String warehouseType;

    @Column(name = "WHSE_LOC_NBR", nullable = false)
    private Integer warehouseNumber;

    @Column(name = "PS_ITM_ID", nullable = false)
    private Integer candidateItemId;

    /**
     * Gets the vendor location type code.
     *
     * @return the vendor location type code.
     */
    public String getVendorType() {
        return this.vendorType;
    }

    /**
     * Sets the vendor location type code.
     *
     * @param vendorType the new vendor location type code.
     */
    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    /**
     * Gets the vendor location number.
     *
     * @return the vendor location number.
     */
    public Integer getVendorNumber() {
        return this.vendorNumber;
    }

    /**
     * Sets the vendor location number.
     *
     * @param vendorNumber the new vendor location number.
     */
    public void setVendorNumber(Integer vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    /**
     * Gets the warehouse location type code.
     *
     * @return the warehouse location type code.
     */
    public String getWarehouseType() {
        return this.warehouseType;
    }

    /**
     * Sets the warehouse location type code.
     *
     * @param warehouseType the new warehouse location type code.
     */
    public void setWarehouseType(String warehouseType) {
        this.warehouseType = warehouseType;
    }

    /**
     * Gets the warehouse location number.
     *
     * @return the warehouse location number.
     */
    public Integer getWarehouseNumber() {
        return this.warehouseNumber;
    }

    /**
     * Sets the warehouse location number.
     *
     * @param warehouseNumber the new warehouse location number.
     */
    public void setWarehouseNumber(Integer warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    /**
     * Gets the candidate item id.
     *
     * @return the candidate item id.
     */
    public Integer getCandidateItemId() {
        return this.candidateItemId;
    }

    /**
     * Sets the candidate item id.
     *
     * @param candidateItemId the new candidate item id.
     */
    public void setCandidateItemId(Integer candidateItemId) {
        this.candidateItemId = candidateItemId;
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
        if (!(o instanceof CandidateItemWarehouseVendorKey)) return false;

        CandidateItemWarehouseVendorKey that = (CandidateItemWarehouseVendorKey) o;

        if (vendorType != null ? !vendorType.equals(that.vendorType) : that.vendorType != null) return false;
        if (vendorNumber != null ? !vendorNumber.equals(that.vendorNumber) : that.vendorNumber != null) return false;
        if (warehouseType != null ? !warehouseType.equals(that.warehouseType) : that.warehouseType != null)
            return false;
        if (warehouseNumber != null ? !warehouseNumber.equals(that.warehouseNumber) : that.warehouseNumber != null)
            return false;
        return candidateItemId != null ? candidateItemId.equals(that.candidateItemId) : that.candidateItemId == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = vendorType != null ? vendorType.hashCode() : 0;
        result = 31 * result + (vendorNumber != null ? vendorNumber.hashCode() : 0);
        result = 31 * result + (warehouseType != null ? warehouseType.hashCode() : 0);
        result = 31 * result + (warehouseNumber != null ? warehouseNumber.hashCode() : 0);
        result = 31 * result + (candidateItemId != null ? candidateItemId.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateItemWarehouseVendorKey{" +
                "vendorType='" + vendorType + '\'' +
                ", vendorNumber=" + vendorNumber +
                ", warehouseType='" + warehouseType + '\'' +
                ", warehouseNumber=" + warehouseNumber +
                ", candidateItemId=" + candidateItemId +
                '}';
    }
}
