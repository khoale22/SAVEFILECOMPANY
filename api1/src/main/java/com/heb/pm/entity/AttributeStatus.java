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
 * Represents an attribute status.
 *
 * @author s573181
 * @since 2.22.0
 */
@Entity
@Table(name = "ATTR_ST")
@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
public class AttributeStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ATTR_ST_CD")
	@Type(type="fixedLengthCharPK")
	private String attributeStatusCode;

	@Column(name = "ATTR_ST_DES")
	private String description;

	/**
	 * Returns the attributeStatusCode.
	 *
	 * @return the attributeStatusCode.
	 */
	public String getAttributeStatusCode() {
		return attributeStatusCode;
	}

	/**
	 * Sets the attributeStatusCode.
	 * @param attributeStatusCode the attributeStatusCode.
	 *
	 * @return the updated AttributeStatus.
	 */
	public AttributeStatus setAttributeStatusCode(String attributeStatusCode) {
		this.attributeStatusCode = attributeStatusCode;
		return this;
	}

	/**
	 * Returns the description.
	 *
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the description.
	 * @return the updated AttributeStatus.
	 */
	public AttributeStatus setDescription(String description) {
		this.description = description;
		return this;
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
		AttributeStatus that = (AttributeStatus) o;
		return Objects.equals(attributeStatusCode, that.attributeStatusCode);
	}

	/**
	 * Returns a hash code for the object. Equal objects return the same falue. Unequal objects (probably) return
	 * different values.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {

		return Objects.hash(attributeStatusCode);
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "AttributeStatus{" +
				"attributeStatusCode='" + attributeStatusCode + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
