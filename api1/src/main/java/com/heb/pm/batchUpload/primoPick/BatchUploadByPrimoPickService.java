/*
 * ExcelUploadByPrimoPickService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.batchUpload.primoPick;

import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Holds all of the business logic for excel upload/download by Primo Pick.
 *
 * @author vn70529
 * @since 2.12
 */
@Service
public class BatchUploadByPrimoPickService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchUploadByPrimoPickService.class);

	private static final String GET_TEMPLATE_LOG_ERROR_MESSAGE =
			"User %s requested a template for primo pick: %s.";

	// Holds the name of Primo Pick template.
	protected static final String PRIMO_PICK_TEMPLATE_NAME = "batch_upload_templete/Excel_Upload_By_PrimoPick.xlsx";

	@Autowired
	private UserInfo userInfo;
	/**
	 * Get the Primo Pick.
	 *
	 * @return input stream of template.
	 */
	public InputStream getPrimoPickTemplate() {

		InputStream is = null;

		try {
			is = new ClassPathResource(BatchUploadByPrimoPickService.PRIMO_PICK_TEMPLATE_NAME).getInputStream();
		} catch (IOException ioe) {
			LOGGER.error(String.format(GET_TEMPLATE_LOG_ERROR_MESSAGE, userInfo.getUserId(), ioe.getMessage()));
		}

		return is;
	}

}
