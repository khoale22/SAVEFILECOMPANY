/*
 * Department
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents departments in the product hierarchy.
 *
 * @author m314029
 * @since 2.4.0
 */
@Entity
//db2oracle change added spaces instead of empty string
@Where(clause = "str_sub_dept_id = ' ' and str_dept_nbr not in (' ','A','B','C','D','E','F','G','H','I','J','K','L','0','00')")
@Table(name = "str_dept")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907

public class Department implements Serializable{

	// default constructor
	public Department(){super();}

	// copy constructor
	// this does not call 'setSubDepartmentList' to prevent infinite loops -- if this method needs
	// to be called, it needs to be done after creating all of the objects
	public Department(Department department){
		super();
		this.setKey(new SubDepartmentKey(department.getKey()));
		this.setName(department.getName());
		this.setReportGroupCode(department.getReportGroupCode());
		this.setGrossProfitLow(department.getGrossProfitLow());
		this.setGrossProfitHigh(department.getGrossProfitHigh());
		this.setShrinkLow(department.getShrinkLow());
		this.setShrinkHigh(department.getShrinkHigh());
	}

	private static final long serialVersionUID = 1L;
	/**
	 * The constant DISPLAY_NAME_FORMAT.
	 */
	private static final String DISPLAY_NAME_FORMAT = "%s[%s%s]";
	/**
	 * The constant NORMALIZED_ID_FORMAT.
	 */
	private static final String NORMALIZED_ID_FORMAT = "%s%s";

	@EmbeddedId
	private SubDepartmentKey key;

	@Column(name="dept_nm")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar") 
	private String name;

	// This was added to facilitate JPA mappings as it was not looking inside a SubDepartmentKey for just
	//  department. Getters and setters not added as this should not be used besides JPA mapping.
	@Column(name="str_dept_nbr", insertable = false, updatable = false)
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String departmentNumber;

	@Column(name="rept_grp_cd")
	private Long reportGroupCode;

	@Column(name="grprft_lo_pct", precision = 7, scale = 4)
	private Double grossProfitLow;

	@Column(name="grprft_hi_pct", precision = 7, scale = 4)
	private Double grossProfitHigh;

	@Column(name="shrnk_lo_pct", precision = 7, scale = 4)
	private Double shrinkLow;

	@Column(name="shrnk_hi_pct", precision = 7, scale = 4)
	private Double shrinkHigh;

	@JsonIgnoreProperties("departmentMaster")
	//db2oracle change added spaces instead of empty string by vn00907
	@Where(clause = "str_sub_dept_id != ' '")
	@OneToMany(mappedBy = "departmentMaster", fetch = FetchType.LAZY)
	private List<SubDepartment> subDepartmentList;

	/**
	 * Gets report group code.
	 *
	 * @return the report group code
	 */
	public Long getReportGroupCode() {
		return reportGroupCode;
	}

	/**
	 * Sets report group code.
	 *
	 * @param reportGroupCode the report group code
	 */
	public void setReportGroupCode(Long reportGroupCode) {
		this.reportGroupCode = reportGroupCode;
	}

	/**
	 * Gets gross profit low.
	 *
	 * @return the gross profit low
	 */
	public Double getGrossProfitLow() {
		return grossProfitLow;
	}

	/**
	 * Sets gross profit low.
	 *
	 * @param grossProfitLow the gross profit low
	 */
	public void setGrossProfitLow(Double grossProfitLow) {
		this.grossProfitLow = grossProfitLow;
	}

	/**
	 * Gets gross profit high.
	 *
	 * @return the gross profit high
	 */
	public Double getGrossProfitHigh() {
		return grossProfitHigh;
	}

	/**
	 * Sets gross profit high.
	 *
	 * @param grossProfitHigh the gross profit high
	 */
	public void setGrossProfitHigh(Double grossProfitHigh) {
		this.grossProfitHigh = grossProfitHigh;
	}

	/**
	 * Gets shrink low.
	 *
	 * @return the shrink low
	 */
	public Double getShrinkLow() {
		return shrinkLow;
	}

	/**
	 * Sets shrink low.
	 *
	 * @param shrinkLow the shrink low
	 */
	public void setShrinkLow(Double shrinkLow) {
		this.shrinkLow = shrinkLow;
	}

	/**
	 * Gets shrink high.
	 *
	 * @return the shrink high
	 */
	public Double getShrinkHigh() {
		return shrinkHigh;
	}

	/**
	 * Sets shrink high.
	 *
	 * @param shrinkHigh the shrink high
	 */
	public void setShrinkHigh(Double shrinkHigh) {
		this.shrinkHigh = shrinkHigh;
	}

	/**
	 * Gets sub department list.
	 *
	 * @return the sub department list
	 */
	public List<SubDepartment> getSubDepartmentList() {
	    if(subDepartmentList == null){
            subDepartmentList = new ArrayList<>();
        }
		return subDepartmentList;
	}

	/**
	 * Sets sub department list.
	 *
	 * @param subDepartmentList the sub department list
	 */
	public void setSubDepartmentList(List<SubDepartment> subDepartmentList) {
		this.subDepartmentList = subDepartmentList;
	}

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public SubDepartmentKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(SubDepartmentKey key) {
		this.key = key;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the department as it should be displayed on the GUI.
	 *
	 * @return A String representation of the department as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		return String.format(Department.DISPLAY_NAME_FORMAT,
				this.name.trim(), this.key.getDepartment().trim(), this.key.getSubDepartment().trim());
	}

	/**
	 * Returns a unique ID for this department as a string. This can be used in things like lists where
	 * you need a unique key and cannot access the components of the key directly.
	 *
	 * @return A unique ID for this department as a string
	 */
	public String getNormalizedId() {
		return String.format(Department.NORMALIZED_ID_FORMAT, this.key.getDepartment().trim(),
				this.key.getSubDepartment().trim());
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Department that = (Department) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Department{" +
				"key=" + key +
				", name='" + name + '\'' +
				", reportGroupCode=" + reportGroupCode +
				", grossProfitLow=" + grossProfitLow +
				", grossProfitHigh=" + grossProfitHigh +
				", shrinkLow=" + shrinkLow +
				", shrinkHigh=" + shrinkHigh +
				'}';
	}
}
