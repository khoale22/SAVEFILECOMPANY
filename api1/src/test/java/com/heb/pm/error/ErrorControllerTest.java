/*
 *  com.heb.pm.error.ErrorControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.error;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Tests ErrorController
 *
 * @author d116773
 * @since 2.0.0
 */
public class ErrorControllerTest {


	private static final String STATUS_CODE_ATTRIBUTE_NAME = "javax.servlet.error.status_code";
	private static final String MESSAGE_ATTRIBUTE_NAME = "javax.servlet.error.message";
	private static final String ROOT_ERROR_ATTRIBUTE_NAME = DefaultErrorAttributes.class.getName() + ".ERROR";

	private static final String STAUS_CODE_RESPONSE_KEY = "status";
	private static final String ERROR_CODE_RESPONSE_KEY = "error";
	private static final String MESSAGE_CODE_RESPONSE_KEY = "message";


	private PmErrorController  errorController = new PmErrorController();

	@Test
	public void testErrroAsHtmlAllNoRootError() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(STATUS_CODE_ATTRIBUTE_NAME))
				.thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
		Mockito.when(request.getAttribute(MESSAGE_ATTRIBUTE_NAME)).thenReturn("test");

		String response = this.errorController.errorAsHtml(request);
		Assert.assertEquals("html incorrect no root", "error=Internal Server Error;message=test;status=500;", response);
	}

	@Test
	public void testErrroAsHtmlAllRootError() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(STATUS_CODE_ATTRIBUTE_NAME))
				.thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
		Mockito.when(request.getAttribute(MESSAGE_ATTRIBUTE_NAME)).thenReturn("test");
		Mockito.when(request.getAttribute(ROOT_ERROR_ATTRIBUTE_NAME))
				.thenReturn(new IllegalArgumentException("iae test"));

		String response = this.errorController.errorAsHtml(request);
		Assert.assertEquals("html incorrect no root", "error=Internal Server Error;message=iae test;status=500;",
				response);
	}

	@Test
	public void testErrroAsHtmlRootErrorNotException() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(STATUS_CODE_ATTRIBUTE_NAME))
				.thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
		Mockito.when(request.getAttribute(MESSAGE_ATTRIBUTE_NAME)).thenReturn("test");
		Mockito.when(request.getAttribute(ROOT_ERROR_ATTRIBUTE_NAME)).thenReturn("this is not an exception");

		String response = this.errorController.errorAsHtml(request);
		Assert.assertEquals("html incorrect no root", "error=Internal Server Error;message=test;status=500;", response);
	}

	@Test
	public void testErrroAsHtmlNoRootErrorOrMessage() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(STATUS_CODE_ATTRIBUTE_NAME))
				.thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());

		String response = this.errorController.errorAsHtml(request);
		Assert.assertEquals("html incorrect no root", "error=Internal Server Error;message=Unknown Error;status=500;",
				response);
	}

	@Test
	public void testErrroAsHtmlAllRootErrorNoMessage() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(STATUS_CODE_ATTRIBUTE_NAME))
				.thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
		Mockito.when(request.getAttribute(ROOT_ERROR_ATTRIBUTE_NAME))
				.thenReturn(new IllegalArgumentException("iae test"));

		String response = this.errorController.errorAsHtml(request);
		Assert.assertEquals("html incorrect no root", "error=Internal Server Error;message=iae test;status=500;",
				response);
	}

	@Test
	public void testErrorAsHtmlBadStatus() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(STATUS_CODE_ATTRIBUTE_NAME)).thenReturn(Integer.valueOf(99999));
		Mockito.when(request.getAttribute(MESSAGE_ATTRIBUTE_NAME)).thenReturn("test");

		String response = this.errorController.errorAsHtml(request);
		Assert.assertEquals("html incorrect no root", "error=Internal Server Error;message=test;status=500;", response);
	}

	@Test
	public void testErrorAsHtmlNullStatus() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(MESSAGE_ATTRIBUTE_NAME)).thenReturn("test");

		String response = this.errorController.errorAsHtml(request);
		Assert.assertEquals("html incorrect no root", "error=Internal Server Error;message=test;status=500;", response);
	}

	@Test
	public void testErrorAsHtmlNonIntegerStatus() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(STATUS_CODE_ATTRIBUTE_NAME)).thenReturn("won't convert to integer");
		Mockito.when(request.getAttribute(MESSAGE_ATTRIBUTE_NAME)).thenReturn("test");

		String response = this.errorController.errorAsHtml(request);
		Assert.assertEquals("html incorrect no root", "error=Internal Server Error;message=test;status=500;", response);
	}

	@Test
	public void testErrorAsJsonNoRootError() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getAttribute(STATUS_CODE_ATTRIBUTE_NAME))
				.thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
		Mockito.when(request.getAttribute(MESSAGE_ATTRIBUTE_NAME)).thenReturn("test");

		ResponseEntity<Map<String, Object>> response = this.errorController.errorAsJson(request);
		Assert.assertEquals("Map wrong size", 3, response.getBody().size());
		Assert.assertEquals("Status wrong", HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		Assert.assertEquals("error wrong", "Internal Server Error", response.getBody().get(ERROR_CODE_RESPONSE_KEY));
		Assert.assertEquals("message wrong", response.getBody().get(MESSAGE_CODE_RESPONSE_KEY),"test");
		Assert.assertEquals("status wrong", Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
				response.getBody().get(STAUS_CODE_RESPONSE_KEY));
	}
}
