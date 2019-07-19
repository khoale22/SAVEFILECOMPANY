/*
 *  FactoryController
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Factory;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents factory information.
 *
 * @author s573181
 * @since 2.6.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + FactoryController.FACTORY_URL)
@AuthorizedResource(ResourceConstants.CASE_PACK_VENDOR_ITEM_FACTORY_INFO)
public class FactoryController {

	private static final Logger logger = LoggerFactory.getLogger(FactoryController.class);

	protected static final String FACTORY_URL = "/factory";
	protected static final String ALL_FACTORIES_URL = "/allfactories";

	// Log Messages
	private static final String GET_ALL_FACTORIES = "User %s from IP %s has requested all factories.";

	@Autowired
	private FactoryService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Returns all factories.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return A list of factories.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ALL_FACTORIES_URL)
			public List<Factory> findAll(HttpServletRequest request){
	   	this.logFindAllFactories(request.getRemoteAddr());
	   	return this.service.findAll();
	}

	/**
	 * Logs a user's request to get all factories.
	 *
	 * @param ip The ip address of the request.
	 */
	private void logFindAllFactories(String ip){
		FactoryController.logger.info(String.format(
				FactoryController.GET_ALL_FACTORIES, this.userInfo.getUserId(), ip));
	}
}
