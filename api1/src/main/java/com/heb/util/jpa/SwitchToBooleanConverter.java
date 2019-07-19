/*
 * com.heb.util.jpa.SwitchToBooleanConverter
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.util.jpa;

import org.apache.commons.lang.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter used to convert standard HEB switches to Booleans.
 *
 * @author d116773
 * @since 2.0.0
 */
@Converter(autoApply = true)
public class SwitchToBooleanConverter implements AttributeConverter<Boolean, String> {

	private static final String YES = "Y";
	private static final String NO = "N";

	private static final String INVALID_SWITCH_MESSAGE = "Y and N are the only supported values for switches.";

	/**
	 * Takes a boolean and converts it to the standard HEB switch. If a null is passed in,
	 * it is assumed to be false.
	 *
	 * @param attribute The value to convert to a switch.
	 * @return Y if attribute is true and false otherwise.
	 */
	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return attribute == null || !attribute ? SwitchToBooleanConverter.NO : SwitchToBooleanConverter.YES;
	}

	/**
	 * Takes a standard HEB switch an converts it to a boolean. The function will only convert
	 * the strings Y or N and a null. Null and N are converted to false. Y is converted to true. Any
	 * other value will throw an exception.
	 *
	 * @param dbData The string to convert to a boolean.
	 * @return True if dbData is Y. False if dbData is N or null.
	 */
	@Override
	public Boolean convertToEntityAttribute(String dbData) {

		if (StringUtils.isBlank(dbData) || SwitchToBooleanConverter.NO.equals(dbData.trim().toUpperCase())) {
			return false;
		}
		if (SwitchToBooleanConverter.YES.equals(dbData.trim().toUpperCase())) {
			return true;
		}

		throw new IllegalArgumentException(SwitchToBooleanConverter.INVALID_SWITCH_MESSAGE);
	}
}
