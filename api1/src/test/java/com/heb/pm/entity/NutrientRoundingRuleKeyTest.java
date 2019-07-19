/*
 *  NutrientRoundingRuleKeyTest
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
 * Tests the NutrientRoundingRuleKey.
 *
 * @author s573181
 * @since 2.1.0
 */
public class NutrientRoundingRuleKeyTest {

	private static final int DEFAULT_NUTRIENT_CODE = 1;
	private static final int DEFAULT_LOWER_BOUND = 0;
	/**
	 * Tests getNutrientCode method.
	 */
	@Test
	public void testGetNutrientCode() {
		NutrientRoundingRuleKey key = this.getDefaultRoundingRuleKey();
		Assert.assertEquals(key.getNutrientCode(), DEFAULT_NUTRIENT_CODE);
	}

	/**
	 * Tests getLowerBound method.
	 */
	@Test
	public void testGetLowerBound() {
		NutrientRoundingRuleKey key = this.getDefaultRoundingRuleKey();
		Assert.assertEquals(key.getLowerBound(), DEFAULT_LOWER_BOUND);
	}


	/*
	 * equals
	 */

	/**
	 * Tests the equals method on itself.
	 */
	@Test
	public void testEqualsSameObject() {
		NutrientRoundingRuleKey key = this.getDefaultRoundingRuleKey();
		Assert.assertTrue(key.equals(key));
	}

	/**
	 * Tests the equals method for similar objects.
	 */
	@Test
	public void testEqualsSimilarObject() {
		NutrientRoundingRuleKey key = this.getDefaultRoundingRuleKey();
		Assert.assertTrue(key.equals(this.getDefaultRoundingRuleKey()));
	}

	/**
	 * Tests the hash code method.
	 */
	@Test
	public void testHashCode() {
		Assert.assertEquals(this.getDefaultRoundingRuleKey().hashCode(), this.getDefaultRoundingRuleKey().hashCode());
	}
	/**
	 * Tests the toString method.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals(this.getDefaultRoundingRuleKey().toString(), "NutrientRoundingRuleKey{nutrientCode=1, " +
				"lowerBound=0}");
	}

	/**
	/*
	 * Support functions
	 */

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
