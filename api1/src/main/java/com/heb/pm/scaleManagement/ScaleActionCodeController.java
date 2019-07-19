/*
 *  ScaleActionCodeController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.Hits;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ScaleActionCode;
import com.heb.pm.entity.ScaleUpc;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller that writes all information related to the scale action codes.
 *
 * @author s573181
 * @since 2.0.8
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ScaleActionCodeController.SCALE_ACTION_CODE_URL)
@AuthorizedResource(ResourceConstants.SCALE_MAINTENANCE_ACTION_CODES)
public class ScaleActionCodeController {

	private static final Logger logger = LoggerFactory.getLogger(ScaleActionCodeController.class);

	protected static final String SCALE_ACTION_CODE_URL = "/scaleManagement";


	// Keys to user facing messages in the message resource bundle.
	private static final String ACTION_CD_MESSAGE_KEY = "ScaleActionCodeController.missingActionCode";
	private static final String DEFAULT_NO_ACTION_CD_MESSAGE = "Must search for at least one action code.";
	private static final String ACTION_CD_DESCRIPTION_MESSAGE_KEY =
			"ScaleActionCodeController.missingActionCodeDescription";
	private static final String DEFAULT_NO_ACTION_CD_DESCRIPTION_MESSAGE =
			"Must search for an action code description.";
	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Action Code: %s added successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY ="ScaleActionCodeController.addSuccessful";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Action Code: %s deleted successfully.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY ="ScaleActionCodeController.deleteSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Action Code: %s updated successfully.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="ScaleActionCodeController.updateSuccessful";

	//log messages
	private static final String FIND_BY_ACTION_CODE_MESSAGE =
			"User %s from IP %s has requested action code data for the following action codes [%s]";
	private static final String FIND_All_ACTION_CODE_MESSAGE =
			"User %s from IP %s has requested all action code data";
	private static final String FIND_BY_ACTION_CODE_DESCRIPTION_MESSAGE =
			"User %s from IP %s has requested action code data containing the wildcard description: %s";
	private static final String FIND_PLUS_BY_ACTION_CODE_MESSAGE =
			"User %s from IP %s has requested PLU data for the following action code %s";
	private static final String ADD_SCALE_ACTION_CODE_MESSAGE = "User %s from IP %s has requested to add a " +
			"ScaleActionCode with the description: '%s'.";
	private static final String DELETE_SCALE_ACTION_CODE_MESSAGE = "User %s from IP %s has requested to delete the " +
			"ScaleActionCode with the action code: %d.";
	private static final String UPDATE_SCALE_ACTION_CODE_MESSAGE = "User %s from IP %s has requested to update the " +
			"ScaleActionCode with the action code: %d to have the description: '%s'.";

	@Autowired
	private ScaleActionCodeService service;

	@Autowired private
	UserInfo userInfo;

	@Autowired private
	NonEmptyParameterValidator parameterValidator;

	@Autowired
	private MessageSource messageSource;

	private static final int DEFAULT_PAGE = 0;


	/**
	 * Returns a list of ScaleActionCodes from a list of action codes.
	 *
	 * @param actionCodes a list of action codes to search on.
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param request the HTTP request that initiated this call.
	 * @return a list of ScaleActionCodes from a list of action codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "actionCodes")
	public PageableResult<ScaleActionCode> findAllByActionCodes(
			@RequestParam("actionCodesList") List<Long> actionCodes,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,HttpServletRequest request){

		this.parameterValidator.validate(actionCodes, ScaleActionCodeController.DEFAULT_NO_ACTION_CD_MESSAGE,
				ScaleActionCodeController.ACTION_CD_MESSAGE_KEY, request.getLocale());
		this.logFindByActionCodes(request.getRemoteAddr(), actionCodes);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleActionCodeController.DEFAULT_PAGE : page;

		return this.service.findByActionCodes(actionCodes, ic, pg);
	}

	/**
	 * Returns a list of ScaleActionCodes from an action code description.
	 *
	 * @param description the action code's description to search on.
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param request the HTTP request that initiated this call.
	 * @return a list of ScaleActionCodes from a list of action codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "actionCodeDescription")
	public PageableResult<ScaleActionCode> findAllByActionCodeDescription(
			@RequestParam("description") String description,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page, HttpServletRequest request){

		this.parameterValidator.validate(description,
				ScaleActionCodeController.DEFAULT_NO_ACTION_CD_DESCRIPTION_MESSAGE,
				ScaleActionCodeController.ACTION_CD_DESCRIPTION_MESSAGE_KEY, request.getLocale());
		this.logFindByActionCodeDescription(request.getRemoteAddr(), description);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleActionCodeController.DEFAULT_PAGE : page;

		return this.service.findByActionCodeDescription(description, ic, pg);
	}
	/**
	 * Returns a list of ScaleActionCodes from all action codes.
	 *
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param request the HTTP request that initiated this call.
	 * @return a list of all ScaleActionCodes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "queryAllActionCodes")
	public PageableResult<ScaleActionCode> findAllActionCodes(
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page, HttpServletRequest request){


		this.logFindAllActionCodes(request.getRemoteAddr());
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleActionCodeController.DEFAULT_PAGE : page;

		return this.service.findAll(ic, pg);
	}

	/**
	 * Returns the Scale Upc information by action cd.
	 *
	 * @param actionCode The action code to search on.
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param request the HTTP request that initiated this call.
	 * @return a list of ScaleUpcs from a list of action codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "pluListByActionCode")
	public PageableResult<ScaleUpc> findAllPlusByActionCode(
			@RequestParam("actionCode") Long actionCode,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,HttpServletRequest request){

		this.parameterValidator.validate(actionCode, ScaleActionCodeController.DEFAULT_NO_ACTION_CD_MESSAGE,
				ScaleActionCodeController.ACTION_CD_MESSAGE_KEY, request.getLocale());
		this.logPlusFindByActionCodes(request.getRemoteAddr(), actionCode);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleActionCodeController.DEFAULT_PAGE : page;
		return this.service.findPlusByActionCode(actionCode, ic, pg);
	}

	/**
	 * Updates a ScaleActionCode's description.
	 *
	 * @param scaleActionCode The scaleActionCode to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The updated ScaleActionCode and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "updateScaleActionCode")
	public ModifiedEntity<ScaleActionCode> updateScaleActionCode(@RequestBody ScaleActionCode scaleActionCode,
																 HttpServletRequest request){
		this.logUpdate(request.getRemoteAddr(), scaleActionCode);
		ScaleActionCode updatedScaleActionCode =  this.service.update(scaleActionCode);
		String updateMessage = this.messageSource.getMessage(ScaleActionCodeController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleActionCode.getActionCode()}, ScaleActionCodeController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(updatedScaleActionCode, updateMessage);
	}

	/**
	 * Adds a new ScaleActionCode.
	 *
	 * @param request the HTTP request that initiated this call.
	 * @return The new ScaleActionCode and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "addScaleActionCode")
	public ModifiedEntity<ScaleActionCode> addScaleActionCode(@RequestBody ScaleActionCode actionCode,
															  HttpServletRequest request){
		this.logAdd(request.getRemoteAddr(), actionCode.getDescription());
		ScaleActionCode scaleActionCode =  this.service.add(actionCode.getActionCode(), actionCode.getDescription());
		String updateMessage = this.messageSource.getMessage(ScaleActionCodeController.ADD_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleActionCode.getActionCode()}, ScaleActionCodeController.DEFAULT_ADD_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(scaleActionCode, updateMessage);
	}

	/**
	 * Deletes a ScaleActionCode.
	 *
	 * @param actionCode The ScaleActionCode actionCode to be deleted.
	 * @param request the HTTP request that initiated this call.
	 * @return The deleted ScaleActionCode and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE, value = "deleteScaleActionCode")
	public ModifiedEntity<Long> deleteScaleActionCode(@RequestParam Long actionCode,
																 HttpServletRequest request){
		this.logDelete(request.getRemoteAddr(), actionCode);
		this.service.delete(actionCode);
		String updateMessage = this.messageSource.getMessage(ScaleActionCodeController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{actionCode}, ScaleActionCodeController.DEFAULT_DELETE_SUCCESS_MESSAGE, request.getLocale());

		return new ModifiedEntity<>(actionCode, updateMessage);
	}

	/**
	 * Returns the Hits result with found and not found action codes.
	 *
	 * @param actionCodes The action code to search on.
	 * @param request the HTTP request that initiated this call.
	 * @return Hits result with found and not found action codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "hits/actionCodes")
	public Hits findHitsByActionCode(@RequestParam("actionCodes") List<Long> actionCodes, HttpServletRequest request){
		this.parameterValidator.validate(actionCodes, ScaleActionCodeController.DEFAULT_NO_ACTION_CD_MESSAGE,
				ScaleActionCodeController.ACTION_CD_MESSAGE_KEY, request.getLocale());
		this.logFindByActionCodes(request.getRemoteAddr(), actionCodes);
		return this.service.findHitsByActionCodeList(actionCodes);
	}

	/**
	 * Logs a user's request to get all records for multiple action codes.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param actionCodes The action codes list the user is searching for.
	 */
	private void logFindByActionCodes(String ip, List<Long> actionCodes) {
		ScaleActionCodeController.logger.info(
				String.format(ScaleActionCodeController.FIND_BY_ACTION_CODE_MESSAGE,
						this.userInfo.getUserId(), ip, ListFormatter.formatAsString(actionCodes)));
	}

	/**
	 * Logs a user's request to get all plus for an action codes.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param actionCode The action code  the user is searching for.
	 */
	private void logPlusFindByActionCodes(String ip, Long actionCode) {
		ScaleActionCodeController.logger.info(
				String.format(ScaleActionCodeController.FIND_PLUS_BY_ACTION_CODE_MESSAGE,
						this.userInfo.getUserId(), ip, actionCode));
	}

	/**
	 * Logs a user's request to get all records for multiple action codes.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param description The action codes description the user is searching for.
	 */
	private void logFindByActionCodeDescription(String ip, String description) {
		ScaleActionCodeController.logger.info(
				String.format(ScaleActionCodeController.FIND_BY_ACTION_CODE_DESCRIPTION_MESSAGE,
						this.userInfo.getUserId(), ip, description));
	}

	/**
	 * Logs a user's request to get all action code records.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logFindAllActionCodes(String ip) {
		ScaleActionCodeController.logger.info(
				String.format(ScaleActionCodeController.FIND_All_ACTION_CODE_MESSAGE,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs a user's request to update a scale action code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param scaleActionCode The scale action code to be updated.
	 */
	private void logUpdate(String ip, ScaleActionCode scaleActionCode){
		ScaleActionCodeController.logger.info(String.format(ScaleActionCodeController.UPDATE_SCALE_ACTION_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, scaleActionCode.getActionCode(), scaleActionCode.getDescription()));
	}

	/**
	 * Logs a user's request to add a scale action code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param description The description scale action code to be added.
	 */
	private void logAdd(String ip, String description){
		ScaleActionCodeController.logger.info(String.format(ScaleActionCodeController.ADD_SCALE_ACTION_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, description));
	}

	/**
	 * Logs a user's request to delete a scale action code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param actionCode The scale action code to be deleted.
	 */
	private void logDelete(String ip, Long actionCode){
		ScaleActionCodeController.logger.info(String.format(ScaleActionCodeController.DELETE_SCALE_ACTION_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, actionCode));
	}

}
