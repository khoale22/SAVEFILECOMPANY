package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Unit tests for the Item Master Audit Key entity
 * @author  s753601
 * @version 2.8.0
 */
public class ItemMasterAuditKeyTest {

	private static final String DEFAULT_KEY_TYPE="ITMCD";
	private static final long DEFAULT_KEY_CODE=1L;


	/**
	 * Tests the getChangedOn method
	 */
	@Test
	public void getChangedOnTest() {
		LocalDateTime test = LocalDateTime.now();
		Assert.assertTrue(getDefaultKey().getChangedOn() instanceof LocalDateTime);
	}

	/**
	 * Tests the setChangedOn method
	 */
	@Test
	public void setChangedOnTest() {
		LocalDateTime test = LocalDateTime.now();
		ItemMasterAuditKey key = getDefaultKey();
		key.setChangedOn(test);
		Assert.assertEquals(test, key.getChangedOn());
	}

	/**
	 * Tests the getItemCode method
	 */
	@Test
	public void getItemCodeTest() {
		Assert.assertEquals(new Long(DEFAULT_KEY_CODE), getDefaultKey().getItemCode());
	}

	/**
	 * Tests the setItemCode method
	 */
	@Test
	public void setItemCodeTest() {
		long test = 2L;
		ItemMasterAuditKey key = getDefaultKey();
		key.setItemCode(test);
		Assert.assertEquals(new Long(test), key.getItemCode());
	}

	/**
	 * Tests the getItemType method
	 */
	@Test
	public void getItemTypeTest() {
		Assert.assertEquals(DEFAULT_KEY_TYPE, getDefaultKey().getItemType());
	}

	/**
	 * Tests the setItemType method
	 */
	@Test
	public void setItemTypeTest() {
		String test = "TEST";
		ItemMasterAuditKey key = getDefaultKey();
		key.setItemType(test);
		Assert.assertEquals(test, key.getItemType());
	}

	/**
	 * Test equals comparing the same object.
	 */
	@Test
	public void equalsSameObject() {
		ItemMasterAuditKey key = this.getDefaultKey();
		boolean equals = key.equals(key);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals comparing an object with the same values.
	 */
	@Test
	public void equalsSimilarObjects() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		boolean equals = key1.equals(key2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals comparing against null.
	 */
	@Test
	public void equalsNull() {
		ItemMasterAuditKey key = this.getDefaultKey();
		boolean equals = key.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on an object with a different item code.
	 */
	@Test
	public void equalsDifferentItemCode() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key2.setItemCode(99L);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on an object with a different item type.
	 */
	@Test
	public void equalsDifferentItemType() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key2.setItemType("XXXXX");
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}


	/**
	 * Test equals on an object with a different warehouse type.
	 */
	@Test
	public void equalsDifferentWarehouseType() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key2.setChangedOn(LocalDateTime.MAX);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the item type of the left object is null and the other is not.
	 */
	@Test
	public void equalsNullItemType() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key1.setItemType(null);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the item type of both objects is null.
	 */
	@Test
	public void equalsNullItemTypeBoth() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		key1.setItemType(null);
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key2.setItemType(null);
		boolean equals = key1.equals(key2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals when the warehouse type of the left object is null and the other is not.
	 */
	@Test
	public void equalsNullChangedOn() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key1.setChangedOn(null);
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when the warehouse type of both objects is null.
	 */
	@Test
	public void equalsNullChangedOnBoth() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		key1.setChangedOn(null);
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key2.setChangedOn(null);
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
		ItemMasterAuditKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}

	/**
	 * Tests to make sure two objects with the same values return the same hash code.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on objects with different item codes return different values.
	 */
	@Test
	public void hashCodeDifferentItemCode() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key2.setItemCode(99L);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode on objects with different item types return different values.
	 */
	@Test
	public void hashCodeDifferentItemType() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key2.setItemType("XXXXX");
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}


	/**
	 * Test hashCode when the item type of the left object is null and the other is not return different values.
	 */
	@Test
	public void hashCodeNullItemType() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key1.setItemType(null);
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Test hashCode when the warehouse type of the left object is null and the other is not return different values.
	 */
	@Test
	public void hashCodeNullChangedOn() {
		ItemMasterAuditKey key1 = this.getDefaultKey();
		ItemMasterAuditKey key2 = this.getDefaultKey();
		key1.setChangedOn(null);
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
		ItemMasterAuditKey key = this.getDefaultKey();
		Assert.assertEquals(
				"ItemMasterAuditKey{itemType='ITMCD', itemCode=1', changedOn=" + key.getChangedOn() + "}",
				key.toString());
	}

	/**
	 * Returns a default key for testing
	 * @return a default key for testing
	 */
	private ItemMasterAuditKey getDefaultKey(){
		ItemMasterAuditKey key = new ItemMasterAuditKey();
		key.setItemType(DEFAULT_KEY_TYPE);
		key.setItemCode(DEFAULT_KEY_CODE);
		key.setChangedOn(LocalDateTime.now());
		return key;
	}
}