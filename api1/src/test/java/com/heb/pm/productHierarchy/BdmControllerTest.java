/*
 * BdmControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.Bdm;
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
 * Tests BdmController.
 *
 * @author m314029
 * @since 2.0.6
 */
public class BdmControllerTest {

	private static final String SEARCH_STRING = "SEARCH STRING";

	private static final String BDM_CODE = "A1   ";
	private static final String FIRST_NAME = "KAREN               ";
	private static final String LAST_NAME = "CASSADY             ";
	private static final String FULL_NAME = "KAREN CASSADY                 ";

	/*
	 * findByRegularExpression.
	 */

	/**
	 * Tests findByRegularExpression with all the values.
	 */
	@Test
	public void findByRegularExpressionAllValues() {

		BdmService service = this.getService(5, 100);
		BdmController controller = new BdmController();
		controller.setBdmService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setParameterValidator(new NonEmptyParameterValidator());

		controller.findBdmsByRegularExpression(SEARCH_STRING, 5, 100,
				CommonMocks.getServletRequest());
	}

	/**
	 * Tests findByRegularExpression with a null search string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByRegularExpressionNullSearchString() {

		BdmService service = this.getService(5, 100);
		BdmController controller = new BdmController();
		controller.setBdmService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setParameterValidator(new NonEmptyParameterValidator());

		controller.findBdmsByRegularExpression(null, 5, 100,
				CommonMocks.getServletRequest());
	}

	/**
	 * Tests findByRegularExpression with null page and page size.
	 */
	@Test
	public void findByRegularExpressionNullPageAndSize() {

		BdmService service = this.getService(0, 15);
		BdmController controller = new BdmController();
		controller.setBdmService(service);
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setParameterValidator(new NonEmptyParameterValidator());

		controller.findBdmsByRegularExpression(SEARCH_STRING, null, null,
				CommonMocks.getServletRequest());
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a Bdm to test with.
	 *
	 * @return A Bdm to test with.
	 */
	private Bdm getTestBdm() {

		Bdm bdm = new Bdm();
		bdm.setBdmCode(BDM_CODE);
		bdm.setFirstName(FIRST_NAME);
		bdm.setLastName(LAST_NAME);
		bdm.setFullName(FULL_NAME);
		return bdm;
	}

	/**
	 * Returns a PageableResult for the service to return.
	 *
	 * @return A PageableResult for the service to return.
	 */
	private PageableResult<Bdm> getResult() {
		List<Bdm> bdms = new ArrayList<>(1);
		bdms.add(this.getTestBdm());

		return new PageableResult<>(0, 1, 1L, bdms);
	}

	/**
	 * Mocks a findByRegularExpression call. It checks to make sure the right parameters are passed in.
	 *
	 * @param page The page value to expect.
	 * @param pageSize The pageSize to expect.
	 * @return A mock for findByRegularExpression.
	 */
	private Answer<PageableResult<Bdm>> getFindByRegularExpressionAnswer(int page, int pageSize) {

		return invocation -> {
			Assert.assertEquals(SEARCH_STRING, invocation.getArguments()[0]);
			Assert.assertEquals(page, invocation.getArguments()[1]);
			Assert.assertEquals(pageSize, invocation.getArguments()[2]);
			return this.getResult();
		};
	}

	/**
	 * Mocks up a BdmService to test with.
	 *
	 * @param page The page parameter to expect in findByRegularExpression.
	 * @param pageSize The pageSize parameter to expect in findByRegularExpression.
	 * @return A BdmService to test with.
	 */
	private BdmService getService(int page, int pageSize) {

		BdmService service = Mockito.mock(BdmService.class);
		Mockito.doAnswer(this.getFindByRegularExpressionAnswer(page, pageSize)).when(service)
				.findByRegularExpression(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());

		return service;
	}
}
