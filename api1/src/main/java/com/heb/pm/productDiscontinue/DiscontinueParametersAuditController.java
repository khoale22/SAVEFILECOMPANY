/*
 *
 *  DiscontinueExceptionParametersAuditController
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.productDiscontinue;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for functions related to viewing audit parameters and their exceptions to the default product discontinue rules.
 * @author s573181
 * @since 2.0.3
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		DiscontinueParametersAuditController.PRODUCT_DISCONTINUE_EXCEPTION_AUDIT_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_DISCONTINUE_PARAMETERS_AUDIT)
public class DiscontinueParametersAuditController {

	private static final Logger logger = LoggerFactory.getLogger(DiscontinueParametersAuditController.class);

	protected static final String PRODUCT_DISCONTINUE_EXCEPTION_AUDIT_URL = "/productDiscontinue";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private DiscontinueParametersAuditService discontinueParametersAuditService;

	// Log Messages.
	private static final String GET_EXCEPTION_PARAMETER_AUDITS =
			"User %s from IP %s has requested a list of product discontinue exception parameters audits for the %s with ID %s.";
	private static final String GET_PARAMETER_AUDITS =
			"User %s from IP %s has requested a list of product discontinue parameters audits.";
	private static final String GET_DELETED_PARAMETER_AUDITS =
			"User %s from IP %s has requested a list of deleted product discontinue exception parameters audits.";

	// Constants related to messages for the front end.
	private static final String MISSING_EXCEPTION_TYPE_DEFAULT_MESSAGE = "Exception Type cannot be null.";
	private static final String MISSING_EXCEPTION_TYPE_MESSAGE_KEY =
			"DiscontinueParametersAuditController.missingExceptionType";
	private static final String MISSING_EXCEPTION_TYPE_ID_DEFAULT_MESSAGE = "Exception Type Id cannot be null.";
	private static final String MISSING_EXCEPTION_TYPE_ID_MESSAGE_KEY =
			"DiscontinueParametersAuditController.missingExceptionTypeId";

	/**
	 * Returns a list of DiscontinueParametersAuditRecords that contain Exception Parameter Audits of Exception type
	 * and its corresponding Id (such as UPC 4011).
	 *
	 * @param exceptionType
	 * @param exceptionTypeId
	 * @param request The HTTP request that initiated this call.
	 * @return a list of DiscontinueParametersAuditRecords.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "/exceptionAudits")
	public List<DiscontinueParametersAuditRecord>  getExceptionParameterAudits(@RequestParam(value = "exceptionType") String exceptionType,
																			   @RequestParam(value = "exceptionTypeId") String exceptionTypeId,
																			   HttpServletRequest request){
		this.parameterValidator.validate(exceptionType, DiscontinueParametersAuditController.MISSING_EXCEPTION_TYPE_MESSAGE_KEY,
				DiscontinueParametersAuditController.MISSING_EXCEPTION_TYPE_DEFAULT_MESSAGE, request.getLocale());
		this.parameterValidator.validate(exceptionTypeId, DiscontinueParametersAuditController.MISSING_EXCEPTION_TYPE_ID_MESSAGE_KEY,
				DiscontinueParametersAuditController.MISSING_EXCEPTION_TYPE_ID_DEFAULT_MESSAGE, request.getLocale());
		this.logGetExceptionParameterAudits(request.getRemoteAddr(), exceptionType, exceptionTypeId);
		return discontinueParametersAuditService.findByExceptionTypeAndExceptionTypeId(exceptionType, exceptionTypeId);
	}

	/**
	 * Returns of a list of DiscontinueParametersAuditRecords that contain Parameter Audits.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return a list of DiscontinueParametersAuditRecords.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "/parametersAudit")
	public List<DiscontinueParametersAuditRecord>  getParameterAudits(HttpServletRequest request){
		this.logGetParameterAudits(request.getRemoteAddr());
		return discontinueParametersAuditService.findAllAuditParameters();
	}

	/**
	 * Returns of a list of DiscontinueRules that contain deleted Parameter Exceptions.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return a list of DiscontinueParametersAuditRecords.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "/deletedParametersAuditTypes")
	public List<DiscontinueRules>  getDeletedParameterAudits(HttpServletRequest request){
		this.logGetDeletedParameterAudits(request.getRemoteAddr());
		return discontinueParametersAuditService.findAllDeletedExceptionParametersAudit();
	}

	/**
	 * Logs a user's request to get a set of Discontinue Exception Parameters Audits.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param exceptionType The ExceptionType to delete.
	 * @param exceptionTypeId The ID of the ExceptionType to delete.
	 */
	private void logGetExceptionParameterAudits(String ip, String exceptionType, String exceptionTypeId){

		DiscontinueParametersAuditController.logger.info(String.format(DiscontinueParametersAuditController.
				GET_EXCEPTION_PARAMETER_AUDITS, this.userInfo.getUserId(), ip, exceptionType, exceptionTypeId));
	}

	/**
	 * Logs a user's request for all discontinue parameter audits.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logGetParameterAudits(String ip){

		DiscontinueParametersAuditController.logger.info(String.format(DiscontinueParametersAuditController.
				GET_PARAMETER_AUDITS, this.userInfo, ip));
	}

	/**
	 * Logs a user's request for all discontinue exception parameter audits.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logGetDeletedParameterAudits(String ip){

		DiscontinueParametersAuditController.logger.info(String.format(DiscontinueParametersAuditController.
				GET_DELETED_PARAMETER_AUDITS, this.userInfo, ip));
	}
}
