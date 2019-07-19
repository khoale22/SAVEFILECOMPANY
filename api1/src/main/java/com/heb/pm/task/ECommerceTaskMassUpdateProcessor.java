/*
 * ECommerceTaskMassUpdateProcessor
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ECommerceTaskMassUpdateProcessor works through every CandidateWorkRequest sent by reader. It basically does the preparation for
 * writing to the data store.
 *
 * @author vn70529
 * @since 2.17.0
 */
public class ECommerceTaskMassUpdateProcessor implements ItemProcessor<CandidateWorkRequest, CandidateWorkRequest> {
    /**
     * Job tracking id.
     */
    @Value("#{jobParameters['transactionId']}")
    private Long transactionId;
    /**
     * Used who submitted the mass update.
     */
    @Value("#{jobParameters['userId']}")
    private String userId;
    /**
     * Type code
     */
    @Value("#{jobParameters['actionType']}")
    private String actionType;

    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is null, it is assumed that processing of the item
     * should not continue.
     *
     * @param selectedCandidateWorkRequest item to be processed
     * @return potentially modified or new item for continued processing, null if processing of the
     * provided item should not continue.
     * @throws Exception
     */
    @Override
    public CandidateWorkRequest process(CandidateWorkRequest selectedCandidateWorkRequest) {
        if(selectedCandidateWorkRequest != null){
            CandidateWorkRequest workRequest = null;
            if(actionType!=null && actionType.equals(String.valueOf(MassUpdateTaskRequest.ACTION_TYPE_PUBLISH_PRODUCT))){
                /* Publish ecommerce view*/
                workRequest = getEmptyWorkRequest(selectedCandidateWorkRequest, CandidateWorkRequest.INTNT_ID_PUBLISH);
                setDataToCandidateStatus(workRequest, CandidateStatusKey.StatusCode.BATCH_UPLOAD, StringUtils.EMPTY);
                setDataToCandidateProductMaster(workRequest);
            } else{
                /* Mass fill update*/
                workRequest = getEmptyWorkRequest(selectedCandidateWorkRequest, CandidateWorkRequest.INTNT_ID_DEFAULT);
                setDataToCandidateStatus(workRequest, CandidateStatusKey.StatusCode.BATCH_UPLOAD, StringUtils.EMPTY);
                CandidateProductMaster candidateProductMaster = setDataToCandidateProductMaster(workRequest);
                setDataToCandidateMasterDataExtensionAttribute(workRequest);
                setDataToFulfillmentChannel(workRequest, selectedCandidateWorkRequest.getCandidateFulfillmentChannels(), candidateProductMaster);
                selectedCandidateWorkRequest.setCandidateFulfillmentChannels(null);
            }
            return workRequest;
        }
        return null;
    }

    /**
     * Get empty CandidateWorkRequest.
     *
     * @param selectedCandidateWorkRequest the selectedCandidateWorkRequest.
     * @return the CandidateWorkRequest.
     */
    private CandidateWorkRequest getEmptyWorkRequest(CandidateWorkRequest selectedCandidateWorkRequest, int intent) {
        CandidateWorkRequest workRequest = new CandidateWorkRequest();
        workRequest.setProductId(selectedCandidateWorkRequest.getProductId());
        workRequest.setCreateDate(LocalDateTime.now());
        workRequest.setUserId(userId);
        workRequest.setLastUpdateDate(LocalDateTime.now());
        workRequest.setReadyToActivate(CandidateWorkRequest.RDY_TO_ACTVD_SW_DEFAULT);
        workRequest.setStatus(CandidateWorkRequest.PD_SETUP_STAT_CD_BATCH_UPLOAD);
        workRequest.setIntent(intent);
        workRequest.setSourceSystem(CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT);
        workRequest.setLastUpdateUserId(userId);
        workRequest.setTrackingId(transactionId);
        workRequest.setStatusChangeReason(CandidateWorkRequest.STAT_CHG_RSN_ID_WRKG);
        workRequest.setPdpTemplate(selectedCandidateWorkRequest.getPdpTemplate());
        return workRequest;
    }

    /**
     * Set data to CandidateStatus.
     *
     * @param candidateWorkRequest the candidateWorkRequest.
     * @param statusCode the status code.
     */
    private void setDataToCandidateStatus(CandidateWorkRequest candidateWorkRequest, CandidateStatusKey.StatusCode statusCode, String errorMessage){
        CandidateStatus candidateStatus = new CandidateStatus();
        CandidateStatusKey candidateStatKey = new CandidateStatusKey();
        candidateStatKey.setStatus(statusCode.getName());
        candidateStatKey.setLastUpdateDate(LocalDateTime.now());
        candidateStatus.setKey(candidateStatKey);
        candidateStatus.setUpdateUserId(userId);
        candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        candidateStatus.setCommentText(errorMessage);
        candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
    }
    /**
     * Set Data To CandidateProductMaster.
     *
     * @param candidateWorkRequest the CandidateWorkRequest.
     * @return the CandidateProductMaster.
     */
    private CandidateProductMaster setDataToCandidateProductMaster(CandidateWorkRequest candidateWorkRequest) {
        CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
        candidateProductMaster.setLstUpdtUsrId(userId);
        candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
        candidateProductMaster.setNewDataSw(false);
        candidateProductMaster.setPackagingText(CandidateProductMaster.STRING_DEFAULT_BLANK);
        candidateProductMaster.setPrprcForNbr(0);
        candidateProductMaster.setWorkRequestId(candidateWorkRequest.getWorkRequestId());
        candidateProductMaster.setCriticalItem(CandidateProductMaster.STRING_DEFAULT_BLANK);
        candidateProductMaster.setProductId(candidateWorkRequest.getProductId());
        candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.setCandidateProductMaster(new ArrayList<>());
        candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);
        return candidateProductMaster;
    }

    /**
     * Set Data To CandidateMasterDataExtensionAttribute.
     *
     * @param candidateWorkRequest the candidateWorkRequest.
     */
    private void setDataToCandidateMasterDataExtensionAttribute(CandidateWorkRequest candidateWorkRequest) {
        if(candidateWorkRequest.getPdpTemplate() != null && StringUtils.isNotBlank(candidateWorkRequest.getPdpTemplate().getDescription())&& StringUtils.isNotBlank(candidateWorkRequest.getPdpTemplate().getId())) {
            candidateWorkRequest.setCandidateMasterDataExtensionAttributes(new ArrayList<>());
            CandidateMasterDataExtensionAttribute candidateMasterDataExtensionAttribute = new CandidateMasterDataExtensionAttribute();
            CandidateMasterDataExtensionAttributeKey candidateMasterDataExtensionAttributeKey = new CandidateMasterDataExtensionAttributeKey();
            candidateMasterDataExtensionAttributeKey.setWorkRequestId(candidateWorkRequest.getWorkRequestId());
            candidateMasterDataExtensionAttributeKey.setAttributeId(515l);
            candidateMasterDataExtensionAttributeKey.setKeyId(candidateWorkRequest.getProductId());
            candidateMasterDataExtensionAttributeKey.setItemProductKey(CandidateMasterDataExtensionAttributeKey.PROD);
            candidateMasterDataExtensionAttributeKey.setSequenceNumber(1L);
            candidateMasterDataExtensionAttributeKey.setDataSourceSystem(Long.valueOf(CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT));
            candidateMasterDataExtensionAttribute.setAttributeCode(0);
            candidateMasterDataExtensionAttribute.setKey(candidateMasterDataExtensionAttributeKey);
            // PDP template Description
            candidateMasterDataExtensionAttribute.setAttributeValueText(candidateWorkRequest.getPdpTemplate().getDescription());
            candidateMasterDataExtensionAttribute.setAttributeValueDate(LocalDate.now());
            candidateMasterDataExtensionAttribute.setCreateDate(LocalDateTime.now());
            candidateMasterDataExtensionAttribute.setLastUpdateDate(LocalDateTime.now());
            candidateMasterDataExtensionAttribute.setCreateUserId(userId);
            candidateMasterDataExtensionAttribute.setLastUpdateUserId(userId);
            candidateMasterDataExtensionAttribute.setNewData(" ");
            candidateMasterDataExtensionAttribute.setCandidateWorkRequest(candidateWorkRequest);
            candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(candidateMasterDataExtensionAttribute);
        }
    }

    /**
     * Set data to FulfillmentChannel.
     *
     * @param candidateWorkRequest the candidateWorkRequest.
     * @param candidateProductMaster the candidateProductMaster.
     */
    private void setDataToFulfillmentChannel(CandidateWorkRequest candidateWorkRequest, List<CandidateFulfillmentChannel> candidateFulfillmentChannels, CandidateProductMaster candidateProductMaster) {
        if(candidateFulfillmentChannels != null){
            candidateProductMaster.setCandidateFulfillmentChannels(new ArrayList<>());
            for (CandidateFulfillmentChannel fulfillChanel:
                    candidateFulfillmentChannels) {
                CandidateFulfillmentChannel candidateProductFulfillChanel = new CandidateFulfillmentChannel();
                CandidateFulfillmentChannelKey key = new CandidateFulfillmentChannelKey();
                key.setCandidateProductId(candidateProductMaster.getCandidateProductId());
                key.setSalesChannelCode(fulfillChanel.getKey().getSalesChannelCode());
                key.setFulfillmentChannelCode(fulfillChanel.getKey().getFulfillmentChannelCode());
                candidateProductFulfillChanel.setKey(key);
                if(StringUtils.isNotBlank(fulfillChanel.getNewData()) && fulfillChanel.getNewData().equalsIgnoreCase(CandidateFulfillmentChannel.YES_STATUS)) {
                    candidateProductFulfillChanel.setEffectiveDate(fulfillChanel.getEffectiveDate());
                    candidateProductFulfillChanel.setExpirationDate(fulfillChanel.getExpirationDate());
                    candidateProductFulfillChanel.setNewData(CandidateFulfillmentChannel.YES_STATUS);
                }else{
                    candidateProductFulfillChanel.setNewData(CandidateFulfillmentChannel.DELETED_STATUS);
                }
                candidateProductFulfillChanel.setCandidateProductMaster(candidateProductMaster);
                candidateProductFulfillChanel.setCandidateWorkRequest(candidateWorkRequest);
                candidateProductMaster.getCandidateFulfillmentChannels().add(candidateProductFulfillChanel);
            }
        }
    }
}
