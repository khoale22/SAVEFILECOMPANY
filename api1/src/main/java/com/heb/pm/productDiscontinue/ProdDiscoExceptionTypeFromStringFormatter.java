/*
 *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 *
 */

package com.heb.pm.productDiscontinue;


import com.heb.pm.entity.ProductDiscontinueExceptionType;
import org.springframework.format.Formatter;

import java.util.Locale;

/**
 * The type ProdDiscoExceptionType from string formatter.
 * @author m314029
 * @since 2.0.2
 */
public class ProdDiscoExceptionTypeFromStringFormatter implements Formatter<ProductDiscontinueExceptionType> {

	/**
	 * Parses the String passed in into a ProdDiscoExceptionType. If the string is null or the enum isn't
	 * found then an IllegalArgumentException is thrown.
	 *
	 * @param stringToParse The String to parse.
	 * @param locale The Locale used.
	 * @return a ProdDiscoExceptionType parsed from stringToParse.
	 */
	@Override
	public ProductDiscontinueExceptionType parse(String stringToParse, Locale locale) {
		if(stringToParse != null) {
			return ProductDiscontinueExceptionType.fromString(stringToParse);
		}
		throw new IllegalArgumentException("No constant with null value found");
	}

	/**
	 * Takes a ProdDiscoExceptionType and turns it into a string.
	 *
	 * @param exceptionType The ProdDiscoExceptionType to turn into a String.
	 * @param locale The Locale used.
	 * @return A String representation of the List.
	 */
	@Override
	public String print(ProductDiscontinueExceptionType exceptionType, Locale locale) {
		return exceptionType.toString();
	}
}