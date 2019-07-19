/*
 * StoreServiceClientTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.store;

import com.heb.xmlns.ei.authentication.Authentication;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests StoreServiceClient.
 *
 * @author d116773
 * @since 2.0.1
 */
public class StoreServiceClientTest {

	/*
	 * getAuthentication
	 */

	/**
	 * Tests getAuthentication.
	 */
	@Test
	public void getAuthentication() {
		StoreServiceClient storeServiceClient = new StoreServiceClient();
		Authentication authentication = storeServiceClient.getAuthentication();
		Assert.assertNotNull(authentication.getUSERID());
		Assert.assertNotNull(authentication.getUSERID());
		Assert.assertNotNull(authentication.getPWD());
	}

	/*
	 * getUri
	 */

	/**
	 * Tests getUri.
	 */
	@Test
	public void getWebServiceUri() {
		StoreServiceClient storeServiceClient = new StoreServiceClient();
		storeServiceClient.setUri("test string");
		Assert.assertEquals("test string", storeServiceClient.getWebServiceUri());
	}
}
