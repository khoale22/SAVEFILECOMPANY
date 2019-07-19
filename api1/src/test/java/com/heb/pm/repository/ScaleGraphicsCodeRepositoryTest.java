/*
 * ScaleGraphicsCodeRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ScaleGraphicsCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Test class for JPA repository - ScaleGraphicsCodeRepository.
 *
 * @author vn40486
 * @since 2.1.0
 */
public interface ScaleGraphicsCodeRepositoryTest extends JpaRepository<ScaleGraphicsCode, Long> {
}
