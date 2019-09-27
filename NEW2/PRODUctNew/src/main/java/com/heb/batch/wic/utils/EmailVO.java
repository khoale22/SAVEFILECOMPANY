/*
 * EmailVO
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @author vn55306.
 */

public class EmailVO implements Serializable {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private String toAddress = "";
	private String fromAddress = "";
	private String subjectName = "";
	private String bodyContent = "";
	private String ccAddress = "";

	private List<String> attachment;

	/**
	 * Default Constructor.
	 */
	public EmailVO() {
		super();

	}


	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public List<String> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<String> attachment) {
		this.attachment = attachment;
	}

	public String getCcAddress() {
		return ccAddress;
	}

}
