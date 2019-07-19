/*
 *  CandidateMasterDataExtensionAttribute
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The master data external attribute of candidate.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_mst_dta_extn_at")
public class CandidateMasterDataExtensionAttribute implements Serializable {
	public static final int NUTRIENT_ATTRIBUTE_ID = 1643;
	public static final long PDP_TEMPLATE_ATTRIBUTE_ID = 515;
	public static final String ITEM_PRODUCT_KEY_UPC = "UPC";
    private static final long serialVersionUID = 1L;

	public static final String YES_STATUS = "Y";
	public static final String DELETED_STATUS = "D";

    @EmbeddedId
    private CandidateMasterDataExtensionAttributeKey key;

    @Column(name = "ATTR_CD_ID")
    private Integer attributeCode;

    @Column(name = "ATTR_VAL_TXT")
    private String attributeValueText;

    @Column(name = "ATTR_VAL_NBR")
    private Double attributeValueNumber;

    @Column(name = "ATTR_VAL_DT")
    private LocalDate attributeValueDate;

    @Column(name = "ATTR_VAL_TS")
    private LocalDateTime attributeValueTime;

    @Column(name = "CRE8_UID")
    private String createUserId;

    @Column(name = "CRE8_TS")
    private LocalDateTime createDate;

    @Column(name = "LST_UPDT_UID")
    private String lastUpdateUserId;

    @Column(name = "LST_UPDT_TS")
    private LocalDateTime lastUpdateDate;

    @Column(name = "NEW_DTA_SW")
    private String newData;

    @JsonIgnoreProperties({"candidateMasterDataExtensionAttributes", "candidateProductPkVariations", "candidateNutrients", "candidateNutrientPanelHeaders", "candidateGenericEntityRelationship", "candidateFulfillmentChannels"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ps_work_id", referencedColumnName = "ps_work_id", insertable = false, updatable = false, nullable = false)
    private CandidateWorkRequest candidateWorkRequest;
    /**
     * Returns the key for this record.
     *
     * @return the key for this record.
     */
    public CandidateMasterDataExtensionAttributeKey getKey() {
        return key;
    }

    /**
     * Sets the key for this record.
     *
     * @param key the key for this record.
     */
    public void setKey(CandidateMasterDataExtensionAttributeKey key) {
        this.key = key;
    }

    /**
     * Returns the attribute code id.
     *
     * @return the attribute code id.
     */
    public int getAttributeCode() {
        return attributeCode;
    }

    /**
     * Sets the attribute code id.
     *
     * @param attributeCode the attribute code id.
     */
    public void setAttributeCode(Integer attributeCode) {
        this.attributeCode = attributeCode;
    }

    /**
     * Returns the attribute value text.
     *
     * @return the attribute value text.
     */
    public String getAttributeValueText() {
        return attributeValueText;
    }

    /**
     * Sets the attribute value text.
     *
     * @param attributeValueText the attribute value text.
     */
    public void setAttributeValueText(String attributeValueText) {
        this.attributeValueText = attributeValueText;
    }

    /**
     * Returns the attribute value number.
     *
     * @return the attribute value number.
     */
    public Double getAttributeValueNumber() {
        return attributeValueNumber;
    }

    /**
     * Sets the attribute value number.
     *
     * @param attributeValueNumber the attribute value number.
     */
    public void setAttributeValueNumber(Double attributeValueNumber) {
        this.attributeValueNumber = attributeValueNumber;
    }

    /**
     * Returns the attribute value date.
     *
     * @return the attribute value date.
     */
    public LocalDate getAttributeValueDate() {
        return attributeValueDate;
    }

    /**
     * Sets the attribute value date.
     *
     * @param attributeValueDate the attribute value date.
     */
    public void setAttributeValueDate(LocalDate attributeValueDate) {
        this.attributeValueDate = attributeValueDate;
    }

    /**
     * Returns the attribute value timestamp.
     *
     * @return the attribute value timestamp.
     */
    public LocalDateTime getAttributeValueTime() {
        return attributeValueTime;
    }

    /**
     * Sets the attribute value timestamp.
     *
     * @param attributeValueTime the attribute value timestamp.
     */
    public void setAttributeValueTime(LocalDateTime attributeValueTime) {
        this.attributeValueTime = attributeValueTime;
    }

    /**
     * Returns the ID of the user who created this record.
     *
     * @return the ID of the user who created this record.
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * Sets the ID of the user who created this record.
     *
     * @param createUserId the ID of the user who created this record.
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * Returns the date and time this record was created.
     *
     * @return the date and time this record was created.
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date and time this record was created.
     *
     * @param createDate the date and time this record was created.
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Returns the one-pass ID of the last user to update this record.
     *
     * @return the one-pass ID of the last user to update this record.
     */
    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    /**
     * Sets the one-pass ID of the last user to update this record.
     *
     * @param lastUpdateUserId the one-pass ID of the last user to update this record.
     */
    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    /**
     * Returns the date and time this record was last updated.
     *
     * @return the date and time this record was last updated.
     */
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets the date and time this record was last updated.
     *
     * @param lastUpdateDate the date and time this record was last updated.
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Returns the newData to take on this record.
     *
     * @return the newData to take on this record.
     */
    public String isNewData() {
        return newData;
    }

    /**
     * Sets the newData to take on this record.
     *
     * @param newData the newData to take on this record.
     */
    public void setNewData(String newData) {
        this.newData = newData;
    }
    public CandidateWorkRequest getCandidateWorkRequest() {
        return candidateWorkRequest;
    }

    public void setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
        this.candidateWorkRequest = candidateWorkRequest;
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
        if (!(o instanceof CandidateMasterDataExtensionAttribute)) return false;

        CandidateMasterDataExtensionAttribute that = (CandidateMasterDataExtensionAttribute) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateMasterDataExtensionAttribute{" +
                "key=" + key +
                ", attributeCode=" + attributeCode +
                ", attributeValueText='" + attributeValueText + '\'' +
                ", attributeValueNumber=" + attributeValueNumber +
                ", newData='" + newData + '\'' +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateKey() {
        if (this.getKey().getWorkRequestId() == null) {
            this.getKey().setWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
        }
    }
}
