/*
 * HebUserDetailsMapperTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 *
 */

package com.heb.jaf.security;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Tests HebUserDetailsMaper.
 *
 * @author d116773
 * @since 2.0.0
 */
public class HebUserDetailsMapperTest {

	public static final String DISPLAY_NAME = "DISPLAY NAME";
	public static final String JOB_CODE = "JOB CODE";
	public static final String JOB_DESCRIPTION = "JOB DESCRIPTION";
	public static final String DEPARTMENT = "DEPARTMENT";
	public static final String MAIL = "MAIL";
	public static final String MOBILE = "MOBILE";
	public static final String GL_LOCATION = "GL LOCATION";
	public static final String USERNAME = "PGDV";
	private HebUserDetailsMapper mapper = new HebUserDetailsMapper();

	/**
	 * Tests mapUSErFromContext with a null user context.
	 */
	@Test
	public void mapUserFromContextNullContext() {
		try {
			UserDetails userDetails = this.mapper.mapUserFromContext(null, HebUserDetailsMapperTest.USERNAME,
					this.getGrantedAuthorities());
			Assert.fail("did not throw exception");
		} catch (IllegalArgumentException e) {
			// this is supposed to happen.
		}
	}

	/**
	 * Tests mapUserFromContext with a null user ID.
	 */
	@Test
	public void mapUserFromContextNullUser() {
		try {
			UserDetails userDetails = this.mapper.mapUserFromContext(this.getContextOperations(), null,
					this.getGrantedAuthorities());
			Assert.fail("did not throw exception");
		} catch (IllegalArgumentException e) {
			// this is supposed to happen.
		}
	}

	/**
	 * Tests mapUerFromContext with null Authorities.
	 */
	@Test
	public void mapUserFromContextNullAuthorities() {
		UserDetails userDetails = this.mapper.mapUserFromContext(this.getContextOperations(),
				HebUserDetailsMapperTest.USERNAME, null);
		Assert.assertTrue(userDetails instanceof HebUserDetails);
		this.checkEverythingButAuthorities((HebUserDetails) userDetails);
		Assert.assertTrue(userDetails.getAuthorities().isEmpty());
	}

	/**
	 * Tests mapUserFromContext with all data.
	 */
	@Test
	public void mapUserFromContextAllData() {
		UserDetails userDetails = this.mapper.mapUserFromContext(this.getContextOperations(),
				HebUserDetailsMapperTest.USERNAME, this.getGrantedAuthorities());
		Assert.assertTrue(userDetails instanceof HebUserDetails);
		this.checkEverythingButAuthorities((HebUserDetails) userDetails);
		Assert.assertEquals("RESOURCE-ACCESS", userDetails.getAuthorities().iterator().next().getAuthority());
	}


	/*
	 * Support functions.
	 */

	/**
	 * Checks to make sure the user details get set to the right values.
	 *
	 * @param userDetails The HebUserDetails to check.
	 */
	public void checkEverythingButAuthorities(HebUserDetails userDetails) {
		Assert.assertEquals(userDetails.getUsername(), HebUserDetailsMapperTest.USERNAME);
		Assert.assertEquals(userDetails.getDisplayName(), HebUserDetailsMapperTest.DISPLAY_NAME);
		Assert.assertEquals(userDetails.getHebJobCode(), HebUserDetailsMapperTest.JOB_CODE);
		Assert.assertEquals(userDetails.getHebJobDesc(), HebUserDetailsMapperTest.JOB_DESCRIPTION);
		Assert.assertEquals(userDetails.getDepartmentNumber(), HebUserDetailsMapperTest.DEPARTMENT);
		Assert.assertEquals(userDetails.getMail(), HebUserDetailsMapperTest.MAIL);
		Assert.assertEquals(userDetails.getMobile(), HebUserDetailsMapperTest.MOBILE);
		Assert.assertEquals(userDetails.getHebGLlocation(), HebUserDetailsMapperTest.GL_LOCATION);
	}

	/**
	 * Returns a list of authorities.
	 *
	 * @return A list of authorities.
	 */
	private Collection<GrantedAuthority> getGrantedAuthorities() {
		Permission p = new Permission();
		p.setResourceName("RESOURCE");
		p.setAccessType("ACCESS");

		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(p);
		return grantedAuthorities;
	}

	/**
	 * Mocks the DirContextOperations that the class under test needs.
	 *
	 * @return A DirContextOperations that will return values needed by HebUserDetailsMapper.
	 */
	private DirContextOperations getContextOperations() {
		DirContextOperations operations = Mockito.mock(DirContextOperations.class);
		Mockito.when(operations.getStringAttribute("displayName")).thenReturn(HebUserDetailsMapperTest.DISPLAY_NAME);
		Mockito.when(operations.getStringAttribute("hebJobCode")).thenReturn(HebUserDetailsMapperTest.JOB_CODE);
		Mockito.when(operations.getStringAttribute("hebJobDesc")).thenReturn(HebUserDetailsMapperTest.JOB_DESCRIPTION);
		Mockito.when(operations.getStringAttribute("departmentNumber")).thenReturn(HebUserDetailsMapperTest.DEPARTMENT);
		Mockito.when(operations.getStringAttribute("mail")).thenReturn(HebUserDetailsMapperTest.MAIL);
		Mockito.when(operations.getStringAttribute("mobile")).thenReturn(HebUserDetailsMapperTest.MOBILE);
		Mockito.when(operations.getStringAttribute("hebGLlocation")).thenReturn(HebUserDetailsMapperTest.GL_LOCATION);

		return operations;
	}

}
