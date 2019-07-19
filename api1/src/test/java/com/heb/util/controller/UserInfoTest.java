/*
 *  com.heb.util.controller.UserInfoTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.util.controller;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * @author d116773
 */
public class UserInfoTest {

	private UserInfo userInfo = new UserInfo();

	/**
	 * Test the happy path.
	 */
	@Test
	public void testGetUserId() {
		SecurityContextHolder.setContext(this.getSecurityContext(this.getAuthentication(this.getUser())));

		String userName = this.userInfo.getUserId();
		Assert.assertEquals("user ID does not match", "PGDV", userName);
	}

	/**
	 * Test null user name.
	 */
	@Test
	public void testGetUserIdNullContext() {
		SecurityContextHolder.setContext(this.getSecurityContext(this.getAuthentication(this.getUserNullName())));

		String userName = this.userInfo.getUserId();
		Assert.assertEquals("user ID does not match", "Anonymous", userName);
	}

	/**
	 * Test null user.
	 */
	@Test
	public void testGetUserIdNullUser() {
		SecurityContextHolder.setContext(this.getSecurityContext(this.getAuthentication(null)));

		String userName = this.userInfo.getUserId();
		Assert.assertEquals("user ID does not match", "Anonymous", userName);
	}

	/**
	 * Test null authentication.
	 */
	@Test
	public void testGetUserIdNullAuthentication() {
		SecurityContextHolder.setContext(this.getSecurityContext(null));

		String userName = this.userInfo.getUserId();
		Assert.assertEquals("user ID does not match", "Anonymous", userName);
	}

	/**
	 * Test principal not a user
	 */
	@Test
	public void testGetUserIdPrincipalNotUser() {
		SecurityContextHolder.setContext(this.getSecurityContext(this.getAuthentication(Integer.valueOf(0))));

		String userName = this.userInfo.getUserId();
		Assert.assertEquals("user ID does not match", "Anonymous", userName);
	}

	/*
	 * Support functions.
	 */
	private User getUserNullName() {
		User user = Mockito.mock(User.class);
		Mockito.when(user.getUsername()).thenReturn(null);
		return user;
	}

	private User getUser() {
		User user = Mockito.mock(User.class);
		Mockito.when(user.getUsername()).thenReturn("PGDV");
		return user;
	}

	private Authentication getAuthentication(Object user) {
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getPrincipal()).thenReturn(user);
		return authentication;
	}

	private SecurityContext getSecurityContext(Authentication authentication) {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		return securityContext;
	}
}
