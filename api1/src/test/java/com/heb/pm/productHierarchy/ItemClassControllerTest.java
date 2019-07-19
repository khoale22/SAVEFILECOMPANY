/*
 * ItemClassControllerTest
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for item class controller.
 *
 * @author l730832
 * @since 2.6.0
 */
public class ItemClassControllerTest {

	private static final String SEARCH_STRING = "TEST SEARCH STRING";
	private static final int CLASS_ID = 2334;
	private static final String DESCRIPTION = "test class";

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests findClassesByRegularExpression with all the values set.
	 */
	@Test
	public void findClassesByRegularExpressionAllValues() {
		ItemClassService service = this.getItemClassService(5, 100);

		ItemClassController controller = this.getItemClassController(service);
		controller.findClassesByRegularExpression(SEARCH_STRING, 5, 100,
				CommonMocks.getServletRequest());
		// All the tests re done in the answer.
	}

	/**
	 * Tests findClassesByRegularExpression with a null search string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findClassesByRegularExpressionNullSearchString() {
		ItemClassService service = this.getItemClassService(0, 100);

		ItemClassController controller = this.getItemClassController(service);
		controller.findClassesByRegularExpression(null, 0, 100,
				CommonMocks.getServletRequest());
	}

	/**
	 * Tests findClassesByRegularExpression with null page an pageSize.
	 */
	@Test
	public void findClassesByRegularExpressionNullPageAndSize() {
		ItemClassService service = this.getItemClassService(0, 15);

		ItemClassController controller = this.getItemClassController(service);
		controller.findClassesByRegularExpression(SEARCH_STRING, null, null,
				CommonMocks.getServletRequest());
		// All the tests re done in the answer.
	}

	/**
	 * Returns a ItemClassController to test with.
	 *
	 * @param service The ItemClassService for the controller to use.
	 * @return A ItemClassController to test with.
	 */
	private ItemClassController getItemClassController(ItemClassService service) {
		ItemClassController controller = new ItemClassController();

		controller.setItemClassService(service);
		controller.setParameterValidator(new NonEmptyParameterValidator());
		controller.setUserInfo(CommonMocks.getUserInfo());
		return controller;
	}

	/**
	 * Returns a ItemClassService to test with.
	 *
	 * @param page The page value for the class to expect.
	 * @param pageSize The pageSize value for the class to expect.
	 * @return A ItemClassService to test with.
	 */
	private ItemClassService getItemClassService(int page, int pageSize) {

		ItemClassService itemClassService = Mockito.mock(ItemClassService.class);
		Mockito.doAnswer(this.getFindByRegularExpressionAnswer(page, pageSize)).when(itemClassService).findItemClassesByRegularExpression(
				Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
		return itemClassService;
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