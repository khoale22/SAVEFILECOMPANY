/*
 *
 * ProductItemVariant.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents Product Item Variant Data from prod_item_var table.
 *
 * @author vn87351
 * @since 2.16.0
 */
@Entity
@Table(name = "prod_itm_var")
public class ProductItemVariant  implements Serializable {
	@EmbeddedId
	private ProductItemVariantKey key;

	@Column(name = "retl_pack_qty")
	private int retailPackQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
	private ProductMaster productMaster;

	@Transient
	private ItemMaster parentItemMaster;

	@Transient
	private boolean isDelete;

	/**
	 * get parent item master
	 * @return
	 */
	public ItemMaster getParentItemMaster() {
		return parentItemMaster;
	}

	/**
	 * set parent item master
	 * @param parentItemMaster
	 */
	public void setParentItemMaster(ItemMaster parentItemMaster) {
		this.parentItemMaster = parentItemMaster;
	}

	/**
	 * get delete flag
	 * @return
	 */
	public boolean getDelete() {
		return isDelete;
	}

	/**
	 * set delete flag
	 * @param delete
	 */
	public void setDelete(boolean delete) {
		isDelete = delete;
	}

	/**
	 * get product master
	 * @return
	 */
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	/**
	 * set product master
	 * @param productMaster
	 */
	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	/**
	 * get retail pack quantity
	 * @return
	 */
	public int getRetailPackQuantity() {
		return retailPackQuantity;
	}

	/**
	 * set retail pack quantity
	 * @param retailPackQuantity
	 */
	public void setRetailPackQuantity(int retailPackQuantity) {
		this.retailPackQuantity = retailPackQuantity;
	}

	/**
	 * get key
	 * @return
	 */
	public ProductItemVariantKey getKey() {
		return key;
	}

	/**
	 * set key
	 * @param key
	 */
	public void setKey(ProductItemVariantKey key) {
		this.key = key;
	}
	/**
	 * Compares another object to this one. If that object is an ProductItemVariant, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductItemVariant)) return false;

		ProductItemVariant itemKey = (ProductItemVariant) o;

		return (key == itemKey.key);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		return result;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductItemVariant{" +
				"key='" + key + '\'' +
				", product Master=" + productMaster +
				'}';
	}

}
