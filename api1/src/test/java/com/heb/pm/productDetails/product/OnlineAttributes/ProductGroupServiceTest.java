/*
 *  ProductGroupsServiceTest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.entity.CustomerProductGroup;
import com.heb.pm.entity.CustomerProductGroupMembership;
import com.heb.pm.entity.CustomerProductGroupMembershipKey;
import com.heb.pm.entity.ProductGroupType;
import com.heb.pm.repository.CustomerProductGroupMembershipRepository;
import junit.framework.TestCase;
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
 * This is the Test class for product group service.
 *
 * @author l730832
 * @since 2.14.0
 */
public class ProductGroupServiceTest {

	private Long testLong = 1L;
	private String testString = "test";

	@InjectMocks
	private ProductGroupService service;

	@Mock
	private CustomerProductGroupMembershipRepository repository;


	/**
	 * Sets up the mockito annotations.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testGetProductGroupList() {
		CustomerProductGroupMembership customerProductGroupMembership = this.getDefaultCustomerProductGroupMembership();
		List<CustomerProductGroupMembership> customerProductGroupMemberships = new ArrayList<>();
		customerProductGroupMemberships.add(customerProductGroupMembership);
		Mockito.when(this.repository.findByKeyProdId(Mockito.anyLong())).thenReturn(customerProductGroupMemberships);
		List<CustomerProductGroupMembership> result = this.service.getProductGroupList(testLong);
		Assert.assertEquals(result, customerProductGroupMemberships);
	}

	/**
	 * Returns a default customer product group membership.
	 * @return CustomerProductGroupMembership
	 */
	private CustomerProductGroupMembership getDefaultCustomerProductGroupMembership() {
		CustomerProductGroup customerProductGroup = new CustomerProductGroup();
		ProductGroupType productGroupType = new ProductGroupType();
		productGroupType.setDepartmentNumberString(testString);
		productGroupType.setProductGroupType(testString);
		productGroupType.setProductGroupTypeCode(testString);
		productGroupType.setSubDepartmentId(testString);
		customerProductGroup.setCustProductGroupDescription(testString);
		customerProductGroup.setCustProductGroupId(testLong);
		customerProductGroup.setCustProductGroupName(testString);
		customerProductGroup.setProductGroupTypeCode(testString);
		customerProductGroup.setProductGroupType(productGroupType);
		CustomerProductGroupMembership customerProductGroupMembership = new CustomerProductGroupMembership();
		customerProductGroupMembership.setCustomerProductGroup(customerProductGroup);
		CustomerProductGroupMembershipKey key = new CustomerProductGroupMembershipKey();
		key.setCustomerProductGroupId(testLong);
		key.setProdId(testLong);
		customerProductGroupMembership.setKey(key);
		return customerProductGroupMembership;
	}
}