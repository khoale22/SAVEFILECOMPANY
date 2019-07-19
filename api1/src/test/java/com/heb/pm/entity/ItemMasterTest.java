/*
 *  com.heb.pm.entity.ItemMasterTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import testSupport.LoggingSupportTestRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tests ItemMaster.
 *
 * @author d116773
 * @since 2.0.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ItemMasterTest {

	private static final int BILLABLE_QUANTITY = 35;
	private static final int TOTAL_ON_HAND = 55;
	private static final int OFFSITE_INVENTORY = 1;
	private static final int ON_HOLD_INVENTORY = 10;
	private static final int DISTRIBUTION_RESERVE = 9;

	@Autowired
	private ItemMasterRepositoryTest itemMasterRepositoryTest;

	/*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		ItemMaster im = this.getDefaultRecord();
		boolean equals = im.equals(im);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject() {
		ItemMaster im1 = this.getDefaultRecord();
		ItemMaster im2 = this.getDefaultRecord();
		boolean equals = im1.equals(im2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on objects with different keys.
	 */
	@Test
	public void equalsDifferentKeys() {
		ItemMaster im1 = this.getDefaultRecord();
		ItemMaster im2 = this.getDiscontinuedDsd();
		boolean equals = im1.equals(im2);
		Assert.assertFalse(equals);
	}
	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		ItemMaster im = this.getDefaultRecord();
		boolean equals = im.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		ItemMaster im = this.getDefaultRecord();
		boolean equals = im.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		ItemMaster im1 = new ItemMaster();
		ItemMaster im2 = this.getDefaultRecord();
		boolean equals = im1.equals(im2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the right side has a null key.
	 */
	@Test
	public void testEqualsNullTargetKey() {
		ItemMaster im1 = this.getDefaultRecord();
		ItemMaster im2 = new ItemMaster();
		boolean equals = im1.equals(im2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when both keys are null.
	 */
	@Test
	public void testEqualsBothKeysNull() {
		ItemMaster im1 = new ItemMaster();
		ItemMaster im2 = new ItemMaster();
		boolean equals = im1.equals(im2);
		Assert.assertTrue(equals);
	}
	/*
	 * hashCode
	 */

	/**
	 * Test hashCode on the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(im.hashCode(), im.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ItemMaster im1 = this.getDefaultRecord();
		ItemMaster im2 = this.getDefaultRecord();
		Assert.assertEquals(im1.hashCode(), im2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		ItemMaster im1 = this.getDefaultRecord();
		ItemMaster im2 = this.getDiscontinuedDsd();
		Assert.assertNotEquals(im1.hashCode(), im2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ItemMaster im = new ItemMaster();
		Assert.assertEquals(0, im.hashCode());
	}

	/**
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		ItemMaster im1 = this.getDefaultRecord();
		ItemMaster im2 = new ItemMaster();
		Assert.assertNotEquals(im1.hashCode(), im2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ItemMaster im = this.getDiscontinuedDsd();
		System.out.println(im.toString());
		Assert.assertEquals("ItemMaster{key=ItemMasterKey{itemType='DSD  ', itemCode=9020508597}, " +
						"description='ELTA SILVER  GEL              ', discontinueDate=2016-04-30, pack=1, " +
						"orderingUpc=0, discontinuedByUID='null', addedDate=null, itemSize='null', retail='null'}", im.toString());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {
		ItemMaster im = this.getDefaultRecord();

		ItemMasterKey key = new ItemMasterKey();
		key.setItemType("ITMCD");
		key.setItemCode(66L);

		Assert.assertEquals(key, im.getKey());
	}

	/**
	 * Tests getDescription.
	 */
	@Test
	public void getDescription() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals("1:24 NEED FOR SPEED VEHICLES  ", im.getDescription());
	}

	/**
	 * Tests getDiscontinueDate.
	 */
	@Test
	public void getDiscontinueDate() {
		ItemMaster im = this.getDiscontinuedDsd();
		Assert.assertEquals(LocalDate.of(2016, 4, 30), im.getDiscontinueDate());
	}



	@Test
	public void getPack() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(6, im.getPack());
	}

	/**
	 * Tests getDisplayReadyUnit
	 */
	@Test
	public void getDisplayReadyUnit() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(true, im.getDisplayReadyUnit());
	}

	/**
	 * Tests getTypeOfDRU
	 */
	@Test
	public void getTypeOfDRU() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals("7", im.getTypeOfDRU());
	}

	/**
	 * Tests getAlwaysSubWhenOut
	 */
	@Test
	public void getAlwaysSubWhenOut() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(false, im.getAlwaysSubWhenOut());
	}

	/**
	 * Tests getRowsFacing
	 */
	@Test
	public void getRowsFacing() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(8L, im.getRowsFacing());
	}

	/**
	 * Tests getRowsDeep
	 */
	@Test
	public void getRowsDeep() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(4L, im.getRowsDeep());
	}

	/**
	 * Tests getRowsHigh
	 */
	@Test
	public void getRowsHigh() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(2l, im.getRowsHigh());
	}

	/**
	 * Tests getOrientation
	 */
	@Test
	public void getOrientation() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(1L, im.getOrientation());
	}

	/**
	 * Tests getTypeOfDRUDisplayName
	 */
	@Test
	public void getTypeOfDRUDisplayName(){
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals("Display Ready Unit", im.getDisplayReadyUnitDisplayName());
	}

	/**
	 * Test getItemType
	 */
	@Test
	public void getItemType() {
		ItemMaster im = this.getDefaultRecord();

		ItemType itemType = new ItemType();
		itemType.setId("0");
		itemType.setAbbreviation("SELL");
		itemType.setDescription("SELLABLE");
		Assert.assertEquals(itemType, im.getItemType());
	}

	/**
	 * Test getOneTouchType
	 */
	@Test
	public void getOneTouchType() {
		ItemMaster im = this.getDefaultRecord();

		OneTouchType oneTouchType = new OneTouchType();
		oneTouchType.setId("N");
		oneTouchType.setAbbreviation("NOT1T");
		oneTouchType.setDescription("NOT A ONE TOUCH ITEM");

		Assert.assertEquals(oneTouchType, im.getOneTouchType());
	}

	/*
	 * Test JPA mapping.
	 */
	/**
	 * Tests that the object has the correct JPA mapping on a warehouse product.
	 */
	@Test
	@Transactional
	public void testMappingWarehouse() {
		ItemMaster im = this.getDefaultRecord();
		this.fullItemCompare(this.getDefaultRecord(), im);
		Assert.assertTrue(im.isDiscontinued());
	}

	/**
	 * Tests that the object has the correct JPA mapping on a DSD product.
	 */
	@Test
	@Transactional
	public void testMappingDsd() {
		ItemMaster im = this.getDiscontinuedDsd();
		this.fullItemCompare(this.getDiscontinuedDsd(), im);
		Assert.assertTrue(im.isDiscontinued());
	}

	/*
	 * inventory functions
	 */

	/**
	 * Tests the inventory functions when warehouse location items are null.
	 */
	@Test
	public void inventoryNullWarehouseLocationItems() {
		ItemMaster im = new ItemMaster();
		Assert.assertEquals(0, im.getDistributionReserveInventory());
		Assert.assertEquals(0, im.getOffsiteInventory());
		Assert.assertEquals(0, im.getTotalOnHandInventory());
		Assert.assertEquals(0, im.getOnHoldInventory());
		Assert.assertEquals(0, im.getBillableInventory());
	}

	/**
	 * Tests the inventory functions when warehouse location items are empty.
	 */
	@Test
	public void inventoryEmptyWarhouseLocationItems() {
		ItemMaster im = new ItemMaster();
		im.setWarehouseLocationItems(new ArrayList<>());
		Assert.assertEquals(0, im.getDistributionReserveInventory());
		Assert.assertEquals(0, im.getOffsiteInventory());
		Assert.assertEquals(0, im.getTotalOnHandInventory());
		Assert.assertEquals(0, im.getOnHoldInventory());
		Assert.assertEquals(0, im.getBillableInventory());
	}

	/**
	 * Tests the inventory functions when warehouse location items have values.
	 */
	@Test
	public void inventory() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(ItemMasterTest.DISTRIBUTION_RESERVE, im.getDistributionReserveInventory());
		Assert.assertEquals(ItemMasterTest.OFFSITE_INVENTORY, im.getOffsiteInventory());
		Assert.assertEquals(ItemMasterTest.TOTAL_ON_HAND, im.getTotalOnHandInventory());
		Assert.assertEquals(ItemMasterTest.ON_HOLD_INVENTORY, im.getOnHoldInventory());
		Assert.assertEquals(ItemMasterTest.BILLABLE_QUANTITY, im.getBillableInventory());
	}

	/*
	 * isDiscontinued
	 */

	/**
	 * Tests isDiscontinued with a null key.
	 */
	@Test
	public void isDiscontinuedNullKey() {

		ItemMaster im = new ItemMaster();
		try {
			im.isDiscontinued();
			Assert.fail("should have thrown exception");
		} catch (IllegalStateException e) {
			// This is supposed to happen
		}
	}

	/**
	 * Tests isDiscontinued with a discontinued DSD item.
	 */
	@Test
	public void isDiscontinuedDsdYes() {
		ItemMaster im = this.getDiscontinuedDsd();
		Assert.assertTrue(im.isDiscontinued());
	}

	/**
	 * Tests isDiscontinued with an active DSD item.
	 */
	@Test
	public void isDiscontinuedDsdNo() {
		ItemMaster im = this.getActiveDsd();
		Assert.assertFalse(im.isDiscontinued());
	}

	/**
	 * Tests isDiscontinued with a discontinued warehouse item.
	 */
	@Test
	public void isDiscontinuedWarehouseYes() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertTrue(im.isDiscontinued());
	}

	/**
	 * Tests isDiscontinued with an active warehouse item.
	 */
	@Test
	public void isDiscontinuedWarehouseNo() {
		ItemMaster im = this.getWarehouseActive();
		Assert.assertFalse(im.isDiscontinued());
	}

	/*
	 * getNormalizedDiscontinueDate
	 */

	/**
	 * Tests getNormalizedDiscontinueDate on an item that is not discontinued.
	 */
	@Test
	public void normalizedDiscontinueDateNotDiscontinued() {
		ItemMaster im = this.getWarehouseActive();
		Assert.assertNull(im.getNormalizedDiscontinueDate());
	}

	/**
	 * Tests getNormalizedDiscontinueDate on a discontinued DSD item.
	 */
	@Test
	public void normalizedDiscontinueDateDsd() {
		ItemMaster im = this.getDiscontinuedDsd();
		Assert.assertEquals(im.getDiscontinueDate().atStartOfDay(), im.getNormalizedDiscontinueDate());
	}

	/**
	 * Tests getNormalizedDiscontinueDate on a discontinued warehouse item.
	 */
	@Test
	public void normalizedDiscontinueDateWarehouse() {
		ItemMaster im = this.getDefaultRecord();
		Assert.assertEquals(LocalDateTime.of(2016, 5, 30, 15, 29, 35), im.getNormalizedDiscontinueDate());
	}

	@Test
	public void normalizedDiscontinueDateComparison(){
		ItemMaster before = this.getDefaultRecord();
		ItemMaster after = this.getWarehouseDiscontinuedAfterDefault();

		Assert.assertTrue(after.getNormalizedDiscontinueDate().isAfter(before.getNormalizedDiscontinueDate()));
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns an ItemMaster object that equals the one that is first in the test table.
	 *
	 * @return An ItemMaster object that equals the one that is first in the test table.
	 */
	private ItemMaster getDefaultRecord() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(66L);
		key.setItemType(ItemMasterKey.WAREHOUSE);

		ItemMaster im = new ItemMaster();
		im.setKey(key);
		im.setDescription("1:24 NEED FOR SPEED VEHICLES  ");
		im.setPack(6);
		im.setOrderingUpc(500400L);
		im.setDisplayReadyUnit(true);
		im.setAlwaysSubWhenOut(false);
		im.setTypeOfDRU("7");
		im.setRowsDeep(4L);
		im.setRowsFacing(8L);
		im.setRowsHigh(2L);
		im.setOrientation(1L);

		ItemType itemType = new ItemType();
		itemType.setId("0");
		itemType.setAbbreviation("SELL");
		itemType.setDescription("SELLABLE");
		im.setItemType(itemType);

		OneTouchType oneTouchType = new OneTouchType();
		oneTouchType.setId("N");
		oneTouchType.setAbbreviation("NOT1T");
		oneTouchType.setDescription("NOT A ONE TOUCH ITEM");
		im.setOneTouchType(oneTouchType);

		WarehouseLocationItemKey warehouseLocationItemKey = new WarehouseLocationItemKey();
		warehouseLocationItemKey.setItemCode(66L);
		warehouseLocationItemKey.setItemType(ItemMasterKey.WAREHOUSE);
		warehouseLocationItemKey.setWarehouseNumber(404);
		warehouseLocationItemKey.setWarehouseType("W ");
		WarehouseLocationItem warehouseLocationItem = new WarehouseLocationItem();
		warehouseLocationItem.setKey(warehouseLocationItemKey);
		warehouseLocationItem.setPurchasingStatus(
				PurchasingStatusCode.CodeValues.DISCONTINUED.getPurchasingStatus());
		warehouseLocationItem.setDistributionReserveInventory(ItemMasterTest.DISTRIBUTION_RESERVE);
		warehouseLocationItem.setOffsiteInventory(ItemMasterTest.OFFSITE_INVENTORY);
		warehouseLocationItem.setTotalOnHandInventory(ItemMasterTest.TOTAL_ON_HAND);
		warehouseLocationItem.setOnHoldInventory(ItemMasterTest.ON_HOLD_INVENTORY);
		warehouseLocationItem.setBillableInventory(ItemMasterTest.BILLABLE_QUANTITY);
		warehouseLocationItem.setOffsiteInventory(ItemMasterTest.OFFSITE_INVENTORY);
		warehouseLocationItem.setPurchaseStatusUpdateTime(LocalDateTime.of(2016, 5, 30, 15, 29, 35));

		List<WarehouseLocationItem> warehouseLocationItems = new ArrayList<>();
		warehouseLocationItems.add(warehouseLocationItem);
		im.setWarehouseLocationItems(warehouseLocationItems);

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(404L);
		im.setPrimaryUpc(primaryUpc);

		return im;
	}

	/**
	 * Returns a warehouse ItemMaster object that is not discontinued.
	 *
	 * @return A warehouse ItemMaster object that is not discontinued.
	 */
	private ItemMaster getWarehouseActive() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(66L);
		key.setItemType(ItemMasterKey.WAREHOUSE);

		ItemMaster im = new ItemMaster();
		im.setKey(key);
		im.setDescription("1:24 NEED FOR SPEED VEHICLES  ");
		im.setPack(6);


		WarehouseLocationItemKey warehouseLocationItemKey = new WarehouseLocationItemKey();
		warehouseLocationItemKey.setItemCode(66L);
		warehouseLocationItemKey.setItemType(ItemMasterKey.WAREHOUSE);
		warehouseLocationItemKey.setWarehouseNumber(404);
		warehouseLocationItemKey.setWarehouseType("W");
		WarehouseLocationItem warehouseLocationItem = new WarehouseLocationItem();
		warehouseLocationItem.setKey(warehouseLocationItemKey);
		warehouseLocationItem.setPurchasingStatus(PurchasingStatusCode.CodeValues.ACTIVE.getPurchasingStatus());
		warehouseLocationItem.setPurchaseStatusUpdateTime(LocalDateTime.of(2016, 5, 31, 15, 29, 35));

		List<WarehouseLocationItem> warehouseLocationItems = new ArrayList<>();
		warehouseLocationItems.add(warehouseLocationItem);
		im.setWarehouseLocationItems(warehouseLocationItems);

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(405L);
		im.setPrimaryUpc(primaryUpc);

		return im;
	}

	/**
	 * Returns a warehouse ItemMaster object that is not discontinued.
	 *
	 * @return A warehouse ItemMaster object that is not discontinued.
	 */
	private ItemMaster getWarehouseDiscontinuedAfterDefault() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(66L);
		key.setItemType(ItemMasterKey.WAREHOUSE);

		ItemMaster im = new ItemMaster();
		im.setKey(key);
		im.setDescription("1:24 NEED FOR SPEED VEHICLES  ");
		im.setPack(6);
		im.setOrderingUpc(500400L);

		WarehouseLocationItemKey warehouseLocationItemKey = new WarehouseLocationItemKey();
		warehouseLocationItemKey.setItemCode(66L);
		warehouseLocationItemKey.setItemType(ItemMasterKey.WAREHOUSE);
		warehouseLocationItemKey.setWarehouseNumber(404);
		warehouseLocationItemKey.setWarehouseType("W ");
		WarehouseLocationItem warehouseLocationItem = new WarehouseLocationItem();
		warehouseLocationItem.setKey(warehouseLocationItemKey);
		warehouseLocationItem.setPurchasingStatus(
				PurchasingStatusCode.CodeValues.DISCONTINUED.getPurchasingStatus());
		warehouseLocationItem.setDistributionReserveInventory(ItemMasterTest.DISTRIBUTION_RESERVE);
		warehouseLocationItem.setOffsiteInventory(ItemMasterTest.OFFSITE_INVENTORY);
		warehouseLocationItem.setTotalOnHandInventory(ItemMasterTest.TOTAL_ON_HAND);
		warehouseLocationItem.setOnHoldInventory(ItemMasterTest.ON_HOLD_INVENTORY);
		warehouseLocationItem.setBillableInventory(ItemMasterTest.BILLABLE_QUANTITY);
		warehouseLocationItem.setOffsiteInventory(ItemMasterTest.OFFSITE_INVENTORY);
		warehouseLocationItem.setPurchaseStatusUpdateTime(LocalDateTime.of(2016, 5, 31, 15, 29, 35));

		List<WarehouseLocationItem> warehouseLocationItems = new ArrayList<>();
		warehouseLocationItems.add(warehouseLocationItem);
		im.setWarehouseLocationItems(warehouseLocationItems);

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(404L);
		im.setPrimaryUpc(primaryUpc);

		return im;
	}

	/**
	 * Returns a DSD record that has been discontinued.
	 *
 	 * @return A DSD record that has been discontinued.
	 */
	private ItemMaster getDiscontinuedDsd() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(9020508597L);
		key.setItemType(ItemMasterKey.DSD);

		ItemMaster im = new ItemMaster();
		im.setKey(key);
		im.setDescription("ELTA SILVER  GEL              ");
		im.setDiscontinueDate(LocalDate.of(2016, 4, 30));
		im.setPack(1);

		im.setWarehouseLocationItems(new ArrayList<>());
		return im;
	}

	/**
	 * Returns a DSD record that is not discontinued.
	 *
	 * @return A DSD record that has not been discontinued.
	 */
	private ItemMaster getActiveDsd() {
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(9020508597L);
		key.setItemType(ItemMasterKey.DSD);

		ItemMaster im = new ItemMaster();
		im.setKey(key);
		im.setDescription("ELTA SILVER  GEL              ");
		im.setPack(1);

		im.setWarehouseLocationItems(new ArrayList<>());
		return im;
	}

	/**
	 * Since the equals on item master only compares keys, this goes deeper anc compares all values in the object.
	 *
	 * @param a The first one to compare.
	 * @param b The second one to compare.
	 */
	private void fullItemCompare(ItemMaster a, ItemMaster b) {
		Assert.assertEquals(a.getKey(), b.getKey());
		Assert.assertEquals(a.getDescription(), b.getDescription());
		Assert.assertEquals(a.getDiscontinueDate(), b.getDiscontinueDate());
		Assert.assertEquals(a.getPack(), b.getPack());

		Assert.assertEquals(a.getWarehouseLocationItems().size(), b.getWarehouseLocationItems().size());
		Iterator<WarehouseLocationItem> aWshe = a.getWarehouseLocationItems().iterator();
		Iterator<WarehouseLocationItem> bWhse = b.getWarehouseLocationItems().iterator();
		for (; aWshe.hasNext(); ) {
			Assert.assertEquals(aWshe.next(), bWhse.next());
		}
	}


}
