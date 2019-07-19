package com.heb.util.ws.converters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * Created by d116773 on 4/25/2016.
 */
public class StringToIntegerTest {

	private static final int TEN_TO_THE_FIFTH = 100000;

	private int testIntgeer;

	private StringToInteger stringToInteger = new StringToInteger();
	private String stringField = new String();
	private Integer integerField = new Integer(0);
	private int intField = 0;

	@Before
	public void setup() {
		this.testIntgeer = (int)(Math.random() * StringToIntegerTest.TEN_TO_THE_FIFTH);
	}

	@Test
	public void testSupportFailsOnNull() {
		boolean shouldBeFalse = this.stringToInteger.supports(null);
		Assert.assertFalse("Support does not return false on null", shouldBeFalse);
	}

	@Test
	public void testSupportsReturnsFalseOnString() {

		boolean shouldBeFalse = this.stringToInteger.supports(this.getStringField());
		Assert.assertFalse("Support does not return false on String", shouldBeFalse);
	}

	@Test
	public void testSupportReturnsTrueOnInteger() {

		boolean shouldBeTrue = this.stringToInteger.supports(this.getIntegerField());
		Assert.assertTrue("Support does not return true on Integer", shouldBeTrue);
	}

	@Test
	public void testSupportReturnsTrueOnInt() {

		boolean shouldBeTrue = this.stringToInteger.supports(this.getIntField());
		Assert.assertTrue("Support does not return true on int", shouldBeTrue);
	}

	@Test
	public void testNotParsableThrowsException() {
		String s = "ldkjafklsdjfasd";

		try {
			this.stringToInteger.convert(s, this.getIntegerField(), this);
			Assert.fail("Unparseable did not throw exception.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (FieldConversionException e) {

		}
	}

	@Test
	public void testGoodConversionToInteger() {
		Integer i = Integer.valueOf(this.testIntgeer);
		String s = i.toString();

		try {
			this.stringToInteger.convert(s, this.getIntegerField(), this);

			Assert.assertEquals("Did not convert to Integer", i, this.integerField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGoodConversionToInt() {
		String s = Integer.toString(this.testIntgeer);

		try {
			this.stringToInteger.convert(s, this.getIntField(), this);

			Assert.assertEquals("Did not convert to Integer", this.testIntgeer, this.intField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}


	@Test
	public void testNullOnInteger() {
		try {
			this.stringToInteger.convert(null, this.getIntegerField(), this);
			Assert.assertNull("Did not set null to Integer", this.integerField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testNullOnIntFails() {
		try {
			this.stringToInteger.convert(null, this.getIntField(), this);
			Assert.fail("Did not throw exception on conversion to int.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (UnsupportedConversionException e) {

		}
	}

	@Test
	public void testTooBigFails() {
		BigInteger bi = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

		try {
			this.stringToInteger.convert(bi, this.getIntegerField(), this);
			Assert.fail("Too big did not fail.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (FieldConversionException e) {

		}
	}

	@Test
	public void testTooSmallFails() {
		BigInteger bi = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);

		try {
			this.stringToInteger.convert(bi, this.getIntegerField(), this);
			Assert.fail("Too small did not fail.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (FieldConversionException e) {

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
