/*
 *  NutrientRoundingRuleKey
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Represents the key for the pd_nutrient_rules table.
 *
 * @author s573181
 * @since 2.1.0
 */
public class NutrientRoundingRuleKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name="pd_lbl_ntrnt_cd")
	private int nutrientCode;

	@Column(name = "pd_ntrnt_brng_qty")
	private int lowerBound;

	/**
	 * Returns the nutrient code.
	 *
	 * @return the nutrient code.
	 */
	public int getNutrientCode() {
		return nutrientCode;
	}

	/**
	 * Sets the nutrient code.
	 *
	 * @param nutrientCode the nutrient code.
	 */
	public void setNutrientCode(int nutrientCode) {
		this.nutrientCode = nutrientCode;
	}

	/**
	 * Returns the lower bound of the rounding rule.
	 *
	 * @return the lower bound of the rounding rule.
	 */
	public int getLowerBound() {
		return lowerBound;
	}

	/**
	 * Sets  the lower bound of the rounding rule.v
	 * @param lowerBound  the lower bound of the rounding rule.
	 */
	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	/**
	 * Tests for equality with another object. Equality is based on the nutrientCode and lowerBound.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NutrientRoundingRuleKey)) return false;

		NutrientRoundingRuleKey that = (NutrientRoundingRuleKey) o;

		if (nutrientCode != that.nutrientCode) return false;
		return lowerBound == that.lowerBound;

	}

	/**
	 * Returns a hash code for this object. Equal objects always return the same value. Unequals objects (probably)
	 * return different values.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = nutrientCode;
		result = PRIME_NUMBER * result + lowerBound;
		return result;
	}
	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "NutrientRoundingRuleKey{" +
				"nutrientCode=" + nutrientCode +
				", lowerBound=" + lowerBound +
				'}';
	}
}
