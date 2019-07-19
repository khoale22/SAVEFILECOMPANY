/*
 *  HebGuaranteeTypeCode
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents HEB_GUARN_TYP information.
 *
 * @author s573181
 * @since 2.14.0
 */
@Entity
@Table(name = "HEB_GUARN_TYP")
public class HebGuaranteeTypeCode implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "HEB_GUARN_TYP_CD")
	private String hebGuaranteeTypeCode;
	@Column(name = "HEB_GUARN_TYP_ABB")
	private String hebGuaranteeTypeAbb;
	@Column(name = "HEB_GUARN_TYP_DES")
	private String hebGuaranteeTypeDescription;

	/**
	 * Returns the hebGuaranteeTypeCode.
	 *
	 * @return the hebGuaranteeTypeCode.
	 */
	public String getHebGuaranteeTypeCode() {
		return hebGuaranteeTypeCode;
	}

	/**
	 * Sets the hebGuaranteeTypeCode.
	 * @param hebGuaranteeTypeCode the hebGuaranteeTypeCode.
	 */
	public void setHebGuaranteeTypeCode(String hebGuaranteeTypeCode) {
		this.hebGuaranteeTypeCode = hebGuaranteeTypeCode;
	}

	/**
	 * Returns the hebGuaranteeTypeAbb.
	 * @return the hebGuaranteeTypeAbb.
	 */
	public String getHebGuaranteeTypeAbb() {
		return hebGuaranteeTypeAbb;
	}

	/**
	 * Sets the hebGuaranteeTypeAbb.
	 *
	 * @param hebGuaranteeTypeAbb the hebGuaranteeTypeAbb.
	 */
	public void setHebGuaranteeTypeAbb(String hebGuaranteeTypeAbb) {
		this.hebGuaranteeTypeAbb = hebGuaranteeTypeAbb;
	}

	/**
	 * Returns the hebGuaranteeTypeDescription.
	 *
	 * @return the hebGuaranteeTypeDescription.
	 */
	public String getHebGuaranteeTypeDescription() {
		return hebGuaranteeTypeDescription;
	}

	/**
	 * Sets the hebGuaranteeTypeDescription.
	 *
	 * @param hebGuaranteeTypeDescription the hebGuaranteeTypeDescription.
	 */
	public void setHebGuaranteeTypeDescription(String hebGuaranteeTypeDescription) {
		this.hebGuaranteeTypeDescription = hebGuaranteeTypeDescription;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "HebGuaranteeTypeCode{" +
				"hebGuaranteeTypeCode=" + hebGuaranteeTypeCode +
				", hebGuaranteeTypeAbb=" + hebGuaranteeTypeAbb +
				", hebGuaranteeTypeDescription='" + hebGuaranteeTypeDescription + '\'' +
				'}';
	}
}
