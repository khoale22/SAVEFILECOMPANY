/*
 *  TexasStateRecordProcessor
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.processor;

import com.heb.batch.wic.index.TexasStateDocument;
/**
 *  This is the adapter class for process body record.
 *
 * @author vn70529
 * @since 1.0.1
 */
public interface TexasStateRecordProcessor {
    /**
     * Process Texas record;
     * @param item the Texas record item.
     * @return the Texas record item.
     * @throws Exception
     */
    TexasStateDocument process(TexasStateDocument item) throws Exception;

    /**
     * What the code need to handle after step.
     */
    void afterStep();
}
