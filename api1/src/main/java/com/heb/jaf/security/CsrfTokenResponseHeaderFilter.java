/*
 * com.heb.jaf.security.CsrfTokenResponseHeaderFilter
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/**
 * A response filter that will put CSRF tokens into a cookie that Angular can interpret.
 *
 * This class is based on an example that can be found here:
 * https://spring.io/blog/2015/01/12/the-login-page-angular-js-and-spring-security-part-ii
 *
 * @author d116773
 */
public class CsrfTokenResponseHeaderFilter extends OncePerRequestFilter {
    public static final String ANGULAR_CSRF_COOKIE_NAME = "XSRF-TOKEN";

	/**
	 * Reads a CSRF token out of the request and writes it to the response as a cookie. That is where Angular
	 * will be looking for a token to send back to the server.
	 *
	 * @param request The HttpServletRequest that initiated this call.
	 * @param response The HttpServletResponse the cookie will be written to.
	 * @param filterChain The servlet filter chain.
	 * @throws ServletException
	 * @throws IOException
	 */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrf != null) {
            Cookie cookie = WebUtils.getCookie(request, CsrfTokenResponseHeaderFilter.ANGULAR_CSRF_COOKIE_NAME);
            String token = csrf.getToken();
            if (cookie==null || token!=null && !token.equals(cookie.getValue())) {
                cookie = new Cookie(CsrfTokenResponseHeaderFilter.ANGULAR_CSRF_COOKIE_NAME, token);
                cookie.setPath("/");

                response.addCookie(cookie);
            }
        }

		// Do this to continue on processing the rest of the filter chain.
        filterChain.doFilter(request, response);
    }
}
