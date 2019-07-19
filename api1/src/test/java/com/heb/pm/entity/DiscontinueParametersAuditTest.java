/*
 *  DiscontinueParametersAuditTest
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tests DiscontinueParametersAudit.
 *
 * @author s573181
 * @since 2.0.4
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class DiscontinueParametersAuditTest {


	private static final int ID = 1;
	private static final int SEQUENCE_NUMBER = 1;
	private static final String PARAMETER_VALUE = "540                                               ";
	private static final int PRIORITY = 0;
	private static final boolean ACTIVE = true;
	private static final String ACTION = "ADD  ";
	private static final LocalDateTime TIMESTAMP = LocalDateTime.parse("2012-05-11 06:09:45.311000",  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
	private static final String USER_ID = "vn44230             ";


	@Autowired
	DiscontinueParametersAuditRepositoryTest discontinueParametersAuditRepositoryTest;

	/*
	 * mapping
	 */

	/**
	 * Tests the JPA mapping.
	 */
	@Test
	public void mapping() {
		DiscontinueParametersAuditKey key = new DiscontinueParametersAuditKey();
		key.setId(ID);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setTimestamp(TIMESTAMP);
		DiscontinueParametersAudit audit = this.discontinueParametersAuditRepositoryTest.findOne(key);
		Assert.assertTrue(audit.equals(this.getDefaultRecord()));
	}


	/*
	 * equals
	 */

	/**
	 * Test equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(audit, audit);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		DiscontinueParametersAudit audit2 = this.getDefaultRecord();
		Assert.assertEquals(audit, audit2);
	}

	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		boolean equals = audit.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		boolean equals = audit.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		DiscontinueParametersAudit audit = new DiscontinueParametersAudit();
		DiscontinueParametersAudit audit2 = this.getDefaultRecord();
		boolean equals = audit.equals(audit2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the right side has a null key.
	 */
	@Test
	public void testEqualsNullTargetKey() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		DiscontinueParametersAudit audit2 = new DiscontinueParametersAudit();
		boolean equals = audit.equals(audit2);
		Assert.assertFalse(equals);
	}

	/*
	 * hashCode
	 */

	/**
	 * Test hashCode on the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(audit.hashCode(), audit.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		DiscontinueParametersAudit audit2 = this.getDefaultRecord();
		Assert.assertEquals(audit.hashCode(), audit2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		DiscontinueParametersAudit audit2 = this.getAlternateRecord();
		Assert.assertNotEquals(audit.hashCode(), audit2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		DiscontinueParametersAudit audit = new DiscontinueParametersAudit();
		Assert.assertEquals(0, audit.hashCode());
	}

	/**
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		DiscontinueParametersAudit audit2 = new DiscontinueParametersAudit();
		Assert.assertNotEquals(audit.hashCode(), audit2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals("DiscontinueParametersAudit{key=DiscontinueParametersAuditKey{timestamp='2012-05-11T06:09:45.311'," +
						" id=1, sequenceNumber=1}, parameterName='null', audit=DiscontinueParametersCommonAudit{parameterValue=" +
						"'540                                               ', priority=0, active=true, action='ADD  ', " +
						"userId='vn44230             '}}",
				audit.toString());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getParameterValue.
	 */
	@Test
	public void getParameterValue() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersAuditTest.PARAMETER_VALUE, audit.getParameterValue());

	}

	/**
	 * Tests getPriority.
	 */
	@Test
	public void getPriority() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersAuditTest.PRIORITY, audit.getPriority());

	}

	/**
	 * Tests isActive.
	 */
	@Test
	public void isActive() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersAuditTest.ACTIVE, audit.isActive());

	}

	/**
	 * Tests getId.
	 */
	@Test
	public void getId() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersAuditTest.ID , audit.getId());

	}

	/**
	 * Tests getSequenceNumber.
	 */
	@Test
	public void getSequenceNumber() {
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersAuditTest.SEQUENCE_NUMBER, audit.getSequenceNumber());

	}

	/**
	 * Tests getAction.
	 */
	@Test
	public void getAction(){
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersAuditTest.ACTION, audit.getAction());

	}

	/**
	 * Tests getTimestamp.
	 */
	@Test
	public void getTimestamp(){
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersAuditTest.TIMESTAMP, audit.getTimestamp());

	}

	/**
	 * Tests getUserId.
	 */
	@Test
	public void getUserId(){
		DiscontinueParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueParametersAuditTest.USER_ID, audit.getUserId());

	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a DiscontinueParametersAudit object that is equal to the first one in the test table.
	 *
	 * @return Returns a DiscontinueParametersAudit object that is equal to the first one in the test table.
	 */
	private DiscontinueParametersAudit getDefaultRecord(){
		DiscontinueParametersAudit audit = new DiscontinueParametersAudit();
		audit.setParameterValue(DiscontinueParametersAuditTest.PARAMETER_VALUE);
		audit.setPriority(DiscontinueParametersAuditTest.PRIORITY);
		audit.setActive(DiscontinueParametersAuditTest.ACTIVE);
		audit.setId(DiscontinueParametersAuditTest.ID);
		audit.setSequenceNumber(DiscontinueParametersAuditTest.SEQUENCE_NUMBER);
		audit.setAction(DiscontinueParametersAuditTest.ACTION);
		audit.setTimestamp(DiscontinueParametersAuditTest.TIMESTAMP);
		audit.setUserId(DiscontinueParametersAuditTest.USER_ID);
		return audit;
	}

	/**
	 * Returns an alternate DiscontinueParametersAudit object that isn't equal to the first one in the test table.
	 *
	 * @return Returns an alternate DiscontinueParametersAudit object that isn't equal to the first one in the test table.
	 */
	private DiscontinueParametersAudit getAlternateRecord(){
		DiscontinueParametersAudit audit = new DiscontinueParametersAudit();
		audit.setParameterValue("540                                               ");
		audit.setPriority(7);
		audit.setActive(false);
		audit.setId(4);
		audit.setSequenceNumber(20);
		audit.setAction("ADD  ");
		audit.setTimestamp(LocalDateTime.parse("2016-05-11 14:41:41.896000",  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
		audit.setUserId("p127765             ");
		return audit;
	}
}
