/*
 *  WineScoringOrganizationController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.wineScoringOrganization;


import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ScoringOrganization;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.list.ListFormatter;
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
 * REST endpoint for functions related to Wine Scoring Organization.
 *
 * @author vn70529
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + WineScoringOrganizationController.COUNTRY_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_WINE_SCORING_ORGANIZATION)
public class WineScoringOrganizationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WineScoringOrganizationController.class);

	protected static final String COUNTRY_URL = "/wineScoringOrganization";
	protected static final String FIND_ALL_ORDER_BY_NAME_URL = "/getAllWineScoringOrganizationsOrderById";
	private static final String ADD_NEW_WINE_SCORING_ORGANIZATIONS = "/addNewWineScoringOrganizations";
	private static final String UPDATE_WINE_SCORING_ORGANIZATIONS = "/updateWineScoringOrganizations";
	private static final String DELETE_WINE_SCORING_ORGANIZATIONS = "/deleteWineScoringOrganizations";


	// Log messages.
	private static final String FIND_ALL_MESSAGE = "User %s from IP %s requested all wine scoring organizations.";
	private static final String ADD_NEW_WINE_SCORING_ORGANIZATIONS_MESSAGE = "User %s from IP %s requested add new the list of scoring organizations [%s].";
	private static final String UPDATE_WINE_SCORING_ORGANIZATIONS_MESSAGE = "User %s from IP %s requested edit the list of scoring organizations [%s].";
	private static final String DELETE_WINE_SCORING_ORGANIZATIONS_MESSAGE = "User %s from IP %s requested add new the list of scoring organizations [%s].";

	// Holds the message to show when add new or update or delete success.
	private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";

	@Autowired
	private WineScoringOrganizationService wineScoringOrganizationService;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Returns a list of all ScoringOrganizations ordered by Scoring Organization id.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return A List of all ScoringOrganizations ordered by Scoring Organization id.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = WineScoringOrganizationController.FIND_ALL_ORDER_BY_NAME_URL)
	public List<ScoringOrganization> findAllOrderByWineScoringOrganization(HttpServletRequest request){

		this.logFindAllOrderByWineScoringOrganizationIdRequest(request.getRemoteAddr());

		return this.wineScoringOrganizationService.findAllByOrderByScoringOrganizationIdAsc();
	}

	/**
	 * Add new the list of Scoring Organizations.
	 *
	 * @param scoringOrganizations The list of Scoring Organizations to add new.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of Scoring Organizations after add new and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = WineScoringOrganizationController.ADD_NEW_WINE_SCORING_ORGANIZATIONS)
	public ModifiedEntity<List<ScoringOrganization>> addWineScoringOrganizations(@RequestBody List<ScoringOrganization> scoringOrganizations,
																				 HttpServletRequest request) {
		//show log message when init method
		WineScoringOrganizationController.LOGGER.info(String.format(WineScoringOrganizationController.ADD_NEW_WINE_SCORING_ORGANIZATIONS_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(scoringOrganizations)));

		//call handle from service
		this.wineScoringOrganizationService.addScoringOrganizations(scoringOrganizations);

		//research data after delete successfully
		List<ScoringOrganization> newScoringOrganizations = this.wineScoringOrganizationService.findAllByOrderByScoringOrganizationIdAsc();

		return new ModifiedEntity<>(newScoringOrganizations, ADD_SUCCESS_MESSAGE);
	}

	/**
	 * Update information for the list of Scoring Organizations.
	 *
	 * @param scoringOrganizations The list of Scoring Organizations to update.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of Scoring Organizations after update and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = WineScoringOrganizationController.UPDATE_WINE_SCORING_ORGANIZATIONS)
	public ModifiedEntity<List<ScoringOrganization>> updateWineScoringOrganizations(@RequestBody List<ScoringOrganization> scoringOrganizations,
																					HttpServletRequest request) {
		//show log message when init method
		WineScoringOrganizationController.LOGGER.info(String.format(WineScoringOrganizationController.UPDATE_WINE_SCORING_ORGANIZATIONS_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(scoringOrganizations)));

		//call handle from service
		this.wineScoringOrganizationService.updateScoringOrganizations(scoringOrganizations);
		//research data after delete successfully
		List<ScoringOrganization> newScoringOrganizations = this.wineScoringOrganizationService.findAllByOrderByScoringOrganizationIdAsc();

		return new ModifiedEntity<>(newScoringOrganizations, UPDATE_SUCCESS_MESSAGE);
	}



	/**
	 * Delete the list of scoring Organizations.
	 *
	 * @param scoringOrganizations The list of scoring Organizations to delete.
	 * @param request     The HTTP request that initiated this call.
	 * @return The list of scoring Organizations after deleted and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = WineScoringOrganizationController.DELETE_WINE_SCORING_ORGANIZATIONS)
	public ModifiedEntity<List<ScoringOrganization>> deleteWineScoringOrganizations(@RequestBody List<ScoringOrganization> scoringOrganizations,
																					HttpServletRequest request) {
		//show log message when init method
		WineScoringOrganizationController.LOGGER.info(String.format(WineScoringOrganizationController.DELETE_WINE_SCORING_ORGANIZATIONS_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(scoringOrganizations)));

		//call handle from service
		this.wineScoringOrganizationService.deleteScoringOrganizations(scoringOrganizations);

		//research data after delete successfully
		List<ScoringOrganization> newScoringOrganizations = this.wineScoringOrganizationService.findAllByOrderByScoringOrganizationIdAsc();

		return new ModifiedEntity<>(newScoringOrganizations, DELETE_SUCCESS_MESSAGE);
	}

	/**
	 * Logs a users request for all wine scoring organizations.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logFindAllOrderByWineScoringOrganizationIdRequest(String ipAddress) {
		WineScoringOrganizationController.LOGGER.info(String.format(WineScoringOrganizationController.FIND_ALL_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}
}
