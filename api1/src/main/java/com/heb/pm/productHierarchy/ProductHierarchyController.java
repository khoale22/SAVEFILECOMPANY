/*
 * ProductHierarchyController
 *
 *  Copyright (c) 2017 HEB
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
import com.heb.pm.entity.Department;
import com.heb.pm.entity.MerchantType;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller that returns all information related to product hierarchy.
 *
 * @author m314029
 * @since 2.4.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductHierarchyController.PRODUCT_HIERARCHY_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_VIEW)
public class ProductHierarchyController {

	private static final Logger logger = LoggerFactory.getLogger(ProductHierarchyController.class);

	/**
	 * The constant PRODUCT_HIERARCHY_URL.
	 */
	protected static final String PRODUCT_HIERARCHY_URL = "/productHierarchy";

	// requests
	private static final String GET_FULL_HIERARCHY = "getFullHierarchy";
	private static final String GET_HIERARCHY_BY_STRING_SEARCH = "getProductHierarchyBySearch";
	private static final String GET_ALL_MERCHANT_TYPES = "merchantType/findAll";

	// default 'no' values for levels of a product hierarchy
	public static final String NO_SUB_DEPARTMENT = "     ";
	public static final Integer NO_ITEM_CLASS = 0;
	public static final Integer NO_COMMODITY = 0;
	public static final Integer NO_SUB_COMMODITY = 0;

	// log messages
	private static final String GET_FULL_HIERARCHY_MESSAGE =
			"User %s from IP %s has requested full product hierarchy data.";
	private static final String HIERARCHY_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for product hierarchy with the pattern '%s' and with search type '%s'";

	// error property file keys
	private static final String NO_SEARCH_STRING_ERROR_KEY = "ProductHierarchyController.missingSearchString";

	// error messages
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ProductHierarchyService productHierarchyService;

	@Autowired private
	UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Represents known levels for the product hierarchy.
	 */
	public enum ProductHierarchyLevel {

		DEPARTMENT,
		SUB_DEPARTMENT,
		ITEM_CLASS,
		COMMODITY,
		SUB_COMMODITY;

		ProductHierarchyLevel() {}
	}

	/**
	 * Get full product hierarchy list.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_FULL_HIERARCHY)
	public List<Department> getFullHierarchy(HttpServletRequest request){

		this.logGetFullHierarchy(request.getRemoteAddr());
		return this.departmentService.findAll();
	}

	/**
	 * Get all levels of a product hierarchy by search string.
	 *
	 * @param searchString the search string.
	 * @param searchType the search type.
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_HIERARCHY_BY_STRING_SEARCH)
	public List<Department> getProductHierarchyBySearch(
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "searchType") String searchType, HttpServletRequest request){
		// Search string is required.
		this.parameterValidator.validate(searchString, ProductHierarchyController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				ProductHierarchyController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());
		this.logGetProductHierarchyBySearch(request.getRemoteAddr(), searchString,searchType);

		// send the service the search string already trimmed and toUpperCase so it only needs to be done once
		return this.departmentService.findHierarchyLevelsByRegularExpression(searchString.trim().toUpperCase(),searchType);
	}

	/**
	 * Get all merchant types.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductHierarchyController.GET_ALL_MERCHANT_TYPES)
	public List<MerchantType> getAllMerchantTypes(HttpServletRequest request){

		this.logGetFullHierarchy(request.getRemoteAddr());
		return this.productHierarchyService.getAllMerchantTypes();
	}

	/**
	 * Logs a user's request to get product hierarchy records by search string.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetProductHierarchyBySearch(String ip, String searchString, String searchType) {
		ProductHierarchyController.logger.info(
				String.format(ProductHierarchyController.HIERARCHY_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString,searchType));
	}

	/**
	 * Logs a user's request to get all product hierarchy records.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetFullHierarchy(String ip) {
		ProductHierarchyController.logger.info(
				String.format(ProductHierarchyController.GET_FULL_HIERARCHY_MESSAGE,
						this.userInfo.getUserId(), ip));
	}
}
