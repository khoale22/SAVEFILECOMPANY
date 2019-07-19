/*
 * NonEmptyParameterValidatorTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.controller;


import org.junit.Assert;
import org.junit.Test;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Tests NonEmptyParameterValidator.
 *
 * @author d116773
 * @since 2.0.2
 */
public class NonEmptyParameterValidatorTest {

	/*
	 * validate for strings.
	 */

	/**
	 * Tests validate when passed a good String.
	 */
	@Test
	public void validateStringGoodString() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		parameterValidator.validate("test", "", "", Locale.US);
		// If it doesn't throw an exception, then it's good.
	}

	/**
	 * Tests validate when passed a null String.
	 */
	@Test
	public void validateStringNull() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		try {
			parameterValidator.validate((String)null, "msg", "", Locale.US);
			Assert.fail("should have thrown an exception.");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("msg", e.getMessage());
		}
	}

	/**
	 * Tests validate when passed an empty String.
	 */
	@Test
	public void validateStringEmpty() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		try {
			parameterValidator.validate("", "msg", "", Locale.US);
			Assert.fail("should have thrown an exception.");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("msg", e.getMessage());
		}
	}

	/**
	 * Tests validate when passed an empty string when the message source has been set.
	 */
	@Test
	public void validateStringEmptyWithMessageSource() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		parameterValidator.setMessageSource(CommonMocks.getMessageSource());
		try {
			parameterValidator.validate("", "msg", "", Locale.US);
			Assert.fail("should have thrown an exception.");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Test message", e.getMessage());
		}
	}

	/*
	 * validate Numbers.
	 */

	/**
	 * Tests validate when passed a good Integer.
	 */
	@Test
	public void validateNumberGoodInteger() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		parameterValidator.validate(Integer.valueOf(1), "", "", Locale.US);
		// If it doesn't throw an exception, then it's good.
	}

	/**
	 * Tests validate when passed a null Integer.
	 */
	@Test
	public void validateNumberNull() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		try {
			parameterValidator.validate((Integer)null, "msg", "", Locale.US);
			Assert.fail("should have thrown an exception.");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("msg", e.getMessage());
		}
	}

	/*
	 * validate Collections.
	 */
	/**
	 * Tests validate when passed a good List.
	 */
	@Test
	public void validateCollectionGoodCollection() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();

		List<Long> longs = new ArrayList<>();
		longs.add(1L);

		parameterValidator.validate(longs, "", "", Locale.US);
		// If it doesn't throw an exception, then it's good.
	}

	/**
	 * Tests validate when passed a null List.
	 */
	@Test
	public void validateCollectionNull() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		try {
			parameterValidator.validate((List<Long>)null, "msg", "", Locale.US);
			Assert.fail("should have thrown an exception.");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("msg", e.getMessage());
		}
	}

	/**
	 * Tests validate when passed an empty Collection.
	 */
	@Test
	public void validateCollectionEmpty() {
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		try {
			parameterValidator.validate(new ArrayList<Long>(), "msg", "", Locale.US);
			Assert.fail("should have thrown an exception.");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("msg", e.getMessage());
		}
	}

}
