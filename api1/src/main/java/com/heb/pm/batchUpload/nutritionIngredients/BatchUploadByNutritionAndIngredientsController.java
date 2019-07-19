/*
 * ExcelUploadByNutritionAndIngredientsController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.batchUpload.nutritionIngredients;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import com.heb.pm.batchUpload.BatchUploadRequest;
import com.heb.pm.batchUpload.BatchUploadResponse;
import com.heb.pm.batchUpload.BatchUploadService;
import com.heb.pm.batchUpload.BatchUploadType;
import com.heb.pm.batchUpload.util.Constants;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.util.controller.UserInfo;
import com.heb.util.sercurity.AntiVirusUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.StreamingExportException;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST endpoint for functions related to download excel template and upload excel template for Nutrition And Ingredients.
 *
 * @author vn70529
 * @since 2.12
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		BatchUploadByNutritionAndIngredientsController.BATCH_UPLOAD_BY_NUTRITION_AND_INGREDIENTS)
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_BY_NUTRITION_AND_INGREDIENTS)
public class BatchUploadByNutritionAndIngredientsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchUploadByNutritionAndIngredientsController.class);
	// Holds the path to request this controller.
	protected static final String BATCH_UPLOAD_BY_NUTRITION_AND_INGREDIENTS = "/nutritionAndIngredients";

	// Holds the path to request download template.
	private static final String DOWNLOAD_TEMPLATE = "/downloadTemplate";
	protected static final String RELATIVE_PATH_NUTRITION_INGREDIENT = "/batchUpload";
	/**
	 * Log messages.
	 */
private static final String DOWNLOAD_TEMPLATE_LOG_MESSAGE =
			"User %s from IP %s requested a template for nutrition and ingredients.";
	private static final String DOWNLOAD_TEMPLATE_LOG_ERROR_MESSAGE =
			"User %s from IP %s requested a template for nutrition and ingredients: %s.";
	private static final String UPLOAD_FILE_NUTRITION_LOG_MESSAGE =
			"User %s from IP %s requested to upload nutrition file.";
	public static final String NUTRITION_INGREDIENT_FILE_NAME = "Nutrition Ingredient";
	private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Nutrition ingredients upload submitted with tracking ID %d";
	@Autowired
	private BatchUploadByNutritionAndIngredientsService batchUploadByNutritionAndIngredientsService;

	@Autowired
	private UserInfo userInfo;
	@Autowired
	private AntiVirusUtil antiVirusUtil;
	@Autowired
	private BatchUploadService batchUploadService;
	@Autowired
	private NutritionValidator nutritionValidator;
	/**
	 * Endpoint function for download Nutrition And Ingredients template.
	 *
	 * @param response  instance of HttpServletResponse.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = BatchUploadByNutritionAndIngredientsController.DOWNLOAD_TEMPLATE)
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {

		this.logRequest(request.getRemoteAddr());

		// Get the input stream of Nutrition And Ingredients template.
		InputStream is = batchUploadByNutritionAndIngredientsService.getNutritionAndIngredientsTemplate();
		ServletOutputStream out = null;

		try {

			response.setHeader("Content-disposition", "attachment; filename=Excel_Upload_By_NtrnIng.xlsx");
			response.setContentType(Constants.DOWNLOAD_TEMPLATE_CONTENT_TYPE_EXCEL);
			out = response.getOutputStream();

			IOUtils.copy(is, out);
			out.flush();
		} catch (IOException e) {
			LOGGER.error(String.format(DOWNLOAD_TEMPLATE_LOG_ERROR_MESSAGE, userInfo.getUserId(),request.getRemoteAddr(),e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		} finally {

			try {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
				LOGGER.error(String.format(DOWNLOAD_TEMPLATE_LOG_ERROR_MESSAGE, userInfo.getUserId(),request.getRemoteAddr(), ioe.getMessage()));
				throw new StreamingExportException(ioe.getMessage(), ioe.getCause());
			}
		}
	}
	/**
	 * process the file upload nutrition ingredient.
	 * @param file the file upload
	 * @param desc the description
	 * @return obj status
	 * @throws com.heb.sadc.utils.AntiVirusException
	 * @throws IOException
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = RELATIVE_PATH_NUTRITION_INGREDIENT)
	public BatchUploadResponse uploadNutritionIngredient(HttpServletRequest request, @RequestParam("file") MultipartFile file,
														 @RequestParam("description") String desc) throws AntiVirusException,IOException {
		LOGGER.info(String.format(UPLOAD_FILE_NUTRITION_LOG_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr()));
		// check virus
		antiVirusUtil.virusCheck(file.getBytes());
		nutritionValidator.validateTemplate(file.getBytes());
		long trackingId = 0;
		BatchUploadRequest excelUploadBase = prepareRequest(file.getBytes(),desc,userInfo.getUserId());
		trackingId = this.batchUploadService.submit(excelUploadBase);
		BatchUploadResponse response = new BatchUploadResponse(trackingId,
				String.format(BATCH_UPLOAD_SUCCESS_MESSAGE, trackingId));
		return response;
	}
	/**
	 * Logs a users request.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logRequest(String ipAddress) {
		BatchUploadByNutritionAndIngredientsController.LOGGER.info(String.format(BatchUploadByNutritionAndIngredientsController.DOWNLOAD_TEMPLATE_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}
	/**
	 * Prepare Batch Upload request for nutrient ingredient Upload.
	 * @param file	file uploaded
	 * @param uploadDescription	batch upload description.
	 * @param userId file uploaded by user id.
	 * @return	prepared batch upload request.
	 */
	private BatchUploadRequest prepareRequest(byte[] file, String uploadDescription, String userId) {
		BatchUploadRequest batchUploadRequest = new BatchUploadRequest();
		batchUploadRequest.setData(file);
		batchUploadRequest.setBatchUploadType(BatchUploadType.NUTRITION);
		batchUploadRequest.setUploadDescription(uploadDescription);
		batchUploadRequest.setUploadFileName(NUTRITION_INGREDIENT_FILE_NAME);
		batchUploadRequest.setUserId(userId);
		return batchUploadRequest;
	}
}
