/*
 * com.heb.jaf.security.HebAuthoritiesPopulator
 *
 * Copyright (c) 2010 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

/**
 * Populates user roles for an authenticated user. This implementation uses ARBAF  *
 * as it's source.
 *
 * @author r511759
 */
@Profile({"dev", "cert", "prod", "local"})
@Component
public class HebAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	private static final String HEB_JOB_CODE_LDAP_PROPERTY = "hebJobCode";
	private static final String USER_NAME_REQUIRED_ERROR_MESSAGE = "A username is required to get GrantedAuthorities";
	private static final String APPLICATION_NAME_REQUIRED_ERROR_MESSAGE = "Application id is null";
	private static final String APPLICATION_NOT_IN_ARBAF_ERROR_MESSAGE =
			"Application has not been configured to use the targeted ARBAF environment";
	private static final String USER_ID_LOG_MESSAGE = "Retrieving authorities for user %s";
	private static final String NO_JOB_CODE_ERROR_MESSAGE = "Job code not available for %s";
	private static final String USR_ROLE_CODE_COLUMN = "usr_role_cd";


	private static final Logger logger = LoggerFactory.getLogger(HebAuthoritiesPopulator.class);

	@Autowired
	@Qualifier("arbafDao")
	private JdbcTemplate arbafDao;

	// This application's ARBAF ID. You can change it by modifying spring.application.name
	// in application.properties.
	@Value("${arbaf.applAbb}")
	private String applAbb = "JAF";

	private static final String FIND_APPLICATION = "select appl_id from appl_nm where appl_nm=?";

	private static final String FIND_ROLES_BY_ONEPASS = "  select usr_sec_grp.usr_role_cd " +
			"from usr_role, usr_sec_grp " +
			"where usr_sec_grp.usr_role_cd=usr_role.usr_role_cd " +
			"and appl_id=? and usr_sec_grp.usr_id=?";

	private static final String FIND_ROLES_BY_ONEPASS_AND_JOBCODE = "select usr_role.usr_role_cd " +
			"from idm, usr_role, usr_sec_grp  " +
			"where idm.usr_id=usr_sec_grp.usr_id  " +
			"and usr_sec_grp.usr_role_cd=usr_role.usr_role_cd " +
			"and appl_id=? " +
			"and idm.usr_id=?  " +
			"union " +
			"select usr_role.usr_role_cd " +
			"from idm, usr_role, usr_sec_grp, job_cd, usr_role_job_cd " +
			"where job_cd.job_cd=usr_role_job_cd.job_cd " +
			"and usr_role_job_cd.usr_role_cd=usr_role.usr_role_cd " +
			"and appl_id=? " +
			"and job_cd.job_cd=?";

	private static final String FIND_RESOURCES_BY_ROLE_CODE = "select resrc_nm, acs_abb " +
			"from sec_grp_resrc, access_type, resrc " +
			"where sec_grp_resrc.acs_cd=access_type.acs_cd " +
			"and sec_grp_resrc.resrc_id=resrc.resrc_id " +
			"and usr_role_cd=?";

	// RowMapper for the FIND_RESOURCES_BY_ROLE_CODE query.
	private RowMapper<Permission> resourcesRowMapper = new RowMapper<Permission>() {

		private static final String RESOURCE_NAME_COLUMN = "resrc_nm";
		private static final String ACCESS_TYPE_COLUMN = "acs_abb";

		@Override
		public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {

			Permission retObj = new Permission();
			retObj.setResourceName(rs.getString(RESOURCE_NAME_COLUMN));
			retObj.setAccessType(rs.getString(ACCESS_TYPE_COLUMN));
			return retObj;
		}
	};

	/**
	 * Returns GrantedAuthority objects for an authenticated user.
	 *
	 * @param ldapCtx The LDAP directory context. This is used to retrieve job code.
	 * @param userId The one-pass ID of the authenticated user.
	 * @return A collection of GrantedAuthorities for a user. This will be a
	 * collection that includes both Role objects (which represent the roles a user is assigned to)
	 * and Permission objects (which represent the user's access level to the various resources
	 * of the application).
	 */
	@Override
	public Collection<GrantedAuthority> getGrantedAuthorities(DirContextOperations ldapCtx, String userId) {

		if (userId == null) {
			throw new IllegalArgumentException(HebAuthoritiesPopulator.USER_NAME_REQUIRED_ERROR_MESSAGE);
		}

		HebAuthoritiesPopulator.logger.debug(String.format(HebAuthoritiesPopulator.USER_ID_LOG_MESSAGE, userId));

		if (this.applAbb == null) {
			throw new IllegalArgumentException(HebAuthoritiesPopulator.APPLICATION_NAME_REQUIRED_ERROR_MESSAGE);
		}

		// Get the ARBAF ID for this application.
		Integer applId = this.arbafDao.queryForObject(HebAuthoritiesPopulator.FIND_APPLICATION,
				Integer.class, this.applAbb);
		if (applId == null) {
			throw new IllegalArgumentException(HebAuthoritiesPopulator.APPLICATION_NOT_IN_ARBAF_ERROR_MESSAGE);
		}

		// Get this user's job code.
		String jobCode = ldapCtx.getStringAttribute(HebAuthoritiesPopulator.HEB_JOB_CODE_LDAP_PROPERTY);
		Set<GrantedAuthority> roles = new HashSet<>();
		List<Map<String, Object>> results;

		// Get a list of roles for the user for this application.
		// If job code is available, get the ones for the user's ID and the job code.
		// If job code is not available, just get the roles for the user ID.
		if (jobCode != null) {
			results = this.arbafDao.queryForList(HebAuthoritiesPopulator.FIND_ROLES_BY_ONEPASS_AND_JOBCODE,
					applId, userId, applId, jobCode);
		} else {
			HebAuthoritiesPopulator.logger.warn(
					String.format(HebAuthoritiesPopulator.NO_JOB_CODE_ERROR_MESSAGE, userId));
			results = this.arbafDao.queryForList(HebAuthoritiesPopulator.FIND_ROLES_BY_ONEPASS, applId, userId);
		}

		for (Map<String, Object> role : results) {

			roles.addAll(this.arbafDao.query(HebAuthoritiesPopulator.FIND_RESOURCES_BY_ROLE_CODE,
					this.resourcesRowMapper, role.get(HebAuthoritiesPopulator.USR_ROLE_CODE_COLUMN)));
		}

		return roles;
	}

	/**
	 * Return the ARBAF abbreviation for this application. q
	 *
	 * @return The ARBAF abbreviation for this application.
	 */
	public String getApplAbb() {
		return this.applAbb;
	}

	/**
	 * Set the ARBAF abbreviation for this application.
	 *
	 * @param applAbb The ARBAF abbreviation for this application.
	 */
	public void setApplAbb(String applAbb) {
		this.applAbb = applAbb;
	}

	/**
	 * Returns the database connection to ARBAF.
	 *
	 * @return The database connection to ARBAF.
	 */
	public JdbcTemplate getArbafDao() {
		return arbafDao;
	}

	/**
	 * Sets the database connection to ARBAF.
	 *
	 * @param arbafDao The database connection to ARBAF.
	 */
	public void setArbafDao(JdbcTemplate arbafDao) {
		this.arbafDao = arbafDao;
	}
}
