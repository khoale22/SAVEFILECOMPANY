/*
 *  ProductMarketingClaimKey
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents a product marketing claim key.
 *
 * @author s573181
 * @since 2.6.0
 */
@Embeddable
public class ProductMarketingClaimKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "prod_id")
	private Long prodId;

	@Column(name = "mkt_clm_cd")
	private String marketingClaimCode;

	/**
	 * Returns the product ID.
	 *
	 * @return the product ID.
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * Sets the product ID.
	 * 
	 * @param prodId the product ID.
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	/**
	 * Returns the marketing claim code.
	 *
	 * @return the marketing claim code.
	 */
	public String getMarketingClaimCode() {
		return marketingClaimCode;
	}

	/**
	 * Sets the marketing claim code.
	 * 
	 * @param marketingClaimCode  the marketing claim code.
	 */
	public void setMarketingClaimCode(String marketingClaimCode) {
		this.marketingClaimCode = marketingClaimCode;
	}

	/**
	 * Compares another object to this one. If that object is a ImportItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductMarketingClaimKey)) return false;

		ProductMarketingClaimKey that = (ProductMarketingClaimKey) o;

		if (!prodId.equals(that.prodId)) return false;
		return marketingClaimCode.equals(that.marketingClaimCode);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = prodId.hashCode();
		result = 31 * result + marketingClaimCode.hashCode();
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductMarketingClaimKey{" +
				"prodId=" + prodId +
				", marketingClaimCode='" + marketingClaimCode + '\'' +
				'}';
	}
}
