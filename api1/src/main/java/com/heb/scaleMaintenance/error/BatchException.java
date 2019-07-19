/*
 * BatchException
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.scaleMaintenance.error;

/**
 * Exception to throw related to batch jobs.
 *
 * @author m314029
 * @since 2.17.8
 */
public class BatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new BatchException.
	 *
	 * @param message The message for the exception.
	 */
	public BatchException(String message) {
		super(message);
	}
}
