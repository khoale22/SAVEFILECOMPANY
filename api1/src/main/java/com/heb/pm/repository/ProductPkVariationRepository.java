/*
 * ProductPkVariationRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductPkVariation;
import com.heb.pm.entity.ProductPkVariationKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for ProductPkVariation.
 *
 * @author vn73545
 * @since 2.0.6
 */
public interface ProductPkVariationRepository extends JpaRepository<ProductPkVariation, ProductPkVariationKey> {

	/**
	 * Returns the list of ProductPkVariations by upc and source system id.
	 * @param upc The scan code.
	 * @param sourceSystem The source system id.
	 * @return the list of ProductPkVariations.
	 */
	List<ProductPkVariation> findByKeyUpcAndKeySourceSystem(Long upc, Integer sourceSystem);

	/**
	 * Returns the list of ProductPkVariations by upc
	 * @param upc The scan code.
	 * @return the list of ProductPkVariations.
	 */
	List<ProductPkVariation> findByKeyUpc(Long upc);
}
