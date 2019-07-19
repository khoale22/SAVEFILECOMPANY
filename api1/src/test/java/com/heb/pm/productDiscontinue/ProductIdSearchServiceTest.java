/*
 * ProductIdSearchServiceTest
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
 * Tests ProductIdSearchService.
 *
 * @author d116773
 * @since 2.0.1
 */
public class ProductIdSearchServiceTest {

	private static final long ACTIVE_WAREHOUSE = 1783150;
	private static final long ACTIVE_DSD = 1797988 ;
	private static final long DISCONTINUED_WAREHOUSE = 1783147;
	private static final long DISCONTINUED_DSD = 1798109;

	private LongPopulator longPopulator = new LongPopulator();

	/*
	 * findByProductIds
	 */

	/**
	 * Tests findByProductIds with null passed in as item codes.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByProductIdsNullProductIds() {

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.findByProductIds(null, this.getPageable(), StatusFilter.NONE, true);
	}

	/**
	 * Tests findByProductIds with null passed in to the itemCodeRequest parameter.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByProductIdsNullPageable() {

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.findByProductIds(this.getProductIds(), null, StatusFilter.NONE, true);
	}

	/**
	 * Tests findByProductIds looking for active items with counts.
	 */
	@Test
	public void findByProductIdsActiveWithCounts() {

		List<Long> productIds = this.getProductIds();
		this.longPopulator.populate(productIds, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, productIds);
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findActiveByProductIdsWithCount(
				Mockito.anyList(), Mockito.anyObject());

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.setRepositoryWithCount(repository);


		PageableResult<ProductDiscontinue> results =
				productIdSearchService.findByProductIds(this.getProductIds(), this.getPageable(),
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
	 * Tests findByProductIds looking for active items without counts.
	 */
	@Test
	public void findByProductIdsActiveNoCounts() {
		List<Long> productIds = this.getProductIds();
		this.longPopulator.populate(productIds, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, productIds);
		ProductDiscontinueRepository repository = Mockito.mock(ProductDiscontinueRepository.class);
		Mockito.doAnswer(callChecker).when(repository).findActiveByProductIds(
				Mockito.anyList(), Mockito.anyObject());

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.setRepository(repository);


		PageableResult<ProductDiscontinue> results =
				productIdSearchService.findByProductIds(this.getProductIds(), this.getPageable(),
						StatusFilter.ACTIVE, false);

		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Tests findByProductIds looking for discontinued items with counts.
	 */
	@Test
	public void findByProductIdsDiscontinuedWithCounts() {
		List<Long> productIds = this.getProductIds();
		this.longPopulator.populate(productIds, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, productIds);
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findDiscontinuedByProductIdsWithCount(
				Mockito.anyList(), Mockito.anyObject());

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.setRepositoryWithCount(repository);


		PageableResult<ProductDiscontinue> results =
				productIdSearchService.findByProductIds(this.getProductIds(), this.getPageable(),
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
	 * Tests findByProductIds looking for discontinued items without counts.
	 */
	@Test
	public void findByProductIdsDiscontinuedNoCounts() {
		List<Long> productIds = this.getProductIds();
		this.longPopulator.populate(productIds, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, productIds);
		ProductDiscontinueRepository repository = Mockito.mock(ProductDiscontinueRepository.class);
		Mockito.doAnswer(callChecker).when(repository).findDiscontinuedByProductIds(
				Mockito.anyList(), Mockito.anyObject());

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.setRepository(repository);


		PageableResult<ProductDiscontinue> results =
				productIdSearchService.findByProductIds(this.getProductIds(), this.getPageable(),
						StatusFilter.DISCONTINUED, false);

		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Tests findByProductIds looking for any status with counts.
	 */
	@Test
	@Transactional
	public void findByProductIdsAllWithCounts() {
		List<Long> productIds = this.getProductIds();
		this.longPopulator.populate(productIds, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, productIds);
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findDistinctByKeyProductIdIn(
				Mockito.anyList(), Mockito.anyObject());

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.setRepositoryWithCount(repository);


		PageableResult<ProductDiscontinue> results =
				productIdSearchService.findByProductIds(this.getProductIds(), this.getPageable(),
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
	 * Tests findByProductIds looking for any status without counts.
	 */
	@Test
	@Transactional
	public void findByProductIdsAllNoCounts() {
		List<Long> productIds = this.getProductIds();
		this.longPopulator.populate(productIds, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, productIds);
		com.heb.pm.repository.ProductDiscontinueRepository repository = Mockito.mock(com.heb.pm.repository.ProductDiscontinueRepository.class);
		Mockito.doAnswer(callChecker).when(repository).findDistinctByKeyProductIdIn(
				Mockito.anyList(), Mockito.anyObject());

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.setRepository(repository);


		PageableResult<ProductDiscontinue> results =
				productIdSearchService.findByProductIds(this.getProductIds(), this.getPageable(),
						StatusFilter.NONE, false);

		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Find all valid products for a given set of product ids input
	 */
	@Test
	public void findAllProducts() {
		List<Long> productIds = this.getProductIds();
		List<ProductDiscontinue> productDiscontinueList = new ArrayList<>();
		this.longPopulator.populate(productIds, 100);

		com.heb.pm.repository.ProductDiscontinueRepository repository = Mockito.mock(com.heb.pm.repository.ProductDiscontinueRepository.class);
		ValidatingCallChecker<List<Long>, List<ProductDiscontinue>> callChecker = new ValidatingCallChecker<>(productIds, productDiscontinueList);
		Mockito.doAnswer(callChecker).when(repository).findAllDistinctByKeyProductIdIn(Mockito.anyList());

		ProductIdSearchService productIdSearchService = new ProductIdSearchService();
		productIdSearchService.setRepository(repository);

		List<Long> results = productIdSearchService.findAllProducts(this.getProductIds());

		// A lot of the testing is done in the call checker.

		Assert.assertNotNull(productIds);
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
	 * Returns a list of product IDs  (includes DSD UPCs) to test with.
	 *
	 * @return A list of product IDs to test with.
	 */
	private List<Long> getProductIds() {

		List<Long> productIds = new ArrayList<>();

		productIds.add(ProductIdSearchServiceTest.ACTIVE_WAREHOUSE);
		productIds.add(ProductIdSearchServiceTest.ACTIVE_DSD);
		productIds.add(ProductIdSearchServiceTest.DISCONTINUED_DSD);
		productIds.add(ProductIdSearchServiceTest.DISCONTINUED_WAREHOUSE);

		return productIds;
	}
}
