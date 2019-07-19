/*
 *  AuthorizationController
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ItemSizeUomCode;
import com.heb.pm.repository.ItemSizeUomCodeRepository;
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
import java.util.Map;

/**
 * Rest Endpoint for authorization.
 *
 * @author vn70529
 * @since 2.23.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/authorization")
@AuthorizedResource(ResourceConstants.AUTHORIZATION)
public class AuthorizationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);
	/* Log messages.*/
	private static final String SEARCH_LOG_MESSAGE =
			"User %s from IP %s has requested find authorize items by upc: %s, store id: %s";
	/*Value of url*/
	private static final String FIND_ITEMS_URL = "/findItems";
    private static final String GET_STORES_URL = "/getStores";
	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private UserInfo userInfo;
	/**
	 * Method to return a object contain the list of items for authorize and isUPCWasSupplied and flexWeightSwitch by upc and store id.
	 *
	 * @param upc              the upc.
	 * @param storeId          the store id.
	 * @param request           the request.
	 * @return a object contain the list of authorize items and isUPCWasSupplied and flexWeightSwitch.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = FIND_ITEMS_URL)
	public Map<String, Object> findItems(@RequestParam(value = "upc") long upc,
										 @RequestParam(value = "storeId") Long storeId,
										 HttpServletRequest request) {
		LOGGER.info(String.format(AuthorizationController.SEARCH_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), upc, storeId));
		return authorizationService.findItems(upc, storeId);
	}

    /**
     * Returns the list of stores and is default store status.
     * @param request the request.
     * @return a Map with isDefaultStore key and store key.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = GET_STORES_URL)
	public Map<String, Object> getStores(HttpServletRequest request){
        return authorizationService.getStores(userInfo.getUserStore());
    }
}