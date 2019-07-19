/*
 * VendorServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.vendor;

import com.heb.pm.entity.Vendor;


import com.heb.pm.repository.VendorIndexRepository;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests VendorService.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorServiceTest {

	protected static final Vendor TEST_VENDOR = new Vendor();
	private static final String SEARCH_STRING = "TEST SEARCH STRING";
	private static final Integer PAGE = 10;
	private static final Integer PAGE_SIZE = 456;

	/**
	 * Tests findByVendorNumber.
	 */
	@Test
	public void findByVendorNumber() {
		VendorService vendorService = new VendorService();
		vendorService.setIndexRepository(this.getVendorIndexRepository());

		Vendor v = vendorService.findByVendorNumber(Integer.valueOf(10));
		Assert.assertEquals(VendorServiceTest.TEST_VENDOR, v);
	}

	/**
	 * Tests findByRegularExpression
	 */
	@Test
	public void findByRegularExpression() {
		VendorService vendorService = new VendorService();
		vendorService.setIndexRepository(this.getVendorIndexRepository());
		PageableResult pr = vendorService.findByRegularExpression(VendorServiceTest.SEARCH_STRING,
				VendorServiceTest.PAGE, VendorServiceTest.PAGE_SIZE);
		Assert.assertTrue(pr.isComplete());
		Assert.assertNotNull(pr.getData());
		Assert.assertEquals(Integer.valueOf(1), pr.getPageCount());
		Assert.assertEquals(Long.valueOf(1), pr.getRecordCount());
		Assert.assertEquals(VendorServiceTest.TEST_VENDOR, pr.getData().iterator().next());
	}

	/**
	 * Returns an answer that will check inputs to findByRegularExpression and return a vendor list with one
	 * vendor in it.
	 *
	 * @return An answer for findByRegularExpression.
	 */
	private Answer<Page<Vendor>> getFindByRegularExpressionAnswer() {

		return invocation -> {
			// The method will make the search string lower case and add a .* to the beginning and end.
			Assert.assertEquals(".*" + VendorServiceTest.SEARCH_STRING.toLowerCase() + ".*",
					invocation.getArguments()[0]);

			// The second argument is a page request.
			Assert.assertTrue(invocation.getArguments()[1] instanceof PageRequest);
			PageRequest p = (PageRequest)invocation.getArguments()[1];
			Assert.assertEquals((long)VendorServiceTest.PAGE, p.getPageNumber());
			Assert.assertEquals((long)VendorServiceTest.PAGE_SIZE, p.getPageSize());

			List<Vendor> vendors = new ArrayList<>();
			vendors.add(VendorServiceTest.TEST_VENDOR);

			return new PageImpl<>(vendors, null, 1);
		};
	}
	/**
	 * Returns a VendorIndexRepository for the tests.
	 *
	 * @return A VendorIndexRepository for the tests.
	 */
	private VendorIndexRepository getVendorIndexRepository() {

		VendorIndexRepository vendorIndexRepository = Mockito.mock(VendorIndexRepository.class);

		Mockito.when(vendorIndexRepository.findOne(Mockito.anyObject())).thenReturn(VendorServiceTest.TEST_VENDOR);

		Mockito.doAnswer(this.getFindByRegularExpressionAnswer())
				.when(vendorIndexRepository).findByRegularExpression(Mockito.anyString(), Mockito.anyObject());

		return vendorIndexRepository;
	}
}
