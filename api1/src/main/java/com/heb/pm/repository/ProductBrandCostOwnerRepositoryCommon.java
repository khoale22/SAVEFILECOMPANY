/*
 * ProductBrandCostOwnerRepositoryCommon
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.CostOwner;
import com.heb.pm.entity.ProductBrand;
import com.heb.pm.entity.ProductBrandCostOwner;
import com.heb.pm.entity.TopToTop;
import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Common constants for the count and non-count ProductBrandCostOwner JPA repository implementations.
 *
 * @author vn70529
 * @since 2.12
 */
public class ProductBrandCostOwnerRepositoryCommon {
	/**
	 * Holds the property name of brand.
	 */
	String PROD_BRND = "productBrand";
	String PROD_BRND_DES = "productBrandDescription";
	String PROD_BRND_ID = "productBrandId";
	/**
	 * Holds the property name of cost owner.
	 */
	String CST_OWNR = "costOwner";
	String CST_OWNR_NM = "costOwnerName";
	String CST_OWNR_ID = "costOwnerId";
	/**
	 * Holds the property name of top 2 top.
	 */
	String T2T = "topToTop";
	String T2T_NM = "topToTopName";
	String T2T_ID = "topToTopId";
	String PERCENT_SIGN = "%";

	/**
	 * Create the query condition by product brand, cost owner and top 2 top.
	 *
	 * @param productBrand the description or id of brand.
	 * @param costOwner the name or id of costowner.
	 * @param top2Top the name or id of top 2 top.
	 * @param criteriaBuilder the criteria Builder.
	 * @param prodBrndCstOwnrRoot the root of prodBrndCstOwnr.
	 * @return the list of Predicates.
	 */
	protected List<Predicate> buildQueryCondition(String productBrand, String costOwner, String top2Top, CriteriaBuilder criteriaBuilder, Root<ProductBrandCostOwner> prodBrndCstOwnrRoot) {
		// Build condition for brand.
		Predicate productBrandPredicate = buildConditionForProductBrand(productBrand, criteriaBuilder, prodBrndCstOwnrRoot);
		// Build condition for costwner.
		Predicate costOwnerPredicate = buildConditionForCostOwner(costOwner, criteriaBuilder, prodBrndCstOwnrRoot);
		// Build condition for top 2 top.
		Predicate top2TopPredicate = buildConditionForTop2Top(top2Top, criteriaBuilder, prodBrndCstOwnrRoot);
		List<Predicate> predicates = new ArrayList<>();
		if (productBrandPredicate != null) { predicates.add(productBrandPredicate);}
		if (costOwnerPredicate != null) { predicates.add(costOwnerPredicate);}
		if (top2TopPredicate != null) { predicates.add(top2TopPredicate);}
		return predicates;
	}
	/**
	 * Creates the condition for ProductBrand.
	 *
	 * @param productBrand the description or id of brand.
	 * @param criteriaBuilder the criteria Builder.
	 * @param prodBrndCstOwnrRoot the root of prodBrndCstOwnr.
	 * @return the instance of Predicate.
	 */
	protected Predicate buildConditionForProductBrand(String productBrand,
													  CriteriaBuilder criteriaBuilder,
													  Root<ProductBrandCostOwner> prodBrndCstOwnrRoot) {
		if (!StringUtils.isEmpty(productBrand)) {
			Path path = criteriaBuilder.treat(prodBrndCstOwnrRoot.get(PROD_BRND), ProductBrand.class);
			return criteriaBuilder.or(
					criteriaBuilder.equal(path.get(PROD_BRND_ID).as(String.class), productBrand)
			);
		}
		return null;
	}
	/**
	 * Creates the condition for costOwner.
	 * @param costOwner the name or id of costowner.
	 * @param criteriaBuilder the criteria Builder.
	 * @param prodBrndCstOwnrRoot the root of prodBrndCstOwnr.
	 * @return the instance of Predicate.
	 */
	protected Predicate buildConditionForCostOwner(String costOwner,
												   CriteriaBuilder criteriaBuilder,
												   Root<ProductBrandCostOwner> prodBrndCstOwnrRoot) {
		if (!StringUtils.isEmpty(costOwner)) {
			Path path = criteriaBuilder.treat(prodBrndCstOwnrRoot.get(CST_OWNR), CostOwner.class);
			return criteriaBuilder.or(
					criteriaBuilder.equal(path.get(CST_OWNR_ID).as(String.class), costOwner)
			);
		}
		return null;
	}
	/**
	 * Creates the condition for Top2Top.
	 *
	 * @param top2Top the name or id of top 2 top.
	 * @param criteriaBuilder the criteria Builder.
	 * @param prodBrndCstOwnrRoot the root of prodBrndCstOwnr.
	 * @return the instance of Predicate.
	 */
	protected Predicate buildConditionForTop2Top(String top2Top,
												 CriteriaBuilder criteriaBuilder,
												 Root<ProductBrandCostOwner> prodBrndCstOwnrRoot) {
		if (!StringUtils.isEmpty(top2Top)) {
			// Filter by top 2 top.
			Path path = criteriaBuilder.treat(prodBrndCstOwnrRoot.get(CST_OWNR), CostOwner.class);
			path = criteriaBuilder.treat(path.get(T2T), TopToTop.class);
			return criteriaBuilder.or(
					criteriaBuilder.equal(path.get(T2T_ID).as(String.class), top2Top)
			);
		}
		return null;
	}
}
