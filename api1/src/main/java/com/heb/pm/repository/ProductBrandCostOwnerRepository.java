/*
 * ProductBrandCostOwnerRepository
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of the Product Brand Cost Owner entity.
 *
 * @author vn70529
 * @since 2.12.0
 */
public interface ProductBrandCostOwnerRepository extends JpaRepository<ProductBrandCostOwner,ProductBrandCostOwnerKey>{
	/**
	 * Find all ProductBrandCostOwners
	 *
	 * @param pageRequest          the page request for pagination.
	 * @return the page of ProductBrandCostOwner.
	 */
	List<ProductBrandCostOwner> findAllBy(Pageable pageRequest);

	/**
	 * Find ProductBrandCostOwners by productBrandId
	 *
	 * @param productBrandId the productBrandId to search.
	 * @return ProductBrandCostOwner.
	 */
	ProductBrandCostOwner findFirstByKeyProductBrandId(Long productBrandId);
}