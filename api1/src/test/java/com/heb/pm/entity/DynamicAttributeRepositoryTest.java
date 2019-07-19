/*
 * DynamicAttributeRepositoryTest
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
 * Repository to test DynamicAttribute JPA functions.
 *
 * @author d116773
 * @since 2.0.7
 */
public interface DynamicAttributeRepositoryTest extends JpaRepository<DynamicAttribute, DynamicAttributeKey> {
}
