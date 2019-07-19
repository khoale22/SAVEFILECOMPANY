/*
 *  ProduceOrganicWorkRequestCreator.java
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.repository.ProductInfoRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Creates work requests that set the Produce Organic attribute.
 *
 * @author vn73545
 * @since 2.40.0
 */
@Service
class ProduceOrganicWorkRequestCreator implements WorkRequestCreator {
	
	private static final int ORGANIC_CLASS_CODE = 42;
	private static final String ORGANIC_SUB_DEPARTMENT_CODE = "09A";
	private static final String NON_PRODUCE_ITEM_MESSAGE = "Could not update Organic for non-produce item.";
	private static final String VESTCOM_MESSAGE = "Could not update Organic from Vestcom.";
	
	@Autowired
	ProductInfoRepository productInfoRepository;

	/**
	 * Creates work requests that set Produce Organic.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest.
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters,
			int sourceSystem) {
		CandidateWorkRequest candidateWorkRequest = null;
		ProductMaster productMaster = this.productInfoRepository.findOne(productId);
		String errorMessage = this.validateProduceOrganicData(productMaster);
		if(StringUtils.isNotEmpty(errorMessage)){
			candidateWorkRequest = WorkRequestCreatorUtils.getWorkRequestActivationFail(productId,
					parameters.getUserId(), transactionId, sourceSystem, errorMessage);
		}else{
			candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
					parameters.getUserId(), transactionId, sourceSystem);
			String actionCode = parameters.getBooleanValue() ? 
					CandidateProductScanCodeNutrient.ACTION_CODE_YES : CandidateProductScanCodeNutrient.ACTION_CODE_NO;
			List<SellingUnit> sellingUnits = productMaster.getSellingUnits();
			if(CollectionUtils.isNotEmpty(sellingUnits)){
				for (SellingUnit sellingUnit : sellingUnits) {
					this.addCandidateProductScanCodeNutrient(candidateWorkRequest, sellingUnit.getUpc(),
							actionCode, parameters.getUserId());
				}
			}
		}
		return candidateWorkRequest;
	}
	
	/**
	 * Validate produce organic data.
	 * @param productMaster - The product master.
	 * @return The error message.
	 */
	private String validateProduceOrganicData(ProductMaster productMaster){
		List<SellingUnit> sellingUnits = productMaster.getSellingUnits();
		if(CollectionUtils.isNotEmpty(sellingUnits)){
			for (SellingUnit sellingUnit : sellingUnits) {
				List<NutritionalClaims> nutritionalClaims = sellingUnit.getNutritionalClaims();
				if(CollectionUtils.isNotEmpty(nutritionalClaims)){
					for (NutritionalClaims nutritionalClaim : nutritionalClaims) {
						if(nutritionalClaim.getSourceSystemId() == SourceSystem.SourceSystemNumber.SOURCE_SYSTEM_VESTCOM.getValue().longValue()
								&& CandidateProductScanCodeNutrient.ORGANIC_NUTRITION_CODE.equals(nutritionalClaim.getKey().getNutritionalClaimsCode())){
							return VESTCOM_MESSAGE;
						}
					}
				}
			}
		}
		if(!(productMaster.getClassCode() == ORGANIC_CLASS_CODE || ORGANIC_SUB_DEPARTMENT_CODE.equals(productMaster.getDepartmentString()))){
			return NON_PRODUCE_ITEM_MESSAGE;
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * Adds a candidate product scan code nutrient to a candidate work request and ties the two together.
	 *
	 * @param candidateWorkRequest The work request to add a product master to.
	 * @param scanCodeId The scan code id.
	 * @param actionCode The action code.
	 * @param userId The user id.
	 */
	private void addCandidateProductScanCodeNutrient(CandidateWorkRequest candidateWorkRequest, Long scanCodeId, String actionCode, String userId) {
		if (candidateWorkRequest.getCandidateProductScanCodeNutrient() == null) {
			candidateWorkRequest.setCandidateProductScanCodeNutrient(new LinkedList<>());
		}
		CandidateProductScanCodeNutrientKey candidateProductScanCodeNutrientKey = new CandidateProductScanCodeNutrientKey();
		candidateProductScanCodeNutrientKey.setScanCodeId(scanCodeId);
		candidateProductScanCodeNutrientKey.setProductNutrientCode(CandidateProductScanCodeNutrient.ORGANIC_NUTRITION_CODE);
		CandidateProductScanCodeNutrient candidateProductScanCodeNutrient = new CandidateProductScanCodeNutrient();
		candidateProductScanCodeNutrient.setCandidateWorkRequest(candidateWorkRequest);
		candidateProductScanCodeNutrient.setKey(candidateProductScanCodeNutrientKey);
		candidateProductScanCodeNutrient.setCreateUserId(userId);
		candidateProductScanCodeNutrient.setCreateDate(LocalDateTime.now());
		candidateProductScanCodeNutrient.setActionCode(actionCode);
		candidateWorkRequest.getCandidateProductScanCodeNutrient().add(candidateProductScanCodeNutrient);
	}
}
