package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.repository.ProductMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Creates work requests to update Primo Pick.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class PrimoPickWorkRequestCreator implements WorkRequestCreator{

	@Autowired
	private ProductMasterRepository productMasterRepository;

	private static final LocalDate FOREVER = LocalDate.of(9999, 12, 31);

	/**
	 * Creates work requests that handle the various types of primo pick operations.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that modify primo pick or distinctive status.
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters,
												  int sourceSystem) {

		CandidateWorkRequest workRequest = null;

		if (parameters.getPrimoPickFunction().equals(MassUpdateParameters.PrimoPickFunction.APPROVE_PRIMO_PICK)) {
			workRequest = this.handleApprovePrimoPick(productId, transactionId, parameters, sourceSystem);
		}

		else if (parameters.getPrimoPickFunction().equals(MassUpdateParameters.PrimoPickFunction.TURN_OFF_PRIMO_PICK)) {
			workRequest = this.handleEndPrimoPick(productId, transactionId, parameters, sourceSystem);
		}

		else if (parameters.getPrimoPickFunction().equals(MassUpdateParameters.PrimoPickFunction.TURN_ON_PRIMO_PICK)) {
			workRequest = this.handleMakePrimoPick(productId, transactionId, parameters, sourceSystem);
		}

		else if (parameters.getPrimoPickFunction().equals(MassUpdateParameters.PrimoPickFunction.REJECT_PRIMO_PICK)) {
			workRequest = this.handleRejectPrimoPick(productId, transactionId, parameters, sourceSystem);
		}

		return workRequest;
	}

	/**
	 * Builds the candidate work request when the user is approving primo picks.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that will approve primo picks.
	 */
	private CandidateWorkRequest handleApprovePrimoPick(Long productId, Long transactionId,
														MassUpdateParameters parameters, int sourceSystem) {

		String primoPickDescription = parameters.primoPickDescriptionFor(productId);

		// Make the work request.
		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);

		// Add the product master.
		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());

		// Add the marketing claim.
		LocalDate effectiveDate = parameters.getEffectiveDate() == null ?
				LocalDate.now() : parameters.getEffectiveDate();

		WorkRequestCreatorUtils.addMarketingClaimForRejectStatus(candidateWorkRequest.getCandidateProductMaster().get(0),
				ProductMarketingClaim.APPROVED, null, MarketingClaim.Codes.PRIMO_PICK.getCode(),
				effectiveDate, PrimoPickWorkRequestCreator.FOREVER, " ");

		// This will also need the product description.
		WorkRequestCreatorUtils.addDescription(candidateWorkRequest.getCandidateProductMaster().get(0),
				parameters.getUserId(), DescriptionType.PRIMO_PICK_SHORT_DESCRIPTION, primoPickDescription);

		return candidateWorkRequest;
	}

	/**
	 * Handles turning on primo pick. This is slightly different than approve in that the primo pick does not have
	 * to be in a pending state and this will automatically turn on distinctive as well.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that will turn on primo picks.
	 */
	private CandidateWorkRequest handleMakePrimoPick(Long productId, Long transactionId,
													 MassUpdateParameters parameters, int sourceSystem) {

		ProductMaster productMaster = this.productMasterRepository.findOne(productId);

		String primoPickDescription = parameters.primoPickDescriptionFor(productId);

		// Make the work request.
		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);

		// Add the product master.
		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());

		LocalDate effectiveDate = parameters.getEffectiveDate() == null ?
				LocalDate.now() : parameters.getEffectiveDate();

		// Add the primo pick marketing claim.
		WorkRequestCreatorUtils.addMarketingClaim(candidateWorkRequest.getCandidateProductMaster().get(0),
				ProductMarketingClaim.APPROVED, CandidateProductMarketingClaim.TURN_CODE_ON,
				MarketingClaim.Codes.PRIMO_PICK.getCode(),
				effectiveDate, PrimoPickWorkRequestCreator.FOREVER);

		// Also add the distinctive marketing claim.
		if (!this.isDistinctive(productMaster)) {
			WorkRequestCreatorUtils.addMarketingClaim(candidateWorkRequest.getCandidateProductMaster().get(0),
					ProductMarketingClaim.APPROVED, CandidateProductMarketingClaim.TURN_CODE_ON,
					MarketingClaim.Codes.DISTINCTIVE.getCode(), null, null);
		}

		// This will also need the product description.
		WorkRequestCreatorUtils.addDescription(candidateWorkRequest.getCandidateProductMaster().get(0),
				parameters.getUserId(), DescriptionType.PRIMO_PICK_SHORT_DESCRIPTION, primoPickDescription);

		return candidateWorkRequest;
	}

	/**
	 * Handles turning on primo pick. This is slightly different than approve in that the primo pick does not have
	 * to be in a pending state and this will automatically turn on distinctive as well.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that will turn on primo picks.
	 */
	private CandidateWorkRequest handleRejectPrimoPick(Long productId, Long transactionId,
													   MassUpdateParameters parameters, int sourceSystem) {

		// Make the work request.
		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);

		// Add the product master.
		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());

		// Add the rejected marketing claim.
		WorkRequestCreatorUtils.addMarketingClaimForRejectStatus(candidateWorkRequest.getCandidateProductMaster().get(0),
				ProductMarketingClaim.REJECTED, CandidateProductMarketingClaim.TURN_CODE_ON,
				MarketingClaim.Codes.PRIMO_PICK.getCode(), null, null, parameters.getChangeReason());

		return candidateWorkRequest;
	}

	/**
	 * Builds the candidate work request when the user is turning off primo pick.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that will turn off primo pick.
	 */
	private CandidateWorkRequest handleEndPrimoPick(Long productId, Long transactionId,
													MassUpdateParameters parameters, int sourceSystem) {

		// Make the work request.
		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);

		// Add the product master.
		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());

		// Set primo pick to expired.
		LocalDate endDate = parameters.getEffectiveDate() == null ?
				LocalDate.now() : parameters.getEffectiveDate();

		WorkRequestCreatorUtils.addMarketingClaim(candidateWorkRequest.getCandidateProductMaster().get(0),
				null, CandidateProductMarketingClaim.UPDATE_CODE, MarketingClaim.Codes.PRIMO_PICK.getCode(),
				null, endDate);

		return candidateWorkRequest;
	}

	/**
	 * Checks to see if the product is a distinctive or not.
	 *
	 * @param productMaster The product to look at.
	 * @return True if the product is distinctive and false otherwise.
	 */
	private boolean isDistinctive(ProductMaster productMaster) {

		if (productMaster == null) {
			return false;
		}

		for (ProductMarketingClaim pmc : productMaster.getProductMarketingClaims()) {
			// If the product has the primo pick marketing claim, and the expiration date is in the future,
			// it is a primo pick and we can turn it off
			if (pmc.getKey().getMarketingClaimCode().equals(MarketingClaim.Codes.DISTINCTIVE.getCode())) {
				return true;
			}
		}

		return false;
	}
}