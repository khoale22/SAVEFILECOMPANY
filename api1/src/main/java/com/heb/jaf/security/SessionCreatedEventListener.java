package com.heb.jaf.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by vn40486 on 5/18/2016.
 */
@Component
public class SessionCreatedEventListener implements ApplicationListener<HttpSessionCreatedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SessionCreatedEventListener.class);

    @Value("${app.sessionIdleTimeout}")
    private int sessionIdleTimeout;

    @Override
    public void onApplicationEvent(HttpSessionCreatedEvent httpSessionCreatedEvent) {
        /*SecurityContext and Authentication/Principal information in the HttpSession at this point are NULL/Empty.
        Hence unable to log the user information at this level. */

        /* Setting User Session Idle Timeout  */
        httpSessionCreatedEvent.getSession().setMaxInactiveInterval(sessionIdleTimeout);
    }

}
