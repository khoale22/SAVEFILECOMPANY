/*
 * ScaleManagementControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.entity.AssociatedUpc;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.PrimaryUpc;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import testSupport.CommonMocks;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for ScaleManagementControllerTest.
 *
 * @author l730832
 * @since 2.2.0
 */
public class ScaleManagementControllerTest {

	private static final String testString = "Test";
	private static final long testLong = 0;
	private static final int testInt = 0;
	private static final LocalDate testLocalDate = LocalDate.now();
	private static final double testDouble = 0.0;
	private static final boolean testBoolean = false;

	@InjectMocks
	private ScaleManagementController scaleManagementController;

	@Mock
	private ScaleManagementService service;

	@Mock
	private UserInfo userInfo;

	@Mock
	private MessageSource messageSource;

	@Mock
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test find all by plus.
	 */
	@Test
	public void testFindAllByPlus() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());

		List<Long> pluList = new ArrayList<>();
		pluList.add(testLong);

		Mockito.when(this.service.findByPlu(Mockito.anyListOf(Long.class), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleUpc> returnResult = this.scaleManagementController.findAllByPlus(pluList, false, testInt, CommonMocks.getServletRequest());
		Assert.assertEquals(pageableResult, returnResult);
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find all by description.
	 */
	@Test
	public void testFindAllByDescription() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());

		Mockito.when(this.service.findByDescription(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleUpc> returnResult = this.scaleManagementController.findAllByDescription(testString, true, testInt, CommonMocks.getServletRequest());
		Assert.assertEquals(pageableResult, returnResult);
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find hits by plu list.
	 */
	@Test
	public void testFindHitsByPluList() {
		List<Long> pluList = new ArrayList<>();
		pluList.add(testLong);
		Hits hits = new Hits(testInt, testInt, pluList);

		Mockito.when(this.service.findHitsByPluList(Mockito.anyListOf(Long.class))).thenReturn(hits);
		Hits returnHits = this.scaleManagementController.findHitsByPluList(pluList, CommonMocks.getServletRequest());
		Assert.assertEquals(hits, returnHits);
	}

	/**
	 * Test update scale upc.
	 */
	@Test
	public void testUpdateScaleUpc() {
		ModifiedEntity<ScaleUpc> modifiedEntity = new ModifiedEntity<>(this.getDefaultScaleUpcList().get(0), testString);

		Mockito.when(this.service.update(Mockito.any(ScaleUpc.class))).thenReturn(this.getDefaultScaleUpcList().get(0));
		ModifiedEntity<ScaleUpc> returnEntity = this.scaleManagementController.updateScaleUpc(this.getDefaultScaleUpcList().get(0),CommonMocks.getServletRequest());
		Assert.assertEquals(modifiedEntity.getData(), returnEntity.getData());
	}

	/**
	 * Test bulk update scale upc.
	 */
	@Test
	public void testBulkUpdateScaleUpc() {
		ModifiedEntity<List<ScaleUpc>> modifiedEntity = new ModifiedEntity<>(this.getDefaultScaleUpcList(), testString);
		ScaleManagementBulkUpdate scaleManagementBulkUpdate = new ScaleManagementBulkUpdate();
		scaleManagementBulkUpdate.setAttribute(ScaleManagementBulkUpdate.BulkUpdateAttribute.ACTION_CODE);
		scaleManagementBulkUpdate.setScaleUpcs(this.getDefaultScaleUpcList());

		Mockito.when(this.service.bulkUpdate(Mockito.any(ScaleManagementBulkUpdate.class))).thenReturn(this.getDefaultScaleUpcList());
		ModifiedEntity<List<ScaleUpc>> returnEntity = this.scaleManagementController.bulkUpdateScaleUpc(scaleManagementBulkUpdate, CommonMocks.getServletRequest());
		Assert.assertEquals(modifiedEntity.getData(), returnEntity.getData());
	}

	/**
	 * Test find by plus export.
	 */
	@Test
	public void testFindByPlusExport(){
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());
		List<Long> scaleUpcs = new ArrayList<>();
		scaleUpcs.add(this.getDefaultScaleUpcList().get(0).getPlu());

		HttpServletResponse response = CommonMocks.getServletResponse();
		try {
			Mockito.when(response.getOutputStream()).thenReturn(Mockito.mock(ServletOutputStream.class));
			Mockito.when(this.service.findByPlu(Mockito.anyListOf(Long.class), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(pageableResult);

		this.scaleManagementController.findByPlusExport(scaleUpcs, 3, testString, CommonMocks.getServletRequest(), response);
	}

	/**
	 * Test find by plus export with exception.
	 */
	@Test(expected = StreamingExportException.class)
	public void testFindByPlusExportWithException(){
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());
		List<Long> scaleUpcs = new ArrayList<>();
		scaleUpcs.add(this.getDefaultScaleUpcList().get(0).getPlu());

		HttpServletResponse response = CommonMocks.getServletResponse();
		try {
			Mockito.doThrow(IOException.class).when(response).getOutputStream();
			Mockito.when(this.service.findByPlu(Mockito.anyListOf(Long.class), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.scaleManagementController.findByPlusExport(scaleUpcs, 3, testString, CommonMocks.getServletRequest(), response);
	}

	/**
	 * Test find by description export.
	 */
	@Test
	public void testFindByDescriptionExport(){
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());

		HttpServletResponse response = CommonMocks.getServletResponse();
		try {
			Mockito.when(response.getOutputStream()).thenReturn(Mockito.mock(ServletOutputStream.class));
			Mockito.when(this.service.findByDescription(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(pageableResult);

		this.scaleManagementController.findByDescriptionExport(testString, 3, testString, CommonMocks.getServletRequest(), response);
	}

	/**
	 * Test find by description export with exception.
	 */
	@Test(expected = StreamingExportException.class)
	public void testFindByDescriptionExportWithException(){
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());

		HttpServletResponse response = CommonMocks.getServletResponse();
		try {
			Mockito.doThrow(IOException.class).when(response).getOutputStream();
			Mockito.when(this.service.findByDescription(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.scaleManagementController.findByDescriptionExport(testString, 3, testString, CommonMocks.getServletRequest(), response);
	}

	/**
	 * Creates a list of scale upcs.
	 * @return the list of scale upcs.
	 */
	private List<ScaleUpc> getDefaultScaleUpcList() {
		List<ScaleUpc> scaleUpcList = new ArrayList<>();
		ScaleUpc scaleUpc = new ScaleUpc();
		AssociatedUpc associatedUpc = new AssociatedUpc();
		PrimaryUpc primaryUpc = new PrimaryUpc();
		SubDepartment subDepartment = new SubDepartment();
		SubDepartmentKey subDepartmentKey = new SubDepartmentKey();
		SellingUnit sellingUnit = new SellingUnit();
		ProductMaster productMaster = new ProductMaster();

		subDepartmentKey.setDepartment(testString);
		subDepartmentKey.setSubDepartment(testString);
		primaryUpc.setItemMasters(this.getDefaultItemMaster());
		associatedUpc.setPrimaryUpc(primaryUpc);
		subDepartment.setKey(subDepartmentKey);
		subDepartment.setName(testString);
		productMaster.setSubDepartment(subDepartment);
		sellingUnit.setProductMaster(productMaster);
		associatedUpc.setSellingUnit(sellingUnit);

		scaleUpc.setShelfLifeDays(testInt);
		scaleUpc.setFreezeByDays(testInt);
		scaleUpc.setServiceCounterTare(testDouble);
		scaleUpc.setIngredientStatement(testLong);
		scaleUpc.setNutrientStatement(testLong);
		scaleUpc.setLabelFormatOne(testLong);
		scaleUpc.setLabelFormatTwo(testLong);
		scaleUpc.setEnglishDescriptionOne(testString);
		scaleUpc.setEnglishDescriptionTwo(testString);
		scaleUpc.setEnglishDescriptionThree(testString);
		scaleUpc.setEnglishDescriptionFour(testString);
		scaleUpc.setSpanishDescriptionOne(testString);
		scaleUpc.setSpanishDescriptionTwo(testString);
		scaleUpc.setSpanishDescriptionThree(testString);
		scaleUpc.setSpanishDescriptionFour(testString);
		scaleUpc.setEffectiveDate(testLocalDate);
		scaleUpc.setEatByDays(testInt);
		scaleUpc.setPrePackTare(testDouble);
		scaleUpc.setActionCode(testLong);
		scaleUpc.setGraphicsCode(testInt);
		scaleUpc.setForceTare(testBoolean);
		scaleUpc.setPriceOverride(testBoolean);
		scaleUpc.setNetWeight(testDouble);
		scaleUpc.setAssociateUpc(associatedUpc);
		scaleUpcList.add(scaleUpc);
		return scaleUpcList;
	}

	/**
	 * Creates an item master list.
	 * @return a list of item masters.
	 */
	private List<ItemMaster> getDefaultItemMaster() {
		List<ItemMaster> itemMasterList = new ArrayList<>();
		ItemMaster itemMaster = new ItemMaster();
		itemMasterList.add(itemMaster);
		return itemMasterList;
	}
}