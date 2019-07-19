/*
 * WarehouseLocationItemKeyTest
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
 * Tests WarehouseLocationItemKey.
 *
 * @author d116773
 * @since 2.0.1
 */
public class WarehouseLocationItemKeyTest {

	private static final long DEFAULT_ITEM_CODE = 66L;
	private static final String DEFAULT_ITEM_TYPE = "ITMCD";
	private static final int DEFAULT_WAREHOUSE_NUMBER = 404;
	private static final String DEFAULT_WAREHOUSE_TYPE = "W";


	/*
	 * equals
	 */

	/**
	 * Test equals comparing the same object.
	 */
	@Test
	public void equalsSameObject() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		boolean equals = key.equals(key);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals comparing an object with the same values.
	 */
	@Test
	public void equalsSimilarObjects() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		boolean equals = key1.equals(key2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals comparing against null.
	 */
	@Test
	public void equalsNull() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		boolean equals = key.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on an object with a different item code.
	 */
	@Test
	public void equalsDifferentItemCode() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setItemCode(99L);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on an object with a different item type.
	 */
	@Test
	public void equalsDifferentItemType() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setItemType("XXXXX");
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on an object with a different warehouse number.
	 */
	@Test
	public void equalsDifferentWarehouse() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setWarehouseNumber(8);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on an object with a different warehouse type.
	 */
	@Test
	public void equalsDifferentWarehouseType() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setWarehouseType("vvv");
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the item type of the left object is null and the other is not.
	 */
	@Test
	public void equalsNullItemType() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key1.setItemType(null);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the item type of both objects is null.
	 */
	@Test
	public void equalsNullItemTypeBoth() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		key1.setItemType(null);
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setItemType(null);
		boolean equals = key1.equals(key2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals when the warehouse type of the left object is null and the other is not.
	 */
	@Test
	public void equalsNullWarehouseType() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key1.setWarehouseType(null);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the warehouse type of both objects is null.
	 */
	@Test
	public void equalsNullWarehouseTypeBoth() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		key1.setWarehouseType(null);
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setWarehouseType(null);
		boolean equals = key1.equals(key2);
		Assert.assertTrue(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests to make sure hashCode returns a consistent value.
	 */
	@Test
	public void hashCodeSameObject() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}

	/**
	 * Tests to make sure two objects with the same values return the same hash code.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on objects with different item codes return different values.
	 */
	@Test
	public void hashCodeDifferentItemCode() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setItemCode(99L);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on objects with different item types return different values.
	 */
	@Test
	public void hashCodeDifferentItemType() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setItemType("XXXXX");
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on objects with different warehouses return different values.
	 */
	@Test
	public void hashCodeDifferentWarehouse() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setWarehouseNumber(2);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on objects with different warehouse types return different values.
	 */
	@Test
	public void hashCodeDifferentWarehouseType() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key2.setWarehouseType("vvv");
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode when the item type of the left object is null and the other is not return different values.
	 */
	@Test
	public void hashCodeNullItemType() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key1.setItemType(null);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode when the warehouse type of the left object is null and the other is not return different values.
	 */
	@Test
	public void hashCodeNullWarehouseType() {
		WarehouseLocationItemKey key1 = this.getDefaultKey();
		WarehouseLocationItemKey key2 = this.getDefaultKey();
		key1.setWarehouseType(null);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		Assert.assertEquals(
				"WarehouseLocationItemKey{itemType='ITMCD', itemCode=66, warehouseType=W, warehouseNumber=404}",
				key.toString());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getItemCode.
	 */
	@Test
	public void getItemCode() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		Assert.assertEquals(WarehouseLocationItemKeyTest.DEFAULT_ITEM_CODE, key.getItemCode());
	}

	/**
	 * Tests getItemType.
	 */
	@Test
	public void getItemType() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		Assert.assertEquals(WarehouseLocationItemKeyTest.DEFAULT_ITEM_TYPE, key.getItemType());
	}

	/**
	 * Tests getWarehouseNumber.
	 */
	@Test
	public void getWarehouseNumber() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		Assert.assertEquals(WarehouseLocationItemKeyTest.DEFAULT_WAREHOUSE_NUMBER, key.getWarehouseNumber());
	}

	/**
	 * Tests getWarehouseType.
	 */
	@Test
	public void getWarehouseType() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		Assert.assertEquals(WarehouseLocationItemKeyTest.DEFAULT_WAREHOUSE_TYPE, key.getWarehouseType());
	}


	/*
	 * Support functions
	 */

	/**
	 * Returns the basic key to test against.
	 *
	 * @return The basic key to test against.
	 */
	private WarehouseLocationItemKey getDefaultKey() {
		WarehouseLocationItemKey key = new WarehouseLocationItemKey();
		key.setItemType(WarehouseLocationItemKeyTest.DEFAULT_ITEM_TYPE);
		key.setItemCode(WarehouseLocationItemKeyTest.DEFAULT_ITEM_CODE);
		key.setWarehouseNumber(WarehouseLocationItemKeyTest.DEFAULT_WAREHOUSE_NUMBER);
		key.setWarehouseType(WarehouseLocationItemKeyTest.DEFAULT_WAREHOUSE_TYPE);
		return key;
	}
}
