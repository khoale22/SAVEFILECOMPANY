/*
 *  ShelfAttributesController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the controller for shelf Attributes screen.
 *
 * @author l730832
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ShelfAttributesController.SHELF_ATTRIBUTES)
@AuthorizedResource(ResourceConstants.SHELF_ATTRIBUTES)
public class ShelfAttributesController implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(ShelfAttributesController.class);

	protected static final String SHELF_ATTRIBUTES = "/shelfAttributes";

	private static final String UPDATE_FINISHED_SHELF_ATTRIBUTE =
			"The server has finished updating the shelf attributes for product Id: %d.";

	private static final String UPDATE_SHELF_ATTRIBUTE =
			"User %s from IP %s has updated the descriptions: %s.";

	private static final String UPDATE_SUCCESS_MESSAGE = "ShelfAttributesController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE =
			"Shelf Attributes for Product Id: %d have been updated successfully.";

	// Customer friendly description and language types.
	private static final String ENGLISH_DESCRIPTION_TAG = "ENG";
	private static final String SPANISH_DESCRIPTION_TAG = "SPN";
	private static final String CUSTOMER_FRIENDLY_TAG_1 = "TAG1";
	private static final String CUSTOMER_FRIENDLY_TAG_2 = "TAG2";

	// Log format message.
	private static final String LOG_UPDATE_MESSAGE = "%s: %s";
	// Log Descriptions.
	private static final String LOG_ENGLISH_DESCRIPTION = "English Description";
	private static final String LOG_SPANISH_DESCRIPTION = "Spanish Description";
	// Logging customer friendly descriptions.
	private static final String LOG_ENGLISH_CUSTOMER_FRIENDLY_TAG_1 = "Customer Friendly English 1";
	private static final String LOG_ENGLISH_CUSTOMER_FRIENDLY_TAG_2 = "Customer Friendly English 2";
	private static final String LOG_SPANISH_CUSTOMER_FRIENDLY_TAG_1 = "Customer Friendly Spanish 1";
	private static final String LOG_SPANISH_CUSTOMER_FRIENDLY_TAG_2 = "Customer Friendly Spanish 2";

	@Autowired
	private ShelfAttributesService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Saves new shelf attribute changes based on only the fields that were changed.
	 *
	 * @param productMaster
	 * @param request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = "updateShelfAttributes")
	public ModifiedEntity<ProductMaster> saveShelfAttributeChanges(@RequestBody ProductMaster productMaster,
																   HttpServletRequest request) {

		// This is marked as view permission as the permission model is complex and is dealt with in the service.
		this.logSaveShelfAttributeChanges(request.getRemoteAddr(), productMaster);

		String updateMessage = this.messageSource.getMessage(
				ShelfAttributesController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{productMaster.getProdId()},
				ShelfAttributesController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());

		ModifiedEntity<ProductMaster> modifiedEntity =
				new ModifiedEntity<>(this.service.updateShelfAttributeChanges(productMaster, this.userInfo), updateMessage);
		this.logFinishedSaveShelfAttributeChanges(request.getRemoteAddr(), modifiedEntity.getData().getProdId());
		return modifiedEntity;
	}

	/**
	 * Returns a list of description types to verify the length of all descriptions for front end validation.
	 * @param request the http request.
	 * @return list of description types.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getListOfDescriptionTypes")
	public List<DescriptionType> getListOfDescriptionTypes(HttpServletRequest request) {
		return this.service.getListOfDescriptionTypes();
	}

	/**
	 * Call the different methods to find the data necessary for a ServiceCaseSign object to return.
	 * @param productId - ProductId provided from the front end.
	 * @return ServiceCaseSign
	 */
	@RequestMapping(method = RequestMethod.POST, value = "getServiceCaseTagData")
	public ServiceCaseSign getServiceCaseTagData(@RequestBody Long productId){

		return this.service.getServiceCaseSignData(productId);
	}

	/**
	 * Logs a user request to update product descriptions. Log only those that have changed.
	 *
	 * @param ip The current ip address of the user.
	 * @param productMaster the product master sent from the front end.
	 */
	private void logSaveShelfAttributeChanges(String ip, ProductMaster productMaster) {
		List<String> logUpdatedDescriptions = new ArrayList<>();
		if(productMaster.getDescription() != null) {
			logUpdatedDescriptions.add(String.format(ShelfAttributesController.LOG_UPDATE_MESSAGE,
					LOG_ENGLISH_DESCRIPTION, productMaster.getDescription()));
		}
		if(productMaster.getSpanishDescription() != null) {
			logUpdatedDescriptions.add(String.format(ShelfAttributesController.LOG_UPDATE_MESSAGE,
					LOG_SPANISH_DESCRIPTION, productMaster.getSpanishDescription()));
		}
		if(productMaster.getProductDescriptions() != null) {
			for (ProductDescription productDescription: productMaster.getProductDescriptions()){
				switch (productDescription.getKey().getDescriptionType().trim()) {

					// Handles all customer friendly tag 2.
					case CUSTOMER_FRIENDLY_TAG_1: {
						if (productDescription.getKey().getLanguageType().trim().equals(ENGLISH_DESCRIPTION_TAG)) {
							logUpdatedDescriptions.add(String.format(ShelfAttributesController.LOG_UPDATE_MESSAGE,
									LOG_ENGLISH_CUSTOMER_FRIENDLY_TAG_1, productDescription.getDescription()));
						}
						if (productDescription.getKey().getLanguageType().trim().equals(SPANISH_DESCRIPTION_TAG)) {
							logUpdatedDescriptions.add(String.format(ShelfAttributesController.LOG_UPDATE_MESSAGE,
									LOG_SPANISH_CUSTOMER_FRIENDLY_TAG_1, productDescription.getDescription()));
						}
						break;
					}
					// Handles all customer friendly tag 2.
					case CUSTOMER_FRIENDLY_TAG_2: {
						if (productDescription.getKey().getLanguageType().trim().equals(ENGLISH_DESCRIPTION_TAG)) {
							logUpdatedDescriptions.add(String.format(ShelfAttributesController.LOG_UPDATE_MESSAGE,
									LOG_ENGLISH_CUSTOMER_FRIENDLY_TAG_2, productDescription.getDescription()));
						}
						if (productDescription.getKey().getLanguageType().trim().equals(SPANISH_DESCRIPTION_TAG)) {
							logUpdatedDescriptions.add(String.format(ShelfAttributesController.LOG_UPDATE_MESSAGE,
									LOG_SPANISH_CUSTOMER_FRIENDLY_TAG_2, productDescription.getDescription()));
						}
						break;
					}
					default:
						break;
				}
			}
		}
		ShelfAttributesController.logger.info(
				String.format(ShelfAttributesController.UPDATE_SHELF_ATTRIBUTE, this.userInfo.getUserId(),
						ip, Arrays.toString(logUpdatedDescriptions.toArray())));
	}

	/**
	 * Log's the finished request to track transaction times.
	 *
	 * @param ip
	 * @param productDescriptionId
	 */
	private void logFinishedSaveShelfAttributeChanges(String ip, long productDescriptionId) {
		ShelfAttributesController.logger.info(
				String.format(ShelfAttributesController.UPDATE_FINISHED_SHELF_ATTRIBUTE, productDescriptionId));
	}

}
