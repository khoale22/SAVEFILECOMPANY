/*
 * ReportController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.reports;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.DynamicAttribute;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST endpoint for application reports.
 *
 * @author d116773
 * @since 2.0.7
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL  + ReportController.REPORTS_URL)
@AuthorizedResource(ResourceConstants.INGREDIENTS_REPORT)
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	// URLs
	protected static final String REPORTS_URL = "/reports";
	protected static final String INGREDIENTS_REPORT = "/ingredients";
	protected static final String INGREDIENTS_EXPORT = "/ingredientsCsv";

	// Defaults page variables.
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 100;

	// Format for text attributes for CSV export.
	private static final String TEXT_EXPORT_FORMAT = "\"%s\",";

	// Log messages.
	private static final String FIND_BY_INGREDIENT_LOG_MESSAGE =
			"User %s from IP %s requested an ingredients report for '%s'";
	private static final String INGREDIENT_EXPORT_LOG_MESSAGE =
			"User %s from IP %s requested an ingredients report export for '%s'";

	// Default error messages.
	private static final String DEFAULT_NO_INGREDIENTS_MESSAGE = "Ingredient is required";

	// Keys to look up error messages.
	private static final String INGREDIENTS_MESSAGE_KEY = "ReportsController.missingIngredient";

	@Autowired
	private IngredientsResolver ingredientsResolver;

	@Autowired
	private IngredientsReportService ingredientsReportService;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Pulls a report of products with a certain ingredient.
	 *
	 * @param ingredient The ingredient to look for.
	 * @param includeCounts Whether or not to include total records and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of products with a given ingredient.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ReportController.INGREDIENTS_REPORT)
	public PageableResult<DynamicAttribute> findByIngredient(@RequestParam("ingredient") String ingredient,
								@RequestParam(value = "includeCounts", required = false)Boolean includeCounts,
								@RequestParam(value = "page", required = false)Integer page,
								@RequestParam(value = "pageSize", required = false)Integer pageSize,
								HttpServletRequest request) {

		// Make sure ingredients is set.
		this.parameterValidator.validate(ingredient, ReportController.DEFAULT_NO_INGREDIENTS_MESSAGE,
				ReportController.INGREDIENTS_MESSAGE_KEY, request.getLocale());

		// Log the request.
		this.logFindByIngredient(request.getRemoteAddr(), ingredient);

		// Set defaults if they are not passed in.
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ReportController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ReportController.DEFAULT_PAGE_SIZE : pageSize;

		// Get and resolve the report.
		PageableResult<DynamicAttribute> results =
				this.ingredientsReportService.getIngredientsReport(ingredient, ic, pg, ps);
		results.getData().forEach(this.ingredientsResolver::fetch);
		return results;
	}

	/**
	 * Rest endpoint to generate a CSV export of the full dataset.
	 *
	 * @param ingredient The Ingredient to look for.
	 * @param recordCount The number of records to pull back.
	 * @param downloadId An optional ID for the download. This will be set as a cookie in the response.
	 * @param request The HTTP request that initiated this request.
	 * @param response The HTTP response that this export will go to.
	 * @return A string that represents a CSV report of the ingredient data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ReportController.INGREDIENTS_EXPORT,
			headers = "Accept=text/csv")
	public String findByIngredientExport(@RequestParam("ingredient") String ingredient,
										@RequestParam(value = "recordCount", required = false)Integer recordCount,
										@RequestParam(value = "downloadId", required = false)String downloadId,
										HttpServletRequest request,
										HttpServletResponse response) {

		this.logFindByIngredientExport(request.getRemoteAddr(), ingredient);

		StringBuilder responseBuilder = new StringBuilder();

		// The headings
		responseBuilder.append("UPC,Description,Commodity,Sub-Commodity,BDM,Source System,Ingredients\n");


		PageableResult<DynamicAttribute> attributes = this.findByIngredient(ingredient, false, 0, recordCount, request);

		// Loop through the results and add the data.
		attributes.getData().forEach((i) -> {
				responseBuilder.append(i.getKey().getKey()).append(',');
				if (i.getProductMaster() != null) {
					responseBuilder.append(
							String.format(TEXT_EXPORT_FORMAT,i.getProductMaster().getDescription()));
					responseBuilder.append(
							String.format(TEXT_EXPORT_FORMAT,
									i.getProductMaster().getClassCommodity().getDisplayName()));
					responseBuilder.append(
							String.format(TEXT_EXPORT_FORMAT,
									i.getProductMaster().getSubCommodity().getDisplayName()));
					responseBuilder.append(
							String.format(TEXT_EXPORT_FORMAT,
									i.getProductMaster().getClassCommodity().getBdm().getDisplayName()));
				} else {
					responseBuilder.append(",,,,");
				}
				responseBuilder.append(String.format(TEXT_EXPORT_FORMAT, i.getSourceSystem().getDescription()));
				responseBuilder.append(String.format(TEXT_EXPORT_FORMAT, i.getTextValue())).append("\n");
		});

		// If the caller set a download ID, then add it as a cookie to the response.
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}

		return responseBuilder.toString();
	}

	/**
	 * Sets parameter validator.
	 *
	 * @param parameterValidator the parameter validator
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Sets ingredients resolver.
	 *
	 * @param ingredientsResolver the ingredients resolver
	 */
	public void setIngredientsResolver(IngredientsResolver ingredientsResolver) {
		this.ingredientsResolver = ingredientsResolver;
	}

	/**
	 * Sets ingredients report service.
	 *
	 * @param ingredientsReportService the ingredients report service
	 */
	public void setIngredientsReportService(IngredientsReportService ingredientsReportService) {
		this.ingredientsReportService = ingredientsReportService;
	}

	/**
	 * Sets user info.
	 *
	 * @param userInfo the user info
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Logs a request for an ingredients report.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param ingredient The ingredient the user is looking for.
	 */
	private void logFindByIngredient(String ip, String ingredient) {
		ReportController.logger.info(String.format(ReportController.FIND_BY_INGREDIENT_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, ingredient));
	}

	/**
	 * Logs a request for an export of an ingredients report.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param ingredient The ingredient the user is looking for.
	 */
	private void logFindByIngredientExport(String ip, String ingredient) {
		ReportController.logger.info(String.format(ReportController.INGREDIENT_EXPORT_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, ingredient));
	}
}
