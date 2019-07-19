/*
 *  WorkRequestCreatorUtils
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.parser;

import com.heb.pm.entity.CandidateWorkRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Some utility functions for the creators to use.
 *
 * @author vn55306
 * @since 2.12.0
 */
public class WorkRequestCreatorUtils {

	// Private so the class cannot be instantiated.
	private WorkRequestCreatorUtils() {}

	/**
	 * Returns a CandidateWorkRequest for the creators to populate. It sets attributes that are common across all
	 * mass updates.
	 *
	 * @param productId The ID of the product being updated.
	 * @param upc The Upc of the product being updated.
	 * @param userId The ID of the user who requested the update.
	 * @param transactionId The transaction ID of the job.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest for the creators to populate
	 */
	public static CandidateWorkRequest getEmptyWorkRequest(String productId,Long upc, String userId, Long transactionId,int sourceSystem,String status) {
		CandidateWorkRequest workRequest = new CandidateWorkRequest();
		if(productId!=null){
			try {
				workRequest.setProductId(Long.valueOf(productId));
			}catch (NumberFormatException e){

			}
		}
		workRequest.setCreateDate(LocalDateTime.now());
		workRequest.setUserId(userId);
		workRequest.setLastUpdateDate(LocalDateTime.now());
		workRequest.setReadyToActivate(true);
		workRequest.setStatus(status);
		workRequest.setIntent(CandidateWorkRequest.INTNT_ID_DEFAULT);
		workRequest.setSourceSystem(sourceSystem);
		workRequest.setLastUpdateUserId(userId);
		workRequest.setTrackingId(transactionId);
		workRequest.setStatusChangeReason(3L);
		workRequest.setUpc(upc);
		return workRequest;
	}
}
