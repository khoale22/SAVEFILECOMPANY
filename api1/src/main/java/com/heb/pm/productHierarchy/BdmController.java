/*
 * BdmController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Bdm;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * REST endpoint for accessing bdm information.
 *
 * @author m314029
 * @since 2.0.6
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_BDM_LIST)
public class BdmController {

	public static final Logger logger = LoggerFactory.getLogger(BdmController.class);

	public static final String BDM_URL = "bdm";

	private static final String BDM_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for bdms with the pattern '%s'";

	private static final String NO_SEARCH_STRING_ERROR_KEY = "Missing a search string.";
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private BdmService bdmService;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Endpoint to search for bdms by a regular expression. The String passed in is treated as the middle
	 * of a regular expression and any bdm with a name or ID that contains the string passed in will be
	 * returned. It supports paging and will return a subset of the bdm that match the search.
	 *
	 * @param searchString The text you are looking for.
	 * @param page The page to look for.
	 * @param pageSize The maximum number of records to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of classes whose name or number match the supplied search string.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = BdmController.BDM_URL)
	public PageableResult<Bdm> findBdmsByRegularExpression(
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		// Search string is required.
		this.parameterValidator.validate(searchString, BdmController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				BdmController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());

		this.logBdmSearch(request.getRemoteAddr(), searchString);

		// Sets defaults for page and pageSize if they are not passed in.
		int pg = page == null ? BdmController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? BdmController.DEFAULT_PAGE_SIZE : pageSize;

		return this.bdmService.findByRegularExpression(searchString, pg, ps);
	}

	/**
	 * Sets the BdmService for this object to use. This is used for testing.
	 *
	 * @param service The BdmService for this object to use.
	 */
	public void setBdmService(BdmService service) {
		this.bdmService = service;
	}

	/**
	 * Sets the UserInfo for this object to use. This is used for testing.
	 *
	 * @param userInfo The UserInfo for this object to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the NonEmptyParameterValidator for this object to use. This is used for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this object to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Logs a search for bdms.
	 *
	 * @param ip The IP address of the user searching for bdms.
	 * @param searchString The string the user is searching for bdms by.
	 */
	private void logBdmSearch(String ip, String searchString) {
		BdmController.logger.info(
				String.format(BdmController.BDM_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString)
		);
	}
}
