/*
 * IngredientSubRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.IngredientSub;
import com.heb.pm.entity.IngredientSubKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about a sub ingredient.
 *
 * @author m314029
 * @since 2.1.0
 */
public interface IngredientSubRepository extends JpaRepository<IngredientSub, IngredientSubKey> {

	/**
	 * Find by key ingredient code list.
	 *
	 * @param ingredientCode the ingredient code
	 * @return the list
	 */
	List<IngredientSub> findByKeyIngredientCode(String ingredientCode);

	/**
	 * Find first by key so ingredient code order by soi sequence desc ingredient sub.
	 *
	 * @param soIngredientCode the so ingredient code
	 * @return the ingredient sub
	 */
	IngredientSub findFirstByKeySoIngredientCodeOrderBySoiSequenceDesc(String soIngredientCode);

	/**
	 * Find by key so ingredient code in list.
	 *
	 * @param soIngredientCodes the so ingredient codes
	 * @return the list
	 */
	List<IngredientSub> findByKeySoIngredientCodeIn(List<String> soIngredientCodes);

	/**
	 * Find by key ingredient code in list.
	 *
	 * @param ingredientCodes the ingredient codes
	 * @return the list
	 */
	List<IngredientSub> findByKeyIngredientCodeIn(List<String> ingredientCodes);
}
