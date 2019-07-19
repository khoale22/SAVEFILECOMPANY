/*
 *
 *  DiscontinueExceptionParametersAuditTest
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tests DiscontinueExceptionParametersAudit.
 *
 * @author s573181
 * @since 2.0.3
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class DiscontinueExceptionParametersAuditTest {

	private static final int EXCEPTION_NUMBER = 1;
	private static final String EXCEPTION_TYPE_ID = "                  ";
	private static final String EXCEPTION_TYPE = "SBT                 ";
	private static final boolean NEVER_DELETE = false;

	private static final int ID = 5;
	private static final int SEQUENCE_NUMBER = 1;
	private static final String PARAMETER_VALUE = "720                                               ";
	private static final int PRIORITY = 1;
	private static final boolean ACTIVE = true;
	private static final String ACTION = "ADD  ";
	private static final LocalDateTime TIMESTAMP = LocalDateTime.parse("2012-05-15 09:47:19.888000",  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
	private static final String USER_ID = "c140171             ";

	@Autowired
	DiscontinueExceptionParametersAuditRepositoryTest discontinueExceptionParametersAuditRepositoryTest;

	/*
	 * mapping
	 */

	/**
	 * Tests the JPA mapping.
	 */
	@Test
	public void mapping() {
		DiscontinueExceptionParametersAuditKey key  = new DiscontinueExceptionParametersAuditKey();
		key.setId(ID);
		key.setTimestamp(TIMESTAMP);
		key.setSequenceNumber(SEQUENCE_NUMBER);
		key.setExceptionNumber(EXCEPTION_NUMBER);
		DiscontinueExceptionParametersAudit audit = this.discontinueExceptionParametersAuditRepositoryTest.findOne(key);
		DiscontinueExceptionParametersAudit audit2 = this.getDefaultRecord();
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
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(audit, audit);
	}

	/**
	 * Test equals on similar objects.
	 */
	@Test
	public void equalsSimilarObject() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		DiscontinueExceptionParametersAudit audit2 = this.getDefaultRecord();
		Assert.assertEquals(audit, audit2);
	}

	/**
	 * Test equals on null
	 */
	@Test
	public void testEqualsNull() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		boolean equals = audit.equals(null);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals on a different class.
	 */
	@Test
	public void testEqualsDifferentClass() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		boolean equals = audit.equals(Integer.valueOf(66));
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the left side has a null key.
	 */
	@Test
	public void testEqualsNullSourceKey() {
		DiscontinueExceptionParametersAudit audit = new DiscontinueExceptionParametersAudit();
		DiscontinueExceptionParametersAudit audit2 = this.getDefaultRecord();
		boolean equals = audit.equals(audit2);
		Assert.assertFalse(equals);
	}

	/**
	 * Tests equals when the right side has a null key.
	 */
	@Test
	public void testEqualsNullTargetKey() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		DiscontinueExceptionParametersAudit audit2 = new DiscontinueExceptionParametersAudit();
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
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(audit.hashCode(), audit.hashCode());
	}

	/**
	 * Test hashCode on similar objects.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		DiscontinueExceptionParametersAudit audit2 = this.getDefaultRecord();
		Assert.assertEquals(audit.hashCode(), audit2.hashCode());
	}

	/**
	 * Test hashCode on different objects.
	 */
	@Test
	public void hashCodeDifferentObjects() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		DiscontinueExceptionParametersAudit audit2 = this.getAlternateRecord();
		Assert.assertNotEquals(audit.hashCode(), audit2.hashCode());
	}

	/**
	 * Test hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		DiscontinueExceptionParametersAudit audit = new DiscontinueExceptionParametersAudit();
		Assert.assertEquals(0, audit.hashCode());
	}


	/**
	 * Test hashCode when the left side has a key and the right side does not.
	 */
	@Test
	public void hashCodeNullRight() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		DiscontinueExceptionParametersAudit audit2 = new DiscontinueExceptionParametersAudit();
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
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals("DiscontinueExceptionParametersAudit{key=DiscontinueExceptionParametersAuditKey{parametersAuditKey=" +
						"DiscontinueParametersAuditKey{timestamp='2012-05-15T09:47:19.888', id=5, sequenceNumber=1}, exceptionNumber=1}" +
						", audit=DiscontinueParametersCommonAudit{parameterValue='720                                               ', priority=1" +
						", active=true, action='ADD  ', userId='c140171             '}, exceptionTypeId='                  ', " +
						"exceptionType='SBT                 ', neverDelete=false}",
				audit.toString());
	}

	 /*
	 * getters
	 */

	 @Test
	 public void getExceptionNumber() {
		 DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		 Assert.assertEquals(DiscontinueExceptionParametersAuditTest.EXCEPTION_NUMBER , audit.getExceptionNumber());

	 }

	@Test
	public void getExceptionTypeId() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.EXCEPTION_TYPE_ID , audit.getExceptionTypeId());

	}

	@Test
	public void getExceptionType() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.EXCEPTION_TYPE , audit.getExceptionType());


	}

	@Test
	public void isNeverDelete() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.NEVER_DELETE , audit.isNeverDelete());

	}

	@Test
	public void getParameterValue() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.PARAMETER_VALUE , audit.getParameterValue());

	}

	@Test
	public void getPriority() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.PRIORITY , audit.getPriority());

	}

	@Test
	public void isActive() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.ACTIVE , audit.isActive());

	}

	@Test
	public void getId() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.ID , audit.getId());

	}

	@Test
	public void getSequenceNumber() {
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.SEQUENCE_NUMBER , audit.getSequenceNumber());

	}

	@Test
	public void getAction(){
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.ACTION , audit.getAction());

	}

	@Test
	public void getTimestamp(){
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.TIMESTAMP , audit.getTimestamp());

	}

	@Test
	public void getUserId(){
		DiscontinueExceptionParametersAudit audit = this.getDefaultRecord();
		Assert.assertEquals(DiscontinueExceptionParametersAuditTest.USER_ID , audit.getUserId());

	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a DiscontinueExceptionParametersAudit object that is equal to the first one in the test table.
	 *
	 * @return Returns a DiscontinueExceptionParametersAudit object that is equal to the first one in the test table.
	 */
	 private DiscontinueExceptionParametersAudit getDefaultRecord(){

		 DiscontinueExceptionParametersAudit audit = new DiscontinueExceptionParametersAudit();
		 audit.setExceptionNumber(DiscontinueExceptionParametersAuditTest.EXCEPTION_NUMBER);
		 audit.setExceptionTypeId(DiscontinueExceptionParametersAuditTest.EXCEPTION_TYPE_ID);
		 audit.setExceptionType(DiscontinueExceptionParametersAuditTest.EXCEPTION_TYPE);
		 audit.setNeverDelete(DiscontinueExceptionParametersAuditTest.NEVER_DELETE);
		 audit.setParameterValue(DiscontinueExceptionParametersAuditTest.PARAMETER_VALUE);
		 audit.setPriority(DiscontinueExceptionParametersAuditTest.PRIORITY);
		 audit.setActive(DiscontinueExceptionParametersAuditTest.ACTIVE);
		 audit.setId(DiscontinueExceptionParametersAuditTest.ID);
		 audit.setSequenceNumber(DiscontinueExceptionParametersAuditTest.SEQUENCE_NUMBER);
		 audit.setAction(DiscontinueExceptionParametersAuditTest.ACTION);
		 audit.setTimestamp(DiscontinueExceptionParametersAuditTest.TIMESTAMP);
		 audit.setUserId(DiscontinueExceptionParametersAuditTest.USER_ID);
		 return audit;
	 }

	private DiscontinueExceptionParametersAudit getAlternateRecord(){
		DiscontinueExceptionParametersAudit audit = new DiscontinueExceptionParametersAudit();
		audit.setExceptionNumber(20);
		audit.setExceptionTypeId("9102411           ");
		audit.setExceptionType("Vendor              ");
		audit.setNeverDelete(false);
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
