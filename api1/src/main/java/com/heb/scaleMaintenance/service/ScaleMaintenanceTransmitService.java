package com.heb.scaleMaintenance.service;

import com.heb.scaleMaintenance.EPlumTransactional;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmitKey;
import com.heb.scaleMaintenance.entity.Status;
import com.heb.scaleMaintenance.model.EPlumMessage;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepository;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTransmitRepositoryWithCount;
import com.heb.util.controller.IntegerListFromStringFormatter;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Holds the business logic for scale maintenance transmit.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class ScaleMaintenanceTransmitService {

	// string formats
	private static final String RESULT_FORMAT = "%s: %d of %d";
	private static final String COMMA_DELIMITER = ",";

	// constants related to authorization
	private static final String AUTHORIZED_STATE_MESSAGE = "Authorized: ";
	private static final String NOT_AUTHORIZED_STATE_MESSAGE = "Not Authorized: ";

	// messages
	private static final String NO_AUTHORIZATIONS_MESSAGE = "No authorization data yet.";

	@Autowired
	private ScaleMaintenanceTransmitRepository repository;

	@Autowired
	private ScaleMaintenanceTransmitRepositoryWithCount repositoryWithCount;

	@Autowired
	private ScaleMaintenanceAuthorizeRetailService scaleMaintenanceAuthorizeRetailService;

	private IntegerListFromStringFormatter integerListFromStringFormatter = new IntegerListFromStringFormatter();

	/**
	 * Gets scale maintenance transmits by transaction id.
	 *
	 * @param transactionId Transaction id to look for.
	 * @return List of scale maintenance transmits with the given transaction id.
	 */
	public List<ScaleMaintenanceTransmit> getScaleMaintenanceTransmitsByTransactionId(Long transactionId) {
		return this.repository.findByKeyTransactionId(transactionId);
	}

	/**
	 * This method creates scale maintenance transmits for a load. A new scale maintenance transmit
	 * is created for each store received into this function, attached to the given transaction id.
	 *
	 * @param storesAsString Store list given as a string from the front end.
	 * @param transactionId Transaction id for this scale maintenance load.
	 */
	@EPlumTransactional
	public void createForLoad(String storesAsString, Long transactionId) {
		List<Integer> stores = this.integerListFromStringFormatter.parse(storesAsString, Locale.US);
		if(!stores.isEmpty()) {
			Long ePlumBatchId = this.getEPlumBatchIdFromTransactionId(transactionId);
			List<ScaleMaintenanceTransmit> newTransmits = new ArrayList<>();
			ScaleMaintenanceTransmit tempTransmit;
			ScaleMaintenanceTransmitKey tempKey;
			for(Integer store : stores){
				tempKey = new ScaleMaintenanceTransmitKey()
						.setStore(store)
						.setTransactionId(transactionId);
				tempTransmit = new ScaleMaintenanceTransmit()
						.setCreateTime(LocalDateTime.now())
						.setLastUpdatedTime(LocalDateTime.now())
						.setKey(tempKey)
						.setePlumBatchId(ePlumBatchId)
						.setePlumMessage(new EPlumMessage())
						.setStatusCode(Status.Code.READY.getId());
				newTransmits.add(tempTransmit);
			}
			this.repository.save(newTransmits);
		}
	}

	/**
	 * Gets an ePlum batch id by modding a transaction id by 10000. This is so scale management team can distinguish
	 * between user created batches (within ePlum application) and 'host' created batches (created outside the ePlum
	 * application).
	 *
	 * @param transactionId Transaction id for this scale maintenance.
	 * @return Transaction id modded by 10000.
	 */
	private Long getEPlumBatchIdFromTransactionId(Long transactionId) {
		return transactionId % 10000;
	}

	/**
	 * This method finds all scale maintenance transmits by transaction id, and ordered by store (ascending).
	 *
	 * @param page The page requested.
	 * @param pageSize The page size requested.
	 * @param includeCount Whether to include count.
	 * @param transactionId Transaction id to search for.
	 * @return Page of scale maintenance transmits.
	 */
	public PageableResult<ScaleMaintenanceTransmit> findAllByTransactionIdOrderedByStore(
			Integer page, Integer pageSize, Boolean includeCount, Long transactionId) {
		PageRequest request = new PageRequest(page, pageSize, ScaleMaintenanceTransmit.getDefaultSort());
		PageableResult<ScaleMaintenanceTransmit> toReturn =  includeCount ?
				this.findAllByTransactionIdOrderedByStoreWithCount(transactionId, request) :
				this.findAllByTransactionIdOrderedByStoreWithOutCount(transactionId, request);
		for(ScaleMaintenanceTransmit transmit : toReturn.getData()){
			transmit.setResult(this.getResultMessageForScaleMaintenanceTransmit(transmit.getKey()));
		}
		return toReturn;
	}

	/**
	 * This gets the result message for a given scale maintenance transmit by finding all linked scale maintenance
	 * authorize retails, and getting counts of all that are authorized, and not authorized.
	 *
	 * @param key Scale maintenance transmit key having required transaction and store to search for.
	 * @return String result message for a scale maintenance transmit.
	 */
	private String getResultMessageForScaleMaintenanceTransmit(ScaleMaintenanceTransmitKey key) {
		List<ScaleMaintenanceAuthorizeRetail> scaleMaintenanceAuthorizeRetails =
				this.scaleMaintenanceAuthorizeRetailService.
						findByTransactionIdAndStore(key.getTransactionId(), key.getStore());
		if(scaleMaintenanceAuthorizeRetails == null || scaleMaintenanceAuthorizeRetails.isEmpty()){
			return NO_AUTHORIZATIONS_MESSAGE;
		}
		int authorizedCount = 0;
		int notAuthorizedCount = 0;

		for(ScaleMaintenanceAuthorizeRetail authorizeRetail : scaleMaintenanceAuthorizeRetails){
			if(authorizeRetail.getAuthorized()){
				authorizedCount++;
			} else {
				notAuthorizedCount++;
			}
		}

		int totalCount = authorizedCount + notAuthorizedCount;

		boolean previousValue = false;
		String toReturn = StringUtils.EMPTY;
		if(authorizedCount > 0){
			previousValue = true;
			toReturn = toReturn.concat(
					String.format(RESULT_FORMAT, AUTHORIZED_STATE_MESSAGE, authorizedCount, totalCount));
		}
		if(notAuthorizedCount > 0){
			if(previousValue){
				toReturn = toReturn.concat(COMMA_DELIMITER);
			}

			toReturn = toReturn.concat(
					String.format(RESULT_FORMAT, NOT_AUTHORIZED_STATE_MESSAGE, notAuthorizedCount, totalCount));
		}
		return toReturn;
	}

	/**
	 * Finds all scale maintenance tracking sorted by created time without including count query.
	 *
	 * @param transactionId Transaction id for this scale maintenance.
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<ScaleMaintenanceTransmit> findAllByTransactionIdOrderedByStoreWithOutCount(Long transactionId, PageRequest request) {
		List<ScaleMaintenanceTransmit> data = this.repository
				.findByKeyTransactionId(transactionId, request);
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Finds all scale maintenance tracking sorted by created time including count query.
	 *
	 * @param transactionId Transaction id for this scale maintenance.
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<ScaleMaintenanceTransmit> findAllByTransactionIdOrderedByStoreWithCount(Long transactionId, PageRequest request) {
		Page<ScaleMaintenanceTransmit> data = this.repositoryWithCount
				.findByKeyTransactionId(transactionId, request);
		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}
}
