/*
 *  MerchantType
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
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
 * Represents merchant type. A merchant type can be a Merchant M, S, A, W, or 0. A type of merchant is according to what
 * kind of merchandise the merchant sells.
 *
 * @author m314029
 * @since 2.5.0
 */
@Entity
@Table(name = "merch_typ_cd")
@TypeDefs({

		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class MerchantType implements Serializable {

	// default constructor
	public MerchantType(){super();}

	// copy constructor
	public MerchantType(MerchantType merchantType){
		super();
		this.setId(merchantType.getId());
		this.setDescription(merchantType.getDescription());
		this.setAbbreviation(merchantType.getAbbreviation());
	}

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="merch_typ_cd")
	//@Type(type="fixedLengthCharPK")
	private String id;

	@Column(name="merch_typ_des")
	@Type(type="fixedLengthChar")
	private String description;

	@Column(name="merch_typ_abb")
	@Type(type="fixedLengthChar")
	private String abbreviation;

	/**
	 * Returns id.
	 *
	 * @return The id.
	 **/
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id The id.
	 **/
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns description.
	 *
	 * @return The description.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description The description.
	 **/
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns abbreviation.
	 *
	 * @return The abbreviation.
	 **/
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the abbreviation.
	 *
	 * @param abbreviation The abbreviation.
	 **/
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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

		MerchantType that = (MerchantType) o;

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
		return "MerchantType{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				'}';
	}
}
