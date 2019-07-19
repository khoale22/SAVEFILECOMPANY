/*
 * ProductBrandCostOwner
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the Product Brand Cost Owner.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Entity
@Table(name = "prod_brnd_cst_own")
public class ProductBrandCostOwner implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The constant FILED_NAME_DEFAULT_SORT.
	 */
	private static final String PRODUCT_BRAND_COST_OWNER_SORT_FIELD = "productBrand.productBrandDescription";

	@EmbeddedId
	private ProductBrandCostOwnerKey key;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_brnd_id", referencedColumnName = "prod_brnd_id", insertable = false, nullable = false, updatable = false)
	private ProductBrand productBrand;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cst_own_id", referencedColumnName = "cst_own_id", insertable = false, nullable = false, updatable = false)
	private CostOwner costOwner;

	/**
	 * Gets the product brand.
	 *
	 * @return the product brand.
	 */
	public ProductBrand getProductBrand() {
		return productBrand;
	}

	/**
	 * Sets the product brand.
	 *
	 * @param productBrand the product brand.
	 */
	public void setProductBrand(ProductBrand productBrand) {
		this.productBrand = productBrand;
	}

	/**
	 * Get the cost owner.
	 *
	 * @return the cost owner.
	 */
	public CostOwner getCostOwner() {
		return costOwner;
	}

	/**
	 * Sets the cost owner.
	 *
	 * @param costOwner the cost owner.
	 */
	public void setCostOwner(CostOwner costOwner) {
		this.costOwner = costOwner;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductBrandCostOwner{" +
				"key=" + key +
				"productBrand=" + productBrand +
				"costOwner=" + costOwner +
				'}';
	}
	/**
	 * Returns the default sort order for the product brand cost owner table.
	 *
	 * @return The default sort order for the product brand cost owner table.
	 */
	public static org.springframework.data.domain.Sort getDefaultSort() {
		return new org.springframework.data.domain.Sort(new org.springframework.data.domain.Sort.Order(org
				.springframework.data.domain.Sort.Direction.ASC, ProductBrandCostOwner.PRODUCT_BRAND_COST_OWNER_SORT_FIELD)
		);
	}
}
