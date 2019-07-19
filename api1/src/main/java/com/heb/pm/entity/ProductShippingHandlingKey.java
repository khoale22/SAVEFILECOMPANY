package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * An embeddable key for a product shipping handling.
 *
 * @author m594201
 * @since 2.7.0
 */
@Embeddable
public class ProductShippingHandlingKey implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "prod_id")
	private Integer prodId;

	@Column(name = "prod_shp_hndlg_cd")
	private String prodShippingHandlingCode;

	/**
	 * Gets prod id.
	 *
	 * @return the prod id, attached to a product.
	 */
	public Integer getProdId() {
		return prodId;
	}

	/**
	 * Sets prod id.
	 *
	 * @param prodId the prod id, attached to a product.
	 */
	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	/**
	 * Gets prod shipping handling code.
	 *
	 * @return the product shipping handling code, that is used to look up the special handling information.
	 */
	public String getProdShippingHandlingCode() {
		return prodShippingHandlingCode;
	}

	/**
	 * Sets prod shipping handling code.
	 *
	 * @param prodShippingHandlingCode the prod shipping handling code, that is used to look up the special handling information.
	 */
	public void setProdShippingHandlingCode(String prodShippingHandlingCode) {
		this.prodShippingHandlingCode = prodShippingHandlingCode;
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
		if (!(o instanceof ProductShippingHandlingKey)) return false;

		ProductShippingHandlingKey that = (ProductShippingHandlingKey) o;

		if (prodId != null ? !prodId.equals(that.prodId) : that.prodId != null) return false;
		return prodShippingHandlingCode != null ? prodShippingHandlingCode.equals(that.prodShippingHandlingCode) : that.prodShippingHandlingCode == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = prodId != null ? prodId.hashCode() : 0;
		result = 31 * result + (prodShippingHandlingCode != null ? prodShippingHandlingCode.hashCode() : 0);
		return result;
	}
}
