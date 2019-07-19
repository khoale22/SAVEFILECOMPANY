package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

/**
 * An embeddable key for a Product Shipping Exception.
 *
 * @author m594201
 * @since 2.14.0
 */
@Embeddable
public class ProductShippingExceptionKey implements Serializable {

	@Column(name ="prod_id")
	private Long prodId;

	@Column(name = "cust_shpng_meth_cd")
	private long customerShippingMethodCode;

	/**
	 * Gets prod id.
	 *
	 * @return the prod id
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * Sets prod id.
	 *
	 * @param prodId the prod id
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	/**
	 * Gets customer shipping method code.
	 *
	 * @return the customer shipping method code
	 */
	public long getCustomerShippingMethodCode() {
		return customerShippingMethodCode;
	}

	/**
	 * Sets customer shipping method code.
	 *
	 * @param customerShippingMethodCode the customer shipping method code
	 */
	public void setCustomerShippingMethodCode(long customerShippingMethodCode) {
		this.customerShippingMethodCode = customerShippingMethodCode;
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
		if (!(o instanceof ProductShippingExceptionKey)) return false;

		ProductShippingExceptionKey that = (ProductShippingExceptionKey) o;

		if (customerShippingMethodCode != that.customerShippingMethodCode) return false;
		return prodId != null ? prodId.equals(that.prodId) : that.prodId == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = prodId != null ? prodId.hashCode() : 0;
		result = 31 * result + (int) (customerShippingMethodCode ^ (customerShippingMethodCode >>> 32));
		return result;
	}
}
