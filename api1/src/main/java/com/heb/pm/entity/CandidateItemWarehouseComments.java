/*
 *  CandidateItemWarehouseComments
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the item warehouse comments of candidate.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_itm_whse_cmts")
@TypeDefs({
        @TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class)
})
public class CandidateItemWarehouseComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CandidateItemWarehouseCommentsKey key;

    @Column(name = "ITM_CMT_TYP_CD", nullable = false, length = 5, insertable = false, updatable = false)
    @Type(type = "fixedLengthChar")
    private String itemCommentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "WHSE_LOC_TYP_CD", referencedColumnName = "WHSE_LOC_TYP_CD", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "WHSE_LOC_NBR", referencedColumnName = "WHSE_LOC_NBR", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "PS_ITM_ID", referencedColumnName = "PS_ITM_ID", nullable = false, insertable = false, updatable = false)
    })
    private CandidateWarehouseLocationItem candidateWarehouseLocationItem;

    @Column(name = "ITM_WHSE_CMT_TXT", nullable = false, length = 80)
    @Type(type = "fixedLengthChar")
    private String comments;

    /**
     * Gets the key for this record.
     *
     * @return the key for this record.
     */
    public CandidateItemWarehouseCommentsKey getKey() {
        return this.key;
    }

    /**
     * Sets the key for this record.
     *
     * @param key the key for this record to set.
     */
    public void setKey(CandidateItemWarehouseCommentsKey key) {
        this.key = key;
    }

    /**
     * Gets the item comment type code.
     *
     * @return the item comment type code.
     */
    public String getItemCommentType() {
        return this.itemCommentType;
    }

    /**
     * Sets the item comment type code.
     *
     * @param itemCommentType the item comment type code.
     */
    public void setItemCommentType(String itemCommentType) {
        this.itemCommentType = itemCommentType;
    }

    /**
     * Gets the candidate warehouse location item.
     *
     * @return the candidate warehouse location item.
     */
    public CandidateWarehouseLocationItem getCandidateWarehouseLocationItem() {
        return this.candidateWarehouseLocationItem;
    }

    /**
     * Sets the candidate warehouse location item.
     *
     * @param candidateWarehouseLocationItem the candidate warehouse location item to set.
     */
    public void setCandidateWarehouseLocationItem(CandidateWarehouseLocationItem candidateWarehouseLocationItem) {
        this.candidateWarehouseLocationItem = candidateWarehouseLocationItem;
    }

    /**
     * Gets the item warehouse comment text.
     *
     * @return the item warehouse comment text.
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * Sets the item warehouse comment text.
     *
     * @param comments the item warehouse comment text.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Compares another object to this one. If that object is a WarehouseLocationItem, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateItemWarehouseComments)) return false;

        CandidateItemWarehouseComments that = (CandidateItemWarehouseComments) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    /**
     * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
     * they have different hash codes.
     *
     * @return The hash code for this obejct.
     */
    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    /**
     * Returns a String representation of the object.
     *
     * @return A String representation of the object.
     */
    @Override
    public String toString() {
        return "CandidateItemWarehouseComments{" +
                "key=" + key +
                ", itemCommentType='" + itemCommentType + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
    /**
     * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
     * it is inserted into the work request table.
     */
    @PrePersist
    public void setCandidateItemId() {
        if (this.getKey().getCandidateItemId() == null) {
            this.getKey().setCandidateItemId(this.candidateWarehouseLocationItem.getKey().getCandidateItemId());
        }
    }
}
