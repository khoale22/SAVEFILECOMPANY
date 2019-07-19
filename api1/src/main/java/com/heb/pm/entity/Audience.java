package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an audience.
 *
 * @author s573181
 * @since 2.22.0
 */
@Entity
@Table(name = "AUDNC")
@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
public class Audience implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AUDNC_CD")
	@Type(type="fixedLengthCharPK")
	private String audienceCode;

	@Column(name = "AUDNC_DES")
	private String description;

	/**
	 * Returns the Audience Code.
	 *
	 * @return the Audience Code.
	 */
	public String getAudienceCode() {
		return audienceCode;
	}

	/**
	 * Sets the Audience Code.
	 *
	 * @param audienceCode the Audience Code.
	 * @return the updated Audience.
	 */
	public Audience setAudienceCode(String audienceCode) {
		this.audienceCode = audienceCode;
		return this;
	}

	/**
	 * Returns the Description.
	 *
	 * @return the desciption.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the description.
	 * @return the updated Audience.
	 */
	public Audience setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "Audience{" +
				"audienceCode='" + audienceCode + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a Location, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Audience audience = (Audience) o;
		return Objects.equals(audienceCode, audience.audienceCode);
	}
	/**
	 * Returns a hash code for the object. Equal objects return the same falue. Unequal objects (probably) return
	 * different values.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(audienceCode);
	}
}
