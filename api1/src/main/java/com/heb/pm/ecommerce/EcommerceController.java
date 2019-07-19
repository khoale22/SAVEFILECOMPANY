/*
 * com.heb.pm.entity.ProductDiscontinue
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */package com.heb.pm.ecommerce;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Attribute;
import com.heb.pm.entity.TargetSystemAttributePriority;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for ECommerce.
 *
 * @author s753601
 * @since 2.5.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + EcommerceController.ECOMMERCE_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class EcommerceController {

    private static final Logger logger = LoggerFactory.getLogger(EcommerceController.class);

    protected static final String ECOMMERCE_URL = "/eCommerce";

    // Log Messages
    private static final String GET_SOURCE_PRIORITY_TABLE =
            "User %s from IP %s has requested the source priority table";

    private LazyObjectResolver<TargetSystemAttributePriority> objectResolver = new eCommerceResolver();

    @Autowired private UserInfo userInfo;
    @Autowired private EcommerceService ecommerceService;

	/**
	 * Get source priority table list.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "sourcePriority")
    public List<TargetSystemAttributePriority> getSourcePriorityTable(HttpServletRequest request){

        this.logFindByProductIds(request.getRemoteAddr());

        return  this.resolveResults(this.ecommerceService.getSourcePriorityTable());
    }

    /**
     * Log's a user's request to get all records for a prodId.
     *
     * @param ip The IP address th user is logged in from.
     */
    private void logFindByProductIds(String ip){
        EcommerceController.logger.info(String.format(EcommerceController.GET_SOURCE_PRIORITY_TABLE, this.userInfo.getUserId(), ip));
    }

    /**
     * Loops through the data of the PageableResult passed in an loads the lazily loaded objects needed by the
     * Product search functions of the REST endpoint.
     *
     * @param results The PageableResult to load data for.
     * @return The PageableResult with its data resolved.
     */
    private List<TargetSystemAttributePriority> resolveResults(List<TargetSystemAttributePriority> results) {
        results.forEach(this.objectResolver::fetch);
        return results;
    }

	/**
	 * Sets the UserInfo for this class to use. This is primarily for testing.
	 *
	 * @param userInfo The UserInfo for this class to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
