/*
 * BatchUploadByCategorySpecificAttributesService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.batchUpload.categorySpecific;

import com.heb.util.controller.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Holds all of the business logic for excel upload/download by category specific attributes.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Service
public class BatchUploadByCategorySpecificAttributesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchUploadByCategorySpecificAttributesService.class);
	/**
	 * Holds the format of template path.
	 */
	private static final String EXCEL_TEMPLATE_PATH_FORMAT = "batch_upload_templete/%s";
	/**
	 * The id of apparel template.
	 */
	private static final int APPAREL_TEMPLATE_ID = 1;
	/**
	 * The id of kitchenware template.
	 */
	private static final int KITCHENWARE_TEMPLATE_ID = 2;
	/**
	 * The id of large and small appliances.
	 */
	private static final int LARGE_AND_SMALL_APPLIANCES_TEMPLATE_ID = 3;
	/**
	 * The id of softlines template
	 */
	private static final int SOFTLINES_TEMPLATE_ID = 4;
	/**
	 * The id of wine template
	 */
	private static final int WINE_TEMPLATE_ID = 5;
	/**
	 * The name of apprarel template.
	 */
	private static final String APPAREL_TEMPLATE_NAME = "Excel_Upload_By_Apparel.xlsx";
	/**
	 * The name of kitchenware template.
	 */
	private static final String KITCHENWARE_TEMPLATE_NAME = "Excel_Upload_By_Kitchenware.xlsx";
	/**
	 * The name of large and small appliances.
	 */
	private static final String LARGE_AND_SMALL_APPLIANCES_TEMPLATE_NAME = "Excel_Upload_By_Large_and_Small_Appliances.xlsx";
	/**
	 * The name of softlines template
	 */
	private static final String SOFTLINES_TEMPLATE_NAME = "Excel_Upload_By_Softlines.xlsx";
	/**
	 * The name of softlines template
	 */
	private static final String WINE_TEMPLATE_NAME = "Excel_Upload_By_Wine.xlsx";
	/**
	 * Error message for get template.
	 */
	private static final String GET_TEMPLATE_LOG_ERROR_MESSAGE =
			"User %s requested a template for category specific attributes: %s.";
	@Autowired
	private UserInfo userInfo;
	/**
	 * Get excel template by template name.
	 *
	 * @param excelTemplateName the name of the template.
	 * @return input stream of template.
	 */
	public InputStream getExcelTemplateByName(String excelTemplateName) {
		InputStream is = null;
		if (!StringUtils.isEmpty(excelTemplateName)) {
			try {
				is = new ClassPathResource(String.format(BatchUploadByCategorySpecificAttributesService.EXCEL_TEMPLATE_PATH_FORMAT, excelTemplateName)).getInputStream();
			} catch (IOException ioe) {
				LOGGER.error(String.format(GET_TEMPLATE_LOG_ERROR_MESSAGE,userInfo.getUserId(), ioe.getMessage()));
			}
		}
		return is;
	}
	/**
	 * The the name of excel template by template id.
	 *
	 * @param excelTemplateId the id of excel template.
	 * @return the name of template.
	 */
	public String getExcelTemplateNameById(int excelTemplateId) {
		switch (excelTemplateId){
			case BatchUploadByCategorySpecificAttributesService.APPAREL_TEMPLATE_ID:
				return BatchUploadByCategorySpecificAttributesService.APPAREL_TEMPLATE_NAME;
			case BatchUploadByCategorySpecificAttributesService.KITCHENWARE_TEMPLATE_ID:
				return BatchUploadByCategorySpecificAttributesService.KITCHENWARE_TEMPLATE_NAME;
			case BatchUploadByCategorySpecificAttributesService.LARGE_AND_SMALL_APPLIANCES_TEMPLATE_ID:
				return BatchUploadByCategorySpecificAttributesService.LARGE_AND_SMALL_APPLIANCES_TEMPLATE_NAME;
			case BatchUploadByCategorySpecificAttributesService.SOFTLINES_TEMPLATE_ID:
				return BatchUploadByCategorySpecificAttributesService.SOFTLINES_TEMPLATE_NAME;
			case BatchUploadByCategorySpecificAttributesService.WINE_TEMPLATE_ID:
				return BatchUploadByCategorySpecificAttributesService.WINE_TEMPLATE_NAME;
			default:
				return null;
		}
	}
}
