/*
 * EcommerceTaskMassUpdateWriter
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.batchUpload.jms.BatchUploadMessageTibcoSender;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * The writer for the mass update batch job.
 *
 * @author vn70529
 * @since 2.17.0
 */
public class EcommerceTaskMassUpdateWriter implements ItemWriter<CandidateWorkRequest>, StepExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(EcommerceTaskMassUpdateWriter.class);
    @Value("#{jobParameters['transactionId']}")
    private Long transactionId;
    @Value("#{jobParameters['assigneeId']}")
    private String assigneeId;

    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Autowired
    private BatchUploadMessageTibcoSender massUploadMessageTibcoSender;
    @Override
    public void write(List<? extends CandidateWorkRequest> items) {
        if(!items.isEmpty()){
            // Save candidateWorkRequestList
            candidateWorkRequestRepository.save(items);
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
        int countWorkIds = this.candidateWorkRequestRepository.countByTransactionTracking_trackingId(transactionId);
        if (countWorkIds > 0) {
            LOGGER.debug("EcomSummaryMassFillJMSQueueListener - receivedMessage, countWorkIds: " + countWorkIds);
            this.massUploadMessageTibcoSender.sendTrkIdToTibcoEMSQueue(transactionId, countWorkIds, StringUtils.isNotBlank(assigneeId)?assigneeId:StringUtils.EMPTY);
        }
        return null;
    }
}
