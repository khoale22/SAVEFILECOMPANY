/*
 * com.heb.util.jpa.DateToLocalDateConverterTest
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Tests DateToLocalDateConverter.
 *
 * @author d116773
 * @since 2.0.0
 */
public class DateToLocalDateConverterTest {

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");
	private DateToLocalDateConverter dateToLocalDateConverter = new DateToLocalDateConverter();

	/*
	 * convertToDatabaseColumn
	 */

	/**
	 * Tests convertToDatabaseColumn with a good date.
	 */
	@Test
	public void convertToDatabaseColumnDate() {
		LocalDate localDate = LocalDate.of(2016, 5, 20);
		Date d = this.dateToLocalDateConverter.convertToDatabaseColumn(localDate);
		try {
			Assert.assertEquals("did not convert good LocaDate", this.dateFormat.parse("2016-5-20"), d);
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests convertToDatabaseColumn with a null.
	 */
	@Test
	public void convertToDatabaseColumnNull() {
		Date d = this.dateToLocalDateConverter.convertToDatabaseColumn(null);
		Assert.assertNull("did not convert good LocaDate",d);
	}

	/*
	 * convertToEntityAttribute
	 */

	/**
	 * Tests convertToEntityAttribute with a good date.
	 */
	@Test
	public void convertToEntityAttributeDate() {
		Date d = new Date();
		LocalDate localDate = this.dateToLocalDateConverter.convertToEntityAttribute(d);
		Assert.assertEquals("did not convert good Date", LocalDate.now(ZoneId.systemDefault()), localDate);
	}

	/**
	 * Tests convertToEntityAttribute with a null passed in.
	 */
	@Test
	public void convertToEntityAttributeNull() {
		LocalDate localDate = this.dateToLocalDateConverter.convertToEntityAttribute(null);
		Assert.assertNull("did not convert null date", localDate);
	}

	/**
	 * Tests convertToEntityAttribue with 1/1/1600 passed in.
	 */
	@Test
	public void convertToEntityAttribute111600() {
		Date d = null;
		try {
			d = this.dateFormat.parse("1600-01-01");
		} catch (ParseException e) {
			Assert.fail(e.getMessage());
		}
		LocalDate localDate = this.dateToLocalDateConverter.convertToEntityAttribute(d);
		Assert.assertNull(localDate);
	}
}
