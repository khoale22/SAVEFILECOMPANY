/*
 * IngredientNutrientStatementWorkRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.IngredientNutrientStatementWork;
import com.heb.pm.entity.IngredientNutrientStatementWorkKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The repository for the entity IngredientNutrientStatementWorkRepository.
 *
 * @author l730832
 * @since 2.3.0
 */
public interface IngredientNutrientStatementWorkRepository extends JpaRepository<IngredientNutrientStatementWork, IngredientNutrientStatementWorkKey> {


	/**
	 * Find by key statement number list.
	 *
	 * @param statementNumber the statement number
	 * @return the list
	 */
	List<IngredientNutrientStatementWork> findByKeyStatementNumber(@Param("statementNumber") long statementNumber);
}
