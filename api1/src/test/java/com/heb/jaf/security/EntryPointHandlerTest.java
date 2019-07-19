/*
 *  com.heb.jaf.security.EntryPointHandlerTest
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
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Test for EntryPointHandler.
 *
 * @author d116773
 */
public class EntryPointHandlerTest {

	private static HttpServletRequest httpServletRequest;
	private static HttpServletResponse httpServletResponse;
	private static AuthenticationException authenticationException;

	private class HandleSendError implements Answer<Void> {

		public boolean sendErrorCalled;

		public HandleSendError() {
			this.sendErrorCalled = false;
		}

		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			Assert.assertEquals("wrong status for error", HttpServletResponse.SC_UNAUTHORIZED,
					invocation.getArguments()[0]);
			this.sendErrorCalled = true;
			return null;
		}
	}

	private EntryPointHandler entryPointHandler = new EntryPointHandler();

	@BeforeClass
	public static void setup() {
		EntryPointHandlerTest.httpServletRequest = Mockito.mock(HttpServletRequest.class);
		EntryPointHandlerTest.httpServletResponse = Mockito.mock(HttpServletResponse.class);
		EntryPointHandlerTest.authenticationException = Mockito.mock(AuthenticationException.class);
	}

	/**
	 * Tests commence with a request and response. This is the ideal case.
	 */
	@Test
	public void testCommence() {
		try {
			HandleSendError handleSendError = new HandleSendError();

			Mockito.doAnswer(handleSendError).when(EntryPointHandlerTest.httpServletResponse)
					.sendError(Mockito.anyInt(), Mockito.anyString());

			this.entryPointHandler.commence(EntryPointHandlerTest.httpServletRequest,
					EntryPointHandlerTest.httpServletResponse,
					EntryPointHandlerTest.authenticationException);

			Assert.assertTrue("send error not called", handleSendError.sendErrorCalled);

		} catch (IOException | ServletException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests commence with a null exception. The result should be the base case.
	 */
	@Test
	public void testCommenceNoException() {
		try {
			HandleSendError handleSendError = new HandleSendError();

			Mockito.doAnswer(handleSendError).when(EntryPointHandlerTest.httpServletResponse)
					.sendError(Mockito.anyInt(), Mockito.anyString());

			this.entryPointHandler.commence(EntryPointHandlerTest.httpServletRequest,
					EntryPointHandlerTest.httpServletResponse,
					null);

			Assert.assertTrue("send error not called", handleSendError.sendErrorCalled);

		} catch (IOException | ServletException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests commence with a null request. The result should be the base case.
	 */
	@Test
	public void testCommenceNoRequest() {
		try {
			HandleSendError handleSendError = new HandleSendError();

			Mockito.doAnswer(handleSendError).when(EntryPointHandlerTest.httpServletResponse)
					.sendError(Mockito.anyInt(), Mockito.anyString());

			this.entryPointHandler.commence(null,
					EntryPointHandlerTest.httpServletResponse,
					EntryPointHandlerTest.authenticationException);

			Assert.assertTrue("send error not called", handleSendError.sendErrorCalled);

		} catch (IOException | ServletException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests commence with a null response. This should throw an illegal argument exception.
	 */
	@Test
	public void testCommenceNoResponse() {
		try {
			HandleSendError handleSendError = new HandleSendError();

			Mockito.doAnswer(handleSendError).when(EntryPointHandlerTest.httpServletResponse)
					.sendError(Mockito.anyInt(), Mockito.anyString());

			this.entryPointHandler.commence(EntryPointHandlerTest.httpServletRequest,
					null, EntryPointHandlerTest.authenticationException);

			Assert.fail("did not throw illegal arguement exception");

		} catch (IOException | ServletException e) {
			Assert.fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			// This is the valid case.
		}
	}

}
