package com.heb.pm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a record in the sals_rstr_code table.
 *
 * @author m314029
 * @since 2.5.0
 */
@Entity
@Table(name="sals_rstr_code")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class SellingRestrictionCode implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String SHIPPING_RESTRICTION_CODE="9";

	// default constructor
	public SellingRestrictionCode(){super();}

	// copy constructor
	public SellingRestrictionCode(SellingRestrictionCode sellingRestrictionCode){
		super();
		this.setRestrictionCode(sellingRestrictionCode.getRestrictionCode());
		this.setRestrictionAbbreviation(sellingRestrictionCode.getRestrictionAbbreviation());
		this.setRestrictionDescription(sellingRestrictionCode.getRestrictionDescription());
		this.setRestrictionGroupCode(sellingRestrictionCode.getRestrictionGroupCode());
		this.setDateTimeRestriction(sellingRestrictionCode.getDateTimeRestriction());
		this.setMinimumRestrictedAge(sellingRestrictionCode.getMinimumRestrictedAge());
		this.setRestrictedQuantity(sellingRestrictionCode.getRestrictedQuantity());
	}

	@Id
	@Column(name = "sals_rstr_cd")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK") 
	private String restrictionCode;

	@Column(name = "sals_rstr_abb")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String restrictionAbbreviation;

	@Column(name = "sals_rstr_des")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String restrictionDescription;

	@Column(name = "sals_rstr_grp_cd")
	@Type(type="fixedLengthCharPK")
	private String restrictionGroupCode;

	@Column(name = "dd_tm_rstr_sw")
	private Boolean dateTimeRestriction;

	@Column(name = "min_rstr_age_nbr")
	private Integer minimumRestrictedAge;

	@Column(name = "rstred_qty")
	private Double restrictedQuantity;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sals_rstr_grp_cd", referencedColumnName = "sals_rstr_grp_cd", insertable = false, updatable = false, nullable = false)
	private SellingRestriction sellingRestriction;

	/**
	 * Returns RestrictionCode.
	 *
	 * @return The RestrictionCode.
	 **/
	public String getRestrictionCode() {
		return restrictionCode;
	}

	/**
	 * Sets the RestrictionCode.
	 *
	 * @param restrictionCode The RestrictionCode.
	 **/
	public void setRestrictionCode(String restrictionCode) {
		this.restrictionCode = restrictionCode;
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
	 * Returns DateTimeRestriction.
	 *
	 * @return The DateTimeRestriction.
	 **/
	public Boolean getDateTimeRestriction() {
		return dateTimeRestriction;
	}

	/**
	 * Sets the DateTimeRestriction.
	 *
	 * @param dateTimeRestriction The DateTimeRestriction.
	 **/
	public void setDateTimeRestriction(Boolean dateTimeRestriction) {
		this.dateTimeRestriction = dateTimeRestriction;
	}

	/**
	 * Returns MinimumRestrictedAge.
	 *
	 * @return The MinimumRestrictedAge.
	 **/
	public Integer getMinimumRestrictedAge() {
		return minimumRestrictedAge;
	}

	/**
	 * Sets the MinimumRestrictedAge.
	 *
	 * @param minimumRestrictedAge The MinimumRestrictedAge.
	 **/
	public void setMinimumRestrictedAge(Integer minimumRestrictedAge) {
		this.minimumRestrictedAge = minimumRestrictedAge;
	}

	/**
	 * Returns RestrictedQuantity.
	 *
	 * @return The RestrictedQuantity.
	 **/
	public Double getRestrictedQuantity() {
		return restrictedQuantity;
	}

	/**
	 * Sets the RestrictedQuantity.
	 *
	 * @param restrictedQuantity The RestrictedQuantity.
	 **/
	public void setRestrictedQuantity(Double restrictedQuantity) {
		this.restrictedQuantity = restrictedQuantity;
	}

	/**
	 * Returns the type of selling restriction associated with this code
	 * @return selling Restriction
	 */
	public SellingRestriction getSellingRestriction() {
		return sellingRestriction;
	}

	/**
	 * Updates the sellingRestriction
	 * @param sellingRestriction the new sellingRestriction
	 */
	public void setSellingRestriction(SellingRestriction sellingRestriction) {
		this.sellingRestriction = sellingRestriction;
	}

	/**
	 * Flag to state if the code is associated with a shipping restriction.
	 * @return
	 */
	public boolean isShippingRestriction(){ return this.restrictionGroupCode.equals(SHIPPING_RESTRICTION_CODE);}

	/**
	 * Compares another object to this one. If that object is a SellingRestrictionCode, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SellingRestrictionCode that = (SellingRestrictionCode) o;

		return restrictionCode != null ? restrictionCode.equals(that.restrictionCode) : that.restrictionCode == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return restrictionCode != null ? restrictionCode.hashCode() : 0;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "SellingRestrictionCode{" +
				"restrictionCode='" + restrictionCode + '\'' +
				", restrictionAbbreviation='" + restrictionAbbreviation + '\'' +
				", restrictionDescription='" + restrictionDescription + '\'' +
				", restrictionGroupCode='" + restrictionGroupCode + '\'' +
				", dateTimeRestriction=" + dateTimeRestriction +
				", minimumRestrictedAge=" + minimumRestrictedAge +
				", restrictedQuantity=" + restrictedQuantity +
				'}';
	}
}
