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
 * Entity representing the language type code table.
 *
 * @author m314029
 * @version 2.14.0
 */
@Entity
@Table(name = "LANG_TYP")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class LanguageType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LANG_TYP_CD")
	@Type(type = "fixedLengthCharPK")
	private String id;

	@Column(name = "LANG_TYP_ABB")
	@Type(type = "fixedLengthChar")
	private String abbreviation;

	@Column(name = "LANG_TYP_DES")
	@Type(type = "fixedLengthChar")
	private String description;

	/**
	 * Represents the values of the known language types.
	 */
	public enum Codes {

		ENGLISH("ENG  "),
		FRENCH("FRN  "),
		GERMAN("GER  "),
		JAPAN("JPN  "),
		SPANISH("SPN  ");

		String id;

		/**
		 * Called to create a value.
		 * @param id The value of the id in the table.
		 */
		Codes(String id) {
			this.id = id;
		}

		/**
		 * Returns the id that represents each value (the value that will go into the database).
		 *
		 * @return The id that represents each value.
		 */
		public String getId() {
			return this.id;
		}
	}

	/**
	 * Returns the string identifier for the language type
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the string abbreviation for the language type
	 * @return id
	 */
	public String getAbbreviation() {
		return abbreviation;
	}


	/**
	 * Sets the abbreviation
	 * @param abbreviation the new abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns the string description for the language type
	 * @return id
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Sets the description
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
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

		LanguageType that = (LanguageType) o;

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
		return "LanguageType{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
