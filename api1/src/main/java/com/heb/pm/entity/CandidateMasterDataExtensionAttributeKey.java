/*
 *  CandidateMasterDataExtensionAttributeKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of CandidateMasterDataExtensionAttribute.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Embeddable
public class CandidateMasterDataExtensionAttributeKey implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * ITEM PRODUCT KEY.
     */
    public static final String PRODUCT_KEY = "PROD";

    /**
     * PROD.
     */
    public static final String PROD = "PROD";
    @Column(name = "PS_WORK_ID")
    private Long workRequestId;

    @Column(name = "ATTR_ID")
    private Long attributeId;

    @Column(name = "KEY_ID")
    private Long keyId;

    @Column(name = "ITM_PROD_KEY_CD")
    private String itemProductKey;

    @Column(name = "SEQ_NBR")
    private Long sequenceNumber;

    @Column(name = "DTA_SRC_SYS")
    private Long dataSourceSystem;

    /**
     * Returns the work request id.
     *
     * @return the work request id.
     */
    public Long getWorkRequestId() {
        return workRequestId;
    }

    /**
     * Sets the work request id.
     *
     * @param workRequestId the work request id.
     */
    public void setWorkRequestId(Long workRequestId) {
        this.workRequestId = workRequestId;
    }

    /**
     * Returns the attribute id.
     *
     * @return the attribute id.
     */
    public Long getAttributeId() {
        return attributeId;
    }

    /**
     * Sets the attribute id.
     *
     * @param attributeId the attribute id.
     */
    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    /**
     * Returns the key id.
     *
     * @return the key id.
     */
    public Long getKeyId() {
        return keyId;
    }

    /**
     * Sets the key id.
     *
     * @param keyId the key id.
     */
    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    /**
     * Returns the item product key.
     *
     * @return the item product key.
     */
    public String getItemProductKey() {
        return itemProductKey;
    }

    /**
     * Sets the item product key.
     *
     * @param itemProductKey the item product key.
     */
    public void setItemProductKey(String itemProductKey) {
        this.itemProductKey = itemProductKey;
    }

    /**
     * Returns the sequence number.
     *
     * @return the sequence number.
     */
    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the sequence number.
     *
     * @param sequenceNumber the sequence number.
     */
    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Returns the data source system.
     *
     * @return the data source system.
     */
    public Long getDataSourceSystem() {
        return dataSourceSystem;
    }

    /**
     * Sets the data source system.
     *
     * @param dataSourceSystem the data source system.
     */
    public void setDataSourceSystem(Long dataSourceSystem) {
        this.dataSourceSystem = dataSourceSystem;
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
        if (!(o instanceof CandidateMasterDataExtensionAttributeKey)) return false;

        CandidateMasterDataExtensionAttributeKey that = (CandidateMasterDataExtensionAttributeKey) o;

        if (workRequestId != null ? !workRequestId.equals(that.workRequestId) : that.workRequestId != null)
            return false;
        if (attributeId != null ? !attributeId.equals(that.attributeId) : that.attributeId != null) return false;
        if (keyId != null ? !keyId.equals(that.keyId) : that.keyId != null) return false;
        if (itemProductKey != null ? !itemProductKey.equals(that.itemProductKey) : that.itemProductKey != null)
            return false;
        if (sequenceNumber != null ? !sequenceNumber.equals(that.sequenceNumber) : that.sequenceNumber != null)
            return false;
        return dataSourceSystem != null ? dataSourceSystem.equals(that.dataSourceSystem) : that.dataSourceSystem == null;
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
        result = 31 * result + (attributeId != null ? attributeId.hashCode() : 0);
        result = 31 * result + (keyId != null ? keyId.hashCode() : 0);
        result = 31 * result + (itemProductKey != null ? itemProductKey.hashCode() : 0);
        result = 31 * result + (sequenceNumber != null ? sequenceNumber.hashCode() : 0);
        result = 31 * result + (dataSourceSystem != null ? dataSourceSystem.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateMasterDataExtensionAttributeKey{" +
                "workRequestId=" + workRequestId +
                ", attributeId=" + attributeId +
                ", keyId=" + keyId +
                ", itemProductKey='" + itemProductKey + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", dataSourceSystem=" + dataSourceSystem +
                '}';
    }
}
