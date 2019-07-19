/*
 * com.heb.jaf.security.LocalSecuirytConfiguration
 *
 * Copyright (c) 2014 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * This is an extension of {@link SecurityConfiguration} providing local-only security configuration.
 *
 * @author p235969
 */
@Deprecated
@Profile({"unused"})
@Configuration
@ConfigurationProperties
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class LocalSecurityConfiguration extends SecurityConfiguration {

    /**
     * Configures users data with default roles for running locally. Edit this
     * to update the users you're able to log in with.
	 *
     * @param auth Object that allows you to configure how users are authenticated
     *             and authorized.
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("user").roles("USER").and()
                .withUser("vendor").password("vendor").roles("USER", "VENDOR").and()
                .withUser("admin").password("admin").roles("USER", "ADMIN");
    }
}
