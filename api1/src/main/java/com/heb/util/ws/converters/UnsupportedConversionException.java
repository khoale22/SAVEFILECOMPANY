package com.heb.util.ws.converters;

import java.lang.reflect.Type;
import java.util.Formatter;


/**
 * Thrown when the type conversion being asked for is not supported.
 *
 * Created by d116773 on 4/20/2016.
 */
public class UnsupportedConversionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// Used to construct the error messages.
	private static final String MESSAGE_FORMAT = "Conversion from %s to %s unsupported";

	/**
	 * Constructs a new UnsupportedConversionException.
	 * @param message The message for the exception to hold.
	 */
	protected UnsupportedConversionException(String message) {
		super(message);
	}

	/**
	 * Use this method to create a new UnsupportedConversionException when there is no
	 * additional exceptons.
	 * @param source The type you are trying to convert from.
	 * @param destination The type you are trying to convert to.
	 * @return The exception.
	 */
	public static UnsupportedConversionException getException(Type source, Type destination) {
		Formatter messageFormatter = new Formatter();
		String message = messageFormatter.format(UnsupportedConversionException.MESSAGE_FORMAT,
				source == null ? "null" : source.getTypeName(),
				destination.getTypeName()).toString();
		return new UnsupportedConversionException(message);
	}
}
