package com.heb.util.ws.converters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * Tests BigIntegerToInteger
 * Created by d116773 on 4/21/2016.
 */
public class BigIntegerToIntegerTest {

	private static final int TEN_TO_THE_FIFTH = 100000;

	private int testInteger;

	private BigIntegerToInteger bigIntegerToInteger = new BigIntegerToInteger();
	private String stringField = new String();
	private Integer integerField = new Integer(0);
	private int intField = 0;

	@Before
	public void setup() {
		this.testInteger = (int)(Math.random() * BigIntegerToIntegerTest.TEN_TO_THE_FIFTH);
	}

	@Test
	public void testSupportsReturnsFalseOnNull() {

		boolean shouldBeFalse = this.bigIntegerToInteger.supports(null);
		Assert.assertFalse("Support does not return false on null", shouldBeFalse);
	}

	@Test
	public void testSupportsReturnsFalseOnString() {

		boolean shouldBeFalse = this.bigIntegerToInteger.supports(this.getStringField());
		Assert.assertFalse("Support does not return false on String", shouldBeFalse);
	}

	@Test
	public void testSupportReturnsTrueOnInteger() {

		boolean shouldBeTrue = this.bigIntegerToInteger.supports(this.getIntegerField());
		Assert.assertTrue("Support does not return true on Integer", shouldBeTrue);
	}

	@Test
	public void testSupportReturnsTrueOnInt() {

		boolean shouldBeTrue = this.bigIntegerToInteger.supports(this.getIntField());
		Assert.assertTrue("Support does not return true on Integer", shouldBeTrue);
	}

	@Test
	public void testNotBigIntegerThrowsException() {
		Integer i = Integer.valueOf(0);
		try {
			this.bigIntegerToInteger.convert(i, this.getIntegerField(), this);
			Assert.fail("Convert did not throw exception on bad object");
		} catch (FieldConversionException e) {

		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testNotIntegerOrIntThrowsException() {
		BigInteger bi = BigInteger.ONE;

		try {
			this.bigIntegerToInteger.convert(bi, this.getStringField(), this);
			Assert.fail("Did not throw exception on String");
		} catch (IllegalAccessException e) {

		} catch (UnsupportedConversionException e) {

		}
	}

	@Test
	public void testGoodConversionToInteger() {
		BigInteger bi = BigInteger.valueOf(this.testInteger);

		try {
			this.bigIntegerToInteger.convert(bi, this.getIntegerField(), this);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals("Did not set Integer field correctly", Integer.valueOf(this.testInteger), this.integerField);
	}

	@Test
	public void testGoodConversionToInt() {
		BigInteger bi = BigInteger.valueOf(this.testInteger);

		try {
			this.bigIntegerToInteger.convert(bi, this.getIntField(), this);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals("Did not set int field correctly", this.testInteger, this.intField);
	}

	@Test
	public void testBigConversionFails() {
		BigInteger bi = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

		try {
			this.bigIntegerToInteger.convert(bi, this.getIntegerField(), this);
			Assert.fail("Too big of a number didn't fail.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (FieldConversionException e) {

		}
	}


	@Test
	public void testSmallConversionFails() {
		BigInteger bi = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);

		try {
			this.bigIntegerToInteger.convert(bi, this.getIntegerField(), this);
			Assert.fail("Too small of a number didn't fail.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (FieldConversionException e) {

		}
	}

	@Test
	public void testSetNullOnIntFails() {
		try {
			this.bigIntegerToInteger.convert(null, this.getIntField(), this);
			Assert.fail("Null to int conversion did not fail.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (UnsupportedConversionException e) {

		}
	}

	@Test
	public void testSetNullOnInteger() {
		try {
			this.bigIntegerToInteger.convert(null, this.getIntegerField(), this);
			Assert.assertNull("Did not set Integer to null", this.integerField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
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
