/*
 * PrimoPickValidator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.primoPick;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
 * The class will validate object primo pick with business rules.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Component
public class PrimoPickValidator extends AbstractBatchUploadValidator {
	private static final Logger logger = LoggerFactory.getLogger(PrimoPickValidator.class);
	public static final String PRIMO_PICK_APPROVE_OR_SUBMITTED_BEFORE_ERROR = "Primo pick status should be APPROVED or SUBMITTED.";
	public static final String ERROR_UPC_MANDATORY_FIELD = "UPC is a mandatory field";
	public static final String ERROR_INVALID_UPC = "Invalid Upc %s.";
	public static final String ERROR_INVALID_START_DATE = "Invalid Start Date.";
	public static final String ERROR_INVALID_END_DATE ="Invalid End Date.";
	public static final String PRIMO_PICK_NO_MATCH_FOUND_UPC = "No matches found for entered UPC %s";
	private static final String ERROR_START_DATE_BEFORE_TOMOROW="Start Date must be Greater than the Current Date.";
	private static final String ERROR_END_DATE_BEFORE_TOMOROW = "End Date must be Greater than the Current Date.";
	private static final String ERROR_END_DATE_BEFORE_START_DATE = "EndDate must be greater than StartDate.";
	private static final int DEFAULT_SHEET = 0;
	private static final int DEFAULT_ROW_BEGIN_READ_DATA = 3;


	PrimoPickBatchUpload primoPick;
	@Autowired
	private SellingUnitRepository sellingUnitRepository;
	@Override
	public void validateRow(BatchUpload batchUpload) {
		primoPick=(PrimoPickBatchUpload)batchUpload;
		runValidation();
	}
	/**
	 * Validate data from file upload.
	 * @author vn87351
	 */
	private void runValidation() {
		validateUpc();
		validatePrimoPickStatus();
		validatePrimoPickDate();
	}

	/**
	 * validate primo pick date
	 */
	private void validatePrimoPickDate(){

		if (!(PrimoPickBatchUpload.PRIMO_PICK_STATUS_REJECT.equalsIgnoreCase(primoPick.getPrimoPickStatus())
				|| PrimoPickBatchUpload.PRIMO_PICK_NO.equalsIgnoreCase(primoPick.getPrimoPick()))) {
			validatePrimoPickStartDate();
			validatePrimoPickEndDate();
			validatePrimoPickStartEndDate();
		}
	}
	/**
	 * validate upc primo pick upload file
	 */
	private void validateUpc(){
		if (StringUtils.isBlank(primoPick.getUpc())) {
			primoPick.getErrors().add(ERROR_UPC_MANDATORY_FIELD);
			throw new UnexpectedInputException(ERROR_UPC_MANDATORY_FIELD);
		}else if (!isUpc(primoPick.getUpc())) {
			String errorMess=String.format(ERROR_INVALID_UPC, primoPick.getUpc());
			primoPick.getErrors().add(errorMess);
			throw new UnexpectedInputException(errorMess);
		}else{
			SellingUnit sellingUnit = sellingUnitRepository.findOne(NumberUtils.toLong(primoPick.getUpc()));
			if (sellingUnit == null || sellingUnit.getProdId() == 0) {
				primoPick.getErrors().add(String.format(PRIMO_PICK_NO_MATCH_FOUND_UPC, primoPick.getUpc()));
				throw new UnexpectedInputException(String.format(PRIMO_PICK_NO_MATCH_FOUND_UPC, primoPick.getUpc()));
			}
		}
	}

	/**
	 * validate primo pick start date
	 */
	private void validatePrimoPickStartDate(){
		if (org.apache.commons.lang.StringUtils.isNotBlank(primoPick.getPrimoPickStartDate())){
			if(DateUtils.getLocalDate(primoPick.getPrimoPickStartDate(),DFLT_DATE_FORMAT)==null) {
				primoPick.getErrors().add(ERROR_INVALID_START_DATE);
			}else if (!DateUtils.isGreaterThanToday(primoPick.getPrimoPickStartDate(), DFLT_DATE_FORMAT)) {
				primoPick.getErrors().add(ERROR_START_DATE_BEFORE_TOMOROW);
			}
		}
	}

	/**
	 * validate primo pick end date
	 */
	private void validatePrimoPickEndDate(){
		if (StringUtils.isNotBlank(primoPick.getPrimoPickEndDate())){
			if(DateUtils.getLocalDate(primoPick.getPrimoPickEndDate(),DFLT_DATE_FORMAT)==null) {
				primoPick.getErrors().add(ERROR_INVALID_END_DATE);
			}else if (!DateUtils.isGreaterThanToday(primoPick.getPrimoPickEndDate(), DFLT_DATE_FORMAT)) {
				primoPick.getErrors().add(ERROR_END_DATE_BEFORE_TOMOROW);
			}
		}
	}
	/**
	 * validate PrimoPick Start and End Date.
	 */
	private void validatePrimoPickStartEndDate() {
		if (StringUtils.isNotBlank(primoPick.getPrimoPickStartDate())
				&& StringUtils.isNotBlank(primoPick.getPrimoPickEndDate())
				&& !this.isValidStartEndDate(primoPick.getPrimoPickStartDate(), primoPick.getPrimoPickEndDate())) {
			primoPick.getErrors().add(ERROR_END_DATE_BEFORE_START_DATE);
		}
	}

	/**
	 * validate the status for primo pick file
	 */
	private void validatePrimoPickStatus() {
		if (StringUtils.isNotBlank(primoPick.getPrimoPick())
				&& PrimoPickBatchUpload.PRIMO_PICK_YES.equalsIgnoreCase(primoPick.getPrimoPick())) {
			if (StringUtils.isBlank(primoPick.getPrimoPickStatus())
					|| PrimoPickBatchUpload.PRIMO_PICK_STATUS_REJECT.equalsIgnoreCase(primoPick.getPrimoPickStatus())) {
				primoPick.getErrors().add(PRIMO_PICK_APPROVE_OR_SUBMITTED_BEFORE_ERROR);
			}
		}
	}

	/**
	 * Validate File Upload.
	 * @param data
	 *            the byte[]
	 * @throws UnexpectedInputException
	 *        The UnexpectedInputException
	 * @author vn87351
	 */
	public void validateTemplate(byte[] data) throws UnexpectedInputException{
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			Workbook workBook = new XSSFWorkbook(inputStream);
			int numberOfSheets = workBook.getNumberOfSheets();
			if (numberOfSheets > 0) {
				Row row = workBook.getSheetAt(DEFAULT_SHEET).getRow(DEFAULT_ROW_BEGIN_READ_DATA);
				String header;
				for (int columnCounter = 0; columnCounter < row.getLastCellNum(); columnCounter++) {
					header = getValueOfCell(row.getCell(columnCounter));
					header= header !=null ? header.trim(): org.apache.commons.lang.StringUtils.EMPTY;
					if(PrimoPickBatchUpload.uploadFileHeader.containsKey(columnCounter) && !PrimoPickBatchUpload.uploadFileHeader.get(columnCounter).equalsIgnoreCase(header)){
						throw new UnexpectedInputException(AbstractBatchUploadValidator.ERROR_FILE_WRONG_FORMAT);
					}
				}
			}
		}catch (Exception e) {
			logger.error("error",e);
			throw new UnexpectedInputException(AbstractBatchUploadValidator.ERROR_FILE_WRONG_FORMAT);
		}
	}

}
