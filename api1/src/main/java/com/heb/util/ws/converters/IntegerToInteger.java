/*
 * IntegerToInt
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.ws.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Handles converting from Integer to Integer or int. This does not seem necessary, but, because all the setting
 * is done through reflection, it needs to be done through a function rather than through direct conversion.
 *
 * @author d116773
 * @since 2.0.2
 */
@SuppressWarnings("rawtypes")
public class IntegerToInteger implements Converter {

	/**
	 * Returns wheter or not conversion to a particular field is supported by this class.
	 *
	 * @param destinationField A field that this object will try to convert an object to.
	 * @return True if the field is supported and false otherwise.
	 */
	@Override
	public boolean supports(Field destinationField) {
		if (destinationField == null) {
			return false;
		}
		Type t = destinationField.getGenericType();
		Class c = destinationField.getType();

		if (t.equals(Integer.TYPE) || c.equals(Integer.class)) {
			return true;
		}
		return false;
	}

	/**
	 * Sets an Integer or int field from a passed in Integer.
	 *
	 * @param source The source object to convert from.
	 * @param destinationField The field to convert to.
	 * @param destination The object that contains the field to convert to.
	 * @throws IllegalAccessException
	 */
	@Override
	public void convert(Object source, Field destinationField, Object destination) throws IllegalAccessException {
		if (source == null) {
			this.handleNull(destinationField, destination);
		} else {
			this.handleNotNull(source, destinationField, destination);
		}
	}

	/**
	 * Handles conversion to null. If the field is an int, then the function throws an error.
	 * If the field is an Integer, then it is set to null.
	 *
	 * @param destinationField The field to convert to.
	 * @param destination The object that contains the field to convert to.
	 * @throws IllegalAccessException
	 */
	private void handleNull(Field destinationField, Object destination) throws IllegalAccessException {
		if (destinationField.getGenericType().equals(Integer.TYPE)) {
			throw UnsupportedConversionException.getException(null, Integer.TYPE);
		}
		boolean accessible = destinationField.isAccessible();
		destinationField.setAccessible(true);
		try {
			destinationField.set(destination, null);
		} catch (IllegalAccessException e) {
			throw e;
		} finally {
			destinationField.setAccessible(accessible);
		}
	}

	/**
	 * Sets an Integer or int field from a passed in Integer.
	 *
	 * @param source The source object to convert from.
	 * @param destinationField The field to convert to.
	 * @param destination The object that contains the field to convert to.
	 * @throws IllegalAccessException
	 */
	private void handleNotNull(Object source, Field destinationField, Object destination)
			throws IllegalAccessException {

		if (!(source instanceof Integer)) {
			throw new FieldConversionException(source.getClass() + " is not supported.");
		}

		Integer i = (Integer)source;

		boolean accessible = destinationField.isAccessible();
		destinationField.setAccessible(true);

		try {
			if (destinationField.getGenericType().equals(Integer.TYPE)) {
				destinationField.setInt(destination, i.intValue());
			} else if (destinationField.getType().equals(Integer.class)) {
				destinationField.set(destination, i);
			} else {
				throw UnsupportedConversionException.getException(Integer.class, destinationField.getGenericType());
			}
		} catch (IllegalAccessException e) {
			throw e;
		}
		finally {
			destinationField.setAccessible(accessible);
		}
	}
}
