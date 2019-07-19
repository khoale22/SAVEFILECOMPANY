/*
 *
 * NutrientPanelDetailKey.java
 *
 * Copyright (c) 2017 HEB
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
 * Represents a dynamic attribute of a NutrientPanelDetailKey.
 *
 * @author vn70529
 * @since 2.20.0
 */
public class NutrientPanelDetailKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ntrn_pan_hdr_id")
	private Long nutrientStatementId;

	@Column(name = "ntrn_pan_col_id")
	private Long nutrientPanelColumnId;

	@Column(name = "ntrnt_id")
	private Long nutrientId;

	public NutrientPanelDetailKey() {
	}

	/**
	 * Get the nutrientStatementId.
	 *
	 * @return the nutrientStatementId
	 */
	public Long getNutrientStatementId() {
		return nutrientStatementId;
	}

	/**
	 * Set the nutrientStatementId.
	 *
	 * @param nutrientStatementId the nutrientStatementId to set
	 */
	public void setNutrientStatementId(Long nutrientStatementId) {
		this.nutrientStatementId=nutrientStatementId;
	}

	/**
	 * Get the nutrientPanelColumnId.
	 *
	 * @return the nutrientPanelColumnId
	 */
	public Long getNutrientPanelColumnId() {
		return nutrientPanelColumnId;
	}

	/**
	 * Set the nutrientPanelColumnId.
	 *
	 * @param nutrientPanelColumnId the nutrientPanelColumnId to set
	 */
	public void setNutrientPanelColumnId(Long nutrientPanelColumnId) {
		this.nutrientPanelColumnId=nutrientPanelColumnId;
	}

	/**
	 * Get the nutrientId.
	 *
	 * @return the nutrientId.
	 */
	public Long getNutrientId() {
		return nutrientId;
	}

	/**
	 * Set the upc.
	 *
	 * @param nutrientId the nutrientId to set
	 */
	public void setNutrientId(Long nutrientId) {
		this.nutrientId=nutrientId;
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
		if (!(o instanceof NutrientPanelDetailKey)) return false;

		NutrientPanelDetailKey that=(NutrientPanelDetailKey) o;

		if (getNutrientStatementId() != null ? !getNutrientStatementId().equals(that.getNutrientStatementId()) : that.getNutrientStatementId() != null)
			return false;
		if (getNutrientPanelColumnId() != null ? !getNutrientPanelColumnId().equals(that.getNutrientPanelColumnId()) : that.getNutrientPanelColumnId() != null)
			return false;
		return getNutrientId() != null ? getNutrientId().equals(that.getNutrientId()) : that.getNutrientId() == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result=getNutrientStatementId() != null ? getNutrientStatementId().hashCode() : 0;
		result=31 * result + (getNutrientPanelColumnId() != null ? getNutrientPanelColumnId().hashCode() : 0);
		result=31 * result + (getNutrientId() != null ? getNutrientId().hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "NutrientPanelDetailKey{" +
				"nutrientStatementId=" + nutrientStatementId +
				", nutrientId=" + nutrientId +
				'}';
	}
}
