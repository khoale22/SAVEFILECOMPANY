/*
 * ScaleLabelFormat
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a scale label format.
 *
 * @author d116773
 * @since 2.0.8
 */
@Entity
@Table(name="sl_label_format")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ScaleLabelFormat implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_SORT = "formatCode";

	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@Id
	@Column(name="sl_lbl_frmat_cd")
	private long formatCode;

	@Column(name = "sl_lbl_frmat_des")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String description;

	@Transient
	private int countOfLabelFormatOneUpcs;

	@Transient
	private int countOfLabelFormatTwoUpcs;

	/**
	 * Gets count of label format one upcs.
	 *
	 * @return the count of label format one upcs
	 */
	public int getCountOfLabelFormatOneUpcs() {
		return countOfLabelFormatOneUpcs;
	}

	/**
	 * Sets count of label format one upcs.
	 *
	 * @param countOfLabelFormatOneUpcs the count of label format one upcs
	 */
	public void setCountOfLabelFormatOneUpcs(int countOfLabelFormatOneUpcs) {
		this.countOfLabelFormatOneUpcs = countOfLabelFormatOneUpcs;
	}

	/**
	 * Gets count of label format two upcs.
	 *
	 * @return the count of label format two upcs
	 */
	public int getCountOfLabelFormatTwoUpcs() {
		return countOfLabelFormatTwoUpcs;
	}

	/**
	 * Sets count of label format two upcs.
	 *
	 * @param countOfLabelFormatTwoUpcs the count of label format two upcs
	 */
	public void setCountOfLabelFormatTwoUpcs(int countOfLabelFormatTwoUpcs) {
		this.countOfLabelFormatTwoUpcs = countOfLabelFormatTwoUpcs;
	}

	/**
	 * Returns the format code.
	 *
	 * @return The format code.
	 */
	public long getFormatCode() {
		return formatCode;
	}

	/**
	 * Sets the format code.
	 *
	 * @param formatCode The format code.
	 */
	public void setFormatCode(long formatCode) {
		this.formatCode = formatCode;
	}

	/**
	 * Returns the description of the label format.
	 *
	 * @return The description of the label format.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the label format.
	 *
	 * @param description The description of the label format.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Compares this object to another to test for equality. Equality is based on the formatCode property.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ScaleLabelFormat)) return false;

		ScaleLabelFormat format = (ScaleLabelFormat) o;

		return formatCode == format.formatCode;

	}

	/**
	 * Generates a hash code for the object. Equal objects have the same hash code. Unequal objects have different
	 * hash codes.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {
		return (int) (formatCode ^ (formatCode >>> 32));
	}

	/**
	 * Provides a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "ScaleLabelFormat{" +
				"formatCode='" + formatCode + '\'' +
				", description='" + description + '\'' +
				", labelOnes='" + this.getCountOfLabelFormatOneUpcs() + "\'" +
				", labelTwos='" + this.getCountOfLabelFormatTwoUpcs() + "\'" +
				'}';
	}

	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(new Sort.Order(Sort.Direction.ASC, ScaleLabelFormat.DEFAULT_SORT));
	}

	/**
	 * Returns the vendor as it should be displayed on the GUI.
	 *
	 * @return A String representation of the vendor as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		return String.format(ScaleLabelFormat.DISPLAY_NAME_FORMAT, this.description.trim(), this.formatCode);
	}
}