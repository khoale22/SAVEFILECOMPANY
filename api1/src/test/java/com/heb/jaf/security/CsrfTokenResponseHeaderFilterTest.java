/*
 *  com.heb.jaf.security.CsrfTokenResponseHeaderFilterTest
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
import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Tests CsrfTokenResponseHeaderFilter
 *
 * @author d116773
 */
public class CsrfTokenResponseHeaderFilterTest {

	// The cookie name Angular uses.
	private static final String COOKIE_NAME = "XSRF-TOKEN";
	private static final String COOKIE_VALUE = "cookeText";

	private static Cookie cookie;
	private static Cookie cookieDifferentValue;

	private static CsrfToken token;
	private static CsrfToken tokenNullToken;

	private static HttpServletRequest requestWithAll;
	private static HttpServletRequest requestWithEmptyToken;
	private static HttpServletRequest requestWithoutToken;
	private static HttpServletRequest requestWithoutCookie;
	private static HttpServletRequest requestWithDifferentCookie;

	private static HttpServletResponse servletResponse;

	// This is used to check that, when the cookie is added to the response, it has the correct values.
	private static Answer<Void> checkAddCookie = invocation -> {
		if (!(invocation.getArguments()[0] instanceof Cookie)){
			Assert.fail("argument not a cookie");
		}
		Cookie c = (Cookie)invocation.getArguments()[0];
		Assert.assertEquals("cookie name incorrect", CsrfTokenResponseHeaderFilterTest.COOKIE_NAME, c.getName());
		Assert.assertEquals("cookie value incorrect", CsrfTokenResponseHeaderFilterTest.token.getToken(),
				c.getValue());
		Assert.assertEquals("cookie path incorrect", "/", c.getPath());
		return null;
	};

	// This is used to test that the class passes control on to the rest of the filter chain.
	private class DoFilterAnswer implements Answer<Void> {

		public boolean doFilterCalled;
		private HttpServletRequest request;
		private HttpServletResponse response;

		public DoFilterAnswer(HttpServletRequest request, HttpServletResponse response) {
			this.doFilterCalled = false;
			this.request = request;
			this.response = response;
		}

		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			Assert.assertEquals("bad request", this.request, invocation.getArguments()[0]);
			Assert.assertEquals("bad response", this.response, invocation.getArguments()[1]);
			this.doFilterCalled = true;

			return null;
		}
	}

	private CsrfTokenResponseHeaderFilter filter = new CsrfTokenResponseHeaderFilter();



	@BeforeClass
	public static void setup() {

		// Create a cookie to represent the one sent by Angular.
		CsrfTokenResponseHeaderFilterTest.cookie = new Cookie(CsrfTokenResponseHeaderFilterTest.COOKIE_NAME,
				CsrfTokenResponseHeaderFilterTest.COOKIE_VALUE);
		CsrfTokenResponseHeaderFilterTest.cookie.setPath("/");

		// Create a cookie that Angular would send but with a different value.
		CsrfTokenResponseHeaderFilterTest.cookieDifferentValue = new Cookie(CsrfTokenResponseHeaderFilterTest.COOKIE_NAME,
				"NOT THE COOKIE VALUE");
		CsrfTokenResponseHeaderFilterTest.cookieDifferentValue.setPath("/");

		// Mock the CSRF token.
		CsrfTokenResponseHeaderFilterTest.token = Mockito.mock(CsrfToken.class);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.token.getToken()).thenReturn(CsrfTokenResponseHeaderFilterTest.COOKIE_VALUE);

		// Mock a CSRF token without a token.
		CsrfTokenResponseHeaderFilterTest.tokenNullToken = Mockito.mock(CsrfToken.class);

		// Mock a servlet request with all the data
		CsrfTokenResponseHeaderFilterTest.requestWithAll = Mockito.mock(HttpServletRequest.class);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.requestWithAll.getAttribute(CsrfToken.class.getName()))
				.thenReturn(CsrfTokenResponseHeaderFilterTest.token);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.requestWithAll.getCookies())
				.thenReturn(new Cookie[]{CsrfTokenResponseHeaderFilterTest.cookie});

		// Mock a servlet request with an empty token
		CsrfTokenResponseHeaderFilterTest.requestWithEmptyToken = Mockito.mock(HttpServletRequest.class);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.requestWithEmptyToken.getAttribute(CsrfToken.class.getName()))
				.thenReturn(CsrfTokenResponseHeaderFilterTest.tokenNullToken);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.requestWithEmptyToken.getCookies())
				.thenReturn(new Cookie[]{CsrfTokenResponseHeaderFilterTest.cookie});

		// Mock a servlet request with all the data but the cookie has a differnt value
		CsrfTokenResponseHeaderFilterTest.requestWithDifferentCookie = Mockito.mock(HttpServletRequest.class);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.requestWithDifferentCookie.getAttribute(CsrfToken.class.getName()))
				.thenReturn(CsrfTokenResponseHeaderFilterTest.token);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.requestWithDifferentCookie.getCookies())
				.thenReturn(new Cookie[]{CsrfTokenResponseHeaderFilterTest.cookieDifferentValue});

		// Mock a servlet request without the token but with a cookie.
		CsrfTokenResponseHeaderFilterTest.requestWithoutToken = Mockito.mock(HttpServletRequest.class);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.requestWithoutToken.getCookies())
				.thenReturn(new Cookie[]{CsrfTokenResponseHeaderFilterTest.cookie});

		// Mock a servlet request with the token but without a cookie.
		CsrfTokenResponseHeaderFilterTest.requestWithoutCookie = Mockito.mock(HttpServletRequest.class);
		Mockito.when(CsrfTokenResponseHeaderFilterTest.requestWithoutCookie.getAttribute(CsrfToken.class.getName()))
				.thenReturn(CsrfTokenResponseHeaderFilterTest.token);

		// Mock the response the function will be writing to.
		CsrfTokenResponseHeaderFilterTest.servletResponse = Mockito.mock(HttpServletResponse.class);
		Mockito.doAnswer(CsrfTokenResponseHeaderFilterTest.checkAddCookie)
				.when(CsrfTokenResponseHeaderFilterTest.servletResponse).addCookie(Mockito.anyObject());

		// Mock the rest of the filter chain.
		// CsrfTokenResponseHeaderFilterTest.filterChain = Mockito.mock(FilterChain.class);
	}

	/**
	 * Test for a good token and cookie. This is the ideal case.
	 */
	@Test
	public void testDoFilterInternalAllValues() {

		try {

			DoFilterAnswer filterAnswer = new DoFilterAnswer(CsrfTokenResponseHeaderFilterTest.requestWithAll,
					CsrfTokenResponseHeaderFilterTest.servletResponse);

			FilterChain filterChain = Mockito.mock(FilterChain.class);
			Mockito.doAnswer(filterAnswer).when(filterChain)
					.doFilter(Mockito.anyObject(), Mockito.anyObject());

			this.filter.doFilterInternal(CsrfTokenResponseHeaderFilterTest.requestWithAll,
					CsrfTokenResponseHeaderFilterTest.servletResponse,
					filterChain);

			Assert.assertTrue("do filter not called", filterAnswer.doFilterCalled);
		} catch (ServletException | IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test for a good token and a cookie, but the cookie in the request has a different value than expected.
	 */
	@Test
	public void testDoFilterInternalDifferentCookie() {

		try {

			DoFilterAnswer filterAnswer = new DoFilterAnswer(
					CsrfTokenResponseHeaderFilterTest.requestWithDifferentCookie,
					CsrfTokenResponseHeaderFilterTest.servletResponse);

			FilterChain filterChain = Mockito.mock(FilterChain.class);
			Mockito.doAnswer(filterAnswer).when(filterChain)
					.doFilter(Mockito.anyObject(), Mockito.anyObject());

			this.filter.doFilterInternal(CsrfTokenResponseHeaderFilterTest.requestWithDifferentCookie,
					CsrfTokenResponseHeaderFilterTest.servletResponse,
					filterChain);

			Assert.assertTrue("do filter not called", filterAnswer.doFilterCalled);
		} catch (ServletException | IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test for no token, but does have a cookie.
	 */
	@Test
	public void testDoFilterInternalNoToken() {

		try {

			DoFilterAnswer filterAnswer = new DoFilterAnswer(CsrfTokenResponseHeaderFilterTest.requestWithoutToken,
					CsrfTokenResponseHeaderFilterTest.servletResponse);

			FilterChain filterChain = Mockito.mock(FilterChain.class);
			Mockito.doAnswer(filterAnswer).when(filterChain)
					.doFilter(Mockito.anyObject(), Mockito.anyObject());

			this.filter.doFilterInternal(CsrfTokenResponseHeaderFilterTest.requestWithoutToken,
					CsrfTokenResponseHeaderFilterTest.servletResponse,
					filterChain);

			Assert.assertTrue("do filter not called", filterAnswer.doFilterCalled);
		} catch (ServletException | IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test for no cookie, but does have a token.
	 */
	@Test
	public void testDoFilterInternalNoCookie() {

		try {

			DoFilterAnswer filterAnswer = new DoFilterAnswer(CsrfTokenResponseHeaderFilterTest.requestWithoutCookie,
					CsrfTokenResponseHeaderFilterTest.servletResponse);

			FilterChain filterChain = Mockito.mock(FilterChain.class);
			Mockito.doAnswer(filterAnswer).when(filterChain)
					.doFilter(Mockito.anyObject(), Mockito.anyObject());

			this.filter.doFilterInternal(CsrfTokenResponseHeaderFilterTest.requestWithoutCookie,
					CsrfTokenResponseHeaderFilterTest.servletResponse,
					filterChain);

			Assert.assertTrue("do filter not called", filterAnswer.doFilterCalled);
		} catch (ServletException | IOException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test for CSRF token that is empty.
	 */
	@Test
	public void testDoFilterInternalEmptyToken() {
		try {

			DoFilterAnswer filterAnswer = new DoFilterAnswer(CsrfTokenResponseHeaderFilterTest.requestWithEmptyToken,
					CsrfTokenResponseHeaderFilterTest.servletResponse);

			FilterChain filterChain = Mockito.mock(FilterChain.class);
			Mockito.doAnswer(filterAnswer).when(filterChain)
					.doFilter(Mockito.anyObject(), Mockito.anyObject());

			this.filter.doFilterInternal(CsrfTokenResponseHeaderFilterTest.requestWithEmptyToken,
					CsrfTokenResponseHeaderFilterTest.servletResponse,
					filterChain);

			Assert.assertTrue("do filter not called", filterAnswer.doFilterCalled);
		} catch (ServletException | IOException e) {
			Assert.fail(e.getMessage());
		}

	}
}
