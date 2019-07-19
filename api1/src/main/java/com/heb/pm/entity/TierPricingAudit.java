/*
 *  TierPricingAudit
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.heb.util.audit.Audit;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This represents the tier pricing Audit entity.
 *
 * @author vn70633
 * @since 2.19.0
 */
@Entity
@Table(name = "prod_disc_thrh_aud")
public class TierPricingAudit implements Serializable , Audit {

    private static final long serialVersionUID = 1L;

    // Constants that deal with the Min Order Quantity Discount Type
    private static final String CENTS_OFF_TYPE_CODE = "A";
    private static final String PERCENT_OFF_TYPE_CODE = "P";
    private static final String CENTS_OFF_DISPLAY_STRING = "Cents Off";
    private static final String PERCENT_OFF_DISPLAY_STRING = "Percent Off";

    public enum QuantityDiscountType{

        CentsOff (CENTS_OFF_TYPE_CODE, CENTS_OFF_DISPLAY_STRING),
        PercentOff (PERCENT_OFF_TYPE_CODE, PERCENT_OFF_DISPLAY_STRING);

        private String code;
        private String displayName;

        /**
         * Constructor for the quantity discount type.
         *
         * @param code, displayName
         * @return the quantity discount type.
         */
        QuantityDiscountType(String code, String displayName){
            this.code = code;
            this.displayName=displayName;
        }

        /**
         * @return the code
         */
        public String getCode(){
            return this.code;
        }
        /**
         * @return the display name
         */
        public String getDisplayName(){
            return this.displayName;
        }

        /**
         * Convert a string to the quantity discount type.
         *
         * @param value the value need convert
         * @return the quantity discount type.
         */
        public static QuantityDiscountType convertStringToQuantityDiscountType(String value){
            if(value.equals(CENTS_OFF_TYPE_CODE) || value.equals(CENTS_OFF_DISPLAY_STRING)){
                return CentsOff;
            } else if(value.equals(PERCENT_OFF_TYPE_CODE) || value.equals(PERCENT_OFF_DISPLAY_STRING)) {
                return PercentOff;
            } else{
                return null;
            }
        }
    }

    @EmbeddedId
    private TierPricingAuditKey key;

    @Column(name = "thrh_disc_amt")
    private Double discountValue;

    @Column(name = "thrh_disc_typ_cd")
    private String discountTypeCode;

    @Column(name = "act_cd")
    private String action;

    @Column(name = "lst_updt_uid")
    private String changedBy;

    @Column(name="cre8_ts")
    private LocalDateTime createDate;

    /**
     * @return the createDate
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    /**
     * Returns the Key
     *
     * @return Key
     */
    public TierPricingAuditKey getKey() {
        return key;
    }

    /**
     * Sets the Key
     *
     * @param key The Key
     */
    public void setKey(TierPricingAuditKey key) {
        this.key = key;
    }



    /**
     * Returns the DiscountValue
     *
     * @return DiscountValue
     */
    public Double getDiscountValue() {
        return discountValue;
    }

    /**
     * Sets the DiscountValue
     *
     * @param discountValue The DiscountValue
     */
    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * Returns the DiscountTypeCode
     *
     * @return DiscountTypeCode
     */
    public String getDiscountTypeCode() {
        return discountTypeCode;
    }

    /**
     * Sets the DiscountTypeCode
     *
     * @param discountTypeCode The DiscountTypeCode
     */
    public void setDiscountTypeCode(String discountTypeCode) {
        this.discountTypeCode = discountTypeCode;
    }

    /**
     * Returns changed by. The changed by shows who was doing the action that is being audited. This is the uid(login)
     * that a user has.
     *
     * @return changedBy
     */
    @Override
    public String getChangedBy() {
        return changedBy;
    }

    /**
     * Sets the changedBy
     *
     * @param changedBy The changedBy
     */
    @Override
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }


    /**
     * Gets the changed on time. This is when the modification was done.
     *
     * @return The time the modification was done.
     */
    @Override
    public LocalDateTime getChangedOn() {
        return this.key.getChangedOn();
    }

    /**
     * Sets the changed on time.
     *
     * @param changedOn The time the modification was done.
     */
    public void setChangedOn(LocalDateTime changedOn) {
        this.key.setChangedOn(changedOn);
    }

    /**
     * Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
     *
     * @return ActionCode
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the ActionCode
     *
     * @param action The ActionCode
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Returns the DiscountTypeCodeDisplayName. This is whether or not it is Cents Off or Percent Off
     *
     * @return DiscountTypeCodeDisplayName
     */
    public String getDiscountTypeCodeDisplayName() {
        if(this.discountTypeCode.trim().equals(CENTS_OFF_TYPE_CODE)) {
            return CENTS_OFF_DISPLAY_STRING;
        } else if(this.discountTypeCode.trim().equals(PERCENT_OFF_TYPE_CODE)) {
            return PERCENT_OFF_DISPLAY_STRING;
        }
        return null;
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

        TierPricingAudit that = (TierPricingAudit) o;

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
        return "TierPricing{" +
                "key=" + key +
                ", discountValue=" + discountValue +
                ", discountTypeCode='" + discountTypeCode + '\'' +
                '}';
    }
}
