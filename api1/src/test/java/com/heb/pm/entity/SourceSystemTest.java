/*
 * SourceSystemTest
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

import java.util.ArrayList;
import java.util.List;

/**
 * Tests SourceSystem.
 *
 * @author d116773
 * @update s753601
 * @since 2.5.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class SourceSystemTest {

	public static final int ID = 534;
	public static final String DESCRIPTION = "Direct Ship Vendor";

	@Autowired
	SourceSystemRepositoryTest repo;

	/*
	 * JPA mapping
	 */

	@Test
	public void jpaMapping() {
		SourceSystem s = this.repo.findOne(491);
		Assert.assertEquals(491, s.getId());
		Assert.assertEquals("STORE                                             ", s.getDescription());
	}
	/*
	 * equals
	 */

	/**
	 * Tests equals when passed the same object.
	 */
	@Test
	public void equalsSameObject() {
		SourceSystem s = this.getSourceSystem();
		boolean equals = s.equals(s);
		Assert.assertTrue(equals);
	}

	/**
	 * Tests equals when passed an equal object.
	 */
	@Test
	public void equalsEqualObjects() {
		SourceSystem s1 = this.getSourceSystem();
		SourceSystem s2 = this.getSourceSystem();
		boolean eq = s1.equals(s2);
		Assert.assertTrue(eq);
	}

	/**
	 * Tests equals when passed an object that is not equal.
	 */
	@Test
	public void equalsNotEquals() {
		SourceSystem s1 = this.getSourceSystem();
		SourceSystem s2 = this.getSourceSystem();
		s2.setId(s2.getId() + 1);
		boolean eq = s1.equals(s2);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed null.
	 */
	@Test
	public void equalsNull() {
		SourceSystem s = this.getSourceSystem();
		boolean eq = s.equals(null);
		Assert.assertFalse(eq);
	}

	/**
	 * Tests equals when passed an object of a different type.
	 */
	@Test
	public void equalsDifferentType() {
		SourceSystem s = this.getSourceSystem();
		boolean eq = s.equals(Long.valueOf(s.getId()));
		Assert.assertFalse(eq);
	}

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode is consistent across calls.
	 */
	@Test
	public void hashCodeSameObject() {
		SourceSystem s = this.getSourceSystem();
		Assert.assertEquals(s.hashCode(), s.hashCode());
	}

	/**
	 * Tests hashCode is the same for equals objects.
	 */
	@Test
	public void hashCodeEqualObjects() {
		SourceSystem s1 = this.getSourceSystem();
		SourceSystem s2 = this.getSourceSystem();
		Assert.assertEquals(s1.hashCode(), s2.hashCode());
	}

	/**
	 * Tests hashCode is different for different objects.
	 */
	@Test
	public void hashCodeNotEquals() {
		SourceSystem s1 = this.getSourceSystem();
		SourceSystem s2 = this.getSourceSystem();
		s2.setId(s2.getId() + 1);
		Assert.assertNotEquals(s1.hashCode(), s2.hashCode());
	}

	/*
	 * toString
	 */

	/**
	 * Tests toString.
	 */
	@Test
	public void testToString() {
		Assert.assertEquals("SourceSystem{id=534, description='Direct Ship Vendor'}",
				this.getSourceSystem().toString());
	}

	/*
	 * getters
	 */

	/**
	 * Tests getId.
	 */
	@Test
	public void getId() {
		SourceSystem s = this.getSourceSystem();
		Assert.assertEquals(SourceSystemTest.ID, s.getId());
	}

	/**
	 * Tests getDescription.
	 */
	@Test
	public void getDescription() {
		SourceSystem s = this.getSourceSystem();
		Assert.assertEquals(SourceSystemTest.DESCRIPTION, s.getDescription());
	}


	/**
	 * Tests getTargetSystemAttributePriorities
	 */
	@Test
	public void getTargetSystemAttributePrioritiesTest(){
		SourceSystem test = getSourceSystem();
		Assert.assertEquals(getDefaultTargetSystemAttributePriority().toString(), test.getTargetSystemAttributePriorities().get(0).toString());
	}

	/**
	 * Tests setTargetSystemAttributePriorities
	 */
	@Test
	public void setTargetSystemAttributePrioritiesTest(){
		SourceSystem test = getSourceSystem();
		Assert.assertEquals(getDefaultTargetSystemAttributePriority().toString(), test.getTargetSystemAttributePriorities().get(0).toString());
		Assert.assertEquals(1, test.getTargetSystemAttributePriorities().size());
		List<TargetSystemAttributePriority> newList = getDefaultTargetSystemAttributePriorityList();
		newList.add(getDefaultTargetSystemAttributePriority());
		test.setTargetSystemAttributePriorities(newList);
		Assert.assertEquals(2, test.getTargetSystemAttributePriorities().size());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a SourceSystem to test with.
	 *
	 * @return A SourceSystem to test with.
	 */
	private SourceSystem getSourceSystem() {
		SourceSystem s = new SourceSystem();

		s.setId(SourceSystemTest.ID);
		s.setDescription(SourceSystemTest.DESCRIPTION);

		s.setTargetSystemAttributePriorities(getDefaultTargetSystemAttributePriorityList());

		return s;
	}

	/**
	 * Generates a generic Target System Attribute Priority object for testing
	 * @return a generic Target System Attribute Priority object for testing
	 */
	private TargetSystemAttributePriority getDefaultTargetSystemAttributePriority(){
		TargetSystemAttributePriority targetSystemAttributePriority = new TargetSystemAttributePriority();
		targetSystemAttributePriority.setAttributePriorityNumber(1);
		return targetSystemAttributePriority;
	}

	/**
	 * Generates a generic Target System Attribute Priority List object for testing
	 * @return a generic Target System Attribute Priority List object for testing
	 */
	private List<TargetSystemAttributePriority> getDefaultTargetSystemAttributePriorityList(){
		ArrayList<TargetSystemAttributePriority> targetSystemAttributePriorityList = new ArrayList<>();
		targetSystemAttributePriorityList.add(getDefaultTargetSystemAttributePriority());
		return targetSystemAttributePriorityList;
	}
}
