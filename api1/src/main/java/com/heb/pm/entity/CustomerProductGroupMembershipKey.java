/*
 *  CustomerProductGroupMembershipKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This is the embedded key for customer product group membership.
 *
 * @author l730832
 * @since 2.14.0
 */
@Embeddable
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CustomerProductGroupMembershipKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "cust_prod_grp_id")
	private Long customerProductGroupId;

	@Column(name = "prod_id")
	private Long prodId;

	/**
	 * Returns the CustomerProductGroupId
	 *
	 * @return CustomerProductGroupId
	 */
	public Long getCustomerProductGroupId() {
		return customerProductGroupId;
	}

	/**
	 * Sets the CustomerProductGroupId
	 *
	 * @param customerProductGroupId The CustomerProductGroupId
	 */
	public void setCustomerProductGroupId(Long customerProductGroupId) {
		this.customerProductGroupId = customerProductGroupId;
	}

	/**
	 * Returns the ProdId
	 *
	 * @return ProdId
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * Sets the ProdId
	 *
	 * @param prodId The ProdId
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
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
		if (o == null || getClass() != o.getClass()) return false;

		CustomerProductGroupMembershipKey that = (CustomerProductGroupMembershipKey) o;

		if (customerProductGroupId != null ? !customerProductGroupId.equals(that.customerProductGroupId) : that.customerProductGroupId != null)
			return false;
		return prodId != null ? prodId.equals(that.prodId) : that.prodId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = customerProductGroupId != null ? customerProductGroupId.hashCode() : 0;
		result = PRIME_NUMBER * result + (prodId != null ? prodId.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "CustomerProductGroupMembershipKey{" +
				"customerProductGroupId=" + customerProductGroupId +
				", prodId=" + prodId +
				'}';
	}
}
