/*
 * UnitOfMeasureRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Unit Of Measure.
 *
 * @author vn70529
 * @since 2.21.0
 */
public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long> {
}
