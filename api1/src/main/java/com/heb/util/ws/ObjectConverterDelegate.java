package com.heb.util.ws;

import java.util.List;
import java.util.function.Supplier;

/**
 * Interface for a class that converts from one type of object to another.
 * Created by d116773 on 4/25/2016.
 */
@SuppressWarnings("rawtypes")
public interface ObjectConverterDelegate {
	/**
	 * Converts object source into object destination. Both objects must
	 * be created.
	 * @param source The object to be copied.
	 * @param destination The object to copy into.
	 */
	public void processClass(Object source, Object destination);

	/**
	 * Allows users of the delegate to change type type of Lists that the delegate
	 * will construct.
	 * @param listConstructor An object that creates a List.
	 */
	public void setListConstructor(Supplier<? extends List> listConstructor);
}
