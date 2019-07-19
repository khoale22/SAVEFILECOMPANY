/*
 * VendorControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.vendor;

import com.heb.pm.entity.Vendor;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests VendorController.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorControllerTest {

	private static final String SEARCH_STRING = "TEST SEARCH STRING";
	private static final int PAGE = 1009;
	private static final int PAGE_SIZE = 82932;
	private static final int VENDOR_NUMBER = 823345;

	/*
	 * findByRegularExpression
	 */

	/**
	 * Tests findByRegularExpression with all good values.
	 */
	@Test
	public void findByRegularExpressionAllValues() {

		VendorService vendorService = this.getVendorService(VendorControllerTest.PAGE, VendorControllerTest.PAGE_SIZE);
		VendorController vendorController = this.getVendorController(vendorService);

		vendorController.findByRegularExpression(VendorControllerTest.SEARCH_STRING,
				VendorControllerTest.PAGE, VendorControllerTest.PAGE_SIZE, CommonMocks.getServletRequest());
	}

	/**
	 * Tests findByRegularExpression with null passed in as the search string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByRegularExpressionNullSearchString() {

		VendorService vendorService = this.getVendorService(VendorControllerTest.PAGE, VendorControllerTest.PAGE_SIZE);
		VendorController vendorController = this.getVendorController(vendorService);

		vendorController.findByRegularExpression(null,
				VendorControllerTest.PAGE, VendorControllerTest.PAGE_SIZE, CommonMocks.getServletRequest());
	}

	/**
	 * Tests findByRegularExpression with null passed in as page and page size.
	 */
	@Test
	public void findByRegularExpressionNullPageAndPageSize() {

		// Default page is 0 and page size is 15 in VendorController
		VendorService vendorService = this.getVendorService(0, 15);
		VendorController vendorController = this.getVendorController(vendorService);

		vendorController.findByRegularExpression(VendorControllerTest.SEARCH_STRING,
				null, null, CommonMocks.getServletRequest());
	}

	/*
	 * findByVendorNumber
	 */
	/**
	 * Test findByVendorNumber with null passed in as vendor number.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByVendorNumberNull() {

		VendorService vendorService = this.getVendorService(VendorControllerTest.PAGE, VendorControllerTest.PAGE_SIZE);
		VendorController vendorController = this.getVendorController(vendorService);

		vendorController.findByVendorNumber(null, CommonMocks.getServletRequest());
	}

	/**
	 * Tests findByVendorNumber with good values.
	 */
	@Test
	public void findByVendorNumberGoodValues() {

		VendorService vendorService = this.getVendorService(VendorControllerTest.PAGE, VendorControllerTest.PAGE_SIZE);
		VendorController vendorController = this.getVendorController(vendorService);

		vendorController.findByVendorNumber(VendorControllerTest.VENDOR_NUMBER, CommonMocks.getServletRequest());
	}

	/*
	 * support functions
	 */

	/**
	 * Returns a Vendor to test with.
	 *
	 * @return A Vendor to test with.
	 */
	private Vendor getVendor() {

		Vendor v = new Vendor();
		v.setVendorNumber(VendorControllerTest.VENDOR_NUMBER);
		v.setVendorName("TEST");
		return v;
	}

	/**
	 * Returns a List of Vendors that can be returned by calls inside VendorService.
	 *
	 * @return A List of Vendors.
	 */
	private PageableResult<Vendor> getResult() {

		List<Vendor> vendors = new ArrayList<>();
		vendors.add(this.getVendor());

		return new PageableResult<>(0, 1, 1L, vendors);
	}

	/**
	 * Returns an Answer that checks the values passed into findByVendor.
	 *
	 * @return An Answer that checks the values passed into findByVendor.
	 */
	private Answer<Vendor> getFindByVendorAnswer() {
		return invocation -> {
			Assert.assertEquals(VendorControllerTest.VENDOR_NUMBER, invocation.getArguments()[0]);
			return this.getVendor();
		};
	}

	/**
	 * Returns an Answer that checks the values passed into findByRegularExpression.
	 *
	 * @param page The page number to check against.
	 * @param pageSize The page size to check against.
	 * @return An Answer that checks the values passed into findByRegularExpression.
	 */
	private Answer<PageableResult<Vendor>> getFindByRegularExpressionAnswer(int page, int pageSize) {

		return invocation -> {
			Assert.assertEquals(VendorControllerTest.SEARCH_STRING, invocation.getArguments()[0]);
			Assert.assertEquals(page, invocation.getArguments()[1]);
			Assert.assertEquals(pageSize, invocation.getArguments()[2]);
			return this.getResult();
		};
	}

	/**
	 * Returns a VendorService to use in tests.
	 *
	 * @param page The page number to check against.
	 * @param pageSize The page size to check against.
	 * @return A VendorService to use in tests.
	 */
	private VendorService getVendorService(int page, int pageSize) {

		VendorService vendorService = Mockito.mock(VendorService.class);
		Mockito.doAnswer(this.getFindByRegularExpressionAnswer(page, pageSize))
				.when(vendorService).findByRegularExpression(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
		Mockito.doAnswer(this.getFindByVendorAnswer()).when(vendorService).findByVendorNumber(Mockito.anyInt());

		return vendorService;
	}

	/**
	 * Returns a VendorController to test against.
	 *
	 * @param vendorService The VendorService to use in the tests.
	 * @return A VendorController to test against.
	 */
	private VendorController getVendorController(VendorService vendorService) {
		VendorController vendorController = new VendorController();
		vendorController.setVendorService(vendorService);
		vendorController.setUserInfo(CommonMocks.getUserInfo());
		vendorController.setParameterValidator(new NonEmptyParameterValidator());

		return vendorController;
	}
}
