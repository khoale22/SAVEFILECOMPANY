/*
 * IngredientSearchTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.functionalTests;

import com.heb.pm.entity.DynamicAttribute;
import com.heb.pm.reports.IngredientsReportService;
import com.heb.util.jpa.PageableResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author d116773
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = com.heb.pm.ApplicationConfiguration.class)
@ActiveProfiles("functional-test")
public class IngredientSearchTest {

	@Autowired
	private IngredientsReportService ingredientsReportService;

	@Test
	public void testSearchForIngredients() {
		PageableResult<DynamicAttribute> pr =
				this.ingredientsReportService.getIngredientsReport("onion", false, 1, 10);
		pr.getData().forEach(System.out::println);
	}
}
