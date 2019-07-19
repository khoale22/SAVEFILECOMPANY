/*
 * ProductBrandCostOwnerRepositoryWithCountImpl
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.CostOwner;
import com.heb.pm.entity.ProductBrand;
import com.heb.pm.entity.ProductBrandCostOwner;
import com.heb.pm.entity.TopToTop;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository of the Product Brand Cost Owner entity with count for filter.
 *
 * @author vn70529
 * @since 2.12.0
 */
public class ProductBrandCostOwnerRepositoryWithCountImpl extends ProductBrandCostOwnerRepositoryCommon {
	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	/**
	 * Find the list of ProductBrandCostOwners by product brand description or product brand id
	 * and cost owner name or cost owner id and top 2 top name or top 2 top id.
	 *
	 * @param productBrand the product brand description or id.
	 * @param costOwner the cost owner name or id.
	 * @param top2Top the top 2 top name or id
	 * @param pageable the paging info.
	 * @return the list of ProductBrandCostOwners.
	 */
	public Page<ProductBrandCostOwner> findBrndCstOwnrT2TByProductBrandAndCostOwnerAndTop2Top(String productBrand,
																							  String costOwner,
																							  String top2Top,
																							  Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductBrandCostOwner> criteriaQuery = criteriaBuilder.createQuery(ProductBrandCostOwner.class);
		Root<ProductBrandCostOwner> prodBrndCstOwnrRoot = criteriaQuery.from(ProductBrandCostOwner.class);
		// Build condition.
		List<Predicate> predicates = buildQueryCondition(productBrand, costOwner, top2Top, criteriaBuilder, prodBrndCstOwnrRoot);
		// Set condition
		Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
		criteriaQuery.where(predicatesArray);
		List<ProductBrandCostOwner> returnList = entityManager.createQuery(criteriaQuery).setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).setMaxResults(pageable.getPageSize()).getResultList();
		return new PageImpl<>(returnList, pageable, getCount(criteriaBuilder, predicatesArray));
	}
	/**
	 * Return the count from specified query.
	 *
	 * @param criteriaBuilder Base query to call db.
	 * @param where condition to count.
	 * @return The count of records based on supplied query.
	 */
	private Long getCount(CriteriaBuilder criteriaBuilder, Predicate[] where){
		final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(ProductBrandCostOwner.class)));
		countQuery.where(where);
		return entityManager.createQuery(countQuery).getSingleResult();
	}
}