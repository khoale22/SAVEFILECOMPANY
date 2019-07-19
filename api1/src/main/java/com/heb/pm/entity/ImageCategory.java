package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Image Category code which are assigned when an image is uploaded
 * @author s753601
 * @updated by vn86116
 * @version 2.13.0
 */
@Entity
@Table(name = "IMG_CAT")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ImageCategory implements Serializable{

	@Id
	@Column(name = "IMG_CAT_CD")
	@Type(type = "fixedLengthCharPK")
	private String id;

	@Column(name = "IMG_CAT_ABB")
	@Type(type = "fixedLengthCharPK")
	private String abbreviation;

	@Column(name = "IMG_CAT_DES")
	@Type(type = "fixedLengthCharPK")
	private String description;

	@JsonIgnore
	@OneToMany(mappedBy = "imageCategory", fetch = FetchType.LAZY)
	private List<ImageMetaData> imageMetaData;
	
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
		return "ImageCategory{" +
				"id=" + id +
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
		if (!(o instanceof ImageCategory)) {
			return false;
		}

		ImageCategory that = (ImageCategory) o;
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
