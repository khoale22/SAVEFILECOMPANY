/*
 * PssDepartmentController
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

/**
 * The test class for the PssDepartmentController.
 *
 * @author l730832
 * @since 2.8.0
 */
public class PssDepartmentControllerTest {

	private static final String testString = "Test";
	private static final String testString1 = "1";
	private static final Integer testInteger = 1;

	@InjectMocks
	private PssDepartmentController pssDepartmentController;

	@Mock
	private PssDepartmentService pssDepartmentService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindItemMaster(){
		ItemMaster itemMaster = null;
		Mockito.when(this.pssDepartmentService.findItemMaster(testString1, testString)).thenReturn(itemMaster);
		ItemMaster result = this.pssDepartmentController.findItemMaster(testString1, testString, CommonMocks.getServletRequest());
		Assert.assertEquals(itemMaster, result);
	}

	@Test
	public void testFindAllDepartment(){
		List<SubDepartment> subDepartments = new ArrayList<>();
		Mockito.when(this.pssDepartmentService.findAllDepartment()).thenReturn(subDepartments);
		List<SubDepartment> result = this.pssDepartmentController.findAllDepartment(CommonMocks.getServletRequest());
		Assert.assertEquals(subDepartments, result);
	}

	@Test
	public void testFindAllMerchandiseType(){
		List<MerchandiseType> merchandiseTypes = new ArrayList<>();
		Mockito.when(this.pssDepartmentService.findAllMerchandiseType()).thenReturn(merchandiseTypes);
		List<MerchandiseType> result = this.pssDepartmentController.findAllMerchandiseType(CommonMocks.getServletRequest());
		Assert.assertEquals(merchandiseTypes, result);
	}

	@Test
	public void testfindPssDepartmentCodeByPssDepartmentId(){
		PssDepartmentCode pssDepartmentCode = null;
		Mockito.when(this.pssDepartmentService.findPssDepartmentCodeByPssDepartmentId(testInteger)).thenReturn(pssDepartmentCode);
		PssDepartmentCode result = this.pssDepartmentController.findPssDepartmentCodeByPssDepartmentId(testInteger,
				CommonMocks.getServletRequest());
		Assert.assertEquals(pssDepartmentCode, result);
	}

	/**
	 * Returns a default item master to use for testing.
	 *
	 * @return Item Master
	 */
	private ItemMaster getDefaultItemMaster(){
		ItemMaster im = new ItemMaster();
		ItemMasterKey key = new ItemMasterKey();
		key.setItemType(testString);
		key.setItemCode(1L);
		im.setKey(key);
		return im;
	}

	/**
	 * Returns a default Pss Department.
	 *
	 * @return Pss Department
	 */
	private PssDepartment getDefaultPssDepartment() {
		PssDepartment pssDepartment = new PssDepartment();
		pssDepartment.setMerchandiseType(this.getDefaultMerchandiseType());
		pssDepartment.setPssDepartmentCode(this.getDefaultControlTable());
		pssDepartment.setSubDepartment(this.getDefaultSubDepartment());
		return pssDepartment;
	}

	/**
	 * Returns a merchandise type.
	 *
	 * @return a default merchandise type.
	 */
	private MerchandiseType getDefaultMerchandiseType() {
		MerchandiseType merchandiseType = new MerchandiseType();
		merchandiseType.setId(testString);
		merchandiseType.setDescription(testString);
		merchandiseType.setAbbreviation(testString);
		return merchandiseType;
	}

	/**
	 * Returns a control table.
	 *
	 * @return a default control table.
	 */
	private PssDepartmentCode getDefaultControlTable() {
		PssDepartmentCode pssDepartmentCode = new PssDepartmentCode();
		PssDepartmentCodeKey pssDepartmentCodeKey = new PssDepartmentCodeKey();
		pssDepartmentCodeKey.setId(testInteger);
		pssDepartmentCode.setKey(pssDepartmentCodeKey);

		return pssDepartmentCode;
	}

	/**
	 * Returns a sub department.
	 *
	 * @return a default sub department.
	 */
	private SubDepartment getDefaultSubDepartment() {

		SubDepartment subDepartment = new SubDepartment();
		SubDepartmentKey subDepartmentKey = new SubDepartmentKey();

		subDepartmentKey.setSubDepartment(testString);
		subDepartmentKey.setDepartment(testString);
		subDepartment.setKey(subDepartmentKey);

		subDepartment.setName(testString);
		return subDepartment;
	}

	/**
	 * Returns a ProductMaster to test with.
	 *
	 * @return A ProductMaster to test with.
	 */
	private ProductMaster getDefaultProductMaster() {

		ProductMaster pm = new ProductMaster();
		pm.setProdItems(new ArrayList<>());
		pm.setSellingUnits(new ArrayList<>());
		return pm;
	}
}