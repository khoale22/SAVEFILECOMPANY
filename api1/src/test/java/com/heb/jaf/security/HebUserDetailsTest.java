/*
 * HebUserDetailsTest
 *
 * Copyright (c) 2010 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Tests HebUserDetails.
 *
 * @author d116773
 * @since 2.0.0
 */
public class HebUserDetailsTest {

	/*
	 * getters
	 */

	/**
	 * Tests getUserName.
	 */
	@Test
	public void getUserName() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("PGDV", hebUserDetails.getUsername());

	}

	/**
	 * Tests getDisplayName.
	 */
	@Test
	public void getDisplayName() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("DISPLAY NAME", hebUserDetails.getDisplayName());
	}

	/**
	 * Tests getJobCode.
	 */
	@Test
	public void getJobCode() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("JOB CODE", hebUserDetails.getHebJobCode());
	}

	/**
	 * Tests getJobDescription.
	 */
	@Test
	public void getJobDescription() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("JOB DESC", hebUserDetails.getHebJobDesc());
	}

	/**
	 * Tests getDepartmentNumber.
	 */
	@Test
	public void getDepartmentNumber() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("DEPARTMENT NUMBER", hebUserDetails.getDepartmentNumber());
	}

	/**
	 * Tests getMail.
	 */
	@Test
	public void getMail() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("MAIL", hebUserDetails.getMail());
	}

	/**
	 * Tests getMobile.
	 */
	@Test
	public void getMobile() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("MOBILE", hebUserDetails.getMobile());
	}

	/**
	 * Tests getVendorOrgId.
	 */
	@Test
	public void getVendorOrgId() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("VENDOR ORG ID", hebUserDetails.getVendorOrgId());
	}

	/**
	 * Tests getVendorOrgName.
	 */
	@Test
	public void getVendorOrgName() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("VENDOR ORG NAME", hebUserDetails.getVendorOrgName());
	}

	/**
	 * Tests getHebGlLocation.
	 */
	@Test
	public void getHebGlLocation() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals("GL LOCATION", hebUserDetails.getHebGLlocation());
	}

	/**
	 * Tests getAuthorities.
	 */
	@Test
	public void getAuthorities() {
		HebUserDetails hebUserDetails = this.getDefaultUserDetails();
		Assert.assertEquals(1, hebUserDetails.getAuthorities().size());
		Assert.assertEquals("RESOURCE-ACCESS", hebUserDetails.getAuthorities().iterator().next().getAuthority());
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a populated HebUserDetails object.
	 *
	 * @return A populated HebUserDetails object.
	 */
	private HebUserDetails getDefaultUserDetails() {

		Permission p = new Permission();
		p.setResourceName("RESOURCE");
		p.setAccessType("ACCESS");

		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(p);

		HebUserDetails hebUserDetails = new HebUserDetails("PGDV", grantedAuthorities);
		hebUserDetails.setDisplayName("DISPLAY NAME");
		hebUserDetails.setDepartmentNumber("DEPARTMENT NUMBER");
		hebUserDetails.setHebGLlocation("GL LOCATION");
		hebUserDetails.setHebJobCode("JOB CODE");
		hebUserDetails.setHebJobDesc("JOB DESC");
		hebUserDetails.setMail("MAIL");
		hebUserDetails.setMobile("MOBILE");
		hebUserDetails.setVendorOrgId("VENDOR ORG ID");
		hebUserDetails.setVendorOrgName("VENDOR ORG NAME");

		return hebUserDetails;
	}
}
