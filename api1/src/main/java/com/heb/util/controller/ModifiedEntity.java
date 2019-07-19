/*
 * ModifiedEntity
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.controller;

/**
 * Holds a value and message. The idea is that you want to send a result and a message back to the front
 * end so you can store your messages for the user here.
 *
 * @author d116773
 * @since 2.0.2
 * @param <T> The type of data to hold.
 */
public class ModifiedEntity<T> {

	private String message;
	private T data;

	/**
	 * Constructs a new ModifiedEntity.
	 *
	 * @param data The data for this object to hold.
	 * @param message The message for the front end.
	 */
	public ModifiedEntity(T data, String message) {
		this.message = message;
		this.data = data;
	}

	/**
	 * Returns the message.
	 *
	 * @return The message for the front end.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the data for the front end.
	 *
	 * @return The data for the front end.
	 */
	public T getData() {
		return data;
	}
}