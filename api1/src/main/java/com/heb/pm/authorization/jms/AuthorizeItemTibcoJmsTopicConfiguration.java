/*
 *  AuthorizeItemTibcoJmsTopicConfiguration
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization.jms;

import com.tibco.tibjms.TibjmsTopicConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * The type jms configuration for authorize item.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Profile({"dev", "cert", "prod", "local"})
@Configuration
@ConfigurationProperties
@EnableJms
public class AuthorizeItemTibcoJmsTopicConfiguration {
    /**
     * The Outbound url.
     */
    @Value("${jms.authorizeItem.outboundUrl}")
    private String outboundUrl;
    /**
     * The Outbound user id.
     */
    @Value("${jms.authorizeItem.outboundUsername}")
    private String outboundUserId;
    /**
     * The Outbound password.
     */
    @Value("${jms.authorizeItem.outboundPassword}")
    private String outboundPassword;


    /**
     * The connection factory to the queue. At this time there is one server with the same username and password so only one connection
     * is needed.
     *
     * @return the caching connection factory
     * @throws JMSException the jms exception
     */
    @Bean(name="authorizeItemTibcoJmsTopicCachingConnectionFactory")
    @Primary
    public CachingConnectionFactory cachingConnectionFactory() throws JMSException {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        TibjmsTopicConnectionFactory tibjmsConnectionFactory =  new TibjmsTopicConnectionFactory();
        tibjmsConnectionFactory.setServerUrl(this.outboundUrl);
        tibjmsConnectionFactory.setUserName(this.outboundUserId);
        tibjmsConnectionFactory.setUserPassword(this.outboundPassword);
        cachingConnectionFactory.setTargetConnectionFactory(tibjmsConnectionFactory);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    /**
     * Outbound status jms template jms template. This is the default spring jms template. The default queue is the outbound queue.
     *
     * @param cachingConnectionFactory the caching connection factory
     * @return the jms template
     * @throws JMSException the jms exception
     */
    @Bean(name = "authorizeItemTibcoJmsTopicTemplate")
    public JmsTemplate outboundStatusJmsTemplate(@Qualifier("authorizeItemTibcoJmsTopicCachingConnectionFactory")ConnectionFactory cachingConnectionFactory){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(cachingConnectionFactory);
        // For publish topic
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }
}
