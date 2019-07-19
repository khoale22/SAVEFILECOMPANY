package com.heb.pm.repository;

import com.heb.pm.entity.NutrientStatementHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for NutrientStatementHeaderRepositoryWithCount
 *
 * @author m594201
 * @since 2.2.0
 */
public interface NutrientStatementHeaderRepositoryWithCount  extends JpaRepository<NutrientStatementHeader, Long> {

	/**
	 * Find by nutrient statement detail list key nutrient label code in page.
	 *
	 * @param nutrientCodes the nutrient codes
	 * @param pageRequest   the page request
	 * @return the page
	 */
	Page<NutrientStatementHeader> findDistinctByNutrientStatementDetailListKeyNutrientLabelCodeInAndStatementMaintenanceSwitchNot(List<Long> nutrientCodes, char deleteCode, Pageable pageRequest);

	/**
	 * Find by nutrient statement number in page.
	 *
	 * @param statementIds the statement ids
	 * @param pageRequest  the page request
	 * @return the page
	 */
	Page<NutrientStatementHeader> findByNutrientStatementNumberInAndStatementMaintenanceSwitchNot(List<Long> statementIds, char deleteCode, Pageable pageRequest);


	/**
	 * Find by statement maintenance switch not page.
	 *
	 * @param statementMaintenanceSwitch the statement maintenance switch
	 * @param statementIdsRequest        the statement ids request
	 * @return the page
	 */
	Page<NutrientStatementHeader> findByStatementMaintenanceSwitchNot(char statementMaintenanceSwitch, Pageable statementIdsRequest);

	/**
	 * Finds a list of nutrient statement headers by nutrient codes.
	 *
	 * @param uomCode the uom codes.
	 * @return the list
	 */


	/**
	 * Finds a list of nutrient statement headers by nutrient codes.
	 *
	 * @param uomCode the uom codes.
	 * @return the list
	 */
	Page<NutrientStatementHeader> findByUomMetricCodeIsOrUomCommonCodeIs(long uomCode, long uomCode2, Pageable uomRequest);
}
