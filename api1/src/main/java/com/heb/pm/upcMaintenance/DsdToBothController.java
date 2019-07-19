/*
 *  DsdToBothController
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
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
 * REST endpoint for functions related to getting details for, and submitting, DSD to Both.
 *
 * @author s573181
 * @since 2.0.5
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		DsdToBothController.DSD_TO_BOTH)
@AuthorizedResource(ResourceConstants.UPC_SWAP_DSD_TO_BOTH)
public class DsdToBothController {

	private static final Logger logger = LoggerFactory.getLogger(DsdToBothController.class);

	protected static final String DSD_TO_BOTH = "/upcMaintenance/dsdToBoth";

	// Log Messages
	private static final String FIND_ALL_DSD_TO_BOTH_DETAILS =
			"User %s from IP %s has requested DSD to Both details for UPC: %s, and item code: %s.";
	private static final String SUBMIT_DSD_TO_BOTH =
			"User %s from IP %s has submitted DSD to Both for : %s.";

	// Constants related to messages for the front end.
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Update successful";
	private static final String DEFAULT_GET_DETAILS_SUCCESS = "Details successfully received.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY = "DsdToBothController.updateSuccessful";
	private static final String GET_DETAILS_SUCCESS_KEY = "DsdToBothController.getDetailsSuccessful";


	@Autowired
	private DsdToBothService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Returns a list of upc swaps for given upcs and item codes.
	 *
	 * @param upcList List of UPCs sent from user to obtain information on.
	 * @param itemCodeList List of item codes sent from user to obtain information on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of upc swaps for given upcs and item codes, and a message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET)
	public ModifiedEntity<List<UpcSwap>> update(@RequestParam(value = "upcList") List<Long> upcList,
			@RequestParam(value = "itemCodeList") List<Long> itemCodeList, HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr(), upcList, itemCodeList);
		List<UpcSwap> listPostSave = this.service.findAll(upcList, itemCodeList);

		String updateMessage = this.messageSource.getMessage(
				DsdToBothController.GET_DETAILS_SUCCESS_KEY,
				null, DsdToBothController.DEFAULT_GET_DETAILS_SUCCESS, request.getLocale());
		return new ModifiedEntity<>(listPostSave, updateMessage);
	}

	/**
	 * Submits a request for moving specified DSD only UPCs into new item codes (to become a both).
	 *
	 * @param dsdToBothList List of upc swaps containing necessary information to complete a DSD to both move.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of upc swaps, and a message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public ModifiedEntity<List<UpcSwap>> submitDsdToBoth(@RequestBody List<UpcSwap> dsdToBothList,
																	HttpServletRequest request){
		this.logSubmitDsdToBoth(request.getRemoteAddr(), dsdToBothList);
		List<UpcSwap> listPostSave = this.service.update(dsdToBothList);
		String updateMessage = this.messageSource.getMessage(
				DsdToBothController.UPDATE_SUCCESS_MESSAGE_KEY,
				null, DsdToBothController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(listPostSave, updateMessage);
	}

	/**
	 * Logs a user's request for all DSD to both requests.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param upcList The list of DSD UPCs to obtain information from.
	 * @param itemCodeList The list of item codes to obtain information from.
	 */
	private void logFindAll(String ip, List<Long> upcList, List<Long> itemCodeList) {
		for(int index = 0; index < upcList.size(); index++) {
			DsdToBothController.logger.info(
					String.format(DsdToBothController.FIND_ALL_DSD_TO_BOTH_DETAILS, this.userInfo.getUserId(),
							ip, upcList.get(index), itemCodeList.get(index)));
		}
	}

	/**
	 * Logs a user's request for submitting DSD to Both.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param dsdToBothList The list of DSDs to both to submit.
	 */
	private void logSubmitDsdToBoth(String ip, List<UpcSwap> dsdToBothList) {
		for (UpcSwap dsdToBoth : dsdToBothList) {
			DsdToBothController.logger.info(
					String.format(DsdToBothController.SUBMIT_DSD_TO_BOTH, this.userInfo.getUserId(),
							ip, dsdToBoth));
		}
	}
}
