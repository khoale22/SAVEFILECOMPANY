/*
 * com.heb.jaf.security.ResourcePermissionEvaluator
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
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * Will evaluate permissions. This allows the application to use ARBAF's resource model
 * rather than the role model.
 *
 * @author d116773
 */
public class ResourcePermissionEvaluator implements PermissionEvaluator {

	private static final Logger logger = LoggerFactory.getLogger(ResourcePermissionEvaluator.class);
	private static final String LOG_PARAMETER_MESSAGE = "Target object=%s:permission=%s";
	private static final String FAILED_ACCESS_MESSAGE =
			"User %s attempted to access resource without permissions." + LOG_PARAMETER_MESSAGE;

	/**
	 * Compares the requested object and permission with the user's granted permissions.
	 *
	 * @param authentication Holds the user principal.
	 * @param targetDomainObject The object being asked for permission on. This must be a resource from ARBAF.
	 * @param permission The type of permission requested. This must be an access type abbreviation from ARBAF.
	 * @return True if the user has access and false otherwise.
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

		ResourcePermissionEvaluator.logger.debug(
				String.format(ResourcePermissionEvaluator.LOG_PARAMETER_MESSAGE, targetDomainObject, permission));

		// The authentication has a collection in it called GrantedAuthorities. I decided against using these
		// because the objects there are SimpleGrantedAuthorities converted from the GrantedAuthorities inside
		// the principal. Since these are converted and no longer match the originals, I decided to extract
		// the GrantedAuthorities from the principal. That path looked cleaner at first, but got hokey as I went along.
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof HebUserDetails)) {
			ResourcePermissionEvaluator.logger.error("User principal not HebUserDetails.");
			return false;
		}

		// See if the class is annotated with AuthorizeRequest. If it is, that annotation
		// will contain the name of the resource to check. Otherwise, compare against
		// targetDomainObject.
		String resource;
		HebUserDetails userDetails = (HebUserDetails)principal;
		if (targetDomainObject.getClass().isAnnotationPresent(AuthorizedResource.class)) {
			AuthorizedResource authorizedResource =
					targetDomainObject.getClass().getAnnotation(AuthorizedResource.class);
			resource = authorizedResource.value();
		} else {
			resource = targetDomainObject.toString();
		}

		// Loop through the user's GrantedAuthorities and see if there is a match
		// for resource calculated above. Return true if there was.
		for (GrantedAuthority ga : userDetails.getAuthorities()) {
			if (ga instanceof Permission) {
				Permission p = (Permission)ga;
				if(p.checkAccess(resource, permission.toString())) {
					return true;
				}
			}
		}

		String user = userDetails.getUsername() != null ? userDetails.getUsername() : "Anonymous";
		ResourcePermissionEvaluator.logger.info(String.format(ResourcePermissionEvaluator.FAILED_ACCESS_MESSAGE,
				user, resource, permission));
		return false;
	}

	/**
	 * Unimplemented.
	 *
	 * @param authentication Ignored.
	 * @param targetId Ignored.
	 * @param targetType Ignored.
	 * @param permission Ignored.
	 * @return False.
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId,
								 String targetType, Object permission) {
		return false;
	}
}
