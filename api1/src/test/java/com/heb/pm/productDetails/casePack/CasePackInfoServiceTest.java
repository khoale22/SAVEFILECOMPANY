/*
 *  CasePackInfoService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.ItemType;
import com.heb.pm.entity.OneTouchType;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.ItemTypeRepository;
import com.heb.pm.repository.OneTouchTypeRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for CasePackInfoService
 *
 * @author l730832
 * @since 2.7.0
 */
public class CasePackInfoServiceTest {

	private static final String testString = "Message";

	@InjectMocks
	private CasePackInfoService service;

	@Mock
	private ItemTypeRepository itemTypeRepository;

	@Mock
	private AuditService auditService;

	@Mock
	private OneTouchTypeRepository oneTouchTypeRepository;

	@Mock
	private ProductManagementServiceClient productManagementServiceClient;

	@Mock
	private ItemMasterRepository itemMasterRepository;

	/**
	 * Initializes mockito.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests the retrieval of all item types.
	 */
	@Test
	public void testGetAllItemTypes() {

		Mockito.when(this.itemTypeRepository.findAll()).thenReturn(this.getDefaultListOfItemTypes());
		List<ItemType> result = this.service.getAllItemTypes();
		Assert.assertEquals(this.getDefaultListOfItemTypes(), result);
	}

	/**
	 * Tests the retrieval of all one touch types.
	 */
	@Test
	public void testGetAllOneTouchTypes() {

		Mockito.when(this.oneTouchTypeRepository.findAll()).thenReturn(this.getDefaultListOfOneTouchTypes());
		List<OneTouchType> result = this.service.getAllOneTouchTypes();
		Assert.assertEquals(this.getDefaultListOfOneTouchTypes(), result);
	}

	/**
	 * Tests the updating and saving of a case pack.
	 */
	@Test
	public void testUpdateCasePackInfo() {

		Mockito.when(this.itemMasterRepository.findOne(Mockito.any(ItemMasterKey.class))).
				thenReturn(this.getDefaultItemMaster());
		ItemMaster result = this.service.updateCasePackInfo(this.getDefaultItemMaster());
		Assert.assertEquals(this.getDefaultItemMaster(), result);
	}

	@Test
	public void testGetCasePackAuditInformation() {
		List<AuditRecord> auditRecords = this.getDefaultRecordList();
		Mockito.when(this.auditService.getCasePackAuditInformation(
				Mockito.any(ItemMasterKey.class), Mockito.anyString())).thenReturn(auditRecords);
		List<AuditRecord> result = this.service.getCasePackAuditInformation(this.getDefaultItemMaster().getKey(), testString);
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