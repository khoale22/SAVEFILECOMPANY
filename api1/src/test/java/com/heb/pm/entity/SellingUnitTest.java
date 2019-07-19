/*
 * SellingUnitTest
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

/**
 * Tests SellingUnit.
 *
 * @author d116773
 * @since 2.0.1
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class SellingUnitTest {

	private static final long TEST_UPC = 9017421713L;
	private static final String TEST_SIZE = "EACH";

	private static final long OTHER_UPC = 9017424231L;
	private static final String OTHER_SIZE = "LB";

	@Autowired
	private SellingUnitRepositoryTest sellingUnitRepositoryTest;

	/*
	 * getters
	 */

	/**
	 * Tests getUpc.
	 */
	@Test
	public void getUpc() {
		Assert.assertEquals(SellingUnitTest.TEST_UPC, this.getTestSellingUnit().getUpc());
	}

	/**
	 * Tests getTagSize.
	 */
	@Test
	public void getTagSize() {
		Assert.assertEquals(SellingUnitTest.TEST_SIZE, this.getTestSellingUnit().getTagSize());
	}


	@Test
	public void getPrimaryUpc() {
		Assert.assertTrue(this.getTestSellingUnit().isPrimaryUpc());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		System.out.println("'" + this.getTestSellingUnit().toString() + "'");
		Assert.assertEquals("SellingUnit{upc=9017421713, " +
				"primaryUpc=true, " +
				"tagSize='EACH', " +
				"discontinueDate=null, " +
				"processedByScanMaintenance =false, " +
				"prodId=0, bonusSwitch=true, " +
				"prodId=0, lastScanDate=null, " +
				"productMaster=null}", this.getTestSellingUnit().toString());
	}

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void equalsSameObject() {
		SellingUnit u = this.getTestSellingUnit();
		boolean equals = u.equals(u);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals when passed an object with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		SellingUnit u1 = this.getTestSellingUnit();
		SellingUnit u2 = this.getTestSellingUnit();
		boolean equals = u1.equals(u2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals when passed null.
	 */
	@Test
	public void equalsNull() {
		SellingUnit u = this.getTestSellingUnit();
		boolean equals = u.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed a different type of object.
	 */
	@Test
	public void equalsDifferentObjectType() {
		SellingUnit u = this.getTestSellingUnit();
		boolean equals = u.equals(Integer.valueOf(0));
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object with all values the same except for UPC.
	 */
	@Test
	public void equalsDifferentUpc() {
		SellingUnit u1 = this.getTestSellingUnit();
		SellingUnit u2 = this.getTestSellingUnit();
		u2.setUpc(-1 * u2.getUpc());
		boolean equals = u1.equals(u2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed a completely different object.
	 */
	@Test
	public void equalsDifferentObject() {
		SellingUnit u1 = this.getTestSellingUnit();
		SellingUnit u2 = this.getOtherSellingUnit();
		boolean equals = u1.equals(u2);
		Assert.assertFalse(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode is consistent.
	 */
	@Test
	public void hashCodeSameObject() {
		SellingUnit u = this.getTestSellingUnit();
		Assert.assertEquals(u.hashCode(), u.hashCode());
	}

	/**
	 * Tests hashCode returns true for objects with the same values.
	 */
	@Test
	public void hashCodeEqualsObject() {
		SellingUnit u1 = this.getTestSellingUnit();
		SellingUnit u2 = this.getTestSellingUnit();
		Assert.assertEquals(u1.hashCode(), u2.hashCode());
	}

	/**
	 * Tests hashCode returns a different value when the objects are different.
	 */
	@Test
	public void hashCodeDifferentObject() {
		SellingUnit u1 = this.getTestSellingUnit();
		SellingUnit u2 = this.getOtherSellingUnit();
		Assert.assertNotEquals(u1.hashCode(), u2.hashCode());
	}

	/**
	 * Tests hashCode is consistent when only the UPC is the same between two objects.
	 */
	@Test
	public void hashCodeUpcSame() {
		SellingUnit u1 = this.getTestSellingUnit();
		SellingUnit u2 = this.getOtherSellingUnit();
		u2.setUpc(u1.getUpc());
		Assert.assertEquals(u1.hashCode(), u2.hashCode());
	}

	/**
	 * Tests hashCode returns a new value when only the UPC is different between two objects.
	 */
	@Test
	public void hashCodeDifferentUpc() {
		SellingUnit u1 = this.getTestSellingUnit();
		SellingUnit u2 = this.getTestSellingUnit();
		u2.setUpc(u2.getUpc() + 1);
		Assert.assertNotEquals(u1.hashCode(), u2.hashCode());
	}

	/*
	 * JPA mapping
	 */

	/**
	 * Tests the mapping on a UPC that is not a primary.
	 */
	@Test
	public void testMappingNonPrimary() {
		SellingUnit u = this.sellingUnitRepositoryTest.findOne(4775428879L);
		Assert.assertEquals(4775428879L, u.getUpc());
		Assert.assertEquals("EACH", u.getTagSize());
		Assert.assertFalse(u.isPrimaryUpc());
	}

	/**
	 * Tests the mapping on a UPC that is a primary.
	 */
	@Test
	public void testMappingPrimary() {
		SellingUnit u = this.sellingUnitRepositoryTest.findOne(4775477779L);
		Assert.assertEquals(4775477779L, u.getUpc());
		Assert.assertEquals("EACH", u.getTagSize());
		Assert.assertTrue(u.isPrimaryUpc());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns the default test SellingUnit.
	 *
	 * @return The default test SellingUnit.
	 */
	private SellingUnit getTestSellingUnit() {
		SellingUnit s = new SellingUnit();
		s.setUpc(SellingUnitTest.TEST_UPC);
		s.setPrimaryUpc(true);
		s.setTagSize(SellingUnitTest.TEST_SIZE);
		s.setBonusSwitch(true);
		return s;
	}

	/**
	 * Returns a different test SellingUnit.
	 *
	 * @return A different test SellingUnit.
	 */
	private SellingUnit getOtherSellingUnit() {
		SellingUnit s = new SellingUnit();
		s.setUpc(SellingUnitTest.OTHER_UPC);
		s.setPrimaryUpc(false);
		s.setTagSize(SellingUnitTest.OTHER_SIZE);
		return s;
	}
}
