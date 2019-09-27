/*
 *  WicSubCategory
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This is the wic sub category code table
 *
 * @author vn55306
 * @since 1.0.0
 */
@Entity
@Table(name = "wic_sub_cat")
public class WicSubCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private WicSubCategoryKey key;

	@Column(name = "wic_sub_cat_des")
	private String description;

	@Column(name = "leb_sw")
	private String lebSwitch;

	@ManyToOne(fetch = FetchType.LAZY)
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
	 * Returns the LebSwitch. This switch determines whether or not it is a leb.
	 *
	 * @return LebSwitch
	 */
	public String getLebSwitch() {
		return lebSwitch;
	}

}
