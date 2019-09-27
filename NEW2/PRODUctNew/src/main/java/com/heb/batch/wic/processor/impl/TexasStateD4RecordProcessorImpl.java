/*
 *  TexasStateD4RecordProcessorImpl
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.processor.impl;

import com.heb.batch.wic.dao.JobParamDAO;
import com.heb.batch.wic.entity.ProductScanCodes;
import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.processor.TexasStateProcessor;
import com.heb.batch.wic.processor.TexasStateRecordProcessor;
import com.heb.batch.wic.repository.ProductScanCodesRepository;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.service.TexasFieldValidator;
import com.heb.batch.wic.service.impl.TexasFieldValidatorImpl;
import com.heb.batch.wic.utils.WicUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
/**
 *  This is the TexasStateD4RecordProcessorImpl class for process D4 Record.
 *
 * @author vn70529
 * @since 1.0.1
 */
public class TexasStateD4RecordProcessorImpl implements TexasStateRecordProcessor {
    private static final Logger LOGGER = LogManager.getLogger(TexasStateProcessor.class);
    private List<TexasStateDocument> texasStateDocumentInCorrects;
    private String fileDataInvalid;

    private TexasFieldValidator texasFieldValidator;

    private EmailService emailService;

    private MessageSource messageSource;

    private JobParamDAO jobParamDAO;

    private ProductScanCodesRepository productScanCodesRepository;

    public TexasStateD4RecordProcessorImpl(TexasFieldValidator texasFieldValidator, ProductScanCodesRepository productScanCodesRepository, JobParamDAO jobParamDAO,
                                           EmailService emailService, MessageSource messageSource, String fileDataInvalid){
        texasStateDocumentInCorrects = new ArrayList<>();
        this.texasFieldValidator = texasFieldValidator;
        this.productScanCodesRepository = productScanCodesRepository;
        this.jobParamDAO = jobParamDAO;
        this.emailService = emailService;
        this.messageSource= messageSource;
        this.fileDataInvalid = fileDataInvalid;
    }
    @Override
    public TexasStateDocument process(TexasStateDocument item) throws Exception {
        // parse data read from file,validate data and convert to TexasStateWicVO
        TexasStateDocument itemReturn = null;
        if(item !=null && item.getIdCode()!=null &&item.getIdCode().trim().startsWith(TexasFieldValidatorImpl.FieldFormats.D4_RECORD.getFieldDescription())) {
            texasFieldValidator.validateWicDataField(item);
            if(StringUtils.isNotEmpty(item.getErrorMessage())){
                texasStateDocumentInCorrects.add(item);
            } else {
                WicUtil.correctTexasStateDocumentKey(item);
                WicUtil.setDefaultValueTexasStateDocument(item);
                // check existing in product Scan Code, if not->ignore
                Long upc = NumberUtils.toLong(item.getScnCdId());
                if(upc>0){
                    Optional<ProductScanCodes> psc = productScanCodesRepository.findById(upc);
                    if(psc.isPresent()) {
                        itemReturn = item;
                    }
                }
            }
        }
        return itemReturn;
    }

    @Override
    public void afterStep() {
        if(texasStateDocumentInCorrects!=null && !texasStateDocumentInCorrects.isEmpty()){
            try {
                File file = WicUtil.createInvalidFile(texasStateDocumentInCorrects, fileDataInvalid);
                String path = file.getPath();
                String content;
                String subject;
                String currentDay = WicUtil.getCurrentDay(WicUtil.FORMAT_DATE_SEND_EMAIL);
                subject = messageSource.getMessage("wic.email.production.subject", null, Locale.US);
                content = messageSource.getMessage("wic.email.production.file.invalid.data", new Object[] { currentDay}, Locale.US);
                String fromAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_FROM);
                String toAddress = this.jobParamDAO.getConfigurationInfor(JobParamDAO.J50X100_EMAIL_ADDRESS_PRODUCTION_SUPPORT);
                this.emailService.sendEmailReport(subject,content,path,fromAddress,toAddress);
                FileUtils.forceDeleteOnExit(file);
            }catch (IOException e){
                LOGGER.error(e.getMessage());
            }catch (Exception e1){
                LOGGER.error(e1.getMessage());
            }
        }
    }

    public List<TexasStateDocument> getTexasStateDocumentInCorrects() {
        return texasStateDocumentInCorrects;
    }

    public void setTexasStateDocumentInCorrects(List<TexasStateDocument> texasStateDocumentInCorrects) {
        this.texasStateDocumentInCorrects = texasStateDocumentInCorrects;
    }
}
