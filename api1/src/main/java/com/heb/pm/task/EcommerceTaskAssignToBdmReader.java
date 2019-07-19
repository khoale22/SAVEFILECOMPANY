/*
 * EcommerceTaskAssignToBdmReader
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * EcommerceTaskAssignToBdmReader is invoked when a batch request(assign to bdm) is submitted for the
 * eCommerce task assign to bdm. It reads the eCommerce task updates related CandidateWorkRequest from database in batch mode and feeds to
 * the batch processor.
 *
 * @author vn70529
 * @since 2.17.0
 */
public class EcommerceTaskAssignToBdmReader implements ItemReader<CandidateWorkRequest>, StepExecutionListener {

    private static final int BATCH_SIZE = 100;
    private int page = 0;
    private int totalPages = 0;
    private MassUpdateTaskRequest massUpdateTaskRequest;
    private boolean isCandidateWorkRequestFromDB = false;
    private Iterator<CandidateWorkRequest> candidateWorkRequests;
    /**
     * Job tracking id.
     */
    @Value("#{jobParameters['transactionId']}")
    private Long transactionId;
    @Value("#{jobParameters['userId']}")
    private String userId;
    @Autowired
    private MassUpdateTaskMap massUpdateTaskMap;
    @Autowired
    private EcommerceTaskService ecommerceTaskService;
    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Override
    public CandidateWorkRequest read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        return getNext();
    }

    /**
     * Initialize the state of the listener with the {@link StepExecution} from
     * the current scope.
     *
     * @param stepExecution
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        page = 0;
        totalPages = 0;
        // Get the data info to create candidate.
        massUpdateTaskRequest = massUpdateTaskMap.get(transactionId);
        massUpdateTaskRequest.setSelectedCandidateWorkRequests(getSelectedCandidateWorkRequestByCurrentUser(massUpdateTaskRequest.getSelectedCandidateWorkRequests()));
        if(massUpdateTaskRequest.isSelectAll()) {
            //Find total number of records to processed.
            Long totalNoOfRecords = this.ecommerceTaskService.getEcommerceTaskUpdatesCount(transactionId, userId);
            calculatePagination(totalNoOfRecords);
        }
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
        return null;
    }

    /**
     * Used to perform pagination calculation for the batch processing.
     *
     * @param totalNoOfRecords total number of records when batch was submitted.
     */
    private void calculatePagination(Long totalNoOfRecords) {
        if (totalNoOfRecords > 0) {
            this.totalPages = (int) Math.ceil((float) totalNoOfRecords / this.BATCH_SIZE);
        }
    }

    /**
     * Fetches an CandidateWorkRequest from the batch to be processed.
     *
     * @return an CandidateWorkRequest records.
     */
    private CandidateWorkRequest getNext() {
        if (this.candidateWorkRequests != null && this.candidateWorkRequests.hasNext()) {
            if(isCandidateWorkRequestFromDB){
                // Get the next product from db and mass fill for it.
                return this.candidateWorkRequests.next();
            }else {
                // Get the next product from the selected candidate work request and add product id to exclude list.
                return addExcludeProduct(getCandidateWorkRequest(this.candidateWorkRequests.next().getProductId()));
            }
        } else {
            if (massUpdateTaskRequest.getSelectedCandidateWorkRequests() != null && !massUpdateTaskRequest.getSelectedCandidateWorkRequests().isEmpty()) {
                // Get the list of candidate work requests from client.
                isCandidateWorkRequestFromDB = false;
                this.candidateWorkRequests = massUpdateTaskRequest.getSelectedCandidateWorkRequests().iterator();
                // clear SelectedCandidateWorkRequests.
                massUpdateTaskRequest.setSelectedCandidateWorkRequests(new ArrayList<>());
                // Get the next product from the selected candidate work request and add product id to exclude list.
                return addExcludeProduct(getCandidateWorkRequest(this.candidateWorkRequests.next().getProductId()));
            } else {
                // Select all case
                if (massUpdateTaskRequest.isSelectAll()) {
                    // Selected all products from database.
                    isCandidateWorkRequestFromDB = true;
                    // Get the list of work requests from db and remove the product has been existed in exclude list.
                    this.candidateWorkRequests = fetchNextSet();
                    return this.candidateWorkRequests.hasNext() ? this.candidateWorkRequests.next() : null;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Add the product into exclude list.
     *
     * @param candidateWorkRequest the candidateWorkRequest object.
     * @return the candidateWorkRequest object.
     */
    private CandidateWorkRequest addExcludeProduct(CandidateWorkRequest candidateWorkRequest){
        if(massUpdateTaskRequest.getExcludedAlerts()==null){
            massUpdateTaskRequest.setExcludedAlerts(new ArrayList<>());
        }
        massUpdateTaskRequest.getExcludedAlerts().add(candidateWorkRequest.getProductId());
        return candidateWorkRequest;
    }
    /**
     * Fetches next of batch to be executed.
     *
     * @return a set of CandidateWorkRequest records.
     */
    private Iterator<CandidateWorkRequest> fetchNextSet() {
        Iterable<CandidateWorkRequest> resultList = new ArrayList<>();
        if (this.page < this.totalPages) {
            Pageable pageable = new PageRequest(page++, this.BATCH_SIZE);
            Iterable<CandidateWorkRequest> result = candidateWorkRequestRepository.findByTrackingIdAndLastUpdateUserIdAndStatus(
                    massUpdateTaskRequest.getTrackingId(), userId.toUpperCase(), CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD, pageable);
            if (result != null) {
                resultList = result;
                applyExclusions(resultList);
                //If applyExclusions has removed all the items in the list, fetch next of result. Otherwise getNext()
                // will return NULL which will force batch to abruptly conclude.
                if (!result.iterator().hasNext()) {
                    return fetchNextSet();
                }
            }
        }
        return resultList.iterator();
    }

    /**
     * Removes the CandidateWorkRequest excluded by user during mass update (reject all).
     * @param resultList list of alerts.
     */
    private void applyExclusions(Iterable<CandidateWorkRequest> resultList) {
        List<Long> productIds = massUpdateTaskRequest.getExcludedAlerts();
        if (productIds != null && !productIds.isEmpty()) {
            Iterator<CandidateWorkRequest> candidateWorkRequests = resultList.iterator();
            while(candidateWorkRequests.hasNext()) {
                if (productIds.contains(candidateWorkRequests.next().getProductId().longValue())) {
                    candidateWorkRequests.remove();
                }
            }
        }
    }

    /**
     * Get candidate work request by product id and assignee(option) and status.
     * @param productId the product id.
     * @return the CandidateWorkRequest.
     */
    private CandidateWorkRequest getCandidateWorkRequest(Long productId){
        return candidateWorkRequestRepository.findOneByTrackingIdAndLastUpdateUserIdAndStatusAndProductId(transactionId,userId.trim().toUpperCase(), CandidateWorkRequest.PD_SETUP_STAT_CD_BATCH_UPLOAD, productId);
    }

    /**
     * Get selected Candidate Work Request By CurrentUser.
     * @param candidateWorkRequests
     * @return the list of candidateWorkRequests.
     */
    private List<CandidateWorkRequest> getSelectedCandidateWorkRequestByCurrentUser(List<CandidateWorkRequest> candidateWorkRequests){
        List<CandidateWorkRequest> resultList = new ArrayList<>();
        candidateWorkRequests.forEach(candidateWorkRequest ->{
            if(StringUtils.isNotBlank(candidateWorkRequest.getLastUpdateUserId()) && candidateWorkRequest.getLastUpdateUserId().trim().equalsIgnoreCase(userId.trim())){
                resultList.add(candidateWorkRequest);
            }
        });
        return resultList;
    }
}