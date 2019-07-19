/*
 * ProductGroupChoiceOption
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the prod_grp_chc_opt table.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Entity
@Table(name = "prod_grp_chc_opt")
public class ProductGroupChoiceOption implements Serializable{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private ProductGroupChoiceOptionKey key;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "chc_opt_cd", referencedColumnName = "chc_opt_cd", insertable = false, updatable = false),
			@JoinColumn(name = "chc_typ_cd", referencedColumnName = "chc_typ_cd", insertable = false, updatable = false)
	})
	private ChoiceOption choiceOption;

	@JsonIgnoreProperties("productGroupChoiceOptions")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "prod_grp_typ_cd", referencedColumnName = "prod_grp_typ_cd", insertable = false, updatable = false),
			@JoinColumn(name = "chc_typ_cd", referencedColumnName = "chc_typ_cd", insertable = false, updatable = false)
	})
	private ProductGroupChoiceType productGroupChoiceType;

	@Transient
	private String action;

	/**
	 * Get the key of ProductGroupChoiceOption.
	 *
	 * @return return the key of ProductGroupChoiceType.
	 */
	public ProductGroupChoiceOptionKey getKey() {
		return key;
	}

	/**
	 * Sets the key of ProductGroupChoiceOption.
	 *
	 * @param key the key of ProductGroupChoiceOption.
	 */
	public void setKey(ProductGroupChoiceOptionKey key) {
		this.key = key;
	}

	/**
	 * Gets the choice option.
	 *
	 * @return the choice option.
	 */
	public ChoiceOption getChoiceOption() {
		return choiceOption;
	}

	/**
	 * Sets the choice option.
	 *
	 * @param choiceOption the choice option.
	 */
	public void setChoiceOption(ChoiceOption choiceOption) {
		this.choiceOption = choiceOption;
	}

	/**
	 * Gets the product group choice type.
	 *
	 * @return the product group choice type.
	 */
	public ProductGroupChoiceType getProductGroupChoiceType() {
		return productGroupChoiceType;
	}

	/**
	 * Sets the product group choice type.
	 *
	 * @param productGroupChoiceType the product group choice type.
	 */
	public void setProductGroupChoiceType(ProductGroupChoiceType productGroupChoiceType) {
		this.productGroupChoiceType = productGroupChoiceType;
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
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductGroupChoiceType{" +
				"key=" + key +
				"choiceOption=" + choiceOption +
				'}';
	}
}
