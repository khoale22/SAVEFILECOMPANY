/*
 * WarehouseLocationItemTest
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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import java.time.LocalDateTime;

/**
 * Tests WarehouseLocationItem.
 *
 * @author d116773
 * @since 2.0.1
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class WarehouseLocationItemTest {

	private static final long DEFAULT_ITEM_CODE = 12282L;
	private static final String DEFAULT_ITEM_TYPE = "ITMCD";
	private static final int DEFAULT_WAREHOUSE_NUMBER = 404;
	private static final String DEFAULT_WAREHOUSE_TYPE = "W ";
	private static final String DEFAULT_PURCHASING_STATUS =
			PurchasingStatusCode.CodeValues.ACTIVE.getPurchasingStatus();
	private static final String DEFAULT_ITEM_DESCRIPTION = "1:24 NEED FOR SPEED VEHICLES  ";
	private static final LocalDateTime DEFAULT_PURCHASING_SATUS_CHANGE = LocalDateTime.of(2014, 4, 21, 15, 27, 20);
	private static final int BILLABLE_QUANTITY = 35;
	private static final int TOTAL_ON_HAND = 55;
	private static final int OFFSITE_INVENTORY = 1;
	private static final int ON_HOLD_INVENTORY = 10;
	private static final int DISTRIBUTION_RESERVE = 9;


	@Autowired
	private WarehouseLocationItemRepositoryTest repository;

	/*
	 * equals
	 */

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		WarehouseLocationItem item = this.getDefaultRecord();
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		WarehouseLocationItem i1 = this.getDefaultRecord();
		WarehouseLocationItem i2 = this.getDefaultRecord();
		boolean equals = i1.equals(i2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		WarehouseLocationItem item = this.getDefaultRecord();
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		WarehouseLocationItem item = this.getDefaultRecord();
		boolean equals = item.equals(Integer.valueOf(0));
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		WarehouseLocationItem item = this.getDefaultRecord();
		WarehouseLocationItem i2 = new WarehouseLocationItem();
		boolean equals = item.equals(i2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		WarehouseLocationItem item = new WarehouseLocationItem();
		WarehouseLocationItem i1 = this.getDefaultRecord();
		boolean equals = item.equals(i1);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		WarehouseLocationItem item = new WarehouseLocationItem();
		WarehouseLocationItem i1 = new WarehouseLocationItem();
		boolean equals = item.equals(i1);
		Assert.assertTrue(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Check hashCode is consistent on the same object.
	 */
	@Test
	public void hashCodeSelf() {
		WarehouseLocationItem item = this.getDefaultRecord();
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		WarehouseLocationItem i1 = this.getDefaultRecord();
		WarehouseLocationItem i2 = this.getDefaultRecord();
		Assert.assertEquals(i1.hashCode(), i2.hashCode());
	}

	@Test
	public void hashCodeDifferentObjects() {
		WarehouseLocationItem i1 = this.getDefaultRecord();
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		WarehouseLocationItem item = new WarehouseLocationItem();
		Assert.assertEquals(0, item.hashCode());
	}

	/*
	 * mapping
	 */

	/**
	 * Tests that the JPA mapping is correct.
	 */
	@Test
	public void mapping() {
		WarehouseLocationItemKey key = this.getDefaultKey();
		WarehouseLocationItem item = this.repository.findOne(key);
		this.fullCompare(this.getDefaultRecord(), item);
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		WarehouseLocationItem item = this.getDefaultRecord();
		Assert.assertEquals("WarehouseLocationItem{key=WarehouseLocationItemKey{itemType='ITMCD', itemCode=12282, " +
						"warehouseType=W , warehouseNumber=404}, purchasingStatus='A    '," +
						" purchaseStatusUpdateTime=2014-04-21T15:27:20, distributionReserveInventory=9, " +
						"offsiteInventory=1, totalOnHandInventory=55, onHoldInventory=10, billableInventory=35, " +
						"discontinueDate=null, discontinuedByUID='null'}",
				item.toString());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(this.getDefaultKey(), warehouseLocationItem.getKey());
	}

	/**
	 * Tests getPurchasingStatus.
	 */
	@Test
	public void getPurchasingStatus() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemTest.DEFAULT_PURCHASING_STATUS,
				warehouseLocationItem.getPurchasingStatus());
	}

	/**
	 * Test getItemMaster.
	 */
	@Test
	public void getItemMaster() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(getDefaultItemMaster(), warehouseLocationItem.getItemMaster());
	}

	/**
	 * Tests getPurchaseStatusUpdateTime.
	 */
	@Test
	public void getPurchaseStatusUpdateTime() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemTest.DEFAULT_PURCHASING_SATUS_CHANGE,
				warehouseLocationItem.getPurchaseStatusUpdateTime());
	}

	/**
	 * Tests getDistributionReserveInventory.
	 */
	@Test
	public void getDistributionReserveInventory() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemTest.DISTRIBUTION_RESERVE,
				warehouseLocationItem.getDistributionReserveInventory());
	}

	/**
	 * Tests getOffsiteInventory.
	 */
	@Test
	public void getOffsiteInventory() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemTest.OFFSITE_INVENTORY,
				warehouseLocationItem.getOffsiteInventory());
	}

	/**
	 * Tests getTotalOnHandInventory.
	 */
	@Test
	public void getTotalOnHandInventory() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemTest.TOTAL_ON_HAND, warehouseLocationItem.getTotalOnHandInventory());
	}

	/**
	 * Tests getOnHoldInventory.
	 */
	@Test
	public void getOnHoldInventory() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemTest.ON_HOLD_INVENTORY, warehouseLocationItem.getOnHoldInventory());
	}

	/**
	 * Tests getBillableInventory.
	 */
	@Test
	public void getBillableInventory() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemTest.BILLABLE_QUANTITY, warehouseLocationItem.getBillableInventory());
	}

	/*
	 * isDiscontinued
	 */

	/**
	 * Tests isDiscontinued on a discontinued item.
	 */
	@Test
	public void isDiscontinuedYes() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		warehouseLocationItem.setPurchasingStatus(
				PurchasingStatusCode.CodeValues.DISCONTINUED.getPurchasingStatus());
		Assert.assertTrue(warehouseLocationItem.isDiscontinued());
	}

	/**
	 * Tests isDiscontinued on an active item.
	 */
	@Test
	public void isDiscontinuedNo() {
		WarehouseLocationItem warehouseLocationItem = this.getDefaultRecord();
		Assert.assertFalse(warehouseLocationItem.isDiscontinued());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a default WarehouseLocationItemKey.
	 *
	 * @return A default WarehouseLocationItemKey.
	 */
	private WarehouseLocationItemKey getDefaultKey() {
		WarehouseLocationItemKey key = new WarehouseLocationItemKey();
		key.setItemType(WarehouseLocationItemTest.DEFAULT_ITEM_TYPE);
		key.setItemCode(WarehouseLocationItemTest.DEFAULT_ITEM_CODE);
		key.setWarehouseNumber(WarehouseLocationItemTest.DEFAULT_WAREHOUSE_NUMBER);
		key.setWarehouseType(WarehouseLocationItemTest.DEFAULT_WAREHOUSE_TYPE);

		return key;
	}

	/**
	 * Returns a default ItemMaster record.
	 *
	 * @return A default ItemMaster record.
	 */
	private ItemMaster getDefaultItemMaster() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(WarehouseLocationItemTest.DEFAULT_ITEM_CODE);
		key.setItemType(WarehouseLocationItemTest.DEFAULT_ITEM_TYPE);

		ItemMaster itemMaster = new ItemMaster();
		itemMaster.setKey(key);
		itemMaster.setDescription(WarehouseLocationItemTest.DEFAULT_ITEM_DESCRIPTION);

		return itemMaster;
	}
	/**
	 * Returns a default WarehouseLocationItem.
	 *
	 * @return A default WarehouseLocationItem.
	 */
	private WarehouseLocationItem getDefaultRecord() {

		WarehouseLocationItem warehouseLocationItem = new WarehouseLocationItem();
		warehouseLocationItem.setKey(this.getDefaultKey());
		warehouseLocationItem.setPurchasingStatus(WarehouseLocationItemTest.DEFAULT_PURCHASING_STATUS);
		warehouseLocationItem.setPurchaseStatusUpdateTime(WarehouseLocationItemTest.DEFAULT_PURCHASING_SATUS_CHANGE);
		warehouseLocationItem.setBillableInventory(WarehouseLocationItemTest.BILLABLE_QUANTITY);
		warehouseLocationItem.setTotalOnHandInventory(WarehouseLocationItemTest.TOTAL_ON_HAND);
		warehouseLocationItem.setDistributionReserveInventory(WarehouseLocationItemTest.DISTRIBUTION_RESERVE);
		warehouseLocationItem.setOffsiteInventory(WarehouseLocationItemTest.OFFSITE_INVENTORY);
		warehouseLocationItem.setOnHoldInventory(WarehouseLocationItemTest.ON_HOLD_INVENTORY);
		warehouseLocationItem.setItemMaster(getDefaultItemMaster());
		return warehouseLocationItem;
	}

	/**
	 * Does a deep compare of two WarehouseLocationItem objects.
	 *
	 * @param i1 The first WarehouseLocationItem.
	 * @param i2 The WarehouseLocationItem to compare i1 to.
	 */
	private void fullCompare(WarehouseLocationItem i1, WarehouseLocationItem i2) {
		Assert.assertEquals(i1.getKey(), i2.getKey());
		Assert.assertEquals(i1.getPurchasingStatus(), i2.getPurchasingStatus());
		Assert.assertEquals(i1.getPurchaseStatusUpdateTime(), i2.getPurchaseStatusUpdateTime());
		Assert.assertEquals(i1.getBillableInventory(), i2.getBillableInventory());
		Assert.assertEquals(i1.getTotalOnHandInventory(), i2.getTotalOnHandInventory());
		Assert.assertEquals(i1.getDistributionReserveInventory(), i2.getDistributionReserveInventory());
		Assert.assertEquals(i1.getOffsiteInventory(), i2.getOffsiteInventory());
		Assert.assertEquals(i1.getOnHoldInventory(), i2.getOnHoldInventory());
	}
}
