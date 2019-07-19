/*
 * VendorController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.vendor;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Vendor;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * REST endpoint to access vendor information.
 *
 * @author d116773
 * @since 2.0.2
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.VENDOR_LIST)
public class VendorController {

	private static final Logger logger = LoggerFactory.getLogger(VendorController.class);

	private static final String VENDOR_URL = "vendor";
	private static final String VENDOR_LOOKUP_URL = VendorController.VENDOR_URL + "/{vendorNumber}";

	private static final String VENDOR_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for vendors with the pattern '%s'";
	private static final String NO_SEARCH_STRING_ERROR_KEY = "VendorController.missingSearchString";
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";

	private static final String VENDOR_SEARCH_BY_NUMBER_MESSAGE =
			"User %s from IP %s searched for vendor ID %d";
	private static final String NO_VENDOR_NUMBER_ERROR_KEY = "VendorController.missingVendorNumber";
	private static final String NO_VENDOR_NUMBER_DEFAULT_ERROR_MESSAGE = "Vendor number is required.";

	// Defaults for searches
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;

	@Autowired private UserInfo userInfo;
	@Autowired private VendorService vendorService;
	@Autowired private NonEmptyParameterValidator parameterValidator;

	@Value("${index.jobs.vendor}")
	private String indexJobName;

	/**
	 * Searches for a list of vendors based. The String passed in is treated as the middle of a regular expression
	 * and any vendor with a name or number that contains the string passed in will be returned. It supports paging and
	 * will return a subset of the vendors that match the search.
	 *
	 * @param searchString The text you are looking for in a vendor's name.
	 * @param page The page to look for.
	 * @param pageSize The maximum number of records to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of vendors whose name or number match the supplied search string.
	 */
	@RequestMapping(method = RequestMethod.GET, value = VendorController.VENDOR_URL)
	@ViewPermission
	@ResponseBody public PageableResult<Vendor> findByRegularExpression(
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		// Search string is required.
		this.parameterValidator.validate(searchString, VendorController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				VendorController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());

		this.logVendorSearch(request.getRemoteAddr(), searchString);

		// Set defaults if page and page size are not passed in.
		int pg = page == null ? VendorController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? VendorController.DEFAULT_PAGE_SIZE : pageSize;

		return this.vendorService.findByRegularExpression(searchString, pg, ps);
	}

	/**
	 * Seraches for a particular vendor.
	 *
	 * @param vendorNumber The AP number of the vendor you wish to find.
	 * @param request The HTTP request that initiated this call.
	 * @return The vendor matching the AP number you passed in. Null if not found.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = VendorController.VENDOR_LOOKUP_URL)
	@ResponseBody public Vendor findByVendorNumber(@PathVariable Integer vendorNumber,
												   HttpServletRequest request) {

		// Vendor number is required.
		this.parameterValidator.validate(vendorNumber, VendorController.NO_VENDOR_NUMBER_DEFAULT_ERROR_MESSAGE,
				VendorController.NO_VENDOR_NUMBER_ERROR_KEY, request.getLocale());

		this.logVendorLookup(request.getRemoteAddr(), vendorNumber);

		return this.vendorService.findByVendorNumber(vendorNumber);
	}

	/**
	 * Sets the NonEmptyParameterValidator for this object to use. This is primarily for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this object to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Sets the VendorService for the class to use. This is primarily for testing.
	 *
	 * @param vendorService The VendorService for the class to use.
	 */
	public void setVendorService(VendorService vendorService) {
		this.vendorService = vendorService;
	}

	/**
	 * Sets the UserInfo fo the class to use. This is primarily for testing.
	 *
	 * @param userInfo The UserInfo for the class to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Logs the user's search for a vendor.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param searchText The search string the user is looking for vendors that match.
	 */
	private void logVendorSearch(String ip, String searchText) {
		VendorController.logger.info(
				String.format(VendorController.VENDOR_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchText)
		);
	}

	/**
	 * Logs the user's search for a vendor by number.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param vendorNumber The AP number the vendor is looking for.
	 */
	private void logVendorLookup(String ip, Integer vendorNumber) {
		VendorController.logger.info(
				String.format(VendorController.VENDOR_SEARCH_BY_NUMBER_MESSAGE, this.userInfo.getUserId(),
						ip, vendorNumber)
		);
	}
}
