/*
 * ProductHierarchy
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.Department;

import java.io.Serializable;
import java.util.List;

/**
 * The type Product hierarchy.
 *
 * @author m314029
 * @since 2.4.0
 */
public class ProductHierarchy implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The Departments.
	 */
	List<Department> departments;

	/**
	 * Gets departments.
	 *
	 * @return the departments
	 */
	public List<Department> getDepartments() {
		return departments;
	}

	/**
	 * Sets departments.
	 *
	 * @param departments the departments
	 */
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
}
