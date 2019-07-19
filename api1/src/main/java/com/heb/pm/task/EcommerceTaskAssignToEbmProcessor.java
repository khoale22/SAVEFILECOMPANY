/*
 * EcommerceTaskAssignToEbmProcessor
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.repository.ProductMasterRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/**
 * EcommerceTaskAssignToEbmProcessor works through every CandidateWorkRequest sent by reader. It basically does the preparation for
 * writing to the data store.
 *
 * @author vn70529
 * @since 2.17.0
 */
public class EcommerceTaskAssignToEbmProcessor implements ItemProcessor<CandidateWorkRequest, CandidateWorkRequest> {
    @Value("#{jobParameters['userId']}")
    private String userId;
    @Value("#{jobParameters['transactionId']}")
    private Long transactionId;
    @Autowired
    private ProductMasterRepository productMasterRepository;

    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is null, it is assumed that processing of the item
     * should not continue.
     *
     * @param candidateWorkRequest item to be processed
     * @return potentially modified or new item for continued processing, null if processing of the
     * provided item should not continue.
     * @throws Exception
     */
    @Override
    public CandidateWorkRequest process(CandidateWorkRequest candidateWorkRequest) {
        if (candidateWorkRequest != null) {
            ProductMaster productMaster = productMasterRepository.findOne(candidateWorkRequest.getProductId());
            if (productMaster != null && productMaster.getClassCommodity() != null) {
                if (StringUtils.isNotBlank(productMaster.getClassCommodity().geteBMid())) {
                    // Prepares candidateWorkRequest
                    // recipient id of getClassCommodity().geteBMid()
                    candidateWorkRequest.setLastUpdateUserId(productMaster.getClassCommodity().geteBMid().trim());
                    candidateWorkRequest.setLastUpdateDate(LocalDateTime.now());
                    candidateWorkRequest.setDelegatedByUserId(userId);
                    candidateWorkRequest.setDelegatedTime(LocalDateTime.now());
                }else{
                    candidateWorkRequest = null;
                }
            }else{
                candidateWorkRequest = null;
            }
        }
        return candidateWorkRequest;
    }
}
