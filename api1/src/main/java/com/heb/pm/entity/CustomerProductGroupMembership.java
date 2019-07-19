/*
 *  CustomerProductGroupMembership
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * This represents the CustomerProductGroupMembership.
 *
 * @author l730832
 * @since 2.14.0
 */
@Entity
@Table(name = "cust_prod_grp_mems")
public class CustomerProductGroupMembership implements Serializable {

	@EmbeddedId
	private CustomerProductGroupMembershipKey key;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cust_prod_grp_id", referencedColumnName = "cust_prod_grp_id", updatable = false, insertable = false)
	private CustomerProductGroup customerProductGroup;

	/**
	 * Returns the CustomerProductGroup
	 *
	 * @return CustomerProductGroup
	 */
	public CustomerProductGroup getCustomerProductGroup() {
		return customerProductGroup;
	}

	/**
	 * Sets the CustomerProductGroup
	 *
	 * @param customerProductGroup The CustomerProductGroup
	 */
	public void setCustomerProductGroup(CustomerProductGroup customerProductGroup) {
		this.customerProductGroup = customerProductGroup;
	}

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public CustomerProductGroupMembershipKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(CustomerProductGroupMembershipKey key) {
		this.key = key;
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

		CustomerProductGroupMembership that = (CustomerProductGroupMembership) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "CustomerProductGroupMembership{" +
				"key=" + key +
				'}';
	}
}
