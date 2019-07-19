/*
 * com.heb.pm.productDiscontinue.ProductDiscontinueController
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.productDiscontinue;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.Hits;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.ListFormatter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REST controller that returns all information related to deleting products.
 *
 * @author d116773
 * @since 2.0.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductDiscontinueController.PRODUCT_DISCONTINUE_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_DISCONTINUE_REPORT)
public class ProductDiscontinueController  {

	private static final Logger logger = LoggerFactory.getLogger(ProductDiscontinueController.class);

	protected static final String PRODUCT_DISCONTINUE_URL = "/productDiscontinue";

	// Keys to user facing messages in the message resource bundle.
	private static final String ITEM_CODES_MESSAGE_KEY = "ProductDiscontinueController.missingItemCodes";
	private static final String DEFAULT_NO_ITEM_CODES_MESSAGE = "Must search for at least one item code.";

	private static final String UPCS_MESSAGE_KEY = "ProductDiscontinueController.missingUpcs";
	private static final String DEFAULT_NO_UPCS_MESSAGE = "Must search for at least one UPC.";

	private static final String PRODUCT_IDS_MESSAGE_KEY = "ProductDiscontinueController.missingProductIds";
	private static final String DEFAULT_NO_PRODUCT_IDS_MESSAGE = "Must search for at least one product ID.";

	private static final String DEFAULT_NO_DEPARTMENT_MESSAGE = "Must search for a department.";
	private static final String DEPARTMENT_MESSAGE_KEY = "ProductDiscontinueController.missingDepartment";
	private static final String DEFAULT_NO_CLASS_MESSAGE = "Must search for a class.";
	private static final String CLASS_MESSAGE_KEY = "ProductDiscontinueController.missingClass";
	private static final String DEFAULT_NO_COMMODITY_MESSAGE = "Must search for a commodity.";
	private static final String COMMODITY_MESSAGE_KEY = "ProductDiscontinueController.missingCommodity";
	private static final String DEFAULT_NO_SUB_COMMODITY_MESSAGE = "Must search for a sub commodity.";
	private static final String SUB_COMMODITY_MESSAGE_KEY = "ProductDiscontinueController.missingSubCommodity";

	private static final String BDM_MESSAGE_KEY = "ProductDiscontinueController.missingBdm";
	private static final String DEFAULT_NO_BDM_MESSAGE = "Must search for a bdm.";
	private static final String DEFAULT_NO_VENDOR_MESSAGE = "Must search for a vendor.";
	private static final String VENDOR_MESSAGE_KEY = "ProductDiscontinueController.missingVendor";

	// Log messages.
	private static final String FIND_ALL_MESSAGE = "User %s at IP address %s requested all product discontinue data.";

	private static final String FIND_BY_MULTIPLE_ITEM_CODE_MESSAGE  =
			"User %s from IP %s has requested product discontinue data for the following item codes [%s]";
	private static final String FIND_BY_MULTIPLE_UPC_MESSAGE  =
			"User %s from IP %s has requested product discontinue data for the following UPCs [%s]";
	private static final String FIND_BY_MULTIPLE_PRODUCT_IDS_MESSAGE =
			"User %s from IP %s has requested product discontinue data for the following Product IDs [%s]";
	private static final String FIND_BY_SUB_DEPARTMENT_MESSAGE =
			"User %s from IP %s has requested product discontinue data for following department: %s, " +
					"and sub department: %s.";
	private static final String FIND_BY_CLASS_AND_COMMODITY_MESSAGE =
			"User %s from IP %s has requested product discontinue data for following class: %d, " +
					"and commodity: %d.";
	private static final String FIND_BY_SUB_COMMODITY_MESSAGE =
			"User %s from IP %s has requested product discontinue data for following class: %d, commodity: %d, " +
					"and sub commodity: %d.";
	private static final String FIND_BY_BDM_MESSAGE =
			"User %s from IP %s has requested product discontinue data for the following bdm: %s.";
	private static final String SUB_DEPARTMENT_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the department: %s," +
					" sub-department: %s.";
	private static final String CLASS_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the class: %s.";
	private static final String CLASS_COMMODITY_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the class: %s, " +
					"commodity: %s.";
	private static final String SUB_COMMODITY_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the class: %s, " +
					"commodity: %s, sub-commodity: %s.";
	private static final String BDM_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for BDM: %s.";
	private static final String ITEM_CODE_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the following item " +
					"codes [%s]";
	private static final String UPC_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the following UPCs [%s]";
	private static final String PRODUCT_ID_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the following product" +
					" ids [%s]";
	private static final String FIND_BY_VENDOR_MESSAGE =
			"User %s from IP %s has requested product data for the following vendor number: %d.";

	private static final String FIND_BY_DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY_MESSAGE =
			"User %s from IP %s has requested product discontinue data for following department: %s, sub department: %s, class: %d, commodity: %d, " +
					"and sub commodity: %d.";
	private static final String FIND_BY_COMMODITY_AND_SUB_COMMODITY_MESSAGE =
			"User %s from IP %s has requested product discontinue data for following commodity: %d, and sub commodity: %d.";
	private static final String FIND_BY_DEPARTMENT_CLASS_COMMODITY_MESSAGE =
			"User %s from IP %s has requested product discontinue data for following department: %s, sub department: %s, class: %d, commodity: %d, ";
	private static final String FIND_BY_DEPARTMENT_COMMODITY_SUB_COMMODITY_MESSAGE =
			"User %s from IP %s has requested product discontinue data for following department: %s, sub department: %s, commodity: %d, and sub commodity: %d.";
	private static final String FIND_BY_DEPARTMENT_CLASS_MESSAGE =
			"User %s from IP %s has requested product discontinue data for following department: %s, sub department: %s, class: %d.";

	private static final String DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the department: %s, sub department: %s, class: %d, commodity: %d, " +
					"and sub commodity: %d.";
	private static final String DEPARTMENT_CLASS_COMMODITY_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the department: %s, sub department: %s, class: %d, commodity: %d.";
	private static final String DEPARTMENT_COMMODITY_SUB_COMMODITY_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the department: %s, sub department: %s, commodity: %d, and sub commodity: %d.";
	private static final String DEPARTMENT_CLASS_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the department: %s, sub department: %s, class: %d.";
	private static final String COMMODITY_SUB_COMMODITY_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export to excel product discontinue data for the commodity: %d, and sub commodity: %d.";

	// Defaults related to paging.
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 25; // lowered from 100 to speed up search (m314029)

	// Defaults related to sorting.
	private static final ProductDiscontinueService.SortColumn DEFAULT_SORT_COLUMN =
			ProductDiscontinueService.SortColumn.UPC;
	private static final ProductDiscontinueService.SortDirection DEFAULT_SORT_DIRECTION =
			ProductDiscontinueService.SortDirection.ASC;
	private static final StatusFilter DEFAULT_STATUS_FILTER = StatusFilter.NONE;

	// Constants related to client facing messages.
	private MessageSource messageSource;

	// Resolves the ProductDiscontinue returned by most of these functions so that it has all needed properties loaded.
	private LazyObjectResolver<ProductDiscontinue> productDiscontinueResolver = new ProductDiscontinueResolver();

	// Holds the business logic around product discontinue.
	@Autowired private ProductDiscontinueService productDiscontinueService;
	@Autowired private UserInfo userInfo;
	@Autowired private NonEmptyParameterValidator parameterValidator;

	/**
	 * Returns a sub-set of all product discontinue records.
	 *
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param request The HTTP request that initiated this call.
	 * @return An iterable list of ProductDiscontinue records.
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ViewPermission
	public PageableResult<ProductDiscontinue> findAll(@RequestParam(value = "page", required = false) Integer page,
													  @RequestParam(value = "pageSize", required = false)
													  Integer pageSize, HttpServletRequest request) {

		this.logFindAll(request.getRemoteAddr());

		int p = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int s = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;

		return this.resolveResults(this.productDiscontinueService.findAll(p, s));
	}

	/**
	 * Returns a page of product discontinue data for an list of item codes.
	 *
	 * @param itemCodes The item codes to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="itemCodes")
	public PageableResult<ProductDiscontinue> findAllByItemCodes(
			@RequestParam("itemCodes") List<Long> itemCodes,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// Item codes are required.
		this.parameterValidator.validate(itemCodes, ProductDiscontinueController.DEFAULT_NO_ITEM_CODES_MESSAGE,
				ProductDiscontinueController.ITEM_CODES_MESSAGE_KEY, request.getLocale());

		this.logFindByItemCodes(request.getRemoteAddr(), itemCodes);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.findByItemCodes(itemCodes, sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for an list of UPCs
	 *
	 * @param upcs The UPCs to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="upcs")
	public PageableResult<ProductDiscontinue> findAllByUpcs(
			@RequestParam("upcs") List<Long> upcs,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// UPCs are required.
		this.parameterValidator.validate(upcs, ProductDiscontinueController.DEFAULT_NO_UPCS_MESSAGE,
				ProductDiscontinueController.UPCS_MESSAGE_KEY, request.getLocale());

		this.logFindByUpcs(request.getRemoteAddr(), upcs);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.findByUpcs(upcs, sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for an list of product IDs
	 *
	 * @param productIds The list of product IDs to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="productIds")
	public PageableResult<ProductDiscontinue> findByProductIds(
			@RequestParam("productIds") List<Long> productIds,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// products are required.
		this.parameterValidator.validate(productIds, ProductDiscontinueController.DEFAULT_NO_PRODUCT_IDS_MESSAGE,
				ProductDiscontinueController.PRODUCT_IDS_MESSAGE_KEY, request.getLocale());

		this.logFindByProductIds(request.getRemoteAddr(), productIds);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.findByProductIds(productIds, sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for a sub department.
	 *
	 * @param department The department to look for data about.
	 * @param subDepartment The sub department to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="subDepartment")
	public PageableResult<ProductDiscontinue> findBySubDepartment(
			@RequestParam("department") String department,
			@RequestParam("subDepartment") String subDepartment,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// hierarchy is required.
		this.parameterValidator.validate(department, ProductDiscontinueController.DEFAULT_NO_DEPARTMENT_MESSAGE,
				ProductDiscontinueController.DEPARTMENT_MESSAGE_KEY, request.getLocale());

		this.logFindBySubDepartment(request.getRemoteAddr(), department, subDepartment);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.
				findBySubDepartment(department, subDepartment, sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for a class and commodity.
	 *
	 * @param classCode The class to look for data about.
	 * @param commodityCode The commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="classAndCommodity")
	public PageableResult<ProductDiscontinue> findByClassAndCommodity(
			@RequestParam("classCode") int classCode,
			@RequestParam("commodityCode") int commodityCode,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// hierarchy is required.
		this.parameterValidator.validate(classCode,	ProductDiscontinueController.DEFAULT_NO_CLASS_MESSAGE,
				ProductDiscontinueController.CLASS_MESSAGE_KEY, request.getLocale());

		// hierarchy is required.
		this.parameterValidator.validate(commodityCode,	ProductDiscontinueController.DEFAULT_NO_COMMODITY_MESSAGE,
				ProductDiscontinueController.COMMODITY_MESSAGE_KEY, request.getLocale());

		this.logFindByClassAndCommodity(request.getRemoteAddr(), classCode, commodityCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.
				findByClassAndCommodity(classCode, commodityCode, sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for a sub commodity.
	 *
	 * @param classCode The class to look for data about.
	 * @param commodityCode The commodity to look for data about.
	 * @param subCommodityCode The sub commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="subCommodity")
	public PageableResult<ProductDiscontinue> findBySubCommodity(
			@RequestParam("classCode") int classCode,
			@RequestParam("commodityCode") int commodityCode,
			@RequestParam("subCommodityCode") int subCommodityCode,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// hierarchy is required.
		this.parameterValidator.validate(classCode, ProductDiscontinueController.DEFAULT_NO_CLASS_MESSAGE,
				ProductDiscontinueController.CLASS_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(commodityCode, ProductDiscontinueController.DEFAULT_NO_COMMODITY_MESSAGE,
				ProductDiscontinueController.COMMODITY_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(subCommodityCode,
				ProductDiscontinueController.DEFAULT_NO_SUB_COMMODITY_MESSAGE,
				ProductDiscontinueController.SUB_COMMODITY_MESSAGE_KEY, request.getLocale());

		this.logFindBySubCommodity(request.getRemoteAddr(), classCode, commodityCode, subCommodityCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.
				findBySubCommodity(classCode, commodityCode, subCommodityCode,
						sf, ic, pg, ps, sc, sd));
	}
	/**
	 * Returns a page of product discontinue data for a bdm.
	 *
	 * @param bdm The bdm code to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="bdm")
	public PageableResult<ProductDiscontinue> findByBdm(
			@RequestParam("bdm") Bdm bdm,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		String bdmCode = bdm.getBdmCode();

		// products are required.
		this.parameterValidator.validate(bdmCode, ProductDiscontinueController.DEFAULT_NO_BDM_MESSAGE,
				ProductDiscontinueController.BDM_MESSAGE_KEY, request.getLocale());

		this.logFindByBdm(request.getRemoteAddr(), bdmCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.findByBdm(bdmCode, sf, ic, pg, ps, sc, sd));
	}


	/**
	 * Search against Product discontinue for occurrence of the input UPC List.
	 *
	 * @param upcs The UPC searched for
	 * @param request The HTTP request that initiated this call.
	 * @return Hits result with Not found UPCs List
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="hits/upcs")
	public Hits findHitsByUPC(@RequestParam("upcs") List<Long> upcs, HttpServletRequest request) {

		// Min one UPC is required.
		this.parameterValidator.validate(upcs, ProductDiscontinueController.DEFAULT_NO_UPCS_MESSAGE,
				ProductDiscontinueController.UPCS_MESSAGE_KEY, request.getLocale());

		this.logFindByUpcs(request.getRemoteAddr(), upcs);
		return this.productDiscontinueService.findHitsByUPCs(upcs);
	}

	/**
	 * Search  against Product discontinue for occurrence of the input item codes List.
	 *
	 * @param itemCodes	The item codes user searched for
	 * @param request The HTTP request that initiated this call.
	 * @return Hits result with Not found Items List
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="hits/itemCodes")
	public Hits findHitsByItemCode(@RequestParam("itemCodes") List<Long> itemCodes, HttpServletRequest request) {

		// Min one Item code is required.
		this.parameterValidator.validate(itemCodes, ProductDiscontinueController.DEFAULT_NO_ITEM_CODES_MESSAGE,
				ProductDiscontinueController.ITEM_CODES_MESSAGE_KEY, request.getLocale());

		this.logFindByItemCodes(request.getRemoteAddr(), itemCodes);
		return this.productDiscontinueService.findHitsByItemCodes(itemCodes);
	}

	/**
	 * Search against Product discontinue for occurrence of the input products List.
	 *
	 * @param productIds The product Ids user searched for
	 * @param request The HTTP request that initiated this call.
	 * @return Hits result with Not found products List
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="hits/productIds")
	public Hits findHitsByProductId(@RequestParam("productIds") List<Long> productIds, HttpServletRequest request) {

		// Min one product Id is required.
		this.parameterValidator.validate(productIds, ProductDiscontinueController.DEFAULT_NO_PRODUCT_IDS_MESSAGE,
				ProductDiscontinueController.PRODUCT_IDS_MESSAGE_KEY, request.getLocale());

		this.logFindByProductIds(request.getRemoteAddr(), productIds);
		return this.productDiscontinueService.findHitsByProducts(productIds);
	}

	/**
	 * Used to get all the app managed item not deleted reason types that contains app managed short description and
	 * the instruction. The result is returned as map of reason types referenced by their respective code as key.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return all managed item not deleted reasons.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="deleteReasons")
	public Map<String, ItemNotDeletedReason> getItemNotDeleteReasons(HttpServletRequest request) {
		this.logFindByProductIds(request.getRemoteAddr(), null);
		return this.productDiscontinueService.getAllItemNotDeleteReasons();
	}

	/**
	 * Calls excel export for BDM.
	 *
	 * @param bdmCode The BDM code to search on..
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportBdmToCsv", headers = "Accept=text/csv")
	public void findByBdmExport(@RequestParam(name = "bdm", required = false) String bdmCode,
								@RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
								@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
								@RequestParam(value = "downloadId", required = false) String downloadId,
								HttpServletRequest request, HttpServletResponse response){

		this.logBdmExportToExcel(bdmCode, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);

		Bdm bdm = new Bdm();
		bdm.setBdmCode(bdmCode);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findByBdm(bdm, statusFilter,
						false, x, DEFAULT_PAGE_SIZE, ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for Department/SubDepartment.
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportDepartmentToCsv", headers = "Accept=text/csv")
	public void findByDepartmentExport(@RequestParam(name = "department", required = false) String department,
									   @RequestParam(name = "subdepartment", required = false) String subdepartment,
									   @RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
									   @RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
									   @RequestParam(value = "downloadId", required = false) String downloadId,
									   HttpServletRequest request, HttpServletResponse response){

		this.logDepartmentExportToExcel(department,subdepartment, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findBySubDepartment(department,
						subdepartment, statusFilter, false, x, DEFAULT_PAGE_SIZE,
						ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for Class/commodity.
	 *
	 * @param classCode The class code.
	 * @param commodity The commodity code.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportClassCommodityToCsv", headers = "Accept=text/csv")
	public void findByClassCommodityExport(
			@RequestParam(name = "classCode", required = false) int classCode,
			@RequestParam(name = "commodityCode", required = false) int commodity,
			@RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
			@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
			@RequestParam(value = "downloadId", required = false) String downloadId, HttpServletRequest request,
			HttpServletResponse response){

		this.logClassCommodityExportToExcel(classCode, commodity, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findByClassAndCommodity(
						classCode, commodity, statusFilter, false, x, DEFAULT_PAGE_SIZE,
						ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for subcommodity.
	 *
	 * @param classCode The class code.
	 * @param commodity The commodity code.
	 * @param subcommodity The subcommodity code.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportSubCommodityToCsv", headers = "Accept=text/csv")
	public void findBySubCommodityExport(
			@RequestParam(name = "classCode", required = false) int classCode,
			@RequestParam(name = "commodityCode", required = false) int commodity,
			@RequestParam(name = "subCommodityCode", required = false) int subcommodity,
			@RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
			@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){

		this.logSubCommodityExportToExcel(classCode, commodity, subcommodity, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findBySubCommodity(classCode,
						commodity, subcommodity, statusFilter, false, x, DEFAULT_PAGE_SIZE,
						ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for item codes.
	 *
	 * @param itemCodes The item code list.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportItemCodesToCsv", headers = "Accept=text/csv")
	public void findByItemCodesExport(
			@RequestParam(name = "itemCodes", required = false) List<Long> itemCodes,
			@RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
			@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){

		this.logItemCodeExportToExcel(StringUtils.join(itemCodes, ","), request.getRemoteAddr());
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findAllByItemCodes(itemCodes,
						statusFilter, false, x, DEFAULT_PAGE_SIZE, ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}

	}

	/**
	 * Calls excel export for upcs.
	 *                              outpu
	 * @param upcs The upc list.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportUpcsToCsv", headers = "Accept=text/csv")
	public void findByUpcsExport(
			@RequestParam(name = "upcs", required = false) List<Long> upcs,
			@RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
			@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){

		this.logUpcExportToExcel(StringUtils.join(upcs, ","), request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findAllByUpcs(upcs,
						statusFilter, false, x, DEFAULT_PAGE_SIZE, ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for product ids.
	 *
	 * @param productIds The product id list.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportProductIdsToCsv", headers = "Accept=text/csv")
	public void findByProductIdsExport(
			@RequestParam(name = "productIds", required = false) List<Long> productIds,
			@RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
			@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){

		this.logProductIdExportToExcel(StringUtils.join(productIds, ","), request.getRemoteAddr());
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {

				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findByProductIds(productIds,
						statusFilter, false, x, DEFAULT_PAGE_SIZE, ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
	}

	/**
	 * Returns a page of product discontinue data for a vendor by id.
	 *
	 * @param vendorNumber The Vendor id look for data about.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="vendor")
	public PageableResult<ProductDiscontinue> findByVendorNumber(
			@RequestParam("vendor") int vendorNumber,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// vendor number required.
		this.parameterValidator.validate(vendorNumber, ProductDiscontinueController.DEFAULT_NO_VENDOR_MESSAGE,
				ProductDiscontinueController.VENDOR_MESSAGE_KEY, request.getLocale());

		this.logFindByVendorNumber(request.getRemoteAddr(), vendorNumber);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(
				this.productDiscontinueService.findByVendorNumber(vendorNumber, sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for a department and class and commodity and sub commodity.
	 *
	 * @param department The department.
	 * @param subDepartment The subDepartment.
	 * @param classCode The class to look for data about.
	 * @param commodityCode The commodity to look for data about.
	 * @param subCommodityCode The sub commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="departmentAndClassAndCommodityAndSubCommodity")
	public PageableResult<ProductDiscontinue> findByDepartmentAndClassAndCommodityAndSubCommodity(
			@RequestParam("department") String department,
			@RequestParam("subDepartment") String subDepartment,
			@RequestParam("classCode") int classCode,
			@RequestParam("commodityCode") int commodityCode,
			@RequestParam("subCommodityCode") int subCommodityCode,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// hierarchy is required.
		this.parameterValidator.validate(department, ProductDiscontinueController.DEFAULT_NO_DEPARTMENT_MESSAGE,
				ProductDiscontinueController.DEPARTMENT_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(classCode, ProductDiscontinueController.DEFAULT_NO_CLASS_MESSAGE,
				ProductDiscontinueController.CLASS_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(commodityCode, ProductDiscontinueController.DEFAULT_NO_COMMODITY_MESSAGE,
				ProductDiscontinueController.COMMODITY_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(subCommodityCode,
				ProductDiscontinueController.DEFAULT_NO_SUB_COMMODITY_MESSAGE,
				ProductDiscontinueController.SUB_COMMODITY_MESSAGE_KEY, request.getLocale());

		this.logFindByDepartmentAndClassAndCommodityAndSubCommodity(request.getRemoteAddr(), department, subDepartment, classCode, commodityCode, subCommodityCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.
				findByDepartmentAndClassAndCommodityAndSubCommodity(department, subDepartment, classCode, commodityCode, subCommodityCode,
						sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for a commodity and sub commodity.
	 *
	 * @param commodityCode The commodity to look for data about.
	 * @param subCommodityCode The sub commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="commodityAndSubCommodity")
	public PageableResult<ProductDiscontinue> findByCommodityAndSubCommodity(
			@RequestParam("commodityCode") int commodityCode,
			@RequestParam("subCommodityCode") int subCommodityCode,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// hierarchy is required.
		this.parameterValidator.validate(commodityCode, ProductDiscontinueController.DEFAULT_NO_COMMODITY_MESSAGE,
				ProductDiscontinueController.COMMODITY_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(subCommodityCode,
				ProductDiscontinueController.DEFAULT_NO_SUB_COMMODITY_MESSAGE,
				ProductDiscontinueController.SUB_COMMODITY_MESSAGE_KEY, request.getLocale());

		this.logFindByCommodityAndSubCommodity(request.getRemoteAddr(), commodityCode, subCommodityCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.findByCommodityAndSubCommodity(commodityCode, subCommodityCode,
						sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for a department and class and commodity.
	 *
	 * @param department The department.
	 * @param subDepartment The subDepartment.
	 * @param classCode The class to look for data about.
	 * @param commodityCode The commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="departmentAndClassAndCommodity")
	public PageableResult<ProductDiscontinue> findByDepartmentAndClassAndCommodity(
			@RequestParam("department") String department,
			@RequestParam("subDepartment") String subDepartment,
			@RequestParam("classCode") int classCode,
			@RequestParam("commodityCode") int commodityCode,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// hierarchy is required.
		this.parameterValidator.validate(department, ProductDiscontinueController.DEFAULT_NO_DEPARTMENT_MESSAGE,
				ProductDiscontinueController.DEPARTMENT_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(classCode, ProductDiscontinueController.DEFAULT_NO_CLASS_MESSAGE,
				ProductDiscontinueController.CLASS_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(commodityCode, ProductDiscontinueController.DEFAULT_NO_COMMODITY_MESSAGE,
				ProductDiscontinueController.COMMODITY_MESSAGE_KEY, request.getLocale());

		this.logFindByDepartmentAndClassAndCommodity(request.getRemoteAddr(), department, subDepartment, classCode, commodityCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.
				findByDepartmentAndClassAndCommodity(department, subDepartment, classCode, commodityCode, sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for a department and commodity and sub commodity.
	 *
	 * @param department The department.
	 * @param subDepartment The subDepartment.
	 * @param commodityCode The commodity to look for data about.
	 * @param subCommodityCode The sub commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="departmentAndCommodityAndSubCommodity")
	public PageableResult<ProductDiscontinue> findByDepartmentAndCommodityAndSubCommodity(
			@RequestParam("department") String department,
			@RequestParam("subDepartment") String subDepartment,
			@RequestParam("commodityCode") int commodityCode,
			@RequestParam("subCommodityCode") int subCommodityCode,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// hierarchy is required.
		this.parameterValidator.validate(department, ProductDiscontinueController.DEFAULT_NO_DEPARTMENT_MESSAGE,
				ProductDiscontinueController.DEPARTMENT_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(commodityCode, ProductDiscontinueController.DEFAULT_NO_COMMODITY_MESSAGE,
				ProductDiscontinueController.COMMODITY_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(subCommodityCode,
				ProductDiscontinueController.DEFAULT_NO_SUB_COMMODITY_MESSAGE,
				ProductDiscontinueController.SUB_COMMODITY_MESSAGE_KEY, request.getLocale());

		this.logFindByDepartmentAndCommodityAndSubCommodity(request.getRemoteAddr(), department, subDepartment, commodityCode, subCommodityCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.
				findByDepartmentAndCommodityAndSubCommodity(department, subDepartment, commodityCode, subCommodityCode,
						sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Returns a page of product discontinue data for a department and class code.
	 *
	 * @param department The department.
	 * @param subDepartment The subDepartment.
	 * @param classCode The class to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of product discontinue data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="departmentAndClass")
	public PageableResult<ProductDiscontinue> findByDepartmentAndClass(
			@RequestParam("department") String department,
			@RequestParam("subDepartment") String subDepartment,
			@RequestParam("classCode") int classCode,
			@RequestParam(value = "status", required = false) StatusFilter statusFilter,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ProductDiscontinueService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) ProductDiscontinueService.SortDirection
					sortDirection, HttpServletRequest request) {

		// hierarchy is required.
		this.parameterValidator.validate(department, ProductDiscontinueController.DEFAULT_NO_DEPARTMENT_MESSAGE,
				ProductDiscontinueController.DEPARTMENT_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(classCode, ProductDiscontinueController.DEFAULT_NO_CLASS_MESSAGE,
				ProductDiscontinueController.CLASS_MESSAGE_KEY, request.getLocale());

		this.logFindByDepartmentAndClass(request.getRemoteAddr(), department, subDepartment, classCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		StatusFilter sf = statusFilter == null ? ProductDiscontinueController.DEFAULT_STATUS_FILTER : statusFilter;
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ProductDiscontinueController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductDiscontinueController.DEFAULT_PAGE_SIZE : pageSize;
		ProductDiscontinueService.SortColumn sc =
				sortColumn == null ? ProductDiscontinueController.DEFAULT_SORT_COLUMN : sortColumn;
		ProductDiscontinueService.SortDirection sd =
				sortDirection == null ? ProductDiscontinueController.DEFAULT_SORT_DIRECTION : sortDirection;

		return this.resolveResults(this.productDiscontinueService.
				findByDepartmentAndClass(department, subDepartment, classCode, sf, ic, pg, ps, sc, sd));
	}

	/**
	 * Calls excel export for search critical is Department/SubDepartment and class code and commodity and subcommodity
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param classCode The class to look for data about.
	 * @param commodityCode The commodity to look for data about.
	 * @param subCommodityCode The sub commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportDepartmentClassCommoditySubCommodityToCsv", headers = "Accept=text/csv")
	public void findByDepartmentClassCommoditySubCommodityExport(@RequestParam(name = "department", required = false) String department,
																 @RequestParam(name = "subdepartment", required = false) String subdepartment,
																 @RequestParam(name = "classCode", required = false) int classCode,
																 @RequestParam(name = "commodityCode", required = false) int commodityCode,
																 @RequestParam(name = "subCommodityCode", required = false) int subCommodityCode,
																 @RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
																 @RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
																 @RequestParam(value = "downloadId", required = false) String downloadId,
																 HttpServletRequest request, HttpServletResponse response){

		this.logDepartmentAndClassAndCommodityAndSubCommodityExportToExcel(department,subdepartment, classCode, commodityCode, subCommodityCode, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findByDepartmentAndClassAndCommodityAndSubCommodity(department,
						subdepartment, classCode, commodityCode, subCommodityCode, statusFilter, false, x, DEFAULT_PAGE_SIZE,
						ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for Department/SubDepartment.
	 *
	 * @param commodityCode The commodity to look for data about.
	 * @param subCommodityCode The sub commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportCommodityAndSubCommodityToCsv", headers = "Accept=text/csv")
	public void findByCommodityAndSubCommodityExport(@RequestParam(name = "commodityCode", required = false) int commodityCode,
													 @RequestParam(name = "subCommodityCode", required = false) int subCommodityCode,
													 @RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
													 @RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
													 @RequestParam(value = "downloadId", required = false) String downloadId,
													 HttpServletRequest request, HttpServletResponse response){

		this.logCommodityAndSubCommodityExportToExcel(commodityCode,subCommodityCode, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findByCommodityAndSubCommodity(commodityCode,
						subCommodityCode, statusFilter, false, x, DEFAULT_PAGE_SIZE,
						ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for search critical is Department/SubDepartment and class code and commodity
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param classCode The class to look for data about.
	 * @param commodityCode The commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportDepartmentClassCommodityToCsv", headers = "Accept=text/csv")
	public void findByDepartmentClassCommodityExport(@RequestParam(name = "department", required = false) String department,
													 @RequestParam(name = "subdepartment", required = false) String subdepartment,
													 @RequestParam(name = "classCode", required = false) int classCode,
													 @RequestParam(name = "commodityCode", required = false) int commodityCode,
													 @RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
													 @RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
													 @RequestParam(value = "downloadId", required = false) String downloadId,
													 HttpServletRequest request, HttpServletResponse response){

		this.logDepartmentAndClassAndCommodityExportToExcel(department,subdepartment, classCode, commodityCode, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findByDepartmentAndClassAndCommodity(department,
						subdepartment, classCode, commodityCode, statusFilter, false, x, DEFAULT_PAGE_SIZE,
						ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for search critical is Department/SubDepartment and class code and commodity and subcommodity
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param commodityCode The commodity to look for data about.
	 * @param subCommodityCode The sub commodity to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportDepartmentCommoditySubCommodityToCsv", headers = "Accept=text/csv")
	public void findByDepartmentCommoditySubCommodityExport(@RequestParam(name = "department", required = false) String department,
															@RequestParam(name = "subdepartment", required = false) String subdepartment,
															@RequestParam(name = "commodityCode", required = false) int commodityCode,
															@RequestParam(name = "subCommodityCode", required = false) int subCommodityCode,
															@RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
															@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
															@RequestParam(value = "downloadId", required = false) String downloadId,
															HttpServletRequest request, HttpServletResponse response){

		this.logDepartmentAndCommodityAndSubCommodityExportToExcel(department,subdepartment,commodityCode, subCommodityCode, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findByDepartmentAndCommodityAndSubCommodity(department,
						subdepartment, commodityCode, subCommodityCode, statusFilter, false, x, DEFAULT_PAGE_SIZE,
						ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for search critical is Department/SubDepartment and class code.
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param classCode The class to look for data about.
	 * @param statusFilter The product status to filter on.
	 * @param totalRecordCount the total number of records.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportDepartmentClassToCsv", headers = "Accept=text/csv")
	public void findByDepartmentClassExport(@RequestParam(name = "department", required = false) String department,
											@RequestParam(name = "subdepartment", required = false) String subdepartment,
											@RequestParam(name = "classCode", required = false) int classCode,
											@RequestParam(name = "statusFilter", required = false) StatusFilter statusFilter,
											@RequestParam(name = "totalRecordCount", required = false) int totalRecordCount,
											@RequestParam(value = "downloadId", required = false) String downloadId,
											HttpServletRequest request, HttpServletResponse response){

		this.logDepartmentAndClassExportToExcel(department,subdepartment, classCode, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		double pageCount = Math.ceil((float)totalRecordCount/DEFAULT_PAGE_SIZE);
		try {
			for(int x=0; x<pageCount; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateDiscontinueCsv.getHeading());
				}
				response.getOutputStream().print(CreateDiscontinueCsv.createCsv(this.findByDepartmentAndClass(department,
						subdepartment, classCode, statusFilter, false, x, DEFAULT_PAGE_SIZE,
						ProductDiscontinueController.DEFAULT_SORT_COLUMN,
						ProductDiscontinueController.DEFAULT_SORT_DIRECTION, request)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Sets the NonEmptyParameterValidator for this object to use. This is mainly for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this object to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Sets service.
	 *
	 * @param service the service
	 */
	public void setService(ProductDiscontinueService service) {
		this.productDiscontinueService = service;
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
	 * Loops through the data of the PageableResult passed in an loads the lazily loaded objects needed by the
	 * ProductDiscontinue search functions of the REST endpoint.
	 *
	 * @param results The PageableResult to load data for.
	 * @return The PageableResult with its data resolved.
	 */
	private PageableResult<ProductDiscontinue> resolveResults(PageableResult<ProductDiscontinue> results) {
		results.getData().forEach(this.productDiscontinueResolver::fetch);
		return results;
	}

	/**
	 * Logs a user's request to get all the records from product discontinue.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindAll(String ip) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_ALL_MESSAGE, this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to get all records for multiple item codes.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param itemCodes The item codes the user is searching for.
	 */
	private void logFindByItemCodes(String ip, List<Long> itemCodes) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_MULTIPLE_ITEM_CODE_MESSAGE,
						this.userInfo.getUserId(), ip, ListFormatter.formatAsString(itemCodes)));
	}

	/**
	 * Log's a user's request to get all records for multiple UPCs.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param upcs The UPCs the user is searching for.
	 */
	private void logFindByUpcs(String ip, List<Long> upcs) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_MULTIPLE_UPC_MESSAGE, this.userInfo.getUserId(),
						ip, ListFormatter.formatAsString(upcs)));
	}

	/**
	 * Log's a user's request to get records for multiple product IDs.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param productIds The product IDs the user is searching for.
	 */
	private void logFindByProductIds(String ip, List<Long> productIds) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_MULTIPLE_PRODUCT_IDS_MESSAGE,
						this.userInfo.getUserId(), ip, ListFormatter.formatAsString(productIds))
		);
	}

	/**
	 * Log's a user's request to get records for a bdm.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param bdm The bdm the user is searching for.
	 */
	private void logFindByBdm(String ip, String bdm) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_BDM_MESSAGE,
						this.userInfo.getUserId(), ip, bdm)
		);
	}

	/**
	 * Log's a user's request to get records for a sub department.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param department The department the user is searching for.
	 * @param subDepartment The subDepartment the user is searching for.
	 */
	private void logFindBySubDepartment(String ip, String department, String subDepartment) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_SUB_DEPARTMENT_MESSAGE,
						this.userInfo.getUserId(), ip, department, subDepartment)
		);
	}

	/**
	 * Log's a user's request to get records for a class and commodity.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param classCode The class the user is searching for.
	 * @param commodity The commodity the user is searching for.
	 */
	private void logFindByClassAndCommodity(String ip, int classCode, int commodity) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_CLASS_AND_COMMODITY_MESSAGE,
						this.userInfo.getUserId(), ip, classCode, commodity)
		);
	}

	/**
	 * Log's a user's request to get records for a sub commodity.
	 * @param ip The IP address th user is logged in from.
	 * @param classCode The class the user is searching for.
	 * @param commodity The commodity the user is searching for.
	 * @param subCommodity The subCommodity the user is searching for.
	 */
	private void logFindBySubCommodity(String ip, int classCode,
									   int commodity, int subCommodity) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_SUB_COMMODITY_MESSAGE,
						this.userInfo.getUserId(), ip, classCode, commodity, subCommodity)
		);
	}

	/**
	 * Log's a user's request to get a sub-department excel export.
	 *
	 * @param department The department.
	 * @param subdepartment the sub-department.
	 * @param ip ip The IP address th user is logged in from.
	 */
	private void logDepartmentExportToExcel(String department, String subdepartment, String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.SUB_DEPARTMENT_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, department, subdepartment));
	}

	/**
	 * Log's a user's request to get a class and commodity excel export.
	 *
	 * @param classCode The class code.
	 * @param commodity The commodity code.
	 * @param ip The IP address th user is logged in from.
	 */
	private void logClassCommodityExportToExcel(int classCode, int commodity,String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.CLASS_COMMODITY_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, classCode, commodity));
	}

	/**
	 * Log's a user's request to get a subcommodity excel export.
	 *
	 * @param classCode The class code.
	 * @param commodity The commodity code.
	 * @param subCommodity The subcommodity code.
	 * @param ip The IP address th user is logged in from.
	 */
	private void logSubCommodityExportToExcel(int classCode, int commodity, int subCommodity,String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.SUB_COMMODITY_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, classCode, commodity, subCommodity));
	}

	/**
	 * Log's a user's request to get a bdm excel export.
	 *
	 * @param bdm the BDM.
	 * @param ip The IP address th user is logged in from.
	 */
	private void logBdmExportToExcel(String bdm,String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.BDM_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, bdm));
	}

	/**
	 * Log's a user's request to get a item code excel export.
	 *
	 * @param params the item codes requested
	 * @param ip The IP address th user is logged in from.
	 */
	private void logItemCodeExportToExcel(String params,String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.ITEM_CODE_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, params));
	}

	/**
	 * Log's a user's request to get a UPC excel export.
	 *
	 * @param params the UPCs requested
	 * @param ip The IP address th user is logged in from.
	 */
	private void logUpcExportToExcel(String params,String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.UPC_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, params));
	}

	/**
	 * Log's a user's request to get a ProductId excel export.
	 *
	 * @param params the product ids requested
	 * @param ip The IP address th user is logged in from.
	 */
	private void logProductIdExportToExcel(String params,String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.PRODUCT_ID_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, params));
	}

	/**
	 * Log's a user's request to get records for a vendor number.
	 * @param ip The IP address th user is logged in from.
	 * @param vendorNumber The vendor number the user is searching for.
	 */
	private void logFindByVendorNumber(String ip, int vendorNumber) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_VENDOR_MESSAGE,
						this.userInfo.getUserId(), ip, vendorNumber)
		);
	}

	/**
	 * Log's a user's request to get the discontinue product for a commodity and sub commodity.
	 * @param ip The IP address th user is logged in from.
	 * @param commodity The commodity the user is searching for.
	 * @param subCommodity The subCommodity the user is searching for.
	 */
	private void logFindByCommodityAndSubCommodity(String ip, int commodity, int subCommodity) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_COMMODITY_AND_SUB_COMMODITY_MESSAGE,
						this.userInfo.getUserId(), ip, commodity, subCommodity)
		);
	}

	/**
	 * Log's a user's request to get the discontinue product for a department and class and commodity and sub commodity.
	 * @param ip The IP address th user is logged in from.
	 * @param classCode The class the user is searching for.
	 * @param commodity The commodity the user is searching for.
	 * @param subCommodity The subCommodity the user is searching for.
	 */
	private void logFindByDepartmentAndClassAndCommodityAndSubCommodity(String ip, String department, String subDepartment,
																		int classCode, int commodity, int subCommodity) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY_MESSAGE,
						this.userInfo.getUserId(), ip, department, subDepartment, classCode, commodity, subCommodity)
		);
	}

	/**
	 * Log's a user's request to get the discontinue product for a department and class and commodity.
	 * @param ip The IP address th user is logged in from.
	 * @param classCode The class the user is searching for.
	 * @param commodity The commodity the user is searching for.
	 */
	private void logFindByDepartmentAndClassAndCommodity(String ip, String department, String subDepartment,
														 int classCode, int commodity) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_DEPARTMENT_CLASS_COMMODITY_MESSAGE,
						this.userInfo.getUserId(), ip, department, subDepartment, classCode, commodity)
		);
	}

	/**
	 * Log's a user's request to get the discontinue product for a department and commodity and sub commodity.
	 * @param ip The IP address th user is logged in from.
	 * @param commodity The commodity the user is searching for.
	 * @param subCommodity The subCommodity the user is searching for.
	 */
	private void logFindByDepartmentAndCommodityAndSubCommodity(String ip, String department, String subDepartment,
																		int commodity, int subCommodity) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_DEPARTMENT_COMMODITY_SUB_COMMODITY_MESSAGE,
						this.userInfo.getUserId(), ip, department, subDepartment, commodity, subCommodity)
		);
	}

	/**
	 * Log's a user's request to get the discontinue product for a department and class .
	 * @param ip The IP address th user is logged in from.
	 * @param classCode The class the user is searching for.
	 */
	private void logFindByDepartmentAndClass(String ip, String department, String subDepartment, int classCode) {
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.FIND_BY_DEPARTMENT_CLASS_MESSAGE, this.userInfo.getUserId(), ip, department, subDepartment, classCode)
		);
	}

	/**
	 * Log's a user's request to get the discontinue product basing on sub-department and class code and commodity and subcommodity excel export.
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param classCode The class to look for data about.
	 * @param commodity The commodity to look for data about.
	 * @param subCommodity The sub commodity to look for data about.
	 *
	 * @param ip ip The IP address th user is logged in from.
	 */
	private void logDepartmentAndClassAndCommodityAndSubCommodityExportToExcel(String department, String subdepartment, int classCode, int commodity, int subCommodity, String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, department, subdepartment, classCode, commodity, subCommodity));
	}

	/**
	 * Log's a user's request to get the discontinue product basing on sub-department and class code and commodity excel export.
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param classCode The class to look for data about.
	 * @param commodity The commodity to look for data about.
	 * @param ip ip The IP address th user is logged in from.
	 */
	private void logDepartmentAndClassAndCommodityExportToExcel(String department, String subdepartment, int classCode, int commodity, String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.DEPARTMENT_CLASS_COMMODITY_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, department, subdepartment, classCode, commodity));
	}

	/**
	 * Log's a user's request to get the discontinue product basing on sub-department and commodity and sub-commodity excel export.
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param commodity The commodity to look for data about.
	 * @param subCommodity The sub commodity to look for data about.
	 * @param ip ip The IP address th user is logged in from.
	 */
	private void logDepartmentAndCommodityAndSubCommodityExportToExcel(String department, String subdepartment, int commodity, int subCommodity, String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.DEPARTMENT_COMMODITY_SUB_COMMODITY_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, department, subdepartment, commodity, subCommodity));
	}

	/**
	 * Log's a user's request to get the discontinue product basing on sub-department and class code and commodity and subcommodity excel export.
	 *
	 * @param department The department.
	 * @param subdepartment The subdepartment.
	 * @param classCode The class to look for data about.
	 * @param ip ip The IP address th user is logged in from.
	 */
	private void logDepartmentAndClassExportToExcel(String department, String subdepartment, int classCode, String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.DEPARTMENT_CLASS_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, department, subdepartment, classCode));
	}

	/**
	 * Log's a user's request to get the discontinue product basing on sub-department and class code and commodity and subcommodity excel export.
	 *
	 * @param commodity The commodity to look for data about.
	 * @param subCommodity The sub commodity to look for data about.
	 * @param ip ip The IP address th user is logged in from.
	 */
	private void logCommodityAndSubCommodityExportToExcel(int commodity, int subCommodity, String ip){
		ProductDiscontinueController.logger.info(
				String.format(ProductDiscontinueController.COMMODITY_SUB_COMMODITY_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, commodity, subCommodity));
	}
}