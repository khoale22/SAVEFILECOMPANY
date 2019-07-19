/*
 *  ProductNutrientAttribute
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a table product nutrient attribute
 *
 * @author vn70516
 * @since 2.14.0
 */
@Entity
@Table(name = "prod_ntrntl_attr")
public class ProductNutrientAttribute implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROD_NTRNTL_CD")
	@Type(type = "fixedLengthCharPK")
	private String productNutrientCode;

	@Column(name = "PROD_NTRNTL_ABB")
	private String abbreviation;

	@Column(name = "PROD_NTRNTL_DES")
	private String description;

	@Column(name = "PRTY_NBR")
	private Long priority;

	@Column(name = "PROD_NTRNTL_LG_DES")
	private String longDescription;

	/**
	 * @return Gets the value of productNutrientCode and returns productNutrientCode
	 */
	public void setProductNutrientCode(String productNutrientCode) {
		this.productNutrientCode = productNutrientCode;
	}

	/**
	 * Sets the productNutrientCode
	 */
	public String getProductNutrientCode() {
		return productNutrientCode;
	}

	/**
	 * @return Gets the value of abbreviation and returns abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Sets the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * @return Gets the value of description and returns description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Gets the value of priority and returns priority
	 */
	public void setPriority(Long priority) {
		this.priority = priority;
	}

	/**
	 * Sets the priority
	 */
	public Long getPriority() {
		return priority;
	}

	/**
	 * @return Gets the value of longDescription and returns longDescription
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * Sets the longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductNutrientAttribute)) return false;

		ProductNutrientAttribute that = (ProductNutrientAttribute) o;

		if (getProductNutrientCode() != null ? !getProductNutrientCode().equals(that.getProductNutrientCode()) : that.getProductNutrientCode() != null)
			return false;
		if (getAbbreviation() != null ? !getAbbreviation().equals(that.getAbbreviation()) : that.getAbbreviation() != null)
			return false;
		if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
			return false;
		if (getPriority() != null ? !getPriority().equals(that.getPriority()) : that.getPriority() != null)
			return false;
		return getLongDescription() != null ? getLongDescription().equals(that.getLongDescription()) : that.getLongDescription() == null;
	}

	@Override
	public int hashCode() {
		int result = getProductNutrientCode() != null ? getProductNutrientCode().hashCode() : 0;
		result = 31 * result + (getAbbreviation() != null ? getAbbreviation().hashCode() : 0);
		result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
		result = 31 * result + (getPriority() != null ? getPriority().hashCode() : 0);
		result = 31 * result + (getLongDescription() != null ? getLongDescription().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProductNutrientAttribute{" +
				"productNutrientCode='" + productNutrientCode + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				", priority=" + priority +
				", longDescription='" + longDescription + '\'' +
				'}';
	}
}