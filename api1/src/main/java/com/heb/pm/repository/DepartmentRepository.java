/*
 * DepartmentRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.Department;
import com.heb.pm.entity.SubDepartmentKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Department.
 *
 * @author m314029
 * @since 2.4.0
 */
public interface DepartmentRepository extends JpaRepository<Department, SubDepartmentKey> {
}
