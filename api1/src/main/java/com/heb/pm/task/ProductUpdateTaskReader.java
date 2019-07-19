/*
 * ProductUpdateTaskMassUpdateReader
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertStaging;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * ProductUpdateTaskReader is invoked when a batch request(mass update) is submitted for the
 * product update task updates. It reads the product update task updates related alerts from database in batch mode and feeds to
 * the batch processor.
 *
 * @author vn87351
 * @since 2.17.0
 */
public class ProductUpdateTaskReader implements ItemReader<AlertStaging>, StepExecutionListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductUpdateTaskWriter.class);
	private static final int BATCH_SIZE = 100;
	private int page = 1;
	private int totalPages = 0;
	private MassUpdateTaskRequest massUpdateTaskRequest;
	private boolean isAlertStagingsFromDB;
	private Iterator<AlertStaging> alertStagings;
	/**
	 * Job tracking id.
	 */
	@Value("#{jobParameters['trackingId']}")
	private Long transactionId;

	@Autowired
	private MassUpdateTaskMap massUpdateTaskMap;
	@Autowired
	private ProductUpdatesTaskService productUpdatesTaskService;

	@Override
	public AlertStaging read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
		return getNext();
	}

	/**
	 * Initialize the state of the listener with the {@link StepExecution} from
	 * the current scope. If select all flag on ui is true then
	 *
	 * @param stepExecution
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		//Find total number of records to processed.
		massUpdateTaskRequest = massUpdateTaskMap.getItem(transactionId);
		if (massUpdateTaskRequest.isSelectAll()) {
			if (massUpdateTaskRequest.getSelectedAlertStaging() != null && !massUpdateTaskRequest.getSelectedAlertStaging().isEmpty()) {
				isAlertStagingsFromDB = false;
				alertStagings = massUpdateTaskRequest.getSelectedAlertStaging().iterator();
				calculatePagination(Long.valueOf(String.valueOf(massUpdateTaskRequest.getSelectedAlertStaging().size())));
			} else {
				isAlertStagingsFromDB = true;
				PageableResult<AlertStaging> alerts = productUpdatesTaskService.getProducts(massUpdateTaskRequest.getAlertType(), massUpdateTaskRequest.getAssigneeId(), massUpdateTaskRequest.getAttributes(),
						massUpdateTaskRequest.getShowOnSiteFilter(), massUpdateTaskRequest.getSalesChannelsFilter(), true, 0, BATCH_SIZE);
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
	 * Fetches an CandidateWorkRequest from the batch to be processed.
	 *
	 * @return an CandidateWorkRequest records.
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
	 * @param alertStaging
	 */
	private void addExcludeAlert(AlertStaging alertStaging) {
		if (massUpdateTaskRequest.getExcludedAlerts() == null) {
			massUpdateTaskRequest.setExcludedAlerts(new ArrayList<>());
		}
		massUpdateTaskRequest.getExcludedAlerts().add(alertStaging.getAlertID().longValue());
	}

	/**
	 * Fetches next of batch to be executed.
	 *
	 * @return a set of CandidateWorkRequest records.
	 */
	private Iterator<AlertStaging> fetchNextSet() {
		Iterable<AlertStaging> resultList = new ArrayList<>();
		PageableResult<AlertStaging> result = null;
		if (!isAlertStagingsFromDB) {
			isAlertStagingsFromDB = true;
			result = productUpdatesTaskService.getProducts(massUpdateTaskRequest.getAlertType(), massUpdateTaskRequest.getAssigneeId(), massUpdateTaskRequest.getAttributes(),
					massUpdateTaskRequest.getShowOnSiteFilter(), massUpdateTaskRequest.getSalesChannelsFilter(), true, 0, BATCH_SIZE);
			this.totalPages = result.getPageCount();
		} else if (this.page < this.totalPages) {
			result = productUpdatesTaskService.getProducts(massUpdateTaskRequest.getAlertType(), massUpdateTaskRequest.getAssigneeId(), massUpdateTaskRequest.getAttributes(),
					massUpdateTaskRequest.getShowOnSiteFilter(), massUpdateTaskRequest.getSalesChannelsFilter(), false, this.page++, BATCH_SIZE);
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
	 * Removes the AlertStaging excluded by user during mass update (reject all).
	 *
	 * @param item list of alerts.
	 */
	private boolean isExclusionsAlert(AlertStaging item) {
		List<Long> alertIds = massUpdateTaskRequest.getExcludedAlerts();
		if (alertIds != null && !alertIds.isEmpty() && alertIds.contains(item.getAlertID().longValue())) {
			return true;
		}
		return false;
	}
}
