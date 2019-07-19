/*
 *  WicLebController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.wicLeb;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.SellingUnitWic;
import com.heb.pm.entity.WicCategoryRetailSize;
import com.heb.pm.entity.WicSubCategory;
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
 * REST endpoint for functions related to wic leb.
 *
 * @author vn70529
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + WicLebController.WIC_LEB_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_WIC_LEB)
public class WicLebController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WicLebController.class);

	protected static final String WIC_LEB_URL = "/wicLeb";
	private static final String FIND_ALL_WIC_LEB_ORDER_BY_NAME_URL = "/findAllWicLebs";
	private static final String FIND_ALL_LEB_UPCS_NAME_URL = "/findAllLebUpcsByWicCategoryIdAndWicSubCategoryId";
	private static final String FIND_ALL_LEB_SIZES_NAME_URL = "/findAllLebSizesByWicCategoryIdAndWicSubCategoryId";
	/**
	 * Holds log message for find all wic lebs.
	 */
	private static final String FIND_ALL_WIC_LEB_MESSAGE = "User %s from IP %s requested all wic lebs.";
	/**
	 * Holds the message for find all leb upcs.
	 */
	private static final String FIND_ALL_LEB_UPCS_MESSAGE = "User %s from IP %s requested all lebs UPCs by wicCategoryId: %s and wicSubCategoryId: %s.";
	/**
	 * Holds the message or find all leb sizes.
	 */
	private static final String FIND_ALL_LEB_SIZES_MESSAGE = "User %s from IP %s requested all lebs Sizes by wicCategoryId: %s and wicSubCategoryId: %s.";
	@Autowired
	private WicLebService wicLebService;
	@Autowired
	private UserInfo userInfo;
	/**
	 * Revolver for ProductScanCodeWic
	 */
	private ProductScanCodeWicResolver productScanCodeWicResolver = new ProductScanCodeWicResolver();
	/**
	 * Returns a list of all wic sub categories ordered by id.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return A List of all wic sub categories ordered by id.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = WicLebController.FIND_ALL_WIC_LEB_ORDER_BY_NAME_URL)
	public List<WicSubCategory> findAllWicLebs(HttpServletRequest request){
		this.logRequest(FIND_ALL_WIC_LEB_MESSAGE, request.getRemoteAddr());
		return this.wicLebService.findAllWicLebs();
	}
	/**
	 * Returns a list of leb upcs by wic category id and wic sub category id.
	 *
	 * @param wicCategoryId the id of wic category to search.
	 * @param wicSubCategoryId the id of wic sub category to search.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of all productScanCodeWics.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = WicLebController.FIND_ALL_LEB_UPCS_NAME_URL)
	public List<SellingUnitWic> findAllLebUpcs(@RequestParam(value = "wicCategoryId") long wicCategoryId,
											   @RequestParam(value = "wicSubCategoryId") long wicSubCategoryId,
											   HttpServletRequest request){
		this.logRequest(FIND_ALL_LEB_UPCS_MESSAGE, request.getRemoteAddr(), wicCategoryId, wicSubCategoryId);
		List<SellingUnitWic> wicSubcategories = this.wicLebService.findLebUpcsByKeyWicCategoryIdAndKeyWicSubCategoryId(wicCategoryId, wicSubCategoryId);
		wicSubcategories.forEach(this.productScanCodeWicResolver::fetch);
		return wicSubcategories;
	}
	/**
	 * Returns a list of leb sizes by wic category id and wic sub category id.
	 *
	 * @param wicCategoryId the id of wic category to search.
	 * @param wicSubCategoryId the id of wic sub category to search.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of all WicCategoryRetailSizes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = WicLebController.FIND_ALL_LEB_SIZES_NAME_URL)
	public List<WicCategoryRetailSize> findAllLebSizes(@RequestParam(value = "wicCategoryId") long wicCategoryId,
																  @RequestParam(value = "wicSubCategoryId") long wicSubCategoryId,
																  HttpServletRequest request){
		this.logRequest(FIND_ALL_LEB_SIZES_MESSAGE, request.getRemoteAddr(), wicCategoryId, wicSubCategoryId);
		return this.wicLebService.findAllLebSizesByWicCategoryIdAndWicSubCategoryId(wicCategoryId, wicSubCategoryId);
	}
	/**
	 * Logs a users request for leb wic.
	 *
	 * @param message the message needs to log.
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logRequest(String message, String ipAddress) {
		WicLebController.LOGGER.info(String.format(message,
				this.userInfo.getUserId(), ipAddress));
	}
	/**
	 * Logs a users request for leb wic.
	 *
	 * @param @param message the message needs to log.
	 * @param ipAddress The IP address the user is logged in from.
	 * @param wicCategoryId the id of wicCategory.
	 * @param wicSubCategoryId the id of wicSubCategory.
	 */
	private void logRequest(String message, String ipAddress, long wicCategoryId, long wicSubCategoryId) {
		WicLebController.LOGGER.info(String.format(message,
				this.userInfo.getUserId(), ipAddress, wicCategoryId, wicSubCategoryId));
	}
}
