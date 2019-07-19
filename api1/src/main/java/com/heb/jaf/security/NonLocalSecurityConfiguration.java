/*
 * com.heb.jaf.security.NonLocalSecurityConfiguration
 *
 * Copyright (c) 2015 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

/**
 * Security Configuration for a Non-local environment. It will configure connections to
 * LDAP (Partner and Vendor) and ARBAF.
 *
 * @author p235969
 */
@Profile({"dev", "cert", "prod", "local"})
@Configuration
@ConfigurationProperties
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class NonLocalSecurityConfiguration extends SecurityConfiguration {
	@Value("${heb.ldap.url}")
	private String url;
	@Value("${heb.ldap.root}")
	private String root;
	@Value("${heb.ldap.userSearchBase}")
	private String userSearchBase;
	@Value("${heb.ldap.userSearchFilter}")
	private String userSearchFilter;
	@Value("${heb.ldap.managerDn}")
	private String managerDn;
	@Value("${heb.ldap.managerPassword}")
	private String managerPassword;

	@Value("${vendor.ldap.url}")
	private String urlV;
	@Value("${vendor.ldap.root}")
	private String rootV;
	@Value("${vendor.ldap.userSearchBase}")
	private String userSearchBaseV;
	@Value("${vendor.ldap.userSearchFilter}")
	private String userSearchFilterV;
	@Value("${vendor.ldap.managerDn}")
	private String managerDnV;
	@Value("${vendor.ldap.managerPassword}")
	private String managerPasswordV;

	@Autowired
	@Qualifier("arbafDataSource")
	private DataSource arbafDataSource;

	@Autowired
	private HebAuthoritiesPopulator hebAuthoritiesPopulator;

	/**
	 * Returns a JdbcTemplate connected to the ARBAF database. It is created as a Spring managed bean
	 * with the ID arbafDao.
	 *
	 * @return The connection to the ARBAF database.
	 */
	@Bean(name = "arbafDao")
	public JdbcTemplate arbafDao() {
		return new JdbcTemplate(this.getArbafDataSource());
	}

	/**
	 * Returns the connection to the ARBAF database.
	 *
	 * @return The connection to the ARBAF database.
	 */
	public DataSource getArbafDataSource() {
		return this.arbafDataSource;
	}

	/**
	 * Returns the LdapTemplate connected to HEB's internal LDAP provider. It is created as a Spring
	 * managed bean with the ID hebLdapTemplate.
	 *
	 * @return The LdapTemplate connected to HEB's internal LDAP provider.
	 * @throws Exception
	 */
	@Bean(name = "hebLdapTemplate")
	public LdapTemplate ldapTemplate() throws Exception {
		LdapTemplate ldapTemplate = new LdapTemplate();
		ldapTemplate.setContextSource(contextSource());
		ldapTemplate.afterPropertiesSet();

		return ldapTemplate;
	}

	/**
	 * Returns the LDAP context for HEB's internal LDAP provider. It is created as a Spring managed
	 * bean witht he ID hebLdapContext.
	 *
	 * @return The LDAP context for HEB's internal LDAP provider.
	 * @throws Exception
	 */
	@Bean(name = "hebLdapContext")
	public BaseLdapPathContextSource contextSource() throws Exception {
		DefaultSpringSecurityContextSource contextSource =
				new DefaultSpringSecurityContextSource(this.url);
		contextSource.setUserDn(this.managerDn);
		contextSource.setPassword(this.managerPassword);
		contextSource.setBase(this.root);
		contextSource.afterPropertiesSet();

		return contextSource;
	}

	// Keep this here for now to check into the TODOs in HebUserDetails
	// remove it after.
    /*@Bean(name = "vendorLdapTemplate")
    public LdapTemplate vendorTemplate() throws Exception {
        LdapTemplate ldapTemplate = new LdapTemplate();
        ldapTemplate.setContextSource(vendorContext());
        ldapTemplate.afterPropertiesSet();

        return ldapTemplate;
    }*/

    /*@Bean(name = "vendorLdapContext")
    public BaseLdapPathContextSource vendorContext() throws Exception {
        DefaultSpringSecurityContextSource contextSource =
                new DefaultSpringSecurityContextSource(this.urlV);
        contextSource.setUserDn(this.managerDnV);
        contextSource.setPassword(this.managerPasswordV);
        contextSource.setBase(this.rootV);
        contextSource.afterPropertiesSet();

        return contextSource;
    }*/

	/**
	 * Configures non-local authentication configuration. This version only
	 * sets up partner LDAP.
	 *
	 * @param auth Object that allows you to configure how users are authenticated
	 *             and authorized.
	 * @throws Exception
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				// Partner LDAP
				.ldapAuthentication()
				.userSearchBase(this.userSearchBase)
				.userSearchFilter(this.userSearchFilter)
				.contextSource(contextSource())
				.userDetailsContextMapper(new HebUserDetailsMapper())
				.ldapAuthoritiesPopulator(this.hebAuthoritiesPopulator);

       /* auth
                // Vendor LDAP
                .ldapAuthentication()
                .userSearchBase(this.userSearchBaseV)
                .userSearchFilter(this.userSearchFilterV)
                .contextSource(vendorContext())
                .userDetailsContextMapper(new HebUserDetailsMapper())
                .ldapAuthoritiesPopulator(this.hebAuthoritiesPopulator); */

		auth
				.userDetailsService(new HebLdapUserService());
	}
}
