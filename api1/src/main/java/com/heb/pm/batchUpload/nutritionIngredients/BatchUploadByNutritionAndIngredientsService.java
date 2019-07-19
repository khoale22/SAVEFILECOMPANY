/*
 * ExcelUploadByNutritionAndIngredientsService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.batchUpload.nutritionIngredients;

import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;

/**
 * Holds all of the business logic for excel upload/download by Nutrition And Ingredients.
 *
 * @author vn70529
 * @since 2.12
 */
@Service
public class BatchUploadByNutritionAndIngredientsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchUploadByNutritionAndIngredientsService.class);

	private static final String GET_NUTRITION_AND_INGREDIENTS_TEMPLATE_LOG_ERROR_MESSAGE =
			"User %s requested a template for nutrition and ingredients: %s.";

	// Holds the name of nutrition and ingredients template.
	protected static final String NUTRITION_AND_INGREDIENTS_TEMPLATE_NAME = "batch_upload_templete/Excel_Upload_By_NtrnIng.xlsx";

	@Autowired
	private UserInfo userInfo;

	/**
	 * Get the Nutrition And Ingredients.
	 *
	 * @return input stream of template.
	 */
	public InputStream getNutritionAndIngredientsTemplate() {

		InputStream is = null;

		try {
			is = new ClassPathResource(BatchUploadByNutritionAndIngredientsService.NUTRITION_AND_INGREDIENTS_TEMPLATE_NAME).getInputStream();
		} catch (IOException ioe) {
			LOGGER.error(String.format(GET_NUTRITION_AND_INGREDIENTS_TEMPLATE_LOG_ERROR_MESSAGE,userInfo.getUserId(), ioe.getMessage()));
		}

		return is;
	}

}
