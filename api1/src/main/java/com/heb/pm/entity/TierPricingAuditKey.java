/*
 *  TierPricingAuditKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This is the key for tier pricing Audit.
 *
 * @author vn70633
 * @since 2.19.0
 */
@Embeddable
public class TierPricingAuditKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int PRIME_NUMBER = 31;

    @Column(name = "prod_id")
    private Long prodId;

    @Column(name="eff_ts")
    private LocalDate effectiveTimeStamp;

    @Column(name = "min_disc_thrh_qty")
    private Long discountQuantity;

    @Column(name = "aud_rec_cre8_ts")
    private LocalDateTime changedOn;

    /**
     * Returns the ProdId
     *
     * @return ProdId
     */
    public Long getProdId() {
        return prodId;
    }

    /**
     * Sets the ProdId
     *
     * @param prodId The ProdId
     */
    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    /**
     * Returns the EffectiveTimeStamp
     *
     * @return EffectiveTimeStamp
     */
    public LocalDate getEffectiveTimeStamp() {
        return effectiveTimeStamp;
    }

    /**
     * Sets the EffectiveTimeStamp
     *
     * @param effectiveTimeStamp The EffectiveTimeStamp
     */
    public void setEffectiveTimeStamp(LocalDate effectiveTimeStamp) {
        this.effectiveTimeStamp = effectiveTimeStamp;
    }

    /**
     * Returns the DiscountQuantity. This is the minimum order quantity for a discount.
     *
     * @return DiscountQuantity
     */
    public Long getDiscountQuantity() {
        return discountQuantity;
    }

    /**
     * Sets the DiscountQuantity
     *
     * @param discountQuantity The DiscountQuantity
     */
    public void setDiscountQuantity(Long discountQuantity) {
        this.discountQuantity = discountQuantity;
    }

    /**
     * Returns the changed on. The changed on is the timestamp in which the transaction occurred.
     *
     * @return Timestamp
     */
    public LocalDateTime getChangedOn() {
        return changedOn;
    }

    /**
     * Sets the Timestamp
     *
     * @param timestamp The Timestamp
     */
    public void setChangedOn(LocalDateTime timestamp) {
        this.changedOn = changedOn;
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
        if (o == null || getClass() != o.getClass()) return false;

        TierPricingAuditKey that = (TierPricingAuditKey) o;

        if (prodId != null ? !prodId.equals(that.prodId) : that.prodId != null) return false;
        if (effectiveTimeStamp != null ? !effectiveTimeStamp.equals(that.effectiveTimeStamp) : that.effectiveTimeStamp != null)
            return false;
        if (discountQuantity != null ? !discountQuantity.equals(that.discountQuantity) : that.discountQuantity != null)
            return false;
        return changedOn != null ? changedOn.equals(that.changedOn) : that.changedOn == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = prodId != null ? prodId.hashCode() : 0;
        result = PRIME_NUMBER * result + (effectiveTimeStamp != null ? effectiveTimeStamp.hashCode() : 0);
        result = PRIME_NUMBER * result + (discountQuantity != null ? discountQuantity.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "TierPricingKey{" +
                "prodId=" + prodId +
                ", effectiveTimeStamp=" + effectiveTimeStamp +
                ", discountQuantity=" + discountQuantity +
                ", changedOn=" + changedOn +
                '}';
    }
}

