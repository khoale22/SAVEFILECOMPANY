/*
 * ProductCategoryRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA Repository for ProductCategory.
 *
 * @author vn70529
 * @since 2.12.0
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>{
	/**
	 * Get a list of product categories from database.
	 *
	 * @return a list of product categories.
	 */
	List<ProductCategory> findAllByOrderByProductCategoryIdAsc();
}
