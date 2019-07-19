/*
 *  ProductGroupController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.CustomerProductGroupMembership;
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
 * This represents the Product group controller
 *
 * @author l730832
 * @since 2.14.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductGroupController.PRODUCT_GROUP_URL)
@AuthorizedResource(ResourceConstants.ONLINE_ATTRIBUTES)
public class ProductGroupController {

	private static final Logger logger = LoggerFactory.getLogger(ProductGroupController.class);

	protected static final String PRODUCT_GROUP_URL = "/productGroup";

	protected static final String GET_PRODUCT_GROUP = "/getProductGroup";

	private static final String LOG_GET_PRODUCT_GROUPS = "User %s from IP %s has requested product group information for product: %s";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ProductGroupService service;

	private CustomerProductGroupMembershipResolver customerProductGroupMembershipResolver = new CustomerProductGroupMembershipResolver();

	/**
	 * This gets all of the product groups that is tied to that prod id.
	 * @param prodId the product id
	 * @param request the http servlet request
	 * @return list of product groups.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductGroupController.GET_PRODUCT_GROUP)
	public List<CustomerProductGroupMembership> getProductGroups(
			@RequestParam(value = "prodId") Long prodId, HttpServletRequest request) {
		this.logGetProductGroups(request.getRemoteAddr(), prodId);
		List<CustomerProductGroupMembership> customerProductGroupMemberships = this.service.getProductGroupList(prodId);
		customerProductGroupMembershipResolver.fetch(customerProductGroupMemberships);
		return customerProductGroupMemberships;
	}

	private void logGetProductGroups(String remoteAddr, Long prodId) {
		ProductGroupController.logger.info(
				String.format(ProductGroupController.LOG_GET_PRODUCT_GROUPS, this.userInfo.getUserId(), remoteAddr, prodId)
		);
	}
}
