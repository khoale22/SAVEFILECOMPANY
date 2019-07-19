/*
 * ProductGroupChoiceType
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * This represents the product group choice type entity.
 *
 * @author l730832
 * @since 2.14.0
 */
@Entity
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
@Table(name = "PROD_GRP_CHC_TYP")
public class ProductGroupChoiceType implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductGroupChoiceTypeKey key;

	@Type(type="fixedLengthCharPK")
	@Column(name = "prod_grp_typ_cd", insertable = false, updatable = false)
	private String productGroupTypeCode;

	@Type(type="fixedLengthCharPK")
	@Column(name = "chc_typ_cd", insertable = false, updatable = false)
	private String choiceTypeCode;
	@Column(name = "pcker_sw")
	private Boolean pickerSwitch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="chc_typ_cd", referencedColumnName = "chc_typ_cd", insertable = false, updatable = false)
	private ChoiceType choiceType;

	@JsonIgnoreProperties("productGroupChoiceTypes")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_grp_typ_cd", referencedColumnName = "prod_grp_typ_cd", insertable = false, nullable = false, updatable = false)
	private ProductGroupType productGroupType;

	@JsonIgnoreProperties("productGroupChoiceType")
	@OneToMany(mappedBy = "productGroupChoiceType",fetch = FetchType.LAZY)
	private List<ProductGroupChoiceOption> productGroupChoiceOptions;

	@Transient
	private String action;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public ProductGroupChoiceTypeKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(ProductGroupChoiceTypeKey key) {
		this.key = key;
	}

	/**
	 * Returns the PickerSwitch
	 *
	 * @return PickerSwitch
	 */
	public Boolean getPickerSwitch() {
		return pickerSwitch;
	}

	/**
	 * Sets the PickerSwitch
	 *
	 * @param pickerSwitch The PickerSwitch
	 */
	public void setPickerSwitch(Boolean pickerSwitch) {
		this.pickerSwitch = pickerSwitch;
	}
	/**
	 * Sets the choiceType
	 *
	 * @return the choice type.
	 */
	public ChoiceType getChoiceType() {
		return this.choiceType;
	}

	/**
	 * Sets the choice type.
	 *
	 * @param choiceType the choice type.
	 */
	public void setChoiceType(ChoiceType choiceType) {
		this.choiceType = choiceType;
	}

	/**
	 * Sets the choiceTypeCode
	 *
	 * @return the choice type code.
	 */
	public String getChoiceTypeCode() {
		return this.choiceTypeCode;
	}

	/**
	 * Sets the choice type code.
	 *
	 * @param choiceTypeCode the choice type code.
	 */
	public void setChoiceTypeCode(String choiceTypeCode) {
		this.choiceTypeCode = choiceTypeCode;
	}

	/**
	 * Gets the product group type.
	 *
	 * @return product group type.
	 */
	public ProductGroupType getProductGroupType() {
		return productGroupType;
	}

	/**
	 * Sets the product group type.
	 *
	 * @param productGroupType the product group type.
	 */
	public void setProductGroupType(ProductGroupType productGroupType) {
		this.productGroupType = productGroupType;
	}

	/**
	 * Gets the list of product groupt choice options.
	 *
	 * @return the list of product group choice options.
	 */
	public List<ProductGroupChoiceOption> getProductGroupChoiceOptions() {
		return productGroupChoiceOptions;
	}

	/**
	 * Sets the list of product group choice options.
	 *
	 * @param productGroupChoiceOptions the list of product group choice option.
	 */
	public void setProductGroupChoiceOptions(List<ProductGroupChoiceOption> productGroupChoiceOptions) {
		this.productGroupChoiceOptions = productGroupChoiceOptions;
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
	 * Sets the action for ProductGroupChoiceType.
	 *
	 * @param action the action of ProductGroupChoiceType.
	 */
	public void setAction(String action) {
		this.action = action;
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

		ProductGroupChoiceType that = (ProductGroupChoiceType) o;

		return (key != null ? key.equals(that.key) : that.key == null);
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductGroupChoiceType{" +
				"key=" + key +
				", pipickerSwitchckerSw='" + pickerSwitch + '\'' +
				", choiceType='" + choiceType + '\'' +
				'}';
	}
}
