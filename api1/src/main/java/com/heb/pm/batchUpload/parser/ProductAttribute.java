/*
 * Copyright (c) 2015 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 * @author Phillip McGraw (m314029)
 */


package com.heb.pm.batchUpload.parser;

/**
 *
 */
public class ProductAttribute implements java.io.Serializable {

    private static final long serialVersionUID = 2L;
    private String attrId;
    private long attrCdId;
    private String attrDomainCode;
    private boolean attrValLstSw;
    private String attrNm;
    private String textValue;
    private double numberValue;

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public long getAttrCdId() {
        return attrCdId;
    }

    public void setAttrCdId(long attrCdId) {
        this.attrCdId = attrCdId;
    }

    public String getAttrDomainCode() {
        return attrDomainCode;
    }

    public void setAttrDomainCode(String attrDomainCode) {
        this.attrDomainCode = attrDomainCode;
    }

    public boolean isAttrValLstSw() {
        return attrValLstSw;
    }

    public void setAttrValLstSw(boolean attrValLstSw) {
        this.attrValLstSw = attrValLstSw;
    }

    public String getAttrNm() {
        return attrNm;
    }

    public void setAttrNm(String attrNm) {
        this.attrNm = attrNm;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public double getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(double numberValue) {
        this.numberValue = numberValue;
    }
}
