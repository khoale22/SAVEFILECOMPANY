/*
 *
 * NutrientPanelColumnHeader.java
 *
 * Copyright (c) 2018 HEB
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
import java.util.List;

/**
 * Represents of a NutrientPanelColumnHeader.
 *
 * @author vn70529
 * @since 2.21.0
 */
@Entity
@Table(name = "ntrn_pan_col_hdr")
public class NutrientPanelColumnHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int SINGLE_COLUMN_HEADER_ID = 1;
	public static final int DUAL_COLUMN_HEADER_ID = 2;

	@EmbeddedId
	private NutrientPanelColumnHeaderKey key;

	@Column(name = "CLRS_QTY")
	private Double caloriesQuantity;

	@Column(name = "SRVNG_SZ_TXT")
	private String servingSizeLabel;

	@JsonIgnoreProperties("nutrientPanelColumnHeaders")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NTRN_PAN_HDR_ID", referencedColumnName = "NTRN_PAN_HDR_ID", insertable = false, updatable = false, nullable = false)
	private NutrientStatementPanelHeader nutrientStatementPanelHeader;

	@JsonIgnoreProperties("nutrientPanelColumnHeader")
	@OneToMany(mappedBy = "nutrientPanelColumnHeader", fetch = FetchType.LAZY)
	private List<NutrientPanelDetail> nutrientPanelDetails;

	public NutrientPanelColumnHeader() {
	}

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public NutrientPanelColumnHeaderKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(NutrientPanelColumnHeaderKey key) {
		this.key=key;
	}

	/**
	 * Gets caloriesQuantity.
	 *
	 * @return the calories quantity
	 */
	public Double getCaloriesQuantity() {
		return caloriesQuantity;
	}

	/**
	 * Sets caloriesQuantity.
	 *
	 * @param caloriesQuantity the calories quantity
	 */
	public void setCaloriesQuantity(Double caloriesQuantity) {
		this.caloriesQuantity=caloriesQuantity;
	}

	/**
	 * Gets servingSizeLabel.
	 *
	 * @return the serving size label
	 */
	public String getServingSizeLabel() {
		return servingSizeLabel;
	}

	/**
	 * Sets servingSizeLabel.
	 *
	 * @param servingSizeLabel the serving size label
	 */
	public void setServingSizeLabel(String servingSizeLabel) {
		this.servingSizeLabel=servingSizeLabel;
	}

	/**
	 * Gets nutrientStatementPanelHeader.
	 *
	 * @return the nutrient statement panel header
	 */
	public NutrientStatementPanelHeader getNutrientStatementPanelHeader() {
		return nutrientStatementPanelHeader;
	}

	/**
	 * Sets nutrientStatementPanelHeader.
	 *
	 * @param nutrientStatementPanelHeader the nutrient statement panel header
	 */
	public void setNutrientStatementPanelHeader(NutrientStatementPanelHeader nutrientStatementPanelHeader) {
		this.nutrientStatementPanelHeader=nutrientStatementPanelHeader;
	}

	/**
	 * Gets list of nutrientPanelDetails.
	 *
	 * @return the list of nutrient panel details
	 */
	public List<NutrientPanelDetail> getNutrientPanelDetails() {
		return nutrientPanelDetails;
	}

	/**
	 * Sets nutrientPanelDetails.
	 *
	 * @param nutrientPanelDetails the nutrient panel details
	 */
	public void setNutrientPanelDetails(List<NutrientPanelDetail> nutrientPanelDetails) {
		this.nutrientPanelDetails=nutrientPanelDetails;
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
		if (!(o instanceof NutrientPanelColumnHeader)) return false;

		NutrientPanelColumnHeader that=(NutrientPanelColumnHeader) o;

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
		return "NutrientPanelColumnHeader{" +
				"key=" + key.toString() +
				", caloriesQuantity=" + caloriesQuantity +
				", servingSizeLabel='" + servingSizeLabel +
				'}';
	}
}


