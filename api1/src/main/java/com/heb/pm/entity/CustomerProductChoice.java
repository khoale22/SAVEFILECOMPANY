/*
 *  CustomerProductChoice
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This represents a customer product choice entity.
 *
 * @author l730832
 * @since 2.14.0
 */
@Entity
@Table(name = "CUST_PROD_CHOICE")
public class CustomerProductChoice implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@EmbeddedId
	private CustomerProductChoiceKey key;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "chc_opt_cd", referencedColumnName = "chc_opt_cd", insertable = false, updatable = false)
	private ChoiceOption choiceOption;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "prod_grp_typ_cd", referencedColumnName = "prod_grp_typ_cd", insertable = false, updatable = false),
			@JoinColumn(name = "chc_typ_cd", referencedColumnName = "chc_typ_cd", insertable = false, updatable = false)
	})
	private ProductGroupChoiceType productGroupChoiceType;

	@Transient
	private String action;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public CustomerProductChoiceKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(CustomerProductChoiceKey key) {
		this.key = key;
	}

	/**
	 * Returns the ChoiceOption
	 *
	 * @return ChoiceOption
	 */
	public ChoiceOption getChoiceOption() {
		return choiceOption;
	}

	/**
	 * Sets the ChoiceOption
	 *
	 * @param choiceOption The ChoiceOption
	 */
	public void setChoiceOption(ChoiceOption choiceOption) {
		this.choiceOption = choiceOption;
	}

	/**
	 * Returns the ProductGroupChoiceType
	 *
	 * @return ProductGroupChoiceType
	 */
	public ProductGroupChoiceType getProductGroupChoiceType() {
		return productGroupChoiceType;
	}

	/**
	 * Sets the ProductGroupChoiceType
	 *
	 * @param productGroupChoiceType The ProductGroupChoiceType
	 */
	public void setProductGroupChoiceType(ProductGroupChoiceType productGroupChoiceType) {
		this.productGroupChoiceType = productGroupChoiceType;
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

		CustomerProductChoice that = (CustomerProductChoice) o;

		if (key != null ? !key.equals(that.key) : that.key != null) return false;
		return choiceOption != null ? choiceOption.equals(that.choiceOption) : that.choiceOption == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = PRIME_NUMBER * result + (choiceOption != null ? choiceOption.hashCode() : 0);
		return result;
	}

	/**
	 * Get the action of ProductGroupChoiceOption.
	 *
	 * @return return the action add/update/delete ProductGroupChoiceOption.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action for ProductGroupChoiceOption.
	 *
	 * @param action the action of ProductGroupChoiceOption.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "CustomerProductChoice{" +
				"key=" + key +
				", choiceOption=" + choiceOption +
				'}';
	}
}
