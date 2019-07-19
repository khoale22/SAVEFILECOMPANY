/*
 * ProductInfo.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
/**
 * Product object. Contains product level information.
 *
 * @author s573181
 * @since 2.23.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productDescription;

	/**
	 * Returns the product description.
	 *
	 * @return the product description.
	 */
	public String getProductDescription() {
		return productDescription;
	}

	/**
	 * Sets the product description.
	 *
	 * @param productDescription the product description.
	 * @return the updated product info.
	 */
	public Product setProductDescription(String productDescription) {
		this.productDescription = productDescription;
		return this;
	}
}
