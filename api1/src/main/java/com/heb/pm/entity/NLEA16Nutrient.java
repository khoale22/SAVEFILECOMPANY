/*
 *
 * NLEA16Nutrient.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a dynamic attribute of a NLEANutrient.
 *
 * @author vn70529
 * @since 2.20.0
 */
@Entity
@Table(name = "ntrnt")
public class NLEA16Nutrient implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_SORT = "nutrientDisplaySequenceNumber";

	@Id
	@Column(name = "NTRNT_ID")
	private Long nutrientId;

	@Column(name = "NTRNT_DES")
	private String nutrientDescription;

	@Column(name = "NTRNT_DSPLY_SEQ_NBR")
	private Integer nutrientDisplaySequenceNumber;

	@Column(name = "PDV_OR_WHOLE_SW")
	private String pdvOrWhole;

	@Column(name = "RCMD_DALY_VAL_QTY")
	private Double recommendedDailyAmount;

	@Column(name = "SRC_SYS_REF_ID")
	@Type(type="fixedLengthCharPK")
	private String sourceSystemReferenceId;

	@ManyToOne
	@JoinColumn(name = "UOM_ID", referencedColumnName = "UOM_ID", insertable = false, updatable = false)
	private UnitOfMeasure unitOfMeasure;

	/**
	 * Get the nutrientId.
	 *
	 * @return the nutrientId.
	 */
	public Long getNutrientId() {
		return nutrientId;
	}

	/**
	 * Set the nutrientId.
	 *
	 * @param nutrientId the nutrientId to set.
	 */
	public void setNutrientId(Long nutrientId) {
		this.nutrientId=nutrientId;
	}

	/**
	 * Get the nutrientDescription.
	 *
	 * @return the nutrientDescription.
	 */
	public String getNutrientDescription() {
		return nutrientDescription;
	}

	/**
	 * Set the nutrientDescription.
	 *
	 * @param nutrientDescription the nutrientDescription to set.
	 */
	public void setNutrientDescription(String nutrientDescription) {
		this.nutrientDescription=nutrientDescription;
	}

	/**
	 * Get the nutrientDisplaySequenceNumber.
	 *
	 * @return the nutrientDisplaySequenceNumber.
	 */
	public Integer getNutrientDisplaySequenceNumber() {
		return nutrientDisplaySequenceNumber;
	}

	/**
	 * Set the nutrientId.
	 *
	 * @param nutrientDisplaySequenceNumber the nutrientDisplaySequenceNumber to set.
	 */
	public void setNutrientDisplaySequenceNumber(Integer nutrientDisplaySequenceNumber) {
		this.nutrientDisplaySequenceNumber=nutrientDisplaySequenceNumber;
	}

	/**
	 * Get the recommendedDailyAmount.
	 *
	 * @return the recommendedDailyAmount.
	 */
	public Double getRecommendedDailyAmount() {
		return recommendedDailyAmount;
	}

	/**
	 * Set the nutrientId.
	 *
	 * @param recommendedDailyAmount the recommendedDailyAmount to set.
	 */
	public void setRecommendedDailyAmount(Double recommendedDailyAmount) {
		this.recommendedDailyAmount=recommendedDailyAmount;
	}

	public String getPdvOrWhole() {
		return pdvOrWhole;
	}

	public void setPdvOrWhole(String pdvOrWhole) {
		this.pdvOrWhole=pdvOrWhole;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure=unitOfMeasure;
	}

	/**
	 * Returns SourceSystemReferenceId.
	 *
	 * @return The SourceSystemReferenceId.
	 **/
	public String getSourceSystemReferenceId() {
		return sourceSystemReferenceId;
	}

	/**
	 * Sets the SourceSystemReferenceId.
	 *
	 * @param sourceSystemReferenceId The SourceSystemReferenceId.
	 **/
	public NLEA16Nutrient setSourceSystemReferenceId(String sourceSystemReferenceId) {
		this.sourceSystemReferenceId = sourceSystemReferenceId;
		return this;
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
		if (!(o instanceof NLEA16Nutrient)) return false;

		NLEA16Nutrient that=(NLEA16Nutrient) o;

		return getNutrientId().equals(that.getNutrientId());
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return getNutrientId().hashCode();
	}

	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(new Sort.Order(Sort.Direction.ASC, NLEA16Nutrient.DEFAULT_SORT));
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "NLEA16Nutrient{" +
				"nutrientId=" + nutrientId +
				", nutrientDescription='" + nutrientDescription + '\'' +
				", nutrientDisplaySequenceNumber=" + nutrientDisplaySequenceNumber +
				", pdvOrWhole='" + pdvOrWhole + '\'' +
				", recommendedDailyAmount=" + recommendedDailyAmount +
				", unitOfMeasure=" + unitOfMeasure +
				'}';
	}
}
