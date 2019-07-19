/*
 *  CandidateItemWarehouseCommentsKey
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
 * Represents the key for CandidateItemWarehouseComments.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateItemWarehouseCommentsKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "WHSE_LOC_NBR", nullable = false)
    private Integer warehouseNumber;

    @Column(name = "WHSE_LOC_TYP_CD", nullable = false, length = 2)
    private String warehouseType;

    @Column(name = "ITM_CMT_TYP_CD", nullable = false, length = 5)
    private String itemCommentType;

    @Column(name = "ITM_WHSE_CMT_NBR", nullable = false)
    private Integer itemCommentNumber;

    @Column(name = "PS_ITM_ID", nullable = false)
    private Integer candidateItemId;

    /**
     * Returns the warehouse location number
     *
     * @return the warehouse location number
     */
    public Integer getWarehouseNumber() {
        return this.warehouseNumber;
    }

    /**
     * Sets the warehouse location number
     *
     * @param warehouseNumber the new warehouse location number
     */
    public void setWarehouseNumber(Integer warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    /**
     * Returns the warehouse location type code.
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
     * Returns the item comment type code.
     *
     * @return the item comment type code.
     */
    public String getItemCommentType() {
        return this.itemCommentType;
    }

    /**
     * Sets the item comment type code.
     *
     * @param itemCommentType the new item comment type code.
     */
    public void setItemCommentType(String itemCommentType) {
        this.itemCommentType = itemCommentType;
    }

    /**
     * Returns the item warehouse comment number.
     *
     * @return the item warehouse comment number.
     */
    public Integer getItemCommentNumber() {
        return this.itemCommentNumber;
    }

    /**
     * Sets the item warehouse comment number.
     *
     * @param itemCommentNumber the new item warehouse comment number.
     */
    public void setItemCommentNumber(Integer itemCommentNumber) {
        this.itemCommentNumber = itemCommentNumber;
    }

    /**
     * Returns the ps item code.
     *
     * @return the ps item code.
     */
    public Integer getCandidateItemId() {
        return this.candidateItemId;
    }

    /**
     * Sets the ps item code.
     *
     * @param candidateItemId the new ps item code.
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
        if (!(o instanceof CandidateItemWarehouseCommentsKey)) return false;

        CandidateItemWarehouseCommentsKey that = (CandidateItemWarehouseCommentsKey) o;

        if (warehouseNumber != that.warehouseNumber) return false;
        if (itemCommentNumber != that.itemCommentNumber) return false;
        if (candidateItemId != that.candidateItemId) return false;
        if (warehouseType != null ? !warehouseType.equals(that.warehouseType) : that.warehouseType != null)
            return false;
        return itemCommentType != null ? itemCommentType.equals(that.itemCommentType) : that.itemCommentType == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = warehouseNumber;
        result = 31 * result + (warehouseType != null ? warehouseType.hashCode() : 0);
        result = 31 * result + (itemCommentType != null ? itemCommentType.hashCode() : 0);
        result = 31 * result + itemCommentNumber;
        result = 31 * result + candidateItemId;
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateItemWarehouseCommentsKey{" +
                "warehouseNumber=" + warehouseNumber +
                ", warehouseType='" + warehouseType + '\'' +
                ", itemCommentType='" + itemCommentType + '\'' +
                ", itemCommentNumber=" + itemCommentNumber +
                ", candidateItemId=" + candidateItemId +
                '}';
    }
}
