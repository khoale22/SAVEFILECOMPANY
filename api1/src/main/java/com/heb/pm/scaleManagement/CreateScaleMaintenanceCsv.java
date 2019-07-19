/*
 *  CreateMaintenanceCsv
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.entity.ScaleUpc;
import com.heb.util.jpa.PageableResult;

/**
 * The type Create scale maintenance csv.
 *
 * @author l730832
 * @since 2.3.0
 */
public class CreateScaleMaintenanceCsv {

	// Format for text attributes for CSV export.
	private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
	private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";
	private static final String CSV_HEADING = "PLU, UPC, English Line 1, English Line 2, Department, " +
			"Effective Date, Ingredient Statement Number, Nutrient Statement Number, Service Counter Tare, " +
			"Pre-pack Tare, Shelf Life, English Line 3, English Line 4, Bilingual English Line 1, " +
			"Bilingual English Line 2, Bilingual Spanish Line 1, Bilingual Spanish Line 2, Graphics Code," +
			"Label Format Code 1, Label Format Code 2, Action Code";

	/**
	 * Creates a CSV string from a list of product discontinues.
	 *
	 * @param scaleUpcs a list of scale upcs.
	 * @return a CSV string with scale upc information.
	 */
	public static String createCsv(PageableResult<ScaleUpc> scaleUpcs){
		StringBuilder csv = new StringBuilder();
		for(ScaleUpc scaleUpc  : scaleUpcs.getData()){
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getPlu()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getUpc()));
			csv.append(String.format(TEXT_EXPORT_FORMAT,scaleUpc.getEnglishDescriptionOne().trim()));
			csv.append(String.format(TEXT_EXPORT_FORMAT,scaleUpc.getEnglishDescriptionTwo().trim()));

			if (scaleUpc.getAssociateUpc() == null || scaleUpc.getAssociateUpc().getSellingUnit() == null ||
					scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster() == null ||
					scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster().getSubDepartment() == null ||
					scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster().getSubDepartment().getDisplayName() == null) {
				csv.append(String.format(TEXT_EXPORT_FORMAT, "Error: Not tied to product."));
			} else {
				csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getAssociateUpc().getSellingUnit().getProductMaster().getSubDepartment().getDisplayName().trim()));
			}
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getEffectiveDate()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getIngredientStatement()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getNutrientStatement()));

			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getServiceCounterTare()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getPrePackTare()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getShelfLifeDays()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getEnglishDescriptionThree()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getEnglishDescriptionFour()));

			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getSpanishDescriptionOne()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getSpanishDescriptionThree()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getSpanishDescriptionTwo()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getSpanishDescriptionFour()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getScaleGraphicsCode().getDisplayName()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getFirstLabelFormat().getDisplayName()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getSecondLabelFormat().getDisplayName()));
			csv.append(String.format(TEXT_EXPORT_FORMAT, scaleUpc.getScaleActionCode().getDisplayName()));

			csv.append(NEWLINE_TEXT_EXPORT_FORMAT);
		}
		return csv.toString();
	}

	/**
	 * Returns the heading to the CSV.
	 *
	 * @return The heading to the CSV.
	 */
	public static String getHeading(){
		return CSV_HEADING;
	}
}
