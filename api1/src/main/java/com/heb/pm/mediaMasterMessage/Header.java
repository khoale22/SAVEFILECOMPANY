/*
 * Header
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

/**
 * The type Header that goes into a MediaMasterMessage.
 *
 * @author m314029
 * @since 2.4.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Header", propOrder = {
		"projectName",
		"transactionType",
		"timeStamp"
})
public class Header implements Serializable{
	private final static long serialVersionUID = 100L;
	/**
	 * The Project name.
	 */
	@XmlElement(name = "ProjectName", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected String projectName;
	/**
	 * The Transaction type.
	 */
	@XmlElement(name = "Transaction_Type", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected String transactionType;
	/**
	 * The Time stamp.
	 */
	@XmlElement(name = "TimeStamp", required = true, namespace = "http://xmlns.heb.com/ei/MENULABEL")
	protected String timeStamp;

	/**
	 * Gets project name.
	 *
	 * @return the project name
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Sets project name.
	 *
	 * @param projectName the project name
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Gets transaction type.
	 *
	 * @return the transaction type
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * Sets transaction type.
	 *
	 * @param transactionType the transaction type
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Gets time stamp.
	 *
	 * @return the time stamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Sets time stamp.
	 *
	 * @param timeStamp the time stamp
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
