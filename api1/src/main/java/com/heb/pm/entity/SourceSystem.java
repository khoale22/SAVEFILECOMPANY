/*
 * SourceSystem
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a record in the src_system table.
 *
 * @author d116773
 * @updated by s753601
 * @updated by vn86116
 * @since 2.5.0
 */
@Entity
@Table(name = "src_system")
public class SourceSystem implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";
	public static final int SOURCE_SYSTEM_PRODUCT_MAINTENANCE  = 4;
	public static final int SOURCE_SYSTEM_GS1 = 9;
	public static final int SOURCE_SYSTEM_BLOSSOM = 14;
	public static final int SOURCE_SYSTEM_INSTACART = 19;
	public static final int SOURCE_SYSTEM_GLADSON = 7;
	public static final long SOURCE_SYSTEM_ECOMMERCE = 13l;
	public enum SourceSystemNumber {
		SOURCE_SYSTEM_ECOMMERCE(13L),
		SOURCE_SYSTEM_GLADSON(7L),
		SOURCE_SYSTEM_VESTCOM(8L),
		SOURCE_SYSTEM_CORE_PRODUCT_SETUP(1L),
		SOURCE_SYSTEM_PRODUCT_MAINTENANCE(4L),
		SOURCE_SYSTEM_OBPS(20L),
		SOURCE_SYSTEM_ESHA_GENESIS(26L),
		SOURCE_SYSTEM_GS1(9L),
		SOURCE_SYSTEM_KWIKEE(12L),
		SOURCE_SYSTEM_ITEMMASTER_COM(11L),
		SOURCE_SYSTEM_MANUFACTURER(15L),
		SOURCE_SYSTEM_OTHER_RETAILERS(16L),
		SOURCE_SYSTEM_SCALE_MANAGEMENT(17L),
		SOURCE_SYSTEM_DEFAULT(0L),
		SOURCE_SYSTEM_MAT(31L);

		private Long value;

		SourceSystemNumber(Long value) {
			this.value = value;
		}

		public Long getValue() {
			return this.value;
		}
	}

	@Id
	@Column(name="src_system_id")
	private long id;

	@Column(name="src_system_des")
	private String description;

	@OneToMany(mappedBy = "sourceSystem", fetch = FetchType.LAZY)
	private List<TargetSystemAttributePriority> targetSystemAttributePriorities;

	@JsonIgnore
	@OneToMany(mappedBy = "sourceSystem", fetch = FetchType.LAZY)
	private List<ImageMetaData> imageMetaData;

	/**
	 * Returns the ID of the source system.
	 *
	 * @return The ID of the source system.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the ID of the source system.
	 *
	 * @param id The ID of the source system.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the Description of the source system.
	 *
	 * @return The Description of the source system.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description of the source system.
	 *
	 * @param description The Description of the source system.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the list of target system attribute priority list
	 * @return the list of target system attribute priority list
	 */
	public List<TargetSystemAttributePriority> getTargetSystemAttributePriorities() {
		return targetSystemAttributePriorities;
	}
	/**
	 * Replaces the list of target system attribute priority list
	 * @param targetSystemAttributePriorities the new list of target system attribute priority list
	 */
	public void setTargetSystemAttributePriorities(List<TargetSystemAttributePriority> targetSystemAttributePriorities) {
		this.targetSystemAttributePriorities = targetSystemAttributePriorities;
	}

	/**
	 * Returns the source as it should be displayed on the GUI.
	 *
	 * @return A String representation of the source as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		if(this.description != null) {
			return String.format(SourceSystem.DISPLAY_NAME_FORMAT, this.getDescription().trim(), this.id);
		} else	{
			return String.valueOf(this.id);
		}
	}

	/**
	 * Tests equality of another object to this one. Equality is based on id.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SourceSystem)) return false;

		SourceSystem that = (SourceSystem) o;

		return id == that.id;
	}

	/**
	 * Returns a hash code for this object. Equal objects return the same hash code. Unequal objects return
	 * different hash codes.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return (int)id;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "SourceSystem{" +
				"id=" + id +
				", description='" + description.trim() +
				"}";
	}
}
