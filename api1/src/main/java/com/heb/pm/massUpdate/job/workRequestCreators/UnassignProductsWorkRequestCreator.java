/*
 *  UnassignProductsWorkRequestCreator
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.MassUpdateParameters;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Creates work requests that unassigns products.
 *
 * @author l730832
 * @since 2.15.0
 */
@Service
class UnassignProductsWorkRequestCreator implements WorkRequestCreator {


	private static final String SETUP_STATUS_CODE = "111";
	private static final String ITEM_KEY_TYPE_CODE = "ENTY";
	/**
	 * Creates work requests that unassigns products from a hierarchy.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters, int sourceSystem) {
		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);
		candidateWorkRequest.setIntent(CandidateWorkRequest.INTNT_ID_UNASSIGN_PRODUCTS);
		candidateWorkRequest.setItemKeyTypeCode(ITEM_KEY_TYPE_CODE);
		candidateWorkRequest.setItemId(parameters.getRootId());
		candidateWorkRequest.setProductId(productId);
		this.setCandidateStatus(candidateWorkRequest);

		return candidateWorkRequest;
	}

	/**
	 * Adds a candidate status to the mass update.
	 * @param workRequest the candidate work request.
	 */
	private void setCandidateStatus(CandidateWorkRequest workRequest) {
		workRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
		CandidateStatus candidateStatus = new CandidateStatus();
		CandidateStatusKey candidateStatusKey = new CandidateStatusKey();
		candidateStatusKey.setStatus(SETUP_STATUS_CODE);
		candidateStatusKey.setLastUpdateDate(LocalDateTime.now());
		candidateStatus.setKey(candidateStatusKey);
		candidateStatus.setUpdateUserId(workRequest.getUserId());
		candidateStatus.setCandidateWorkRequest(workRequest);
		candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
		workRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
		workRequest.getCandidateStatuses().add(candidateStatus);
	}
}
