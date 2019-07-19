/*
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
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
 * Supports the ability to add associate UPCs to an existing UPC.
 *
 * @author d116773
 * @since 2.6.1
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		AddAssociateController.ADD_ASSOCIATE)
@AuthorizedResource(ResourceConstants.ADD_ASSOCIATE)
public class AddAssociateController {

	private static final Logger logger = LoggerFactory.getLogger(AddAssociateController.class);

	protected static final String ADD_ASSOCIATE = "/upcMaintenance/addAssociate";
	protected static final String FETCH_DETAILS = "/fetchDetails";

	private static final String ADD_SUCCESS_KEY = "AddAssociateController.successfulAdd";
	private static final String ADD_SUCCESS_DEFAULT_MESSAGE = "UPCs successfully added";

	private static final String GET_DETAILS_LOG_MESSAGE =
			"User %s from IP %s has requested details to add associates for the UPCs [%s]";
	private static final String ADD_ASSOCIATE_LOG_MESSAGE =
			"User %s from IP %s has requested adding the following associate UPCs [%s]";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private AddAssociateService addAssociateService;

	@Autowired
	private MessageSource messageSource;

	// Even though this method does not affect the back end, because the front end is passing in a list of objects,
	// it has to be a post so that the parameters are in the request body rather than in the URL.

	/**
	 * Takes in a partially filled in list of add associate requests and fills out the details for each of them.
	 *
	 * @param addAssociatesList The list of add associate requests to populate.
	 * @param request The HTTP Request that initiated this call.
	 * @return The same list of requests but populated with details.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = AddAssociateController.FETCH_DETAILS)
	public List<UpcSwap> getAddAssociateDetails(@RequestBody List<UpcSwap> addAssociatesList,
												HttpServletRequest request) {

		this.logGetDetails(request.getRemoteAddr(), addAssociatesList);

		return this.addAssociateService.getAddAssociateDetails(addAssociatesList);
	}

	/**
	 * Processes a list of add associates requests.
	 *
	 * @param addAssociatesList The list of add associates request to process.
	 * @param request The HTTP Request that initiated this call.
	 * @return A ModifiedEntity which wrapps a success message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public ModifiedEntity<List<UpcSwap>> addAssociates(
			@RequestBody List<UpcSwap> addAssociatesList, HttpServletRequest request) {

		this.logAddAssociates(request.getRemoteAddr(), addAssociatesList);

		this.addAssociateService.addAssociates(addAssociatesList);

		String updateMessage = this.messageSource.getMessage(
				AddAssociateController.ADD_SUCCESS_KEY,
				null, AddAssociateController.ADD_SUCCESS_DEFAULT_MESSAGE, request.getLocale());
		return new ModifiedEntity<List<UpcSwap>>(addAssociatesList, updateMessage);
	}

	/**
	 * Logs a user's request for details about a list of add associates requests.
	 *
	 * @param ipAddress The IP address the request came from.
	 * @param addAssociatesList The list of associates to get details about.
	 */
	private void logGetDetails(String ipAddress, List<UpcSwap> addAssociatesList) {
		AddAssociateController.logger.info(String.format(AddAssociateController.GET_DETAILS_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress, this.formatAddAssociatesForLog(addAssociatesList)));
	}

	/**
	 * Logs a user's request to add associate UPCs.
	 *
	 * @param ipAddress The IP address the request came from.
	 * @param addAssociatesList This list of add associate UPC requests.
	 */
	private void logAddAssociates(String ipAddress, List<UpcSwap> addAssociatesList) {
		AddAssociateController.logger.info(String.format(AddAssociateController.ADD_ASSOCIATE_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress, this.formatAddAssociatesForLog(addAssociatesList)));
	}

	/**
	 * Returns a string representation fo a list of UPC Swaps that is suitable for logging.
	 *
	 * @param addAssociatesList The list of UPC Swaps to format.
	 * @return The list formatted as a string for the log.
	 */
	private String formatAddAssociatesForLog(List<UpcSwap> addAssociatesList) {
		StringBuilder sb = new StringBuilder();
		addAssociatesList.forEach((u) -> {
			sb.append("{target:");
			if (u.getSource().getItemCode() == null) {
				sb.append("UPC=").append(u.getSource().getUpc());
			} else {
				sb.append("item code=").append(u.getSource().getItemCode());
			}
			sb.append(", UPC to add:").append(u.getDestination()).append("},");
		});
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
