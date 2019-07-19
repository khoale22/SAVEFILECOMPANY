/*
 * com.heb.util.jpa.SwitchToBooleanConverterTest
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.util.jpa;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests SwitchToBooleanConverter.
 *
 * @author d116773
 */
public class SwitchToBooleanConverterTest {

	private SwitchToBooleanConverter switchToBooleanConverter = new SwitchToBooleanConverter();

	/*
	 * convertToDatabaseColumn
	 */

	/**
	 * Tests convertToDatabaseColumn when true is passed in.
	 */
	@Test
	public void convertToDatabaseColumnTrue() {
		String s = this.switchToBooleanConverter.convertToDatabaseColumn(true);
		Assert.assertEquals("did not convert true to Y", "Y", s);
	}

	/**
	 * Tests convertToDatabaseColumn when false is passed in.
	 */
	@Test
	public void convertToDatabaseColumnFalse() {
		String s = this.switchToBooleanConverter.convertToDatabaseColumn(false);
		Assert.assertEquals("did not convert false to N", "N", s);
	}

	/**
	 * Tests convertToDatabaseColumn when null is passed in.
	 */
	@Test
	public void convertToDatabaseColumnNull() {
		String s = this.switchToBooleanConverter.convertToDatabaseColumn(null);
		Assert.assertEquals("did not convert null to N", "N", s);
	}


	/*
	 * convertToEntityAttribute
	 */
	/**
	 * Tests convertToEntityAttribute when Y is passed in.
	 */
	@Test
	public void convertToEntityAttributeY() {
		Boolean b = this.switchToBooleanConverter.convertToEntityAttribute("Y");
		Assert.assertTrue("did not convert Y to true", b);
	}

	/**
	 * Tests convertToEntityAttribute when N is passed in.
	 */
	@Test
	public void convertToEntityAttributeN() {
		Boolean b = this.switchToBooleanConverter.convertToEntityAttribute("N");
		Assert.assertFalse("did not convert N= to false", b);
	}

	/**
	 * Tests convertToEntityAttribute when null is passed in.
	 */
	@Test
	public void convertToEntityAttributeNull() {
		Boolean b = this.switchToBooleanConverter.convertToEntityAttribute(null);
		Assert.assertFalse("did not convert null to false", b);
	}

	/**
	 * Tests convertToEntityAttribute when something not N, Y, or null is passed in.
	 */
	@Test
	public void convertToEntityAttributeSomethingElse() {
		try {
			Boolean b = this.switchToBooleanConverter.convertToEntityAttribute("TEST");
			Assert.fail("should have thrown exception");
		} catch (IllegalArgumentException e) {
			// This is what is supposed to happen.
		}
	}
}
