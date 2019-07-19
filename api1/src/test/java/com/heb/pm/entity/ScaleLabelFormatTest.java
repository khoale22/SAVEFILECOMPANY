/*
 * ScaleLabelFormatTest
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
 * Tests ScaleLableFormat.
 *
 * @author d116773
 * @since 2.0.8
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ScaleLabelFormatTest {

	private static final int FORMAT_CODE = 74;
	private static final String FORMAT_DESCRIPTION = "BREAKFAST PORK CHOPS          ";

	@Autowired
	private ScaleLabelFormatRepositoryTest repository;

	/*
	 * JPA Mapping.
	 */

	/**
	 * Test the JPA Mapping.
	 */
	@Test
	public void mapping() {

		ScaleLabelFormat slf = this.repository.findOne(ScaleLabelFormatTest.FORMAT_CODE);
		Assert.assertEquals(ScaleLabelFormatTest.FORMAT_CODE, slf.getFormatCode());
		Assert.assertEquals(ScaleLabelFormatTest.FORMAT_DESCRIPTION, slf.getDescription());
	}

	/*
	 * Getters.
	 */

	/**
	 * Test getFormatCode.
	 */
	@Test
	public void getFormatCode() {
		Assert.assertEquals(ScaleLabelFormatTest.FORMAT_CODE, this.getTestObject().getFormatCode());
	}

	/**
	 * Tests getDescription.
	 */
	@Test
	public void getDescription() {
		Assert.assertEquals(ScaleLabelFormatTest.FORMAT_DESCRIPTION, this.getTestObject().getDescription());
	}

	/*
	 * toString.
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals("ScaleLabelFormat{formatCode='74', description='BREAKFAST PORK CHOPS          '}",
				this.getTestObject().toString());
	}

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed itself.
	 */
	@Test
	public void equalsSameObject() {
		ScaleLabelFormat slf1 = this.getTestObject();
		boolean eq = slf1.equals(slf1);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when passed an equal object.
	 */
	@Test
	public void equalsEqualObjects() {
		ScaleLabelFormat slf1 = this.getTestObject();
		ScaleLabelFormat slf2 = this.getTestObject();
		boolean eq = slf1.equals(slf2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when passed an object with the same key.
	 */
	@Test
	public void equalsEqualKeys() {
		ScaleLabelFormat slf1 = this.getTestObject();
		ScaleLabelFormat slf2 = this.getTestObject();
		slf2.setDescription("not equal description");
		boolean eq = slf1.equals(slf2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when passed an object with a different key.
	 */
	@Test
	public void equalsDifferentKey() {
		ScaleLabelFormat slf1 = this.getTestObject();
		ScaleLabelFormat slf2 = this.getTestObject();
		slf2.setFormatCode(ScaleLabelFormatTest.FORMAT_CODE + 1);
		boolean eq = slf1.equals(slf2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed null.
	 */
	@Test
	public void equalsNull() {
		ScaleLabelFormat slf1 = this.getTestObject();
		boolean eq = slf1.equals(null);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed an object of a different type.
	 */
	@Test
	public void equalsDifferentObjectType() {
		ScaleLabelFormat slf1 = this.getTestObject();
		boolean eq = slf1.equals(Integer.valueOf(ScaleLabelFormatTest.FORMAT_CODE));
		Assert.assertFalse(eq);
	}

	/*
	 * hashCode
	 */

	/**
	 * Test hashCode when the objects are equal.
	 */
	@Test
	public void hashCodeEqualObjects() {
		ScaleLabelFormat slf1 = this.getTestObject();
		ScaleLabelFormat slf2 = this.getTestObject();
		Assert.assertEquals(slf1.hashCode(), slf2.hashCode());
	}

	/**
	 * Tests hashCode when the objects are not equal.
	 */
	@Test
	public void hashCodeUnequalObjects() {
		ScaleLabelFormat slf1 = this.getTestObject();
		ScaleLabelFormat slf2 = this.getTestObject();
		slf2.setFormatCode(ScaleLabelFormatTest.FORMAT_CODE + 1);
		Assert.assertNotEquals(slf1.hashCode(), slf2.hashCode());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns an object to test against.
	 *
	 * @return An object to test against.
	 */
	private ScaleLabelFormat getTestObject() {

		ScaleLabelFormat slf = new ScaleLabelFormat();
		slf.setFormatCode(ScaleLabelFormatTest.FORMAT_CODE);
		slf.setDescription(ScaleLabelFormatTest.FORMAT_DESCRIPTION);

		return slf;
	}
}
