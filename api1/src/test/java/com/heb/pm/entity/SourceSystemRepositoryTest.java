/*
 * SourceSystemRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to test JPA mapping of source system.
 *
 * @author d116773
 * @since 2.0.7
 */
public interface SourceSystemRepositoryTest extends JpaRepository<SourceSystem, Integer> {
}
