/*
 * PssDepartmentService
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.PssDepartmentCode;
import com.heb.pm.entity.PssDepartmentCodeKey;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.MerchandiseType;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
import com.heb.pm.repository.PssDepartmentCodeRepository;
import com.heb.pm.repository.SubDepartmentRepository;
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
 * The test class for the PssDepartmentService.
 *
 * @author l730832
 * @since 2.8.0
 */
public class PssDepartmentServiceTest {

	private static final String testString = "Test";
	private static final Integer testInteger = 1;

	@InjectMocks
	private PssDepartmentService pssDepartmentService;

	@Mock
	private SubDepartmentRepository subDepartmentRepository;

	@Mock
	private PssDepartmentCodeRepository pssDepartmentCodeRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Returns a default item master to use for testing.
	 *
	 * @return Item Master
	 */
	private ItemMaster getDefaultItemMaster(){
		ItemMaster im = new ItemMaster();
		ItemMasterKey key = new ItemMasterKey();
		key.setItemType("test1");
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
		pm.setSubDepartment(this.getDefaultSubDepartment());
		return pm;
	}
}