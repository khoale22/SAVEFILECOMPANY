/*
 *  TexasStateD4RecordWriterImpl
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is TexasStateD4RecordWriterImpl class for writer D4 record.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class TexasStateD4RecordWriterImpl implements TexasStateRecordWriter<TexasStateDocument> {
    private static final Logger LOGGER = LogManager.getLogger(TexasStateD4RecordWriterImpl.class);
    private String fileDataParse;
    private MasterDataService masterDataService;
    private TexasStateIndexRepository texasStateIndexRepository;

    public TexasStateD4RecordWriterImpl(MasterDataService masterDataService, TexasStateIndexRepository texasStateIndexRepository, String fileDataParse){
        this.texasStateIndexRepository = texasStateIndexRepository;
        this.masterDataService = masterDataService;
        this.fileDataParse = fileDataParse;
    }
    @Override
    public void write(List<? extends TexasStateDocument> items) throws Exception {
        List<? extends TexasStateDocument> newItems = processPurchaseIndicator(items);
        if(newItems != null && !newItems.isEmpty()) {
            try {
                for(TexasStateDocument texasStateDocument : newItems) {
                    this.texasStateIndexRepository.save(texasStateDocument);
                }
                WicUtil.createCsvTexasStateDocument(new ArrayList<>(newItems), fileDataParse);
            }catch (Exception e){
                LOGGER.error("TexasStateWriter "+e.getMessage());
            }
        }
    }

    /**
     * Process purchase indicator that equals to one.
     * @param items the list of Texas items
     * @return the list of Texas items after process purchase items.
     */
    private List<? extends TexasStateDocument> processPurchaseIndicator(List<? extends TexasStateDocument> items){
        List<TexasStateDocument> newItems = new ArrayList<>();
        for (TexasStateDocument texasStateDocument: items) {
            newItems.add(texasStateDocument);
            // Check the upc is attachment broadband or not
            if(TexasStateDocument.PURCHASE_INDICATOR_ONE.equals(texasStateDocument.getPurchaseIndicator())){
                // Just only create broadband sub cat when wic sub cat is not broadband (sub cat id > 0).
                if (Integer.valueOf(texasStateDocument.getWicSubCatId()) > 0) {
                    // Get sub category description for broadband.
                    Optional<WicSubCategoryDocument> wicSubCategoryDocuments = masterDataService.getSubCategory(
                            String.valueOf(Integer.valueOf(texasStateDocument.getWicCatId())),
                            TexasStateDocument.BROADBAND_SUB_CAT_ID);
                    //if sub cat id 000 is existing in wic sub cat table by cat id and in texas file, then we will save second record for broadband.
                    if (wicSubCategoryDocuments.isPresent()) {
                        try {
                            TexasStateDocument newTexasStateDocument = texasStateDocument.clone();
                            newTexasStateDocument.setWicSubCatId(TexasStateDocument.BROADBAND_SUB_CAT_ID);
                            // Generate new id by broadband sub cat change to identify TexasStateDocument
                            newTexasStateDocument.setId(TexasStateDocument.generateId(newTexasStateDocument.getWicAplId(),
                                    newTexasStateDocument.getScnCdId(), newTexasStateDocument.getWicCatId(), newTexasStateDocument.getWicSubCatId()));
                            newTexasStateDocument.setWicSubCategoryDesc(wicSubCategoryDocuments.get().getDescription());
                            newItems.add(newTexasStateDocument);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            LOGGER.error("TexasStateWriter " + ex.getMessage());
                        }
                    }
                }
            }
        }
        return newItems;
    }
}
