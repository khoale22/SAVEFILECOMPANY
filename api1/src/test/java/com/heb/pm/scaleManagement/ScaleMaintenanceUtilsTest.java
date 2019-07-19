/*
 *
 * NutrientsServiceTest.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.scaleManagement;

import com.heb.scaleMaintenance.utils.ScaleMaintenanceUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 • This class is used to test ScaleMaintenanceUtils.
 *
 • @author m314029
 * @since 2.17.8

 */
public class ScaleMaintenanceUtilsTest {

	@InjectMocks
	private ScaleMaintenanceUtils scaleMaintenanceUtils;

	@Test
	public void testMethod(){
//		List<Long> upcsToTest = new ArrayList<>();
//		upcsToTest.add(20260600000L);
//		upcsToTest.add(20300000000L);
//		List<ScaleMaintenanceProduct> testReturn =
//				scaleMaintenanceUtils.convertUpcsToScaleMaintenanceProduct(upcsToTest);
//		System.out.println(testReturn);
	}

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
}
