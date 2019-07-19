/*
 *  StreamingExportException
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.controller;

/**
 * Exception that can be thrown to indicate a problem happened when streaming an export.
 * @author s573181
 * @since 2.0.7
 */
public class StreamingExportException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new StreamingExportException.
	 *
	 * @param message A description of the error.
	 */
	public StreamingExportException(String message) {
		super(message);
	}

	/**
	 * Constructs a new StreamingExportException.
	 *
	 * @param message A description of the error.
	 * @param throwable The base cause of the exception.
	 */
	public StreamingExportException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
