/*
 *  ShelfAttributesService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.ResourceConstants;
import com.heb.pm.batchUpload.jms.BatchUploadMessageTibcoSender;
import com.heb.pm.entity.*;
import com.heb.pm.jms.MediaMasterXmlHelper;
import com.heb.pm.mediaMasterMessage.MediaMasterEvent;
import com.heb.pm.mediaMasterMessage.MenuLabel;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.CandidateDescriptionRepository;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import com.heb.pm.repository.DescriptionTypeRepository;
import com.heb.pm.repository.ProductDescriptionRepository;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.ws.MasterDataServiceClient;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.SoapException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This holds all of the business logic for shelf attributes.
 *
 * @author l730832
 * @since 2.8.0
 */
@Service
public class ShelfAttributesService {

	private static final String USER_NOT_REJECT_DESCRIPTION_MESSAGE = 
			"User does not have access to reject service case sign description";
	private static final String USER_NOT_APPROVE_DESCRIPTION_MESSAGE = 
			"User does not have access to approve service case sign description";
	private static final String USER_NOT_HAVE_ACCESS_TO_SUBMIT_SERVICE_CASE_MESSAGE =
			"User does not have access to submit service case sign description";
	private static final String CHANGED_APPROVED_DESCRIPTION_MESSAGE = 
			"Approved Service Case Sign Description can only be changed for approved products";
	private static final String BLANK_APPROVED_DESCRIPTION_MESSAGE = 
			"Approved Service Case Sign Description should be blank when Service Case Status is Rejected";
	private static final String MANDATORY_APPROVED_DESCRIPTION_MESSAGE = 
			"Approved Service Case Sign Description should be mandatory when Service Case Status is Approved";
	private static final String MANDATORY_PROPOSED_DESCRIPTION_MESSAGE = 
			"Proposed Service Case Sign Description should be mandatory when Service Case Status is Submitted";

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private DescriptionTypeRepository descriptionTypeRepository;

	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;

	@Autowired
	private ProductDescriptionRepository productDescriptionRepository;

	@Autowired
	private CandidateDescriptionRepository candidateDescriptionRepository;

	@Autowired
    private MasterDataServiceClient masterDataServiceClient;

	@Autowired
	private ProductInfoResolver productInfoResolver = new ProductInfoResolver();

	@Autowired
	private BatchUploadMessageTibcoSender messageTibcoSender;

	@Autowired
	private MediaMasterXmlHelper mediaMasterXmlHelper;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	public static final Integer  SHELF_CASE_TAG_INTENT_CD = 27;

	private ProductMarketingClaim constructProductMarketingClaim(Long productId, String status, String userId,
																 String code,
																 LocalDate effectiveDate, LocalDate endDate, String statusChangeReason) {

		ProductMarketingClaim productMarketingClaim = new ProductMarketingClaim();
		productMarketingClaim.setKey(new ProductMarketingClaimKey());
		productMarketingClaim.getKey().setProdId(productId);
		productMarketingClaim.getKey().setMarketingClaimCode(code);
		productMarketingClaim.setMarketingClaimStatusCode(status);
		productMarketingClaim.setLastUpdateId(userId);
		productMarketingClaim.setPromoPickEffectiveDate(effectiveDate);
		productMarketingClaim.setPromoPickExpirationDate(endDate);
		productMarketingClaim.setStatusChangeReason(statusChangeReason);

		return productMarketingClaim;
	}

	/**
	 * This takes in a product master from the front end that holds only the changed values of the shelf attributes screen.
	 *
	 * @param productMaster A product master from the front end.
	 * @return
	 */
	// Needs to be transactional since it reads and then refreshes the product master object.
	@CoreTransactional
	public ProductMaster updateShelfAttributeChanges(ProductMaster productMaster, UserInfo user) {
		boolean coolFlag = false;

		ProductMaster originalProductMaster = this.productInfoRepository.findOne(productMaster.getProdId());

		// See if they've changed the tag type and if they have permission to change it.
		if (productMaster.getGoodsProduct()!= null && productMaster.getGoodsProduct().getTagType() != null) {
			if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_TAG_TYPE)) {
				throw new IllegalArgumentException("User does not have permission to update tag type");
			}
		}

		// See if they've updated customer friendly descriptions but don't have the permissions.
		if (productMaster.getProductDescriptions()!= null && productMaster.getProductDescriptions().size() > 0) {

			for (ProductDescription pd : productMaster.getProductDescriptions()) {
				if (pd.getKey().getDescriptionType().equals(DescriptionType.CUSTOMER_FRIENDLY_DESCRIPTION_LINE_ONE) ||
						pd.getKey().getDescriptionType().equals(DescriptionType.CUSTOMER_FRIENDLY_DESCRIPTION_LINE_TWO)) {
					if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_CUSTOMER_FRIENDLY_DESCRIPTION)) {
						throw new IllegalArgumentException("User does not have permission to update customer friendly descriptions");
					}
				}
				if (pd.getKey().getDescriptionType().equals(DescriptionType.SERVICE_CASE_CALLOUT)) {
					if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_SERVICE_CASE_CALLOUT)) {
						throw new IllegalArgumentException("User does not have permission to update service case callout");
					}
				}
			}

		}

		// These will be used when looking at changes coming in for the different marketing claims.
		ProductMarketingClaim existingDistinctiveMarketingClaim = null;
		ProductMarketingClaim existingGoLocalMarketingClaim = null;
		ProductMarketingClaim existingPrimoPickMarketingClaim = null;

		for (ProductMarketingClaim marketingClaim : originalProductMaster.getProductMarketingClaims()) {
			if (marketingClaim.getKey().getMarketingClaimCode().equals(MarketingClaim.Codes.GO_LOCAL.getCode())) {
				existingGoLocalMarketingClaim = marketingClaim;
			}
			if (marketingClaim.getKey().getMarketingClaimCode().equals(MarketingClaim.Codes.DISTINCTIVE.getCode())) {
				existingDistinctiveMarketingClaim = marketingClaim;
			}
			if (marketingClaim.getKey().getMarketingClaimCode().equals(MarketingClaim.Codes.PRIMO_PICK.getCode())) {
				existingPrimoPickMarketingClaim = marketingClaim;
			}
		}

		List<ProductMarketingClaim> productMarketingClaims = new LinkedList<>();

		// If they've modified the go local, add the change to the marketing claim.
		if (productMaster.getGoLocal() != null) {

			boolean isGoLocal = existingGoLocalMarketingClaim != null;
			if (isGoLocal != productMaster.getGoLocal()) {
				if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_GO_LOCAL)) {
					throw new IllegalArgumentException("User does not have access to modify Go Local");
				}
				ProductMarketingClaim goLocalMarketingClaim = this.constructProductMarketingClaim(productMaster.getProdId(),
						ProductMarketingClaim.APPROVED, user.getUserId(), MarketingClaim.Codes.GO_LOCAL.getCode(),
						null, null, null);
				if (!productMaster.getGoLocal()) {
					goLocalMarketingClaim.setAction(ProductMarketingClaim.DELETE);
				}
				productMarketingClaims.add(goLocalMarketingClaim);
			}
		}

		// Validate primo pick properties if they are being set.
		if (productMaster.getPrimoPickProperties() != null) {

			// Handle a change to the distinctive status.
			boolean isDistinctive = existingDistinctiveMarketingClaim != null;

			if (isDistinctive != productMaster.getPrimoPickProperties().getDistinctive()) {
				if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_DISTINCTIVE)) {
					throw new IllegalArgumentException("User does not have permission to mark a product as distinctive");
				}
				ProductMarketingClaim distinctiveMarketingClaim = this.constructProductMarketingClaim(productMaster.getProdId(),
						ProductMarketingClaim.APPROVED, user.getUserId(), MarketingClaim.Codes.DISTINCTIVE.getCode(),
						null, null, null);
				if (!productMaster.getPrimoPickProperties().getDistinctive()) {
					distinctiveMarketingClaim.setAction(ProductMarketingClaim.DELETE);
					if(productMaster.getPrimoPickProperties().getPrimoPickExpirationDate()!=null){
						if(productMaster.getPrimoPickProperties().getPrimoPickExpirationDate().isAfter(LocalDate.now())){
							productMaster.getPrimoPickProperties().setPrimoPickExpirationDate(LocalDate.now());
						}
					}
					if(productMaster.getPrimoPickProperties().getPrimoPickEffectiveDate() != null){
						if(productMaster.getPrimoPickProperties().getPrimoPickEffectiveDate().isAfter(productMaster.getPrimoPickProperties().getPrimoPickExpirationDate())){
							productMaster.getPrimoPickProperties().setPrimoPickEffectiveDate(productMaster.getPrimoPickProperties().getPrimoPickExpirationDate());
						}
					}
				}
				productMarketingClaims.add(distinctiveMarketingClaim);
			}

			// If there's no primo pick status, then they're just messing with distinctive
			if (productMaster.getPrimoPickProperties().getPrimoPickStatus() != null) {
				// Handle submitting a primo-pick
				if (productMaster.getPrimoPickProperties().getPrimoPickStatus().equals(ProductMarketingClaim.SUBMITTED.trim())) {
					if (existingPrimoPickMarketingClaim!=null
							&& !ProductMarketingClaim.SUBMITTED.trim().equals(existingPrimoPickMarketingClaim.getMarketingClaimStatusCode().trim())
							&& !user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_SUBMIT_PRIMO_PICK)) {
						throw new IllegalArgumentException("User does not have permission to submit a Primo Pick");
					}
					if (existingPrimoPickMarketingClaim!=null  && existingPrimoPickMarketingClaim.getMarketingClaimStatusCode().trim().equals(ProductMarketingClaim.APPROVED.trim())) {
						throw new IllegalArgumentException("An existing approved Primo Pick cannot be submitted. " +
								"Please use End Date to manage the Primo Pick status.");
					}
					if (!productMaster.getPrimoPickProperties().getDistinctive()){
						throw new IllegalArgumentException("Distinctive flag is required for submitted Primo Picks");
					}
					if (StringUtils.isBlank(productMaster.getPrimoPickProperties().getPrimoPickProposedDescription())){
						throw new IllegalArgumentException("Primo Pick Story description is required for submitted Primo Picks");
					}
					productMarketingClaims.add(constructProductMarketingClaim(productMaster.getProdId(),
							ProductMarketingClaim.SUBMITTED, user.getUserId(),
							MarketingClaim.Codes.PRIMO_PICK.getCode(), null, null, null));
				}
				// Handle approving a primo-pick
				if (productMaster.getPrimoPickProperties().getPrimoPickStatus().equals(ProductMarketingClaim.APPROVED.trim())) {
					if (existingPrimoPickMarketingClaim!=null
							&& !ProductMarketingClaim.APPROVED.trim().equals(existingPrimoPickMarketingClaim.getMarketingClaimStatusCode().trim())
							&& !user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_APPROVE_PRIMO_PICK)) {
						throw new IllegalArgumentException("User does not have permission to approve a Primo Pick");
					}
					// Validate that the primo pick meets all the rules.
					if((existingPrimoPickMarketingClaim==null
							|| (!StringUtils.trim(existingPrimoPickMarketingClaim.getMarketingClaimStatusCode()).equals(ProductMarketingClaim.APPROVED.trim()))
							|| (existingPrimoPickMarketingClaim.getPromoPickExpirationDate() != null
								&& !existingPrimoPickMarketingClaim.getPromoPickExpirationDate().isAfter(LocalDate.now())
								&& productMaster.getPrimoPickProperties().getPrimoPickExpirationDate().isAfter(existingPrimoPickMarketingClaim.getPromoPickExpirationDate())))
							&& !productMaster.getPrimoPickProperties().getDistinctive()){
						throw new IllegalArgumentException("A product must be distinctive to be a Primo Pick");
					}

					// Set defaults for effective and end dates.
					if (productMaster.getPrimoPickProperties().getPrimoPickEffectiveDate() == null) {
						productMaster.getPrimoPickProperties().setPrimoPickEffectiveDate(LocalDate.now().plusDays(1));
					}
					if (productMaster.getPrimoPickProperties().getPrimoPickExpirationDate() == null) {
						productMaster.getPrimoPickProperties().setPrimoPickExpirationDate(LocalDate.of(9999,12,31));
					}

					// End date must be after effective date.
					if (productMaster.getPrimoPickProperties().getPrimoPickExpirationDate().isBefore(
							productMaster.getPrimoPickProperties().getPrimoPickEffectiveDate()
					)) {
						throw new IllegalArgumentException("Primo Pick effective date must be before the expiration date");
					}

					// A description is required.
					if (productMaster.getPrimoPickProperties().getPrimoPickDescription() == null ||
							productMaster.getPrimoPickProperties().getPrimoPickDescription().isEmpty()) {
						throw new IllegalArgumentException("Primo Pick shelf tag description is required for approved Primo Picks");
					}

					// Make the description, don't assume it's here.
					productMarketingClaims.add(constructProductMarketingClaim(productMaster.getProdId(),
							ProductMarketingClaim.APPROVED, user.getUserId(),
							MarketingClaim.Codes.PRIMO_PICK.getCode(),
							productMaster.getPrimoPickProperties().getPrimoPickEffectiveDate(),
							productMaster.getPrimoPickProperties().getPrimoPickExpirationDate(), " "));
				}
				if (productMaster.getPrimoPickProperties().getPrimoPickStatus().equals(ProductMarketingClaim.REJECTED.trim())) {
					if (existingPrimoPickMarketingClaim!=null
							&& !ProductMarketingClaim.REJECTED.trim().equals(existingPrimoPickMarketingClaim.getMarketingClaimStatusCode().trim())
							&& !user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_APPROVE_PRIMO_PICK)) {
						throw new IllegalArgumentException("User does not have permission to reject a Primo Pick");
					}
					if (existingPrimoPickMarketingClaim == null) {
						throw new IllegalArgumentException("Primo Pick in an undefined state, please contact support");
					}
					// Make sure they're not rejecting an existing primo pick.
					if (existingPrimoPickMarketingClaim.getMarketingClaimStatusCode().trim().equals(ProductMarketingClaim.APPROVED.trim())) {
						throw new IllegalArgumentException("An existing approved Primo Pick cannot be rejected. Please use End Date to manage the Primo Pick status.");
					}

					productMarketingClaims.add(constructProductMarketingClaim(productMaster.getProdId(),
							ProductMarketingClaim.REJECTED, user.getUserId(),
							MarketingClaim.Codes.PRIMO_PICK.getCode(), null, null, productMaster.getPrimoPickProperties().getStatusChangeReason()));
				}
			}
		}

		// Validate the user has permissions when editing any of the description attributes.
		if (productMaster.getDescription() != null || productMaster.getSpanishDescription() != null) {
			if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_CUSTOMER_FRIENDLY_DESCRIPTION)) {
				throw new IllegalArgumentException("User does not have permission to update product descriptions");
			}
		}
		for (ProductDescription productDescription : productMaster.getProductDescriptions()) {
			if (productDescription.getKey().getDescriptionType().equals(DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_ONE.getId().trim()) ||
					productDescription.getKey().getDescriptionType().equals(DescriptionType.Codes.TAG_CUSTOMER_FRIENDLY_LINE_TWO.getId().trim())) {
				if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_CUSTOMER_FRIENDLY_DESCRIPTION)) {
					throw new IllegalArgumentException("User does not have permission to update product descriptions");
				}
			}
			if (productDescription.getKey().getDescriptionType().equals(DescriptionType.Codes.COUNTRY_OF_ORIGIN.getId().trim())){
				coolFlag = true;
			}
		}

		// Update any service case sign data.
		if (productMaster.getServiceCaseSign() != null) {
			productMaster.getProductDescriptions().addAll(this.updateServiceCaseTagData(productMaster.getServiceCaseSign(), user));
		}

		//Update organic
		List<NutritionalClaims> nutritionalClaims = new ArrayList<>();
		if(productMaster.getSellingUnits() != null){
			for (SellingUnit sellingUnit : productMaster.getSellingUnits()) {
				if(sellingUnit.getNutritionalClaims() != null && !sellingUnit.getNutritionalClaims().isEmpty()){
					nutritionalClaims.addAll(sellingUnit.getNutritionalClaims());
				}
			}
		}

		// Call to webservice to update
		this.productManagementServiceClient.updateShelfAttributes(productMaster);

		try {
			this.productAttributeManagementServiceClient.updateShelfAttributes(productMarketingClaims, nutritionalClaims, user.getUserId());
		} catch (Exception e) {
			String message = String.format("Primo Pick descriptions were updated, but the status update failed, please contact support [%s].", e.getLocalizedMessage());
			throw new SoapException(message, e.getCause());
		}

		//send msg to media master when cool is changed.
		if(coolFlag){
			MediaMasterEvent mediaMasterEvent = new MediaMasterEvent();
			mediaMasterEvent.setHeader(this.mediaMasterXmlHelper.generateMediaMasterEventHeader());
			mediaMasterEvent.setBody(this.mediaMasterXmlHelper.generateMediaMasterEventBody(new MenuLabel
					(BigInteger.valueOf(productMaster.getProdId()), DescriptionType.Codes.COUNTRY_OF_ORIGIN.getId().trim())));
			this.messageTibcoSender.sendMesageToJMSQueue(this.mediaMasterXmlHelper.marshallFulfillmentEvent(mediaMasterEvent));
		}

		// Refresh the Product Master entity.
		this.entityManager.refresh(originalProductMaster);
		if (originalProductMaster.getGoodsProduct() != null) {
			this.entityManager.refresh(originalProductMaster.getGoodsProduct());
		}
		originalProductMaster.getProductMarketingClaims().forEach(this.entityManager::refresh);
		originalProductMaster.getProductDescriptions().forEach(this.entityManager::refresh);
		originalProductMaster.getSellingUnits().forEach(this.entityManager::refresh);
		originalProductMaster.getSellingUnits().forEach(sellingUnit -> {
			if(sellingUnit.getNutritionalClaims() != null) {
				sellingUnit.getNutritionalClaims().size();
			}
		});
		this.productInfoResolver.fetch(originalProductMaster);
		return originalProductMaster;
	}

	/**
	 * Returns a list of description types.
	 *
	 * @return List of description types.
	 */
	public List<DescriptionType> getListOfDescriptionTypes() {
		return this.descriptionTypeRepository.findAll();
	}

    /**
     * Gets the status of a Service Case Tag based on Intent and ProductId
     * @param productId
     * @return
     */
	public CandidateWorkRequest getExistingServieCaseDescriptionWorkRequest(Long productId) {

		List<CandidateWorkRequest> candidateWorkRequests = this.candidateWorkRequestRepository.findByIntentAndProductIdInAndStatusNot(
				SHELF_CASE_TAG_INTENT_CD, Arrays.asList(productId),CandidateWorkRequest.REQUEST_STATUS_DELETED);

		if (!candidateWorkRequests.isEmpty()) {
			return candidateWorkRequests.get(0);
		}
		return null;
	}

	/**
	 * Returns whether or not the approved service case sign description has changed from what's in the DB.
	 *
	 * @param productId The product ID to look for.
	 * @param serviceCaseSignDescription The approved service case sign description the user has sent up.
	 * @return True if it has changed and false otherwise.
	 */
	private boolean hasApprovedServiceCaseSignChanged(Long productId, String serviceCaseSignDescription) {
		String approvedDescription = this.getApprovedDescription(productId);
		if (StringUtils.isNotBlank(approvedDescription)) {
			return !approvedDescription.equals(serviceCaseSignDescription);
		}
		return StringUtils.isNotBlank(serviceCaseSignDescription);
	}


	/**
     * Saves the workflow data related to service case sign description for a product. When the description is
	 * approved, this returns the descriptions that need to be saved to prod_des_txt.
	 *
     * @param serviceCaseSign The service case sign data to save.
     * @param user The object that represents the logged-in user.
     * @return A list of product descriptions to save. The list is guaranteed to be not-null, but it may be empty
	 * if nothing needs to be persisted to prod_des_txt.
     */
    @CoreTransactional
    public List<ProductDescription> updateServiceCaseTagData(ServiceCaseSign serviceCaseSign, UserInfo user){
		String status = serviceCaseSign.getStatus();
		// Find the existing request if it's there.
		CandidateWorkRequest workRequest =
				this.getExistingServieCaseDescriptionWorkRequest(serviceCaseSign.getProductId());

    	// See if the approved description has changed.
		boolean approvedDescriptionChanged = this.hasApprovedServiceCaseSignChanged(serviceCaseSign.getProductId(),
				serviceCaseSign.getApprovedDescription());

		// If there is no status, the default is working.
		if (serviceCaseSign.getStatus() == null) {
			serviceCaseSign.setStatus(CandidateWorkRequest.StatusCode.IN_PROGRESS.getName());
		}

		// Regardless of what passed in from the front-end, if the user does not have permission to approve, set
		// the status to submitted.
		if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_APPROVE_CASE_SIGN)) {
			serviceCaseSign.setStatus(CandidateWorkRequest.StatusCode.IN_PROGRESS.getName());
		}

		// If the approved description changed and it's not in a status of approved, throw an error.
		if (approvedDescriptionChanged && !serviceCaseSign.getStatus().equals(CandidateWorkRequest.REQUEST_STATUS_PASS)) {
			if (!(serviceCaseSign.getStatus().equals(CandidateWorkRequest.REQUEST_STATUS_REJECTED)
					&& StringUtils.isEmpty(serviceCaseSign.getApprovedDescription()))) {
				throw new IllegalArgumentException(CHANGED_APPROVED_DESCRIPTION_MESSAGE);
			}
		}

		long statusChange = 0;

		// Validate the user has the right permissions.
		if (serviceCaseSign.getStatus().equals(CandidateWorkRequest.StatusCode.IN_PROGRESS.getName())) {
			statusChange = CandidateStatus.STAT_CHG_RSN_ID_WRKG;
			if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_SUBMIT_SERVICE_CASE_SIGN)) {
				throw new IllegalArgumentException(USER_NOT_HAVE_ACCESS_TO_SUBMIT_SERVICE_CASE_MESSAGE);
			}
			// if the request is submitted, an proposed description is required.
			if (!CandidateStatus.REQUEST_STATUS_REJECTED.equals(status)
					&& !CandidateStatus.REQUEST_STATUS_APPROVED.equals(status)
					&& StringUtils.isEmpty(serviceCaseSign.getProposedDescription())) {
				throw new IllegalArgumentException(MANDATORY_PROPOSED_DESCRIPTION_MESSAGE);
			}
		}
		if (serviceCaseSign.getStatus().equals(CandidateWorkRequest.REQUEST_STATUS_PASS)) {
			statusChange = CandidateStatus.STAT_CNG_RSN_ID_ACTIVATED;
			if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_APPROVE_CASE_SIGN)) {
				throw new IllegalArgumentException(USER_NOT_APPROVE_DESCRIPTION_MESSAGE);
			}
			// if the request is approved, an approved description is required.
			if (StringUtils.isEmpty(serviceCaseSign.getApprovedDescription())) {
				throw new IllegalArgumentException(MANDATORY_APPROVED_DESCRIPTION_MESSAGE);
			}
		}
		if (serviceCaseSign.getStatus().equals(CandidateWorkRequest.REQUEST_STATUS_REJECTED)) {
			statusChange = CandidateStatus.STAT_CNG_RSN_ID_REJECTED;
			if (!user.canUserEditResource(ResourceConstants.SHELF_ATTRIBUTES_APPROVE_CASE_SIGN)) {
				throw new IllegalArgumentException(USER_NOT_REJECT_DESCRIPTION_MESSAGE);
			}
			// if the request is rejected, an approved description is blank.
			if (StringUtils.isNotEmpty(serviceCaseSign.getApprovedDescription())) {
				throw new IllegalArgumentException(BLANK_APPROVED_DESCRIPTION_MESSAGE);
			}
		}

		//Apply rule for #PM-1059
		if(StringUtils.EMPTY.equals(serviceCaseSign.getProposedDescription())
				&& (CandidateStatus.REQUEST_STATUS_REJECTED.equals(status)
				|| CandidateStatus.REQUEST_STATUS_APPROVED.equals(status))) {
			serviceCaseSign.setStatus(status);
		}

		boolean newRequest = false;
    	if (workRequest == null) {
    		// If it's not, make a new one.
			newRequest = true;
			workRequest = this.createEmptyWorkRequestForServiceCaseSign(serviceCaseSign, user.getUserId());
		} else {
    		// If it is there, update the proposed description.
            workRequest.getCandidateProductMaster().get(0).getCandidateDescriptions().get(0).setDescription(serviceCaseSign.getProposedDescription());
        }

        // Update the status of the work request.
		workRequest.setStatus(serviceCaseSign.getStatus());

    	// Add a status change to the work request.
		CandidateStatusKey candidateStatusKey = new CandidateStatusKey();
		candidateStatusKey.setStatus(serviceCaseSign.getStatus());
		candidateStatusKey.setLastUpdateDate(LocalDateTime.now());

		CandidateStatus candidateStatus = new CandidateStatus();
		candidateStatus.setKey(candidateStatusKey);
		candidateStatus.setCandidateWorkRequest(workRequest);
		candidateStatus.setUpdateUserId(user.getUserId());
		candidateStatus.setStatusChangeReason(statusChange);
		workRequest.getCandidateStatuses().add(candidateStatus);

    	// Save the candidate. We only need to directly call the save when it's a new one, Spring will automatically
		// save if it exits. Calling save directly will throw an exception.
		if (newRequest) {
			this.candidateWorkRequestRepository.save(workRequest);
		}

		LinkedList<ProductDescription> productDescriptionsToAdd = new LinkedList<>();

		// If we're approving the description, then create the descriptions to save to the prod_des table
		if (approvedDescriptionChanged){
			productDescriptionsToAdd.add(ProductDescription.from(serviceCaseSign.getProductId(),
					DescriptionType.Codes.PROPOSED_SIGN_ROMANCE_COPY.getId(), "ENG",
					serviceCaseSign.getProposedDescription()));

			productDescriptionsToAdd.add(ProductDescription.from(serviceCaseSign.getProductId(),
					DescriptionType.Codes.SIGN_ROMANCE_COPY.getId(), "ENG",
					serviceCaseSign.getApprovedDescription()));
		}

		return productDescriptionsToAdd;
    }

	/**
	 * Fetches service case sign data for a product.
	 *
	 * @param productId The ID of the product to look for data for.
	 * @return The service case sign data for a product. This is guaranteed to be not-null, but may be empty.
	 */
	public ServiceCaseSign getServiceCaseSignData(Long productId) {

    	ServiceCaseSign serviceCaseSign = new ServiceCaseSign();
    	serviceCaseSign.setProductId(productId);
		CandidateWorkRequest workRequest = this.getExistingServieCaseDescriptionWorkRequest(productId);
		if (workRequest != null) {
			serviceCaseSign.setStatus(workRequest.getStatus());
			serviceCaseSign.setProposedDescription(
					workRequest.getCandidateProductMaster().get(0).getCandidateDescriptions().get(0).getDescription());
		}
		serviceCaseSign.setApprovedDescription(this.getApprovedDescription(productId));

		return serviceCaseSign;
	}
    /**
     * Creates an empty work request for service case tag.
     *
     * @param serviceCaseSign The service case sign data to create work request data about.
     * @param userId The ID of the logged in user.
     * @return A candidate work request for the service case sign data.
     */
    public CandidateWorkRequest createEmptyWorkRequestForServiceCaseSign(ServiceCaseSign serviceCaseSign, String userId){
        CandidateWorkRequest workRequest = new CandidateWorkRequest();

        workRequest.setProductId(serviceCaseSign.getProductId());
        workRequest.setCreateDate(LocalDateTime.now());
        workRequest.setUserId(userId);
        workRequest.setLastUpdateDate(workRequest.getCreateDate());
        workRequest.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        workRequest.setReadyToActivate(Boolean.FALSE);
        workRequest.setIntent(ShelfAttributesService.SHELF_CASE_TAG_INTENT_CD);
        workRequest.setSourceSystem(CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT);
        workRequest.setStatus(serviceCaseSign.getStatus());
        workRequest.setLastUpdateUserId(userId);

        CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
        candidateProductMaster.setCandidateWorkRequest(workRequest);
        candidateProductMaster.setProductId(serviceCaseSign.getProductId());
        candidateProductMaster.setLstUpdtUsrId(userId);
        candidateProductMaster.setLastUpdateTs(LocalDateTime.now());

		workRequest.getCandidateProductMaster().add(candidateProductMaster);

        CandidateDescriptionKey candidateDescriptionKey = new CandidateDescriptionKey();
        candidateDescriptionKey.setLanguageType(ProductDescription.ENGLISH);
        candidateDescriptionKey.setDescriptionType(DescriptionType.Codes.PROPOSED_SIGN_ROMANCE_COPY.getId());

        CandidateDescription candidateDescription = new CandidateDescription();
        candidateDescription.setKey(candidateDescriptionKey);
        candidateDescription.setDescription(serviceCaseSign.getProposedDescription());
        candidateDescription.setCandidateProductMaster(candidateProductMaster);
        candidateDescription.setLastUpdateUserId(userId);
        candidateDescription.setLastUpdateDate(LocalDateTime.now());

        candidateProductMaster.getCandidateDescriptions().add(candidateDescription);

        return workRequest;
    }

    /**
     * Gets the approved description by productId, descriptonType, and language.
     * Will return null if no results are found.
	 *
     * @param productId The ID of the product to look for a service case sign description of.
     * @return The approved service case sign description for this product. This may be null.
     */
	public String getApprovedDescription(Long productId){
		List<ProductDescription> productDescriptions = this.productDescriptionRepository
				.findByKeyProductIdAndAndKeyDescriptionTypeInAndKeyLanguageType(productId,
						new ArrayList<>(Arrays.asList(DescriptionType.Codes.SIGN_ROMANCE_COPY.getId())),
						ProductDescription.ENGLISH);
		if(productDescriptions.size() != 0){
			return productDescriptions.get(0).getDescription();
		}
		return null;
	}
}
