/*
 *  DistinctiveWorkRequestCreator
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.repository.ProductMarketingClaimRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Creates work requests to update Distinctive.
 *
 * @author vn70516
 * @since 2.39.0
 */
@Service
public class DistinctiveWorkRequestCreator implements WorkRequestCreator{

	private static final String PRIMO_PICK_MARKETING_CLAIM_CODE = "00002";
	private static final String PRIMO_PICK_MARKETING_CLAIM_STATUS_CODE_APPROVED = "A";
	private static final String PRIMO_PICK_MARKETING_CLAIM_STATUS_CODE_SUBMITTED = "S";


	private static final String PRIMO_PICK_IS_ACTIVE_MESSAGE = "Cannot remove Distinctive when Primo Pick is active.";

	@Autowired
	private ProductMarketingClaimRepository productMarketingClaimRepository;

	/**
	 * Creates work requests that handle the various types of distinctive operations.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that modify distinctive status.
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters,
												  int sourceSystem) {

		CandidateWorkRequest workRequest = null;

		if (parameters.getDistinctiveFunction().equals(MassUpdateParameters.DistinctiveFunction.TURN_ON_DISTINCTIVE)) {
			workRequest = this.handleTurnOnDistinctive(productId, transactionId, parameters, sourceSystem);
		}

		else if (parameters.getDistinctiveFunction().equals(MassUpdateParameters.DistinctiveFunction.TURN_OFF_DISTINCTIVE)) {
			workRequest = this.handleTurnOffDistinctive(productId, transactionId, parameters, sourceSystem);
		}

		return workRequest;
	}

	/**
	 * Builds the candidate work request when the user is turning on distinctive.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that will turn on distinctive.
	 */
	private CandidateWorkRequest handleTurnOnDistinctive(Long productId, Long transactionId,
														 MassUpdateParameters parameters, int sourceSystem) {

		// Make the work request.
		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);

		// Add the product master.
		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());

		// Also add the distinctive marketing claim.
		WorkRequestCreatorUtils.addMarketingClaimForDistinctive(candidateWorkRequest.getCandidateProductMaster().get(0),
				ProductMarketingClaim.APPROVED, CandidateProductMarketingClaim.TURN_CODE_ON,
				MarketingClaim.Codes.DISTINCTIVE.getCode(), LocalDate.now().plusDays(1), LocalDate.of(9999,12,31));

		return candidateWorkRequest;
	}

	/**
	 * Builds the candidate work request when the user is turning off distinctive.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that will turn on distinctive.
	 */
	private CandidateWorkRequest handleTurnOffDistinctive(Long productId, Long transactionId,
														  MassUpdateParameters parameters, int sourceSystem) {
		//Validation primopick is active
		boolean promoPickIsActive = this.doValidatePrimoPickActive(productId);
		// Make the work request.
		CandidateWorkRequest candidateWorkRequest = null;
		if(promoPickIsActive){
			candidateWorkRequest = WorkRequestCreatorUtils.getWorkRequestActivationFail(productId,
					parameters.getUserId(), transactionId, sourceSystem, PRIMO_PICK_IS_ACTIVE_MESSAGE);
		}
		else{
			candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
					parameters.getUserId(), transactionId, sourceSystem);
			// Add the product master.
			WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());

			// Also add the distinctive marketing claim.
			WorkRequestCreatorUtils.addMarketingClaimForDistinctive(candidateWorkRequest.getCandidateProductMaster().get(0),
					null, CandidateProductMarketingClaim.TURN_CODE_OFF,
					MarketingClaim.Codes.DISTINCTIVE.getCode(), LocalDate.now().plusDays(1), LocalDate.of(9999,12,31));
		}

		return candidateWorkRequest;
	}

	/**
	 * Validate a product is primo pick when make distinctive. A product is primopick, make distinctive will be break.
	 * @param productId - The product id.
	 * @return {{boolean}}
	 */
	private boolean doValidatePrimoPickActive(Long productId){
		boolean promoPickIsActive= false;
		//Find primo pick active
		ProductMarketingClaim productMarketingClaim = this.productMarketingClaimRepository.findFirstByKeyProdIdAndKeyMarketingClaimCode(productId, PRIMO_PICK_MARKETING_CLAIM_CODE);
		if(productMarketingClaim != null && (PRIMO_PICK_MARKETING_CLAIM_STATUS_CODE_APPROVED.equalsIgnoreCase(StringUtils.trimToEmpty(productMarketingClaim.getMarketingClaimStatusCode()))
				||PRIMO_PICK_MARKETING_CLAIM_STATUS_CODE_SUBMITTED.equalsIgnoreCase(StringUtils.trimToEmpty(productMarketingClaim.getMarketingClaimStatusCode())))){
			promoPickIsActive = true;
		}
		return promoPickIsActive;
	}
}