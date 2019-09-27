/*
 *  TexasStateWriter
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.writer;

import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.index.repository.TexasStateIndexRepository;
import com.heb.batch.wic.service.MasterDataService;
import com.heb.batch.wic.service.impl.TexasFieldValidatorImpl;
import com.heb.batch.wic.writer.impl.TexasStateD4RecordWriterImpl;
import com.heb.batch.wic.writer.impl.TexasStateD6RecordWriterImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * This is TexasStateWriter class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class TexasStateWriter implements ItemWriter<TexasStateDocument>,StepExecutionListener {
    private static final Logger LOGGER = LogManager.getLogger(TexasStateWriter.class);
    @Value("${texasState.dataparse}")
    String fileDataParse;
    @Autowired
    private TexasStateIndexRepository texasStateIndexRepository;
    @Autowired
    private MasterDataService masterDataService;
    private TexasStateRecordWriter<TexasStateDocument> texasStateRecordWriter;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        if(TexasFieldValidatorImpl.FieldFormats.D6_RECORD.getFieldDescription().equals(recordType)) {
            texasStateRecordWriter = new TexasStateD6RecordWriterImpl(texasStateIndexRepository, fileDataParse);
        }else{
            texasStateRecordWriter = new TexasStateD4RecordWriterImpl( masterDataService, texasStateIndexRepository, fileDataParse);
        }
    }
    /**
     * Record type (D4 or D6)
     */
    private String recordType;
    @Override
    public void write(List<? extends TexasStateDocument> items) throws Exception {
         texasStateRecordWriter.write(items);
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
