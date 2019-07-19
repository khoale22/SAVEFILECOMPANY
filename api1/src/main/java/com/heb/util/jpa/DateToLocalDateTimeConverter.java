/*
 * com.heb.util.jpa.DateToLocalDateTimeConverter
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Converts between java.util.date and java.time.LocalDateTime. Intended to be used as a converter on JPA entities.
 *
 * @author d116773
 * @since 2.0.0
 */
@Converter(autoApply = true)
public class DateToLocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date>{
	@Override
	public Date convertToDatabaseColumn(LocalDateTime attribute) {
		return attribute == null ? null : Date.from(attribute.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Converts from java.util.Date to java.time.LocalDateTime. If null is passed in, null will be returned.
	 *
	 * param dbData The Date to convert.
	 * @return dbData converted to a LocalDateTime.
	 */
	@Override
	public LocalDateTime convertToEntityAttribute(Date dbData) {
		return dbData == null ? null : LocalDateTime.ofInstant(dbData.toInstant(), ZoneId.systemDefault());
	}
}
