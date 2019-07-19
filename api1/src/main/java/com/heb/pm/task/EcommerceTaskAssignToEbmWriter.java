/*
 * EcommerceTaskAssignToEbmWriter
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.*;
import com.heb.pm.repository.BdmRepository;
import com.heb.pm.repository.AlertRecipientRepository;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import com.heb.pm.repository.ProductMasterRepository;
import com.heb.pm.repository.AlertUserResponseRepository;
import com.heb.pm.repository.AlertRecipientRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * The writer for the assign to ebm batch job.
 *
 * @author vn70529
 * @since 2.17.0
 */
public class EcommerceTaskAssignToEbmWriter implements ItemWriter<CandidateWorkRequest>, StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EcommerceTaskAssignToEbmWriter.class);
    private static final String ERROR_ALERT_USER_RESP_UPDATE = "Error while updating Alert User Response: %s.";
    private static final String ERROR_ALERT_RECIPIENT_DELETE = "Error while updating Alert Recipient Response: %s.";
    @Value("#{jobParameters['userId']}")
    private String userId;
    @Value("#{jobParameters['alertId']}")
    private Long alertId;
    @Value("#{jobParameters['transactionId']}")
    private Long transactionId;
    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Autowired
    EcommerceTaskManagementServiceClient taskManagementServiceClient;
    @Autowired
    private AlertUserResponseRepository alertUserResponseRepository;
    @Autowired
    private AlertRecipientRepository alertRecipientRepository;
    @Override
    public void write(List<? extends CandidateWorkRequest> items) {
        if (!items.isEmpty()) {
            /* Prepare: Collect all candidate work request into a single collection. */
            List<CandidateWorkRequest> candidateWorkRequestList = new ArrayList<>();
            items.forEach(candidateWorkRequest -> {
                if (candidateWorkRequest != null) {
                    candidateWorkRequestList.add(candidateWorkRequest);
                }
            });
            if (!candidateWorkRequestList.isEmpty()) {
                // Save candidateWorkRequestList
                candidateWorkRequestRepository.save(candidateWorkRequestList);
                candidateWorkRequestList.forEach(candidateWorkRequest -> {
                    // Insert Alert Recipient for BDM with alert id and recipient id.
                    // recipient id of bdm = assignTaskRequest.getCandidateWorkRequest().getLastUpdateUserId()
                    createAlertRecipient(alertId.intValue(), candidateWorkRequest.getLastUpdateUserId());
                    // Check if there are any records in
                    // ALERT_USER_RESP for the BDM in the CLOSE State
                    // then we need to reset it to spaces
                    // getClassCommodity().geteBMid() == candidateWorkRequest.getLastUpdateUserId()
                    AlertUserResponse alertUserResponse = getAlertUserResponse(alertId.intValue(), candidateWorkRequest.getLastUpdateUserId());
                    if (alertUserResponse != null && StringUtils.isNotBlank(alertUserResponse.getAlertResolutionCode()) &&
                            AlertUserResponse.RESOLUTION_CODE_CLOSE.equalsIgnoreCase(alertUserResponse.getAlertResolutionCode().trim())) {
                        alertUserResponse.setAlertResolutionCode(" ");
                        try {
                            taskManagementServiceClient.updateAlertUserResp(alertUserResponse);
                        } catch (Exception e) {
                            LOGGER.error(String.format(ERROR_ALERT_USER_RESP_UPDATE, e.getMessage()));
                        }
                    }
                });
            }
        }
    }

    /**
     * Initialize the state of the listener with the {@link StepExecution} from
     * the current scope.
     *
     * @param stepExecution
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
    }
    /**
     * Give a listener a chance to modify the exit status from a step. The value
     * returned will be combined with the normal exit status using
     * {@link ExitStatus#and(ExitStatus)}.
     * <p>
     * Called after execution of step's processing logic (both successful or
     * failed). Throwing exception in this method has no effect, it will only be
     * logged.
     *
     * @param stepExecution
     * @return an {@link ExitStatus} to combine with the normal value. Return
     * null to leave the old value unchanged.
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (candidateWorkRequestRepository.countByTrackingIdAndLastUpdateUserIdAndStatus(transactionId, CandidateWorkRequest.PD_SETUP_STAT_CD_BATCH_UPLOAD, userId.toUpperCase()) == 0) {
            deleteAlertRecipient(alertId.intValue(), userId);
        }
        return null;
    }
    /**
     * Create alert recipient.
     * @param alertId the alert id
     * @param userId the user id.
     */
    private void createAlertRecipient(Integer alertId, String userId){
        AlertRecipientKey alertRecipientKey = new AlertRecipientKey();
        alertRecipientKey.setAlertID(alertId);
        alertRecipientKey.setAlertRecipientTypeCode( AlertStaging.ALERT_RECIPIENT_TYPE_USER);
        alertRecipientKey.setRecipientId(userId.toUpperCase().trim());
        if(alertRecipientRepository.findOne(alertRecipientKey) == null){
            taskManagementServiceClient.createAlertRecipient(alertId.intValue(), userId);
        }
    }
    /**
     * Get the alert user response by alert id and user id.
     *
     * @param alertId the alert id.
     * @param userId  the user id.
     * @return the AlertUserResponse.
     */
    private AlertUserResponse getAlertUserResponse(Integer alertId, String userId) {
        AlertUserResponseKey alertUserResponseKey = new AlertUserResponseKey();
        alertUserResponseKey.setUserId(userId);
        alertUserResponseKey.setAlertID(alertId);
        return alertUserResponseRepository.findOne(alertUserResponseKey);
    }
    /**
     * Delete alert recipient.
     * @param alertId the alert id.
     * @param userId the user id.
     */
    private void deleteAlertRecipient(Integer alertId, String userId){
        try{
            taskManagementServiceClient.deleteAlertRecipient(alertId.intValue(), userId);
        } catch (Exception ex) {
            LOGGER.error(String.format(ERROR_ALERT_RECIPIENT_DELETE, ex.getMessage()));
        }
    }
}