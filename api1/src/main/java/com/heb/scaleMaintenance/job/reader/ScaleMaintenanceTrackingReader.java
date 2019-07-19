package com.heb.scaleMaintenance.job.reader;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTrackingRepository;
import com.heb.util.controller.LongListFromStringFormatter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Reads a list of batch numbers.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceTrackingReader implements ItemReader<ScaleMaintenanceTracking>, StepExecutionListener {

	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;

	@Autowired
	private ScaleMaintenanceTrackingRepository repository;

	private boolean readTransactionId;

	@Override
	public ScaleMaintenanceTracking read() throws Exception {
		// If there is still data, return it.
		if (!this.readTransactionId) {
			this.readTransactionId = true;
			return this.repository.findOne(this.transactionId);
		}

		// we're at the end of the data.
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.readTransactionId = false;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
