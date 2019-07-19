/*
 *  CheckedSoapErrorException
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.ws;

/**
 * Unchecked exception to be thrown by webservice clients.
 *
 * @author s573181
 * @since 2.0.5
 */
public class CheckedSoapException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new Checked soap exception.
	 */
	public CheckedSoapException() { super();}

	/**
	 * Instantiates a new Checked soap exception.
	 *
	 * @param message the message
	 */
	public CheckedSoapException(String message) { super(message); }

	/**
	 * Instantiates a new Checked soap exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public CheckedSoapException(String message, Throwable cause) { super(message, cause); }

	/**
	 * Instantiates a new Checked soap exception.
	 *
	 * @param cause the cause
	 */
	public CheckedSoapException(Throwable cause) { super(cause); }

}
