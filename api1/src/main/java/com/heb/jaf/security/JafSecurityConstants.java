/*
 * com.heb.jaf.security.JafSecurityConstants
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

/**
 * Used to store constants common to the JAF security framework.
 *
 * @author d116773
 */
public final class JafSecurityConstants {
	private JafSecurityConstants() {};

	public static final String RESPONSE_CONTENT_TYPE = "application/json";
	public static final String UNKNOWN_VALUE = "<unknown>";

	public static final String USER_ID_KEY = "id";
	public static final String USER_ROLES_KEY = "roles";
	public static final String USER_NAME_KEY = "name";

	// Permissions granted on resources to various roles.
	public static final String VIEW = "VIEW";
	public static final String EDIT = "EDIT";
	public static final String CREATE = "EDIT";
	public static final String DELETE = "EDIT";
}
