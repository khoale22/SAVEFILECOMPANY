/*
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.util.upc;

/**
 * Provides a suite of functions related to UPCs and PLUs.
 *
 * @author d116773
 * @since 2.6.1
 */
final public class UpcUtils {

	private static final String EMPTY_STRING = "";
	private static final String G14_FORMAT = "%013d";
	private static final String PREDIGIT_TWO = "002";
	private static final String PREDIGIT_FOUR = "004";
	private static final long MAX_G14_UPC = 9999999999999l;
	private static final long MAX_PLU = 99999l;

	private UpcUtils (){}

	/**
	 * Determines if a UPC is a pre-digit 2 UPC.
	 *
	 * @param upc The UPC to validate.
	 * @return True if the UPC is a pre-digit 2 and false otherwise.
	 */
	public static boolean isPreDigitTwo(long upc) {

		return UpcUtils.getG14Upc(upc).startsWith(UpcUtils.PREDIGIT_TWO);
	}

	/**
	 * Determines if a UPC is a pre-digit 4 UPC.
	 *
	 * @param upc The UPC to validate.
	 * @return True if the UPC is a pre-digit 4 and false otherwise.
	 */
	public static boolean isPreDigitFour(long upc) {

		return UpcUtils.getG14Upc(upc).startsWith(UpcUtils.PREDIGIT_FOUR);
	}

	/**
	 * Determines if a check digit is valid for a UPC.
	 *
	 * @param upc The UPC to validate a check digit for.
	 * @param checkDigit The check digit you want to validate.
	 * @return True if the check digit is valid and false otherwise.
	 */
	public static boolean validateCheckDigit(long upc, int checkDigit) {
		return UpcUtils.calculateCheckDigit(upc) == checkDigit;
	}

	/**
	 * Calculates a check digit for a UPC in the G12 format. Documentation on this can be found here:
	 * https://en.wikipedia.org/wiki/Check_digit
	 *
	 * @param upc The UPC to calculate a check digit for.
	 * @return The check digit for the UPC.
	 */
	public static int calculateCheckDigit(long upc) {

		// Put the UPC into an 11 digit string.
		String unitUpc = UpcUtils.getG14Upc(upc);

		int evenDigitCount = 0;
		int oddDigitCount = 0;

		for (int i = 0; i < unitUpc.length(); i++) {
			if (i % 2 != 0)
				evenDigitCount = evenDigitCount + Integer.parseInt(unitUpc.charAt(i) + UpcUtils.EMPTY_STRING);
			else {
				oddDigitCount = oddDigitCount + Integer.parseInt(unitUpc.charAt(i) + UpcUtils.EMPTY_STRING);
			}
		}
		oddDigitCount = oddDigitCount * 3;

		int checkDigit = (evenDigitCount + oddDigitCount) % 10;
		if (checkDigit % 10 != 0) {
			checkDigit = java.lang.Math.abs(10 - checkDigit);
		}

		return checkDigit;
	}

	/**
	 * Returns a UPC in the G14 format minus the check digit.
	 *
	 * @param upc The number to convert to the G12 format.
	 * @return The UPC formatted in the G12 format.
	 */
	public static String getG14Upc(long upc) {

		if (upc <= 0 || upc > UpcUtils.MAX_G14_UPC) {
			throw new IllegalArgumentException(String.format("%d is not a G14 format UPC", upc));
		}
		return String.format(UpcUtils.G14_FORMAT, upc);
	}

	/**
	 * Checks to see if something could be a PLU. This is a simple test based on length.
	 *
	 * @param upc The UPC to see if it could be a PLU.
	 * @return True if the UPC can be a PLU and false otherwise.
	 */
	public static boolean isPlu(long upc) {
		return upc > 0 && upc < UpcUtils.MAX_PLU;
	}
}
