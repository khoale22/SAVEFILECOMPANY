/*
 *  com.heb.jaf.security.PermissionTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.jaf.security;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests Permission.
 *
 * @author d116773
 */
public class PermissionTest {

	/*
	 * toString
	 */
	@Test
	public void testToString() {
		Permission p = this.getPermission1();
		Assert.assertEquals("toString did not return same as getAuthority", p.toString(), p.getAuthority());
	}

	/*
	 * getAuthority
	 */

	/**
	 * Tests getAuthority when resourceName and accessType are set.
	 */
	@Test
	public void testGetAuthorityGoodValues() {
		Permission p = this.getPermission1();
		Assert.assertEquals("get authority wrong value", "RN_TEST-AC_TEST", p.getAuthority());
	}

	/**
	 * Tests getAuthority when resourceName is null.
	 */
	@Test
	public void testGetAuthorityNoResourceName() {
		Permission p = new Permission();
		p.setAccessType("AC_TEST");
		Assert.assertEquals("get authority wrong value", "NULL-AC_TEST", p.getAuthority());
	}

	/**
	 * Tests getAuthority when accessType is null.
	 */
	@Test
	public void testGetAuthorityNoAccessType() {
		Permission p = new Permission();
		p.setResourceName("RN_TEST");
		Assert.assertEquals("get authority wrong value", "RN_TEST-NULL", p.getAuthority());
	}

	/**
	 * Tests getAuthority when both are null.
	 */
	@Test
	public void testGetAuthorityAllNull() {
		Permission p = new Permission();
		Assert.assertEquals("get authority wrong value", "NULL-NULL", p.getAuthority());
	}

	/*
	 * equals
	 */

	/**
	 * Tests equals when they are the same object.
	 */
	@Test
	public void testEuqlasSameObject() {
		Permission p = this.getPermission1();
		boolean equals = p.equals(p);
		Assert.assertTrue("same object equals fails", equals);
	}

	/**
	 * Tests equals when passed in a null.
	 */
	@Test
	public void testEqualsSecondNull() {
		Permission p = this.getPermission1();
		boolean equals = p.equals(null);
		Assert.assertFalse("said null is equals", equals);
	}

	/**
	 * Test equals when passed in a different type of object.
	 */
	@Test
	public void testEqualsNotPermission() {
		Permission p = this.getPermission1();
		boolean equals = p.equals(Integer.valueOf(0));
		Assert.assertFalse("said integer was equal", equals);
	}

	/**
	 * Tests equals when the objects have the same values but are not the same object.
	 */
	@Test
	public void testEqualsSimilarObject() {
		Permission p1 = this.getPermission1();
		Permission p2 = this.getPermission1();
		boolean equals = p1.equals(p2);
		Assert.assertTrue("similar object not equal", equals);
	}

	/**
	 * Tests equals on an object with a different resource name.
	 */
	@Test
	public void testEqualsDifferentResouceName() {
		Permission p1 = this.getPermission1();
		Permission p2 = this.getPermission3();
		boolean equals = p1.equals(p2);
		Assert.assertFalse("different object not equal", equals);
	}

	/**
	 * Tests equals on an object with a different access type.
	 */
	@Test
	public void testEqualsDifferentAccessType() {
		Permission p1 = this.getPermission1();
		Permission p2 = this.getPermission2();
		boolean equals = p1.equals(p2);
		Assert.assertFalse("different object not equal", equals);
	}

	/*
	 * checkAccess
	 */

	/**
	 * Test checkAccess when passed in values that match the object.
	 */
	@Test
	public void testCheckAccessGoodValues() {
		Permission p = this.getPermission1();
		boolean ok = p.checkAccess("RN_TEST", "AC_TEST");
		Assert.assertTrue("did not check access correctly", ok);
	}

	/**
	 * Test checkAccess when access type does not match.
	 */
	@Test
	public void testCheckAccessBadAccessType() {
		Permission p = this.getPermission1();
		boolean ok = p.checkAccess(p.getResourceName(), "bad");
		Assert.assertFalse("bad access type fails", ok);
	}

	/**
	 * Test checkAccess when resource name does not match.
	 */
	@Test
	public void testCheckAccessBadResourceName() {
		Permission p = this.getPermission1();
		boolean ok = p.checkAccess("bad", p.getAccessType());
		Assert.assertFalse("bad resource name fails", ok);
	}

	/**
	 * Tests checkAccess when resource name is null in both places.
	 */
	@Test
	public void testCheckAccessNullResourceNameBoth() {
		Permission p = new Permission();
		p.setAccessType("ttt");
		boolean ok =  p.checkAccess(null, "ttt");
		Assert.assertTrue("null both fails", ok);
	}

	/**
	 * Tests checkAccess when resource name is null but other has a value.
	 */
	@Test
	public void testCheckAccessNullResourceNameSource() {
		Permission p = new Permission();
		p.setAccessType("ttt");
		boolean ok =  p.checkAccess("bad", "ttt");
		Assert.assertFalse("null one fails", ok);
	}

	/**
	 * Tests checkAccess when resource name is null but other has a value.
	 */
	@Test
	public void testCheckAccessNullResourceNameTarget() {
		Permission p = this.getPermission1();
		boolean ok =  p.checkAccess(null, "AC_TEST");
		Assert.assertFalse("null one fails", ok);
	}

	/**
	 * Test checkAccess when accessType is null for both.
	 */
	@Test
	public void testCheckAccessNullAccessTypeBoth() {
		Permission p = new Permission();
		p.setResourceName("RN_TEST");
		boolean ok = p.checkAccess("RN_TEST", null);
		Assert.assertTrue("access type null both fails", ok);
	}

	/**
	 * Test checkAccess when accessType is null on the other.
	 */
	@Test
	public void testCheckAccessNullAccessTypeOther() {
		Permission p = this.getPermission1();
		boolean ok = p.checkAccess("RN_TEST", null);
		Assert.assertFalse("access type null other fails", ok);
	}

	/**
	 * Test checkAccess when accessType is null on source.
	 */
	@Test
	public void testCheckAccessNulllAccessTypeSource() {
		Permission p = new Permission();
		p.setResourceName("RN_TEST");
		boolean ok = p.checkAccess("RN_TEST", "AC_TEST");
		Assert.assertFalse("access type null source fails", ok);
	}

	/*
	 * getResourceName
	 */

	/**
	 * Test get and set resouce name
	 */
	@Test
	public void testGetResouceName() {
		Permission p = this.getPermission1();
		Assert.assertEquals("resouce name not set", "RN_TEST", p.getResourceName());
	}

	/**
	 * Test when resource name is null.
	 */
	@Test
	public void testGetResourceNameNull() {
		Permission p = new Permission();
		Assert.assertNull("resource name not null", p.getResourceName());
	}

	/*
	 * getAccessType
	 */
	/**
	 * Test get and set access type.
	 */
	@Test
	public void testGetAccessType() {
		Permission p = this.getPermission1();
		Assert.assertEquals("access type not set", "AC_TEST", p.getAccessType());
	}

	/**
	 * Test when access type is null.
	 */
	@Test
	public void testGetAccessTypeNull() {
		Permission p = new Permission();
		Assert.assertNull("access type not null", p.getAccessType());
	}

	/*
	 * hashCode
	 */

	/**
	 * Test that when hash code and resource name is null is not the same as hash code when access type is null
	 */
	@Test
	public void testHashCodeDifferentNullsNotTheSame() {
		Permission p1 = new Permission();
		p1.setResourceName("RN_TEST");
		Permission p2 = new Permission();
		p2.setAccessType("AC_TEST");
		Assert.assertNotEquals("nulls make same hash code", p1.hashCode(), p2.hashCode());
	}

	/**
	 * Tests similar objects get different hash codes.
	 */
	@Test
	public void testHashCodeSimilarObjects() {
		Permission p1 = this.getPermission1();
		Permission p2 = this.getPermission1();
		Assert.assertEquals("hash codes not equal similar object", p1.hashCode(), p2.hashCode());
	}

	/**
	 * Test different objects get different hash codes.
	 */
	@Test
	public void testHashCodeDifferentObjects() {
		Permission p1 = this.getPermission1();
		Permission p2 = this.getPermission2();
		Assert.assertNotEquals("hash codes same for differnt objects", p1.hashCode(), p2.hashCode());
	}

	/*
	 * Support functions
	 */

	/**
	 * Return the basic Permission object for testing.
	 * @return
	 */
	private Permission getPermission1() {
		Permission p = new Permission();
		p.setAccessType("AC_TEST");
		p.setResourceName("RN_TEST");

		return p;
	}


	/**
	 * Return a Permssion with the same resource name as object 1 but a different access type.
	 * @return
	 */
	private Permission getPermission2() {
		Permission p = new Permission();
		p.setAccessType("AC_TEST2");
		p.setResourceName("RN_TEST");

		return p;
	}

	/**
	 * Return a Permission with the same access type as object 1 but a different resource name.
	 * @return
	 */
	private Permission getPermission3() {
		Permission p = new Permission();
		p.setAccessType("AC_TEST");
		p.setResourceName("RN_TES2T");

		return p;
	}
}
