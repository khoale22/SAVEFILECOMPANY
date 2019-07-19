/*
 *  UpdateHierarchyProductsWorkRequestCreator
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * This
 *
 * @author l730832
 * @since 2.
 */
@Service
public class MoveProductsWorkRequestCreator implements WorkRequestCreator {

	private static final String SETUP_STATUS_CODE = "111";
	private static final Long SEQUENCE_NUMBER = 1L;
	private static final String MOVE_ACTION_CODE = "M";
	private static final String SPACE = " ";

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	/**
	 * Creates a work request that either moves products to a hierarchy.
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
		candidateWorkRequest.setIntent(CandidateWorkRequest.MOVE_PRODUCT_INTNT_ID);
		candidateWorkRequest.setProductId(productId);

		this.setCandidateRelationships(candidateWorkRequest, parameters);
		this.setCandidateStatus(candidateWorkRequest);

		return candidateWorkRequest;
	}

	/**
	 * This sets the candidate relationships.
	 * @param workRequest
	 * @param parameter
	 */
	private void setCandidateRelationships(CandidateWorkRequest workRequest, MassUpdateParameters parameter) {
		CandidateGenericEntityRelationship candidateGenericEntityRelationship = new CandidateGenericEntityRelationship();
		CandidateGenericEntityRelationshipKey key = new CandidateGenericEntityRelationshipKey();
		key.setWorkRequestId(workRequest.getWorkRequestId());
		key.setHierarchyContext(parameter.getEntityRelationship().getKey().getHierarchyContext());
		key.setParentEntityId(parameter.getEntityRelationship().getKey().getChildEntityId());
		key.setChildEntityId(GenericEntity.EntyType.PROD.getBaseId());
		candidateGenericEntityRelationship.setKey(key);
		candidateGenericEntityRelationship.setDefaultParent(true);
		candidateGenericEntityRelationship.setDisplay(false);
		candidateGenericEntityRelationship.setSequence(SEQUENCE_NUMBER);
		candidateGenericEntityRelationship.setCreateUserId(parameter.getUserId());
		candidateGenericEntityRelationship.setCreateDate(LocalDateTime.now());
		candidateGenericEntityRelationship.setLastUpdatedUserId(parameter.getUserId());
		candidateGenericEntityRelationship.setLastUpdatedTimestamp(LocalDateTime.now());
		candidateGenericEntityRelationship.setCandidateWorkRequest(workRequest);
		candidateGenericEntityRelationship.setNewDataSwitch(MOVE_ACTION_CODE);
		candidateGenericEntityRelationship.setActiveSwitch(SPACE);
		workRequest.setCandidateGenericEntityRelationships(new LinkedList<>());
		workRequest.getCandidateGenericEntityRelationships().add(candidateGenericEntityRelationship);
	}

	/**
	 * Adds a candidate status to the mass update.
	 * @param workRequest the candidate work request.
	 */
	private void setCandidateStatus(CandidateWorkRequest workRequest) {
		CandidateStatus candidateStatus = new CandidateStatus();
		CandidateStatusKey candidateStatusKey = new CandidateStatusKey();
		candidateStatusKey.setStatus(SETUP_STATUS_CODE);
		candidateStatusKey.setLastUpdateDate(LocalDateTime.now());
		candidateStatus.setKey(candidateStatusKey);
		candidateStatus.setUpdateUserId(workRequest.getUserId());
		candidateStatus.setCandidateWorkRequest(workRequest);
		candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
		workRequest.setCandidateStatuses(new LinkedList<>());
		workRequest.getCandidateStatuses().add(candidateStatus);
	}
}
