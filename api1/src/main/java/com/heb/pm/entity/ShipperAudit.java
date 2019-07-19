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

import com.heb.util.audit.Audit;
import com.heb.util.audit.AuditableField;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Shipper Audit. Shipper audit holds information on transactions that have been
 * performed on a pd_shipper.
 *
 * @author l730832
 * @since 2.6.0
 */
@Entity
@Table(name="pd_shipper_aud")
public class ShipperAudit implements Serializable, Audit {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ShipperAuditKey key;

	@Column(name = "act_cd")
	private String action;

	@Column(name = "lst_updt_uid")
	private String changedBy;

	@AuditableField(displayName = "Quantity")
	@Column(name = "pd_shpr_qty")
	private Long shipperQuantity;

	@Column(name = "pd_shpr_typ_cd")
	private char shipperTypeCode;

	/**
	 * Returns the Key. The keys for the shipper audit.
	 *
	 * @return Key
	 */
	public ShipperAuditKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(ShipperAuditKey key) {
		this.key = key;
	}

	/**
	 * Returns the ActionCode. The action code pertains to the action of the audit. 'UPDAT', 'PURGE', or 'ADD'.
	 *
	 * @return ActionCode
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the ActionCode
	 *
	 * @param action The ActionCode
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Returns the ShipperTypeCode. This code tells whether the shipper is an MRT or not.
	 *
	 * @return ShipperTypeCode
	 */
	public char getShipperTypeCode() {
		return shipperTypeCode;
	}

	/**
	 * Sets the ShipperTypeCode
	 *
	 * @param shipperTypeCode The ShipperTypeCode
	 */
	public void setShipperTypeCode(char shipperTypeCode) {
		this.shipperTypeCode = shipperTypeCode;
	}

	/**
	 * Returns the ShipperQuantity. The quantity of the current audit transaction.
	 *
	 * @return ShipperQuantity
	 */
	public Long getShipperQuantity() {
		return shipperQuantity;
	}

	/**
	 * Sets the ShipperQuantity
	 *
	 * @param shipperQuantity The ShipperQuantity
	 */
	public void setShipperQuantity(Long shipperQuantity) {
		this.shipperQuantity = shipperQuantity;
	}


	/**
	 * Returns changed by. The changed by shows who was doing the action that is being audited. This is the uid(login)
	 * that a user has.
	 *
	 * @return changedBy
	 */
	@Override
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Sets the changedBy
	 *
	 * @param changedBy The changedBy
	 */
	@Override
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Gets the changed on time. This is when the modification was done.
	 *
	 * @return The time the modification was done.
	 */
	@Override
	public LocalDateTime getChangedOn() {
		return this.key.getChangedOn();
	}

	/**
	 * Sets the changed on time.
	 *
	 * @param changedOn The time the modification was done.
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.key.setChangedOn(changedOn);
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ShipperAudit that = (ShipperAudit) o;

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
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ShipperAudit{" +
				"key=" + key +
				", action='" + action + '\'' +
				", changedBy='" + changedBy + '\'' +
				", shipperQuantity=" + shipperQuantity +
				", shipperTypeCode=" + shipperTypeCode +
				'}';
	}
}
