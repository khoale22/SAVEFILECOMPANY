/*
 * DeleteScanCodesWICAndUpdateScanCodesWriter class.
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.writer;

import com.heb.batch.wic.dao.JobParamDAO;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import com.heb.batch.wic.index.repository.ProductScanCodeWicIndexRepository;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.service.MasterDataService;
import com.heb.batch.wic.service.TexasStateData;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.webservice.ProductScanCodeServiceClient;
import com.heb.batch.wic.webservice.ProductScanCodeWicServiceClient;
import com.heb.batch.wic.webservice.vo.BaseVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Delete all data from PROD_SCN_CD_WIC and update data in PROD_SCN_CODES by UPC
 *
 * @author vn03500
 * @since 1.0.0
 */
public class DeleteScanCodesWICAndUpdateScanCodesWriter implements ItemWriter<ProductScanCodeWicDocument>, StepExecutionListener {
    private static final Logger LOGGER = LogManager.getLogger(DeleteScanCodesWICAndUpdateScanCodesWriter.class);
    @Autowired
    private ProductScanCodeWicServiceClient productScanCodeWicServiceClient;
    @Autowired
    private ProductScanCodeServiceClient productScanCodeServiceClient;
    @Autowired
    private MasterDataService masterDataService;
    @Autowired
    private ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository;
    @Autowired
    private JobParamDAO jobParamDAO;
    @Autowired
    private TexasStateData texasStateData;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private EmailService emailService;
    private String apiKey;
    private String deleteProductScanCodeWicDaoUrl;
    private String updateProductScanCodeDaoUrl;
    private List<BaseVO> productScanCodesList;
    private String apiError;

    /**
     * Called by the Spring Batch framework to delete and update data
     *
     * @param productScanCodeWicList The list of ProductScanCodes to delete and update.
     * @throws Exception
     */
    @Override
    public void write(List<? extends ProductScanCodeWicDocument> productScanCodeWicList) throws Exception {
        if (productScanCodeWicList != null && !productScanCodeWicList.isEmpty()) {
            texasStateData.getProductScanCodeWicDocumentDeletes().addAll(productScanCodeWicList);
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.productScanCodesList = new ArrayList<>();
        this.apiKey = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_APIKEY);
        this.updateProductScanCodeDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_UPDATE_PRODUCT_SCAN_CODE_ORACLE_URL);
        this.deleteProductScanCodeWicDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_DELETE_PRODUCT_SCAN_CODE_WIC_ORACLE_URL);
        this.apiError = StringUtils.EMPTY;

    }

    /**
     * Give a listener a chance to modify the exit status from a step. The value
     * returned will be combined with the normal exit status using
     * {@link ExitStatus#and(ExitStatus)}.
     * <p>
     * Called after execution of step's processing logic (both successful or
     * failed). Throwing exception in this method has no effect, it will only be
     * logged.
     *
     * @param stepExecution {@link StepExecution} instance.
     * @return an {@link ExitStatus} to combine with the normal value. Return
     * null to leave the old value unchanged.
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        this.afterStep1();
        if (StringUtils.isEmpty(apiError)) {
            this.afterStep2();
        } else {
            String subject;
            String content;
            String fromAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_FROM);
            String toAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT);
            subject = messageSource.getMessage("wic.email.production.subject", null, Locale.US);
            content = messageSource.getMessage("wic.email.production.javadao.corrupt", new Object[]{apiError}, Locale.US);
            this.emailService.sendProductionSupportCorruptFile(subject, content, fromAddress, toAddress);
        }
        return null;
    }

    private void afterStep1() {
        if (!texasStateData.getProductScanCodeWicDocumentDeletes().isEmpty()) {
            try {
                List<BaseVO> productScanCodeWicVOs = WicUtil.convertProductScanCodeWicToProductScanCodeWicVOtoDelete(texasStateData.getProductScanCodeWicDocumentDeletes());
                if (productScanCodeWicVOs != null && !productScanCodeWicVOs.isEmpty()) {
                    this.productScanCodeWicServiceClient.submitRequest(productScanCodeWicVOs, deleteProductScanCodeWicDaoUrl, this.apiKey);
                    this.deleteAllProdScn();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                apiError = deleteProductScanCodeWicDaoUrl;
            }
        }
        List<ProductScanCodeWicDocument> productScanCodeUpdateWicSwitchs = new ArrayList<>();
        productScanCodeUpdateWicSwitchs.addAll(texasStateData.getProductScanCodeWicDocumentDeletes());
        productScanCodeUpdateWicSwitchs.addAll(texasStateData.getProductScanCodeChangeWicSwitchs());
        if (!productScanCodeUpdateWicSwitchs.isEmpty()) {
            productScanCodesList = this.masterDataService.getProductScanCodeUpdateWicSwitch(productScanCodeUpdateWicSwitchs);
            if (!productScanCodesList.isEmpty()) {
                LOGGER.info("CHANGE WIC_SW IN PROD_SCN_CODES = " + productScanCodesList.size());
                this.updateWicFlag(productScanCodesList);
            }
        }
    }

    private void deleteAllProdScn() {
        try {
            this.productScanCodeWicIndexRepository.deleteAll(texasStateData.getProductScanCodeWicDocumentDeletes());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void updateWicFlag(List<BaseVO> productScanCodesList) {
        List<BaseVO> productScanCodeSends;
        try {
            // update Wic Flag
            for (BaseVO baseVO : productScanCodesList) {
                productScanCodeSends = new ArrayList<>();
                productScanCodeSends.add(baseVO);
                this.productScanCodeServiceClient.submitRequest(productScanCodeSends, this.updateProductScanCodeDaoUrl, this.apiKey);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            apiError = updateProductScanCodeDaoUrl;
        }
    }

    private void afterStep2() {
        if (!texasStateData.getProductScanCodeWicDocuments().isEmpty() || !texasStateData.getProductScanCodeWicDocumentDeletes().isEmpty()) {
            LOGGER.info("SEND EMAIL DATA CHANGE BETWEEN WICTX.TXT AND PROD_SCN_CD_WIC");
            try {
                List<ProductScanCodeWicDocument> dataChanges = new ArrayList<>();
                dataChanges.addAll(texasStateData.getProductScanCodeWicDocuments().values());
                dataChanges.addAll(texasStateData.getProductScanCodeWicDocumentDeletes());
                if (!dataChanges.isEmpty()) {
                    String fromAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_FROM);
                    String toAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_ADDRESS_DATA_CHANGE);
                    File file = WicUtil.createPosFile(dataChanges, WicConstants.FILE_DATA_REPORT);
                    String path = file.getPath();
                    String content;
                    String subject;
                    subject = messageSource.getMessage("wic.email.production.subject", null, Locale.US);
                    content = messageSource.getMessage("wic.email.production.file.pos", null, Locale.US);
                    this.emailService.sendEmailReport(subject, content, path, fromAddress, toAddress);
                    FileUtils.forceDeleteOnExit(file);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            LOGGER.info("NO DATA CHANGE BETWEEN WICTX.TXT AND PROD_SCN_CD_WIC");
        }
        // delete file after process
        String fileName = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INPUT_NAS);
        if (StringUtils.isNotEmpty(fileName) && StringUtils.isEmpty(apiError)) {
            File file = new File(fileName);
            try {
                FileUtils.forceDeleteOnExit(file);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        LOGGER.info("Job: [FlowJob: [name=J50X100D]] completed with the following status: [COMPLETED]");
    }
}
