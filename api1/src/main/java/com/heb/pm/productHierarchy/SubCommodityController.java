/*
 * SubCommodityController
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
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.SubCommodity;
import com.heb.pm.entity.SubCommodityAuditKey;
import com.heb.pm.entity.SubCommodityKey;
import com.heb.pm.entity.VertexTaxCategory;
import com.heb.pm.taxCategory.TaxCategoryService;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * REST endpoint for accessing sub-commodity information.
 *
 * @author d116773
 * @since 2.0.2
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + SubCommodityController.SUB_COMMODITY_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_LIST)
public class SubCommodityController {

	public static final Logger logger = LoggerFactory.getLogger(SubCommodityController.class);

	// urls
	public static final String SUB_COMMODITY_URL = "/productHierarchy/subCommodity";
	private static final String FIND_BY_REGULAR_EXPRESSION = "findByRegularExpression";
	private static final String GET_EMPTY = "getEmpty";
	private static final String UPDATE = "update";
	private static final String GET_ALL_VERTEX_TAX_CATEGORIES = "getAllVertexTaxCategories";
	private static final String FIND_BY_COMMODITY = "findByCommodity";
	private static final String GET_SUB_COMMODITY_DEFAUTS_AUDIT = "getSubCommodityDefaultsAudits";
	private static final String FIND_ALL_SUB_COMMODITIES_BY_PAGE_REQUEST = "findAllByPageRequest";
	private static final String FIND_SUB_COMMODITIES_BY_SEARCH_TEXT = "findBySearchText";

	// logs
	private static final String SUB_COMMODITY_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for sub-commodities with the pattern '%s'";
	private static final String LOG_COMPLETE_MESSAGE =
			"The SubCommodityController method: %s is complete.";
	private static final String GET_EMPTY_LOG_MESSAGE =
			"User %s from IP %s requested an empty sub-commodity.";
	private static final String UPDATE_LOG_MESSAGE =
			"User %s from IP %s requested to update sub-commodity: %s.";
	private static final String GET_ALL_VERTEX_TAX_CATEGORIES_MESSAGE =
			"User %s from IP %s requested to get all vertex tax categories.";
	private static final String FIND_BY_COMMODITY_LOG_MESSAGE =
			"User %s from IP %s requested to all sub commodities under commodity code: %d.";
	private static final String FIND_ALL_SUB_COMMODITIES_BY_PAGE_REQUEST_LOG_MESSAGE =
			"User %s from IP %s requested to find all sub commodities by page request";
	private static final String FIND_SUB_COMMODITIES_BY_SEARCH_TEXT_LOG_MESSAGE =
			"User %s from IP %s requested to find sub commodities by search text";
	private static final String SUB_COMMODITY_LOG_MESSAGE =
			"User %s from IP %s requested sub-commodity: %s.";
	private static final String SUB_COMMODITY_AUDIT_LOG_MESSAGE =
			"User %s from IP %s requested sub-commodity: %s.";

	// errors
	private static final String NO_SEARCH_STRING_ERROR_KEY = "SubCommodityController.missingSearchString";
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";
	private static final String NO_COMMODITY_CODE_ERROR_KEY = "SubCommodityController.missingCommodityCode";
	private static final String NO_COMMODITY_CODE_ERROR_MESSAGE = "Must have a commodity code to search for.";
	private static final String NO_CLASS_COMMODITY_ERROR_KEY = "ClassCommodityController.missingItemClass";
	private static final String NULL_CLASS_COMMODITY_ERROR_MESSAGE = "Must have an sub-commodity to search for.";

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private SubCommodityService subCommodityService;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private TaxCategoryService taxCategoryService;

	@Autowired
	private AuditService auditService;

	/**
	 * Endpoint to search for sub-commodities by a regular expression.The String passed in is treated as the middle
	 * of a regular expression and any sub-commodity with a name or ID that contains the string passed in will be
	 * returned. It supports paging and will return a subset of the sub-commodities that match the search.
	 *
	 * @param searchString The text you are looking for.
	 * @param page The page to look for.
	 * @param pageSize The maximum number of records to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of classes whose name or number match the supplied search string.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value= SubCommodityController.FIND_BY_REGULAR_EXPRESSION)
	public PageableResult<SubCommodity> findSubCommoditiesByRegularExpression(
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		// Search string is required.
		this.parameterValidator.validate(searchString, SubCommodityController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				SubCommodityController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());

		this.logSubCommoditySearch(request.getRemoteAddr(), searchString);

		// Sets defaults for page and pageSize if they are not passed in.
		int pg = page == null ? SubCommodityController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? SubCommodityController.DEFAULT_PAGE_SIZE : pageSize;

		return this.subCommodityService.findByRegularExpression(searchString, pg, ps);
	}

	/**
	 * Endpoint to retrieve an empty subCommodity used for editing.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return An empty subCommodity.
	 */
	@RequestMapping(method = RequestMethod.GET, value=SubCommodityController.GET_EMPTY)
	public SubCommodity getEmpty(HttpServletRequest request){
		this.logGetEmptySubCommodity(request.getRemoteAddr());
		return new SubCommodity();
	}

	/**
	 * Endpoint to update a subCommodity.
	 *
	 * @param subCommodity The subCommodity to update.
	 * @param request The HTTP request that initiated this call.
	 */
	@RequestMapping(method = RequestMethod.POST, value=SubCommodityController.UPDATE)
	public SubCommodity update(@RequestBody SubCommodity subCommodity, HttpServletRequest request){
		this.logUpdateCommodity(request.getRemoteAddr(), subCommodity);
		this.subCommodityService.update(subCommodity);
		SubCommodity subCommodityAfterUpdate = this.subCommodityService.findOne(subCommodity.getKey());
		BaseHierarchyResolver resolver = new BaseHierarchyResolver();
		resolver.resolveSubCommodity(subCommodityAfterUpdate);
		resolver.resolveCommodity(subCommodityAfterUpdate.getCommodityMaster());
		resolver.resolveItemClass(subCommodityAfterUpdate.getCommodityMaster().getItemClassMaster());
		this.logRequestComplete(SubCommodityController.UPDATE);
		return subCommodityAfterUpdate;
	}

	/**
	 * Finds sub commodities by commodity.
	 *
	 * @param commodityCode The commodity code to look for.
	 * @param request The HTTP request that initiated this call.
	 */
	@RequestMapping(method = RequestMethod.GET, value=SubCommodityController.FIND_BY_COMMODITY)
	public List<SubCommodity> findByCommodity(@RequestParam Integer commodityCode, HttpServletRequest request){
		// Commodity code is required.
		this.parameterValidator.validate(commodityCode, SubCommodityController.NO_COMMODITY_CODE_ERROR_MESSAGE,
				SubCommodityController.NO_COMMODITY_CODE_ERROR_KEY, request.getLocale());
		this.logFindByCommodity(request.getRemoteAddr(), commodityCode);
		return this.subCommodityService.findByCommodity(commodityCode);
	}

	/**
	 * Finds sub-commodities by page.
	 *
	 * @param page The page to look for.
	 * @param pageSize The maximum number of records to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of sub commodities.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value= SubCommodityController.FIND_ALL_SUB_COMMODITIES_BY_PAGE_REQUEST)
	public List<SubCommodity> findAllSubCommoditiesByPageRequest(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		SubCommodityController.logger.info(
				String.format(SubCommodityController.FIND_ALL_SUB_COMMODITIES_BY_PAGE_REQUEST_LOG_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr())
		);
		// Sets defaults for page and pageSize if they are not passed in.
		int pg = page == null ? SubCommodityController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? SubCommodityController.DEFAULT_PAGE_SIZE : pageSize;

		return this.subCommodityService.findAllSubCommoditiesByPageRequest(pg, ps);
	}

	/**
	 * Find sub commodities by search text.
	 * @param searchText the text to find
	 * @param page The page to look for.
	 * @param pageSize The maximum number of records to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return PageableResult with data SubCommodity.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SubCommodityController.FIND_SUB_COMMODITIES_BY_SEARCH_TEXT)
	public PageableResult<SubCommodity> findSubCommoditiesBySearchText(
			@RequestParam(value = "searchText", required = false) String searchText,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		SubCommodityController.logger.info(
				String.format(SubCommodityController.FIND_SUB_COMMODITIES_BY_SEARCH_TEXT_LOG_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr())
		);
		// Sets defaults for page and pageSize if they are not passed in.
		int pg = page == null ? SubCommodityController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? SubCommodityController.DEFAULT_PAGE_SIZE : pageSize;

		return this.subCommodityService.findSubCommoditiesBySearchText(searchText, pg, ps);
	}

	/**
	 * Logs a find subCommodities by commodity code.
	 *
	 *  @param ip The IP address of the user updating the subCommodity.
	 * @param commodityCode The commodity code the user is sub commodities for.
	 */
	private void logFindByCommodity(String ip, Integer commodityCode) {
		SubCommodityController.logger.info(
				String.format(SubCommodityController.FIND_BY_COMMODITY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, commodityCode)
		);
	}

	/**
	 * Logs an update for subCommodity.
	 *
	 * @param ip The IP address of the user updating the subCommodity.
	 * @param subCommodity The subCommodity the user is updating.
	 */
	private void logUpdateCommodity(String ip, SubCommodity subCommodity) {
		SubCommodityController.logger.info(
				String.format(SubCommodityController.UPDATE_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, subCommodity.toString())
		);
	}

	/**
	 * Logs a get empty for subCommodity.
	 *
	 * @param ip The IP address of the user getting an empty subCommodity.
	 */
	private void logGetEmptySubCommodity(String ip) {
		SubCommodityController.logger.info(
				String.format(SubCommodityController.GET_EMPTY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip)
		);
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		SubCommodityController.logger.info(
				String.format(SubCommodityController.LOG_COMPLETE_MESSAGE, method));
	}

	/**
	 * Sets the SubCommodityService for this object to use. This is used for testing.
	 *
	 * @param service The SubCommodityService for this object to use.
	 */
	public void setSubCommodityService(SubCommodityService service) {
		this.subCommodityService = service;
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
	 * Logs a search for sub-commodities.
	 *
	 * @param ip The IP address of the user searching for commodities.
	 * @param searchString The string the user is searching for commodities by.
	 */
	private void logSubCommoditySearch(String ip, String searchString) {
		SubCommodityController.logger.info(
				String.format(SubCommodityController.SUB_COMMODITY_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString)
		);
	}

	/**
	 * Gets all tax qualifying conditions.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return Collection of all tax qualifying conditions.
	 *
	 * @author vn70633
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SubCommodityController.GET_ALL_VERTEX_TAX_CATEGORIES)
	public Collection<VertexTaxCategory> getAllVertexTaxCategories(HttpServletRequest request){
		SubCommodityController.logger.info(
				String.format(SubCommodityController.GET_ALL_VERTEX_TAX_CATEGORIES_MESSAGE,
						this.userInfo.getUserId(), request.getRemoteAddr())
		);
		List<VertexTaxCategory> toReturn = new ArrayList<VertexTaxCategory>();
		VertexTaxCategory vertexTaxCategory = new VertexTaxCategory();
		vertexTaxCategory.setDvrCode("");
		vertexTaxCategory.setCategoryName("NONE");
		toReturn.add(vertexTaxCategory);
		toReturn.addAll(this.taxCategoryService.fetchAllTaxCategories());
		return toReturn;
	}

	/**
	 * Endpoint to search for sub-commodities by a SubCommodityKey
	 * @param key SubCommodityKey the user is looking for
	 * @param request The HTTP request that initiated this call.
	 * @return The SubCommodity the user searched for.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value="getCurrentSubCommodityClassInfo")
	public SubCommodity getCurrentCommodityClassInfo(
			@RequestBody SubCommodityKey key,
			HttpServletRequest request) {
		// Search string is required.
		this.parameterValidator.validate(key, SubCommodityController.NULL_CLASS_COMMODITY_ERROR_MESSAGE,
				SubCommodityController.NO_CLASS_COMMODITY_ERROR_KEY, request.getLocale());
		this.parameterValidator.validate(key.getSubCommodityCode(), SubCommodityController.NULL_CLASS_COMMODITY_ERROR_MESSAGE,
				SubCommodityController.NO_CLASS_COMMODITY_ERROR_KEY, request.getLocale());
		this.parameterValidator.validate(key.getCommodityCode(), SubCommodityController.NULL_CLASS_COMMODITY_ERROR_MESSAGE,
				SubCommodityController.NO_CLASS_COMMODITY_ERROR_KEY, request.getLocale());
		this.parameterValidator.validate(key.getClassCode(), SubCommodityController.NULL_CLASS_COMMODITY_ERROR_MESSAGE,
				SubCommodityController.NO_CLASS_COMMODITY_ERROR_KEY, request.getLocale());

		this.logClassSubCommodityKey(request.getRemoteAddr(), key);

		SubCommodity subCommodity = subCommodityService.findOne(key);
		BaseHierarchyResolver resolver = new BaseHierarchyResolver();
		resolver.resolveSubCommodity(subCommodity);
		resolver.resolveCommodity(subCommodity.getCommodityMaster());
		resolver.resolveItemClass(subCommodity.getCommodityMaster().getItemClassMaster());

		return subCommodity;
	}

	/**
	 * Logs the item-class code the user accessing.
	 *
	 * @param ip The IP address of the user searching for classes.
	 * @param key The subCommodity code the user is updating.
	 */
	private void logClassSubCommodityKey(String ip, SubCommodityKey key) {
		SubCommodityController.logger.info(
				String.format(SubCommodityController.SUB_COMMODITY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, key.toString())
		);
	}

	/**
	 * Get sub commodity defaults audit.
	 * @param subCommodityCode the sub-commodity code.
	 * @param request
	 * @return List<AuditRecord>
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value=SubCommodityController.GET_SUB_COMMODITY_DEFAUTS_AUDIT)
	public List<AuditRecord>  getSubCommodityDefaultsAudits( @RequestParam(value = "subCommodityCode") Integer subCommodityCode,
															 HttpServletRequest request) {
		this.logGetSubSommodityAudit(request.getRemoteAddr(), subCommodityCode);
		return this.auditService.getSubCommodityDefaultsAudits(subCommodityCode);
	}

	/**
	 * Logs the get sub-commodity defaults audit.
	 *
	 * @param ip The IP address of the user searching for classes.
	 * @param subCommodityCode The subCommodity code the user is updating.
	 */
	private void logGetSubSommodityAudit(String ip, Integer subCommodityCode) {
		SubCommodityController.logger.info(
				String.format(SubCommodityController.SUB_COMMODITY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, subCommodityCode.toString())
		);
	}
}