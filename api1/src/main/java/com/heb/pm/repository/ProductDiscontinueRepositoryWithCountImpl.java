/*
 * ProductDiscontinueRepositoryWithCountImpl
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.productDiscontinue.StatusFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * JPA repository implementation for data from the prod_del table for queries without parameters including count.
 *
 * @author m314029
 * @since 2.0.8
 */
public class ProductDiscontinueRepositoryWithCountImpl  implements ProductDiscontinueRepositoryDelegateWithCount {

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	/**
	 * Search prod_del by a department.
	 *
	 * @param department The department to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Override
	public Page<ProductDiscontinue> findByDepartment(
			String department, StatusFilter statusFilter, Pageable pageRequest) {
		return this.getPage(department, null, pageRequest, statusFilter);
	}

	/**
	 * Search prod_del by a department and sub department.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@Override
	public Page<ProductDiscontinue> findByDepartmentAndSubDepartment(
			String department, String subDepartment, StatusFilter statusFilter, Pageable pageRequest) {
		return this.getPage(department, subDepartment, pageRequest, statusFilter);
	}

	/**
	 * Return a page of product discontinue data based on search criteria.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @param statusFilter  The filter to apply to item status.
	 * @return A populated Page with records from prod_del. Will include total available
	 * record counts and number of available pages.
	 */
	@SuppressWarnings("unchecked")
	private Page<ProductDiscontinue> getPage(String department, String subDepartment, Pageable pageRequest,
											 StatusFilter statusFilter){
		String baseQuery = this.getBaseQueryString(department, subDepartment, statusFilter);
		Query returnQuery;
		long count = this.getCount(baseQuery);
		returnQuery = entityManager.createQuery(BASE_NON_COUNT_SEARCH.concat(baseQuery), ProductDiscontinue.class);
		returnQuery.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
		returnQuery.setMaxResults(pageRequest.getPageSize());
		List<ProductDiscontinue> returnList = returnQuery.getResultList();
		return new PageImpl<ProductDiscontinue>(returnList, pageRequest, count);
	}

	/**
	 * Get the base query to use to call the db.
	 *
	 * @param department The department to search by.
	 * @param subDepartment The sub department to search by.
	 * @param statusFilter  The filter to apply to item status.
	 * @return Base query to use to call db.
	 */
	private String getBaseQueryString(String department, String subDepartment, StatusFilter statusFilter){
		String queryString;

		if(StringUtils.isBlank(subDepartment)){
			queryString = String.format(BASE_DEPARTMENT_SEARCH, department.trim());
		} else {
			queryString = String.format(BASE_DEPARTMENT_AND_SUB_DEPARTMENT_SEARCH, department.trim(),
					subDepartment.trim());
		}

		switch (statusFilter) {
			case ACTIVE:
				queryString = queryString.concat(ACTIVE_ITEMS_PREDICATE);
				break;
			case DISCONTINUED:
				queryString = queryString.concat(DISCONTINUED_ITEMS_PREDICATE);
				break;
		}

		return queryString;
	}

	/**
	 * Return the count from specified query.
	 *
	 * @param baseQuery Base query to call db.
	 * @return The count of records based on supplied query.
	 */
	private Long getCount(String baseQuery){
		return (Long)entityManager.createQuery(BASE_COUNT_SEARCH.concat(baseQuery)).getSingleResult();
	}
}
