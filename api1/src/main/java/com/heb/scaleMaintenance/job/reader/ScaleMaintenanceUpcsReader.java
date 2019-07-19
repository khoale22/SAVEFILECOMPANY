package com.heb.scaleMaintenance.job.reader;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpcKey;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTrackingRepository;
import com.heb.util.controller.LongListFromStringFormatter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Reads a list of UPCs, and returns a subset of the list.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceUpcsReader implements ItemReader<List<ScaleMaintenanceUpc>>, StepExecutionListener {


	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;

	@Autowired
	ScaleMaintenanceTrackingRepository repository;

	private List<Long> data;

	private boolean readUpcs;

	private LongListFromStringFormatter longListFromStringFormatter = new LongListFromStringFormatter();

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ScaleMaintenanceTracking tracking=repository.findOne(transactionId);
		data=this.longListFromStringFormatter.parse(tracking.getLoadParametersAsJson().getUpcs(),Locale.US);
		readUpcs = false;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	@Override
	public List<ScaleMaintenanceUpc> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(!readUpcs) {
			readUpcs = true;
			ScaleMaintenanceUpc scaleMaintenanceUpc;
			List<ScaleMaintenanceUpc> toReturn;
			// If there is still data, return it.
			toReturn = new ArrayList<>();
			for (Long upc : this.data) {
				scaleMaintenanceUpc = new ScaleMaintenanceUpc()
						.setKey(
								new ScaleMaintenanceUpcKey()
										.setTransactionId(transactionId)
										.setUpc(upc))
						.setCreateTime(LocalDateTime.now());
				toReturn.add(scaleMaintenanceUpc);
			}
			return toReturn;
		} else {
			// else we are at end of list
			return null;
		}
	}
}
