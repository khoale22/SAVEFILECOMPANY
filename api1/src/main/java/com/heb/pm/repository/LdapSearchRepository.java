/*
 * LdapSearchRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.*;

/**
 * The repository to search on Ldap.
 *
 * @author l730832
 * @since  2.6.0
 */
@Repository
public class LdapSearchRepository {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LdapSearchRepository.class);

	@Autowired
	private LdapTemplate hebLdapTemplate;

	@Value("${heb.ldap.userSearchBase}")
	private String searchBase;

	@Value("${heb.ldap.url}")
	private String ldapUrl;

	/**
	 * This is the mapper that comes from LDAP and maps it to User.
	 */
	private class LdapAttributeMapper implements AttributesMapper<User> {

		/**
		 * Other attributes that may be needed similiar to "uid".
		 * hebJobCode      - the Heb Job Code.
		 * hebjobdesc      - the Heb Job Desc
		 * mail            - the mail.
		 * hebUser         - the Heb User
		 * hebDeptName     - the Heb Dept Name
		 * hebGLdepartment - the Heb GL Department
		 * hebGLlocation   - the Heb GL Location
		 * hebRegion       - the Heb role code.
		 * @param attributes
		 * @return
		 * @throws NamingException
		 */
		@Override
		public User mapFromAttributes(Attributes attributes) throws NamingException {
			User user = new User();

			if (attributes.get("uid") != null) {
				user.setUid(attributes.get("uid").get().toString());
			}
			if (attributes.get("hebLegacyID") != null) {
				user.setHebLegacyID(attributes.get("hebLegacyID").get().toString());
			}
			if (attributes.get("givenName") != null) {
				user.setGivenName(attributes.get("givenName").get().toString());
			}
			if (attributes.get("sn") != null) {
				user.setLastName(attributes.get("sn").get().toString());
			}
			if(attributes.get("cn") != null) {
				user.setFullName(attributes.get("cn").get().toString());
			}
			if (attributes.get("mail") != null) {
				user.setMail(attributes.get("mail").get().toString());
			}
			if (attributes.get("telephonenumber") != null) {
				user.setTelephoneNumber(attributes.get("telephonenumber").get().toString());
			}
			return user;
		}
	}

	/**
	 * Returns a list of users from LDAP using the uid or the hebLegacyId.
	 * @param userNameList
	 * @return List of users without duplicates.
	 * @throws Exception
	 */
	public List<User> getUserList(Iterable<String> userNameList) throws Exception {
		List<User> users = new ArrayList<>();

		if (LdapSearchRepository.logger.isDebugEnabled()) {
			LdapSearchRepository.logger.debug("Connected to LDAP with URL " + this.ldapUrl);
			StringBuilder sb = new StringBuilder();
			Iterator<String> userNameIterator = userNameList.iterator();
			while (userNameIterator.hasNext()) {
				sb.append(userNameIterator.next());
				if (userNameIterator.hasNext()) {
					sb.append(",");
				}
			}
			LdapSearchRepository.logger.debug("Looking for users [" + sb.toString() + "]");
		}

		OrFilter filter = new OrFilter();
		OrFilter hebLegacyIdFilter = new OrFilter();
		if(userNameList != null) {
			for (String userName : userNameList) {
				if(StringUtils.isNotBlank(userName)) {
					filter.or(new EqualsFilter("uid", userName.trim()));
					hebLegacyIdFilter.or(new EqualsFilter("hebLegacyId", userName.trim()));
					filter.append(hebLegacyIdFilter);
				}
			}
			users = this.hebLdapTemplate.search(this.searchBase, filter.encode(), new LdapAttributeMapper());
		}

		if(users != null) {
			Set<User> hashset = new HashSet<>(users);
			users.clear();
			users.addAll(hashset);
		}
		return users;
	}
}
