package com.heb.pm.entity;


import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The Key for the imageMetaData object
 * @author s753601
 * @version 2.13.0
 */
@Embeddable
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ImageMetaDataKey implements Serializable{

	private static final int PRIME_NUMBER=59;
	private static final int FOUR_BYTES = 32;

	@Column(name = "KEY_ID")
	private long id;

	@Column(name = "SEQ_NBR")
	private long sequenceNumber;

	@Column(name = "IMG_SUBJ_TYP_CD")
	@Type(type = "fixedLengthCharPK")
	private String imageSubjectTypeCode;

	/**
	 * Returns a numeric component of the image metadata
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Updates the id
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the sequence number component of the imageMetaData
	 * @return sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Updates the sequenceNumber
	 * @param sequenceNumber the new SequenceNumber
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Gets the subject of the image that can be used to determine where the image is destined to go
	 * @return imageSubjectTypeCode
	 */
	public String getImageSubjectTypeCode() {
		return imageSubjectTypeCode;
	}

	/**
	 * Updates the imageSubjectTypeCode
	 * @param imageSubjectTypeCode the new imageSubjectTypeCode
	 */
	public void setImageSubjectTypeCode(String imageSubjectTypeCode) {
		this.imageSubjectTypeCode = imageSubjectTypeCode;
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

		ImageMetaDataKey that = (ImageMetaDataKey) o;

		if (this.id != that.id) return false;
		if (this.sequenceNumber != that.sequenceNumber) return false;
		if (this.imageSubjectTypeCode != null ? !this.imageSubjectTypeCode.equals(that.imageSubjectTypeCode) : that.imageSubjectTypeCode != null) return false;
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
		int result = id != 0 ? (int)(id ^ (id >>> FOUR_BYTES)) : 0;
		result = PRIME_NUMBER * result + (int)(sequenceNumber ^ (sequenceNumber >>> FOUR_BYTES));
		result = PRIME_NUMBER * result + (imageSubjectTypeCode != null ? imageSubjectTypeCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ImageMetaData{" +
				"id='" + id + "\'" +
				", sequenceNumber='" + sequenceNumber + "\'" +
				", imageSubjectTypeCode='" + imageSubjectTypeCode + "\'" +
				'}';
	}
}
