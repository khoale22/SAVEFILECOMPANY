/*
 *  ProductGroupControllerTest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.entity.CustomerProductGroupMembership;
import com.heb.util.controller.UserInfo;
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
 * This is the Test class for product group controller.
 *
 * @author l730832
 * @since 2.14.0
 */
public class ProductGroupControllerTest {

	private Long testLong = 1L;

	@InjectMocks
	private ProductGroupController controller;

	@Mock
	private ProductGroupService service;

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
	 * Tests the method of get product groups.
	 */
	@Test
	public void testGetProductGroups() {
		CustomerProductGroupMembership customerProductGroupMembership = new CustomerProductGroupMembership();
		List<CustomerProductGroupMembership> customerProductGroupMemberships = new ArrayList<>();
		customerProductGroupMemberships.add(customerProductGroupMembership);
		Mockito.when(this.service.getProductGroupList(Mockito.anyLong())).thenReturn(customerProductGroupMemberships);
		List<CustomerProductGroupMembership> result = this.controller.getProductGroups(testLong, CommonMocks.getServletRequest());
		Assert.assertEquals(result, customerProductGroupMemberships);
	}

}