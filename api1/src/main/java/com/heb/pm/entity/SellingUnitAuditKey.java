/*
 *  SellingUnitAuditKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This is the embedded key for a selling unit audit.
 *
 * @author l730832
 * @since 2.12.0
 */
@Embeddable
public class SellingUnitAuditKey implements Serializable {

	@Column(name = "scn_cd_id")
	private Long upc;

	@Column(name = "aud_rec_cre8_ts")
	private LocalDateTime changedOn;

	/**
	 * Returns the Upc
	 *
	 * @return Upc
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets the Upc
	 *
	 * @param upc The Upc
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Get the timestamp for when the record was created
	 * @return the timestamp for when the record was created
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Updates the timestamp for when the record was created
	 * @param changedOn the new timestamp for when the record was created
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
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

		SellingUnitAuditKey that = (SellingUnitAuditKey) o;

		if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
		return changedOn != null ? changedOn.equals(that.changedOn) : that.changedOn == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = upc != null ? upc.hashCode() : 0;
		result = 31 * result + (changedOn != null ? changedOn.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "SellingUnitAuditKey{" +
				"upc=" + upc +
				", changedOn=" + changedOn +
				'}';
	}
}
