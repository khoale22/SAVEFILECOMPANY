/*
 *  CandidateFulfillmentChannelKey
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
 * The primary key class for the PS_PROD_FLFL_CHNL database table.
 *
 * @author
 * @since 2.12.0
 */
@Embeddable
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateFulfillmentChannelKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "PS_PROD_ID")
    private Long candidateProductId;

    @Column(name = "PS_WORK_ID")
    private Long workRequestId;

    @Column(name = "SALS_CHNL_CD", length = 5, insertable = false, updatable = false)
    @Type(type = "fixedLengthCharPK")
    private String salesChannelCode;

    @Column(name = "FLFL_CHNL_CD", length = 5, insertable = false, updatable = false)
    @Type(type = "fixedLengthCharPK")
    private String fulfillmentChannelCode;

    /**
     * Returns the product Id of this record.
     *
     * @return the product Id of this record.
     */
    public Long getCandidateProductId() {
        return this.candidateProductId;
    }

    /**
     * Sets the product Id of this record.
     *
     * @param candidateProductId the product Id of this record.
     */
    public void setCandidateProductId(Long candidateProductId) {
        this.candidateProductId = candidateProductId;
    }

    /**
     * Returns the work request Id of this record.
     *
     * @return the work request Id of this record.
     */
    public Long getWorkRequestId() {
        return this.workRequestId;
    }

    /**
     * Sets the work request Id of this record.
     *
     * @param workRequestId the work request Id of this record.
     */
    public void setWorkRequestId(Long workRequestId) {
        this.workRequestId = workRequestId;
    }

    /**
     * Returns the sales channel code.
     *
     * @return the sales channel code.
     */
    public String getSalesChannelCode() {
        return this.salesChannelCode;
    }

    /**
     * Sets the sales channel code.
     *
     * @param salesChannelCode the sales channel code.
     */
    public void setSalesChannelCode(String salesChannelCode) {
        this.salesChannelCode = salesChannelCode;
    }

    /**
     * Returns the fulfillment channel code.
     *
     * @return the fulfillment channel code.
     */
    public String getFulfillmentChannelCode() {
        return this.fulfillmentChannelCode;
    }

    /**
     * Sets the fulfillment channel code.
     *
     * @param fulfillmentChannelCode the fulfillment channel code.
     */
    public void setFulfillmentChannelCode(String fulfillmentChannelCode) {
        this.fulfillmentChannelCode = fulfillmentChannelCode;
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
        if (!(o instanceof CandidateFulfillmentChannelKey)) return false;

        CandidateFulfillmentChannelKey that = (CandidateFulfillmentChannelKey) o;

        if (candidateProductId != null ? !candidateProductId.equals(that.candidateProductId) : that.candidateProductId != null) return false;
        if (workRequestId != null ? !workRequestId.equals(that.workRequestId) : that.workRequestId != null)
            return false;
        if (salesChannelCode != null ? !salesChannelCode.equals(that.salesChannelCode) : that.salesChannelCode != null)
            return false;
        return fulfillmentChannelCode != null ? fulfillmentChannelCode.equals(that.fulfillmentChannelCode) : that.fulfillmentChannelCode == null;
    }

    /**
     * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects have
     * different hash codes.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = candidateProductId != null ? candidateProductId.hashCode() : 0;
        result = 31 * result + (workRequestId != null ? workRequestId.hashCode() : 0);
        result = 31 * result + (salesChannelCode != null ? salesChannelCode.hashCode() : 0);
        result = 31 * result + (fulfillmentChannelCode != null ? fulfillmentChannelCode.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateFulfillmentChannelKey{" +
                "candidateProductId=" + candidateProductId +
                ", workRequestId=" + workRequestId +
                ", salesChannelCode='" + salesChannelCode + '\'' +
                ", fulfillmentChannelCode='" + fulfillmentChannelCode + '\'' +
                '}';
    }
}