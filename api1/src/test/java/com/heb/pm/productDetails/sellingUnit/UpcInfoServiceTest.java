/*
 *  UpcInfoServiceTest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.sellingUnit;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.SellingUnitAudit;
import com.heb.pm.entity.SellingUnitWic;
import com.heb.pm.entity.WicCategoryRetailSize;
import com.heb.pm.repository.SellingUnitAuditRepository;
import com.heb.pm.repository.SellingUnitWicRepository;
import com.heb.pm.repository.WicCategoryRetailSizeRepository;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for UpcInfoServiceTest
 *
 * @author l730832
 * @since 2.12.0
 */
public class UpcInfoServiceTest {

	@InjectMocks
	private UpcInfoService service;

	@Mock
	private UserInfo userInfo;

	@Mock
	private SellingUnitWicRepository repository;

	@Mock
	private WicCategoryRetailSizeRepository wicCategoryRetailSizeRepository;

	@Mock
	private SellingUnitAuditRepository sellingUnitAuditRepository;

	@Mock
	private MessageSource messageSource;

	@Mock
	private AuditService auditService;

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

		Mockito.when(this.repository.findByKeyUpc(Mockito.anyLong())).thenReturn(sellingUnitWics);

		List<SellingUnitWic> results = this.service.findByUpc(testLong);
		Assert.assertEquals(results, sellingUnitWics);
	}

	/**
	 * Tests getCrossLinkedListOfUPCs
	 */
	@Test
	public void testGetCrossLinkedListOfUPCs() {
		List<SellingUnitWic> productDetailsList;
		productDetailsList = this.getDefaultListWicProductDetails();
		Mockito.when(this.repository.findByKeyWicApprovedProductListIdOrderByKeyUpc(Mockito.anyLong())).thenReturn(productDetailsList);
		List<SellingUnitWic> result = this.service.getCrossLinkedListOfUPCs(testLong);
		Assert.assertEquals(result, productDetailsList);
	}

	/**
	 * Tests get Leb Upcs.
	 */
	@Test
	public void testGetLebUpcs() {
		List<SellingUnitWic> sellingUnitWics = this.getDefaultListWicProductDetails();
		Mockito.when(this.repository.findByKeyWicCategoryIdAndKeyWicSubCategoryIdOrderByKeyUpc(
				Mockito.anyLong(), Mockito.anyLong())).thenReturn(sellingUnitWics);
		List<SellingUnitWic> result = this.service.getLebUpcs(testLong, testLong);
		Assert.assertEquals(result, sellingUnitWics);
	}

	/**
	 * Tests Get LEB Sizes.
	 */
	@Test
	public void testGetLebSizes() {
		List<WicCategoryRetailSize> wicCategoryRetailSizes = this.getDefaultListWicCategoryRetailSizes();
		Mockito.when(this.wicCategoryRetailSizeRepository.findByKeyWicCategoryIdAndKeyWicSubCategoryId(
				Mockito.anyLong(), Mockito.anyLong())).thenReturn(wicCategoryRetailSizes);
		List<WicCategoryRetailSize> result = this.service.getLebSizes(testLong, testLong);
		Assert.assertEquals(result, wicCategoryRetailSizes);
	}

	/**
	 * This tests get upc audit information.
	 */
	@Test
	public void testGetUpcAuditInformation() {
		List<AuditRecord> auditRecords = this.getDefaultRecordList();
		List<SellingUnitAudit> audits = new ArrayList<>();
		SellingUnitAudit sellingUnitAudit = this.getDefaultSellingUnit();
		audits.add(sellingUnitAudit);
		Mockito.when(this.sellingUnitAuditRepository.findByKeyUpcOrderByKeyChangedOn(
				Mockito.anyLong())).thenReturn(audits);
		List<AuditRecord> result = this.service.getUpcAuditInformation(testLong, testString);
		Assert.assertEquals(result, auditRecords);
	}

	/**
	 * Gets a default list of wicProductDetails.
	 * @return list of wicProductDetail objects.
	 */
	private List<SellingUnitWic> getDefaultListWicProductDetails(){
		List<SellingUnitWic> productDetailsList = new ArrayList<>();
		SellingUnitWic wicProductDetails = new SellingUnitWic();
		productDetailsList.add(wicProductDetails);
		return productDetailsList;
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
	private SellingUnitAudit getDefaultSellingUnit() {
		return new SellingUnitAudit();
	}

}