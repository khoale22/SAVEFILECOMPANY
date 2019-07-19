/*
 *  ProductGroupSearchController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productGroup;


import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.CustomerProductGroup;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * REST Controller to support the generic product group search framework.
 *
 * @author vn86116
 * @since 2.15.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/productGroup")
@AuthorizedResource(ResourceConstants.PRODUCT_GROUP_SEARCH)
public class ProductGroupSearchController {
	private static final Logger logger = LoggerFactory.getLogger(ProductGroupSearchController.class);

	/* Log messages.*/
	private static final String SEARCH_LOG_MESSAGE =
			"User %s from IP %s has requested find product group by id: %s, product name: %s, product group type: %s, customer hierarchy: %s";
	/*Value of url*/
	private static final String PRODUCT_SEARCH_URL = "/findProductGroup";

	private static final String PRODUCT_GRP_IDS_LOG_MESSAGE =
			"User %s from IP %s has requested find all product group ids by id: %s, product name: %s, product group type: %s, customer hierarchy: %s";

	private static final String PRODUCT_GRP_BY_ID_LOG_MESSAGE =
			"User %s from IP %s has requested find product group by product group id %s";

	private static final String CHECK_LOWEST_LEVEL_LOG_MESSAGE = "User %s from IP %s has requested check lowest level by lowest level id %s";

	private static final String FIND_ALL_PRODUCT_GROUP_ID = "/findProductGroupIds";

	private static final String FIND_PRODUCT_GROUP_BY_ID = "/{productGroupId}";

	private static final String CHECK_LOWEST_LEVEL = "/checkLowestLevel";

	@Autowired
	private ProductGroupSearchService productGroupService;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Method to return a PageableResult object contain result search.
	 *
	 * @param page              the page display.
	 * @param pageSize          the number element per page.
	 * @param productGroupId    the product group id.
	 * @param productGroupName  the product group name.
	 * @param productGroupType  the product group type.
	 * @param customerHierarchyId the customer hierarchy.
	 * @param request           the request.
	 * @return data The PageableResult object.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = PRODUCT_SEARCH_URL)
	public PageableResult<CustomerProductGroup> findProductGroup(@RequestParam(value = "page") int page,
																 @RequestParam(value = "pageSize") int pageSize,
																 @RequestParam(value = "productGroupId", required = false) Long productGroupId,
																 @RequestParam(value = "productGroupName", required = false) String productGroupName,
																 @RequestParam(value = "productGroupType", required = false) String productGroupType,
																 @RequestParam(value = "customerHierarchyId", required = false) Long customerHierarchyId,
																 @RequestParam(value = "firstSearch", required = false) boolean firstSearch,
																 HttpServletRequest request) {

		logger.info(String.format(ProductGroupSearchController.SEARCH_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(),
				productGroupId, productGroupName, productGroupType, customerHierarchyId));
		return this.productGroupService.findProductGroup(page, pageSize, productGroupId,
				productGroupName, productGroupType,customerHierarchyId, firstSearch);
	}

	/**
	 * Find product group ids.
	 *
	 * @param productGroupId the product group id.
	 * @param productGroupName the product group name.
	 * @param productGroupType the product group type.
	 * @param customerHierarchyId customer hierarchy id.
	 * @param request the request.
	 * @return list of product group ids.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_PRODUCT_GROUP_ID)
	public List<Long> findProductGroupIds(@RequestParam(value = "productGroupId",required = false) Long productGroupId,
										  @RequestParam(value = "productGroupName",required = false) String productGroupName,
										  @RequestParam(value = "productGroupType",required = false) String productGroupType,
										  @RequestParam(value = "customerHierarchyId",required = false) Long customerHierarchyId,
										  HttpServletRequest request){
		logger.info(String.format(PRODUCT_GRP_IDS_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(),
				productGroupId, productGroupName, productGroupType, customerHierarchyId));
		return this.productGroupService.findProductGroupIds(productGroupId, productGroupName, productGroupType, customerHierarchyId);
	}

	/**
	 * Find Product Group by product group ids.
	 *
	 * @param productGroupId the product group id.
	 * @param request the request.
	 * @return CustomerProductGroup object.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = FIND_PRODUCT_GROUP_BY_ID)
	public CustomerProductGroup findProductGroupById(@PathVariable Long productGroupId, HttpServletRequest request){
		logger.info(String.format(PRODUCT_GRP_BY_ID_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), productGroupId ));
		return this.productGroupService.findProductGroupById(productGroupId);
	}

	/**
	 * Find Product Group by product group ids.
	 *
	 * @param lowestLevelId the product group id.
	 * @param request the request.
	 * @return CustomerProductGroup object.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CHECK_LOWEST_LEVEL)
	public Map<String, Boolean> checkLowestLevel (@RequestParam(value = "lowestLevelId") Long lowestLevelId, HttpServletRequest request){
		logger.info(String.format(CHECK_LOWEST_LEVEL_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), lowestLevelId));
		return this.productGroupService.checkLowestLevel(lowestLevelId);
	}
}