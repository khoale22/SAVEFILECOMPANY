/*
 *  ExcelSpreadsheetPropertyDescriptor
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.parser;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * This class contain property of file upload.
 *
 * @author vn55306
 * @since 2.8.0
 */
public class ExcelColumnProperty implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    private String spreadsheetFormattedPropertyName;
    private String voPropertyName;
    private boolean required;
    private String propertyType = "String";
    private String valueCondition;
    private String errorMessPattern = "";

    /**
     * @return spreadsheetFormattedPropertyName
     */
    public String getSpreadsheetFormattedPropertyName() {
	return this.spreadsheetFormattedPropertyName;
    }

    /**
     * @param sPreadsheetFormattedPropertyName
     *            String
     * @author vn55306
     */
    public void setSpreadsheetFormattedPropertyName(String sPreadsheetFormattedPropertyName) {
	this.spreadsheetFormattedPropertyName = sPreadsheetFormattedPropertyName;
    }

    /**
     * @return voPropertyName
     * @author vn55306
     */
    public String getVoPropertyName() {
	return this.voPropertyName;
    }

    /**
     * @param vOPropertyName
     *            String
     * @author vn55306
     */
    public void setVoPropertyName(String vOPropertyName) {
	this.voPropertyName = vOPropertyName;
    }

    /**
     * @return required
     */
    public boolean isRequired() {
	return this.required;
    }

    /**
     * @param reQuired
     *            String
     */
    public void setRequired(boolean reQuired) {
	this.required = reQuired;
    }

    /**
     * @param raw
     *            String
     * @return String
     */
    public static String convertToMatcher(String raw) {
	    if(StringUtils.isNotEmpty(raw)){
            return raw.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        }
        return raw;
    }

    /**
     * @return propertyType
     */
    public String getPropertyType() {
	return this.propertyType;
    }

    /**
     * @param proPertyType
     *            String
     */
    public void setPropertyType(String proPertyType) {
	this.propertyType = proPertyType;
    }

    /**
     * @return valueCondition
     */
    public String getValueCondition() {
	return this.valueCondition;
    }

    /**
     * @param valueConDition
     *            String
     */
    public void setValueCondition(String valueConDition) {
	this.valueCondition = valueConDition;
    }

    /**
     * @return the errorMessPattern
     */
    public String getErrorMessPattern() {
	return this.errorMessPattern;
    }

    /**
     * @param errorMessPatterns
     *            the errorMessPattern to set
     * @author vn55306
     */
    public void setErrorMessPattern(String errorMessPatterns) {
	this.errorMessPattern = errorMessPatterns;
    }
}
