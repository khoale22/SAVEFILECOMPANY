/*
 *  CSVUtils
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

/**
 * The class is used to format data for CSV file.
 *
 * @author vn87351
 * @since 2.34.0
 */
public class CSVUtils {

    // Format for text attributes for CSV export.
    private static final String DOUBLE_QUOTES_FORMAT = "\"";
    private static final String ESCAPED_DOUBLE_QUOTES_FORMAT = "\"\"";

    /**
     * Escaped the double-quotes with another double quote.
     * @param value The string will be formatted.
     * @return a formatted string.
     */
    public static String formatCsvData(String value) {
        String result = value;
        if (result != null && result.contains(DOUBLE_QUOTES_FORMAT)) {
            result = result.replace(DOUBLE_QUOTES_FORMAT, ESCAPED_DOUBLE_QUOTES_FORMAT);
        }
        return result;
    }
}
