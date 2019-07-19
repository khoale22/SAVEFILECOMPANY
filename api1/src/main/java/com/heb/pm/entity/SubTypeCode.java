package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents sub-type-code, a code table to pull the Instructions on how the substitution item is to be handled.
 *
 * @author m594201
 * @since 2.0.8
 */
@Entity
@Table(name = "sub_type_cd")
public class SubTypeCode implements Serializable{

	@Id
	@Column(name = "sub_typ_cd")
	private String subTypeCode;

	@Column(name = "sub_type_abb")
	private String subTypeAbbriviation;

	@Column(name = "sub_typ_des")
	private String subTypeDescription;

	/**
	 * Gets sub type code.
	 *
	 * @return the Instructions on how the substitution item is to be handled.
	 */
	public String getSubTypeCode() {
		return subTypeCode;
	}

	/**
	 * Sets sub type code.
	 *
	 * @param subTypeCode the Instructions on how the substitution item is to be handled.
	 */
	public void setSubTypeCode(String subTypeCode) {
		this.subTypeCode = subTypeCode;
	}

	/**
	 * Gets sub type abbriviation.
	 *
	 * @return the sub type abbriviation for the subTypeCode of the the Instructions on how the substitution item is to be handled.
	 */
	public String getSubTypeAbbriviation() {
		return subTypeAbbriviation;
	}

	/**
	 * Sets sub type abbriviation.
	 *
	 * @param subTypeAbbriviation the sub type abbriviation for the Instructions on how the substitution item is to be handled.
	 */
	public void setSubTypeAbbriviation(String subTypeAbbriviation) {
		this.subTypeAbbriviation = subTypeAbbriviation;
	}

	/**
	 * Gets sub type description.
	 *
	 * @return the sub type description, which is the English friendly description of the substitution type.
	 */
	public String getSubTypeDescription() {
		return subTypeDescription;
	}

	/**
	 * Sets sub type description.
	 *
	 * @param subTypeDescription the sub type description, which is the English friendly description of the substitution type.
	 */
	public void setSubTypeDescription(String subTypeDescription) {
		this.subTypeDescription = subTypeDescription;
	}
}
