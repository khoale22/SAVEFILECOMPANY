/*
 *  TexasStateD6RecordWriterImpl
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.writer.impl;

import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.index.WicSubCategoryDocument;
import com.heb.batch.wic.index.repository.TexasStateIndexRepository;
import com.heb.batch.wic.service.MasterDataService;
import com.heb.batch.wic.utils.WicUtil;
import com.heb.batch.wic.writer.TexasStateRecordWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is TexasStateD6RecordWriterImpl class for writer D6 record.
 *
 * @author vn70529
 * @since 1.0.1
 */
public class TexasStateD6RecordWriterImpl implements TexasStateRecordWriter<TexasStateDocument> {
    private static final Logger LOGGER = LogManager.getLogger(TexasStateD4RecordWriterImpl.class);
    private String fileDataParse;
    private TexasStateIndexRepository texasStateIndexRepository;

    public TexasStateD6RecordWriterImpl(TexasStateIndexRepository texasStateIndexRepository, String fileDataParse){
        this.texasStateIndexRepository = texasStateIndexRepository;
        this.fileDataParse = fileDataParse;
    }
    @Override
    public void write(List<? extends TexasStateDocument> items) throws Exception {
        if (items != null && !items.isEmpty()) {
            try {
                for (TexasStateDocument texasStateDocument : items) {
                    this.texasStateIndexRepository.save(texasStateDocument);
                }
                WicUtil.createCsvTexasStateDocument(new ArrayList<>(items), fileDataParse);
            } catch (Exception e) {
                LOGGER.error("TexasStateWriter " + e.getMessage());
            }
        }
    }
}
