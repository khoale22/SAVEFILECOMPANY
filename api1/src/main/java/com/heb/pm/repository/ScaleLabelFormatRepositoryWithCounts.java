/*
 * ScaleLableFormatRepositoryWithCounts
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ScaleLabelFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for ScaleLabelFormat objects with functions that return record counts.
 *
 * @author d116773
 * @since 2.0.8
 */
public interface ScaleLabelFormatRepositoryWithCounts extends JpaRepository<ScaleLabelFormat, Long>{

	/**
	 * Searches for ScaleLabelFormats by description. The function will use a LIKE in the search, but the
	 * regular expression must be set by the calling function.
	 *
	 * @param description The description to search for.
	 * @param pageable The page request.
	 * @return A pageable list of ScaleLabelFormats.
	 */
	Page<ScaleLabelFormat> findByDescriptionContains(@Param("description")String description, Pageable pageable);

	/**
	 * Find distinct by format code in page.
	 *
	 * @param formatCodes the format codes
	 * @param pageable    the pageable
	 * @return the page
	 */
	Page<ScaleLabelFormat> findDistinctByFormatCodeIn(@Param("formatCodes")List<Long> formatCodes, Pageable pageable);
}
