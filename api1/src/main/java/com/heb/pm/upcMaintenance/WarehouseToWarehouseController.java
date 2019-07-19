/*
 * MoveWarehouseUpcSwapController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
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
 * REST endpoint for functions related to getting details for, and submitting, warehouse move upc swaps.
 *
 * @author m314029
 * @since 2.0.4
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		WarehouseToWarehouseController.UPC_SWAP_WAREHOUSE_MOVE_URL)
@AuthorizedResource(ResourceConstants.UPC_SWAP_WAREHOUSE_TO_WAREHOUSE)
public class WarehouseToWarehouseController {

	private static final Logger logger = LoggerFactory.getLogger(WarehouseToWarehouseController.class);

	protected static final String UPC_SWAP_WAREHOUSE_MOVE_URL = "/upcMaintenance/warehouseMoveUpcSwap";

	// Log Messages
	private static final String FIND_ALL_UPC_SWAP_DETAILS =
			"User %s from IP %s has requested upc swap details for UPC: %s, and item code: %s.";
	private static final String SUBMIT_UPC_SWAP =
			"User %s from IP %s has submitted UPC swaps : %s.";

	// Constants related to messages for the front end.
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Update successful";
	private static final String DEFAULT_GET_DETAILS_SUCCESS = "Details successfully received.";
	private static final String GET_DETAILS_SUCCESS_KEY =
			"MoveWarehouseUpcSwapController.getDetailsSuccessful";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY =
			"MoveWarehouseUpcSwapController.updateSuccessful";

	@Autowired
	private WarehouseToWarehouseService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Returns a list of upc swaps for given upcs and item codes.
	 *
	 * @param upcList List of upcs sent from user to obtain information on.
	 * @param itemCodeList List of item codes sent from user to obtain information on.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of upc swaps for given upcs and item codes, and a message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET)
	public ModifiedEntity<List<UpcSwap>> getWarehouseMoveUpcSwapDetails(
			@RequestParam(value = "upcList") List<Long> upcList,
			@RequestParam(value = "itemCodeList") List<Long> itemCodeList, HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr(), upcList, itemCodeList);

		List<UpcSwap> listPostSave = this.service.findAll(upcList, itemCodeList);

		String updateMessage = this.messageSource.getMessage(
				WarehouseToWarehouseController.GET_DETAILS_SUCCESS_KEY,
				null, WarehouseToWarehouseController.DEFAULT_GET_DETAILS_SUCCESS, request.getLocale());
		return new ModifiedEntity<>(listPostSave, updateMessage);
	}

	/**
	 * Submits a request for moving specified upcs into new item codes.
	 *
	 * @param upcSwapList List of upc swaps containing necessary information to complete a warehouse move upc swap.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of upc swaps, and a message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public ModifiedEntity<List<UpcSwap>> update(@RequestBody List<UpcSwap> upcSwapList,	HttpServletRequest request){

		this.logSubmitUpcSwap(request.getRemoteAddr(), upcSwapList);

		List<UpcSwap> listPostSave = this.service.update(upcSwapList);

		String updateMessage = this.messageSource.getMessage(
				WarehouseToWarehouseController.UPDATE_SUCCESS_MESSAGE_KEY,
				null, WarehouseToWarehouseController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(listPostSave, updateMessage);
	}

	/**
	 * Logs a user's request for submitting a upc swap.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param upcSwapList The list of upc swaps to submit.
	 */
	private void logSubmitUpcSwap(String ip, List<UpcSwap> upcSwapList) {
		for (UpcSwap upcSwap : upcSwapList) {
			WarehouseToWarehouseController.logger.info(
					String.format(WarehouseToWarehouseController.SUBMIT_UPC_SWAP, this.userInfo.getUserId(),
							ip, upcSwap));
		}
	}

	/**
	 * Logs a user's request for all upc swaps.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param upcList The list of upcs to obtain information from.
	 * @param itemCodeList The list of item codes to obtain information from.
	 */
	private void logFindAll(String ip, List<Long> upcList, List<Long> itemCodeList) {
		for(int index = 0; index < upcList.size(); index++) {
			WarehouseToWarehouseController.logger.info(
					String.format(WarehouseToWarehouseController.FIND_ALL_UPC_SWAP_DETAILS, this.userInfo.getUserId(),
							ip, upcList.get(index), itemCodeList.get(index)));
		}
	}
}
