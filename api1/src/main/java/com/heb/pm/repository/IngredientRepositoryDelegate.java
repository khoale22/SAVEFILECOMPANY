package com.heb.pm.repository;

import com.heb.pm.entity.Ingredient;

/**
 * Repository to retrieve ingredient information without count.
 *
 * @author m594201
 * @since 2.0.9
 */
public interface IngredientRepositoryDelegate extends IngredientRepositoryCommon {

	/**
	 * Find first by order by ingredient code desc ingredient.
	 *
	 * @return the ingredient
	 */
	Ingredient findFirstByOrderByIngredientCodeDesc();
}
