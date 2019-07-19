/*
 * ClassCommodityControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ItemClass;
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
 * Tests ClassCommodityController.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ClassCommodityControllerTest {

	private static final String SEARCH_STRING = "TEST SEARCH STRING";
	private static final int CLASS_ID = 2334;
	private static final int COMMODITY_ID = 23423;
	private static final String DESCRIPTION = "test class";

	/**
	 * Tests findCommoditiesByRegularExpression with all the values set.
	 */
	@Test
	public void findCommoditiesByRegularExpressionAllValues() {
		ClassCommodityService service = this.getClassCommodityService(5, 100);

		ClassCommodityController controller = this.getClassCommodityController(service);
		controller.findCommoditiesByRegularExpression(ClassCommodityControllerTest.SEARCH_STRING, 5, 100,
				CommonMocks.getServletRequest());
		// All the tests re done in the answer.
	}

	/**
	 * Tests findCommoditiesByRegularExpression with a null search string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findCommoditiesByRegularExpressionNullSearchString() {
		ClassCommodityService service = this.getClassCommodityService(0, 100);

		ClassCommodityController controller = this.getClassCommodityController(service);
		controller.findCommoditiesByRegularExpression(null, 0, 100,
				CommonMocks.getServletRequest());
	}

	/**
	 * Tests findCommoditiesByRegularExpression with null page an pageSize.
	 */
	@Test
	public void findCommoditiesByRegularExpressionNullPageAndSize() {
		ClassCommodityService service = this.getClassCommodityService(0, 15);

		ClassCommodityController controller = this.getClassCommodityController(service);
		controller.findCommoditiesByRegularExpression(ClassCommodityControllerTest.SEARCH_STRING, null, null,
				CommonMocks.getServletRequest());
		// All the tests re done in the answer.
	}

	/*
	 * support functions.
	 */

	/**
	 * Returns a ClassCommodityService to test with.
	 *
	 * @param page The page value for the class to expect.
	 * @param pageSize The pageSize value for the class to expect.
	 * @return A ClassCommodityService to test with.
	 */
	private ClassCommodityService getClassCommodityService(int page, int pageSize) {

		ClassCommodityService classCommodityService = Mockito.mock(ClassCommodityService.class);
		Mockito.doAnswer(this.getFindByRegularExpressionAnswer(page, pageSize)).when(classCommodityService)
				.findCommoditiesByRegularExpression(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
		return classCommodityService;
	}

	/**
	 * Returns a ClassCommodityController to test with.
	 *
	 * @param service The ClassCommodityService for the controller to use.
	 * @return A ClassCommodityController to test with.
	 */
	private ClassCommodityController getClassCommodityController(ClassCommodityService service) {
		ClassCommodityController controller = new ClassCommodityController();

		controller.setClassCommodityService(service);
		controller.setParameterValidator(new NonEmptyParameterValidator());
		controller.setUserInfo(CommonMocks.getUserInfo());
		return controller;
	}

	/**
	 * Returns an answer for the findByRegularExpressions calls. It checks to make sure the right parameters are
	 * passed in.
	 *
	 * @param page The page value for the class to expect.
	 * @param pageSize The pageSize value for the class to expect.
	 * @return An answer for the findByRegularExpressions calls.
	 */
	private Answer<PageableResult<ItemClass>> getFindByRegularExpressionAnswer(int page, int pageSize) {

		return invocation -> {
			Assert.assertEquals(SEARCH_STRING, invocation.getArguments()[0]);
			Assert.assertEquals(page, invocation.getArguments()[1]);
			Assert.assertEquals(pageSize, invocation.getArguments()[2]);
			return this.getResult();
		};
	}

	/**
	 * Returns a PageableResult for the service functions to return.
	 *
	 * @return A PageableResult for the service functions to return.
	 */
	private  PageableResult<ItemClass> getResult() {
		List<ItemClass> itemClassList = new ArrayList<>();
		itemClassList.add(this.getTestItemClass());

		return new PageableResult<>(0, 1, 1L, itemClassList);
	}

	/**
	 * Creates a ItemClass to test with.
	 *
	 * @return A ItemClass to test with.
	 */
	private ItemClass getTestItemClass() {
		ItemClass cc = new ItemClass();
		cc.setItemClassCode(CLASS_ID);
		cc.setItemClassDescription(DESCRIPTION);
		return cc;
	}
}
