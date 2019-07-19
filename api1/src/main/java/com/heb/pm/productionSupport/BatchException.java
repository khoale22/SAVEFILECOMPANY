/*
 * BatchException
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productionSupport;

/**
 * Exception to throw related to batch jobs.
 *
 * @author d116773
 * @since 2.0.2
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
