/*
 * ScaleGraphicsCode
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a Graphics Code Code table.
 *
 * @author vn40486
 * @since 2.1.0
 */
@Entity
@Table(name="sl_safe_handlg_cd")
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ScaleGraphicsCode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Supported Sort By fields.
	 */
	public static final String SORT_FIELD_SCALE_GRAPHICS_CD = "scaleGraphicsCode";
	public static final String SORT_FIELD_SCALE_GRAPHICS_CD_DES = "scaleGraphicsCodeDescription";
	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@Id
	@Column(name = "PD_SAFE_HAND_CD")
	 //db2o changes  vn00907
	private Long scaleGraphicsCode;
	@Type(type="fixedLengthChar")    
	@Column(name = "PD_SAFE_HAND_DES")
	
	private String scaleGraphicsCodeDescription;

	@Transient
	private int scaleScanCodeCount;

	/**
	 * Returns scale graphics code of this object.
	 * @return long representation of scale graphics code id.
	 */
	public Long getScaleGraphicsCode() {
		return scaleGraphicsCode;
	}

	/**
	 * Sets scale graphics code.
	 *
	 * @param scaleGraphicsCode scale graphics code.
	 */
	public void setScaleGraphicsCode(Long scaleGraphicsCode) {
		this.scaleGraphicsCode = scaleGraphicsCode;
	}

	/**
	 * Returns scale graphics code description.
	 * @return scale graphics code description.
	 */
	public String getScaleGraphicsCodeDescription() {
		return scaleGraphicsCodeDescription;
	}

	/**
	 * Sets scale graphics code description.
	 * @param scaleGraphicsCodeDescription scale graphics code description.
	 */
	public void setScaleGraphicsCodeDescription(String scaleGraphicsCodeDescription) {
		this.scaleGraphicsCodeDescription = scaleGraphicsCodeDescription;
	}

	/**
	 * Returns scale scan code count.
	 * @return	 scale upc/scan code count.
	 */
	public int getScaleScanCodeCount() {
		return scaleScanCodeCount;
	}

	/**
	 * Sets scale scan/upc count.
	 * @param scaleScanCodeCount scale scan/upc count.
	 */
	public void setScaleScanCodeCount(int scaleScanCodeCount) {
		this.scaleScanCodeCount = scaleScanCodeCount;
	}

	/**
	 * Returns the default sort order.
	 *
	 * @return The default sort order.
	 */
	public static Sort getDefaultSort() {
		return new Sort(new Sort.Order(Sort.Direction.ASC, SORT_FIELD_SCALE_GRAPHICS_CD));
	}

	/**
	 * Compares another object to this one. If that object is an ItemNotDeleted, it uses they key-scaleGraphicsCode
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ScaleGraphicsCode that = (ScaleGraphicsCode) o;

		return !(scaleGraphicsCode != null ? !scaleGraphicsCode.equals(that.scaleGraphicsCode) : that.scaleGraphicsCode != null);

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return scaleGraphicsCode != null ? scaleGraphicsCode.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ScaleGraphicsCode{" +
				"scaleGraphicsCode=" + scaleGraphicsCode +
				", scaleGraphicsCodeDescription='" + scaleGraphicsCodeDescription + '\'' +
				", scaleScanCodeCount=" + scaleScanCodeCount +
				'}';
	}

	/**
	 * Returns the graphics code as it should be displayed on the GUI.
	 *
	 * @return A String representation of the graphics code as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		return String.format(ScaleGraphicsCode.DISPLAY_NAME_FORMAT, this.scaleGraphicsCodeDescription.trim(), this.scaleGraphicsCode);
	}
}
