/*
 * ScaleGraphicsCodeRepository.java
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ScaleGraphicsCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for Graphics Code. Provides operation to perform CRUD operations over graphics code data.
 *
 * @author vn40486
 * @since 2.1.0
 */
@Repository
public interface ScaleGraphicsCodeRepository extends
		 JpaRepository<ScaleGraphicsCode, Long> {

	/**
	 * Used to get the record with max of Graphics Code code. This value in in turn used to generate the next Grpahics
	 * code for the new records to be inserted.
	 *
	 * @return Graphics Code record with max of Graphics Code .
	 */
	ScaleGraphicsCode findTopByOrderByScaleGraphicsCodeDesc();

	/**
	 * Find all Graphics Codes by Graphics Codes ids without pagination info.
	 *
	 * @param scaleGraphicsCodes	Graphics Codes to be searched for.
	 * @param pageable	pagination pagination object.
	 * @return	list of Graphics Codes.
	 */
	List<ScaleGraphicsCode> findByScaleGraphicsCodeIn(@Param("scaleGraphicsCodes") List<Long> scaleGraphicsCodes, Pageable pageable);

	/**
	 * Find all Graphics Codes by description without page count.
	 *
	 * @param scaleGraphicsCodeDescription	Graphics Code description to be searched for.
	 * @param pageable	pagination pagination object.
	 * @return	list of Graphics Codes.
	 */
	List<ScaleGraphicsCode> findByScaleGraphicsCodeDescriptionIgnoreCaseContaining(String scaleGraphicsCodeDescription, Pageable pageable);

	/**
	 * Used to get the hits count. Returns list of match found.
	 * @param scaleGraphicsCodes	Graphics Codes to be searched for.
	 * @return	matching Graphics Codes.
	 */
	@Query(value = "select sf.scaleGraphicsCode from ScaleGraphicsCode sf where sf.scaleGraphicsCode in :scaleGraphicsCodes")
	List<Long> findScaleGraphicsCodes(@Param("scaleGraphicsCodes")List<Long> scaleGraphicsCodes);

}
