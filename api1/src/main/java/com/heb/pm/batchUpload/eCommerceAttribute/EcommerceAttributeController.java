/*
 *  eCommerceAttributeController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.eCommerceAttribute;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.BatchUploadRequest;
import com.heb.pm.batchUpload.BatchUploadResponse;
import com.heb.pm.batchUpload.BatchUploadService;
import com.heb.pm.batchUpload.BatchUploadType;
import com.heb.pm.batchUpload.util.Constants;
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
 * batch upload file.
 *
 * @author vn87351
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + EcommerceAttributeController.RELATIVE_PATH_BATCH_UPLOAD_ECCOMMERCE_ATTRIBUTE)
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_BY_eCOMMERCE_ATTRIBUTE)
public class EcommerceAttributeController {
	//log messages
	private static final String MESSAGE_BY_METHOD = "User %s from IP %s requested %s";
	private static final Logger logger = LoggerFactory.getLogger(EcommerceAttributeController.class);

	protected static final String RELATIVE_PATH_BATCH_UPLOAD_ECCOMMERCE_ATTRIBUTE = "/eCommerceAttribute";
	private static final String RELATIVE_PATH_BATCH_UPLOAD_DOWN_LOAD_TEMPLATE = "/downloadTemplate";
	protected static final String RELATIVE_PATH_BATCH_UPLOAD = "/batchUpload";
	private static final int MIN_SIZE_TEMP= 0;
	private static final int MAX_SIZE_TEMP= 512;
	private static final String eCCOMMERCE_ATTRIBUTE_FILE = "batch_upload_templete/Excel_Upload_By_BigFive.xlsx";
	private static final String HEADER_DOWNLOAD_REQUEST = "Content-disposition";
	private static final String HEADER_ATTACHMENT_DOWNLOAD_REQUEST = "attachment; filename=Excel_Upload_By_BigFive.xlsx";
	private static final String ACCEPT_HEADER = "Accept=text/xlsx";
	private static final String DOWNLOAD_eCOMMERCE_ATTRIBUTE_TEMPLATE = "download eCommerce Attribute template file.";
	private static final String UPLOAD_eCOMMERCE_ATTRIBUTE = "upload eCommerce Attribute file with file name: %s.";
	private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "eCommerce Atribute batch upload submitted with tracking ID %d";
	public static final String BATCH_UPLOAD_ASSORTMENT_FILE_NAME = "Big Five";
	@Autowired
	private UserInfo userInfo;
	@Autowired
	private BatchUploadService batchUploadService;
	@Autowired
	private AntiVirusUtil antiVirusUtil;
	@Autowired
	private EcommerceAttributeValidator ecommerceAttributeValidator;

	/**
	 * Calls excel export for Class/commodity.
	 *
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value=RELATIVE_PATH_BATCH_UPLOAD_DOWN_LOAD_TEMPLATE, headers = ACCEPT_HEADER)
	public void downloadTemplate(HttpServletRequest request,HttpServletResponse response){
		this.showLogFromMethodRequest(request.getRemoteAddr(), EcommerceAttributeController.DOWNLOAD_eCOMMERCE_ATTRIBUTE_TEMPLATE);
		try {
			response.setHeader(HEADER_DOWNLOAD_REQUEST,HEADER_ATTACHMENT_DOWNLOAD_REQUEST);
			response.setContentType(Constants.EXCEL_2007_FILE_TYPE);
			// put file to InputStream
			InputStream is = new ClassPathResource(EcommerceAttributeController.eCCOMMERCE_ATTRIBUTE_FILE).getInputStream();
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
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * process the file upload eCommerce Attribute.
	 *	@param file MultipartFile the file upload
	 * 	@param desc the description
	 * 	@return value of process.
	 * 	@author vn87351
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = EcommerceAttributeController.RELATIVE_PATH_BATCH_UPLOAD)
	public BatchUploadResponse processeCommerceAttribute(@RequestParam("file") MultipartFile file,
														 @RequestParam("name") String desc,
														 HttpServletRequest request) throws Exception{
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(EcommerceAttributeController.UPLOAD_eCOMMERCE_ATTRIBUTE,file.getOriginalFilename()));
		antiVirusUtil.virusCheck(file.getBytes());
		this.ecommerceAttributeValidator.validateTemplate(file.getBytes());
		long trackingId = 0;
		BatchUploadRequest excelUploadBase = new BatchUploadRequest();
		excelUploadBase.setData(file.getBytes());
		excelUploadBase.setBatchUploadType(BatchUploadType.BIG_5);
		excelUploadBase.setUploadDescription(desc);
		excelUploadBase.setUploadFileName(BATCH_UPLOAD_ASSORTMENT_FILE_NAME);
		excelUploadBase.setUserId(userInfo.getUserId());
		excelUploadBase.setProductAttributes(this.ecommerceAttributeValidator.getProductAttributes());
		trackingId = this.batchUploadService.submit(excelUploadBase);
		return new BatchUploadResponse(trackingId,BATCH_UPLOAD_SUCCESS_MESSAGE);
	}
	/**
	 * Logs a users request for eCommerce Attribute upload.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @messageFromMethod messageFromMethod The detail message sent from method.
	 */
	private void showLogFromMethodRequest(String ipAddress, String messageFromMethod) {
		EcommerceAttributeController.logger.info(String.format(EcommerceAttributeController.MESSAGE_BY_METHOD, this.userInfo
				.getUserId(), ipAddress, messageFromMethod));
	}

}
