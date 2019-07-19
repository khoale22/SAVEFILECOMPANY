/*
 * com.heb.jaf.security.SessionDestroyedListener
 *
 * Copyright (c) 2014 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Listener for session expiration events. It requires that a HttpSessionEventPublisher
 * is created somewhere else in the application. If one is created, this will be called
 * automatically and no other configuration is necessary.
 *
 * @author d116773
 */
@Component
public class SessionDestroyedLogger implements ApplicationListener<HttpSessionDestroyedEvent> {

	private static final String SESSION_END_LOG_MESSAGE = "User session ended for %s";
	private static final String SECURITY_CONTEXTS_EMPTY = "Security contexts empty";

	private static final Logger logger = LoggerFactory.getLogger(SessionDestroyedLogger.class);

	/**
	 * Called when a user's session is destroyed. This implementation will just
	 * logger the event happening.
	 *
	 * @param event The event that represents the user's session being destroyed.
	 */
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
		// the only thing I want to do is logger that the user's session ends
		List<SecurityContext> securityContexts = event.getSecurityContexts();

		if (securityContexts == null) {
			SessionDestroyedLogger.logger.error(SessionDestroyedLogger.SECURITY_CONTEXTS_EMPTY);
			return;
		}

		for (SecurityContext securityContext : securityContexts) {
			// if there is any error here, just log it
			try {
				UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
				SessionDestroyedLogger.logger.info(String.format(SessionDestroyedLogger.SESSION_END_LOG_MESSAGE,
						userDetails.getUsername()));
			} catch (Exception e) {
				SessionDestroyedLogger.logger.error(e.getMessage());
			}
		}
	}
}
