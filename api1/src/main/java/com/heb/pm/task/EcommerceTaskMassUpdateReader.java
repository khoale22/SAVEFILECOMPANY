/*
 * EcommerceTaskMassUpdateReader
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.*;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import com.heb.util.DateUtils;
import com.heb.util.jpa.PageableResult;
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
 * EcommerceTaskMassUpdateReader is invoked when a batch request(mass update) is submitted for the
 * eCommerce task updates. It reads the eCommerce task updates related CandidateWorkRequest from database in batch mode and feeds to
 * the batch processor.
 *
 * @author vn70529
 * @since 2.17.0
 */
public class EcommerceTaskMassUpdateReader implements ItemReader<CandidateWorkRequest>, StepExecutionListener {

    private static final int BATCH_SIZE = 100;
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
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
        //if(massUpdateTaskRequest.isSelectAll()) {
            //Find total number of records to processed.
        //    Long totalNoOfRecords = this.ecommerceTaskService.getEcommerceTaskUpdatesCount(massUpdateTaskRequest.getTrackingId(), massUpdateTaskRequest.getAssigneeId());
        //    calculatePagination(totalNoOfRecords);
        //}
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
                return applyMassFillParams(this.candidateWorkRequests.next());
            }else {
                // Get the next product from the selected candidate work request and add product id to exclude list.
                return addExcludeProduct(this.candidateWorkRequests.next());
            }
        } else {
            if (massUpdateTaskRequest.getSelectedCandidateWorkRequests() != null && !massUpdateTaskRequest.getSelectedCandidateWorkRequests().isEmpty()) {
                // Get the list of candidate work requests from client.
                isCandidateWorkRequestFromDB = false;
                this.candidateWorkRequests = massUpdateTaskRequest.getSelectedCandidateWorkRequests().iterator();
                // clear SelectedCandidateWorkRequests.
                massUpdateTaskRequest.setSelectedCandidateWorkRequests(new ArrayList<>());
                // Get the next product from the selected candidate work request and add product id to exclude list.
                return addExcludeProduct(this.candidateWorkRequests.next());
            } else {
                // Select all case
                if (massUpdateTaskRequest.isSelectAll()) {
                    // Selected all products from database.
                    isCandidateWorkRequestFromDB = true;
                    // Get the list of work requests from db and remove the product has been existed in exclude list.
                    this.candidateWorkRequests = fetchNextSet();
                    return this.candidateWorkRequests.hasNext() ? applyMassFillParams(this.candidateWorkRequests.next()) : null;
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
        Iterable<CandidateWorkRequest> result = null;
        if (this.page == 0) {
            result = getCandidateWorkRequests(massUpdateTaskRequest.getTrackingId(),
                    massUpdateTaskRequest.getAssigneeId(), massUpdateTaskRequest.getShowOnSiteFilter(), massUpdateTaskRequest.getSalesChannelsFilter(), true, this.page++, this.BATCH_SIZE);
        } else if (this.page < this.totalPages) {
            result = getCandidateWorkRequests(massUpdateTaskRequest.getTrackingId(),
                    massUpdateTaskRequest.getAssigneeId(), massUpdateTaskRequest.getShowOnSiteFilter(), massUpdateTaskRequest.getSalesChannelsFilter(), false, this.page++, this.BATCH_SIZE);
        }
        if (result != null) {
            resultList = result;
            applyExclusions(resultList);
            //If applyExclusions has removed all the items in the list, fetch next of result. Otherwise getNext()
            // will return NULL which will force batch to abruptly conclude.
            if (!result.iterator().hasNext()) {
                return fetchNextSet();
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
     * Apply mass fill params to candidateWorkRequest.
     *
     * @param candidateWorkRequest the candidateWorkRequest
     * @return the candidateWorkRequest.
     */
    private CandidateWorkRequest applyMassFillParams(CandidateWorkRequest candidateWorkRequest){
        candidateWorkRequest.setPdpTemplate(massUpdateTaskRequest.getPdpTemplate());
        if(massUpdateTaskRequest.getLstFulfillmentChannel()!=null){
            candidateWorkRequest.setCandidateFulfillmentChannels(new ArrayList<>());
            for (FulfillmentChannel fulfillmentChannel: massUpdateTaskRequest.getLstFulfillmentChannel()){
                CandidateFulfillmentChannel candidateFulfillmentChannel = new CandidateFulfillmentChannel();
                CandidateFulfillmentChannelKey candidateFulfillmentChannelKey = new CandidateFulfillmentChannelKey();
                candidateFulfillmentChannelKey.setFulfillmentChannelCode(fulfillmentChannel.getKey().getFulfillmentChannelCode());
                candidateFulfillmentChannelKey.setSalesChannelCode(massUpdateTaskRequest.getSalesChannel().getId());
                candidateFulfillmentChannel.setKey(candidateFulfillmentChannelKey);
                if(StringUtils.isNotBlank(massUpdateTaskRequest.isShowOnSite()) && massUpdateTaskRequest.isShowOnSite().trim().equalsIgnoreCase(CandidateFulfillmentChannel.YES_STATUS)) {
                    candidateFulfillmentChannel.setEffectiveDate(DateUtils.getLocalDate(massUpdateTaskRequest.getEffectiveDate(), YYYY_MM_DD));
                    candidateFulfillmentChannel.setExpirationDate(DateUtils.getLocalDate(massUpdateTaskRequest.getExpirationDate(), YYYY_MM_DD));
                }
                candidateFulfillmentChannel.setNewData(massUpdateTaskRequest.isShowOnSite());
                candidateWorkRequest.getCandidateFulfillmentChannels().add(candidateFulfillmentChannel);
            }
        }
        return candidateWorkRequest;
    }
    /**
     * Returns list of candidate products added under it filtered by  the supplied pagination
     * condition.
     *
     * @param trackingId    tracking id of an alert/task.
     * @param assignee      products assigned to user.
     * @param page          selected page number.
     * @param pageSize      page size; number of records to be fetched per page.
     * @return list of candidate work request (products) tagged under the given tracking id or task id.
     */
    Iterable<CandidateWorkRequest> getCandidateWorkRequests(long trackingId, String assignee, String showOnSite, String salesChannels, boolean includeCount, int page, int pageSize) {
        PageableResult<CandidateWorkRequest> productData = this.ecommerceTaskService.getTaskProducts(trackingId, assignee, showOnSite, salesChannels, includeCount, page, pageSize);;
        if(includeCount){
            this.totalPages=productData.getPageCount();
        }
        //PageableResult<CandidateWorkRequest> productData = this.ecommerceTaskService.getTaskProducts(trackingId, assignee, showOnSite, false, page, pageSize);
        return productData.getData();
        /*if(StringUtils.isNotBlank(assignee)) {
            return candidateWorkRequestRepository.findByTrackingIdAndLastUpdateUserIdAndStatus(
                    trackingId, assignee.toUpperCase(), CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD, pageable);
        }
        return candidateWorkRequestRepository.findByTrackingIdAndStatus(
                trackingId, CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD, pageable);*/
    }
}