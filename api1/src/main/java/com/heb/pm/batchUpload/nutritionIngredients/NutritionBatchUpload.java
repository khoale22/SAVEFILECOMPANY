/*
 * NutritionExcelFileInfo
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.nutritionIngredients;

import com.heb.pm.batchUpload.BatchUpload;

import java.util.HashMap;
import java.util.Map;

/**
 * the info object for nutrition upload
 * @author vn87351
 * @since 2.12.0
 */
public class NutritionBatchUpload extends BatchUpload {
	private static final long serialVersionUID = 3884054421968532653L;
	/** The column Constant in file upload. */
	public static final int COL_POS_PRODUCT =0;
	public static final String COL_NM_PRODUCT = "Product";
	public static final int COL_POS_UPC =1;
	public static final String COL_NM_UPC = "UPC/PLU";
	public static final int COL_POS_SERVING_SIZE =2;
	public static final String COL_NM_SERVING_SIZE = "Serving Size";
	public static final int COL_POS_SERVING_SIZE_UNITS_MEASURE =3;
	public static final String COL_NM_SERVING_SIZE_UNITS_MEASURE = "Serving Size Units Measure (g or mL)";
	public static final int COL_POS_HOUSEHOLD_MEASURE =4;
	public static final String COL_NM_HOUSEHOLD_MEASURE = "Household Measure";
	public static final int COL_POS_SERVING_PER_CONTAINER =5;
	public static final String COL_NM_SERVING_PER_CONTAINER = "Servings Per Container";
	public static final int COL_POS_TOTAL_CALORIES =6;
	public static final String COL_NM_TOTAL_CALORIES = "Total calories";
	public static final int COL_POS_CALORIES_FROM_FAT =7;
	public static final String COL_NM_CALORIES_FROM_FAT = "Calories from fat";
	public static final int COL_POS_TOTAL_FAT =8;
	public static final String COL_NM_TOTAL_FAT = "Total fat (g)";
	public static final int COL_POS_TOTAL_FAT_DV =9;
	public static final String COL_NM_TOTAL_FAT_DV = "Total Fat %DV (%)";
	public static final int COL_POS_SATURATED_FAT =10;
	public static final String COL_NM_SATURATED_FAT = "Saturated fat (g)";
	public static final int COL_POS_SATURATED_FAT_DV =11;
	public static final String COL_NM_SATURATED_FAT_DV = "Saturated Fat %DV (%)";
	public static final int COL_POS_TRANS_FAT =12;
	public static final String COL_NM_TRANS_FAT = "Trans fat (g)";
	public static final int COL_POS_CHOLESTEROL =13;
	public static final String COL_NM_CHOLESTEROL = "Cholesterol (mg)";
	public static final int COL_POS_CHOLESTEROL_DV =14;
	public static final String COL_NM_CHOLESTEROL_DV = "Cholesterol %DV (%)";
	public static final int COL_POS_SODIUM =15;
	public static final String COL_NM_SODIUM = "Sodium (mg)";
	public static final int COL_POS_SODIUM_DV =16;
	public static final String COL_NM_SODIUM_DV = "Sodium %DV (%)";
	public static final int COL_POS_POTASSIUM =17;
	public static final String COL_NM_POTASSIUM = "Potassium (g)";
	public static final int COL_POS_TOTAL_CARBOHYDRATES =18;
	public static final String COL_NM_TOTAL_CARBOHYDRATES = "Total Carbohydrates (g)";
	public static final int COL_POS_TOTAL_CARBOHYDRATES_DV =19;
	public static final String COL_NM_TOTAL_CARBOHYDRATES_DV = "Total Carbohydrates %DV (%)";
	public static final int COL_POS_DIETARY_FIBER =20;
	public static final String COL_NM_DIETARY_FIBER = "Dietary Fiber (g)";
	public static final int COL_POS_DIETARY_FIBER_DV =21;
	public static final String COL_NM_DIETARY_FIBER_DV = "Dietary Fiber %DV (%)";
	public static final int COL_POS_SUGARS =22;
	public static final String COL_NM_SUGARS = "Sugars (g)";
	public static final int COL_POS_PROTEIN =23;
	public static final String COL_NM_PROTEIN = "Protein (g)";
	public static final int COL_POS_VITAMIN_A =24;
	public static final String COL_NM_VITAMIN_A = "Vitamin A (%)";
	public static final int COL_POS_VITAMIN_C =25;
	public static final String COL_NM_VITAMIN_C = "Vitamin C (%)";
	public static final int COL_POS_CALCIUM =26;
	public static final String COL_NM_CALCIUM = "Calcium (%)";
	public static final int COL_POS_IRON =27;
	public static final String COL_NM_IRON = "Iron (%)";
	public static final int COL_POS_INGREDIENTS =28;
	public static final String COL_NM_INGREDIENTS = "Ingredients";
	public static final Map<Integer,String> uploadFileHeader;
	static {
		uploadFileHeader = new HashMap<>();
		uploadFileHeader.put(COL_POS_PRODUCT,COL_NM_PRODUCT);
		uploadFileHeader.put(COL_POS_UPC, COL_NM_UPC);
		uploadFileHeader.put(COL_POS_SERVING_SIZE,COL_NM_SERVING_SIZE);
		uploadFileHeader.put(COL_POS_SERVING_SIZE_UNITS_MEASURE,COL_NM_SERVING_SIZE_UNITS_MEASURE);
		uploadFileHeader.put(COL_POS_HOUSEHOLD_MEASURE,COL_NM_HOUSEHOLD_MEASURE);
		uploadFileHeader.put(COL_POS_SERVING_PER_CONTAINER,COL_NM_SERVING_PER_CONTAINER);
		uploadFileHeader.put(COL_POS_TOTAL_CALORIES,COL_NM_TOTAL_CALORIES);
		uploadFileHeader.put(COL_POS_CALORIES_FROM_FAT,COL_NM_CALORIES_FROM_FAT);
		uploadFileHeader.put(COL_POS_TOTAL_FAT,COL_NM_TOTAL_FAT);
		uploadFileHeader.put(COL_POS_TOTAL_FAT_DV,COL_NM_TOTAL_FAT_DV);
		uploadFileHeader.put(COL_POS_SATURATED_FAT,COL_NM_SATURATED_FAT);
		uploadFileHeader.put(COL_POS_SATURATED_FAT_DV,COL_NM_SATURATED_FAT_DV);
		uploadFileHeader.put(COL_POS_TRANS_FAT,COL_NM_TRANS_FAT);
		uploadFileHeader.put(COL_POS_CHOLESTEROL,COL_NM_CHOLESTEROL);
		uploadFileHeader.put(COL_POS_CHOLESTEROL_DV,COL_NM_CHOLESTEROL_DV);
		uploadFileHeader.put(COL_POS_SODIUM,COL_NM_SODIUM);
		uploadFileHeader.put(COL_POS_SODIUM_DV,COL_NM_SODIUM_DV);
		uploadFileHeader.put(COL_POS_POTASSIUM,COL_NM_POTASSIUM);
		uploadFileHeader.put(COL_POS_TOTAL_CARBOHYDRATES,COL_NM_TOTAL_CARBOHYDRATES);
		uploadFileHeader.put(COL_POS_TOTAL_CARBOHYDRATES_DV,COL_NM_TOTAL_CARBOHYDRATES_DV);
		uploadFileHeader.put(COL_POS_DIETARY_FIBER,COL_NM_DIETARY_FIBER);
		uploadFileHeader.put(COL_POS_DIETARY_FIBER_DV,COL_NM_DIETARY_FIBER_DV);
		uploadFileHeader.put(COL_POS_SUGARS,COL_NM_SUGARS);
		uploadFileHeader.put(COL_POS_PROTEIN,COL_NM_PROTEIN);
		uploadFileHeader.put(COL_POS_VITAMIN_A,COL_NM_VITAMIN_A);
		uploadFileHeader.put(COL_POS_VITAMIN_C,COL_NM_VITAMIN_C);
		uploadFileHeader.put(COL_POS_CALCIUM,COL_NM_CALCIUM);
		uploadFileHeader.put(COL_POS_IRON,COL_NM_IRON);
		uploadFileHeader.put(COL_POS_INGREDIENTS,COL_NM_INGREDIENTS);
	}
	private long psWorkId;
	private String upcPlu;
	private String servingSize;
	private String servingSizeUOM;
	private String houseHoldMeasurement;
	private String servingsPerContainerText;
	private String servingsPerContainer;
	private String totalCalories;
	private String caloriesFromFat;
	private String totalFat;
	private String totalFatDV;
	private String saturatedFat;
	private String saturatedFatDV;
	private String transFat;
	private String cholesterol;
	private String cholesterolDV;
	private String sodium;
	private String sodiumDV;
	private String potassium;
	private String totalCarbohydrates;
	private String totalCarbohydratesDV;
	private String dietaryFiber;
	private String dietaryFiberDV;
	private String sugars;
	private String protein;
	private String vitaminA;
	private String vitaminC;
	private String calcium;
	private String iron;
	private String ingredientsDescription;
	private String servingSizeUOMCode;
	private String userId;

	public String getUserId() {
		return userId;
	}

	public NutritionBatchUpload setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	/**
	 * Gets serving size uom code.
	 *
	 * @return the serving size uom code
	 */
	public String getServingSizeUOMCode() {
		return servingSizeUOMCode;
	}

	/**
	 * Sets serving size uom code.
	 *
	 * @param servingSizeUOMCode the serving size uom code
	 */
	public void setServingSizeUOMCode(String servingSizeUOMCode) {
		this.servingSizeUOMCode = servingSizeUOMCode;
	}

	/**
	 * Gets ps work id.
	 *
	 * @return the ps work id
	 */
	public long getPsWorkId() {
		return psWorkId;
	}

	/**
	 * Sets ps work id.
	 *
	 * @param psWorkId the ps work id
	 */
	public void setPsWorkId(long psWorkId) {
		this.psWorkId = psWorkId;
	}

	/**
	 * Gets upc plu.
	 *
	 * @return the upc plu
	 */
	public String getUpcPlu() {
		return upcPlu;
	}

	/**
	 * Sets upc plu.
	 *
	 * @param upcPlu the upc plu
	 */
	public void setUpcPlu(String upcPlu) {
		this.upcPlu = upcPlu;
	}

	/**
	 * Gets serving size.
	 *
	 * @return the serving size
	 */
	public String getServingSize() {
		return servingSize;
	}

	/**
	 * Sets serving size.
	 *
	 * @param servingSize the serving size
	 */
	public void setServingSize(String servingSize) {
		this.servingSize = servingSize;
	}

	/**
	 * Gets serving size uom.
	 *
	 * @return the serving size uom
	 */
	public String getServingSizeUOM() {
		return servingSizeUOM;
	}

	/**
	 * Sets serving size uom.
	 *
	 * @param servingSizeUOM the serving size uom
	 */
	public void setServingSizeUOM(String servingSizeUOM) {
		this.servingSizeUOM = servingSizeUOM;
	}

	/**
	 * Gets house hold measurement.
	 *
	 * @return the house hold measurement
	 */
	public String getHouseHoldMeasurement() {
		return houseHoldMeasurement;
	}

	/**
	 * Sets house hold measurement.
	 *
	 * @param houseHoldMeasurement the house hold measurement
	 */
	public void setHouseHoldMeasurement(String houseHoldMeasurement) {
		this.houseHoldMeasurement = houseHoldMeasurement;
	}

	/**
	 * Gets servings per container text.
	 *
	 * @return the servings per container text
	 */
	public String getServingsPerContainerText() {
		return servingsPerContainerText;
	}

	/**
	 * Sets servings per container text.
	 *
	 * @param servingsPerContainerText the servings per container text
	 */
	public void setServingsPerContainerText(String servingsPerContainerText) {
		this.servingsPerContainerText = servingsPerContainerText;
	}

	/**
	 * Gets servings per container.
	 *
	 * @return the servings per container
	 */
	public String getServingsPerContainer() {
		return servingsPerContainer;
	}

	/**
	 * Sets servings per container.
	 *
	 * @param servingsPerContainer the servings per container
	 */
	public void setServingsPerContainer(String servingsPerContainer) {
		this.servingsPerContainer = servingsPerContainer;
	}

	/**
	 * Gets total calories.
	 *
	 * @return the total calories
	 */
	public String getTotalCalories() {
		return totalCalories;
	}

	/**
	 * Sets total calories.
	 *
	 * @param totalCalories the total calories
	 */
	public void setTotalCalories(String totalCalories) {
		this.totalCalories = totalCalories;
	}

	/**
	 * Gets calories from fat.
	 *
	 * @return the calories from fat
	 */
	public String getCaloriesFromFat() {
		return caloriesFromFat;
	}

	/**
	 * Sets calories from fat.
	 *
	 * @param caloriesFromFat the calories from fat
	 */
	public void setCaloriesFromFat(String caloriesFromFat) {
		this.caloriesFromFat = caloriesFromFat;
	}

	/**
	 * Gets total fat.
	 *
	 * @return the total fat
	 */
	public String getTotalFat() {
		return totalFat;
	}

	/**
	 * Sets total fat.
	 *
	 * @param totalFat the total fat
	 */
	public void setTotalFat(String totalFat) {
		this.totalFat = totalFat;
	}

	/**
	 * Gets total fat dv.
	 *
	 * @return the total fat dv
	 */
	public String getTotalFatDV() {
		return totalFatDV;
	}

	/**
	 * Sets total fat dv.
	 *
	 * @param totalFatDV the total fat dv
	 */
	public void setTotalFatDV(String totalFatDV) {
		this.totalFatDV = totalFatDV;
	}

	/**
	 * Gets saturated fat.
	 *
	 * @return the saturated fat
	 */
	public String getSaturatedFat() {
		return saturatedFat;
	}

	/**
	 * Sets saturated fat.
	 *
	 * @param saturatedFat the saturated fat
	 */
	public void setSaturatedFat(String saturatedFat) {
		this.saturatedFat = saturatedFat;
	}

	/**
	 * Gets saturated fat dv.
	 *
	 * @return the saturated fat dv
	 */
	public String getSaturatedFatDV() {
		return saturatedFatDV;
	}

	/**
	 * Sets saturated fat dv.
	 *
	 * @param saturatedFatDV the saturated fat dv
	 */
	public void setSaturatedFatDV(String saturatedFatDV) {
		this.saturatedFatDV = saturatedFatDV;
	}

	/**
	 * Gets trans fat.
	 *
	 * @return the trans fat
	 */
	public String getTransFat() {
		return transFat;
	}

	/**
	 * Sets trans fat.
	 *
	 * @param transFat the trans fat
	 */
	public void setTransFat(String transFat) {
		this.transFat = transFat;
	}

	/**
	 * Gets cholesterol.
	 *
	 * @return the cholesterol
	 */
	public String getCholesterol() {
		return cholesterol;
	}

	/**
	 * Sets cholesterol.
	 *
	 * @param cholesterol the cholesterol
	 */
	public void setCholesterol(String cholesterol) {
		this.cholesterol = cholesterol;
	}

	/**
	 * Gets cholesterol dv.
	 *
	 * @return the cholesterol dv
	 */
	public String getCholesterolDV() {
		return cholesterolDV;
	}

	/**
	 * Sets cholesterol dv.
	 *
	 * @param cholesterolDV the cholesterol dv
	 */
	public void setCholesterolDV(String cholesterolDV) {
		this.cholesterolDV = cholesterolDV;
	}

	/**
	 * Gets sodium.
	 *
	 * @return the sodium
	 */
	public String getSodium() {
		return sodium;
	}

	/**
	 * Sets sodium.
	 *
	 * @param sodium the sodium
	 */
	public void setSodium(String sodium) {
		this.sodium = sodium;
	}

	/**
	 * Gets sodium dv.
	 *
	 * @return the sodium dv
	 */
	public String getSodiumDV() {
		return sodiumDV;
	}

	/**
	 * Sets sodium dv.
	 *
	 * @param sodiumDV the sodium dv
	 */
	public void setSodiumDV(String sodiumDV) {
		this.sodiumDV = sodiumDV;
	}

	/**
	 * Gets potassium.
	 *
	 * @return the potassium
	 */
	public String getPotassium() {
		return potassium;
	}

	/**
	 * Sets potassium.
	 *
	 * @param potassium the potassium
	 */
	public void setPotassium(String potassium) {
		this.potassium = potassium;
	}

	/**
	 * Gets total carbohydrates.
	 *
	 * @return the total carbohydrates
	 */
	public String getTotalCarbohydrates() {
		return totalCarbohydrates;
	}

	/**
	 * Sets total carbohydrates.
	 *
	 * @param totalCarbohydrates the total carbohydrates
	 */
	public void setTotalCarbohydrates(String totalCarbohydrates) {
		this.totalCarbohydrates = totalCarbohydrates;
	}

	/**
	 * Gets total carbohydrates dv.
	 *
	 * @return the total carbohydrates dv
	 */
	public String getTotalCarbohydratesDV() {
		return totalCarbohydratesDV;
	}

	/**
	 * Sets total carbohydrates dv.
	 *
	 * @param totalCarbohydratesDV the total carbohydrates dv
	 */
	public void setTotalCarbohydratesDV(String totalCarbohydratesDV) {
		this.totalCarbohydratesDV = totalCarbohydratesDV;
	}

	/**
	 * Gets dietary fiber.
	 *
	 * @return the dietary fiber
	 */
	public String getDietaryFiber() {
		return dietaryFiber;
	}

	/**
	 * Sets dietary fiber.
	 *
	 * @param dietaryFiber the dietary fiber
	 */
	public void setDietaryFiber(String dietaryFiber) {
		this.dietaryFiber = dietaryFiber;
	}

	/**
	 * Gets dietary fiber dv.
	 *
	 * @return the dietary fiber dv
	 */
	public String getDietaryFiberDV() {
		return dietaryFiberDV;
	}

	/**
	 * Sets dietary fiber dv.
	 *
	 * @param dietaryFiberDV the dietary fiber dv
	 */
	public void setDietaryFiberDV(String dietaryFiberDV) {
		this.dietaryFiberDV = dietaryFiberDV;
	}

	/**
	 * Gets sugars.
	 *
	 * @return the sugars
	 */
	public String getSugars() {
		return sugars;
	}

	/**
	 * Sets sugars.
	 *
	 * @param sugars the sugars
	 */
	public void setSugars(String sugars) {
		this.sugars = sugars;
	}

	/**
	 * Gets protein.
	 *
	 * @return the protein
	 */
	public String getProtein() {
		return protein;
	}

	/**
	 * Sets protein.
	 *
	 * @param protein the protein
	 */
	public void setProtein(String protein) {
		this.protein = protein;
	}

	/**
	 * Gets vitamin a.
	 *
	 * @return the vitamin a
	 */
	public String getVitaminA() {
		return vitaminA;
	}

	/**
	 * Sets vitamin a.
	 *
	 * @param vitaminA the vitamin a
	 */
	public void setVitaminA(String vitaminA) {
		this.vitaminA = vitaminA;
	}

	/**
	 * Gets vitamin c.
	 *
	 * @return the vitamin c
	 */
	public String getVitaminC() {
		return vitaminC;
	}

	/**
	 * Sets vitamin c.
	 *
	 * @param vitaminC the vitamin c
	 */
	public void setVitaminC(String vitaminC) {
		this.vitaminC = vitaminC;
	}

	/**
	 * Gets calcium.
	 *
	 * @return the calcium
	 */
	public String getCalcium() {
		return calcium;
	}

	/**
	 * Sets calcium.
	 *
	 * @param calcium the calcium
	 */
	public void setCalcium(String calcium) {
		this.calcium = calcium;
	}

	/**
	 * Gets iron.
	 *
	 * @return the iron
	 */
	public String getIron() {
		return iron;
	}

	/**
	 * Sets iron.
	 *
	 * @param iron the iron
	 */
	public void setIron(String iron) {
		this.iron = iron;
	}

	/**
	 * Gets ingredients description.
	 *
	 * @return the ingredients description
	 */
	public String getIngredientsDescription() {
		return ingredientsDescription;
	}

	/**
	 * Sets ingredients description.
	 *
	 * @param ingredientsDescription the ingredients description
	 */
	public void setIngredientsDescription(String ingredientsDescription) {
		this.ingredientsDescription = ingredientsDescription;
	}
}
