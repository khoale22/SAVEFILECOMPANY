/*
 * com.heb.pm.entity.ProductDiscontinueKey
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.domain.Sort;

/**
 * The prod_del table has a composite key. This class represents that key.
 *
 * @author d11677
 * @since 2.0.0
 */
@Embeddable
public class ProductDiscontinueKey implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final String DSD = "DSD  ";
	public static final String WAREHOUSE = "ITMCD";
	
	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	private static final String UPC_SORT_FIELD = "key.upc";
	private static final String PRODUCT_ID_SORT_FIELD = "key.productId";
	private static final String ITEM_CODE_SORT_FIELD = "key.itemCode";
	private static final String ITEM_TYPE_SORT_FIELD = "key.itemType";

	@Column(name="scn_cd_id")
	private long upc;

	@Column(name="prod_id")
	private long productId;

	@Column(name="itm_id")
	private long itemCode;

	@Column(name="itm_key_typ_cd", length = 5)
	private String itemType;

	/**
	 * Returns the UPC for the ProductDiscontinue object.
	 *
	 * @return The UPC for the ProductDiscontinue object.
	 */
	public long getUpc() {
		return upc;
	}

	/**
	 * Sets the UPC for the ProductDiscontinue object.
	 *
	 * @param upc The UPC for the ProductDiscontinue object.
	 */
	public void setUpc(long upc) {
		this.upc = upc;
	}

	/**
	 * Returns the product ID for the ProductDiscontinue object.
	 *
	 * @return The product ID for the ProductDiscontinue object.
	 */
	public long getProductId() {
		return productId;
	}

	/**
	 * Sets the product ID for the ProductDiscontinue object.
	 *
	 * @param productId The product ID for the ProductDiscontinue object.
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}

	/**
	 * Returns the item code for the ProductDiscontinue object.
	 *
	 * @return The item code for the ProductDiscontinue object.
	 */
	public long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the item code for the ProductDiscontinue object.
	 *
	 * @param itemCode The item code for the ProductDiscontinue object.
	 */
	public void setItemCode(long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the item type for the ProductDiscontinue object.
	 *
	 * @return The item type for the ProductDiscontinue object.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Returns the item type for the ProductDiscontinue object.
	 *
	 * @param itemType The item type for the ProductDiscontinue object.
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductDiscontinueKey{" +
				"upc=" + this.upc +
				", productId=" + this.productId +
				", itemCode=" + this.itemCode +
				", itemType='" + this.itemType + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductDiscontinueKey)) return false;

		ProductDiscontinueKey that = (ProductDiscontinueKey) o;

		if (this.itemCode != that.itemCode) return false;
		if (this.productId != that.productId) return false;
		if (this.upc != that.upc) return false;
		if (!this.itemType.equals(that.itemType)) return false;

		return true;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they
	 * are not, they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = (int) (upc ^ (upc >>> ProductDiscontinueKey.FOUR_BYTES));
		result =  ProductDiscontinueKey.PRIME_NUMBER * result + (int) (productId ^ (productId >>> ProductDiscontinueKey.FOUR_BYTES));
		result = ProductDiscontinueKey.PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> ProductDiscontinueKey.FOUR_BYTES));
		result = ProductDiscontinueKey.PRIME_NUMBER * result + itemType.hashCode();
		return result;
	}

	/**
	 * Returns the default sort order for the prod_del table.
	 *
	 * @return The default sort order for the prod_del table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, ProductDiscontinueKey.UPC_SORT_FIELD),
				new Sort.Order(Sort.Direction.ASC, ProductDiscontinueKey.PRODUCT_ID_SORT_FIELD),
				new Sort.Order(Sort.Direction.ASC, ProductDiscontinueKey.ITEM_CODE_SORT_FIELD),
				new Sort.Order(Sort.Direction.ASC, ProductDiscontinueKey.ITEM_TYPE_SORT_FIELD)
		);
	}

	/**
	 * Returns a sort order of UPC, product ID, item code, item type.
	 *
	 * @param direction The direction the sort should be in.
	 * @return A sort sort order of UPC, product ID, item code, item type.
	 */
	public static Sort getSortByUpc(Sort.Direction direction) {
		if (direction.equals(Sort.Direction.ASC)) {
			return ProductDiscontinueKey.getDefaultSort();
		} else {
			return new Sort(
					new Sort.Order(direction, ProductDiscontinueKey.UPC_SORT_FIELD),
					new Sort.Order(direction, ProductDiscontinueKey.PRODUCT_ID_SORT_FIELD),
					new Sort.Order(direction, ProductDiscontinueKey.ITEM_CODE_SORT_FIELD),
					new Sort.Order(direction, ProductDiscontinueKey.ITEM_TYPE_SORT_FIELD)
			);
		}
	}

	/**
	 * Returns a sort order of item code, UPC, product ID, item type.
	 *
	 * @param direction The direction the sort should be in.
	 * @return A sort order of item code, UPC, product ID, item type.
	 */
	public static Sort getSortByItemCode(Sort.Direction direction) {
		return new Sort(
				new Sort.Order(direction, ProductDiscontinueKey.ITEM_CODE_SORT_FIELD),
				new Sort.Order(direction, ProductDiscontinueKey.UPC_SORT_FIELD),
				new Sort.Order(direction, ProductDiscontinueKey.PRODUCT_ID_SORT_FIELD),
				new Sort.Order(direction, ProductDiscontinueKey.ITEM_TYPE_SORT_FIELD)
		);
	}
}
