/*
 * ClassCommodity
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.functionalTests;

import com.heb.pm.productHierarchy.ClassCommodityController;
import com.heb.util.jpa.PageableResult;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import testSupport.CommonMocks;

/**
 * Tests functionality exposed by the ClassCommodityController. The tests here require that the class/commodity
 * index is generated already. This can be done with the ProductionSupport class in this package.
 *
 * @author d116773
 * @since 2.0.3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = com.heb.pm.ApplicationConfiguration.class)
@ActiveProfiles("functional-test")
public class ClassCommodity {

	@Autowired
	private ClassCommodityController controller;


	@Test
	public void findCommoditiesByRegularExpression() {
		PageableResult<com.heb.pm.entity.ClassCommodity> classCommodities =
				this.controller.findCommoditiesByRegularExpression("CLE", 0, 20,
						CommonMocks.getServletRequest());

		Assert.assertEquals(Long.valueOf(8), classCommodities.getRecordCount());
	}
}
