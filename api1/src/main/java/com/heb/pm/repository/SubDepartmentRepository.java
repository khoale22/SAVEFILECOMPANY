/*
 * SubDepartmentRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA Repository for SubDepartments.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface SubDepartmentRepository extends JpaRepository<SubDepartment, SubDepartmentKey>{

	/**
	 * Find distinct by key sub department list.
	 *
	 * @param subDepartment the sub department
	 * @return the list
	 */
	SubDepartment findOneByKeyDepartmentAndKeySubDepartment(String department, String subDepartment);

	/**
	 * Find SubDepartment by key department .
	 *
	 * @param department the  department
	 * @return the list SubDepartment.
	 */
	List<SubDepartment> findByKeyDepartment(String department);
}
