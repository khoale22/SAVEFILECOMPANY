/*
 * com.heb.jaf.security.HebLdapUserService
 *
 * Copyright (c) 2010 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

/**
 * Provides user information from HEB's LDAP providers and ARBAF.
 *
 * @author r511759
 */
public class HebLdapUserService implements UserDetailsService {

	private static final String USER_SEARCH_LOG_MESSAGE = "Searching LDAP for user %s";
	private static final String USER_NOT_FOUND_LOG_MESSAGE = "User %s not found";
	private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found on any of the specified ldap contexts";

	private static final Logger logger = LoggerFactory.getLogger(HebLdapUserService.class);

	private List<LdapUserSearch> userFinders;
	private UserDetailsContextMapper userMapper;
	private LdapAuthoritiesPopulator authPopulator;

	/**
	 * Search HEB's LDAP providers for a user's information. If found, will connect
	 * to ARBAF and populate the user's GrantedAuthorities.
	 *
	 * @param userId The one-pass ID of the user attempting to log in.
	 * @return A populated UserDetails object. See HebUserDetails mapper for specifics of what is populated.
	 * @throws UsernameNotFoundException Thrown when a user's ID is not found.
	 * @throws DataAccessException Thrown when there is a problem with a data source.
	 */
	@Override
	public UserDetails loadUserByUsername(String userId) {

		HebLdapUserService.logger.debug(String.format(HebLdapUserService.USER_SEARCH_LOG_MESSAGE, userId));

		DirContextOperations ldapUser = null;

		// Try to search each ldap server for the user ID.
		for (LdapUserSearch finder : this.getUserFinders()) {
			try {
				ldapUser = finder.searchForUser(userId);
				if (ldapUser != null) {
					break;
				}
			} catch (UsernameNotFoundException e) {
				HebLdapUserService.logger.debug(String.format(HebLdapUserService.USER_NOT_FOUND_LOG_MESSAGE, userId));
				// Ignore the exception for now, as it is possible that a user is not found. If
				// the user is not found across multiple LDAP providers, an error is thrown below.
			}
		}

		if (ldapUser == null) {
			throw new UsernameNotFoundException(HebLdapUserService.USER_NOT_FOUND_ERROR_MESSAGE);
		}

		// Populate with permissions
		Collection<? extends GrantedAuthority> authorities =
				this.getAuthPopulator().getGrantedAuthorities(ldapUser, userId);

		// Set the fields from ldap
		return this.getUserMapper().mapUserFromContext(ldapUser, userId, authorities);
	}

	/**
	 * Returns a list of objects that will search LDAP providers.
	 *
	 * @return A list of objects that will search LDAP providers.
	 */
	public List<LdapUserSearch> getUserFinders() {
		return this.userFinders;
	}

	/**
	 * Sets the list of objects that will search LDAP providers.
	 *
	 * @param userFinders The list of objects that will search LDAP providers.
	 */
	public void setUserFinders(List<LdapUserSearch> userFinders) {
		this.userFinders = userFinders;
	}

	/**
	 * Returns the object that will populate a user's GrantedAuthorities.
	 *
	 * @return The object that will populate a user's GrantedAuthorities.
	 */
	public LdapAuthoritiesPopulator getAuthPopulator() {
		return this.authPopulator;
	}

	/**
	 * Returns the object that will map the user's LDAP details.
	 *
	 * @return The object that will map the user's LDAP details.
	 */
	public UserDetailsContextMapper getUserMapper() {
		return this.userMapper;
	}

	/**
	 * Sets he object that will map the user's LDAP details.
	 *
	 * @param userMapper The object that will map the user's LDAP details.
	 */
	public void setUserMapper(UserDetailsContextMapper userMapper) {
		this.userMapper = userMapper;
	}

	/**
	 * Sets the object that will populate a user's GrantedAuthorities.
	 *
	 * @param authPopulator The object that will populate a user's GrantedAuthorities.
	 */
	public void setAuthPopulator(LdapAuthoritiesPopulator authPopulator) {
		this.authPopulator = authPopulator;
	}
}
