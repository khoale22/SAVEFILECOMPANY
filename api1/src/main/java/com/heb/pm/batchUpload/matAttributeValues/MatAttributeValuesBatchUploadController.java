/*
 *  MatAttributeValuesBatchUploadController
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.matAttributeValues;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.BatchUploadRequest;
import com.heb.pm.batchUpload.BatchUploadResponse;
import com.heb.pm.batchUpload.BatchUploadService;
import com.heb.pm.batchUpload.BatchUploadType;
import com.heb.pm.batchUpload.earley.FileProcessingException;
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
 * This is Controller for MAT Attribute Values for Batch Upload.
 *
 * @author s573181
 * @since 2.28.0
 */

@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + MatAttributeValuesBatchUploadController.RELATIVE_PATH_BATCH_UPLOAD_MAT_ATTRIBUTE_VALUES)
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_BY_MAT_ATTRIBUTE_VALUES)
public class MatAttributeValuesBatchUploadController {


	private static final Logger logger = LoggerFactory.getLogger(MatAttributeValuesBatchUploadController.class);
	protected static final String RELATIVE_PATH_BATCH_UPLOAD_MAT_ATTRIBUTE_VALUES = "/matAttributeValues";
	public static final String RELATIVE_PATH_BATCH_UPLOAD = "/batchUpload";
	public static final String RELATIVE_PATH_BATCH_UPLOAD_DOWN_LOAD_TEMPLATE = "/downloadTemplate";
	public static final String BATCH_UPLOAD_MAT_ATTRIBUTE_VALUES_FILE_NAME = "matAttributeValues";
	private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Your upload request with ID %d is being processed. Would you like to check the status?";
	private static final String FILE_UPLOAD_MESSAGE =
			"User %s from IP %s has requested to upload file %s as an MAT attribute values upload.";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private AntiVirusUtil antiVirusUtil;

	@Autowired
	private MatAttributeValuesUploadService matAttributeValuesUploadService;

	@Autowired
	private BatchUploadService batchUploadService;

	/**
	 * Calls excel export for related products.
	 *
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value=RELATIVE_PATH_BATCH_UPLOAD_DOWN_LOAD_TEMPLATE, headers = "Accept=text/xlsx")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setHeader("Content-disposition","attachment; filename=Excel_Upload_By_MAT_Attribute_Values.xlsx");
			response.setContentType("xlsx");
			// put file to InputStream
			InputStream is = new ClassPathResource("batch_upload_templete/Excel_Upload_By_MAT_Attribute_Values.xlsx").getInputStream();
			ServletOutputStream out = response.getOutputStream();
			try {
				byte[] outputByte = new byte[512];
				int read;
				while ((read = is.read(outputByte)) > 0) {
					out.write(outputByte, 0, read);
				}
			} finally {
				is.close();
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Process the file upload .
	 *
	 * @return value of process.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = MatAttributeValuesBatchUploadController.RELATIVE_PATH_BATCH_UPLOAD)
	public BatchUploadResponse uploadMatAttributeValues(@RequestParam("file") MultipartFile file, @RequestParam("name") String desc,
													 HttpServletRequest request) throws IOException, AntiVirusException, FileProcessingException {

		logger.info(String.format(MatAttributeValuesBatchUploadController.FILE_UPLOAD_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr(), file.getName()));

		// check virus
		antiVirusUtil.virusCheck(file.getBytes());

		logger.info("File scanned for viruses");

		BatchUploadRequest batchUploadRequest = prepareRequest(file.getBytes(), desc, userInfo.getUserId());
//		long trackingId = this.batchUploadService.submit(batchUploadRequest);
		long trackingId = this.matAttributeValuesUploadService.submitMatAttributeValuesUpload(batchUploadRequest);
		return new BatchUploadResponse(trackingId,
				String.format(BATCH_UPLOAD_SUCCESS_MESSAGE, trackingId));
	}

	/**
	 * Prepare Batch Upload request for related products upload.
	 *
	 * @param file	file uploaded
	 * @param uploadDescription	batch upload description.
	 * @param userId file uploaded by user id.
	 * @return	prepared batch upload request.
	 */
	private BatchUploadRequest prepareRequest(byte[] file, String uploadDescription, String userId) {
		BatchUploadRequest batchUploadRequest = new BatchUploadRequest();
		batchUploadRequest.setData(file);
		batchUploadRequest.setBatchUploadType(BatchUploadType.RELATED_PRODUCTS);
		batchUploadRequest.setUploadDescription(uploadDescription);
		batchUploadRequest.setUploadFileName(BATCH_UPLOAD_MAT_ATTRIBUTE_VALUES_FILE_NAME);
		batchUploadRequest.setUserId(userId);
		return batchUploadRequest;
	}
}
