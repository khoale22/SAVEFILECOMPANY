/*
 * UpcSearchServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.entity.ProductDiscontinueKey;
import com.heb.pm.repository.ProductDiscontinueRepository;
import com.heb.pm.repository.ProductDiscontinueRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.LongPopulator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import testSupport.ValidatingCallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests UpcSerachService.
 *
 * @author d116773
 * @since 2.0.1
 */

public class UpcSearchServiceTest {

	private static final long ACTIVE_DSD = 9020507600L;
	private static final long DISCONTINUE_DSD = 9020508597L;
	private static final long ACTIVE_WAREHOUSE = 9015932326L;
	private static final long DISCONTINUED_WAREHOUSE = 9015932361L;

	private LongPopulator longPopulator = new LongPopulator();

	/*
	 * findByUpcs
	 */

	/**
	 * Tests findByUpcs with null passed in as upcs.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByUpcsNullUpcs() {
		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.findByUpcs(null, this.getPageable(), StatusFilter.NONE, true);
	}

	/**
	 * Tests findByUpcs with null passed in to the itemCodeRequest parameter.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByUpcsNullPageable() {

		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.findByUpcs(this.getUpcList(), null, StatusFilter.NONE, true);
	}

	/**
	 * Tests findByUpcs looking for active items with counts.
	 */
	@Test
	public void findByUpcsActiveWithCounts() {

		List<Long> upcs = this.getUpcList();
		this.longPopulator.populate(upcs, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, upcs);
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findActiveByUpcsWithCount(
				Mockito.anyList(), Mockito.anyObject());

		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.setRepositoryWithCount(repository);

		PageableResult<ProductDiscontinue> results =
				upcSearchService.findByUpcs(this.getUpcList(), this.getPageable(),
						StatusFilter.ACTIVE, true);
		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertTrue(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(0, results.getPage());
		Assert.assertEquals(Long.valueOf(1), results.getRecordCount());
	}

	/**
	 * Tests findByUpcs looking for active items without counts.
	 */
	@Test
	public void findByUpcsActiveNoCounts() {
		List<Long> upcs = this.getUpcList();
		this.longPopulator.populate(upcs, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, upcs);
		ProductDiscontinueRepository repository = Mockito.mock(ProductDiscontinueRepository.class);
		Mockito.doAnswer(callChecker).when(repository).findActiveByUpcs(
				Mockito.anyList(), Mockito.anyObject());

		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.setRepository(repository);

		PageableResult<ProductDiscontinue> results =
				upcSearchService.findByUpcs(this.getUpcList(), this.getPageable(),
						StatusFilter.ACTIVE, false);
		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Tests findByUpcs looking for discontinued items with counts.
	 */
	@Test
	public void findByUpcsDiscontinuedWithCounts() {
		List<Long> upcs = this.getUpcList();
		this.longPopulator.populate(upcs, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, upcs);
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findDiscontinuedByUpcsWithCount(
				Mockito.anyList(), Mockito.anyObject());

		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.setRepositoryWithCount(repository);

		PageableResult<ProductDiscontinue> results =
				upcSearchService.findByUpcs(this.getUpcList(), this.getPageable(),
						StatusFilter.DISCONTINUED, true);
		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertTrue(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(0, results.getPage());
		Assert.assertEquals(Long.valueOf(1), results.getRecordCount());
	}

	/**
	 * Tests findByUpcs looking for discontinued items without counts.
	 */
	@Test
	public void findByUpcsDiscontinuedNoCounts() {
		List<Long> upcs = this.getUpcList();
		this.longPopulator.populate(upcs, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, upcs);
		ProductDiscontinueRepository repository = Mockito.mock(ProductDiscontinueRepository.class);
		Mockito.doAnswer(callChecker).when(repository).findDiscontinuedByUpcs(
				Mockito.anyList(), Mockito.anyObject());

		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.setRepository(repository);

		PageableResult<ProductDiscontinue> results =
				upcSearchService.findByUpcs(this.getUpcList(), this.getPageable(),
						StatusFilter.DISCONTINUED, false);
		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Tests findByUpcs looking for any status with counts.
	 */
	@Test
	@Transactional
	public void findByUpcsAllWithCounts() {
		List<Long> upcs = this.getUpcList();
		this.longPopulator.populate(upcs, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, upcs);
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findDistinctByKeyUpcIn(
				Mockito.anyList(), Mockito.anyObject());

		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.setRepositoryWithCount(repository);

		PageableResult<ProductDiscontinue> results =
				upcSearchService.findByUpcs(this.getUpcList(), this.getPageable(),
						StatusFilter.NONE, true);
		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertTrue(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(0, results.getPage());
		Assert.assertEquals(Long.valueOf(1), results.getRecordCount());
	}

	/**
	 * Tests findByUpcs looking for any status without counts.
	 */
	@Test
	public void findByUpcsAllNoCounts() {
		List<Long> upcs = this.getUpcList();
		this.longPopulator.populate(upcs, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, upcs);
		com.heb.pm.repository.ProductDiscontinueRepository repository = Mockito.mock(com.heb.pm.repository.ProductDiscontinueRepository.class);
		Mockito.doAnswer(callChecker).when(repository).findDistinctByKeyUpcIn(
				Mockito.anyList(), Mockito.anyObject());

		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.setRepository(repository);

		PageableResult<ProductDiscontinue> results =
				upcSearchService.findByUpcs(this.getUpcList(), this.getPageable(),
						StatusFilter.NONE, false);
		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Find all valid UPCs for a given set of UPCs input
	 */
	@Test
	public void findAllUPCs() {

		List<Long> upcs = this.getUpcList();
		List<ProductDiscontinue> productDiscontinueList = new ArrayList<>();
		this.longPopulator.populate(upcs, 100);

		com.heb.pm.repository.ProductDiscontinueRepository repository = Mockito.mock(com.heb.pm.repository.ProductDiscontinueRepository.class);
		ValidatingCallChecker<List<Long>, List<ProductDiscontinue>> callChecker = new ValidatingCallChecker<>(upcs, productDiscontinueList);
		Mockito.doAnswer(callChecker).when(repository).findAllDistinctByKeyUpcIn(Mockito.anyList());

		UpcSearchService upcSearchService = new UpcSearchService();
		upcSearchService.setRepository(repository);

		List<Long> results = upcSearchService.findAllUPCs(this.getUpcList());

		// A lot of the testing is done in the call checker.

		Assert.assertNotNull(upcs);
		Assert.assertNotNull(results);
		Assert.assertTrue(callChecker.isMethodCalled());
	}


	/*
	 * Support functions.
	 */

	/**
	 * Returns an object that has the page to get, it's size, and how to sort.
	 *
	 * @return An object that has the page to get, it's size, and how to sort.
	 */
	private Pageable getPageable() {
		return new PageRequest(0, 100, ProductDiscontinueKey.getDefaultSort());
	}

	/**
	 * Returns a list of UPCs to look for.
	 *
	 * @return A list of UPCs to look for.
	 */
	private List<Long> getUpcList() {

		List<Long> upcs = new ArrayList<>();

		upcs.add(UpcSearchServiceTest.ACTIVE_DSD);
		upcs.add(UpcSearchServiceTest.ACTIVE_WAREHOUSE);
		upcs.add(UpcSearchServiceTest.DISCONTINUE_DSD);
		upcs.add(UpcSearchServiceTest.DISCONTINUED_WAREHOUSE);

		return upcs;
	}
}
