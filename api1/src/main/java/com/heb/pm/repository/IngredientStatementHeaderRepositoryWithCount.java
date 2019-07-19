/*
 *  IngredientStatementHeaderRepositoryWithCount
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.IngredientStatementHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve ingredient statement information based on different criteria.
 * @author s573181
 * @since 2.2.0
 */
public interface IngredientStatementHeaderRepositoryWithCount extends JpaRepository<IngredientStatementHeader, Long> {

	/**
	 * Returns a pageable list of IngredientStatementHeader by StatementNumber.
	 *
	 * @param pageable The page request.
	 * @return A page of IngredientStatementHeader requested.
	 */
	Page<IngredientStatementHeader> findByStatementNumberInAndMaintenanceCodeNot(List<Long> statementNumber, char deleteCode, Pageable pageable);

	/**
	 * Returns a pageable list of IngredientStatementHeader by ingredientCode.
	 *
	 * @param pageable The page request.
	 * @return A page of IngredientStatementHeader requested.
	 */
	Page<IngredientStatementHeader> findByIngredientStatementDetailsIngredientIngredientCodeInAndMaintenanceCodeNot(
			List<String> ingredientCodes, char deleteCode, Pageable pageable);

	/**
	 * Returns a pageable list of IngredientStatementHeader by ingredient description.
	 *
	 * @param pageable The page request.
	 * @return A page of IngredientStatementHeader requested.
	 */
	Page<IngredientStatementHeader> findDistinctByIngredientStatementDetailsIngredientIngredientDescriptionIgnoreCaseContainingAndMaintenanceCodeNot(
			String description, char deleteCode, Pageable pageable);

	/**
	 * Returns a pageable list of IngredientStatementHeaders that contain all of the requested ingredient codes
	 * in no particular order.
	 *
	 * @param ingredientCodes The ingredient codes to search for. The method will only return ingredient statemetns with
	 *                        all the requested ingredients.
	 * @param ingredientCount The number of ingredient codes in the list.
	 * @param pageable The page request.
	 * @param ignoreMaintenanceCode A delete maintenance code to ignore.
	 * @return A page of IngredientStatementHeaders.
	 */
	//db2oracle change vn00907 added TRIM 
	@Query(value="  select ish from IngredientStatementHeader ish where ish.statementNumber in (" +
			"select isd.key.statementNumber " +
			"from IngredientStatementDetail isd " +
			"where isd.key.ingredientCode in :ingredientCodes " +
			"group by isd.key.statementNumber " +
			"having count(isd.key.ingredientCode) = :ingredientCount) " +
			"and ish.maintenanceCode <> :ignoreMaintenanceCode")
	
	Page<IngredientStatementHeader> findByIngredientCodesMatchingAll(@Param("ingredientCodes")List<String> ingredientCodes,
														  @Param("ingredientCount")long ingredientCount,
														  @Param("ignoreMaintenanceCode")char ignoreMaintenanceCode,
														  Pageable pageable);

	/**
	 * Find by maintenance code not page.
	 *
	 * @param maintenanceCode the maintenance code
	 * @param pageable        the pageable
	 * @return the page
	 */
	Page<IngredientStatementHeader> findByMaintenanceCodeNot(char maintenanceCode, Pageable pageable);
}
