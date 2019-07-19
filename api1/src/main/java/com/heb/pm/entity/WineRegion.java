/*
 * WineRegion
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name="wine_rgn")
public class WineRegion implements Serializable {
	private static final String DEFAULT_SORT_BY_WINE_REGION_ID = "wineRegionId";
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="wine_rgn_id")
	private Integer wineRegionId;

	@Column(name="wine_rgn_des")
	private String wineRegionDescription;

	@Column(name="wine_rgn_nm")
	private String wineRegionName;

	@JsonIgnoreProperties("wineArea")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name="wine_area_id", referencedColumnName = "wine_area_id", insertable = false, updatable = false, nullable = false)
	})
	private WineArea wineArea;
	/**
	 * get wine region id
	 * @return
	 */
	public Integer getWineRegionId() {
		return wineRegionId;
	}

	/**
	 * set wine region id
	 * @param wineRegionId
	 * @return
	 */
	public WineRegion setWineRegionId(Integer wineRegionId) {
		this.wineRegionId = wineRegionId;
		return this;
	}

	/**
	 * get wine region description
	 * @return
	 */
	public String getWineRegionDescription() {
		return wineRegionDescription;
	}

	/**
	 * set wine region description
	 * @param wineRegionDescription
	 * @return
	 */
	public WineRegion setWineRegionDescription(String wineRegionDescription) {
		this.wineRegionDescription = wineRegionDescription;
		return this;
	}

	/**
	 * get wine region name
	 * @return
	 */
	public String getWineRegionName() {
		return wineRegionName;
	}

	/**
	 * set wine region name
	 * @param wineRegionName
	 * @return
	 */
	public WineRegion setWineRegionName(String wineRegionName) {
		this.wineRegionName = wineRegionName;
		return this;
	}

	/**
	 * get wine area
	 * @return
	 */
	public WineArea getWineArea() {
		return wineArea;
	}

	/**
	 * set wine area
	 * @param wineArea
	 * @return
	 */
	public WineRegion setWineArea(WineArea wineArea) {
		this.wineArea = wineArea;
		return this;
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
		WineRegion region = (WineRegion) o;
		return region.getWineRegionId()==this.getWineRegionId();
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = wineRegionId != null ? wineRegionId.hashCode() : 0;
		result = 31 * result + (wineRegionName != null ? wineRegionName.hashCode() : 0);
		result = 31 * result + (wineRegionDescription != null ? wineRegionDescription.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "WineRegion{" +
				"id='" + wineRegionId + '\'' +
				", wineRegionName='" + wineRegionName + '\'' +
				", wineRegionDescription='" + wineRegionDescription + '\'' +
				'}';
	}
	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(WineRegion.DEFAULT_SORT_BY_WINE_REGION_ID);
	}
}
