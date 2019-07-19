/*
 *  WineValidator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.categorySpecific;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.batchUpload.BatchUploadType;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.heb.pm.batchUpload.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Holds all validate of Wine Batch Upload.
 *
 * @author vn55306
 * @since 2.7.0
 */
@Component
public class WineValidator extends AbstractBatchUploadValidator {
	private static final Logger logger = LoggerFactory.getLogger(WineValidator.class);
	private static final String DATE_MM_DD_YYYY = "MM/dd/yyyy";
	private static final String INVALID_MIN_INVENTORY_THRESHOLD_COUNT = "Min Inventory Threshold Count must be a integer number.";
	private static final String ERROR_VINTAGE_MUST_BE_EQUAL_TO_OR_GREATER_THAN_1900 = "Vintage must be equal to or greater than 1900.";
	private static final String INVALID_VINTAGE_REQUIRED ="Vintage is a mandatory field.";
	private static final String ERROR_MUST_BE_A_DECIMAL_TWO_PLACES = " must be a decimal number with 2 places. " +
			"Example: 99.999.";
	private static final String ERROR_MUST_BE_GREATER_THAN_ZERO_AND_SMALLER_THAN_ONE_HUNDRED = "must be greater than 0 and smaller than 100.";
	WineBatchUpload wine;
	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	SellingUnit sellingUnit;
	@Override
	public void validateRow(BatchUpload batchUpload) {
		wine=(WineBatchUpload)batchUpload;
		sellingUnit=null;
		this.runValidation();
	}
	/**
	 * Validate data from file upload.
	 * @author vn55306
	 */
	private void runValidation() {
		this.validateUpc();
		this.validateMinInventoryThresholdCount();
		this.validateForWineScore();
		this.validateVintageYear();
		this.validateAlcoholPercent();
		this.validateCriticalScorePercent();
		this.validateUpdatedDate();

	}
	/**
	 * Validate Upc.
	 * @author vn55306
	 */
	private void validateUpc() {
		if(!isValidUpc()){
			throw new UnexpectedInputException(WineBatchUpload.uploadFileHeader.get(WineBatchUpload.COL_POS_UPC) + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD);
		}
	}

	/**
	 * check if the upc is valid or not.
	 * @return boolean
	 * @author vn55306
	 */
	private boolean isValidUpc() {
		if(StringUtils.isNotBlank(this.wine.getUpc()) &&  this.isUpc(this.wine.getUpc()) && this.getSellingUnit()
				!=null){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Validate Min Inventory Threshold Count.
	 * @author vn55306
	 */
	private void validateMinInventoryThresholdCount() {
		if(StringUtils.isNotBlank(this.wine.getMinInventoryThresholdCount())){
			if(!isInteger(this.wine.getMinInventoryThresholdCount())){
				this.wine.getErrors().add(WineValidator.INVALID_MIN_INVENTORY_THRESHOLD_COUNT);
			}
		}
	}
	/**
	 * validate Business Data For WineScore.
	 * @author vn55306
	 */
	private void validateForWineScore() {
		String errorMes = this.checkDataForWine(this.wine.getCriticalScore1(), this.wine.getScoredBy1(), this.wine.getTastingNotes1(), this.wine.getUpdatedDate1(),
				this.wine.getScore1Vintage(), 1);
		// validate prod score 2
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkDataForWine(this.wine.getCriticalScore2(), this.wine.getScoredBy2(), this.wine.getTastingNotes2(), this.wine.getUpdatedDate2(),
					this.wine.getScore2Vintage(), 2);
		}
		// validate prod score 3
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkDataForWine(this.wine.getCriticalScore3(), this.wine.getScoredBy3(), this.wine.getTastingNotes3(), this.wine.getUpdatedDate3(),
					this.wine.getScore3Vintage(), 3);
		}

		if (StringUtils.isNotBlank(errorMes)) {
			this.wine.getErrors().add(errorMes);
		}
	}/**
	 * check Data For Wine.
	 * @param criticalScore
	 *            String
	 * @param scoredBy
	 *            String
	 * @param tastingNotes
	 *            String
	 * @param updatedDate
	 *            String
	 * @param order
	 *            int
	 * @return String
	 * @author vn55306
	 */
	private String checkDataForWine(String criticalScore, String scoredBy, String tastingNotes, String updatedDate, String scoreVintage, int order) {
		String errorMes = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(criticalScore) || StringUtils.isNotBlank(scoredBy) || StringUtils.isNotBlank(tastingNotes)
				|| StringUtils.isNotBlank(updatedDate) || StringUtils.isNotBlank(scoreVintage)) {
			if (StringUtils.isBlank(criticalScore)) {
				errorMes = WineBatchUpload.CRITICAL_SCORE + order + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD;
			} else if (StringUtils.isBlank(scoredBy)) {
				errorMes = WineBatchUpload.SCORE_BY + order + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD;
			} else if (StringUtils.isBlank(tastingNotes)) {
				errorMes = WineBatchUpload.TASTING_NOTES + order + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD;
			} else if (StringUtils.isBlank(updatedDate)) {
				errorMes = WineBatchUpload.UPDATE_DATE + order + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD;
			} else if (StringUtils.isBlank(scoreVintage)) {
				errorMes = "Score  " + order + " Vintage" + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD;
			}
		}
		return errorMes;
	}
	/**
	 * validate Vintage Year.
	 * @author vn55306
	 */
	private void validateVintageYear() {
		String errorMes = this.checkVintageYear(wine.getVintage(), 0);
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkVintageYear(this.wine.getScore1Vintage(), 1);
		}
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkVintageYear(this.wine.getScore2Vintage(), 2);
		}
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkVintageYear(this.wine.getScore3Vintage(), 3);
		}
		if (StringUtils.isNotBlank(errorMes)) {
			this.wine.getErrors().add(errorMes);
		}

	}
	/**
	 * validate Vintage Year.
	 * @param year
	 *            String
	 *  @param order
	 *  		int
	 * @author vn55306
	 */
	private String checkVintageYear(String year, int order) {
		String errorMes = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(year)) {
			if (!this.isYear(year)) {
				if (order > 0) {
					errorMes = "Score " + order + " " +WineValidator.ERROR_VINTAGE_MUST_BE_EQUAL_TO_OR_GREATER_THAN_1900;
				} else {
					errorMes = WineValidator.ERROR_VINTAGE_MUST_BE_EQUAL_TO_OR_GREATER_THAN_1900;
				}
			}
		} else {
			if (order > 0) {
				errorMes = "Score " + order + " "+WineValidator.INVALID_VINTAGE_REQUIRED;
			} else {
				errorMes = WineValidator.INVALID_VINTAGE_REQUIRED;
			}
		}
		return errorMes;
	}
	/**
	 * check Alcohol Percent.
	 * @author vn55306
	 */
	private void validateAlcoholPercent() {
		String errorMes = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(this.wine.getAlcoholPercent())) {
			if (this.wine.getAlcoholPercent().length() < 6) {
				Double percent = NumberUtils.toDouble(this.wine.getAlcoholPercent());
				if (percent == 0) {
					errorMes = WineBatchUpload.uploadFileHeader.get(WineBatchUpload.COL_POS_ALCOHOL_PERCENTAGE) + WineValidator.ERROR_MUST_BE_GREATER_THAN_ZERO_AND_SMALLER_THAN_ONE_HUNDRED;
				} else if (percent >= 100) {
					errorMes = WineBatchUpload.uploadFileHeader.get(WineBatchUpload.COL_POS_ALCOHOL_PERCENTAGE) + WineValidator.ERROR_MUST_BE_GREATER_THAN_ZERO_AND_SMALLER_THAN_ONE_HUNDRED;
				}
			} else {
				errorMes = WineBatchUpload.uploadFileHeader.get(WineBatchUpload.COL_POS_ALCOHOL_PERCENTAGE) + WineValidator.ERROR_MUST_BE_A_DECIMAL_TWO_PLACES;
			}
		}
		if (StringUtils.isNotBlank(errorMes)) {
			this.wine.getErrors().add(errorMes);
		}
	}
	/**
	 * validate Critical Score Percent.
	 * @author vn55306
	 */
	private void validateCriticalScorePercent() {
		String errorMes = this.checkCriticalScorePercent(this.wine.getCriticalScore1(), 1);
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkCriticalScorePercent(this.wine.getCriticalScore2(), 2);
		}
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkCriticalScorePercent(this.wine.getCriticalScore3(),3);
		}
		if (StringUtils.isNotBlank(errorMes)) {
			this.wine.getErrors().add(errorMes);
		}

	}
	/**
	 * check Critical Score Percent.
	 * @param value
	 *            String
	 * @param order
	 *           int
	 * @author vn55306
	 */
	private String checkCriticalScorePercent(String value, int order) {
		String errorMes = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(value)) {
			if (value.length() < 7) {
				double percent = NumberUtils.toDouble(value);
				if (percent < 1 || percent > 100) {
					errorMes = WineBatchUpload.CRITICAL_SCORE + order + WineValidator.ERROR_MUST_BE_GREATER_THAN_ZERO_AND_SMALLER_THAN_ONE_HUNDRED;
				}
			} else {
				errorMes = WineBatchUpload.CRITICAL_SCORE + order + WineValidator.ERROR_MUST_BE_A_DECIMAL_TWO_PLACES;
			}
		}
		return errorMes;
	}
	/**
	 * validate update date value.
	 * @author vn55306
	 */
	private void validateUpdatedDate() {
		String errorMes = this.checkFormatUpdatedDate(this.wine.getUpdatedDate1(), 1);
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkFormatUpdatedDate(this.wine.getUpdatedDate2(), 2);
		}
		if (StringUtils.isBlank(errorMes)) {
			errorMes = this.checkFormatUpdatedDate(this.wine.getUpdatedDate3(),3);
		}
		if (StringUtils.isNotBlank(errorMes)) {
			this.wine.getErrors().add(errorMes);
		}

	}
	/**
	 * check update date value.
	 * @param date
	 *            String
	 * @param order
	 * 			int
	 * @author vn55306
	 */
	private String checkFormatUpdatedDate(String date, int order) {
		String errorMes = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(date)) {
			if (DateUtils.getLocalDate(date,DATE_MM_DD_YYYY)==null) {
				errorMes = WineBatchUpload.UPDATE_DATE + order + AbstractBatchUploadValidator.ERROR_DATE_FIELD;
			}
		} else {
			errorMes = WineBatchUpload.UPDATE_DATE + order + AbstractBatchUploadValidator.ERROR_MANDATORY_FIELD;
		}
		return errorMes;
	}
	/**
	 * Return The SellingUnit.
	 * @return The SellingUnit.
	 * @author vn55306
	 */
	private SellingUnit getSellingUnit() {
		if(sellingUnit==null){
			sellingUnit = sellingUnitRepository.findOne(Long.valueOf(this.wine.getUpc()));
		}
		return sellingUnit;
	}
	/**
	 * Validate File Upload.
	 * @param data
	 *            the byte[]
	 * @throws UnexpectedInputException
	 *        The UnexpectedInputException
	 * @author vn55306
	 */
	public void validateTemplate(byte[] data) throws UnexpectedInputException{
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			Workbook workBook = new XSSFWorkbook(inputStream);
			int numberOfSheets = workBook.getNumberOfSheets();
			if (numberOfSheets > 0) {
				Row rowHeader = workBook.getSheetAt(0).getRow(1);
				String header;
				for (int columnCounter = 0; columnCounter < rowHeader.getLastCellNum(); columnCounter++) {
					header = this.getValueOfCell(rowHeader.getCell(columnCounter));
					header= header !=null ? header.trim():StringUtils.EMPTY;
					if(WineBatchUpload.uploadFileHeader.containsKey(columnCounter)){
						if(!WineBatchUpload.uploadFileHeader.get(columnCounter).equalsIgnoreCase(header)){
							throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
						}
					}
				}

			}
		}catch (Exception e) {
			throw new UnexpectedInputException(ERROR_FILE_WRONG_FORMAT);
		}

	}
	/**
	 * This method is used to parse excel file for Apparel,
	 * Kitchenware, Large_and_Small_Appliances and Softlines template.
	 *
	 * @param fileData
	 *        byte[]
	 * @param batchUploadType
	 *       BatchUploadType
	 * @return String
	 * @author vn55306
	 */
	public String getCategorySpecificName(byte[] fileData, BatchUploadType batchUploadType) {
		Workbook workBook = null;
		Sheet sheet = null;
		String categorySpecificName = StringUtils.EMPTY;
		try {
			InputStream inputStream = new ByteArrayInputStream(fileData);
			workBook = new XSSFWorkbook(inputStream);
			if (workBook.getNumberOfSheets() > 0) {
				sheet = workBook.getSheetAt(0);
				Row firstRow = sheet.getRow(0);
				Cell cell = null;
				// for upload category specific attribute
				if (batchUploadType == batchUploadType.CATEGORY_SPECIFIC) {
					cell = firstRow.getCell((short) 0);
				} else	{// for upload category specific attribute
					cell = firstRow.getCell((short) 1);
				}
				categorySpecificName = (cell != null) ? this.getValueOfCell(cell) : StringUtils.EMPTY;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return categorySpecificName;
	}
}
