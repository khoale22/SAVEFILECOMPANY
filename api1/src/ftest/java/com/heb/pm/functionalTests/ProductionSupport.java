/*
 * ProductionSupport
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.functionalTests;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.Vendor;
import com.heb.pm.productionSupport.ProductionSupportController;
import com.heb.pm.productHierarchy.SubDepartmentService;
import com.heb.pm.vendor.VendorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import testSupport.CommonMocks;


/**
 * Tests the functions exposed by ProductionSupportController.
 *
 * @author d116773
 * @since 2.0.3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = com.heb.pm.ApplicationConfiguration.class)
@ActiveProfiles("functional-test")
public class ProductionSupport {

	@Autowired
	private ProductionSupportController controller;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private SubDepartmentService subDepartmentService;

	/**
	 * Test the call to load the vendor index.
	 */
	@Test
	public void loadVendorIndex() {
		// Run the job to load the index.
		ProductionSupportController.BatchStatusWrapper status =
				this.controller.runJob("I18X901D", CommonMocks.getServletRequest());
		Assert.assertEquals(BatchStatus.COMPLETED.toString(), status.getStatus());

		// Pull a vendor to make sure data was loaded.
		Vendor v = this.vendorService.findByVendorNumber(50022);
		Assert.assertNotNull(v);
		Assert.assertEquals("HEB MEAT PROCESSING CENTER    ", v.getVendorName());
	}

	/**
	 * Tests the call to load the sub-department index.
	 */
	@Test
	public void loadSubDepartmentIndex() {
		ProductionSupportController.BatchStatusWrapper status =
				this.controller.runJob("I18X902D", CommonMocks.getServletRequest());
		Assert.assertEquals(BatchStatus.COMPLETED.toString(), status.getStatus());

		// Pull a sub-department to make sure data was loaded.
		SubDepartment sd = this.subDepartmentService.findSubDepartment("07A");
		Assert.assertNotNull(sd);
		Assert.assertEquals("DRY GROCERY - 7A              ", sd.getName());
	}

	@Test
	public void loadClassCommodityIndex() {
		ProductionSupportController.BatchStatusWrapper status =
				this.controller.runJob("I18X903D", CommonMocks.getServletRequest());
		Assert.assertEquals(BatchStatus.COMPLETED.toString(), status.getStatus());
	}
}
