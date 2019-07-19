package com.heb.scaleMaintenance.job.reader;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTrackingRepository;
import com.heb.util.controller.IntegerListFromStringFormatter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.Locale;

/**
 * Reader for a list of stores that returns one store.
 *
 * @author m314029
 * @since 2.17.8
 */
public class StoresReader implements ItemReader<Integer>, StepExecutionListener {

	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;

	private Iterator<Integer> data;

	@Autowired
	ScaleMaintenanceTrackingRepository repository;

	private IntegerListFromStringFormatter integerListFromStringFormatter = new IntegerListFromStringFormatter();

	@Override
	public Integer read() throws Exception {

		// If there is still data, return it.
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}

		// we're at the end of the data.
		return null;
	}

	/**
	 * Sets up the data to be returned.
	 *
	 * @param stepExecution The environment this step is going to run in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		ScaleMaintenanceTracking tracking=repository.findOne(transactionId);
		data=this.integerListFromStringFormatter.parse(tracking.getLoadParametersAsJson().getStores(),Locale.US).iterator();
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Always returns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
