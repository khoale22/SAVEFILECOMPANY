/*
 * NutritionUpdatesMassUpdateReader
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.massUpdate.job.MassUpdateProductMap;
import com.heb.util.jpa.PageableResult;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NutritionUpdatesMassUpdateReader is invoked when a batch request(mass update) is submitted for the
 * nutrition updates(rejects). It reads the nutrition updates realted alerts from database in batch mode and feeds to
 * the batch processor.
 *
 * @author vn40486
 * @since 2.13.0
 */
public class NutritionUpdatesMassUpdateReader implements ItemReader<AlertStaging>, StepExecutionListener {

    private static final int BATCH_SIZE = 100;
    private int page = 0;
    private int totalPages = 0;
    private Iterator<AlertStaging> alertStagings;
    private MassUpdateTaskRequest massUpdateTaskRequest;
    private boolean isAlertStagingsFromDB;
    /**
     * Job tracking id.
     */
    @Value("#{jobParameters['trackingId']}")
    private Long trackingId;

    @Autowired
    private MassUpdateProductMap massUpdateProductMap;
    @Autowired
    private MassUpdateTaskMap massUpdateTaskMap;

    @Autowired
    private NutritionUpdatesTaskService nutritionUpdatesTaskService;
    @Override
    public AlertStaging read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
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
        //Find total number of records to processed.
        massUpdateTaskRequest = massUpdateTaskMap.getItem(trackingId);
        if (massUpdateTaskRequest.isSelectAll()) {
            if (massUpdateTaskRequest.getSelectedAlertStaging() != null && !massUpdateTaskRequest.getSelectedAlertStaging().isEmpty()) {
                isAlertStagingsFromDB = false;
                alertStagings = massUpdateTaskRequest.getSelectedAlertStaging().iterator();
                calculatePagination(Long.valueOf(String.valueOf(massUpdateTaskRequest.getSelectedAlertStaging().size())));
            } else {
                isAlertStagingsFromDB = true;
                PageableResult<AlertStaging> alerts = nutritionUpdatesTaskService.searchActiveNutritionUpdates(
                        massUpdateTaskRequest.getProductSearchCriteria(),
                        0, BATCH_SIZE , true);
                alertStagings = alerts.getData().iterator();
                this.totalPages = alerts.getPageCount();
            }
        } else {
            if (massUpdateTaskRequest.getSelectedAlertStaging() != null && !massUpdateTaskRequest.getSelectedAlertStaging().isEmpty()) {
                isAlertStagingsFromDB = false;
                alertStagings = massUpdateTaskRequest.getSelectedAlertStaging().iterator();
                calculatePagination(Long.valueOf(String.valueOf(massUpdateTaskRequest.getSelectedAlertStaging().size())));
            }
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
     * Used to perform pagination claculation for the batch processing.
     * @param totalNoOfRecords total number of records when batch was submitted.
     */
    private void calculatePagination(Long totalNoOfRecords) {
        if (totalNoOfRecords > 0) {
            this.totalPages = (int) Math.ceil((float)totalNoOfRecords/this.BATCH_SIZE);
        }
    }

    /**
     * Fetches an AlertStaging from the batch to be processed.
     * @return an AlertStaging records.
     */
    private AlertStaging getNext() {
        if (this.alertStagings != null && this.alertStagings.hasNext()) {
            AlertStaging alertStaging = this.alertStagings.next();
            if (alertStaging.getProductMaster() == null || (isAlertStagingsFromDB && isExclusionsAlert(alertStaging))) {
                return getNext();
            }
            addExcludeAlert(alertStaging);
            return alertStaging;
        } else {
            if (massUpdateTaskRequest.isSelectAll()) {
                this.alertStagings = fetchNextSet();
                if (this.alertStagings.hasNext())
                    return getNext();
            }
        }
        return null;
    }
    /**
     * add alert id to exclude list
     *
     * @param alertStaging a alertStaging contain alert ID
     */
    private void addExcludeAlert(AlertStaging alertStaging) {
        if (massUpdateTaskRequest.getExcludedAlerts() == null) {
            massUpdateTaskRequest.setExcludedAlerts(new ArrayList<>());
        }
        massUpdateTaskRequest.getExcludedAlerts().add(alertStaging.getAlertID().longValue());
    }
    /**
     * Fetches next of batch to be executed.
     * @return a set of AlertStaging records.
     */
    private Iterator<AlertStaging> fetchNextSet() {
        Iterable<AlertStaging> resultList = new ArrayList<>();
        PageableResult<AlertStaging> result = null;
        if (!isAlertStagingsFromDB) {
            isAlertStagingsFromDB = true;
            result = nutritionUpdatesTaskService.searchActiveNutritionUpdates(massUpdateTaskRequest.getProductSearchCriteria(),
                    0, BATCH_SIZE , true);

            this.totalPages = result.getPageCount();
        } else if (this.page < this.totalPages) {
            result=nutritionUpdatesTaskService.searchActiveNutritionUpdates(massUpdateTaskRequest.getProductSearchCriteria(),
                    this.page++, BATCH_SIZE , false);
        }
        if (result != null && result.getData() != null) {
            resultList = result.getData();
            // will return NULL which will force batch to abruptly conclude.
            if (!result.getData().iterator().hasNext()) {
                fetchNextSet();
            }
        }
        return resultList.iterator();
    }

    /**
     * Removes the alerts excluded by user during mass update (reject all).
     * @param resultList list of alerts.
     */
    private void applyExclusions(Iterable<AlertStaging> resultList) {
        List<Long> productIds = massUpdateProductMap.get(this.trackingId).getProductIds();
        if (productIds != null && !productIds.isEmpty()) {
            Iterator<AlertStaging> alertStagingIterator = resultList.iterator();
            while(alertStagingIterator.hasNext()) {
                if (productIds.contains(alertStagingIterator.next().getAlertID().longValue())) {
                    alertStagingIterator.remove();
                }
            }
        }
    }
    /**
     * Removes the alerts excluded by user during mass update (reject all).
     *
     * @param item list of alerts.
     * @return true if it is success or not.
     */
    private boolean isExclusionsAlert(AlertStaging item) {
        List<Long> alertIds = massUpdateTaskRequest.getExcludedAlerts();
        return alertIds != null && !alertIds.isEmpty() && alertIds.contains(item.getAlertID().longValue());
    }
}