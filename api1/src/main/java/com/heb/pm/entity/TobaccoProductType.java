package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity holds type information for a tobacco product
 * @author s753601
 * @version 2.13.0
 */
@Entity
@Table(name = "TBCO_PROD_TYP")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class TobaccoProductType {

	@Id
	@Column(name = "TBCO_PROD_TYP_CD")
	@Type(type = "fixedLengthCharPK")
	private String tobaccoProductTypeCode;

	@Column(name="TBCO_PROD_ABB")
	@Type(type = "fixedLengthChar")
	private String abbreviation;

	@Column(name = "TBCO_PROD_DES")
	@Type(type = "fixedLengthChar")
	private String description;

	@Column(name = "TBCO_TAX_RATE_PCT")
	private Double taxRate;

	/**
	 * Unique identifier for the object
	 * @return
	 */
	public String getTobaccoProductTypeCode() {
		return tobaccoProductTypeCode;
	}

	/**
	 * updates the id
	 * @param tobaccoProductTypeCode the new id
	 */
	public void setTobaccoProductTypeCode(String tobaccoProductTypeCode) {
		this.tobaccoProductTypeCode = tobaccoProductTypeCode;
	}

	/**
	 * Gets the abbreviation for the object
	 * @return
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Updates the abbreviation
	 * @param abbreviation the new abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Gets the description for the object
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Updates the description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the tax rate of the tobacco product type.
	 *
	 * @return taxRate.
	 */
	public Double getTaxRate() {
		return taxRate;
	}

	/**
	 * Updates the taxRate
	 *
	 * @param taxRate the new taxRate of the tobacco product type.
	 */
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
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

		TobaccoProductType that = (TobaccoProductType) o;

		if (this.tobaccoProductTypeCode != null ? !this.tobaccoProductTypeCode.equals(that.tobaccoProductTypeCode) : that.tobaccoProductTypeCode != null) return false;

		return true;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = tobaccoProductTypeCode != null ? tobaccoProductTypeCode.hashCode() : 0;
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "TobaccoProductType{" +
				"tobaccoProductTypeCode='" + tobaccoProductTypeCode + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
