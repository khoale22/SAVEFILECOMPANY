/*
 *  CustomerProductChoiceKey
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
 * This represents the key for the customer product choice entity.
 *
 * @author l730832
 * @since 2.14.0
 */
@Embeddable
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CustomerProductChoiceKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "prod_id")
	private Long productId;

	@Column(name = "prod_chc_seq_nbr")
	private Long productChoiceSequenceNumber;

	@Type(type="fixedLengthCharPK")
	@Column(name = "prod_grp_typ_cd")
	private String productGroupTypeCode;

	@Type(type="fixedLengthCharPK")
	@Column(name = "chc_opt_cd")
	private String choiceOptionCode;

	@Type(type="fixedLengthCharPK")
	@Column(name = "chc_typ_cd")
	private String choiceTypeCode;


	/**
	 * Returns the ProductId. The product id for the product this represents.
	 *
	 * @return ProductId
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Sets the ProductId
	 *
	 * @param productId The ProductId
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Returns the ProductChoiceSequenceNumber. This is the sequence number for each product choice.
	 *
	 * @return ProductChoiceSequenceNumber
	 */
	public Long getProductChoiceSequenceNumber() {
		return productChoiceSequenceNumber;
	}

	/**
	 * Sets the ProductChoiceSequenceNumber
	 *
	 * @param productChoiceSequenceNumber The ProductChoiceSequenceNumber
	 */
	public void setProductChoiceSequenceNumber(Long productChoiceSequenceNumber) {
		this.productChoiceSequenceNumber = productChoiceSequenceNumber;
	}

	/**
	 * Returns the ProductGroupTypeCode. Represents the type of a product group name. I.g. "Custom cut" for a Mesquite
	 * Smoke Ham.
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
	 * Returns the ChoiceOptionCode. The choice option represents an option of some type. I.g. Shaved, Very Thin,
	 * Sandwich Slice, 1 lb. Package
	 *
	 * @return ChoiceOptionCode
	 */
	public String getChoiceOptionCode() {
		return choiceOptionCode;
	}

	/**
	 * Sets the ChoiceOptionCode
	 *
	 * @param choiceOptionCode The ChoiceOptionCode
	 */
	public void setChoiceOptionCode(String choiceOptionCode) {
		this.choiceOptionCode = choiceOptionCode;
	}

	/**
	 * Returns the ChoiceTypeCode. The choice type code represents the type for the choice option.
	 * I.g. Package Weight, Thickness.
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

		CustomerProductChoiceKey that = (CustomerProductChoiceKey) o;

		if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
		if (productChoiceSequenceNumber != null ? !productChoiceSequenceNumber.equals(that.productChoiceSequenceNumber) : that.productChoiceSequenceNumber != null)
			return false;
		if (productGroupTypeCode != null ? !productGroupTypeCode.equals(that.productGroupTypeCode) : that.productGroupTypeCode != null)
			return false;
		if (choiceOptionCode != null ? !choiceOptionCode.equals(that.choiceOptionCode) : that.choiceOptionCode != null)
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
		int result = productId != null ? productId.hashCode() : 0;
		result = PRIME_NUMBER * result + (productChoiceSequenceNumber != null ? productChoiceSequenceNumber.hashCode() : 0);
		result = PRIME_NUMBER * result + (productGroupTypeCode != null ? productGroupTypeCode.hashCode() : 0);
		result = PRIME_NUMBER * result + (choiceOptionCode != null ? choiceOptionCode.hashCode() : 0);
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
		return "CustomerProductChoiceKey{" +
				"productId=" + productId +
				", productChoiceSequenceNumber=" + productChoiceSequenceNumber +
				", productGroupTypeCode='" + productGroupTypeCode + '\'' +
				", choiceOptionCode='" + choiceOptionCode + '\'' +
				", choiceTypeCode='" + choiceTypeCode + '\'' +
				'}';
	}
}
