package com.heb.pm.productDetails.product.taxonomy;

import java.io.Serializable;
import java.util.List;

public class ProductAttributeHeader implements Serializable {

	private static final long serialVersionUID = -1679674882145613172L;

	private Long productId;
	private String productType;
	private List<ProductAttribute> productAttributes;

	public ProductAttributeHeader() {
	}

	public ProductAttributeHeader(Long productId, String productType, List<ProductAttribute> productAttributes) {
		this.productId = productId;
		this.productType = productType;
		this.productAttributes = productAttributes;
	}

	/**
	 * Returns ProductId.
	 *
	 * @return The ProductId.
	 **/
	public Long getProductId() {
		return productId;
	}

	/**
	 * Returns ProductType.
	 *
	 * @return The ProductType.
	 **/
	public String getProductType() {
		return productType;
	}

	/**
	 * Returns ProductAttributes.
	 *
	 * @return The ProductAttributes.
	 **/
	public List<ProductAttribute> getProductAttributes() {
		return productAttributes;
	}

	/**
	 * Sets the ProductId.
	 *
	 * @param productId The ProductId.
	 **/
	public ProductAttributeHeader setProductId(Long productId) {
		this.productId = productId;
		return this;
	}

	/**
	 * Sets the ProductType.
	 *
	 * @param productType The ProductType.
	 **/
	public ProductAttributeHeader setProductType(String productType) {
		this.productType = productType;
		return this;
	}

	/**
	 * Sets the ProductAttributes.
	 *
	 * @param productAttributes The ProductAttributes.
	 **/
	public ProductAttributeHeader setProductAttributes(List<ProductAttribute> productAttributes) {
		this.productAttributes = productAttributes;
		return this;
	}
}
