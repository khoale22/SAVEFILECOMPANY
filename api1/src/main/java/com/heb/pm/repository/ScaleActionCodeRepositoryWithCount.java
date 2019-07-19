/*
 *  ScaleActionCodeRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ScaleActionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for scale action codes with counts.
 *
 * @author s573181
 * @since 2.0.8
 */
public interface ScaleActionCodeRepositoryWithCount extends JpaRepository<ScaleActionCode, Long> {

	/**
	 * Search ScaleActionCode by action code.
	 *
	 * @param actionCodeList a list of action codes.
	 * @param pageRequest  Page information to include page, page size, and sort order.
	 * @return A populated Page with records from ScaleActionCode. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ScaleActionCode> findByActionCodeIn(List<Long> actionCodeList, Pageable pageRequest);

	/**
	 * Search ScaleActionCode by description.
	 *
	 * @param description The ScaleActionCode description.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from ScaleActionCode. Will include total available
	 * record counts and number of available pages.
	 */
	Page<ScaleActionCode> findByDescriptionContains(String description, Pageable pageRequest);
}
