/*
 * LabelFormatServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.functionalTests;

import com.heb.pm.entity.ScaleLabelFormat;
import com.heb.pm.scaleManagement.ScaleLabelFormatService;
import com.heb.util.jpa.PageableResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

/**
 * Functional tests for LabelFormatService.
 *
 * @author d116773
 * @since 2.0.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = com.heb.pm.ApplicationConfiguration.class)
@ActiveProfiles("functional-test")
public class ScaleLabelFormatServiceTest {

	@Autowired
	private ScaleLabelFormatService scaleLabelFormatService;

	@Test
	@Transactional(transactionManager = "jpaTransactionManager")
	public void findAll() {

		PageableResult<ScaleLabelFormat> results = this.scaleLabelFormatService.findAllLabelFormats(true, 0, 20);
		System.out.println(String.format("page=%d", results.getPage()));
		System.out.println(String.format("is complete=%b", results.isComplete()));
		System.out.println(String.format("page count=%d", results.getPageCount()));
		System.out.println(String.format("record count=%d", results.getRecordCount()));
		results.getData().forEach(System.out::println);

		System.out.println("******************************");

		results = this.scaleLabelFormatService.findAllLabelFormats(false, 1, 20);
		System.out.println(String.format("page=%d", results.getPage()));
		System.out.println(String.format("is complete=%b", results.isComplete()));
		results.getData().forEach(System.out::println);
	}

	//@Test
	//@Transactional
	public void findByDescription() {

		PageableResult<ScaleLabelFormat> results = this.scaleLabelFormatService.findByDescription("PIG", true, 0, 20);
		System.out.println(String.format("page=%d", results.getPage()));
		System.out.println(String.format("is complete=%b", results.isComplete()));
		System.out.println(String.format("page count=%d", results.getPageCount()));
		System.out.println(String.format("record count=%d", results.getRecordCount()));
		results.getData().forEach(System.out::println);

		System.out.println("******************************");

		results = this.scaleLabelFormatService.findByDescription("PIG", false, 0, 20);
		System.out.println(String.format("page=%d", results.getPage()));
		System.out.println(String.format("is complete=%b", results.isComplete()));
		results.getData().forEach(System.out::println);
	}
}
