package com.heb.pm.massUpdate.job;

import com.heb.pm.batchUpload.jms.BatchUploadMessageTibcoSender;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Listener for the mass update job that performs any operations that need to be completed after the job is done
 * or before it starts.
 *
 * @author d116773
 * @since 2.13.0
 */
public class MassUpdateJobListener implements JobExecutionListener{

	private static final Logger logger = LoggerFactory.getLogger(MassUpdateJobListener.class);

	private static final String ACTIVATION_JOB_MESSAGE = "Starting activation job for tracking ID %d";
	private static final String SKIPPING_ACTIVATION_MESSAGE = "No records were written for tracking ID %d, activation job not started";

	@Autowired
	private BatchUploadMessageTibcoSender batchUploadMessageTibcoSender;

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	/**
	 * Not implemented.
	 *
	 * @param jobExecution Ignored.
	 */
	@Override
	public void beforeJob(JobExecution jobExecution) {

	}

	/**
	 * Responds to the end of the job.
	 *
	 * @param jobExecution The instance of the executed job.
	 */
	@Override
	public void afterJob(JobExecution jobExecution) {

		// If anything was written out, then kick off the activation job.
		int recordsWritten = jobExecution.getStepExecutions().iterator().next().getWriteCount();
		if ( recordsWritten > 0) {

			MassUpdateJobListener.logger.info(
					String.format(MassUpdateJobListener.ACTIVATION_JOB_MESSAGE, this.transactionId));

			this.batchUploadMessageTibcoSender.sendTrkIdToTibcoEMSQueue(this.transactionId, recordsWritten, StringUtils.EMPTY);

		} else {
			// If not, don't. This should be if there was an error and no records were created.
			MassUpdateJobListener.logger.info(
					String.format(MassUpdateJobListener.SKIPPING_ACTIVATION_MESSAGE, this.transactionId));
		}

	}
}
