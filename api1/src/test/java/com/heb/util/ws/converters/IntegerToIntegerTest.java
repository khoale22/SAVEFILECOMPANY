/*
 * IntegerToIntegerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.ws.converters;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Tests the IntegerToInteger converter.
 *
 * @author d116773
 * @since 2.0.2
 */
public class IntegerToIntegerTest {

	private static final int TEN_TO_THE_FIFTH = 100000;

	private int testIntgeer;
	private IntegerToInteger integerToInteger = new IntegerToInteger();

	private String stringField = new String();
	private Integer integerField = new Integer(0);
	private int intField = 0;

	@Before
	public void setup() {
		this.testIntgeer = (int)(Math.random() * IntegerToIntegerTest.TEN_TO_THE_FIFTH);
	}

	@Test
	public void testSupportFailsOnNull() {
		boolean shouldBeFalse = this.integerToInteger.supports(null);
		Assert.assertFalse("support does not return false on null", shouldBeFalse);
	}

	@Test
	public void testSupportReturnsFalseOnString() {
		boolean shouldBeFalse = this.integerToInteger.supports(this.getStringField());
		Assert.assertFalse("support does not return false on string", shouldBeFalse);
	}

	@Test
	public void testSupportReturnsTrueOnInteger() {
		boolean shouldBeTrue = this.integerToInteger.supports(this.getIntegerField());
		Assert.assertTrue("support does not return true on integer", shouldBeTrue);
	}

	@Test
	public void testSupportReturnsTrueOnInt() {
		boolean shouldBeTrue = this.integerToInteger.supports(this.getIntField());
		Assert.assertTrue("support does not return true on integer", shouldBeTrue);
	}

	@Test
	public void testGoodConversionToInteger() {
		Integer i = this.testIntgeer;
		try {
			this.integerToInteger.convert(i, this.getIntegerField(), this);
			Assert.assertEquals("did not convert to Intger", i, this.integerField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGoodConversionToInt() {
		Integer i = this.testIntgeer;

		try {
			this.integerToInteger.convert(i, this.getIntField(), this);
			Assert.assertEquals("did not convert to Intger", i, (Integer)this.intField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testNullToInteger() {
		try {
			this.integerToInteger.convert(null, this.getIntegerField(), this);
			Assert.assertNull("did not convert null to Integer", this.integerField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testNullToIntFails() {
		try {
			this.integerToInteger.convert(null, this.getIntField(), this);
			Assert.fail("Did not throw exception on conversion to int.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (UnsupportedConversionException e) {

		}
	}


	private Field getIntField() {
		try {
			return this.getClass().getDeclaredField("intField");
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'intField'");
		}
		return null;
	}

	private Field getStringField() {
		try {
			return this.getClass().getDeclaredField("stringField");
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'stringField'");
		}
		return null;
	}


	private Field getIntegerField() {
		try {
			return this.getClass().getDeclaredField("integerField");
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'integerField'");
		}
		return null;
	}
}
