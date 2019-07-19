/*
 * ProductScanCodeNutrientRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductScanCodeNutrient;
import com.heb.pm.entity.ProductScanCodeNutrientKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about an product scan code nutrient.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface ProductScanCodeNutrientRepository extends JpaRepository<ProductScanCodeNutrient, ProductScanCodeNutrientKey> {

    List<ProductScanCodeNutrient> findByKeyScanCodeId(Long scanCodeId);
}
