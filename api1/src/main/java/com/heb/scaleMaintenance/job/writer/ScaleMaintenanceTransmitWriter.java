package com.heb.scaleMaintenance.job.writer;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writer for a scale maintenance upc.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceTransmitWriter implements ItemWriter<List<ScaleMaintenanceTransmit>> {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceTransmitWriter.class);
	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called writer with null or empty list.";

	@Autowired
	private ScaleMaintenanceTransmitRepository repository;

	@Override
	public void write(List<? extends List<ScaleMaintenanceTransmit>> items) throws Exception {

		if (items == null || items.isEmpty()) {
			ScaleMaintenanceTransmitWriter.logger.info(ScaleMaintenanceTransmitWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}
		for(List<ScaleMaintenanceTransmit> scaleMaintenanceUpcs : items){
			if (scaleMaintenanceUpcs == null || scaleMaintenanceUpcs.isEmpty()) {
				ScaleMaintenanceTransmitWriter.logger.info(ScaleMaintenanceTransmitWriter.EMPTY_LIST_LOGGER_MESSAGE);
			} else {
				this.repository.save(scaleMaintenanceUpcs);
			}
		}
	}
}
