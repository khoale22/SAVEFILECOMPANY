/*
 *  WarehouseLocationItemExtendedAttributesTest
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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests WarehouseLocationItemExtendedAttributes.
 *
 * @author s573181
 * @since 2.0.4
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class WarehouseLocationItemExtendedAttributesTest {

	private static final String DEFAULT_ITEM_TYPE = "ITMCD";
	private static final long DEFAULT_ITEM_CODE = 1;
	private static final String DEFAULT_WAREHOUSE_TYPE = "W ";
	private static final int DEFAULT_WAREHOUSE_NUMBER = 207;
	private static final int DEFAULT_ON_ORDER_FOR_FORWARD_BUY = 0;
	private static final int DEFAULT_ON_ORDER_PROMOTION = 0;
	private static final int DEFAULT_TOTAL_ON_ORDER = 0;
	private static final int DEFAULT_ON_ORDER_FOR_TURN = 0;

	@Autowired
	private WarehouseLocationItemExtendedAttributesRepositoryTest repository;

	 /*
	 * equals
	 */

	/**
	 * Tests that an object equals itself.
	 */
	@Test
	public void equalsSameObject() {
		WarehouseLocationItemExtendedAttributes item = this.getDefaultRecord();
		boolean equals = item.equals(item);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that an object equals an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		WarehouseLocationItemExtendedAttributes i1 = this.getDefaultRecord();
		WarehouseLocationItemExtendedAttributes i2 = this.getDefaultRecord();
		boolean equals = i1.equals(i2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests that it does not equal when passed null.
	 */
	@Test
	public void equalsNull() {
		WarehouseLocationItemExtendedAttributes item = this.getDefaultRecord();
		boolean equals = item.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		WarehouseLocationItemExtendedAttributes item = this.getDefaultRecord();
		boolean equals = item.equals(Integer.valueOf(0));
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object with a null key.
	 */
	@Test
	public void equalsNullKeyOther() {
		WarehouseLocationItemExtendedAttributes item = this.getDefaultRecord();
		WarehouseLocationItemExtendedAttributes i2 = new WarehouseLocationItemExtendedAttributes();
		boolean equals = item.equals(i2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when it's key is null.
	 */
	@Test
	public void equalsNullKeySelf() {
		WarehouseLocationItemExtendedAttributes item = new WarehouseLocationItemExtendedAttributes();
		WarehouseLocationItemExtendedAttributes i1 = this.getDefaultRecord();
		boolean equals = item.equals(i1);
		Assert.assertFalse(equals);
	}

	/**
	 * Test that it does not equal an object when both keys are null
	 */
	@Test
	public void equalsNullKeyBoth() {
		WarehouseLocationItemExtendedAttributes item = new WarehouseLocationItemExtendedAttributes();
		WarehouseLocationItemExtendedAttributes i1 = new WarehouseLocationItemExtendedAttributes();
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
		WarehouseLocationItemExtendedAttributes item = this.getDefaultRecord();
		Assert.assertEquals(item.hashCode(), item.hashCode());
	}

	/**
	 * Check hashCode is the same on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		WarehouseLocationItemExtendedAttributes i1 = this.getDefaultRecord();
		WarehouseLocationItemExtendedAttributes i2 = this.getDefaultRecord();
		Assert.assertEquals(i1.hashCode(), i2.hashCode());
	}

	/**
	 * Check that the hashCode is different on different on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		WarehouseLocationItemExtendedAttributes i1 = this.getDefaultRecord();
		WarehouseLocationItemExtendedAttributes i2 = this.getAlternateObject();
		Assert.assertNotEquals(i1, i2);
	}

	/**
	 * Check hashCode is zero with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		WarehouseLocationItemExtendedAttributes item = new WarehouseLocationItemExtendedAttributes();
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
		WarehouseLocationItemExtendedAttributes item = this.repository.findOne(key);
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
		WarehouseLocationItemExtendedAttributes item = this.getDefaultRecord();
		Assert.assertEquals("WarehouseLocationItemExtendedAttributes{key=WarehouseLocationItemKey{itemType='ITMCD', itemCode=1, warehouseType=W , warehouseNumber=207}, onOrderForForwardBuy=0, onOrderPromotion=0, totalOnOrder=0, onOrderForTurn=0}",
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
		WarehouseLocationItemExtendedAttributes warehouseLocationItemExtendedAttributes = this.getDefaultRecord();
		Assert.assertEquals(this.getDefaultKey(), warehouseLocationItemExtendedAttributes.getKey());
	}

	/**
	 * Tests getOnOrderForForwardBuy.
	 */
	@Test
	public void getOnOrderForForwardBuy() {
		WarehouseLocationItemExtendedAttributes warehouseLocationItemExtendedAttributes = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemExtendedAttributesTest.DEFAULT_ON_ORDER_FOR_FORWARD_BUY,
				warehouseLocationItemExtendedAttributes.getOnOrderForForwardBuy());
	}

	/**
	 * Tests getOnOrderForForwardBuy.
	 */
	@Test
	public void getOnOrderForTurn() {
		WarehouseLocationItemExtendedAttributes warehouseLocationItemExtendedAttributes = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemExtendedAttributesTest.DEFAULT_ON_ORDER_FOR_TURN,
				warehouseLocationItemExtendedAttributes.getOnOrderForTurn());
	}

	/**
	 * Tests getOnOrderForForwardBuy.
	 */
	@Test
	public void getOnOrderPromotion() {
		WarehouseLocationItemExtendedAttributes warehouseLocationItemExtendedAttributes = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemExtendedAttributesTest.DEFAULT_ON_ORDER_PROMOTION,
				warehouseLocationItemExtendedAttributes.getOnOrderPromotion());
	}

	/**
	 * Tests getTotalOnOrder.
	 */
	@Test
	public void getTotalOnOrder() {
		WarehouseLocationItemExtendedAttributes warehouseLocationItemExtendedAttributes = this.getDefaultRecord();
		Assert.assertEquals(WarehouseLocationItemExtendedAttributesTest.DEFAULT_TOTAL_ON_ORDER,
				warehouseLocationItemExtendedAttributes.getTotalOnOrder());
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
		key.setItemType(WarehouseLocationItemExtendedAttributesTest.DEFAULT_ITEM_TYPE);
		key.setItemCode(WarehouseLocationItemExtendedAttributesTest.DEFAULT_ITEM_CODE);
		key.setWarehouseNumber(WarehouseLocationItemExtendedAttributesTest.DEFAULT_WAREHOUSE_NUMBER);
		key.setWarehouseType(WarehouseLocationItemExtendedAttributesTest.DEFAULT_WAREHOUSE_TYPE);

		return key;
	}

	/**
	 * Returns a default WarehouseLocationItemExtendedAttributes.
	 *
	 * @return A default WarehouseLocationItemExtendedAttributes.
	 */
	private WarehouseLocationItemExtendedAttributes getDefaultRecord() {

		WarehouseLocationItemExtendedAttributes warehouseLocationItemExtendedAttributes = new WarehouseLocationItemExtendedAttributes();
		warehouseLocationItemExtendedAttributes.setKey(this.getDefaultKey());
		warehouseLocationItemExtendedAttributes.setOnOrderForForwardBuy(DEFAULT_ON_ORDER_FOR_FORWARD_BUY);
		warehouseLocationItemExtendedAttributes.setOnOrderPromotion(DEFAULT_ON_ORDER_PROMOTION);
		warehouseLocationItemExtendedAttributes.setTotalOnOrder(DEFAULT_TOTAL_ON_ORDER);
		warehouseLocationItemExtendedAttributes.setOnOrderForTurn(DEFAULT_ON_ORDER_FOR_TURN);

		return warehouseLocationItemExtendedAttributes;
	}

	private WarehouseLocationItemExtendedAttributes getAlternateObject() {
		WarehouseLocationItemExtendedAttributes warehouseLocationItemExtendedAttributes = new WarehouseLocationItemExtendedAttributes();
		WarehouseLocationItemKey key = new WarehouseLocationItemKey();
		key.setItemType(WarehouseLocationItemExtendedAttributesTest.DEFAULT_ITEM_TYPE);
		key.setItemCode(1);
		key.setWarehouseNumber(100);
		key.setWarehouseType(WarehouseLocationItemExtendedAttributesTest.DEFAULT_WAREHOUSE_TYPE);
		warehouseLocationItemExtendedAttributes.setKey(key);

		warehouseLocationItemExtendedAttributes.setOnOrderForForwardBuy(1);
		warehouseLocationItemExtendedAttributes.setOnOrderPromotion(2);
		warehouseLocationItemExtendedAttributes.setTotalOnOrder(3);
		warehouseLocationItemExtendedAttributes.setOnOrderForTurn(4);

		return warehouseLocationItemExtendedAttributes;
	}


	/**
	 * Does a deep compare of two WarehouseLocationItem objects.
	 *
	 * @param i1 The first WarehouseLocationItem.
	 * @param i2 The WarehouseLocationItem to compare i1 to.
	 */
	private void fullCompare(WarehouseLocationItemExtendedAttributes i1, WarehouseLocationItemExtendedAttributes i2) {
		Assert.assertEquals(i1.getKey(), i2.getKey());
		Assert.assertEquals(i1.getOnOrderForForwardBuy(), i2.getOnOrderForForwardBuy());
		Assert.assertEquals(i1.getOnOrderForTurn(), i2.getOnOrderForTurn());
		Assert.assertEquals(i1.getOnOrderPromotion(), i2.getOnOrderPromotion());
		Assert.assertEquals(i1.getTotalOnOrder(), i2.getTotalOnOrder());
	}
}
