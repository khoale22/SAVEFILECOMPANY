/*
 * ScaleNutrientUomCodeRepositoryWithCounts.java
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientUom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for Nutrient Uom Code.
 *
 * @author vn18422
 * @since 2.1.0
 */
@Repository
public interface ScaleNutrientUomCodeRepositoryWithCounts extends
		 JpaRepository<NutrientUom, Long> {

	/**
	 * Find all Nutrient UOM Codes by Nutrient Uom  Codes ids.
	 *
	 * @param nutrientUomCode	Nutrient Uom Codes to be searched for.
	 * @param pageable	pagination pagination object.
	 * @return	list of Graphics Codes with pagination info.
	 */
	Page<NutrientUom> findByNutrientUomCodeIn(List<Long> nutrientUomCode, Pageable pageable);

	/**
	 * Find all Nutrient UOM Codes by description.
	 *
	 * @param nutrientUomDescription	Nutrient Uom  Code description to be searched for.
	 * @param pageable	pagination pagination object.
	 * @return	list of Nutrient Uom  Codes with pagination info.
	 */
	Page<NutrientUom> findByNutrientUomDescriptionIgnoreCaseContaining(String nutrientUomDescription,
                                                                                   Pageable pageable);
}
