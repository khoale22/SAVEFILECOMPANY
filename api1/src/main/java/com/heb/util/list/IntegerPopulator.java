/*
 * com.heb.util.list.IntegerPopulator
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.util.list;

/**
 * Convenience object that extends ListPopulator for lists of Integers. All created Integers will hold the same value.
 *
 * @author d116773
 * @since 2.0.0
 */
public class IntegerPopulator extends ListPopulator<Integer> {

	private static final int DEFAULT_VALUE = 0;

	/**
	 * Constructs a new IntegerPopulator. If this constructor is called, all Integers will hold zero.
	 */
	public IntegerPopulator() {
		this(IntegerPopulator.DEFAULT_VALUE);
	}

	/**
	 * Constructs a new IntegerPopulator. This constructor takes a parameter of a value which all created Integers
	 * will hold.
	 *
	 * @param defaultValue The value of the Integer to create when adding to the list.
	 */
	public IntegerPopulator(int defaultValue) {
		super(() ->Integer.valueOf(defaultValue));
	}
}
