package com.heb.pm.entity;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Represents a dynamic attribute of a NutrientStatementDetailsKey.
 *
 * @author m594201
 * @since 2.1.0
 */
public class NutrientStatementDetailsKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "pd_ntrnt_stmt_no")
	private long nutrientStatementNumber;

	@Column(name = "pd_lbl_ntrnt_cd")
	private long nutrientLabelCode;

	/**
	 * Gets nutrient statement number.
	 *
	 * @return the nutrient statement number
	 */
	public long getNutrientStatementNumber() {
		return nutrientStatementNumber;
	}

	/**
	 * Sets nutrient statement number.
	 *
	 * @param nutrientStatementNumber the nutrient statement number
	 */
	public void setNutrientStatementNumber(long nutrientStatementNumber) {
		this.nutrientStatementNumber = nutrientStatementNumber;
	}

	/**
	 * Gets nutrient label code.
	 *
	 * @return the nutrient label code
	 */
	public long getNutrientLabelCode() {
		return nutrientLabelCode;
	}

	/**
	 * Sets nutrient label code.
	 *
	 * @param nutrientLabelCode the nutrient label code
	 */
	public void setNutrientLabelCode(long nutrientLabelCode) {
		this.nutrientLabelCode = nutrientLabelCode;
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

		NutrientStatementDetailsKey that = (NutrientStatementDetailsKey) o;

		if (nutrientStatementNumber != that.nutrientStatementNumber) return false;
		return nutrientLabelCode == that.nutrientLabelCode;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = (int) (nutrientStatementNumber ^ (nutrientStatementNumber >>> NutrientStatementDetailsKey.FOUR_BYTES));
		result = NutrientStatementDetailsKey.PRIME_NUMBER * result +
				(int) (nutrientLabelCode ^ (nutrientLabelCode >>> NutrientStatementDetailsKey.FOUR_BYTES));
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "NutrientStatementDetailsKey{" +
				"nutrientStatementNumber=" + nutrientStatementNumber +
				", nutrientLabelCode=" + nutrientLabelCode +
				'}';
	}
}
