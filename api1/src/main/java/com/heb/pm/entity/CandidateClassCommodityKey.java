/*
 *  CandidateClassCommodityKey
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key for CandidateClassCommodity.
 *
 * @author vn70529
 * @since 2.33.0
 */
@Embeddable
public class CandidateClassCommodityKey implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int PRIME_NUMBER = 31;

    @Column(name = "PS_WORK_ID")
    private Long workRequestId;

    @Column(name="pd_omi_com_cls_cd")
    private Integer classCode;

    @Column(name="pd_omi_com_cd")
    private Integer commodityCode;

    /**
     * Get the workRequestId.
     *
     * @return the workRequestId
     */
    public Long getWorkRequestId() {
        return workRequestId;
    }

    /**
     * Set the workRequestId.
     *
     * @param workRequestId the workRequestId to set
     */
    public void setWorkRequestId(Long workRequestId) {
        this.workRequestId = workRequestId;
    }

    /**
     * Returns the OMI Class for this object.
     *
     * @return The OMI Class for this object.
     */
    public Integer getClassCode() {
        return classCode;
    }

    /**
     * Sets the OMI Class for this object.
     *
     * @param classCode The Class for this object.
     */
    public void setClassCode(Integer classCode) {
        this.classCode = classCode;
    }

    /**
     * Returns the Commodity for this object.
     *
     * @return The Commodity for this object.
     */
    public Integer getCommodityCode() {
        return commodityCode;
    }

    /**
     * Sets the Commodity for this object.
     *
     * @param commodityCode The Commodity for this object.
     */
    public void setCommodityCode(Integer commodityCode) {
        this.commodityCode = commodityCode;
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
        if (o == null || getClass() != o.getClass()) return false;

        CandidateClassCommodityKey that = (CandidateClassCommodityKey) o;

        if (workRequestId != null ? !workRequestId.equals(that.workRequestId) : that.workRequestId != null) return false;
        if (classCode != null ? !classCode.equals(that.classCode) : that.classCode != null) return false;
        return commodityCode != null ? commodityCode.equals(that.commodityCode) : that.commodityCode == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = workRequestId != null ? workRequestId.hashCode() : 0;
        result = CandidateClassCommodityKey.PRIME_NUMBER * result + (classCode != null ? classCode.hashCode() : 0) + (commodityCode != null ? commodityCode.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateClassCommodityKey{" +
                "workRequestId=" + workRequestId +
                "classCode=" + classCode +
                ", commodityCode=" + commodityCode +
                '}';
    }
}
