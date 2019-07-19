/*
 *  com.heb.jaf.security.AuthenticationFailureHandlerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.jaf.security;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Tests AuthenticationFailureHandler
 *
 * @author d116773
 */
public class AuthenticationFailureHandlerTest {

	AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailureHandler();

	AuthenticationException exception = Mockito.mock(AuthenticationException.class);

	Answer<Void> checkServletResponse = invocation -> {
		Assert.assertEquals("did not set response to unauthorized", HttpServletResponse.SC_UNAUTHORIZED,
				invocation.getArguments()[0]);
		return null;
	};

	@Test
	public void testFailedLoginAllValuesPresent() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getParameter("username")).thenReturn("test-id");
		Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.0");

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(this.checkServletResponse).when(response).setStatus(Mockito.anyInt());

		try {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response, this.exception);
		} catch (IOException | ServletException e){
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFailedLoginNoIP() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getParameter("username")).thenReturn("test-id");

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(this.checkServletResponse).when(response).setStatus(Mockito.anyInt());

		try {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response, this.exception);
		} catch (IOException | ServletException e){
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFailedLoginNoUser() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.0");

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(this.checkServletResponse).when(response).setStatus(Mockito.anyInt());

		try {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response, this.exception);
		} catch (IOException | ServletException e){
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFailedLoginNoUser2() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.0");

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(this.checkServletResponse).when(response).setStatus(Mockito.anyInt());

		try {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response, this.exception);
		} catch (IOException | ServletException e){
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test that onAuthenticationFailure handles errors while logging gracefully.
	 */
	@Test
	public void testLoginFailedThrowsError() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getParameter("username")).thenReturn("test-id");
		Mockito.when(request.getRemoteAddr()).thenThrow(new IllegalArgumentException("thrown when getting IP"));

		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(this.checkServletResponse).when(response).setStatus(Mockito.anyInt());

		try {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response, this.exception);
		} catch (IOException | ServletException e){
			Assert.fail(e.getMessage());
		}
	}
}
