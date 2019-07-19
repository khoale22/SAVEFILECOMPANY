package com.heb.scaleMaintenance.job.processor;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpc;
import com.heb.scaleMaintenance.utils.ScaleMaintenanceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Processor for converting list of longs into ScaleMaintenanceUpcs.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceUpcProcessor implements ItemProcessor<List<ScaleMaintenanceUpc>,  List<ScaleMaintenanceUpc>> {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceUpcProcessor.class);

	// error messages
	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called processor with null or empty list.";

	@Autowired
	private ScaleMaintenanceUtils scaleMaintenanceUtils;

	@Override
	public List<ScaleMaintenanceUpc> process(List<ScaleMaintenanceUpc> upcs) throws Exception {
		if (upcs == null || upcs.isEmpty()) {
			ScaleMaintenanceUpcProcessor.logger.info(ScaleMaintenanceUpcProcessor.EMPTY_LIST_LOGGER_MESSAGE);
			return null;
		}

		return this.scaleMaintenanceUtils.updateScaleMaintenanceUpcWithProductData(upcs);
	}
}
