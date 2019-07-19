/*
 * ItemCodeSearchServiceTest
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
import testSupport.ValidatingCallChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests ItemCodeSearchServce.
 *
 * @author d116773
 * @since 2.0.1
 */

public class ItemCodeSearchServiceTest {

	private static final long ACTIVE_WAREHOUSE = 223L;
	private static final long ACTIVE_DSD = 9020507600L ;
	private static final long DISCONTINUED_WAREHOUSE = 66L;
	private static final long DISCONTINUED_DSD = 9020508597L;

	private LongPopulator longPopulator = new LongPopulator();
	/*
	 * findByItemCodes
	 */

	/**
	 * Tests findByItemCodes with null passed in as item codes.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByItemCodesNullItemCodes() {

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();
		itemCodeSearchService.findByItemCodes(null, this.getPageable(), StatusFilter.NONE, true);
	}

	/**
	 * Tests findByItemCodes with null passed in to the itemCodeRequest parameter.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findByItemCodesNullPageable() {

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();
		itemCodeSearchService.findByItemCodes(this.getItemCodeList(), null, StatusFilter.NONE, true);
	}

	/**
	 * Tests findByItemCodes looking for with counts.
	 */
	@Test
	public void findByItemCodesActiveWithCounts() {

		List<Long> itemCodes = this.getItemCodeList();
		this.longPopulator.populate(itemCodes, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, itemCodes);

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findActiveByItemCodesWithCount(
				Mockito.anyList(), Mockito.anyObject());
		itemCodeSearchService.setRepositoryWithCount(repository);

		PageableResult<ProductDiscontinue> results =
				itemCodeSearchService.findByItemCodes(this.getItemCodeList(), this.getPageable(),
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
	 * Tests findByItemCodes looking for active items without counts.
	 */
	@Test
	public void findByItemCodesActiveNoCounts() {
		List<Long> itemCodes = this.getItemCodeList();
		this.longPopulator.populate(itemCodes, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, itemCodes);

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();
		ProductDiscontinueRepository repository = Mockito.mock(ProductDiscontinueRepository.class);
		Mockito.doAnswer(callChecker).when(repository).findActiveByItemCodes(
				Mockito.anyList(), Mockito.anyObject());
		itemCodeSearchService.setRepository(repository);

		PageableResult<ProductDiscontinue> results =
				itemCodeSearchService.findByItemCodes(this.getItemCodeList(), this.getPageable(),
						StatusFilter.ACTIVE, false);

		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Tests findByItemCodes looking for discontinued items with counts.
	 */
	@Test
	public void findByItemCodesDiscontinuedWithCounts() {
		List<Long> itemCodes = this.getItemCodeList();
		this.longPopulator.populate(itemCodes, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, itemCodes);

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
		Mockito.doAnswer(callChecker).when(repository).findDiscontinuedByItemCodesWithCount(
				Mockito.anyList(), Mockito.anyObject());
		itemCodeSearchService.setRepositoryWithCount(repository);

		PageableResult<ProductDiscontinue> results =
				itemCodeSearchService.findByItemCodes(this.getItemCodeList(), this.getPageable(),
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
	 * Tests findByItemCodes looking for discontinued items without counts.
	 */
	@Test
	public void findByItemCodesDiscontinuedNoCounts() {
		List<Long> itemCodes = this.getItemCodeList();
		this.longPopulator.populate(itemCodes, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, itemCodes);

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();
		ProductDiscontinueRepository repository = Mockito.mock(ProductDiscontinueRepository.class);
		Mockito.doAnswer(callChecker).when(repository).findDiscontinuedByItemCodes(
				Mockito.anyList(), Mockito.anyObject());
		itemCodeSearchService.setRepository(repository);

		PageableResult<ProductDiscontinue> results =
				itemCodeSearchService.findByItemCodes(this.getItemCodeList(), this.getPageable(),
						StatusFilter.DISCONTINUED, false);

		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Tests findByItemCodes looking for any status with counts.
	 */
	@Test
	public void findByItemCodesAllWithCounts() {
		List<Long> itemCodes = this.getItemCodeList();
		this.longPopulator.populate(itemCodes, 100);

		PageImplReturningCallChecker callChecker = new PageImplReturningCallChecker(0, 100, itemCodes);

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();
		ProductDiscontinueRepositoryWithCount repository = Mockito.mock(ProductDiscontinueRepositoryWithCount.class);
//		Mockito.doAnswer(callChecker).when(repository).findDistinctByKeyItemCodeIn(
//				Mockito.anyList(), Mockito.anyObject());
		itemCodeSearchService.setRepositoryWithCount(repository);

		PageableResult<ProductDiscontinue> results =
				itemCodeSearchService.findByItemCodes(this.getItemCodeList(), this.getPageable(),
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
	 * Tests findByItemCodes looking for any status without counts.
	 */
	@Test
	public void findByItemCodesAllNoCounts() {
		List<Long> itemCodes = this.getItemCodeList();
		this.longPopulator.populate(itemCodes, 100);

		ListReturningCallChecker callChecker = new ListReturningCallChecker(0, 100, itemCodes);

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();
		ProductDiscontinueRepository productDiscontinueRepository = Mockito.mock(ProductDiscontinueRepository.class);
//		Mockito.doAnswer(callChecker).when(productDiscontinueRepository).findDistinctByKeyItemCodeIn(
//				Mockito.anyList(), Mockito.anyObject());
		itemCodeSearchService.setRepository(productDiscontinueRepository);

		PageableResult<ProductDiscontinue> results =
				itemCodeSearchService.findByItemCodes(this.getItemCodeList(), this.getPageable(),
						StatusFilter.NONE, false);

		// A lot of the testing is done in the call checker.

		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertFalse(results.isComplete());
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(0, results.getPage());
	}

	/**
	 * Find all valid Items Codes for an given set of item codes input
	 */
	@Test
	public void findAllItemCodes() {

		List<Long> itemCodes = this.getItemCodeList();
		List<ProductDiscontinue> productDiscontinueList = new ArrayList<>();
		this.longPopulator.populate(itemCodes, 100);

		ItemCodeSearchService itemCodeSearchService = new ItemCodeSearchService();

		com.heb.pm.repository.ProductDiscontinueRepository repository = Mockito.mock(com.heb.pm.repository.ProductDiscontinueRepository.class);
		ValidatingCallChecker<List<Long>, List<ProductDiscontinue>> callChecker = new ValidatingCallChecker<>(itemCodes, productDiscontinueList);
		Mockito.doAnswer(callChecker).when(repository).findAllDistinctByKeyItemCodeIn(Mockito.anyList());
		itemCodeSearchService.setRepository(repository);

		List<Long> results = itemCodeSearchService.findAllItemCodes(this.getItemCodeList());

		// A lot of the testing is done in the call checker.

		Assert.assertNotNull(itemCodes);
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
	 * Returns a list of item codes (includes DSD UPCs) to test with.
	 *
	 * @return A list of item codes to test with.
	 */
	private List<Long> getItemCodeList() {

		List<Long> itemCodes = new ArrayList<>();

		itemCodes.add(ItemCodeSearchServiceTest.ACTIVE_WAREHOUSE);
		itemCodes.add(ItemCodeSearchServiceTest.ACTIVE_DSD);
		itemCodes.add(ItemCodeSearchServiceTest.DISCONTINUED_DSD);
		itemCodes.add(ItemCodeSearchServiceTest.DISCONTINUED_WAREHOUSE);

		return itemCodes;
	}
}
