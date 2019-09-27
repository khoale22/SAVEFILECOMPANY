/*
 *  BenefitUnitDescriptionWriter
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.writer;

import com.heb.batch.wic.dao.JobParamDAO;
import com.heb.batch.wic.exception.WicException;
import com.heb.batch.wic.index.WicCategoryDocument;
import com.heb.batch.wic.index.WicSubCategoryDocument;
import com.heb.batch.wic.index.repository.WicCategoryIndexRepository;
import com.heb.batch.wic.index.repository.WicSubCategoryIndexRepository;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.utils.MasterDataWrapper;
import com.heb.batch.wic.utils.WicConstants;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.webservice.WicCategoryServiceClient;
import com.heb.batch.wic.webservice.WicSubCategoryServiceClient;
import com.heb.batch.wic.webservice.vo.BaseVO;
import com.heb.batch.wic.webservice.vo.WicCategoryVO;
import com.heb.batch.wic.webservice.vo.WicSubCategoryVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * This is BenefitUnitDescriptionWriter class for writer benefit unit, wic cat and wic sub cat.
 *
 * @author vn70529
 * @since 1.0.1
 */
public class BenefitUnitDescriptionWriter implements ItemWriter<MasterDataWrapper>, StepExecutionListener {
    private static final Logger LOGGER = LogManager.getLogger(MasterDataWrapperWriter.class);
    @Autowired
    private WicCategoryServiceClient wicCategoryServiceClient;
    @Autowired
    private WicSubCategoryServiceClient wicSubCategoryServiceClient;
    @Autowired
    private WicCategoryIndexRepository wicCategoryIndexRepository;
    @Autowired
    private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;
    @Autowired
    JobParamDAO jobParamDAO;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageSource messageSource;
    private String insertWicCatDaoUrl;
    private String insertWicSubCatDaoUrl;
    private String apiKey;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.apiKey = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_APIKEY);
        this.insertWicCatDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INSERT_WIC_CAT_ORACLE_URL);
        this.insertWicSubCatDaoUrl = jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_INSERT_WIC_SUB_CAT_ORACLE_URL);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("AddUpdateWicSubCategoryWriter afterStep ");
        return null;
    }

    /**
     * Called by the Spring Batch framework to save WIC_CAT, WIC_SUB_CAT, PROD_SCN_CD_WIC, PROD_SCN_CODES to the index.
     *
     * @param masterDataWrapper The list of MasterDataWrapper to save.
     * @throws Exception
     */
    @Override
    public void write(List<? extends MasterDataWrapper> masterDataWrapper) throws Exception {
        List<BaseVO> wicCategoryList = new ArrayList<>();
        List<BaseVO> wicSubCategoryList = new ArrayList<>();
        // Get list of WIC_CAT, WIC_SUB_CAT, PROD_SCN_CODES_WIC
        for (MasterDataWrapper record : masterDataWrapper) {
            if (record.getWicCategory() != null) {
                wicCategoryList.add(record.getWicCategory());
            }
            if (record.getWicSubCategory() != null) {
                wicSubCategoryList.add(record.getWicSubCategory());
            }
        }
        // insert Wic Category
        this.insertWicCategory(wicCategoryList);
        // insert Wic Sub Category
        this.insertWicSubCategory(wicSubCategoryList);
    }

    /**
     * Insert list of Wic Category.
     *
     * @param wicCategoryList the list of Wic Category.
     * @throws WicException
     */
    private void insertWicCategory(List<BaseVO> wicCategoryList) throws WicException {
        if (!wicCategoryList.isEmpty()) {
            try {
                this.wicCategoryServiceClient.submitRequest(wicCategoryList, this.insertWicCatDaoUrl, this.apiKey);
                this.wicCategoryIndexRepository.saveAll(WicUtil.convertWicCategoryVoToWicCategoryDocument(wicCategoryList));
            } catch (WicException e) {
                this.sendProductionSupportApiCorrupt(this.insertWicCatDaoUrl);
                throw new WicException(e.getMessage());
            }
        }
    }

    /**
     * Insert list of wic sub category.
     *
     * @param wicSubCategoryList the list of wic sub category.
     * @throws WicException
     */
    private void insertWicSubCategory(List<BaseVO> wicSubCategoryList) throws WicException {
        if (!wicSubCategoryList.isEmpty()) {
            try {
                this.wicSubCategoryServiceClient.submitRequest(wicSubCategoryList, this.insertWicSubCatDaoUrl, this.apiKey);
                this.wicSubCategoryIndexRepository.saveAll(WicUtil.convertWicSubCategoryVoToWicSubCategoryDocument(wicSubCategoryList));
            } catch (WicException e) {
                this.sendProductionSupportApiCorrupt(this.insertWicSubCatDaoUrl);
                throw new WicException(e.getMessage());
            }
        }
    }

    /**
     * Send mail for production support api corrupt
     *
     * @param url the url.
     */
    private void sendProductionSupportApiCorrupt(String url) {
        String fromAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_FROM);
        String toAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT);
        String subject = messageSource.getMessage("wic.email.production.subject", null, Locale.US);
        String content = messageSource.getMessage("wic.email.production.javadao.corrupt", new Object[]{url}, Locale.US);
        this.emailService.sendProductionSupportCorruptFile(subject, content, fromAddress, toAddress);
    }
}