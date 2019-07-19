/*
 * SubDepartmentKeyTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

/**
 * Tests SubDepartmentKey.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubDepartmentKeyTest {

	private static final String DEFAULT_DEPARTMENT = "07   ";
	private static final String DEFAULT_SUB_DEPARTMENT = "A    ";

	/*
	 * getters
	 */

	/**
	 * Tests getDepartment.
	 */
	@Test
	public void getDepartment() {

		SubDepartmentKey subDepartmentKey = this.getSubDepartmentKey();
		Assert.assertEquals(SubDepartmentKeyTest.DEFAULT_DEPARTMENT, subDepartmentKey.getDepartment());
	}

	/**
	 * Tests getSubDepartment.
	 */
	@Test
	public void getSubDepartment() {

		SubDepartmentKey subDepartmentKey = this.getSubDepartmentKey();
		Assert.assertEquals(SubDepartmentKeyTest.DEFAULT_SUB_DEPARTMENT, subDepartmentKey.getSubDepartment());
	}


	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void equalsSameObject() {
		SubDepartmentKey subDepartmentKey = this.getSubDepartmentKey();
		boolean equals = subDepartmentKey.equals(subDepartmentKey);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals when passed an equal object.
	 */
	@Test
	public void equalsSimilarObject() {
		SubDepartmentKey subDepartmentKey1 = this.getSubDepartmentKey();
		SubDepartmentKey subDepartmentKey2 = this.getSubDepartmentKey();
		boolean equals = subDepartmentKey1.equals(subDepartmentKey2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals when passed an object with the same department but a different sub-department.
	 */
	@Test
	public void equalsDifferentSubDepartment() {
		SubDepartmentKey subDepartmentKey1 = this.getSubDepartmentKey();
		SubDepartmentKey subDepartmentKey2 = this.getSubDepartmentKey();
		subDepartmentKey2.setSubDepartment("other sub department");
		boolean equals = subDepartmentKey1.equals(subDepartmentKey2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when passed an object with a different department and the same sub-department.
	 */
	@Test
	public void equalsDifferentDepartment() {
		SubDepartmentKey subDepartmentKey1 = this.getSubDepartmentKey();
		SubDepartmentKey subDepartmentKey2 = this.getSubDepartmentKey();
		subDepartmentKey2.setDepartment("other department");
		boolean equals = subDepartmentKey1.equals(subDepartmentKey2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed an object that is not a SubDepartmentKey.
	 */
	@Test
	public void equalsOtherObject() {
		SubDepartmentKey subDepartmentKey = this.getSubDepartmentKey();
		boolean equals = subDepartmentKey.equals("07     ");
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when passed nul.
	 */
	@Test
	public void equalsNull() {
		SubDepartmentKey subDepartmentKey = this.getSubDepartmentKey();
		boolean equals = subDepartmentKey.equals(null);
		Assert.assertFalse(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode with the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		SubDepartmentKey subDepartmentKey = this.getSubDepartmentKey();
		Assert.assertEquals(subDepartmentKey.hashCode(), subDepartmentKey.hashCode());
	}

	/**
	 * Tests hashCode with equal objects.
	 */
	@Test
	public void hashCodeSimilarObject() {
		SubDepartmentKey subDepartmentKey1 = this.getSubDepartmentKey();
		SubDepartmentKey subDepartmentKey2 = this.getSubDepartmentKey();
		Assert.assertEquals(subDepartmentKey1.hashCode(), subDepartmentKey2.hashCode());
	}

	/**
	 * Tests hashCode with objects with the same department and different sub-departments.
	 */
	@Test
	public void hashCodeDifferentSubDepartment() {
		SubDepartmentKey subDepartmentKey1 = this.getSubDepartmentKey();
		SubDepartmentKey subDepartmentKey2 = this.getSubDepartmentKey();
		subDepartmentKey2.setSubDepartment("other sub department");
		Assert.assertNotEquals(subDepartmentKey1.hashCode(), subDepartmentKey2.hashCode());
	}

	/**
	 * Tests hashCode with objects with different departments and the same sub-department.
	 */
	@Test
	public void hashCodeDifferentDepartment() {
		SubDepartmentKey subDepartmentKey1 = this.getSubDepartmentKey();
		SubDepartmentKey subDepartmentKey2 = this.getSubDepartmentKey();
		subDepartmentKey2.setDepartment("other department");
		Assert.assertNotEquals(subDepartmentKey1.hashCode(), subDepartmentKey2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals("SubDepartmentKey{department='07   ', subDepartment='A    '}",
				this.getSubDepartmentKey().toString());
	}

	/*
	 * serializable
	 */

	/**
	 * Makes sure SubDeaprtmentKey implements Serializable.
	 */
	@Test
	public void isSerializable() {
		SubDepartmentKey subDepartmentKey = this.getSubDepartmentKey();
		Assert.assertTrue(subDepartmentKey instanceof Serializable);
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a SubDepartmentKey to test with.
	 *
	 * @return A SubDepartmentKey to test with.
	 */
	private SubDepartmentKey getSubDepartmentKey() {

		SubDepartmentKey subDepartmentKey = new SubDepartmentKey();
		subDepartmentKey.setDepartment(SubDepartmentKeyTest.DEFAULT_DEPARTMENT);
		subDepartmentKey.setSubDepartment(SubDepartmentKeyTest.DEFAULT_SUB_DEPARTMENT);
		return subDepartmentKey;
	}
}
