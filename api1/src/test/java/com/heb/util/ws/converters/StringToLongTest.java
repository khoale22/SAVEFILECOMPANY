package com.heb.util.ws.converters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * Created by d116773 on 4/25/2016.
 */
public class StringToLongTest {

	private static final int TEN_TO_THE_FIFTH = 100000;

	private int testIntgeer;

	private StringToLong stringToLong = new StringToLong();
	private String stringField = new String();
	private Long longField = new Long(0);
	private long lField = 0;

	@Before
	public void setup() {
		this.testIntgeer = (int)(Math.random() * StringToLongTest.TEN_TO_THE_FIFTH);
	}

	@Test
	public void testSupportFailsOnNull() {
		boolean shouldBeFalse = this.stringToLong.supports(null);
		Assert.assertFalse("Support does not return false on null", shouldBeFalse);
	}

	@Test
	public void testSupportsReturnsFalseOnString() {

		boolean shouldBeFalse = this.stringToLong.supports(this.getStringField());
		Assert.assertFalse("Support does not return false on String", shouldBeFalse);
	}

	@Test
	public void testSupportReturnsTrueOnLong() {

		boolean shouldBeTrue = this.stringToLong.supports(this.getLongField());
		Assert.assertTrue("Support does not return true on Long", shouldBeTrue);
	}

	@Test
	public void testSupportReturnsTrueOnInt() {

		boolean shouldBeTrue = this.stringToLong.supports(this.getLField());
		Assert.assertTrue("Support does not return true on long", shouldBeTrue);
	}

	@Test
	public void testNotParsableThrowsException() {
		String s = "ldkjafklsdjfasd";

		try {
			this.stringToLong.convert(s, this.getLongField(), this);
			Assert.fail("Unparseable did not throw exception.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (FieldConversionException e) {

		}
	}

	@Test
	public void testGoodConversionToLong() {
		Long l = Long.valueOf(this.testIntgeer);
		String s = l.toString();

		try {
			this.stringToLong.convert(s, this.getLongField(), this);

			Assert.assertEquals("Did not convert to Long", l, this.longField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGoodConversionToLongRaw() {
		String s = Long.toString(this.testIntgeer);

		try {
			this.stringToLong.convert(s, this.getLField(), this);

			Assert.assertEquals("Did not convert to long", this.testIntgeer, this.lField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}


	@Test
	public void testNullOnLong() {
		try {
			this.stringToLong.convert(null, this.getLongField(), this);
			Assert.assertNull("Did not set null to Long", this.longField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testNullOnLongRawFails() {
		try {
			this.stringToLong.convert(null, this.getLField(), this);
			Assert.fail("Did not throw exception on conversion to long.");
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		} catch (UnsupportedConversionException e) {

		}
	}

	@Test
	public void testTooBigFails() {
		BigInteger bi = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

		try {
			this.stringToLong.convert(bi, this.getLongField(), this);
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
			this.stringToLong.convert(bi, this.getLongField(), this);
			Assert.fail("Too small did not fail.");
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
