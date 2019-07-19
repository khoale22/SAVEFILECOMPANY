/*
 *  MassFillWorkRequestCreator
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateGenericEntityRelationship;
import com.heb.pm.entity.CandidateGenericEntityRelationshipKey;
import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.GenericEntityRelationshipKey;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import com.heb.pm.repository.GenericEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the work request creator for a mass fill work request creator.
 *
 * @author l730832
 * @since 2.17.0
 */
@Service
public class MassFillWorkRequestCreator implements WorkRequestCreator {

	private static final String SETUP_STATUS_CODE = "111";
	private static final Long SEQUENCE_NUMBER = 1L;
	private static final String YES = "Y";
	private static final String NO = "N";

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	private GenericEntityRepository genericEntityRepository;

	/**
	 * Creates a work request that updates a products primary path.
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
		workRequest.setCandidateGenericEntityRelationships(new LinkedList<>());
		boolean alreadyParent = false;
		List<GenericEntityRelationship> genericEntityRelationshipsExistingParents = this.genericEntityRelationshipRepository.
				findByKeyHierarchyContextAndGenericChildEntityDisplayNumberAndDefaultParent(
						parameter.getEntityRelationship().getKey().getHierarchyContext(),
						workRequest.getProductId(),
						true);
		List<GenericEntity> genericEntities = this.genericEntityRepository.findByDisplayNumber(workRequest.getProductId());
		GenericEntityRelationshipKey key = new GenericEntityRelationshipKey();
		key.setHierarchyContext(parameter.getEntityRelationship().getKey().getHierarchyContext());
		key.setParentEntityId(parameter.getEntityRelationship().getKey().getChildEntityId());
		// Always use 0 as the default.
		key.setChildEntityId(genericEntities.get(0).getId());
		if(!genericEntityRelationshipsExistingParents.isEmpty()) {
			for (GenericEntityRelationship ger : genericEntityRelationshipsExistingParents) {
				if(!ger.getKey().equals(key)){
					workRequest.getCandidateGenericEntityRelationships().add(this.createCandidateEntityRelationship(workRequest, parameter, ger));
				} else {
					alreadyParent = true;
				}
			}
		}
		if(!alreadyParent){
			workRequest.getCandidateGenericEntityRelationships().add(this.createCandidateEntityRelationship(workRequest, parameter, null));
		}
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

	/**
	 * This creates a candidate generic entity relationship.
	 * @param workRequest
	 * @param parameter
	 * @return
	 */
	private CandidateGenericEntityRelationship createCandidateEntityRelationship(CandidateWorkRequest workRequest, MassUpdateParameters parameter, GenericEntityRelationship ger) {
		List<GenericEntity> genericEntities = this.genericEntityRepository.findByDisplayNumber(workRequest.getProductId());
		CandidateGenericEntityRelationship candidateGenericEntityRelationship = new CandidateGenericEntityRelationship();
		CandidateGenericEntityRelationshipKey key = new CandidateGenericEntityRelationshipKey();
		key.setWorkRequestId(workRequest.getWorkRequestId());
		if(ger == null) {
			key.setHierarchyContext(parameter.getEntityRelationship().getKey().getHierarchyContext());
			key.setParentEntityId(parameter.getEntityRelationship().getKey().getChildEntityId());
			// Always use 0 as the default.
			key.setChildEntityId(genericEntities.get(0).getId());
			candidateGenericEntityRelationship.setDefaultParent(true);
		} else {
			key.setHierarchyContext(ger.getKey().getHierarchyContext());
			key.setParentEntityId(ger.getKey().getParentEntityId());
			key.setChildEntityId(ger.getKey().getChildEntityId());
			candidateGenericEntityRelationship.setDefaultParent(false);
		}
		candidateGenericEntityRelationship.setKey(key);
		candidateGenericEntityRelationship.setDisplay(false);
		candidateGenericEntityRelationship.setSequence(SEQUENCE_NUMBER);
		candidateGenericEntityRelationship.setCreateUserId(parameter.getUserId());
		candidateGenericEntityRelationship.setCreateDate(LocalDateTime.now());
		candidateGenericEntityRelationship.setLastUpdatedUserId(parameter.getUserId());
		candidateGenericEntityRelationship.setLastUpdatedTimestamp(LocalDateTime.now());
		candidateGenericEntityRelationship.setCandidateWorkRequest(workRequest);
		candidateGenericEntityRelationship.setActiveSwitch(YES);
		candidateGenericEntityRelationship.setNewDataSwitch(NO);
		return candidateGenericEntityRelationship;
	}
}
