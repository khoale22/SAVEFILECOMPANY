/*
 *  IngredientCategoryTest
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
 * Test IngredientCategory entity.
 *
 * @author s573181
 * @since 2.1.0
 */
public class IngredientCategoryTest {

	private IngredientCategory category = this.getDefaultIngredientCategory();


	/*
	 * equals
	 */
	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void testEqualsSameObject() {
		Assert.assertTrue("same object not equal", category.equals(category));
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsSimilarObject() {
		IngredientCategory category2 = this.getDefaultIngredientCategory();
		boolean equal = category.equals(category2);
		Assert.assertTrue(equal);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void testEqualsNull() {
		Assert.assertFalse(category.equals(null));
	}

	/**
	 * Tests equals when passed an object with a different item code but the same type.
	 */
	@Test
	public void testEqualsOtherItemCode() {
		IngredientCategory category2 = this.getAlternateIngredientCategory();
		boolean equals = category.equals(category2);
		Assert.assertFalse(equals);
	}

	/*
	 * getters
	 */

	/**
	 * Tests getItemCode.
	 */
	@Test
	public void testGetCategoryCode() {
		Assert.assertEquals(category.getCategoryCode(), 3L);
	}

	/**
	 * Tests getItemCode.
	 */
	@Test
	public void testGetCategoryDescription() {
		Assert.assertEquals(category.getCategoryDescription(), "BREADS");
	}

	/**
	 * Tests getDisplayText.
	 */
	@Test
	public void getDisplayText(){
		Assert.assertEquals(category.getDisplayText(), "3-BREADS");
	}

	/**
	 * Tests getNormalizedId.
	 */
	@Test
	public void getNormalizedId(){
		Assert.assertEquals(category.getNormalizedId(), "3");
	}

	/**
	 * Tests getDisplayName.
	 */
	@Test
	public void getDisplayName(){
		Assert.assertEquals(category.getDisplayName(), "BREADS[3]");
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode returns the same value for the same object.
	 */

	/**
	 * Tests hashCode returns the same value for different objects with the same values.
	 */
	@Test
	public void testHashCodeSimilarObject() {
		IngredientCategory category2 = this.getDefaultIngredientCategory();
		Assert.assertEquals(category.hashCode(), category2.hashCode());
	}


	@Test
	public void testHashCodeSameObject() {
		Assert.assertEquals(category.hashCode(), category.hashCode());
	}

	/**
	 * Tests hashCode returns different values for different objects.
	 */
	@Test
	public void testHashCodeDifferentObject() {
		IngredientCategory category2 = this.getAlternateIngredientCategory();
		Assert.assertNotEquals(category.hashCode(), category2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals("IngredientCategory{categoryCode=3, categoryDescription='BREADS'}", category.toString());
	}

	/**
	 * Support functions.
	 */

	/**
	 * Returns the default test value.
	 *
	 * @return The default test value.
	 */
	private IngredientCategory getDefaultIngredientCategory(){
		IngredientCategory category = new IngredientCategory();
		category.setCategoryCode(3);
		category.setCategoryDescription("BREADS");
		return category;
	}

	/**
	 * Returns an alternate test value.
	 *
	 * @return The an alternate value.
	 */
	private IngredientCategory getAlternateIngredientCategory(){
		IngredientCategory category = new IngredientCategory();
		category.setCategoryCode(4);
		category.setCategoryDescription("BASES");
		return category;
	}


}
