package com.heb.util.ws;

import java.util.List;
import java.util.function.Supplier;

/**
 * Converts an object of one type to an object of another type. It's main purpose is to allow
 * you to create a nicely constructed domain object and easily convert the generated web-service
 * classes to your object.
 *
 * To use this object, annotate the fields of your domain object with the MessageField annotation.
 * Set the sourceField parameter of the annotation to the property sourceField inside the generated web-service
 * class (without get or set). If your property is a List, you must also add the type of class
 * stored in the list as the innerType property of the annotation.
 *
 * The currently supported types of transition are to:
 * Integer
 * int
 * Long
 * long
 * String
 *
 * The only currently supported collection type is List.
 *
 * You can create new conversions by creating a Converter in the converters package and updating
 * the ConverterFactory to instantiate it.
 *
 * Created by d116773 on 4/19/2016.
 */
@SuppressWarnings("rawtypes")
public class ObjectConverter<SourceType, DestinationType>  {

	// The conversion logic lives in a delegate.
	private ObjectConverterDelegate objectConverter;

	// This object creates the actual object returned by the convert function.
	private final Supplier<? extends DestinationType> destinationCreator;

	/**
	 * Creates an ObjectConverter.
	 *
	 * @param destinationCreator A method to create objects of the DestinationType. The simplest thing
	 *                to pass in is your type::new. For example, if you want to instantiate
	 *                an object of type Product, pass in Product::new.
	 */
	public ObjectConverter(Supplier<? extends DestinationType> destinationCreator) {
		this(destinationCreator, new ObjectConverterImpl());
	}

	/**
	 * Creates an ObjectConverter.
	 * @param destinationCreator A method to create objects of the DestinationType. The simplest thing
	 *                to pass in is your type::new. For example, if you want to instantiate
	 *                an object of type Product, pass in Product::new.
	 * @param objectConverter Most of the logic to do the conversion is farmed out to this
	 *                        object. This allows this class to be type-safe since
	 *                        all of the reflection code cannot be aware of the
	 *                        type information as it is stripped out before runtime.
	 */
	public ObjectConverter(Supplier<? extends DestinationType> destinationCreator,
						   ObjectConverterDelegate objectConverter) {
		this.destinationCreator = destinationCreator;
		this.objectConverter = objectConverter;
	}

	/**
	 * Creates a new DestinationType object as a copy of source.
	 * @param source The object to copy into a new DestinationType.
	 * @return The new object.
	 * @throws com.heb.util.ws.converters.UnsupportedConversionException
	 * @throws com.heb.util.ws.converters.FieldConversionException
	 */
	public DestinationType convert(SourceType source) {

		// Return null if passed a null.
		if (source == null) {
			return null;
		}

		// Construct a new DestinationType.
		DestinationType destination = this.destinationCreator.get();

		// Use the Impl class to do the actual conversion.
		this.objectConverter.processClass(source, destination);

		return destination;
	}

	/**
	 * Allows user's of this class to override the default concrete List used by the
	 * converter (ArrayList). This cannot be null. If null is passed in, the default
	 * @param listConstructor A function for the object converter to use to create lists.
	 */
	public void setListConstructor(Supplier<? extends List> listConstructor) {
		// Don't allow it to be null
		if (listConstructor != null) {
			this.objectConverter.setListConstructor(listConstructor);
		}
	}
}
