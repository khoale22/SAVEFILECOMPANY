package com.heb.pm.repository;

import com.heb.pm.entity.NutrientStatementHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for NutrientStatementHeaderRepository
 *
 * @author m594201
 * @since 2.2.0
 */
public interface NutrientStatementHeaderRepository extends JpaRepository<NutrientStatementHeader, Long> {

	/**
	 * Find by nutrient statement detail list key nutrient label code in list.
	 *
	 * @param nutrientCodes the nutrient codes
	 * @param pageRequest   the page request
	 * @return the list
	 */
	List<NutrientStatementHeader> findDistinctByNutrientStatementDetailListKeyNutrientLabelCodeInAndStatementMaintenanceSwitchNot(List<Long> nutrientCodes, char deleteCode, Pageable pageRequest);

	/**
	 * Find by nutrient statement number in list.
	 *
	 * @param statementIds the statement ids
	 * @param pageRequest  the page request
	 * @return the list
	 */
	List<NutrientStatementHeader> findByNutrientStatementNumberInAndStatementMaintenanceSwitchNot(List<Long> statementIds, char deleteCode, Pageable pageRequest);

	/**
	 * Find all by page list.
	 *
	 * @param statementIdsRequest the statement ids request
	 * @return the list
	 */
	List<NutrientStatementHeader> findByStatementMaintenanceSwitchNot(char statementMaintenanceSwitch, Pageable statementIdsRequest);

	/**
	 * Returns a list of NutrientStatementHeader by nutrient statement codes. Used to calculate hits.
	 * @param nutrientStatements nutrient statements.
	 * @return A List of NutrientStatementHeader requested.
	 */
	List<NutrientStatementHeader> findByNutrientStatementNumberIn(List<Long> nutrientStatements);

	// Created for PM-985
	@Query("select nsh.nutrientStatementNumber from NutrientStatementHeader nsh where nutrientStatementNumber in (:nutrientStatements)")
	List<Long> findNutrientStatementNumberByNutrientStatementNumberIn(@Param("nutrientStatements")List<Long> nutrientStatements);

	/**
	 * Finds a list of nutrient statement headers by nutrient codes.
	 *
	 * @param nutrientCodes the nutrient codes.
	 * @return the list
	 */
	List<NutrientStatementHeader> findByNutrientStatementDetailListKeyNutrientLabelCodeIn(List<Long> nutrientCodes);

	/**
	 * Finds a list of nutrient statement headers by nutrient codes.
	 *
	 * @param uomCode the uom codes.
	 * @return the list
	 */
	Page<NutrientStatementHeader> findByUomMetricCodeIsOrUomCommonCodeIs(long uomCode, long uomCode2, Pageable uomRequest);
}

