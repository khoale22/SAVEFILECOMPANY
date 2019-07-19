/*
 * UpcLink.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents upc link info.
 *
 * @author s573181
 * @since 2.23.0
 */
@Entity
@Table(name = "pd_upc_link")
public class RetailLink implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PD_UPC_NO")
	private Long upc;

	@Column(name = "PD_LINK_CD")
	private Long retailLinkCd;

	@Column(name = "PD_SEQUENCE_NO")
	private Integer sequenceNumber;

	@Column(name = "PD_CENTS_OFF_AMT")
	private double centsOffAmount;



	/**
	 * Returns the upc.
	 *
	 * @return the upc.
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets the upc.
	 *
	 * @param upc the upc.
	 * @return the updated RetailLink.
	 */
	public RetailLink setUpc(Long upc) {
		this.upc = upc;
		return this;
	}

	/**
	 * Returns the retail link.
	 *
	 * @return the retail link.
	 */
	public Long getRetailLinkCd() {
		return retailLinkCd;
	}

	/**
	 * Sets the retail link cd.
	 *
	 * @param retailLinkCd the retail link cd.
	 * @return the updated retail link cd.
	 */
	public RetailLink setRetailLinkCd(Long retailLinkCd) {
		this.retailLinkCd = retailLinkCd;
		return this;
	}

	/**
	 * Returns the sequence number.
	 *
	 * @return the sequence number.
	 */
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Sets the sequence number.
	 *
	 * @param sequenceNumber the sequence number.
	 * @return the updated retail link.
	 */
	public RetailLink setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
		return this;
	}

	/**
	 * Returns the cents off amount.
	 *
	 * @return the cents off amount.
	 */
	public double getCentsOffAmount() {
		return centsOffAmount;
	}

	/**
	 * Sets the cents off amount.
	 *
	 * @param centsOffAmount the cents off amount.
	 * @return the updated retail link.
	 */
	public RetailLink setCentsOffAmount(double centsOffAmount) {
		this.centsOffAmount = centsOffAmount;
		return this;
	}
	/**
	 * Compares another object to this one. If that object is an RetailLink, it uses the keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RetailLink that = (RetailLink) o;
		return Objects.equals(upc, that.upc);
	}
	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {

		return Objects.hash(upc);
	}
	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "RetailLink{" +
				"upc=" + upc +
				", retailLinkCd=" + retailLinkCd +
				", sequenceNumber=" + sequenceNumber +
				", centsOffAmount=" + centsOffAmount +
				'}';
	}
}
