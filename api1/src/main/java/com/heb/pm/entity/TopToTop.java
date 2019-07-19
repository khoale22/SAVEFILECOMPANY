/*
 *  TopToTop
 *
 *  Copyright (c) 2017 HEB
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the T2T.
 *
 * @author l730832
 * @since 2.5.0
 */
@Entity
@Table(name = "t2t")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class TopToTop  implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final String DISPLAY_NAME_FORMAT = "%s [%d]";
	private static final String DEFAULT_SORT_FIELD = "topToTopId";

	@Id
	@Column(name = "t2t_id")
	private Integer topToTopId;

	@Column(name = "t2t_nm")
	 //db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String topToTopName;

	/**
	 * Returns the TopToTopId
	 *
	 * @return TopToTopId
	 **/
	public Integer getTopToTopId() {
		return topToTopId;
	}

	/**
	 * Sets the TopToTopId
	 *
	 * @param topToTopId The TopToTopId
	 **/

	public void setTopToTopId(Integer topToTopId) {
		this.topToTopId = topToTopId;
	}

	/**
	 * Returns the TopToTopName
	 *
	 * @return TopToTopName
	 **/
	public String getTopToTopName() {
		return topToTopName;
	}

	/**
	 * Sets the TopToTopName
	 *
	 * @param topToTopName The TopToTopName
	 **/

	public void setTopToTopName(String topToTopName) {
		this.topToTopName = topToTopName;
	}

	/**
	 * The display name for the T2T
	 * @return the display name in the format "General Mills [1250]"
	 */
	public String getDisplayName(){
		return String.format(TopToTop.DISPLAY_NAME_FORMAT, this.getTopToTopName().trim(),
				this.getTopToTopId());
	}
	
	/**
	 * Returns the default sort order for the top 2 top table.
	 *
	 * @return The default sort order for the top 2 top table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, TopToTop.DEFAULT_SORT_FIELD)
		);
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "TopToTop{" +
				"topToTopId=" + topToTopId +
				", topToTopName='" + topToTopName + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a Location, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TopToTop topToTop = (TopToTop) o;

		if (topToTopId != null ? !topToTopId.equals(topToTop.topToTopId) : topToTop.topToTopId != null) return false;
		return topToTopName != null ? topToTopName.equals(topToTop.topToTopName) : topToTop.topToTopName == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = topToTopId != null ? topToTopId.hashCode() : 0;
		result = 31 * result + (topToTopName != null ? topToTopName.hashCode() : 0);
		return result;
	}
}
