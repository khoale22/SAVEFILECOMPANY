/*
 * MenuLabel
 *  
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information 
 *  of HEB.
 */

package com.heb.pm.mediaMasterMessage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * The type Menu Label that goes into a MediaMasterMessage Body.
 *
 * @author m314029
 * @since 2.4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MENULABEL", propOrder = {
		"prodId",
		"attributeNameText",
		"lastUpdatedTimeStamp"
})
public class MenuLabel implements Serializable {
	private final static long serialVersionUID = 100L;

	/**
	 * The Prod id.
	 */
	@XmlElement(name = "PROD-ID", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected BigInteger prodId;

	/**
	 * The Attribute name text.
	 */
	@XmlElement(name = "ATTR-NM-TEXT", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected String attributeNameText;

	/**
	 * The Last updated time stamp.
	 */
	@XmlElement(name = "LST-UPDT-TS", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected String lastUpdatedTimeStamp;

	/**
	 * Gets prod id.
	 *
	 * @return the prod id
	 */
	public BigInteger getProdId() {
		return prodId;
	}

	/**
	 * Sets prod id.
	 *
	 * @param prodId the prod id
	 */
	public void setProdId(BigInteger prodId) {
		this.prodId = prodId;
	}

	/**
	 * Gets attribute name text.
	 *
	 * @return the attribute name text
	 */
	public String getAttributeNameText() {
		return attributeNameText;
	}

	/**
	 * Sets attribute name text.
	 *
	 * @param attributeNameText the attribute name text
	 */
	public void setAttributeNameText(String attributeNameText) {
		this.attributeNameText = attributeNameText;
	}

	/**
	 * Gets last updated time stamp.
	 *
	 * @return the last updated time stamp
	 */
	public String getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * Sets last updated time stamp.
	 *
	 * @param lastUpdatedTimeStamp the last updated time stamp
	 */
	public void setLastUpdatedTimeStamp(String lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	public MenuLabel() {
	}

	public MenuLabel(BigInteger prodId, String attributeNameText) {
		this.prodId = prodId;
		this.attributeNameText = attributeNameText;
	}
}
