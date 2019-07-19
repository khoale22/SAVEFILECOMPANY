/*
 * ExcelUploadByPrimoPickController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.batchUpload.primoPick;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.BatchUploadRequest;
import com.heb.pm.batchUpload.BatchUploadResponse;
import com.heb.pm.batchUpload.BatchUploadService;
import com.heb.pm.batchUpload.BatchUploadType;
import com.heb.pm.batchUpload.assortment.AssortmentValidator;
import com.heb.pm.batchUpload.util.Constants;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.util.controller.StreamingExportException;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * REST endpoint for functions related to download excel template and upload excel template for Primo Pick.
 *
 * @author vn70529
 * @since 2.12
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		BatchUploadByPrimoPickController.BATCH_UPLOAD_BY_PRIMO_PICK)
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_BY_PRIMO_PICK)
public class BatchUploadByPrimoPickController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchUploadByPrimoPickController.class);

	// Log messages.
	private static final String DOWNLOAD_TEMPLATE_LOG_MESSAGE =
			"User %s from IP %s requested a template for primo pick.";

	private static final String UPLOAD_FILE_PRIMO_PICK_LOG_MESSAGE =
			"User %s from IP %s requested to upload primo pick file.";

	private static final String DOWNLOAD_TEMPLATE_LOG_ERROR_MESSAGE =
			"User %s from IP %s requested a template for primo pick: %s.";
	private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Primo pick upload submitted with tracking ID %d";
	/**
	 * Holds the path to request this controller.
 	 */
	protected static final String BATCH_UPLOAD_BY_PRIMO_PICK = "/primoPick";

	/**
	 * Holds the path to request download template.
 	 */
	private static final String DOWNLOAD_TEMPLATE = "/downloadTemplate";
	protected static final String RELATIVE_PATH_UPLOAD_PRIMO_PICK = "/batchUpload";
	/**
	 * file name for primo pick
	 */
	public static final String PRIMO_PICK_FILE_NAME = "Primo Pick";

	@Autowired
	private BatchUploadByPrimoPickService batchUploadByPrimoPickService;

	@Autowired
	private UserInfo userInfo;
	@Autowired
	private AntiVirusUtil antiVirusUtil;
	@Autowired
	private BatchUploadService batchUploadService;
	@Autowired
	private PrimoPickValidator primoPickValidator;
	/**
	 * Endpoint function for download Primo Pick template.
	 *
	 * @param response  instance of HttpServletResponse.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = BatchUploadByPrimoPickController.DOWNLOAD_TEMPLATE)
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {

		this.logRequest(request.getRemoteAddr());

		// Get the input stream of primo pick template.
		InputStream is = batchUploadByPrimoPickService.getPrimoPickTemplate();
		ServletOutputStream out = null;

		try {

			response.setHeader("Content-disposition", "attachment; filename=Excel_Upload_By_PrimoPick.xlsx");
			response.setContentType(Constants.DOWNLOAD_TEMPLATE_CONTENT_TYPE_EXCEL);
			out = response.getOutputStream();

			IOUtils.copy(is, out);
			out.flush();
		} catch (IOException e) {
			LOGGER.error(String.format(DOWNLOAD_TEMPLATE_LOG_ERROR_MESSAGE, userInfo.getUserId(),request.getRemoteAddr(), e.getMessage()));
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
	 * process the file upload Primo Pick.
	 * @param file the file upload
	 * @param desc the description
	 * @return obj status
	 * @throws AntiVirusException
	 * @throws IOException
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = RELATIVE_PATH_UPLOAD_PRIMO_PICK)
	public BatchUploadResponse uploadPrimoPick(HttpServletRequest request,@RequestParam("file") MultipartFile file,
											   @RequestParam("description") String desc) throws AntiVirusException,IOException {
		BatchUploadByPrimoPickController.LOGGER.info(String.format(BatchUploadByPrimoPickController.UPLOAD_FILE_PRIMO_PICK_LOG_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr()));
		// check virus
		antiVirusUtil.virusCheck(file.getBytes());
		primoPickValidator.validateTemplate(file.getBytes());
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
		BatchUploadByPrimoPickController.LOGGER.info(String.format(BatchUploadByPrimoPickController.DOWNLOAD_TEMPLATE_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}
	/**
	 * Prepare Batch Upload request for Primo pick Upload.
	 * @param file	file uploaded
	 * @param uploadDescription	batch upload description.
	 * @param userId file uploaded by user id.
	 * @return	prepared batch upload request.
	 */
	private BatchUploadRequest prepareRequest(byte[] file, String uploadDescription, String userId) {
		BatchUploadRequest batchUploadRequest = new BatchUploadRequest();
		batchUploadRequest.setData(file);
		batchUploadRequest.setBatchUploadType(BatchUploadType.PRIMO_PICK);
		batchUploadRequest.setUploadDescription(uploadDescription);
		batchUploadRequest.setUploadFileName(PRIMO_PICK_FILE_NAME);
		batchUploadRequest.setUserId(userId);
		return batchUploadRequest;
	}
}
