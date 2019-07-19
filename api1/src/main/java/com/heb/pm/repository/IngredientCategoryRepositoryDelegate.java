/*
 * IngredientCategoryRepositoryDelegate
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.IngredientCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repository to retrieve ingredient categories without count.
 *
 * @author s573181
 * @since 2.1.0
 */
public interface IngredientCategoryRepositoryDelegate extends IngredientCategoryRepositoryCommon {

	/**
	 * Returns a list of Ingredient Categories based on the descriptions requested.
	 *
	 * @param categoryDescriptions the descriptions requested.
	 * @param request The page requested.
	 * @return a list of Ingredient Categories based on the descriptions requested.
	 */
	List<IngredientCategory> findByCategoryDescriptionContains(List<String> categoryDescriptions,
															   Pageable request);
}
