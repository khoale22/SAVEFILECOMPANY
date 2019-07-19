/*
 *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 *
 */


package com.heb.pm.entity;

/*
 * created by m314029 on 06/23/2016.
*/

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests ShipperKey
 */
public class ShipperKeyTest {

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void testEqualsSameObject(){
		ShipperKey key = this.getDefaultKey();
		boolean equal = key.equals(key);
		Assert.assertTrue("same object not equal", equal);
	}

	/**
	 * Tests equals when passed a different object with the same values.
	 */
	@Test
	public void testEqualsSimilarObject() {
		ShipperKey key1 = this.getDefaultKey();
		ShipperKey key2 = this.getDefaultKey();
		boolean equal = key1.equals(key2);
		Assert.assertTrue(equal);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void testEqualsNull() {
		ShipperKey key = this.getDefaultKey();
		boolean equal = key.equals(null);
		Assert.assertFalse(equal);
	}
	/**
	 * Tests equals when passed an object with a different item code but the same type.
	 */
	@Test
	public void testEqualsDifferentShipperUpc() {
		ShipperKey key1 = this.getDefaultKey();
		ShipperKey key2 = this.getAlternateShipper();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object with the same item code but a different type.
	 */
	@Test
	public void testEqualsDifferentUpc() {
		ShipperKey key1 = this.getDefaultKey();
		ShipperKey key2 = this.getAlternateUpc();
		boolean equals = key1.equals(key2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object with a different item code and different type.
	 */
	@Test
	public void testEqualsDifferentBoth() {
		ShipperKey key1 = this.getDefaultKey();
		ShipperKey key2 = this.getAlternateBoth();
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
		ShipperKey key = this.getDefaultKey();
		Assert.assertEquals(key.hashCode(), key.hashCode());
	}

	/**
	 * Tests hashCode returns the same value for different objects with the same values.
	 */
	@Test
	public void testHashCodeSimilarObject() {
		ShipperKey key1 = this.getDefaultKey();
		ShipperKey key2 = this.getDefaultKey();
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns different values for objects with the same upc but different shipper upc.
	 */
	@Test
	public void testHashCodeDifferentShipperUpc() {
		ShipperKey key1 = this.getDefaultKey();
		ShipperKey key2 = this.getAlternateShipper();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns a different value for objects with the same shipper upc but different upc.
	 */
	@Test
	public void testHashCodeDifferentUpc() {
		ShipperKey key1 = this.getDefaultKey();
		ShipperKey key2 = this.getAlternateUpc();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/**
	 * Tests hashCode returns a different value for objects with different shipper upc and upc.
	 */
	@Test
	public void testHashCodeDifferentBoth() {
		ShipperKey key1 = this.getDefaultKey();
		ShipperKey key2 = this.getAlternateBoth();
		Assert.assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	/*
	 * getters
	 */


	/**
	 * Tests getItemCode.
	 */
	@Test
	public void testGetUpc() {
		ShipperKey key = this.getDefaultKey();
		Assert.assertEquals(key.getUpc(), 45);
	}

	/**
	 * Tests getItemType.
	 */
	@Test
	public void testGetShipperUpc() {
		ShipperKey key = this.getDefaultKey();
		Assert.assertEquals(key.getShipperUpc(), 66);
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ShipperKey key = this.getDefaultKey();
		Assert.assertEquals("PdShipperKey{upc='45', shipperUpc=66}", key.toString());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns the default test value.
	 *
	 * @return The default test value.
	 */
	private ShipperKey getDefaultKey(){
		ShipperKey key = new ShipperKey();
		key.setUpc(45);
		key.setShipperUpc(66);
		return key;
	}

	/**
	 * Returns a ShipperKey with different Shipper UPC.
	 *
	 * @return ShipperKey with different Shipper UPC.
	 */
	private ShipperKey getAlternateShipper(){
		ShipperKey key = new ShipperKey();
		key.setUpc(45);
		key.setShipperUpc(88);
		return key;
	}

	/**
	 * Returns a ShipperKey with different UPC.
	 *
	 * @return ShipperKey with different UPC.
	 */
	private ShipperKey getAlternateUpc(){
		ShipperKey key = new ShipperKey();
		key.setUpc(77);
		key.setShipperUpc(66);
		return key;
	}

	/**
	 * Returns a ShipperKey with different Shipper UPC and UPC.
	 *
	 * @return ShipperKey with different Shipper UPC and UPC.
	 */
	private ShipperKey getAlternateBoth(){
		ShipperKey key = new ShipperKey();
		key.setUpc(77);
		key.setShipperUpc(88);
		return key;
	}
}
