/*
 * NutritionSetter
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.nutritionIngredients;

import com.heb.pm.entity.*;
import com.heb.pm.repository.CandidateProductPkVariationRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.heb.pm.repository.SellingUnitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * the setter for nutrition upload
 *
 * @author vn87351
 * @since 2.12.0
 */
@Component
public class NutritionSetter {
	private static final int SEQUENCE_ZERO = 0;
	private static final int SEQUENCE_ONE = 1;
	private static final int SEQUENCE_TWO = 2;
	private static final int SEQUENCE_THREE = 3;
	private static final int SEQUENCE_FOUR = 4;
	private static final int SEQUENCE_FIVE = 5;
	private static final int SEQUENCE_SIX = 6;
	private static final int SEQUENCE_SEVEN = 7;
	private static final int SEQUENCE_EIGHT = 8;
	private static final int SEQUENCE_NINE = 9;
	private static final int SEQUENCE_TEN = 10;
	private static final int SEQUENCE_ELEVEN = 11;
	private static final int SEQUENCE_TWELVE = 12;
	private static final int SEQUENCE_THIRTEEN = 13;
	private static final int SEQUENCE_FOURTEEN = 14;
	private static final int SEQUENCE_FIFTEEN = 15;
	
	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	/**
	 * set CandidateStat entity.
	 *
	 * @param candidateWorkRequest CandidateWorkRequest
	 * @param statusCode status batch upload
	 * @param  comments the comment text
	 */
	public void setCandidateStatus(CandidateWorkRequest candidateWorkRequest, CandidateStatusKey.StatusCode statusCode, String comments) {
		candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
		CandidateStatus candidateStatus = new CandidateStatus();
		CandidateStatusKey candidateStatKey = new CandidateStatusKey();
		candidateStatKey.setStatus(statusCode.getName());
		candidateStatKey.setLastUpdateDate(LocalDateTime.now());
		candidateStatus.setKey(candidateStatKey);
		candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
		candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
		candidateStatus.setCommentText(comments);
		candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
		candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
	}

	/**
	 * set CandidateMasterDataExtensionAttribute
	 *
	 * @param nutrient             nutrient
	 * @param candidateWorkRequest candidate work request
	 */
	public void setCandidateMasterDataExtensionAttribute(NutritionBatchUpload nutrient,
															   CandidateWorkRequest candidateWorkRequest) {
		if (nutrient.getErrors().isEmpty()) {
			CandidateMasterDataExtensionAttribute attr = new CandidateMasterDataExtensionAttribute();
			CandidateMasterDataExtensionAttributeKey key = new CandidateMasterDataExtensionAttributeKey();
			key.setWorkRequestId(candidateWorkRequest.getWorkRequestId());
			key.setAttributeId(Long.valueOf(CandidateMasterDataExtensionAttribute.NUTRIENT_ATTRIBUTE_ID));
			key.setKeyId(NumberUtils.toLong(nutrient.getUpcPlu()));
			key.setItemProductKey(CandidateMasterDataExtensionAttribute.ITEM_PRODUCT_KEY_UPC);
			key.setSequenceNumber(0L);
			key.setDataSourceSystem(Long.valueOf(SourceSystem.SOURCE_SYSTEM_PRODUCT_MAINTENANCE));
			attr.setAttributeValueText(nutrient.getIngredientsDescription());
			attr.setNewData("Y");
			attr.setKey(key);
			attr.setCandidateWorkRequest(candidateWorkRequest);
			attr.setCreateDate(LocalDateTime.now());
			attr.setLastUpdateUserId(candidateWorkRequest.getLastUpdateUserId());
			attr.setCreateUserId(candidateWorkRequest.getLastUpdateUserId());
			attr.setLastUpdateDate(LocalDateTime.now());
			candidateWorkRequest.setCandidateMasterDataExtensionAttributes(new ArrayList<>());
			candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(attr);
		}
	}

	/**
	 * set candidateProductPkVariation entity.
	 *
	 * @param nutrient             NutritionBatchUpload
	 * @param candidateWorkRequest CandidateWorkRequest
	 */
	public void setCandidateNutrient(NutritionBatchUpload nutrient,
																	 CandidateWorkRequest candidateWorkRequest) {
		candidateWorkRequest.setCandidateNutrients(new ArrayList<>());
		String servingSizeUOMCode = nutrient.getServingSizeUOMCode();
		//insert total calories without percent
		setCandidateNutrientWithoutPercent(nutrient, NumberUtils.toDouble(nutrient.getTotalCalories()),
				CandidateNutrient.NutrientMaster.Calories.getId(), servingSizeUOMCode, SEQUENCE_ZERO,
				candidateWorkRequest);

		//insert calories from fat without percent
		setCandidateNutrientWithoutPercent(nutrient, NumberUtils.toDouble(nutrient.getCaloriesFromFat()),
				CandidateNutrient.NutrientMaster.CaloriesFromFat.getId(), servingSizeUOMCode, SEQUENCE_ONE,
				candidateWorkRequest);

		//insert total fat with percent
		setCandidateNutrientWithPercent(nutrient, NumberUtils.toDouble(nutrient.getTotalFat()),
				NumberUtils.toDouble(nutrient.getTotalFatDV()),
				CandidateNutrient.NutrientMaster.TotalFat.getId(), servingSizeUOMCode, SEQUENCE_TWO, candidateWorkRequest);


		//insert saturated fat with percent
		setCandidateNutrientWithPercent(nutrient, NumberUtils.toDouble(nutrient.getSaturatedFat()),
				NumberUtils.toDouble(nutrient.getSaturatedFatDV()),
				CandidateNutrient.NutrientMaster.SaturatedFat.getId(), servingSizeUOMCode, SEQUENCE_THREE, candidateWorkRequest);

		//insert trans fat without percent
		setCandidateNutrientWithoutPercent(nutrient, NumberUtils.toDouble(nutrient.getTransFat()),
				CandidateNutrient.NutrientMaster.TransFat.getId(),
				servingSizeUOMCode, SEQUENCE_FOUR, candidateWorkRequest);

		//insert cholesterol with percent
		setCandidateNutrientWithPercent(nutrient, NumberUtils.toDouble(nutrient.getCholesterol()),
				NumberUtils.toDouble(nutrient.getCholesterolDV()),
				CandidateNutrient.NutrientMaster.Cholesterol.getId(), servingSizeUOMCode, SEQUENCE_FIVE, candidateWorkRequest);

		//insert sodium with percent
		setCandidateNutrientWithPercent(nutrient, NumberUtils.toDouble(nutrient.getSodium()),
				NumberUtils.toDouble(nutrient.getSodiumDV()),
				CandidateNutrient.NutrientMaster.Sodium.getId(), servingSizeUOMCode, SEQUENCE_SIX, candidateWorkRequest);

		//insert potassium without percent
		setCandidateNutrientWithoutPercent(nutrient, NumberUtils.toDouble(nutrient.getPotassium()),
				CandidateNutrient.NutrientMaster.Potassium.getId(),
				servingSizeUOMCode, SEQUENCE_SEVEN, candidateWorkRequest);

		//insert carbohydrates with percent
		setCandidateNutrientWithPercent(nutrient, NumberUtils.toDouble(nutrient.getTotalCarbohydrates()),
				NumberUtils.toDouble(nutrient.getTotalCarbohydratesDV()), CandidateNutrient.NutrientMaster.TotalCarbohydrate.getId(),
				servingSizeUOMCode, SEQUENCE_EIGHT, candidateWorkRequest);

		//insert dietary fiber with percent
		setCandidateNutrientWithPercent(nutrient, NumberUtils.toDouble(nutrient.getDietaryFiber()),
				NumberUtils.toDouble(nutrient.getDietaryFiberDV()), CandidateNutrient.NutrientMaster.DietaryFiber.getId(),
				servingSizeUOMCode, SEQUENCE_NINE, candidateWorkRequest);

		//insert sugars without percent
		setCandidateNutrientWithoutPercent(nutrient, NumberUtils.toDouble(nutrient.getSugars()),
				CandidateNutrient.NutrientMaster.Sugars.getId(), servingSizeUOMCode, SEQUENCE_TEN, candidateWorkRequest);

		//insert protein without percent
		setCandidateNutrientWithoutPercent(nutrient, NumberUtils.toDouble(nutrient.getProtein()),
				CandidateNutrient.NutrientMaster.Protein.getId(), servingSizeUOMCode, SEQUENCE_ELEVEN, candidateWorkRequest);

		//insert vitamin A with only percent
		setCandidateNutrientWithOnlyPercent(nutrient, NumberUtils.toDouble(nutrient.getVitaminA()),
				CandidateNutrient.NutrientMaster.VitaminA.getId(), servingSizeUOMCode, SEQUENCE_TWELVE, candidateWorkRequest);

		//insert vitamin C with only percent
		setCandidateNutrientWithOnlyPercent(nutrient, NumberUtils.toDouble(nutrient.getVitaminC()),
				CandidateNutrient.NutrientMaster.VitaminC.getId(), servingSizeUOMCode, SEQUENCE_THIRTEEN, candidateWorkRequest);

		//insert calcium with only percent
		setCandidateNutrientWithOnlyPercent(nutrient, NumberUtils.toDouble(nutrient.getCalcium()),
				CandidateNutrient.NutrientMaster.Calcium.getId(), servingSizeUOMCode, SEQUENCE_FOURTEEN, candidateWorkRequest);

		//insert iron with only percent
		setCandidateNutrientWithOnlyPercent(nutrient, NumberUtils.toDouble(nutrient.getIron()),
				CandidateNutrient.NutrientMaster.Iron.getId(), servingSizeUOMCode, SEQUENCE_FIFTEEN, candidateWorkRequest);
	}

	/**
	 * convert to nutrient with percent
	 *
	 * @param nutrient             nutrient
	 * @param ntrntQty             nutrient Qty
	 * @param ntrntPct             nutrient percent
	 * @param ntrntMstrID          nutrient master id
	 * @param uomCode              uom code
	 * @param seqNbr               sequence
	 * @param candidateWorkRequest candidate request
	 */
	public void setCandidateNutrientWithPercent(NutritionBatchUpload nutrient, double ntrntQty, double ntrntPct, int ntrntMstrID,
												String uomCode, long seqNbr, CandidateWorkRequest candidateWorkRequest) {
		CandidateNutrient candidate = setCandidateNutrient(nutrient, ntrntQty, ntrntMstrID, uomCode, seqNbr, candidateWorkRequest);
		candidate.setValPreprdTypCd(CandidateNutrient.VALUE_PREPRD_TYPE_CODE);
		candidate.setDalyValSrvngPct(ntrntPct);
		candidateWorkRequest.getCandidateNutrients().add(candidate);
	}

	/**
	 * convert to nutrient with out percent
	 *
	 * @param nutrient             nutrient
	 * @param ntrntQty             nutrient Qty
	 * @param ntrntMstrID          nutrient master id
	 * @param uomCode              uom code
	 * @param seqNbr               sequence
	 * @param candidateWorkRequest candidate request
	 */
	public void setCandidateNutrientWithoutPercent(NutritionBatchUpload nutrient, double ntrntQty, int ntrntMstrID,
												   String uomCode, long seqNbr, CandidateWorkRequest candidateWorkRequest) {
		CandidateNutrient candidate = setCandidateNutrient(nutrient, ntrntQty, ntrntMstrID, uomCode, seqNbr, candidateWorkRequest);
		candidate.setValPreprdTypCd(CandidateNutrient.VALUE_PREPRD_TYPE_CODE);
		candidateWorkRequest.getCandidateNutrients().add(candidate);
	}

	/**
	 * convert to nutrient with only percent
	 *
	 * @param nutrient             nutrient
	 * @param ntrntMstrID          nutrient master id
	 * @param uomCode              uom code
	 * @param seqNbr               sequence
	 * @param candidateWorkRequest candidate request
	 */
	public void setCandidateNutrientWithOnlyPercent(NutritionBatchUpload nutrient, double ntrntPct, int ntrntMstrID,
													String uomCode, long seqNbr, CandidateWorkRequest candidateWorkRequest) {
		CandidateNutrient candidate = setCandidateNutrient(nutrient, null, ntrntMstrID, uomCode, seqNbr, candidateWorkRequest);
		candidate.setValPreprdTypCd(CandidateNutrient.VALUE_PREPRD_TYPE_CODE);
		candidate.setDalyValSrvngPct(ntrntPct);
		candidateWorkRequest.getCandidateNutrients().add(candidate);
	}

	/**
	 * convert to nutrient common
	 *
	 * @param nutrient             nutrient
	 * @param ntrntQty             nutrient Qty
	 * @param ntrntMstrID          nutrient master id
	 * @param uomCode              uom code
	 * @param seqNbr               sequence
	 * @param candidateWorkRequest candidate request
	 * @return
	 */
	private CandidateNutrient setCandidateNutrient(NutritionBatchUpload nutrient, Double ntrntQty, int ntrntMstrID,
												   String uomCode, long seqNbr, CandidateWorkRequest candidateWorkRequest) {
		CandidateNutrient candidate = new CandidateNutrient();
		CandidateNutrientKey key = new CandidateNutrientKey();
		key.setCandidateWorkId(candidateWorkRequest.getWorkRequestId());
		key.setSequence(seqNbr);
		key.setUpc(NumberUtils.toLong(nutrient.getUpcPlu()));
		candidate.setKey(key);
		candidate.setMasterId(ntrntMstrID);
		candidate.setNutrientQuantity(ntrntQty);
		candidate.setUomCode(uomCode);
		candidate.setSourceSystem(SourceSystem.SOURCE_SYSTEM_PRODUCT_MAINTENANCE);
		candidate.setDclrOnLblSw(CandidateNutrient.NUTRIENT_LBL_SWITCH);
		candidate.setNtrntMeasrTxt(CandidateNutrient.NUTRIENT_MEASR_EXACT);
		candidate.setCandidateWorkRequest(candidateWorkRequest);
		return candidate;
	}

	/**
	 * set data To candidateProductPkVariation entity.
	 *
	 * @param nutrient             NutritionBatchUpload
	 * @param candidateWorkRequest CandidateWorkRequest
	 */
	public void setCandidateProductPkVariation(NutritionBatchUpload nutrient, CandidateWorkRequest candidateWorkRequest) {
		CandidateProductPkVariation candidate = new CandidateProductPkVariation();
		CandidateProductPkVariationKey key = new CandidateProductPkVariationKey();
		key.setCandidateWorkRequestId(candidateWorkRequest.getWorkRequestId());
		key.setSequenceNumber(CandidateProductPkVariationKey.SEQUENCE_ZERO);
		key.setUpc(NumberUtils.toLong(nutrient.getUpcPlu()));
		candidate.setKey(key);
		candidate.setSourceSystem(SourceSystem.SOURCE_SYSTEM_PRODUCT_MAINTENANCE);
		candidate.setServingSizeQuantity(NumberUtils.toDouble(nutrient.getServingSize()));
		candidate.setSrvngSzUomCd(nutrient.getServingSizeUOMCode());
		candidate.setPreprdProdSw(CandidateProductPkVariation.PREPRD_PRODUCT_NO);
		candidate.setHouseHoldMeasurement(StringUtils.trimToEmpty(nutrient.getHouseHoldMeasurement()));
		candidate.setServingsPerContainerText(nutrient.getServingsPerContainerText());
		candidate.setMinServingsPerContainer(NumberUtils.toDouble(nutrient.getServingsPerContainer()));
		candidate.setCandidateWorkRequest(candidateWorkRequest);
		candidateWorkRequest.setCandidateProductPkVariations(new ArrayList<>());
		candidateWorkRequest.getCandidateProductPkVariations().add(candidate);
	}
	
	/**
	 *
	 * set product Id for ps work request
	 * @param nutrient             NutritionBatchUpload
	 * @param candidateWorkRequest candidate work request
	 */
	public void setProductIdForPsWorkRequest(NutritionBatchUpload nutrient, CandidateWorkRequest candidateWorkRequest) {
		SellingUnit sellingUnit = sellingUnitRepository.findOne(NumberUtils.toLong(nutrient.getUpcPlu
				(), 0L));
		if(null!=sellingUnit){
			candidateWorkRequest.setProductId(sellingUnit.getProdId());
		}
	}
}
