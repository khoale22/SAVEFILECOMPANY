/*
 *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 *
 */

package com.heb.pm.productDiscontinue;/*
  * Created by m314029 on 6/24/2016.
 */

import com.heb.pm.entity.ProductDiscontinueExceptionType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;

public class ProductDiscontinueExceptionTypeFromStringFormatterTest {

	private static Locale locale;

	private ProdDiscoExceptionTypeFromStringFormatter prodDiscoExceptionTypeFromStringFormatter =
			new ProdDiscoExceptionTypeFromStringFormatter();

	@BeforeClass
	public static void setup(){
		ProductDiscontinueExceptionTypeFromStringFormatterTest.locale = Locale.getDefault();
	}

	/**
	 * Test parsing correct value.
	 */
	@Test
	public void parseEnumType(){
		String valueThatShouldWork = "Vendor";
		ProductDiscontinueExceptionType discoExceptionType =
				prodDiscoExceptionTypeFromStringFormatter.parse(valueThatShouldWork, locale);

		Assert.assertNotNull(discoExceptionType);
		Assert.assertEquals(discoExceptionType, ProductDiscontinueExceptionType.VENDOR);
		Assert.assertEquals(discoExceptionType.getType(), ProductDiscontinueExceptionType.VENDOR.getType());
	}

	/**
	 * Test parsing null string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void parseNull_Throw_NumberFormatException(){
		prodDiscoExceptionTypeFromStringFormatter.parse(null, locale);
	}

	/**
	 * Test parsing value not in ProdDiscoExceptionTypeFromStringFormatter.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void parseNonEnumType_Throw_NumberFormatException(){
		String valueThatIsNotInEnum = "No";
		prodDiscoExceptionTypeFromStringFormatter.parse(valueThatIsNotInEnum, locale);
	}


	/**
	 * Test print.
	 */
	@Test
	public void testPrint() {
		ProductDiscontinueExceptionType discoExceptionType = ProductDiscontinueExceptionType.ALL;
		String stringToPrint = prodDiscoExceptionTypeFromStringFormatter.print(discoExceptionType, Locale.getDefault());

		Assert.assertNotNull(stringToPrint);
		Assert.assertEquals("All", stringToPrint);
	}
}