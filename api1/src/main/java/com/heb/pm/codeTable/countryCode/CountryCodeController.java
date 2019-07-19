/*
 *  CountryCodesController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.countryCode;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Country;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for functions related to get the list of country codes.
 *
 * @author vn70529
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CountryCodeController.COUNTRY_CODE_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_COUNTRY_CODE)
public class CountryCodeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountryCodeController.class);
	protected static final String COUNTRY_CODE_URL = "/countryCode";
	protected static final String FIND_ALL_ORDER_BY_ID_URL = "/getAllCountriesOrderById";
	private static final String ADD_NEW_COUNTRIES = "/addNewCountries";
	private static final String UPDATE_COUNTRIES = "/updateCountries";
	private static final String DELETE_COUNTRIES = "/deleteCountries";
	/**
	 *  Log messages.
	 */
	private static final String FIND_ALL_LOG_MESSAGE = "User %s from IP %s requested all countries.";
	private static final String ADD_NEW_COUNTRIES_LOG_MESSAGE = "User %s from IP %s requested add new the list of Country Codes [%s].";
	private static final String UPDATE_COUNTRIES_LOG_MESSAGE = "User %s from IP %s requested edit the list of Country Codes [%s].";
	private static final String DELETE_COUNTRIES_LOG_MESSAGE = "User %s from IP %s requested add new the list of Country Codes [%s].";
	/**
	 * Holds the message to show when add new or update or delete success.
	 */
	private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";
	@Autowired
	private CountryCodeService countryCodeService;
	@Autowired
	private UserInfo userInfo;
	/**
	 * Returns a list of all countries ordered by country id.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return A List of all countries ordered by country id.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CountryCodeController.FIND_ALL_ORDER_BY_ID_URL)
	public List<Country> findAllOrderByCountryId(
												   HttpServletRequest request){
		this.logRequest(request.getRemoteAddr());
		return this.countryCodeService.findAllByOrderByCountryIdAsc();
	}
	/**
	 * Add new the list of country Codes.
	 *
	 * @param countryCodes The list of country Codes to add new.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of country Codes after add new and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CountryCodeController.ADD_NEW_COUNTRIES)
	public ModifiedEntity<List<Country>> addCountryCodes(@RequestBody List<Country> countryCodes,
														 HttpServletRequest request) {
		//show log message when init method
		CountryCodeController.LOGGER.info(String.format(CountryCodeController.ADD_NEW_COUNTRIES_LOG_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(countryCodes)));
		//call handle from service
		this.countryCodeService.addCountryCodes(countryCodes);
		//research data after delete successfully
		List<Country> newCountryCodes = this.countryCodeService.findAllByOrderByCountryIdAsc();
		return new ModifiedEntity<>(newCountryCodes, ADD_SUCCESS_MESSAGE);
	}
	/**
	 * Update information for the list of country Codes.
	 *
	 * @param countryCodes The list of country Codes to update.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of country Codes after update and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CountryCodeController.UPDATE_COUNTRIES)
	public ModifiedEntity<List<Country>> updateCountryCodes(@RequestBody List<Country> countryCodes,
															HttpServletRequest request) {
		//show log message when init method
		CountryCodeController.LOGGER.info(String.format(CountryCodeController.UPDATE_COUNTRIES_LOG_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(countryCodes)));
		//call handle from service
		this.countryCodeService.updateCountryCodes(countryCodes);
		//research data after delete successfully
		List<Country> newCountryCodes = this.countryCodeService.findAllByOrderByCountryIdAsc();
		return new ModifiedEntity<>(newCountryCodes, UPDATE_SUCCESS_MESSAGE);
	}
	/**
	 * Delete the list of country Codes.
	 *
	 * @param countryCodes The list of country Codes to delete.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of country Codes after deleted and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CountryCodeController.DELETE_COUNTRIES)
	public ModifiedEntity<List<Country>> deleteCountryCodes(@RequestBody List<Country> countryCodes,
															HttpServletRequest request) {
		//show log message when init method
		CountryCodeController.LOGGER.info(String.format(CountryCodeController.DELETE_COUNTRIES_LOG_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(countryCodes)));
		//call handle from service
		this.countryCodeService.deleteCountryCodes(countryCodes);
		//research data after delete successfully
		List<Country> newCountryCodes = this.countryCodeService.findAllByOrderByCountryIdAsc();
		return new ModifiedEntity<>(newCountryCodes, DELETE_SUCCESS_MESSAGE);
	}
	/**
	 * Logs a users request for all country codes.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logRequest(String ipAddress) {
		CountryCodeController.LOGGER.info(String.format(CountryCodeController.FIND_ALL_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}
}
