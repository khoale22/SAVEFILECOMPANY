package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the code table for applicable types.
 *
 * @author m314029
 * @since 2.19.0
 */
@Entity
@Table(name="ITM_PROD_KEY_CODE")
@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
public class ApplicableType implements Serializable {
	private static final long serialVersionUID = -3615625964369869891L;

	@Id
	@Column(name = "ITM_PROD_KEY_CD")
	@Type(type="fixedLengthCharPK")
	private String id;

	@Column(name = "ITM_PROD_KEY_DES")
	@Type(type="fixedLengthCharPK")
	private String description;

	@Column(name = "ITM_PROD_ABB")
	private String abbreviation;

	public enum Code{
		COST_LINK("CLINK"),
		DSD_ITEM("DSD"),
		WHSE_ITEM("ITMCD"),
		PRODUCT("PROD"),
		VENDOR("VEND"),
		UPC("UPC");

		String id;

		/**
		 * Called to create a value.
		 * @param id The value of the code in the table.
		 */
		Code(String id) {
			this.id = id;
		}

		/**
		 * Returns the code that represents each value (the value that will go into the database).
		 *
		 * @return The code that represents each value.
		 */
		public String getId() {
			return this.id;
		}
	}

	/**
	 * Returns Id.
	 *
	 * @return The Id.
	 **/
	public String getId() {
		return id;
	}

	/**
	 * Sets the Id.
	 *
	 * @param id The Id.
	 **/
	public ApplicableType setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Returns Description.
	 *
	 * @return The Description.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description.
	 *
	 * @param description The Description.
	 **/
	public ApplicableType setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Returns Abbreviation.
	 *
	 * @return The Abbreviation.
	 **/
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the Abbreviation.
	 *
	 * @param abbreviation The Abbreviation.
	 **/
	public ApplicableType setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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
		if (o == null || getClass() != o.getClass()) return false;

		ApplicableType that = (ApplicableType) o;

		return id != null ? id.equals(that.id) : that.id == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ApplicableType{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				'}';
	}
}
