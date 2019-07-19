/*
 * IngredientRepositoryImpl
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * JPA repository implementation for data from the sl_ingredient table for queries requiring non-JPA sql.
 *
 * @author m314029
 * @since 2.1.0
 */
public class IngredientRepositoryImpl implements IngredientRepositoryDelegate {

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	/**
	 * Finds first ingredient code in db after converting to number then sorting in desc order by ingredient code.
	 * @return ingredient
	 */
	@Override
	public Ingredient findFirstByOrderByIngredientCodeDesc() {
		String baseQuery = "select i from Ingredient i order by to_number(i.ingredientCode) desc";
		Query returnQuery;

		returnQuery = entityManager.createQuery(baseQuery, Ingredient.class);
		returnQuery.setFirstResult(0);
		returnQuery.setMaxResults(1);
		return (Ingredient)returnQuery.getSingleResult();
	}
}
