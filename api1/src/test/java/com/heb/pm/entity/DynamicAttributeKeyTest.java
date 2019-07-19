/*
 * DynamicAttributeKeyTest
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
 * Tests DynamicAttributeKey.
 *
 * @author d116773
 * @since 2.0.7
 */
public class DynamicAttributeKeyTest {

	private static final int ID = 2342;
	private static final long KEY = 234234523L;
	private static final String KEY_TYPE = "dfakjewiorjklsdan";
	private static final int SEQUENCE_NUMBER = 3242334;
	private static final int SOURCE_SYSTEM = 32423;

	// Getters

	/**
	 * Tests getId.
	 */
	@Test
	public void getId() {
		Assert.assertEquals(ID, this.getTestKey().getAttributeId());
	}

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {
		Assert.assertEquals(KEY, this.getTestKey().getKey());
	}

	/**
	 * Tests getKeyType.
	 */
	@Test
	public void getKeyType() {
		Assert.assertEquals(KEY_TYPE, this.getTestKey().getKeyType());
	}

	/**
	 * Tests getSequenceNumber.
	 */
	@Test
	public void getSequenceNumber() {
		Assert.assertEquals(SEQUENCE_NUMBER, this.getTestKey().getSequenceNumber());
	}

	/**
	 * Tests getSourceSystem.
	 */
	@Test
	public void getSourceSystem() {
		Assert.assertEquals(SOURCE_SYSTEM, this.getTestKey().getSourceSystem());
	}

	// toString
	/**
	 * Test toString
	 */
	@Test
	public void test_toString() {
		Assert.assertEquals("DynamicAttributeKey{attributeId=2342, key=234234523, keyType='dfakjewiorjklsdan', sequenceNumber=3242334, sourceSystem=32423}",
				this.getTestKey().toString());
	}

	// Equals

	/**
	 * Tests equals on the same object.
	 */
	@Test
	public void equals_sameObject() {
		DynamicAttributeKey key = this.getTestKey();
		boolean eq = key.equals(key);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals on equal objects.
	 */
	@Test
	public void equals_equalObjects() {
		DynamicAttributeKey key1 = this.getTestKey();
		DynamicAttributeKey key2 = this.getTestKey();
		boolean eq = key1.equals(key2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals on a different type of object.
	 */
	@Test
	public void equals_differentObjectType() {
		DynamicAttributeKey key1 = this.getTestKey();
		boolean eq = key1.equals(Integer.valueOf(ID));
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void equals_null() {
		DynamicAttributeKey key1 = this.getTestKey();
		boolean eq = key1.equals(null);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when attributeId is different.
	 */
	@Test
	public void equals_differentId() {
		DynamicAttributeKey key1 = this.getTestKey();
		DynamicAttributeKey key2 = this.getTestKey();
		key2.setAttributeId(key2.getAttributeId() * 2);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when key is different.
	 */
	@Test
	public void equals_differentKey() {
		DynamicAttributeKey key1 = this.getTestKey();
		DynamicAttributeKey key2 = this.getTestKey();
		key2.setKey(key2.getKey() * 2);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when keyType is different.
	 */
	@Test
	public void equals_differentKeyType() {
		DynamicAttributeKey key1 = this.getTestKey();
		DynamicAttributeKey key2 = this.getTestKey();
		key2.setKeyType(key2.getKeyType() + " ");
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when sequenceNumber is different.
	 */
	@Test
	public void equals_differentSequenceNumber() {
		DynamicAttributeKey key1 = this.getTestKey();
		DynamicAttributeKey key2 = this.getTestKey();
		key2.setSequenceNumber(key2.getSequenceNumber() * 2);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when sourceSystem is different.
	 */
	@Test
	public void equals_differentSourceSystem() {
		DynamicAttributeKey key1 = this.getTestKey();
		DynamicAttributeKey key2 = this.getTestKey();
		key2.setSourceSystem(key2.getSourceSystem() * 2);
		boolean eq = key1.equals(key2);
		Assert.assertFalse(eq);
	}

	// hashCode

	/**
	 * Tests hashCode on equal objects.
	 */
	@Test
	public void hashCode_equalObjects() {
		DynamicAttributeKey key1 = this.getTestKey();
		DynamicAttributeKey key2 = this.getTestKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on unequal objects.
	 */
	@Test
	public void hashCode_unequalObjects() {

		DynamicAttributeKey key1 = this.getTestKey();
		DynamicAttributeKey key2 = this.getTestKey();

		key2.setAttributeId(key2.getAttributeId() * 2);
		key2.setKey(key2.getKey() * 2);
		key2.setKeyType(key2.getKeyType() + " ");
		key2.setSequenceNumber(key2.getSequenceNumber() * 2);
		key2.setSourceSystem(key2.getSourceSystem() * 2);

		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	// Support functions

	/**
	 * Returns a DynamicAttributeKey to test with.
	 *
	 * @return A DynamicAttributeKey to test with.
	 */
	private DynamicAttributeKey getTestKey() {

		DynamicAttributeKey key = new DynamicAttributeKey();

		key.setAttributeId(ID);
		key.setKey(KEY);
		key.setKeyType(KEY_TYPE);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setSourceSystem(SOURCE_SYSTEM);

		return key;
	}
}
