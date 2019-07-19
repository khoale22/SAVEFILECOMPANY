/*
 *  TierPricing
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * This represents the tier pricing entity.
 *
 * @author l730832
 * @since 2.13.0
 */
@Entity
@Table(name = "prod_disc_thrh")
public class TierPricing implements Serializable {

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


		QuantityDiscountType(String code, String displayName){
			this.code = code;
			this.displayName=displayName;
		}

		public String getCode(){
			return this.code;
		}
		public String getDisplayName(){
			return this.displayName;
		}

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
	private TierPricingKey key;

	@Column(name = "thrh_disc_amt")
	private Double discountValue;

	@Column(name = "thrh_disc_typ_cd")
	private String discountTypeCode;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public TierPricingKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(TierPricingKey key) {
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

		TierPricing that = (TierPricing) o;

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
