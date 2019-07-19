package com.heb.util.ws.converters;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * Created by d116773 on 4/26/2016.
 */
public class ConverterFactoryTest {

	private Integer integer = Integer.valueOf(555);
	private BigInteger bi = BigInteger.ONE;
	private String s = "Test string";

	private String stringField = new String();
	private Long longField = new Long(0);
	private long lField = 0;
	private Integer integerField = new Integer(0);
	private int intField = 0;

	private ConverterFactory factory = new ConverterFactory();

	@Test
	public void testIntegerToInteger() {
		Converter c = this.factory.getConverter(this.integer, this.getIntegerField());
		Assert.assertTrue("wrong type for Integer to Integer", c instanceof IntegerToInteger);
	}

	@Test
	public void testBigIntegerToLong() {
		Converter c = this.factory.getConverter(this.bi, this.getLongField());
		Assert.assertTrue("Wrong type for BigInteger to Long", c instanceof BigIntegerToLong);
	}

	@Test
	public void testBigIntegerToLongRaw() {
		Converter c = this.factory.getConverter(this.bi, this.getLField());
		Assert.assertTrue("Wrong type for BigInteger to long", c instanceof BigIntegerToLong);
	}

	@Test
	public void testBigIntegerToInteger() {
		Converter c = this.factory.getConverter(this.bi, this.getIntegerField());
		Assert.assertTrue("Wrong type for BigInteger to Integer", c instanceof BigIntegerToInteger);
	}

	@Test
	public void testBigIntegerToInt() {
		Converter c = this.factory.getConverter(this.bi, this.getIntField());
		Assert.assertTrue("Wrong type for BigInteger to int", c instanceof BigIntegerToInteger);
	}

	@Test
	public void testStringToLong() {
		Converter c = this.factory.getConverter(this.s, this.getLongField());
		Assert.assertTrue("Wrong type for String to Long", c instanceof StringToLong);
	}

	@Test
	public void testStringToLongRaw() {
		Converter c = this.factory.getConverter(this.s, this.getLField());
		Assert.assertTrue("Wrong type for String to long", c instanceof StringToLong);
	}

	@Test
	public void testStringToInteger() {
		Converter c = this.factory.getConverter(this.s, this.getIntegerField());
		Assert.assertTrue("Wrong type for String to Integer", c instanceof StringToInteger);
	}

	@Test
	public void testStringToIntegerRaw() {
		Converter c = this.factory.getConverter(this.s, this.getIntField());
		Assert.assertTrue("Wrong type for String to int", c instanceof StringToInteger);
	}

	@Test
	public void testStringToString() {
		Converter c = this.factory.getConverter(this.s, this.getStringField());
		Assert.assertTrue("Wrong type for String to String", c instanceof StringToString);
	}

	@Test
	public void testNullToLong() {
		Converter c = this.factory.getConverter(null, this.getLongField());
		Assert.assertTrue("Wrong type for null to Long", c instanceof StringToLong);
	}

	@Test
	public void testNullToInteger() {
		Converter c = this.factory.getConverter(null, this.getIntegerField());
		Assert.assertTrue("Wrong type for null to Integer", c instanceof StringToInteger);
	}

	@Test
	public void testNullToString() {
		Converter c = this.factory.getConverter(null, this.getStringField());
		Assert.assertTrue("Wrong type for null to String", c instanceof StringToString);
	}

	@Test
	public void testBadObjectThrowsException() {
		try {
			Converter c = this.factory.getConverter(this, this.getLongField());
			Assert.fail("Bad object didn't throw exception.");
		} catch (UnsupportedConversionException e) {

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

	private Field getIntField() {
		try {
			return this.getClass().getDeclaredField("intField");
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'intField'");
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
