/*
 * SubDepartmentKey
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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key for the sub-department entity. If the key represents a department,
 * then subDepartment is all blanks.
 *
 * @author d116773
 * @since 2.0.2
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907

public class SubDepartmentKey implements Serializable {

	// default constructor
	public SubDepartmentKey(){super();}

	//initialize constructor with field department and sub-department
	public SubDepartmentKey(String department, String subDepartment) {
		this.department = department;
		this.subDepartment = subDepartment;
	}

	// copy constructor
	public SubDepartmentKey(SubDepartmentKey key){
		super();
		this.setDepartment(key.getDepartment());
		this.setSubDepartment(key.getSubDepartment());
	}

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name="str_dept_nbr")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String department;
	

	@Column(name="str_sub_dept_id")
	 //db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	private String subDepartment;

	/**
	 * Returns the department part of the key. If this is 07A, this is 07.
	 *
	 * @return The department.
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * Sets the department part of the key.
	 *
	 * @param department The department.
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * Returns the sub-department part of they key. If this is 07A, this is A.
	 *
	 * @return The sub-department.
	 */
	public String getSubDepartment() {
		return subDepartment;
	}

	/**
	 * Sets the sub-department part of the key.
	 *
	 * @param subDepartment The sub-department.
	 */
	public void setSubDepartment(String subDepartment) {
		this.subDepartment = subDepartment;
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
		if (!(o instanceof SubDepartmentKey)) return false;

		SubDepartmentKey that = (SubDepartmentKey) o;

		if (!department.equals(that.department)) return false;
		return subDepartment.equals(that.subDepartment);

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = department.hashCode();
		result = SubDepartmentKey.PRIME_NUMBER * result + subDepartment.hashCode();
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "SubDepartmentKey{" +
				"department='" + department + '\'' +
				", subDepartment='" + subDepartment + '\'' +
				'}';
	}
}
