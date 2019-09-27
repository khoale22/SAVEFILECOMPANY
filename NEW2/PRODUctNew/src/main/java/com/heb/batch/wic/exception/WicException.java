/*
 *  WicException
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.exception;

/**
 * @author vn55306
 *
 */
public class WicException extends Exception {

	private static final long serialVersionUID = -2936242847851749733L;
	public static final String FILE_NOT_FOUND = "File Not Found.";

	/**
	 * @param message
	 */
	public WicException(String message){
		super(message);
	}

	public WicException(Exception exception){
		super(exception);
	}
}
