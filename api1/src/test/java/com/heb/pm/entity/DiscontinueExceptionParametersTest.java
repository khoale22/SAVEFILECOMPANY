/*
 *
 *  DiscontinueExceptionParametersTest
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests DiscontinueExceptionParameters.
 *
 * @author s573181
 * @since 2.0.2
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class DiscontinueExceptionParametersTest {

	private static final int ID = 1;
	private static final int SEQUENCE_NUMBER = 1;
	private static final int EXCEPTION_SEQUENCE_NUMBER = 3;

	private static final String PARAMETER_VALUE = "540                                               ";
	private static final String ATTRIBUTE_VALUE = "50016             ";
	private static final String ATTRIBUTE_TYPE = "Vendor              ";
	private static final boolean ACTIVE = true;
	private static final boolean NEVER_DELETE = true;
	private static final int PRIORITY = 7;

	@Autowired
	private DiscontinueExceptionRulesRepositoryTest discontinueExceptionRulesRepositoryTest;

	/*
	 * mapping
	 */

	/**
	 * Tests the JPA mapping.
	 */
	@Test
	public void mapping() {
		DiscontinueExceptionParametersKey key = this.getDefaultKey();
		DiscontinueExceptionParameters parameters = this.discontinueExceptionRulesRepositoryTest.findOne(key);
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
		DiscontinueExceptionParameters rules = this.getDefaultRecord();
		Assert.assertEquals(rules, rules);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject() {
		DiscontinueExceptionParameters rules = this.getDefaultRecord();
		DiscontinueExceptionParameters rules2 = this.getDefaultRecord();
		Assert.assertEquals(rules, rules2);
	}

	/**
	 * Test equals on objects with different keys.
	 */
	@Test
	public void equalsDifferentKeys() {
		DiscontinueExceptionParameters rules = this.getDefaultRecord();
		DiscontinueExceptionParameters rules2 = this.getAlternateRecord();
		boolean equals = rules.equals(rules2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		DiscontinueExceptionParameters rules = this.getDefaultRecord();
		boolean equals = rules.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		DiscontinueExceptionParameters rules = this.getDefaultRecord();
		boolean equals = rules.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		DiscontinueExceptionParameters rules1 = new DiscontinueExceptionParameters();
		DiscontinueExceptionParameters rules2 = this.getDefaultRecord();
		boolean equals = rules1.equals(rules2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the right side has a null key.
	 */
	@Test
	public void testEqualsNullTargetKey() {
		DiscontinueExceptionParameters rules1 = this.getDefaultRecord();
		DiscontinueExceptionParameters rules2 = new DiscontinueExceptionParameters();
		boolean equals = rules1.equals(rules2);
		Assert.assertFalse(equals);
	}

	/**
	 * Test equals when both keys are null.
	 */
	@Test
	public void testEqualsBothKeysNull() {
		DiscontinueExceptionParameters rules1 = new DiscontinueExceptionParameters();
		DiscontinueExceptionParameters rules2 = new DiscontinueExceptionParameters();
		boolean equals = rules1.equals(rules2);
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
		DiscontinueExceptionParameters rules = this.getDefaultRecord();
		Assert.assertEquals(rules.hashCode(), rules.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		DiscontinueExceptionParameters rules1 = this.getDefaultRecord();
		DiscontinueExceptionParameters rules2 = this.getDefaultRecord();
		Assert.assertEquals(rules1.hashCode(), rules2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		DiscontinueExceptionParameters rules1 = this.getDefaultRecord();
		DiscontinueExceptionParameters rules2 = this.getAlternateRecord();
		Assert.assertNotEquals(rules1.hashCode(), rules2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		DiscontinueExceptionParameters rules = new DiscontinueExceptionParameters();
		Assert.assertEquals(0, rules.hashCode());
	}

	/**
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		DiscontinueExceptionParameters rules1 = this.getDefaultRecord();
		DiscontinueExceptionParameters rules2 = new DiscontinueExceptionParameters();
		Assert.assertNotEquals(rules1.hashCode(), rules2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		DiscontinueExceptionParameters rules = this.getDefaultRecord();
		Assert.assertEquals("DiscontinueExceptionParameters{key=DiscontinueExceptionRulesKey{id=1, sequenceNumber=1, exceptionNumber=3}, parameterValue='540                                               ', priority=7, exceptionTypeId='50016             ', exceptionType='Vendor              ', active=true, neverDelete=true}",
				rules.toString());
	}

	 /*
	 * getters
	 */

	/**
	 * Tests getKey.
	 */
	@Test
	public void getKey() {
		DiscontinueExceptionParameters rules = this.getDefaultRecord();
		Assert.assertEquals(this.getDefaultKey(), rules.getKey());
	}

	/**
	 * Tests
	 */
	@Test
	public void getParameterValue() {
		DiscontinueExceptionParameters parameters = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersTest.PARAMETER_VALUE, parameters.getParameterValue());
	}

	/**
	 * Tests getPriority
	 */
	@Test
	public void getPriority() {
		DiscontinueExceptionParameters parameters = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersTest.PRIORITY, parameters.getPriority());
	}

	/**
	 * Tests getExceptionTypeId
	 */
	@Test
	public void getExceptionTypeId() {
		DiscontinueExceptionParameters parameters = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersTest.ATTRIBUTE_VALUE, parameters.getExceptionTypeId());
	}

	/**
	 * Tests getExceptionType
	 */
	@Test
	public void getAttributeType() {
		DiscontinueExceptionParameters parameters = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersTest.ATTRIBUTE_TYPE, parameters.getExceptionType());
	}

	/**
	 * Tests isActive
	 */
	@Test
	public void isActive() {
		DiscontinueExceptionParameters parameters = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersTest.ACTIVE, parameters.isActive());
	}

	/**
	 * Tests isNeverDelete
	 */
	@Test
	public void isNeverDelete() {
		DiscontinueExceptionParameters parameters = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersTest.NEVER_DELETE, parameters.isNeverDelete());
	}

	/**
	 * Tests getAttributes.
	 */
	@Test
	public void getAttributes() {
		DiscontinueExceptionParameters parameters = this.getDefaultRecord();
		Assert.assertEquals(parameters.isActive(), parameters.getAttributes().isActive());
		Assert.assertEquals(parameters.getParameterValue(), parameters.getAttributes().getParameterValue());
		Assert.assertEquals(parameters.getPriority(), parameters.getAttributes().getPriority());
	}
	/*
	 * Support functions.
	 */

	/**
	 * Returns a default key to test with.
	 *
	 * @return A default key to test with.
	 */
	private DiscontinueExceptionParametersKey getDefaultKey() {

		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setId(DiscontinueExceptionParametersTest.ID);
		key.setSequenceNumber(DiscontinueExceptionParametersTest.SEQUENCE_NUMBER);
		key.setExceptionNumber(DiscontinueExceptionParametersTest.EXCEPTION_SEQUENCE_NUMBER);
		return key;
	}

	/**
	 * Returns an DiscontinueExceptionRules object that equals the one that is first in the test table.
	 *
	 * @return An DiscontinueExceptionRules object that equals the one that is first in the test table.
	 */
	private DiscontinueExceptionParameters getDefaultRecord() {


		DiscontinueExceptionParameters rules = new DiscontinueExceptionParameters();
		rules.setKey(this.getDefaultKey());
		rules.setParameterValue(DiscontinueExceptionParametersTest.PARAMETER_VALUE);
		rules.setPriority(DiscontinueExceptionParametersTest.PRIORITY);
		rules.setExceptionTypeId(DiscontinueExceptionParametersTest.ATTRIBUTE_VALUE);
		rules.setExceptionType(DiscontinueExceptionParametersTest.ATTRIBUTE_TYPE);
		rules.setActive(DiscontinueExceptionParametersTest.ACTIVE);
		rules.setNeverDelete(DiscontinueExceptionParametersTest.NEVER_DELETE);

		return rules;
	}

	/**
	 * Returns an DiscontinueExceptionRules object that equals the one that is last in the test table.
	 *
	 * @return An DiscontinueExceptionRules object that equals the one that is last in the test table.
	 * -- is completely different than default
	 */
	private DiscontinueExceptionParameters getAlternateRecord() {
		DiscontinueExceptionParametersKey key = new DiscontinueExceptionParametersKey();
		key.setId(4);
		key.setSequenceNumber(2);
		key.setExceptionNumber(10);

		DiscontinueExceptionParameters rules = new DiscontinueExceptionParameters();
		rules.setKey(key);
		rules.setParameterValue("0                                                 ");
		rules.setPriority(DiscontinueExceptionParametersTest.PRIORITY);
		rules.setExceptionTypeId(DiscontinueExceptionParametersTest.ATTRIBUTE_VALUE);
		rules.setExceptionType("Vendor              ");
		rules.setActive(false);
		rules.setNeverDelete(true);

		return rules;
	}

	/**
	 * Since the equals on DiscontinueExceptionParameters only compares keys, this goes deeper anc compares all
	 * values in the object.
	 *
	 * @param a The first one to compare.
	 * @param b The second one to compare.
	 */
	private void fullItemCompare(DiscontinueExceptionParameters a, DiscontinueExceptionParameters b) {
		Assert.assertEquals(a.getKey(), b.getKey());
		Assert.assertEquals(a.getParameterValue(), b.getParameterValue());
		Assert.assertEquals(a.getPriority(), b.getPriority());
		Assert.assertEquals(a.getExceptionTypeId(), b.getExceptionTypeId());
		Assert.assertEquals(a.getExceptionType(), b.getExceptionType());
		Assert.assertEquals(a.isActive(), b.isActive());
		Assert.assertEquals(a.isNeverDelete(), b.isNeverDelete());
	}
}
