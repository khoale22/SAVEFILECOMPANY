/*
 * BdmTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.heb.pm.repository.BdmRepositoryTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests Bdm.
 *
 * @author m314029
 * @since 2.0.6
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class BdmTest {

	private static final String BDM_CODE = "A1   ";
	private static final String FIRST_NAME = "KAREN               ";
	private static final String LAST_NAME = "CASSADY             ";
	private static final String FULL_NAME = "KAREN CASSADY                 ";

	@Autowired
	private BdmRepositoryTest repository;

	/*
	 * getters
	 */

	/**
	 * Tests getBdmCode.
	 */
	@Test
	public void getBdmCode() throws Exception {
		Bdm bdm = this.getTestBdm();
		Assert.assertEquals(bdm.getBdmCode(), this.getTestKey());
	}

	/**
	 * Tests getFirstName.
	 */
	@Test
	public void getFirstName() throws Exception {
		Bdm bdm = this.getTestBdm();
		Assert.assertEquals(FIRST_NAME, bdm.getFirstName());
	}

	/**
	 * Tests getLastName.
	 */
	@Test
	public void getLastName() throws Exception {
		Bdm bdm = this.getTestBdm();
		Assert.assertEquals(LAST_NAME, bdm.getLastName());
	}

	/**
	 * Tests getFullName.
	 */
	@Test
	public void getFullName() throws Exception {
		Bdm bdm = this.getTestBdm();
		Assert.assertEquals(FULL_NAME, bdm.getFullName());
	}

	/**
	 * Tests getDisplayName.
	 */
	@Test
	public void getDisplayName() throws Exception {
		Bdm bdm = this.getTestBdm();
		String displayName = bdm.getFullName().trim() + "[" + bdm.getBdmCode().trim() + "]";
		Assert.assertEquals(displayName, bdm.getDisplayName());
	}

	/**
	 * Tests getNormalizedId.
	 */
	@Test
	public void getNormalizedId() throws Exception {
		Bdm bdm = this.getTestBdm();
		Assert.assertEquals(BDM_CODE, bdm.getNormalizedId());
	}

	/*
	 * equals
	 */

	/**
	 * Tests equals when passed itself.
	 */
	@Test
	public void equalsSameObject() {
		Bdm bdm = this.getTestBdm();
		boolean eq = bdm.equals(bdm);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals on an object with all matching values.
	 */
	@Test
	public void equalsEqualObjects() {
		Bdm bdm1 = this.getTestBdm();
		Bdm bdm2 = this.getTestBdm();
		boolean eq = bdm1.equals(bdm2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when only the keys match.
	 */
	@Test
	public void equalsKeysOnly() {
		Bdm bdm1 = this.getTestBdm();
		Bdm bdm2 = new Bdm();
		bdm2.setBdmCode(this.getTestBdm().getBdmCode());
		boolean eq = bdm1.equals(bdm2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when the keys are different.
	 */
	@Test
	public void equalsDifferentKey() {
		Bdm bdm1 = this.getTestBdm();
		Bdm bdm2 = this.getTestBdm();
		bdm2.setBdmCode(bdm2.getBdmCode() + 1);
		boolean eq = bdm1.equals(bdm2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed a null.
	 */
	@Test
	public void equalsNull() {
		Bdm bdm = this.getTestBdm();
		boolean eq = bdm == null;
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		Bdm bdm = this.getTestBdm();
		boolean eq = bdm.equals(this.getTestBdm().getBdmCode());
		Assert.assertFalse(eq);
	}

	/*
	 * hashCode
	 */

	/**
	 * Makes sure hashCode equals the key's hashCode.
	 */
	@Test
	public void hashCodeEqualsKey() {
		Assert.assertEquals(this.getTestBdm().hashCode(), this.getTestBdm().getBdmCode().hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() throws Exception {
		Bdm bdm = this.getTestBdm();
		Assert.assertEquals(bdm.toString(), "BDM{bdmCode='A1   ', firstName='KAREN               ', " +
				"lastName='CASSADY             ', fullName='KAREN CASSADY                 '}");
	}

	/*
	 * mapping
	 */

	/**
	 * Test Jpa mapping.
	 */
	@Test
	public void jpaMapping() {
		String key = this.getTestKey();
		Bdm bdm = this.repository.findOne(key);
		Assert.assertEquals(BDM_CODE, bdm.getBdmCode());
		Assert.assertEquals(FIRST_NAME, bdm.getFirstName());
		Assert.assertEquals(LAST_NAME, bdm.getLastName());
		Assert.assertEquals(FULL_NAME, bdm.getFullName());
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a Bdm to test with.
	 *
	 * @return A Bdm to test with.
	 */
	private Bdm getTestBdm() {

		Bdm bdm = new Bdm();
		bdm.setBdmCode(BDM_CODE);
		bdm.setFirstName(FIRST_NAME);
		bdm.setLastName(LAST_NAME);
		bdm.setFullName(FULL_NAME);
		return bdm;
	}

	/**
	 * Returns a key to test with.
	 *
	 * @return A key to test with.
	 */
	private String getTestKey() {

		return BDM_CODE;
	}
}
