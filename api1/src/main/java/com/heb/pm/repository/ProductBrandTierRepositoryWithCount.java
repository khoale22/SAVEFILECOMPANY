/*
 * ProductBrandTierRepository
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductBrandTier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository of the ProductBrandTier entity.
 *
 * @author vn87351
 * @since 2.41.0
 */
public interface ProductBrandTierRepositoryWithCount extends JpaRepository<ProductBrandTier, Integer>, ProductBrandTierRepositoryCommon {

    /**
     * Find all tier code data filter by prod brand name.
     *
     * @param productBrandName the description to search.
     * @param pageRequest the page request for pagination.
     * @return the page of product sub brands.
     */
    Page<ProductBrandTier> findByProductBrandNameIgnoreCaseContaining(String productBrandName, Pageable pageRequest);

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param id          The product brand tier code to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product sub brands.
     */
    @Query(value = FIND_BY_TIER_CODE_ID_SQL)
    Page<ProductBrandTier> findById(@Param("id") String id, Pageable pageRequest);

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param id          The product brand tier code to search.
     * @param name        The prod sub brand name to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product sub brands.
     */
    @Query(value = FIND_BY_TIER_CODE_ID_AND_TIER_CODE_NAME_SQL)
    Page<ProductBrandTier> findByIdAndName(@Param("id") String id, @Param("name") String name, Pageable pageRequest);
}
