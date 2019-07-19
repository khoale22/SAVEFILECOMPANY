/*
 *  UnexpectedInputException
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload;

/**
 * This is UnexpectedInputException class.
 *
 * @author vn55306
 * @since 2.8.0
 */
public class UnexpectedInputException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a FieldConversionException.
     *
     * @param t The raw exception.
     */
    public UnexpectedInputException(Throwable t) {
        super(t);
    }

    /**
     * Constructs a new FieldConversionException.
     *
     * @param s A message for the exception.
     */
    public UnexpectedInputException(String s) {
        super(s);
    }
}
