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
 * Entity representing the image type code table
 * @author s753601
 * @version 2.13.0
 */
@Entity
@Table(name = "IMG_TYP")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ImageType implements Serializable{

	public static final String IMAGE_FORMAT_TGA = "TGA";

	@Id
	@Column(name = "IMG_TYP_CD")
	@Type(type = "fixedLengthCharPK")
	private String id;

	@Column(name = "IMG_FRMAT_CD")
	@Type(type = "fixedLengthChar")
	private String imageFormat;

	/**
	 * Returns the string identifier for the image type
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Updates id
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the format string for the image
	 * @return imageFormat
	 */
	public String getImageFormat() {
		return imageFormat;
	}

	/**
	 * Updates the imageFormat
	 * @param imageFormat the new imageFormat
	 */
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ImageType{" +
				"id='" + id + '\'' +
				", imageFormat='" + imageFormat + '\'' +
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
		if (!(o instanceof ImageType)) {
			return false;
		}

		ImageType that = (ImageType) o;
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
