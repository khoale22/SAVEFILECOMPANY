/*
 * SoapException
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.ws;

/**
 * Exception that can be thrown to indicate a problem happened with a SOAP call.
 *
 * @author d116773
 * @since 2.0.1
 */
public class SoapException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new SoapException.
	 *
	 * @param message A description of the error.
	 */
	public SoapException(String message) {
		super(message);
	}

	/**
	 * Constructs a new SoapException.
	 *
	 * @param message A description of the error.
	 * @param t The base cause of the exception.
	 */
	public SoapException(String message, Throwable t) {
		super(message, t);
	}
}
