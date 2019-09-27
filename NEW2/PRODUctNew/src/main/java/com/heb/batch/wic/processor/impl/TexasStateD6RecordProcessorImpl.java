/*
 *  TexasStateD6RecordProcessorImpl
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.processor.impl;

import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.processor.TexasStateRecordProcessor;
import com.heb.batch.wic.service.impl.TexasFieldValidatorImpl;
import com.heb.batch.wic.utils.WicUtil;

/**
 *  This is the TexasStateD6RecordProcessorImpl class for process D6 Record.
 *
 * @author vn70529
 * @since 1.0.1
 */
public class TexasStateD6RecordProcessorImpl implements TexasStateRecordProcessor {
    @Override
    public TexasStateDocument process(TexasStateDocument item) {
        // Process D6
        if (item != null && item.getIdCode() != null &&
                item.getIdCode().trim().startsWith(TexasFieldValidatorImpl.FieldFormats.D6_RECORD.getFieldDescription())) {
            WicUtil.correctTexasStateDocumentKeyForD6(item);
            WicUtil.setDefaultValueTexasStateDocument(item);
            return item;
        }
        return null;
    }

    @Override
    public void afterStep() {

    }
}
