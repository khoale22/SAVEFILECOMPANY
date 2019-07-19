/*
 * SubDepartmentProcessorTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
import com.heb.pm.productHierarchy.SubDepartmentDocument;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests SubDepartmentProcessor.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubDepartmentProcessorTest {

	@Test
	public void process() {

		SubDepartmentProcessor subDepartmentProcessor = new SubDepartmentProcessor();

		SubDepartmentKey key = new SubDepartmentKey();
		key.setDepartment("07     ");
		key.setSubDepartment("A     ");

		SubDepartment subDepartment = new SubDepartment();
		subDepartment.setKey(key);
		subDepartment.setName("my sub-deparment name");

		try {
			SubDepartmentDocument document = subDepartmentProcessor.process(subDepartment);
			Assert.assertEquals(subDepartment.getKey().getDepartment(), document.getData().getKey().getDepartment());
			Assert.assertEquals(subDepartment.getKey().getSubDepartment(),
					document.getData().getKey().getSubDepartment());
			Assert.assertEquals(subDepartment.getName(), document.getData().getName());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}
}
