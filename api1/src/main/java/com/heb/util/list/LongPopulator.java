/*
 * com.heb.util.list.LongPopulator
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.util.list;

/**
 * Convenience object that extends ListPopulator for lists of Longs. All created Longs will hold the same value.
 *
 * @author d116773
 * @since 2.0.0
 */
public class LongPopulator extends ListPopulator<Long> {

	public static final int DEFAULT_VALUE = 0;

	/**
	 * Constructs a new LongPopulator. If this constructor is called, all Longs will hold zero.
	 */
	public LongPopulator() {
		this(LongPopulator.DEFAULT_VALUE);
	}

	/**
	 * Constructs a new LongPopulator. This constructor takes a parameter of a value which all created Longs
	 * will hold.
	 *
	 * @param defaultValue The value of the Integer to create when adding to the list.
	 */
	public LongPopulator(int defaultValue) {
		super(() ->Long.valueOf(defaultValue));
	}
}
