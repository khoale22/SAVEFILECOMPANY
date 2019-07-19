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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests Shipper.
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ShipperTest {

	@Autowired
	private ShipperRepositoryTest shipperRepositoryTest;

	/*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		Shipper ship = this.getDefaultRecord();
		boolean equals = ship.equals(ship);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject() {
		Shipper ship = this.getDefaultRecord();
		Shipper ship2 = this.getDefaultRecord();
		boolean equals = ship.equals(ship2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on objects with different keys.
	 */
	@Test
	public void equalsDifferentKeys() {
		Shipper ship1 = this.getDefaultRecord();
		Shipper ship2 = this.getAlternateKey();
		boolean equals = ship1.equals(ship2);
		Assert.assertFalse(equals);
	}
	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		Shipper ship = this.getDefaultRecord();
		boolean equals = ship.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		Shipper ship = this.getDefaultRecord();
		boolean equals = ship.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		Shipper ship1 = new Shipper();
		Shipper ship2 = this.getDefaultRecord();
		boolean equals = ship1.equals(ship2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the right side has a null key.
	 */
	@Test
	public void testEqualsNullTargetKey() {
		Shipper ship1 = this.getDefaultRecord();
		Shipper ship2 = new Shipper();
		boolean equals = ship1.equals(ship2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when both keys are null.
	 */
	@Test
	public void testEqualsBothKeysNull() {
		Shipper ship1 = new Shipper();
		Shipper ship2 = new Shipper();
		boolean equals = ship1.equals(ship2);
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
		Shipper ship = this.getDefaultRecord();
		Assert.assertEquals(ship.hashCode(), ship.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		Shipper ship1 = this.getDefaultRecord();
		Shipper ship2 = this.getDefaultRecord();
		Assert.assertEquals(ship1.hashCode(), ship2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		Shipper ship1 = this.getDefaultRecord();
		Shipper ship2 = this.getAlternateKey();
		Assert.assertNotEquals(ship1.hashCode(), ship2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		Shipper ship = new Shipper();
		Assert.assertEquals(0, ship.hashCode());
	}

	/**
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		Shipper ship1 = this.getDefaultRecord();
		Shipper ship2 = new Shipper();
		Assert.assertNotEquals(ship1.hashCode(), ship2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		Shipper ship = this.getDefaultRecord();
		System.out.println(ship.toString());
		Assert.assertEquals("Shipper{keyPdShipperKey{upc='1', shipperUpc=7080562010}, shipperTypeCode'M'}",
				ship.toString());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {
		Shipper ship = this.getDefaultRecord();

		ShipperKey key = new ShipperKey();
		key.setShipperUpc(7080562010L);
		key.setUpc(1);

		Assert.assertEquals(key, ship.getKey());
	}

	/**
	 * Tests getPrimaryUpc.
	 */
	@Test
	public void getPrimaryUpc() {
		Shipper ship = this.getDefaultRecord();

		PrimaryUpc primaryUpc = null;

		Assert.assertEquals(null, ship.getPrimaryUpc());
	}

	/**
	 * Tests getShipperTypeCode.
	 */
	@Test
	public void getShipperTypeCode() {
		Shipper ship = this.getDefaultRecord();

		char typeCode = 'M';

		Assert.assertEquals(typeCode, ship.getShipperTypeCode());
	}

	/**
	 * Tests getRealUpc.
	 */
	@Test
	public void getRealUpc() {
		Shipper ship = this.getDefaultRecord();

		PrimaryUpc realUpc = new PrimaryUpc();
		realUpc.setUpc(7080562010L);

		Assert.assertEquals(realUpc, ship.getRealUpc());
	}

	/*
	 * Test JPA mapping.
	 */
	/**
	 * Tests that the object has the correct JPA mapping on a shipper.
	 */
	@Test
	@Transactional
	public void testMapping() {
		Shipper ship = this.shipperRepositoryTest.findOne(this.getDefaultRecord().getKey());
		this.fullItemCompare(this.getDefaultRecord(), ship);
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a Shipper object that equals the one that is first in the test table.
	 *
	 * @return A Shipper object that equals the one that is first in the test table.
	 */
	private Shipper getDefaultRecord(){
		ShipperKey key = new ShipperKey();
		key.setShipperUpc(7080562010L);
		key.setUpc(1);

		Shipper shipper = new Shipper();
		shipper.setKey(key);
		shipper.setShipperTypeCode('M');

		shipper.setPrimaryUpc(null);

		PrimaryUpc realUpc = new PrimaryUpc();
		realUpc.setUpc(7080562010L);
		shipper.setRealUpc(realUpc);

		return shipper;
	}


	/**
	 * Returns a Shipper object that has a different key than the default.
	 *
	 * @return A Shipper object that has a different key than the default.
	 */
	private Shipper getAlternateKey(){
		ShipperKey key = new ShipperKey();
		key.setShipperUpc(67);
		key.setUpc(88);

		Shipper shipper = new Shipper();
		shipper.setKey(key);
		shipper.setShipperTypeCode('M');

		shipper.setPrimaryUpc(null);

		PrimaryUpc realUpc = new PrimaryUpc();
		realUpc.setUpc(67);
		shipper.setRealUpc(realUpc);

		return shipper;
	}

	/**
	 * Since the equals on item master only compares keys, this goes deeper anc compares all values in the object.
	 *
	 * @param a The first one to compare.
	 * @param b The second one to compare.
	 */
	private void fullItemCompare(Shipper a, Shipper b) {
		Assert.assertEquals(a.getKey(), b.getKey());
		Assert.assertEquals(a.getRealUpc(), b.getRealUpc());
		Assert.assertEquals(a.getShipperTypeCode(), b.getShipperTypeCode());
	}
}
