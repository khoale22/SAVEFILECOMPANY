/*
 *  DescriptionTypeRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.DescriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for DescriptionType.
 *
 * @author l730832
 * @since 2.8.0
 */
public interface DescriptionTypeRepository extends JpaRepository<DescriptionType, String> {
}
