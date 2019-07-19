package com.heb.util.ws.converters;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by d116773 on 4/25/2016.
 */
public class StringToStringTest {

	private StringToString stringToString = new StringToString();
	private String stringField = new String("test");
	private Long longField = new Long(0);

	@Test
	public void testSupportFailsOnNull() {
		boolean shouldBeFalse = this.stringToString.supports(null);
		Assert.assertFalse("Support does not return false on null", shouldBeFalse);
	}

	@Test
	public void testSupportsReturnsFalseOnLong() {

		boolean shouldBeFalse = this.stringToString.supports(this.getLongField());
		Assert.assertFalse("Support does not return false on Long", shouldBeFalse);
	}

	@Test
	public void testSupportReturnsTrueOnString() {

		boolean shouldBeTrue = this.stringToString.supports(this.getStringField());
		Assert.assertTrue("Support does not return true on String", shouldBeTrue);
	}

	@Test
	public void testGoodConversionToString() {

		String s = "new string";

		try {
			this.stringToString.convert(s, getStringField(), this);

			Assert.assertEquals("Did not convert to String", s, this.stringField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testNull() {
		try {
			this.stringToString.convert(null, this.getStringField(), this);
			Assert.assertNull("Did not set to null", this.stringField);
		} catch (IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
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
