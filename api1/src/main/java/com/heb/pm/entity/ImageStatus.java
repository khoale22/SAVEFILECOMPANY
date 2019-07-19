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
 * Entity representing the image status code table
 * @author s753601
 * @version 2.13.0
 */
@Entity
@Table(name = "IMG_STAT")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ImageStatus implements Serializable{

	public enum ImageStatusCode {
		APPROVED("A"),
		REJECTED("R"),
		FORREVIEW("S");

		private String name;
		ImageStatusCode(String name) {
			this.name = name;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
	}

	@Id
	@Column(name = "IMG_STAT_CD")
	@Type(type = "fixedLengthCharPK")
	private String id;

	@Column(name = "IMG_STAT_ABB")
	@Type(type = "fixedLengthChar")
	private String abbreviation;

	@Column(name = "IMG_STAT_DES")
	@Type(type = "fixedLengthChar")
	private String description;

	/**
	 * Returns the category code to uniquely identify the type of image
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Updates the id
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Return the abbreviation for the image category
	 * @return abbreviation
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
	 * Returns the description of the image category
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Updates the description
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ImageStatus{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a WarehouseLocationItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ImageStatus)) {
			return false;
		}

		ImageStatus that = (ImageStatus) o;
		if (this.id != null ? !this.id.equals(that.id) : that.id != null) return false;

		return true;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return this.id == null ? 0 : this.id.hashCode();
	}
}
