/*
 * com.heb.pm.StatusController
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.monitor;

import com.heb.pm.ApiConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * REST endpoint that provides a status for the application.
 *
 * @author d116773
 * @since 2.0.0
 */
@RestController
@RequestMapping(ApiConstants.STATUS_URL)
public class StatusController {

	private static final String LOG_MESSAGE = "Received status request from IP %s";
	private static final String ERROR_LOOKING_UP_HOST_MESSAGE = "Error looking up local host name: %s";
	private static final String DEFAULT_HOST_NAME = "Unknown";

	private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

	@Value("${app.version}")
	private String applicationVersion;

	@Value("${app.domainNameFilter}")
	private String domainNameFilter;

	private String applicationServerName;

	/**
	 * Wraps the response to the status request so that it has a JSON format.
	 */
	protected class StatusWrapper {

		private String version;
		private String applicationServerName;

		/**
		 * Creates a new StatusWrapper.
		 *
		 * @param version The version number of the application.
		 * @param applicationServerName The name of the server serving up information.
		 */
		public StatusWrapper(String version, String applicationServerName) {
			this.version = version;
			this.applicationServerName = applicationServerName;
		}

		/**
		 * Returns the version number of the application.
		 *
		 * @return The version number of the application.
		 */
		public String getVersion() {
			return this.version;
		}

		/**
		 * Returns the name of the server serving up information.
		 *
		 * @return The name of the server serving up information.
		 */
		public String getApplicationServerName() {
			return applicationServerName;
		}
	}

	/**
	 * Implements the REST endpoint to return the status of the application.
	 * The response is meant to be interpreted through HTTP return codes. 200
	 * means the application is in a good state. Anything else should be
	 * interpreted as an error. With a status of 200, the text "OK" is returned
	 * by the method. For any other HTTP return code, the String returned
	 * can be treated as an error message.
	 *
	 * @param request The HttpServletRequest that initiated this call.
	 * @return A message with status information.
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody public StatusWrapper	getStatus(HttpServletRequest request) {

		StatusController.logger.debug(String.format(StatusController.LOG_MESSAGE, request.getRemoteAddr()));

		return new StatusWrapper(this.applicationVersion, this.getApplicationServerName());
	}

	/**
	 * Sets the application's version number.
	 *
	 * @param applicationVersion The application's version number.
	 */
	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	/**
	 * Returns the name of the server this application is running on.
	 *
	 * @return The name of the server this application is running on.
	 */
	private String getApplicationServerName() {
		if (this.applicationServerName == null) {
			try {
				this.applicationServerName =
						InetAddress.getLocalHost().getHostName().replace(this.domainNameFilter, StringUtils.EMPTY);
			} catch (UnknownHostException e) {
				StatusController.logger.error(
						String.format(StatusController.ERROR_LOOKING_UP_HOST_MESSAGE, e.getMessage()));
				this.applicationServerName = StatusController.DEFAULT_HOST_NAME;
			}
		}
		return this.applicationServerName;
	}
}
