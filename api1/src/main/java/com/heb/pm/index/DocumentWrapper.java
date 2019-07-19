/*
 * IndexableDocumentWrapper
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import org.springframework.data.annotation.Id;

/**
 * The Spring Elasticsearch project only supports documents that have a key of types Integer, Long, Float, or String
 * (at least they must be assignable to those types). This class serves as a wrapper around objects to be stored in
 * an index that do not have a key of that type. It does not provide much functionality, just consistency.
 *
 * Extend this class and set the type of data to store and the key. You will need to provide a default constructor for
 * the object to be read from the index. A copy constructor is provided for convenience. Implement the toKey method
 * to set the key for this object in the index.
 *
 * @param <T> The raw type this wrapper should store.
 * @param <K> The type to use as the key in the index.
 * @author d116773
 * @since 2.0.2
 *
 * */
public abstract class DocumentWrapper<T, K>{

	@Id
	private K key;
	private T data;

	/**
	 * Constructs a new DocumentWrapper.
	 */
	public DocumentWrapper() {}

	/**
	 * Constructs a new DocumentWrapper. This constructor will call toKey passing in the object passed as a parameter
	 * to set the key and will store the object passed in as a parameter as data.
	 *
	 * @param data The data for this class to hold.
	 */
	public DocumentWrapper(T data) {
		this.key = this.toKey(data);
		this.data = data;
	}

	/**
	 * Returns the key for the object.
	 *
	 * @return The key for the object.
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Sets the key for the object.
	 *
	 * @param key The key for the object.
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * Gets the data this object is wrapping.
	 *
	 * @return The data this object is wrapping.
	 */
	public T getData() {
		return data;
	}

	/**
	 * Sets the data this object is wrapping.
	 *
	 * @param data The data this object is wrapping.
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return data.toString();
	}

	/**
	 * Retruns an appropriate key for for this object based on the object's data.
	 *
	 * @param data The data this object will store.
	 * @return A unique key for this object based on the data it is wrapping.
	 */
	protected abstract K toKey(T data);
}
