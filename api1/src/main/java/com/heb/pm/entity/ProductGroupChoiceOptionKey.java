/*
 * ProductGroupChoiceOptionKey
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of the prod_grp_chc_opt table.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Embeddable
@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductGroupChoiceOptionKey implements Serializable{

	private static final long serialVersionUID = 1L;
	@Column(name = "prod_grp_typ_cd")
	private String productGroupTypeCode;
	@Column(name="chc_typ_cd")
	@Type(type="fixedLengthCharPK")
	private String choiceTypeCode;
	@Column(name="chc_opt_cd")
	@Type(type="fixedLengthCharPK")
	private String choiceOptionCode;
	/**
	 * Gets product group type code.
	 *
	 * @return code the code ot product group type.
	 */
	public String getProductGroupTypeCode() {
		return productGroupTypeCode;
	}
	/**
	 * Sets product group type code.
	 *
	 * @param productGroupTypeCode the code of product type code.
	 */
	public void setProductGroupTypeCode(String productGroupTypeCode) {
		this.productGroupTypeCode = productGroupTypeCode;
	}

	/**
	 * Sets the choice type code.
	 *
	 * @param choiceTypeCode the code of choice type.
	 */
	public void setChoiceTypeCode(String choiceTypeCode) {
		this.choiceTypeCode = choiceTypeCode;
	}

	/**
	 * Gets the choice option code.
	 *
	 * @return the code of choice option.
	 */
	public String getChoiceOptionCode() {
		return choiceOptionCode;
	}

	/**
	 * Sets the choice option code.
	 *
	 * @param choiceOptionCode the code of choice option.
	 */
	public void setChoiceOptionCode(String choiceOptionCode) {
		this.choiceOptionCode = choiceOptionCode;
	}

	/**
	 * Gets the choice type code.
	 *
	 * @return the code of choice type.
	 */
	public String getChoiceTypeCode() {
		return choiceTypeCode;
	}

	@Override
	public String toString() {
		return "ProductGroupChoiceTypeKey{" +
				"productGroupTypeCode=" + this.productGroupTypeCode +
				", choiceTypeCode=" + this.choiceTypeCode +
				", choiceOptionCode=" + this.choiceOptionCode +
				'}';
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductGroupChoiceOptionKey)) return false;

		ProductGroupChoiceOptionKey that = (ProductGroupChoiceOptionKey) o;

		if (productGroupTypeCode != null ? !productGroupTypeCode.equals(that.productGroupTypeCode) : that.productGroupTypeCode != null)
			return false;
		if (choiceTypeCode != null ? !choiceTypeCode.equals(that.choiceTypeCode) : that.choiceTypeCode != null)
			return false;
		return choiceOptionCode != null ? choiceOptionCode.equals(that.choiceOptionCode) : that.choiceOptionCode == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = productGroupTypeCode != null ? productGroupTypeCode.hashCode() : 0;
		result = 31 * result + (choiceTypeCode != null ? choiceTypeCode.hashCode() : 0);
		result = 31 * result + (choiceOptionCode != null ? choiceOptionCode.hashCode() : 0);
		return result;
	}
}