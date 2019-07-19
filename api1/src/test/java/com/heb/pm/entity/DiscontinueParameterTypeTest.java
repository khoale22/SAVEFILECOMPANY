/*
 *  DiscontinueParameterTypeTest
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author s573181
 * @since 2.0.4
 */
public class DiscontinueParameterTypeTest {

	/**
	 * Tests getTypeById.
	 */
	@Test
	public void getTypeById() {
		Assert.assertEquals(DiscontinueParameterType.getTypeById(1), DiscontinueParameterType.STORE_SALES);
		Assert.assertEquals(DiscontinueParameterType.getTypeById(2), DiscontinueParameterType.NEW_ITEM_PERIOD);
		Assert.assertEquals(DiscontinueParameterType.getTypeById(3), DiscontinueParameterType.WAREHOUSE_UNITS);
		Assert.assertEquals(DiscontinueParameterType.getTypeById(4), DiscontinueParameterType.STORE_UNITS);
		Assert.assertEquals(DiscontinueParameterType.getTypeById(5), DiscontinueParameterType.STORE_RECEIPTS);
		Assert.assertEquals(DiscontinueParameterType.getTypeById(6), DiscontinueParameterType.PURCHASE_ORDERS);
	}

	/**
	 * Tests getTypeById when passed a bad Id value.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getTypeByIdBadValue() {
		DiscontinueParameterType type = DiscontinueParameterType.getTypeById(100);
	}

	/**
	 * Tests getId.
	 */
	@Test
	public void getId() {
		Assert.assertEquals(1, DiscontinueParameterType.STORE_SALES.getId());
		Assert.assertEquals(2, DiscontinueParameterType.NEW_ITEM_PERIOD.getId());
		Assert.assertEquals(3, DiscontinueParameterType.WAREHOUSE_UNITS.getId());
		Assert.assertEquals(4, DiscontinueParameterType.STORE_UNITS.getId());
		Assert.assertEquals(5, DiscontinueParameterType.STORE_RECEIPTS.getId());
		Assert.assertEquals(6, DiscontinueParameterType.PURCHASE_ORDERS.getId());
	}

	/**
	 * Tests getDescription.
	 */
	@Test
	public void getDescription(){
		Assert.assertEquals("Store Sales", DiscontinueParameterType.STORE_SALES.getDescription());
		Assert.assertEquals("New Product Setup", DiscontinueParameterType.NEW_ITEM_PERIOD.getDescription());
		Assert.assertEquals("Inventory at Warehouse", DiscontinueParameterType.WAREHOUSE_UNITS.getDescription());
		Assert.assertEquals("Inventory at Stores", DiscontinueParameterType.STORE_UNITS.getDescription());
		Assert.assertEquals("Receipts", DiscontinueParameterType.STORE_RECEIPTS.getDescription());
		Assert.assertEquals("Purchase Orders", DiscontinueParameterType.PURCHASE_ORDERS.getDescription());
	}

	/**
	 * Tests getParameterName.
	 */
	@Test
	public void getParameterName() {
		Assert.assertEquals("LST_SCN_DT", DiscontinueParameterType.STORE_SALES.getParameterName());
		Assert.assertEquals("ADDED_DT", DiscontinueParameterType.NEW_ITEM_PERIOD.getParameterName());
		Assert.assertEquals("WHSE_INVEN.ON_HAND_QTY", DiscontinueParameterType.WAREHOUSE_UNITS.getParameterName());
		Assert.assertEquals("STR_INVEN.ON_HAND_QTY", DiscontinueParameterType.STORE_UNITS.getParameterName());
		Assert.assertEquals("LST_RECD_TS", DiscontinueParameterType.STORE_RECEIPTS.getParameterName());
		Assert.assertEquals("ORDERED_DT", DiscontinueParameterType.PURCHASE_ORDERS.getParameterName());
	}

	/**
	 * Tests that the list has the right number of entries.
	 */
	@Test
	public void theList() {
		Assert.assertEquals(6, DiscontinueParameterType.allTypes.size());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals("Store Sales", DiscontinueParameterType.STORE_SALES.toString());
		Assert.assertEquals("New Product Setup", DiscontinueParameterType.NEW_ITEM_PERIOD.toString());
		Assert.assertEquals("Inventory at Warehouse", DiscontinueParameterType.WAREHOUSE_UNITS.toString());
		Assert.assertEquals("Inventory at Stores", DiscontinueParameterType.STORE_UNITS.toString());
		Assert.assertEquals("Receipts", DiscontinueParameterType.STORE_RECEIPTS.toString());
		Assert.assertEquals("Purchase Orders", DiscontinueParameterType.PURCHASE_ORDERS.toString());
	}
}
