/*
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

/**
 * Stores cost information about an item.
 *
 * @author d116773
 * @since 2.6.0
 */
public class Cost {

	private double cost;

	/**
	 * Returns the dollar amount of the cost.
	 *
	 * @return The dollar amount of the cost.
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Sets the dollar amount of the cost.
	 *
	 * @param cost The dollar amount of the cost.
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "Cost{" +
				"cost=" + cost +
				'}';
	}
}
