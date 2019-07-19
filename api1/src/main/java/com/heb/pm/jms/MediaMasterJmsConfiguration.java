/*
 * ApplicationJmsConfiguration
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.jms;

import com.tibco.tibjms.TibjmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * The type MediaMaster jms configuration.
 *
 * @author m314029
 * @since 2.4.0
 */
@Profile({"dev", "cert", "prod", "local"})
@Configuration
@ConfigurationProperties
@EnableJms
public class MediaMasterJmsConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(MediaMasterJmsConfiguration.class);
	/**
	 * The Outbound url.
	 */
	@Value("${jms.mediaMaster.outboundUrl}")
	private String outboundUrl;
	/**
	 * The Outbound user id.
	 */
	@Value("${jms.mediaMaster.outboundUserId}")
	private String outboundUserId;
	/**
	 * The Outbound password.
	 */
	@Value("${jms.mediaMaster.outboundPassword}")
	private String outboundPassword;
	/**
	 * The Outbound status queue.
	 */
	@Value("${jms.mediaMaster.outboundStatusQueue}")
	private String outboundStatusQueue;

	/**
	 * The connection factory to the queue. At this time there is one server with the same username and password so only one connection
	 * is needed.
	 *
	 * @return the caching connection factory
	 * @throws JMSException the jms exception
	 */
	@Bean(name="mediaMasterJmsCachingConnectionFactory")
	public CachingConnectionFactory cachingConnectionFactory() throws JMSException {
		LOGGER.info("Configuring caching connection factory with TIBCO connection factory...");
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		TibjmsConnectionFactory tibjmsConnectionFactory =  new TibjmsConnectionFactory();
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
	@Bean(name = "jmsTemplate")
	public JmsTemplate outboundStatusJmsTemplate(@Qualifier("mediaMasterJmsCachingConnectionFactory")ConnectionFactory cachingConnectionFactory) throws JMSException {
		LOGGER.info("Creating JmsTemplate for the outbound message queue...");
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(cachingConnectionFactory);
		jmsTemplate.setDefaultDestinationName(this.outboundStatusQueue);
		return jmsTemplate;
	}
}
