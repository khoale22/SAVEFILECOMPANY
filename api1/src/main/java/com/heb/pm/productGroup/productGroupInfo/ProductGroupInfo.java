/*
 * ProductGroupInfo
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productGroup.productGroupInfo;

import com.heb.pm.entity.CustomerProductGroup;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.ProductOnline;

import java.util.List;

/**
 * object to represent data product group info screen.
 *
 * @author vn87351
 * @since 2.14.0
 */
public class ProductGroupInfo {
	private String primaryPath;
	private CustomerProductGroup customerProductGroup;
	private ProductOnline productOnline;
	private AssociatedProduct dataAssociatedProduct;
	private Boolean isRetailDrivingPicker;
	private List<GenericEntityRelationship> hierarchies;

	/**
	 * get retail driving picker
	 * @return
	 */
	public Boolean getRetailDrivingPicker() {
		return isRetailDrivingPicker;
	}

	/**
	 * set retail driving picker flag
	 * @param retailDrivingPicker
	 */
	public void setRetailDrivingPicker(Boolean retailDrivingPicker) {
		isRetailDrivingPicker = retailDrivingPicker;
	}

	/**
	 * get primary path
	 * @return path
	 */
	public String getPrimaryPath() {
		return primaryPath;
	}

	/**
	 * set primary path
	 * @param primaryPath
	 */
	public void setPrimaryPath(String primaryPath) {
		this.primaryPath = primaryPath;
	}

	/**
	 * get customer product group
	 * @return customer product group
	 */
	public CustomerProductGroup getCustomerProductGroup() {
		return customerProductGroup;
	}

	/**
	 * set customer product group
	 * @param customerProductGroup
	 */
	public void setCustomerProductGroup(CustomerProductGroup customerProductGroup) {
		this.customerProductGroup = customerProductGroup;
	}

	/**
	 * get product online
	 * @return Product Online
	 */
	public ProductOnline getProductOnline() {
		return productOnline;
	}

	/**
	 * set product online
	 * @param productOnline
	 */
	public void setProductOnline(ProductOnline productOnline) {
		this.productOnline = productOnline;
	}

	/**
	 * get data associated product table
	 * @return data associated product
	 */
	public AssociatedProduct getDataAssociatedProduct() {
		return dataAssociatedProduct;
	}

	/**
	 * set data associated product
	 * @param dataAssociatedProduct
	 */
	public void setDataAssociatedProduct(AssociatedProduct dataAssociatedProduct) {
		this.dataAssociatedProduct = dataAssociatedProduct;
	}

	/**
	 * Returns the list of Hierarchies.
	 *
	 * @return the list of hierarchies.
	 */
	public List<GenericEntityRelationship> getHierarchies() {
		return hierarchies;
	}

	/**
	 * Sets the list of hierarchies.
	 *
	 * @param hierarchies the list of hierarchies.
	 */
	public void setHierarchies(List<GenericEntityRelationship> hierarchies) {
		this.hierarchies = hierarchies;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "ProductGroupInfo{" +
				"primaryPath='" + primaryPath + '\'' +
				", customerProductGroup='" + customerProductGroup + '\'' +
				", productOnline='" + productOnline + '\'' +
				", dataAssociatedProduct=" + dataAssociatedProduct +
				'}';
	}
}
