/*
 * MediaMasterMessageSender
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.jms;

import com.heb.pm.mediaMasterMessage.Body;
import com.heb.pm.mediaMasterMessage.Header;
import com.heb.pm.mediaMasterMessage.MediaMasterEvent;
import com.heb.pm.mediaMasterMessage.MenuLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Media Master message sender.
 *
 * @author m314029
 * @since 2.4.0
 */
@Component
public class MediaMasterMessageSender {
	private static final Logger LOGGER = LoggerFactory.getLogger(MediaMasterMessageSender.class);
	private static final String PROJECT_NAME = "MainframeMenuLabelPublisher";
	private static final String MAINFRAME_DATE_FORMAT = "yyyy-MM-dd-HH.mm.ss";
	/**
	 * The Jms messaging template. This uses the default jms template which is the outbound queue.
	 */
	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private MediaMasterXmlHelper mediaMasterXmlHelper;
	/**
	 * Send status update.
	 *
	 * @param menuLabel the menuLabel
	 */
	public void sendStatusUpdate(MenuLabel menuLabel) {
		MediaMasterEvent mediaMasterEvent = new MediaMasterEvent();
		mediaMasterEvent.setHeader(this.generateMediaMasterEventHeader());
		mediaMasterEvent.setBody(this.generateMediaMasterEventBody(menuLabel));
		LOGGER.info("Sending " + menuLabel.getAttributeNameText() + " event for product id: " + menuLabel.getProdId());
		Object messageBody = this.mediaMasterXmlHelper.marshallFulfillmentEvent(mediaMasterEvent);
		this.jmsTemplate.convertAndSend(messageBody);
	}

	/**
	 * Generate mediaMaster event body.
	 *
	 * @param menuLabel the menuLabel
	 * @return the header
	 */
	private Body generateMediaMasterEventBody(MenuLabel menuLabel) {
		Body body = new Body();
		body.setMenuLabel(menuLabel);
		DateTimeFormatter format = DateTimeFormatter.ofPattern(MAINFRAME_DATE_FORMAT);
		body.getMenuLabel().setLastUpdatedTimeStamp(LocalDateTime.now().format(format));
		return body;
	}

	/**
	 * Generate mediaMaster event header.
	 *
	 * @return the header
	 */
	private Header generateMediaMasterEventHeader() {
		Header mediaMasterEventHeader = new Header();
		mediaMasterEventHeader.setProjectName(MediaMasterMessageSender.PROJECT_NAME);
		mediaMasterEventHeader.setTransactionType("");
		DateTimeFormatter format = DateTimeFormatter.ofPattern(MAINFRAME_DATE_FORMAT);
		mediaMasterEventHeader.setTimeStamp(LocalDateTime.now().format(format));
		return mediaMasterEventHeader;
	}
}
