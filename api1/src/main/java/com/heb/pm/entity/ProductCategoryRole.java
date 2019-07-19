/*
 *  ProductCategoryRole
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a Product Category Role.
 * @author vn70529
 * @since 2.12.0
 */
@Entity
@Table(name = "prod_cat_role")
public class ProductCategoryRole implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * format to show summary detail product category role
	 */
	private static final String PRODUCT_CATEGORY_ROLE_SUMMARY_FORMAT="%s [%s]";

	@Id
	@Column(name = "prod_cat_role_cd")
	private String productCategoryRoleCode;

	@Column(name = "prod_cat_role_des")
	private String productCategoryRoleDescription;

	@JsonIgnoreProperties("productCategoryRole")
	@OneToMany(mappedBy = "productCategoryRole", fetch = FetchType.LAZY)
	private List<ProductCategory> productCategory;

	/**
	 * get Product Category Role Code
	 *
	 * @return Product Category Role Code
	 */
	public String getProductCategoryRoleCode() {
		return productCategoryRoleCode;
	}

	/**
	 * Set the Product Category Role Code
	 *
	 * @param productCategoryRoleCode The Product Role Code
	 **/
	public void setProductCategoryRoleCode(String productCategoryRoleCode) {
		this.productCategoryRoleCode = productCategoryRoleCode;
	}

	/**
	 * get Product Category Role Description
	 *
	 * @return Product Category Role Description
	 */
	public String getProductCategoryRoleDescription() {
		return productCategoryRoleDescription;
	}

	/**
	 * Set the Product Category Role Description
	 *
	 * @param productCategoryRoleDescription The Product Role Description
	 **/
	public void setProductCategoryRoleDescription(String productCategoryRoleDescription) {
		this.productCategoryRoleDescription = productCategoryRoleDescription;
	}

	/**
	 * get summary of field Product Category Role to show ui
	 *
	 * @return summary of field Product Category Role
	 */
	public String getProductCategoryRoleSummary(){
		return String.format(PRODUCT_CATEGORY_ROLE_SUMMARY_FORMAT, StringUtils.trimToEmpty(this.getProductCategoryRoleDescription()), StringUtils.trimToEmpty(this.getProductCategoryRoleCode()));
	}

	/**
	 * Compares another object to this one. If that object is a ProductCategoryRole, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (!(o instanceof ProductCategoryRole)) return false;

		ProductCategoryRole productCategoryRole = (ProductCategoryRole) o;

		return this.getProductCategoryRoleCode().equals(productCategoryRole.getProductCategoryRoleCode());
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return this.getProductCategoryRoleCode().hashCode();
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductCategoryRole{" +
				"productCategoryRoleCode=" + this.getProductCategoryRoleCode() +
				", productCategoryRoleDescription='" + this.getProductCategoryRoleDescription() + '\'' +
				'}';
	}
}
