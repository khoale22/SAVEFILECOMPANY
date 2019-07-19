/*
 * EbmBdaCandidateWorkRequestCreator
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.ebmBda;

import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreator;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.batchUpload.parser.WorkRequestCreatorUtils;
import com.heb.pm.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The class create the candidate request for ebmBda file.
 * datas have been validate then insert to candidate work request table.
 *
 * @author vn70529
 * @since 2.33.0
 */
@Service
public class EbmBdaCandidateWorkRequestCreator extends CandidateWorkRequestCreator {

    private static final String SINGLE_SPACE = " ";
    private static final String STRING_EMPTY = "EMPTY";

    @Autowired
    private EbmBdaValidator ebmBdaValidator;

    @Override
    public CandidateWorkRequest createRequest(List<String> cellValues, List<ProductAttribute> productAttributes, long transactionId, String userId) {
        EbmBdaBatchUpload ebmBdaBatchUpload = this.createEbmBdaBatchUpload(cellValues);
        this.ebmBdaValidator.validateRow(ebmBdaBatchUpload);
        CandidateWorkRequest candidateWorkRequest =
                WorkRequestCreatorUtils.getEmptyWorkRequest(null, null, userId, transactionId,
                        CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT, getBatchStatus(ebmBdaBatchUpload).getName());
        if (!ebmBdaBatchUpload.hasErrors()) {
            setDataToCandidateClassCommodity(candidateWorkRequest, ebmBdaBatchUpload);
        }
        setDataToCandidateStatus(candidateWorkRequest, ebmBdaBatchUpload);
        return candidateWorkRequest;
    }

    /**
     * set data to CandidateClassCommodity
     *
     * @param candidateWorkRequest the CandidateWorkRequest
     * @param ebmBdaBatchUpload    the EbmBdaBatchUpload
     */
    public void setDataToCandidateClassCommodity(CandidateWorkRequest candidateWorkRequest, EbmBdaBatchUpload ebmBdaBatchUpload) {

        CandidateClassCommodity candidateClassCommodity = new CandidateClassCommodity();
        CandidateClassCommodityKey candidateClassCommodityKey = new CandidateClassCommodityKey();
        candidateClassCommodityKey.setClassCode(Integer.valueOf(ebmBdaBatchUpload.getClassCode()));
        candidateClassCommodityKey.setCommodityCode(Integer.valueOf(ebmBdaBatchUpload.getCommodityCode()));
        candidateClassCommodity.setKey(candidateClassCommodityKey);
        candidateClassCommodity.setEbmId(this.getEbmBdaId(ebmBdaBatchUpload.getEbmId()));
        candidateClassCommodity.setBdaId(this.getEbmBdaId(ebmBdaBatchUpload.getBdaId()));
        candidateClassCommodity.setCreateDate(LocalDateTime.now());
        candidateClassCommodity.setCreateUserId(candidateWorkRequest.getUserId());
        candidateClassCommodity.setLastUpdateDate(LocalDateTime.now());
        candidateClassCommodity.setLastUpdateUserId(candidateWorkRequest.getUserId());
        candidateClassCommodity.setCandidateWorkRequest(candidateWorkRequest);

        candidateWorkRequest.setCandidateClassCommodities(new ArrayList<CandidateClassCommodity>());
        candidateWorkRequest.getCandidateClassCommodities().add(candidateClassCommodity);
    }

    /**
     * get value for edm, bda.
     *
     * @param value the value of ebm, bda.
     * @return the id of ebm, bda
     */
    public String getEbmBdaId(String value) {
        //If value is null or empty then return null.
        if (StringUtils.trimToEmpty(value).equals(StringUtils.EMPTY)) return null;
        //If value is 'empty' or 'EMPTY' then return blank.
        if (StringUtils.trimToEmpty(value).toUpperCase().equals(STRING_EMPTY)) return SINGLE_SPACE;
        // If value is not null, not empty and not 'empty' then return value.
        return value.trim();
    }

    /**
     * set Data To CandidateStatus.
     *
     * @param candidateWorkRequest the CandidateWorkRequest
     * @param ebmBdaBatchUpload    the EbmBdaBatchUpload
     */
    public void setDataToCandidateStatus(CandidateWorkRequest candidateWorkRequest, EbmBdaBatchUpload ebmBdaBatchUpload) {
        String errorMessage = ebmBdaBatchUpload.hasErrors() ? ebmBdaBatchUpload.getErrors().get(0) : StringUtils.EMPTY;
        candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
        CandidateStatusKey key = new CandidateStatusKey();
        key.setStatus(this.getBatchStatus(ebmBdaBatchUpload).getName());
        key.setLastUpdateDate(LocalDateTime.now());
        CandidateStatus candidateStatus = new CandidateStatus();
        candidateStatus.setKey(key);
        candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
        candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        candidateStatus.setCommentText(errorMessage);
        candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
    }

    /**
     * Create data for EbmBdaBatchUpload.
     *
     * @param cellValues the cell value.
     * @return the EbmBdaBatchUpload
     */
    private EbmBdaBatchUpload createEbmBdaBatchUpload(List<String> cellValues) {
        EbmBdaBatchUpload eBMBDABatchUpload = new EbmBdaBatchUpload();
        String value;
        for (int j = 0; j < cellValues.size(); j++) {
            value = cellValues.get(j);
            switch (j) {
                case EbmBdaBatchUpload.COL_POS_CLASS_CODE:
                    eBMBDABatchUpload.setClassCode(value);
                    break;
                case EbmBdaBatchUpload.COL_POS_COMMODITY_CODE:
                    eBMBDABatchUpload.setCommodityCode(value);
                    break;
                case EbmBdaBatchUpload.COL_POS_EBM_CODE:
                    eBMBDABatchUpload.setEbmId(value);
                    break;
                case EbmBdaBatchUpload.COL_POS_BDA_CODE:
                    eBMBDABatchUpload.setBdaId(value);
                    break;
            }
        }
        return eBMBDABatchUpload;
    }
}
