/*
 * LabelFormatController
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
import com.heb.pm.entity.ScaleLabelFormat;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for scale label format data.
 *
 * @author d116773
 * @since 2.0.8
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ScaleLabelFormatController.LABEL_FORMAT_URL)
@AuthorizedResource(ResourceConstants.SCALE_MAINTENANCE_LABEL_FORMATS)
public class ScaleLabelFormatController {

	private static final Logger logger = LoggerFactory.getLogger(ScaleLabelFormatController.class);

	protected static final String LABEL_FORMAT_URL = "/scaleManagement/labelFormats";
	protected static final String SEARCH_BY_DESCRIPTION_URL = "/description";
	protected static final String SEARCH_BY_CODE_URL = "/code";
	protected static final String SEARCH_FOR_FORMAT_ONE_UPCS_URL = "/upcs/one";
	protected static final String SEARCH_FOR_FORMAT_TWO_UPCS_URL = "/upcs/two";

	private static final String FORMAT_CD_MESSAGE_KEY = "ScaleLabelFormatController.missingFormatCode";
	private static final String DEFAULT_NO_FORMAT_CD_MESSAGE = "Must search for at least one label format code.";
	private static final String FORMAT_CD_DESCRIPTION_MESSAGE_KEY =
			"ScaleLabelFormatController.missingFormatDescription";
	private static final String DEFAULT_NO_FORMAT_CD_DESCRIPTION_MESSAGE =
			"Must search for a label format code description.";

	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Label Format Code: %s added successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY ="ScaleLabelFormatController.addSuccessful";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Label Format Code: %s deleted successfully.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY ="ScaleLabelFormatController.deleteSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Label Format Code: %s updated successfully.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="ScaleLabelFormatController.updateSuccessful";

	// Log messages.
	private static final String FIND_ALL_MESSAGE = "User %s from IP %s requested all label formats";
	private static final String FIND_BY_DESCRIPTION_MESSAGE =
			"User %s from IP %s requested labels with a description of '%s'";
	private static final String FIND_BY_FORMAT_CODE_MESSAGE =
			"User %s from IP %s requested the label formats with the following codes [%s]";
	private static final String FIND_UPCS_BY_FORMAT_CODE_ONE_MESSAGE =
			"User %s from IP %s requested a list of UPCs with the format code one of %d";
	private static final String FIND_UPCS_BY_FORMAT_CODE_TWO_MESSAGE =
			"User %s from IP %s requested a list of UPCs with the format code two of %d";
	private static final String ADD_SCALE_LABEL_FORMAT_CODE_MESSAGE = "User %s from IP %s has requested to add a " +
			"ScaleLabelFormatCode with the description: '%s'.";
	private static final String DELETE_SCALE_FORMAT_CODE_MESSAGE = "User %s from IP %s has requested to delete the " +
			"ScaleLabelFormatCode with the format code: %d.";
	private static final String UPDATE_SCALE_FORMAT_CODE_MESSAGE = "User %s from IP %s has requested to update the " +
			"ScaleLabelFormatCode with the format code: %d to have the description: '%s'.";

	// Defaults
	private static final boolean DEFAULT_INCLUDE_COUNTS = false;
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 10;

	@Autowired
	private ScaleLabelFormatService scaleLabelFormatService;

	@Autowired
	private	UserInfo userInfo;

	@Autowired private
	NonEmptyParameterValidator parameterValidator;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Finds a page of label formats without constraint.
	 *
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of label formats without constraint.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public PageableResult<ScaleLabelFormat> findAll(
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		this.logFindAllRequest(request.getRemoteAddr());

		boolean ic = includeCounts == null ? ScaleLabelFormatController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? ScaleLabelFormatController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ScaleLabelFormatController.DEFAULT_PAGE_SIZE : pageSize;

		PageableResult<ScaleLabelFormat> result = this.scaleLabelFormatService.findAllLabelFormats(ic, pg, ps);
		return result;
	}

	/**
	 * Finds a page of label formats by description.
	 *
	 * @param description The description to search for.
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of label formats.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ScaleLabelFormatController.SEARCH_BY_DESCRIPTION_URL)
	public PageableResult<ScaleLabelFormat> findByDescription(
			@RequestParam(value = "description") String description,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		this.parameterValidator.validate(description, ScaleLabelFormatController.DEFAULT_NO_FORMAT_CD_DESCRIPTION_MESSAGE,
				ScaleLabelFormatController.FORMAT_CD_DESCRIPTION_MESSAGE_KEY, request.getLocale());
		this.logFindByDescription(request.getRemoteAddr(), description);

		boolean ic = includeCounts == null ? ScaleLabelFormatController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? ScaleLabelFormatController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ScaleLabelFormatController.DEFAULT_PAGE_SIZE : pageSize;

		return this.scaleLabelFormatService.findByDescription(description, ic, pg, ps);
	}

	/**
	 * Finds a page of label formats by format code.
	 *
	 * @param formatCodes A list of codes to search for.
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of label formats.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ScaleLabelFormatController.SEARCH_BY_CODE_URL)
	public PageableResult<ScaleLabelFormat> findByFormatCodes(
			@RequestParam(value = "formatCode") List<Long> formatCodes,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		this.parameterValidator.validate(formatCodes, ScaleLabelFormatController.DEFAULT_NO_FORMAT_CD_MESSAGE,
				ScaleLabelFormatController.FORMAT_CD_MESSAGE_KEY, request.getLocale());
		this.logFindByFormatCodes(request.getRemoteAddr(), formatCodes);

		boolean ic = includeCounts == null ? ScaleLabelFormatController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? ScaleLabelFormatController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ScaleLabelFormatController.DEFAULT_PAGE_SIZE : pageSize;

		return this.scaleLabelFormatService.findByFormatCode(formatCodes, ic, pg, ps);
	}

	/**
	 * Returns a list of ScaleUpcs that match a particular label format in its label format one column.
	 *
	 * @param formatCode The format code to search for.
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of ScaleUpcs.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value= ScaleLabelFormatController.SEARCH_FOR_FORMAT_ONE_UPCS_URL)
	public PageableResult<ScaleUpc> findUpcsByFormatCodeOne(
			@RequestParam(value = "formatCode") Long formatCode,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		this.parameterValidator.validate(formatCode, "", "", request.getLocale());
		this.logFindUpcByFormatCodeOne(request.getRemoteAddr(), formatCode);

		boolean ic = includeCounts == null ? ScaleLabelFormatController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? ScaleLabelFormatController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ScaleLabelFormatController.DEFAULT_PAGE_SIZE : pageSize;

		return this.scaleLabelFormatService.findUpcsByFormatCodeOne(formatCode, ic, pg, ps);
	}

	/**
	 * Returns a list of ScaleUpcs that match a particular label format in its label format two column.
	 *
	 * @param formatCode The format code to search for.
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of ScaleUpcs.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value= ScaleLabelFormatController.SEARCH_FOR_FORMAT_TWO_UPCS_URL)
	public PageableResult<ScaleUpc> findUpcsByFormatCodeTwo(
			@RequestParam(value = "formatCode") Long formatCode,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		this.parameterValidator.validate(formatCode, "", "", request.getLocale());
		this.logFindUpcByFormatCodeTwo(request.getRemoteAddr(), formatCode);

		boolean ic = includeCounts == null ? ScaleLabelFormatController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? ScaleLabelFormatController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ScaleLabelFormatController.DEFAULT_PAGE_SIZE : pageSize;

		return this.scaleLabelFormatService.findUpcsByFormatCodeTwo(formatCode, ic, pg, ps);
	}

	/**
	 * Updates a ScaleLabelFormatCode's description.
	 *
	 * @param scaleLabelFormatCode The scaleLabelFormatCode to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The updated ScaleLabelFormatCode and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "updateScaleLabelFormatCode")
	public ModifiedEntity<ScaleLabelFormat> updateScaleLabelFormat(@RequestBody ScaleLabelFormat scaleLabelFormatCode,
																 HttpServletRequest request){
		this.logUpdate(request.getRemoteAddr(), scaleLabelFormatCode);
		ScaleLabelFormat updatedScaleLabelFormatCode =  this.scaleLabelFormatService.update(scaleLabelFormatCode);
		String updateMessage = this.messageSource.getMessage(ScaleLabelFormatController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleLabelFormatCode.getFormatCode()}, ScaleLabelFormatController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(updatedScaleLabelFormatCode, updateMessage);
	}

	/**
	 * Adds a new ScaleLabelFormatCode.
	 *
	 * @param request the HTTP request that initiated this call.
	 * @return The new ScaleLabelFormatCode and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "addScaleLabelFormatCode")
	public ModifiedEntity<ScaleLabelFormat> addScaleLabelFormat(@RequestBody ScaleLabelFormat labelFormat,
															  HttpServletRequest request){
		this.logAdd(request.getRemoteAddr(), labelFormat.getDescription());
		ScaleLabelFormat scaleLabelFormatCode =  this.scaleLabelFormatService.add(labelFormat.getFormatCode(), labelFormat.getDescription());
		String updateMessage = this.messageSource.getMessage(ScaleLabelFormatController.ADD_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleLabelFormatCode.getFormatCode()}, ScaleLabelFormatController.DEFAULT_ADD_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(scaleLabelFormatCode, updateMessage);
	}

	/**
	 * Deletes a ScaleLabelFormatCode.
	 *
	 * @param formatCode The ScaleLabelFormatCode formatCode to be deleted.
	 * @param request the HTTP request that initiated this call.
	 * @return The deleted ScaleLabelFormatCode and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE, value = "deleteScaleLabelFormatCode")
	public ModifiedEntity<Long> deleteScaleLabelFormatCode(@RequestParam Long formatCode,
													  HttpServletRequest request){
		this.logDelete(request.getRemoteAddr(), formatCode);
		this.scaleLabelFormatService.delete(formatCode);
		String updateMessage = this.messageSource.getMessage(ScaleLabelFormatController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{formatCode}, ScaleLabelFormatController.DEFAULT_DELETE_SUCCESS_MESSAGE, request.getLocale());

		return new ModifiedEntity<>(formatCode, updateMessage);
	}

	/**
	 * Returns the Hits result with found and not found format codes.
	 *
	 * @param formatCodes The format code to search on.
	 * @param request the HTTP request that initiated this call.
	 * @return Hits result with found and not found format codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "hits")
	public Hits findHitsByFormatCode(@RequestParam("formatCodes") List<Long> formatCodes, HttpServletRequest request){
		this.parameterValidator.validate(formatCodes, ScaleLabelFormatController.DEFAULT_NO_FORMAT_CD_MESSAGE,
				ScaleLabelFormatController.FORMAT_CD_MESSAGE_KEY, request.getLocale());
		this.logFindByFormatCodes(request.getRemoteAddr(), formatCodes);
		return this.scaleLabelFormatService.findHitsByLabelFormatCodeList(formatCodes);
	}

	/**
	 * Logs a search of ScaleUPcs by format code one.
	 *
	 * @param ipAddress The IP address of the logged in user.
	 * @param formatCode The format code they searched for.
	 */
	private void logFindUpcByFormatCodeOne(String ipAddress, Long formatCode) {
		ScaleLabelFormatController.logger.info(String.format(ScaleLabelFormatController.FIND_UPCS_BY_FORMAT_CODE_ONE_MESSAGE,
				this.userInfo.getUserId(), ipAddress,formatCode));
	}

	/**
	 * Logs a search of ScaleUPcs by format code two.
	 *
	 * @param ipAddress The IP address of the logged in user.
	 * @param formatCode The format code they searched for.
	 */
	private void logFindUpcByFormatCodeTwo(String ipAddress, Long formatCode) {
		ScaleLabelFormatController.logger.info(String.format(ScaleLabelFormatController.FIND_UPCS_BY_FORMAT_CODE_TWO_MESSAGE,
				this.userInfo.getUserId(), ipAddress,formatCode));
	}

	/**
	 * Logs a search of label formats by code.
	 *
	 * @param ipAddress The IP address of the logged in user.
	 * @param formatCodes The format codes they searched for.
	 */
	private void logFindByFormatCodes(String ipAddress, List<Long> formatCodes) {
		ScaleLabelFormatController.logger.info(String.format(ScaleLabelFormatController.FIND_BY_FORMAT_CODE_MESSAGE,
				this.userInfo.getUserId(), ipAddress, ListFormatter.formatAsString(formatCodes)));
	}

	/**
	 * Logs a users request for all label formats.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logFindAllRequest(String ipAddress) {
		ScaleLabelFormatController.logger.info(String.format(ScaleLabelFormatController.FIND_ALL_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}

	/**
	 * Logs a users request for all label formats.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @param description The description the user is looking for.
	 */
	private void logFindByDescription(String ipAddress, String description) {
		ScaleLabelFormatController.logger.info(String.format(ScaleLabelFormatController.FIND_BY_DESCRIPTION_MESSAGE,
				this.userInfo.getUserId(), ipAddress, description));
	}

	/**
	 * Logs a user's request to update a scale label format code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param scaleLabelFormatCode The scale format code to be updated.
	 */
	private void logUpdate(String ip, ScaleLabelFormat scaleLabelFormatCode){
		ScaleLabelFormatController.logger.info(String.format(ScaleLabelFormatController.UPDATE_SCALE_FORMAT_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, scaleLabelFormatCode.getFormatCode(), scaleLabelFormatCode.getDescription()));
	}

	/**
	 * Logs a user's request to add a scale label format code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param description The description scale format code to be added.
	 */
	private void logAdd(String ip, String description){
		ScaleLabelFormatController.logger.info(String.format(ScaleLabelFormatController.ADD_SCALE_LABEL_FORMAT_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, description));
	}

	/**
	 * Logs a user's request to delete a scale format code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param formatCode The scale format code to be deleted.
	 */
	private void logDelete(String ip, Long formatCode){
		ScaleLabelFormatController.logger.info(String.format(ScaleLabelFormatController.DELETE_SCALE_FORMAT_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, formatCode));
	}
}
