/*
 * com.heb.util.jpa.DateToLocalDateConverter
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.util.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Converts between java.util.date and java.time.LocalDate. Intended to be used as a converter on JPA entities.
 *
 * @author d116773
 */
@Converter(autoApply = true)
public class DateToLocalDateConverter implements AttributeConverter<LocalDate, Date> {

	private static LocalDate DB2_NULL_LOCAL_DATE = LocalDate.of(1600, 1, 1);


	/**
	 * Converts from java.time.LocalDate to java.util.Date. If null is passed in, null will be returned.
	 *
	 * @param attribute The LocalDate to convert.
	 * @return attribute converted to a Date.
	 */
	@Override
	public Date convertToDatabaseColumn(LocalDate attribute) {
		return attribute == null ? getNullDate() : Date.from(Instant.from(attribute.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	/**
	 * Converts from java.util.Date to java.time.LocalDate. If null or 01/01/1600 is passed in, null will be returned.
	 *
	 * param dbData The Date to convert.
	 * @return dbData converted to a LocalDate.
	 */
	@Override
	public LocalDate convertToEntityAttribute(Date dbData) {
		if (dbData == null) {
			return null;
		}
		LocalDate date = dbData.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return date.equals(DateToLocalDateConverter.DB2_NULL_LOCAL_DATE) ? null : date;
	}

	/**
	 * Returns a Date in null date format.
	 * @return a Date in null date format.
	 */
	public Date getNullDate() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse("1600-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
