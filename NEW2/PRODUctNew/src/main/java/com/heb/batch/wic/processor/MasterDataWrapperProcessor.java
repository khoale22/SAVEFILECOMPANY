/*
 * MasterDataWrapperProcessor
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */
package com.heb.batch.wic.processor;

import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.service.MasterDataService;
import com.heb.batch.wic.service.TexasStateData;
import com.heb.batch.wic.utils.MasterDataWrapper;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;


/**
 * Converts TexasStateDocument to MasterDataWrapper.
 *
 * @author vn03512
 * @since 1.0.0
 */
public class MasterDataWrapperProcessor implements ItemProcessor<TexasStateDocument, MasterDataWrapper>, StepExecutionListener {
    @Autowired
    private MasterDataService masterDataService;
    @Autowired
    private TexasStateData texasStateData;
    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.masterDataService.setWicCategoryNewKeys(new HashSet<>());
        this.masterDataService.setWicSubCategoryNewKeys(new HashSet<String>());
    	// clear any changes
        this.texasStateData.getProductScanCodeWicDocuments().clear();
        this.texasStateData.getProductScanCodeWicDocumentDeletes().clear();
        this.texasStateData.getProductScanCodeChangeWicSwitchs().clear();
        this.texasStateData.getKeyDeleteChangeDatas().clear();
    }
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
    /**
     * Called by the Spring Batch framework. It will wrap a TexasStateDocument in a MasterDataWrapper and return it.
     *
     * @param item The TexasStateDocument to wrap.
     * @return The wrapped ProductScanCodes.
     * @throws Exception
     */
    @Override
	public MasterDataWrapper process(TexasStateDocument item) throws Exception {
        return this.masterDataService.processTexasStateDocument(item);
    }
}