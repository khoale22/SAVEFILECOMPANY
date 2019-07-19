/*
 *  IngredientCategoryRepositoryWithCountImpl
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * JPA repository implementation for data from the prod_del table for queries without parameters including count.
 *
 * @author s573181
 * @since 2.1.0
 */
public class IngredientCategoryRepositoryWithCountImpl implements IngredientCategoryRepositoryDelegateWithCount {

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
	public Page<IngredientCategory> findByCategoryDescriptionContains(List<String> categoryDescriptions,
																	  Pageable categoryRequest){

		String baseQuery = QUERY;
		for(String s: categoryDescriptions){
			if(categoryDescriptions.indexOf(s)!= 0){
				baseQuery = baseQuery.concat(OR);
			}
			baseQuery =  baseQuery.concat(String.format(DESCRIPTION,
					String.format(DESCRIPTION_REGEX, s.trim().toUpperCase())));
		}
		Query returnQuery;

		long count = this.getCount(baseQuery);

		returnQuery = entityManager.createQuery(SELECT.concat(baseQuery), IngredientCategory.class);
		returnQuery.setFirstResult(categoryRequest.getPageNumber() * categoryRequest.getPageSize());
		returnQuery.setMaxResults(categoryRequest.getPageSize());
		List<IngredientCategory> returnList = returnQuery.getResultList();
		return new PageImpl<IngredientCategory>(returnList, categoryRequest, count);
	}

	/**
	 * Return the count from specified query.
	 *
	 * @param baseQuery Base query to call db.
	 * @return The count of records based on supplied query.
	 */
	private Long getCount(String baseQuery){
		return (Long)entityManager.createQuery(COUNT.concat(baseQuery)).getSingleResult();
	}


}
