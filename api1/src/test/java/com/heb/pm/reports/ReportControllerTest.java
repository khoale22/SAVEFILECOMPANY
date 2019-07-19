/*
 * ReportControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.reports;

import com.heb.pm.entity.DynamicAttribute;
import com.heb.pm.entity.DynamicAttributeKey;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.jpa.PageableResult;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import testSupport.CommonMocks;

import java.util.ArrayList;

/**
 * Tests ReportController.
 *
 * @author d116773
 * @since 2.0.7
 */
public class ReportControllerTest {

	private static final int ID = 1643;
	private static final long KEY = 61345;
	private static final String KEY_TYPE = "UPC  ";
	private static final int SEQUENCE_NUMBER = 0;
	private static final int SOURCE_SYSTEM = 4;

	private static final String TEXT_VALUE = "100% Arabica Coffee with Natural and Artificial Flavors.";

	/**
	 * Tests findByIngredient when passed null for an ingredient.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByIngredient_nullIngredient() {

		ReportController reportController = new ReportController();
		reportController.setParameterValidator(this.getParameterValidator());

		reportController.findByIngredient(null, false, 0, 0, CommonMocks.getServletRequest());
	}

	@Test
	public void findByIngredient_defaults() {

		ReportController reportController = new ReportController();
		reportController.setParameterValidator(this.getParameterValidator());
		reportController.setIngredientsResolver(this.getIngredientsResolver());
		IngredientsReportAnswer answer = new IngredientsReportAnswer("test", false, 0, 100);
		reportController.setIngredientsReportService(this.getReportService(answer));
		reportController.setUserInfo(CommonMocks.getUserInfo());

		reportController.findByIngredient("test", null, null, null, CommonMocks.getServletRequest());
		Assert.assertTrue(answer.isMethodCalled());
	}

	@Test
	public void findByIngredient_notDefaults() {

		ReportController reportController = new ReportController();
		reportController.setParameterValidator(this.getParameterValidator());
		reportController.setIngredientsResolver(this.getIngredientsResolver());
		IngredientsReportAnswer answer = new IngredientsReportAnswer("test", true, 5, 123);
		reportController.setIngredientsReportService(this.getReportService(answer));
		reportController.setUserInfo(CommonMocks.getUserInfo());

		reportController.findByIngredient("test", true, 5, 123, CommonMocks.getServletRequest());
		Assert.assertTrue(answer.isMethodCalled());
	}


	// Support functions.
//
//	private DynamicAttribute getTestDynamicAttribute() {
//		DynamicAttributeKey key = new DynamicAttributeKey();
//
//		key.setAttributeId(ID);
//		key.setKey(KEY);
//		key.setKeyType(KEY_TYPE);
//		key.setSequenceNumber(SEQUENCE_NUMBER);
//		key.setSourceSystem(SOURCE_SYSTEM);
//
//		DynamicAttribute dynamicAttribute =  new DynamicAttribute();
//
//		dynamicAttribute.setKey(this.getTestKey());
//		dynamicAttribute.setTextValue(TEXT_VALUE);
//		dynamicAttribute.setSellingUnit(this.getTestSellingUnit());
//
//		return dynamicAttribute;
//	}
	private class IngredientsReportAnswer implements Answer<PageableResult<DynamicAttribute>> {

		private boolean methodCalled;
		private String ingredient;
		private boolean includeCounts;
		private int page;
		private int pageSize;

		public IngredientsReportAnswer(String ingredient, boolean includeCounts, int page, int pageSize) {
			this.methodCalled = false;
			this.ingredient = ingredient;
			this.includeCounts = includeCounts;
			this.page = page;
			this.pageSize = pageSize;
		}

		@Override
		public PageableResult<DynamicAttribute> answer(InvocationOnMock invocation) throws Throwable {

			String ingr = (String)invocation.getArguments()[0];
			boolean ic = (Boolean)invocation.getArguments()[1];
			int p = (Integer)invocation.getArguments()[2];
			int ps = (Integer)invocation.getArguments()[3];

			Assert.assertEquals(this.ingredient, ingr);
			Assert.assertEquals(this.includeCounts, ic);
			Assert.assertEquals(this.page, p);
			Assert.assertEquals(this.pageSize, ps);
			this.methodCalled = true;

			return new PageableResult<>(this.page, new ArrayList<>());
		}

		public boolean isMethodCalled() {
			return methodCalled;
		}
	}

	private IngredientsReportService getReportService(IngredientsReportAnswer answer) {

		IngredientsReportService service = Mockito.mock(IngredientsReportService.class);

		Mockito.doAnswer(answer).when(service)
				.getIngredientsReport(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt());

		return service;
	}

	private NonEmptyParameterValidator getParameterValidator() {

		MessageSource messageSource = Mockito.mock(MessageSource.class);
		NonEmptyParameterValidator parameterValidator = new NonEmptyParameterValidator();
		parameterValidator.setMessageSource(messageSource);

		return  parameterValidator;
	}

	private IngredientsResolver getIngredientsResolver() {
		return Mockito.mock(IngredientsResolver.class);
	}

}
