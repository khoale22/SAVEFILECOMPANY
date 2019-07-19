package com.heb.scaleMaintenance.service;

import com.heb.scaleMaintenance.EPlumTransactional;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.entity.ScaleTransactionType;
import com.heb.scaleMaintenance.entity.Status;
import com.heb.scaleMaintenance.model.ScaleMaintenanceLoadParameters;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceTrackingRepository;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Holds the business logic for scale maintenance tracking.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class ScaleMaintenanceTrackingService {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceTrackingService.class);

	// string formats
	private static final String RESULT_FORMAT = "%s: %d of %d";
	private static final String COMMA_DELIMITER = ",";

	// messages
	private static final String NO_TRANSMISSIONS_MESSAGE = "Nothing transmitted for this transaction yet.";

	@Autowired
	private ScaleMaintenanceTrackingRepository repository;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ScaleMaintenanceTransmitService scaleMaintenanceTransmitService;

	/**
	 * Creates a new scale maintenance tracking for a load with the given parameters for upcs and stores.
	 *
	 * @param parameters Information surrounding the stores and PLU to send to ePlum.
	 * @return The new scale maintenance tracking.
	 */
	@EPlumTransactional
	public ScaleMaintenanceTracking createLoad(ScaleMaintenanceLoadParameters parameters){
		return this.repository.save(
				new ScaleMaintenanceTracking()
						.setCreateTime(LocalDateTime.now())
						.setUserId(this.userInfo.getUserId())
						.setStatusCode(Status.Code.READY.getId())
						.setJsonVersion(ScaleMaintenanceLoadParameters.CURRENT_VERSION)
						.setScaleTransactionTypeCode(ScaleTransactionType.Code.LOAD.getId())
						.setLoadParametersAsJson(parameters)
						.setLastUpdatedTime(LocalDateTime.now()));
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	
	@EPlumTransactional
	public ScaleMaintenanceTracking createLoadByDept(ScaleMaintenanceLoadParameters parameters){
		return this.repository.save(
				new ScaleMaintenanceTracking()
						.setCreateTime(LocalDateTime.now())
						.setUserId(this.userInfo.getUserId())
						.setStatusCode(Status.Code.READY.getId())
						.setJsonVersion(ScaleMaintenanceLoadParameters.CURRENT_VERSION)
						.setScaleTransactionTypeCode(ScaleTransactionType.Code.LOADBYDEPT.getId())
						.setLoadParametersAsJson(parameters)
						.setLastUpdatedTime(LocalDateTime.now()));
	}
	
	//End- Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * This method gets the result message of a scale maintenance tracking based on the scale maintenance transmits
	 * associated with the tracking id of the scale maintenance tracking. This method first gets the count of each of
	 * the different types of status (in progress, completed, and not transmitted), then builds the return message
	 * based on those numbers.
	 *
	 * @param transactionId Transaction id of scale maintenance tracking to find result message for.
	 * @return Result message for a scale maintenance tracking.
	 */
	private String getResultMessageForScaleMaintenanceTracking(Long transactionId) {
		
		List<ScaleMaintenanceTransmit> scaleMaintenanceTransmits = this.scaleMaintenanceTransmitService.
				getScaleMaintenanceTransmitsByTransactionId(transactionId);
		if(scaleMaintenanceTransmits == null || scaleMaintenanceTransmits.isEmpty()){
			return NO_TRANSMISSIONS_MESSAGE;
		}
		Map<Status.Code, Integer> codeMap = new EnumMap<>(Status.Code.class);

		int totalCount = 0;
		Integer currentStatusCode;
		for(ScaleMaintenanceTransmit transmit : scaleMaintenanceTransmits){
			currentStatusCode = transmit.getStatusCode();
			codeMap.putIfAbsent(Status.Code.getById(currentStatusCode), 0);
			codeMap.replace(Status.Code.getById(currentStatusCode), (codeMap.get(Status.Code.getById(currentStatusCode)) + 1));
			totalCount++;
		}

		String toReturn = StringUtils.EMPTY;
		boolean firstValue = true;
		for(Status.Code statusCode : codeMap.keySet()){
			if(firstValue){
				firstValue = false;
			} else {
				toReturn = toReturn.concat(COMMA_DELIMITER);
			}
			toReturn = toReturn.concat(
					String.format(RESULT_FORMAT, statusCode.getDescription(), codeMap.get(statusCode), totalCount));
		}
		return toReturn;
	}

	/**
	 * Finds a scale maintenance transaction by transaction id.
	 *
	 * @param transactionId Transaction id to look for.
	 * @return Scale maintenance tracking matching the request transaction id.
	 */
	public ScaleMaintenanceTracking findByTransactionId(Long transactionId) {
		ScaleMaintenanceTracking toReturn = this.repository.findOne(transactionId);
		toReturn.setResult(this.getResultMessageForScaleMaintenanceTracking(toReturn.getTransactionId()));
		return toReturn;
	}

	/**
	 * Finds all scale maintenance tracking sorted by created time. If this is the first search, include the 'total record'
	 * count as well as data query; else just do a data query.
	 *
	 * @param page Page to look for.
	 * @param pageSize Page size to set.
	 * @param includeCount Whether to include total record count in queries.
	 * @return Page of data based on parameters.
	 */
	public PageableResult<ScaleMaintenanceTracking> findAllByCreatedTime(Integer page, Integer pageSize, Boolean includeCount) {
		PageRequest request = new PageRequest(page, pageSize, ScaleMaintenanceTracking.getDefaultSort());
		PageableResult<ScaleMaintenanceTracking> toReturn =  includeCount ?
				this.findAllByCreatedTimeWithCount(request) :
				this.findAllByCreatedTimeWithOutCount(request);
		for(ScaleMaintenanceTracking tracking : toReturn.getData()){
			tracking.setResult(this.getResultMessageForScaleMaintenanceTracking(tracking.getTransactionId()));
		}
		return toReturn;
	}

	/**
	 * Finds all scale maintenance tracking sorted by created time without including count query.
	 *
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<ScaleMaintenanceTracking> findAllByCreatedTimeWithOutCount(PageRequest request) {
		List<ScaleMaintenanceTracking> data = this.repository
				.findAllByPage(request);
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Finds all scale maintenance tracking sorted by created time including count query.
	 *
	 * @param request Request containing search criteria (page, pageSize, sort).
	 * @return Page of data.
	 */
	private PageableResult<ScaleMaintenanceTracking> findAllByCreatedTimeWithCount(PageRequest request) {
		Page<ScaleMaintenanceTracking> data = this.repository
				.findAll(request);
		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}
}
