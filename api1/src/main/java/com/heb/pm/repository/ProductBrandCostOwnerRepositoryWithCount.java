/*
 * ProductBrandCostOwnerRepositoryWithCount
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductBrandCostOwner;
import com.heb.pm.entity.ProductBrandCostOwnerKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of the Product Brand Cost Owner entity with count.
 *
 * @author vn70529
 * @since 2.12.0
 */
public interface ProductBrandCostOwnerRepositoryWithCount extends JpaRepository<ProductBrandCostOwner,ProductBrandCostOwnerKey>{
	/**
	 * Find all ProductBrandCostOwners
	 *
	 * @param pageRequest          the page request for pagination.
	 * @return the page of ProductBrandCostOwner.
	 */
	Page<ProductBrandCostOwner> findAll(Pageable pageRequest);
}