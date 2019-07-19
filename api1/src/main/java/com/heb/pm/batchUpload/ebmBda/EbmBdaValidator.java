/*
 * EbmBdaValidator
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.ebmBda;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.repository.ClassCommodityRepository;
import com.heb.pm.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Holds all validate of EBM_BDA Batch Upload.
 *
 * @author VN70529
 * @since 2.33.0
 */
@Component
public class EbmBdaValidator extends AbstractBatchUploadValidator {

    private static final Character CLASS_COMMODITY_ACTIVE_CODE = 'A';
    private static final String ERROR_COMMODITY_NOT_ACTIVE = "Commodity is not active.";
    private static final String ERROR_CLASS_COMMODITY_NOT_MATCH_HIERARCHY = "Commodity/Class is not under the same hierarchy.";
    private static final String ERROR_COMMODITY_MANDATORY_FIELD = "OMI Commodity ID field is mandatory.";
    private static final String ERROR_COMMODITY_TYPE_NUMBER = "OMI Commodity ID must be integer number.";
    private static final String ERROR_COMMODITY_VALUE = "OMI Commodity ID must be greater than 0 and less than 10000.";
    private static final String ERROR_CLASS_MANDATORY_FIELD = "Class field is mandatory.";
    private static final String ERROR_CLASS_TYPE_NUMBER = "Class must be integer number.";
    private static final String ERROR_CLASS_VALUE = "Class ID must be greater than 1 and less than 99.";
    private static final String ERROR_ONE_PASS_ID_NOT_VALID = "OnePass ID is not valid.";
    private static final String STRING_EMPTY = "EMPTY";
    private static final int DEFAULT_SHEET = 0;
    private static final int DEFAULT_ROW_BEGIN_READ_DATA = 0;

    private EbmBdaBatchUpload ebmBdaBatchUpload;
    private ClassCommodity classCommodity;

    @Autowired
    private ClassCommodityRepository classCommodityRepository;
    @Autowired
    private UserService userService;

    public void validateRow(BatchUpload batchUpload) {
        this.ebmBdaBatchUpload = (EbmBdaBatchUpload) batchUpload;
        this.validateClassCode();
        if (!this.ebmBdaBatchUpload.hasErrors()) this.validateCommodityCode();
        if (!this.ebmBdaBatchUpload.hasErrors()) this.validateClassCommodityCode();
        if (!this.ebmBdaBatchUpload.hasErrors()) this.validateEbm();
        if (!this.ebmBdaBatchUpload.hasErrors()) this.validateBda();
    }

    /**
     * Validate class code.
     */
    private void validateClassCode() {
        if (!StringUtils.trimToEmpty(this.ebmBdaBatchUpload.getClassCode()).equals(StringUtils.EMPTY)) {
            try {
                int classCode = Integer.parseInt(this.ebmBdaBatchUpload.getClassCode());
                if (classCode <= 1 || classCode >= 99) {
                    this.ebmBdaBatchUpload.getErrors().add(ERROR_CLASS_VALUE);
                }
            } catch (NumberFormatException ex) {
                this.ebmBdaBatchUpload.getErrors().add(ERROR_CLASS_TYPE_NUMBER);
            }
        } else {
            this.ebmBdaBatchUpload.getErrors().add(ERROR_CLASS_MANDATORY_FIELD);
        }
    }

    /**
     * Validate commodity code.
     */
    private void validateCommodityCode() {
        if (!StringUtils.trimToEmpty(this.ebmBdaBatchUpload.getCommodityCode()).equals(StringUtils.EMPTY)) {
            try {
                int commodityCode = Integer.parseInt(this.ebmBdaBatchUpload.getCommodityCode());
                if (commodityCode <= 0 || commodityCode > 9999) {
                    this.ebmBdaBatchUpload.getErrors().add(ERROR_COMMODITY_VALUE);
                }
            } catch (NumberFormatException ex) {
                this.ebmBdaBatchUpload.getErrors().add(ERROR_COMMODITY_TYPE_NUMBER);
            }
        } else {
            this.ebmBdaBatchUpload.getErrors().add(ERROR_COMMODITY_MANDATORY_FIELD);
        }
    }

    /**
     * Validate class commodity has active.
     */
    private void validateClassCommodityCode() {
        int classCode = Integer.parseInt(this.ebmBdaBatchUpload.getClassCode());
        int commodityCode = Integer.parseInt(this.ebmBdaBatchUpload.getCommodityCode());
        this.classCommodity = this.classCommodityRepository.findFirstByKeyClassCodeAndKeyCommodityCode(classCode, commodityCode);
        if (this.classCommodity == null) {
            this.ebmBdaBatchUpload.getErrors().add(ERROR_CLASS_COMMODITY_NOT_MATCH_HIERARCHY);
        } else {
            if (!this.classCommodity.getClassCommodityActive().equals(CLASS_COMMODITY_ACTIVE_CODE)) {
                this.ebmBdaBatchUpload.getErrors().add(ERROR_COMMODITY_NOT_ACTIVE);
            }
        }
    }

    /**
     * Validate for ebm id.
     */
    private void validateEbm() {
        if (!StringUtils.trimToEmpty(this.ebmBdaBatchUpload.getEbmId()).equals(StringUtils.EMPTY) && !this.ebmBdaBatchUpload.getEbmId().trim().toUpperCase().equals(STRING_EMPTY)
                && this.userService.getUserById(StringUtils.trim(this.ebmBdaBatchUpload.getEbmId())) == null) {
            this.ebmBdaBatchUpload.getErrors().add(ERROR_ONE_PASS_ID_NOT_VALID);
        }
    }

    /**
     * Validate for bda id.
     */
    private void validateBda() {
        if (!StringUtils.trimToEmpty(this.ebmBdaBatchUpload.getBdaId()).equals(StringUtils.EMPTY) && !this.ebmBdaBatchUpload.getBdaId().trim().toUpperCase().equals(STRING_EMPTY)
                && this.userService.getUserById(StringUtils.trim(this.ebmBdaBatchUpload.getBdaId())) == null) {
            this.ebmBdaBatchUpload.getErrors().add(ERROR_ONE_PASS_ID_NOT_VALID);
        }
    }

    /**
     * Validate File Upload.
     *
     * @param data the byte[]
     * @throws UnexpectedInputException The UnexpectedInputException
     */
    public void validateTemplate(byte[] data) throws UnexpectedInputException {
        try {
            InputStream inputStream = new ByteArrayInputStream(data);
            Workbook workBook = new XSSFWorkbook(inputStream);
            int numberOfSheets = workBook.getNumberOfSheets();
            if (numberOfSheets > 0) {
                Row row = workBook.getSheetAt(DEFAULT_SHEET).getRow(DEFAULT_ROW_BEGIN_READ_DATA);
                String header;
                for (int columnCounter = 0; columnCounter < row.getLastCellNum(); columnCounter++) {
                    header = getValueOfCell(row.getCell(columnCounter));
                    header = header != null ? header.trim() : StringUtils.EMPTY;
                    if (ebmBdaBatchUpload.uploadFileHeader.containsKey(columnCounter) && !ebmBdaBatchUpload.uploadFileHeader.get(columnCounter).equalsIgnoreCase(header)) {
                        throw new UnexpectedInputException(AbstractBatchUploadValidator.ERROR_FILE_WRONG_FORMAT);
                    }
                }
            }
        } catch (Exception e) {
            throw new UnexpectedInputException(AbstractBatchUploadValidator.ERROR_FILE_WRONG_FORMAT);
        }
    }
}
