package com.heb.scaleMaintenance.job.reader;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpcKey;
import com.heb.scaleMaintenance.service.ScaleMaintenanceAuthorizeRetailService;
import com.heb.util.controller.LongListFromStringFormatter;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reads a list of UPCs, and returns a subset of the list.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleScanUpcByDeptReader implements ItemReader<List<ScaleMaintenanceUpc>>, StepExecutionListener {

	private static final Logger logger = LoggerFactory.getLogger(ScaleScanUpcByDeptReader.class);

	@Value("#{jobParameters['transactionId']}")
	private Long transactionId;
	@Value("#{jobParameters['sourceStoreNo']}")
	private int sourceStoreNo;

	@Autowired
	private ScaleMaintenanceAuthorizeRetailService service;

	private Iterator<ScaleMaintenanceAuthorizeRetail> data;

	private boolean readUpcs;

	private LongListFromStringFormatter longListFromStringFormatter = new LongListFromStringFormatter();

	@Override
	public void beforeStep(StepExecution stepExecution) {
		data = this.service.getAuthorizedByTransactionIdAndStoreAndNoMessage(transactionId, sourceStoreNo).iterator();
		logger.info("Inside Before Step 3:  ScaleScanUpcByDeptReader");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("Inside After Step 3:  ScaleScanUpcByDeptReader");
		return null;
	}

	@Override
	public List<ScaleMaintenanceUpc> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(data.hasNext()) {
			ScaleMaintenanceAuthorizeRetail scaleMaintenanceAuthorizeRetail = data.next();
			ScaleMaintenanceUpc scaleMaintenanceUpc = new ScaleMaintenanceUpc()
					.setKey(
							new ScaleMaintenanceUpcKey()
									.setTransactionId(transactionId)
									.setUpc(scaleMaintenanceAuthorizeRetail.getKey().getUpc()))
					.setCreateTime(LocalDateTime.now());
			List<ScaleMaintenanceUpc> scaleMaintenanceUpcList = new ArrayList<ScaleMaintenanceUpc>();
			scaleMaintenanceUpcList.add(scaleMaintenanceUpc);
			return scaleMaintenanceUpcList;
		} else {
			// else we are at end of list
			return null;
		}
	}
}
