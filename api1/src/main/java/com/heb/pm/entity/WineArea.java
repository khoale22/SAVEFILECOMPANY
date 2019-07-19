/*
 * WineArea
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 * entity wine area
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name="WINE_AREA")
public class WineArea implements Serializable {
	private static final String DEFAULT_SORT_BY_WINE_AREA_ID = "wineAreaId";
	/**
	 * format to show summary detail AREA
	 */
	private static final String AREA_SUMMARY_FORMAT="%s [%s]";
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="wine_area_id")
	private Long wineAreaId;

	@Column(name="wine_area_nm")
	private String wineAreaName;

	@Column(name="wine_area_des")
	private String wineAreaDescription;

	/**
	 * get wine area id
	 * @return long
	 */
	public Long getWineAreaId() {
		return wineAreaId;
	}

	/**
	 * set wine area id
	 * @param wineAreaId
	 * @return
	 */
	public WineArea setWineAreaId(Long wineAreaId) {
		this.wineAreaId = wineAreaId;
		return this;
	}

	/**
	 * get wine area name
	 * @return
	 */
	public String getWineAreaName() {
		return wineAreaName;
	}

	/**
	 * set wine area name
	 * @param wineAreaName
	 * @return
	 */
	public WineArea setWineAreaName(String wineAreaName) {
		this.wineAreaName = wineAreaName;
		return this;
	}

	/**
	 * get wine area description
	 * @return
	 */
	public String getWineAreaDescription() {
		return wineAreaDescription;
	}

	/**
	 * set wine area description
	 * @param wineAreaDescription
	 * @return
	 */
	public WineArea setWineAreaDescription(String wineAreaDescription) {
		this.wineAreaDescription = wineAreaDescription;
		return this;
	}
	/**
	 * get summary field to show ui
	 * @return summary detail
	 */
	public String getWineAreaSummary(){
		return String.format(AREA_SUMMARY_FORMAT, StringUtils.trimToEmpty(this.getWineAreaDescription()), String.valueOf(this.getWineAreaId()));
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
		WineArea wine = (WineArea) o;
		return wine.getWineAreaId()==this.getWineAreaId();
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = wineAreaId != null ? wineAreaId.hashCode() : 0;
		result = 31 * result + (wineAreaName != null ? wineAreaName.hashCode() : 0);
		result = 31 * result + (wineAreaDescription != null ? wineAreaDescription.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "WineArea{" +
				"id='" + wineAreaId + '\'' +
				", wineAreaName='" + wineAreaName + '\'' +
				", wineAreaDescription='" + wineAreaDescription + '\'' +
				'}';
	}
	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(WineArea.DEFAULT_SORT_BY_WINE_AREA_ID);
	}
}
