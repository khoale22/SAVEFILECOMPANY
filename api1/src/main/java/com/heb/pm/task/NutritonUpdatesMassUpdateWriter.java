/*
 * NutritonUpdatesMassUpdateWriter
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import com.heb.pm.ws.CheckedSoapException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * NutritonUpdatesMassUpdateWriter write the updates(rejects) into the data store. It involves closing the Alert,
 * update candidate as rejected and insert  a candidate status change record that contains the reject reason.
 *
 * @author vn40486
 * @since 2.13.0
 */
public class NutritonUpdatesMassUpdateWriter implements ItemWriter<AlertStaging> {

    private static final Logger logger = LoggerFactory.getLogger(NutritonUpdatesMassUpdateWriter.class);

    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Autowired
    private ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;

    private static final String ERROR_RESPONSE = "Error with nutrition updates mass update: %s ";

    /**
     * Process the supplied data element. Will not be called with any null items
     * in normal operation.
     *
     * @param items items to be written
     * @throws Exception if there are errors. The framework will catch the
     *                   exception and convert or rethrow it as appropriate.
     */
    @Override
    public void write(List<? extends AlertStaging> items) throws Exception {
        /* Prepare: Collect all candidate work request of all Alerts into a single collection. */
        List<CandidateWorkRequest> candidateWorkRequestList = new ArrayList<>();
        items.forEach(alertStaging -> {
                            if(alertStaging.getCandidateWorkRequests() != null){
                                candidateWorkRequestList.addAll(alertStaging.getCandidateWorkRequests());
                            }
                        });
        /* Save:  this should update PS_WORK_RQST and insert a record into PS_CANDIDATE_STAT. */
        candidateWorkRequestRepository.save(candidateWorkRequestList);
        /* Save: update ALERT records using webservice. */
        items.forEach(alertStaging -> {
            try {
                applicationAlertStagingServiceClient.updateAlert(
                        alertStaging.getAlertID(), AlertStaging.AlertStatusCD.CLOSE.getName());
            } catch (CheckedSoapException e) {
                logger.error(String.format(ERROR_RESPONSE, e.getMessage()));
            }
        });
    }
}