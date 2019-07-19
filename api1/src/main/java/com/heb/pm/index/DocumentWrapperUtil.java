/*
 * WrapperUtil
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import java.util.Collection;

/**
 * Utility functions for conversion between wrapped object and the objects they wrap.
 * @author d116773
 */
public final class DocumentWrapperUtil {

	private static final String MISSING_SOURCE_ERROR_MESSAGE = "Source of conversion cannot be null.";
	private static final String MISSING_DESTINATION_ERROR_MESSAGE = "Destination of conversion cannot be null.";

	/**
	 * Private constructor so the object cannot be instantiated.
	 */
	private DocumentWrapperUtil() {}

	/**
	 * Takes an Iterable of the wrapper type as source and loads a collection of the data type from the source.
	 *
	 * @param source The Iterable with the wrapped object to copy.
	 * @param destination The Collection to copy the wrapped values to.
	 * @param <T> The type of data the wrapper stores.
	 * @param <K> The key type of th wrapper.
	 */
	public static <T, K> void toDataCollection (Iterable<? extends DocumentWrapper<T, K>> source ,
											 Collection<? super T> destination) {
		if (source == null) {
			throw new IllegalArgumentException(DocumentWrapperUtil.MISSING_SOURCE_ERROR_MESSAGE);
		}
		if (destination == null) {
			throw new IllegalArgumentException(DocumentWrapperUtil.MISSING_DESTINATION_ERROR_MESSAGE);
		}
		source.forEach((s) -> destination.add(s.getData()));
	}

	/**
	 * Extracts the data portion from a wrapped type. This is mainly here to save null checks in the calling code.
	 *
	 * @param source The wrapped object to get data from.
	 * @param <T> The type of data the wrapper stores.
	 * @param <K> The key type of th wrapper.
	 * @return The wrapped object. It will return null if source is null.
	 */
	public static <T, K> T toData(DocumentWrapper<T, K> source) {
		if (source == null) {
			return null;
		}
		return source.getData();
	}
}
