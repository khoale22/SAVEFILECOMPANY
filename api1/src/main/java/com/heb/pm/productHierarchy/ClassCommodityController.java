/*
 * ClassCommodityController
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
import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ClassCommodityKey;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * REST endpoint for accessing class and commodity information.
 *
 * @author d116773
 * @since 2.0.2
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ClassCommodityController.COMMODITY_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_CLASS_COMMODITYLIST)
public class ClassCommodityController {

	public static final Logger logger = LoggerFactory.getLogger(ClassCommodityController.class);

	// urls
	static final String COMMODITY_URL = "/productHierarchy/commodity";
	private static final String FIND_BY_REGULAR_EXPRESSION = "findByRegularExpression";
	private static final String GET_EMPTY = "getEmpty";
	private static final String UPDATE = "update";

	// logs
	private static final String COMMODITY_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for commodities with the pattern '%s'";
	private static final String LOG_COMPLETE_MESSAGE =
			"The ClassCommodityController method: %s is complete.";
	private static final String COMMODITY_GET_EMPTY_LOG_MESSAGE =
			"User %s from IP %s requested an empty commodity.";
	private static final String COMMODITY_UPDATE_LOG_MESSAGE =
			"User %s from IP %s requested to update commodity: %s.";
	private static final String COMMODITY_LOG_MESSAGE =
			"User %s from IP %s requested commodity: %s.";

	// errors
	private static final String NO_SEARCH_STRING_ERROR_KEY = "ClassCommodityController.missingSearchString";
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";
	private static final String NO_CLASS_COMMODITY_ERROR_KEY = "ClassCommodityController.missingItemClass";
	private static final String NULL_CLASS_COMMODITY_ERROR_MESSAGE = "Must have an commodity to search for.";

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ClassCommodityService classCommodityService;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Endpoint to search for commodities by a regular expression.The String passed in is treated as the middle
	 * of a regular expression and any commodity with a name or ID that contains the string passed in will be
	 * returned. It supports paging and will return a subset of the commodities that match the search.
	 *
	 * @param searchString The text you are looking for.
	 * @param page The page to look for.
	 * @param pageSize The maximum number of records to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of commodities whose name or number match the supplied search string.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value=ClassCommodityController.FIND_BY_REGULAR_EXPRESSION)
	public PageableResult<ClassCommodity> findCommoditiesByRegularExpression(
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		// Search string is required.
		this.parameterValidator.validate(searchString, ClassCommodityController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				ClassCommodityController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());

		this.logCommoditySearch(request.getRemoteAddr(), searchString);

		// Sets defaults for page and pageSize if they are not passed in.
		int pg = page == null ? ClassCommodityController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ClassCommodityController.DEFAULT_PAGE_SIZE : pageSize;

		return this.classCommodityService.findCommoditiesByRegularExpression(searchString, pg, ps);
	}

	/**
	 * Endpoint to retrieve an empty commodity used for editing.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return An empty commodity.
	 */
	@RequestMapping(method = RequestMethod.GET, value=ClassCommodityController.GET_EMPTY)
	public ClassCommodity getEmpty(HttpServletRequest request){
		this.logGetEmptyCommodity(request.getRemoteAddr());
		return new ClassCommodity();
	}

	/**
	 * Endpoint to update a commodity.
	 *
	 * @param commodity The commodity to update.
	 * @param request The HTTP request that initiated this call.
	 */
	@RequestMapping(method = RequestMethod.POST, value=ClassCommodityController.UPDATE)
	public ClassCommodity update(@RequestBody ClassCommodity commodity, HttpServletRequest request){
		this.logUpdateCommodity(request.getRemoteAddr(), commodity);
		this.classCommodityService.update(commodity);
		ClassCommodity commodityAfterUpdate = this.classCommodityService.findOne(commodity.getKey());
		this.logRequestComplete(ClassCommodityController.UPDATE);
		return commodityAfterUpdate;
	}

	/**
	 * Logs an update for commodity.
	 *
	 * @param ip The IP address of the user updating the commodity.
	 * @param commodity The commodity the user is updating.
	 */
	private void logUpdateCommodity(String ip, ClassCommodity commodity) {
		ClassCommodityController.logger.info(
				String.format(ClassCommodityController.COMMODITY_UPDATE_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, commodity.toString())
		);
	}

	/**
	 * Logs a get empty for commodity.
	 *
	 * @param ip The IP address of the user getting an empty commodity.
	 */
	private void logGetEmptyCommodity(String ip) {
		ClassCommodityController.logger.info(
				String.format(ClassCommodityController.COMMODITY_GET_EMPTY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip)
		);
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		ClassCommodityController.logger.info(
				String.format(ClassCommodityController.LOG_COMPLETE_MESSAGE, method));
	}

	/**
	 * Sets The NonEmptyParameterValidator for this object to use.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this object to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Sets the ClassCommodityService for this object to use. This is used for testing.
	 *
	 * @param service The ClassCommodityService for this object to use.
	 */
	public void setClassCommodityService(ClassCommodityService service) {
		this.classCommodityService = service;
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
	 * Logs a search for commodities.
	 *
	 * @param ip The IP address of the user searching for commodities.
	 * @param searchString The string the user is searching for commodities by.
	 */
	private void logCommoditySearch(String ip, String searchString) {
		ClassCommodityController.logger.info(
				String.format(ClassCommodityController.COMMODITY_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString)
		);
	}

	/**
	 * Endpoint to search for commodities by a ClassCommodityKey
	 * @param key ClassCommodityKey the user is looking for
	 * @param request The HTTP request that initiated this call.
	 * @return The ClassCommodity the user searched for
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value="getCurrentCommodityClassInfo")
	public ClassCommodity getCurrentCommodityClassInfo(
			@RequestBody ClassCommodityKey key,
			HttpServletRequest request) {
		// Search string is required.
		this.parameterValidator.validate(key, ClassCommodityController.NULL_CLASS_COMMODITY_ERROR_MESSAGE,
				ClassCommodityController.NO_CLASS_COMMODITY_ERROR_KEY, request.getLocale());
		this.parameterValidator.validate(key.getCommodityCode(), ClassCommodityController.NULL_CLASS_COMMODITY_ERROR_MESSAGE,
				ClassCommodityController.NO_CLASS_COMMODITY_ERROR_KEY, request.getLocale());
		this.parameterValidator.validate(key.getClassCode(), ClassCommodityController.NULL_CLASS_COMMODITY_ERROR_MESSAGE,
				ClassCommodityController.NO_CLASS_COMMODITY_ERROR_KEY, request.getLocale());

		this.logClassCommodityKey(request.getRemoteAddr(), key);

		ClassCommodity commodity = classCommodityService.findOne(key);
		BaseHierarchyResolver resolver = new BaseHierarchyResolver();
		resolver.resolveCommodity(commodity);
		resolver.resolveItemClass(commodity.getItemClassMaster());

		return commodity;
	}

	/**
	 * Logs the item-class code the user accessing.
	 *
	 * @param ip The IP address of the user searching for classes.
	 * @param key The commodity code the user is updating.
	 */
	private void logClassCommodityKey(String ip, ClassCommodityKey key) {
		ClassCommodityController.logger.info(
				String.format(ClassCommodityController.COMMODITY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, key.toString())
		);
	}
}
