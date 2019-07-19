/*
 * AttributeValue
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

/**
 * Represents a dimension and specification.
 *
 * @author vn73545
 * @since 2.0.1
 */
public class AttributeValue {
	private static final long serialVersionUID = 1L;
	private String attrId;
	private long upcId;
	private String name;
	private String details;
	private String description;

	/**
	 * @return the id
	 */
	public String getAttrId() {
		return this.attrId;
	}

	/**
	 * @param baseid
	 *            the id to set
	 */
	public void setAttrId(String baseid) {
		this.attrId = baseid;
	}

	/**
	 * @return the upcId
	 */
	public long getUpcId() {
		return this.upcId;
	}

	/**
	 * @param upcId
	 *            the upcId to set
	 */
	public void setUpcId(long upcId) {
		this.upcId = upcId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param baseName
	 *            the name to set
	 */
	public void setName(String baseName) {
		this.name = baseName;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return this.details;
	}

	/**
	 * @param baseDetails
	 *            the details to set
	 */
	public void setDetails(String baseDetails) {
		this.details = baseDetails;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param baseDescription
	 *            the description to set
	 */
	public void setDescription(String baseDescription) {
		this.description = baseDescription;
	}
}
