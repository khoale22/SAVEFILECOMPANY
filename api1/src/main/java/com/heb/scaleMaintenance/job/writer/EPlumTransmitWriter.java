package com.heb.scaleMaintenance.job.writer;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.Status;
import com.heb.scaleMaintenance.model.WrappedScaleMaintenanceTracking;
import com.heb.scaleMaintenance.service.EPlumApiService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Writer for wrapped scale maintenance tracking related transmits.
 *
 * @author m314029
 * @since 2.20.0
 */
public class EPlumTransmitWriter implements ItemWriter<WrappedScaleMaintenanceTracking> {

	private static final Logger logger = LoggerFactory.getLogger(EPlumTransmitWriter.class);

	// logs
	private static final String SEND_TO_EPLUM_LOG = "Sending %d messages to ePlum.";

	@Value("#{jobParameters['userIp']}")
	private String userIp;

	@Autowired
	private EPlumApiService ePlumApiService;

	@Override
	public void write(List<? extends WrappedScaleMaintenanceTracking> items) throws Exception {
		List<ScaleMaintenanceTransmit> transmits;
		for(WrappedScaleMaintenanceTracking tracking : items){
			if(CollectionUtils.isNotEmpty(tracking.getRelatedTransmits())){
				transmits = this.getTransmitsWaitingToTransmit(tracking.getRelatedTransmits());
				if(CollectionUtils.isNotEmpty(transmits)){
					this.sendMessagesToEPlum(transmits);
				}
			}
			tracking.getTracking().setStatusCode(Status.Code.TRANSMITTED.getId());
		}
	}

	/**
	 * Sends messages within the given scale maintenance transmits to ePlum.
	 *
	 * @param transmits Scale maintenance transmits with messages to send to ePlum.
	 */
	private void sendMessagesToEPlum(List<ScaleMaintenanceTransmit> transmits) {

		logger.info(String.format(SEND_TO_EPLUM_LOG, transmits.size()));
		this.ePlumApiService.sendMessagesToEPlum(transmits, userIp);
	}

	/**
	 * Gets all scale maintenance transmits that are in a transmitting status.
	 *
	 * @param relatedTransmits All related transmits to a given scale maintenance tracking.
	 * @return List of transmits that contain a message to send to ePlum.
	 */
	private List<ScaleMaintenanceTransmit> getTransmitsWaitingToTransmit(List<ScaleMaintenanceTransmit> relatedTransmits) {
		return relatedTransmits.stream()
				.filter(transmit -> Status.Code.TRANSMITTING.getId()
						.equals(transmit.getStatusCode())).collect(Collectors.toList());
	}
}
