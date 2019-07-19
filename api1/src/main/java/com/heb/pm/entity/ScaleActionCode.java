/*
 *  ActionCode
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
 * Represents the scale maintenance action codes.
 *
 * @author s573181
 * @since 2.0.8
 */
@Entity
@Table(name = "sl_hilite_prnt_cd")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ScaleActionCode implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final String SCALE_ACTION_CODE_SORT_FIELD = "actionCode";
	private static final String DISPLAY_NAME_FORMAT = "%s[%d]";

	@Id
	@Column(name = "pd_hilite_prnt_cd", scale = 5)
	private Long actionCode;

	@Column(name = "pd_hilite_prnt_des")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")  
	private String description;

	@Transient
	private long countOfActionCodeUpcs;

	/**
	 * Returns the count of action code upcs.
	 *
	 * @return The count of action code upcs.
	 */
	public long getCountOfActionCodeUpcs() {
		return countOfActionCodeUpcs;
	}

	/**
	 * Sets the count of action code upcs.
	 *
	 * @param countOfActionCodeUpcs the count of action code upcs.
	 */
	public void setCountOfActionCodeUpcs(long countOfActionCodeUpcs) {
		this.countOfActionCodeUpcs = countOfActionCodeUpcs;
	}

	/**
	 * Returns the action code.
	 *
	 * @return the action code.
	 */
	public Long getActionCode() {
		return actionCode;
	}

	/**
	 * Sets the action code.
	 *
	 * @param actionCode the action code.
	 */
	public void setActionCode(Long actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Returns the description.
	 *
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Compares another object to this one. If that object is an action code, it uses they keys to determine if
	 * they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ScaleActionCode that = (ScaleActionCode) o;

		return actionCode.equals(that.actionCode);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return actionCode.hashCode();
	}

	/**
	 * Provides a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "ActionCode{" +
				"actionCode=" + actionCode +
				'}';
	}

	/**
	 * Returns the default sort order for the scale action code table.
	 *
	 * @return The default sort order for the scale action code table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, ScaleActionCode.SCALE_ACTION_CODE_SORT_FIELD)
		);
	}

	/**
	 * Returns the vendor as it should be displayed on the GUI.
	 *
	 * @return A String representation of the vendor as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		return String.format(ScaleActionCode.DISPLAY_NAME_FORMAT, this.description.trim(), this.actionCode);
	}
}
