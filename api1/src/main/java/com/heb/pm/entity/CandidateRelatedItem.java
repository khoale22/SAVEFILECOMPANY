/*
 * CandidateRelatedItem
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a ps_related_items
 *
 * @author vn70516
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_related_items")
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateRelatedItem implements Serializable {
    public static String ITM_RLSHP_TYP_CD_MORPH = "MORPH";
    @EmbeddedId
    private CandidateRelatedItemKey key;

    @Column(name = "rlshp_seq_nbr")
    private Integer sequenceNumber;

    /**
     * The itm rlshp typ cd.
     */
    @Column(name = "ITM_RLSHP_TYP_CD", nullable = false, length = 5)
    @Type(type="fixedLengthCharPK")
    private String itemRelationshipType;

    @Column(name = "itm_qty")
    private Integer quantity;

    /**
     * The ps item master by ps itm id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PS_ITM_ID", nullable = false, insertable = false, updatable = false)
    private CandidateItemMaster candidateItemMasterByPsItmId;

    /**
     * The ps item master by ps related itm id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PS_RELATED_ITM_ID", nullable = false, insertable = false, updatable = false)
    private CandidateItemMaster candidateItemMasterByPsRelatedItmId;
    /**
     * @return Gets the value of key and returns key
     */
    public void setKey(CandidateRelatedItemKey key) {
        this.key = key;
    }

    /**
     * Sets the key
     */
    public CandidateRelatedItemKey getKey() {
        return key;
    }

    /**
     * @return Gets the value of sequenceNumber and returns sequenceNumber
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Sets the sequenceNumber
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @return Gets the value of itemRelationshipType and returns itemRelationshipType
     */
    public void setItemRelationshipType(String itemRelationshipType) {
        this.itemRelationshipType = itemRelationshipType;
    }

    /**
     * Sets the itemRelationshipType
     */
    public String getItemRelationshipType() {
        return itemRelationshipType;
    }

    /**
     * @return Gets the value of quantity and returns quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public CandidateItemMaster getCandidateItemMasterByPsItmId() {
        return candidateItemMasterByPsItmId;
    }

    public void setCandidateItemMasterByPsItmId(CandidateItemMaster candidateItemMasterByPsItmId) {
        this.candidateItemMasterByPsItmId = candidateItemMasterByPsItmId;
    }

    public CandidateItemMaster getCandidateItemMasterByPsRelatedItmId() {
        return candidateItemMasterByPsRelatedItmId;
    }

    public void setCandidateItemMasterByPsRelatedItmId(CandidateItemMaster candidateItemMasterByPsRelatedItmId) {
        this.candidateItemMasterByPsRelatedItmId = candidateItemMasterByPsRelatedItmId;
    }
    /**
     * Sets the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateRelatedItem)) return false;

        CandidateRelatedItem that = (CandidateRelatedItem) o;

        if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
        if (getSequenceNumber() != null ? !getSequenceNumber().equals(that.getSequenceNumber()) : that.getSequenceNumber() != null)
            return false;
        if (getItemRelationshipType() != null ? !getItemRelationshipType().equals(that.getItemRelationshipType()) : that.getItemRelationshipType() != null)
            return false;
        return getQuantity() != null ? getQuantity().equals(that.getQuantity()) : that.getQuantity() == null;
    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getSequenceNumber() != null ? getSequenceNumber().hashCode() : 0);
        result = 31 * result + (getItemRelationshipType() != null ? getItemRelationshipType().hashCode() : 0);
        result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CandidateRelatedItem{" +
                "key=" + key +
                ", sequenceNumber=" + sequenceNumber +
                ", itemRelationshipType=" + itemRelationshipType +
                ", quantity=" + quantity +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateItemId() {
        if (this.getKey().getCandidateItemId() == null) {
            this.getKey().setCandidateItemId(this.candidateItemMasterByPsItmId.getCandidateItemId());
        }
        if (this.getKey().getCandidateRelatedItemCode() == null) {
            this.getKey().setCandidateRelatedItemCode(this.candidateItemMasterByPsRelatedItmId.getCandidateItemId());
        }
    }
}
