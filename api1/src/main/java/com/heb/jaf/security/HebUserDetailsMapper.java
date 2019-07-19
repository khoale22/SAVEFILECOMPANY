/*
 * com.heb.jaf.security.HebUserDetailsMapper
 *
 * Copyright (c) 2010 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;
import java.util.HashSet;

/**
 * Creates HebUserDetails objects based on information store in one of HEB's LDAP providers.
 *
 * @author r511759
 */
public class HebUserDetailsMapper implements UserDetailsContextMapper {

	private static final String MAIL_KEY = "mail";
	private static final String DEPARTMENT_NUMBER_KEY = "departmentNumber";
	private static final String HEB_JOB_DESC_KEY = "hebJobDesc";
	private static final String HEB_JOB_CODE_KEY = "hebJobCode";
	private static final String DISPLAY_NAME_KEY = "displayName";
	private static final String MOBILE_KEY = "mobile";
	private static final String GL_LOCATION_KEY = "hebGLlocation";
	private static final String LOCATION_TYPE_KEY = "hebLocType";


	/**
	 * Maps user details from LDAP.
	 *
	 * @param ctx The LDAP context about the user.
	 * @param username The user's one-pass ID.
	 * @param authorities The GrantedAuthorities of the user.
	 * @return A populated HebUserDetails object.
	 */
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
										  Collection<? extends GrantedAuthority> authorities) {

		if (ctx == null || username == null) {
			throw new IllegalArgumentException("user name and context cannot be null");
		}
		if (authorities == null) {
			authorities = new HashSet<>();
		}

		HebUserDetails retObj = new HebUserDetails(username, authorities);
		retObj.setDisplayName(ctx.getStringAttribute(HebUserDetailsMapper.DISPLAY_NAME_KEY));
		retObj.setHebJobCode(ctx.getStringAttribute(HebUserDetailsMapper.HEB_JOB_CODE_KEY));
		retObj.setHebJobDesc(ctx.getStringAttribute(HebUserDetailsMapper.HEB_JOB_DESC_KEY));
		retObj.setDepartmentNumber(ctx.getStringAttribute(HebUserDetailsMapper.DEPARTMENT_NUMBER_KEY));
		retObj.setMail(ctx.getStringAttribute(HebUserDetailsMapper.MAIL_KEY));
		retObj.setMobile(ctx.getStringAttribute(HebUserDetailsMapper.MOBILE_KEY));
		retObj.setHebGLlocation(ctx.getStringAttribute(GL_LOCATION_KEY));
		retObj.setHebLocType(ctx.getStringAttribute(LOCATION_TYPE_KEY));
		return retObj;
	}

	/**
	 * This method is not implemented.
	 *
	 * @param arg0 Ignored.
	 * @param arg1 Ignored.
	 */
	@Override
	public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
		// Un-implemented
	}

}
