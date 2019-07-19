/*
 *  NutrientStatementDetailRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientStatementDetail;
import com.heb.pm.entity.NutrientStatementDetailsKey;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about an Nutrient Statement Detail.
 * @author m594201
 * @since 2.3.0
 */

public interface NutrientStatementDetailRepository extends JpaRepository<NutrientStatementDetail, NutrientStatementDetailsKey> {

	// Updated for PM-985
	String NUTRIENT_LABEL_CODE_BY_LIST_QUERY = "select detail.key.nutrientStatementNumber from NutrientStatementDetail detail where detail.key.nutrientLabelCode in :detailList";

	String NUTRIENT_LABEL_CODE_QUERY = "select distinct detail from NutrientStatementDetail detail where detail.key.nutrientLabelCode = :nutrientCode";

	/**
	 * Finds the nutrient Statement Number.
	 * @param statementNumber Statement Number
	 * @return The Nutrient Statement Number.
	 */
	List<NutrientStatementDetail> findByKeyNutrientStatementNumber(Long statementNumber);

	/**
	 * Finds the nutrient label code by nutrient label code
	 * @param nutrientLabelCodeList List of nutrient label codes.
	 * @return List of nutrient statement details.
	 */
	@Query(value = NUTRIENT_LABEL_CODE_BY_LIST_QUERY)
	List<Long>  findNutrientLabelCodeByKeyNutrientLabelCodeIn(@Param("detailList")List<Long> nutrientLabelCodeList);


	/**
	 * Finds a list of nutrient statement details that contain a given nutrient. It is really intended to check
	 * for nutrients before deletion, so it takes a page request but returns a list.
	 *
	 * @param nutrientCode The ID of the nutrient to look for.
	 * @param pageRequest The page request to limit the number of rows.
	 * @return A list of nutrient statement detail records that contain that nutrient.
	 */
	@Query(value = NUTRIENT_LABEL_CODE_QUERY)
	List<NutrientStatementDetail> findNutrientLabelCodeByKeyNutrientLabelCode(
			@Param("nutrientCode")long nutrientCode, Pageable pageRequest);

}
