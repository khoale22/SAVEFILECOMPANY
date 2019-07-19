/*
 * BatchUploadByEbmBdaController
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.ebmBda;

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
 * REST endpoint for functions related to download excel template and upload excel template for EBM_BDA .
 *
 * @author vn70529
 * @since 2.33.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
        BatchUploadByEbmBdaController.BATCH_UPLOAD_BY_EBM_BDA)
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_BY_EBM_BDA)
public class BatchUploadByEbmBdaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchUploadByEbmBdaController.class);

    protected static final String BATCH_UPLOAD_BY_EBM_BDA = "/ebmBda";
    /**
     * Holds the path to request download template.
     */
    private static final String DOWNLOAD_TEMPLATE = "/downloadTemplate";
    private static final String HEADER_DOWNLOAD_REQUEST = "Content-disposition";
    private static final String HEADER_ATTACHMENT_DOWNLOAD_REQUEST = "attachment; filename=Excel_Upload_By_eBM_BDA.xlsx";
    private static final String RELATIVE_PATH_UPLOAD_EBM_BDA = "/batchUpload";
    private static final String EBM_BDA_TEMPLATE_NAME = "batch_upload_templete/Excel_Upload_By_eBM_BDA.xlsx";
    private static final int MIN_SIZE_TEMP = 0;
    private static final int MAX_SIZE_TEMP = 512;

    /**
     * file name for eBM_BDA
     */
    public static final String EBM_BDA_FILE_NAME = "EBM_BDA";

    // Log messages.
    private static final String DOWNLOAD_TEMPLATE_LOG_MESSAGE =
            "User %s from IP %s requested a template for EBM_BDA.";
    private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "EBM_BDA upload submitted with tracking ID %d";
    private static final String UPLOAD_FILE_EBM_BDA_LOG_MESSAGE =
            "User %s from IP %s requested to upload EBM_BDA file.";

    @Autowired
    private UserInfo userInfo;
    @Autowired
    private AntiVirusUtil antiVirusUtil;
    @Autowired
    private BatchUploadService batchUploadService;
    @Autowired
    private EbmBdaValidator ebmBdaValidator;

    /**
     * Download batch upload EBM_BDA template
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = DOWNLOAD_TEMPLATE)
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        BatchUploadByEbmBdaController.LOGGER.info(String.format(DOWNLOAD_TEMPLATE_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        // Get the input stream of EBM_BDA template.
        try {
            response.setHeader(HEADER_DOWNLOAD_REQUEST, HEADER_ATTACHMENT_DOWNLOAD_REQUEST);
            response.setContentType(Constants.DOWNLOAD_TEMPLATE_CONTENT_TYPE_EXCEL);
            // put file to InputStream
            InputStream is = new ClassPathResource(EBM_BDA_TEMPLATE_NAME).getInputStream();
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
     * Upload data EBM_BDA.
     *
     * @param request     the HttpServletRequest
     * @param file        the file upload
     * @param description the description batch upload
     * @return the BatchUploadResponse
     * @throws AntiVirusException
     * @throws IOException
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = RELATIVE_PATH_UPLOAD_EBM_BDA)
    public BatchUploadResponse uploadEbmBda(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                            @RequestParam("description") String description) throws AntiVirusException, IOException {
        BatchUploadByEbmBdaController.LOGGER.info(String.format(BatchUploadByEbmBdaController.UPLOAD_FILE_EBM_BDA_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        // check virus
        antiVirusUtil.virusCheck(file.getBytes());
        this.ebmBdaValidator.validateTemplate(file.getBytes());
        BatchUploadRequest excelUploadBase = prepareRequest(file.getBytes(), description, userInfo.getUserId());
        long trackingId = this.batchUploadService.submit(excelUploadBase);
        BatchUploadResponse response = new BatchUploadResponse(trackingId,
                String.format(BATCH_UPLOAD_SUCCESS_MESSAGE, trackingId));
        return response;
    }

    /**
     * Prepare Batch Upload request for EBM_BDA Upload.
     *
     * @param file              file uploaded
     * @param uploadDescription batch upload description.
     * @param userId            file uploaded by user id.
     * @return prepared batch upload request.
     */
    private BatchUploadRequest prepareRequest(byte[] file, String uploadDescription, String userId) {
        BatchUploadRequest batchUploadRequest = new BatchUploadRequest();
        batchUploadRequest.setData(file);
        batchUploadRequest.setBatchUploadType(BatchUploadType.EBM_BDA);
        batchUploadRequest.setUploadDescription(uploadDescription);
        batchUploadRequest.setUploadFileName(EBM_BDA_FILE_NAME);
        batchUploadRequest.setUserId(userId);
        return batchUploadRequest;
    }
}
