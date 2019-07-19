/*
 * ProductGroupTypeRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductGroupType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * product group type repository
 *
 * @author vn87351
 * @since 2.12.0
 */
public interface ProductGroupTypeRepository extends JpaRepository<ProductGroupType, String> {

}
