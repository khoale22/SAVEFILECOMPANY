package com.heb.scaleMaintenance.job.processor;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.model.WrappedScaleMaintenanceTracking;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Processor for converting batch numbers into ePlum Messages.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceTransmitProcessor implements ItemProcessor<ScaleMaintenanceTracking, WrappedScaleMaintenanceTracking> {

	@Autowired
	private ScaleMaintenanceTransmitRepository scaleMaintenanceTransmitRepository;

	@Override
	public WrappedScaleMaintenanceTracking process(ScaleMaintenanceTracking item) throws Exception {
		if(item == null){
			return null;
		}
		return this.getEPlumMessagesForRelatedTransmits(item);
	}

	/**
	 * Gets ePlum messages for a scale maintenance tracking and related transmits.
	 *
	 * @param tracking Tracking read into this processor.
	 * @return Wrapped tracking containing the given scale maintenance tracking and related transmits.
	 */
	private WrappedScaleMaintenanceTracking getEPlumMessagesForRelatedTransmits(ScaleMaintenanceTracking tracking) {

		List<ScaleMaintenanceTransmit> transmits = this.findRelatedTransmits(tracking);
		return new WrappedScaleMaintenanceTracking(tracking, transmits);
	}

	/**
	 * Gets all scale maintenance transmits that share the same tracking id as the given scale maintenance tracking.
	 *
	 * @param tracking Scale maintenance tracking to look for related transmits by.
	 * @return List of scale maintenance transmits that share the transaction id.
	 */
	private List<ScaleMaintenanceTransmit> findRelatedTransmits(ScaleMaintenanceTracking tracking) {
		return this.scaleMaintenanceTransmitRepository.findByKeyTransactionId(tracking.getTransactionId());
	}
}
