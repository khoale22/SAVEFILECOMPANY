/*
 *  com.heb.jaf.security.AuthenticationSuccessHandlerTest
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
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import testSupport.CallChecker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Test AuthenticationSuccessHandler.
 *
 * @author d116773
 */
public class AuthenticationSuccessHandlerTest {

	private AuthenticationSuccessHandler authenticationSuccessHandler = new AuthenticationSuccessHandler();

	private static HebUserDetails user;
	private static HebUserDetails userWithoutId;

	private static Authentication authenticationWithUser;
	private static Authentication authenticationWithUserWithoutId;
	private static Authentication authenticationWithNullUser;

	private static HttpServletRequest requestWithIp;
	private static HttpServletRequest requestWithoutIp;
	private static HttpServletRequest requestIpError;

	// This checks that the function sets the correct response type
	private class CheckSetContentType implements Answer<Void> {

		private boolean setContentTypeCalled;

		public CheckSetContentType() {
			this.setContentTypeCalled = false;
		}

		public boolean isSetContentTypeCalled() {
			return this.setContentTypeCalled;
		}

		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			this.setContentTypeCalled = true;
			Assert.assertEquals("wrong content type", "application/json", invocation.getArguments()[0]);
			return null;
		}
	}

	// This class checks that the right stuff gets written to the response.
	private class CheckPrint implements Answer<Void> {

		private boolean printCalled;
		private String authorityToCheck;

		public CheckPrint(String authorityToCheck) {
			this.printCalled = false;
			this.authorityToCheck = authorityToCheck;
		}

		public boolean isPrintCalled() {
			return this.printCalled;
		}

		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			this.printCalled = true;
			Assert.assertEquals("printed wrong object",
					this.authorityToCheck,
					invocation.getArguments()[0].toString());

			return null;
		}
	}

	@BeforeClass
	public static void setup() {

		// Set up and array of granted authorities
		Permission p = new Permission();
		p.setResourceName("RA_TEST");
		p.setAccessType("AC_TEST");

		Collection<GrantedAuthority> ga = new ArrayList<>(1);
		ga.add(p);

		// Mock up the User
		AuthenticationSuccessHandlerTest.user = Mockito.mock(HebUserDetails.class);
		Mockito.when(AuthenticationSuccessHandlerTest.user.getUsername()).thenReturn("PGDV");
		Mockito.when(AuthenticationSuccessHandlerTest.user.getDisplayName()).thenReturn("Test User");
		Mockito.when(AuthenticationSuccessHandlerTest.user.getAuthorities()).thenReturn(ga);

		// Mock the user with no ID
		AuthenticationSuccessHandlerTest.userWithoutId = Mockito.mock(HebUserDetails.class);
		Mockito.when(AuthenticationSuccessHandlerTest.userWithoutId.getUsername()).thenReturn(null);
		Mockito.when(AuthenticationSuccessHandlerTest.userWithoutId.getDisplayName()).thenReturn(null);
		Mockito.when(AuthenticationSuccessHandlerTest.userWithoutId.getAuthorities()).thenReturn(ga);


		// Mock up the Authentication with a user inside
		AuthenticationSuccessHandlerTest.authenticationWithUser = Mockito.mock(Authentication.class);
		Mockito.when(AuthenticationSuccessHandlerTest.authenticationWithUser.getPrincipal())
				.thenReturn(AuthenticationSuccessHandlerTest.user);

		// Mock up the Authentication with a user that has no ID
		AuthenticationSuccessHandlerTest.authenticationWithUserWithoutId = Mockito.mock(Authentication.class);
		Mockito.when(AuthenticationSuccessHandlerTest.authenticationWithUserWithoutId.getPrincipal())
				.thenReturn(AuthenticationSuccessHandlerTest.userWithoutId);

		// Mock up the Authentication without a user inside.
		AuthenticationSuccessHandlerTest.authenticationWithNullUser = Mockito.mock(Authentication.class);
		Mockito.when(AuthenticationSuccessHandlerTest.authenticationWithNullUser.getPrincipal())
				.thenReturn(null);

		// Mock up the servlet request that returns IP address
		AuthenticationSuccessHandlerTest.requestWithIp = Mockito.mock(HttpServletRequest.class);
		Mockito.when(AuthenticationSuccessHandlerTest.requestWithIp.getRemoteAddr()).thenReturn("127.0.0.0");

		// Mock up the servlet request that returns null for IP address
		AuthenticationSuccessHandlerTest.requestWithoutIp = Mockito.mock(HttpServletRequest.class);
		Mockito.when(AuthenticationSuccessHandlerTest.requestWithoutIp.getRemoteAddr()).thenReturn(null);

		// Mock up a servlet request that throws an error when you ask for IP address.
		AuthenticationSuccessHandlerTest.requestIpError = Mockito.mock(HttpServletRequest.class);
		Mockito.when(AuthenticationSuccessHandlerTest.requestIpError.getRemoteAddr()).thenThrow(
				new IllegalArgumentException("bad IP address")
		);
	}

	/**
	 * Test the happy path.
	 */
	@Test
	public void testOnAuthenticationSuccess() {
		CheckPrint checkPrint = new CheckPrint(
				"{\"id\":\"PGDV\",\"roles\":[\"RA_TEST-AC_TEST\"],\"name\":\"Test User\"}");

		// Mock up a PrintWriter
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		this.successfulCall(AuthenticationSuccessHandlerTest.requestWithIp,
				AuthenticationSuccessHandlerTest.authenticationWithUser, checkPrint);
	}

	/**
	 * Test when logSuccessfulLogin thorws an error.
	 */
	@Test
	public void testOnAuthenticationSuccessLogSuccessfulLoginThrowsError() {
		CheckPrint checkPrint = new CheckPrint(
				"{\"id\":\"PGDV\",\"roles\":[\"RA_TEST-AC_TEST\"],\"name\":\"Test User\"}");

		// Mock up a PrintWriter
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		this.successfulCall(AuthenticationSuccessHandlerTest.requestIpError,
				AuthenticationSuccessHandlerTest.authenticationWithUser, checkPrint);
	}

	/**
	 * Test when getUserName and getRemoteAddr return null.
	 */
	@Test
	public void testOnAuthenticationSuccessNotUserNameOrRemoteAddr() {
		CheckPrint checkPrint = new CheckPrint(
				"{\"id\":null,\"roles\":[\"RA_TEST-AC_TEST\"],\"name\":null}");

		// Mock up a PrintWriter
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		this.successfulCall(AuthenticationSuccessHandlerTest.requestWithoutIp,
				AuthenticationSuccessHandlerTest.authenticationWithUserWithoutId, checkPrint);
	}

	/**
	 * There's lots of different ways for the call to be successful as there's lots of error handling
	 * in the method. ALl the checks are extracted to here.
	 * @param request
	 * @param authentication
	 * @param checkPrint
	 */
	public void successfulCall(HttpServletRequest request, Authentication authentication, CheckPrint checkPrint) {
		CallChecker checkFlush = new CallChecker();
		CallChecker checkClose = new CallChecker();
		CheckSetContentType checkSetContentType = new CheckSetContentType();
		PrintWriter writer = Mockito.mock(PrintWriter.class);


		Mockito.doAnswer(checkFlush).when(writer).flush();
		Mockito.doAnswer(checkClose).when(writer).close();
		Mockito.doAnswer(checkPrint).when(writer).print((Object)Mockito.anyObject());

		// Mock the
		// Mock up the response
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(checkSetContentType).when(response).setContentType(Mockito.anyString());
		try {
			Mockito.when(response.getWriter()).thenReturn(writer);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}

		try {
			this.authenticationSuccessHandler.onAuthenticationSuccess(request,
					response, authentication);
		} catch (IOException | ServletException e) {
			Assert.fail(e.getMessage());
		}

		Assert.assertTrue("close not called", checkClose.isMethodCalled());
		Assert.assertTrue("flush not called", checkFlush.isMethodCalled());
		Assert.assertTrue("set content type not called", checkSetContentType.isSetContentTypeCalled());
		Assert.assertTrue("print not called", checkPrint.isPrintCalled());
	}

	/**
	 * Test when authentication does not contain a HebUserDetails object.
	 */
	@Test
	public void testOnAuthenticationSuccessNoHebUserDetails() {

		// Mock the response.
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

		try {
			this.authenticationSuccessHandler.onAuthenticationSuccess(AuthenticationSuccessHandlerTest.requestWithIp,
					response, AuthenticationSuccessHandlerTest.authenticationWithNullUser);
			Assert.fail("did not throw illegal argument exception");
		} catch (IOException | ServletException e) {
			Assert.fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			// This is expected
		}
	}
}
