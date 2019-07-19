/*
 *
 * DiscontinueExceptionParameters
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 *
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests DiscontinueExceptionParameters
 *
 * @author 573181
 * @since 3.0.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class DiscontinueParametersTest {

	private static final int ID = 4;
	private static final int SEQUENCE_NUMBER = 1;
	private static final String PARAMETER_VALUE = "0                                                 ";
	private static final boolean ACTIVE = true;
	private static final String NAME = "STR_INVEN.ON_HAND_QTY         ";

	@Autowired
	private DiscontinueParametersRepositoryTest repositoryTest;

	/*
	 * mapping
	 */

	/**
	 * Tests the JPA mapping.
	 */
	@Test
	public void mapping() {

		DiscontinueParameters parameters = this.repositoryTest.findOne(this.getTestKey());
		this.fullItemCompare(this.getDefaultRecord(), parameters);
	}

	/*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		DiscontinueParameters params = this.getDefaultRecord();
		boolean equals = params.equals(params);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject() {
		DiscontinueParameters params1 = this.getDefaultRecord();
		DiscontinueParameters params2 = this.getDefaultRecord();
		boolean equals = params1.equals(params2);
		Assert.assertTrue(equals);
	}

	/**
	 * Test equals on objects with different keys.
	 */
	@Test
	public void equalsDifferentKeys() {
		DiscontinueParameters params1 = this.getDefaultRecord();
		DiscontinueParameters params2 = this.getAlternateRecord();
		boolean equals = params1.equals(params2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		DiscontinueParameters params = this.getDefaultRecord();
		boolean equals = params.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		DiscontinueParameters params = this.getDefaultRecord();
		boolean equals = params.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		DiscontinueParameters params1 = new DiscontinueParameters();
		DiscontinueParameters params2 = this.getDefaultRecord();
		boolean equals = params1.equals(params2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the right side has a null key.
	 */
	@Test
	public void testEqualsNullTargetKey() {
		DiscontinueParameters params1 = this.getDefaultRecord();
		DiscontinueParameters params2 = new DiscontinueParameters();
		boolean equals = params1.equals(params2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when both keys are null.
	 */
	@Test
	public void testEqualsBothKeysNull() {
		DiscontinueParameters params1 = new DiscontinueParameters();
		DiscontinueParameters params2 = new DiscontinueParameters();
		boolean equals = params1.equals(params2);
		Assert.assertTrue(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Test hashCode on the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		DiscontinueParameters params = this.getDefaultRecord();
		Assert.assertEquals(params.hashCode(), params.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		DiscontinueParameters params1 = this.getDefaultRecord();
		DiscontinueParameters params2 = this.getDefaultRecord();
		Assert.assertEquals(params1.hashCode(), params2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		DiscontinueParameters params1 = this.getDefaultRecord();
		DiscontinueParameters params2 = this.getAlternateRecord();
		Assert.assertNotEquals(params1.hashCode(), params2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		DiscontinueParameters params = new DiscontinueParameters();
		Assert.assertEquals(0, params.hashCode());
	}

	/**
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		DiscontinueParameters params1 = this.getDefaultRecord();
		DiscontinueParameters params2 = new DiscontinueParameters();
		Assert.assertNotEquals(params1.hashCode(), params2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		DiscontinueParameters parameters = this.getDefaultRecord();
		System.out.println(parameters.toString());
		Assert.assertEquals("DiscontinueParameters{key=DiscontinueExceptionParametersKey{id=4, sequenceNumber=1}, parameterValue='0                                                 ', active=true}",
				parameters.toString());
	}

	 /*
	 * getters
	 */

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {

		DiscontinueParameters parameters = this.getDefaultRecord();
		Assert.assertEquals(this.getTestKey(), parameters.getKey());
	}

	/**
	 * Tests isActive
	 */
	@Test
	public void getActive(){
		DiscontinueParameters params = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersTest.ACTIVE, params.isActive());
	}

	/**
	 * Tests getParameterValue
	 */
	@Test
	public void getParameterValue(){

		DiscontinueParameters params = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersTest.PARAMETER_VALUE, params.getParameterValue());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a key for a test record.
	 *
	 * @return A key for the test record.
	 */
	private DiscontinueParametersKey getTestKey() {

		DiscontinueParametersKey key = new DiscontinueParametersKey();
		key.setId(DiscontinueParametersTest.ID);
		key.setSequenceNumber(DiscontinueParametersTest.SEQUENCE_NUMBER);
		return key;
	}

	/**
	 * Returns an DiscontinueExceptionParameters object that equals the one that is first in the test table.
	 *
	 * @return An DiscontinueExceptionParameters object that equals the one that is first in the test table.
	 */
	private DiscontinueParameters getDefaultRecord() {

		DiscontinueParameters parameters = new DiscontinueParameters();
		parameters.setKey(this.getTestKey());
		parameters.setParameterValue(DiscontinueParametersTest.PARAMETER_VALUE);
		parameters.setActive(DiscontinueParametersTest.ACTIVE);

		return parameters;
	}

	/**
	 * Returns an DiscontinueExceptionParameters object that equals the one that is last in the test table.
	 *  -- Is completely different.
	 *
	 * @return An DiscontinueExceptionParameters object that equals the one that is last in the test table.
	 */

	private DiscontinueParameters getAlternateRecord() {
		DiscontinueParametersKey key = new DiscontinueParametersKey();
		key.setId(7);
		key.setSequenceNumber(2);

		DiscontinueParameters parameters = new DiscontinueParameters();
		parameters.setKey(key);
		parameters.setParameterValue("541                                               ");
		parameters.setActive(false);

		return parameters;
	}

	/**
	 * Since the equals on DiscontinueExceptionParameters only compares keys, this goes deeper anc compares all
	 * values in the object.
	 *
	 * @param a The first one to compare.
	 * @param b The second one to compare.
	 */
	private void fullItemCompare(DiscontinueParameters a, DiscontinueParameters b) {
		Assert.assertEquals(a.getKey(), b.getKey());
		Assert.assertEquals(a.getParameterValue(), b.getParameterValue());
		Assert.assertEquals(a.isActive(), b.isActive());
	}

}
