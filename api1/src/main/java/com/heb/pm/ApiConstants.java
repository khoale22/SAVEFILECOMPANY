/*
 * com.heb.pm.ApiConstants
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm;

/**
 * Class containing constants related to exposure of the application to the outside world.
 *
 * @author d116773
 * @since 2.0.0
 */
public final class ApiConstants {
	/**
	 * Private so the class cannot be instantiated.
	 */
	private ApiConstants() {}

	/**
	 * The URL to expose the application status endpoint.
	 */
	public static final String STATUS_URL = "${app.permitAllUrlBase}";

	/**
	 * The base URL for all functions.
	 */
	public static final String BASE_APPLICATION_URL = "${app.secureUrlBase}";
	/**
	 * The base URL for all WSAG functions.
	 */
	public static final String BASE_WSAG_APPLICATION_URL = "${app.permitAllWsagUrlBase}";
}
