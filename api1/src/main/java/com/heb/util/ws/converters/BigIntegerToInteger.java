package com.heb.util.ws.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * Converts BigInteger objects to either Integers or ints.
 *
 * Created by d116773 on 4/19/2016.
 */
@SuppressWarnings("rawtypes")
class BigIntegerToInteger implements Converter {

	private static final BigInteger LARGEST_VALUE = BigInteger.valueOf(Integer.MAX_VALUE);
	private static final BigInteger SMALLEST_VALUE = BigInteger.valueOf(Integer.MIN_VALUE);
	/**
	 * Takes a BigInteger and will convert it either to an Integer or an int
	 * depending on the type of filed passed in.
	 * @param source The source object to convert from.
	 * @param destinationField The field in the destination object to copy to.
	 * @param destination The destination object.
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

	private void handleNotNull(Object source, Field destinationField, Object destination) throws IllegalAccessException {
		if (!(source instanceof BigInteger)) {
			throw new FieldConversionException(source.getClass() + " is not supported.");
		}

		BigInteger bi = (BigInteger)source;
		if (bi.compareTo(BigIntegerToInteger.LARGEST_VALUE) == 1) {
			throw new FieldConversionException(bi.toString() + " too large to fit in Integer");
		}

		if (bi.compareTo(BigIntegerToInteger.SMALLEST_VALUE) == -1) {
			throw new FieldConversionException(bi.toString() + " too small to fit in Integer");
		}
		Integer intValue = bi.intValue();

		boolean accessible = destinationField.isAccessible();
		destinationField.setAccessible(true);

		try {
			if (destinationField.getGenericType().equals(Integer.TYPE)) {
				destinationField.setInt(destination, intValue.intValue());
			} else if (destinationField.getType().equals(Integer.class)) {
				destinationField.set(destination, intValue);
			} else {
				throw UnsupportedConversionException.getException(BigInteger.class, destinationField.getGenericType());
			}
		} catch (IllegalAccessException e) {
			throw e;
		}
		finally {
			destinationField.setAccessible(accessible);
		}
	}

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
}
