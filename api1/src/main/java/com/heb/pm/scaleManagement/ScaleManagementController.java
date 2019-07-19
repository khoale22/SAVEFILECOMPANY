/*
 * UpcPluMaintenanceController
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
import com.heb.pm.entity.ScaleUpc;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.ListFormatter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author m314029
 * @since 2.2.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ScaleManagementController.SCALE_MANAGEMENT_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_UPC_MAINTENANCE)
public class ScaleManagementController {

	private static final Logger logger = LoggerFactory.getLogger(ScaleManagementController.class);

	protected static final String SCALE_MANAGEMENT_URL = "/scaleManagement";

	// Keys to user facing messages in the message resource bundle.
	private static final String PLU_MESSAGE_KEY = "UpcPluMaintenanceController.missingPlus";
	private static final String DEFAULT_NO_PLU_MESSAGE = "Must search for at least one PLU.";
	private static final String DESCRIPTION_MESSAGE_KEY = "UpcPluMaintenanceController.missingDescription";
	private static final String DEFAULT_NO_DESCRIPTION_MESSAGE = "Must search for a description.";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Scale Plu: %d updated successfully.";
	private static final String DEFAULT_BULK_UPDATE_SUCCESS_MESSAGE ="Scale Plu Bulk updated successfully.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="ScaleManagementController.updateSuccessful";
	private static final String BULK_UPDATE_SUCCESS_MESSAGE_KEY ="ScaleManagementController.bulkUpdateSuccessful";

	//log messages
	private static final String FIND_BY_PLU_MESSAGE =
			"User %s from IP %s has requested scale scan data for the following PLUs [%s]";
	private static final String FIND_BY_DESCRIPTION_MESSAGE =
			"User %s from IP %s has requested scale scan data containing the wildcard description: %s";
	private static final String UPDATE_SCALE_UPC_MESSAGE = "User %s from IP %s has requested to update the " +
			"ScaleUpc with a the following upc: %d and the following plu: %d.";
	private static final String UPDATE_BULK_SCALE_UPCS_MESSAGE = "User %s from IP %s has requested to update a list of " +
			"ScaleUpcs with the bulk update values: %s";
	private static final String PLU_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export scale maintenance data to excel for the following PLUs [%s]";
	private static final String DESCRIPTION_EXPORT_TO_EXCEL_MESSAGE =
			"User %s from IP %s has requested to export scale maintenance data to excel with the following description: [%s]";
	private static final String EXPORT_CODE_TYPE_LOG_MESSAGE =
			"User %s from IP %s requested an %s report for code %s";
	private static final String MAINTENANCE_SCALE_UPC_MESSAGE = "User %s from IP %s has requested to maintenance the " +
			"ScaleUpc with a the following upc: %d and the following plu: %d.";

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 100;

	@Autowired
	private ScaleManagementService service;

	@Autowired private
	UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired private
	NonEmptyParameterValidator parameterValidator;

	private LazyObjectResolver<ScaleUpc> objectResolver = new ScaleUpcResolver();

	/**
	 * Returns a list of scale upc data for a list of plus.
	 *
	 * @param pluList The plus to look for data about.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of scale upc data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "pluList")
	public PageableResult<ScaleUpc> findAllByPlus(
			@RequestParam("pluList")List<Long> pluList,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,HttpServletRequest request){

		this.parameterValidator.validate(pluList, ScaleManagementController.DEFAULT_NO_PLU_MESSAGE,
				ScaleManagementController.PLU_MESSAGE_KEY, request.getLocale());
		this.logFindByPluList(request.getRemoteAddr(), pluList);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleManagementController.DEFAULT_PAGE : page;

		return this.resolveResults(this.service.findByPlu(pluList, ic, pg));
	}

	/**
	 * Loads the objects into a scale upc that were JPA lazy loaded.
	 *
	 * @param results List of Scale Upcs.
	 * @return A list of pageable results of scale upcs that were lazy loaded.
	 */
	private PageableResult<ScaleUpc> resolveResults(PageableResult<ScaleUpc> results) {
		results.getData().forEach(this.objectResolver::fetch);
		return results;
	}

	/**
	 * Loads the objects into a scale upc that were JPA lazy loaded.
	 *
	 * @param results List of Scale Upcs.
	 * @return A list of pageable results of scale upcs that were lazy loaded.
	 */
	private List<ScaleUpc> resolveResults(List<ScaleUpc> results) {
		results.forEach(this.objectResolver::fetch);
		return results;
	}

	/**
	 * Returns a list of scale upc data matching a description.
	 *
	 * @param description The description to look for data about.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of scale upc data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "description")
	public PageableResult<ScaleUpc> findAllByDescription(
			@RequestParam("description") String description,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			HttpServletRequest request){
		this.parameterValidator.validate(description, ScaleManagementController.DEFAULT_NO_DESCRIPTION_MESSAGE,
				ScaleManagementController.DESCRIPTION_MESSAGE_KEY, request.getLocale());
		this.logFindByDescription(request.getRemoteAddr(), description);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleManagementController.DEFAULT_PAGE : page;

		return this.resolveResults(this.service.findByDescription(description, ic, pg));
	}

	/**
	 * Search against scale upc for occurrence of the input PLU List.
	 *
	 * @param pluList The PLUs searched for.
	 * @param request The HTTP request that initiated this call.
	 * @return Hits result with Not found UPCs List
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="hits/pluList")
	public Hits findHitsByPluList(@RequestParam("pluList") List<Long> pluList, HttpServletRequest request) {

		// Min one UPC is required.
		this.parameterValidator.validate(pluList, ScaleManagementController.DEFAULT_NO_PLU_MESSAGE,
				ScaleManagementController.PLU_MESSAGE_KEY, request.getLocale());

		this.logFindByPluList(request.getRemoteAddr(), pluList);
		return this.service.findHitsByPluList(pluList);
	}

	/**
	 * Updates a ScaleUpc.
	 *
	 * @param scaleUpc The scaleUpc to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The updated ScaleUpc and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "updateScaleUpc")
	public ModifiedEntity<ScaleUpc> updateScaleUpc(@RequestBody ScaleUpc scaleUpc,
												   HttpServletRequest request){
		this.logUpdate(request.getRemoteAddr(), scaleUpc);
		ScaleUpc updatedScaleUpc =  this.service.update(scaleUpc);
		String updateMessage = this.messageSource.getMessage(ScaleManagementController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleUpc.getPlu()}, ScaleManagementController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				request.getLocale());
		this.objectResolver.fetch(updatedScaleUpc);
		return new ModifiedEntity<>(updatedScaleUpc, updateMessage);
	}

	/**
	 * Maintenance a ScaleUpc.
	 *
	 * @param scaleUpc The scaleUpc to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The updated ScaleUpc and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "sendMaintenance")
	public ModifiedEntity<ScaleUpc> sendMaintenance(@RequestBody ScaleUpc scaleUpc,
														HttpServletRequest request){
		this.logSendMaintenance(request.getRemoteAddr(), scaleUpc);
		ScaleUpc updatedScaleUpc =  this.service.sendMaintenance(scaleUpc);
		String updateMessage = this.messageSource.getMessage(ScaleManagementController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleUpc.getPlu()}, ScaleManagementController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				request.getLocale());
		this.objectResolver.fetch(updatedScaleUpc);
		return new ModifiedEntity<>(updatedScaleUpc, updateMessage);
	}

	/**
	 * Bulk updates a list of ScaleUpc.
	 *
	 * @param scaleManagementBulkUpdate A scaleManagementBulkUpdate containing a list of scale upcs, an attribute to be updated, and the value.
	 * @param request the HTTP request that initiated this call.
	 * @return The updated ScaleUpc and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "bulkUpdateScaleUpc")
	public ModifiedEntity<List<ScaleUpc>> bulkUpdateScaleUpc(@RequestBody ScaleManagementBulkUpdate scaleManagementBulkUpdate,
															 HttpServletRequest request){
		this.logBulkUpdate(request.getRemoteAddr(), scaleManagementBulkUpdate);
		List<ScaleUpc> updatedScaleUpcList =  this.service.bulkUpdate(scaleManagementBulkUpdate);
		String updateMessage = this.messageSource.getMessage(ScaleManagementController.BULK_UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleManagementBulkUpdate.getAttribute().toString()}, ScaleManagementController.DEFAULT_BULK_UPDATE_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(this.resolveResults(updatedScaleUpcList), updateMessage);
	}

	/**
	 * Calls excel export for plu scale maintenance search.
	 *
	 * @param plus List of plus to search on.
	 * @param totalPages Total amount of pages.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportPlusToCsv", headers = "Accept=text/csv")
	public void findByPlusExport(
			@RequestParam(name = "plus", required = false) List<Long> plus,
			@RequestParam(name = "totalPages", required = false) int totalPages,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){

		this.logPluExportToExcel(StringUtils.join(plus, ","), request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			for(int x = 0; x < totalPages; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateScaleMaintenanceCsv.getHeading());
				}
				response.getOutputStream().print(CreateScaleMaintenanceCsv.createCsv(this.service.findByPlu(plus,
						false, x)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Calls excel export for plu scale description maintenance search.
	 *
	 * @param description Description to search on.
	 * @param totalPages Total amount of pages.
	 * @param downloadId The download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="exportDescriptionToCsv", headers = "Accept=text/csv")
	public void findByDescriptionExport(
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "totalPages", required = false) int totalPages,
			@RequestParam(value = "downloadId", required = false) String downloadId,
			HttpServletRequest request, HttpServletResponse response){

		this.logDescriptionExportToExcel(description, request.getRemoteAddr());
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			for(int x = 0; x < totalPages; x++) {
				if (x == 0) {
					response.getOutputStream().println(CreateScaleMaintenanceCsv.getHeading());
				}
				response.getOutputStream().print(CreateScaleMaintenanceCsv.createCsv(this.service.findByDescription(description,
						false, x)));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Log's a user's request to get all records for multiple PLUs.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param pluList The PLU list the user is searching for.
	 */
	private void logFindByPluList(String ip, List<Long> pluList) {
		ScaleManagementController.logger.info(
				String.format(ScaleManagementController.FIND_BY_PLU_MESSAGE,
						this.userInfo.getUserId(), ip, ListFormatter.formatAsString(pluList)));
	}

	/**
	 * Log's a user's request to get all records matching a description.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param description The description the user is searching for.
	 */
	private void logFindByDescription(String ip, String description) {
		ScaleManagementController.logger.info(
				String.format(ScaleManagementController.FIND_BY_DESCRIPTION_MESSAGE,
						this.userInfo.getUserId(), ip, description));
	}

	/**
	 * Logs a user's request to update a scale upc.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param scaleUpc The scale upc to be updated.
	 */
	private void logUpdate(String ip, ScaleUpc scaleUpc){
		ScaleManagementController.logger.info(String.format(ScaleManagementController.UPDATE_SCALE_UPC_MESSAGE,
				this.userInfo.getUserId(), ip, scaleUpc.getUpc(), scaleUpc.getPlu()));
	}

	/**
	 * Logs a user's request to maintenance a scale upc.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param scaleUpc The scale upc to be updated.
	 */
	private void logSendMaintenance(String ip, ScaleUpc scaleUpc){
		ScaleManagementController.logger.info(String.format(ScaleManagementController.MAINTENANCE_SCALE_UPC_MESSAGE,
				this.userInfo.getUserId(), ip, scaleUpc.getUpc(), scaleUpc.getPlu()));
	}

	/**
	 * Logs a user's request to update a list of scale upcs.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param scaleManagementBulkUpdate A bulk update of the values that need to be updated.
	 */
	private void logBulkUpdate(String ip, ScaleManagementBulkUpdate scaleManagementBulkUpdate){
		ScaleManagementController.logger.info(String.format(ScaleManagementController.UPDATE_BULK_SCALE_UPCS_MESSAGE,
				this.userInfo.getUserId(), ip, scaleManagementBulkUpdate.toString()));
	}

	/**
	 * Log's a user's request to get a PLU excel export.
	 *
	 * @param params the PLUs requested
	 * @param ip The IP address th user is logged in from.
	 */
	private void logPluExportToExcel(String params,String ip){
		ScaleManagementController.logger.info(
				String.format(ScaleManagementController.PLU_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, params));
	}

	/**
	 * Log's a user's request to get a PLU excel export.
	 *
	 * @param params the description requested
	 * @param ip The IP address th user is logged in from.
	 */
	private void logDescriptionExportToExcel(String params,String ip){
		ScaleManagementController.logger.info(
				String.format(ScaleManagementController.DESCRIPTION_EXPORT_TO_EXCEL_MESSAGE,
						this.userInfo.getUserId(), ip, params));
	}

	/**
	 * Generates a CSV of an graphics code. This includes all UPCs & PLUs the graphic code is part of,
	 * and all the descriptions, ingredient statement number, nutrient statement number, & effective date
	 * tied to each PLU.
	 *
	 * @param requestedCode The code to search super-ingredients for.
	 * @param requestedCodeType	The type of code they are looking for
	 * @param downloadId An ID to put into a cookie so the front end can identify this download.
	 * @param request The HTTP request that initiated the request.
	 * @param response The HTTP response that the report will be streamed to.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportToCSV", headers = "Accept=text/csv")
	public void exportScaleManagementCode(@RequestParam(value = "requestedCode", required = true) Long requestedCode,
								  @RequestParam(value = "requestedCodeType", required = true) String requestedCodeType,
								  @RequestParam(value = "downloadId", required = false) String downloadId,
								  HttpServletRequest request, HttpServletResponse response) {

		this.logExportScaleManagementCodeReportRequest(requestedCode, request.getRemoteAddr(), requestedCodeType);

		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		// includeCounts in streamScaleManagementCode() is true because we want to return all the data. So, we need the
		// the paging information for the ScaleManagement Service to loop through all the pages of info
		try {
			this.service.streamScaleManagementCode(response.getOutputStream(), requestedCodeType, requestedCode,
					true, DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Logs a user's request for an export of graphic code.
	 *
	 * @param requestedCode The code they are looking for.
	 * @param codeType	The type of code they are looking for
	 * @param ip The IP address the request came from.
	 */
	private void logExportScaleManagementCodeReportRequest(Long requestedCode, String ip, String codeType) {
		ScaleManagementController.logger.info(String.format(ScaleManagementController.EXPORT_CODE_TYPE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, codeType, requestedCode));
	}
}
