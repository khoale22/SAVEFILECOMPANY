/*
 *  AbstractBatchUploadValidator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.batchUpload;
import com.heb.pm.batchUpload.parser.ExcelParser;
import com.heb.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Holds all validate of Batch Upload.
 *
 * @author vn55306
 * @since 2.7.0
 */
public abstract class AbstractBatchUploadValidator implements Serializable{
	private static final Logger logger = LoggerFactory.getLogger(AbstractBatchUploadValidator.class);
	public static final String ERROR_MANDATORY_FIELD = " is a mandatory field.";
	public static final String ERROR_FILE_WRONG_FORMAT = "File wrong format.";
	public static final String ERROR_INVALID_UPC = "Invalid Upc ";
	public static final String ERROR_INVALID_PRODUCT_ID = "Invalid ProductId ";
	public static final String ERROR_DATE_FIELD = " format must be MM/DD/YYYY.";
	/**
	 * DFLT_DATE_FORMAT.
	 */
	public static final String DFLT_DATE_FORMAT = "MM/dd/yyyy";
	/**
	 * MAX_LENGTH_YEAR.
	 */
	public static final int MAX_LENGTH_YEAR = 4;
	/**
	 * YEAR_MIN_VALUE.
	 */
	public static final int YEAR_MIN_VALUE = 1900;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	 /**
     * Validate row value of file Upload.
     * @param batchUpload : the BatchUpload
     *            List<String>
	 * @author vn55306
     */

	public abstract void validateRow(BatchUpload batchUpload);
	/**
	 * check limit value.
	 * @param value
	 *            Object
	 * @param maxlength
	 *            short
	 * @return boolean
	 * @author vn55306
	 */
	protected boolean isStringLimit(String value, int maxlength) {
		if (StringUtils.isNotBlank(value)) {
			if (value.trim().length() > maxlength) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Validate limit integer.
	 * @param value
	 *            the Value
	 * @author vn55306
	 */
	protected boolean isInteger(String value) {
		boolean flag=true;
		try {
			// check if valid integer number
			if (new BigInteger(value).compareTo(new BigInteger("999999999")) > 0) {
				flag = false;
			} else if (new BigInteger(value).compareTo(BigInteger.ZERO) <= 0) {
				flag = false;
			}
		}catch (NumberFormatException e){
			flag = false;
		}
		return flag;
	}

	/**
	 * check valid effective start date.
	 * @param effectiveDate
	 *            String
	 * @return boolean
	 * @author vn55306
	 */
	protected static boolean isValidEffectiveStartDate(String effectiveDate) {
		return DateUtils.isGreaterThanToday(effectiveDate,DFLT_DATE_FORMAT);
	}

	/**
	 * check valid effective end date.
	 * @param effectiveDate
	 *            String
	 * @return boolean
	 * @author vn55306
	 */
	protected static boolean isValidEffectiveEndDate(String effectiveDate) {
		return DateUtils.isGreaterThanToday(effectiveDate,DFLT_DATE_FORMAT);
	}

	/**
	 * check valid format effective start and end date.
	 * @param effectiveStartDate
	 *            String
	 * @param endDate  String
	 * @return boolean
	 * @author vn55306
	 */
	protected static boolean isValidStartEndDate(String effectiveStartDate, String endDate) {
		LocalDate effStartDt = DateUtils.getLocalDate(effectiveStartDate, DFLT_DATE_FORMAT);
		LocalDate endDt = DateUtils.getLocalDate(endDate, DFLT_DATE_FORMAT);
		if (effStartDt != null && endDt != null) {
			return endDt.compareTo(effStartDt) > 0;
		}
		return false;
	}

	/**
	 * validate UPC.
	 * @param value
	 *            Object
	 * @return boolean
	 * @author vn55306
	 */
	protected boolean isUpc(String value) {
		boolean isValid = true;
		if (StringUtils.isBlank(value)) {
			isValid = false;
		} else {
			try {
				Long.parseLong(value.toString());
			} catch (NumberFormatException ex) {
				isValid = false;
			}
		}
		return isValid;
	}
	/**
	 * validate Year.
	 * @param value
	 *            Object
	 * @return boolean
	 * @author vn55306
	 */
	protected boolean isYear(String value) {
		boolean isValid = true;
		if (StringUtils.isNotBlank(value)) {
			if (value.trim().length() == MAX_LENGTH_YEAR) {
				try {
					int year = Integer.parseInt(value);
					if (year < YEAR_MIN_VALUE) {
						isValid = false;
					}
				} catch (NumberFormatException e) {
					isValid = false;
				}
			} else {
				isValid = false;
			}
		} else {
			isValid = false;
		}
		return isValid;
	}
	/**
	 * Verifies valid URL.
	 * @param urlString String
	 * @return true if valid
	 * @author vn55306
	 */
	protected boolean isValidURL(String urlString){
		UrlValidator urlValidator = new UrlValidator();
		return urlValidator.isValid(urlString);
	}
	/**
	 * parse number .
	 * @param aStr String
	 * @param targetClass target number class
	 * @return number with target type
	 * @author vn55306
	 */
	protected  static <T extends Number> T parseNumber(String aStr, Class<T> targetClass) {
		try {
			return org.springframework.util.NumberUtils.parseNumber(aStr, targetClass);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * get Value Of Cell.
	 *
	 * @param cell
	 *            Cell
	 * @return String
	 */
	protected String getValueOfCell(Cell cell) {
		String cellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					cellValue = cell.getRichStringCellValue().getString();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat dateFormat = new SimpleDateFormat(ExcelParser.DATE_MM_DD_YYYY, Locale.getDefault());
						cellValue = dateFormat.format(cell.getDateCellValue());
					} else {
						double val = cell.getNumericCellValue();
						if (Math.round(val) == val) {
							cellValue = String.valueOf(Math.round(val));
						} else {
							cellValue = new DecimalFormat("#############.#####").format(val);
						}
					}
					break;
				case Cell.CELL_TYPE_BLANK:
					cellValue = ExcelParser.STR_EMPTY;
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				default:
					break;
			}
		}
		return cellValue;
	}
	/**
	 * Check if is able to be parsed into a double, returns boolean.
	 *
	 * @param str
	 *            the str
	 * @return the boolean
	 */
	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	/**
	 * getTrimmedValue.
	 *
	 * @param value
	 *            String value
	 * @return String value
	 */
	protected String getTrimmedValue(final String value) {
		if (StringUtils.isNotBlank(value)) {
			return value.trim();
		}
		return StringUtils.EMPTY;
	}
}
