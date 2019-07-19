/*
 *  NutrientRoundingRule
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a nutrient rounding rule.
 *
 * @author s573181
 * @since 2.1.0
 */
@Entity
@Table(name = "pd_nutrient_rules")
public class NutrientRoundingRule implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NutrientRoundingRuleKey key;

	@Column(name = "pd_ntrnt_erng_qty")
	private int upperBound;

	@Column(name = "pd_ntrnt_incrm_qty")
	private double incrementQuantity;

	@Transient
	private String roundingRulesError;

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public NutrientRoundingRuleKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key The key for this object.
	 */
	public void setKey(NutrientRoundingRuleKey key) {
		this.key = key;
	}

	/**
	 * Returns the upper bound of the rounding rule.
	 *
	 * @return the upper bound of the rounding rule.
	 */
	public int getUpperBound() {
		return upperBound;
	}

	/**
	 * Sets the upper bound of the rounding rule.
	 *
	 * @param upperBound the upper bound of the rounding rule.
	 */
	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	/**
	 * Returns the nutrition increment quantity.
	 *
	 *  @return the nutrition increment quantity.
	 */
	public double getIncrementQuantity() {
		return incrementQuantity;
	}

	/**
	 * Sets the nutrition increment quantity.
	 *
	 * @param incrementQuantity the nutrition increment quantity.
	 */
	public void setIncrementQuantity(double incrementQuantity) {
		this.incrementQuantity = incrementQuantity;
	}

	/**
	 * Returns the rounding rules error message.
	 *
	 * @return the rounding rules error message.
	 */
	public String getRoundingRulesError() {
		return roundingRulesError;
	}

	/**
	 * Sets the rounding rules error message.
	 *
	 * @param roundingRulesError the rounding rules error message.
	 */
	public void setRoundingRulesError(String roundingRulesError) {
		this.roundingRulesError = roundingRulesError;
	}
	/**
	 * Tests for equality with another object. Equality is based on the key.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NutrientRoundingRule)) return false;

		NutrientRoundingRule that = (NutrientRoundingRule) o;

		return key.equals(that.key);

	}

	/**
	 * Returns a hash code for this object. Equal objects always return the same value. Unequals objects (probably)
	 * return different values.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key.hashCode();
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "NutrientRoundingRule{" +
				"key=" + key +
				", upperBound=" + upperBound +
				", incrementQuantity=" + incrementQuantity +
				'}';
	}
}
