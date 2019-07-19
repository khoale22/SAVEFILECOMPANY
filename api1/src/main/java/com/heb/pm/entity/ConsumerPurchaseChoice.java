package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is a code table for the Sold By options
 *@author s753601
 * @version 2.15.0
 */
@Entity
@Table(name = "CONSM_PRCH_CHC")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ConsumerPurchaseChoice {

	@Id
	@Column(name = "CONSM_PRCH_CHC_CD")
	@Type(type = "fixedLengthCharPK")
	private String id;

	@Column(name = "CONSM_PRCH_CHC_ABB")
	@Type(type = "fixedLengthChar")
	private String abbreviation;

	@Column(name = "CONSM_PRCH_CHC_DES")
	@Type(type = "fixedLengthChar")
	private String description;


	/**
	 * Unique identifier for the object
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * updates the id
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ConsumerPurchaseChoice that = (ConsumerPurchaseChoice) o;

		if (this.id != null ? !this.id.equals(that.id) : that.id != null) return false;

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
		int result = id != null ? id.hashCode() : 0;
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ConsumerPurchaseChoice{" +
				"id='" + id + "\'" +
				", abbreviation='" + abbreviation + "\'" +
				", description='" + description + "\'" +
				'}';
	}
}
