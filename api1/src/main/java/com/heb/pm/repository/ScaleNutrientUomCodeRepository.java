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

import com.heb.pm.entity.NutrientUom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for Nutrient Uom Code. Provides operation to perform CRUD operations over graphics code data.
 *
 * @author vn18422
 * @since 2.1.0
 */
@Repository
public interface ScaleNutrientUomCodeRepository extends
		 JpaRepository<NutrientUom, Long> {

	/**
	 * Used to get the record with max of Nutrient Uom Code. This value in in turn used to generate the next Grpahics
	 * code for the new records to be inserted.
	 *
	 * @return Graphics Code record with max of Graphics Code .
	 */
/*	NutrientUom findTopByOrderByNutrientUomCodeDesc(); */

	/**
	 * Find all Graphics Codes by Graphics Codes ids without pagination info.
	 *
	 * @param nutrientUomCode	Graphics Codes to be searched for.
	 * @param pageable	pagination pagination object.
	 * @return	list of Graphics Codes.
	 */
	List<NutrientUom> findByNutrientUomCodeIn(@Param("nutrientUomCodes") List<Long> nutrientUomCode, Pageable pageable);

	/**
	 * Find all Graphics Codes by description without page count.
	 *
	 * @param nutrientUomDescription	Graphics Code description to be searched for.
	 * @param pageable	pagination pagination object.
	 * @return	list of Graphics Codes.
	 */
	List<NutrientUom> findByNutrientUomDescriptionIgnoreCaseContaining(String nutrientUomDescription, Pageable pageable);

	/**
	 * Used to get the hits count. Returns list of match found.
	 * @param nutrientUomCodes	Graphics Codes to be searched for.
	 * @return	matching Graphics Codes.
	 */
	@Query(value = "select nu.nutrientUomCode from NutrientUom nu where nu.nutrientUomCode in :nutrientUomCodes")
	List<Long> findNutrientUomCode(@Param("nutrientUomCodes") List<Long> nutrientUomCodes);

	/**
	 * Returns the max ID for NutrientUom.
	 *
	 * @return The max ID for NutrientUom
	 */
	@Query("select max(n.nutrientUomCode) from NutrientUom n")
	Long findMaxNutrientUomCode();

	/**
	 * Finds the first 100 NutrientUom code greater than or equal to the NutrientUom code provided. Used to find
	 * the next missing NutrientUom code.
	 *
	 * @param nutrientUomCode The nutrientUomCode.
	 * @return The first 100 statement numbers greater than or equal to the NutrientUom code provided.
	 */
	List<NutrientUom> findFirst100ByNutrientUomCodeGreaterThanEqualOrderByNutrientUomCode(Long nutrientUomCode);

}
