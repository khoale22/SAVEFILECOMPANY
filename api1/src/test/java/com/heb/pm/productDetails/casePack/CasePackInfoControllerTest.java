/*
 *  CasePackInfoController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.ItemType;
import com.heb.pm.entity.OneTouchType;
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
 * Test class for CasePackInfoController
 *
 * @author l730832
 * @since 2.7.0
 */
public class CasePackInfoControllerTest {

	private static final Long testUpc = 4011L;

	private static final Long longCheckDigit = 2L;

	private static final Integer intCheckDigit = 2;

	private static final Integer wrongCheckDigit = 15;

	private static final String testString = "Message";

	@InjectMocks
	private CasePackInfoController casePackInfoController;

	@Mock
	private CasePackInfoService service;

	@Mock
	private MessageSource messageSource;

	@Mock
	private UserInfo userInfo;

	/**
	 * Initializes mockito.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests the calculation of the check digit.
	 */
	@Test
	public void testCalculateCheckDigit() {

		ModifiedEntity<Long> result = casePackInfoController.calculateCheckDigit(testUpc, CommonMocks.getServletRequest());
		Assert.assertEquals(longCheckDigit, result.getData());
	}

	/**
	 * Tests the retrieval of the list of one touch types from the code table.
	 */
	@Test
	public void testGetAllOneTouchType() {

		Mockito.when(this.service.getAllOneTouchTypes()).thenReturn(this.getDefaultListOfOneTouchTypes());
		List<OneTouchType> result = this.casePackInfoController.getAllOneTouchType(CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultListOfOneTouchTypes(), result);
	}

	/**
	 * Tests the retrieval of the list of item types from the code table.
	 */
	@Test
	public void testGetAllItemTypes() {

		Mockito.when(this.service.getAllItemTypes()).thenReturn(this.getDefaultListOfItemTypes());
		List<ItemType> result = this.casePackInfoController.getAllItemTypes(CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultListOfItemTypes(), result);
	}

	/**
	 * Tests the confirmation of a correct check digit.
	 */
	@Test
	public void testConfirmCheckDigit() {

		ModifiedEntity<Boolean> result = casePackInfoController.confirmCheckDigit(intCheckDigit, testUpc, CommonMocks.getServletRequest());
		Assert.assertEquals(true, result.getData());
	}

	/**
	 * Tests the incorrect confirmation of a check digit.
	 */
	@Test
	public void testIncorrectCheckDigit() {

		ModifiedEntity<Boolean> result = casePackInfoController.confirmCheckDigit(wrongCheckDigit, testUpc, CommonMocks.getServletRequest());
		Assert.assertEquals(false, result.getData());
	}

	/**
	 * Tests the updating and saving of a case pack info.
	 */
	@Test
	public void testSaveCasePackInfoChanges() {

		Mockito.when(this.service.updateCasePackInfo(Mockito.any(ItemMaster.class))).thenReturn(this.getDefaultItemMaster());
		ModifiedEntity<ItemMaster> result = this.casePackInfoController.saveCasePackInfoChanges(this.getDefaultItemMaster(),
				CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultItemMaster(), result.getData());
	}

	/**
	 * Tests getCasePackAuditInfo
	 */
	@Test
	public void testGetCasePackAuditInfo() {
		List<AuditRecord> auditRecords = this.getDefaultRecordList();
		Mockito.when(this.service.getCasePackAuditInformation(Mockito.any(ItemMasterKey.class), Mockito.anyString())).thenReturn(auditRecords);
		List<AuditRecord> result = this.casePackInfoController.getCasePackAuditInfo(this.getDefaultItemMaster().getKey(), CommonMocks.getServletRequest());
		Assert.assertEquals(result, auditRecords);
	}

	/**
	 * Returns a list of one touch types to use for testing.
	 *
	 * @return list of one touch types
	 */
	private List<OneTouchType> getDefaultListOfOneTouchTypes() {
		List<OneTouchType> oneTouchTypeList = new ArrayList<>();
		OneTouchType oneTouchType = new OneTouchType();
		oneTouchType.setId(testString);
		oneTouchType.setAbbreviation(testString);
		oneTouchType.setDescription(testString);
		oneTouchTypeList.add(oneTouchType);
		return oneTouchTypeList;
	}

	/**
	 * Returns a list of Item types to use for testing.
	 *
	 * @return list of item types to use for testing.
	 */
	private List<ItemType> getDefaultListOfItemTypes() {
		List<ItemType> itemTypeList = new ArrayList<>();
		ItemType itemType = new ItemType();
		itemType.setId(testString);
		itemType.setDescription(testString);
		itemType.setAbbreviation(testString);
		itemTypeList.add(itemType);
		return itemTypeList;
	}

	/**
	 * This retuns a default item master to be used for testing.
	 *
	 * @return a test item master
	 */
	private ItemMaster getDefaultItemMaster(){
		ItemMaster im = new ItemMaster();
		ItemMasterKey key = new ItemMasterKey();
		key.setItemType("test1");
		key.setItemCode(2L);
		im.setKey(key);
		return im;
	}

	/**
	 * Create a generic list of Audit Records
	 * @return
	 */
	private List<AuditRecord> getDefaultRecordList(){
		return new ArrayList<>();
	}

}