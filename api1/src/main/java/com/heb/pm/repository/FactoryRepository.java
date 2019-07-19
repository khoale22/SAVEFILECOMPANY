/*
 * FactoryRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve information about factories.
 *
 * @author s573181
 * @since 2.6.0
 */
public interface FactoryRepository extends JpaRepository<Factory, Integer> {
}
