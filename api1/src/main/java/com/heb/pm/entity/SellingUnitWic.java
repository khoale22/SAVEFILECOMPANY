/*
 *  WIC
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * This is the wic entity. This represents a wic (woman infant child) product which is a govt sponsored program where you can
 * buy wic products using wic cards.
 *
 * @author l730832
 * @since 2.12.0
 */
@Entity
@Table(name = "prod_scn_cd_wic")
public class SellingUnitWic implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SellingUnitWicKey key;

	@Column(name = "leb_sw")
	private Boolean lebSwitch;

	@Column(name = "wic_prod_des")
	private String wicDescription;

	@Column(name = "wic_pkg_sz_qty", precision = 7, scale = 2)
	private Double wicPackageSize;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wic_cat_id", referencedColumnName = "wic_cat_id", insertable = false, updatable = false)
	private WicCategory wicCategory;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "wic_sub_cat_id", referencedColumnName = "wic_sub_cat_id", insertable = false, updatable = false),
			@JoinColumn(name = "wic_cat_id", referencedColumnName = "wic_cat_id", insertable = false, updatable = false)
	})
	private WicSubCategory wicSubCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scn_cd_id", referencedColumnName = "scn_cd_id", insertable = false, updatable = false)
	private SellingUnit sellingUnit;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public SellingUnitWicKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(SellingUnitWicKey key) {
		this.key = key;
	}

	/**
	 * Returns the LebSwitch. The LEB switch determines whether or not it is LEB.
	 *
	 * @return LebSwitch
	 */
	public Boolean getLebSwitch() {
		return lebSwitch;
	}

	/**
	 * Sets the LebSwitch
	 *
	 * @param lebSwitch The LebSwitch
	 */
	public void setLebSwitch(Boolean lebSwitch) {
		this.lebSwitch = lebSwitch;
	}

	/**
	 * Returns the WicDescription. The wic description of the current wic.
	 *
	 * @return WicDescription
	 */
	public String getWicDescription() {
		return wicDescription;
	}

	/**
	 * Sets the WicDescription
	 *
	 * @param wicDescription The WicDescription
	 */
	public void setWicDescription(String wicDescription) {
		this.wicDescription = wicDescription;
	}

	/**
	 * Returns the WicPackageSize. The Size of the WIC UPC
	 *
	 * @return WicPackageSize
	 */
	public Double getWicPackageSize() {
		return wicPackageSize;
	}

	/**
	 * Sets the WicPackageSize
	 *
	 * @param wicPackageSize The WicPackageSize
	 */
	public void setWicPackageSize(Double wicPackageSize) {
		this.wicPackageSize = wicPackageSize;
	}

	/**
	 * Returns the WicCategory. The category the current wic is in.
	 *
	 * @return WicCategory
	 */
	public WicCategory getWicCategory() {
		return wicCategory;
	}

	/**
	 * Sets the WicCategory
	 *
	 * @param wicCategory The WicCategory
	 */
	public void setWicCategory(WicCategory wicCategory) {
		this.wicCategory = wicCategory;
	}

	/**
	 * Returns the WicSubCategory. The sub category the current wic is in.
	 *
	 * @return WicSubCategory
	 */
	public WicSubCategory getWicSubCategory() {
		return wicSubCategory;
	}

	/**
	 * Sets the WicSubCategory
	 *
	 * @param wicSubCategory The WicSubCategory
	 */
	public void setWicSubCategory(WicSubCategory wicSubCategory) {
		this.wicSubCategory = wicSubCategory;
	}

	/**
	 * Returns the CrossLinkedSellingUnit. A cross linked selling unit.
	 *
	 * @return CrossLinkedSellingUnit
	 */
	public SellingUnit getSellingUnit() {
		return sellingUnit;
	}

	/**
	 * Sets the Selling Unit. This is a representation of the selling unit that is the wic.
	 *
	 * @param sellingUnit The selling unit
	 */
	public void setSellingUnit(SellingUnit sellingUnit) {
		this.sellingUnit = sellingUnit;
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

		SellingUnitWic sellingUnitWic = (SellingUnitWic) o;

		return key != null ? !key.equals(sellingUnitWic.key) : sellingUnitWic.key != null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "SellingUnitWic{" +
				"key=" + key +
				", lebSwitch=" + lebSwitch +
				'}';
	}
}
