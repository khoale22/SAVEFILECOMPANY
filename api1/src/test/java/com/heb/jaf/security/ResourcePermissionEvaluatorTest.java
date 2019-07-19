/*
 *  com.heb.jaf.security.ResourcePermissionEvaluatorTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.jaf.security;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests ResourcePermissionEvaluator.
 *
 * @author d116773
 */
public class ResourcePermissionEvaluatorTest {

	private static Permission permission;

	private static HebUserDetails userDetails;
	private static HebUserDetails userDetailsNoUserName;

	private static Authentication authentication;
	private static Authentication authenticationUserDetailsNoUserName;
	private static Authentication authenticationNoHebUserDetails;

	private static Serializable serializable;

	@AuthorizedResource("RN_TEST")
	private class AnnotatedClass {

	}

	@AuthorizedResource("XXXXX")
	private class AnnotatedClassDifferentResource {

	}

	private ResourcePermissionEvaluator resourcePermissionEvaluator = new ResourcePermissionEvaluator();

	@BeforeClass
	public static void setup() {
		// Create a permission object to put into the user details.
		ResourcePermissionEvaluatorTest.permission = new Permission();
		ResourcePermissionEvaluatorTest.permission.setResourceName("RN_TEST");
		ResourcePermissionEvaluatorTest.permission.setAccessType("AC_TEST");

		// It needs to be in a list.
		List<GrantedAuthority> authorities = new ArrayList<>(1);
		authorities.add(ResourcePermissionEvaluatorTest.permission);

		// Also need a list of authoroties that are not permissions
		GrantedAuthority ga = Mockito.mock(GrantedAuthority.class);
		List<GrantedAuthority> authoritiesNotPermissions = new ArrayList<>(1);
		authoritiesNotPermissions.add(ga);

		// Mock up the HebUserDetails object.
		ResourcePermissionEvaluatorTest.userDetails = Mockito.mock(HebUserDetails.class);
		Mockito.when(ResourcePermissionEvaluatorTest.userDetails.getAuthorities())
				.thenReturn(authorities);
		Mockito.when(ResourcePermissionEvaluatorTest.userDetails.getUsername()).thenReturn("PGDV");

		// Mock a HebUserDetails object with no user name.
		ResourcePermissionEvaluatorTest.userDetailsNoUserName = Mockito.mock(HebUserDetails.class);
		Mockito.when(ResourcePermissionEvaluatorTest.userDetailsNoUserName.getAuthorities())
				.thenReturn(authoritiesNotPermissions);

		// Mock the authentication object.
		ResourcePermissionEvaluatorTest.authentication = Mockito.mock(Authentication.class);
		Mockito.when(ResourcePermissionEvaluatorTest.authentication.getPrincipal())
				.thenReturn(ResourcePermissionEvaluatorTest.userDetails);

		// Mock the authentication object that returns a HebUserDetails with no user name
		ResourcePermissionEvaluatorTest.authenticationUserDetailsNoUserName = Mockito.mock(Authentication.class);
		Mockito.when(ResourcePermissionEvaluatorTest.authenticationUserDetailsNoUserName.getPrincipal())
				.thenReturn(ResourcePermissionEvaluatorTest.userDetailsNoUserName);

		// Mock an authentication that does not return a HebUserDetails.
		ResourcePermissionEvaluatorTest.authenticationNoHebUserDetails = Mockito.mock(Authentication.class);
		Mockito.when(ResourcePermissionEvaluatorTest.authenticationNoHebUserDetails.getPrincipal())
				.thenReturn("Not a HEB UserDetails");

		// Mock a serializable.
		ResourcePermissionEvaluatorTest.serializable = Mockito.mock(Serializable.class);
	}

	/*
	 * hasPermission with four parameters
	 */

	/**
	 * hasPermission with four parameters is unimplemented and should return false.
	 */
	@Test
	public void testHasPermissionFourParameter() {
		Assert.assertFalse("four parameter version did not return false",
				this.resourcePermissionEvaluator.hasPermission(ResourcePermissionEvaluatorTest.authentication,
						ResourcePermissionEvaluatorTest.serializable, "Test String", "Test Object"
				));
	}

	/*
	 * hasPermission with three parameters no annotations
	 */

	/**
	 * Tests hasPermission with a good principal and no annotations on the objects passed in and the user does
	 * have access.
	 */
	@Test
	public void testHasPermissionGoodPrincipalNonAnnotationHasPermission() {
		Assert.assertTrue(this.resourcePermissionEvaluator.hasPermission(
				ResourcePermissionEvaluatorTest.authentication,
				"RN_TEST", "AC_TEST"));
	}

	/**
	 * Tests hasPermission with a good principal and no annotations on the objects passed in and the user does no
	 * have access based on access type.
	 */
	@Test
	public void testHasPermissionGoodPrincipalNonAnnotationDoesNotHavePermissionAccessType() {
		Assert.assertFalse(this.resourcePermissionEvaluator.hasPermission(
				ResourcePermissionEvaluatorTest.authentication,
				"RN_TEST", "XXXXX"));
	}

	/**
	 * Tests hasPermission with a good principal and no annotations on the objects passed in and the user does no
	 * have access based on resource name.
	 */
	@Test
	public void testHasPermissionGoodPrincipalNonAnnotationDoesNotHavePermissionResourceName() {
		Assert.assertFalse(this.resourcePermissionEvaluator.hasPermission(
				ResourcePermissionEvaluatorTest.authentication,
				"XXXXXX", "AC_TEST"));
	}

	/**
	 * Tests hasPermission when the principal is not a HebUserDetails object.
	 */
	@Test
	public void testHasPermissionBadPrincipal() {
		Assert.assertFalse(this.resourcePermissionEvaluator.hasPermission(
				ResourcePermissionEvaluatorTest.authenticationNoHebUserDetails,
				"RN_TEST", "AC_TEST"));
	}

	/**
	 * Test hasPermission when the HebUserDetails does not have a user name.
	 */
	@Test
	public void testHasPermissionNoUserName() {
		Assert.assertFalse(this.resourcePermissionEvaluator.hasPermission(
				ResourcePermissionEvaluatorTest.authenticationUserDetailsNoUserName,
				"RN_TEST", "AC_TEST"));
	}
	/*
	 * hasPermission three parameters annotation.
	 */

	/**
	 * Test hasPermission when the user has access and the object passed in is annotated with AuthorizedResource.
	 */
	@Test
	public void testHasPermissionAnnotatedObjectHasAccess() {
		Assert.assertTrue(this.resourcePermissionEvaluator.hasPermission(
				ResourcePermissionEvaluatorTest.authentication,
				new AnnotatedClass(), "AC_TEST"	));
	}

	/**
	 * Test hasPermission when the user does not have access and the object passed in
	 * is annotated with AuthorizedResource based on access type.
	 */
	@Test
	public void testHasPermissionAnnotatedObjectDoesNotHaveAccessByType() {
		Assert.assertFalse(this.resourcePermissionEvaluator.hasPermission(
				ResourcePermissionEvaluatorTest.authentication,
				new AnnotatedClass(), "XXXXX"));
	}

	/**
	 * Test hasPermission when the user does not have access and the object passed in
	 * is annotated with AuthorizedResource based on resource name.
	 */
	@Test
	public void testHasPermissionAnnotatedObjectDoesNotHaveAccessByResource() {
		Assert.assertFalse(this.resourcePermissionEvaluator.hasPermission(
				ResourcePermissionEvaluatorTest.authentication,
				new AnnotatedClassDifferentResource(), "AC_TEST"));
	}
}
