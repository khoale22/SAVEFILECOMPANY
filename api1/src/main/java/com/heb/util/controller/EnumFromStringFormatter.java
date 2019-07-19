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

package com.heb.util.controller;/*
  * Created by m314029 on 6/24/2016.
 */


import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * The type Enum from string formatter.
 */
public abstract class EnumFromStringFormatter <E extends Enum<E>> implements Formatter<E> {

	@Override
	public String print(E object, Locale locale) {
		return null;
	}

	@Override
	public E parse(String text, Locale locale) throws ParseException {
		return null;
	}
}