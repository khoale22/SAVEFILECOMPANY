/*
 * TexasFieldValidatorImpl
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */
package com.heb.batch.wic.service.impl;

import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.service.TexasFieldValidator;
import com.heb.batch.wic.utils.WicUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This is TexasFieldValidator  class.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Service
public class TexasFieldValidatorImpl implements TexasFieldValidator {
    private static final Logger LOGGER = LogManager.getLogger(TexasFieldValidatorImpl.class);
    static final String FORMATTER_TXTSTATE_DATE = "yyyyMMdd";
    static final String COMMA_STRING =",";
    @Autowired
    private MessageSource messageSource;
    @Override
    public boolean validateFormatFile(TexasStateDocument texasStateDocument){
        boolean valid = true;
        if(texasStateDocument !=null && texasStateDocument.getIdCode()!=null && !StringUtils.EMPTY.equals(texasStateDocument.getIdCode().trim())
                &&(!texasStateDocument.getIdCode().trim().startsWith(FieldFormats.HEADER.fieldDescription)
                &&!texasStateDocument.getIdCode().trim().startsWith(FieldFormats.BODY.fieldDescription)
                &&!texasStateDocument.getIdCode().trim().startsWith(FieldFormats.FOOTER.fieldDescription))){
            valid = false;
        }
        return valid;
    }

    private String getValidateUpcCheckDigitError(TexasStateDocument texasStateDocument){
        StringBuilder errorMess = new StringBuilder();
        if(!this.validateMandatoryField(texasStateDocument.getScnCdId())){
            errorMess.append(messageSource.getMessage("upc.mandatory.error",null, Locale.US)).append(COMMA_STRING);
        } else if(!this.validateNumber(texasStateDocument.getScnCdId())){
            errorMess.append(messageSource.getMessage("upc.number.error",null, Locale.US)).append(COMMA_STRING);
        } else if(!this.validateNumber(texasStateDocument.getUpcCheckDigit())){
            errorMess.append(messageSource.getMessage("upc.checkdigit.error",null, Locale.US)).append(COMMA_STRING);
        } else if(!this.validateUpcCheckDigit(texasStateDocument.getScnCdId(),texasStateDocument.getUpcCheckDigit())){
            errorMess.append(messageSource.getMessage("upc.checkdigit.invalid",null, Locale.US)).append(COMMA_STRING);
        }
        return errorMess.toString();
    }

    @Override
    public String validateWicDataField(TexasStateDocument texasStateDocument) {
        StringBuilder errorMess = new StringBuilder();
        if(!this.validateMandatoryField(texasStateDocument.getWicProdDes())){
            errorMess.append(messageSource.getMessage("wicproddes.mandatory.error",null, Locale.US)).append(COMMA_STRING);
        }
        // validate APL-PREFIX
        if(!this.validateMandatoryField(texasStateDocument.getWicAplId())){
            errorMess.append(messageSource.getMessage("wicappid.mandatory.error",null, Locale.US)).append(COMMA_STRING);
        } else if(!this.validateNumber(texasStateDocument.getWicAplId())){
            errorMess.append(messageSource.getMessage("wicappid.number.error",null, Locale.US)).append(COMMA_STRING);
        }
        // validate UPC/ Check Digit
        errorMess.append(this.getValidateUpcCheckDigitError(texasStateDocument));

        // check effectDay required
        if(!this.validateMandatoryField(texasStateDocument.getEffDt())){
            errorMess.append(messageSource.getMessage("effectday.mandatory.error",null, Locale.US)).append(COMMA_STRING);
        } else if(!validateDate(texasStateDocument.getEffDt(),FORMATTER_TXTSTATE_DATE)){
            errorMess.append(messageSource.getMessage("effectday.format.error",null, Locale.US)).append(COMMA_STRING);
        }
        // check END-DATE
        if(!validateDate(texasStateDocument.getEndDt(),FORMATTER_TXTSTATE_DATE)){
            errorMess.append(messageSource.getMessage("endday.format.error",null, Locale.US)).append(COMMA_STRING);
        }

        // check wic category
        if(!this.validateMandatoryField(texasStateDocument.getWicCatId())){
            errorMess.append(messageSource.getMessage("wiccategory.code.mandatory.error",null, Locale.US)).append(COMMA_STRING);
        } else if(!this.validateNumber(texasStateDocument.getWicCatId())){
            errorMess.append(messageSource.getMessage("wiccategory.number.error",null, Locale.US)).append(COMMA_STRING);
        }
        if(!this.validateMandatoryField(texasStateDocument.getWicCategoryDesc())){
            errorMess.append(messageSource.getMessage("wiccategory.mandatory.error",null, Locale.US)).append(COMMA_STRING);
        }
        // check sub-wic category
        if(!this.validateMandatoryField(texasStateDocument.getWicSubCatId())){
            errorMess.append(messageSource.getMessage("subwiccategory.code.mandatory.error",null, Locale.US)).append(COMMA_STRING);
        } else if(!this.validateNumber(texasStateDocument.getWicSubCatId())){
            errorMess.append(messageSource.getMessage("subwiccategory.number.error",null, Locale.US)).append(COMMA_STRING);
        }
        if(!this.validateMandatoryField(texasStateDocument.getWicSubCatId())){
            errorMess.append(messageSource.getMessage("subwiccategory.mandatory.error",null, Locale.US)).append(COMMA_STRING);
        }
        // check PACKAGE-SIZE
        if(!this.validateNumber(texasStateDocument.getWicPkgSzQty())){
            errorMess.append(messageSource.getMessage("packagesize.number.error",null, Locale.US)).append(COMMA_STRING);
        }
        // check ITEM-PRICE
        if(!this.validateNumber(texasStateDocument.getWicPrcAmt())){
            errorMess.append(messageSource.getMessage("wicprocamt.number.error",null, Locale.US)).append(COMMA_STRING);
        }
        // check BENEFIT-QTY
        if(!this.validateNumber(texasStateDocument.getWicBnFtQty())){
            errorMess.append(messageSource.getMessage("wicbenefitqty.number.error",null, Locale.US)).append(COMMA_STRING);
        }
        texasStateDocument.setErrorMessage(errorMess.toString());
        return errorMess.toString();
    }
    @Override
    public boolean validateUpcCheckDigit(String upc,String checkDigit){
        boolean flag = false;
        int checkDigitCheck = WicUtil.calculateCheckDigit(upc);
         if(this.validateNumber(checkDigit) && checkDigitCheck == Integer.valueOf(checkDigit)){
            flag = true;
        }
        return flag;
    }
    public boolean validateMandatoryField(String values){
        boolean flag = true;
        if(StringUtils.isEmpty(values)|| StringUtils.isBlank(values)){
            flag = false;
        }        return flag;
    }
    @Override
    public boolean validateNumber(String value){
        boolean flag = true;
       if(StringUtils.isNotEmpty(value)){
           try {
              double valueParse = Double.parseDouble(value);
              if(valueParse>=0) {
                  flag = true;
              }else {
                  flag = false;
              }
           } catch (NumberFormatException nfe) {
               flag = false;
           }
       }
       return flag;
    }

    @Override
    public boolean validateDate(String value, String format) {
        boolean flag = true;
        if(this.validateNumber(value)){
            try{
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                formatter.parse(value);
            }catch(Exception e){
                flag = false;
               LOGGER.error("validateDate = "+e.getMessage());
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public enum FieldFormats {
        HEADER("A"), BODY("D"),FOOTER("Z"),D4_RECORD("D4"),D6_RECORD("D6");
        private String fieldDescription="";
        private FieldFormats(String value) {
            fieldDescription = value;
        }

        public String getFieldDescription() {
            return fieldDescription;
        }
    }
}
