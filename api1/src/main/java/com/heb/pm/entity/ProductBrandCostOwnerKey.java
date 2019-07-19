/*
 * ProductBrandCostOwnerKey
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key for the ProductBrandCostOwner.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Embeddable
public class ProductBrandCostOwnerKey implements Serializable {

    private static final long serialVersionUID = 1L;

	@Column(name = "prod_brnd_id")
	private Long productBrandId;

	@Column(name = "cst_own_id")
	private Integer costOwnerId;

	/**
	 * Gets the product brand id.
	 *
	 * @return the product brand id.
	 */
	public Long getProductBrandId() {
		return productBrandId;
	}

	/**
	 * Sets the product brand id.
	 *
	 * @param productBrandId the product brand id.
	 */
	public void setProductBrandId(Long productBrandId) {
		this.productBrandId = productBrandId;
	}

	/**
	 * Gets the cost owner id.
	 *
	 * @return the cost owner id.
	 */
	public Integer getCostOwnerId() {
		return costOwnerId;
	}

	/**
	 * Sets the cost owner id.
	 *
	 * @param costOwnerId the cost owner id.
	 */
	public void setCostOwnerId(Integer costOwnerId) {
		this.costOwnerId = costOwnerId;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
    @Override
    public String toString() {
        return "ProductBrandCostOwnerKey{" +
                "productBrandId='" + productBrandId + '\'' +
                ", costOwnerId=" + costOwnerId +
                '}';
    }

	/**
	 * Compares another object to this one for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductBrandCostOwnerKey)) return false;

		ProductBrandCostOwnerKey that = (ProductBrandCostOwnerKey) o;

		if (productBrandId != null ? !productBrandId.equals(that.productBrandId) : that.productBrandId != null)
			return false;
		return costOwnerId != null ? costOwnerId.equals(that.costOwnerId) : that.costOwnerId == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = productBrandId != null ? productBrandId.hashCode() : 0;
		result = 31 * result + (costOwnerId != null ? costOwnerId.hashCode() : 0);
		return result;
	}
}
