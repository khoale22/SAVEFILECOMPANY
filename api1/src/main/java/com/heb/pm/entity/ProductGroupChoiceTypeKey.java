/*
 *  ProductGroupChoiceTypeKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This is the key for the product group choice type key.
 *
 * @author l730832
 * @since 2.14.0
 */
@Embeddable
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductGroupChoiceTypeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Type(type="fixedLengthCharPK")
	@Column(name = "prod_grp_typ_cd")
	private String productGroupTypeCode;

	@Type(type="fixedLengthCharPK")
	@Column(name = "chc_typ_cd")
	private String choiceTypeCode;

	/**
	 * Returns the ProductGroupTypeCode. This represents the product group type code.
	 *
	 * @return ProductGroupTypeCode
	 */
	public String getProductGroupTypeCode() {
		return productGroupTypeCode;
	}

	/**
	 * Sets the ProductGroupTypeCode
	 *
	 * @param productGroupTypeCode The ProductGroupTypeCode
	 */
	public void setProductGroupTypeCode(String productGroupTypeCode) {
		this.productGroupTypeCode = productGroupTypeCode;
	}

	/**
	 * Returns the ChoiceTypeCode. This is the tie to the choice type code.
	 *
	 * @return ChoiceTypeCode
	 */
	public String getChoiceTypeCode() {
		return choiceTypeCode;
	}

	/**
	 * Sets the ChoiceTypeCode
	 *
	 * @param choiceTypeCode The ChoiceTypeCode
	 */
	public void setChoiceTypeCode(String choiceTypeCode) {
		this.choiceTypeCode = choiceTypeCode;
	}

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductGroupChoiceTypeKey that = (ProductGroupChoiceTypeKey) o;

		if (productGroupTypeCode != null ? !productGroupTypeCode.equals(that.productGroupTypeCode) : that.productGroupTypeCode != null)
			return false;
		return choiceTypeCode != null ? choiceTypeCode.equals(that.choiceTypeCode) : that.choiceTypeCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = productGroupTypeCode != null ? productGroupTypeCode.hashCode() : 0;
		result = PRIME_NUMBER * result + (choiceTypeCode != null ? choiceTypeCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ProductGroupChoiceTypeKey{" +
				"productGroupTypeCode='" + productGroupTypeCode + '\'' +
				", choiceTypeCode='" + choiceTypeCode + '\'' +
				'}';
	}
}
