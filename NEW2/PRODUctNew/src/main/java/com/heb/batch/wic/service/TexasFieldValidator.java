/*
 * TexasFieldValidator
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */
package com.heb.batch.wic.service;

import com.heb.batch.wic.index.TexasStateDocument;
/**
 * TexasFieldValidator Class.
 * @author vn55306
 * @see com.heb.batch.wic.service.impl.TexasFieldValidatorImpl
 */
public interface TexasFieldValidator {

    /**
     * validate Wic DataField.
     * @param texasStateDocument TexasStateDocument
     * @return String
     * @author vn55306
     */
    String validateWicDataField(TexasStateDocument texasStateDocument);
    /**
     * validate Format File.
     * @param texasStateDocument TexasStateDocument
     * @return boolean
     * @author vn55306
     */
    boolean validateFormatFile(TexasStateDocument texasStateDocument);
    /**
     * validate Number.
     * @param value String
     * @return boolean
     * @author vn55306
     */
    boolean validateNumber(String value);
    /**
     * validate Date.
     * @param value String
     * @param format String
     * @return boolean
     * @author vn55306
     */
    boolean validateDate(String value, String format);
    /**
     * validate Upc Check Digit.
     * @param upc String
     * @param  checkDigit String
     * @return boolean
     * @author vn55306
     */
    boolean validateUpcCheckDigit(String upc,String checkDigit);
    /**
     * validate Mandatory Field.
     * @param values String
     * @return boolean
     * @author vn55306
     */
    boolean validateMandatoryField(String values);
}
