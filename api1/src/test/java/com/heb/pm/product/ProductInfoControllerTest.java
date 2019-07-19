/*
 *
 * ProductInfoControllerTest
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 *
 */

package com.heb.pm.product;

import com.heb.pm.entity.ProductMaster;
import com.heb.util.controller.NonEmptyParameterValidator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import testSupport.CommonMocks;

import java.util.ArrayList;


/**
 * Test class for ProductInfoController
 *
 * @author s573181
 * @since 2.0.1
 */


public class ProductInfoControllerTest {

	/**
	 * Tests getProduct when passed a good value.
	 */
	@Test
	public void getProductGoodValues() {

		ProductInfoController controller = new ProductInfoController();
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setParameterValidator(new NonEmptyParameterValidator());
		controller.setProductInfoService(this.getProductInfoService(127127L));
		controller.findProductByProductId(127127L, CommonMocks.getServletRequest());
		// Tests are done in the answer below.
	}

	/**
	 * Tests getProduct when passed a null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getProductNull() {
		ProductInfoController controller = new ProductInfoController();
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setParameterValidator(new NonEmptyParameterValidator());
		controller.setProductInfoService(this.getProductInfoService(100L));
		controller.findProductByProductId(null, CommonMocks.getServletRequest());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a ProductMaster to test with.
	 *
	 * @return A ProductMaster to test with.
	 */
	private ProductMaster getTestProductMaster() {

		ProductMaster pm = new ProductMaster();
		pm.setProdItems(new ArrayList<>());
		pm.setSellingUnits(new ArrayList<>());
		return pm;
	}

	/**
	 * Mocks up a call to findProductInfoByProdId. It will make sure a particular product ID is passed in.
	 *
	 * @param productId The product ID to look for.
	 * @return The mocked call to findProductInfoByProdId.
	 */
	private Answer<ProductMaster> getFindProductInfoByProdIdAnswer(long productId) {
		return invocation -> {
			Assert.assertEquals(productId, invocation.getArguments()[0]);
			return this.getTestProductMaster();
		};
	}

	/**
	 * Returns a ProductInfoService for testing.
	 *
	 * @param productId The product ID for the service to expect.
	 * @return A ProductInfoService for testing.
	 */
	private ProductInfoService getProductInfoService(long productId) {

		ProductInfoService service = Mockito.mock(ProductInfoService.class);
		Mockito.doAnswer(this.getFindProductInfoByProdIdAnswer(productId))
				.when(service).findProductInfoByProdId(Mockito.anyLong());
		return service;
	}
}
