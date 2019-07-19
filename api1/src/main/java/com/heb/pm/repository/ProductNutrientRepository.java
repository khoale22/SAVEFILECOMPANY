/*
 * ProductNutrientRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductNutrient;
import com.heb.pm.entity.ProductNutrientKey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for ProductNutrient.
 *
 * @author vn73545
 * @since 2.0.6
 */
public interface ProductNutrientRepository extends JpaRepository<ProductNutrient, ProductNutrientKey> {
	List<ProductNutrient> findByKeyUpcAndKeySourceSystemOrderByKeyValPreprdTypCdDescKeyMasterIdAsc(Long upc, Integer sourceSystem);
}
