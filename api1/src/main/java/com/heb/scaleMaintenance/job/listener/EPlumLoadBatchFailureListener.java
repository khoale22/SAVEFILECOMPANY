package com.heb.scaleMaintenance.job.listener;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.Status;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTrackingRepository;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles ePlum load batch failures by updating the statuses for EPlum Tracking and Transmits.
 *
 * @author m314029
 * @since 2.35.0
 */
public class EPlumLoadBatchFailureListener implements JobExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(EPlumLoadBatchFailureListener.class);

	// log messages
	private static final String JOB_COMPLETED_AS_NORMAL_LOG = "EPlum load for transaction id: %d completed as normal.";
	private static final String TRANSACTION_NOT_FOUND_LOG = "Transaction not found for transaction id: %d.";
	private static final String TRANSMITS_NOT_FOUND_LOG = "No transmits found for transaction id: %d. Setting " +
			"tracking with transaction id: %d to ERROR status.";

	// error messages
	private static final String CHECK_E_PLUM_JOB_FAILURE_MESSAGE = "Even though this transmit failed, it may still " +
			"have been sent to ePlum. Please check ePlum application for batch related to store: %d, batch number: " +
			"%d for results.";

	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;

	@Autowired
	private ScaleMaintenanceTrackingRepository scaleMaintenanceTrackingRepository;

	@Autowired
	private ScaleMaintenanceTransmitRepository scaleMaintenanceTransmitRepository;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// intentionally empty
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// the job completed as normal, log a 'completed as normal' message
		if (ExitStatus.COMPLETED.getExitCode().equalsIgnoreCase(jobExecution.getExitStatus().getExitCode())) {
			logger.info(String.format(JOB_COMPLETED_AS_NORMAL_LOG, transactionId));
		} else {
			// else handle an abnormal completed status
			this.handleJobWithNonCompletedStatus();
		}
	}

	/**
	 * Handles job completed statuses other than COMPLETED. This includes updating both ePlum tracking and related
	 * transmits where applicable. If a transmit was not successful, a message is added to the result specifying
	 * to the user that the transmission still may have been sent to ePlum.
	 */
	private void handleJobWithNonCompletedStatus() {
		ScaleMaintenanceTracking tracking = this.scaleMaintenanceTrackingRepository.findOne(transactionId);
		// if tracking was not found, log message and return
		if (tracking == null) {
			logger.info(String.format(TRANSACTION_NOT_FOUND_LOG, transactionId));
			return;
		}
		List<ScaleMaintenanceTransmit> relatedTransmits =
				this.scaleMaintenanceTransmitRepository.findByKeyTransactionId(transactionId);
		// if no transmits were found, update tracking status to error and return
		if (CollectionUtils.isEmpty(relatedTransmits)) {
			logger.info(String.format(TRANSMITS_NOT_FOUND_LOG, transactionId, transactionId));
			tracking.setStatusCode(Status.Code.ERROR.getId());
			this.scaleMaintenanceTrackingRepository.save(tracking);
			return;
		}

		// keep track of whether any transmits were sent to ePlum
		boolean transmittedToEPlumSuccessfully = false;
		List<ScaleMaintenanceTransmit> transmitsToUpdate = new ArrayList<>();
		for (ScaleMaintenanceTransmit relatedTransmit : relatedTransmits) {

			// if related transmit's status is TRANSMITTED, update transmitted successful tracker to true
			if (Status.Code.TRANSMITTED.getId().equals(relatedTransmit.getStatusCode())) {
				transmittedToEPlumSuccessfully = true;
			} else if (!Status.Code.ERROR.getId().equals(relatedTransmit.getStatusCode())){
				// else if the related transmit's status is not already ERROR, set to error with failure message
				relatedTransmit
						.setStatusCode(Status.Code.ERROR.getId())
						.setResponseMessage(String.format(CHECK_E_PLUM_JOB_FAILURE_MESSAGE,
								relatedTransmit.getKey().getStore(),
								relatedTransmit.getePlumBatchId()));
				transmitsToUpdate.add(relatedTransmit);
			}
		}

		// if any of the transmits were successfully sent to ePlum, set tracking status to TRANSMITTED
		if (transmittedToEPlumSuccessfully) {
			tracking.setStatusCode(Status.Code.TRANSMITTED.getId());
		} else {
			// else set tracking status to ERROR
			tracking.setStatusCode(Status.Code.ERROR.getId());
			if (CollectionUtils.isNotEmpty(transmitsToUpdate)) {
				// if any transmits need to be updated, update them
				this.scaleMaintenanceTransmitRepository.save(transmitsToUpdate);
			}
		}

		// save tracking with updated status
		this.scaleMaintenanceTrackingRepository.save(tracking);
	}
}
