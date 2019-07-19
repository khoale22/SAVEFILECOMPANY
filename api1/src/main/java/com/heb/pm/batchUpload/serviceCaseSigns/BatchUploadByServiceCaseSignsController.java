/*
 * BatchUploadByServiceCaseSignsController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.batchUpload.serviceCaseSigns;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.*;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.sercurity.AntiVirusUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
 * REST endpoint for functions related to download excel template and upload excel template for Service Case Signs.
 *
 * @author vn70529
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + BatchUploadByServiceCaseSignsController.RELATIVE_PATH_BATCH_UPLOAD_SERVICE_CASE_SIGNS)
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_BY_SERVICE_CASE_SIGN)
public class BatchUploadByServiceCaseSignsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchUploadByServiceCaseSignsController.class);
	protected static final String RELATIVE_PATH_BATCH_UPLOAD_SERVICE_CASE_SIGNS = "/serviceCaseSigns";
	public static final String RELATIVE_PATH_BATCH_UPLOAD = "/batchUpload";
	/**
	 * Holds the path to request download template.
	 */
	private static final String DOWNLOAD_TEMPLATE = "/downloadTemplate";
	private static final String SERVICE_CASE_SIGN_FILE = "batch_upload_templete/Excel_Upload_By_ServiceCaseSign.xlsx";
	private static final String HEADER_DOWNLOAD_REQUEST = "Content-disposition";
	private static final String HEADER_ATTACHMENT_DOWNLOAD_REQUEST = "attachment; filename=Excel_Upload_By_ServiceCaseSign.xlsx";
	public static final String EXCEL_2007_FILE_TYPE = "xlsx";
	private static final int MIN_SIZE_TEMP= 0;
	private static final int MAX_SIZE_TEMP= 512;
	/**
	 * Log messages.
	 */
	private static final String DOWNLOAD_TEMPLATE_LOG_MESSAGE =
			"User %s from IP %s requested a template for service case signs.";
	private static final String DOWNLOAD_TEMPLATE_LOG_ERROR_MESSAGE =
			"User %s requested a template for service case signs: %s.";
	public static final String BATCH_UPLOAD_SERVICE_CASE_SIGNS_FILE_NAME = "Service Case Sign";
	private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Your upload request with ID %d is being processed. Would you like to check the status?";
	@Autowired
	private UserInfo userInfo;
	@Autowired
	private BatchUploadService batchUploadService;
	@Autowired
	private AntiVirusUtil antiVirusUtil;
	@Autowired
	private ServiceCaseSignValidator serviceCaseSignValidator;
	/**
	 * Endpoint function for download Service Case Signs template.
	 *
	 * @param response  instance of HttpServletResponse.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = BatchUploadByServiceCaseSignsController.DOWNLOAD_TEMPLATE, headers = "Accept=text/xlsx")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
		this.logRequest(request.getRemoteAddr());
		try {
			response.setHeader(HEADER_DOWNLOAD_REQUEST,HEADER_ATTACHMENT_DOWNLOAD_REQUEST);
			response.setContentType(EXCEL_2007_FILE_TYPE);
			// put file to InputStream
			InputStream is = new ClassPathResource(BatchUploadByServiceCaseSignsController.SERVICE_CASE_SIGN_FILE).getInputStream();
			ServletOutputStream out = response.getOutputStream();
			try {
				byte[] outputByte = new byte[MAX_SIZE_TEMP];
				int read;
				while ((read = is.read(outputByte)) > MIN_SIZE_TEMP) {
					out.write(outputByte, MIN_SIZE_TEMP, read);
				}
			} finally {
				is.close();
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * process the file upload eCommerce Attribute.
	 *
	 * @return value of process.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = BatchUploadByServiceCaseSignsController.RELATIVE_PATH_BATCH_UPLOAD)
	public BatchUploadResponse uploadServiceCaseSign(@RequestParam("file") MultipartFile file, @RequestParam("description") String desc) throws AntiVirusException,IOException {
		// check virus
		antiVirusUtil.virusCheck(file.getBytes());
		this.serviceCaseSignValidator.validateTemplate(file.getBytes());
		long trackingId = 0;
		BatchUploadRequest excelUploadBase = new BatchUploadRequest();
		excelUploadBase.setData(file.getBytes());
		excelUploadBase.setBatchUploadType(BatchUploadType.SERVICE_CASE_SIGNS);
		excelUploadBase.setUploadDescription(desc);
		excelUploadBase.setUploadFileName(BATCH_UPLOAD_SERVICE_CASE_SIGNS_FILE_NAME);
		excelUploadBase.setUserId(userInfo.getUserId());
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
		BatchUploadByServiceCaseSignsController.LOGGER.info(String.format(BatchUploadByServiceCaseSignsController.DOWNLOAD_TEMPLATE_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}
}
