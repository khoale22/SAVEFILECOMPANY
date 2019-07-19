/*
 *  CodeDateServiceTest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

/**
 * Created by l730832 on 9/14/2017.
 */
public class CodeDateServiceTest {

	@InjectMocks
	private CodeDateService service;

	@Mock
	private ProductInfoRepository repository;

	@Mock
	private ProductManagementServiceClient productManagementServiceClient;

	@Mock
	private ProductInfoResolver resolver;

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
		Mockito.when(this.repository.findOne(Mockito.anyLong())).thenReturn(productMaster);
		ProductMaster result = this.service.saveCodeDateChanges(productMaster);
		Assert.assertEquals(result, productMaster);
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