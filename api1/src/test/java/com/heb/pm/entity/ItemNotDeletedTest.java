package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the ItemNotDeleted class.
 *
 * @author vn40486
 * @since 2.0.4
 */
public class ItemNotDeletedTest {

	//DSD item code.
	public static final String DSD = "DSD";

	//Warehouse item code.
	public static final String WAREHOUSE = "ITMCD";

	//item not deleted reason code - Alternate Pack.
	public static final String ITM_NOT_DELD_RSN_CD_1 = "ALTP";

	/*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		ItemNotDeleted item = this.getDefaultRecord();
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject(){
		ItemNotDeleted item1 = this.getDefaultRecord();
		ItemNotDeleted item2 = this.getDefaultRecord();
		boolean equals = item1.equals(item2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on objects with different keys.
	 */
	@Test
	public void equalsDifferentKeys() {
		ItemNotDeleted item1 = this.getDefaultRecord();
		ItemNotDeleted item2 = this.getDSDRecord();
		boolean equals = item1.equals(item2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		ItemNotDeleted item = this.getDefaultRecord();
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		ItemNotDeleted item = this.getDefaultRecord();
		boolean equals = item.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}


	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		ItemNotDeleted item1 = new ItemNotDeleted();
		ItemNotDeleted item2 = this.getDefaultRecord();
		boolean equals = item1.equals(item2);
		Assert.assertFalse(equals);
	}


	/**
	 * Test hashCode on the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		ItemNotDeleted item = this.getDefaultRecord();
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ItemNotDeleted item1 = this.getDefaultRecord();
		ItemNotDeleted item2 = this.getDefaultRecord();
		Assert.assertEquals(item1.hashCode(), item2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		ItemNotDeleted item1 = this.getDefaultRecord();
		ItemNotDeleted item2 = this.getDSDRecord();
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
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		ItemNotDeleted item1 = this.getDefaultRecord();
		ItemNotDeleted item2 = new ItemNotDeleted();
		Assert.assertNotEquals(item1.hashCode(), item2.hashCode());
	}

	/**
	 * TODO:
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ItemNotDeleted item = this.getDSDRecord();
		Assert.assertEquals("ItemNotDeleted{key=ItemNotDeletedKey{itemCode=66, itemType='DSD', " +
						"notDeletedReasonCode='ALTP'}, lastUpdateTime=null, itemMaster=null, itemNotDeletedReason=null}",
				item.toString());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns an ItemNotDeleted object that equals the one that is first in the test table.
	 *
	 * @return An ItemNotDeleted object that equals the one that is first in the test table.
	 */
	private ItemNotDeleted getDefaultRecord() {
		ItemNotDeletedKey key = new ItemNotDeletedKey();
		key.setItemCode(66L);
		key.setItemType(ItemNotDeletedTest.WAREHOUSE);
		key.setNotDeletedReasonCode(ItemNotDeletedTest.ITM_NOT_DELD_RSN_CD_1);
		ItemNotDeleted itemNotDeleted = new ItemNotDeleted();
		itemNotDeleted.setKey(key);
		return itemNotDeleted;
	}

	/**
	 * Returns an DSD ItemNotDeleted object .
	 *
	 * @return An ItemNotDeleted object a default ItemNotDeleted Object.
	 */
	private ItemNotDeleted getDSDRecord() {
		ItemNotDeletedKey key = new ItemNotDeletedKey();
		key.setItemCode(66L);
		key.setItemType(ItemNotDeletedTest.DSD);
		key.setNotDeletedReasonCode(ItemNotDeletedTest.ITM_NOT_DELD_RSN_CD_1);
		ItemNotDeleted itemNotDeleted = new ItemNotDeleted();
		itemNotDeleted.setKey(key);
		return itemNotDeleted;
	}

}
