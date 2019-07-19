/*
 * IngredientsReportServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.reports;

import com.heb.pm.entity.DynamicAttribute;

import com.heb.pm.repository.DynamicAttributeRepository;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests IngredientsReportService.
 *
 * @author d116773
 * @since 2.0.7
 */
public class IngredientsReportServiceTest {


	// getIngredientsReport

	/**
	 * Tests getIngredientsReport when the caller asks for record counts.
	 */
	@Test
	public void getIngredientsReport_withCounts() {
		DynamicAttributeRepository repo = Mockito.mock(DynamicAttributeRepository.class);
		WithCountsAnswer answer = new WithCountsAnswer(1643, "TEST STRING");
		Mockito.doAnswer(answer).when(repo).findByTextAttributeRegularExpressionWithCounts(Mockito.anyInt(),
				Mockito.anyString(), Mockito.anyInt(), Mockito.anyObject());

		IngredientsReportService ingredientsReportService = new IngredientsReportService();
		ingredientsReportService.setIngredientsRepository(repo);
		PageableResult<DynamicAttribute> result =
				ingredientsReportService.getIngredientsReport("test string", true, 1, 100);

		Assert.assertTrue(result.isComplete());
		Assert.assertEquals(100, result.getRecordCount().intValue());
		Assert.assertEquals(1, result.getPage());
		Assert.assertTrue(answer.isMethodCalled());
	}

	/**
	 * Tests getIngredientsReport when the caller asks for no record counts.
	 */
	@Test
	public void getIngredientsReport_withoutCounts() {
		DynamicAttributeRepository repo = Mockito.mock(DynamicAttributeRepository.class);
		WithoutCountsAnswer answer = new WithoutCountsAnswer(1643, "TEST STRING");
		Mockito.doAnswer(answer).when(repo).findByTextAttributeRegularExpression(Mockito.anyInt(),
				Mockito.anyString(), Mockito.anyInt(), Mockito.anyObject());

		IngredientsReportService ingredientsReportService = new IngredientsReportService();
		ingredientsReportService.setIngredientsRepository(repo);
		PageableResult<DynamicAttribute> result =
				ingredientsReportService.getIngredientsReport("test string", false, 1, 100);

		Assert.assertFalse(result.isComplete());
		Assert.assertTrue(answer.isMethodCalled());
	}
	// Support functions.

	/**
	 * An answer that will help test when the call is made to get the counts.
	 */
	private class WithCountsAnswer implements Answer<Page<DynamicAttribute>> {

		private boolean methodCalled;
		private String searchString;
		private int attributeId;

		/**
		 * Constructs a new WithCountsAnswer.
		 *
		 * @param attributeId The attribute ID that will be passed to the function.
		 * @param searchString The search string that will be passed to the function.
		 */
		public WithCountsAnswer(int attributeId, String searchString) {
			this.attributeId = attributeId;
			this.searchString = searchString;
			this.methodCalled = false;
		}

		/**
		 * Returns true if this method is called and false otherwise.
		 *
		 * @return True if the method is called.
		 */
		public boolean isMethodCalled() {
			return methodCalled;
		}

		/**
		 * Mocks up the  call to the repository. Will check that some of the paramaters are correctly passed.
		 *
		 * @param invocation The mocked call to the function.
		 * @return A fake page of data.
		 * @throws Throwable Any error.
		 */
		@Override
		public Page<DynamicAttribute> answer(InvocationOnMock invocation) throws Throwable {

			Integer attributeId = (Integer)invocation.getArguments()[0];
			Assert.assertEquals(this.attributeId, attributeId.intValue());

			String searchString = (String)invocation.getArguments()[1];
			Assert.assertEquals(this.searchString, searchString);

			Pageable p = (Pageable)invocation.getArguments()[2];
			PageImpl<DynamicAttribute> pageImpl = new PageImpl<>(new ArrayList<>(), p, 100);

			this.methodCalled = true;
			return pageImpl;
		}
	}

	private class WithoutCountsAnswer implements Answer<List<DynamicAttribute>> {

		private boolean methodCalled;
		private String searchString;
		private int attributeId;

		public WithoutCountsAnswer(int attributeId, String searchString) {
			this.attributeId = attributeId;
			this.searchString = searchString;
			this.methodCalled = false;
		}

		public boolean isMethodCalled() {
			return methodCalled;
		}

		@Override
		public List<DynamicAttribute> answer(InvocationOnMock invocation) throws Throwable {

			Integer attributeId = (Integer)invocation.getArguments()[0];
			Assert.assertEquals(this.attributeId, attributeId.intValue());

			String searchString = (String)invocation.getArguments()[1];
			Assert.assertEquals(this.searchString, searchString);

			this.methodCalled = true;
			return new ArrayList<>();
		}
	}
}
