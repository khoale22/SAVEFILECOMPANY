/*
 *  CountryController
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.country;


import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Country;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents country information.
 *
 * @author s573181
 * @since 2.5.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CountryController.COUNTRY_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class CountryController {

	private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

	protected static final String COUNTRY_URL = "/country";
	protected static final String FIND_ALL_ORDER_BY_NAME_URL = "/allCountriesOrderByName";

	// Log messages.
	private static final String FIND_ALL_MESSAGE = "User %s from IP %s requested all countries.";

	@Autowired
	private CountryService service;
	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Returns a list of all countries ordered by country name.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return A List of all countries ordered by country name.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CountryController.FIND_ALL_ORDER_BY_NAME_URL)
	public List<Country> findAllOrderByCountryName(HttpServletRequest request){
		this.logFindAllByCountryNameRequest(request.getRemoteAddr());
		return this.service.findAllOrderByCountryName();
	}

	/**
	 * Logs a users request for all ingredient statements.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logFindAllByCountryNameRequest(String ipAddress) {
		CountryController.logger.info(String.format(CountryController.FIND_ALL_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}
}
