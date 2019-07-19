/*
 *  CodeDateControllerTest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.entity.ProductMaster;
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

/**
 * The unit tests for the code date controller test.
 *
 * @author l730832
 * @since 2.12.0
 */
public class CodeDateControllerTest  {

	@InjectMocks
	private CodeDateController codeDateController;

	@Mock
	private CodeDateService service;

	@Mock
	private MessageSource messageSource;

	@Mock
	private UserInfo userInfo;

	/**
	 * Sets up the mockito annotations.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests the save code date changes.
	 */
	@Test
	public void testSaveCodeDateChanges() {
		ProductMaster productMaster = this.getDefaultProductMaster();
		Mockito.when(this.service.saveCodeDateChanges(Mockito.any(ProductMaster.class))).thenReturn(productMaster);
		ModifiedEntity<ProductMaster> result = this.codeDateController.saveCodeDateChanges(productMaster,
				CommonMocks.getServletRequest());
		Assert.assertEquals(result.getData(), productMaster);
	}

	/**
	 * Creates a defualt product master used for testing.

	 * @return product Master
	 */
	private ProductMaster getDefaultProductMaster() {
		ProductMaster pm = new ProductMaster();
		pm.setProdItems(new ArrayList<>());
		pm.setSellingUnits(new ArrayList<>());
		return pm;
	}

}