/*
 * MediaMasterEvent
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.mediaMasterMessage;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * The type Media Master Event.
 *
 * @author m314029
 * @since 2.4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Response", propOrder = {
		"header",
		"body"
})
@XmlRootElement(name = "MSG_Container", namespace = "http://xmlns.heb.com/ei/MENULABEL")
public class MediaMasterEvent implements Serializable{
	private final static long serialVersionUID = 100L;

	/**
	 * The Header.
	 */
	@XmlElement(name = "Header", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected Header header;

	/**
	 * The Body.
	 */
	@XmlElement(name = "Body", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected Body body;

	/**
	 * Gets header.
	 *
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}

	/**
	 * Sets header.
	 *
	 * @param header the header
	 */
	public void setHeader(Header header) {
		this.header = header;
	}

	/**
	 * Gets body.
	 *
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Sets body.
	 *
	 * @param body the body
	 */
	public void setBody(Body body) {
		this.body = body;
	}
}
