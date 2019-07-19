/*
 * SubDepartmentTest
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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests SubDepartment.
 *
 * @author d116773
 * @since 2.0.2
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class SubDepartmentTest {

	private static final String DEFAULT_DEPARTMENT = "07   ";
	private static final String DEFAULT_SUB_DEPARTMENT = "A    ";
	private static final String DEFAULT_NAME = "DRY GROCERY - 7A              ";

	@Autowired
	private SubDepartmentRepositoryTest subDepartmentRepository;

	/*
	 * getters
	 */

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {
		SubDepartment subDepartment = this.getSubDepartment();
		Assert.assertEquals(this.getSubDepartmentKey(), subDepartment.getKey());
	}

	/**
	 * Tests getName
	 */
	@Test
	public void getName() {
		SubDepartment subDepartment = this.getSubDepartment();
		Assert.assertEquals(SubDepartmentTest.DEFAULT_NAME, subDepartment.getName());
	}

	/**
	 * Tests getDisplayName.
	 */
	@Test
	public void getDisplayName() {
		SubDepartment subDepartment = this.getSubDepartment();
		String displayName = subDepartment.getName().trim() + "[" + subDepartment.getKey().getDepartment().trim()
				+ subDepartment.getKey().getSubDepartment().trim() + "]";
		Assert.assertEquals(displayName, subDepartment.getDisplayName());
	}

	/**
	 * Tests getNormalizedId.
	 */
	@Test
	public void getNormalizedId() {
		SubDepartment subDepartment = this.getSubDepartment();
		String id = subDepartment.getKey().getDepartment().trim() + subDepartment.getKey().getSubDepartment().trim();
		Assert.assertEquals(id, subDepartment.getNormalizedId());
	}

	/*
	 * equals
	 */

	/**
	 * Tests equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		SubDepartment subDepartment = this.getSubDepartment();
		boolean equals = subDepartment.equals(subDepartment);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals on objects with the same values.
	 */
	@Test
	public void equalsSimilarObjects() {
		SubDepartment subDepartment1 = this.getSubDepartment();
		SubDepartment subDepartment2 = this.getSubDepartment();
		boolean equals = subDepartment1.equals(subDepartment2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals on an object with the same key but other values for other properties.
	 */
	@Test
	public void equalsSameKey() {
		SubDepartment subDepartment1 = this.getSubDepartment();
		SubDepartment subDepartment2 = new SubDepartment();
		subDepartment2.setKey(this.getSubDepartmentKey());
		subDepartment2.setName("other name");
		boolean equals = subDepartment1.equals(subDepartment2);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals on two objects with different keys.
	 */
	@Test
	public void equalsDifferentKey() {
		SubDepartment subDepartment1 = this.getSubDepartment();
		SubDepartment subDepartment2 = this.getOtherSubDepartment();
		boolean equals = subDepartment1.equals(subDepartment2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different type of object.
	 */
	@Test
	public void equalsDifferentObject() {
		SubDepartment subDepartment = this.getSubDepartment();
		boolean equals = subDepartment.equals("test value)");
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals against null.
	 */
	@Test
	public void equalsNull() {
		SubDepartment subDepartment = this.getSubDepartment();
		boolean equals = subDepartment.equals(null);
		Assert.assertFalse(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode on the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		SubDepartment subDepartment = this.getSubDepartment();
		Assert.assertEquals(subDepartment.hashCode(), subDepartment.hashCode());
	}

	/**
	 * Tests hashCode with objects that have all the same values.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		SubDepartment subDepartment1 = this.getSubDepartment();
		SubDepartment subDepartment2 = this.getSubDepartment();
		Assert.assertEquals(subDepartment1.hashCode(), subDepartment2.hashCode());
	}

	/**
	 * Tests hashCode when the objects have the same keys but different values for other properties.
	 */
	@Test
	public void hashCodeSameKey() {
		SubDepartment subDepartment1 = this.getSubDepartment();
		SubDepartment subDepartment2 = this.getSubDepartment();
		subDepartment2.setName("other name");
		Assert.assertEquals(subDepartment1.hashCode(), subDepartment2.hashCode());
	}

	/**
	 * Tests hashCode when the objects have all different values.
	 */
	@Test
	public void hashCodeDifferentKeys() {
		SubDepartment subDepartment1 = this.getSubDepartment();
		SubDepartment subDepartment2 = this.getOtherSubDepartment();
		Assert.assertNotEquals(subDepartment1.hashCode(), subDepartment2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals("SubDepartment{key=SubDepartmentKey{department='07   ', subDepartment='A    '}, name='DRY GROCERY - 7A              '}",
				this.getSubDepartment().toString());
	}

	/*
	 * Mappings.
	 */

	/**
	 * Test the mapping of the key.
	 */
	@Test
	public void mappingKey() {
		SubDepartment subDepartment = this.subDepartmentRepository.findOne(this.getSubDepartmentKey());
		Assert.assertEquals(this.getSubDepartmentKey(), subDepartment.getKey());
	}

	/**
	 * Test the mapping of the name parameter.
	 */
	@Test
	public void mappingName() {
		SubDepartment subDepartment = this.subDepartmentRepository.findOne(this.getSubDepartmentKey());
		Assert.assertEquals(SubDepartmentTest.DEFAULT_NAME, subDepartment.getName());
	}


	/*
	 * Support functions.
	 */

	/**
	 * Returns the default key to test with.
	 *
	 * @return The default key to test with.
	 */
	private SubDepartmentKey getSubDepartmentKey() {

		SubDepartmentKey subDepartmentKey = new SubDepartmentKey();
		subDepartmentKey.setDepartment(SubDepartmentTest.DEFAULT_DEPARTMENT);
		subDepartmentKey.setSubDepartment(SubDepartmentTest.DEFAULT_SUB_DEPARTMENT);
		return subDepartmentKey;
	}

	/**
	 * Returns the default sub-department to test with.
	 *
	 * @return The default sub-department to test with.
	 */
	private SubDepartment getSubDepartment() {

		SubDepartment subDepartment = new SubDepartment();
		subDepartment.setKey(this.getSubDepartmentKey());
		subDepartment.setName(SubDepartmentTest.DEFAULT_NAME);
		return subDepartment;
	}

	/**
	 * Returns a different key to test with. It does not equal the one from getSubDepartmentKey.
	 *
	 * @return A different key to test with.
	 */
	private SubDepartmentKey getOtherKey() {
		SubDepartmentKey subDepartmentKey = new SubDepartmentKey();
		subDepartmentKey.setDepartment("09");
		subDepartmentKey.setSubDepartment("F");
		return subDepartmentKey;
	}

	/**
	 * Returns another sub-deparment to test with.
	 *
	 * @return Another sub-department to test with.
	 */
	private SubDepartment getOtherSubDepartment() {
		SubDepartment subDepartment = new SubDepartment();
		subDepartment.setKey(this.getOtherKey());
		subDepartment.setName("Floral");
		return subDepartment;
	}
}
