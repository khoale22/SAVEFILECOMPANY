package com.heb.scaleMaintenance.job.writer;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.model.WrappedScaleMaintenanceTracking;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTrackingRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Writer for wrapped scale maintenance tracking.
 *
 * @author m314029
 * @since 2.20.0
 */
public class WrappedTrackingWriter implements ItemWriter<WrappedScaleMaintenanceTracking> {

	private static final Logger logger = LoggerFactory.getLogger(WrappedTrackingWriter.class);

	private static final String DATABASE_SAVE_LOG_MESSAGE = "Writing back to %d completed tracking records.";

	@Autowired
	private ScaleMaintenanceTrackingRepository repository;

	@Override
	public void write(List<? extends WrappedScaleMaintenanceTracking> items) throws Exception {

		List<ScaleMaintenanceTracking> tracking = items.stream()
				.map(WrappedScaleMaintenanceTracking::getTracking)
				.filter(Objects::nonNull).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(tracking)) {
			logger.info(String.format(DATABASE_SAVE_LOG_MESSAGE, tracking.size()));
			this.repository.save(tracking);
		}

	}
}
