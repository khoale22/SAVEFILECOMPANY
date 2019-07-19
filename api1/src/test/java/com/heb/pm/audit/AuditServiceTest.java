/*
 *
 * AuditServiceTest.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.audit;

import com.heb.pm.entity.ItemMasterAudit;
import com.heb.pm.entity.ItemMasterAuditKey;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.ShipperAudit;
import com.heb.pm.entity.ShipperAuditKey;
import com.heb.pm.repository.ItemMasterAuditRepository;
import com.heb.pm.repository.ShipperAuditRepository;
import com.heb.util.audit.AuditComparisonDelegate;
import com.heb.util.audit.AuditComparisonImpl;
import com.heb.util.audit.AuditRecord;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for Audit Service
 * @author l730832
 * @since 2.6.0
 */
public class AuditServiceTest {

	private static final String testString = "Test";
	private static final long testLong = 0;
	private static final boolean testBool = true;

	// These are the constants for attribute names.
	private static final String QUANTITY_ATTRIBUTE_NAME = "Quantity";
	private static final String UPC_ATTRIBUTE_NAME = "UPC";
	private static final String NA_ATTRIBUTE_NAME = "NA";
	private static final String UNKNOWN_ATTRIBUTE_NAME = "Unknown";

	// These are the constants for the action codes of what happened in the audit.
	private static final String ADD_ACTION_CODE = "ADD";
	private static final String PURGE_ACTION_CODE = "PURGE";
	private static final String UPDATE_ACTION_CODE = "UPDAT";

	//Default DRU Audit Values
	private static final String DEFAULT_DRU_TYPE="7";
	private static final String DEFAULT_DRU_AUDIT_ATTRIBUTE="Orientation on shelf";
	private static final String DEFAULT_CASE_PACK_AUDIT_ATTRIBUTE="One Touch Type";

	@InjectMocks
	private AuditService auditService;

	@Mock
	private ShipperAuditRepository shipperAuditRepository;

	@Mock
	private ItemMasterAuditRepository itemMasterAuditRepository;

	@Mock
	private AuditComparisonDelegate auditComparisonDelegate;

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests with update action code with no known earlier audit.
	 */
	@Test
	public void testGetMrtAuditInformationAddAction() {
		List<ShipperAudit> shipperAuditList = new ArrayList<>();
		shipperAuditList.add(this.getDefaultShipperAudit(ADD_ACTION_CODE, UPC_ATTRIBUTE_NAME, "0", NA_ATTRIBUTE_NAME));
		Mockito.when(this.shipperAuditRepository.findByKeyUpcOrderByKeyChangedOn(Mockito.anyLong())).thenReturn(shipperAuditList);
		List<AuditRecordWithUpc> returnResult = this.auditService.getMrtAuditInformation(testLong);
		Assert.assertEquals(shipperAuditList, returnResult);
	}

	/**
	 * Tests with update action code with no known earlier audit.
	 */
	@Test
	public void testGetMrtAuditInformationUpdateActionWithNullName() {
		List<ShipperAudit> shipperAuditList = new ArrayList<>();
		shipperAuditList.add(this.getDefaultShipperAudit(UPDATE_ACTION_CODE, QUANTITY_ATTRIBUTE_NAME, "0", UNKNOWN_ATTRIBUTE_NAME));
		Mockito.when(this.shipperAuditRepository.findByKeyUpcOrderByKeyChangedOn(Mockito.anyLong())).thenReturn(shipperAuditList);
		List<AuditRecordWithUpc> returnResult = this.auditService.getMrtAuditInformation(testLong);
		Assert.assertEquals(shipperAuditList, returnResult);
	}

	/**
	 * Tests with update action code with no known earlier audit.
	 */
	@Test
	public void testGetMrtAuditInformationPurgeAction() {
		List<ShipperAudit> shipperAuditList = new ArrayList<>();
		shipperAuditList.add(this.getDefaultShipperAudit(PURGE_ACTION_CODE, UPC_ATTRIBUTE_NAME, NA_ATTRIBUTE_NAME, "0"));
		Mockito.when(this.shipperAuditRepository.findByKeyUpcOrderByKeyChangedOn(Mockito.anyLong())).thenReturn(shipperAuditList);
		List<AuditRecordWithUpc> returnResult = this.auditService.getMrtAuditInformation(testLong);
		Assert.assertEquals(shipperAuditList, returnResult);
	}

	/**
	 * Tests if the action code is an empty string.
	 */
	@Test
	public void testGetMrtAuditInformationEmptyAction() {
		List<ShipperAudit> shipperAuditList = new ArrayList<>();
		shipperAuditList.add(this.getDefaultShipperAudit(StringUtils.EMPTY, QUANTITY_ATTRIBUTE_NAME, "0", NA_ATTRIBUTE_NAME));
		Mockito.when(this.shipperAuditRepository.findByKeyUpcOrderByKeyChangedOn(Mockito.anyLong())).thenReturn(shipperAuditList);
		List<AuditRecordWithUpc> returnResult = this.auditService.getMrtAuditInformation(testLong);
		Assert.assertEquals(shipperAuditList, returnResult);
	}

	/**
	 * Tests if the previous quantity is not null.
	 */
	@Test
	public void testGetMrtAuditInformationPreviousQuantityNotNull() {
		List<ShipperAudit> shipperAuditList = new ArrayList<>();
		shipperAuditList.add(this.getDefaultShipperAudit(UPDATE_ACTION_CODE, QUANTITY_ATTRIBUTE_NAME, "5", "5"));
		ShipperAudit shipperAudit = this.getDefaultShipperAudit(UPDATE_ACTION_CODE, QUANTITY_ATTRIBUTE_NAME, "6", "5");
		shipperAudit.setShipperQuantity(5L);
		shipperAuditList.add(shipperAudit);
		shipperAuditList.add(this.getDefaultShipperAudit(UPDATE_ACTION_CODE, QUANTITY_ATTRIBUTE_NAME, "7", "5"));
		Mockito.when(this.shipperAuditRepository.findByKeyUpcOrderByKeyChangedOn(Mockito.anyLong())).thenReturn(shipperAuditList);
		List<AuditRecordWithUpc> returnResult = this.auditService.getMrtAuditInformation(testLong);
		Assert.assertEquals(shipperAuditList.get(1), returnResult.get(1));
	}

	/**
	 * Tests getDruAuditInformation
	 */
	@Test
	public void getDruAuditInformationTest(){
		List<AuditRecord> test = getDruAuditList();
		Mockito.when(this.itemMasterAuditRepository.findByKeyItemCodeAndKeyItemTypeOrderByKeyChangedOn(testLong, testString))
				.thenReturn(getDefaultItemMasterAuditList());
		Mockito.when(this.auditComparisonDelegate.processClassFromList(Mockito.anyList(), Mockito.anyString())).thenReturn(test);
		List<AuditRecord> result = this.auditService.getDruAuditInformation(getItemMasterKey(), testString);
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests getCasePackInformation.
	 */
	@Test
	public void testGetCasePackInformationTest() {
		List<AuditRecord> test = this.getCasePackInfoAuditList();
		Mockito.when(this.itemMasterAuditRepository.findByKeyItemCodeAndKeyItemTypeOrderByKeyChangedOn(testLong, testString))
				.thenReturn(getDefaultItemMasterAuditList());
		Mockito.when(this.auditComparisonDelegate.processClassFromList(Mockito.anyList(), Mockito.anyString())).thenReturn(test);
		List<AuditRecord> result = this.auditService.getCasePackAuditInformation(getItemMasterKey(), testString);
		Assert.assertEquals(test, result);
	}

	/**
	 * This returns a default shipper audit.
	 * @param actionCode The action code for the shipper audit.
	 * @param attributeName The attribute name for the shipper audit.
	 * @param changedTo The changed to for the shipper audit.
	 * @param changedFrom The changed from for the shipper audit.
	 * @return
	 */
	private ShipperAudit getDefaultShipperAudit(String actionCode, String attributeName, String changedTo, String changedFrom) {
		ShipperAuditKey key= new ShipperAuditKey();
		key.setUpc(testLong);
		key.setModifiedUpc(1L);
		key.setChangedOn(LocalDateTime.now());

		// Creates the shipper audit.
		ShipperAudit shipperAudit = new ShipperAudit();
		shipperAudit.setKey(key);
		shipperAudit.setShipperQuantity(testLong);
		shipperAudit.setAction(actionCode);
		shipperAudit.setChangedBy(testString);
		return shipperAudit;
	}

	private ItemMasterAudit getDefaultItemMasterAudit(long orientation){
		ItemMasterAuditKey key = new ItemMasterAuditKey();
		key.setChangedOn(LocalDateTime.now());
		key.setItemCode(testLong);
		key.setItemType(testString);
		ItemMasterAudit audit = new ItemMasterAudit();
		audit.setAction(UPDATE_ACTION_CODE);
		audit.setAlwaysSubWhenOut(testBool);
		audit.setChangedBy(testString);
		audit.setDisplayReadyUnit(testBool);
		audit.setKey(key);
		audit.setOrientation(orientation);
		audit.setRowsDeep(testLong);
		audit.setRowsHigh(testLong);
		audit.setRowsFacing(testLong);
		audit.setTypeOfDRU(DEFAULT_DRU_TYPE);
		return audit;
	}

	private List<ItemMasterAudit> getDefaultItemMasterAuditList(){
		ArrayList<ItemMasterAudit> list = new ArrayList<>();
		list.add(getDefaultItemMasterAudit(1L));
		list.add(getDefaultItemMasterAudit(2L));
		return list;
	}

	private List<AuditRecord> getDruAuditList(){
		AuditRecord record = new AuditRecord(LocalDateTime.now(), testString, testString, DEFAULT_DRU_AUDIT_ATTRIBUTE);
		record.setChangedFrom("1");
		record.setChangedTo("2");
		List<AuditRecord> list = new ArrayList<>();
		list.add(record);
		return list;
	}

	/**
	 * Returns a list of case pack info auits
	 * @return
	 */
	private List<AuditRecord> getCasePackInfoAuditList() {
		AuditRecord record = new AuditRecord(LocalDateTime.now(), testString, testString, DEFAULT_CASE_PACK_AUDIT_ATTRIBUTE);
		record.setChangedFrom("1");
		record.setChangedTo("2");
		List<AuditRecord> list = new ArrayList<>();
		list.add(record);
		return list;
	}

	private ItemMasterKey getItemMasterKey(){
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(testLong);
		key.setItemType(testString);
		return key;
	}

}