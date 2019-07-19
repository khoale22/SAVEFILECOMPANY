/*
 * SubDepartmentController
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
import com.heb.pm.entity.SubDepartment;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * REST endpoint to access sub-department information.
 *
 * @author d116773
 * @since 2.0.2
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_DEPARTMENT_LIST)
public class SubDepartmentController {

	private static final Logger logger = LoggerFactory.getLogger(SubDepartmentController.class);

	private static final String SUB_DEPARTMENT_URL = "subDepartment";

	private static final String SUB_DEPARTMENT_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for sub-departments with the pattern '%s'";
	private static final String NO_SEARCH_STRING_ERROR_KEY = "SubDepartmentController.missingSearchString";
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";

	// Defaults for searches
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;

	@Autowired private UserInfo userInfo;
	@Autowired private SubDepartmentService subDepartmentService;
	@Autowired private NonEmptyParameterValidator parameterValidator;

	/**
	 * Endpoint to search for sub-departments by a regular expression.The String passed in is treated as the middle
	 * of a regular expression and any sub-department with a name or ID that contains the string passed in will be
	 * returned. It supports paging and will return a subset of the sub-departments that match the search.
	 *
	 * @param searchString The text you are looking for.
	 * @param page The page to look for.
	 * @param pageSize The maximum number of records to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of sub-departments whose name or number match the supplied search string.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SubDepartmentController.SUB_DEPARTMENT_URL)
	@ResponseBody public PageableResult<SubDepartment> findByRegularExpression(
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		// Search string is required
		this.parameterValidator.validate(searchString, SubDepartmentController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				SubDepartmentController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());

		this.logSubDepartmentSearch(request.getRemoteAddr(), searchString);

		// Set defaults for page and page size
		// Set defaults if page and page size are not passed in.
		int pg = page == null ? SubDepartmentController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? SubDepartmentController.DEFAULT_PAGE_SIZE : pageSize;

		return this.subDepartmentService.findByRegularExpression(searchString, pg, ps);
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
	 * Sets the SubDepartmentService for the class to use. This is primarily for testing.
	 *
	 * @param subDepartmentService The SubDepartmentService for the class to use
	 */
	public void setSubDepartmentService(SubDepartmentService subDepartmentService) {
		this.subDepartmentService = subDepartmentService;
	}

	/**
	 * Sets the NonEmptyParameterValidator for this object to use. This is for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this object to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Logs a user request to search for sub-departments.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param searchString The string the user is using to search sub-departments for.
	 */
	private void logSubDepartmentSearch(String ip, String searchString) {
		SubDepartmentController.logger.info(
				String.format(SubDepartmentController.SUB_DEPARTMENT_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString));
	}
}
