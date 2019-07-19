/*
 * NutritionCandidateWorkRequestCreator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.nutritionIngredients;

import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreator;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.batchUpload.parser.WorkRequestCreatorUtils;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.ServingSizeUOM;
import com.heb.pm.entity.SourceSystem;
import com.heb.pm.repository.ServingSizeUOMRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * the creator for nutrition upload
 * @author vn87351
 * @since 2.12.0
 */
@Service
public class NutritionCandidateWorkRequestCreator extends CandidateWorkRequestCreator {
	private static final Logger LOGGER = LoggerFactory.getLogger(NutritionCandidateWorkRequestCreator.class);
	@Autowired
	private NutritionSetter nutritionSetter;
	/**
	 * inject validator for nutrition
	 */
	@Autowired
	private NutritionValidator nutritionValidator;
	
	@Autowired
	ServingSizeUOMRepository servingSizeUOMRepository;

	@Override
	public CandidateWorkRequest createRequest(List<String> cellValues, List<ProductAttribute> productAttributes,
											  long transactionId, String userId){
		LOGGER.info(cellValues.size()+":size nutrient");
		NutritionBatchUpload nutritionBatchUpload = parseNutrition(cellValues);
		nutritionBatchUpload.setUserId(userId);
		try {
			nutritionValidator.validateRow(nutritionBatchUpload);
		}catch (UnexpectedInputException e){
			//set upc null in case user input invalid value for upc
			nutritionBatchUpload.setUpcPlu(null);
		}
		CandidateWorkRequest candidateWorkRequest =
				WorkRequestCreatorUtils.getEmptyWorkRequest(null, NumberUtils.toLong(nutritionBatchUpload.getUpcPlu()),
						userId,transactionId, CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT, getBatchStatus(nutritionBatchUpload).getName());
		if(!nutritionBatchUpload.hasErrors()){
		     List<ServingSizeUOM> uoms = servingSizeUOMRepository.
					findBySourceSystemAndServingSizeUomDescription(SourceSystem.SOURCE_SYSTEM_GLADSON, nutritionBatchUpload.getServingSizeUOM().trim());
			if(CollectionUtils.isNotEmpty(uoms)){
				//get first UOM of return list
				nutritionBatchUpload.setServingSizeUOMCode(uoms.get(0).getServingSizeUomCode());
			}

			nutritionSetter.setProductIdForPsWorkRequest(nutritionBatchUpload,candidateWorkRequest);
			nutritionSetter.setCandidateProductPkVariation(nutritionBatchUpload,candidateWorkRequest);
			nutritionSetter.setCandidateNutrient(nutritionBatchUpload,candidateWorkRequest);
			nutritionSetter.setCandidateMasterDataExtensionAttribute(nutritionBatchUpload,candidateWorkRequest);
			nutritionSetter.setCandidateStatus(candidateWorkRequest, getBatchStatus(nutritionBatchUpload), StringUtils.EMPTY);
		}
		else{
		    //set product id in order to show data of product in tracking detail page
			if(null!=nutritionBatchUpload.getUpcPlu()){
				nutritionSetter.setProductIdForPsWorkRequest(nutritionBatchUpload,candidateWorkRequest);
			}
			nutritionSetter.setCandidateStatus(candidateWorkRequest, getBatchStatus(nutritionBatchUpload),getLengthOptimizedError(nutritionBatchUpload.getErrors()));
		}

		return candidateWorkRequest;
	}

	/**
	 * parse row data to nutrition object
	 * @param cellValues row data
	 * @return nutrition batch upload
	 *
	 */
	private NutritionBatchUpload parseNutrition(List<String> cellValues){
		NutritionBatchUpload nutrition = new NutritionBatchUpload();
		String value = null;
		for (int columnCounter = 0; columnCounter < cellValues.size(); columnCounter++) {
			value = cellValues.get(columnCounter);
			switch (columnCounter) {
				case NutritionBatchUpload.COL_POS_UPC: {
					nutrition.setUpcPlu(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_SERVING_SIZE: {
					nutrition.setServingSize(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_SERVING_SIZE_UNITS_MEASURE: {
					nutrition.setServingSizeUOM(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_HOUSEHOLD_MEASURE: {
					nutrition.setHouseHoldMeasurement(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_SERVING_PER_CONTAINER: {
					nutrition.setServingsPerContainerText(value);
					nutrition.setServingsPerContainer(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_TOTAL_CALORIES: {
					nutrition.setTotalCalories(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_CALORIES_FROM_FAT: {
					nutrition.setCaloriesFromFat(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_TOTAL_FAT: {
					nutrition.setTotalFat(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_TOTAL_FAT_DV: {
					nutrition.setTotalFatDV(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_SATURATED_FAT: {
					nutrition.setSaturatedFat(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_SATURATED_FAT_DV: {
					nutrition.setSaturatedFatDV(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_TRANS_FAT: {
					nutrition.setTransFat(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_CHOLESTEROL: {
					nutrition.setCholesterol(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_CHOLESTEROL_DV: {
					nutrition.setCholesterolDV(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_SODIUM: {
					nutrition.setSodium(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_SODIUM_DV: {
					nutrition.setSodiumDV(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_POTASSIUM: {
					nutrition.setPotassium(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_TOTAL_CARBOHYDRATES: {
					nutrition.setTotalCarbohydrates(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_TOTAL_CARBOHYDRATES_DV: {
					nutrition.setTotalCarbohydratesDV(value);

					break;
				}
				case NutritionBatchUpload.COL_POS_DIETARY_FIBER : {
					nutrition.setDietaryFiber(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_DIETARY_FIBER_DV: {
					nutrition.setDietaryFiberDV(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_SUGARS: {
					nutrition.setSugars(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_PROTEIN: {
					nutrition.setProtein(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_VITAMIN_A: {
					nutrition.setVitaminA(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_VITAMIN_C: {
					nutrition.setVitaminC(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_CALCIUM: {
					nutrition.setCalcium(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_IRON: {
					nutrition.setIron(value);
					break;
				}
				case NutritionBatchUpload.COL_POS_INGREDIENTS: {
					nutrition.setIngredientsDescription(value);
					break;
				}
			}
		}

		return nutrition;
	}
}
