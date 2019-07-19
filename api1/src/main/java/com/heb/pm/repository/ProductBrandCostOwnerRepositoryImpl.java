/*
 * ProductBrandCostOwnerRepositoryImpl
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Repository of the Product Brand Cost Owner entity for filter.
 *
 * @author vn70529
 * @since 2.12.0
 */
public class ProductBrandCostOwnerRepositoryImpl extends ProductBrandCostOwnerRepositoryCommon {
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
	public List<ProductBrandCostOwner> findBrndCstOwnrT2TByProductBrandAndCostOwnerAndTop2Top(String productBrand,
																							  String costOwner,
																							  String top2Top,
																							  Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductBrandCostOwner> criteriaQuery = criteriaBuilder.createQuery(ProductBrandCostOwner.class);
		Root<ProductBrandCostOwner> prodBrndCstOwnrRoot = criteriaQuery.from(ProductBrandCostOwner.class);
		// Build condition.
		List<Predicate> predicates = buildQueryCondition(productBrand, costOwner, top2Top, criteriaBuilder, prodBrndCstOwnrRoot);
		// Sets condition.
		Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
		criteriaQuery.where(predicatesArray);
		return entityManager.createQuery(criteriaQuery).
				setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).
				setMaxResults(pageable.getPageSize()).getResultList();
	}
}