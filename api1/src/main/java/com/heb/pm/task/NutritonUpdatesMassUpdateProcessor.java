/*
 * NutritonUpdatesMassUpdateProcessor
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * NutritonUpdatesMassUpdateProcessor works through every Alert sent by reader. It basically does the prepartion for
 * writing to the data store.  Fetches the candidate work request that matches the Alert to be rejected. And adds a
 * candidate status change record to it which contains the reject reason.
 *
 * @author vn40486
 * @since 2.13.0
 */
public class NutritonUpdatesMassUpdateProcessor implements ItemProcessor<AlertStaging, AlertStaging> {
    private MassUpdateTaskRequest massUpdateTaskRequest;
    /**
     * Job tracking Id.
     */
    @Value("#{jobParameters['trackingId']}")
    private Long transactionId;

    /**
     * Used who submitted the mass update.
     */
    @Value("#{jobParameters['userId']}")
    private String userId;

    /**
     * Intent id 26 denotes "Pending Genesis product update approval"
     */
    private static int CANDIDATE_INTENT_NUTRITION_UPDATES = 26;

    /**
     * Src system id 26 denotes "Pending Genesis product update approval"
     */
    private static int CANDIDATE_SRC_NUTRITION_UPDATES = 26;

    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Autowired
    private MassUpdateTaskMap massUpdateTaskMap;

    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is null, it is assumed that processing of the item
     * should not continue.
     *
     * @param alertStaging item to be processed
     * @return potentially modified or new item for continued processing, null if processing of the
     * provided item should not continue.
     * @throws Exception
     */
    @Override
    public AlertStaging process(AlertStaging alertStaging) throws Exception {
        massUpdateTaskRequest=massUpdateTaskMap.getItem(transactionId);
        /* Fetch work request candidates related to the nutrition updated product's Alert. */
        List<CandidateWorkRequest> candidateWorkRequests = candidateWorkRequestRepository.
                findByIntentAndStatusAndSourceSystemAndProductId(
                        CANDIDATE_INTENT_NUTRITION_UPDATES, CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD,
                        CANDIDATE_SRC_NUTRITION_UPDATES, Long.parseLong(alertStaging.getAlertKey().trim()));
        /* Set status of all related work request to status = REJECTED and add a status change reason record. */
        candidateWorkRequests = NutritionUpdatesHelper.setCandidateAsRejectedWithComments
                (candidateWorkRequests, massUpdateTaskRequest.getDescription(), userId);
        alertStaging.setCandidateWorkRequests(candidateWorkRequests);
        /* Set status of the Alert to status = CLOSED. */
        alertStaging.setAlertStatusCD(AlertStaging.AlertStatusCD.CLOSE.getName());
        return alertStaging;
    }
}
