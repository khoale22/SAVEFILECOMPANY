/*
 *  AssortmentBatchUploadController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.assortment;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.BatchUploadRequest;
import com.heb.pm.batchUpload.BatchUploadResponse;
import com.heb.pm.batchUpload.BatchUploadService;
import com.heb.pm.batchUpload.BatchUploadType;
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
 * This is Controller for assortment Batch Upload.
 *
 * @author vn55306
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + AssortmentBatchUploadController.RELATIVE_PATH_BATCH_UPLOAD_ASSORTMENT)
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_BY_ASSORTMENT)
public class AssortmentBatchUploadController {

	private static final Logger logger = LoggerFactory.getLogger(AssortmentBatchUploadController.class);
	protected static final String RELATIVE_PATH_BATCH_UPLOAD_ASSORTMENT = "/assortment";
	public static final String RELATIVE_PATH_BATCH_UPLOAD = "/batchUpload";
	public static final String RELATIVE_PATH_BATCH_UPLOAD_DOWN_LOAD_TEMPLATE = "/downloadTemplate";
	public static final String BATCH_UPLOAD_ASSORTMENT_FILE_NAME = "assortment";
	private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Your upload request with ID %d is being processed. Would you like to check the status?";
	@Autowired
	private UserInfo userInfo;
	@Autowired
	private BatchUploadService batchUploadService;
	@Autowired
	private AntiVirusUtil antiVirusUtil;
	@Autowired
	private AssortmentValidator assortmentValidator;

	/**
	 * process the file upload eCommerce Attribute.
	 *
	 * @return value of process.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = AssortmentBatchUploadController.RELATIVE_PATH_BATCH_UPLOAD)
	public BatchUploadResponse uploadAssortment(@RequestParam("file") MultipartFile file,@RequestParam("name") String desc) throws AntiVirusException,IOException {

		// check virus
		antiVirusUtil.virusCheck(file.getBytes());
		this.assortmentValidator.validateTemplate(file.getBytes());
		BatchUploadRequest batchUploadRequest = prepareRequest(file.getBytes(), desc, userInfo.getUserId());
		long trackingId = this.batchUploadService.submit(batchUploadRequest);
		BatchUploadResponse response = new BatchUploadResponse(trackingId,
				String.format(BATCH_UPLOAD_SUCCESS_MESSAGE, trackingId));
	    return response;
	}
    /**
     * Calls excel export for Class/commodity.
     *
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value=RELATIVE_PATH_BATCH_UPLOAD_DOWN_LOAD_TEMPLATE, headers = "Accept=text/xlsx")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response){
        try {
			response.setHeader("Content-disposition","attachment; filename=Excel_Upload_By_Assortment.xlsx");
            response.setContentType("xlsx");
            // put file to InputStream
            InputStream is = new ClassPathResource("batch_upload_templete/Excel_Upload_By_Assortment.xlsx").getInputStream();
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
	 * Prepare Batch Upload request for Assortment Upload.
	 * @param file	file uploaded
	 * @param uploadDescription	batch upload description.
	 * @param userId file uploaded by user id.
	 * @return	prepared batch upload request.
	 */
    private BatchUploadRequest prepareRequest(byte[] file, String uploadDescription, String userId) {
		BatchUploadRequest batchUploadRequest = new BatchUploadRequest();
		batchUploadRequest.setData(file);
		batchUploadRequest.setBatchUploadType(BatchUploadType.ASSORTMENT);
		batchUploadRequest.setUploadDescription(uploadDescription);
		batchUploadRequest.setUploadFileName(BATCH_UPLOAD_ASSORTMENT_FILE_NAME);
		batchUploadRequest.setUserId(userId);
		return batchUploadRequest;
	}
}
