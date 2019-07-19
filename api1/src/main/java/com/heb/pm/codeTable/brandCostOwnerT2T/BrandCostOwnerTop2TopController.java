/*
 * BrandCostOwnerTop2TopController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.codeTable.brandCostOwnerT2T;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.productBrand.ProductBrandController;
import com.heb.pm.entity.CostOwner;
import com.heb.pm.entity.ProductBrand;
import com.heb.pm.entity.ProductBrandCostOwner;
import com.heb.pm.entity.TopToTop;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * REST endpoint for functions related to get the list of brand cost owner top 2 tops.
 *
 * @author vn70529
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		BrandCostOwnerTop2TopController.BRND_CST_OWNR_T2T_URL)
@AuthorizedResource(ResourceConstants.BRND_CST_OWNR_T2T)
public class BrandCostOwnerTop2TopController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrandCostOwnerTop2TopController.class);
	/**
	 * Holds the path to request this controller.
	 */
	protected static final String BRND_CST_OWNR_T2T_URL = "/brndCstOwnrT2T";
	/**
	 * Holds the path to request the list of brand cost owner t2ts.
	 */
	private static final String FIND_ALL = "/findAllBrndCstOwnrT2Ts";
	/**
	 * Holds the path to request the list of brand cost owner t2ts by criteria.
	 */
	private static final String FIND_BY_CRITERIA = "/findBrndCstOwnrT2TsByCriteria";
	/**
	 * Holds the path to request to export the list of brand cost owner t2ts to excel file.
	 */
	private static final String EXPORT_ALL_TO_CSV = "/exportAllToCSV";
	/**
	 * Holds the path to request to export the list of brand cost owner t2ts to excel file.
	 */
	private static final String EXPORT_BY_CRITERIA_TO_CSV = "/exportByCriteriaToCSV";
	private static final String URL_FILTER_COST_OWNER_BY_ID_AND_DESCRIPTION = "/filterCostOwnerByIdAndDescription";
	private static final String URL_FILTER_TOP_TO_TOP_BY_ID_AND_NAME = "/filterTopToTopByIdAndName";
	private static final String URL_FILTER_BY_SELECTED_DATA = "/filterBySelectedData";
	/**
	 * Log error message.
	 */
	private static final String LOG_ERROR_MESSAGE_EXPORT_BRND_CST_OWNR_T2TS =
			"User %s from IP %s requested the brand cost owner top 2 top report by product-brand:%s, cost-owner:%s, T2T:%s: %s";
	/**
	 * Holds the message for get brand list.
	 */
	private static final String LOG_MESSAGE_GET_BRND_CST_OWNR_T2TS_BY_FILTER =
			"User %s from IP %s requested brand cost owner top 2 top report by product-brand:%s, cost-owner:%s, T2T:%s";
	private static final String FILTER_COST_OWNER_BY_ID_AND_DESCRIPTION_MESSAGE = "User %s from IP %s requested to filter cost owner by text ='%s'.";
	private static final String FILTER_TOP_TO_TOP_BY_ID_AND_NAME_MESSAGE = "User %s from IP %s requested to filter top to top by text ='%s'.";
	private static final String FILTER_BY_SELECTED_DATA_MESSAGE =
			"User %s from IP %s requested to filter by selected product brand id ='%s' and cost owner id='%s' and top to top id = %s.";
	private static final int DEFAULT_PAGE = 0;

	@Autowired
	private BrandCostOwnerTop2TopService brandCostOwnerTop2TopService;
	@Autowired
	private UserInfo userInfo;
	/**
	 * The resolver for productBrandCostOwner.
	 */
	private ProductBrandCostOwnerResolver productBrandCostOwnerResolver = new ProductBrandCostOwnerResolver();

	/**
	 * Get the list of BrandCostOwnerT2Ts by paging and the params that you want to filter as product brand description,
	 * cost owner name and top 2 top name.
	 *
	 * @param page                    the start position that want to get.
	 * @param pageSize                the number of rows that want to get per page.
	 * @param includeCount            Set to true to include the record counts.
	 * @param request                 The HTTP request that initiated the request.
	 * @return the list of brand cost owner top 2 tops.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = BrandCostOwnerTop2TopController.FIND_ALL)
	public Page<ProductBrandCostOwner> findAll( @RequestParam(value = "page") int page,
												@RequestParam(value = "pageSize") int pageSize,
												@RequestParam(value = "includeCount") boolean includeCount,
			HttpServletRequest request) {
		// Log request.
		logRequest(LOG_MESSAGE_GET_BRND_CST_OWNR_T2TS_BY_FILTER, userInfo.getUserId(), request.getRemoteAddr(), null, null, null);
		Page<ProductBrandCostOwner> pageResults = this.brandCostOwnerTop2TopService.findAll(page, pageSize, includeCount);
		pageResults.getContent().forEach(this.productBrandCostOwnerResolver::fetch);
		return pageResults;
	}
	/**
	 * Get the list of BrandCostOwnerT2Ts by paging and the params that you want to filter as product brand description,
	 * cost owner name and top 2 top name.
	 *
	 * @param productBrandId the description or id to search brand for.
	 * @param costOwner           the name or id to search cost owner for.
	 * @param top2Top             the name or id to search top 2 top for.
	 * @param page                    the start position that want to get.
	 * @param pageSize                the number of rows that want to get per page.
	 * @param includeCount            Set to true to include the record counts.
	 * @param request                 The HTTP request that initiated the request.
	 * @return the list of brand cost owner top 2 tops.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = BrandCostOwnerTop2TopController.FIND_BY_CRITERIA)
	public Page<ProductBrandCostOwner> findByCriteria(@RequestParam(required=false, value = "productBrandId") String productBrandId,
													  @RequestParam(required=false, value = "costOwner") String costOwner,
													  @RequestParam(required=false, value = "top2Top") String top2Top,
													  @RequestParam(value = "page") int page,
													  @RequestParam(value = "pageSize") int pageSize,
													  @RequestParam(value = "includeCount") boolean includeCount,
													  HttpServletRequest request) {
		// Log request.
		logRequest(LOG_MESSAGE_GET_BRND_CST_OWNR_T2TS_BY_FILTER, userInfo.getUserId(), request.getRemoteAddr(), productBrandId, costOwner, top2Top);
		Page<ProductBrandCostOwner> pageResults = this.brandCostOwnerTop2TopService.findByCriteria(productBrandId, costOwner, top2Top, page, pageSize, includeCount);
		pageResults.getContent().forEach(this.productBrandCostOwnerResolver::fetch);
		return pageResults;
	}
	/**
	 * Generates a CSV of all BrandCostOwnerT2Ts. This includes all BrandCostOwnerT2Ts
	 * by the params that you want to filter as product brand description, cost owner name and top 2 top name.
	 *
	 * @param downloadId              An ID to put into a cookie so the front end can identify this download.
	 * @param request                 The HTTP request that initiated the request.
	 * @param response                The HTTP response that the report will be streamed to.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = BrandCostOwnerTop2TopController.EXPORT_ALL_TO_CSV, headers = "Accept=text/csv")
	public void exportAllToCSV(@RequestParam(value = "downloadId", required = false) String downloadId,
										   HttpServletRequest request, HttpServletResponse response) {
		// Log request.
		logRequest(LOG_MESSAGE_GET_BRND_CST_OWNR_T2TS_BY_FILTER, userInfo.getUserId(), request.getRemoteAddr(), null, null, null);
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			this.brandCostOwnerTop2TopService.streamAll(response.getOutputStream());
		} catch (IOException e) {
			LOGGER.error(String.format(LOG_ERROR_MESSAGE_EXPORT_BRND_CST_OWNR_T2TS, userInfo.getUserId(), request.getRemoteAddr(), null, null, null, e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Generates a CSV of the list of BrandCostOwnerT2Ts. This includes all BrandCostOwnerT2Ts
	 * by the params that you want to filter as product brand description, cost owner name and top 2 top name.
	 *
	 * @param productBrand the description or id to search brand for.
	 * @param costOwner           the name or id to search cost owner for.
	 * @param top2Top             the name or id to search top 2 top for.
	 * @param downloadId              An ID to put into a cookie so the front end can identify this download.
	 * @param request                 The HTTP request that initiated the request.
	 * @param response                The HTTP response that the report will be streamed to.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = BrandCostOwnerTop2TopController.EXPORT_BY_CRITERIA_TO_CSV, headers = "Accept=text/csv")
	public void exportByCriteriaToCSV(@RequestParam(value = "productBrand", required = false) String productBrand,
									  @RequestParam(value = "costOwner", required = false) String costOwner,
									  @RequestParam(value = "top2Top", required = false) String top2Top,
									  @RequestParam(value = "downloadId", required = false) String downloadId,
									  HttpServletRequest request, HttpServletResponse response) {
		// Log request.
		logRequest(LOG_MESSAGE_GET_BRND_CST_OWNR_T2TS_BY_FILTER, userInfo.getUserId(), request.getRemoteAddr(), productBrand, costOwner, top2Top);
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			this.brandCostOwnerTop2TopService.streamByCriteria(response.getOutputStream(), productBrand, costOwner, top2Top);
		} catch (IOException e) {
			LOGGER.error(String.format(LOG_ERROR_MESSAGE_EXPORT_BRND_CST_OWNR_T2TS, userInfo.getUserId(), request.getRemoteAddr(), productBrand, costOwner, top2Top, e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Logs a users request for all brand cost owner top 2 tops.
	 *
	 * @param logMessage the message to log.
	 * @param userId current user id
	 * @param ip request ip.
	 * @param productBrand the description or id to search brand for.
	 * @param costOwner the name or id to search top 2 top for.
	 * @param t2t the name or id to search top 2 top for.
	 */
	private void logRequest(String logMessage, String userId, String ip, String productBrand , String costOwner, String t2t) {
		BrandCostOwnerTop2TopController.LOGGER.info(String.format(logMessage, userId, ip, productBrand, costOwner, t2t));
	}

	/**
	 * Find cost owner top to top by search text
	 * @param searchText the text to search cost owner
	 * @param pageSize item load into dropdown
	 * @param includeCount include count
	 * @param request
	 * @return PageableResult
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET,
			value = BrandCostOwnerTop2TopController.URL_FILTER_COST_OWNER_BY_ID_AND_DESCRIPTION)
	public PageableResult<CostOwner> findCostOwnerByIdAndName(
			@RequestParam(value = "searchText", required = false) String searchText,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "includeCount", required = false) Boolean includeCount,
			HttpServletRequest request) {
		BrandCostOwnerTop2TopController.LOGGER.info(String.format(FILTER_COST_OWNER_BY_ID_AND_DESCRIPTION_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(),searchText));
		return this.brandCostOwnerTop2TopService.findCostOwnerByIdAndName(BrandCostOwnerTop2TopController.DEFAULT_PAGE,pageSize,
				searchText);
	}

	/**
	 * Find top to top by search text. It's will search contain of id or name top to top
	 * @param searchText the text to searcj top to top
	 * @param pageSize item load into dropdown
	 * @param includeCount include count
	 * @param request
	 * @return PageableResult
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET,
			value = BrandCostOwnerTop2TopController.URL_FILTER_TOP_TO_TOP_BY_ID_AND_NAME)
	public PageableResult<TopToTop> findTopToTopByIdAndName(
			@RequestParam(value = "searchText", required = false) String searchText,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "includeCount", required = false) Boolean includeCount,
			HttpServletRequest request) {
		BrandCostOwnerTop2TopController.LOGGER.info(String.format(FILTER_TOP_TO_TOP_BY_ID_AND_NAME_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(),searchText));
		return this.brandCostOwnerTop2TopService.findTopToTopByIdAndName(BrandCostOwnerTop2TopController.DEFAULT_PAGE,pageSize,
				searchText);
	}
	/**
	 * Find data filter by selected product Brand, Cost Owner or Top To Top. It will search with id
	 * @param productBrandId the product brand id
	 * @param costOwnerId cost owner id
	 * @param topToTopId top to top id
	 * @param request
	 * @return PageableResult
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET,
			value = BrandCostOwnerTop2TopController.URL_FILTER_BY_SELECTED_DATA)
	public PageableResult<ProductBrand> findProductBrandsBySelectedFilter(
			@RequestParam(value = "productBrandId", required = false) Long productBrandId,
			@RequestParam(value = "costOwnerId", required = false) Integer costOwnerId,
			@RequestParam(value = "topToTopId", required = false) Integer topToTopId,
			HttpServletRequest request) {
		BrandCostOwnerTop2TopController.LOGGER.info(String.format(FILTER_BY_SELECTED_DATA_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(),productBrandId,costOwnerId,topToTopId));
		return this.brandCostOwnerTop2TopService.findProductBrandsBySelectedFilter(productBrandId,costOwnerId,topToTopId);
	}
}