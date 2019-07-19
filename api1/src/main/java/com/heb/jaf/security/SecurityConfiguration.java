/*
 * com.heb.jaf.security.SecurityConfiguration
 *
 * Copyright (c) 2014 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Configures Spring Web Security.
 *
 * @author p235969
 */
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String JAVA_SESSION_COOKIE_NAME = "JSESSIONID";
    private static final String ANGULAR_CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    @Value("${spring.application.name}")
    private String realmName;

    @Value("${app.permitAllUrlBase}")
    private String permitAllUrlBase;

    @Value("${app.permitAllWsagUrlBase}")
    private String permitAllWsagUrlBase;

    @Value("${app.secureUrlBase}")
    private String secureUtlBase;

    @Value("${app.loginUrl}")
    private String loginUrl;

    @Value("${app.logoutUrl}")
    private String logoutUrl;

    @Value("${app.loginPage}")
    private String loginPage;

    @Value("${app.maxSessions}")
    private int maxSessions;

    @Autowired
    private EntryPointHandler entryPointHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    /**
     * Configures Spring Web Security.
	 *
     * @param http An object that allows this function to configure HTTP security.
     * @throws Exception Any error that happens during configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Configure authentication/authorization
        http
                .authorizeRequests()
                // Set the status URL to not require the user to be logged on.
                .antMatchers(this.permitAllUrlBase + "**").permitAll()
				.antMatchers(this.permitAllWsagUrlBase + "/**").permitAll()
				.antMatchers("/api/login").permitAll()
                // Everything else must be authenticated.
                .anyRequest().authenticated()
                .and()
                // Set the realm name for this security configuration
                // Everything under this realm should support the same
                // set of user credentials.
                .httpBasic().realmName(this.realmName)
                .and()
                .exceptionHandling()
                // Set the entry point to the login handling scheme.
                // It will always return a 401.
                .authenticationEntryPoint(this.entryPointHandler)
                .and()
                .formLogin()
                .loginProcessingUrl(this.loginUrl).loginPage(this.loginPage)
                .successHandler(this.authenticationSuccessHandler)
                .failureHandler(this.authenticationFailureHandler)
                .and()
                .logout().logoutUrl(this.logoutUrl)
                // Cookies to delete  when the user logs out.
                .deleteCookies(SecurityConfiguration.JAVA_SESSION_COOKIE_NAME)
                .deleteCookies(CsrfTokenResponseHeaderFilter.ANGULAR_CSRF_COOKIE_NAME)
                .invalidateHttpSession(true).logoutSuccessUrl(this.loginPage)
                // Add custom CSRF filters after the existing one in Spring security.
                .and().addFilterAfter(new CsrfTokenResponseHeaderFilter(), CsrfFilter.class)
                .csrf().csrfTokenRepository(this.csrfTokenRepository())
                .ignoringAntMatchers(this.logoutUrl, this.loginUrl)
                .and()
                .sessionManagement().maximumSessions(this.maxSessions);
    }

	/**
	 * Creates a repository used to generate CSRF tokens. This repository will tell Spring to
	 * expect the token in the header that Angular sends by default.
	 *
	 * This function is based on an example that can be found here:
	 * https://spring.io/blog/2015/01/12/the-login-page-angular-js-and-spring-security-part-ii
	 *
	 * @return A CSRF header repository.
	 */
    private CsrfTokenRepository csrfTokenRepository() {
        HEBCookieTokenRepository repository = new HEBCookieTokenRepository();
        repository.setHeaderName(SecurityConfiguration.ANGULAR_CSRF_HEADER_NAME);

        return repository;
    }
}
