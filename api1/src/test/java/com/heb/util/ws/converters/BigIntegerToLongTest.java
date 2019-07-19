package com.heb.util.ws.converters;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * Tests BigIntegerToInteger
 * Created by d116773 on 4/21/2016.
 */
public class BigIntegerToLongTest {

	private BigIntegerToLong bigIntegerToLong = new BigIntegerToLong();
	private String stringField = new String();
	private Long longField = new Long(0);
	private long lField = 0;

	@Test
	public void testSupportsReturnsFalseOnNull() {

		boolean shouldBeFalse = this.bigIntegerToLong.supports(null);
		Assert.assertFalse("Support does not return false on null", shouldBeFalse);

	}

	@Test
	public void testSupportsReturnsFalseOnString() {

		boolean shouldBeFalse = this.bigIntegerToLong.supports(this.getStringField());
		Assert.assertFalse("Support does not return false on String", shouldBeFalse);

	}

	@Test
	public void testSupportReturnsTrueOnLong() {

		boolean shouldBeTrue = this.bigIntegerToLong.supports(this.getLField());
		Assert.assertTrue("Support does not return true on Long", shouldBeTrue);

	}

	@Test
	public void testSupportReturnsTrueOnRawLong() {

		boolean shouldBeTrue = this.bigIntegerToLong.supports(this.getLField());
		Assert.assertTrue("Support does not return true on long", shouldBeTrue);

	}

	@Test
	public void testNotBigIntegerThrowsException() {
		Integer i = Integer.valueOf(0);
		try {
			this.bigIntegerToLong.convert(i, this.getLongField(), this);
			Assert.fail("Convert did not throw exception on bad object");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (FieldConversionException e) {
		}
	}

	@Test
	public void testNotLongOrLongThrowsException() {
		BigInteger bi = BigInteger.ONE;

		try {
			this.bigIntegerToLong.convert(bi, this.getStringField(), this);
			Assert.fail("Did not throw exception on String");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());

		} catch (UnsupportedConversionException e) {

		}
	}

	@Test
	public void testSetNullOnLong() {
		try {
			this.bigIntegerToLong.convert(null, this.getLongField(), this);
			Assert.assertNull("Did not set null on Long", this.longField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testSetNullOnLValueFails() {
		try {
			this.bigIntegerToLong.convert(null, this.getLField(), this);
			Assert.fail("Convert null to long did not fail.");
		} catch (UnsupportedConversionException e) {
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}

	}
	@Test
	public void testGoodConversionToLong() {
		BigInteger bi = BigInteger.ONE;

		try {
			this.bigIntegerToLong.convert(bi, this.getLongField(), this);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals("Did not set Integer field correctly", Long.valueOf(1), this.longField);
	}

	@Test
	public void testGoodConversionToLongRaw() {
		BigInteger bi = BigInteger.ONE;

		try {
			this.bigIntegerToLong.convert(bi, this.getLField(), this);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals("Did not set int field correctly", 1l, this.lField);
	}

	@Test
	public void testBigConversionFails() {
		BigInteger bi = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

		try {
			this.bigIntegerToLong.convert(bi, this.getLongField(), this);
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
			this.bigIntegerToLong.convert(bi, this.getLongField(), this);
			Assert.fail("Too small of a number didn't fail.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (FieldConversionException e) {

		}
	}

	private Field getLField() {
		try {
			return this.getClass().getDeclaredField("lField");
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'lField'");
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


	private Field getLongField() {
		try {
			return this.getClass().getDeclaredField("longField");
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'longField'");
		}
		return null;
	}
}
