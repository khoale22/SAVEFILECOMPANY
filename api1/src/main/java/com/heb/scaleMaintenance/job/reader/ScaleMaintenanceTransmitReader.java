package com.heb.scaleMaintenance.job.reader;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Reads a list of batch numbers.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceTransmitReader implements ItemReader<List<ScaleMaintenanceTransmit>>, StepExecutionListener {

	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;

	@Autowired
	private ScaleMaintenanceTransmitRepository repository;

	private boolean readTransactionId;

	@Override
	public List<ScaleMaintenanceTransmit> read() throws Exception {
		// If there is still data, return it.
		if (!this.readTransactionId) {
			this.readTransactionId = true;
			return this.repository.findByKeyTransactionId(this.transactionId);
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
