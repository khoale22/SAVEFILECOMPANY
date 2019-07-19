/*
 * ProductDiscontinueNewRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.productDiscontinue.StatusFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for data from the prod_del table for queries without parameters.
 *
 * @author m314029
 * @since 2.0.8
 */
public interface ProductDiscontinueRepositoryDelegate extends ProductDiscontinueRepositoryDelegateCommon {

	/**
	 * Search prod_del by a department.
	 *
	 * @param department The department to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	List<ProductDiscontinue> findByDepartment(@Param("department") String department,
											  @Param("statusFilter") StatusFilter statusFilter,
											  Pageable pageRequest);

	/**
	 * Search prod_del by a department and sub department.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A List of records from prod delete. Will not include total available
	 * record counts and number of available pages.
	 */
	List<ProductDiscontinue> findByDepartmentAndSubDepartment(@Param("department") String department,
															  @Param("subDepartment") String subDepartment,
															  @Param("statusFilter") StatusFilter statusFilter,
															  Pageable pageRequest);
}
