/*
 *
 * NLEA16NutrientRepository.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NLEA16Nutrient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for NLEA16Nutrient.
 *
 * @author vn70529
 * @since 2.21.0
 */
public interface NLEA16NutrientRepository extends JpaRepository<NLEA16Nutrient, Long> {

}
