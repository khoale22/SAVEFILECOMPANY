/*
 * ScaleGraphicsCodeRepositoryWithCounts.java
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ScaleGraphicsCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for Graphics Code.
 *
 * @author vn40486
 * @since 2.1.0
 */
@Repository
public interface ScaleGraphicsCodeRepositoryWithCounts extends
		 JpaRepository<ScaleGraphicsCode, Long> {

	/**
	 * Find all Graphics Codes by Graphics Codes ids.
	 *
	 * @param scaleGraphicsCodes	Graphics Codes to be searched for.
	 * @param pageable	pagination pagination object.
	 * @return	list of Graphics Codes with pagination info.
	 */
	Page<ScaleGraphicsCode> findByScaleGraphicsCodeIn(@Param("scaleGraphicsCodes")List<Long> scaleGraphicsCodes, Pageable pageable);

	/**
	 * Find all Graphics Codes by description.
	 *
	 * @param scaleGraphicsCodeDescription	Graphics Code description to be searched for.
	 * @param pageable	pagination pagination object.
	 * @return	list of Graphics Codes with pagination info.
	 */
	Page<ScaleGraphicsCode> findByScaleGraphicsCodeDescriptionIgnoreCaseContaining(String scaleGraphicsCodeDescription ,
																				   Pageable pageable);
}
