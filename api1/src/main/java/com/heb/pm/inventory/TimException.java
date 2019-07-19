/*
 * TimException
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.inventory;

/**
 * Exception to be thrown when any error occurs contacting TIM.
 *
 * @author d116773
 * @since 2.0.1
 */
public class TimException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new TimException.
	 *
	 * @param message A description of the error that happened.
	 * @param cause What caused the exception.
	 */
	public TimException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new TimException.
	 *
	 * @param message A description of the error that happened.
	 */
	public TimException(String message) {
		super(message);
	}
}
