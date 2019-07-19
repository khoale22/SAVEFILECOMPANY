/*
 * LazyObjectResolver
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.jpa;

/**
 * Interface for classes that go through an entity object and loads properties that are lazily loaded. The intention
 * is that a controller would use one of these classes to make sure that properties that it is sending back have
 * values. To use this interfce, extend it for a particular data type and then access each property that is
 * lazily loaded. If that property is an object, access a property of that object. If that property is a collection,
 * then access the collection's size.
 *
 * @author d116773
 * @since 2.0.1
 * @param <DataType> The type of object the implementation of this interface resolves.
 */
public interface LazyObjectResolver<DataType> {

	/**
	 * Performs the actual resolution of lazily loaded parameters.
	 * @param d The object to resolve.
	 */
	void fetch(DataType d);
}
