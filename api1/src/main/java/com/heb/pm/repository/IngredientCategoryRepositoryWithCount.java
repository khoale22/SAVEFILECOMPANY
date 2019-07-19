/*
 *  IngredientCategoryRepositoryWithCount
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.IngredientCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve ingredient category information based on different criteria.
 *
 * @author s573181
 * @since 2.1.0
 */
public interface IngredientCategoryRepositoryWithCount extends JpaRepository<IngredientCategory, Long>,
		IngredientCategoryRepositoryDelegateWithCount  {
	/**
	 * Returns a page of Ingredient Categories based on the category codes.
	 *
	 * @param categoryCodes The category codes to search on.
	 * @param page The page of requested.
	 * @return A page of Ingredient Categories based on the category codes searched.
	 */
	Page<IngredientCategory> findByCategoryCodeIn(List<Long> categoryCodes, Pageable page);
}
