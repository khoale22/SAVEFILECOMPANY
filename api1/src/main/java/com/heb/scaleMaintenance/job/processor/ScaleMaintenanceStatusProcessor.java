package com.heb.scaleMaintenance.job.processor;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.Status;
import com.heb.scaleMaintenance.model.WrappedScaleMaintenanceTracking;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * This processor takes in a scale maintenance tracking, updates the status to in progress, then returns the scale
 * maintenance tracking.
 *
 * @author m314029
 * @since 2.17.8
 */
public class ScaleMaintenanceStatusProcessor implements ItemProcessor<ScaleMaintenanceTracking, WrappedScaleMaintenanceTracking> {

	private Status.Code statusCode;

	@Autowired
	private ScaleMaintenanceTransmitRepository scaleMaintenanceTransmitRepository;

	@Override
	public WrappedScaleMaintenanceTracking process(ScaleMaintenanceTracking item) throws Exception {
		if(item == null){
			return null;
		}
		List<ScaleMaintenanceTransmit> transmits = this.findRelatedTransmits(item);
		return this.updateStatusForTrackingAndTransmits(item, transmits);
	}

	/**
	 * Update the status of the tracking and related transmits to the given status code.
	 *
	 * @param tracking Scale maintenance tracking to update status on.
	 * @param transmits Scale maintenance transmit to update status on.
	 * @return Wrapped scale maintenance tracking containing the tracking and transmits.
	 */
	private WrappedScaleMaintenanceTracking updateStatusForTrackingAndTransmits(
			ScaleMaintenanceTracking tracking, List<ScaleMaintenanceTransmit> transmits) {
		tracking.setStatusCode(statusCode.getId());
		transmits.forEach(transmit -> transmit.setStatusCode(statusCode.getId()));
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

	/**
	 * Sets the StatusCode.
	 *
	 * @param statusCode The StatusCode.
	 **/
	@Required
	public ScaleMaintenanceStatusProcessor setStatusCode(Status.Code statusCode) {
		this.statusCode = statusCode;
		return this;
	}
}
