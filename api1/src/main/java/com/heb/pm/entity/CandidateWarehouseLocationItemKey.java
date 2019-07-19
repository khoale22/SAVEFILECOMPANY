/*
 * CandidateWarehouseLocationItemKey
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Composite key for CandidateWarehouseLocationItem.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Embeddable
public class CandidateWarehouseLocationItemKey implements Serializable {

    private static final long serialVersionUID = 1L;
    public static String WHSE_LOC_TYP_CD="W";
    @Column(name = "ps_itm_id")
    private Integer candidateItemId;

    @Column(name = "whse_loc_typ_cd")
    private String warehouseType;

    @Column(name = "whse_loc_nbr")
    private Integer warehouseNumber;

    /**
     * Returns the item code for the WarehouseLocationItem object.
     *
     * @return The item code for the WarehouseLocationItem object.
     */
    public Integer getCandidateItemId() {
        return candidateItemId;
    }

    /**
     * Sets the item code for the WarehouseLocationItem object.
     *
     * @param candidateItemId The item code for the WarehouseLocationItem object.
     */
    public void setCandidateItemId(Integer candidateItemId) {
        this.candidateItemId = candidateItemId;
    }

    /**
     * Returns the warehouse type for the WarehouseLocationItem object.
     *
     * @return The warehouse type for the WarehouseLocationItem object.
     */
    public String getWarehouseType() {
        return warehouseType;
    }

    /**
     * Sets the warehouse type for the WarehouseLocationItem object.
     *
     * @param warehouseType The warehouse type for the WarehouseLocationItem object.
     */
    public void setWarehouseType(String warehouseType) {
        this.warehouseType = warehouseType;
    }

    /**
     * Returns the warehouse number for the WarehouseLocationItem object.
     *
     * @return The warehouse number for the WarehouseLocationItem object.
     */
    public Integer getWarehouseNumber() {
        return warehouseNumber;
    }

    /**
     * Sets the warehouse number for the WarehouseLocationItem object.
     *
     * @param warehouseNumber The warehouse number for the WarehouseLocationItem object.
     */
    public void setWarehouseNumber(Integer warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    /**
     * Compares this object to another for equality.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateWarehouseLocationItemKey)) return false;

        CandidateWarehouseLocationItemKey that = (CandidateWarehouseLocationItemKey) o;

        if (candidateItemId != null ? !candidateItemId.equals(that.candidateItemId) : that.candidateItemId != null)
            return false;
        if (warehouseType != null ? !warehouseType.equals(that.warehouseType) : that.warehouseType != null)
            return false;
        return warehouseNumber != null ? warehouseNumber.equals(that.warehouseNumber) : that.warehouseNumber == null;
    }

    /**
     * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
     * different hash codes.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = candidateItemId != null ? candidateItemId.hashCode() : 0;
        result = 31 * result + (warehouseType != null ? warehouseType.hashCode() : 0);
        result = 31 * result + (warehouseNumber != null ? warehouseNumber.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateWarehouseLocationItemKey{" +
                "candidateItemId=" + candidateItemId +
                ", warehouseType='" + warehouseType + '\'' +
                ", warehouseNumber=" + warehouseNumber +
                '}';
    }
}