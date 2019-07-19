/*
 *
 *  DiscontinueParametersCommonAuditTest
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
/**
 * Tests DiscontinueParametersCommonAudit.
 *
 * @author s573181
 * @since 2.0.3
 */
public class DiscontinueParametersCommonAuditTest {
	private static final int ID = 1;
	private static final int SEQUENCE_NUMBER = 2;
	private static final String PARAMETER_VALUE = "540";
	private static final int PRIORITY = 0;
	private static final boolean ACTIVE = true;
	private static final String ACTION = "ADD";
	private static final String TIMESTAMP = "2012-05-11 06:09:45.311000";
	private static final String USER_ID = "vn44230";

	/*
	 * getters
	 */

	/**
	 * Tests
	 */
	@Test
	public void getUserId() {
		DiscontinueParametersCommonAudit audit = this.getTestObject();
		Assert.assertEquals(DiscontinueParametersCommonAuditTest.USER_ID, audit.getUserId());
	}


	/**
	 * Tests getParameterValue
	 */
	@Test
	public void getParameterValue() {
		DiscontinueParametersCommonAudit audit = this.getTestObject();
		Assert.assertEquals(DiscontinueParametersCommonAuditTest.PARAMETER_VALUE, audit.getParameterValue());
	}

	/**
	 * Tests getPriority
	 */
	@Test
	public void getPriority() {
		DiscontinueParametersCommonAudit audit = this.getTestObject();
		Assert.assertEquals(DiscontinueParametersCommonAuditTest.PRIORITY, audit.getPriority());
	}

	/**
	 * Tests isActive
	 */
	@Test
	public void isActive() {
		DiscontinueParametersCommonAudit audit = this.getTestObject();
		Assert.assertEquals(DiscontinueParametersCommonAuditTest.ACTIVE, audit.isActive());
	}

	/**
	 * Tests getAction
	 */
	@Test
	public void getAction() {
		DiscontinueParametersCommonAudit audit = this.getTestObject();
		Assert.assertEquals(DiscontinueParametersCommonAuditTest.ACTION, audit.getAction());
	}


	/*
	 * Support functions.
	 */

	/**
	 * Returns a DiscontinueParametersCommonAudit to test with.
	 *
	 * @return A DiscontinueParametersCommonAudit to test with.
	 */
	private DiscontinueParametersCommonAudit getTestObject(){
		DiscontinueParametersCommonAudit audit = new DiscontinueParametersCommonAudit();
		audit.setParameterValue(PARAMETER_VALUE);
		audit.setPriority(PRIORITY);
		audit.setActive(ACTIVE);
		audit.setAction(ACTION);
		audit.setUserId(USER_ID);
		return audit;
	}
}
