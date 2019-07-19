/*
 *  ShowOnSiteWorkRequestCreator
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewUtil;
import com.heb.pm.repository.MasterDataExtensionAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles mass update requests for show on site.
 *
 * @author vn70529
 * @since 2.26.0
 */
@Service
public class ShowOnSiteWorkRequestCreator implements WorkRequestCreator {
    private static final String NON_PUBLISHED_PRODUCT_ERROR_MESSAGE = "The product is non-published.";
    private static final String DATE_INVALID_ERROR_MESSAGE = "The date is invalid.";
    @Autowired
    private MasterDataExtensionAttributeRepository masterDataExtensionAttributeRepository;
    @Value("${app.ecommerce.sourceSystemId}")
    private int eCommerceSourceSystem;
    private static final LocalDate MAX_END_DATE = LocalDate.of(9999, 12, 31);
    /**
     * Creates a candidate work request for inserting show on site case.
     *
     * @param productId     The product ID the request is for.
     * @param transactionId The transaction ID being used to group all the requests together.
     * @param parameters    The parameters the user wants to set the different values to.
     * @param sourceSystem  The ID of this system.
     * @return A CandidateWorkRequest with the necessary attributes to update show on site.
     */
    @Override
    public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters, int sourceSystem) {
        CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
                parameters.getUserId(), transactionId, sourceSystem);
        // Check product is published or non published.
        if (isProductPublished(productId)) {
            if(!isValidDate(parameters)){
                // The date is invalid.
                setCandidateErrorStatus(candidateWorkRequest, DATE_INVALID_ERROR_MESSAGE);
                return candidateWorkRequest;
            }
            // Create work request between a published product and the list of sale channels.
            createWorkRequestWithSaleChannels(parameters, candidateWorkRequest);
        } else {
            // Set error code if product is not publish
            setCandidateErrorStatus(candidateWorkRequest, NON_PUBLISHED_PRODUCT_ERROR_MESSAGE);
        }
        return candidateWorkRequest;
    }

    /**
     * Check the effective date and end date are valid or not.
     * @param parameters the parameters that holds the effective date and end date.
     * @return true if the date is valid or false.
     */
    private boolean isValidDate(MassUpdateParameters parameters){
        if(parameters.getBooleanValue()) {
            // Case 1: Sow On Site Flag equal to TRUE.
            // if the effective or end date occurred null, it is invalid
            if (parameters.getEffectiveDate() == null || parameters.getEndDate() == null) {
                return false;
            }
            // if the effective date occurred before current, it is invalid
            if (parameters.getEffectiveDate().isBefore(LocalDate.now())) {
                return false;
            }
            // if the end date is less than or equal to effective date, it is invalid
            if (parameters.getEndDate().isEqual(parameters.getEffectiveDate()) ||
                    parameters.getEndDate().isBefore(parameters.getEffectiveDate())) {
                return false;
            }
            // if the end date is greater than 9999/12/31, it is invalid
            if (parameters.getEndDate().isAfter(MAX_END_DATE)) {
                return false;
            }
            // valid
            return true;
        }
        // Case 2: Show On Site Flag equal to FALSE.
        // if the effective or end date occurred null, it is invalid
        if (parameters.getEndDate() == null) {
            return false;
        }
        // if the end date is greater than 9999/12/31, it is invalid
        if (parameters.getEndDate().isAfter(MAX_END_DATE)) {
            return false;
        }
        // valid
        return true;
    }
    /**
     * Set error status and error message to candidate status table candidate work request in the cases:
     * product is not published or the effective date or  end date is invalid.
     * It will set the error message to candidate status's comment text and 109 error code to candidate status's status
     * and candidate work request's status.
     * @param candidateWorkRequest the candidate work request holds the candidate status.
     * @param errorMessage the error message
     */
    private void setCandidateErrorStatus(CandidateWorkRequest candidateWorkRequest, String errorMessage) {
        candidateWorkRequest.getCandidateStatuses().get(0).getKey().setStatus(CandidateStatusKey.StatusCode.FAILURE.getName());
        candidateWorkRequest.getCandidateStatuses().get(0).setCommentText(errorMessage);
        candidateWorkRequest.setStatus(CandidateStatusKey.StatusCode.FAILURE.getName());
    }

    /**
     * Create candidate work request with the list of sale channels.
     *
     * @param parameters the parameters.
     */
    private void createWorkRequestWithSaleChannels(MassUpdateParameters parameters, CandidateWorkRequest candidateWorkRequest) {
        if (parameters.getSelectedSaleChannels() != null) {
            // create candidate product master.
            WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());
            for (SalesChannel salesChannel : parameters.getSelectedSaleChannels()) {
                // Create candidate product online
                CandidateProductOnline candidateProductOnline = new CandidateProductOnline();
                CandidateProductOnlineKey key = new CandidateProductOnlineKey();
                key.setSaleChannelCode(salesChannel.getId());
                candidateProductOnline.setKey(key);
                candidateProductOnline.setShowOnSite(CandidateProductOnline.SHOW_ON_SITE_YES);
                if(parameters.getBooleanValue()) {
                    // Just set effective date when show on site flag equal to TRUE (YES), otherwise it is null.
                    candidateProductOnline.setEffectiveDate(ProductECommerceViewUtil.convertLocalDateToDate(parameters.getEffectiveDate()));
                }
                candidateProductOnline.setExpirationDate(parameters.getEndDate());
                candidateProductOnline.setCreateDate(LocalDateTime.now());
                candidateProductOnline.setCreateUserId(candidateWorkRequest.getUserId());
                candidateProductOnline.setLastUpdateUserId(candidateWorkRequest.getUserId());
                candidateProductOnline.setLastUpdateDate(candidateWorkRequest.getCreateDate());
                // Set ps product master to ps product online.
                candidateProductOnline.setCandidateProductMaster(candidateWorkRequest.getCandidateProductMaster().get(0));
                // Set candidate work request to ps product online.
                candidateProductOnline.setCandidateWorkRequest(candidateWorkRequest);
                // Add ps product online to candidate work request.
                candidateWorkRequest.getCandidateProductOnlines().add(candidateProductOnline);
            }
        }
    }

    /**
     * Check the product is published or not.
     * If product is existing in MasterDataExtensionAttribute that based on key id equals to product id
     * and item product id code equals to "PROD" and source system is 13 then it is published or not.
     * @param productId the product id.
     * @return return true if the product is published or not return false.
     */
    private boolean isProductPublished(Long productId) {
        List<MasterDataExtensionAttribute> masterDataExtensionAttributes = masterDataExtensionAttributeRepository.
                findByKeyIdAndKeyItemProdIdCodeAndKeyDataSourceSystemOrderByKeyAttributeId(productId,
                        MasterDataExtensionAttributeKey.PRODUCT_KEY_TYPE, eCommerceSourceSystem);
        return masterDataExtensionAttributes != null && !masterDataExtensionAttributes.isEmpty();
    }
}
