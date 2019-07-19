/*
 * MediaMasterXmlHelper
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The Media Master XML Helper.
 *
 * @author m314029
 * @since 2.4.0
 */
@Component
public class MediaMasterXmlHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(MediaMasterXmlHelper.class);
	/**
	 * DATE_YYYYMMDDHHMMSSSS.
	 */
	public static final String DATE_YYYYMMDDHHMMSSSS = "yyyy-MM-dd'-'HH.mm.ss.SSSSSS";

	public static final String DATE_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	public static final String PROJECT_NAME = "MainframeMenuLabelPublisher";

	private JAXBContext feJaxbContext;
	/**
	 * Constructor that makes a JAXBContext instance from the MediaMasterEvent class.
	 */
	public MediaMasterXmlHelper() {
		try {
			this.feJaxbContext = JAXBContext.newInstance(MediaMasterEvent.class);
		} catch (JAXBException e) {
			LOGGER.error("Error instantiating JAXB contexts for Media Master message types.", e);
		}
	}
	/**
	 * Create XML from java MediaMasterEvent.
	 * @param mediaMasterEvent    the object
	 * @return the string
	 */
	public String marshallFulfillmentEvent(MediaMasterEvent mediaMasterEvent) {
		String xml = "";
		try {
			StringWriter writer = new StringWriter();
			this.feJaxbContext.createMarshaller().marshal(mediaMasterEvent, writer);
			xml = writer.toString();
		} catch (JAXBException e) {
			LOGGER.error("Unable to Marshall the object to XML: ", e);
		}
		return xml;
	}

	/**
	 * Generate mediaMaster event body.
	 *
	 * @param menuLabel the menuLabel
	 * @return the header
	 */
	public Body generateMediaMasterEventBody(MenuLabel menuLabel) {
		Body body = new Body();
		body.setMenuLabel(menuLabel);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		DateFormat formatter = new SimpleDateFormat(DATE_YYYYMMDDHHMMSSSS, Locale.getDefault());
		body.getMenuLabel().setLastUpdatedTimeStamp(formatter.format(time));
		return body;
	}

	/**
	 * Generate mediaMaster event header.
	 *
	 * @return the header
	 */
	public Header generateMediaMasterEventHeader() {
		//
		Header mediaMasterEventHeader = new Header();
		mediaMasterEventHeader.setProjectName(PROJECT_NAME);
		mediaMasterEventHeader.setTransactionType(StringUtils.EMPTY);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		DateFormat formatter = new SimpleDateFormat(DATE_YYYYMMDDHHMMSS, Locale.getDefault());
		mediaMasterEventHeader.setTimeStamp(formatter.format(time));
		return mediaMasterEventHeader;
	}
}
