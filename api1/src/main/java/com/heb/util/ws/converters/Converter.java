package com.heb.util.ws.converters;

import java.lang.reflect.Field;

/**
 * Interface for classes than handle conversion between object types.
 *
 * Created by d116773 on 4/20/2016.
 */
public interface Converter {

	/**
	 * Returns true if the converter supports a particular type of conversion.
	 *
	 * @param destinationField A field that this object will try to convert an object to.
	 * @return True if this class supports the conversion and false otherwise.
	 */
	boolean supports(Field destinationField);

	/**
	 * Converts an object from one type to another.
	 * @param source The source object to convert from.
	 * @param destinationField The field to convert to.
	 * @param destination The object that contains the field to convert to.
	 * @throws IllegalAccessException
	 */
	void convert(Object source, Field destinationField, Object destination) throws IllegalAccessException;

}
