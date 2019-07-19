/*
 * SubDepartmentControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
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
 * Tests SubDepartmentController.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubDepartmentControllerTest {

	private static final String SEARCH_STRING = "TEST SEARCH STRING";
	private static final int PAGE = 1009;
	private static final int PAGE_SIZE = 82932;
	private static final String DEPARTMENT = "07   ";
	private static final String SUB_DEPARTMENT = "A    ";
	private static final String SUB_DEPARTMENT_NAME = "GROCERY";

	/*
	 * findByRegularExpression
	 */

	/**
	 * Tests findByRegularExpression with a search string, page, and page size.
	 */
	@Test
	public void findByRegularExpressionAllValues() {
		SubDepartmentService subDepartmentService = this.getSubDepartmentService(SubDepartmentControllerTest.PAGE,
				SubDepartmentControllerTest.PAGE_SIZE);
		SubDepartmentController subDepartmentController = this.getSubDepartmentController(subDepartmentService);
		subDepartmentController.findByRegularExpression(SubDepartmentControllerTest.SEARCH_STRING,
				SubDepartmentControllerTest.PAGE, SubDepartmentControllerTest.PAGE_SIZE,
				CommonMocks.getServletRequest());
		// All the tests are done in the answer to findByRegularExpression.
	}

	/**
	 * Tests findByRegularExpression with a null search string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByRegularExpressionNullSearchString() {
		SubDepartmentService subDepartmentService = this.getSubDepartmentService(SubDepartmentControllerTest.PAGE,
				SubDepartmentControllerTest.PAGE_SIZE);
		SubDepartmentController subDepartmentController = this.getSubDepartmentController(subDepartmentService);
		subDepartmentController.findByRegularExpression(null,
				SubDepartmentControllerTest.PAGE, SubDepartmentControllerTest.PAGE_SIZE,
				CommonMocks.getServletRequest());
	}

	/**
	 * Tests findByRegularExpression with a search string but null for page and page size.
	 */
	@Test
	public void findByRegularExpressionNullPageAndPageSize() {
		SubDepartmentService subDepartmentService = this.getSubDepartmentService(0, 15);
		SubDepartmentController subDepartmentController = this.getSubDepartmentController(subDepartmentService);
		subDepartmentController.findByRegularExpression(SubDepartmentControllerTest.SEARCH_STRING,
				null, null,
				CommonMocks.getServletRequest());
		// All the tests are done in the answer to findByRegularExpression.
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a SubDepartment to test with.
	 *
	 * @return A SubDepartment to test with.
	 */
	private SubDepartment getSubDepartment() {

		SubDepartmentKey key = new SubDepartmentKey();
		key.setDepartment(SubDepartmentControllerTest.DEPARTMENT);
		key.setSubDepartment(SubDepartmentControllerTest.SUB_DEPARTMENT);

		SubDepartment subDepartment = new SubDepartment();
		subDepartment.setKey(key);
		subDepartment.setName(SubDepartmentControllerTest.SUB_DEPARTMENT_NAME);

		return subDepartment;
	}

	/**
	 * Returns a PageableResult that the service layer will return.
	 *
	 * @return A PageableResult that the service layer will return.
	 */
	private PageableResult<SubDepartment> getResult() {
		List<SubDepartment> subDepartmentList = new ArrayList<>();
		subDepartmentList.add(this.getSubDepartment());

		return new PageableResult<>(0, 1, 1L, subDepartmentList);
	}

	/**
	 * Mocks up the call to findByRegularExpression in the service. It checks to make sure the right parameters
	 * are getting passed down.
	 *
	 * @param page The page number being asked for.
	 * @param pageSize The number of records
	 * @return The mocked call to findByRegularExpression.
	 */
	private Answer<PageableResult<SubDepartment>> getFindByRegularExpressionAnswer(int page, int pageSize) {

		return invocation -> {
			Assert.assertEquals(SubDepartmentControllerTest.SEARCH_STRING, invocation.getArguments()[0]);
			Assert.assertEquals(page, invocation.getArguments()[1]);
			Assert.assertEquals(pageSize, invocation.getArguments()[2]);
			return this.getResult();
		};
	}

	/**
	 * Returns a SubDepartmentService to test with.
	 *
	 * @param page The page for the service to expect.
	 * @param pageSize A page size for the service to expect.
	 * @return A SubDepartmentService to test with.
	 */
	private SubDepartmentService getSubDepartmentService(int page, int pageSize) {

		SubDepartmentService service = Mockito.mock(SubDepartmentService.class);
		Mockito.doAnswer(this.getFindByRegularExpressionAnswer(page, pageSize)).when(service)
				.findByRegularExpression(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());

		return service;
	}

	/**
	 * Returns a SubDepartment controller to test with.
	 *
	 * @param subDepartmentService The SubDepartmentService for the controller to use.
	 * @return A SubDepartment controller to test with.
	 */
	private SubDepartmentController getSubDepartmentController(SubDepartmentService subDepartmentService) {
		SubDepartmentController subDepartmentController = new SubDepartmentController();
		subDepartmentController.setUserInfo(CommonMocks.getUserInfo());
		subDepartmentController.setSubDepartmentService(subDepartmentService);
		subDepartmentController.setParameterValidator(new NonEmptyParameterValidator());

		return subDepartmentController;
	}
}
