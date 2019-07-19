/*
 *  ProductScanCodeNutrient
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a product scan code extent data that is extended attributes for UPCs.
 *
 * @author vn70516
 * @since 2.14.0
 */
@Entity
@Table(name = "prod_scn_ntrntl")
public class ProductScanCodeNutrient implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductScanCodeNutrientKey key;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_ntrntl_cd", referencedColumnName = "prod_ntrntl_cd", insertable = false, updatable = false, nullable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private ProductNutrientAttribute productNutrientAttribute;

	/**
	 * @return Gets the value of key and returns key
	 */
	public void setKey(ProductScanCodeNutrientKey key) {
		this.key = key;
	}

	/**
	 * Sets the key
	 */
	public ProductScanCodeNutrientKey getKey() {
		return key;
	}

	/**
	 * @return Gets the value of productNutrientAttribute and returns productNutrientAttribute
	 */
	public void setProductNutrientAttribute(ProductNutrientAttribute productNutrientAttribute) {
		this.productNutrientAttribute = productNutrientAttribute;
	}

	/**
	 * Sets the productNutrientAttribute
	 */
	public ProductNutrientAttribute getProductNutrientAttribute() {
		return productNutrientAttribute;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductScanCodeNutrient)) return false;

		ProductScanCodeNutrient that = (ProductScanCodeNutrient) o;

		if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null) return false;
		return getProductNutrientAttribute() != null ? getProductNutrientAttribute().equals(that.getProductNutrientAttribute()) : that.getProductNutrientAttribute() == null;
	}

	@Override
	public int hashCode() {
		int result = getKey() != null ? getKey().hashCode() : 0;
		result = 31 * result + (getProductNutrientAttribute() != null ? getProductNutrientAttribute().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProductScanCodeNutrient{" +
				"key=" + key +
				", productNutrientAttribute=" + productNutrientAttribute +
				'}';
	}
}