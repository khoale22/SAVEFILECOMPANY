/*
 * RemoveFromStoresController
 *  *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 */

package com.heb.pm.productDiscontinue;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.ModifiedEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller that writes all information related to remove from stores.
 *  @author m594201
 * @since 2.0.4
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		RemoveFromStoresController.REMOVE_FROM_STORES_URL)
@AuthorizedResource(ResourceConstants.REMOVE_FROM_STORES_SUPPORT)
public class RemoveFromStoresController {

	private static final Logger logger = LoggerFactory.getLogger(RemoveFromStoresController.class);

	/**
	 * The constant REMOVE_FROM_STORES_URL.
	 */
	protected static final String REMOVE_FROM_STORES_URL = "/productDiscontinue/removeFromStores";

	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Update successful";

	private static final String DEFAULT_REMOVE_SUCCESS_MESSAGE = "Remove from Stores Successful";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private RemoveFromStoresService removeFromStoresService;


	/**
	 * Remove from store modified entity.  Calls service and passes the a list of RemoveFromStores
	 *
	 * @param returnList the return list containing data that identifies which set of data will change
	 * @param request    The HTTP request that initiated this call.
	 * @return the modified entity
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModifiedEntity<List<RemoveFromStores>> removeFromStore(@RequestBody List<RemoveFromStores> returnList, HttpServletRequest request){

		logger.info("In RemoveFromStoresController");

		String updateMessage = "";

		List<RemoveFromStores> returnMessages;

		returnMessages = this.removeFromStoresService.update(returnList);

		for(RemoveFromStores removeFromStores: returnMessages) {
			if (removeFromStores.getErrorMessage() != null) {
				updateMessage =	updateMessage.concat(removeFromStores.getErrorMessage() + " for UPC " + removeFromStores.getUpc());
			}
		}

		if(updateMessage.equals(StringUtils.EMPTY)) {
			updateMessage = this.messageSource.getMessage(
					RemoveFromStoresController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
					null, RemoveFromStoresController.DEFAULT_REMOVE_SUCCESS_MESSAGE, request.getLocale());
		}
		return new ModifiedEntity<>(returnList, updateMessage);
	}
}
