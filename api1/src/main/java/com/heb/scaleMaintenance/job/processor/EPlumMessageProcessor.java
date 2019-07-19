package com.heb.scaleMaintenance.job.processor;

import com.heb.scaleMaintenance.entity.*;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import com.heb.scaleMaintenance.service.ScaleMaintenanceAuthorizeRetailService;
import com.heb.scaleMaintenance.service.ScaleMaintenanceUpcService;
import com.heb.scaleMaintenance.utils.EPlumApiUtils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Processor for converting batch numbers into ePlum Messages.
 *
 * @author m314029
 * @since 2.17.8
 */
public class EPlumMessageProcessor implements ItemProcessor<List<ScaleMaintenanceTransmit>, List<ScaleMaintenanceTransmit>> {

	private static final Logger logger = LoggerFactory.getLogger(EPlumMessageProcessor.class);

	@Autowired
	private ScaleMaintenanceAuthorizeRetailService scaleMaintenanceAuthorizeRetailService;

	@Autowired
	private ScaleMaintenanceUpcService scaleMaintenanceUpcService;

	@Autowired
	private EPlumApiUtils ePlumApiUtils;
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	@Value("#{jobParameters['targetStoreNbr']}")
	private int targetStoreNbr;
	
	@Value("#{jobParameters['effectiveDate']}")
	private Date effectiveDate;
   //End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	@Autowired
	private ScaleMaintenanceTransmitRepository scaleMaintenanceTransmitRepository;

	@Override
	public List<ScaleMaintenanceTransmit> process(List<ScaleMaintenanceTransmit> transmits) throws Exception {
		if(CollectionUtils.isEmpty(transmits)){
			return null;
		}
		Long transactionId = transmits.get(0).getKey().getTransactionId();
		Long ePlumBatchNumber = transactionId % 10000;
		List<ScaleMaintenanceUpc> currentUpcs =
				this.scaleMaintenanceUpcService.getByTransactionId(transactionId);
		List<ScaleMaintenanceAuthorizeRetail> currentAuthorizeRetails;
		boolean hasEPlumMessage = false;
		for(ScaleMaintenanceTransmit transmit : transmits){
			currentAuthorizeRetails = this.scaleMaintenanceAuthorizeRetailService
					.getAuthorizedByTransactionIdAndStoreAndNoMessage(transactionId, transmit.getKey().getStore());
			if(CollectionUtils.isNotEmpty(currentAuthorizeRetails)){
				if(!hasEPlumMessage){
					hasEPlumMessage = this.setEPlumMessageOnTransmit(ePlumBatchNumber, transmit, currentUpcs, currentAuthorizeRetails);
				} else {
					this.setEPlumMessageOnTransmit(ePlumBatchNumber, transmit, currentUpcs, currentAuthorizeRetails);
				}
			} else {
				transmit.setStatusCode(Status.Code.COMPLETED.getId());
			}
			transmit.setLastUpdatedTime(LocalDateTime.now());
		}
		return transmits;
	}

	/**
	 * Sets ePlum message on a given scale maintenance transmit.
	 *
	 * @param ePlumBatchNumber ePlum batch number.
	 * @param transmit Scale maintenance transmit with information to send to ePlum.
	 * @param currentUpcs List of current upcs.
	 * @param currentAuthorizeRetails List of current authorize and retails.
	 * @return Whether or not there is a message to send to ePlum.
	 */
	private boolean setEPlumMessageOnTransmit(Long ePlumBatchNumber, ScaleMaintenanceTransmit transmit,
										   List<ScaleMaintenanceUpc> currentUpcs,
										   List<ScaleMaintenanceAuthorizeRetail> currentAuthorizeRetails) {
		try{
			//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			transmit.setePlumMessage(
					this.ePlumApiUtils.generateEPlumMessage(
							ePlumBatchNumber, transmit.getKey().getStore(), currentUpcs, currentAuthorizeRetails,targetStoreNbr,effectiveDate));
			//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani				
			return true;
		} catch (IllegalArgumentException e){
			logger.info(e.getLocalizedMessage());
			transmit.setResult(e.getLocalizedMessage())
					.setStatusCode(Status.Code.COMPLETED.getId());
			return false;
		}
	}
}
