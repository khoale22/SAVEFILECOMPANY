/*
 *  ProductFeeder
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.massUpdate.job;

/**
 * This is an interface for product feeder. This allows different product feeders to be created to read different types
 * of data.
 *
 * @author l730832
 * @since 2.17.0
 */
public interface ProductFeeder {

	/**
	 * Initialization of the product feeder.
	 */
	void init();

	/**
	 * This will read whatever type of data is being processed in its respective product feeder.
	 * @return
	 */
	Long read();
}
