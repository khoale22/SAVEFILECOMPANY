/*
 *  CategorySpecificControler
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.categorySpecific;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.*;
import com.heb.pm.batchUpload.parser.ExcelParser;
import com.heb.pm.batchUpload.util.Constants;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.sercurity.AntiVirusUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is Controller for Category Specific Batch Upload.
 *
 * @author vn55306
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + BatchUploadByCategorySpecificAttributesController.BATCH_UPLOAD_BY_CATEGORY_SPECIFIC_ATTRIBUTES_RULES_URL)
@AuthorizedResource(ResourceConstants.BATCH_UPLOAD_BY_CATEGORY_SPECIFIC_ATTRIBUTES)
public class BatchUploadByCategorySpecificAttributesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchUploadByCategorySpecificAttributesController.class);
    protected static final String BATCH_UPLOAD_BY_CATEGORY_SPECIFIC_ATTRIBUTES_RULES_URL = "/categorySpecific";
    public static final String RELATIVE_PATH_BATCH_UPLOAD = "/batchUpload";
    /**
     * Holds the path to request download template.
     */
    private static final String GENERATE_TEMPLATE = "/generateTemplate/{excelTemplateId}";
    /**
     * Log message
     */
    private static final String DOWNLOAD_TEMPLATE_LOG_MESSAGE =
            "User %s from IP %s requested %s template for category specific attributtes.";
    private static final String DOWNLOAD_TEMPLATE_ERROR_LOG_MESSAGE =
            "User %s from IP %s requested %s template for category specific attributtes:%s";
    private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Category Specific batch upload submitted with tracking ID %d";
    private static final String BATCH_UPLOAD_CATEGORY_SPECIFIC_WINE = "5";
    private static final String DEFAUL_WINE_FILE_NAME = "Wine";
    @Autowired
    private UserInfo userInfo;
    @Autowired
    private BatchUploadByCategorySpecificAttributesService batchUploadByCategorySpecificAttributesService;
    @Autowired
    private BatchUploadService batchUploadService;
    @Autowired
    private AntiVirusUtil antiVirusUtil;
    @Autowired
    private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;
    @Autowired
    private WineValidator wineValidator;

    /**
     * Endpoint function for generate template.
     *
     * @param excelTemplateId the id of template that want to download.
     * @param request         instance of HttpServletRequest.
     * @param response        instance of HttpServletResponse.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = BatchUploadByCategorySpecificAttributesController.GENERATE_TEMPLATE)
    public void generateTemplate(@PathVariable Integer excelTemplateId, HttpServletRequest request, HttpServletResponse response) {
        // Get the name of template file by id
        final String templateName = batchUploadByCategorySpecificAttributesService.getExcelTemplateNameById(excelTemplateId);
        // Log request
        BatchUploadByCategorySpecificAttributesController.LOGGER.info(String.format(BatchUploadByCategorySpecificAttributesController.DOWNLOAD_TEMPLATE_LOG_MESSAGE,
                this.userInfo.getUserId(), templateName, request.getRemoteAddr()));
        // Get the input stream of template by template name
        InputStream is = batchUploadByCategorySpecificAttributesService.getExcelTemplateByName(templateName);
        ServletOutputStream out = null;
        try {
            response.setHeader("Content-disposition", String.format("attachment; filename=%s", templateName));
            response.setContentType(Constants.DOWNLOAD_TEMPLATE_CONTENT_TYPE_EXCEL);
            out = response.getOutputStream();
            IOUtils.copy(is, out);
            out.flush();
        } catch (IOException e) {
            BatchUploadByCategorySpecificAttributesController.LOGGER.info(String.format(BatchUploadByCategorySpecificAttributesController.DOWNLOAD_TEMPLATE_ERROR_LOG_MESSAGE,
                    this.userInfo.getUserId(), templateName, request.getRemoteAddr(), e.getMessage()));
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
                BatchUploadByCategorySpecificAttributesController.LOGGER.info(String.format(BatchUploadByCategorySpecificAttributesController.DOWNLOAD_TEMPLATE_ERROR_LOG_MESSAGE,
                        this.userInfo.getUserId(), templateName, request.getRemoteAddr(), ioe.getMessage()));
            }
        }
    }
    /**
     * process the file upload eCommerce Attribute.
     *
     * @return value of process.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.POST, value = BatchUploadByCategorySpecificAttributesController.RELATIVE_PATH_BATCH_UPLOAD)
    public BatchUploadResponse uploadCategorySpecific(@RequestParam("file") MultipartFile file, @RequestParam("name") String desc,
													  @RequestParam("fileType") String fileType) throws AntiVirusException,IOException {
        LOGGER.info("uploadCategorySpecific fileType="+fileType);
        // check virus
        antiVirusUtil.virusCheck(file.getBytes());
        BatchUploadResponse response = null;
        long trackingId = 0;
        String categorySpecificName = StringUtils.EMPTY;
        if(BATCH_UPLOAD_CATEGORY_SPECIFIC_WINE.equalsIgnoreCase(fileType)){
            this.wineValidator.validateTemplate(file.getBytes());
            categorySpecificName = wineValidator.getCategorySpecificName(file.getBytes(),BatchUploadType.WINE);
            BatchUploadRequest excelUploadBase = new BatchUploadRequest();
            excelUploadBase.setData(file.getBytes());
            excelUploadBase.setBatchUploadType(BatchUploadType.WINE);
            excelUploadBase.setUploadDescription(desc);
            excelUploadBase.setUploadFileName(DEFAUL_WINE_FILE_NAME);
            excelUploadBase.setUserId(userInfo.getUserId());
            trackingId = this.batchUploadService.submit(excelUploadBase);
        } else {
            categorySpecificName = wineValidator.getCategorySpecificName(file.getBytes(),BatchUploadType.CATEGORY_SPECIFIC);
            trackingId = this.productAttributeManagementServiceClient.updateExtAttributeBatchUpload(categorySpecificName,desc,file.getBytes(),userInfo.getUserId());
        }
       response = new BatchUploadResponse(trackingId,
                String.format(BATCH_UPLOAD_SUCCESS_MESSAGE, trackingId));
        return response;
    }
}
