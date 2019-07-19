/*
 *  IngredientCategoryRepositoryImpl
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.IngredientCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 *  JPA repository implementation for data from the sl_ingrd_cat_grp table for queries without parameters.
 *
 * @author s573181
 * @since 2.1.0
 */
public class IngredientCategoryRepositoryImpl implements IngredientCategoryRepositoryDelegate {

	private static final String DESCRIPTION_REGEX = "%%%s%%";

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	/**
	 * Returns a page of Ingredient Categories based on the descriptions.
	 *
	 * @param categoryDescriptions The category descriptions.
	 * @param categoryRequest The category request.
	 * @return A page of Ingredient Categories based on the descriptions.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<IngredientCategory> findByCategoryDescriptionContains(List<String> categoryDescriptions, Pageable categoryRequest) {
		String baseQuery = QUERY;
		for(String s: categoryDescriptions){
			if(categoryDescriptions.indexOf(s)!= 0){
				baseQuery = baseQuery.concat(OR);
			}
			baseQuery =  baseQuery.concat(String.format(DESCRIPTION,
					String.format(DESCRIPTION_REGEX,s.trim().toUpperCase())));
		}
		Query returnQuery;

		returnQuery = entityManager.createQuery(SELECT.concat(baseQuery), IngredientCategory.class);
		returnQuery.setFirstResult(categoryRequest.getPageNumber() * categoryRequest.getPageSize());
		returnQuery.setMaxResults(categoryRequest.getPageSize());
		return returnQuery.getResultList();
	}
}
