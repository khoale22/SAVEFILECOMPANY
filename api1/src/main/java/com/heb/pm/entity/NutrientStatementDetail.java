/*
 *
 * NutrientStatementDetail.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a dynamic attribute of a NutrientStatementDetail.
 *
 * @author m594201
 * @since 2.1.0
 */
@Entity
@Table(name = "pd_ntrnt_stmt_dtl")
public class NutrientStatementDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NutrientStatementDetailsKey key;

	@Column(name = "pd_lbl_fed_req_qty")
	private double nutrientStatementQuantity;

	@Column(name = "pd_ntrnt_pdv_qty")
	private long nutrientDailyValue;

	@Column(name = "pd_pdd_req_qty")
	private double nutrientPDDQuantity;

	@Transient
	private boolean nutrientRoundingRequired;

	@Transient
	private boolean inAlternateEditMode;

	@JsonIgnoreProperties("nutrientStatementDetails")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_lbl_ntrnt_cd", referencedColumnName = "pd_lbl_ntrnt_cd", insertable = false, updatable = false, nullable = false)
	private Nutrient nutrient;

	@JsonIgnoreProperties("nutrientStatementDetailList")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pd_ntrnt_stmt_no", referencedColumnName = "pd_ntrnt_stmt_no", insertable = false, updatable = false, nullable = false)
	private NutrientStatementHeader nutrientStatementHeader;

	/**
	 * Gets Nutrient Statement Header.
	 *
	 * @return nutrientStatementHeader
	 */
	public NutrientStatementHeader getNutrientStatementHeader() {
		return nutrientStatementHeader;
	}

	/**
	 * Sets the nutrient statement header.
	 *
	 * @param nutrientStatementHeader The nutrientstatement header to set.
	 */
	public void setNutrientStatementHeader(NutrientStatementHeader nutrientStatementHeader) {
		this.nutrientStatementHeader = nutrientStatementHeader;
	}
	/**
	 * Gets nutrient pdd quantity.
	 *
	 * @return the nutrient pdd quantity
	 */
	public double getNutrientPDDQuantity() {
		return nutrientPDDQuantity;
	}

	/**
	 * Sets nutrient pdd quantity.
	 *
	 * @param nutrientPDDQuantity the nutrient pdd quantity
	 */
	public void setNutrientPDDQuantity(double nutrientPDDQuantity) {
		this.nutrientPDDQuantity = nutrientPDDQuantity;
	}

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public NutrientStatementDetailsKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(NutrientStatementDetailsKey key) {
		this.key = key;
	}

	/**
	 * Gets nutrient.
	 *
	 * @return the nutrient
	 */
	public Nutrient getNutrient() {
		return nutrient;
	}

	/**
	 * Sets nutrient.
	 *
	 * @param nutrient the nutrient
	 */
	public void setNutrient(Nutrient nutrient) {
		this.nutrient = nutrient;
	}

	/**
	 * Gets nutrient statement quantity.
	 *
	 * @return the nutrient statement quantity
	 */
	public double getNutrientStatementQuantity() {
		return nutrientStatementQuantity;
	}

	/**
	 * Sets nutrient statement quantity.
	 *
	 * @param nutrientStatementQuantity the nutrient statement quantity
	 */
	public void setNutrientStatementQuantity(double nutrientStatementQuantity) {
		this.nutrientStatementQuantity = nutrientStatementQuantity;
	}

	/**
	 * Gets nutrient daily value.
	 *
	 * @return the nutrient daily value
	 */
	public long getNutrientDailyValue() {
		return nutrientDailyValue;
	}

	/**
	 * Sets nutrient daily value.
	 *
	 * @param nutrientDailyValue the nutrient daily value
	 */
	public void setNutrientDailyValue(long nutrientDailyValue) {
		this.nutrientDailyValue = nutrientDailyValue;
	}

	/**
	 * Returns whether or not nutrient rounding is required.
	 *
	 * @return the nutrient rounding required value
	 */
	public boolean isNutrientRoundingRequired() {
		return nutrientRoundingRequired;
	}

	/**
	 * Sets whether or not nutrient rounding is required.
	 *
	 * @param nutrientRoundingRequired the nutrient rounding required value
	 */
	public void setNutrientRoundingRequired(boolean nutrientRoundingRequired) {
		this.nutrientRoundingRequired = nutrientRoundingRequired;
	}

	/**
	 * Returns whether or not the use has entered and alternate edit mode for the quantities. For example,
	 * if the nutrient should be edited by measure, they've chosen to edit by percent and vice-versa.
	 *
	 * @return True if the nutrient is in alternate edit mode and false otherwise.
	 */
	public boolean isInAlternateEditMode() {
		return inAlternateEditMode;
	}

	/**
	 * Sets whether or not the use has entered and alternate edit mode for the quantities.
	 *
	 * @param inAlternateEditMode True if the nutrient is in alternate edit mode and false otherwise.
	 */
	public void setInAlternateEditMode(boolean inAlternateEditMode) {
		this.inAlternateEditMode = inAlternateEditMode;
	}

	/**
	 * Compares another object to this one. If that object is a NutrientStatementDetail, it uses the keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NutrientStatementDetail that = (NutrientStatementDetail) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
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
		return "NutrientStatementDetail{" +
				"key=" + key +
				", nutrientStatementQuantity=" + nutrientStatementQuantity +
				", nutrientDailyValue=" + nutrientDailyValue +
				", nutrientPDDQuantity=" + nutrientPDDQuantity +
				", nutrientRoundingRequired=" + nutrientRoundingRequired +
				'}';
	}
}
