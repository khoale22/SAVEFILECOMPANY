/*
 * ScaleUpcRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ScaleUpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for scale upc with counts.
 *
 * @author m314029
 * @since 2.0.8
 */
public interface ScaleUpcRepositoryWithCount extends JpaRepository<ScaleUpc, Long> {

	/**
	 * Search ScaleUpc by description.
	 *
	 * @param queryString1 The first query string.
	 * @param queryString2 The second query string.
	 * @param queryString3 The third query string.
	 * @param queryString4 The fourth query string.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from ScaleUpc. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ScaleUpc> findByEnglishDescriptionOneContainsOrEnglishDescriptionTwoContainsOrEnglishDescriptionThreeContainsOrEnglishDescriptionFourContains(
			@Param("queryString1") String queryString1,
			@Param("queryString2") String queryString2,
			@Param("queryString3") String queryString3,
			@Param("queryString4") String queryString4,
			Pageable pageRequest);
	/**
	 * Search ScaleUpc by description.
	 *
	 * @param upcList The list of upcs to search for
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from ScaleUpc. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ScaleUpc> findByUpcIn(@Param("upcList") List<Long> upcList, Pageable pageRequest);

	/**
	 * Count by label format one int.
	 *
	 * @param firstLabelFormat the first label format
	 * @return the int
	 */
	int countByLabelFormatOne(@Param("firstLabelFormat") long firstLabelFormat);

	/**
	 * Count by label format two int.
	 *
	 * @param secondLabelFormat the second label format
	 * @return the int
	 */
	int countByLabelFormatTwo(@Param("secondLabelFormat") long secondLabelFormat);

	/**
	 * Find by label format one page.
	 *
	 * @param firstLabelFormat the first label format
	 * @param pageRequest      the page request
	 * @return the page
	 */
	Page<ScaleUpc> findByLabelFormatOne(@Param("firstLabelFormat") long firstLabelFormat,
										Pageable pageRequest);

	/**
	 * Find by label format two page.
	 *
	 * @param secondLabelFormat the second label format
	 * @param pageRequest       the page request
	 * @return the page
	 */
	Page<ScaleUpc> findByLabelFormatTwo(@Param("secondLabelFormat") long secondLabelFormat,
										Pageable pageRequest);

	/**
	 * Search ScaleUpc by ActionCode.
	 *
	 * @param actionCode the ActionCode.
	 * @param pageRequest  Page information to include page, page size, and sort order.
	 * @return A populated Page with records from ScaleUpc. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ScaleUpc> findByActionCode(Long actionCode, Pageable pageRequest);

	/**
	 * Returns the upc count of a action code.
	 *
	 * @param actionCode The action code.
	 * @return the upc count of a action code.
	 */
	long countByActionCode(long actionCode);

	/**
	 * Get count of scale upcs mathcing the graphics code input.
	 * @param graphicsCode graphics code to be matched.
	 * @return count of scale upcs.
	 */
	int countByGraphicsCode(@Param("graphicsCode") Long graphicsCode);

	/**
	 * Search ScaleUpc by graphics Code.
	 *
	 * @param graphicsCode The first query string.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from ScaleUpc. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ScaleUpc> findByGraphicsCode(@Param("graphicsCode") Long graphicsCode, Pageable pageRequest);

	/**
	 * Returns a pageable list of scale UPCs that have a looked for ingredient statement number.
	 *
	 * @param ingredientStatement The ingredient statement number to look for.
	 * @param pageRequest Page request to include page, page size, and sort order.
	 * @return A list of scale UPCs with a given ingredient statement number including page and total counts.
	 */
	Page<ScaleUpc> findByIngredientStatement(@Param("ingredientStatement") Long ingredientStatement,
											 Pageable pageRequest);
}
