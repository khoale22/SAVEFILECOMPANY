/*
 * ChoiceOptionKey
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of the choice option table.
 *
 * @author vn70516
 * @since 2.12.0
 */
@Embeddable
public class ChoiceOptionKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="chc_opt_cd")
	@Type(type="fixedLengthCharPK")
	private String choiceOptionCode;

	@Column(name="chc_typ_cd")
	@Type(type="fixedLengthCharPK")
	private String choiceTypeCode;

	/**
	 * @return Gets the value of choiceOptionCode and returns choiceOptionCode
	 */
	public void setChoiceOptionCode(String choiceOptionCode) {
		this.choiceOptionCode = choiceOptionCode;
	}

	/**
	 * Sets the choiceOptionCode
	 */
	public String getChoiceOptionCode() {
		return choiceOptionCode;
	}

	/**
	 * @return Gets the value of choiceTypeCode and returns choiceTypeCode
	 */
	public void setChoiceTypeCode(String choiceTypeCode) {
		this.choiceTypeCode = choiceTypeCode;
	}

	/**
	 * Sets the choiceTypeCode
	 */
	public String getChoiceTypeCode() {
		return choiceTypeCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ChoiceOptionKey)) return false;

		ChoiceOptionKey that = (ChoiceOptionKey) o;

		if (getChoiceOptionCode() != null ? !getChoiceOptionCode().equals(that.getChoiceOptionCode()) : that.getChoiceOptionCode() != null)
			return false;
		return getChoiceTypeCode() != null ? getChoiceTypeCode().equals(that.getChoiceTypeCode()) : that.getChoiceTypeCode() == null;
	}

	@Override
	public int hashCode() {
		int result = getChoiceOptionCode() != null ? getChoiceOptionCode().hashCode() : 0;
		result = 31 * result + (getChoiceTypeCode() != null ? getChoiceTypeCode().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ChoiceOptionKey{" +
				"choiceOptionCode='" + choiceOptionCode + '\'' +
				", choiceTypeCode='" + choiceTypeCode + '\'' +
				'}';
	}
}