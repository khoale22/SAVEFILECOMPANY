/*
 *  IngredientStatementDetailRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.IngredientStatementDetail;
import com.heb.pm.entity.IngredientStatementDetailsKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve ingredient details without count.
 *
 * @author s573181
 * @since 2.2.0
 */
public interface IngredientStatementDetailRepository extends
		JpaRepository<IngredientStatementDetail, IngredientStatementDetailsKey> {

	/**
	 * Finds IngredientStatementDetail by statement number, with the lowest ingredient percentage.
	 *
	 * @param statementNumber The statement number.
	 * @return The IngredientStatementDetail by statement number, with the lowest ingredient percentage.
	 */
	public IngredientStatementDetail findFirstByKeyStatementNumberOrderByIngredientPercentageAsc(Long statementNumber);

	/**
	 * Finds all IngredientStatementDetail by key ingredient code.
	 *
	 * @param ingredientCode The ingredient code.
	 * @return The list.
	 */
	List<IngredientStatementDetail> findByKeyIngredientCode(String ingredientCode);
}
