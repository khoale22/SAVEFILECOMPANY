/*
 *  NutrientRoundingRuleTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;


/**
 * Tests the NutrientRoundingRule entity.
 *
 * @author s573181
 * @since 2.1.0
 */
public class NutrientRoundingRuleTest {

	private static final int DEFAULT_NUTRIENT_CODE = 1;
	private static final int DEFAULT_LOWER_BOUND = 0;
	private static final int DEFAULT_UPPER_BOUND = 2;
	private static final double DEFAULT_INCREMENT_QUANTITY = 0.0;

	/**
	 * Tests getKey method.
	 */
	@Test
	public void testsGetKey(){
		NutrientRoundingRuleKey key = this.getDefaultRoundingRuleKey();
		Assert.assertEquals(key, this.getDefaultRoundingRuleKey());
	}

	/**
	 * Test getUpperBound method.
	 */
	@Test
	public void testGetUpperBound() {
		NutrientRoundingRule roundingRule = this.getDefaultRoundingRule();
		Assert.assertEquals(roundingRule.getUpperBound(),DEFAULT_UPPER_BOUND);
	}

	/**
	 * Tests the getIncrementQuantity method.
	 */
	@Test
	public void testGetIncrementQuantity() {
		NutrientRoundingRule roundingRule = this.getDefaultRoundingRule();
		Assert.assertEquals(roundingRule.getIncrementQuantity(), DEFAULT_INCREMENT_QUANTITY, 0);
	}

	/*
	 * equals
	 */

	/**
	 * Tests the equals method on itself.
	 */
	@Test
	public void testEqualsSameObject() {
		NutrientRoundingRule roundingRule = this.getDefaultRoundingRule();
		Assert.assertTrue(roundingRule.equals(roundingRule));
	}

	/**
	 * Tests the equals method for similar objects.
	 */
	@Test
	public void testEqualsSimilarObject() {
		NutrientRoundingRule roundingRule = this.getDefaultRoundingRule();
		Assert.assertTrue(roundingRule.equals(this.getDefaultRoundingRule()));
	}

	/**
	 * Tests equals when only the keys match.
	 */
	@Test
	public void equalsKeysOnly() {
		NutrientRoundingRule roundingRule = this.getDefaultRoundingRule();
		NutrientRoundingRule roundingRule2 = new NutrientRoundingRule();
		roundingRule2.setKey(this.getDefaultRoundingRuleKey());
		Assert.assertTrue(roundingRule.equals(roundingRule2));
	}

	/**
	 * Tests equals when the keys are different.
	 */
	@Test
	public void equalsDifferentKey(){
		NutrientRoundingRule roundingRule = this.getDefaultRoundingRule();
		NutrientRoundingRule roundingRule2 = new NutrientRoundingRule();
		NutrientRoundingRuleKey key = new NutrientRoundingRuleKey();
		key.setNutrientCode(5);
		key.setLowerBound(5);
		roundingRule.setKey(key);
		Assert.assertFalse(roundingRule.equals(roundingRule2));
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void equalsNull() {
		NutrientRoundingRule roundingRule = this.getDefaultRoundingRule();
		Assert.assertFalse(roundingRule == null);
	}

	/**
	 * Tests equals when passed an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		NutrientRoundingRule roundingRule = this.getDefaultRoundingRule();
		Assert.assertFalse(roundingRule.equals(roundingRule.getKey()));
	}

	/**
	 * Tests the hash code method.
	 */
	@Test
	public void testHashCode() {
		Assert.assertEquals(this.getDefaultRoundingRule().hashCode(), this.getDefaultRoundingRule().hashCode());
	}

	/**
	 * Tests the toString method.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals(this.getDefaultRoundingRule().toString(), "NutrientRoundingRule" +
				"{key=NutrientRoundingRuleKey{nutrientCode=1, lowerBound=0}, upperBound=2, incrementQuantity=0.0}");
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a default rounding rule to test with.
	 *
	 * @return a default rounding rule to test with.
	 */
	private NutrientRoundingRule getDefaultRoundingRule(){
		NutrientRoundingRule roundingRule = new NutrientRoundingRule();
		roundingRule.setKey(this.getDefaultRoundingRuleKey());
		roundingRule.setUpperBound(DEFAULT_UPPER_BOUND);
		roundingRule.setIncrementQuantity(DEFAULT_INCREMENT_QUANTITY);
		return roundingRule;
	}

	/**
	 * Returns a default rounding rule to test with.
	 *
	 * @return a default rounding rule to test with.
	 */
	private NutrientRoundingRuleKey getDefaultRoundingRuleKey(){
		NutrientRoundingRuleKey key = new NutrientRoundingRuleKey();
		key.setNutrientCode(DEFAULT_NUTRIENT_CODE);
		key.setLowerBound(DEFAULT_LOWER_BOUND);
		return key;
	}

}