/*
 * DiscontinueExceptionParametersServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.*;
import com.heb.pm.product.UpcService;
import com.heb.pm.repository.DiscontinueExceptionParametersAuditRepository;
import com.heb.pm.repository.DiscontinueExceptionParametersRepository;
import com.heb.util.controller.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests DiscontinueExceptionParametersService.
 *
 * @author m314029
 * @since 2.0.3
 */
public class DiscontinueExceptionParametersServiceTest {

	/*
	 * findAll
	 */

	/**
	 * Tests findAll.
	 */
	@Test
	public void findAll() {
		DiscontinueExceptionParametersService service = new DiscontinueExceptionParametersService();
		service.setRepository(this.getAllRepository());
		service.setConverter(this.getVendorConverter());
		DiscontinueRules returnRule = service.findAll().get(0);

		this.fullItemCompare(returnRule, this.getVendorRulesObject());
	}

	/*
	 * findByExceptionType
	 */

	/**
	 * Tests the findByExceptionType when passed a valid exception type.
	 */
	@Test
	public void findByExceptionType() {
		DiscontinueExceptionParametersService service = new DiscontinueExceptionParametersService();
		service.setRepository(this.getRepositoryReturnsVendor());
		service.setConverter(this.getVendorConverter());
		DiscontinueRules returnRule = service.findByExceptionType(ProductDiscontinueExceptionType.VENDOR).get(0);

		this.fullItemCompare(returnRule, this.getVendorRulesObject());
	}

	/*
	 * update
	 */

	public void update() {

	}

	/*
	 * add
	 */

	/**
	 * Tests the addRule method when passed valid DiscontinueRule.
	 */
	@Test
	public void add() {

		DiscontinueExceptionParametersService service = new DiscontinueExceptionParametersService();
		UserInfo userInfo = Mockito.mock(UserInfo.class);
		Mockito.when(userInfo.getUserId()).thenReturn("v357407");
		DiscontinueExceptionParametersRepository repository = Mockito.mock(DiscontinueExceptionParametersRepository.class);
		Mockito.when(repository.getMaxExceptionSequenceNumber()).thenReturn(25);
		Mockito.when(repository.save(this.getVendorExceptionParametersList()))
				.thenReturn(this.getVendorExceptionParametersList());
		service.setRepository(repository);
		DiscontinueExceptionParametersAuditRepository auditRepository = Mockito.mock(DiscontinueExceptionParametersAuditRepository.class);
		Mockito.when(auditRepository.save(this.createExceptionParametersAudits(this.getVendorExceptionParametersList(),"ADD")))
				.thenReturn(this.createExceptionParametersAudits(this.getVendorExceptionParametersList(), "ADD"));
		service.setAuditRepository(auditRepository);


		DiscontinueParametersToDiscontinueRules converter = Mockito.mock(DiscontinueParametersToDiscontinueRules.class);
		Mockito.when(converter.toDiscontinueExceptionParameters(this.getVendorRulesObject()))
				.thenReturn(this.getVendorExceptionParametersList());
		Mockito.when(converter.toDiscontinueRulesFromExceptions(this.getVendorExceptionParametersList()))
				.thenReturn(this.getVendorRulesObject());
		service.setConverter(converter);
		service.setUserInfo(userInfo);
		DiscontinueRules returnRule = service.add(this.getVendorRulesObject());

		Assert.assertNotNull("results are null", returnRule);
	}

	/**
	 * Tests the addRule method when passed an SBT DiscontinueRule, when an SBT exception already exists.
	 * The call should fail, because you are only allowed one SBT exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addSBTExists(){

		DiscontinueExceptionParametersService service = new DiscontinueExceptionParametersService();
		service.setRepository(this.getRepositoryReturnsSbt());
		service.setConverter(new DiscontinueParametersToDiscontinueRules());
		DiscontinueRules sbtRule = getSBTRulesObject();
		service.add(sbtRule);
	}

	/**
	 * Tests the addRule method when passed an DiscontinueRule, when exception already exists.
	 * The call should fail, because you can have only one exception tied to a type and id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addRecordFound(){

		DiscontinueExceptionParametersService service = new DiscontinueExceptionParametersService();
		service.setRepository(this.getRepositoryReturnsSbt());
		Mockito.when(service.findByExceptionTypeAndId("Vendor", "10056")).thenReturn(this.getVendorExceptionParametersList());


		DiscontinueParametersToDiscontinueRules converter = Mockito.mock(DiscontinueParametersToDiscontinueRules.class);
		Mockito.when(converter.toDiscontinueRulesFromExceptions(this.getVendorExceptionParametersList()))
				.thenReturn(this.getVendorRulesObject());
		service.setConverter(converter);
		DiscontinueRules sbtRule = getSBTRulesObject();
		service.add(sbtRule);
	}

	/**
	 * Tests the addRule method when passed an SBT DiscontinueRule, with a UPC that doesn't exist.
	 * The call should fail, because the UPC needs to be valid.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addUpcNotFound(){

		DiscontinueExceptionParametersService service = new DiscontinueExceptionParametersService();
		service.setRepository(this.getRepositoryReturnsSbt());

		DiscontinueParametersToDiscontinueRules converter = Mockito.mock(DiscontinueParametersToDiscontinueRules.class);
		Mockito.when(converter.toDiscontinueRulesFromExceptions(this.getVendorExceptionParametersList()))
				.thenReturn(this.getVendorRulesObject());
		service.setConverter(converter);
		service.setUpcService(this.getUpcServiceNoUpcFound());
		DiscontinueRules sbtRule = getSBTRulesObject();
		service.add(sbtRule);
	}

	/*
	 * delete
	 */

	public void delete() {

	}

	/*
	 * toDiscontinueRules
	 */

	public void toDiscontinueRules(){

	}

	/*
	 * findByExceptionTypeAndId
	 */

	public void findByExceptionTypeAndId(){

	}

	/*
	 * support functions
	 */

	/**
	 *
	 * This method returns a DiscontinueExceptionParametersRepository with an SBT exception type.
	 *
	 * @return DiscontinueExceptionParametersRepository containing an sbt exception type.
	 */
	private DiscontinueExceptionParametersRepository getRepositoryReturnsSbt() {

		DiscontinueExceptionParametersRepository repository = Mockito.mock(DiscontinueExceptionParametersRepository.class);
		List<DiscontinueExceptionParameters> parametersList = new ArrayList<>();
		DiscontinueExceptionParameters parameters = new DiscontinueExceptionParameters();
		parameters.setExceptionType("SBT");
		parameters.setExceptionTypeId("");
		parameters.setActive(true);
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setExceptionNumber(0);
		key.setSequenceNumber(-1);
		key.setId(-2);
		parameters.setKey(key);
		parametersList.add(parameters);
		Mockito.when(repository.findByExceptionType(Mockito.anyString(), Mockito.anyObject())).thenReturn(parametersList);
		return repository;

	}

	/**
	 * This method returns a DiscontinueExceptionParametersRepository with a vendor exception type.
	 *
	 * @return DiscontinueExceptionParametersRepository containing a vendor exception type.
	 */
	private DiscontinueExceptionParametersRepository getRepositoryReturnsVendor() {

		DiscontinueExceptionParametersRepository repository = Mockito.mock(DiscontinueExceptionParametersRepository.class);
		List<DiscontinueExceptionParameters> parametersList = new ArrayList<>();
		DiscontinueExceptionParameters parameters = new DiscontinueExceptionParameters();
		parameters.setExceptionType("Vendor");
		parameters.setExceptionTypeId("50016");
		parameters.setActive(true);
		parameters.setNeverDelete(false);
		parameters.setParameterValue("540");
		parameters.setPriority(7);
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setExceptionNumber(3);
		key.setSequenceNumber(1);
		key.setId(1);
		parameters.setKey(key);
		parametersList.add(parameters);
		Mockito.when(repository.findByExceptionType(Mockito.anyString(), Mockito.anyObject())).thenReturn(parametersList);
		return repository;

	}

	/**
	 *  This method returns a DiscontinueExceptionParametersRepository with a vendor exception type.
	 *
	 * @return DiscontinueExceptionParametersRepository containing vendor exception type.
	 */
	private DiscontinueExceptionParametersRepository getAllRepository() {

		DiscontinueExceptionParametersRepository repository = Mockito.mock(DiscontinueExceptionParametersRepository.class);
		List<DiscontinueExceptionParameters> parametersList = new ArrayList<>();
		DiscontinueExceptionParameters parameters = new DiscontinueExceptionParameters();
		parameters.setExceptionType("Vendor");
		parameters.setExceptionTypeId("50016");
		parameters.setActive(true);
		parameters.setNeverDelete(false);
		parameters.setParameterValue("540");
		parameters.setPriority(7);
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setExceptionNumber(3);
		key.setSequenceNumber(1);
		key.setId(1);
		parameters.setKey(key);
		parametersList.add(parameters);
		Mockito.when(repository.findAll()).thenReturn(parametersList);
		return repository;
	}

	/**
	 *  This Function creates a default DiscontinueRules with the numeric params in number of months values.
	 * @return DiscontinueRules.
	 */
	private DiscontinueRules getDefaultRulesObjectInMonths() {

		DiscontinueRules exceptionObject = new DiscontinueRules();
		exceptionObject.setStoreSales("12");
		exceptionObject.setSalesSwitch(true);
		exceptionObject.setNewItemPeriod("12");
		exceptionObject.setNewProductSetupSwitch(true);
		exceptionObject.setWarehouseUnits("0");
		exceptionObject.setWarehouseUnitSwitch(true);
		exceptionObject.setStoreUnits("0");
		exceptionObject.setStoreUnitSwitch(true);
		exceptionObject.setStoreReceipts("18");
		exceptionObject.setReceiptsSwitch(true);
		exceptionObject.setPurchaseOrders("18");
		exceptionObject.setPurchaseOrderSwitch(true);
		return exceptionObject;
	}

	/**
	 *  This Function creates a DiscontinueRules where the exception type is 'SBT'
	 *  with the numeric params in number of month values.
	 * @return DiscontinueRules.
	 */
	private DiscontinueRules getSBTRulesObject() {

		DiscontinueRules sbtException = this.getDefaultRulesObjectInMonths();
		sbtException.setNeverDiscontinueSwitch(false);
		sbtException.setExceptionTypeId("");
		sbtException.setExceptionName("SCAN BASED TRADE");
		sbtException.setExceptionType(ProductDiscontinueExceptionType.SBT.getType());
		sbtException.setPriorityNumber(ProductDiscontinueExceptionType.SBT.getPriority());
		sbtException.setExceptionNumber(22);
		return sbtException;
	}

	/**
	 *  This Function creates a DiscontinueRules where the exception type is 'Vendor'
	 *  with the numeric params in number of month values.
	 * @return DiscontinueRules.
	 */
	private DiscontinueRules getVendorRulesObject() {

		DiscontinueRules sbtException = this.getDefaultRulesObjectInMonths();
		sbtException.setNeverDiscontinueSwitch(false);
		sbtException.setExceptionTypeId("50016");
		sbtException.setExceptionName("DUMMY NATIONAL PORK[50016]");
		sbtException.setExceptionType(ProductDiscontinueExceptionType.VENDOR.getType());
		sbtException.setPriorityNumber(ProductDiscontinueExceptionType.VENDOR.getPriority());
		sbtException.setExceptionNumber(3);
		return sbtException;
	}

	/**
	 * Return a list of DiscontinueExceptionParameters for a vendor exception.
	 * @return A list of DiscontinueExceptionParameters for a vendor exception.
	 */
	private List<DiscontinueExceptionParameters> getVendorExceptionParametersList(){
		List<DiscontinueExceptionParameters> returnList = new ArrayList<>();

		DiscontinueExceptionParameters exceptionParameters = null;
		DiscontinueRules discontinueRules = this.getVendorRulesObject();
		for(DiscontinueParameterType discontinueParameterType:
				DiscontinueParameterType.allTypes) {
			exceptionParameters = new DiscontinueExceptionParameters();

			DiscontinueExceptionParametersKey exceptionParametersKey = new DiscontinueExceptionParametersKey();
			exceptionParametersKey.setId(discontinueParameterType.getId());
			// For purchase orders, the sequence number is different than all the others.
			if (discontinueParameterType == DiscontinueParameterType.PURCHASE_ORDERS) {
				exceptionParametersKey.setSequenceNumber(2);
			} else {
				exceptionParametersKey.setSequenceNumber(1);
			}
			exceptionParameters.setKey(exceptionParametersKey);

			exceptionParametersKey.setExceptionNumber(discontinueRules.getExceptionNumber());
			exceptionParameters.setPriority(discontinueRules.getPriorityNumber());
			exceptionParameters.setExceptionType(discontinueRules.getExceptionType());
			exceptionParameters.setExceptionTypeId(discontinueRules.getExceptionTypeId());
			exceptionParameters.setNeverDelete(discontinueRules.isNeverDiscontinueSwitch());

			DiscontinueParameterCommonAttributes attributes = exceptionParameters.getAttributes();
				if(DiscontinueParameterType.STORE_SALES == discontinueParameterType) {
					attributes.setParameterValue(stringValueToDays(discontinueRules.getStoreSales()));
					attributes.setActive(discontinueRules.isSalesSwitch());
				} else if(DiscontinueParameterType.NEW_ITEM_PERIOD == discontinueParameterType) {
					attributes.setParameterValue(stringValueToDays(discontinueRules.getNewItemPeriod()));
					attributes.setActive(discontinueRules.isNewProductSetupSwitch());
				} else if(DiscontinueParameterType.WAREHOUSE_UNITS == discontinueParameterType) {
					attributes.setParameterValue(discontinueRules.getWarehouseUnits());
					attributes.setActive(discontinueRules.isWarehouseUnitSwitch());
				} else if(DiscontinueParameterType.STORE_UNITS == discontinueParameterType) {
					attributes.setParameterValue(discontinueRules.getStoreUnits());
					attributes.setActive(discontinueRules.isStoreUnitSwitch());
				} else if(DiscontinueParameterType.STORE_RECEIPTS == discontinueParameterType) {
					attributes.setParameterValue(stringValueToDays(discontinueRules.getStoreReceipts()));
					attributes.setActive(discontinueRules.isReceiptsSwitch());
				} else if(DiscontinueParameterType.PURCHASE_ORDERS == discontinueParameterType) {
					attributes.setParameterValue(stringValueToDays(discontinueRules.getPurchaseOrders()));
					attributes.setActive(discontinueRules.isPurchaseOrderSwitch());
				}
			returnList.add(exceptionParameters);
		}
		return returnList;
	}

	/**
	 * Used to convert a string integer value from months to number of days.
	 * @param value the string int value
	 * @return String int value in days
	 */
	private String stringValueToDays(String value){
		try{
			return Integer.toString(Integer.parseInt(value.trim()) * 30);
		}catch (NumberFormatException e){
			throw new IllegalArgumentException("Parameter cannot be null, empty, or parse to NAN.");
		}
	}

	/**
	 *  Extracts the audit information from the DiscontinueExceptionParameters entity and saves it into the audit table.
	 * @param discontinueExceptionParametersList The list of exception parameters to be saved to the audit table.
	 * @param actionCode The action code reflecting the change made to exception parameters (ADD/MOD/DEL).
	 */
	private List<DiscontinueExceptionParametersAudit> createExceptionParametersAudits(List<DiscontinueExceptionParameters> discontinueExceptionParametersList, String actionCode) {
		List<DiscontinueExceptionParametersAudit> exceptionParametersAuditList = new ArrayList<>();
		DiscontinueExceptionParametersAudit exceptionParametersAudit = null;
		LocalDateTime timestamp= null;
		for (DiscontinueExceptionParameters aDiscontinueExceptionParametersList : discontinueExceptionParametersList) {

			// Time is the only key in the Audit table, so to ensure that each time is different we added one microsecond
			// to each TS.
			timestamp = LocalDateTime.now();
			exceptionParametersAudit = new DiscontinueExceptionParametersAudit();
			exceptionParametersAudit.setId(aDiscontinueExceptionParametersList.getKey().getId());
			exceptionParametersAudit.setSequenceNumber(aDiscontinueExceptionParametersList.getKey().getSequenceNumber());
			exceptionParametersAudit.setParameterValue(aDiscontinueExceptionParametersList.getParameterValue());
			exceptionParametersAudit.setPriority(aDiscontinueExceptionParametersList.getPriority());
			exceptionParametersAudit.setActive(aDiscontinueExceptionParametersList.isActive());
			exceptionParametersAudit.setAction(actionCode);
			exceptionParametersAudit.setUserId("v357407");
			exceptionParametersAudit.setExceptionNumber(aDiscontinueExceptionParametersList.getKey().getExceptionNumber());
			exceptionParametersAudit.setExceptionType(aDiscontinueExceptionParametersList.getExceptionType());
			exceptionParametersAudit.setExceptionTypeId(aDiscontinueExceptionParametersList.getExceptionTypeId());
			exceptionParametersAudit.setNeverDelete(aDiscontinueExceptionParametersList.isNeverDelete());
			exceptionParametersAudit.setTimestamp(timestamp);
			exceptionParametersAuditList.add(exceptionParametersAudit);
		}
		return exceptionParametersAuditList;
	}

	/**
	 * Return a upc service that didn't find a upc, so it will return null to the caller.
	 *
	 * @return A upc service with null as it's return.
	 */
	private UpcService getUpcServiceNoUpcFound(){
		UpcService service = Mockito.mock(UpcService.class);
		Mockito.when(service.find(Mockito.anyLong())).thenReturn(null);
		return service;
	}

	/**
	 * Return a DiscontinueParametersToDiscontinueRules with vendor rule.
	 *
	 * @return A DiscontinueParametersToDiscontinueRules with vendor rule.
	 */
	private DiscontinueParametersToDiscontinueRules getVendorConverter(){
		DiscontinueParametersToDiscontinueRules converter = Mockito.mock(DiscontinueParametersToDiscontinueRules.class);
		Mockito.when(converter.toDiscontinueRulesFromExceptions(this.getVendorExceptionParametersList())).thenReturn(this.getVendorRulesObject());
		return converter;
	}

	/**
	 * Return a DiscontinueExceptionParametersRepository with no records found.
	 *
	 * @return A DiscontinueExceptionParametersRepository with no records found.
	 */
	private DiscontinueExceptionParametersRepository getDefaultDiscontinueExceptionParametersRepositoryNoRecordsFound(){
		DiscontinueExceptionParametersRepository repository = Mockito.mock(DiscontinueExceptionParametersRepository.class);
		Mockito.when(repository.findByExceptionTypeAndExceptionTypeId(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<>());
		return repository;
	}

	/**
	 * Return a DiscontinueParametersToDiscontinueRules with a record that already exists.
	 *
	 * @return A DiscontinueParametersToDiscontinueRules with a record that already exists.
	 */
	private DiscontinueExceptionParametersRepository getDiscontinueExceptionParametersRepositoryReturnAlreadyExists(){
		DiscontinueExceptionParametersRepository repository = Mockito.mock(DiscontinueExceptionParametersRepository.class);
		Mockito.when(repository.findByExceptionTypeAndExceptionTypeId(Mockito.anyString(), Mockito.anyString())).thenReturn(this.getVendorExceptionParametersList());
		return repository;
	}

	/**
	 * Deep comparison of two DiscontinueRules.
	 *
	 * @param a First DiscontinueRules to compare.
	 * @param b Second DiscontinueRules to compare.
	 */
	private void fullItemCompare(DiscontinueRules a, DiscontinueRules b) {
		Assert.assertEquals(a.getExceptionType(), b.getExceptionType());
		Assert.assertEquals(a.getExceptionName(), b.getExceptionName());
		Assert.assertEquals(a.getExceptionNumber(), b.getExceptionNumber());
		Assert.assertEquals(a.getExceptionTypeId(), b.getExceptionTypeId());
		Assert.assertEquals(a.getNewItemPeriod(), b.getNewItemPeriod());
		Assert.assertEquals(a.getPriorityNumber(), b.getPriorityNumber());
		Assert.assertEquals(a.getPurchaseOrders(), b.getPurchaseOrders());
		Assert.assertEquals(a.getStoreReceipts(), b.getStoreReceipts());
		Assert.assertEquals(a.getStoreSales(), b.getStoreSales());
		Assert.assertEquals(a.getStoreUnits(), b.getStoreUnits());
		Assert.assertEquals(a.getWarehouseUnits(), b.getWarehouseUnits());
		Assert.assertEquals(a.isNeverDiscontinueSwitch(), b.isNeverDiscontinueSwitch());
		Assert.assertEquals(a.isNewProductSetupSwitch(), b.isNewProductSetupSwitch());
		Assert.assertEquals(a.isPurchaseOrderSwitch(), b.isPurchaseOrderSwitch());
		Assert.assertEquals(a.isReceiptsSwitch(), b.isReceiptsSwitch());
		Assert.assertEquals(a.isSalesSwitch(), b.isSalesSwitch());
		Assert.assertEquals(a.isStoreUnitSwitch(), b.isStoreUnitSwitch());
		Assert.assertEquals(a.isWarehouseUnitSwitch(), b.isWarehouseUnitSwitch());
		Assert.assertEquals(a, b);
	}
}
