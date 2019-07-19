/*
 * ShipperAudit
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Shipper Audit Key for shipper audit.
 *
 * @author l730832
 * @since  2.6.0
 */
@Embeddable
public class ShipperAuditKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "pd_upc_no")
	private Long upc;

	@Column(name = "pd_shpr_upc_no")
	private Long modifiedUpc;

	@Column(name = "aud_rec_cre8_ts")
	private LocalDateTime changedOn;

	/**
	 * Returns the Upc. The upc that is tied to the shipper level. The upc attaining to the whole 'mrt' or 'alt pack'.
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
	 * Returns the modifiedUpc. The modified Upc is the upc that the actions have taken place on. If the quantity is
	 * updated then the the modified upc is the upc thats correlated to the quantity that has been modified. The modified
	 * upc is a upc that is inside of the 'mrt' or 'alt pack' of the pd_upc_no.
	 *
	 * @return modifiedUpc
	 */
	public Long getModifiedUpc() {
		return modifiedUpc;
	}

	/**
	 * Sets the modifiedUpc.
	 *
	 * @param modifiedUpc The modifiedUpc
	 */
	public void setModifiedUpc(Long modifiedUpc) {
		this.modifiedUpc = modifiedUpc;
	}

	/**
	 * Returns the changed on. The changed on is the timestamp in which the transaction occurred.
	 *
	 * @return Timestamp
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Sets the Timestamp
	 *
	 * @param timestamp The Timestamp
	 */
	public void setChangedOn(LocalDateTime timestamp) {
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

		ShipperAuditKey that = (ShipperAuditKey) o;

		if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
		if (modifiedUpc != null ? !modifiedUpc.equals(that.modifiedUpc) : that.modifiedUpc != null) return false;
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
		result = 31 * result + (modifiedUpc != null ? modifiedUpc.hashCode() : 0);
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
		return "ShipperAuditKey{" +
				"upc=" + upc +
				", modifiedUpc=" + modifiedUpc +
				", changedOn=" + changedOn +
				'}';
	}
}
