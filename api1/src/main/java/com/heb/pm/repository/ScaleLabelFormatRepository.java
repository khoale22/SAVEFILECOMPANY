/*
 * ScaleLabelFormatRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ScaleLabelFormat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for ScaleLabelFormat objects.
 *
 * @author d116773
 * @since 2.0.8
 */
public interface ScaleLabelFormatRepository extends JpaRepository<ScaleLabelFormat, Long> {

	/**
	 * Retuns a pageable list of all ScaleLabelFormats.
	 *
	 * @param pageable The page request.
	 * @return A pageable list of ScaleLabelFormats.
	 */
	@Query(value = "select slf from ScaleLabelFormat slf")
	List<ScaleLabelFormat> findAllByPage(Pageable pageable);

	/**
	 * Searches for ScaleLabelFormats by description. The function will use a LIKE in the search, but the
	 * regular expression must be set by the calling function.
	 *
	 * @param description The description to search for.
	 * @param pageable The page request.
	 * @return A pageable list of ScaleLabelFormats.
	 */
	List<ScaleLabelFormat> findByDescriptionContains(@Param("description")String description, Pageable pageable);

	/**
	 * Searches for ScaleLabelFormats by format code.
	 *
	 * @param formatCodes List of format codes.
	 * @param pageable The page request.
	 * @return A pageable list of ScaleLabelFormats.
	 */
	List<ScaleLabelFormat> findDistinctByFormatCodeIn(@Param("formatCodes")List<Long> formatCodes, Pageable pageable);

	/**
	 * Find first by order by format code desc scale label format.
	 *
	 * @return the scale label format
	 */
	ScaleLabelFormat findFirstByOrderByFormatCodeDesc();
}
