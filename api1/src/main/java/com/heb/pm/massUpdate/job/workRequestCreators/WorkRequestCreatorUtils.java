package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Some utility functions for the creators to use.
 *
 * @author d116773
 * @since 2.12.0
 */
final class WorkRequestCreatorUtils {

	private static final long WORKING_STATUS = 3l;
	private static final String UPLOAD = "111";
	private static final int MASS_UPDATE_INTENT_ID = 21;
	private static final String ACTIVATION_FAIL_STATUS = "109";
	private static final long REJECT_STATUS = 2l;


	// Private so the class cannot be instantiated.
	private WorkRequestCreatorUtils() {}

	/**
	 * Returns a CandidateWorkRequest for the creators to populate. It sets attributes that are common across all
	 * mass updates.
	 *
	 * @param productId The ID of the product being updated.
	 * @param userId The ID of the user who requested the update.
	 * @param transactionId The transaction ID of the job.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest for the creators to populate
	 */
	public static CandidateWorkRequest getEmptyWorkRequest(Long productId, String userId, Long transactionId,
														   int sourceSystem) {

		CandidateWorkRequest workRequest = new CandidateWorkRequest();

		workRequest.setProductId(productId);
		workRequest.setCreateDate(LocalDateTime.now());
		workRequest.setUserId(userId);
		workRequest.setLastUpdateDate(workRequest.getCreateDate());
		workRequest.setStatusChangeReason(WorkRequestCreatorUtils.WORKING_STATUS);
		workRequest.setReadyToActivate(CandidateWorkRequest.RDY_TO_ACTVD_SW_DEFAULT);
		workRequest.setStatus(WorkRequestCreatorUtils.UPLOAD);
		workRequest.setIntent(WorkRequestCreatorUtils.MASS_UPDATE_INTENT_ID);
		workRequest.setSourceSystem(sourceSystem);
		workRequest.setLastUpdateUserId(userId);
		workRequest.setTrackingId(transactionId);
		TransactionTracker tracker=new TransactionTracker();
		tracker.setTrackingId(transactionId);
		workRequest.setTransactionTracking(tracker);

		// Add a record for the status update table.
		CandidateStatusKey candidateStatusKey = new CandidateStatusKey();
		candidateStatusKey.setStatus(WorkRequestCreatorUtils.UPLOAD);
		candidateStatusKey.setLastUpdateDate(LocalDateTime.now());

		CandidateStatus candidateStatus = new CandidateStatus();
		candidateStatus.setKey(candidateStatusKey);
		candidateStatus.setCandidateWorkRequest(workRequest);
		candidateStatus.setUpdateUserId(userId);
		candidateStatus.setStatusChangeReason(WorkRequestCreatorUtils.WORKING_STATUS);

		workRequest.setCandidateStatuses(new LinkedList<>());
		workRequest.getCandidateStatuses().add(candidateStatus);

		return workRequest;
	}

	/**
	 * Adds a candidate product master to a candidate work request and ties the two together.
	 *
	 * @param candidateWorkRequest The work request to add a product master to.
	 */
	public static void addProductMaster(CandidateWorkRequest candidateWorkRequest, String userId) {

		if (candidateWorkRequest.getCandidateProductMaster() == null) {
			candidateWorkRequest.setCandidateProductMaster(new LinkedList<>());
		}

		CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
		candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
		candidateProductMaster.setProductId(candidateWorkRequest.getProductId());
		candidateProductMaster.setLstUpdtUsrId(userId);
		candidateProductMaster.setLastUpdateTs(LocalDateTime.now());

		candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);

	}

	/**
	 * Adds a candidate product description to a work request.
	 *
	 * @param candidateProductMaster The candidate product master to add the description to.
	 * @param userId The ID of the user adding the description.
	 * @param type The type of description it is.
	 * @param description The  actual description of the product.
	 */
	public static void addDescription(CandidateProductMaster candidateProductMaster, String userId, String type,
									  String description) {

		if (candidateProductMaster.getCandidateDescriptions() == null) {
			candidateProductMaster.setCandidateDescriptions(new LinkedList<>());
		}

		CandidateDescriptionKey candidateProductDescriptionKey = new CandidateDescriptionKey();
		candidateProductDescriptionKey.setLanguageType(ProductDescription.ENGLISH);
		candidateProductDescriptionKey.setDescriptionType(type);

		CandidateDescription candidateProductDescription = new CandidateDescription();
		candidateProductDescription.setKey(candidateProductDescriptionKey);
		candidateProductDescription.setDescription(description);
		candidateProductDescription.setCandidateProductMaster(candidateProductMaster);
		candidateProductDescription.setLastUpdateUserId(userId);
		candidateProductDescription.setLastUpdateDate(LocalDateTime.now());

		candidateProductMaster.getCandidateDescriptions().add(candidateProductDescription);
	}

	/**
	 * Adds a candidate marketing claim to a candidate product master.
	 *
	 * @param candidateProductMaster The product master to add the candidate to.
	 * @param status The status of the claim (only valid for primo pick).
	 * @param flag The flag to set. True will add the claim and false will remove it.
	 * @param code The code representing the claim to add.
	 * @param effectiveDate The date this code should be effective (only valid for primo pick).
	 * @param endDate The date this code should end (only valid for primo pick).
	 */
	public static void addMarketingClaim(CandidateProductMaster candidateProductMaster, String status,
										 String flag, String code, LocalDate effectiveDate, LocalDate endDate) {

		// If there isn't already a list of claims, create it
		if (candidateProductMaster.getProductMarketingClaims() == null) {
			candidateProductMaster.setProductMarketingClaims(new LinkedList<>());
		}

		// Create the product marketing claim.
		CandidateProductMarketingClaimKey productMarketingClaimKey = new CandidateProductMarketingClaimKey();
		productMarketingClaimKey.setMarketingClaimCode(code);

		CandidateProductMarketingClaim productMarketingClaim = new CandidateProductMarketingClaim();
		productMarketingClaim.setKey(productMarketingClaimKey);

		// If it's a primo pick, copy the status and dates in. These may be null.
		if (code.equals(MarketingClaim.Codes.PRIMO_PICK.getCode())) {
			productMarketingClaim.setStatus(status);
			productMarketingClaim.setEffectiveDate(effectiveDate);
			productMarketingClaim.setExpirationDate(endDate);
		}

		productMarketingClaim.setNewData(flag);
		productMarketingClaim.setCandidateProductMaster(candidateProductMaster);

		// Add the product marketing claim to the product master
		candidateProductMaster.getProductMarketingClaims().add(productMarketingClaim);
	}

	/**
	 * Adds a candidate marketing claim to a candidate product master for reject status.
	 *
	 * @param candidateProductMaster The product master to add the candidate to.
	 * @param status The status of the claim (only valid for primo pick).
	 * @param flag The flag to set. True will add the claim and false will remove it.
	 * @param code The code representing the claim to add.
	 * @param effectiveDate The date this code should be effective (only valid for primo pick).
	 * @param endDate The date this code should end (only valid for primo pick).
	 * @param changeReason The selected reason code for reject status  (only valid for primo pick).
	 */
	public static void addMarketingClaimForRejectStatus(CandidateProductMaster candidateProductMaster, String status,
										 String flag, String code, LocalDate effectiveDate, LocalDate endDate, String changeReason) {

		// If there isn't already a list of claims, create it
		if (candidateProductMaster.getProductMarketingClaims() == null) {
			candidateProductMaster.setProductMarketingClaims(new LinkedList<>());
		}

		// Create the product marketing claim.
		CandidateProductMarketingClaimKey productMarketingClaimKey = new CandidateProductMarketingClaimKey();
		productMarketingClaimKey.setMarketingClaimCode(code);

		CandidateProductMarketingClaim productMarketingClaim = new CandidateProductMarketingClaim();
		productMarketingClaim.setKey(productMarketingClaimKey);

		// If it's a primo pick, copy the status and dates in. These may be null.
		if (code.equals(MarketingClaim.Codes.PRIMO_PICK.getCode())) {
			productMarketingClaim.setStatus(status);
			productMarketingClaim.setEffectiveDate(effectiveDate);
			productMarketingClaim.setExpirationDate(endDate);
			if(changeReason != null){
				productMarketingClaim.setChangeReason(changeReason);
			}
		}

		productMarketingClaim.setNewData(flag);
		productMarketingClaim.setCandidateProductMaster(candidateProductMaster);

		// Add the product marketing claim to the product master
		candidateProductMaster.getProductMarketingClaims().add(productMarketingClaim);
	}

	/**
	 * Adds a candidate marketing claim to a candidate product master for distinctive.
	 *
	 * @param candidateProductMaster The product master to add the candidate to.
	 * @param status The status of the claim (only valid for primo pick).
	 * @param flag The flag to set. True will add the claim and false will remove it.
	 * @param code The code representing the claim to add.
	 * @param effectiveDate The date this code should be effective (only valid for primo pick).
	 * @param endDate The date this code should end (only valid for primo pick).
	 */
	public static void addMarketingClaimForDistinctive(CandidateProductMaster candidateProductMaster, String status,
													   String flag, String code, LocalDate effectiveDate, LocalDate endDate) {

		// If there isn't already a list of claims, create it
		if (candidateProductMaster.getProductMarketingClaims() == null) {
			candidateProductMaster.setProductMarketingClaims(new LinkedList<>());
		}

		// Create the product marketing claim.
		CandidateProductMarketingClaimKey productMarketingClaimKey = new CandidateProductMarketingClaimKey();
		productMarketingClaimKey.setMarketingClaimCode(code);

		CandidateProductMarketingClaim productMarketingClaim = new CandidateProductMarketingClaim();
		productMarketingClaim.setKey(productMarketingClaimKey);

		productMarketingClaim.setStatus(status);
		productMarketingClaim.setEffectiveDate(effectiveDate);
		productMarketingClaim.setExpirationDate(endDate);

		productMarketingClaim.setNewData(flag);
		productMarketingClaim.setCandidateProductMaster(candidateProductMaster);

		// Add the product marketing claim to the product master
		candidateProductMaster.getProductMarketingClaims().add(productMarketingClaim);
	}

	/**
	 * Returns a CandidateWorkRequest for the creators to populate for request fail. It sets attributes that are common across all
	 * mass updates.
	 *
	 * @param productId The ID of the product being updated.
	 * @param userId The ID of the user who requested the update.
	 * @param transactionId The transaction ID of the job.
	 * @param sourceSystem The ID of this system.
	 * @param failMessage The message activation fail.
	 * @return A CandidateWorkRequest for the creators to populate
	 */
	public static CandidateWorkRequest getWorkRequestActivationFail(Long productId, String userId, Long transactionId,
																	int sourceSystem, String failMessage) {

		CandidateWorkRequest workRequest = new CandidateWorkRequest();

		workRequest.setProductId(productId);
		workRequest.setCreateDate(LocalDateTime.now());
		workRequest.setUserId(userId);
		workRequest.setLastUpdateDate(workRequest.getCreateDate());
		workRequest.setStatusChangeReason(WorkRequestCreatorUtils.REJECT_STATUS);
		workRequest.setReadyToActivate(CandidateWorkRequest.RDY_TO_ACTVD_SW_DEFAULT);
		workRequest.setStatus(WorkRequestCreatorUtils.ACTIVATION_FAIL_STATUS);
		workRequest.setIntent(WorkRequestCreatorUtils.MASS_UPDATE_INTENT_ID);
		workRequest.setSourceSystem(sourceSystem);
		workRequest.setLastUpdateUserId(userId);
		workRequest.setTrackingId(transactionId);
		TransactionTracker tracker=new TransactionTracker();
		tracker.setTrackingId(transactionId);
		workRequest.setTransactionTracking(tracker);

		// Add a record for the status update table.
		CandidateStatusKey candidateStatusKey = new CandidateStatusKey();
		candidateStatusKey.setStatus(WorkRequestCreatorUtils.ACTIVATION_FAIL_STATUS);
		candidateStatusKey.setLastUpdateDate(LocalDateTime.now());

		CandidateStatus candidateStatus = new CandidateStatus();
		candidateStatus.setKey(candidateStatusKey);
		candidateStatus.setCandidateWorkRequest(workRequest);
		candidateStatus.setUpdateUserId(userId);
		candidateStatus.setStatusChangeReason(WorkRequestCreatorUtils.REJECT_STATUS);
		candidateStatus.setCommentText(failMessage);

		workRequest.setCandidateStatuses(new LinkedList<>());
		workRequest.getCandidateStatuses().add(candidateStatus);

		return workRequest;
	}
}