/*
 *  UpcInfoControllerTest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.entity.SellingUnit;
import com.heb.pm.entity.SellingUnitWic;
import com.heb.pm.entity.WicCategoryRetailSize;
import com.heb.pm.productDetails.product.SellingUnitWicResolver;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for UpcInfoControllerTest
 *
 * @author l730832
 * @since 2.12.0
 */
public class UpcInfoControllerTest {

	@InjectMocks
	private UpcInfoController controller;

	@Mock
	private UpcInfoService service;

	@Mock
	private UserInfo userInfo;

	@Mock
	private MessageSource messageSource;

	@Mock
	private SellingUnitWicResolver resolver;

	private String testString = "Test";

	private Long testLong = 2L;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests find by upc.
	 */
	@Test
	public void testFindByUpc() {
		List<SellingUnitWic> sellingUnitWics = this.getDefaultListWicProductDetails();

		Mockito.when(this.service.findByUpc(Mockito.anyLong())).thenReturn(sellingUnitWics);

		List<SellingUnitWic> results = this.controller.findByUpc(testLong, CommonMocks.getServletRequest());
		Assert.assertEquals(results, sellingUnitWics);
	}

	/**
	 * Tests getListOfCrossLinkedUPCs.
	 */
	@Test
	public void testGetListOfCrossLinkedUPCs() {
		List<SellingUnitWic> productDetailsList = this.getDefaultListWicProductDetails();
		Mockito.when(this.service.getCrossLinkedListOfUPCs(Mockito.anyLong())).thenReturn(productDetailsList);
		ModifiedEntity<List<SellingUnitWic>> result = this.controller.getListOfCrossLinkedUPCs(testLong, CommonMocks.getServletRequest());
		Assert.assertEquals(result.getData(), productDetailsList);
	}

	/**
	 * Tests get LEB Upcs.
	 */
	@Test
	public void testGetLebUpcs() {
		List<SellingUnitWic> sellingUnitWics = this.getDefaultListWicProductDetails();
		Mockito.when(this.service.getLebUpcs(Mockito.anyLong(), Mockito.anyLong())).thenReturn(sellingUnitWics);
		List<SellingUnitWic> result = this.controller.getLebUpcs(testLong, testLong, CommonMocks.getServletRequest());
		Assert.assertEquals(result, sellingUnitWics);
	}

	/**
	 * Tests get LEB sizes.
	 */
	@Test
	public void testGetLebSizes() {
		List<WicCategoryRetailSize> wicCategoryRetailSizes = this.getDefaultListWicCategoryRetailSizes();
		Mockito.when(this.service.getLebSizes(Mockito.anyLong(), Mockito.anyLong())).thenReturn(wicCategoryRetailSizes);
		List<WicCategoryRetailSize> result = this.controller.getLebSizes(testLong, testLong, CommonMocks.getServletRequest());
		Assert.assertEquals(result, wicCategoryRetailSizes);
	}

	@Test
	public void testGetUpcInfoAuditInfo() {
		List<AuditRecord> auditRecords = this.getDefaultRecordList();
		Mockito.when(this.service.getUpcAuditInformation(Mockito.anyLong(), Mockito.anyString())).thenReturn(auditRecords);
		List<AuditRecord> result = this.controller.getUpcInfoAuditInfo(this.getDefaultSellingUnit(), CommonMocks.getServletRequest());
		Assert.assertEquals(result, auditRecords);
	}

	/**
	 * Gets a default list of wicProductDetails.
	 * @return list of wicProductDetail objects.
	 */
	private List<SellingUnitWic> getDefaultListWicProductDetails() {
		List<SellingUnitWic> productDetailsList = new ArrayList<>();
		SellingUnitWic wicProductDetails = new SellingUnitWic();
		productDetailsList.add(wicProductDetails);
		return productDetailsList;
	}

	/**
	 * Create a generic list of Audit Records
	 * @return
	 */
	private List<AuditRecord> getDefaultRecordList(){
		return new ArrayList<>();
	}

	/**
	 * Returns a default selling unit for testing.
	 * @return
	 */
	private SellingUnit getDefaultSellingUnit() {
		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setUpc(testLong);
		return sellingUnit;
	}

	/**
	 * Gets a default list of wicCategoryRetailSizes.
	 * @return list of wicCategoryRetailSizes.
	 */
	private List<WicCategoryRetailSize> getDefaultListWicCategoryRetailSizes() {
		List<WicCategoryRetailSize> wicCategoryRetailSizes = new ArrayList<>();
		WicCategoryRetailSize wicCategoryRetailSize = new WicCategoryRetailSize();
		wicCategoryRetailSizes.add(wicCategoryRetailSize);
		return wicCategoryRetailSizes;
	}
}