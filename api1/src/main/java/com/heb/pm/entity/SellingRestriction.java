package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a record in the sals_rstr_grp table.
 *
 * @author m314029
 * @since 2.5.0
 */
@Where(clause = "sals_rstr_grp_cd <> 9")
@Entity
@Table(name="sals_rstr_grp")
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class SellingRestriction implements Serializable {

	private static final long serialVersionUID = 1L;

	// default constructor
	public SellingRestriction(){super();}

	// copy constructor
	public SellingRestriction(SellingRestriction sellingRestriction){
		super();
		this.setRestrictionGroupCode(sellingRestriction.getRestrictionGroupCode());
		this.setRestrictionAbbreviation(sellingRestriction.getRestrictionAbbreviation());
		this.setRestrictionDescription(sellingRestriction.getRestrictionDescription());
	}

	@Id
	@Column(name = "sals_rstr_grp_cd")
	@Type(type="fixedLengthCharPK")
	private String restrictionGroupCode;

	@Column(name = "sals_rstr_grp_abb")
	@Type(type="fixedLengthChar")
	private String restrictionAbbreviation;

	@Column(name = "sals_rstr_grp_des")
	@Type(type="fixedLengthChar")
	private String restrictionDescription;

	/**
	 * Returns RestrictionGroupCode.
	 *
	 * @return The RestrictionGroupCode.
	 **/
	public String getRestrictionGroupCode() {
		return restrictionGroupCode;
	}

	/**
	 * Sets the RestrictionGroupCode.
	 *
	 * @param restrictionGroupCode The RestrictionGroupCode.
	 **/
	public void setRestrictionGroupCode(String restrictionGroupCode) {
		this.restrictionGroupCode = restrictionGroupCode;
	}

	/**
	 * Returns RestrictionAbbreviation.
	 *
	 * @return The RestrictionAbbreviation.
	 **/
	public String getRestrictionAbbreviation() {
		return restrictionAbbreviation;
	}

	/**
	 * Sets the RestrictionAbbreviation.
	 *
	 * @param restrictionAbbreviation The RestrictionAbbreviation.
	 **/
	public void setRestrictionAbbreviation(String restrictionAbbreviation) {
		this.restrictionAbbreviation = restrictionAbbreviation;
	}

	/**
	 * Returns RestrictionDescription.
	 *
	 * @return The RestrictionDescription.
	 **/
	public String getRestrictionDescription() {
		return restrictionDescription;
	}

	/**
	 * Sets the RestrictionDescription.
	 *
	 * @param restrictionDescription The RestrictionDescription.
	 **/
	public void setRestrictionDescription(String restrictionDescription) {
		this.restrictionDescription = restrictionDescription;
	}

	/**
	 * Compares another object to this one. If that object is a SellingRestriction, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SellingRestriction that = (SellingRestriction) o;

		return restrictionGroupCode != null ? restrictionGroupCode.equals(that.restrictionGroupCode) : that.restrictionGroupCode == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return restrictionGroupCode != null ? restrictionGroupCode.hashCode() : 0;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "SellingRestriction{" +
				"restrictionGroupCode='" + restrictionGroupCode + '\'' +
				", restrictionAbbreviation='" + restrictionAbbreviation + '\'' +
				", restrictionDescription='" + restrictionDescription + '\'' +
				'}';
	}
}
