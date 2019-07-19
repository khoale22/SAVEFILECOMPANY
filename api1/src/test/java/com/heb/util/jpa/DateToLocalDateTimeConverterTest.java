/*
 * com.heb.util.jpa.DateToLocalDateTimeConverterTest
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Tests DateToLocalDateTimeConverter.
 *
 * @author d116773
 * @since 2.0.0
 */
public class DateToLocalDateTimeConverterTest {

	private DateToLocalDateTimeConverter dateToLocalDateTimeConverter = new DateToLocalDateTimeConverter();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");

	/*
	 * convertToDatabaseColumn
	 */

	/**
	 * Tests convertToDatabaseColumn when passed in a good LocalDateTime.
	 */
	@Test
	public void testConvertToDatabaseColumnLocalDateTime() {
		LocalDateTime dateTime = LocalDateTime.of(2016, 5, 20, 16, 32, 1);
		try {
			Date date = this.dateFormat.parse("2016-05-20 16:32:01");
			Date d = this.dateToLocalDateTimeConverter.convertToDatabaseColumn(dateTime);
			Assert.assertEquals("did not convert good LocalDateTime", date, d);
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests convertToDatabaseColumn when passed null.
	 */
	@Test
	public void testConvertToDatabaseColumnNull() {
		Date d = this.dateToLocalDateTimeConverter.convertToDatabaseColumn(null);
		Assert.assertNull("did not convert null", d);
	}

	/*
	 * convertToEntityAttribute
	 */

	/**
	 * Test convertToEntityAttribute with a good date.
	 */
	@Test
	public void convertToEntityAttributeDate() {
		try {
			Date d = this.dateFormat.parse("2016-05-20 16:32:01");
			LocalDateTime dateTime = this.dateToLocalDateTimeConverter.convertToEntityAttribute(d);
			LocalDateTime t = LocalDateTime.of(2016, 5, 20, 16, 32, 1);
			Assert.assertEquals("did not convert good Date", t, dateTime);
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test convertToEntityAttribute with a null.
	 */
	@Test
	public void convertToEntityAttributeNull() {

		LocalDateTime dateTime = this.dateToLocalDateTimeConverter.convertToEntityAttribute(null);

		Assert.assertNull("did not convert null date", dateTime);
	}
}
