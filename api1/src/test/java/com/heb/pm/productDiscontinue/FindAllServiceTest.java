/*
 * FindAllServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.repository.ProductDiscontinueRepositoryWithCount;
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
 * Tests FindAllService.
 *
 * @author d116773
 * @since 2.0.1
 */
public class FindAllServiceTest {

	/*
	 * findAll
	 */

	/**
	 * Tests findAll.
	 */
	@Test
	public void findAllWithAllParameters() {

		PageRequestVerifyingCallChecker callChecker = new PageRequestVerifyingCallChecker(0, 100, this.getResultList());
		FindAllService findAllService = new FindAllService();
		findAllService.setRepository(this.getRepository(callChecker));

		PageableResult<ProductDiscontinue> results = findAllService.findAll(0, 100);

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertTrue(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(0, results.getPage());
		Assert.assertEquals(Long.valueOf(1), results.getRecordCount());
	}


	/*
	 * Support functions
	 */
	private class PageRequestVerifyingCallChecker implements Answer<Page<ProductDiscontinue>> {

		private boolean methodCalled;
		private int page;
		private int pageSize;
		private List<ProductDiscontinue> toReturn;

		public PageRequestVerifyingCallChecker(int page, int pageSize, List<ProductDiscontinue> toReturn) {
			this.methodCalled = false;
			this.page = page;
			this.pageSize = pageSize;
			this.toReturn = toReturn;
		}

		public boolean isMethodCalled() {
			return this.methodCalled;
		}

		@Override
		public Page<ProductDiscontinue> answer(InvocationOnMock invocation) throws Throwable {
			this.methodCalled = true;
			Pageable request = (Pageable)invocation.getArguments()[0];
			Assert.assertEquals(this.page, request.getPageNumber());
			Assert.assertEquals(this.pageSize, request.getPageSize());
			return new PageImpl<>(this.toReturn, null, 1);
		}
	}

	private List<ProductDiscontinue> getResultList() {

		List<ProductDiscontinue> list = new ArrayList<>();
		list.add(new ProductDiscontinue());
		return list;
	}

	private ProductDiscontinueRepositoryWithCount getRepository(PageRequestVerifyingCallChecker callChecker) {

		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findAll((Pageable)Mockito.anyObject());
		return repository;
	}
}
