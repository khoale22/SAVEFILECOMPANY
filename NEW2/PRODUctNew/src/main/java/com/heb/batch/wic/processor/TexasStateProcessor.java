/*
 *  TexasStateProcessor
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.processor;

import com.heb.batch.wic.dao.JobParamDAO;
import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.processor.impl.TexasStateD4RecordProcessorImpl;
import com.heb.batch.wic.processor.impl.TexasStateD6RecordProcessorImpl;
import com.heb.batch.wic.repository.ProductScanCodesRepository;
import com.heb.batch.wic.service.EmailService;
import com.heb.batch.wic.service.TexasFieldValidator;
import com.heb.batch.wic.service.impl.TexasFieldValidatorImpl;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;


/**
 *  This is the TexasStateProcessor class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class TexasStateProcessor implements ItemProcessor<TexasStateDocument, TexasStateDocument>, StepExecutionListener {

    /**
     * Record type (D4 or D6)
     */
    private String recordType;
    @Value("${texasState.invalidData}")
    String fileDataInvalid;

    @Autowired
    private TexasFieldValidator texasFieldValidator;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private JobParamDAO jobParamDAO;
    @Autowired
    private ProductScanCodesRepository productScanCodesRepository;
    private TexasStateRecordProcessor texasStateRecordProcessor;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        if(TexasFieldValidatorImpl.FieldFormats.D6_RECORD.getFieldDescription().equals(recordType)){
            texasStateRecordProcessor = new TexasStateD6RecordProcessorImpl();
        } else {
            texasStateRecordProcessor = new TexasStateD4RecordProcessorImpl(texasFieldValidator, productScanCodesRepository, jobParamDAO,
                    emailService, messageSource, fileDataInvalid);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        texasStateRecordProcessor.afterStep();
        return null;
    }

    @Override
    public TexasStateDocument process(TexasStateDocument item) throws Exception {
        return texasStateRecordProcessor.process(item);
    }
    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
}
