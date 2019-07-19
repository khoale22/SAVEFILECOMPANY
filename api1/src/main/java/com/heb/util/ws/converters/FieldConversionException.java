package com.heb.util.ws.converters;

/**
 * Thrown when an error happens during conversion.
 *
 * Created by d116773 on 4/20/2016.
 */
public class FieldConversionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a FieldConversionException.
	 * @param t The raw exception.
	 */
	public FieldConversionException(Throwable t) {
		super(t);
	}

	/**
	 * Constructs a new FieldConversionException.
	 * @param s A message for the exception.
	 */
	public FieldConversionException(String s) {
		super(s);
	}
}
