/*
 * VendorServiceClientTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.vendor;

import com.heb.xmlns.ei.authentication.Authentication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests VendorServiceClient.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorServiceClientTest {

	/**
	 * Tests getAuthentication.
	 */
	@Test
	public void getAuthentication() {
		VendorServiceClient vendorServiceClient = new VendorServiceClient();
		Authentication authentication = vendorServiceClient.getAuthentication();
		Assert.assertNotNull(authentication.getUSERID());
		Assert.assertNotNull(authentication.getUSERID());
		Assert.assertNotNull(authentication.getPWD());
	}

	/**
	 * Tests getWebServiceUri.
	 */
	@Test
	public void getWebServiceUri() {
		VendorServiceClient vendorServiceClient = new VendorServiceClient();
		vendorServiceClient.setUri("test string");
		Assert.assertEquals("test string", vendorServiceClient.getWebServiceUri());
	}

	/**
	 * Tests getServiceAgent.
	 */
	@Test
	public void getServiceAgent() {
		VendorServiceClient vendorServiceClient = new VendorServiceClient();
		vendorServiceClient.setUri("http://drdapl0046702.heb.com:9574/VendorService");
		Assert.assertNotNull(vendorServiceClient.getServiceAgent());
	}

	/**
	 * Tests getPort.
	 */
	@Test
	public void getPort() {
		VendorServiceClient vendorServiceClient = new VendorServiceClient();
		vendorServiceClient.setUri("http://drdapl0046702.heb.com:9574/VendorService");
		Assert.assertNotNull(vendorServiceClient.getPort());
	}
}
