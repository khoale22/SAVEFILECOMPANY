/*
 * SubDepartmentDocumentTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;


import org.junit.Assert;
import org.junit.Test;

/**
 * Tests SubDepartmentDocument.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubDepartmentDocumentTest {

	private static final String DEPARTMENT = "07   ";
	private static final String SUB_DEPARTMENT = "A    ";
	private static final String KEY = DEPARTMENT.trim() + SUB_DEPARTMENT.trim();
	private static final String SUB_DEPARTMENT_NAME = "GROCERY";


	/**
	 * Tests the non-default constructor for SubDepartmentDocument;
	 */
	@Test
	public void constructor() {

		SubDepartmentDocument document = new SubDepartmentDocument(this.getSubDepartment());
		Assert.assertEquals(this.getSubDepartment(), document.getData());
		Assert.assertEquals(SubDepartmentDocumentTest.KEY, document.getKey());
	}

	/**
	 * Tests toKey.
	 */
	@Test
	public void toKey() {
		SubDepartmentDocument document = new SubDepartmentDocument();
		Assert.assertEquals(SubDepartmentDocumentTest.KEY, document.toKey(this.getSubDepartment()));
	}

	/**
	 * Test toKey when the object passed in is null.
	 */
	@Test
	public void toKeyNull() {
		SubDepartmentDocument document = new SubDepartmentDocument();
		Assert.assertEquals("", document.toKey(null));
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a SubDepartment to test with.
	 *
	 * @return A SubDepartment to test with.
	 */
	private SubDepartment getSubDepartment() {

		SubDepartmentKey key = new SubDepartmentKey();
		key.setDepartment(SubDepartmentDocumentTest.DEPARTMENT);
		key.setSubDepartment(SubDepartmentDocumentTest.SUB_DEPARTMENT);

		SubDepartment subDepartment = new SubDepartment();
		subDepartment.setKey(key);
		subDepartment.setName(SubDepartmentDocumentTest.SUB_DEPARTMENT_NAME);

		return subDepartment;
	}
}
