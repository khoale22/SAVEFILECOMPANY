/*
 * ItemType
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the item type code table. This tells whether it is Sellable or an ingredient or display or freights.
 *
 * @author l730832
 * @since 2.7.0
 */
@Entity
@Table(name = "itm_typ_codes")
public class ItemType implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;
	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";
	public static final String ITEM_TYPE_CODE_DEFAULT = "";
	public static final String ITEM_TYPE_CODE_0 = "0";
	public static final String ITEM_TYPE_CODE_1 = "1";
	public static final String ITEM_TYPE_CODE_2 = "2";
	public static final String ITEM_TYPE_CODE_3 = "3";
	public static final String ITEM_TYPE_CODE_4 = "4";
	public static final String ITEM_TYPE_CODE_5 = "5";
	public static final String ITEM_TYPE_CODE_6 = "6";
	public static final String ITEM_TYPE_CODE_7 = "7";
	public static final String ITEM_TYPE_CODE_9 = "9";
	public static final String ITEM_TYPE_CODE_F = "F";
	public static final String ITEM_TYPE_CODE_T = "T";
	/**
	 * STRING_SELL.
	 */
	public static final String ITEM_TYPE_CODE_SELL = "SELL";

	/** The Constants PRODUCT_TYPE_PRMO. */
	public static final String PRODUCT_TYPE_PRMO = "PRMO";

	/** The Constants PRODUCT_TYPE_SHPR. */
	public static final String PRODUCT_TYPE_SHPR = "SHPR";

	/** The Constants PRODUCT_TYPE_XDK. */
	public static final String PRODUCT_TYPE_XDK = "XDK";
	/** The Constants PRODUCT_TYPE_8. */
	public static final String PRODUCT_TYPE_8 = "8";
	/** The Constants PRODUCT_TYPE_9. */
	public static final String PRODUCT_TYPE_9 = "9";
	/** The Constants PRODUCT_TYPE_DEFAULT. */
	public static final String PRODUCT_TYPE_DEFAULT = "0";
	@Id
	@Column(name="itm_typ_cd")
	private String id;

	@Column(name="itm_typ_des")
	private String description;

	@Column(name="itm_typ_abb")
	private String abbreviation;

	/**
	 * Returns the Id in the code table.
	 *
	 * @return Id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the Id
	 *
	 * @param id The Id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the Description
	 *
	 * @return Description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description
	 *
	 * @param description The Description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the Abbreviation
	 *
	 * @return Abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets the Abbreviation
	 *
	 * @param abbreviation The Abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Returns the display name. The display name is a nicer way to display the entity. i.e. Test[10]
	 *
	 */
	public String getDisplayName(){
		return String.format(ItemType.DISPLAY_NAME_FORMAT, this.getDescription().trim(), this.getId().trim());
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

		ItemType itemType = (ItemType) o;

		if (id != null ? !id.equals(itemType.id) : itemType.id != null) return false;
		if (description != null ? !description.equals(itemType.description) : itemType.description != null)
			return false;
		return abbreviation != null ? abbreviation.equals(itemType.abbreviation) : itemType.abbreviation == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = PRIME_NUMBER * result + (description != null ? description.hashCode() : 0);
		result = PRIME_NUMBER * result + (abbreviation != null ? abbreviation.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ItemType{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				'}';
	}
}
