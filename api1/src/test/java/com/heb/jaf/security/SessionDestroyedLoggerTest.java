/*
 * SessionDestroyedLoggerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 *
 */

package com.heb.jaf.security;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

/**
 * Tests SessionDestroyedLogger.
 *
 * @author d116773
 */
public class SessionDestroyedLoggerTest {

	private SessionDestroyedLogger sessionDestroyedLogger = new SessionDestroyedLogger();

	@Test
	public void onApplicationEventHappyPath() {
		UserDetails userDetails = Mockito.mock(UserDetails.class);
		Mockito.when(userDetails.getUsername()).thenReturn("PGDV");

		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

		List<SecurityContext> contexts = new ArrayList<>();
		contexts.add(securityContext);

		HttpSessionDestroyedEvent sessionDestroyedEvent = Mockito.mock(HttpSessionDestroyedEvent.class);
		Mockito.when(sessionDestroyedEvent.getSecurityContexts()).thenReturn(contexts);

		// Basically, as long as this doesn't throw an error, no problem.
		this.sessionDestroyedLogger.onApplicationEvent(sessionDestroyedEvent);
	}

	/**
	 * Test onApplicationEvent where the HttpSessionDestroyedEvent returns null
	 * for getSecurityContexts.
	 */
	@Test
	public void onApplicationEventNullContexts() {
		HttpSessionDestroyedEvent sessionDestroyedEvent = Mockito.mock(HttpSessionDestroyedEvent.class);
		Mockito.when(sessionDestroyedEvent.getSecurityContexts()).thenReturn(null);

		// Basically, as long as this doesn't throw an error, no problem.
		this.sessionDestroyedLogger.onApplicationEvent(sessionDestroyedEvent);
	}

	/**
	 * Test onApplicationEvent where getAuthentication returns an error.
	 */
	@Test
	public void onApplicationEventGetAuthenticationError() {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenThrow(new RuntimeException("bad"));

		List<SecurityContext> contexts = new ArrayList<>();
		contexts.add(securityContext);

		HttpSessionDestroyedEvent sessionDestroyedEvent = Mockito.mock(HttpSessionDestroyedEvent.class);
		Mockito.when(sessionDestroyedEvent.getSecurityContexts()).thenReturn(contexts);

		// Basically, as long as this doesn't throw an error, no problem.
		this.sessionDestroyedLogger.onApplicationEvent(sessionDestroyedEvent);
	}

	/**
	 * Test onApplicationEvent when getPrincipal returns something that is not a UserDetails object.
	 */
	@Test
	public void onApplicationEventGetPrincipalNotUser() {
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getPrincipal()).thenReturn(new Integer(0));

		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

		List<SecurityContext> contexts = new ArrayList<>();
		contexts.add(securityContext);

		HttpSessionDestroyedEvent sessionDestroyedEvent = Mockito.mock(HttpSessionDestroyedEvent.class);
		Mockito.when(sessionDestroyedEvent.getSecurityContexts()).thenReturn(contexts);

		// Basically, as long as this doesn't throw an error, no problem.
		this.sessionDestroyedLogger.onApplicationEvent(sessionDestroyedEvent);
	}
}
