package com.heb.pm.batchUpload.earley;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.TransactionTracker;
import com.heb.pm.repository.TransactionTrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Used as a job listener to update the transaction tracer table the the job completed.
 *
 * @author d116773
 * @since 2.16.0
 */
public class JobUpdaterTransactionTable implements JobExecutionListener{

	private static final Logger logger = LoggerFactory.getLogger(JobUpdaterTransactionTable.class);

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Autowired
	private TransactionTrackingRepository transactionTrackingRepository;

	/**
	 * Unimplemented.
	 *
	 * @param jobExecution Ignored
	 */
	@Override
	public void beforeJob(JobExecution jobExecution) {

	}

	/**
	 * Called by Spring Batch to update the status of the job after it has completed.
	 * @param jobExecution The context the job is executing in.
	 */
	@Override
	@CoreTransactional
	public void afterJob(JobExecution jobExecution) {

		TransactionTracker transactionTracker =  this.transactionTrackingRepository.findOne(this.transactionId);
		if (transactionTracker == null) {
			JobUpdaterTransactionTable.logger.error(
					String.format("Unable to find transaction for transaction ID %d", this.transactionId));
			return;
		}
		JobUpdaterTransactionTable.logger.info(
				String.format("Updating transaction %d to complete",this.transactionId));
		try {
			transactionTracker.setTrxStatCd(TransactionTracker.STAT_CODE_COMPLETE);
			this.transactionTrackingRepository.save(transactionTracker);
		} catch (Exception e) {
			JobUpdaterTransactionTable.logger.error(e.getLocalizedMessage());
		}
	}
}
