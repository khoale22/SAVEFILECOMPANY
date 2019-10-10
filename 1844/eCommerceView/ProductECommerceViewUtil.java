/*
 *  ProductECommerceViewUtil.
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.eCommerceView;

import com.heb.pm.entity.MasterDataExtensionAttribute;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Compares between two data..
 *
 * @author vn70516
 * @since 2.12.0
 */
public class ProductECommerceViewUtil {
	private static final String SPECIAL_AMPERSAND_ENTITY = "&amp;";
	private static final String SPECIAL_AMPERSAND_DECIMAL = "&#38;";
	private static final String SPECIAL_AMPERSAND_CHAR = "&";
	private static final String SPECIAL_SPACE_PLUS_CHAR = " +";
	public static final String SPACE = " ";
	/**
	 * STRING.
	 */
	public static final String STRING_S = "S";
	/**
	 * TIMESTMP.
	 */
	public static final String TIMESTMP = "TS";
	/**
	 * DATETIME.
	 */
	public static final String DATETIME = "DT";
	/**
	 * DECIMAL.
	 */
	public static final String DEC = "DEC";
	/**
	 * INTEGER.
	 */
	public static final String INTEGER = "I";

	/**
	 * STRING_SUB.
	 */
	public static final String STRING_SUB = "-";

	/**
	 * STRING_COLONS.
	 */
	public static final String STRING_COLONS = ";";
	/**
	 * DATE_YYYY_MM_DD.
	 */
	public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
	/**
	 * Compare two descriptions
	 *
	 * @param descriptionOne the first description.
	 * @param descriptionTwo the second description.
	 * @return true if descriptionOne is not equals descriptionTwo, or false.
	 */
	public static boolean isEqualTwoDescriptions(String descriptionOne, String descriptionTwo) {
		descriptionOne = replaceAmpersand(descriptionOne);
		descriptionOne = replaceSpaceString(descriptionOne);
		descriptionTwo = replaceAmpersand(descriptionTwo);
		descriptionTwo = replaceSpaceString(descriptionTwo);
		boolean flag = false;
		if (StringUtils.isEmpty(descriptionOne) && StringUtils.isEmpty(descriptionTwo)) {
			flag = false;
		} else if (StringUtils.isEmpty(descriptionOne) || StringUtils.isEmpty(descriptionTwo)) {
			flag = true;
		} else if (!descriptionOne.equals(descriptionTwo)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * replaceAmpersand.
	 *
	 * @param str string
	 * @return formatted string value
	 */
	private static String replaceAmpersand(String str) {
		String newString = str;
		if (str != null) {
			if (newString.contains(SPECIAL_AMPERSAND_ENTITY) || newString.contains(SPECIAL_AMPERSAND_DECIMAL)) {
				newString = newString.replaceAll(SPECIAL_AMPERSAND_ENTITY, SPECIAL_AMPERSAND_CHAR).replaceAll(SPECIAL_AMPERSAND_DECIMAL, SPECIAL_AMPERSAND_CHAR);
			}
		}
		return newString;
	}

	/**
	 * replaceSpace.
	 *
	 * @param str String
	 * @return formatted string value
	 */
	private static String replaceSpaceString(String str) {
		String newString = StringUtils.trimToEmpty(str);
		if (!StringUtils.isEmpty(newString)) {
			newString = str.replaceAll(SPECIAL_SPACE_PLUS_CHAR, SPACE);
		}
		return newString.trim();
	}

	/**
	 * Returns Tag summary.
	 *
	 * @param masterDataExtensionAttribute the MasterDataExtensionAttribute.
	 * @return String
	 */
	public static String getTagSummary(MasterDataExtensionAttribute masterDataExtensionAttribute) {
		String detail = StringUtils.EMPTY;
		String dmCd = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttribute().getAttributeDomainCode());
		boolean entyAttValLstSw = masterDataExtensionAttribute.getAttribute().getAttributeValueList();
		if (entyAttValLstSw) {
			if(null != masterDataExtensionAttribute.getAttributeCode()){
				detail = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeCode().getAttributeValueCode());
			}
		} else {
			switch (dmCd) {
				case STRING_S:
					detail = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttributeValueText());
					break;
				case TIMESTMP:
					detail = masterDataExtensionAttribute.getAttributeValueTime().toString();
					break;
				case DATETIME:
					detail = masterDataExtensionAttribute.getAttributeValueDate().toString();
					break;
				case DEC:
					if (masterDataExtensionAttribute.getAttributeValueNumber() != null) {
						detail = String.valueOf(masterDataExtensionAttribute.getAttributeValueNumber());
					}
					break;
				case INTEGER:
					if (masterDataExtensionAttribute.getAttributeValueNumber() != null) {
						detail = String.valueOf(masterDataExtensionAttribute.getAttributeValueNumber().intValue());
					}
					break;
				default:
			}
		}
		// ATTR_DES
		String attrDes = StringUtils.trimToEmpty(masterDataExtensionAttribute.getAttribute().getAttributeDescription());
		if (!StringUtils.isEmpty(attrDes) && !StringUtils.isEmpty(detail)) {
			detail = attrDes.concat(SPACE).concat(STRING_SUB).concat(SPACE).concat(detail);
			detail = detail.trim();
		} else {
			detail = null;
		}
		return detail;
	}

	/**
	 * Returns the Date object from date string and format.
	 *
	 * @param dateString  the string of date.
	 * @param format the format date.
	 * @return Date type.
	 */
	public static Date getDate(final String dateString, final String format) {
		if (StringUtils.isBlank(dateString)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
		Date date;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}
	/**
	 * Convert local date to string.
	 *
	 * @param date the local date
	 * @return the date string
	 */
	public static String convertDateToStringDateYYYYMMDD(final LocalDate date) {
		if (date == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_YYYY_MM_DD);
		return date.format(formatter);
	}

	/**
	 * Convert Local date to date.
	 *
	 * @param date the local date.
	 * @return the date.
	 */
	public static Date convertLocalDateToDate(final LocalDate date) {
		if (date == null) {
			return null;
		}
		return getDate(convertDateToStringDateYYYYMMDD(date),DATE_YYYY_MM_DD);
	}

	/**
	 * Get current date as string with format YYYYMMDD.
	 *
	 * @return the date as string.
	 */
	public static String getCurrentDateByDateYYYYMMDD(){
		return new SimpleDateFormat(DATE_YYYY_MM_DD, Locale.getDefault()).format(new Date());
	}
	/**
	 * Gets current date.
	 *
	 * @return the current date.
	 */
	public static Date getCurrentDate(){
		return getDate(getCurrentDateByDateYYYYMMDD(), DATE_YYYY_MM_DD);
	}

	/**
	 * Compare input date greater than or equal to current date.
	 *
	 * @param date the input date.
	 * @return true if input greater than current date.
	 */
	public static boolean compareGreaterThanOrEqualToCurrentDate(LocalDate date) {
		boolean check = false;
		Date varDate = convertLocalDateToDate(date);
		Date currentDate = getCurrentDate();
		if (varDate.compareTo(currentDate) >= 0) {
			check = true;
		}
		return check;
	}
}
