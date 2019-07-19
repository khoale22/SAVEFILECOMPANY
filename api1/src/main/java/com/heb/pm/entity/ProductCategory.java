/*
 *  ProductCategory
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Product Category.
 * @author vn70529
 * @since 2.12.0
 */
@Entity
@Table(name = "prod_cat")
public class ProductCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s - %d";

	@Id
	@Column(name = "prod_cat_id")
	private Integer productCategoryId;

	@Column(name = "prod_cat_abb")
	private String productCategoryAbb;

	@Column(name = "prod_cat_nm")
	private String productCategoryName;

	@Column(name = "mkt_consm_evnt_cd")
	private String marketConsumerEventCode;

	@Column(name = "prod_cat_role_cd")
	private String productCategoryRoleCode;

	@JsonIgnoreProperties("prod_cat")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name="mkt_consm_evnt_cd", referencedColumnName = "mkt_consm_evnt_cd", insertable = false, updatable = false, nullable = false)
	})
	private MarketConsumerEventType marketConsumerEventType;

	@JsonIgnoreProperties("prod_cat")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name="prod_cat_role_cd", referencedColumnName = "prod_cat_role_cd", insertable = false, updatable = false, nullable = false)
	})
	private ProductCategoryRole productCategoryRole;

	/**
	 * get Product Category Id
	 *
	 * @return Product Category Id
	 */
	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	/**
	 * Sets the Product Category Id
	 *
	 * @param productCategoryId The Product Category Id
	 **/
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	/**
	 * get Product Category Abbreviation
	 *
	 * @return Product Category Abbreviation
	 */
	public String getProductCategoryAbb() {
		return productCategoryAbb;
	}

	/**
	 * Sets the Product Category Abbreviation
	 *
	 * @param productCategoryAbb The Product Category Abbreviation
	 **/
	public void setProductCategoryAbb(String productCategoryAbb) {
		this.productCategoryAbb = productCategoryAbb;
	}

	/**
	 * get Product Category Name
	 *
	 * @return Product Category Name
	 */
	public String getProductCategoryName() {
		return productCategoryName;
	}

	/**
	 * Sets the Product Category Name
	 *
	 * @param productCategoryName The Product Category Name
	 **/
	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public String getProductCategorySummary(){
		return String.format(ProductCategory.DISPLAY_NAME_FORMAT, this.getProductCategoryName().trim(),
				this.getProductCategoryId());
	}

	/**
	 * get Market Consumer Event Code
	 *
	 * @return Market Consumer Event Code
	 */
	public String getMarketConsumerEventCode() {
		return marketConsumerEventCode;
	}

	/**
	 * Sets the Market Consumer Event Code
	 *
	 * @param marketConsumerEventCode Market Consumer Event Code
	 **/
	public void setMarketConsumerEventCode(String marketConsumerEventCode) {
		this.marketConsumerEventCode = marketConsumerEventCode;
	}

	/**
	 * get Product Category Role Code
	 *
	 * @return Product Category Role Code
	 */
	public String getProductCategoryRoleCode() {
		return productCategoryRoleCode;
	}

	/**
	 * Sets the Product Category Role Code
	 *
	 * @param productCategoryRoleCode Product Category Role Code
	 **/
	public void setProductCategoryRoleCode(String productCategoryRoleCode) {
		this.productCategoryRoleCode = productCategoryRoleCode;
	}

	/**
	 * get Market Consumer Event Type
	 *
	 * @return Market Consumer Event Type
	 */
	public MarketConsumerEventType getMarketConsumerEventType() {
		return marketConsumerEventType;
	}

	/**
	 * get Product Category Role
	 *
	 * @return Product Category Role
	 */
	public ProductCategoryRole getProductCategoryRole() {
		return productCategoryRole;
	}

	/**
	 * Compares another object to this one. If that object is a ProductCategory, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductCategory)) return false;

		ProductCategory productCategory = (ProductCategory) o;

		return this.getProductCategoryId().equals(productCategory.getProductCategoryId());
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return this.getProductCategoryId().hashCode();
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductCategory{" +
				"productCategoryId=" + this.getProductCategoryId() +
				", productCategoryAbb='" + this.getProductCategoryAbb() + '\'' +
				", productCategoryName='" + this.getProductCategoryName() + '\'' +
				", marketConsumerEventCode='" + this.getMarketConsumerEventCode() + '\'' +
				", productCategoryRoleCode='" + this.productCategoryRoleCode + '\'' +
				'}';
	}
}
