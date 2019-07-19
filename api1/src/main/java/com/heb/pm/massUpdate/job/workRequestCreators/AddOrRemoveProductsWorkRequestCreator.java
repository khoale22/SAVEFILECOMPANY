/*
 *  AddProductsWorkRequestCreator
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateGenericEntityRelationship;
import com.heb.pm.entity.CandidateGenericEntityRelationshipKey;
import com.heb.pm.entity.CandidateProductMaster;
import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the work request creator to Add products to a hierarchy.
 *
 * @author l730832
 * @since 2.17.0
 */
@Service
public class AddOrRemoveProductsWorkRequestCreator implements WorkRequestCreator{

	private static final Long SEQUENCE_NUMBER = 1L;
	private static final Integer ZERO = 0;
	private static final String SPACE = " ";
	private CandidateStatusKey.StatusCode candidateStatusCode;
	private CandidateStatus.CandidateStatusChangeReason candidateChangeReasonCode;

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	/**
	 * Creates a work request that adds products to a hierarcy.
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
		candidateWorkRequest.setIntent(CandidateWorkRequest.INTNT_ID);
		candidateWorkRequest.setProductId(productId);
		candidateStatusCode = CandidateStatusKey.StatusCode.BATCH_UPLOAD;
		candidateChangeReasonCode = CandidateStatus.CandidateStatusChangeReason.WORKING;
		if(parameters.getAttribute().equals(MassUpdateParameters.Attribute.ADD_HIERARCHY_PRODUCT) ||
				parameters.getAttribute().equals(MassUpdateParameters.Attribute.REMOVE_HIERARCHY_PRODUCT)){
			this.setCandidateProductMaster(candidateWorkRequest, parameters);
			this.setCandidateRelationshipsForProduct(candidateWorkRequest, parameters);
		} else {
			this.setCandidateRelationshipsForProductGroup(candidateWorkRequest, parameters);
		}
		this.setCandidateStatus(candidateWorkRequest);
		return candidateWorkRequest;
	}

	/**
	 * This sets a candidate product Master.
	 */
	private void setCandidateProductMaster(CandidateWorkRequest workRequest, MassUpdateParameters parameters) {
		CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
		candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
		candidateProductMaster.setLstUpdtUsrId(parameters.getUserId());
		candidateProductMaster.setNewDataSw(false);
		candidateProductMaster.setPrprcForNbr(ZERO);
		candidateProductMaster.setWorkRequestId(workRequest.getWorkRequestId());
		candidateProductMaster.setProductId(workRequest.getProductId());
		candidateProductMaster.setCandidateWorkRequest(workRequest);
		workRequest.setCandidateProductMaster(new LinkedList<>());
		workRequest.getCandidateProductMaster().add(candidateProductMaster);
	}

	/**
	 * This sets the candidate relationships for a product.
	 * @param workRequest The candidate work request.
	 * @param parameters The parameters the user wants to set the different values to.
	 */
	private void setCandidateRelationshipsForProduct(CandidateWorkRequest workRequest, MassUpdateParameters parameters) {
		CandidateGenericEntityRelationship candidateGenericEntityRelationship = new CandidateGenericEntityRelationship();
		CandidateGenericEntityRelationshipKey key = new CandidateGenericEntityRelationshipKey();
		key.setWorkRequestId(workRequest.getWorkRequestId());
		key.setHierarchyContext(parameters.getEntityRelationship().getKey().getHierarchyContext());
		key.setParentEntityId(parameters.getEntityRelationship().getKey().getChildEntityId());
		key.setChildEntityId(GenericEntity.EntyType.PROD.getBaseId());
		candidateGenericEntityRelationship.setKey(key);
		candidateGenericEntityRelationship.setCandidateWorkRequest(workRequest);
		candidateGenericEntityRelationship.setDisplay(false);
		candidateGenericEntityRelationship.setSequence(SEQUENCE_NUMBER);
		candidateGenericEntityRelationship.setCreateUserId(parameters.getUserId());
		candidateGenericEntityRelationship.setCreateDate(LocalDateTime.now());
		candidateGenericEntityRelationship.setLastUpdatedUserId(parameters.getUserId());
		candidateGenericEntityRelationship.setLastUpdatedTimestamp(LocalDateTime.now());
		candidateGenericEntityRelationship.setNewDataSwitch(parameters.getEntityRelationship().getActionCode());
		candidateGenericEntityRelationship.setActiveSwitch(SPACE);
		if(parameters.getAttribute().equals(MassUpdateParameters.Attribute.REMOVE_HIERARCHY_PRODUCT)) {
			GenericEntityRelationship currentRelationship = this.genericEntityRelationshipRepository.
					findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityTypeAndGenericChildEntityDisplayNumber(
							parameters.getEntityRelationship().getKey().getChildEntityId(),
							parameters.getEntityRelationship().getKey().getHierarchyContext(),
							GenericEntity.EntyType.PROD.getName(),
							workRequest.getProductId());
			if(currentRelationship == null){
				candidateStatusCode = CandidateStatusKey.StatusCode.FAILURE;
				candidateChangeReasonCode = CandidateStatus.CandidateStatusChangeReason.REJECTED;
				return;
			}
			candidateGenericEntityRelationship.setDefaultParent(currentRelationship.getDefaultParent());
		} else if(parameters.getAttribute().equals(MassUpdateParameters.Attribute.ADD_HIERARCHY_PRODUCT)) {
			List<GenericEntityRelationship> genericEntityRelationships =
					this.genericEntityRelationshipRepository.
							findByKeyHierarchyContextAndGenericChildEntityDisplayNumberAndDefaultParentAndGenericChildEntityType(
									parameters.getEntityRelationship().getKey().getHierarchyContext(),
									workRequest.getProductId(),
									true,
									GenericEntity.EntyType.PROD.getName());
			if(genericEntityRelationships.size() == 0) {
				candidateGenericEntityRelationship.setDefaultParent(true);
			} else {
				candidateGenericEntityRelationship.setDefaultParent(false);
			}
		}
		workRequest.setCandidateGenericEntityRelationships(new LinkedList<>());
		workRequest.getCandidateGenericEntityRelationships().add(candidateGenericEntityRelationship);
	}

	/**
	 * This sets the candidate relationships for a product group.
	 * @param workRequest The candidate work request.
	 * @param parameters The parameters the user wants to set the different values to.
	 */
	private void setCandidateRelationshipsForProductGroup(CandidateWorkRequest workRequest, MassUpdateParameters parameters) {
		CandidateGenericEntityRelationship candidateGenericEntityRelationship = new CandidateGenericEntityRelationship();
		CandidateGenericEntityRelationshipKey key = new CandidateGenericEntityRelationshipKey();
		key.setWorkRequestId(workRequest.getWorkRequestId());
		key.setHierarchyContext(parameters.getEntityRelationship().getKey().getHierarchyContext());
		key.setParentEntityId(parameters.getEntityRelationship().getKey().getChildEntityId());
		key.setChildEntityId(GenericEntity.EntyType.PGRP.getBaseId());
		workRequest.setItemKeyTypeCode(GenericEntity.EntyType.PGRP.getName());
		workRequest.setItemId(workRequest.getProductId());
		workRequest.setProductId(null);
		candidateGenericEntityRelationship.setKey(key);
		candidateGenericEntityRelationship.setCandidateWorkRequest(workRequest);
		candidateGenericEntityRelationship.setDisplay(false);
		candidateGenericEntityRelationship.setSequence(SEQUENCE_NUMBER);
		candidateGenericEntityRelationship.setCreateUserId(parameters.getUserId());
		candidateGenericEntityRelationship.setCreateDate(LocalDateTime.now());
		candidateGenericEntityRelationship.setLastUpdatedUserId(parameters.getUserId());
		candidateGenericEntityRelationship.setLastUpdatedTimestamp(LocalDateTime.now());
		candidateGenericEntityRelationship.setNewDataSwitch(parameters.getEntityRelationship().getActionCode());
		candidateGenericEntityRelationship.setActiveSwitch(SPACE);
		if(parameters.getAttribute().equals(MassUpdateParameters.Attribute.REMOVE_HIERARCHY_PRODUCT_GROUP)) {
			GenericEntityRelationship currentRelationship = this.genericEntityRelationshipRepository.
					findByKeyParentEntityIdAndKeyHierarchyContextAndGenericChildEntityTypeAndGenericChildEntityDisplayNumber(
							parameters.getEntityRelationship().getKey().getChildEntityId(),
							parameters.getEntityRelationship().getKey().getHierarchyContext(),
							GenericEntity.EntyType.PGRP.getName(),
							workRequest.getItemId());
			if(currentRelationship == null){
				candidateStatusCode = CandidateStatusKey.StatusCode.FAILURE;
				candidateChangeReasonCode = CandidateStatus.CandidateStatusChangeReason.REJECTED;
				return;
			}
			candidateGenericEntityRelationship.setDefaultParent(currentRelationship.getDefaultParent());
		} else if(parameters.getAttribute().equals(MassUpdateParameters.Attribute.ADD_HIERARCHY_PRODUCT_GROUP)) {
			List<GenericEntityRelationship> genericEntityRelationships =
					this.genericEntityRelationshipRepository.
							findByKeyHierarchyContextAndGenericChildEntityDisplayNumberAndDefaultParentAndGenericChildEntityType(
									parameters.getEntityRelationship().getKey().getHierarchyContext(),
									workRequest.getItemId(),
									true,
									GenericEntity.EntyType.PGRP.getName());
			if(genericEntityRelationships.size() == 0) {
				candidateGenericEntityRelationship.setDefaultParent(true);
			} else {
				candidateGenericEntityRelationship.setDefaultParent(false);
			}
		}
		workRequest.setCandidateGenericEntityRelationships(new LinkedList<>());
		workRequest.getCandidateGenericEntityRelationships().add(candidateGenericEntityRelationship);
	}

	/**
	 * Adds a candidate status to the mass update.
	 * @param workRequest the candidate work request.
	 */
	private void setCandidateStatus(CandidateWorkRequest workRequest) {
		workRequest.setCandidateStatuses(new LinkedList<>());
		CandidateStatus candidateStatus = new CandidateStatus();
		CandidateStatusKey candidateStatusKey = new CandidateStatusKey();
		candidateStatusKey.setStatus(candidateStatusCode.getName());
		candidateStatusKey.setLastUpdateDate(LocalDateTime.now());
		candidateStatus.setKey(candidateStatusKey);
		candidateStatus.setUpdateUserId(workRequest.getUserId());
		candidateStatus.setCandidateWorkRequest(workRequest);
		candidateStatus.setStatusChangeReason(candidateChangeReasonCode.getName());
		workRequest.getCandidateStatuses().add(candidateStatus);
	}
}
