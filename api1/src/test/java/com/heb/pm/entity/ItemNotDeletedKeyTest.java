/*
 *  com.heb.pm.entity.ItemMasterRepositoryTest
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
 * Test for ItemNotDeletedKey entity class.
 *
 * @author vn40486
 * @since 2.0.4
 */
public class ItemNotDeletedKeyTest {

	//DSD item code
	public static final String DSD = "DSD";
	//Warehouse item code
	public static final String WAREHOUSE = "ITMCD";
	//item not deleted reason code - Alternate Pack
	public static final String ITM_NOT_DELD_RSN_CD_1 = "ALTP";

	/*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		ItemNotDeletedKey item = this.getDefaultRecord();
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject(){
		ItemNotDeletedKey item1 = this.getDefaultRecord();
		ItemNotDeletedKey item2 = this.getDefaultRecord();
		boolean equals = item1.equals(item2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on objects with different keys.
	 */
	@Test
	public void equalsDifferentKeys() {
		ItemNotDeletedKey item1 = this.getDefaultRecord();
		ItemNotDeletedKey item2 = this.getDSDRecord();
		boolean equals = item1.equals(item2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		ItemNotDeletedKey item = this.getDefaultRecord();
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		ItemNotDeletedKey item = this.getDefaultRecord();
		boolean equals = item.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}


	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		ItemNotDeletedKey item1 = new ItemNotDeletedKey();
		ItemNotDeletedKey item2 = this.getDefaultRecord();
		boolean equals = item1.equals(item2);
		Assert.assertFalse(equals);
	}


	/**
	 * Test hashCode on the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		ItemNotDeletedKey item = this.getDefaultRecord();
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ItemNotDeletedKey item1 = this.getDefaultRecord();
		ItemNotDeletedKey item2 = this.getDefaultRecord();
		Assert.assertEquals(item1.hashCode(), item2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		ItemNotDeletedKey item1 = this.getDefaultRecord();
		ItemNotDeletedKey item2 = this.getDSDRecord();
		Assert.assertNotEquals(item1.hashCode(), item2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ItemMaster item = new ItemMaster();
		Assert.assertEquals(0, item.hashCode());
	}

	/**
	 * TODO:
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ItemNotDeletedKey pm = this.getDSDRecord();
		Assert.assertEquals("ItemNotDeletedKey{itemCode=66, itemType='DSD', notDeletedReasonCode='ALTP'}", pm.toString());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns an ItemNotDeletedKey object that equals the one that is first in the test table.
	 *
	 * @return An ItemNotDeletedKey object that equals the one that is first in the test table.
	 */
	private ItemNotDeletedKey getDefaultRecord() {
		ItemNotDeletedKey key = new ItemNotDeletedKey();
		key.setItemCode(66L);
		key.setItemType(ItemNotDeletedKeyTest.WAREHOUSE);
		key.setNotDeletedReasonCode(ItemNotDeletedKeyTest.ITM_NOT_DELD_RSN_CD_1);
		return key;
	}

	/**
	 * Returns an DSD ItemNotDeletedKey object .
	 *
	 * @return An ItemNotDeletedKey object a default ItemNotDeletedKey Object.
	 */
	private ItemNotDeletedKey getDSDRecord() {
		ItemNotDeletedKey key = new ItemNotDeletedKey();
		key.setItemCode(66L);
		key.setItemType(ItemNotDeletedKeyTest.DSD);
		key.setNotDeletedReasonCode(ItemNotDeletedKeyTest.ITM_NOT_DELD_RSN_CD_1);
		return key;
	}


	/**
	 * Test getItemCode.
	 */
	@Test
	public void getItemCode() {
		ItemNotDeletedKey itemNotDeletedKey = this.getDefaultRecord();
		Assert.assertEquals(itemNotDeletedKey.getItemCode(), 66L);
	}

	/**
	 * Test getItemType.
	 */
	@Test
	public void getItemType() {
		ItemNotDeletedKey itemNotDeletedKey = this.getDefaultRecord();
		Assert.assertEquals(itemNotDeletedKey.getItemType(), ItemNotDeletedKeyTest.WAREHOUSE);
	}


	/**
	 * Test getNotDeletedReasonCode.
	 */
	@Test
	public void getNotDeletedReasonCode() {
		ItemNotDeletedKey itemNotDeletedKey = this.getDefaultRecord();
		Assert.assertEquals(itemNotDeletedKey.getNotDeletedReasonCode(), ItemNotDeletedKeyTest.ITM_NOT_DELD_RSN_CD_1);
	}

}
