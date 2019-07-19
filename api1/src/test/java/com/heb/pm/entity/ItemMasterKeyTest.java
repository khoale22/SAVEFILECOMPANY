/*
 *  com.heb.pm.entity.ItemMasterKeyTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests ItemMasterKey.
 *
 * @author d116773
 */
public class ItemMasterKeyTest {

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void testEqualsSameObject() {
		ItemMasterKey key = this.getDefaultKey();
		boolean equal = key.equals(key);
		Assert.assertTrue("same object not equal", equal);
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsSimilarObject() {
		ItemMasterKey key1 = this.getDefaultKey();
		ItemMasterKey key2 = this.getDefaultKey();
		boolean equal = key1.equals(key2);
		Assert.assertTrue(equal);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void testEqualsNull() {
		ItemMasterKey key = this.getDefaultKey();
		boolean equal = key.equals(null);
		Assert.assertFalse(equal);
	}

	/**
	 * Tests equals when passed an object with a different item code but the same type.
	 */
	@Test
	public void testEqualsOtherItemCode() {
		ItemMasterKey key1 = this.getDefaultKey();
		ItemMasterKey key2 = this.getAlternateItemCode();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object with the same item code but a different type.
	 */
	@Test
	public void testEqualsDifferentType() {
		ItemMasterKey key1 = this.getDefaultKey();
		ItemMasterKey key2 = this.getAlternateDsd();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object with a different item code and different type.
	 */
	@Test
	public void testEqualsDifferentBoth() {
		ItemMasterKey key1 = this.getDefaultKey();
		ItemMasterKey key2 = this.getUpc();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode returns the same value for the same object.
	 */
	@Test
	public void testHashCodeSameObject() {
		ItemMasterKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}

	/**
	 * Tests hashCode returns the same value for different objects with the same values.
	 */
	@Test
	public void testHashCodeSimilarObject() {
		ItemMasterKey key1 = this.getDefaultKey();
		ItemMasterKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same type but different item codes.
	 */
	@Test
	public void testHashCodeDifferentItemCode() {
		ItemMasterKey key1 = this.getDefaultKey();
		ItemMasterKey key2 = this.getAlternateItemCode();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns a different value for objects with the same item code but different types.
	 */
	@Test
	public void testHashCodeDifferentType() {
		ItemMasterKey key1 = this.getDefaultKey();
		ItemMasterKey key2 = this.getAlternateDsd();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns a different value for objects with different item codes and types.
	 */
	@Test
	public void testHashCodeDifferentBoth() {
		ItemMasterKey key1 = this.getDefaultKey();
		ItemMasterKey key2 = this.getUpc();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/*
	 * isDsd
	 */
	/**
	 * Test isDsd on a DSD product.
	 */
	@Test
	public void isDsdDsd() {
		ItemMasterKey key = this.getAlternateDsd();
		Assert.assertTrue(key.isDsd());
	}

	/**
	 * Tests isDsd on a warehouse product.
	 */
	@Test
	public void isDsdWarehouse() {
		ItemMasterKey key = this.getDefaultKey();
		Assert.assertFalse(key.isDsd());
	}

	/*
	 * isWarehouse
	 */
	/**
	 * Tests isWarehouse on a DSD product.
	 */
	@Test
	public void isWarehouseDsd() {
		ItemMasterKey key = this.getAlternateDsd();
		Assert.assertFalse(key.isWarehouse());
	}

	/**
	 * Tests isWarehouse on a warehouse product.
	 */
	@Test
	public void isWarehouseWarehouse() {
		ItemMasterKey key = this.getDefaultKey();
		Assert.assertTrue(key.isWarehouse());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getItemCode.
	 */
	@Test
	public void testGetItemCode() {
		ItemMasterKey key = this.getDefaultKey();
		Assert.assertEquals(key.getItemCode(), new Long(66L));
	}

	/**
	 * Tests getItemType.
	 */
	@Test
	public void testGetItemType() {
		ItemMasterKey key = this.getDefaultKey();
		Assert.assertEquals(key.getItemType(), "ITMCD");
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ItemMasterKey key = this.getDefaultKey();
		Assert.assertEquals("ItemMasterKey{itemType='ITMCD', itemCode=66}", key.toString());
	}
	/*
	 * Support functions.
	 */

	/**
	 * Returns the default test value.
	 *
	 * @return The default test value.
	 */
	private ItemMasterKey getDefaultKey() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(66L);
		key.setItemType(ItemMasterKey.WAREHOUSE);
		return key;
	}

	/***
	 * Returns an ItemCodeKey with the same type as the default but a different item code.
	 *
	 * @return An ItemCodeKey with the same type as the default but a different item code.
	 */
	private ItemMasterKey getAlternateItemCode() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(86L);
		key.setItemType(ItemMasterKey.WAREHOUSE);
		return key;
	}

	/**
	 * Returns an ItemCodeKey with the same item code as the default but a different type.
	 *
	 * @return An ItemCodeKey with the same item code as the default but a different type
	 */
	private ItemMasterKey getAlternateDsd() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(66L);
		key.setItemType(ItemMasterKey.DSD);
		return key;
	}

	/**
	 * Returns an ItemCodeKey with a different item code and type from the default.
	 *
	 * @return An ItemCodeKey with a different item code and type from the default.
	 */
	private ItemMasterKey getUpc() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(9020215100L);
		key.setItemType(ItemMasterKey.DSD);
		return key;
	}
}
