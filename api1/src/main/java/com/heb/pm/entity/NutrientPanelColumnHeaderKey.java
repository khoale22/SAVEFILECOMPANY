/*
 *
 * NutrientPanelColumnHeaderKey.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Represents a dynamic attribute of a NutrientPanelColumnHeaderKey.
 *
 * @author vn70529
 * @since 2.21.0
 */
public class NutrientPanelColumnHeaderKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ntrn_pan_hdr_id")
	private Long nutrientPanelHeaderId;

	@Column(name = "ntrn_pan_col_id")
	private Long nutrientPanelColumnId;

	public NutrientPanelColumnHeaderKey() {
	}

	/**
	 * Gets NutrientPanelHeaderId.
	 *
	 * @return the nutrient panel header id
	 */
	public Long getNutrientPanelHeaderId() {
		return nutrientPanelHeaderId;
	}

	/**
	 * Sets nutrientPanelHeaderId.
	 *
	 * @param nutrientPanelHeaderId the nutrient panel header id
	 */
	public void setNutrientPanelHeaderId(Long nutrientPanelHeaderId) {
		this.nutrientPanelHeaderId=nutrientPanelHeaderId;
	}

	/**
	 * Gets nutrientPanelColumnId.
	 *
	 * @return the nutrient panel column id
	 */
	public Long getNutrientPanelColumnId() {
		return nutrientPanelColumnId;
	}

	/**
	 * Sets nutrientPanelColumnId
	 *
	 * @param nutrientPanelColumnId the nutrient panel column id
	 */
	public void setNutrientPanelColumnId(Long nutrientPanelColumnId) {
		this.nutrientPanelColumnId=nutrientPanelColumnId;
	}

	/**
	 * Compares this object to another for equality. Equality is based on ingredient statement number.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NutrientPanelColumnHeaderKey)) return false;

		NutrientPanelColumnHeaderKey that=(NutrientPanelColumnHeaderKey) o;

		if (!getNutrientPanelHeaderId().equals(that.getNutrientPanelHeaderId())) return false;
		return getNutrientPanelColumnId().equals(that.getNutrientPanelColumnId());
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result=getNutrientPanelHeaderId().hashCode();
		result=31 * result + getNutrientPanelColumnId().hashCode();
		return result;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "NutrientPanelColumnHeaderKey{" +
				"nutrientPanelHeaderId=" + nutrientPanelHeaderId +
				", nutrientPanelColumnId=" + nutrientPanelColumnId +
				'}';
	}


}
