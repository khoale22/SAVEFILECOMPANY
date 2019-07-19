/*
 *  CreateCheckStatusRetailCsv
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.scaleMaintenance;

import com.heb.pm.entity.IngredientStatementHeader;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpcKey;
import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceUpcRepository;
import com.heb.scaleMaintenance.utils.EPlumApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Creates a CSV string from a list of ScaleMaintenanceAuthorizeRetail.
 *
 * @author s573181
 * @since 2.22.0
 */
@Component
public class CreateCheckStatusRetailCsv {

	// Format for text attributes for CSV export.
	private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
	private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\"%s\"%n";
	private static final String CSV_HEADING = "UPC, Authorized, Message, Retail, Weighed, X For, Shelf Life Days, Freeze By Days, " +
			"Eat By Days, Action Code, Graphics Code, Label Format 1, Label Format 2, Grade, Service Counter Tare, " +
			"Pre-Pack Tare, Net Weight, English Description 1, English Description 2, English Description 3, English Description 4, " +
			"Spanish Description 1, Spanish Description 2, Spanish Description 3, Spanish Description 4, Department, Sub Department, " +
			"Ingredient Statement, Ingredient Text, Nutrient Statement Code, Nutrient Statement, Ascorbic Acid," +
			" Biotin, Calcium, Calories, Calories From Fat, Cholesterol, Copper, Dietary Fiber, Folacin, Folic Acid, Iodine, " +
			"Iron, Magnesium, Niacin, Pantothenic Acid, Phosphorous, Potassium, Protein, Riboflavin, Saturated Fat, Sodium, " +
			"Sugar, Sugar Alcohol, Thiamine, Total Carbohydrates, Total Fat, Trans Fat, Vitamin A, Vitamin C, Vitamin D," +
			" Vitamin E, Vitamin B1, Vitamin B2, Vitamin B6, Vitamin B12, Zinc";


	@Autowired
	private EPlumApiUtils ePlumApiUtils;

	@Autowired
	private ScaleMaintenanceUpcRepository scaleMaintenanceUpcRepository;

	/**
	 * Create csv string.
	 *
	 * @param retail the ScaleMaintenanceAuthorizeRetail.
	 * @return the csv string.
	 */
	public String createCsv(ScaleMaintenanceAuthorizeRetail retail) {
		StringBuilder csv = new StringBuilder();
		csv.append(String.format(TEXT_EXPORT_FORMAT, retail.getKey().getUpc()));
		if(retail.getAuthorized()) {
			ScaleMaintenanceUpcKey scaleMaintenanceUpcKey = new ScaleMaintenanceUpcKey().setUpc(
					retail.getKey().getUpc()).setTransactionId( retail.getKey().getTransactionId());
			ScaleMaintenanceProduct scaleMaintenanceProduct =
					this.scaleMaintenanceUpcRepository.findOne(scaleMaintenanceUpcKey).getScaleProductAsJson();
			csv.append(String.format(TEXT_EXPORT_FORMAT, retail.getAuthorized()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, retail.getMessage()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, retail.getRetail()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, retail.getWeighed()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, retail.getByCountQuantity()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getShelfLifeDays()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getFreezeByDays()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getEatByDays()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getActionCode()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getGraphicsCode()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getLabelFormatOne()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getLabelFormatTwo()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getGrade()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getServiceCounterTare()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getPrePackTare()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getNetWeight()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getEnglishDescriptionOne()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getEnglishDescriptionTwo()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getEnglishDescriptionThree()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getEnglishDescriptionFour()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getSpanishDescriptionOne()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getSpanishDescriptionTwo()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getSpanishDescriptionThree()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getSpanishDescriptionFour()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getDepartment()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getSubDepartment()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getIngredientStatement()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getIngredientText()));
			if (Arrays
					.stream(IngredientStatementHeader.EMPTY_SCALE_NUTRIENT_STATEMENT_NUMBERS)
					.anyMatch((n) -> n == scaleMaintenanceProduct.getNutrientStatementCode())) {
				csv.append(String.format("%n"));
			} else {
				csv.append(String.format(TEXT_EXPORT_FORMAT, scaleMaintenanceProduct.getNutrientStatementCode()));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.
						getEPlumServingSizeFromScaleMaintenanceNutrient(scaleMaintenanceProduct.getNutrientStatement())));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getAscorbicAcid(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getBiotin(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getCalcium(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getCalories(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getCaloriesFromFat(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getCholesterol(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getCopper(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getDietaryFiber(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getFolacin(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getFolicAcid(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getIodine(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getIron(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getMagnesium(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getNiacin(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getPantothenicAcid(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getPhosphorus(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getPotassium(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getProtein(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getRiboflavin(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getSaturatedFat(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getSodium(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getSugar(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getSugarAlcohol(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getThiamine(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getTotalCarbohydrates(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getTotalFat(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getTransFat(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getVitaminA(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getVitaminC(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getVitaminD(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getVitaminE(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getVitaminB1(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getVitaminB2(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getVitaminB6(), true, true)));
				csv.append(String.format(TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getVitaminB12(), true, true)));
				csv.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT, this.ePlumApiUtils.getStringFormattedForNutrient(
						scaleMaintenanceProduct.getZinc(), true, true)));
			}
		} else {
			csv.append(String.format(TEXT_EXPORT_FORMAT, retail.getAuthorized()));
			csv.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT, retail.getMessage()));
		}
		return csv.toString();
	}

	/**
	 * Returns the heading to the CSV.
	 *
	 * @return The heading to the CSV.
	 */
	public String getHeading(){
		return CSV_HEADING;
	}
}
