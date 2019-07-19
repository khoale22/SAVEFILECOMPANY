package com.heb.scaleMaintenance.job.writer;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.model.WrappedScaleMaintenanceTracking;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Writer for wrapped scale maintenance tracking related transmits.
 *
 * @author m314029
 * @since 2.20.0
 */
public class WrappedTrackingTransmitsWriter implements ItemWriter<WrappedScaleMaintenanceTracking> {

	private static final Logger logger = LoggerFactory.getLogger(WrappedTrackingTransmitsWriter.class);

	private static final String DATABASE_SAVE_LOG_MESSAGE = "Writing back to %d completed transmit records.";

	@Autowired
	private ScaleMaintenanceTransmitRepository repository;

	@Override
	public void write(List<? extends WrappedScaleMaintenanceTracking> items) throws Exception {

		List<ScaleMaintenanceTransmit> transmits = new ArrayList<>();
		items.stream().filter(tracking ->
				CollectionUtils.isNotEmpty(tracking.getRelatedTransmits()))
				.map(WrappedScaleMaintenanceTracking::getRelatedTransmits)
				.forEach(transmits::addAll);
		if(CollectionUtils.isNotEmpty(transmits)){
			logger.info(String.format(DATABASE_SAVE_LOG_MESSAGE, transmits.size()));
			this.repository.save(transmits);
		}
	}
}
