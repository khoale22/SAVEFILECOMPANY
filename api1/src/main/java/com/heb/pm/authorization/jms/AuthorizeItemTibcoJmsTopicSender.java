/*
 * AuthorizeItemTibcoJmsTopicSender
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.authorization.jms;

import com.heb.pm.authorization.util.AuthorizationConstants;
import com.heb.pm.authorization.util.AuthorizationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * This is template for Authorize Item.
 *
 * @author vn70529
 * @since 2.23.0
 */
@Service
public class AuthorizeItemTibcoJmsTopicSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeItemTibcoJmsTopicSender.class);
    private static String ERROR_MESSAGE_SENDING_DATA_INTO_POS = "An error occurs while sending message.";
	@Autowired
	@Qualifier("authorizeItemTibcoJmsTopicTemplate")
	private JmsTemplate jmsTemplate;
    /**
     * The Outbound status queue.
     */
    @Value("${jms.authorizeItem.outboundTopic.name}")
    private String topicName;
    /**
     * Use this key to put store id to topic name.
     */
    private static final String REPLACE_STORE_KEY = "xxxxx";
    /**
     * Publish the message.
     * @param message the message to publish.
     * @param transaction the transaction.
     * @param storeId the store id.
     */
    public void send(String message, String transaction, String storeId) {
        try {
            String newTopicName = topicName.replaceFirst(REPLACE_STORE_KEY, AuthorizationUtils.formatDecimal(Integer.parseInt(storeId), AuthorizationConstants.DECIMAL_00000_FORMAT));
            jmsTemplate.setDefaultDestinationName(newTopicName);
            jmsTemplate.send(session -> {
                TextMessage textMessage = session.createTextMessage(message);
                textMessage.setJMSCorrelationID(transaction);
                return textMessage;
            });
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE_SENDING_DATA_INTO_POS, e);
        }
    }
}
