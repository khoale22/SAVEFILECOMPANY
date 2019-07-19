/*
 *
 * ProductItemVariantKey.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents Product Item Variant key from prod_item_var table.
 *
 * @author vn87351
 * @since 2.16.0
 */
public class ProductItemVariantKey implements Serializable{
	@Column(name = "itm_key_typ_cd")
	private String itemKeyTypeCode;
	@Column(name = "itm_id")
	private Long itemId;
	@Column(name = "prod_id")
	private Long productId;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	/**
	 * get item key type code
	 * @return
	 */
	public String getItemKeyTypeCode() {
		return itemKeyTypeCode;
	}

	/**
	 * set item key type code
	 * @param itemKeyTypeCode
	 */
	public void setItemKeyTypeCode(String itemKeyTypeCode) {
		this.itemKeyTypeCode = itemKeyTypeCode;
	}

	/**
	 * get item id
	 * @return
	 */
	public Long getItemId() {
		return itemId;
	}

	/**
	 * set item id
	 * @param itemId
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/**
	 * get product id
	 * @return
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * set product id
	 * @param productId
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * Compares another object to this one. If that object is an ProductItemVariantKey, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductItemVariantKey)) return false;

		ProductItemVariantKey itemKey = (ProductItemVariantKey) o;

		return (productId == itemKey.productId && itemId == itemKey.itemId && itemKeyTypeCode.equalsIgnoreCase(itemKey.getItemKeyTypeCode()));
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = productId != null ? productId.hashCode() : 0;
		result = PRIME_NUMBER * result + (int) (itemId ^ (itemId >>> FOUR_BYTES));
		result = PRIME_NUMBER * result + (int) (itemKeyTypeCode != null ? itemKeyTypeCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductItemVariantKey{" +
				"product Id='" + productId + '\'' +
				", Item id=" + itemId +
				", Item Code=" + itemKeyTypeCode +
				'}';
	}

}
