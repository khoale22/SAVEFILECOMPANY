package com.heb.util.ws.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * Converts BigIntegers to Longs or longs.
 *
 * Created by d116773 on 4/20/2016.
 */
@SuppressWarnings("rawtypes")
class BigIntegerToLong implements Converter {

	// used to determine if the BigInteger will convert
	private static final BigInteger LARGEST_VALUE = BigInteger.valueOf(Long.MAX_VALUE);
	private static final BigInteger SMALLEST_VALUE = BigInteger.valueOf(Long.MIN_VALUE);

	/**
	 * Converts a BigInteger to a Long or a long.
	 * @param source The source destination to convert from. This must extend BigInteger.
	 * @param destinationField The destinationField to set. It must be a Long or a long.
	 * @param destination The destination containing the destinationField to set.
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
	 * Setting an a Long to a null is delegated here. It throws an error when it is long because null
	 * cannot be converted to a primitive.
	 * @param destinationField The destinationField to set to null.
	 * @param destination The destination that contains the destinationField.
	 * @throws IllegalAccessException
	 */
	private void handleNull(Field destinationField, Object destination) throws IllegalAccessException {
		if (destinationField.getGenericType().equals(Long.TYPE)) {
			throw UnsupportedConversionException.getException(null, Long.TYPE);
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
	 * Setting the destination to a value is delegated to this function.
	 * @param source The BigInteger to convert to Long or long.
	 * @param destinationField The destinationField to set. It must be a Long or long.
	 * @param destination The destination that contains the destinationField.
	 * @throws IllegalAccessException
	 */
	private void handleNotNull(Object source, Field destinationField, Object destination) throws IllegalAccessException {
		if (!(source instanceof BigInteger)) {
			throw new FieldConversionException(source.getClass() + " is not supported");
		}
		BigInteger bi = (BigInteger) source;

		if (bi.compareTo(BigIntegerToLong.LARGEST_VALUE) == 1) {
			throw new FieldConversionException(bi.toString() + " is too large to convert to Long.");
		}
		if (bi.compareTo(BigIntegerToLong.SMALLEST_VALUE) == -1) {
			throw new FieldConversionException(bi.toString() + " is too small to convert to Long.");
		}

		Long lValue = bi.longValue();

		boolean accessible = destinationField.isAccessible();

		try {
			destinationField.setAccessible(true);
			if (destinationField.getGenericType().equals(Long.TYPE)) {
				destinationField.setLong(destination, lValue.longValue());
			} else if (destinationField.getType().equals(Long.class)) {
				destinationField.set(destination, lValue);
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
}
