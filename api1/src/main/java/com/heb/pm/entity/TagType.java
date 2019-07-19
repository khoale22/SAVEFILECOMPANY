package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a type of tag to print out.
 *
 * @author d116773
 * @since 2.17.0
 */
@Entity
@Table(name = "tag_typ")
@TypeDefs({
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class TagType {

	@Id
	@Column(name = "tag_typ_cd")
	private String tagTypeCode;

	@Column(name = "tag_typ_abb")
	private String tagTypeAbbreviation;

	@Column(name="tag_typ_des")
	@Type(type = "fixedLengthCharPK")
	private String tagTypeDescription;

	/**
	 * Returns the ID for this code.
	 *
	 * @return The ID for this code.
	 */
	public String getTagTypeCode() {
		return tagTypeCode;
	}

	/**
	 * Sets the ID for this code.
	 *
	 * @param tagTypeCode The ID for this code.
	 */
	public void setTagTypeCode(String tagTypeCode) {
		this.tagTypeCode = tagTypeCode;
	}

	/**
	 * Returns the abbreviation for this code.
	 *
	 * @return The abbreviation for this code.
	 */
	public String getTagTypeAbbreviation() {
		return tagTypeAbbreviation;
	}

	/**
	 * Sets the abbreviation for this code.
	 *
	 * @param tagTypeAbbreviation The abbreviation for this code.
	 */
	public void setTagTypeAbbreviation(String tagTypeAbbreviation) {
		this.tagTypeAbbreviation = tagTypeAbbreviation;
	}

	/**
	 * Returns the description for this code.
	 *
	 * @return The description for this code.
	 */
	public String getTagTypeDescription() {
		return tagTypeDescription;
	}

	/**
	 * Sets the description for this code.
	 *
	 * @param tagTypeDescription The description for this code.
	 */
	public void setTagTypeDescription(String tagTypeDescription) {
		this.tagTypeDescription = tagTypeDescription;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "TagType{" +
				"tagTypeCode='" + tagTypeCode + '\'' +
				", tagTypeAbbreviation='" + tagTypeAbbreviation + '\'' +
				", tagTypeDescription='" + tagTypeDescription + '\'' +
				'}';
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TagType)) return false;

		TagType tagType = (TagType) o;

		return tagTypeCode != null ? tagTypeCode.equals(tagType.tagTypeCode) : tagType.tagTypeCode == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return tagTypeCode != null ? tagTypeCode.hashCode() : 0;
	}
}
