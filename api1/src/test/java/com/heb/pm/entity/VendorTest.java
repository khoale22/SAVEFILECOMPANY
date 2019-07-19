/*
 * VendorTest
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests Vendor.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorTest {

	private static final int ID = 55;
	private static final String NAME = "HEB VENDOR";

	/*
	 * getters
	 */

	/**
	 * Tests getVendorNumber.
	 */
	@Test
	public void getVendorNumber() {
		Assert.assertEquals(VendorTest.ID, this.getVendor().getVendorNumber());
	}

	/**
	 * Tests getVendorName.
	 */
	@Test
	public void getVendorName() {
		Assert.assertEquals(VendorTest.NAME, this.getVendor().getVendorName());
	}

	/**
	 * Tests getVendorNameAsString.
	 */
	@Test
	public void getVendorNameAsString() {
		Assert.assertEquals(Integer.toString(VendorTest.ID), this.getVendor().getVendorNumberAsString());
	}

	/**
	 * Tests getDisplayName.
	 */
	@Test
	public void getDisplayName() {
		Vendor v = this.getVendor();
		String displayName = v.getVendorName().trim() + "[" + v.getVendorNumber() + "]";
		System.out.println(v.getDisplayName());
		Assert.assertEquals(displayName, v.getDisplayName());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals("Vendor{vendorNumber=55, vendorName='HEB VENDOR'}", this.getVendor().toString());
	}

	/*
	 * equals
	 */

	/**
	 * Tests equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		Vendor v = this.getVendor();
		boolean equals = v.equals(v);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals on objects with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		Vendor v1 = this.getVendor();
		Vendor v2 = this.getVendor();
		boolean equals = v1.equals(v2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals with different vendor numbers.
	 */
	@Test
	public void equalsDifferentVendorNumber() {
		Vendor v1 = this.getVendor();
		Vendor v2 = this.getVendor();
		v2.setVendorNumber(-1 * v2.getVendorNumber());
		boolean equals = v1.equals(v2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests vendors with different vendor names.
	 */
	@Test
	public void equalsDifferentVendorName() {
		Vendor v1 = this.getVendor();
		Vendor v2 = this.getVendor();
		v2.setVendorName("other name");
		boolean equals = v1.equals(v2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void equalsNull() {
		Vendor v = this.getVendor();
		boolean equals = v.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when passed an object of a different type of object.
	 */
	@Test
	public void equalsDifferentType() {
		Vendor v = this.getVendor();
		boolean equals = v.equals(Integer.valueOf(VendorTest.ID));
		Assert.assertFalse(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Test hashCode is the same when called on the same object twice.
	 */
	@Test
	public void hashCodeSameObject() {
		Vendor v = this.getVendor();
		Assert.assertEquals(v.hashCode(), v.hashCode());
	}

	/**
	 * Test hashCode when passed objects with the same values.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		Vendor v1 = this.getVendor();
		Vendor v2 = this.getVendor();
		Assert.assertEquals(v1.hashCode(), v2.hashCode());
	}

	/**
	 * Tests hashCode when the vendor number is different between two objects.
	 */
	@Test
	public void hashCodeDifferentVendorNumber() {
		Vendor v1 = this.getVendor();
		Vendor v2 = this.getVendor();
		v2.setVendorNumber(v2.getVendorNumber() + 10);
		Assert.assertNotEquals(v1.hashCode(), v2.hashCode());
	}

	/**
	 * Tests hashCode when vendor name is different between two objects.
	 */
	@Test
	public void hashCodeDifferentVendorName() {
		Vendor v1 = this.getVendor();
		Vendor v2 = this.getVendor();
		v2.setVendorName("other name");
		Assert.assertEquals(v1.hashCode(), v2.hashCode());
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a Vendor object to test with.
	 *
	 * @return A Vendor object to test with.
	 */
	private Vendor getVendor() {

		Vendor v = new Vendor();
		v.setVendorName(VendorTest.NAME);
		v.setVendorNumber(VendorTest.ID);
		v.setVendorNumberAsString(Integer.toString(VendorTest.ID));
		return v;
	}
}
