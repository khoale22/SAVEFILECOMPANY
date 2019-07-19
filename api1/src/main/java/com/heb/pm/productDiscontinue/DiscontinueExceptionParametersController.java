/*
 *
 *  DiscontinueExceptionController
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
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductDiscontinueExceptionType;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST endpoint for functions related to viewing and modifying exceptions to the default product discontinue rules.
 *
 * @author s573181
 * @since 2.0.2
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		DiscontinueExceptionParametersController.PRODUCT_DISCONTINUE_EXCEPTION_RULES_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_DISCONTINUE_EXCEPTION_PARAMETERS)
public class DiscontinueExceptionParametersController {

	private static final Logger logger = LoggerFactory.getLogger(DiscontinueExceptionParametersController.class);

	protected static final String PRODUCT_DISCONTINUE_EXCEPTION_RULES_URL = "/productDiscontinue/exceptions";

	// Log Messages
	private static final String GET_TYPES_MESSAGE =
			"User %s from IP %s has requested a list of product discontinue rule exception types.";
	private static final String FIND_EXCEPTIONS_BY_TYPE_MESSAGE =
			"User %s from IP %s has requested product discontinue exception rules info for %s.";
	private static final String FIND_ALL_EXCEPTIONS_MESSAGE =
			"User %s from IP %s has requested all product discontinue exception rules.";
	private static final String UPDATE_EXCEPTION_MESSAGE =
			"User %s from IP %s has requested to update discontinue exception rule: %s";
	private static final String ADD_EXCEPTION_MESSAGE =
			"User %s from IP %s has requested to add discontinue exception rule: %s";
	private static final String DELETE_EXCEPTION_MESSAGE =
			"User %s from IP %s has requested to delete discontinue exception rule with the ID %d";

	// Constants related to messages for the front end.
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Update successful";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY =
			"DiscontinueExceptionParametersController.updateSuccessful";
	private static final String DEFAULT_ADD_SUCCESS_MESSAGE = "Add successful";
	private static final String UPDATE_ADD_MESSAGE_KEY =
			"DiscontinueExceptionParametersController.addSuccessful";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE = "Delete successful";
	private static final String DELETE_SUCCESS_MESSAGE_KEY =
			"DiscontinueExceptionParametersController.deleteSuccessful";
	private static final String MISSING_EXCEPTION_ID_DEFAULT_MESSAGE = "Exception ID cannot be null";
	private static final String MISSING_EXCEPTION_ID_MESSAGE_KEY =
			"DiscontinueExceptionParametersController.missingExceptionId";

	@Autowired
	private DiscontinueExceptionParametersService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Returns a list of exception types.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return A list of exception types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "types")
	public List<String> getExceptionTypes(HttpServletRequest request) {

		this.logGetExceptionTypes(request.getRemoteAddr());

		return ProductDiscontinueExceptionType.allTypes.stream().map(
				ProductDiscontinueExceptionType::getType).collect(Collectors.toList());
	}

	/**
	 * Returns a list of exception rules.
	 *
	 * @param exceptionType An optional parameter that can be used to filter the returned rules by type.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of exception rules.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public List<DiscontinueRules> findExceptionRules (
			@RequestParam(value = "exceptionType", required = false) ProductDiscontinueExceptionType exceptionType,
			HttpServletRequest request) {

		// If the type is null or equals ALL, then return all the exceptions.
		if (exceptionType == null || exceptionType.equals(ProductDiscontinueExceptionType.ALL)) {
			this.logFindAll(request.getRemoteAddr());
			return this.service.findAll();
		}

		// Otherwise, call the function that returns a filtered list.
		this.logFindByType(request.getRemoteAddr(), exceptionType);
		return this.service.findByExceptionType(exceptionType);
	}


	/**
	 * Updates a set of product discontinue exception rules.
	 *
	 * @param updatedRules The updated rules.
	 * @param request The HTTP request that initiated this call.
	 * @return The updated rules and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT)
	public ModifiedEntity<DiscontinueRules> updateRule(@RequestBody DiscontinueRules updatedRules,
													   HttpServletRequest request){

		this.logUpdate(request.getRemoteAddr(), updatedRules);
		DiscontinueRules rulesPostSave = this.service.update(updatedRules);

		String updateMessage = this.messageSource.getMessage(
				DiscontinueExceptionParametersController.UPDATE_SUCCESS_MESSAGE_KEY,
				null, DiscontinueExceptionParametersController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(rulesPostSave, updateMessage);
	}

	/**
	 * Saves a new set of product discontinue exception rules.
	 *
	 * @param newRules The new rules.
	 * @param request The HTTP request that initiated this call
	 * @return The new rules and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public ModifiedEntity<DiscontinueRules> addRule(
			@RequestBody DiscontinueRules newRules, HttpServletRequest request){

		this.logAdd(request.getRemoteAddr(), newRules);
		DiscontinueRules rulesPostSave = this.service.add(newRules);

		String updateMessage = this.messageSource.getMessage(
				DiscontinueExceptionParametersController.UPDATE_ADD_MESSAGE_KEY,
				null, DiscontinueExceptionParametersController.DEFAULT_ADD_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(rulesPostSave, updateMessage);
	}

	/**
	 * Deletes set of product discontinue exception rules.
	 *
	 * @param exceptionNumber The ID of the rules to delete.
	 * @param request The HTTP request that initiated this call
	 * @return This returns a modified entry with the rules that were deleted.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE)
	public ModifiedEntity<Integer> deleteRule(
			@RequestParam("exceptionNumber") Integer exceptionNumber,
			HttpServletRequest request){

		this.parameterValidator.validate(exceptionNumber,
				DiscontinueExceptionParametersController.MISSING_EXCEPTION_ID_MESSAGE_KEY,
				DiscontinueExceptionParametersController.MISSING_EXCEPTION_ID_DEFAULT_MESSAGE, request.getLocale());

		this.logDelete(request.getRemoteAddr(), exceptionNumber);

		this.service.delete(exceptionNumber);

		String updateMessage = this.messageSource.getMessage(
				DiscontinueExceptionParametersController.DELETE_SUCCESS_MESSAGE_KEY,
				null, DiscontinueExceptionParametersController.DEFAULT_DELETE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(exceptionNumber, updateMessage);
	}

	/**
	 * Sets the DiscontinueExceptionParametersService for this object to use. This is for testing.
	 *
	 * @param service The DiscontinueExceptionParametersService for this object to use
	 */
	public void setService(DiscontinueExceptionParametersService service) {
		this.service = service;
	}

	/**
	 * Sets the UserInfo for this object to use. This is for testing.
	 *
	 * @param userInfo The UserInfo for this object to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the NonEmptyParameterValidator for this object to use. This is for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this object to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Sets the MessageSource for this object to use. This is for testing.
	 *
	 * @param messageSource The MessageSource for this object to use.
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Logs a user's request for all exception types.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logGetExceptionTypes(String ip) {

		DiscontinueExceptionParametersController.logger.info(
				String.format(DiscontinueExceptionParametersController.GET_TYPES_MESSAGE,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs a user's request for all discontinue exception rules.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindAll(String ip) {

		DiscontinueExceptionParametersController.logger.info(
				String.format(DiscontinueExceptionParametersController.FIND_ALL_EXCEPTIONS_MESSAGE,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs a user's request for  discontinue exception rules filtered by type.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param type The type they are filtering on.
	 */
	private void logFindByType(String ip, ProductDiscontinueExceptionType type) {

		DiscontinueExceptionParametersController.logger.info(
				String.format(DiscontinueExceptionParametersController.FIND_EXCEPTIONS_BY_TYPE_MESSAGE,
						this.userInfo.getUserId(), ip, type));
	}


	/**
	 * Logs a user's request to update a set of product discontinue exception rules.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param discontinueRules The updated set of rules.
	 */
	private void logUpdate(String ip, DiscontinueRules discontinueRules) {

		DiscontinueExceptionParametersController.logger.info(
				String.format(DiscontinueExceptionParametersController.UPDATE_EXCEPTION_MESSAGE,
						this.userInfo.getUserId(), ip, discontinueRules));
	}

	/**
	 * Logs a user's request to add a set of product discontinue exception rules.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param discontinueRules The new set of rules.
	 */
	private void logAdd(String ip, DiscontinueRules discontinueRules) {

		DiscontinueExceptionParametersController.logger.info(
				String.format(DiscontinueExceptionParametersController.ADD_EXCEPTION_MESSAGE,
						this.userInfo.getUserId(), ip, discontinueRules));
	}

	/**
	 * Logs a user's request to delete a set of product discontinue exception rules.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param exceptionSequenceNumber The ID of the rule to delete.
	 */
	private void logDelete(String ip, int exceptionSequenceNumber) {

		DiscontinueExceptionParametersController.logger.info(
				String.format(DiscontinueExceptionParametersController.DELETE_EXCEPTION_MESSAGE,
						this.userInfo.getUserId(), ip, exceptionSequenceNumber));
	}
}
