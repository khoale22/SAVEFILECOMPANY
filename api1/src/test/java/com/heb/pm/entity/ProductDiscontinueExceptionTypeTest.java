/*
 * ProductDiscontinueExceptionTypeTest
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

/**
 * Tests ProductDiscontinueExceptionTypeTest.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ProductDiscontinueExceptionTypeTest {


	/**
	 * Tests getType.
	 */
	@Test
	public void getType() {
		ProductDiscontinueExceptionType type = ProductDiscontinueExceptionType.VENDOR;
		Assert.assertEquals("Vendor", type.getType());
	}

	/**
	 * Tests getPriority.
	 */
	@Test
	public void getPriority() {
		ProductDiscontinueExceptionType type = ProductDiscontinueExceptionType.VENDOR;
		Assert.assertEquals(7, type.getPriority());
	}

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		ProductDiscontinueExceptionType type = ProductDiscontinueExceptionType.VENDOR;
		Assert.assertEquals("Vendor", type.toString());
	}

	/**
	 * Tests that the list has the right number of entries.
	 */
	@Test
	public void theList() {
		Assert.assertEquals(8, ProductDiscontinueExceptionType.allTypes.size());
	}

	/**
	 * Tests fromString.
	 */
	@Test
	public void fromString() {
		ProductDiscontinueExceptionType type = ProductDiscontinueExceptionType.fromString("Vendor");
		Assert.assertEquals(ProductDiscontinueExceptionType.VENDOR, type);
	}

	/**
	 * Tests fromString when passed a string that does not correspond to a value.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void fromStringBadValue() {
		ProductDiscontinueExceptionType type = ProductDiscontinueExceptionType.fromString("NotVendor");
	}

}
