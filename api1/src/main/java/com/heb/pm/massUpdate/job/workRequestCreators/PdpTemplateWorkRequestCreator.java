/*
 *  PdpTemplateWorkRequestCreator
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateMasterDataExtensionAttribute;
import com.heb.pm.entity.CandidateMasterDataExtensionAttributeKey;
import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.MassUpdateParameters;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This is the work request creator for a pdp template.
 *
 * @author l730832
 * @since 2.17.0
 */
@Service
public class PdpTemplateWorkRequestCreator implements WorkRequestCreator {

	private static final String SETUP_STATUS_CODE = "111";
	private static final Long PDP_ATTRIBUTE_ID = 515L;
	private static final Long PDP_SEQUENCE_NUMBER = 1L;
	private static final String ITEM_PROD_KEY_CODE = "PROD";
	private static final Integer ZERO = 0;
	private static final Long DATA_SOURCE = 4L;
	private static final String EMPTY = " ";

	/**
	 * Creates an empty work request for a pdp template.
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
		candidateWorkRequest.setIntent(CandidateWorkRequest.INTNT_ID_DEFAULT);
		candidateWorkRequest.setProductId(productId);
		this.setMasterDataExtensionAttribute(candidateWorkRequest, parameters);
		this.setCandidateStatus(candidateWorkRequest);

		return candidateWorkRequest;
	}

	/**
	 * This sets the master data extension attribute for the pdp ps tables.
	 * @param workRequest
	 */
	private void setMasterDataExtensionAttribute(CandidateWorkRequest workRequest, MassUpdateParameters parameters) {
		workRequest.setCandidateMasterDataExtensionAttributes(new LinkedList<>());
		CandidateMasterDataExtensionAttribute candidateMasterDataExtensionAttribute =
				new CandidateMasterDataExtensionAttribute();
		CandidateMasterDataExtensionAttributeKey key = new CandidateMasterDataExtensionAttributeKey();
		key.setAttributeId(PDP_ATTRIBUTE_ID);
		key.setKeyId(workRequest.getProductId());
		key.setSequenceNumber(PDP_SEQUENCE_NUMBER);
		key.setItemProductKey(ITEM_PROD_KEY_CODE);
		key.setDataSourceSystem(DATA_SOURCE);
		candidateMasterDataExtensionAttribute.setKey(key);
		candidateMasterDataExtensionAttribute.setAttributeValueText(
				parameters.getPdpTemplate());
		candidateMasterDataExtensionAttribute.setAttributeValueDate(LocalDate.now());
		candidateMasterDataExtensionAttribute.setAttributeValueTime(LocalDateTime.now());
		candidateMasterDataExtensionAttribute.setLastUpdateDate(LocalDateTime.now());
		candidateMasterDataExtensionAttribute.setLastUpdateUserId(workRequest.getUserId());
		candidateMasterDataExtensionAttribute.setCreateUserId(workRequest.getUserId());
		candidateMasterDataExtensionAttribute.setCreateDate(LocalDateTime.now());
		candidateMasterDataExtensionAttribute.setAttributeCode(ZERO);
		candidateMasterDataExtensionAttribute.setCandidateWorkRequest(workRequest);
		candidateMasterDataExtensionAttribute.setNewData(EMPTY);
		workRequest.getCandidateMasterDataExtensionAttributes().add(candidateMasterDataExtensionAttribute);
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
