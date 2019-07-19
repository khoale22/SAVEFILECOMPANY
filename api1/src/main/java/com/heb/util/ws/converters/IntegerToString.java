/*
 * IntegerToString
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.ws.converters;

import java.lang.reflect.Field;

/**
 * @author d116773
 */
@SuppressWarnings("rawtypes")
public class IntegerToString implements Converter {

	@Override
	public boolean supports(Field destinationField) {
		if (destinationField == null) {
			return false;
		}

		return destinationField.getType().equals(String.class);
	}

	@Override
	public void convert(Object source, Field destinationField, Object destination) throws IllegalAccessException {
		if (!(source instanceof Integer)) {
			throw new FieldConversionException(source.getClass() + " is not supported.");
		}

		Integer i = (Integer)source;
		boolean accessible = destinationField.isAccessible();
		destinationField.setAccessible(true);

		destinationField.set(destination, i.toString());
		destinationField.setAccessible(accessible);
	}
}
