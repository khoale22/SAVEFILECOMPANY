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

package com.heb.pm.entity;/*
  * Created by m314029 on 6/23/2016.
 */

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import testSupport.LoggingSupportTestRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tests PrimaryUpc.
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class PrimaryUpcTest {

	@Autowired
	private PrimaryUpcRepositoryTest primaryUpcRepositoryTest;

	/*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		PrimaryUpc primaryUpc = this.getDefaultRecord();
		boolean equals = primaryUpc.equals(primaryUpc);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject() {
		PrimaryUpc primaryUpc1 = this.getDefaultRecord();
		PrimaryUpc primaryUpc2 = this.getDefaultRecord();
		boolean equals = primaryUpc1.equals(primaryUpc2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on objects with different keys.
	 */
	@Test
	public void equalsDifferentKeys() {
		PrimaryUpc primaryUpc1 = this.getDefaultRecord();
		PrimaryUpc primaryUpc2 = this.getAlternateRecord();
		boolean equals = primaryUpc1.equals(primaryUpc2);
		Assert.assertFalse(equals);
	}
	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		PrimaryUpc primaryUpc = this.getDefaultRecord();
		boolean equals = primaryUpc.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		PrimaryUpc primaryUpc = this.getDefaultRecord();
		boolean equals = primaryUpc.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		PrimaryUpc primaryUpc1 = new PrimaryUpc();
		PrimaryUpc primaryUpc2 = this.getDefaultRecord();
		boolean equals = primaryUpc1.equals(primaryUpc2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the right side has a null key.
	 */
	@Test
	public void testEqualsNullTargetKey() {
		PrimaryUpc primaryUpc1 = this.getDefaultRecord();
		PrimaryUpc primaryUpc2 = new PrimaryUpc();
		boolean equals = primaryUpc1.equals(primaryUpc2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when both keys are null.
	 */
	@Test
	public void testEqualsBothKeysNull() {
		PrimaryUpc primaryUpc1 = new PrimaryUpc();
		PrimaryUpc primaryUpc2 = new PrimaryUpc();
		boolean equals = primaryUpc1.equals(primaryUpc2);
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
		PrimaryUpc primaryUpc = this.getDefaultRecord();
		Assert.assertEquals(primaryUpc.hashCode(), primaryUpc.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		PrimaryUpc primaryUpc1 = this.getDefaultRecord();
		PrimaryUpc primaryUpc2 = this.getDefaultRecord();
		Assert.assertEquals(primaryUpc1.hashCode(), primaryUpc2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		PrimaryUpc primaryUpc1 = this.getDefaultRecord();
		PrimaryUpc primaryUpc2 = this.getAlternateRecord();
		Assert.assertNotEquals(primaryUpc1.hashCode(), primaryUpc2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		PrimaryUpc primaryUpc = new PrimaryUpc();
		Assert.assertEquals(0, primaryUpc.hashCode());
	}

	/**
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		PrimaryUpc primaryUpc1 = this.getDefaultRecord();
		PrimaryUpc primaryUpc2 = new PrimaryUpc();
		Assert.assertNotEquals(primaryUpc1.hashCode(), primaryUpc2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		PrimaryUpc primaryUpc = this.getDefaultRecord();
		System.out.println(primaryUpc.toString());
		Assert.assertEquals("PrimaryUpc{upc='9015932501'}",
				primaryUpc.toString());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getAssociateUpcs.
	 */
	@Test
	public void getAssociateUpcs() {
		PrimaryUpc primaryUpc = this.getDefaultRecord();

		AssociatedUpc associate = new AssociatedUpc();
		associate.setUpc(9015932501L);
		AssociatedUpc associate2 = new AssociatedUpc();
		associate2.setUpc(9015932509L);

		List<AssociatedUpc> associateUpcList = new ArrayList<>();
		associateUpcList.add(associate);
		associateUpcList.add(associate2);

		Assert.assertEquals(associateUpcList, primaryUpc.getAssociateUpcs());
	}

	/**
	 * Tests getShipper.
	 */
	@Test
	public void getShipper() {
		PrimaryUpc primaryUpc = this.getDefaultRecord();

		List<Shipper> shipList = new ArrayList<>();

		Assert.assertEquals(shipList, primaryUpc.getShipper());
	}

	/**
	 * Tests getUpc.
	 */
	@Test
	public void getUpc() {
		PrimaryUpc primaryUpc = this.getDefaultRecord();
		Assert.assertEquals(9015932501L, primaryUpc.getUpc());
	}

	/*
	 * Test JPA mapping.
	 */
	/**
	 * Tests that the object has the correct JPA mapping as an object in mock db.
	 */
	@Test
	@Transactional
	public void testMappingWarehouse() {
		PrimaryUpc primaryUpc = this.primaryUpcRepositoryTest.findOne(this.getDefaultRecord().getUpc());
		this.fullItemCompare(this.getDefaultRecord(), primaryUpc);
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns an PrimaryUpc object that equals the one that is first in the test table.
	 *
	 * @return An PrimaryUpc object that equals the one that is first in the test table.
	 */
	private PrimaryUpc getDefaultRecord() {

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(9015932501L);

		AssociatedUpc associate = new AssociatedUpc();
		associate.setUpc(9015932501L);
		AssociatedUpc associate2 = new AssociatedUpc();
		associate2.setUpc(9015932509L);

		List<AssociatedUpc> associateUpcList = new ArrayList<>();
		associateUpcList.add(associate);
		associateUpcList.add(associate2);
		primaryUpc.setAssociateUpcs(associateUpcList);

		List<Shipper> shipList = new ArrayList<>();

		primaryUpc.setShipper(shipList);

		return primaryUpc;
	}

	/**
	 * Returns a PrimaryUpc object that is the same as the default except the key is different.
	 *
	 * @return A PrimaryUpc object that is the same as the default except the key is different.
	 */
	private PrimaryUpc getAlternateRecord() {

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(7080562010L);

		AssociatedUpc associate = new AssociatedUpc();
		associate.setUpc(9015932501L);
		AssociatedUpc associate2 = new AssociatedUpc();
		associate2.setUpc(9015932599L);

		List<AssociatedUpc> associateUpcList = new ArrayList<>();
		associateUpcList.add(associate);
		associateUpcList.add(associate2);
		primaryUpc.setAssociateUpcs(associateUpcList);

		ShipperKey shipKey = new ShipperKey();
		shipKey.setUpc(88);
		shipKey.setShipperUpc(99);
		Shipper ship = new Shipper();
		ship.setKey(shipKey);

		List<Shipper> shipList = new ArrayList<>();
		shipList.add(ship);

		primaryUpc.setShipper(shipList);

		return primaryUpc;
	}

	/**
	 * Since the equals on item master only compares keys, this goes deeper anc compares all values in the object.
	 *
	 * @param a The first one to compare.
	 * @param b The second one to compare.
	 */
	private void fullItemCompare(PrimaryUpc a, PrimaryUpc b) {
		Assert.assertEquals(a.getUpc(), b.getUpc());

		Assert.assertEquals(a.getAssociateUpcs().size(), b.getAssociateUpcs().size());
		Iterator<AssociatedUpc> aAssocUpcs = a.getAssociateUpcs().iterator();
		Iterator<AssociatedUpc> bAssocUpcs = b.getAssociateUpcs().iterator();
		for (; aAssocUpcs.hasNext(); ) {
			Assert.assertEquals(aAssocUpcs.next().getUpc(), bAssocUpcs.next().getUpc());
		}

		Assert.assertEquals(a.getShipper().size(), b.getShipper().size());
		Iterator<Shipper> aShip = a.getShipper().iterator();
		Iterator<Shipper> bShip = b.getShipper().iterator();
		for (; aShip.hasNext(); ) {
			Assert.assertEquals(aShip.next().getKey(), bShip.next().getKey());
		}
	}
}