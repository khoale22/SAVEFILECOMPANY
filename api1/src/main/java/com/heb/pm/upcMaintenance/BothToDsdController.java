/*
 *   BothToDsdController
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for functions related to getting details for, and submitting, both to DSD.
 *
 * @author s573181
 * @since 2.0.5
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		BothToDsdController.BOTH_TO_DSD)
@AuthorizedResource(ResourceConstants.UPC_SWAP_BOTH_TO_DSD)
public class BothToDsdController {

	private static final Logger logger = LoggerFactory.getLogger(DsdToBothController.class);

	protected static final String BOTH_TO_DSD = "/upcMaintenance/bothToDsd";

	// Log Messages
	private static final String FIND_ALL_BOTH_TO_DETAILS =
			"User %s from IP %s has requested both to DSD details for UPC: %s";
	private static final String SUBMIT_BOTH_TO_DSD =
			"User %s from IP %s has submitted both to DSD for : %s.";

	// Constants related to messages for the front end.
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Update successful";
	private static final String DEFAULT_GET_DETAILS_SUCCESS = "Details successfully received.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY = "BothToDsdController.updateSuccessful";
	private static final String GET_DETAILS_SUCCESS_KEY = "BothToDsdController.getDetailsSuccessful";

	@Autowired
	private BothToDsdService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Returns a list of upc swaps for given UPCs of type both.
	 *
	 * @param upcList List of UPCs sent from user to obtain information on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of upc swaps for given upcs and item codes, and a message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET)
	public ModifiedEntity<List<UpcSwap>> getBothToDsdDetails(
			@RequestParam(value = "upcList") List<Long> upcList, HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr(), upcList);
		List<UpcSwap> listPostSave = this.service.findAll(upcList);
		String updateMessage = this.messageSource.getMessage(
				BothToDsdController.GET_DETAILS_SUCCESS_KEY,
				null, BothToDsdController.DEFAULT_GET_DETAILS_SUCCESS, request.getLocale());
		return new ModifiedEntity<>(listPostSave, updateMessage);
	}

	/**
	 * Submits a request for moving specified UPCs of type both into DSD only.
	 *
	 * @param bothToDsdList List of upc swaps containing necessary information to complete a both to DSD move.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of upc swaps, and a message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public ModifiedEntity<List<UpcSwap>> update(@RequestBody List<UpcSwap> bothToDsdList, HttpServletRequest request){
		this.logSubmitBothToDsd(request.getRemoteAddr(), bothToDsdList);
		List<UpcSwap> listPostSave = this.service.update(bothToDsdList);
		String updateMessage = this.messageSource.getMessage(
				BothToDsdController.UPDATE_SUCCESS_MESSAGE_KEY,
				null, BothToDsdController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(listPostSave, updateMessage);
	}

	/**
	 * Logs a user's request for all both to DSD requests.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param upcList The list of DSD UPCs to obtain information from.
	 */
	private void logFindAll(String ip, List<Long> upcList) {
		for(int index = 0; index < upcList.size(); index++) {
			BothToDsdController.logger.info(
					String.format(BothToDsdController.FIND_ALL_BOTH_TO_DETAILS, this.userInfo.getUserId(),
							ip, upcList.get(index)));
		}
	}

	/**
	 * Logs a user's request to submit both to DSD requests.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param bothToDsdList The list of DSDs to both to submit.
	 */
	private void logSubmitBothToDsd(String ip, List<UpcSwap> bothToDsdList) {
		for (UpcSwap bothToDsd : bothToDsdList) {
			BothToDsdController.logger.info(
					String.format(BothToDsdController.SUBMIT_BOTH_TO_DSD, this.userInfo.getUserId(),
							ip, bothToDsd));
		}
	}
}
