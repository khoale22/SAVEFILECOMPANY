/*
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

/**
 * Holds information about a retail.
 *
 * @author d116773
 * @since 2.6.0
 */
public class Retail {

	private static final String RETAIL_FORMAT = "%d/%.2f";

	private float retail;
	private int xFor;

	/**
	 * Returns the dollar portion of a retail. E.g., when an item is 3 for $5.50, this is the 5.50.
	 *
	 * @return The dollar portion of a retail.
	 */
	public float getRetail() {
		return retail;
	}

	/**
	 * Sets the dollar portion of a retail.
	 *
	 * @param retail The dollar portion of a retail.
	 */
	public void setRetail(float retail) {
		this.retail = retail;
	}

	/**
	 * Returns the x for portion of a retail. E.g., when an item is 3 for $5.50, this is the 3.
	 *
	 * @return The x for portion of a retail.
	 */
	public int getxFor() {
		return xFor;
	}

	/**
	 * Sets the x for portion of a retail.
	 *
	 * @param xFor The x for portion of a retail.
	 */
	public void setxFor(int xFor) {
		this.xFor = xFor;
	}

	/**
	 * Returns a string representation of the retail.
	 *
	 * @return A string representation of the retail.
	 */
	@Override
	public String toString() {
		return String.format(Retail.RETAIL_FORMAT, this.xFor, this.retail);
	}
}
