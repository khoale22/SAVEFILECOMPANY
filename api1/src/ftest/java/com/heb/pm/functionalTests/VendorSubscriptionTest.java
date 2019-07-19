package com.heb.pm.functionalTests;

import com.heb.gdsn.VendorSubscription;
import com.heb.gdsn.VendorSubscriptionController;
import com.heb.util.jpa.PageableResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import testSupport.CommonMocks;

/**
 * Tests stuff related to GDSN Vendor Subscriptions
 * @author d116773
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = com.heb.pm.ApplicationConfiguration.class)
@ActiveProfiles("functional-test")
public class VendorSubscriptionTest {

	private static final Logger logger = LoggerFactory.getLogger(VendorSubscriptionTest.class);

	@Autowired
	private VendorSubscriptionController vendorSubscriptionController;

	@Test
	public void testFindAll() {
		PageableResult<VendorSubscription> vendorSubscriptions =
				this.vendorSubscriptionController.findAll(0, 50, true, CommonMocks.getServletRequest());

		for (VendorSubscription vs : vendorSubscriptions.getData()) {
			logger.info(vs.toString());
		}

	}
}
