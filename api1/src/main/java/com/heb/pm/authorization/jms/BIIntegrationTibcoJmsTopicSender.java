/*
 * BIIntegrationTibcoJmsTopicSender
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.authorization.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * This is template for BI Integration for new item setup.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Service
public class BIIntegrationTibcoJmsTopicSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(BIIntegrationTibcoJmsTopicSender.class);
    private static String ERROR_MESSAGE_SENDING_DATA_INTO_BI = "An error occurs while sending message to BI Integration.";
	@Autowired
	@Qualifier("bIIntegrationTibcoJmsTopicTemplate")
	private JmsTemplate jmsTemplate;
    /**
     * Send the message.
     * @param message the message to send.
     * @param transaction the transaction.
     */
    public void send(String message, String transaction) {
        try {
            jmsTemplate.send(session -> {
                TextMessage textMessage = session.createTextMessage(message);
                textMessage.setJMSCorrelationID(transaction);
                return textMessage;
            });
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE_SENDING_DATA_INTO_BI, e);
        }
    }
}
