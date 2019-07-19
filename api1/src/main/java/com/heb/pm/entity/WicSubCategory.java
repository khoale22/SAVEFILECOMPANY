/*
 *  WicSubCategory
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This is the wic sub category code table
 *
 * @author l730832
 * @since 2.12.0
 */
@Entity
@Table(name = "wic_sub_cat")
public class WicSubCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s [%d]";

	@EmbeddedId
	private WicSubCategoryKey key;

	@Column(name = "wic_sub_cat_des")
	private String description;

	@Column(name = "leb_sw")
	private Boolean lebSwitch;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wic_cat_id", referencedColumnName = "wic_cat_id", insertable = false,  updatable = false, nullable = false)
	private WicCategory wicCategory;

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public WicSubCategoryKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(WicSubCategoryKey key) {
		this.key = key;
	}

	/**
	 * Returns the WicSubCategoryDescription. This is the description for the wic sub category.
	 *
	 * @return WicSubCategoryDescription
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the WicSubCategoryDescription
	 *
	 * @param description The WicSubCategoryDescription
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the LebSwitch. This switch determines whether or not it is an leb.
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
	 * Returns the wic category.
	 *
	 * @return WicCategory
	 */
	public WicCategory getWicCategory() {
		return wicCategory;
	}

	/**
	 * Sets the wic category.
	 * @param wicCategory
	 */
	public void setWicCategory(WicCategory wicCategory) {
		this.wicCategory = wicCategory;
	}

	/**
	 * Gets display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return String.format(WicSubCategory.DISPLAY_NAME_FORMAT, this.description.trim(), this.key.getWicSubCategoryId());
	}

	/**
	 * Gets display LebSwitch.
	 *
	 * @return the display LebSwitch
	 */
	public String getDisplayLebSwitch() {
		return getLebSwitch()?"Y":"N";
	}
}
