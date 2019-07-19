/*
 *
 * NutrientPanelDetail.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents of a NutrientPanelDetail.
 *
 * @author vn70529
 * @since 2.20.0
 */
@Entity
@Table(name = "ntrn_pan_dtl")
public class NutrientPanelDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NutrientPanelDetailKey key;

	@Column(name = "NTRNT_QTY")
	private Double nutrientQuantity;

	@Column(name = "DALY_VAL_PCT")
	private Double nutrientDailyValue;

	@Column(name= "less_than_sw")
	private char lessThanSwitch;

	@ManyToOne
	@JoinColumn(name = "NTRNT_ID", referencedColumnName = "NTRNT_ID", insertable = false, updatable = false, nullable = false)
	private NLEA16Nutrient nutrient;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="NTRN_PAN_HDR_ID", referencedColumnName = "NTRN_PAN_HDR_ID", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="NTRN_PAN_COL_ID", referencedColumnName = "NTRN_PAN_COL_ID", insertable = false, updatable = false, nullable = false)
	})
	private NutrientPanelColumnHeader nutrientPanelColumnHeader;

	/**
	 * Get the key.
	 *
	 * @return the key.
	 */
	public NutrientPanelDetailKey getKey() {
		return key;
	}

	/**
	 * Set the key.
	 *
	 * @param key the key to set
	 */
	public void setKey(NutrientPanelDetailKey key) {
		this.key=key;
	}

	/**
	 * Get the nutrientQuantity.
	 *
	 * @return the nutrientQuantity
	 */
	public Double getNutrientQuantity() {
		return nutrientQuantity;
	}

	/**
	 * Set the nutrientQuantity.
	 *
	 * @param nutrientQuantity the nutrientQuantity to set
	 */
	public void setNutrientQuantity(Double nutrientQuantity) {
		this.nutrientQuantity=nutrientQuantity;
	}

	/**
	 * Get the nutrientDailyValue.
	 *
	 * @return the nutrientDailyValue.
	 */
	public Double getNutrientDailyValue() {
		return nutrientDailyValue;
	}

	/**
	 * Set the nutrientDailyValue.
	 *
	 * @param nutrientDailyValue the nutrientDailyValue to set
	 */
	public void setNutrientDailyValue(Double nutrientDailyValue) {
		this.nutrientDailyValue=nutrientDailyValue;
	}

	/**
	 * Get the nutrient.
	 *
	 * @return the nutrient.
	 */
	public NLEA16Nutrient getNutrient() {
		return nutrient;
	}

	/**
	 * Set the nutrient.
	 *
	 * @param nutrient the nutrient to set
	 */
	public void setNutrient(NLEA16Nutrient nutrient) {
		this.nutrient=nutrient;
	}

	/**
	 * Get the lessThanSwitch.
	 *
	 * @return the lessThanSwitch.
	 */
	public char getLessThanSwitch() {
		return lessThanSwitch;
	}

	/**
	 * Set the lessThanSwitch.
	 *
	 * @param lessThanSwitch the lessThanSwitch
	 */
	public void setLessThanSwitch(char lessThanSwitch) {
		this.lessThanSwitch=lessThanSwitch;
	}

	/**
	 * Get the nutrientPanelColumnHeader.
	 *
	 * @return the nutrientPanelColumnHeader.
	 */
	public NutrientPanelColumnHeader getNutrientPanelColumnHeader() {
		return nutrientPanelColumnHeader;
	}

	/**
	 * Set the nutrientPanelColumnHeader.
	 *
	 * @param nutrientPanelColumnHeader the nutrientPanelColumnHeader to set
	 */
	public void setNutrientPanelColumnHeader(NutrientPanelColumnHeader nutrientPanelColumnHeader) {
		this.nutrientPanelColumnHeader=nutrientPanelColumnHeader;
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
		if (!(o instanceof NutrientPanelDetail)) return false;

		NutrientPanelDetail that=(NutrientPanelDetail) o;

		return getKey() != null ? getKey().equals(that.getKey()) : that.getKey() == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return getKey() != null ? getKey().hashCode() : 0;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "NutrientPanelDetail{" +
				"key=" + key +
				", nutrientQuantity=" + nutrientQuantity +
				", nutrientDailyValue=" + nutrientDailyValue +
				'}';
	}
}

