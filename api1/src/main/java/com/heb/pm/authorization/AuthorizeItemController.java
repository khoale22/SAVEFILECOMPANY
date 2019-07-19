/*
 *  AuthorizeItemController
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Rest Endpoint for authorize item.
 *
 * @author vn70529
 * @since 2.23.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/authorizeItem")
@AuthorizedResource(ResourceConstants.AUTHORIZE_ITEM)
public class AuthorizeItemController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeItemController.class);
	/*Value of url*/
	private static final String SUBMIT_AUTHORIZE_ITEM_URL = "/submit";
    /* Log messages.*/
    private static final String AUTHORIZE_ITEM_LOG_MESSAGE =
            "User %s from IP %s has requested authorize items for upc: %s";
	/**
	 * Success message.
	 */
	private static final String AUTHORIZE_ITEM_SUCCESS = "Details have been submitted successfully!";
	@Autowired
	private AuthorizeItemService authorizeItemService;
    @Autowired
    private UserInfo userInfo;

	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = SUBMIT_AUTHORIZE_ITEM_URL)
	public ModifiedEntity<String> submit(@RequestBody AuthorizeItem authorizeItem, HttpServletRequest request) throws Exception {
		authorizeItemService.submitAuthorizeItem(authorizeItem);
		LOGGER.info(String.format(AuthorizeItemController.AUTHORIZE_ITEM_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr(), authorizeItem.getUpc()));
        return new ModifiedEntity<>(AUTHORIZE_ITEM_SUCCESS, AUTHORIZE_ITEM_SUCCESS);
	}
}