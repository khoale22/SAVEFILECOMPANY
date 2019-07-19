package com.heb.util.ws.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Converts Strings to Longs or longs.
 *
 * Created by d116773 on 4/20/2016.
 */
@SuppressWarnings("rawtypes")
class StringToLong implements Converter {

	/**
	 * Takes a String as a parameter and will convert it to a Long or long
	 * depending on the target field.
	 * @param source The source destination to convert from. This does not technically have to be
	 *               a string, just something whose toString returns something parsable into a
	 *               long.
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

		if (t.equals(Long.TYPE) || c.equals(Long.class)) {
			return true;
		}
		return false;
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
	private void handleNotNull(Object source, Field destinationField, Object destination) throws IllegalAccessException {

		Long lValue;
		try {
			lValue = Long.parseLong(source.toString());
		} catch (NumberFormatException e) {
			throw new FieldConversionException(e.getMessage());
		}

		boolean accessible = destinationField.isAccessible();
		try {
			destinationField.setAccessible(true);
			if (destinationField.getGenericType().equals(Long.TYPE)) {
				long l = lValue.longValue();
				destinationField.setLong(destination, l);
			}
			if (destinationField.getType().equals(Long.class)) {
				destinationField.set(destination, lValue);
			}
		} catch (IllegalAccessException e) {
			throw e;
		}
		finally {
			destinationField.setAccessible(accessible);
		}
	}

	/**
	 * Handles setting nulls. Since a long cannot be null, if the target field is the primitive,
	 * then this function throws a UnsupportedConversionException.
	 * @param destinationField The field to set.
	 * @param destination The destination destination.
	 * @throws IllegalAccessException
	 * @throws FieldConversionException
	 */
	private void handleNull(Field destinationField, Object destination) throws IllegalAccessException {
		if (destinationField.getType().equals(Long.class)) {
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
			throw UnsupportedConversionException.getException(null, Long.TYPE);
		}



	}
}
