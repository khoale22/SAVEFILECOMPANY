/*
 * com.heb.jaf.security.ViewPermission
 *
 * Copyright (c) 2014 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Helper annotation to make it easier to define methods that need to be pre-authorized
 * before calling. This would be used on methods that allow users to view data. Use this
 * in conjunction with the AuthorizedResource annotation. The user is assumed to have view permission on
 * a resource if they have edit permission.
 *
 * It would have been easier if I could have put it in one annotation. The issue
 * was this one is evaluated at compile time where the other is evaluated at run time
 * and could not reconcile the two.
 *
 * @author d116773
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(this, '" + JafSecurityConstants.VIEW + "') or hasPermission(this, '" + JafSecurityConstants.EDIT + "')")
public @interface ViewPermission {
}
