/*
 *  com.heb.pm.productDiscontinue.ProductDiscontinueServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ItemNotDeletedReason;
import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.entity.ProductDiscontinueKey;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import testSupport.CallChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Test class for ProductDiscontinueService.
 *
 * @author d11677
 * @since 2.0.0
 */
public class ProductDiscontinueServiceTest {

	/*
	 * This class really just farms out it's work to other objects. The test class will test that it is farmed out
	 * correctly, not that the other classes work correctly. Those checks will be done in the tests for those classes.
	 */

	private  ProductDiscontinueService productDiscontinueService = new ProductDiscontinueService();

	/*
	 * findAll
	 */

	@Test
	public void findAll() {
		CallChecker checkFindAll = new CallChecker();

		FindAllService findAllService = Mockito.mock(FindAllService.class);
		Mockito.doAnswer(checkFindAll).when(findAllService).findAll(Mockito.anyInt(), Mockito.anyInt());

		this.productDiscontinueService.setFindAllService(findAllService);
		this.productDiscontinueService.findAll(0, 100);
		Assert.assertTrue(checkFindAll.isMethodCalled());
	}


	/*
	 * findByUpcs
	 */

	/**
	 * Tests findByUpcs sorting ascending by UPC ascending.
	 */
	@Test
	public void findByUpcsSortByUpcAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		UpcSearchService upcSearchService = Mockito.mock(UpcSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(upcSearchService).findByUpcs(Mockito.anyList(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setUpcSearchService(upcSearchService);
		this.productDiscontinueService.findByUpcs(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, ProductDiscontinueService.SortDirection.ASC);
	}

	/**
	 * Tests findByUpcs sorting by item code ascending.
	 */
	@Test
	public void findByUpcsSortByItemCodesAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByItemCode(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		UpcSearchService upcSearchService = Mockito.mock(UpcSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(upcSearchService).findByUpcs(Mockito.anyList(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setUpcSearchService(upcSearchService);
		this.productDiscontinueService.findByUpcs(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.ITEM_CODE, ProductDiscontinueService.SortDirection.ASC);
	}

	/**
	 * Tests findByUpcs sorting by discontinue eligible ascending.
	 */
	@Test
	public void findByUpcsSortByEligibleAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinue.getSortByAllColumns(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		UpcSearchService upcSearchService = Mockito.mock(UpcSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(upcSearchService).findByUpcs(Mockito.anyList(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setUpcSearchService(upcSearchService);
		this.productDiscontinueService.findByUpcs(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.DISCONTINUE_ELIGIBLE, ProductDiscontinueService.SortDirection.ASC);
	}

	/**
	 * Tests findByUpcs sorting ascending by UPC descending.
	 */
	@Test
	public void  findByUpcsSortByUpcDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		UpcSearchService upcSearchService = Mockito.mock(UpcSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(upcSearchService).findByUpcs(Mockito.anyList(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setUpcSearchService(upcSearchService);
		this.productDiscontinueService.findByUpcs(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByUpcs sorting by item code descending.
	 */
	@Test
	public void findByUpcsSortByItemCodesDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByItemCode(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		UpcSearchService upcSearchService = Mockito.mock(UpcSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(upcSearchService).findByUpcs(Mockito.anyList(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setUpcSearchService(upcSearchService);
		this.productDiscontinueService.findByUpcs(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.ITEM_CODE, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByUpcs sorting by discontinue eligible descending.
	 */
	@Test
	public void findByUpcsSortByEligibleDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinue.getSortByAllColumns(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		UpcSearchService upcSearchService = Mockito.mock(UpcSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(upcSearchService).findByUpcs(Mockito.anyList(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setUpcSearchService(upcSearchService);
		this.productDiscontinueService.findByUpcs(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.DISCONTINUE_ELIGIBLE, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByUpcs with a null sort column.
	 */
	@Test
	public void findByUpcsNullSortColumn() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getDefaultSort(), pageable.getSort());

			return null;
		};

		UpcSearchService upcSearchService = Mockito.mock(UpcSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(upcSearchService).findByUpcs(Mockito.anyList(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setUpcSearchService(upcSearchService);
		this.productDiscontinueService.findByUpcs(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				null, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByUpcs sorting by upc with null for direction.
	 */
	@Test
	public void findByUpcsNullSortDirection() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		UpcSearchService upcSearchService = Mockito.mock(UpcSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(upcSearchService).findByUpcs(Mockito.anyList(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setUpcSearchService(upcSearchService);
		this.productDiscontinueService.findByUpcs(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, null);
	}


	/*
	 * findByItemCodes
	 */

	/**
	 * Tests findByItemCodes sorting ascending by UPC ascending.
	 */
	@Test
	public void findByItemCodesSortByUpcAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		ItemCodeSearchService itemCodeSearchService = Mockito.mock(ItemCodeSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(itemCodeSearchService).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setItemCodeSearchService(itemCodeSearchService);
		this.productDiscontinueService.findByItemCodes(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, ProductDiscontinueService.SortDirection.ASC);
	}

	/**
	 * Tests findByItemCodes sorting ascending by item code ascending.
	 */
	@Test
	public void findByItemCodesSortByItemAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByItemCode(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		ItemCodeSearchService itemCodeSearchService = Mockito.mock(ItemCodeSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(itemCodeSearchService).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setItemCodeSearchService(itemCodeSearchService);
		this.productDiscontinueService.findByItemCodes(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.ITEM_CODE, ProductDiscontinueService.SortDirection.ASC);
	}
	/**
	 * Tests findByItemCodes sorting by discontinue eligible ascending.
	 */
	@Test
	public void findByItemCodesSortByEligibleAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinue.getSortByAllColumns(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		ItemCodeSearchService itemCodeSearchService = Mockito.mock(ItemCodeSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(itemCodeSearchService).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setItemCodeSearchService(itemCodeSearchService);
		this.productDiscontinueService.findByItemCodes(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.DISCONTINUE_ELIGIBLE, ProductDiscontinueService.SortDirection.ASC);
	}

	/**
	 * Tests findByItemCodes sorting ascending by UPC descending.
	 */
	@Test
	public void findByItemCodesSortByUpcDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		ItemCodeSearchService itemCodeSearchService = Mockito.mock(ItemCodeSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(itemCodeSearchService).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setItemCodeSearchService(itemCodeSearchService);
		this.productDiscontinueService.findByItemCodes(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByItemCodes sorting ascending by item code descending.
	 */
	@Test
	public void findByItemCodesSortByItemDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByItemCode(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		ItemCodeSearchService itemCodeSearchService = Mockito.mock(ItemCodeSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(itemCodeSearchService).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setItemCodeSearchService(itemCodeSearchService);
		this.productDiscontinueService.findByItemCodes(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.ITEM_CODE, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByItemCodes sorting by discontinue eligible descending.
	 */
	@Test
	public void findByItemCodesSortByEligibleDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinue.getSortByAllColumns(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		ItemCodeSearchService itemCodeSearchService = Mockito.mock(ItemCodeSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(itemCodeSearchService).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setItemCodeSearchService(itemCodeSearchService);
		this.productDiscontinueService.findByItemCodes(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.DISCONTINUE_ELIGIBLE,
				ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByItemCodes with a null sort column.
	 */
	@Test
	public void findByItemCodesNullSortColumn() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getDefaultSort(), pageable.getSort());

			return null;
		};

		ItemCodeSearchService itemCodeSearchService = Mockito.mock(ItemCodeSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(itemCodeSearchService).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setItemCodeSearchService(itemCodeSearchService);
		this.productDiscontinueService.findByItemCodes(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				null, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByItemCodes sorting by upc with null for direction.
	 */
	@Test
	public void findByItemCodesNullSortDirection() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		ItemCodeSearchService itemCodeSearchService = Mockito.mock(ItemCodeSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(itemCodeSearchService).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setItemCodeSearchService(itemCodeSearchService);
		this.productDiscontinueService.findByItemCodes(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, null);
	}

	/*
	 * findByProductIds
	 */

	/**
	 * Tests findByProductIds sorting ascending by UPC ascending.
	 */
	@Test
	public void findByProductIdsSortByUpcAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		ProductIdSearchService productIdSearchService = Mockito.mock(ProductIdSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(productIdSearchService).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setProductIdSearchService(productIdSearchService);
		this.productDiscontinueService.findByProductIds(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, ProductDiscontinueService.SortDirection.ASC);
	}

	/**
	 * Tests findByProductIds sorting ascending by item code ascending.
	 */
	@Test
	public void findByProductIdsSortByItemAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByItemCode(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		ProductIdSearchService productIdSearchService = Mockito.mock(ProductIdSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(productIdSearchService).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setProductIdSearchService(productIdSearchService);
		this.productDiscontinueService.findByProductIds(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.ITEM_CODE, ProductDiscontinueService.SortDirection.ASC);
	}

	/**
	 * Tests findByProductIds sorting by discontinue eligible ascending.
	 */
	@Test
	public void findByProductIdsSortByEligibleAscending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinue.getSortByAllColumns(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		ProductIdSearchService productIdSearchService = Mockito.mock(ProductIdSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(productIdSearchService).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setProductIdSearchService(productIdSearchService);
		this.productDiscontinueService.findByProductIds(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.DISCONTINUE_ELIGIBLE, ProductDiscontinueService.SortDirection.ASC);
	}

	/**
	 * Tests findByProductIds sorting ascending by UPC descending.
	 */
	@Test
	public void findByProductIdsSortByUpcDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		ProductIdSearchService productIdSearchService = Mockito.mock(ProductIdSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(productIdSearchService).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setProductIdSearchService(productIdSearchService);
		this.productDiscontinueService.findByProductIds(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByProductIds sorting ascending by item code descending.
	 */
	@Test
	public void findByProductIdsSortByItemDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByItemCode(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		ProductIdSearchService productIdSearchService = Mockito.mock(ProductIdSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(productIdSearchService).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setProductIdSearchService(productIdSearchService);
		this.productDiscontinueService.findByProductIds(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.ITEM_CODE, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByProductIds sorting by discontinue eligible descending.
	 */
	@Test
	public void findByProductIdsSortByEligibleDescending() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinue.getSortByAllColumns(Sort.Direction.DESC), pageable.getSort());

			return null;
		};

		ProductIdSearchService productIdSearchService = Mockito.mock(ProductIdSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(productIdSearchService).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setProductIdSearchService(productIdSearchService);
		this.productDiscontinueService.findByProductIds(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.DISCONTINUE_ELIGIBLE,
				ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByProductIds with a null sort column.
	 */
	@Test
	public void findByProductIdsNullSortColumn() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getDefaultSort(), pageable.getSort());

			return null;
		};

		ProductIdSearchService productIdSearchService = Mockito.mock(ProductIdSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(productIdSearchService).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setProductIdSearchService(productIdSearchService);
		this.productDiscontinueService.findByProductIds(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				null, ProductDiscontinueService.SortDirection.DESC);
	}

	/**
	 * Tests findByProductIds sorting by upc with null for direction.
	 */
	@Test
	public void findByProductIdsNullSortDirection() {
		Answer<PageableResult<ProductDiscontinue>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(0, pageable.getPageNumber());
			Assert.assertEquals(20, pageable.getPageSize());
			Assert.assertEquals(ProductDiscontinueKey.getSortByUpc(Sort.Direction.ASC), pageable.getSort());

			return null;
		};

		ProductIdSearchService productIdSearchService = Mockito.mock(ProductIdSearchService.class);
		Mockito.doAnswer(checkSearchCall).when(productIdSearchService).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean());

		this.productDiscontinueService.setProductIdSearchService(productIdSearchService);
		this.productDiscontinueService.findByProductIds(this.getLongList(), StatusFilter.NONE, true, 0, 20,
				ProductDiscontinueService.SortColumn.UPC, null);
	}

	/**
	 * Tests for loading of item not delete reason properties
	 */
	@Test
	public void getAllItemNotDeleteReasons() {
		Properties properties = Mockito.mock(Properties.class);
		this.productDiscontinueService.setItemNotDelReasons(properties);
		Mockito.when((properties.getProperty("all.codes"))).thenReturn("altp,asdsd");
		Map itemNotDeletedRsnMap = productDiscontinueService.getAllItemNotDeleteReasons();
		Assert.assertFalse(itemNotDeletedRsnMap.isEmpty());
		Assert.assertNotNull(itemNotDeletedRsnMap.get("ALTP"));
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a list of Longs. Since thes functions don't actually search, it doesn't matter what the values are.
	 *
	 * @return A list of item codes to search for.
	 */
	private List<Long> getLongList() {
		List<Long> list = new ArrayList<>();

		list.add(223L);
		list.add(9020507600L);
		list.add(1783150L);
		list.add(1797988L);
		list.add(1783147L);
		list.add(1798109L);
		list.add(4775466379L);
		list.add(9015932505L);
		list.add(9015981063L);
		list.add(9036105270L);

		return list;
	}
}
