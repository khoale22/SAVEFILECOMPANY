package com.heb.util.ws;

import com.heb.util.ws.converters.Converter;
import com.heb.util.ws.converters.ConverterFactory;
import com.heb.util.ws.converters.FieldConversionException;
import com.heb.util.ws.converters.UnsupportedConversionException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Handles the logic to do an object conversion. It was placed here in a package level class
 * because all of the reflection is running at runtime with all the type information
 * stripped out. I wanted to have the exposed converter be type-safe from a construction
 * perspective. Since the generic type information is not available at runtime, then the
 * actual work to construct and convert objects cannot rely on it.
 *
 * Created by d116773 on 4/21/2016.
 */
@SuppressWarnings("rawtypes")
class ObjectConverterImpl implements ObjectConverterDelegate {

	// The prefix for a getter method.
	private static final String GETTER_METHOD_PREFIX = "get";

	// Returns classes that does field level conversions.
	private final ConverterFactory converterFactory = new ConverterFactory();

	// Used to construct List objects when needed. Can be overridden if you
	// don't want ArrayList.
	private Supplier<? extends List> listConstructor = ArrayList::new;

	/**
	 * Does the work to process a whole class.
	 * @param source The object to copy.
	 * @param destination The object to copy to.
	 * @return The copied object.
	 * @throws com.heb.util.ws.converters.FieldConversionException
	 * @throws com.heb.util.ws.converters.UnsupportedConversionException
	 */
	@Override
	public void processClass(Object source, Object destination) {

		Field[] fields = destination.getClass().getDeclaredFields();

		// Loop through all the fields in the destination object.
		// If it is annotated with MessageField, copy it.
		for (Field field : fields) {
			if (field.isAnnotationPresent(MessageField.class)) {
				try {

					this.processField(field, destination, source);

					// Convert all the exceptions to a runtime exception.
				} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
					throw new FieldConversionException(e.getCause());
				}
			}
		}

	}

	/**
	 * Handles copying an individual field.
	 *
	 * @param destinationField The field to copy into.
	 * @param destination The object to copy into.
	 * @param source The source object to copy.
	 *
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public void processField(Field destinationField, Object destination, Object source) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		MessageField annotation = destinationField.getAnnotation(MessageField.class);

		// If the field to copy into is a list, it needs to be handled differently.
		if (List.class.isAssignableFrom(destinationField.getType())) {

			this.processCollection(destinationField, destination, annotation, source);

		// If the field is not a list, then copy it.
		} else {

			Object sourceValue = this.getSourcePropertyValue(annotation, source);
			Converter converter = this.converterFactory.getConverter(sourceValue, destinationField);
			converter.convert(sourceValue, destinationField, destination);

		}
	}

	/**
	 * Returns the getter method on the source object for a particular field.
	 *
	 * @param destinationAnnotation The annotation on the field in the destination object.
	 * @param source The source object you want the getter from.
	 * @return The getter method.
	 *
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public Object getSourcePropertyValue(MessageField destinationAnnotation, Object source) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

		String propertyFunctionName = ObjectConverterImpl.GETTER_METHOD_PREFIX + destinationAnnotation.sourceField();
		Method method = source.getClass().getMethod(propertyFunctionName);
		return method.invoke(source);

	}

	/**
	 * Handles copying a field that implements List.
	 * @param field The field to copy into.
	 * @param destination The object to copy into.
	 * @param destinationAnnotation The annotation on the field to copy into.
	 * @param source The object to copy from.
	 *
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	public void processCollection(Field field, Object destination, MessageField destinationAnnotation, Object source ) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

		boolean accessible = field.isAccessible();

		try {

			List destinationList = this.listConstructor.get();
			field.setAccessible(true);
			field.set(destination, destinationList);

			Object obj = this.getSourcePropertyValue(destinationAnnotation, source);
			if (List.class.isAssignableFrom(obj.getClass())) {
				List sourceList = (List) obj;

				for (Object aSourceList : sourceList) {
					Object innerDestination = destinationAnnotation.innerType().newInstance();
					this.processClass(aSourceList, innerDestination);
					destinationList.add(innerDestination);
				}
			} else {
				throw UnsupportedConversionException.getException(obj.getClass(), field.getGenericType());
			}
		} catch(IllegalAccessException | NoSuchMethodException | InstantiationException e) {
			throw e;
		}
		finally {
			field.setAccessible(accessible);
		}
	}

	/**
	 * Allows users of this class to override the types of Lists
	 * that are created.
	 * @param listConstructor An function that creates lists.
	 */
	@Override
	public void setListConstructor(Supplier<? extends List> listConstructor) {
		this.listConstructor = listConstructor;
	}
}
