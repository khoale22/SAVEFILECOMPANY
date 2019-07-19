/*
 *  com.heb.util.controller.UserInfo
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.util.controller;

import com.heb.jaf.security.HebUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * A bean meant to be part of a Spring context that has a collection of functions retlated to the logged in user.
 *
 * @author d116773
 * @since 2.0.0
 */
@Service
public class UserInfo {

	private static final String MISSING_USER_NAME_ID = "Anonymous";
	private static final String AUTHORITY_ROLE_PREFIX = "ROLE_";
	private static final String AUTHORITY_EDIT_SUFFIX = "-EDIT";
	private static final String AUTHORITY_VIEW_SUFFIX = "-VIEW";
	/**
	 * The type of location
	 */
	private static final String LOCATION_TYPE = "S";
	/**
	 * Pulls the logged in user from the Security context.
	 *
	 * @return The name of the user logged into the system.
	 */
	public String getUserId() {
		if (SecurityContextHolder.getContext() == null ||
				SecurityContextHolder.getContext().getAuthentication() == null ||
				SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null ||
				!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)) {
			return UserInfo.MISSING_USER_NAME_ID;
		}

		User u = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return u.getUsername() == null ? UserInfo.MISSING_USER_NAME_ID : u.getUsername();
	}

	/**
	 * This method will return whether or not the logged in user can edit a particular resource. This is done by
	 * looking at all the associations the user has, and looking for "ROLE_{given resource}_EDIT". If the association
	 * is found, return true. Else return false.
	 *
	 * @param resourceToCheckForEditAccess The resource to check for edit access.
	 * @return True if user can edit the resource, false otherwise.
	 */
	public boolean canUserEditResource(String resourceToCheckForEditAccess) {
		String fullyPopulatedResource = UserInfo.AUTHORITY_ROLE_PREFIX
				.concat(resourceToCheckForEditAccess)
				.concat(UserInfo.AUTHORITY_EDIT_SUFFIX);
		Collection<? extends GrantedAuthority> authorities =
				SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for(GrantedAuthority authority : authorities){
			if(authority.getAuthority().equals(fullyPopulatedResource)){
				return true;
			}
		}
		return false;
	}

	/**
	 * This method will return whether or not the logged in user can view a particular resource. This is done by
	 * looking at all the associations the user has, and looking for "ROLE_{given resource}_VIEW". If the association
	 * is found, return true. Else return false.
	 *
	 * @param resourceToCheckForViewAccess The resource to check for view access.
	 * @return True if user can view the resource, false otherwise.
	 */
	public boolean canUserViewResource(String resourceToCheckForViewAccess) {
		String fullyPopulatedResource = UserInfo.AUTHORITY_ROLE_PREFIX
				.concat(resourceToCheckForViewAccess)
				.concat(UserInfo.AUTHORITY_VIEW_SUFFIX);
		Collection<? extends GrantedAuthority> authorities =
				SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for(GrantedAuthority authority : authorities){
			if(authority.getAuthority().equals(fullyPopulatedResource)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Return user store from user's HebGLlocation by user's HebLocType equals to 'S' or null.
	 * It there is no any store, then return null.
	 * @return the user store.
	 */
	public String getUserStore(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String store = null;
		if (principal != null && (principal instanceof HebUserDetails)) {
			HebUserDetails user = (HebUserDetails) principal;
			if(user.getHebLocType() == null || user.getHebLocType().equalsIgnoreCase(LOCATION_TYPE)) {
				store = user.getHebGLlocation();
			}
		}
		return store;
	}
}
