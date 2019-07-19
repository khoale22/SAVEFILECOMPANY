package com.heb.util.ws.converters;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * Constructs converters.
 *
 * Created by d116773 on 4/20/2016.
 */
public class ConverterFactory {

	// All the converters available to return.
	private StringToString stringToString = new StringToString();
	private StringToLong stringToLong = new StringToLong();
	private StringToInteger stringToInteger = new StringToInteger();
	private BigIntegerToInteger bigIntegerToInteger = new BigIntegerToInteger();
	private BigIntegerToLong bigIntegerToLong = new BigIntegerToLong();
	private IntegerToInteger integerToInteger = new IntegerToInteger();
	private IntegerToString integerToString = new IntegerToString();

	/**
	 * Returns a converter that will convert from the type of source to the type
	 * of destination.
	 *
	 * @param source The source object you want to convert.
	 * @param destination The field you want to convert to.
	 * @return A converter that will perform the conversion.
	 * @throws UnsupportedConversionException
	 */
	public Converter getConverter(Object source, Field destination) {

		// For null, the only thing that really matters is what to convert it to
		// as all the converters support setting a null
		if (source == null || source instanceof String) {
			if (this.stringToString.supports(destination)) {
				return this.stringToString;
			}
			if (this.stringToLong.supports(destination)) {
				return this.stringToLong;
			}
			if (this.stringToInteger.supports(destination)) {
				return this.stringToInteger;
			}
		}

		if (source instanceof BigInteger) {
			if (this.bigIntegerToLong.supports(destination)) {
				return this.bigIntegerToLong;
			}
			if (this.bigIntegerToInteger.supports(destination)) {
				return this.bigIntegerToInteger;
			}
		}

		if (source instanceof Integer) {
			if (this.integerToInteger.supports(destination)) {
				return this.integerToInteger;
			}
			if (this.integerToString.supports(destination)) {
				return this.integerToString;
			}
		}

		throw UnsupportedConversionException.getException(source.getClass(), destination.getGenericType());
	}
}
