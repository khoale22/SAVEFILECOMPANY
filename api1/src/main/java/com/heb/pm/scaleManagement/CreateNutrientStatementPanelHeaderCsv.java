/*
 *  CreateNutrientStatementPanelHeaderCsv
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.entity.NutrientStatementPanelHeader;
import com.heb.util.jpa.PageableResult;

/**
 * Creates a CSV string from a list of Nutrient statement panel headers.
 *
 * @author vn70633
 * @since 2.20.0
 */
public class CreateNutrientStatementPanelHeaderCsv {

    // Format for text attributes for CSV export.
    private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
    private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\"%s\"\n";
    private static final String CSV_HEADING = "Statement ID, Measure Quantity, Metric Quantity, " +
            "Servings Per Containers, Effective Date";

    /**
     * Create csv string.
     *
     * @param nutrientStatements the nutrient statements
     * @return the string
     */
    public static String createCsv(PageableResult<NutrientStatementPanelHeader> nutrientStatements){
        StringBuilder csv = new StringBuilder();
        for(NutrientStatementPanelHeader statement : nutrientStatements.getData()){
            csv.append(String.format(TEXT_EXPORT_FORMAT, statement.getSourceSystemReferenceId()));
            csv.append(String.format(TEXT_EXPORT_FORMAT, statement.getImperialDisplayName()));
            csv.append(String.format(TEXT_EXPORT_FORMAT, statement.getMetricDisplayName()));
            csv.append(String.format(TEXT_EXPORT_FORMAT, statement.getServingPerContainerDisplayName()));
            csv.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT, statement.getEffectiveDate()));
        }
        return csv.toString();
    }

    /**
     * Returns the heading to the CSV.
     * @return The heading to the CSV.
     */
    public static String getHeading(){
        return CSV_HEADING;
    }
}

