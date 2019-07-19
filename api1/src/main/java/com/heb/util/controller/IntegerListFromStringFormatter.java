/*
 * Copyright (c) 2015 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 * @author Phillip McGraw (m314029)
 */


package com.heb.util.controller;

/**
 * The type Integer list from string formatter.
 *
 * @author s573181
 * @since 2.0.0
 */
public class IntegerListFromStringFormatter extends ListFromStringFormatter<Integer> {

	/**
	 * Constructs a new IntegerListFromStringFormatter.
	 */
	public IntegerListFromStringFormatter() {
		super();
	}

	/**
	 * Creates an Integer from a String.
	 *
	 * @param parsedValue The String to convert to an Integer.
	 * @return ParsedValue converted to an Integer.
	 */
	@Override
	public Integer convert(String parsedValue) {
		return Integer.valueOf(parsedValue);
	}
}
