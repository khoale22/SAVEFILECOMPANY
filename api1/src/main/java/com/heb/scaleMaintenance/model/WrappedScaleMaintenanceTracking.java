package com.heb.scaleMaintenance.model;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;

import java.util.List;

/**
 * Represents an object containing a list of scale maintenance tracking and scale maintenance transmits.
 *
 * @author m314029
 * @since 2.20.0
 */
public class WrappedScaleMaintenanceTracking {

	private final ScaleMaintenanceTracking tracking;
	private final List<ScaleMaintenanceTransmit> relatedTransmits;

	public WrappedScaleMaintenanceTracking(ScaleMaintenanceTracking tracking, List<ScaleMaintenanceTransmit> relatedTransmits) {
		this.tracking = tracking;
		this.relatedTransmits = relatedTransmits;
	}

	/**
	 * Returns Tracking.
	 *
	 * @return The Tracking.
	 **/
	public ScaleMaintenanceTracking getTracking() {
		return tracking;
	}

	/**
	 * Returns RelatedTransmits.
	 *
	 * @return The RelatedTransmits.
	 **/
	public List<ScaleMaintenanceTransmit> getRelatedTransmits() {
		return relatedTransmits;
	}
}
