/*
 * com.heb.jaf.security.EntryPointHandler
 *
 * Copyright (c) 2014 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The beginning of the authentication chain when the server gets sent most URLs that
 * are protected. (login and logout are excluded). Since this server supports REST calls
 * only, all requests except login and logout should be sent an access denied error.  This
 * class responds with a 401 to all request to being authentication.
 *
 * @author p235969
 */
@Component
public class EntryPointHandler implements AuthenticationEntryPoint {

	private static final String NULL_RESPONSE_ERROR_MESSAGE = "HTTP Response cannot be null.";
	/**
	 * Called upon the beginning of the authentication chain. This function will always
	 * return 401 UNAUTHORIZED.
	 *
	 * @param request The HTTP request for a resource on the server.
	 * @param response The HTTP response the server will write to.
	 * @param authException The authentication exception that caused this method to be called.
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException)
			throws IOException, ServletException {
		if (response == null) {
			throw new IllegalArgumentException(EntryPointHandler.NULL_RESPONSE_ERROR_MESSAGE);
		}
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
	}
}
