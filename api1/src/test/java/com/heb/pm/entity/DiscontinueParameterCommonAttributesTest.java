/*
 * DiscontinueCommonAttributesTest
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
 * Tests DiscontinueParameterCommonAttributes.
 *
 * @author d116773
 * @since 2.0.2
 */
public class DiscontinueParameterCommonAttributesTest {

	private static final String PARAMETER_VALUE = "test_param";
	private static final int PRIORITY = 5;
	private static final boolean ACTIVE = true;

	/*
	 * getters
	 */

	/**
	 * Tests getParameterValue.
	 */
	@Test
	public void getParameterValue() {
		DiscontinueParameterCommonAttributes attributes = this.getTestObject();
		Assert.assertEquals(DiscontinueParameterCommonAttributesTest.PARAMETER_VALUE, attributes.getParameterValue());
	}

	/**
	 * Tests getPriority.
	 */
	@Test
	public void getPriority() {
		DiscontinueParameterCommonAttributes attributes = this.getTestObject();
		Assert.assertEquals(DiscontinueParameterCommonAttributesTest.PRIORITY, attributes.getPriority());
	}

	/**
	 * Tests isActive.
	 */
	@Test
	public void isActive() {
		DiscontinueParameterCommonAttributes attributes = this.getTestObject();
		Assert.assertEquals(DiscontinueParameterCommonAttributesTest.ACTIVE, attributes.isActive());
	}


	/*
	 * Support functions.
	 */

	/**
	 * Returns a DiscontinueParameterCommonAttributes to test with.
	 *
	 * @return A DiscontinueParameterCommonAttributes to test with.
	 */
	private DiscontinueParameterCommonAttributes getTestObject() {

		DiscontinueParameterCommonAttributes attributes = new DiscontinueParameterCommonAttributes();
		attributes.setParameterValue(DiscontinueParameterCommonAttributesTest.PARAMETER_VALUE);
		attributes.setPriority(DiscontinueParameterCommonAttributesTest.PRIORITY);
		attributes.setActive(DiscontinueParameterCommonAttributesTest.ACTIVE);

		return attributes;
	}
}
