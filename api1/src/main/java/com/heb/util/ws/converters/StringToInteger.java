package com.heb.util.ws.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Converts Strings to Integers or ints.
 *
 * Created by d116773 on 4/20/2016.
 */
@SuppressWarnings("rawtypes")
class StringToInteger implements Converter {

	/**
	 * Takes a String as a parameter and will convert it to an Integer or it
	 * depending on the target field.
	 * @param source The source destination to convert from. This does not technically have to be
	 *               a string, just something whose toString returns something parsable into an
	 *               integer.
	 * @param destinationField The field to set.
	 * @param destination The destination destination.
	 * @throws IllegalAccessException
	 * @throws FieldConversionException
	 */
	@Override
	public void convert(Object source, Field destinationField, Object destination) throws IllegalAccessException {

		if (source == null) {
			this.handleNull(destinationField, destination);
		} else {
			this.handleNotNull(source, destinationField, destination);
		}
	}

	@Override
	public boolean supports(Field field) {

		if (field == null) {
			return false;
		}

		Type t = field.getGenericType();
		Class c = field.getType();

		if (t.equals(Integer.TYPE) || c.equals(Integer.class)) {
			return true;
		}
		return false;
	}

	/**
	 * Handles setting nulls. Since an int cannot be null, if the target field is the primitive,
	 * then this function throws a UnsupportedConversionException.
	 * @param destinationField The field to set.
	 * @param destination The destination destination.
	 * @throws IllegalAccessException
	 * @throws FieldConversionException
	 */
	private void handleNull(Field destinationField, Object destination) throws IllegalAccessException{
		if (destinationField.getType().equals(Integer.class)) {
			boolean accessible = destinationField.isAccessible();
			try {
				destinationField.setAccessible(true);
				destinationField.set(destination, null);
			} catch (IllegalAccessException e) {
				throw e;
			} finally {
				destinationField.setAccessible(accessible);
			}
		} else {
			throw UnsupportedConversionException.getException(null, Integer.TYPE);
		}
	}

	/**
	 * Handles setting the value when the object passed in is not null.
	 * @param source The source destination to convert from. This does not technically have to be
	 *               a string, just something whose toString returns something parsable into a
	 *               long.
	 * @param destinationField The field to set.
	 * @param destination The destination destination.
	 * @throws IllegalAccessException
	 * @throws FieldConversionException
	 */
	private void handleNotNull(Object source, Field destinationField, Object destination) throws IllegalAccessException{
		Integer intValue;
		try {
			intValue = Integer.parseInt(source.toString());
		} catch (NumberFormatException e) {
			throw new FieldConversionException(e.getMessage());
		}

		boolean accessible = destinationField.isAccessible();
		try {
			destinationField.setAccessible(true);
			if (destinationField.getGenericType().equals(Integer.TYPE)) {
				destinationField.setInt(destination, intValue.intValue());
			}
			if (destinationField.getType().equals(Integer.class)) {
				destinationField.set(destination, intValue);
			}
		} catch (IllegalAccessException e) {
			throw e;
		}
		finally {
			destinationField.setAccessible(accessible);
		}
	}
}
