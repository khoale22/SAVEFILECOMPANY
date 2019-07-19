/*
 * WineMaker
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name="winemkr")
public class WineMaker implements Serializable {
	private static final String DEFAULT_SORT_BY_WINE_MAKER_ID = "wineMakerId";
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="winemkr_id")
	private Long wineMakerId;

	@Column(name="winemkr_nm")
	private String wineMakerName;

	@Column(name="winemkr_narr_txt")
	private String wineMakerDescription;

	/**
	 * get wine maker id
	 * @return
	 */
	public Long getWineMakerId() {
		return wineMakerId;
	}

	/**
	 * set wine maker id
	 * @param wineMakerId
	 * @return
	 */
	public WineMaker setWineMakerId(Long wineMakerId) {
		this.wineMakerId = wineMakerId;
		return this;
	}

	/**
	 * get wine maker name
	 * @return
	 */
	public String getWineMakerName() {
		return wineMakerName;
	}

	/**
	 * set wine maker name
	 * @param wineMakerName
	 * @return
	 */
	public WineMaker setWineMakerName(String wineMakerName) {
		this.wineMakerName = wineMakerName;
		return this;
	}

	/**
	 * get wine maker description
	 * @return
	 */
	public String getWineMakerDescription() {
		return wineMakerDescription;
	}

	/**
	 * set wine maker description
	 * @param wineMakerDescription
	 * @return
	 */
	public WineMaker setWineMakerDescription(String wineMakerDescription) {
		this.wineMakerDescription = wineMakerDescription;
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
		WineMaker maker = (WineMaker) o;
		return maker.getWineMakerId()==this.getWineMakerId();
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = wineMakerId != null ? wineMakerId.hashCode() : 0;
		result = 31 * result + (wineMakerName != null ? wineMakerName.hashCode() : 0);
		result = 31 * result + (wineMakerDescription != null ? wineMakerDescription.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "WineMaker{" +
				"id='" + wineMakerId + '\'' +
				", wineMakerName='" + wineMakerName + '\'' +
				", wineMakerDescription='" + wineMakerDescription + '\'' +
				'}';
	}
	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(WineMaker.DEFAULT_SORT_BY_WINE_MAKER_ID);
	}
}
