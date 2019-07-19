/*
 *  com.heb.pm.productDiscontinue.ProductDiscontinueControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.Hits;
import com.heb.pm.entity.ItemNotDeletedReason;
import com.heb.pm.entity.ProductDiscontinue;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import testSupport.CommonMocks;
import testSupport.ValidatingCallChecker;

import java.util.*;


/**
 * Test class for ProductDiscontinueController.
 *
 * @author d11677
 * @since 2.0.0
 */
public class ProductDiscontinueControllerTest {

	/*
	 * The general idea behind this is that the controller just sets some reasonable defaults if nulls are passed in
	 * or it'll throw an exception. This test does not look much at the data that are returned as those tests are
	 * expected to be in the the service test. These tests will basically make sure the functions run when nulls are passed
	 * in when it should and throw an error when it shouldn't.
	 */

	private static final int PAGE = 6;
	private static final int PAGE_SIZE = 105;
	private static final long ITEM_CODE = 622L;
	private static final long UPC = 4775426679L;
	private Map<String, ItemNotDeletedReason> ALL_DEL_REASONS;

	@InjectMocks
	ProductDiscontinueController productDiscontinueController;

	@Mock
	ProductDiscontinueService productDiscontinueService;

	@Mock
	UserInfo userInfo;

	@Mock
	Properties itemNotDelReasons;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		productDiscontinueService = new ProductDiscontinueService();
		itemNotDelReasons = new Properties();
		ALL_DEL_REASONS = new HashMap<>();
	}

	/*
	 * findAll
	 */

	/**
	 * Tests findAll with all parameters set.
	 */
	@Test
	public void findAllWithAllParameters() {

		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAllAnswer(ProductDiscontinueControllerTest.PAGE,
						ProductDiscontinueControllerTest.PAGE_SIZE))
				.when(service).findAll(Mockito.anyInt(), Mockito.anyInt());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findAll(ProductDiscontinueControllerTest.PAGE,
					ProductDiscontinueControllerTest.PAGE_SIZE, CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data  are null", results.getData());
	}

	/**
	 * Tests findAll with null for page.
	 */
	@Test
	public void findAllWithNullPage() {
		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAllAnswer(0, ProductDiscontinueControllerTest.PAGE_SIZE))
				.when(service).findAll(Mockito.anyInt(), Mockito.anyInt());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findAll(null,
						ProductDiscontinueControllerTest.PAGE_SIZE, CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data  are null", results.getData());
	}

	/**
	 * Tests findAll with a null page size.
	 */
	@Test
	public void findAllWithNullPageSize() {
		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAllAnswer(ProductDiscontinueControllerTest.PAGE, 100))
				.when(service).findAll(Mockito.anyInt(), Mockito.anyInt());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findAll(ProductDiscontinueControllerTest.PAGE,
						null, CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data  are null", results.getData());
	}

	/**
	 * Mocks up the call to findAll on the service.
	 */
	private Answer<PageableResult<ProductDiscontinue>> getFindAllAnswer(int page, int pageSize) {
		return invocation -> {
			Assert.assertEquals(page, invocation.getArguments()[0]);
			Assert.assertEquals(pageSize, invocation.getArguments()[1]);
			return new PageableResult<>(page, new ArrayList<>());
		};
	}

	/*
	 * findByItemCode
	 */

	/**
	 * Tests findByItemCode with a list of item codes, none status filter, with counts, all other parameters set.
	 */
	@Test
	public void findByItemCodeItemsNoneFilterCountsAll() {

		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAnswer(this.getItemCodeList(),
						StatusFilter.NONE,
						true,
						ProductDiscontinueControllerTest.PAGE,
						ProductDiscontinueControllerTest.PAGE_SIZE,
						ProductDiscontinueService.SortColumn.ITEM_CODE,
						ProductDiscontinueService.SortDirection.ASC))
				.when(service).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyBoolean(),
				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findAllByItemCodes(this.getItemCodeList(), StatusFilter.NONE,
						true, ProductDiscontinueControllerTest.PAGE, ProductDiscontinueControllerTest.PAGE_SIZE,
						ProductDiscontinueService.SortColumn.ITEM_CODE, ProductDiscontinueService.SortDirection.ASC,
						CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data are null", results.getData());
	}

	/**
	 * Tests findByItemCodes with a list of item codes, null all other parameters.
	 */
	@Test
	public void findByItemCodesItemsNull() {
		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAnswer(this.getItemCodeList(),
						StatusFilter.NONE,
						false,
						0,
						100,
						ProductDiscontinueService.SortColumn.UPC,
						ProductDiscontinueService.SortDirection.ASC))
				.when(service).findByItemCodes(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyBoolean(),
				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findAllByItemCodes(this.getItemCodeList(),
						null,
						false,
						null,
						null,
						null,
						null,
						CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data are null", results.getData());
	}

	/*
	 * findByUpcs
	 */

	/**
	 * Tests findByUpcs with UPCs, none for status filter, counts included, all other parameters set.
	 */
	@Test
	public void findAllByUpcsNoneCountsAll () {

		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAnswer(this.getUpcList(),
						StatusFilter.NONE,
						true,
						ProductDiscontinueControllerTest.PAGE,
						ProductDiscontinueControllerTest.PAGE_SIZE,
						ProductDiscontinueService.SortColumn.UPC,
						ProductDiscontinueService.SortDirection.ASC))
				.when(service).findByUpcs(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyBoolean(),
				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findAllByUpcs(this.getUpcList(), StatusFilter.NONE,
						true, ProductDiscontinueControllerTest.PAGE, ProductDiscontinueControllerTest.PAGE_SIZE,
						ProductDiscontinueService.SortColumn.UPC, ProductDiscontinueService.SortDirection.ASC,
						CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data are null", results.getData());
	}

	/**
	 * Tests findAllByUpcs with UPCs all other parameters null.
	 */
	@Test
	public void testFindAllByUpcUpcsNulls() {
		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAnswer(this.getUpcList(),
						StatusFilter.NONE,
						false,
						0,
						100,
						ProductDiscontinueService.SortColumn.UPC,
						ProductDiscontinueService.SortDirection.ASC))
				.when(service).findByUpcs(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyBoolean(),
				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findAllByUpcs(this.getUpcList(),
						null,
						false,
						null,
						null,
						null,
						null,
						CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data are null", results.getData());
	}

	/*
	 * findByProductIds
	 */

	/**
	 * Tests findByProductIds with products, none for status filter, counts included, all other parameters set.
	 */
	@Test
	public void findByProductIdsNoneCountsAll () {

		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAnswer(this.getProductList(),
						StatusFilter.NONE,
						true,
						ProductDiscontinueControllerTest.PAGE,
						ProductDiscontinueControllerTest.PAGE_SIZE,
						ProductDiscontinueService.SortColumn.UPC,
						ProductDiscontinueService.SortDirection.ASC))
				.when(service).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyBoolean(),
				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findByProductIds(this.getProductList(), StatusFilter.NONE,
						true, ProductDiscontinueControllerTest.PAGE, ProductDiscontinueControllerTest.PAGE_SIZE,
						ProductDiscontinueService.SortColumn.UPC, ProductDiscontinueService.SortDirection.ASC,
						CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data are null", results.getData());
	}

	/**
	 * Tests findByProductIds with UPCs all other parameters null.
	 */
	@Test
	public void findByProductIdssNulls() {
		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		Mockito.doAnswer(
				this.getFindAnswer(this.getProductList(),
						StatusFilter.NONE,
						false,
						0,
						100,
						ProductDiscontinueService.SortColumn.UPC,
						ProductDiscontinueService.SortDirection.ASC))
				.when(service).findByProductIds(Mockito.anyList(),
				Mockito.anyObject(), Mockito.anyBoolean(),
				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		PageableResult<ProductDiscontinue> results =
				productDiscontinueController.findByProductIds(this.getProductList(),
						null,
						false,
						null,
						null,
						null,
						null,
						CommonMocks.getServletRequest());

		Assert.assertNotNull("results are null", results);
		Assert.assertNotNull("data are null", results.getData());
	}

	/**
	 * Test for matching UPCs Hits
	 */
	@Test
	public void findHitsByUPC() {

		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		Hits hitsToReturn = new Hits(0, 0, new ArrayList<>());

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		ValidatingCallChecker<List<Long>, Hits> callChecker = new ValidatingCallChecker<>(this.getUpcList(),
				hitsToReturn);
		Mockito.doAnswer(callChecker).when(service).findHitsByUPCs(Mockito.anyList());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		Hits h = productDiscontinueController.findHitsByUPC(this.getUpcList(), CommonMocks.getServletRequest());
		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertEquals(hitsToReturn, h);
	}

	/**
	 * Test for matching item codes Hits
	 */
	@Test
	public void findHitsByItemCode() {
		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		Hits hitsToReturn = new Hits(0, 0, new ArrayList<>());

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		ValidatingCallChecker<List<Long>, Hits> callChecker = new ValidatingCallChecker<>(this.getUpcList(),
				hitsToReturn);
		Mockito.doAnswer(callChecker).when(service).findHitsByItemCodes(Mockito.anyList());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		Hits h = productDiscontinueController.findHitsByItemCode(this.getUpcList(), CommonMocks.getServletRequest());
		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertEquals(hitsToReturn, h);
	}

	/**
	 * Test for matching products Hits
	 */
	@Test
	public void findHitsByProductIds() {
		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();

		Hits hitsToReturn = new Hits(0, 0, new ArrayList<>());

		ProductDiscontinueService service = Mockito.mock(ProductDiscontinueService.class);
		ValidatingCallChecker<List<Long>, Hits> callChecker = new ValidatingCallChecker<>(this.getUpcList(),
				hitsToReturn);
		Mockito.doAnswer(callChecker).when(service).findHitsByProducts(Mockito.anyList());

		productDiscontinueController.setParameterValidator(Mockito.mock(NonEmptyParameterValidator.class));
		productDiscontinueController.setService(service);
		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());

		Hits h = productDiscontinueController.findHitsByProductId(this.getUpcList(), CommonMocks.getServletRequest());
		Assert.assertTrue(callChecker.isMethodCalled());
		Assert.assertEquals(hitsToReturn, h);
	}

	/**
	 * Tests for loading of item not delete reason properties
	 */
	@Test
	public void getItemNotDeleteReasons() {
		this.productDiscontinueService.setItemNotDelReasons(itemNotDelReasons);
		itemNotDelReasons.setProperty("all.codes", "");
		ALL_DEL_REASONS = this.productDiscontinueService.getAllItemNotDeleteReasons();

		//Execute
		Mockito.when(this.productDiscontinueController.getItemNotDeleteReasons(CommonMocks.getServletRequest())).thenReturn(ALL_DEL_REASONS);

		Assert.assertFalse(ALL_DEL_REASONS.isEmpty());
	}

	/**
	 *
	 */
	@Test
	public void exportToExcel() {

//		Map<String,String> upcCsvMap = new HashMap<>();
//		String csvString = "";
//		upcCsvMap.put("CSV", csvString);
//
//		ProductDiscontinueController productDiscontinueController = new ProductDiscontinueController();
//		ProductDiscontinueService productDiscontinueService = Mockito.mock(ProductDiscontinueService.class);
//		productDiscontinueController.setUserInfo(CommonMocks.getUserInfo());
//
//		Mockito.when(productDiscontinueService.getUpcsExportToExcel(this.getUpcList(), StatusFilter.NONE, this.getUpcList().size(),ProductDiscontinueService.SortColumn.UPC,
//				ProductDiscontinueService.SortDirection.ASC)).thenReturn("");
//		productDiscontinueController.setService(productDiscontinueService);
//
//		Mockito.doReturn("").when(productDiscontinueService).getUpcsExportToExcel(this.getUpcList(), StatusFilter.NONE, this.getUpcList().size(),ProductDiscontinueService.SortColumn.UPC,
//				ProductDiscontinueService.SortDirection.ASC);

//		Map<String,String> csv = productDiscontinueController.exportToExcel(DiscontinueType.UPC, this.getUpcList(), null,
//				StatusFilter.NONE,this.getUpcList().size(),CommonMocks.getServletRequest());
//		Assert.assertEquals(csv.get("CSV"), this.getUpcCsvMap().get("CSV"));
	}

	/*
	 * Support functions.
	 */

	/**
	 * Mocks up the call to any of the find methods on the service.
	 */
	private Answer<PageableResult<ProductDiscontinue>> getFindAnswer(List<Long> stuffToLookFor,
																	 StatusFilter filter, boolean includeCounts, int page, int pageSize,
																	 ProductDiscontinueService.SortColumn sortColumn,
																	 ProductDiscontinueService.SortDirection sortDirection) {

		return invocation -> {
			Assert.assertEquals(stuffToLookFor, invocation.getArguments()[0]);
			Assert.assertEquals(filter, invocation.getArguments()[1]);
			Assert.assertEquals(includeCounts, invocation.getArguments()[2]);
			Assert.assertEquals(page, invocation.getArguments()[3]);
			Assert.assertEquals(pageSize, invocation.getArguments()[4]);
			Assert.assertEquals(sortColumn, invocation.getArguments()[5]);
			Assert.assertEquals(sortDirection, invocation.getArguments()[6]);
			return new PageableResult<>(page, new ArrayList<>());
		};
	}

	/**
	 * Returns a list of item codes to test with.
	 *
	 * @return A list of item codes to test with.
	 */
	private List<Long> getItemCodeList() {
		List<Long> list = new ArrayList<>(2);
		list.add(ProductDiscontinueControllerTest.ITEM_CODE);
		list.add(223L);
		return list;
	}

	/**
	 * returns a list of UPCs to test with.
	 *
	 * @return A list of UPCs to test with.
	 */
	private List<Long> getUpcList() {
		List<Long> list = new ArrayList<>(3);
		list.add(4775466379L);
		list.add(9015932505L);
		list.add(9015981063L);
		return list;
	}

	/**
	 * returns a list of Product IDs to test with.
	 *
	 * @return A list of Product IDs to test with.
	 */
	private List<Long> getProductList() {
		List<Long> list = new ArrayList<>(2);
		list.add(1783158L);
		list.add(1783150L);
		list.add(790643L);
		return list;
	}

	private Map<String,String> getUpcCsvMap(){
		Map<String,String> upcCsvMap = new HashMap<>();
		String csvString = "";
		upcCsvMap.put("CSV", csvString);
		return upcCsvMap;
	}
}
