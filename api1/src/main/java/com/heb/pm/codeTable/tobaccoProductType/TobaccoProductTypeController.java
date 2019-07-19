/*
 * TobaccoProductTypeController
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.codeTable.tobaccoProductType;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.TobaccoProductType;
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
import java.util.List;

/**
 * REST API endpoint for Tobacco Product Group Type.
 *
 * @author vn75469
 * @since 2.16.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + TobaccoProductTypeController.TOBACCO_PRODUCT_TYPE_URL)
@AuthorizedResource(ResourceConstants.TOBACCO_PRODUCT_TYPE_CODE)
public class TobaccoProductTypeController {

	@Autowired
	private UserInfo userInfo;
	@Autowired
	private TobaccoProductTypeService service;

	/**
	 * Logs the client request.
	 */
	private static final Logger logger = LoggerFactory.getLogger(TobaccoProductTypeController.class);

	/**
	 * URL of controller.
	 */
	protected static final String TOBACCO_PRODUCT_TYPE_URL = "/tobaccoProductType";

	/**
	 * Log messages.
	 */
	private static final String GET_ALL_TOBACCO_TYPE_LOG = "User %s from IP %s requested get all Tobacco Product Types";
	private static final String UPDATE_TOBACCO_PRODUCT_LOG = "User %s from IP %s requested update Tobacco Product Types";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";

	/**
	 * Returns a list of tobacco product types.
	 *
	 * @param request the http request.
	 * @return list of tobacco product type.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public List<TobaccoProductType> getTobaccoProductType(HttpServletRequest request) {
		TobaccoProductTypeController.logger.info(String.format(GET_ALL_TOBACCO_TYPE_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr()));
		return this.service.getTobaccoProductTypes();
	}

	/**
	 * Call service to update tax rate of Tobacco Product Type.
	 *
	 * @param tobaccoProductTypes list of Tobacco Product Type to update tax rate.
	 * @param request The HTTP request that initiated this call.
	 * @return list of tobacco product type code.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public ModifiedEntity<List<TobaccoProductType>> updateTobaccoProductType(
			@RequestBody List<TobaccoProductType> tobaccoProductTypes, HttpServletRequest request) {
		//show log message when init method
		TobaccoProductTypeController.logger.info(UPDATE_TOBACCO_PRODUCT_LOG, request.getRemoteAddr());
		//call handle from service.
		this.service.updateTobaccoProductType(tobaccoProductTypes);
		//get new data after update tobacco product type.
		List<TobaccoProductType> tobaccoProductTypesResponse = this.service.getTobaccoProductTypes();
		return new ModifiedEntity<>(tobaccoProductTypesResponse, UPDATE_SUCCESS_MESSAGE);
	}
}
