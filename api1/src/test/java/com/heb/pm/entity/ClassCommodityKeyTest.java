/*
 * ClassCommodityKeyTest
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
 * Tests ClassCommodityKey.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ClassCommodityKeyTest {

	private static final int CLASS_CODE = 34435;
	private static final int COMMODITY_CODE = 23432;

	/*
	 * equals
	 */
	/**
	 * Tests equals when passed the object itself.
	 */
	@Test
	public void equalsSameObject() {
		ClassCommodityKey key = this.getTestKey();
		boolean eq = key.equals(key);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals of equal objects.
	 */
	@Test
	public void equalsEqualObjects() {
		ClassCommodityKey key1 = this.getTestKey();
		ClassCommodityKey key2 = this.getTestKey();
		boolean eq = key1.equals(key2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when the class code has changed.
	 */
	@Test
	public void equalsDifferentClassCode() {
		ClassCommodityKey key1 = this.getTestKey();
		ClassCommodityKey key2 = this.getTestKey();
		key2.setClassCode(key2.getClassCode() + 1);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when the commodity code has changed.
	 */
	@Test
	public void equalsDifferentCommodityCode() {
		ClassCommodityKey key1 = this.getTestKey();
		ClassCommodityKey key2 = this.getTestKey();
		key2.setCommodityCode(key2.getCommodityCode() - 1);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when padded null.
	 */
	@Test
	public void equalsNull() {
		ClassCommodityKey key = this.getTestKey();
		boolean eq = key.equals(null);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed a different type of class.
	 */
	@Test
	public void equalsDifferentClass() {
		ClassCommodityKey key = this.getTestKey();
		boolean eq = key.equals(Integer.valueOf(ClassCommodityKeyTest.CLASS_CODE));
		Assert.assertFalse(eq);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode is consistent across calls.
	 */
	@Test
	public void hashCodeSameObject() {
		ClassCommodityKey key = this.getTestKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}

	/**
	 * Tests hashCode returns the same value for equal objects.
	 */
	@Test
	public void hashCodeEqualObjects() {
		ClassCommodityKey key1 = this.getTestKey();
		ClassCommodityKey key2 = this.getTestKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode when the class code has changed.
	 */
	@Test
	public void hashCodeDifferentClassCode() {
		ClassCommodityKey key1 = this.getTestKey();
		ClassCommodityKey key2 = this.getTestKey();
		key2.setClassCode(key2.getClassCode() + 1);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode when the commodity code has changed.
	 */
	@Test
	public void hashCodeDifferentCommodityCode() {
		ClassCommodityKey key1 = this.getTestKey();
		ClassCommodityKey key2 = this.getTestKey();
		key2.setCommodityCode(key2.getCommodityCode() - 1);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getClass.
	 */
	@Test
	public void getClassCode() {
		ClassCommodityKey key = this.getTestKey();
		Assert.assertEquals(ClassCommodityKeyTest.CLASS_CODE, (int) key.getClassCode());
	}

	/**
	 * Tests getCommodityCode.
	 */
	@Test
	public void getCommodityCode() {
		ClassCommodityKey key = this.getTestKey();
		Assert.assertEquals(ClassCommodityKeyTest.COMMODITY_CODE, (int) key.getCommodityCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString
	 */
	@Test
	public void testToString() {
		ClassCommodityKey key = this.getTestKey();
		Assert.assertEquals("ClassCommodityKey{classCode=34435, commodityCode=23432}", key.toString());
	}

	/*
	 * Support functions
	 */
	private ClassCommodityKey getTestKey() {

		ClassCommodityKey key = new ClassCommodityKey();
		key.setClassCode(ClassCommodityKeyTest.CLASS_CODE);
		key.setCommodityCode(ClassCommodityKeyTest.COMMODITY_CODE);
		return key;
	}
}
