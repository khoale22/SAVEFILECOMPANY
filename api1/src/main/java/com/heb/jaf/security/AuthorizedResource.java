/*
 * com.heb.jaf.security.AuthorizedResource
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Helper annotation to make it easier to define methods that need to be
 * pre-authorized before calling. Use this in conjunction with the one of the
 * Permission annotations. This holds the resource name where the other holds the
 * permission type.
 *
 * It would have been easier if I could have put it in one annotation. The issue
 * was this one is evaluated at runtime where the other is evaluated at compile time
 * and could not reconcile the two.
 *
 * @author d116773
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizedResource {
	/**
	 * The name of the ARBAF resource to authorize.
	 *
	 * @return The name of the ARBAF resource to authorize.
	 */
	String value();
}
